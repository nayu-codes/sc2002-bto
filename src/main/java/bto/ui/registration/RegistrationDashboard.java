package bto.ui.registration;

import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.InputMismatchException;

import bto.database.BTOProjectDB;
import bto.database.RegistrationDB;
import bto.model.project.BTOProject;
import bto.model.registration.OfficerRegistration;
import bto.model.user.User;
import bto.model.user.HDBOfficer;
import bto.model.user.HDBManager;
import bto.controller.ProjectController;
import bto.ui.TerminalUtils;

public class RegistrationDashboard {
    private RegistrationDashboard(){} // Prevents Instantiation

    /**
     * Displays the registration dashboard for the user.
     * This method is called when the user selects the option to view their registered projects.
     * 
     * @param user the HDB Officer user whose registered projects are to be displayed
     */
    public static void start(User user){
        Scanner scanner = new Scanner(System.in);
        
        TerminalUtils.clearScreen();
        System.out.println(" \nRegistered projects for: " + user.getName());
        System.out.println("-".repeat(87));
        System.out.printf(" %5s | %15s | %23s | %10s | %20s\n", "Index", "Project Name", "Application Period", "Time", "Registration Status");
        System.out.println("-".repeat(87));

        HDBOfficer officer = (HDBOfficer) user;
        // Check if the user has any applications
        if (officer.getRegisteredProjects().isEmpty()){
            System.out.println("You have not registered for any projects.");
        }
        else{
            // Count for list indexing
            int i = 1;

            // Print each applied project
            for (OfficerRegistration registration: officer.getRegisteredProjects()) {
                // Print in table format, with consistent spacing
                // i | Project Name | Application Opening - Closing Date | Status
                System.out.printf("  %3d. | %15s | %10s - %10s | %10s | %20s\n", 
                    i, registration.getProject().getName(),
                    new SimpleDateFormat("dd/MM/yyyy").format(registration.getProject().getApplicationOpeningDate()),
                    new SimpleDateFormat("dd/MM/yyyy").format(registration.getProject().getApplicationClosingDate()),
                    ProjectController.getProjectStatus(registration.getProject()),
                    registration.getRegistrationStatus()
                );
                i++;
            };
            System.out.println("-".repeat(87));
            System.out.println("These are your registered projects.");
        }    
        System.out.println("Enter any key to go back to the main menu.");
        scanner.nextLine();
        TerminalUtils.clearScreen();
        return;
    }

    public static void viewAllRegistration(HDBManager manager) {
        Scanner scanner = new Scanner(System.in);
        
        TerminalUtils.clearScreen();
        System.out.println(" \nAll Officer Registrations");
        System.out.println("-".repeat(110));
        System.out.printf(" %5s | %15s | %15s | %23s | %10s | %20s\n", "Index", "Officer Name", "Project Name", "Application Period", "Time", "Registration Status");
        System.out.println("-".repeat(110));
        
        // Count for list indexing
        int i = 1;

        // Print each applied project
        for (OfficerRegistration registration: RegistrationDB.getAllRegistrations()) {
            // Print in table format, with consistent spacing
            // i | Project Name | Application Opening - Closing Date | Status
            System.out.printf("  %3d. | %15s | %15s | %10s - %10s | %10s | %20s\n", 
                i, registration.getOfficer().getName(), registration.getProject().getName(),
                new SimpleDateFormat("dd/MM/yyyy").format(registration.getProject().getApplicationOpeningDate()),
                new SimpleDateFormat("dd/MM/yyyy").format(registration.getProject().getApplicationClosingDate()),
                ProjectController.getProjectStatus(registration.getProject()),
                registration.getRegistrationStatus()
            );
            i++;
        };
        System.out.println("-".repeat(110));

        System.out.println("Enter any key to go back to the main menu.");
        scanner.nextLine();
        TerminalUtils.clearScreen();
    }

    public static void viewRegistrationRequsts(HDBManager manager) {
        int option = -1;
        Scanner scanner = new Scanner(System.in);
        TerminalUtils.clearScreen();
        
        do {
            System.out.println(" \nOfficer Registration Requests");
            System.out.println("-".repeat(110));
            System.out.printf(" %5s | %15s | %15s | %23s | %10s | %20s\n", "Index", "Officer Name", "Project Name", "Application Period", "Time", "Registration Status");
            System.out.println("-".repeat(110));
            
            // Count for list indexing
            int i = 1;

            // Get list of managed BTO Projects
            List<BTOProject> managedProjects = BTOProjectDB.getBTOProjectsByManager(manager);

            // Get list of officer registrations for each managed project
            List<OfficerRegistration> officerRegistrations = new ArrayList<>();
            for (OfficerRegistration registration : RegistrationDB.getAllRegistrations()) {
                for (BTOProject project : managedProjects) {
                    if (registration.getProject().getName().equals(project.getName())) {
                        officerRegistrations.add(registration);
                    }
                }
            }

            // Print each applied project
            for (OfficerRegistration registration : officerRegistrations) {
                // Print in table format, with consistent spacing
                // i | Officer Name | Project Name | Application Opening - Closing Date | Time | Registration Status
                System.out.printf("  %3d. | %15s | %15s | %10s - %10s | %10s | %20s\n", 
                    i, registration.getOfficer().getName(), registration.getProject().getName(),
                    new SimpleDateFormat("dd/MM/yyyy").format(registration.getProject().getApplicationOpeningDate()),
                    new SimpleDateFormat("dd/MM/yyyy").format(registration.getProject().getApplicationClosingDate()),
                    ProjectController.getProjectStatus(registration.getProject()),
                    registration.getRegistrationStatus()
                );
                i++;
            }
            System.out.println("-".repeat(110));

            System.out.println("These are the officer registration requests for your managed projects.");
            System.out.println("Please enter the index of the officer registration you want to view more information about, or '0' to go back to the main menu.");
            System.out.print("Enter your choice: ");
            try {
                option = scanner.nextInt(); // Read the user's choice
                scanner.nextLine(); // Consume the newline character
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
            
            if (option < 0 || option > officerRegistrations.size()) {
                System.out.println("Invalid choice. Please try again.");
            } else if (option == 0) {
                TerminalUtils.clearScreen();
                return; // Exit the loop and return to the main menu
            } else {
                // Get registration details for the selected officer registration
                OfficerRegistration selectedRegistration = officerRegistrations.get(option - 1);
                RegistrationDetails.start(manager, selectedRegistration);
            }
        } while (option != 0);
    }
}
