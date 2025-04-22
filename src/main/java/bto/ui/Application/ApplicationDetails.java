package bto.ui.application;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.text.SimpleDateFormat;

import bto.controller.ApplicationController;
import bto.model.application.BTOApplication;
import bto.model.user.User;
import bto.model.user.UserType;
import bto.ui.TerminalUtils;

public class ApplicationDetails {
    private ApplicationDetails(){} // Prevents Instantiation

    public static void start(User user, BTOApplication application){
        int option = -1;
        String userOption = "";
        Scanner scanner = new Scanner(System.in);

        TerminalUtils.clearScreen();
        do{
            System.out.println("\nProject Name: " + application.getProject().getName() + "\n" +
            "Neighbourhood: " + application.getProject().getNeighbourhood() + "\n" +
            "Applied Flat: " + application.getFlatType().getDisplayName() + "\n" +
            "Application Date: " + new SimpleDateFormat("dd/MM/yyyy").format(application.getApplicationDate()) + "\n\n" + 
            "Status: " + application.getStatus());

            if(application.getBookingDate() != null){
                System.out.println("Booking Date: " + new SimpleDateFormat("dd/MM/yyyy").format(application.getBookingDate()));
            }

            // Menu for the user to select an option
            System.out.println("\n+---+----------------------------+\n" +
                               "| # | Option                     |\n" +
                               "+---+----------------------------+");
            if (user.getUserType() == UserType.APPLICANT && application.getStatus().getStatus() != "Unsuccessful") {
                System.out.println("| 1 | Withdraw Application       |");
            } else if (user.getUserType() == UserType.HDB_MANAGER && application.getStatus().getStatus() == "Pending") {
                System.out.println("| 1 | Approve Application        |\n" +
                                   "| 2 | Reject Application         |");
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
                    do{
                        // If original status is "Unsuccessful", treat as invalid input
                        if (user.getUserType() == UserType.APPLICANT && application.getStatus().getStatus() == "Unsuccessful") {
                            System.out.println("Invalid option. Please try again.");
                            break;
                        }
                        if (user.getUserType() == UserType.HDB_MANAGER && application.getStatus().getStatus() != "Pending") {
                            System.out.println("Invalid option. Please try again.");
                            break;
                        }
                        if (user.getUserType() == UserType.APPLICANT){
                            System.out.println("Do you really want to withdraw your application? This step is IRREVERSIBLE. (Y for Yes, N for No)");
                        } else if (user.getUserType() == UserType.HDB_MANAGER) {
                            System.out.println("Do you really want to approve this application? (Y for Yes, N for No)");
                        }
                        System.out.print("Enter your choice: ");
                        userOption = scanner.nextLine();
                        if(userOption.toLowerCase().contains("y")){
                            if (user.getUserType() == UserType.APPLICANT) {
                                // Calls ApplicationController to withdraw application
                                switch (application.getStatus().getStatus()) {
                                    case "Pending":
                                    case "Successful":
                                        ApplicationController.withdrawApplication(user, application);
                                        break;
                                    case "Booked":
                                        ApplicationController.withdrawBooking(user, application);
                                        break;
                                }
                            } else if (user.getUserType() == UserType.HDB_MANAGER) {
                                // Calls ApplicationController to approve application
                                ApplicationController.approveApplication(user, application);
                            }
                            break;
                        }
                        else if(userOption.toLowerCase().contains("n")){
                            break;
                        }
                        else{
                            System.out.println("Invalid input. Please enter either Y or N.\n");
                            continue;
                        }
                    } while(!(userOption.toLowerCase().contains("y")) || !(userOption.toLowerCase().contains("n")));
                    break;
                case 2:
                    do {
                        // If original status is "Unsuccessful", treat as invalid input
                        if (application.getStatus().getStatus() == "Unsuccessful") {
                            System.out.println("Invalid option. Please try again.");
                            break;
                        }
                        if (user.getUserType() == UserType.HDB_MANAGER && application.getStatus().getStatus() != "Pending") {
                            System.out.println("Invalid option. Please try again.");
                            break;
                        }
                        if (user.getUserType() == UserType.HDB_MANAGER) {
                            System.out.println("Do you really want to reject this application? (Y for Yes, N for No)");
                        } else if (user.getUserType() == UserType.APPLICANT) {
                            // Treat as invalid input
                            System.out.println("Invalid option. Please try again.");
                            break;
                        }
                        System.out.print("Enter your choice: ");
                        userOption = scanner.nextLine();
                        if (userOption.toLowerCase().contains("y")) {
                            if (user.getUserType() == UserType.HDB_MANAGER) {
                                // Calls ApplicationController to approve application
                                ApplicationController.rejectApplication(user, application);
                            }
                            break;
                        } else if (userOption.toLowerCase().contains("n")) {
                            break;
                        } else {
                            System.out.println("Invalid input. Please enter either Y or N.\n");
                            continue;
                        }
                    } while (!(userOption.toLowerCase().contains("y")) || !(userOption.toLowerCase().contains("n")));
                    break;
                case 0:
                    // Goes back to ApplicationDashboard
                    TerminalUtils.clearScreen();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while(option != 0);     
    }
}
