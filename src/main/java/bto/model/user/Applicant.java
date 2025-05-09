package bto.model.user;

import bto.database.ApplicationDB;
import bto.database.EnquiryDB;
import bto.model.application.ApplicationStatus;
import bto.model.application.BTOApplication;
import bto.model.project.BTOProject;
import bto.model.project.FlatType;
import bto.model.enquiry.Enquiry;

import java.util.ArrayList;
import java.util.List;

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
     * @param project The BTOProject object representing the project to apply for.
     * @param flatType The FlatType object representing the type of flat to apply for.
     * 
     * @throws IllegalStateException if the applicant does not meet the requirements for the specified flat type.
     */
    public void submitApplication(BTOProject project, FlatType flatType) throws IllegalStateException {
        // Check if Applicant applied for other projects that is not unsuccessful
        for (BTOApplication application : ApplicationDB.getApplicationsByApplicant(getUserId())) {
            if (application.getProject().getProjectId() == project.getProjectId()) {
                if (application.getStatus() != ApplicationStatus.UNSUCCESSFUL) {
                    throw new IllegalStateException("Cannot apply for this project as there is a pending/completed application for the same project.");
                }
            }
        }
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
     * 
     * @param project The BTOProject object representing the project to be enquired about.
     * @param enquiry The message content of the enquiry.
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
     * @return An List of Enquiry objects representing the enquiries made by the applicant.
     */
    public List<Enquiry> getEnquiries() {
        return EnquiryDB.getEnquiriesByApplicant(this); // Return the list of enquiries made by the applicant
    }

    /**
     * Edit the applicant message in the enquiry if the enquiry is not solved.
     * 
     * @param enquiry The Enquiry object representing the enquiry to be edited.
     * @param message The new message content.
     */
    public void editEnquiry(Enquiry enquiry, String message) throws IllegalStateException {
        if (enquiry.isSolved()) {
            throw new IllegalStateException("Cannot modify message in a solved enquiry.");
        }
        enquiry.addApplicantMessage(this, message); // Modify the applicant message in the enquiry
    }

    /**
     * Delete the enquiry before it is solved.
     * 
     * @param enquiry The Enquiry object representing the enquiry to be deleted.
     */
    public void deleteEnquiry(Enquiry enquiry) throws IllegalStateException {
        if (enquiry.isSolved()) {
            throw new IllegalStateException("Cannot delete a solved enquiry.");
        }
        enquiry.deleteEnquiry();
    }
}
