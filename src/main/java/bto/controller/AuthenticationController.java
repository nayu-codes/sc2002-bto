package bto.controller;

import bto.database.UserDB;
import bto.model.user.User;
import bto.model.user.UserType;
import bto.model.user.MaritalStatus;
import bto.model.user.Applicant;

import java.util.Scanner;

public class AuthenticationController {
    public AuthenticationController() {
        // Constructor for the AuthenticationController class
    }

    /**
     * Login method to authenticate the user.
     * @param userId the user ID entered by the user
     * @param password the password entered by the user
     * 
     * @return User object if authentication is successful, null otherwise
     */
    public static User loginAsUser(String userId, String password) {
        // Validate the user credentials
        if (validateCredentials(userId, password)) {
            // If valid, retrieve the user from the database
            return UserDB.getUserById(userId);
        } else {
            // If invalid, return null
            return null;
        }
    }

    /**
     * Validates the user credentials against the database.
     * @param userId the user ID entered by the user
     * @param password the password entered by the user
     * @return true if the credentials are valid, false otherwise
     */
    public static boolean validateCredentials(String userId, String password) {
        System.out.println("User count: " + UserDB.getUserCount()); // Debugging line to check user count
        // Validate the user credentials
        if (userId == null || password == null) {
            return false;
        }
        
        // Check if the user exists in the database
        User user = UserDB.getUserById(userId);
        if (user == null) {
            return false; // User not found
        }

        // Check if the password matches, true if it does, false otherwise
        return user.getPassword().equals(password);
    }

    /**
     * Create a new Applicant user in the database.
     * @param userId the user ID entered by the user
     * @param password the password entered by the user
     * @param name the name entered by the user
     * @param age the age entered by the user
     * @param maritalStatus the marital status entered by the user
     */
    public static void createApplicant(String userId, String password, String name, int age, MaritalStatus maritalStatus) {
        // Assume the user type is Applicant
        UserType userType = UserType.APPLICANT;
        // Create a new Applicant object with the provided details
        Applicant newApplicant = new Applicant(name, userId, password, age, maritalStatus, userType);
        // Add the new applicant to the database
        UserDB.addUser(newApplicant);
    }

    /**
     * Prompts the user for a new password and update the password in the database.
     * 
     * @param User the user whose password is to be updated
     */
    public static void updatePassword(User user) {
        // Prompt the user for a new password
        System.out.print("Enter new password: ");
        Scanner scanner = new Scanner(System.in);
        String newPassword = scanner.nextLine();

        // Set new password for the user
        user.setPassword(newPassword);

        // Update the user's password in the database
        UserDB.updateUser(user);
    }

}