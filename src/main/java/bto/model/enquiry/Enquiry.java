package bto.model.enquiry;

import bto.database.EnquiryDB;
import bto.model.user.User;

import java.util.Date;

public class Enquiry {
    private final int enquiryId;
    private final String applicantName;
    private String projectName;
    private EnquiryMessage applicantMessage;
    private EnquiryMessage replyMessage;
    private boolean isSolved = false;

    /**
     * Constructor for a new Enquiry.
     * 
     * @param applicantName The name of the applicant.
     * @param projectName The name of the project.
     */
    public Enquiry(String applicantName, String projectName) {
        this.enquiryId = EnquiryDB.getEnquiryCount()+1; // Set the enquiry ID to the next available ID
        this.applicantName = applicantName;
        this.projectName = projectName;
        this.applicantMessage = null; // Initialise applicantMessage to null
        this.replyMessage = null; // Initialise replyMessage to null
        this.isSolved = false; // Set the enquiry as not solved
    }

    /**
     * Constructor for Enquiry with messages. This is ONLY called during {@link EnquiryDB#readFromCSV()}
     * 
     * @param enquiryId The ID of the enquiry.
     * @param applicantName The name of the applicant.
     * @param projectName The name of the project.
     * @param applicantMessage The message from the applicant.
     * @param replyMessage The reply message from the HDB Officer / Manager.
     */
    public Enquiry(int enquiryId, String applicantName, String projectName, EnquiryMessage applicantMessage, EnquiryMessage replyMessage) {
        this.enquiryId = enquiryId;
        this.applicantName = applicantName;
        this.projectName = projectName;
        this.applicantMessage = applicantMessage;
        this.replyMessage = replyMessage;
        this.isSolved = replyMessage != null; // Set the enquiry as solved if there is a reply message
    }

    /**
     * Gets the enquiry ID.
     * 
     * @return The ID of the enquiry.
     */
    public int getEnquiryId() {
        return enquiryId;
    }

    /**
     * Gets the applicant name.
     * 
     * @return The name of the applicant.
     */
    public String getApplicantName() {
        return applicantName;
    }

    /**
     * Gets the project name.
     * 
     * @return The name of the project.
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Set the project name.
     * 
     * @param projectName The name of the project.
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
        EnquiryDB.updateEnquiry(this); // Update the enquiry in the database
    }

    /**
     * Gets the applicant message of the enquiry.
     * 
     * @return The applicant message in the enquiry.
     */
    public EnquiryMessage getApplicantMessage() {
        return applicantMessage;
    }

    /**
     * Sets the applicant message of the enquiry.
     * 
     * @param applicantMessage The message from the applicant.
     */
    public void setApplicantMessage(EnquiryMessage applicantMessage) {
        this.applicantMessage = applicantMessage;
    }

    /**
     * Gets the reply message of the enquiry.
     * 
     * @return The reply message in the enquiry.
     */
    public EnquiryMessage getReplyMessage() {
        return replyMessage;
    }

    /**
     * Gets the status of the enquiry.
     *
     * @return true if the enquiry is solved, false otherwise.
     */
    public boolean isSolved() {
        return isSolved;
    }

    /**
     * Add/modify applicant message in the enquiry if the enquiry is not solved.
     * 
     * @param applicant The applicant who is sending the message.
     * @param message The message content.
     * 
     * @throws IllegalStateException if the enquiry is already solved, or if there was an error updating the enquiry in the database.
     */
    public void addApplicantMessage(User applicant, String message) throws IllegalStateException {
        if (isSolved) {
            throw new IllegalStateException("Cannot add message to a solved enquiry.");
        }
        this.applicantMessage = new EnquiryMessage(applicant.getName(), message, new Date());

        // Add the enquiry to the database
        if (!EnquiryDB.addEnquiry(this)) {
            // If is to update the enquiry, then update the enquiry in the database instead of adding it
            if (!EnquiryDB.updateEnquiry(this)) {
                throw new IllegalStateException("Failed to update enquiry in the database.");
            }
        }
    }

    /**
     * Add reply message in the enquiry if the enquiry is not solved, and set the enquiry as solved.
     * 
     * @param user The user who is replying to the enquiry.
     * @param message The reply message content.
     * 
     * @throws IllegalStateException if the enquiry is already solved.
     */
    public void addReplyMessage(User user, String message) throws IllegalStateException {
        if (isSolved) {
            throw new IllegalStateException("Cannot add reply to a solved enquiry.");
        }
        this.replyMessage = new EnquiryMessage(user.getName(), message, new Date());
        this.isSolved = true; // Set the enquiry as solved

        // Update the enquiry in the database
        if (!EnquiryDB.updateEnquiry(this)) {
            throw new IllegalStateException("Failed to update enquiry in the database.");
        }
    }

    /**
     * Delete the enquiry by marking it as solved.
     * 
     * @throws IllegalStateException if there was an error updating the enquiry in the database.
     */
    public void deleteEnquiry() throws IllegalStateException {
        this.isSolved = true; // Mark the enquiry as solved

        // Since there is no status info when exporting to CSV, workaround by setting the replyMessage date to current date, but set replyMessage to "Enquiry Deleted"
        this.replyMessage = new EnquiryMessage("", "Enquiry Deleted", new Date());

        // Update the enquiry in the database
        if (!EnquiryDB.updateEnquiry(this)) {
            throw new IllegalStateException("Failed to update enquiry in the database.");
        }
    }
}
