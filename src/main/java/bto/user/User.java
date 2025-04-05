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
     * Constructor to initialize the User object with common attributes.
     * This constructor is used for creating a new user with default values for password and userType.
     * @param name
     * @param userId
     * @param age
     * @param maritalStatus
     */
    public User(String name, String userId, int age, String maritalStatus) {
        this.name = name;
        this.userId = userId;
        this.password = "password"; // Default password, can be changed later
        this.userType = "Applicant"; // Default user type, can be changed later
        this.age = age;
        this.maritalStatus = maritalStatus;
    }

    /**
     * Getter methods to access the attributes of the User class.
     * These methods are used to retrieve the values of the private attributes.
     * @return
     */
    public String getName() {
        return name;
    }
    public String getUserId() {
        return userId;
    }
    public String getPassword() {
        return password;
    }
    public int getAge() {
        return age;
    }
    public String getMaritalStatus() {
        return maritalStatus;
    }
    public String getUserType() {
        return userType;
    }

    /**
     * Setter methods to modify the attributes of the User class.
     * These methods are used to set the values of the private attributes.
     * @param name
     * @param userId
     * @param password
     * @param age
     * @param maritalStatus
     * @param userType
     */
    public void setName(String name) {
        this.name = name;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
}
