package com.university;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MainController {
    @FXML private StackPane centerPane;
    private final Label dashboardStatus = new Label(
        "Welcome to the University Dashboard!");

    @FXML
    public void initialize() {
        showDashboard();
    }

    @FXML
    private void showDashboard() {
        centerPane.getChildren().setAll(dashboardStatus);
    }

    @FXML
    private void showStudents() {
        try {
            Parent studentView = FXMLLoader.load(
                getClass().getResource("StudentView.fxml"));
            centerPane.getChildren().setAll(studentView);
        } catch (IOException exception) {
            centerPane.getChildren().setAll(
                new Label("Could not load Student Management: " +
                    exception.getMessage()));
        }
    }

    public void refreshHeartbeat() {
        dashboardStatus.setText("Dashboard heartbeat: " +
            LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
}
