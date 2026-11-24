package com.calculator;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CalculatorApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
            CalculatorApp.class.getResource("/com/calculator/calculator.fxml")
        );
        Scene scene = new Scene(loader.load());
        stage.setTitle("JavaFX Calculator");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
