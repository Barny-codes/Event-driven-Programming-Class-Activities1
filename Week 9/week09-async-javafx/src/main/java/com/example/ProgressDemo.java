package com.example;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicReference;

public class ProgressDemo extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label statusLabel = new Label("Ready");
        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(300);
        Button startButton = new Button("Download File");
        Button cancelButton = new Button("Cancel");
        cancelButton.setDisable(true);
        AtomicReference<Task<Void>> activeTask = new AtomicReference<>();

        // Start button ActionEvent -> create a Task and bind its progress/message properties to UI controls.
        startButton.setOnAction(event -> {
            startButton.setDisable(true);
            progressBar.progressProperty().unbind();
            progressBar.setProgress(0);
            statusLabel.textProperty().unbind();
            cancelButton.setDisable(false);

            Task<Void> downloadTask = new Task<>() {
                @Override
                protected Void call() throws InterruptedException {
                    for (int i = 0; i <= 100; i++) {
                        if (isCancelled()) {
                            break;
                        }
                        Thread.sleep(50);
                        updateProgress(i, 100);
                        updateMessage("Downloading... " + i + "%");
                    }
                    if (isCancelled()) {
                        return null;
                    }
                    return null;
                }
            };
            activeTask.set(downloadTask);
            progressBar.progressProperty().bind(downloadTask.progressProperty());
            statusLabel.textProperty().bind(downloadTask.messageProperty());
            downloadTask.setOnSucceeded(success -> {
                statusLabel.textProperty().unbind();
                statusLabel.setText(downloadTask.isCancelled()
                    ? "Download Cancelled"
                    : "Download Complete!");
                startButton.setDisable(false);
                cancelButton.setDisable(true);
            });
            downloadTask.setOnCancelled(cancelled -> {
                statusLabel.textProperty().unbind();
                statusLabel.setText("Download Cancelled");
                startButton.setDisable(false);
                cancelButton.setDisable(true);
            });
            downloadTask.setOnFailed(failed -> {
                statusLabel.textProperty().unbind();
                statusLabel.setText("Download Failed");
                startButton.setDisable(false);
                cancelButton.setDisable(true);
            });
            Thread worker = new Thread(downloadTask, "week09-progress-task");
            worker.setDaemon(true);
            worker.start();
        });
        cancelButton.setOnAction(event -> {
            Task<Void> task = activeTask.get();
            if (task != null) {
                task.cancel();
            }
            cancelButton.setDisable(true);
        });

        VBox root = new VBox(20, new javafx.scene.layout.HBox(10, startButton, cancelButton),
            progressBar, statusLabel);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-alignment: center;");
        primaryStage.setTitle("Progress Bar Demo");
        primaryStage.setScene(new Scene(root, 400, 200));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
