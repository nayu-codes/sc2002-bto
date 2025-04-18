package bto.model.application;

import java.util.Date;

import bto.database.ApplicationDB;
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
     * @param applicant The applicant associated with this application.
     * @param project The BTO project associated with this application.
     * @param flatType The type of flat applied for (e.g., 2-room, 3-room).
     */
    public BTOApplication(Applicant applicant, BTOProject project, FlatType flatType) {
        this.applicationId = ApplicationDB.getApplicationCount() + 1; // Set the application ID to the next available ID
        this.applicant = applicant;
        this.status = ApplicationStatus.PENDING; // Setting the initial status to PENDING
        this.project = project;
        this.flatType = flatType;
        this.applicationDate = new Date(); // Setting the application date to the current date
        this.bookingDate = null; // Initialising bookingDate to null as it may not be set at the time of application

        // Add the application to the database
        ApplicationDB.addApplication(this);
    }

    /**
     * Constructor to import a BTOApplication object. This is ONLY called during {@link ApplicationDB#readFromCSV()}
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
     * Sets the status of the application.
     * 
     * @param status The new status of the application.
     */
    public void setStatus(ApplicationStatus status) {
        this.status = status;
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

    /**
     * Approves the application by updating its status to SUCCESSFUL. Only the HDB Officer / Manager can approve an application.
     */
    public boolean approve() {
        this.status = ApplicationStatus.SUCCESSFUL; // Set the status to SUCCESSFUL
        ApplicationDB.updateApplication(this); // Update the application in the database

        return true; // Return true to indicate success
    }

    /**
     * Rejects the application by updating its status to UNSUCCESSFUL. Only the HDB Officer / Manager can approve an application.
     */
    public boolean reject() {
        this.status = ApplicationStatus.UNSUCCESSFUL; // Set the status to UNSUCCESSFUL
        ApplicationDB.updateApplication(this); // Update the application in the database

        return true; // Return true to indicate success
    }

    /**
     * Withdraws the application by updating its status to UNSUCCESSFUL.
     */
    public boolean withdraw() {
        this.status = ApplicationStatus.UNSUCCESSFUL; // Set the status to UNSUCCESSFUL
        ApplicationDB.updateApplication(this); // Update the application in the database

        return true; // Return true to indicate success
    }

    /**
     * Books the application by updating its status to BOOKED.
     * 
     * @param bookingDate The date the booking was made.
     */
    public boolean book(Date bookingDate) {
        this.status = ApplicationStatus.BOOKED; // Set the status to BOOKED
        this.bookingDate = bookingDate; // Set the booking date
        ApplicationDB.updateApplication(this); // Update the application in the database
        return true; // Return true to indicate success
    }
}
