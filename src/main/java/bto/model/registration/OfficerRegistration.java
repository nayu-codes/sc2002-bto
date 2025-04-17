package bto.model.registration;

import bto.model.project.BTOProject;
import bto.model.user.HDBOfficer;

public class OfficerRegistration {
    private int registrationId; // Unique identifier for the registration
    private HDBOfficer officer; // The applicant associated with this registration
    private RegistrationStatus status; // The current status of the registration (e.g., Pending, Successful, Unsuccessful)
    private BTOProject project; // The BTO project associated with this registration

    /**
     * Constructor to initialise the OfficerRegistration object with common attributes.
     * @param registrationId The unique identifier for the registration.
     * @param officer The HDBOfficer associated with this registration.
     * @param status The current status of the registration (e.g., Pending, Successful, Unsuccessful).
     * @param project The BTO project associated with this registration.
     */
    public OfficerRegistration(int registrationId, HDBOfficer officer, RegistrationStatus status, BTOProject project) {
        this.registrationId = registrationId;
        this.officer = officer;
        this.status = status;
        this.project = project;
    }

    /**
     * Gets the registration ID.
     * @return The unique identifier for the registration.
     */
    public int getRegistrationId() {
        return registrationId;
    }

    /**
     * Gets the officer associated with this registration.
     * 
     * @return The HDBOfficer associated with this registration.
     */
    public HDBOfficer getOfficer() {
        return officer;
    }

    /**
     * Gets the current status of the registration.
     * 
     * @return The current status of the registration (e.g., Pending, Successful, Unsuccessful).
     */
    public RegistrationStatus getRegistrationStatus() {
        return status;
    }

    /**
     * Gets the BTO project associated with this registration.
     * 
     * @return The BTO project associated with this registration.
     */
    public BTOProject getProject() {
        return project;
    }
}
