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
import java.io.IOException;

public class UserDB {
    /**
     * UserDB class to manage the user database.
     * This class is responsible for adding, removing, and retrieving users from the database.
     * It uses a HashMap to store users with their userId as the key.
     * 
     * @see User
     */
    private HashMap<String, User> userList;

    /**
     * Constructor to initialize the UserDB object with a user list.
     * The user list is represented as a HashMap where the key is the userId and the value is the User object.
     */
    public UserDB() {
        userList = new HashMap<>();

        // Load users from CSV files
        processCsv("ApplicantList.csv", UserType.APPLICANT);
        processCsv("OfficerList.csv", UserType.HDB_OFFICER);
        processCsv("ManagerList.csv", UserType.HDB_MANAGER);
    }
    public void processCsv(String filename, UserType userType) {
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
    public void addUser(User user) {
        userList.put(user.getUserId(), user);
    }
    /**
     * Method to retrieve a user from the user list using the userId.
     * @param userId The unique identifier of the user to be retrieved.
     * @return The User object associated with the given userId, or null if not found.
     */
    public User getUser(String userId) {
        // Return either Applicant, HDB Officer or HDB Manager class on the user retrieval
        if (userList.get(userId) instanceof Applicant) {
            return (Applicant) userList.get(userId);
        } else if (userList.get(userId) instanceof HDBOfficer) {
            return (HDBOfficer) userList.get(userId);
        } else if (userList.get(userId) instanceof HDBManager) {
            return (HDBManager) userList.get(userId);
        }
        // If the user is not found, return null
        return null;
    }
    /**
     * Method to remove a user from the user list using the userId.
     * @param userId The unique identifier of the user to be removed.
     */
    public void removeUser(String userId) {
        userList.remove(userId);
    }
    /**
     * Method to check if a user exists in the user list using the userId.
     * @param userId The unique identifier of the user to be checked.
     * @return true if the user exists, false otherwise.
     */
    public boolean userExists(String userId) {
        return userList.containsKey(userId);
    }
    /**
     * Method to get the user list.
     * @return The HashMap containing all users in the user list.
     */
    public HashMap<String, User> getUserList() {
        return userList;
    }
    /**
     * Method to clear the user list.
     * This method removes all users from the user list.
     */
    public void clearUserList() {
        userList.clear();
    }
    /**
     * Method to get the count of users in the user list.
     * @return The number of users in the user list.
     */
    public int getUserCount() {
        return userList.size();
    }
    /**
     * Method to check if the user list is empty.
     * @return true if the user list is empty, false otherwise.
     */
    public boolean isEmpty() {
        return userList.isEmpty();
    }
    /**
     * Method to print the user list.
     * This method prints all users in the user list to the console.
     */
    public void printUserList() {
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
    public void updateUser(User user) {
        if (userList.containsKey(user.getUserId())) {
            userList.put(user.getUserId(), user);
        } else {
            System.out.println("User not found in the database.");
        }
    }
}
