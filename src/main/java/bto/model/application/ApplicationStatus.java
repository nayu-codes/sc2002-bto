package bto.model.application;

/**
 * Enum representing the status of an application.
 */
public enum ApplicationStatus {
    
    /**
     * The application is pending review.
     */
    PENDING("Pending"),
    /**
     * The application has been approved.
     */
    SUCCESSFUL("Successful"),
    /**
     * The application has been rejected.
     */
    UNSUCCESSFUL("Unsuccessful"),
    /**
     * The application has been booked.
     */
    BOOKED("Booked"),
    /**
     * The application has been withdrawn.
     */
    WITHDRAWN("Withdrawn");

    private final String status;

    ApplicationStatus(String status) {
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
