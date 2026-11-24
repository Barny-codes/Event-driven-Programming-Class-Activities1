package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class DragDropDemo extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label itemToDrag = new Label("Drag Me ");
        itemToDrag.setStyle("-fx-background-color: #f39c12; -fx-padding: 10; -fx-font-size: 16px; -fx-text-fill: white;");

        Rectangle dropZone = new Rectangle(200, 100);
        dropZone.setFill(Color.LIGHTGRAY);
        dropZone.setStroke(Color.GRAY);
        dropZone.setStrokeWidth(2);

        Label dropLabel = new Label("Drop Here!");
        VBox dropContainer = new VBox(10, dropZone, dropLabel);
        dropContainer.setAlignment(Pos.CENTER);

        itemToDrag.setOnDragDetected(event -> {
            Dragboard db = itemToDrag.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(itemToDrag.getText());
            db.setContent(content);
            event.consume();
        });

        dropZone.setOnDragOver(event -> {
            if (event.getGestureSource() != dropZone && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        dropZone.setOnDragEntered(event -> {
            if (event.getGestureSource() != dropZone && event.getDragboard().hasString()) {
                dropZone.setFill(Color.LIGHTGREEN);
            }
        });

        dropZone.setOnDragExited(event -> {
            dropZone.setFill(Color.LIGHTGRAY);
        });

        dropZone.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                dropLabel.setText("Dropped: " + db.getString());
                itemToDrag.setDisable(true);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

        VBox root = new VBox(30, itemToDrag, dropContainer);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        Button resetButton = new Button("Reset");
        resetButton.setOnAction(event -> {
            itemToDrag.setDisable(false);
            dropLabel.setText("Drop Here!");
            dropZone.setFill(Color.LIGHTGRAY);
        });
        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white;");
        exitButton.setOnAction(event -> primaryStage.close());
        javafx.scene.layout.HBox controls =
            new javafx.scene.layout.HBox(10, resetButton, exitButton);
        controls.setAlignment(Pos.CENTER);
        root.getChildren().add(controls);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Drag and Drop Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
