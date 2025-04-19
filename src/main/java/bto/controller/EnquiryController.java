package bto.controller;

import bto.model.enquiry.Enquiry;
import bto.model.user.*;
import bto.model.project.BTOProject;

import java.util.Scanner;

public class EnquiryController {
    /**
     * Constructor for EnquiryController.
     */
    public EnquiryController() {
    }

    /**
     * Creates a new enquiry submitted by an applicant.
     *
     * @param user The applicant submitting the enquiry.
     * @param project The BTOProject related to the enquiry.
     */
    public void createEnquiry(User user, BTOProject project) {
        // Check if user is an applicant
        if (user.getUserType() == UserType.HDB_MANAGER) {
            System.out.println("Only applicants can submit enquiries.");
            return;
        }

        // Check if project is null or not
        if (project == null) {
            System.out.println("Project cannot be null.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("--- Submit New Enquiry ---");
        System.out.print("Enter Enquiry Message: ");
        String message = scanner.nextLine();

        Applicant applicant = (Applicant) user; // Assuming applicant is of type Applicant
        if (applicant.submitEnquiry(project, message)){
            System.out.println("Enquiry submitted successfully!");
        } else {
            System.out.println("Failed to submit enquiry. Please try again.");
            return;
        };

        System.out.println("Enquiry submitted successfully!");
    }

    /**
     * Edit an existing enquiry submitted by an applicant.
     * 
     * @param user
     * @param enquiry
     */
    public void editEnquiry(User user, Enquiry enquiry) {
        // Check if enquiry is null or not
        if (enquiry == null) {
            System.out.println("Enquiry cannot be null.");
            return;
        }

        // Check if user is an applicant
        if (user.getUserType() == UserType.HDB_MANAGER) {
            System.out.println("Only applicants can edit their own enquiries.");
            return;
        }

        // Check if enquiry is solved or not
        if (enquiry.isSolved()) {
            System.out.println("Enquiry is already solved. Cannot edit.");
            return;
        }

        // Check if the user is the one who created the enquiry
        if (!enquiry.getApplicantName().equals(user.getName())) {
            System.out.println("You are not authorized to edit this enquiry.");
            return;
        }

        // Get the new message from the user
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- Edit Enquiry ---");
        System.out.print("Enter New Message: ");
        String message = scanner.nextLine();

        try {
            Applicant applicant = (Applicant) user; // Assuming applicant is of type Applicant
            applicant.editEnquiry(enquiry, message); // Edit the enquiry message
            System.out.println("Enquiry updated successfully!");
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage()); // Print error message
        }
    }

    /**
     * Allows an officer/manager managing thes project to reply to an enquiry.
     *
     * @param user The user replying to the enquiry.
     * @param enquiry The enquiry to be replied to.
     */
    public void replyToEnquiry(User user, Enquiry enquiry) {        
        // Check if enquiry is null or not
        if (enquiry == null) {
            System.out.println("Enquiry cannot be null.");
            return;
        }

        // Check if user is an officer or manager
        if (user.getUserType() == UserType.APPLICANT) {
            System.out.println("Only officers or managers can reply to enquiries.");
            return;
        }

        // Check if enquiry is solved or not
        if (enquiry.isSolved()) {
            System.out.println("Enquiry is already solved. Cannot reply.");
            return;
        }

        // Get the reply message from the user
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- Reply to Enquiry ---");
        System.out.print("Enter Reply Message: ");
        String message = scanner.nextLine();

        if (user.getUserType() == UserType.HDB_MANAGER) {
            try {
                HDBManager manager = (HDBManager) user; // Assuming user is of type HDBManager
                manager.replyToEnquiry(enquiry, message); // Reply to the enquiry
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage()); // Print error message
            }
        }
        else if (user.getUserType() == UserType.HDB_OFFICER) {
            try {
                HDBOfficer officer = (HDBOfficer) user; // Assuming user is of type Officer
                officer.replyToEnquiry(enquiry, message); // Reply to the enquiry
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage()); // Print error message
            }
        } else {
            System.out.println("Invalid user type. Cannot reply to enquiry.");
            return;
        }
    }
}
