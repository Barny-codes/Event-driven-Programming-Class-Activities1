package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            System.out.println("Error: Fields cannot be empty.");
            return;
        }

        if ("admin".equals(username) && "password123".equals(password)) {
            System.out.println("Login Successful!");
            usernameField.clear();
            passwordField.clear();
        } else {
            System.out.println("Login Failed!");
        }
    }
}
