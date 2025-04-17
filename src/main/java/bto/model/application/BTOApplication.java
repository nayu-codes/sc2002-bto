package bto.model.application;

import java.util.Date; // Importing the Date class from java.util package

import bto.model.project.BTOProject;
import bto.model.project.FlatType;
import bto.model.user.Applicant;

public class BTOApplication {
    private int applicationId; // Unique identifier for the application
    private Applicant applicant; // The applicant associated with this application
    private ApplicationStatus status; // The current status of the application (e.g., Pending, Successful, Unsuccessful, Booked, Withdrawn)
    private BTOProject project; // The BTO project associated with this application
    private FlatType flatType; // The type of flat applied for (e.g., 2-room, 3-room, etc.)
    private Date applicationDate; // The date the application was submitted
    private Date bookingDate; // The date the booking was made (if applicable) TODO: Do we need this?

    /**
     * Constructor to initialise the BTOApplication object with common attributes.
     * 
     * @param applicationId The unique identifier for the application.
     * @param applicant The applicant associated with this application.
     * @param status The current status of the application (e.g., Pending, Successful, Unsuccessful, Booked, Withdrawn).
     * @param project The BTO project associated with this application.
     * @param flatType The type of flat applied for (e.g., 2-room, 3-room, etc.).
     * @param applicationDate The date the application was submitted.
     */
    public BTOApplication(int applicationId, Applicant applicant, ApplicationStatus status, BTOProject project,
            FlatType flatType, Date applicationDate) {
        this.applicationId = applicationId;
        this.applicant = applicant;
        this.status = status;
        this.project = project;
        this.flatType = flatType;
        this.applicationDate = applicationDate;
        this.bookingDate = null; // Initialising bookingDate to null as it may not be set at the time of application
    }

    /**
     * Constructor to import a BTOApplication object.
     * 
     * @param applicationId   The unique identifier for the application.
     * @param applicant       The applicant associated with this application.
     * @param status          The current status of the application (e.g., Pending,
     *                        Successful, Unsuccessful, Booked, Withdrawn).
     * @param project         The BTO project associated with this application.
     * @param flatType        The type of flat applied for (e.g., 2-room, 3-room,
     *                        etc.).
     * @param applicationDate The date the application was submitted.
     * @param bookingDate     The date the booking was made (if applicable).
     */
    public BTOApplication(int applicationId, Applicant applicant, ApplicationStatus status, BTOProject project,
            FlatType flatType, Date applicationDate, Date bookingDate) {
        this.applicationId = applicationId;
        this.applicant = applicant;
        this.status = status;
        this.project = project;
        this.flatType = flatType;
        this.applicationDate = applicationDate;
        this.bookingDate = bookingDate;
    }

    /**
     * Gets the application ID.
     * 
     * @return The unique identifier for the application.
     */
    public int getApplicationId() {
        return applicationId;
    }

    /**
     * Gets the applicant associated with this application.
     * 
     * @return The applicant associated with this application.
     */
    public Applicant getApplicant() {
        return applicant;
    }

    /**
     * Gets the current status of the application.
     * 
     * @return The current status of the application (e.g., Pending, Successful, Unsuccessful, Booked, Withdrawn).
     */
    public ApplicationStatus getStatus() {
        return status;
    }

    /**
     * Gets the BTO project associated with this application.
     * 
     * @return The BTO project associated with this application.
     */
    public BTOProject getProject() {
        return project;
    }

    /**
     * Gets the type of flat applied for.
     * 
     * @return The type of flat applied for (e.g., 2-room, 3-room, etc.).
     */
    public FlatType getFlatType() {
        return flatType;
    }

    /**
     * Gets the date the application was submitted.
     * 
     * @return The date the application was submitted.
     */
    public Date getApplicationDate() {
        return applicationDate;
    }

    /**
     * Gets the date the booking was made (if applicable).
     * 
     * @return The date the booking was made (if applicable).
     */
    public Date getBookingDate() {
        return bookingDate;
    }
}
