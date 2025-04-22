package bto.ui.project;

import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

import bto.controller.ProjectController;
import bto.controller.RegistrationController;
import bto.database.ApplicationDB;
import bto.database.BTOProjectDB;
import bto.model.application.BTOApplication;
import bto.model.registration.OfficerRegistration;
import bto.model.registration.RegistrationStatus;
import bto.model.user.User;
import bto.ui.TerminalUtils;
import bto.ui.application.ApplicationManagement;

public class ProjectManagement {
    private ProjectManagement(){} // Prevents Instantiation

    public static void start(User user){
        int option = -1;
        Scanner scanner = new Scanner(System.in);
        
        TerminalUtils.clearScreen();
        for (OfficerRegistration registration : RegistrationController.getRegistrationsByOfficer(user)){
            if ((registration.getRegistrationStatus() == RegistrationStatus.SUCCESSFUL) && (ProjectController.getProjectStatus(registration.getProject()) == "Current")){
                do{
                    // Printing the managed project details
                    System.out.print("\n---------- Managed project ----------");
                    BTOProjectDB.printBTOProjectDetails(user, registration.getProject());
                    System.out.println("-".repeat(37));

                    // Printing the project applicants
                    System.out.println("\nThese are the applicants for this project.");
                    System.out.println("-".repeat(61));
                    System.out.printf(" %5s | %15s | %15s | %15s\n", "Index", "Applicant Name", "Application Date", "Status");
                    System.out.println("-".repeat(61));

                    // Count for list indexing
                    int i = 1;
                    for(BTOApplication application:ApplicationDB.getApplicationsByProjectName(registration.getProject().getName())){
                        // Index| Applicant Name | Application Date | Status
                        System.out.printf("  %3d. | %15s | %15s  | %15s\n", 
                        i, application.getApplicant().getName(),
                        new SimpleDateFormat("dd/MM/yyyy").format(application.getApplicationDate()),
                        application.getStatus());
                        i++;
                    };
                    System.out.println("-".repeat(61));
                    System.out.println("  Please enter the index of the applicant you want to view more information about, or '0' to go back to the main menu.");
                    System.out.print("  Enter your choice: ");

                    try {
                        option = scanner.nextInt(); // Read the user's choice
                        scanner.nextLine();
                    } catch (InputMismatchException e) {
                        System.out.println("  Invalid input. Please enter a number.");
                        scanner.nextLine(); // Clear the invalid input
                        continue; // Skip to the next iteration of the loop
                    }

                    if (option < 0 || option > ApplicationDB.getApplicationsByProjectName(registration.getProject().getName()).size()) {
                        System.out.println("  Invalid choice. Please try again.");
                    } else if (option == 0) {
                        TerminalUtils.clearScreen();
                        return; // Exit the loop and return to the main menu
                    } else {
                        // Get application details for the selected applicant
                        BTOApplication selectedApplication = ApplicationDB.getApplicationsByProjectName(registration.getProject().getName()).get(option - 1);

                        // Pass the selected applicant to the ProjectDetailsScreen
                        ApplicationManagement.start(user, selectedApplication);
                    }
                } while(option != 0); 
            }
            else{
                System.out.println("You are currently not managing any projects.");
                System.out.println("Enter any key to go back to the main menu.");
                scanner.nextLine();
                TerminalUtils.clearScreen();
                return;
            }    
        }
    }
}
