package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class MainApp extends Application {

    static DBConnector dbConnector = new DBConnector();

    static UserDA userDA = new UserDA(dbConnector);
    static UserService  userService = new UserService(userDA);

    private static boolean isLoggedIn = false;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        TMenu menu = new TMenu(userService);

        // Start the login prompt
        isLoggedIn = menu.startLogin();

        // If login is successful, ask for reading articles
        if (isLoggedIn) {
            System.out.println("Press 'r' to read articles, or 'q' to quit.");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("r")) {
                launch(args);  // Launch the JavaFX window to show articles
            } else if (input.equalsIgnoreCase("q")) {
                System.out.println("Goodbye!");
                System.exit(0);
            } else {
                System.out.println("Invalid input.");
            }
        } else {
            System.out.println("Login failed. Exiting.");
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        if (isLoggedIn) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Articles1.fxml"));



            Scene scene = new Scene(fxmlLoader.load(), 400, 300);
            primaryStage.setTitle("Articles");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }
}



