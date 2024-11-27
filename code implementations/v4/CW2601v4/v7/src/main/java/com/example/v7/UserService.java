package com.example.v7;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class UserService {

    UserDA access;
    User user;
    Scanner scanner = new Scanner(System.in);

    private final Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;


    public UserService(UserDA access, Socket clientSocket) throws IOException {

        this.clientSocket = clientSocket;

        InputStream inputStream = clientSocket.getInputStream();
        OutputStream outputStream = clientSocket.getOutputStream();

        this.reader = new BufferedReader(new InputStreamReader(inputStream));
        this.writer = new PrintWriter(outputStream, true); // Auto-flush enabled
        this.access = access;

//        this.access = new UserDA(new DBConnector());

    }

    //    Method to login
    public boolean login() throws IOException {
//        First checks whether the username is correct
        writer.println("Please enter your username: ");
        String username = reader.readLine().trim();
        if(access.checkUser(username)){
            writer.println("Please enter you password: ");
            String password = reader.readLine().trim();
            if(access.checkPassword(username, password)){
                int id = access.getUserId(username);
                this.user = new User(id,username,password);
                System.out.println("user id: "+id);
                System.out.println("Successfully logged in!");

                writer.println("Successfully logged in!");
            }else{
                System.out.println("The password do not match");

                writer.println("The password do not match");
                return false;
            }
        }else{
            System.out.println("The username is incorrect");

            writer.println("The username is incorrect");
            return false;
        }
        return true;
    }


    //    Method to sign in
    public boolean signin() throws IOException {
//        Prompt user for details
        writer.print("Enter your username: ");
        String username = reader.readLine();
        if(!access.checkUser(username)){
            writer.print("Enter password: ");
            String password = reader.readLine();
            writer.print("Re-enter your password: ");
            String RePassword = reader.readLine();
            if(password.equals(RePassword)){
                access.addUser(username,password);

            }else{
                System.out.println("The password do not match");

                writer.println("The password do not match");

                return false;
            }
        }else{
            System.out.println("User already exist!");

            writer.println("User already exist!");

            return false;
        }

        return true;
    }


}
