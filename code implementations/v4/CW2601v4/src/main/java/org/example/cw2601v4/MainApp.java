package org.example.cw2601v4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

//public class MainApp extends Application {
//
//    private static boolean showWindow = false;
//
//    public static void main(String[] args) {
//        // Create a scanner for terminal input
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Type 'y' to display the window: ");
//
//        // Wait for the user to type 'y'
//        String input = scanner.nextLine();
//        if (input.equalsIgnoreCase("y")) {
//            showWindow = true;
//        }
//
//        // Start the JavaFX application
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage primaryStage) {
//        if (showWindow) {
//            try {
//                // Load the FXML file
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
//                Scene scene = new Scene(fxmlLoader.load(), 400, 300);
//
//                // Set the stage
//                primaryStage.setTitle("Hello View");
//                primaryStage.setScene(scene);
//                primaryStage.show();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println("No window will be displayed.");
//        }
//    }
//}

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class MainApp extends Application {

    static DBConnector dbConnector = new DBConnector();

    FXMLLoader loader = new FXMLLoader(getClass().getResource("Articles1.fxml"));
    static UserDA userDA = new UserDA(dbConnector);
//    ArticleFetcher controller =  loader.getController();
    static UserService  userService = new UserService(userDA);
//    ArticleController1 ArticleController = new ArticleController1(dbConnector);

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

            ArticleFetcher articleFetcher = new ArticleFetcher(dbConnector);
            fxmlLoader.setController(articleFetcher);

            Scene scene = new Scene(fxmlLoader.load(), 400, 300);
            primaryStage.setTitle("Articles");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }
}



