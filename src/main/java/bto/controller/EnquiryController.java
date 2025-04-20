package bto.controller;

import bto.database.BTOProjectDB;
import bto.database.EnquiryDB;
import bto.model.enquiry.Enquiry;
import bto.model.user.*;
import bto.model.project.BTOProject;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class EnquiryController {
    private EnquiryController(){} // Prevents Instantiation

    /**
     * Creates a new enquiry submitted by an applicant.
     *
     * @param user The applicant submitting the enquiry.
     * @param project The BTOProject related to the enquiry.
     */
    public static void createEnquiry(User user, BTOProject project) {
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
        System.out.println("Enter Enquiry Message (or enter -1 to exit without submitting enquiry): ");
        String message = scanner.nextLine().trim(); // Remove leading and trailing whitespace

        // Check if message is -1 to exit without submitting enquiry
        if (message.equals("-1")) {
            System.out.println("Exiting without submitting enquiry.");
            return;
        }

        // Check if message is empty or not
        if (message.isEmpty()) {
            System.out.println("Enquiry message cannot be empty.");
            return;
        }
        
        Applicant applicant = (Applicant) user; // Assuming user is of type Applicant
        if (applicant.submitEnquiry(project, message)){
            System.out.println("Enquiry submitted successfully!");
        } else {
            System.out.println("Failed to submit enquiry. Please try again.");
            return;
        };
    }

    /**
     * Edit an existing enquiry submitted by an applicant.
     * 
     * @param user
     * @param enquiry
     */
    public static void editEnquiry(User user, Enquiry enquiry) {
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
    public static void replyToEnquiry(User user, Enquiry enquiry) {        
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

    /**
     * Get enquiries that belong to a specific user.
     * 
     * @param user The user whose enquiries are to be retrieved.
     * 
     * @return List of enquiries belonging to the user. 
     *         Returns null if the user is an HDB Manager.
     */
    public static List<Enquiry> getEnquiriesByUser(User user) {
        // Check if user is an applicant
        if (user.getUserType() == UserType.HDB_MANAGER) {
            System.out.println("Only applicants can view their own enquiries.");
            return null;
        }

        // Get the list of enquiries for the user
        return EnquiryDB.getEnquiriesByApplicant(user);
    }

    /**
     * Get enquiries from projects that a officer is managing. Sorted by project application opening date, latest first. Then, enquiry date, latest first.
     * 
     * @param user The HDB officer/HDB Manager whose enquiries are to be retrieved.
     * 
     * @return List of enquiries belonging to the HDB Officer / HDB Manager's managed projects.
     */
    public static List<Enquiry> getEnquiriesByOfficerOrManager(User user) {
        // Check if user is an officer or manager
        if (user.getUserType() != UserType.APPLICANT) {
            System.out.println("Only officers/managers can view enquiries by other applicants.");
            return null;
        }

        List<BTOProject> managedProjects = new ArrayList<>();

        if (user.getUserType() == UserType.HDB_MANAGER) {
            // Get a list of projects that the manager is managing
            HDBManager manager = (HDBManager) user; // Assuming user is of type HDBManager
            managedProjects = BTOProjectDB.getBTOProjectsByManager(manager);
        } else if (user.getUserType() == UserType.HDB_OFFICER) {
            // Get a list of projects that the officer is managing
            HDBOfficer officer = (HDBOfficer) user; // Assuming user is of type HDBOfficer
            managedProjects = BTOProjectDB.getBTOProjectsByOfficer(officer);
        }

        // Sort by project opening date. Index 0 is the latest project.
        managedProjects.sort((project1, project2) -> project2.getApplicationOpeningDate().compareTo(project1.getApplicationOpeningDate()));

        // Get the list of enquiries for the officer's managed projects
        List<Enquiry> managedEnquiries = new ArrayList<>();
        for (BTOProject project : managedProjects) {
            List<Enquiry> projectEnquiries = EnquiryDB.getEnquiriesByProject(project);
            if (projectEnquiries != null) {
                // Sort by enquiry date. Index 0 is the latest enquiry.
                projectEnquiries.sort((enquiry1, enquiry2) -> enquiry2.getApplicantMessage().getDateTime().compareTo(enquiry1.getApplicantMessage().getDateTime()));
                managedEnquiries.addAll(projectEnquiries);
            }
        }

        return managedEnquiries;
    }

    /**
     * Get all enquiries in the system.
     * 
     * @return List of all enquiries in the system.
     */
    public static List<Enquiry> getAllEnquiries() {
        List<Enquiry> enquiryList = EnquiryDB.getEnquiryList();

        // Sort by enquiry date. Index 0 is the latest enquiry.
        enquiryList.sort((enquiry1, enquiry2) -> enquiry2.getApplicantMessage().getDateTime().compareTo(enquiry1.getApplicantMessage().getDateTime()));
        return enquiryList;
    }
}
