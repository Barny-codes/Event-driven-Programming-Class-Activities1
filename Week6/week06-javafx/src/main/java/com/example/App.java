package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("My First JavaFX App");
        Button button = new Button("Click Me!");

        button.setOnAction(event -> {
            label.setText("Button was clicked!");
            System.out.println("Button clicked on: " + Thread.currentThread().getName());
        });

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f0f0f0;");
        root.getChildren().addAll(label, button);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("JavaFX Programmatic App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
