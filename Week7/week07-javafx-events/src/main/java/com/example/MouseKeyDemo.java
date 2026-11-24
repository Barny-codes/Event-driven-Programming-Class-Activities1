package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MouseKeyDemo extends Application {

    private double x = 250;
    private double y = 200;
    private final double STEP = 15;

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #edf2f7;");
        root.setPrefSize(500, 400);

        Circle player = new Circle(x, y, 20, Color.BLUE);
        player.setStroke(Color.DARKBLUE);
        player.setStrokeWidth(2);
        Text mouseCoords = new Text(10, 20, "Mouse: (0, 0)");
        mouseCoords.setFill(Color.valueOf("#334155"));
        Label instruction = new Label("Use the arrow keys to move. ESC resets.");
        instruction.setStyle("-fx-text-fill: #475569;");

        root.getChildren().addAll(player, mouseCoords);

        root.setOnMouseMoved(e -> {
            mouseCoords.setText(String.format("Mouse: (%.0f, %.0f)", e.getX(), e.getY()));
        });

        root.setFocusTraversable(true);
        root.setOnKeyPressed(e -> {
            KeyCode key = e.getCode();
            if (key == KeyCode.UP) {
                y -= STEP;
            }
            if (key == KeyCode.DOWN) {
                y += STEP;
            }
            if (key == KeyCode.LEFT) {
                x -= STEP;
            }
            if (key == KeyCode.RIGHT) {
                x += STEP;
            }
            if (key == KeyCode.ESCAPE) {
                x = 250;
                y = 200;
            }

            x = Math.max(20, Math.min(x, 480));
            y = Math.max(20, Math.min(y, 380));

            player.setCenterX(x);
            player.setCenterY(y);
        });

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(event -> {
            x = 250;
            y = 200;
            player.setCenterX(x);
            player.setCenterY(y);
            root.requestFocus();
        });
        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white;");
        exitButton.setOnAction(event -> primaryStage.close());

        HBox controls = new HBox(10, instruction, resetButton, exitButton);
        controls.setAlignment(Pos.CENTER_RIGHT);
        controls.setStyle("-fx-padding: 10;");
        BorderPane layout = new BorderPane(root, null, null, controls, null);
        layout.setStyle("-fx-background-color: #edf2f7;");

        Scene scene = new Scene(layout, 500, 440);
        primaryStage.setTitle("Mouse and Key Demo");
        primaryStage.setScene(scene);
        primaryStage.setOnShown(e -> root.requestFocus());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
