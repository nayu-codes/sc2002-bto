package bto.controller;

import bto.model.user.User;
import bto.database.UserDB;

public class AuthenticationController {
    public AuthenticationController() {
        // Constructor for the AuthenticationController class
    }

    /**
     * Validates the user credentials against the database.
     * @param userId
     * @param password
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

        // Check if the password matches
        if (!user.getPassword().equals(password)) {
            return false; // Password does not match
        }

        // User is authenticated successfully
        return true;
    }
}
