package bto.model.user;

public class HDBOfficer extends Applicant {
    /**
     * Constructor to initialise the HDBOfficer object with common attributes.
     * 
     * @param name The name of the HDBOfficer.
     * @param userId The unique identifier for the HDBOfficer.
     * @param password The password for the HDBOfficer.
     * @param age The age of the HDBOfficer.
     * @param maritalStatus The marital status of the HDBOfficer (e.g., "Single", "Married").
     * @param userType The type of user (e.g., {@link UserType#HDB_OFFICER}).
     * @see UserType
     */
    public HDBOfficer(String name, String userId, String password, int age, MaritalStatus maritalStatus, UserType userType) {
        super(name, userId, password, age, maritalStatus, userType);
    }

}