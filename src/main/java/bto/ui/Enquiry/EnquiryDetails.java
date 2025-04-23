package bto.ui.enquiry;

import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

import bto.controller.EnquiryController;
import bto.database.BTOProjectDB;
import bto.model.enquiry.Enquiry;
import bto.model.project.BTOProject;
import bto.model.user.HDBManager;
import bto.model.user.User;
import bto.model.user.UserType;
import bto.ui.TerminalUtils;

public class EnquiryDetails {
    private EnquiryDetails(){} // Prevents Instantiation

    /**
     * Displays the enquiry details for a selected enquiry.
     * 
     * @param user The user whose enquiry details is to be displayed.
     * @param enquiry The selected enquiry which details is to be displayed.
     */    
    public static void start(User user, Enquiry enquiry){
        int option = -1;
        String delete = "";
        boolean isManaged = false;
        Scanner scanner = new Scanner(System.in);

        TerminalUtils.clearScreen();
        do{
            System.out.print("\nProject Name: " + enquiry.getProjectName() + "\n" +
            "Enquiry: " + enquiry.getApplicantMessage().getMessage() + "\n\n");

            if(enquiry.getReplyMessage() != null){
                // If there is a reply message, display it
                if(!enquiry.getReplyMessage().getAuthorName().isEmpty()){
                    System.out.println("Replied by: " + enquiry.getReplyMessage().getAuthorName() + "\n" +
                                    "Reply: " + enquiry.getReplyMessage().getMessage());
                }
                // If the user deletes the message
                else if((enquiry.getReplyMessage().getAuthorName().isEmpty()) && (enquiry.isSolved())){
                    System.out.println("Enquiry is deleted.");
                }
            }
            // If there is no reply message
            else{
                System.out.println("Reply: No reply yet.");
            }

            if(user.getUserType() == UserType.HDB_MANAGER){
                HDBManager manager = (HDBManager) user;

                List<BTOProject> managedProjects = BTOProjectDB.getBTOProjectsByManager(manager);
                for (BTOProject project : managedProjects){
                    if(project.getName().equals(enquiry.getProjectName())){
                        isManaged = true;
                        break;
                    }
                }
            }

            // Menu for the user to select an option
            System.out.println("\n+---+---------------------+\n" +
                               "| # | Option              |\n" +
                               "+---+---------------------+");
            // Check if the user is a Applicant
            if (user.getName().equals(enquiry.getApplicantName())) {
                if (!enquiry.isSolved()){
                System.out.println("| 1 | Edit Enquiry        |");
                System.out.println("| 2 | Delete Enquiry      |");
                }
            }
            // Check if the user is a Officer / Manager
            else if(enquiry.getReplyMessage() == null){
                if(user.getUserType() == UserType.HDB_OFFICER){
                    System.out.println("| 1 | Add Reply           |");
                }else if((user.getUserType() == UserType.HDB_MANAGER) && isManaged){
                    System.out.println("| 1 | Add Reply           |");
                }
            }

            System.out.println("| 0 | Go Back             |\n" +
                               "+---+---------------------+\n");

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
                    // Calls EnquiryController for applicants to edit their enquiry
                    if (user.getName().equals(enquiry.getApplicantName())) {
                        EnquiryController.editEnquiry(user, enquiry);
                        break;
                    }
                    // Calls EnquiryController for officer/managers to reply to enquiry
                    else if(enquiry.getReplyMessage() == null){
                        if(user.getUserType() == UserType.HDB_OFFICER){
                            EnquiryController.replyToEnquiry(user, enquiry);
                        }else if((user.getUserType() == UserType.HDB_MANAGER) && isManaged){
                            EnquiryController.replyToEnquiry(user, enquiry);
                        }
                        else{
                            System.out.println("Invalid option. Please try again.");
                            break;
                        }
                    }else{
                        System.out.println("Invalid option. Please try again.");
                        break;
                    }
                    break;
                case 2:
                    if (user.getName().equals(enquiry.getApplicantName())) {
                        do{
                            System.out.println("Do you really want to delete your enquiry? This step is IRREVERSIBLE. (Y for Yes, N for No)");
                            System.out.print("Enter your choice: ");
                            delete = scanner.nextLine();
                            if(delete.toLowerCase().contains("y")){
                                // Deletes enquiry (set it to solved)
                                enquiry.deleteEnquiry();
                                System.out.println("Enquiry Deleted!");
                                break;
                            }
                            else if(delete.toLowerCase().contains("n")){
                                break;
                            }
                            else{
                                System.out.println("Invalid input. Please enter either Y or N.\n");
                                continue;
                            }
                        }while(!(delete.toLowerCase().contains("y")) || !(delete.toLowerCase().contains("n")));
                    }else{
                        System.out.println("Invalid option. Please try again.");
                        break;
                    }
                case 0:
                    // Goes back to EnquiryDashboard
                    TerminalUtils.clearScreen();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while(option != 0);     
    }
}
