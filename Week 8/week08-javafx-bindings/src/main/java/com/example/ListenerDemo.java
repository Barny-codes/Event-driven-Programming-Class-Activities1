package com.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class ListenerDemo extends Application {
    @Override
    public void start(Stage primaryStage) {
        Person person = new Person();
        // InvalidationListener lambda: event source is person.nameProperty(); only a change notification is needed.
        person.nameProperty().addListener(observable ->
            System.out.println("1. [InvalidationListener] Name was changed!"));
        // ChangeListener lambda: the property change event provides the old and new values for auditing/output.
        person.nameProperty().addListener((observable, oldValue, newValue) ->
            System.out.println("2. [ChangeListener] Name changed from '" +
                oldValue + "' to '" + newValue + "'"));

        System.out.println("--- Setting name to Alice ---");
        person.setName("Alice");
        System.out.println("--- Setting name to Bob ---");
        person.setName("Bob");
        System.out.println("--- Setting name to Bob (same value) ---");
        person.setName("Bob");
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
