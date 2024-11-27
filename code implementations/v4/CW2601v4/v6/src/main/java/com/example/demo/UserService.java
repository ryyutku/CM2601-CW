package com.example.demo;

import java.util.Scanner;

public class UserService {

    UserDA access;
    User user;
    Scanner scanner = new Scanner(System.in);


    public UserService(UserDA access) {
        this.access = access;

//        this.access = new UserDA(new DBConnector());

    }

    //    Method to login
    public boolean login() {
//        First checks whether the username is correct
        System.out.println("Please enter your username: ");
        String username = scanner.nextLine().trim();
        if(access.checkUser(username)){
            System.out.println("Please enter you password: ");
            String password = scanner.nextLine().trim();
            if(access.checkPassword(username, password)){
                int id = access.getUserId(username);
                this.user = new User(id,username,password);
                System.out.println("user id: "+id);
                System.out.println("Successfully logged in!");
            }else{
                System.out.println("The password do not match");
                return false;
            }
        }else{
            System.out.println("The username is incorrect");
            return false;
        }
        return true;
    }


    //    Method to sign in
    public boolean signin(){
//        Prompt user for details
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        if(!access.checkUser(username)){
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            System.out.print("Re-enter your password: ");
            String RePassword = scanner.nextLine();
            if(password.equals(RePassword)){
                access.addUser(username,password);

            }else{
                System.out.println("The password do not match");
                return false;
            }
        }else{
            System.out.println("User already exist!");
            return false;
        }

        return true;
    }


}
