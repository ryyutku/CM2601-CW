package org.example.cw2601v4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientMain {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8818);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // Login command
            out.println("LOGIN");
            System.out.println(in.readLine());  // Prompt for username
            out.println("user");  // Send username
            System.out.println(in.readLine());  // Prompt for password
            out.println("password");  // Send password
            System.out.println(in.readLine());  // Receive login response

            // Read articles command
            out.println("READ_ARTICLES");
            System.out.println(in.readLine());  // Display articles

            // Quit command
            out.println("QUIT");
            System.out.println(in.readLine());  // Display quit message

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
