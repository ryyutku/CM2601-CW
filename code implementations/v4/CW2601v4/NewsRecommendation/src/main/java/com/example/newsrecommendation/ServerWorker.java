package com.example.newsrecommendation;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

//To handle the connection between the client (user) amd the Server(thread)
public class ServerWorker implements Runnable {

    private final Socket clientSocket;
    static DBConnector dbConnector;
    static UserDA userDA;
    static UserService userService;

    private static boolean isLoggedIn;
//To interact with the user from the terminal
    private BufferedReader reader;
    private PrintWriter writer;

//    Initializing the SeverWorker
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
//If the login is successful then user is directed to the system
        if (isLoggedIn) {
            if(Singleton.getInstance().getUser().isAdmin()){
                adminSession(); //To handle the case when the user is an admin
                }else if (!Singleton.getInstance().getUser().isAdmin()) {

                    writer.println("Press 'r' to read articles, or 'q' to quit.");
                    String input = reader.readLine().trim();
//If the user insert r will let the user read the articles and the corresponding gui is loaded
                    if (input.equalsIgnoreCase("r")) {
                        writer.println("Launching JavaFX...");
                        // Launch JavaFX on its thread

//                        To run the JavaFX from the terminal when prompted to
                        Platform.runLater(() -> {
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Articles1.fxml"));
                                Scene scene = new Scene(fxmlLoader.load(), 670, 600);

                                boolean resize = false;

                                if(Singleton.getInstance().getUser().isAdmin()){
                                    resize = true;
                                }

                                Stage articleStage = new Stage();
                                articleStage.setTitle("User: "+Singleton.getInstance().getUser().getUsername());
                                articleStage.setScene(scene);

                                articleStage.setResizable(resize);

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
                }
            }else {
            writer.println("Login failed. Exiting.");
        }
    }


//    To handle the session when logged in as an admin
    public void adminSession() throws IOException {

        writer.println("Press 'r' to read articles| 'p' to publish articles, or 'q' to quit.");
        String input = reader.readLine().trim();

        if (input.equalsIgnoreCase("r")) {
            writer.println("Launching JavaFX...");
            // Launch JavaFX on its thread

            Platform.runLater(() -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Articles1.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 780.0, 600);

                    Stage articleStage = new Stage();
                    articleStage.setTitle("Admin "+Singleton.getInstance().getUser().getUsername());
                    articleStage.setScene(scene);
                    articleStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } else if(input.equalsIgnoreCase("p")) {

            Platform.runLater(() -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("AdministerArticle.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 600, 400);

                    Stage articleStage = new Stage();
                    articleStage.setTitle("Admin: "+Singleton.getInstance().getUser().getUsername());
                    articleStage.setScene(scene);

//                    articleStage.setResizable(false);
                    articleStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


        }else if (input.equalsIgnoreCase("q")) {
            writer.println("Goodbye!");
            clientSocket.close(); // Close the socket
        } else {
            writer.println("Invalid input.");
            adminSession();
        }


    }
}
