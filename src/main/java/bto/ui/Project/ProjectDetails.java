package bto.ui.project;

import java.util.Scanner;
import java.util.InputMismatchException;

import bto.controller.ApplicationController;
import bto.controller.ProjectController;
import bto.controller.RegistrationController;
import bto.controller.EnquiryController;
import bto.database.BTOProjectDB;
import bto.model.user.HDBManager;
import bto.model.user.User;
import bto.model.user.UserType;
import bto.model.project.BTOProject;

import bto.ui.TerminalUtils;

public class ProjectDetails {
    private ProjectDetails(){} // Prevents Instantiation

    /**
     * Displays the project details for a selected project.
     * 
     * @param user The user whose project details is to be displayed.
     * @param project The selected project which details is to be displayed.
     */
    public static void start(User user, BTOProject project){
        int option = -1;
        String register = "";
        String visible = "";
        Scanner scanner = new Scanner(System.in);

        TerminalUtils.clearScreen();
        do{
            BTOProjectDB.printBTOProjectDetails(user, project);
            // Menu for the user to select an option
            System.out.println("\n+---+----------------------------+\n" +
                               "| # | Option                     |\n" +
                               "+---+----------------------------+");
            // Check if the user is not a manager
            if (user.getUserType() != UserType.HDB_MANAGER) {            
                System.out.println("| 1 | Apply for Project          |\n" +
                                   "| 2 | Enquire about Project      |");
            }
            // Check if the user is an officer
            if (user.getUserType() == UserType.HDB_OFFICER) {
                System.out.println("| 3 | Register for Project       |");
            }
            // Check if the user is a manager
            if (user.getUserType() == UserType.HDB_MANAGER && project.getProjectManager().getName().equals(user.getName())) {
                System.out.println("| 1 | Set Visibility             |");
                System.out.println("| 2 | Edit Project               |");
                System.out.println("| 3 | Delete Project             |");
            }

            System.out.println("| 0 | Go Back                    |\n" +
                               "+---+----------------------------+\n");

           System.out.print("Please select an option: ");
            
            try {
                option = scanner.nextInt(); // Read the user's choice
                scanner.nextLine(); // Consume the newline character
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
                continue; // Skip to the next iteration of the loop
            }
            
            switch (option) {
                case 1:
                    if(user.getUserType() != UserType.HDB_MANAGER){
                        // Calls ApplicationController to submit an application with the right conditions
                        if(ApplicationController.checkStatus(user, project)){
                            ApplicationController.selectFlatType(user, project);
                        }
                    } else if (user.getUserType() == UserType.HDB_MANAGER && project.getProjectManager().getName().equals(user.getName())){
                        do{
                            System.out.println("Do you want to toggle the visibility of this project? (Y for Yes, N for No)");
                            System.out.print("Enter your choice: ");
                            visible = scanner.nextLine();
                            if(visible.toLowerCase().contains("y")){
                                // Calls ProjectController to toggle visibility
                                ProjectController.toggleVisibility(user, project);
                                break;
                            }
                            else if(visible.toLowerCase().contains("n")){
                                break;
                            }
                            else{
                                System.out.println("Invalid input. Please enter either Y or N.\n");
                                continue;
                            }
                        }while(!(visible.toLowerCase().contains("y")) || !(visible.toLowerCase().contains("n")));
                    } else{
                        System.out.println("Invalid option. Please try again.");
                        break;
                    }
                    break;
                case 2:
                    if(user.getUserType() != UserType.HDB_MANAGER){
                        // Creates an enquiry
                        EnquiryController.createEnquiry(user, project);
                    }
                    else if (user.getUserType() == UserType.HDB_MANAGER && project.getProjectManager().getName().equals(user.getName())) {
                        // Go to ProjectCreatorWizard to edit project
                        ProjectCreatorWizard.editProject((HDBManager) user, project);
                    }
                    else{
                        System.out.println("Invalid option. Please try again.");
                        break;
                    }
                    break;
                case 3:
                    if(user.getUserType() == UserType.HDB_OFFICER){
                        do{
                            System.out.println("Do you want to apply as an officer for this project?. (Y for Yes, N for No)");
                            System.out.print("Enter your choice: ");
                            register = scanner.nextLine();
                            if(register.toLowerCase().contains("y")){
                                // Calls RegistrationController to submit officer registration for project
                                RegistrationController.registerForProject(user, project);
                                break;
                            }
                            else if(register.toLowerCase().contains("n")){
                                break;
                            }
                            else{
                                System.out.println("Invalid input. Please enter either Y or N.\n");
                                continue;
                            }
                        }while(!(register.toLowerCase().contains("y")) || !(register.toLowerCase().contains("n")));
                    }
                    else if (user.getUserType() == UserType.HDB_MANAGER && project.getProjectManager().getName().equals(user.getName())) {
                        // Go to ProjectCreatorWizard to delete project
                        ProjectCreatorWizard.deleteProject((HDBManager) user, project);
                    }
                    else{
                        System.out.println("Invalid option. Please try again.");
                        break;
                    }
                    break;
                case 0:
                    // Goes back to ProjectDashboard
                    TerminalUtils.clearScreen();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while(option != 0);     
    }
}
