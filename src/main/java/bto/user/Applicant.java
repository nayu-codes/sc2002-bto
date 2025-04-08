package bto.user;

public class Applicant extends User {
    /**
     * Constructor to initialise the Applicant object with common attributes.
     * 
     * @param name The name of the applicant.
     * @param userId The unique identifier for the applicant.
     * @param password The password for the applicant.
     * @param age The age of the applicant.
     * @param maritalStatus The marital status of the applicant (e.g., "Single", "Married").
     */
    public Applicant(String name, String userId, String password, int age, String maritalStatus) {
        super(name, userId, password, age, maritalStatus, "Applicant");
    }
}
