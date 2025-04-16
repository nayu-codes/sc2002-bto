package bto;

import bto.database.UserDB;
import bto.database.BTOProjectDB;
import bto.ui.LoginScreen;

import java.util.Scanner;

public final class App {
    private App() {
    }
    /**
     * Main class for the BTO application system.
     * This class contains the main method to run the application.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        int option;

        // Menu for the user to select an option
        System.out.println("Welcome to the BTO Application System!");
        System.out.println("  1. Login");
        System.out.println("  2. Register");
        System.out.println("  3. Exit");
        System.out.println();
        System.out.print("Please select an option: ");

        Scanner scanner = new Scanner(System.in);
        option = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        System.out.println();

        while(option != 3) {
            // Initialise UserDB
            UserDB.init();
            switch (option) {
                case 1:
                    // Login option
                    LoginScreen.start();
                    // Call the login method (not implemented yet)
                    break;
                case 2:
                    // Register option
                    System.out.print("Enter your username: ");
                    String newUserId = scanner.nextLine();
                    System.out.print("Enter your password: ");
                    String newPassword = scanner.nextLine();
                    // Call the register method (not implemented yet)
                    System.out.println("Registration successful! Welcome, " + newUserId + "."); // Placeholder message
                    break;
                case 3:
                    // Exit option
                    System.out.println("Exiting the application. Goodbye!");
                    return; // Exit the application
                default:
                    System.out.println("Invalid option. Please try again.");
            }
            System.out.println();
            System.out.println("Welcome to the BTO Application System!");
            System.out.println("  1. Login");
            System.out.println("  2. Register");
            System.out.println("  3. Exit");
            System.out.println();
            System.out.print("Please select an option: ");
            
            option = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
        }

        if (UserDB.isEmpty()){
            UserDB.init();
        }

        // Print all users in the user list
        System.out.println("User List:");
        UserDB.printUserList();

        // Initialise the project database
        BTOProjectDB btoProjectDB = new BTOProjectDB();

        // Print all BTO projects in the project list
        System.out.println("BTO Project List:");
        BTOProjectDB.printBTOProjectList();
        
    }
}
