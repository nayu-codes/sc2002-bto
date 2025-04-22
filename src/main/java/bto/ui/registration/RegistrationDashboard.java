package bto.ui.registration;

import java.text.SimpleDateFormat;
import java.util.Scanner;

import bto.model.user.User;
import bto.model.user.HDBOfficer;
import bto.model.user.HDBManager;
import bto.controller.ProjectController;
import bto.database.RegistrationDB;
import bto.model.registration.OfficerRegistration;
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
}
