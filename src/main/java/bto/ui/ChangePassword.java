package bto.ui;

import bto.controller.AuthenticationController;
import bto.model.user.User;

import java.util.Scanner;

public class ChangePassword {
    private ChangePassword(){} // Prevents instantiation

    public static void start(User user) {
        System.out.println("  ------------------------------------");
        System.out.println("           Change Password");
        System.out.println("  ------------------------------------");
        // Prompt the user for a new password
        System.out.print("  Enter new password: ");
        Scanner scanner = new Scanner(System.in);
        String newPassword = scanner.nextLine();
        
        // Prompt the user to confirm the new password
        System.out.print("  Confirm new password: ");
        String confirmPassword = scanner.nextLine();

        // Check if the new password and confirm password match
        if (newPassword.equals(confirmPassword)) {
            // If they match, update the password
            AuthenticationController.updatePassword(user, newPassword);
            System.out.println("Password changed successfully!");
        } else {
            // If they don't match, inform the user
            System.out.println("  Passwords do not match. Please try again.");
        }
    }
}
