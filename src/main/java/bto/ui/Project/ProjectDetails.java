package bto.ui.project;

import java.util.Scanner;

import bto.controller.EnquiryController;
import bto.database.BTOProjectDB;
import bto.model.user.User;
import bto.model.user.UserType;
import bto.model.project.BTOProject;
import bto.ui.TerminalUtils;

public class ProjectDetails {
    private ProjectDetails(){} // Prevents Instantiation

    public static void showProjectDetails(User user, BTOProject project){
        int option = -1;
        Scanner scanner = new Scanner(System.in);

        TerminalUtils.clearScreen();
        do{
            BTOProjectDB.printBTOProjectDetails(project);
            // Menu for the user to select an option
            System.out.println("\n+---+----------------------------+\n" +
                               "| # | Option                     |\n" +
                               "+---+----------------------------+\n" +
                               "| 1 | Enquire about Project      |\n" +
                               "| 2 | Apply for Project          |");


            if (user.getUserType() == UserType.HDB_OFFICER) {
                System.out.println("| 3 | Register for Project       |");
            }

            if (user.getUserType() == UserType.HDB_MANAGER) {
                System.out.println("| 3 | Set Visibility             |");
            }

            System.out.println("| 0 | Go Back                    |\n" +
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
                    // Creates an enquiry
                    EnquiryController.createEnquiry(user, project);
                    break;
                case 2:
                    // Calls ProjectDashboard
                    ProjectDashboard.start(user);
                    break;
                case 3:
                    // Calls ApplicationDashboard
                    //ApplicationDashboard.start(user);
                    break;
                case 0:
                    // Goes back to ProjectDashboard
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while(option != 0);     
    }
}
