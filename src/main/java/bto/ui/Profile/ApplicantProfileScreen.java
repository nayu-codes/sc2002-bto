package bto.ui.profile;

import bto.model.user.User;
import bto.ui.ChangePassword;
import bto.ui.TerminalUtils;
import bto.ui.enquiry.EnquiryDashboard;
import bto.ui.project.ProjectDashboard;
import bto.ui.application.ApplicationDashboard;

import java.util.Scanner;

public class ApplicantProfileScreen {
    private ApplicantProfileScreen() {} // Prevents instantiation

    public static void start(User user){
        int option = -1;
        Scanner scanner = new Scanner(System.in);

        TerminalUtils.clearScreen();
        do{
            // If login successful, welcome the user
            System.out.println("\nLogin successful! Welcome, " + user.getName() + ".");

            // Menu for the user to select an option
            System.out.println("+---+----------------------------+\n" +
                               "| # | Option                     |\n" +
                               "+---+----------------------------+\n" +
                               "| 1 | Change Password            |\n" +
                               "| 2 | View Available Projects    |\n" +
                               "| 3 | View Application(s)        |\n" +
                               "| 4 | View Enquiries             |\n" +
                               "| 0 | Log Out                    |\n" +
                               "+---+----------------------------+\n");

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
                    // Calls ChangePassword to change user password
                    ChangePassword.start(user);
                    break;
                case 2:
                    // Calls ProjectDashboard to view available projects
                    ProjectDashboard.start(user);
                    break;
                case 3:
                    // Calls ApplicationDashboard to view applied projects
                    ApplicationDashboard.start(user);
                    break;
                case 4:
                    // Calls EnquiryDashboard to view user enquiries
                    EnquiryDashboard.start(user);
                    break;
                case 0:
                    // Log Out - goes back to Main Screen
                    System.out.println("Logging out. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while(option != 0);      
    }
}
