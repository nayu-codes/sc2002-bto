package bto.user;

public abstract class User {
    /**
     * User class representing a user in the system.
     * This class is abstract and should be extended by specific user types.
     * It contains common attributes and methods for all users.
     * 
     * @param name The name of the user.
     * @param userId The unique identifier for the user.
     * @param password The password for the user.
     * @param age The age of the user.
     * @param maritalStatus The marital status of the user (e.g., "Single", "Married").
     * @param userType The type of user (e.g., "Applicant", "HDBOfficer", "HDBManager").
     * 
     * This class is part of the BTO application system.
     * It is designed to be extended by specific user types such as Applicant, HDBOfficer, and HDBManager.
     * The class provides a constructor to initialise the common attributes of a user.
     * It also provides getter methods to access the attributes.
     * 
     * @see Applicant
     * @see HDBOfficer
     * @see HDBManager
     */
    private String name;
    private String userId;
    private String password;
    private int age;
    private String maritalStatus; // "Single", "Married"
    private String userType; // "Applicant", "HDBOfficer", "HDBManager"

    /**
     * Constructor to initialize the User object with common attributes.
     * 
     * @param name The name of the user.
     * @param userId The unique identifier for the user.
     * @param password The password for the user.
     * @param age The age of the user.
     * @param maritalStatus The marital status of the user (e.g., "Single", "Married").
     * @param userType The type of user (e.g., "Applicant", "HDBOfficer", "HDBManager").
     */
    public User(String name, String userId, String password, int age, String maritalStatus, String userType) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.userType = userType;
    }

    /**
     * Getter method to access the name of the User class.
     * @return The name of the user.
     *         This method returns the name of the user as a String.
     */
    public String getName() {
        return name;
    }
    /**
     * Getter method to access the UserID of the User class.
     * @return The userId of the user.
     * This method returns the userId of the user as a String.
     */
    public String getUserId() {
        return userId;
    }
    /**
     * Getter method to access the password of the User class.
     * @return The password of the user.
     * This method returns the password of the user as a String.
     */
    public String getPassword() {
        return password;
    }
    /**
     * Getter method to access the age of the User class.
     * @return The age of the user.
     * This method returns the age of the user as an int.
     */
    public int getAge() {
        return age;
    }
    /**
     * Getter method to access the marital status of the User class.
     * @return The marital status of the user.
     * This method returns the marital status of the user as a String.
     */
    public String getMaritalStatus() {
        return maritalStatus;
    }
    /**
     * Getter method to access the user type of the User class.
     * @return The user type of the user.
     * This method returns the user type of the user as a String.
     */
    public String getUserType() {
        return userType;
    }

    /**
     * Setter methods to set the name of the User class.
     * @return void
     * This method sets the name of the user to the provided value.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Setter methods to set the userId of the User class.
     * @return void
     * This method sets the userId of the user to the provided value.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    /**
     * Setter methods to set the password of the User class.
     * @return void
     * This method sets the password of the user to the provided value.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * Setter methods to set the age of the User class.
     * @return void
     * This method sets the age of the user to the provided value.
     */
    public void setAge(int age) {
        this.age = age;
    }
    /**
     * Setter methods to set the marital status of the User class.
     * @return void
     * This method sets the marital status of the user to the provided value.
     */
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }
    /**
     * Setter methods to set the user type of the User class.
     * @return void
     * This method sets the user type of the user to the provided value.
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }
}
