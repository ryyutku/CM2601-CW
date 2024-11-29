package com.example.v9;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ArticleApp extends Application {

    private static Stage primaryStage; // Save reference to the primary stage

    @Override
    public void start(Stage stage) {
        primaryStage = stage; // Save the primary stage reference
        primaryStage.setTitle("Articles");
    }

    // Method to display articles when called
    public static void displayArticles() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ArticleApp.class.getResource("Articles1.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 460.0, 244.0);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
