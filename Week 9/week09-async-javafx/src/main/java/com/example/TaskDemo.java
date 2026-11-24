package com.example;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TaskDemo extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label statusLabel = new Label("Ready");
        Button startButton = new Button("Start Background Task");

        startButton.setOnAction(event -> {
            statusLabel.setText("Processing...");
            startButton.setDisable(true);
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws InterruptedException {
                    for (int i = 1; i <= 10; i++) {
                        if (isCancelled()) {
                            return null;
                        }
                        updateProgress(i, 10);
                        updateMessage("Processing step " + i + " of 10");
                        Thread.sleep(500);
                    }
                    return null;
                }
            };
            task.setOnCancelled(cancelled -> {
                statusLabel.setText("Task Cancelled");
                startButton.setDisable(false);
            });
            task.setOnSucceeded(success -> {
                statusLabel.setText("Task Complete!");
                startButton.setDisable(false);
            });
            task.setOnFailed(failure -> {
                statusLabel.setText("Task Failed");
                startButton.setDisable(false);
            });
            new Thread(task, "week09-task-demo").start();
        });

        VBox root = new VBox(20, startButton, statusLabel);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");
        primaryStage.setTitle("Task Demo - The Fix");
        primaryStage.setScene(new Scene(root, 400, 200));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
