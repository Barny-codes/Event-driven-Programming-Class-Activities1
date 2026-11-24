package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DashboardApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #2c3e50;");

        Label title = new Label("My SaaS Dashboard");
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font-family: Arial; -fx-font-size: 20px; -fx-font-weight: bold;");
        topBar.getChildren().add(title);

        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(15));
        sidebar.setPrefWidth(200);
        sidebar.setStyle("-fx-background-color: #ecf0f1;");

        Button overviewBtn = new Button("📊 Overview");
        Button settingsBtn = new Button("⚙️ Settings");
        Button logoutBtn = new Button("🚪 Logout");
        sidebar.getChildren().addAll(overviewBtn, settingsBtn, logoutBtn);

        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(30));

        Label welcomeLabel = new Label("Welcome back, User!");
        welcomeLabel.setStyle("-fx-font-family: Arial; -fx-font-size: 24px;");

        HBox statsCards = new HBox(20);
        statsCards.getChildren().addAll(
                createCard("Revenue", "$12,450", "#e74c3c"),
                createCard("Users", "1,204", "#3498db"),
                createCard("Orders", "342", "#2ecc71")
        );

        centerContent.getChildren().addAll(welcomeLabel, statsCards);

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setLeft(sidebar);
        root.setCenter(centerContent);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20));
        card.setPrefWidth(180);
        card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10; -fx-border-radius: 10;");

        Label titleLabel = new Label(title);
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle("-fx-font-family: Arial; -fx-font-size: 14px;");

        Label valueLabel = new Label(value);
        valueLabel.setTextFill(Color.WHITE);
        valueLabel.setStyle("-fx-font-family: Arial; -fx-font-size: 28px; -fx-font-weight: bold;");

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
