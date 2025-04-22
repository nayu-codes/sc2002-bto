package bto.ui.registration;

import bto.controller.RegistrationController;
import bto.model.registration.OfficerRegistration;
import bto.model.registration.RegistrationStatus;
import bto.model.user.HDBManager;
import bto.ui.TerminalUtils;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.text.SimpleDateFormat;

public class RegistrationDetails {
    private RegistrationDetails(){} // Prevents Instantiation
    
    /**
    * Displays the registration details for the user.
    * This method is called when the user selects the option to view their registration details.
    * 
    * @param manager the HDB Manager user whose registration details are to be displayed
    */
    public static void start(HDBManager manager, OfficerRegistration registration){
        Scanner scanner = new Scanner(System.in);

        int option = -1;
        String confirm = "";
        
        TerminalUtils.clearScreen();
        
        do{
            System.out.println(" \nRegistration Request Details");
            System.out.println("Officer Name: " + registration.getOfficer().getName());
            System.out.println("Project Name: " + registration.getProject().getName());
            System.out.println("Project Application Period: " + 
                new SimpleDateFormat("dd/MM/yyyy").format(registration.getProject().getApplicationOpeningDate()) + " - " +
                new SimpleDateFormat("dd/MM/yyyy").format(registration.getProject().getApplicationClosingDate()));
            System.out.println("Registration Status: " + registration.getRegistrationStatus());
            System.out.println("-".repeat(87)); 
            // Menu for the user to select an option
            System.out.println("\n+---+------------------------------+\n" +
                                 "| # | Option                       |\n" +
                                 "+---+------------------------------+");
            if (registration.getRegistrationStatus() == RegistrationStatus.PENDING) {
                System.out.println("| 1 | Approve Registration Request |\n" +
                                   "| 2 | Reject Registration Request  |");
            }
            System.out.println("| 0 | Go Back                      |\n" +
                               "+---+------------------------------+\n");

           System.out.print("Please select an option: ");
            
            try {
                option = scanner.nextInt(); // Read the user's choice
                scanner.nextLine(); // Consume the newline character
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
                continue; // Skip to the next iteration of the loop
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                continue; // Skip to the next iteration of the loop
            }
            
            switch (option) {
                case 1:
                    // Asks for confirmation before approving the registration request
                    do {
                        System.out.println("Are you sure you want to approve this registration request? (Y for Yes, N for No)");
                        System.out.print("Enter your choice: ");
                        confirm = scanner.nextLine();
                        if(confirm.toLowerCase().contains("y")){
                            // Calls RegistrationController to approve the registration request
                            RegistrationController.approveRegistration(manager, registration);
                            break;
                        }
                        else if(confirm.toLowerCase().contains("n")){
                            break;
                        }
                        else{
                            System.out.println("Invalid input. Please enter either Y or N.\n");
                            continue;
                        }
                    } while(!(confirm.toLowerCase().contains("y")) || !(confirm.toLowerCase().contains("n")));
                    break;
                case 2:
                    // Asks for confirmation before rejecting the registration request
                    do {
                        System.out.println("Are you sure you want to reject this registration request? (Y for Yes, N for No)");
                        System.out.print("Enter your choice: ");
                        confirm = scanner.nextLine();
                        if(confirm.toLowerCase().contains("y")){
                            // Calls RegistrationController to reject the registration request
                            RegistrationController.rejectRegistration(manager, registration);
                            break;
                        }
                        else if(confirm.toLowerCase().contains("n")){
                            break;
                        }
                        else{
                            System.out.println("Invalid input. Please enter either Y or N.\n");
                            continue;
                        }
                    } while(!(confirm.toLowerCase().contains("y")) || !(confirm.toLowerCase().contains("n")));
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
