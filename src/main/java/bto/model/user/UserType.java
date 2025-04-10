package bto.model.user;

public enum UserType {
    /**
     * Represents an applicant user type.
     */
    APPLICANT("Applicant"),
    /**
     * Represents an HDB officer user type.
     */
    HDB_OFFICER("HDB Officer"),
    /**
     * Represents an HDB manager user type.
     */
    HDB_MANAGER("HDB Manager");

    private final String displayName;

    UserType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
