package com.example.v8;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ServerWorker implements Runnable {

    private final Socket clientSocket;

    static DBConnector dbConnector;
    static UserDA userDA;
    static UserService userService;

    private static boolean isLoggedIn;

    private BufferedReader reader;
    private PrintWriter writer;

    public ServerWorker(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;

        InputStream in = clientSocket.getInputStream();
        OutputStream out = clientSocket.getOutputStream();

        this.reader = new BufferedReader(new InputStreamReader(in));
        this.writer = new PrintWriter(out, true); // Auto-flush enabled

        this.dbConnector = new DBConnector();
        Singleton.getInstance().setDBConnector(dbConnector);
        this.userDA = new UserDA(dbConnector);
        this.userService = new UserService(userDA,clientSocket);
    }

    @Override
    public void run() {
        try {
            // Handle client socket communication
            handleClientSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientSocket() throws IOException {
        writer.println("Starting the handleClientSocket..."); // Send to Telnet client
        TMenu menu = new TMenu(userService, clientSocket); // Pass socket for communication

        isLoggedIn = menu.startLogin();

        if (isLoggedIn) {
            writer.println("Press 'r' to read articles, or 'q' to quit.");
            String input = reader.readLine().trim();

            if (input.equalsIgnoreCase("r")) {
                writer.println("Launching JavaFX...");
                // Launch JavaFX on its thread

                Platform.runLater(() -> {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Articles1.fxml"));
                        Scene scene = new Scene(fxmlLoader.load(), 460.0, 244.0);

                        ArticleController articleController = fxmlLoader.getController();
                        // (Optional) Pass any data to the controller if needed
                        // articleController.initializeData(yourData);

                        Stage articleStage = new Stage();
                        articleStage.setTitle("Articles");
                        articleStage.setScene(scene);
                        articleStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            } else if (input.equalsIgnoreCase("q")) {
                writer.println("Goodbye!");
                clientSocket.close(); // Close the socket
            } else {
                writer.println("Invalid input.");
            }
        } else {
            writer.println("Login failed. Exiting.");
        }
    }
}
