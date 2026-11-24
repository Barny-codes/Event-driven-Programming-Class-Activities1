package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomEventDemo extends Application {

    @Override
    public void start(Stage primaryStage) {
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setPrefWidth(220);

        Button loginBtn = new Button("Login");
        loginBtn.setDefaultButton(true);
        loginBtn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white;");
        Label statusLabel = new Label("Please log in.");
        statusLabel.setWrapText(true);
        statusLabel.setStyle("-fx-text-fill: #475569;");

        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white;");
        exitButton.setOnAction(event -> primaryStage.close());
        javafx.scene.layout.HBox controls =
            new javafx.scene.layout.HBox(10, loginBtn, exitButton);
        controls.setAlignment(Pos.CENTER);

        VBox root = new VBox(15, new Label("Welcome"), usernameField, controls, statusLabel);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);

        loginBtn.setOnAction(e -> {
            String username = usernameField.getText().trim();
            if (!username.isEmpty()) {
                root.fireEvent(new LoginSuccessEvent(username));
            } else {
                statusLabel.setText("Please enter a username.");
                usernameField.requestFocus();
            }
        });

        root.addEventHandler(LoginSuccessEvent.LOGIN_SUCCESS, event -> {
            System.out.println("Custom Event Received! User logged in: " + event.getUsername());
            statusLabel.setText("Welcome back, " + event.getUsername() + "!");
            usernameField.clear();
            usernameField.requestFocus();
        });

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Custom Event Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
