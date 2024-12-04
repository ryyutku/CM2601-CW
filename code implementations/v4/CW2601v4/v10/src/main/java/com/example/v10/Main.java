package com.example.v10;

import javafx.application.Application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//This is the ServerMain

public class Main {

    public static void main(String[] args) {
        // Start the JavaFX application in a separate thread
        Thread javafxThread = new Thread(() -> Application.launch(ArticleApp.class));
        javafxThread.setDaemon(true); // letting the JVM exit if this thread is the only one left
        javafxThread.start();

//         Start the server on the main thread
        int port = 8818;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

//                 Start a ServerWorker thread for each client
                ServerWorker worker = new ServerWorker(clientSocket);
                new Thread(worker).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
