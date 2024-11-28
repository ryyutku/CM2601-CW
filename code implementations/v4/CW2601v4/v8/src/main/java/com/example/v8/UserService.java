package com.example.v8;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class UserService {

    UserDA access;
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
//                this.user = new User(id,username,password);
                Singleton.getInstance().setUser(new User(id,username,password));


                System.out.println();
                System.out.println("user id: "+id);
                System.out.println("Getting the userId from singleton: "+ Singleton.getInstance().getUser().getUserId());
                System.out.println();

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
        writer.println("Enter your username: ");
        String username = reader.readLine();
        if(!access.checkUser(username)){
            writer.println("Enter password: ");
            String password = reader.readLine();
            writer.println("Re-enter your password: ");
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

//    Method  for administrator to login
    public boolean adminLogin() throws IOException {
        //        First checks whether the username is correct
        writer.println("Please enter your admin username: ");
        String username = reader.readLine().trim();
        if(access.checkUser(username)){
            writer.println("Please enter you admin password: ");
            String password = reader.readLine().trim();
            if(access.checkPassword(username, password)){

                int id = access.getUserId(username);
                Singleton.getInstance().setUser(new User(id,username,password));
                Singleton.getInstance().getUser().setAdmin(true);


                System.out.println();
                System.out.println("admin id: "+id);
                System.out.println("Getting the admin from singleton: "+ Singleton.getInstance().getUser().getUserId());
                System.out.println();

                System.out.println("Successfully logged in!");

                writer.println("Successfully logged in!");
            }else{
                System.out.println("The admin password do not match");

                writer.println("The admin password do not match");
                return false;
            }
        }else{
            System.out.println("The admin username is incorrect");

            writer.println("The admin username is incorrect");
            return false;
        }
        return true;

    }


}
