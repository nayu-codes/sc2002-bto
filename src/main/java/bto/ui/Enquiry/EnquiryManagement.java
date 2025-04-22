package bto.ui.enquiry;

import java.util.InputMismatchException;
import java.util.Scanner;

import bto.controller.ProjectController;
import bto.controller.RegistrationController;
import bto.database.BTOProjectDB;
import bto.database.EnquiryDB;
import bto.model.enquiry.Enquiry;
import bto.model.registration.OfficerRegistration;
import bto.model.registration.RegistrationStatus;
import bto.model.user.User;
import bto.ui.TerminalUtils;

public class EnquiryManagement {
    private EnquiryManagement(){} // Prevents Instantiation

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
                    System.out.println("\nThese are the applicants' enquiries for this project.");
                    System.out.println("-".repeat(71));
                    System.out.printf(" %5s | %15s | %30s | %10s\n", "Index", "Applicant Name", "Enquiry", "Reply");
                    System.out.println("-".repeat(71));

                    // Count for list indexing
                    int i = 1;
                    for(Enquiry enquiry:EnquiryDB.getEnquiriesByProject(registration.getProject())){
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

                        // Index| Applicant Name | Enquiry | Reply
                        System.out.printf("  %3d. | %15s | %30s | %10s\n", 
                        i, enquiry.getApplicantName(), enquiry.getApplicantMessage().getMessage(), reply);
                        i++;
                    };
                    System.out.println("-".repeat(71));
                    System.out.println("  Please enter the index of the enquiry you want to view more information about, or '0' to go back to the main menu.");
                    System.out.print("  Enter your choice: ");

                    try {
                        option = scanner.nextInt(); // Read the user's choice
                        scanner.nextLine();
                    } catch (InputMismatchException e) {
                        System.out.println("  Invalid input. Please enter a number.");
                        scanner.nextLine(); // Clear the invalid input
                        continue; // Skip to the next iteration of the loop
                    }

                    if (option < 0 || option > EnquiryDB.getEnquiriesByProject(registration.getProject()).size()) {
                        System.out.println("  Invalid choice. Please try again.");
                    } else if (option == 0) {
                        TerminalUtils.clearScreen();
                        return; // Exit the loop and return to the main menu
                    } else {
                        // Get enquiry details for the selected enquiry
                        Enquiry selectedEnquiry = EnquiryDB.getEnquiriesByProject(registration.getProject()).get(option - 1);

                        // Pass the selected enquiry to EnquiryDetails
                        EnquiryDetails.start(user, selectedEnquiry);
                    }
                } while(option != 0); 
            }
            else{
                System.out.println("You are currently not managing any projects and cannot see its enquiries.");
                System.out.println("Enter any key to go back to the main menu.");
                scanner.nextLine();
                TerminalUtils.clearScreen();
                return;
            }    
        }
        }
}
