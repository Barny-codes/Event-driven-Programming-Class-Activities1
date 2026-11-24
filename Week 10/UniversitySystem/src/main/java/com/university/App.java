package com.university;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App extends Application {
    private ScheduledExecutorService scheduler;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("MainView.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();

        primaryStage.setTitle("University Management System");
        primaryStage.setScene(new Scene(root, 800, 600));
        // Minimum size keeps the navigation and scrollable content usable at smaller desktop windows.
        primaryStage.setMinWidth(520);
        primaryStage.setMinHeight(400);
        primaryStage.setOnCloseRequest(event -> stopScheduler());
        primaryStage.show();

        scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "dashboard-heartbeat");
            thread.setDaemon(true);
            return thread;
        });
        scheduler.scheduleAtFixedRate(() -> {
            Platform.runLater(controller::refreshHeartbeat);
        }, 0, 5, TimeUnit.SECONDS);
    }

    private void stopScheduler() {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }

    @Override
    public void stop() {
        stopScheduler();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
