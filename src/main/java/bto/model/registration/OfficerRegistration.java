package bto.model.registration;

import java.util.Date;

import bto.model.project.BTOProject;
import bto.model.user.HDBOfficer;

public class OfficerRegistration {
    private int registrationId; // Unique identifier for the registration
    private HDBOfficer officer; // The applicant associated with this registration
    private RegistrationStatus status; // The current status of the registration (e.g., Pending, Successful, Unsuccessful)
    private BTOProject project; // The BTO project associated with this registration
    private Date applicationDate; // The date the registration was submitted

    /**
     * Constructor to initialise the OfficerRegistration object with common attributes.
     * @param registrationId The unique identifier for the registration.
     * @param officer The HDBOfficer associated with this registration.
     * @param status The current status of the registration (e.g., Pending, Successful, Unsuccessful).
     * @param project The BTO project associated with this registration.
     * @param applicationDate The date the registration was submitted.
     */
    public OfficerRegistration(int registrationId, HDBOfficer officer, RegistrationStatus status, BTOProject project,
            Date applicationDate) {
        this.registrationId = registrationId;
        this.officer = officer;
        this.status = status;
        this.project = project;
        this.applicationDate = applicationDate;
    }
}
