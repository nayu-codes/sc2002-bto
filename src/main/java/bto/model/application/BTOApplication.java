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
}
