package com.example.v10;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

//Gui to display article description and the interaction gui components for the user
public class ArticleApp extends Application {

    private static Stage primaryStage; // Save reference to the primary stage

    @Override
    public void start(Stage stage) {
        primaryStage = stage; // Save the primary stage reference
        primaryStage.setTitle("Articles");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
