package bto.database;

import bto.model.user.User;
import bto.model.user.Applicant;
import bto.model.user.HDBOfficer;
import bto.model.user.HDBManager;
import bto.model.user.UserType;
import bto.model.user.MaritalStatus;

import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class UserDB {
    /**
     * UserDB class to manage the user database.
     * This class is responsible for adding, removing, and retrieving users from the database.
     * It uses a HashMap to store users with their userId as the key.
     * 
     * @see User
     */
    private static HashMap<String, User> userList = new HashMap<>(); // HashMap to store users with userId as the key
    private static final String CSV_FILE_PATH = "resources/UserList.csv"; // Path to the CSV file

    /**
     * Constructor to initialize the UserDB object with a user list.
     * The user list is represented as a HashMap where the key is the userId and the value is the User object.
     */
    public UserDB() {
        // Constructor to initialize the user list
    }

    /**
     * Method to initialize the user list.
     * This method is called to reload the user list from the CSV file.
     */
    public static void init(){
        userList = new HashMap<>();

        // Load users from CSV file, if available
        try {
            readFromCsv(); // Read users from the CSV file
        } catch (IOException e) {
            // Load users from raw CSV files for different user types
            processRawCsv("ApplicantList.csv", UserType.APPLICANT);
            processRawCsv("OfficerList.csv", UserType.HDB_OFFICER);
            processRawCsv("ManagerList.csv", UserType.HDB_MANAGER);
        }

        // Export users to CSV file after loading
        exportToCsv();
    }

    /**
     * Method to export users to a CSV file.
     * As a no-databse implementation, data persistency is achieved through exporting to CSV files.
     * This method is only useful after the first run of the program.
     * @param filename The name of the CSV file to export users to.
     */
    public static void exportToCsv() {
        // Write to CSV file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            // Write header
            bw.write("Name,userId,age,maritalStatus,password,userType\n");

            // Write user data
            for (User user : userList.values()) {
                String maritalStatusStr = user.getMaritalStatus().toString();
                String userTypeStr = user.getUserType().toString();
                bw.write(user.getName() + "," + user.getUserId() + "," + user.getAge() + "," +
                        maritalStatusStr + "," + user.getPassword() + "," + userTypeStr + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
    
    /**
     * Method to read users from a CSV file.
     * This method is only useful after the first run of the program where the users are processed and exported before. If first-run, use {@link #processRawCsv(String, UserType)} instead.
     * @param filename The name of the CSV file to read users from.
     * 
     * @return true if the users were read successfully, false otherwise.
     */
    public static void readFromCsv() throws IOException {
        String line = "";
        String csvSplitBy = ","; // The character used to separate values

        // Use try-with-resources to ensure the reader is closed automatically
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {

            br.readLine(); // Skip the header line

            // Read the rest of the file line by line
            while ((line = br.readLine()) != null) {

                // Split the line into an array of strings using the comma as a delimiter
                String[] fields = line.split(csvSplitBy);
                
                // Create a User object from the fields
                // Assuming the CSV has the following columns: Name, userId, age, maritalStatus, password
                String name = fields[0]; // Assuming the first column is Name
                String userId = fields[1]; // Assuming the second column is userId
                int age = Integer.parseInt(fields[2]); // Assuming the third column is age
                String maritalStatusStr = fields[3]; // Assuming the fourth column is maritalStatus
                String password = fields[4]; // Assuming the fifth column is userType

                // Convert maritalStatus strings to enums
                MaritalStatus maritalStatus = MaritalStatus.valueOf(maritalStatusStr.toUpperCase());
                
                // Create a new User object based on userType
                User user;
                if (fields.length == 6) {
                    UserType userType = UserType.valueOf(fields[5].toUpperCase());
                    if (userType == UserType.APPLICANT) {
                        user = new Applicant(name, userId, password, age, maritalStatus, UserType.APPLICANT);
                    } else if (userType == UserType.HDB_OFFICER) {
                        user = new HDBOfficer(name, userId, password, age, maritalStatus, UserType.HDB_OFFICER);
                    } else if (userType == UserType.HDB_MANAGER) {
                        user = new HDBManager(name, userId, password, age, maritalStatus, UserType.HDB_MANAGER);
                    } else {
                        throw new IllegalArgumentException("Invalid user type: " + userType);
                    }
                } else {
                    throw new IllegalArgumentException("Invalid number of fields in CSV file.");
                }
                
                // Add the user to the user list
                addUser(user);
            }
        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            System.err.println("Error processing user type: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error processing line: " + line + " - " + e.getMessage());
        }
    }

    /**
     * Method to process raw CSV files for different user types.
     * This method reads the CSV file, creates User objects, and adds them to the user list.
     * @param filename The name of the CSV file to be processed.
     * @param userType The type of user to be processed (Applicant, HDB Officer, or HDB Manager).
     */
    public static void processRawCsv(String filename, UserType userType) {
        String csvFilePath = "resources/" + filename;
        String line = "";
        String csvSplitBy = ","; // The character used to separate values

        // Use try-with-resources to ensure the reader is closed automatically
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {

            br.readLine(); // Skip the header line

            // Read the rest of the file line by line
            while ((line = br.readLine()) != null) {

                // Split the line into an array of strings using the comma as a delimiter
                String[] fields = line.split(csvSplitBy);
                
                // Create a User object from the fields
                // Assuming the CSV has the following columns: Name, userId, age, maritalStatus, password
                String name = fields[0]; // Assuming the first column is Name
                String userId = fields[1]; // Assuming the second column is userId
                int age = Integer.parseInt(fields[2]); // Assuming the third column is age
                String maritalStatusStr = fields[3]; // Assuming the fourth column is maritalStatus
                String password = fields[4]; // Assuming the fifth column is userType

                // Convert maritalStatus strings to enums
                MaritalStatus maritalStatus = MaritalStatus.valueOf(maritalStatusStr.toUpperCase());
                
                // Create a new User object based on userType
                User user;
                if (userType == UserType.APPLICANT) {
                    user = new Applicant(name, userId, password, age, maritalStatus, UserType.APPLICANT);
                } else if (userType == UserType.HDB_OFFICER) {
                    user = new HDBOfficer(name, userId, password, age, maritalStatus, UserType.HDB_OFFICER);
                } else if (userType == UserType.HDB_MANAGER) {
                    user = new HDBManager(name, userId, password, age, maritalStatus, UserType.HDB_MANAGER);
                } else {
                    throw new IllegalArgumentException("Invalid user type: " + userType);
                }
                
                // Add the user to the user list
                addUser(user);
            }

        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Error processing user type: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error processing line: " + line + " - " + e.getMessage());
        }
    }
    /**
     * Method to add a user to the user list.
     * The user is added to the HashMap with the userId as the key.
     * @param user The User object to be added to the user list.
     */
    public static void addUser(User user) {
        userList.put(user.getUserId(), user);

        // Save to CSV file after adding the user
        exportToCsv();
    }

    /**
     * Helper function to typecast the user object to the correct type.
     * Takes in a general User object and returns the specific type of user.
     * @param user The User object to be typecasted.
     * @return The specific type of user (Applicant, HDBOfficer, or HDBManager).
     */
    public static User typecastUser(User user) {
        if (user instanceof Applicant) {
            return (Applicant) user;
        } else if (user instanceof HDBOfficer) {
            return (HDBOfficer) user;
        } else if (user instanceof HDBManager) {
            return (HDBManager) user;
        }
        return null; // Return null if the user is not of a known type
    }

    /**
     * Method to retrieve a user from the user list using the userId.
     * @param userId The unique identifier of the user to be retrieved.
     * @return The User object associated with the given userId, or null if not found.
     */
    public static User getUserById(String userId) {
        // Check if the userId exists in the user list
        if (!userExists(userId)) {
            return null; // User not found
        }

        User user = userList.get(userId);
        return typecastUser(user); // Typecast the user to the correct type
    }
    /**
     * Method to retrieve a user from the user list using their name.
     * @param name The name of the user to be retrieved. (This program assumes that names are unique)
     * @return The User object associated with the given name, or null if not found.
     */
    public static User getUserByName(String name) {
        for (User user : userList.values()) {
            if (user.getName().equalsIgnoreCase(name)) {
                return typecastUser(user); // Typecast the user to the correct type
            }
        }
        // If the user is not found, return null
        return null;
    }
    
    /**
     * Method to remove a user from the user list using the userId.
     * @param userId The unique identifier of the user to be removed.
     */
    public static void removeUser(String userId) {
        userList.remove(userId);

        // Save to CSV file after removing the user
        exportToCsv();
    }
    /**
     * Method to check if a user exists in the user list using the userId.
     * @param userId The unique identifier of the user to be checked.
     * @return true if the user exists, false otherwise.
     */
    public static boolean userExists(String userId) {
        return userList.containsKey(userId);
    }
    /**
     * Method to get the user list.
     * @return The HashMap containing all users in the user list.
     */
    public static HashMap<String, User> getUserList() {
        return userList;
    }
    /**
     * Method to clear the user list.
     * This method removes all users from the user list.
     */
    public static void clearUserList() {
        userList.clear();
        
        // Save to CSV file after nuking the user list
        exportToCsv();
    }
    /**
     * Method to get the count of users in the user list.
     * @return The number of users in the user list.
     */
    public static int getUserCount() {
        return userList.size();
    }
    /**
     * Method to check if the user list is empty.
     * @return true if the user list is empty, false otherwise.
     */
    public static boolean isEmpty() {
        return userList.isEmpty();
    }
    /**
     * Method to print the user list.
     * This method prints all users in the user list to the console.
     */
    public static void printUserList() {
        for (User user : userList.values()) {
            // Print user details based on user type
            if (user instanceof HDBOfficer) {
                System.out.println("HDB Officer: " + user.getName());
            } else if (user instanceof Applicant) {
                System.out.println("Applicant: " + user.getName());
            } else if (user instanceof HDBManager) {
                System.out.println("HDB Manager: " + user.getName());
            } else {
                System.out.println("Unknown User Type: " + user.getName());
            }
        }
    }
    /**
     * Method to update a user in the user list.
     * This method replaces the existing user with the same userId with the new user object.
     * @param user The User object to be updated in the user list.
     */
    public static void updateUser(User user) {
        if (userList.containsKey(user.getUserId())) {
            userList.put(user.getUserId(), user);

            // Save to CSV file after adding the user
            exportToCsv();
        } else {
            System.out.println("User not found in the database.");
        }
    }
}
