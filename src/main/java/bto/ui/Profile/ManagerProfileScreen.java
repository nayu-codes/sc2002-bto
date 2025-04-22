package bto.ui.profile;

import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

import bto.controller.ProjectController;
import bto.database.BTOProjectDB;
import bto.model.project.BTOProject;
import bto.model.user.HDBManager;
import bto.model.user.User;
import bto.ui.ChangePassword;
import bto.ui.TerminalUtils;
import bto.ui.application.ApplicationDashboard;
import bto.ui.enquiry.EnquiryDashboard;
import bto.ui.project.ProjectDashboard;
import bto.ui.project.ProjectCreatorWizard;
import bto.ui.registration.RegistrationDashboard;
import bto.ui.report.ReportDashboard;

public class ManagerProfileScreen {
    private ManagerProfileScreen() {}

    public static void start(User user){
        int option = -1;
        Scanner scanner = new Scanner(System.in);

        TerminalUtils.clearScreen();
        do{
            // If login successful, welcome the user
            System.out.print("\nLogin successful! Welcome, " + user.getName() + ".\n"+
                               "HDB Manager for: ");

            // Downcast user to Manager to access getBTOProjectsByManager method
            HDBManager manager = (HDBManager) user;

            if(BTOProjectDB.getBTOProjectsByManager(manager) != null){
                for (BTOProject project : BTOProjectDB.getBTOProjectsByManager(manager)){
                    if (ProjectController.getProjectStatus(project) == "Current"){
                        // Project Name (openingDate - ClosingDate)
                        System.out.print(project.getName() + " (" + 
                                           new SimpleDateFormat("dd/MM/yyyy").format(project.getApplicationOpeningDate()) + " - " +
                                           new SimpleDateFormat("dd/MM/yyyy").format(project.getApplicationClosingDate()) + ")\n"
                                           );
                    }
                }
            }

            // Menu for the user to select an option
            System.out.println("+---+----------------------------------+\n" +
                               "| # | Option                           |\n" +
                               "+---+----------------------------------+\n" +                       
                               "| 1 | Change Password                  |\n" +
                               "| 2 | View All Projects                |\n" +
                               "| 3 | Create New Project               |\n" +
                               "| 4 | Edit / Delete A Project          |\n" +
                               "| 5 | Officer Registration Management  |\n" + 
                               "| 6 | BTO Application Management       |\n" +
                               "| 7 | Enquiry Management               |\n" +
                               "| 8 | Generate Booking Reports         |\n" +
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
                    // Calls ProjectDashboard to view all projects
                    ProjectDashboard.start(user);
                    break;
                case 3:
                    // Calls ProjectCreatorWizard to create a new project
                    ProjectCreatorWizard.start(user);
                    break;
                case 4:
                    // Calls ProjectCreatorWizard to edit/delete a project
                    //ProjectCreatorWizard.start(user);
                    break;
                case 5:
                    // Calls RegistrationDashboard to view all registrations
                    RegistrationDashboard.managerView(manager);
                    break;
                case 6:
                    // Calls ApplicationDashboard as a manager
                    ApplicationDashboard.managerView(manager);                    
                    break;
                case 7:
                    // Calls EnquiryDashboard to view all enquiries
                    EnquiryDashboard.managerView(manager);
                    break;
                case 8:
                    // Calls ReportDashboard to generate booking reports
                    ReportDashboard.start(manager);
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
