package bto.model.application;

import java.util.Date;

import bto.database.ApplicationDB;
import bto.database.BTOProjectDB;
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
     * 
     * @return true if the application is successfully approved, false otherwise.
     */
    public boolean approve() throws IllegalStateException {
        this.status = ApplicationStatus.SUCCESSFUL; // Set the status to SUCCESSFUL
        return ApplicationDB.updateApplication(this); // Update the application in the database
    }

    /**
     * Rejects the application by updating its status to UNSUCCESSFUL. Only the HDB Officer / Manager can approve an application.
     * 
     * @return true if the application is successfully rejected, false otherwise.
     */
    public boolean reject() {
        this.status = ApplicationStatus.UNSUCCESSFUL; // Set the status to UNSUCCESSFUL
        return ApplicationDB.updateApplication(this); // Update the application in the database
    }

    /**
     * Withdraws the application by updating its status to UNSUCCESSFUL.
     * 
     * @return true if the application is successfully withdrawn, false otherwise.
     */
    public boolean withdraw() {
        this.status = ApplicationStatus.UNSUCCESSFUL; // Set the status to UNSUCCESSFUL
        return ApplicationDB.updateApplication(this); // Update the application in the database
    }

    /**
     * Books the application by updating its status to BOOKED.
     * 
     * @param bookingDate The date the booking was made.
     * 
     * @return true if the application is successfully booked, false otherwise.
     */
    public boolean book(Date bookingDate) {
        this.status = ApplicationStatus.BOOKED; // Set the status to BOOKED
        this.bookingDate = bookingDate; // Set the booking date
        return ApplicationDB.updateApplication(this); // Update the application in the database
    }

    /**
     * Un-book the application by updating its status to UNCESSFUL, and incrementing the flatCountRemaining of the project.
     */
    public boolean unbook() {
        this.status = ApplicationStatus.UNSUCCESSFUL; // Set the status to UNSUCCESSFUL
        // Update the application in the database
        if(ApplicationDB.updateApplication(this)) {
            // Increment the flatCountRemaining of the project
            try {
                getProject().increaseFlatCountRemaining(getFlatType());
                // Update the project in the database
                try {
                    BTOProjectDB.updateBTOProject(getProject().getName(), getProject()); // Update the project in the database
                } catch (IllegalAccessException e) {
                    System.out.println("Failed to update project in the database: " + e.getMessage());
                    return false; // Return false if the project could not be updated in the database
                }
                return true; // Return true if the application was unbooked successfully
            } catch (IllegalStateException e) {
                System.out.println("Failed to increase flat count remaining: " + e.getMessage());

                // Undo the status change if flat count could not be increased
                this.status = ApplicationStatus.BOOKED; // Revert the status to BOOKED
                ApplicationDB.updateApplication(this); // Update the application in the database
                return false; // Return false if the flat count could not be increased
            }
        } else {
            return false; // Return false if the application was not unbooked successfully
        }
    }
}
