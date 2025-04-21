package bto.ui.enquiry;

import java.text.SimpleDateFormat;
import java.util.Scanner;

import bto.controller.ApplicationController;
import bto.controller.EnquiryController;
import bto.model.enquiry.Enquiry;
import bto.model.user.User;
import bto.model.user.UserType;

import bto.ui.TerminalUtils;

public class EnquiryDetails {
    private EnquiryDetails(){} // Prevents Instantiation

    public static void start(User user, Enquiry enquiry){
        int option = -1;
        Scanner scanner = new Scanner(System.in);

        TerminalUtils.clearScreen();
        do{
            System.out.print("\nProject Name: " + enquiry.getProjectName() + "\n" +
            "Enquiry: " + enquiry.getApplicantMessage().getMessage() + "\n\n" +
            "Reply: ");

            if(enquiry.getReplyMessage() != null){
                System.out.println(enquiry.getReplyMessage().getMessage());
            }
            else{
                System.out.println("No reply yet.");
            }

            // Menu for the user to select an option
            System.out.println("\n+---+---------------------+\n" +
                               "| # | Option              |\n" +
                               "+---+---------------------+");
            // Check if the user is a Applicant
            if (user.getUserType() == UserType.APPLICANT) {
                if (!enquiry.isSolved()){
                System.out.println("| 1 | Edit Enquiry        |");
                System.out.println("| 2 | Delete Enquiry      |");
                }
            }
            // Check if the user is a Officer / Manager
            else{
                System.out.println("| 1 | Add Reply           |");
            }

            System.out.println("| 0 | Go Back             |\n" +
                               "+---+---------------------+\n");

           System.out.print("Please select an option: ");
            
            try {
                option = scanner.nextInt(); // Read the user's choice
                scanner.nextLine(); // Consume the newline character
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
                continue; // Skip to the next iteration of the loop
            }
            
            switch (option) {
                case 1:
                    // Calls EnquiryController for applicants to edit their enquiry
                    if (user.getUserType() == UserType.APPLICANT) {
                        EnquiryController.editEnquiry(user, enquiry);
                    }
                    // Calls EnquiryController for officer/managers to reply to enquiry
                    else{
                        EnquiryController.replyToEnquiry(user, enquiry);
                    }
                    break;
                case 2:
                    // Sets the enquiry to solved
                    enquiry.deleteEnquiry();
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
