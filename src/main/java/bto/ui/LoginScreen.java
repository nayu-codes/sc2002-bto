package bto.ui;

import bto.controller.AuthenticationController;
import bto.model.user.User;
import bto.database.UserDB;

import java.util.Scanner;

public class LoginScreen {
    public LoginScreen() {
        // Constructor for the LoginScreen class
    }

    /**
     * Starts the login screen for the user to enter their credentials.
     * This method prompts the user for their username and password,
     * validates the credentials, and proceeds to the next screen if successful.
     */
    public static void start() {
        // Start the login screen
        System.out.println("Please enter your username and password to login.");

        // Add code here to handle user input for username and password
        System.out.print("  Enter User ID: ");
        Scanner scanner = new Scanner(System.in);
        String userId = scanner.nextLine();

        System.out.print("  Enter password: ");
        String password = scanner.nextLine();

        // Authenticate the user
        if (AuthenticationController.validateCredentials(userId, password)) {
            // If authentication is successful, retrieve the user object
            User user = UserDB.getUserById(userId);
            System.out.println("Login successful! Welcome, " + user.getName() + ".");

            // Proceed to the next screen or functionality
            // ProfileScreen.showProfile(user);
        } else {
            System.out.println("Invalid username or password. Please try again.");
            
            // Exit back to main menu
            return;
        }
    }

}
