package bto.ui.application;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.text.SimpleDateFormat;

import bto.database.ApplicationDB;
import bto.database.BTOProjectDB;
import bto.model.application.BTOApplication;
import bto.model.project.BTOProject;
import bto.model.user.User;
import bto.model.user.Applicant;
import bto.model.user.HDBManager;

import bto.ui.TerminalUtils;

public class ApplicationDashboard {
    private ApplicationDashboard(){} // Prevents Instantiation

    /**
     * Displays the application dashboard for the Applicant.
     * 
     * @param user The user whose application dashboard is to be displayed.
     */
    public static void start(User user){
        int choice = -1; // Initialize choice to an invalid value
        Scanner scanner = new Scanner(System.in);
        
        TerminalUtils.clearScreen();
        do {
            System.out.println(" \nApplied projects for: " + user.getName());
            System.out.println("-".repeat(83));
            System.out.printf(" %5s | %15s | %15s | %20s | %15s\n", "Index", "Project Name", "Neighbourhood", "Application Date", "Status");
            System.out.println("-".repeat(83));

            Applicant applicant = (Applicant) user;
            // Check if the user has any applications
            if (applicant.appliedProjects().isEmpty()) {
                System.out.println("You have not applied for any projects.");
                System.out.println("Enter any key to go back to the main menu.");
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
                    // i | Project Name | Neighbourhood: Location | Application Date | Status
                    System.out.printf("  %3d. | %15s | %15s | %20s | %15s\n", 
                        i, application.getProject().getName(), application.getProject().getNeighbourhood(),
                        new SimpleDateFormat("dd/MM/yyyy").format(application.getApplicationDate()),
                        application.getStatus()
                    );
                    i++;
                };
                System.out.println("-".repeat(83));
                System.out.println("  Please enter the index of the applied project you want to view more information about, or '0' to go back to the main menu.");
                System.out.print("  Enter your choice: ");

                try {
                    choice = scanner.nextInt(); // Read the user's choice
                    scanner.nextLine();
                } catch (InputMismatchException e) {
                    System.out.println("  Invalid input. Please enter a number.");
                    scanner.nextLine(); // Clear the invalid input
                    continue; // Skip to the next iteration of the loop
                }

                if (choice < 0 || choice > applicant.appliedProjects().size()) {
                    System.out.println("  Invalid choice. Please try again.");
                } else if (choice == 0) {
                    TerminalUtils.clearScreen();
                    return; // Exit the loop and return to the main menu
                } else {
                    // Get application details for the selected project
                    BTOApplication selectedApplication = applicant.appliedProjects().get(choice - 1);

                    // Pass the selected application to ApplicationDetails
                    ApplicationDetails.start(user, selectedApplication);
                }
            }
        }while(choice != 0); // Continue until the user chooses to go back to the main menu
    }

    public static void managerView(HDBManager manager) {
        int choice = -1; // Initialize choice to an invalid value
        Scanner scanner = new Scanner(System.in);

        TerminalUtils.clearScreen();
        do {
            System.out.println(" \nBTO Application Portal");
            System.out.println("-".repeat(110));
            System.out.printf(" %5s | %15s | %15s | %15s | %11s | %10s | %15s\n", "Index", "Applicant Name", "Project Name", "Neighbourhood", "Application Date", "Room Type", "Status");
            System.out.println("-".repeat(110));

            // Get the list of applications for the manager
            ArrayList<BTOApplication> applications = new ArrayList<>(); // Initialize an empty list to store applications
            for (BTOProject project : BTOProjectDB.getBTOProjectsByManager(manager)) {
                for (BTOApplication application : ApplicationDB.getApplicationsByProjectName(project.getName())) {
                    applications.add(application); // Add each application to the list
                }
            }

            // Count for list indexing
            int i = 1;

            // Print each applied project
            for (BTOApplication application : applications) {
                // Print in table format, with consistent spacing
                // i | Applicant Name | Project Name | Neighbourhood | Application Date | Room Type | Status
                System.out.printf("  %3d. | | %15s | %15s | %15s | %11s | %10s | %15s\n",
                        i, application.getApplicant().getName(), application.getProject().getName(), application.getProject().getNeighbourhood(),
                        new SimpleDateFormat("dd/MM/yyyy").format(application.getApplicationDate()), application.getFlatType().getDisplayName(), application.getStatus());
                i++;
            }
            System.out.println("-".repeat(83));
            System.out.println(
                    "  Please enter the index of the applied project you want to view more information about, or '0' to go back to the main menu.");
            System.out.print("  Enter your choice: ");

            try {
                choice = scanner.nextInt(); // Read the user's choice
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("  Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
                continue; // Skip to the next iteration of the loop
            }

            if (choice < 0 || choice > i) {
                System.out.println("  Invalid choice. Please try again.");
            } else if (choice == 0) {
                TerminalUtils.clearScreen();
                return; // Exit the loop and return to the main menu
            } else {
                // Get application details for the selected project
                BTOApplication selectedApplication = applications.get(choice - 1);

                // Pass the selected application to ApplicationDetails
                ApplicationDetails.start(manager, selectedApplication);
            }
        } while (choice != 0); // Continue until the user chooses to go back to the main menu
    }
}