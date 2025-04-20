package bto.ui.application;

import java.text.SimpleDateFormat;
import java.util.Scanner;

import bto.controller.ApplicationController;
import bto.model.application.BTOApplication;
import bto.model.user.User;
import bto.ui.TerminalUtils;

public class ApplicationDetails {
    private ApplicationDetails(){} // Prevents Instantiation

    public static void start(User user, BTOApplication application){
        int option = -1;
        String withdraw = "";
        Scanner scanner = new Scanner(System.in);

        TerminalUtils.clearScreen();
        do{
            System.out.println("\nProject Name: " + application.getProject().getName() + "\n" +
            "Neighbourhood: " + application.getProject().getNeighbourhood() + "\n" +
            "Applied Flat: " + application.getFlatType() + "\n" +
            "Application Data: " + new SimpleDateFormat("dd/MM/yyyy").format(application.getApplicationDate()) + "\n\n" + 
            "Status: " + application.getStatus());

            // Menu for the user to select an option
            System.out.println("\n+---+----------------------------+\n" +
                               "| # | Option                     |\n" +
                               "+---+----------------------------+");
            if (application.getStatus().getStatus() != "Unsuccessful") {
                System.out.print(
                             "| 1 | Withdraw Application       |\n");
            }
            System.out.print(
                               "| 0 | Go Back                    |\n" +
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
            
            switch (option) {
                case 1:
                    do{
                        // If original status is not "Unsuccessful", treat as invalid input
                        if (application.getStatus().getStatus() == "Unsuccessful") {
                            System.out.println("Invalid option. Please try again.");
                            break;
                        }
                        System.out.println("Do you really want to withdraw your application? This step is irreversible. (Y for Yes, N for No)");
                        System.out.print("Enter your choice: ");
                        withdraw = scanner.nextLine();
                        if(withdraw.toLowerCase().contains("y")){
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
                            break;
                        }
                        else if(withdraw.toLowerCase().contains("n")){
                            break;
                        }
                        else{
                            System.out.println("Invalid input. Please enter either Y or N.\n");
                            continue;
                        }
                    }while(!(withdraw.toLowerCase().contains("y")) || !(withdraw.toLowerCase().contains("n")));
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
