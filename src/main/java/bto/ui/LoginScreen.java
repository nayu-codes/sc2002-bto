package bto.ui;

import bto.controller.AuthenticationController;
import bto.model.user.User;
import bto.ui.profile.ApplicantProfileScreen;
import bto.ui.profile.OfficerProfileScreen;
import bto.ui.profile.ManagerProfileScreen;

import java.util.Scanner;

public class LoginScreen {
    private LoginScreen(){} // Prevents instantiation

    /**
     * Starts the login screen for the user to enter their credentials.
     * This method prompts the user for their username and password,
     * validates the credentials, and proceeds to the next screen if successful.
     */
    public static void start() {
        // Start the login screen
        System.out.println("--- Log In ---");
        System.out.println("Please enter your username and password to login.");

        // Add code here to handle user input for username and password
        System.out.print("  Enter User ID: ");
        Scanner scanner = new Scanner(System.in);
        String userId = scanner.nextLine();

        System.out.print("  Enter password: ");
        String password = scanner.nextLine();

        // Authenticate the user
        User user = AuthenticationController.loginAsUser(userId, password);
        if (user != null) {
            switch(user.getUserType().getDisplayName()) {
                case "Applicant":
                    // Applicant dashboard
                    ApplicantProfileScreen.start(user);
                    break;

                case "HDB Officer":
                    // HDB Officer dashboard
                    OfficerProfileScreen.start(user);
                    break;

                case "HDB Manager":
                    // Admin dashboard
                    ManagerProfileScreen.start(user);
                    break;

                default:
                    System.out.println("Unknown user type. Please contact support.");
            }
        } else {
            // Display an error message if authentication fails, and exit back to the main menu
            System.out.println("Invalid username or password. Please try again.");
        }
    }

}