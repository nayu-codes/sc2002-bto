package bto.model.registration;

/**
 * Enum representing the registration status of an applicant.
 */
public enum RegistrationStatus {
    /**
     * The registration is pending review.
     */
    PENDING("Pending"),
    /**
     * The registration has been approved.
     */
    SUCCESSFUL("Successful"),
    /**
     * The registration has been rejected.
     */
    UNSUCCESSFUL("Unsuccessful");

    private final String status;

    RegistrationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}
