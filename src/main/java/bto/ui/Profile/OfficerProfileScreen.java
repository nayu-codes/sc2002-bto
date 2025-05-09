package bto.ui.profile;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.text.SimpleDateFormat;

import bto.controller.ProjectController;
import bto.controller.RegistrationController;
import bto.model.user.User;
import bto.model.registration.OfficerRegistration;
import bto.model.registration.RegistrationStatus;
import bto.ui.TerminalUtils;
import bto.ui.ChangePassword;
import bto.ui.project.ProjectDashboard;
import bto.ui.application.ApplicationDashboard;
import bto.ui.enquiry.EnquiryDashboard;
import bto.ui.registration.RegistrationDashboard;
import bto.ui.project.ProjectManagement;
import bto.ui.enquiry.EnquiryManagement;

public class OfficerProfileScreen {
    public OfficerProfileScreen() {} // Prevents instantiation

    /**
     * Displays the profile screen for a HDB officer
     * 
     * @param user a HDB officer
     */
    public static void start(User user){
        int option = -1;
        Scanner scanner = new Scanner(System.in);

        TerminalUtils.clearScreen();
        do{
            // If login successful, welcome the user
            System.out.print("\nLogin successful! Welcome, " + user.getName() + ".\n" +
                               "HDB Officer for: ");

            if(!RegistrationController.getRegistrationsByOfficer(user).isEmpty()){
                for (OfficerRegistration registration : RegistrationController.getRegistrationsByOfficer(user)){
                    if ((registration.getRegistrationStatus() == RegistrationStatus.SUCCESSFUL) && (ProjectController.getProjectStatus(registration.getProject()) == "Current")){
                        // Project Name (openingDate - ClosingDate)
                        System.out.print(registration.getProject().getName() + " (" + 
                                           new SimpleDateFormat("dd/MM/yyyy").format(registration.getProject().getApplicationOpeningDate()) + " - " +
                                           new SimpleDateFormat("dd/MM/yyyy").format(registration.getProject().getApplicationClosingDate()) + ")\n"
                                           );
                    }else{
                        System.out.println();
                        break;
                    }
                }
            }else{
                System.out.println();
            }

            // Menu for the user to select an option
            System.out.println("+---+----------------------------------+\n" +
                               "| # | Option                           |\n" +
                               "+---+----------------------------------+\n" +                       
                               "| 1 | Change Password                  |\n" +
                               "+---+----------------------------------+\n" +
                               "| As an applicant                      |\n" +    
                               "+---+----------------------------------+\n" +   
                               "| 2 | View Available Projects          |\n" +
                               "| 3 | View Application(s)              |\n" +
                               "| 4 | View Enquiries                   |\n" +
                               "+---+----------------------------------+\n" +
                               "| As a HDB Officer                     |\n" +    
                               "+---+----------------------------------+\n" +  
                               "| 5 | View Registration Status         |\n" +
                               "| 6 | Project Application Management   |\n" +
                               "| 7 | Enquiry Management               |\n" +
                               "+---+----------------------------------+\n" +
                               "| 0 | Log Out                          |\n" +
                               "+---+----------------------------------+\n");

            System.out.print("Please select an option: ");
            
            try {
                option = scanner.nextInt(); // Read the user's choice
                scanner.nextLine(); // Consume the newline character
            } catch (InputMismatchException e) {
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
                case 5:
                    // Calls RegistrationDashboard to view registered projects
                    RegistrationDashboard.start(user);
                    break;
                case 6:
                    // Calls ProjectManagement to view managed project
                    ProjectManagement.start(user);
                    break;
                case 7:
                    // Calls ProjectManagement to view managed project
                    EnquiryManagement.start(user);
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
