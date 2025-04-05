package bto;

import bto.user.User;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }
    /**
     * Main class for the BTO application system.
     * This class contains the main method to run the application.
     * 
     * @param args
     */
    public static void main(String[] args) {
        // Main method to run the application
        System.out.println("Welcome to the BTO Application System!");
        // You can add more functionality here, such as initializing the user interface
        // or loading data.

        // Example of creating a user object
        User user = new User("John Doe", "johndoe", "password123", 30, "Single", "Applicant") {
            // This is an anonymous class extending the abstract User class
        };
        System.out.println("User created: " + user.getName() + ", User ID: " + user.getUserId());
        
    }
}
