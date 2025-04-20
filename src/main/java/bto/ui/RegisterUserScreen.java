package bto.ui;

import java.util.Scanner;

import bto.controller.AuthenticationController;
import bto.model.user.MaritalStatus;

public class RegisterUserScreen {
    private RegisterUserScreen(){} // Prevents instantiation

    /**
     * Starts the registration screen for the user to enter their details.
     * This method prompts the user for their details and registers them in the system.
     */
    public static void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("  ------------------------------------");
        System.out.println("  Registering a new user...");
        System.out.println("  ------------------------------------");
        // UserID (NRIC) must start with S or T, followed by 7 digits and a letter, all uppercase.
        String newUserId;
        do {
            System.out.println("  UserID must be all uppercase, 8 characters long, starting with S or T, followed by 7 digits and a letter. (e.g., S1234567A)");
            System.out.print("  Enter your UserID (NRIC): ");
            newUserId = scanner.nextLine();
            if (!newUserId.matches("^[ST]\\d{7}[A-Z]$")) {
                System.out.println("  Invalid UserID format. Please try again.");
            } else {
                break; // Exit the loop if the UserID is valid
            }
        } while (true);
        System.out.print("  Enter your password: ");
        String newPassword = scanner.nextLine();

        System.out.print("  Enter your name: ");
        String newName = scanner.nextLine();
        System.out.print("  Enter your age: ");
        int newAge = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        String maritalStatusInput;
        MaritalStatus maritalStatus = null;
        do {
            System.out.print("  Enter your marital status (Single/Married): ");
            maritalStatusInput = scanner.nextLine();
            maritalStatus = null;
            if (maritalStatusInput.equalsIgnoreCase("Single")) {
                maritalStatus = MaritalStatus.SINGLE;
                break;
            } else if (maritalStatusInput.equalsIgnoreCase("Married")) {
                maritalStatus = MaritalStatus.MARRIED;
                break;
            } else {
                System.out.println("  Invalid marital status. Please enter 'Single' or 'Married'.");
            }
        } while (true); // Exit the loop if the marital status is valid

        AuthenticationController.createApplicant(newUserId, newPassword, newName, newAge, maritalStatus);

        System.out.println("  Registration successful! Your UserID is: " + newUserId);
        System.out.println("  Please login to continue.");
        return;
    }
}
