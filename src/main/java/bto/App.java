package bto;

import bto.model.user.User;
import bto.model.user.Applicant;
import bto.model.user.HDBOfficer;
import bto.model.user.HDBManager;
import bto.model.user.UserType;
import bto.model.user.MaritalStatus;
import bto.database.UserDB;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }
    /**
     * Main class for the BTO application system.
     * This class contains the main method to run the application.
     * 
     * @param args
     */
    public static void main(String[] args) {
        // Main method to run the application
        System.out.println("Welcome to the BTO Application System!");
        // You can add more functionality here, such as initializing the user interface
        // or loading data.

        // Call UserDB
        UserDB userDB = new UserDB();

        // Print all users in the user list
        System.out.println("User List:");
        userDB.printUserList();
        
    }
}
