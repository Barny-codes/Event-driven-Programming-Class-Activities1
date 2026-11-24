package com.example;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ReactiveFormApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(15);

        TextField nameField = new TextField();
        TextField emailField = new TextField();
        TextArea bioField = new TextArea();
        bioField.setPrefRowCount(3);
        bioField.setWrapText(true);
        Label charCountLabel = new Label();
        Label previewLabel = new Label("Preview will appear here");
        previewLabel.setTextFill(Color.BLUE);
        Button submitButton = new Button("Submit Registration");

        grid.addRow(0, new Label("Name:"), nameField);
        grid.addRow(1, new Label("Email:"), emailField);
        grid.addRow(2, new Label("Bio:"), bioField);
        grid.add(charCountLabel, 1, 3);
        grid.add(previewLabel, 1, 4);
        grid.add(submitButton, 1, 5);

        charCountLabel.textProperty().bind(
            bioField.textProperty().length().asString().concat(" / 150 characters"));
        // Bindings connect UI properties directly, so preview text changes for every text event.
        previewLabel.textProperty().bind(
            Bindings.concat("Hi, I am ", nameField.textProperty(), ". ", bioField.textProperty()));
        // Lambda suppliers calculate validation state and the binding rechecks it when the named properties change.
        submitButton.disableProperty().bind(
            nameField.textProperty().isEmpty()
                .or(emailField.textProperty().isEmpty())
                .or(Bindings.createBooleanBinding(
                    () -> !emailField.getText().trim().matches(
                        "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"),
                    emailField.textProperty()))
                .or(bioField.textProperty().length().greaterThan(150)));

        // Submit button ActionEvent source -> this lambda handles success feedback.
        submitButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Registration Successful");
            alert.setContentText("Welcome, " + nameField.getText() + "!");
            alert.showAndWait();
        });

        primaryStage.setTitle("Reactive Registration Form");
        primaryStage.setScene(new Scene(grid, 500, 450));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
