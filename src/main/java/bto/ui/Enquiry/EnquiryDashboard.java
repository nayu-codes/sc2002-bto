package bto.ui.enquiry;

import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

import bto.controller.EnquiryController;
import bto.database.EnquiryDB;
import bto.model.enquiry.Enquiry;
import bto.model.user.HDBManager;
import bto.model.user.User;
import bto.ui.TerminalUtils;

public class EnquiryDashboard {
    private EnquiryDashboard(){} // Prevents Instantiation

    /**
     * Displays the enquiry dashboard for the user.
     * This method is called when the user selects the option to view their sent enquiries.
     * 
     * @param user the user (Applicant/HDB Officer) whose sent enquiries are to be displayed
     */
    public static void start(User user){
        int choice = -1; // Initialize choice to an invalid value
        Scanner scanner = new Scanner(System.in);

        TerminalUtils.clearScreen();
        do{
            List<Enquiry> applicantEnquiries = EnquiryDB.getEnquiriesByApplicant(user);
            System.out.println(" \nSent enquiries by: " + user.getName());
            System.out.println("-".repeat(72));
            System.out.printf(" %5s | %15s | %30s | %10s\n", "Index", "Project Name", "Enquiry", "Reply");
            System.out.println("-".repeat(72));

            // Check if the user has sent any enquiries
            if (applicantEnquiries.isEmpty()) {
                System.out.println("You have not sent any enquiries.");
                System.out.println("Enter any key to go back to the main menu.");
                scanner.nextLine();
                TerminalUtils.clearScreen();
                return;
            }
            else{
                // Count for list indexing
                int i = 1;

                // Print each applied project
                for (Enquiry enquiry : applicantEnquiries) {
                    String reply = "No";
                    
                    if(enquiry.getReplyMessage() != null){
                        // Check if there is a reply
                        if(!enquiry.getReplyMessage().getAuthorName().isEmpty()){
                            reply = "Yes";
                        }
                        // Shows "Deleted" if the user deletes the enquiry
                        else if ((enquiry.getReplyMessage().getAuthorName().isEmpty()) && (enquiry.isSolved())){
                            reply = "Deleted";
                        }
                    }

                    // Print in table format, with consistent spacing
                    // i | Project Name | Enquiry | Reply
                    System.out.printf("  %3d. | %15s | %30s | %10s\n", 
                        i, enquiry.getProjectName(), enquiry.getApplicantMessage().getMessage(), reply);
                    i++;
                };
                System.out.println("-".repeat(72));
                System.out.println("  Please enter the index of the enquiry you want to view more information about, or '0' to go back to the main menu.");
                System.out.print("  Enter your choice: ");

                try {
                    choice = scanner.nextInt(); // Read the user's choice
                    scanner.nextLine();
                } catch (InputMismatchException e) {
                    System.out.println("  Invalid input. Please enter a number.");
                    scanner.nextLine(); // Clear the invalid input
                    continue; // Skip to the next iteration of the loop
                }

                if (choice < 0 || choice > applicantEnquiries.size()) {
                    System.out.println("  Invalid choice. Please try again.");
                } else if (choice == 0) {
                    TerminalUtils.clearScreen();
                    return; // Exit the loop and return to the main menu
                } else {
                    // Get enquiry details for the selected enquiry
                    Enquiry selectedEnquiry = applicantEnquiries.get(choice - 1);

                    // Pass the selected enquiry to EnquiryDetails
                    EnquiryDetails.start(user, selectedEnquiry);
                }
            }
        }while(choice != 0); // Continue until the user chooses to go back to the main menu
    }

    /**
     * Displays the enquiry dashboard for a HDB manager.
     * Displays enquiries of all projects while being able to reply to enquiries of managed project
     * 
     * @param manager the HDB manager
     */
    public static void managerView(HDBManager manager) {
        int choice = -1;
        Scanner scanner = new Scanner(System.in);

        TerminalUtils.clearScreen();
        do {
            System.out.println(" \nBTO Enquiry Portal");
            System.out.println("-".repeat(110));
            System.out.printf(" %5s | %15s | %15s | %30s | %30s\n", "Index", "Applicant Name", "Project Name", "Enquiry", "Reply Message");
            System.out.println("-".repeat(110));

            int i = 1;
            for (Enquiry enquiry : EnquiryController.getAllEnquiries()) {
                String replyMessage = "Not replied yet";
                    
                if(enquiry.getReplyMessage() != null){
                    // Check if there is a reply
                    if(!enquiry.getReplyMessage().getAuthorName().isEmpty()){
                        replyMessage = enquiry.getReplyMessage().getMessage();
                    }
                    // Shows "Deleted" if the user deletes the enquiry
                    else if ((enquiry.getReplyMessage().getAuthorName().isEmpty()) && (enquiry.isSolved())){
                        replyMessage = "Enquiry was deleted";
                    }
                }
                // Print in table format, with consistent spacing
                // i | Applicant Name | Project Name | Enquiry | Reply Message
                System.out.printf("  %3d. | %15s | %15s |  %29s | %30s\n",
                        i, enquiry.getApplicantName(), enquiry.getProjectName(), enquiry.getApplicantMessage().getMessage(), replyMessage);
                i++;
            }
            System.out.println("-".repeat(110));
            System.out.println("  Please enter the index of the enquiry you want to view more information about, or '0' to go back to the main menu.");
            System.out.print("  Enter your choice: ");

            try {
                choice = scanner.nextInt(); // Read the user's choice
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("  Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
                continue; // Skip to the next iteration of the loop
            }

            if (choice < 0 || choice > EnquiryController.getAllEnquiries().size()) {
                System.out.println("  Invalid choice. Please try again.");
            } else if (choice == 0) {
                TerminalUtils.clearScreen();
                return; // Exit the loop and return to the main menu
            } else {
                // Get enquiry details for the selected enquiry
                Enquiry selectedEnquiry = EnquiryController.getAllEnquiries().get(choice - 1);

                // Pass the selected enquiry to EnquiryDetails
                EnquiryDetails.start(manager, selectedEnquiry);
            }
        } while (choice != 0); // Continue until the user chooses to go back to the main menu
    }
}