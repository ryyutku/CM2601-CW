package com.example.v9;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TMenu {
    private UserService service;
    private final Scanner scanner = new Scanner(System.in);

    private final Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;

    public TMenu(UserService service, Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;

        InputStream inputStream = clientSocket.getInputStream();
        OutputStream outputStream = clientSocket.getOutputStream();

        this.reader = new BufferedReader(new InputStreamReader(inputStream));
        this.writer = new PrintWriter(outputStream, true); // Auto-flush enabled
        this.service = service;
    }

    // Prompt the user for login or signup, returning true if login is successful
    public boolean startLogin() throws IOException {
        boolean loop = true;

        while (loop) {
            // Send messages to the Telnet client instead of IntelliJ terminal
            writer.println("Would you like to login or sign up? (press 'l' for login, 's' for sign up)--(press 'al' to login as admin");
            writer.println("Press 'q' to exit");

            String input = reader.readLine().trim().toLowerCase(); // Read input from Telnet client

            switch (input) {
                case "l":
                    if (service.login()) {
                        writer.println("Login successful."); // Send success message to client
                        return true;  // Login successful
                    } else {
                        writer.println("Login failed. Try again.");
                    }
                    break;
                case "s":
                    if (service.signin()) {
                        writer.println("Signup successful. Please login.");
                    } else {
                        writer.println("Signup failed. Try again.");
                    }
                    break;
                case "al":
                    if(service.adminLogin()){
                        writer.println("Admin login successful.");
                        return true;
                    }else{
                        writer.println("Admin login failed. Try again.");
                    }
                case "q":
                    writer.println("Goodbye!");
                    System.exit(0);
                default:
                    writer.println("Invalid input.");
            }
        }
        return false;
    }
}
