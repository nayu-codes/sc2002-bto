package bto.model.user;

public class HDBManager extends User{
    /**
     * Constructor to initialise the HDBManager object with common attributes.
     * 
     * @param name The name of the HDBManager.
     * @param userId The unique identifier for the HDBManager.
     * @param password The password for the HDBManager.
     * @param age The age of the HDBManager.
     * @param maritalStatus The marital status of the HDBManager (e.g., "Single", "Married").
     */
    public HDBManager(String name, String userId, String password, int age, MaritalStatus maritalStatus, String userType) {
        super(name, userId, password, age, maritalStatus, userType);
    }
}
