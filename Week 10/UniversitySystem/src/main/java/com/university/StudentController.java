package com.university;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class StudentController {
    @FXML private TextField regField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> colReg;
    @FXML private TableColumn<Student, String> colFirst;
    @FXML private TableColumn<Student, String> colLast;
    @FXML private TableColumn<Student, String> colEmail;
    @FXML private Label statusLabel;
    @FXML private Button saveButton;

    private final ObservableList<Student> studentList =
        FXCollections.observableArrayList();
    private final BooleanProperty saving = new SimpleBooleanProperty(false);
    private final BooleanProperty loading = new SimpleBooleanProperty(false);

    @FXML
    public void initialize() {
        // Lambda: read a Student row and expose its JavaFX property for TableView cell rendering.
        colReg.setCellValueFactory(data -> data.getValue().regNumberProperty());
        colFirst.setCellValueFactory(data -> data.getValue().firstNameProperty());
        colLast.setCellValueFactory(data -> data.getValue().lastNameProperty());
        colEmail.setCellValueFactory(data -> data.getValue().emailProperty());
        studentTable.setItems(studentList);

        // Lambda: reactively recalculate the Save button state when any input property changes.
        saveButton.disableProperty().bind(Bindings.createBooleanBinding(
            () -> regField.getText().trim().isEmpty()
                || firstNameField.getText().trim().isEmpty()
                || lastNameField.getText().trim().isEmpty()
                || emailField.getText().trim().isEmpty()
                || saving.get()
                || loading.get(),
            regField.textProperty(),
            firstNameField.textProperty(),
            lastNameField.textProperty(),
            emailField.textProperty(),
            saving,
            loading));
        loadStudents();
    }

    @FXML
    private void handleSaveStudent() {
        // ActionEvent source: Save Student button; handler validates and starts the insert Task.
        String registration = regField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();

        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            statusLabel.setText("Enter a valid email such as student@university.edu.");
            return;
        }

        Task<Void> saveTask = new Task<>() {
            // Runs on a background thread; SQL and network I/O stay off the JavaFX Application Thread.
            @Override
            protected Void call() throws Exception {
                String sql = "INSERT INTO students " +
                    "(registration_number, first_name, last_name, email) VALUES (?, ?, ?, ?)";
                try (Connection connection = DatabaseUtil.getConnection();
                     PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, registration);
                    statement.setString(2, firstName);
                    statement.setString(3, lastName);
                    statement.setString(4, email);
                    statement.executeUpdate();
                }
                return null;
            }
        };
        saving.set(true);
        // Lambda: saveTask success event is delivered on the JavaFX thread; update controls safely here.
        saveTask.setOnSucceeded(event -> {
            saving.set(false);
            statusLabel.setText("Student saved successfully!");
            regField.clear();
            firstNameField.clear();
            lastNameField.clear();
            emailField.clear();
            loadStudents();
        });
        // Lambda: failed event displays SQL or connection errors without crashing the UI.
        saveTask.setOnFailed(event -> {
            saving.set(false);
            Throwable error = saveTask.getException();
            statusLabel.setText("Save failed: " +
                (error == null ? "Unknown database error" : error.getMessage()));
        });
        Thread worker = new Thread(saveTask, "save-student-task");
        worker.setDaemon(true);
        worker.start();
    }

    public void loadStudents() {
        if (loading.get()) {
            return;
        }
        // Creates a one-shot background query task for loading the TableView.
        LoadStudentsTask task = new LoadStudentsTask();
        loading.set(true);
        // Lambda: success handler runs on the UI thread and replaces the observable table list.
        task.setOnSucceeded(event -> {
            loading.set(false);
            studentList.setAll(task.getValue());
            statusLabel.setText("Loaded " + studentList.size() +
                " students from database.");
        });
        task.setOnFailed(event -> {
            loading.set(false);
            Throwable error = task.getException();
            statusLabel.setText("Database unavailable: " +
                (error == null ? "Unknown error" : error.getMessage()));
        });
        Thread worker = new Thread(task, "load-students-task");
        worker.setDaemon(true);
        worker.start();
    }
}
