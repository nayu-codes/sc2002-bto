package bto.controller;

import bto.model.application.ApplicationStatus;
import bto.model.application.BTOApplication;
import bto.model.user.HDBManager;
import bto.model.user.User;
import bto.model.user.UserType;

public class ApplicationController {
    public ApplicationController() {
        // Constructor for the ApplicationController class
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

        // Check if the application is not null and is in a state that allows cancellation
        if (application.getStatus() == ApplicationStatus.BOOKED) {
            // Cancel the booking
            try {
                application.getProject().increaseFlatCountRemaining(application.getFlatType());
                application.setStatus(ApplicationStatus.UNSUCCESSFUL);
                System.out.println("Booking cancelled successfully.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
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
}
