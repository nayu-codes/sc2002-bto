package bto.ui;

import bto.controller.AuthenticationController;
import bto.model.user.User;

import java.util.Scanner;

public class ChangePassword {
    private ChangePassword(){} // Prevents instantiation

    /**
     * This method is called when the user selects the option to change their password
     * 
     * @param user any user
     */
    public static void start(User user) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- Change Password ---");
        // Prompt the user for a new password
        System.out.println("Enter new password: (type 'exit' to go back)");
        String newPassword = scanner.nextLine();
        
        if(newPassword.toLowerCase().contains("exit")){
            TerminalUtils.clearScreen();
            return;
        }

        // Prompt the user to confirm the new password
        System.out.println("\nConfirm new password: ");
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
