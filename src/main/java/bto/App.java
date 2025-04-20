package bto;

import bto.ui.LoginScreen;
import bto.ui.RegisterUserScreen;
import bto.ui.TerminalUtils;
import bto.database.Database;

import java.util.Scanner;

public final class App {
    private App(){} // Prevents instantiation
    /**
     * Main class for the BTO application system.
     * This class contains the main method to run the application.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) { 
        int option = -1;
        Scanner scanner = new Scanner(System.in);

        // Initialise the database
        Database.init();
        TerminalUtils.clearScreen();
        do{
            // Menu for the user to select an option
            System.out.println("\nWelcome to the BTO Application System!\n" +
                               "+---+---------------------+\n" +
                               "| # | Option              |\n" +
                               "+---+---------------------+\n" +
                               "| 1 | Login as a User     |\n" +
                               "| 2 | Register as a User  |\n" +
                               "| 0 | Exit Application    |\n" +
                               "+---+---------------------+\n");

            System.out.print("Please select an option: ");
            
            try {
                option = scanner.nextInt(); // Read the user's choice
                scanner.nextLine(); // Consume the newline character
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
                continue; // Skip to the next iteration of the loop
            }
            System.out.println();
            
            switch (option) {
                case 1:
                    // Login option
                    LoginScreen.start();
                    break;
                case 2:
                    // Register option
                    RegisterUserScreen.start();
                    break;
                case 0:
                    // Exit option
                    System.out.println("Exiting the application. Goodbye!");
                    return; // Exit the application
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while(option != 0);        
    }
}
