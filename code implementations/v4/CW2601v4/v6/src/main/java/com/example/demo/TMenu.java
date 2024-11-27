package com.example.demo;

import java.util.Scanner;

public class TMenu {
    private  UserService service;
    private  Scanner scanner = new Scanner(System.in);

    public TMenu(UserService service) {
        this.service = service;
    }

    // Prompt the user for login or signup, returning true if login is successful
    public boolean startLogin() {
        boolean loop = true;

        while (loop) {
            System.out.println("Would you like to login or sign up? (press 'l' for login, 's' for sign up)");
            System.out.println("Press 'q' to exit");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "l":
                    if (service.login()) {
                        return true;  // Login successful
                    } else {
                        System.out.println("Login failed. Try again.");
                    }
                    break;
                case "s":
                    if (service.signin()) {
                        System.out.println("Signup successful. Please login.");
                    } else {
                        System.out.println("Signup failed. Try again.");
                    }
                    break;
                case "q":
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid input.");
            }
        }
        return false;
    }
}
