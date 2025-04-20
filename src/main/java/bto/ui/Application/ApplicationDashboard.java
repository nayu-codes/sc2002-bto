package bto.ui.application;

import java.util.Scanner;

import bto.controller.ApplicationController;
import bto.model.project.BTOProject;
import bto.model.project.FlatType;
import bto.model.user.User;

public class ApplicationDashboard {
    private ApplicationDashboard(){} // Prevents Instantiation

    public static void selectFlatType(User user, BTOProject project){
        int option = -1;
        Scanner scanner = new Scanner(System.in);

        do{
            System.out.println("\n--- Project Application ---");
            System.out.println("Please enter the flat of choice - '2' for 2-room Flat, '3' for 3-room Flat, '0' to go back");
            System.out.print("Enter your choice: ");
            
            try {
                option = scanner.nextInt(); // Read the user's choice
                scanner.nextLine(); // Consume the newline character
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
                continue; // Skip to the next iteration of the loop
            }

            switch (option) {
                case 2:
                    // Submit application for 2-room flat
                    ApplicationController.submitApplication(user, project, FlatType.TWO_ROOM);
                    break;
                case 3:
                    // Submit application for 3-room flat
                    ApplicationController.submitApplication(user, project, FlatType.THREE_ROOM);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while(option != 2 || option != 3);  
    }
}