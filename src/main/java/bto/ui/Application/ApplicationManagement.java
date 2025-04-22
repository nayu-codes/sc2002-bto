package bto.ui.application;

import bto.model.user.User;
import bto.ui.TerminalUtils;

import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

import bto.controller.ApplicationController;
import bto.controller.ReportController;
import bto.model.application.BTOApplication;

public class ApplicationManagement {
    private ApplicationManagement(){} // Prevents Instantiation

    public static void start(User user, BTOApplication application){
        int option = -1;
        String book = "";
        String receipt = "";
        Scanner scanner = new Scanner(System.in);

        TerminalUtils.clearScreen();
        do{
            System.out.println("\nApplicant Name: " + application.getApplicant().getName() + "\n" +
            "Applied Flat: " + application.getFlatType() + "\n" +
            "Application Date: " + new SimpleDateFormat("dd/MM/yyyy").format(application.getApplicationDate()) + "\n\n" + 
            "Status: " + application.getStatus());

            if(application.getBookingDate() != null){
                System.out.println("Booking Date: " + new SimpleDateFormat("dd/MM/yyyy").format(application.getBookingDate()));
            }

            // Menu for the user to select an option
            System.out.println("\n+---+----------------------------+\n" +
                               "| # | Option                     |\n" +
                               "+---+----------------------------+");
            if (application.getStatus().getStatus() == "Successful") {
                System.out.println("| 1 | Update status to Booked    |");
            }
            if (application.getStatus().getStatus() == "Booked") {
                System.out.println("| 1 | Generate receipt           |");
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
                    if(application.getStatus().getStatus() == "Successful"){
                        do{
                            System.out.println("Do you want to book applicant for this project? (Y for Yes, N for No)");
                            System.out.print("Enter your choice: ");
                            book = scanner.nextLine();
                            if(book.toLowerCase().contains("y")){
                                // Calls ApplicationController to change applicant status to "Booked"
                                ApplicationController.bookFlat(user, application);
                                break;
                            }
                            else if(book.toLowerCase().contains("n")){
                                break;
                            }
                            else{
                                System.out.println("Invalid input. Please enter either Y or N.\n");
                                continue;
                            }
                        }while(!(book.toLowerCase().contains("y")) || !(book.toLowerCase().contains("n")));
                    }
                    else if(application.getStatus().getStatus() == "Booked") {
                        do{
                            System.out.println("Do you want to generate receipt for this applicant? (Y for Yes, N for No)");
                            System.out.print("Enter your choice: ");
                            receipt = scanner.nextLine();
                            if(receipt.toLowerCase().contains("y")){
                                // Calls ApplicationController to change applicant status to "Booked"
                                ReportController.generateReceipt(user, application);
                                break;
                            }
                            else if(receipt.toLowerCase().contains("n")){
                                break;
                            }
                            else{
                                System.out.println("Invalid input. Please enter either Y or N.\n");
                                continue;
                            }
                        }while(!(receipt.toLowerCase().contains("y")) || !(receipt.toLowerCase().contains("n")));
                    }   
                    else{
                        System.out.println("Invalid option. Please try again.");
                        break;
                    }
                    break;
                case 0:
                    // Goes back to ProjectManagement
                    TerminalUtils.clearScreen();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while(option != 0);     
    }
}
