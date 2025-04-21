package bto.controller;

import java.util.Scanner;

import bto.model.application.ApplicationStatus;
import bto.model.application.BTOApplication;
import bto.model.project.BTOProject;
import bto.model.project.FlatType;
import bto.model.user.Applicant;
import bto.model.user.HDBManager;
import bto.model.user.User;
import bto.model.user.UserType;
import bto.ui.TerminalUtils;

public class ApplicationController {
    private ApplicationController(){} // Prevents Instantiation

    /**
     * Check if the user is an applicant and if he/she has already applied for the project.
     * @param user the user who wants to submit the application
     * @param project the project to which the application is submitted
     * 
     * @return true if the user can submit the application, false otherwise
     */
    public static boolean checkStatus(User user, BTOProject project){
        // Check if the project is null
        if (project == null) {
            System.out.println("Project not found.");
            return false;
        }
        // Check if the user is an applicant
        if (user.getUserType() == UserType.HDB_MANAGER) {
            System.out.println("Only applicants can submit applications.");
            return false;
        }

        // If is officer, check if application is for a project that officer is managing
        if (user.getUserType() == UserType.HDB_OFFICER) {
            if (project.getAssignedOfficers().contains(user.getName())) {
                System.out.println("Cannot submit application for a project you are managing.");
                return false;
            }
        }

        Applicant applicant = (Applicant) user;
        // Check if the user has already submitted an application for the project
        for (BTOApplication application : applicant.appliedProjects()) {
            if (application.getProject().getName().equals(project.getName())) {
                if (application.getStatus() == ApplicationStatus.PENDING) {
                    System.out.println("You have already submitted an application for this project.");
                }
                if (application.getStatus() == ApplicationStatus.SUCCESSFUL) {
                    System.out.println("You have already been successful in this project. You cannot apply again.");
                }
                if (application.getStatus() == ApplicationStatus.BOOKED) {
                    System.out.println("You have already booked a flat in this project. You cannot apply again.");
                }
                if (application.getStatus() == ApplicationStatus.UNSUCCESSFUL) {
                    System.out.println("You have already been unsuccessful in this project. You cannot apply again. Please apply for another project.");
                }
            return false;
            }
        }
        return true;
    }

