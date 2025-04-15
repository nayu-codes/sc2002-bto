package bto;

import bto.model.user.User;
import bto.model.project.BTOProject;
import bto.model.user.Applicant;
import bto.model.user.HDBOfficer;
import bto.model.user.HDBManager;
import bto.model.user.UserType;
import bto.model.user.MaritalStatus;

import bto.database.UserDB;
import bto.database.BTOProjectDB;

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
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Main method to run the application
        System.out.println("Welcome to the BTO Application System!");
        // You can add more functionality here, such as initializing the user interface
        // or loading data.

        // Initialise the user database
        UserDB userDB = new UserDB();

        // Print all users in the user list
        System.out.println("User List:");
        UserDB.printUserList();

        // Initialise the project database
        BTOProjectDB btoProjectDB = new BTOProjectDB();

        // Print all BTO projects in the project list
        System.out.println("BTO Project List:");
        BTOProjectDB.printBTOProjectList();
        
    }
}
