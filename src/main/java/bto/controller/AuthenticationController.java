package bto.controller;

import bto.database.UserDB;
import bto.model.user.User;

public class AuthenticationController {
    public AuthenticationController() {
        // Constructor for the AuthenticationController class
    }

    /**
     * Login method to authenticate the user.     * 
     * @param userId
     * @param password
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
}
