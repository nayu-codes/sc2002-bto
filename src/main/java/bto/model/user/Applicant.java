package bto.model.user;

import bto.database.ApplicationDB;
import bto.database.EnquiryDB;
import bto.model.application.BTOApplication;
import bto.model.application.ApplicationStatus;
import bto.model.project.BTOProject;
import bto.model.project.FlatType;
import bto.model.enquiry.Enquiry;

import java.util.ArrayList;

public class Applicant extends User {
    /**
     * Constructor to initialise the Applicant object with common attributes.
     * 
     * @param name The name of the applicant.
     * @param userId The unique identifier for the applicant.
     * @param password The password for the applicant.
     * @param age The age of the applicant.
     * @param maritalStatus The marital status of the applicant (e.g., "Single", "Married").
     * @param userType The type of user (e.g., {@link UserType#APPLICANT}).
     * @see UserType
     */
    public Applicant(String name, String userId, String password, int age, MaritalStatus maritalStatus, UserType userType) {
        super(name, userId, password, age, maritalStatus, userType);
    }

    /**
     * Method to get the list of BTOApplications made by the applicant.
     * 
     * @return An ArrayList of BTOApplication objects representing the applications made by the applicant.
     * @see BTOApplication
     */
    public ArrayList<BTOApplication> appliedProjects() {
        ArrayList<BTOApplication> applications = new ArrayList<>(); // List to store the applications made by the applicant
        for (BTOApplication application : ApplicationDB.getApplicationsByApplicant(getUserId())) { // Get all applications made by the applicant
            applications.add(application); // Add the application to the list
        }
        return applications; // Return the list of applications made by the applicant
    }

    /**
     * Submit an application for a BTO project.
     * 
     * Assumes that the application has been validated and is ready for submission.
     * 
     * @param application The BTOApplication object representing the application to be submitted.
     * 
     * @return true if the application was submitted successfully, false otherwise.
     */
    public void submitApplication(BTOProject project, FlatType flatType) throws IllegalStateException {
        if (flatType == FlatType.THREE_ROOM){
            // Applicant must be married, and above 21 years old
            if (getMaritalStatus() != MaritalStatus.MARRIED || getAge() < 21) {
                throw new IllegalStateException("Applicant must be married and above 21 years old to apply for a 3-room flat.");
            }
        }
        if (flatType == FlatType.TWO_ROOM) {
            // Applicant must be married, and above 21 years old, or single and above 35 years old
            if (getMaritalStatus() != MaritalStatus.MARRIED && getAge() < 35) {
                throw new IllegalStateException("Applicant must be married and above 21 years old, or single and above 35 years old to apply for a 2-room flat.");
            }
            if (getMaritalStatus() == MaritalStatus.MARRIED && getAge() < 21) {
                throw new IllegalStateException("Applicant must be married and above 21 years old to apply for a 2-room flat.");
            }
        }
        BTOApplication newApplication = new BTOApplication(this, project, flatType); // Create a new application object
        if (ApplicationDB.addApplication(newApplication)) { // Add the application to the database
            System.out.println("Application submitted successfully."); // Print success message
        } else {
            System.out.println("Failed to submit application."); // Print failure message
        }
    }

    /**
     * Withdraw an application for a BTO project.
     * 
     * @param application The BTOApplication object representing the application to be withdrawn.
     * 
     * @return true if the application was withdrawn successfully, false otherwise.
     */
    public boolean withdrawApplication(BTOApplication application) {
        return application.withdraw(); // Withdraw the application
    }

    /**
     * Submit an enquiry for a BTOProject
     * @param project The BTOProject object representing the project to be enquired about.
     * 
     * @return true if the enquiry was submitted successfully, false otherwise.
     */
    public boolean submitEnquiry(BTOProject project, String enquiry) {
        Enquiry newEnquiry = new Enquiry(getName(), project.getName()); // Create a new enquiry object
        try {
            newEnquiry.addApplicantMessage(this, enquiry);
            return true;
        } catch (IllegalStateException e) {
            System.out.println("Enquiry already solved. Cannot add message.");
            return false; // Return false if the enquiry is already solved
        }
    }

    /**
     * Gets the list of enquiries made by the applicant.
     * 
     * @return An ArrayList of Enquiry objects representing the enquiries made by the applicant.
     */
    public ArrayList<Enquiry> getEnquiries() {
        return EnquiryDB.getEnquiriesByApplicant(this); // Return the list of enquiries made by the applicant
    }

    /**
     * Edit the applicant message in the enquiry if the enquiry is not solved.
     */
    public void editEnquiry(Enquiry enquiry, String message) throws IllegalStateException {
        if (enquiry.isSolved()) {
            throw new IllegalStateException("Cannot modify message in a solved enquiry.");
        }
        enquiry.addApplicantMessage(this, message);; // Modify the applicant message in the enquiry
    }
}
