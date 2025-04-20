package bto.ui.application;

import java.text.SimpleDateFormat;
import java.util.Scanner;

import bto.model.application.BTOApplication;
import bto.model.user.User;
import bto.model.user.Applicant;
import bto.ui.TerminalUtils;

public class ApplicationDashboard {
    private ApplicationDashboard(){} // Prevents Instantiation

    public static void start(User user){
        int choice = -1; // Initialize choice to an invalid value

        Scanner scanner = new Scanner(System.in);
        TerminalUtils.clearScreen();
        do {
            System.out.println(" \n Applied Projects for: " + user.getName());
            System.out.println(" " + "-".repeat(83));
            System.out.printf(" %5s | %15s | %15s | %20s | %15s\n", "Index", "Project Name", "Neighbourhood", "Application Date", "Status");
            System.out.println(" " + "-".repeat(83));

            Applicant applicant = (Applicant) user;
            // Check if the user has any applications
            if (applicant.appliedProjects().isEmpty()) {
                System.out.println("  You have not applied for any projects.");
                System.out.print("  Enter any key to go back to the main menu.");
                scanner.nextLine();
                TerminalUtils.clearScreen();
                return;
            }
            else{
                // Count for list indexing
                int i = 1;

                // Print each applied project
                for (BTOApplication application : applicant.appliedProjects()) {
                    // Print in table format, with consistent spacing
                    // i, Project Name (Neighbourhood: Location) | Application Opening - Closing Date
                    System.out.printf("  %3d. | %15s | %15s | %20s | %15s\n", 
                        i, application.getProject().getName(), application.getProject().getNeighbourhood(),
                        new SimpleDateFormat("dd/MM/yyyy").format(application.getApplicationDate()),
                        application.getStatus()
                    );
                    i++;
                };
                System.out.println(" " + "-".repeat(83));
                System.out.println("  Please enter the index of the applied project you want to view more information about, or '0' to go back to the main menu.");
                System.out.print("  Enter your choice: ");

                try {
                    choice = scanner.nextInt(); // Read the user's choice
                    scanner.nextLine();
                } catch (NumberFormatException e) {
                    System.out.println("  Invalid input. Please enter a number.");
                    continue; // Skip to the next iteration of the loop
                }

                if (choice < 0 || choice > applicant.appliedProjects().size()) {
                    System.out.println("  Invalid choice. Please try again.");
                } else if (choice == 0) {
                    System.out.println("  Returning to the main menu...");
                    return; // Exit the loop and return to the main menu
                } else {
                    // Get project details for the selected project
                    BTOApplication selectedApplication= applicant.appliedProjects().get(choice - 1);

                    // Pass the selected project to the ProjectDetailsScreen
                    ApplicationDetails.start(user, selectedApplication);
                }
            }
        }while(choice != 0);
    }
}