    public static void selectFlatType(User user, BTOProject project){
        int option = -1;
        Scanner scanner = new Scanner(System.in);

        do{
            System.out.println("\n--- Project Application ---");
            System.out.println("Please enter the flat of choice - '2' for 2-room flat, '3' for 3-room flat or '0' to go back");
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
                    submitApplication(user, project, FlatType.TWO_ROOM);
                    return;
                case 3:
                    // Submit application for 3-room flat
                    submitApplication(user, project, FlatType.THREE_ROOM);
                    return;
                case 0:
                    // Goes back to ProjectDetails
                    TerminalUtils.clearScreen();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while(option != 0);  
    }

    /**
     * Submit an application to the system for a given user.
     * @param user the user who wants to submit the application
     * @param project the project to which the application is submitted
     * @param flatType the type of flat being applied for
     */
    public static void submitApplication(User user, BTOProject project, FlatType flatType) {
        Applicant applicant = (Applicant) user;
        // Check if project still has flats available for the given flat type
        if (project.getFlatCountRemaining(flatType) > 0) {
            try {
                applicant.submitApplication(project, flatType);
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("No more " + flatType.getDisplayName() + " flats available for this project.");
        }
    }

    /**
     * Withdraw an application from the system for a given user.
     * 
     * @param user the user who wants to withdraw the application
     * @param application the BTOApplication to be withdrawn
     */
    public static void withdrawApplication(User user, BTOApplication application) {
        // Check if the user is an applicant
        if (user.getUserType() != UserType.APPLICANT) {
            System.out.println("Only applicants can withdraw applications.");
            return;
        }

        // Check if the application belongs to the user
        if (application.getApplicant().getUserId() != user.getUserId()) {
            System.out.println("You can only withdraw your own applications.");
            return;
        }

        // Check if the application is not null and is in a state that allows withdrawal
        if (application != null && application.getStatus() != ApplicationStatus.UNSUCCESSFUL) {
            // Withdraw the application
            if(application.withdraw()){
                System.out.println("Application withdrawn successfully.");
            } else {
                System.out.println("There was an error withdrawing the application. Please try again.");
            }
        } else {
            System.out.println("Application not found / cannot be withdrawn at this stage.");
        }
    }

    /**
     * Book a flat for a given user.
     * 
     * @param user the officer who wants to book the flat
     * @param application the BTOApplication to be booked
     */
    public static void bookFlat(User user, BTOApplication application) {
        // Check if the user is an officer
        if (user.getUserType() != UserType.HDB_OFFICER) {
            System.out.println("Only HDB Officers can book flats.");
            return;
        }
        // Check if the application is not null and is in a state that allows booking
        if (application != null && application.getStatus() == ApplicationStatus.SUCCESSFUL) {
            // Book the flat
            if(application.getProject().getFlatCountRemaining(application.getFlatType()) > 0){
                try{
                    application.getProject().decreaseFlatCountRemaining(application.getFlatType());
                    application.setStatus(ApplicationStatus.BOOKED);
                } catch (IllegalArgumentException e){
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                System.out.println("No more " + application.getFlatType().getDisplayName() + " flats available for booking in " + application.getProject().getName() + ".");
            }
        } else {
            System.out.println("Application not found / cannot be booked at this stage.");
        }
    }

    /**
     * Withdraw a booked flat for a given user.
     * 
     * @param user the user who wants to withdraw the booking
     * @param application the BTOApplication to be withdrawn
     */
    public static void withdrawBooking(User user, BTOApplication application) {
        // Check if application is null
        if (application == null) {
            System.out.println("Application not found.");
            return;
        }
        // Check if the user is an Applicant
        if (user.getUserType() != UserType.APPLICANT) {
            System.out.println("Only Applicants can withdraw bookings.");
            return;
        }

        // Check if the application belongs to the user
        if (application.getApplicant().getUserId() != user.getUserId()) {
            System.out.println("You can only withdraw your own bookings.");
            return;
        }

        // Check if the application is in a state that allows cancellation
        if (application.getStatus() == ApplicationStatus.BOOKED) {
            // Cancel the booking
            if (application.unbook()){
                System.out.println("Booking cancelled successfully.");
            } else {
                System.out.println("There was an error cancelling the booking. Please try again.");
            }
        } else if (application.getStatus() == ApplicationStatus.SUCCESSFUL || application.getStatus() == ApplicationStatus.PENDING) {
            System.out.println("There is no booking to cancel. Please withdraw the application instead.");
        }
        else {
            System.out.println("Unknown application status. Cannot withdraw booking.");
        }
    }

    /**
     * Approve an application for a given user.
     * 
     * @param user the manager who wants to approve the application
     * @param application the BTOApplication to be approved
     */
    public static void approveApplication(User user, BTOApplication application) {
        // Check if the user is a manager
        if (user.getUserType() != UserType.HDB_MANAGER) {
            System.out.println("Only HDB Managers can approve applications.");
            return;
        }

        // Check if the application is not null and is in a state that allows approval
        if (application != null && application.getStatus() == ApplicationStatus.PENDING) {
            // Approve the application
            HDBManager manager = (HDBManager) user;
            try {
                manager.approveApplication(application);
                System.out.println("Application approved successfully.");
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Application not found / cannot be approved at this stage.");
        }
    }

    /**
     * Reject an application for a given user.
     * 
     * @param user the manager who wants to reject the application
     * @param application the BTOApplication to be rejected
     */
    public static void rejectApplication(User user, BTOApplication application) {
        // Check if the user is a manager
        if (user.getUserType() != UserType.HDB_MANAGER) {
            System.out.println("Only HDB Managers can reject applications.");
            return;
        }

        // Check if the application is not null and is in a state that allows rejection
        if (application != null && application.getStatus() == ApplicationStatus.PENDING) {
            // Reject the application
            HDBManager manager = (HDBManager) user;
            try {
                manager.rejectApplication(application);
                System.out.println("Application rejected successfully.");
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Application not found / cannot be rejected at this stage.");
        }
    }
}
