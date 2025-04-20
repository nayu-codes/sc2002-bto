package bto.ui.project;

import java.util.Scanner;

import bto.database.BTOProjectDB;
import bto.model.user.User;
import bto.model.user.UserType;
import bto.model.project.BTOProject;
import bto.ui.ChangePassword;
import bto.ui.TerminalUtils;

public class ProjectDetails {
    private ProjectDetails(){} // Prevents Instantiation

    public static void showProjectDetails(User user, BTOProject project){
        int option = -1;
        Scanner scanner = new Scanner(System.in);

        BTOProjectDB.printBTOProjectDetails(project);
        do{
            // Menu for the user to select an option
            System.out.println("+---+----------------------------+\n" +
                               "| # | Option                     |\n" +
                               "+---+----------------------------+\n" +
                               "| 1 | Enquire about Project      |\n" +
                               "| 2 | Apply for Project          |\n");


            if (user.getUserType() == UserType.HDB_OFFICER) {
                System.out.println("| 3 | Register for Project       |\n");
            }

            if (user.getUserType() == UserType.HDB_MANAGER) {
                System.out.println("| 3 | Set Visibility             |\n");
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
                    // Calls ChangePassword
                    ChangePassword.start(user);
                    break;
                case 2:
                    // Calls ProjectDashboard
                    ProjectDashboard.start(user);
                    break;
                case 3:
                    // Calls ApplicationDashboard
                    //ApplicationDashboard.start(user);
                    break;
                case 4:
                    // Calls EnquiryDashboard
                    //EnquiryDashboard.start(user);
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
