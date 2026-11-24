package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Week7Launcher extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label title = new Label("Week 7 Event Handling Lab");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        Label subtitle = new Label("Choose a demonstration:");

        GridPane choices = new GridPane();
        choices.setHgap(12);
        choices.setVgap(12);
        choices.setAlignment(Pos.CENTER);

        choices.add(createDemoButton("Mouse and Keyboard",
            stage -> new MouseKeyDemo().start(stage)), 0, 0);
        choices.add(createDemoButton("Drag and Drop",
            stage -> new DragDropDemo().start(stage)), 1, 0);
        choices.add(createDemoButton("Custom Events",
            stage -> new CustomEventDemo().start(stage)), 0, 1);
        choices.add(createDemoButton("Event Propagation",
            stage -> new EventPropagationDemo().start(stage)), 1, 1);

        Button exitButton = new Button("Exit Application");
        exitButton.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white;");
        exitButton.setOnAction(event -> primaryStage.close());

        VBox root = new VBox(18, title, subtitle, choices, exitButton);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f8fafc;");

        primaryStage.setTitle("Week 7 - JavaFX Events");
        primaryStage.setScene(new Scene(root, 520, 380));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Button createDemoButton(String text, DemoLauncher launcher) {
        Button button = new Button(text);
        button.setPrefSize(220, 70);
        button.setStyle("-fx-font-size: 15px; -fx-background-color: #2563eb; -fx-text-fill: white;");
        button.setOnAction(event -> {
            Stage demoStage = new Stage();
            demoStage.initOwner(button.getScene().getWindow());
            launcher.start(demoStage);
        });
        return button;
    }

    private interface DemoLauncher {
        void start(Stage stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
