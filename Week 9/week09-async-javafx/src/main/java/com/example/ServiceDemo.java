package com.example;

import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ServiceDemo extends Application {
    private static class DataSyncService extends Service<String> {
        @Override
        protected Task<String> createTask() {
            return new Task<>() {
                @Override
                protected String call() throws InterruptedException {
                    for (int i = 0; i <= 10; i++) {
                        if (isCancelled()) {
                            break;
                        }
                        updateProgress(i, 10);
                        updateMessage("Syncing " + i * 10 + "%");
                        Thread.sleep(500);
                    }
                    if (isCancelled()) {
                        return null;
                    }
                    return "Data Synced Successfully!";
                }
            };
        }
    }

    @Override
    public void start(Stage primaryStage) {
        Label statusLabel = new Label("Ready");
        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setVisible(false);
        Button syncButton = new Button("Sync Data");
        Button cancelButton = new Button("Cancel");
        cancelButton.setDisable(true);
        DataSyncService service = new DataSyncService();

        // Sync button ActionEvent -> service.restart() starts or replaces the reusable background Task.
        syncButton.setOnAction(event -> {
            service.restart();
            statusLabel.setText("Syncing...");
            spinner.setVisible(true);
            syncButton.setDisable(true);
            cancelButton.setDisable(false);
        });
        // Cancel button ActionEvent -> request cancellation from the Service and its Task.
        cancelButton.setOnAction(event -> service.cancel());

        // Service event callbacks run on the JavaFX thread; lambdas update controls without touching state from worker threads.
        service.setOnCancelled(event -> {
            statusLabel.setText("Sync Cancelled.");
            spinner.setVisible(false);
            syncButton.setDisable(false);
            cancelButton.setDisable(true);
        });
        service.setOnSucceeded(event -> {
            statusLabel.setText(service.getValue());
            spinner.setVisible(false);
            syncButton.setDisable(false);
            cancelButton.setDisable(true);
        });
        service.setOnFailed(event -> {
            Throwable error = service.getException();
            statusLabel.setText("Sync Failed: " +
                (error == null ? "Unknown error" : error.getMessage()));
            spinner.setVisible(false);
            syncButton.setDisable(false);
            cancelButton.setDisable(true);
        });

        HBox buttons = new HBox(10, syncButton, cancelButton);
        VBox root = new VBox(20, buttons, spinner, statusLabel);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-alignment: center;");
        primaryStage.setTitle("Service Demo");
        primaryStage.setScene(new Scene(root, 400, 250));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
