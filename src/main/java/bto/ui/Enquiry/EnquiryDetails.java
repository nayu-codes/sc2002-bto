package bto.ui.enquiry;

import java.util.Scanner;
import java.util.InputMismatchException;

import bto.controller.EnquiryController;
import bto.model.enquiry.Enquiry;
import bto.model.user.User;

import bto.ui.TerminalUtils;

public class EnquiryDetails {
    private EnquiryDetails(){} // Prevents Instantiation

    public static void start(User user, Enquiry enquiry){
        int option = -1;
        String delete = "";
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
                System.out.println("| 1 | Add Reply           |");
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
                    }
                    // Calls EnquiryController for officer/managers to reply to enquiry
                    else if(enquiry.getReplyMessage() == null){
                        EnquiryController.replyToEnquiry(user, enquiry);
                    }
                    break;
                case 2:
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
