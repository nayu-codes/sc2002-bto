package bto.model.user;

/**
 * Enum representing the marital status of a user.
 */
public enum MaritalStatus {
    /**
     * The user is single.
     */
    SINGLE("Single"),
    /**
     * The user is married.
     */
    MARRIED("Married");

    private final String status;

    MaritalStatus(String status) {
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
