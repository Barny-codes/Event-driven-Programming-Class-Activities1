package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LayoutDemo extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label headerTitle = new Label("App Header");
        headerTitle.setTextFill(Color.WHITE);
        headerTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER);
        topBar.setStyle("-fx-background-color: #333;");
        topBar.getChildren().add(headerTitle);

        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(10));
        sidebar.setPrefWidth(150);
        sidebar.setStyle("-fx-background-color: #ddd;");

        Button homeBtn = new Button("Home");
        Button profileBtn = new Button("Profile");
        Button settingsBtn = new Button("Settings");
        sidebar.getChildren().addAll(homeBtn, profileBtn, settingsBtn);

        GridPane centerGrid = new GridPane();
        centerGrid.setPadding(new Insets(20));
        centerGrid.setHgap(10);
        centerGrid.setVgap(10);
        centerGrid.setAlignment(Pos.CENTER);

        int index = 1;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button button = new Button("Btn " + index++);
                button.setPrefSize(80, 40);
                centerGrid.add(button, col, row);
            }
        }

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setLeft(sidebar);
        root.setCenter(centerGrid);

        Scene scene = new Scene(root, 700, 500);
        primaryStage.setTitle("JavaFX Layouts Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
