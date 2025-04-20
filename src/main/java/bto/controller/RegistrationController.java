package bto.controller;

import bto.database.RegistrationDB;
import bto.model.user.HDBOfficer;
import bto.model.user.HDBManager;
import bto.model.user.User;
import bto.model.user.UserType;
import bto.model.project.BTOProject;
import bto.model.registration.OfficerRegistration;

import java.util.List;

public class RegistrationController {
    public RegistrationController() {
        // Constructor for RegistrationController
    }

    /**
     * Creates a new registration request for a project submitted by an officer.
     * 
     * @param user The officer submitting the registration request.
     * @param project The BTOProject related to the registration request.
     */
    public static void registerForProject(User user, BTOProject project) {
        // Check if user is an officer
        if (user.getUserType() != UserType.HDB_OFFICER) {
            System.out.println("Only officers can register to manage a projects.");
            return;
        }

        // Check if project is null or not
        if (project == null) {
            System.out.println("Project cannot be null.");
            return;
        }

        // Re-initialise the user as an HDBOfficer
        HDBOfficer officer = (HDBOfficer) user; // Assuming user is of type HDBOfficer

        // Submit registration request
        try {
            officer.registerForProject(project);
            System.out.println("Registration request submitted successfully!");
        } catch (IllegalStateException e) {
            System.out.println("Failed to submit registration request: " + e.getMessage());
            return;
        }
    }

    /**
     * Approves a registration request for a project submitted by an officer.
     * 
     * @param user The HDB Manager approving the registration request.
     * @param registration The OfficerRegistration object representing the registration request.
     */
    public static void approveRegistration(User user, OfficerRegistration registration) {
        // Check if user is an officer
        if (user.getUserType() != UserType.HDB_MANAGER) {
            System.out.println("Only HDB Managers can approve registration requests.");
            return;
        }

        // Check if registration is null or not
        if (registration == null) {
            System.out.println("Registration cannot be null.");
            return;
        }

        // Re-initialise the user as an HDBManager
        HDBManager manager = (HDBManager) user;

        // Approve registration request
        try {
            manager.approveRegistration(registration);
            System.out.println("Registration request approved successfully!");
        } catch (IllegalStateException e) {
            System.out.println("Failed to approve registration request: " + e.getMessage());
            return;
        }
    }

    /**
     * Rejects a registration request for a project submitted by an officer.
     * 
     * @param user The HDB Manager rejecting the registration request.
     * @param registration The OfficerRegistration object representing the registration request.
     */
    public static void rejectRegistration(User user, OfficerRegistration registration) {
        // Check if user is an officer
        if (user.getUserType() != UserType.HDB_MANAGER) {
            System.out.println("Only HDB Managers can reject registration requests.");
            return;
        }

        // Check if registration is null or not
        if (registration == null) {
            System.out.println("Registration cannot be null.");
            return;
        }

        // Re-initialise the user as an HDBManager
        HDBManager manager = (HDBManager) user;

        // Reject registration request
        try {
            manager.rejectRegistration(registration);
            System.out.println("Registration request rejected successfully!");
        } catch (IllegalStateException e) {
            System.out.println("Failed to reject registration request: " + e.getMessage());
            return;
        }
    }

    /**
     * Retrieves all registrations made by a specific officer.
     * 
     * @param user The officer whose registrations are to be retrieved.
     * 
     * @return List of OfficerRegistration objects associated with the specified officer.
     */
    public static List<OfficerRegistration> getRegistrationsByOfficer(User user) {
        // Check if user is an officer
        if (user.getUserType() != UserType.HDB_OFFICER) {
            System.out.println("Only officers can view their registrations.");
            return null;
        }

        // Re-initialise the user as an HDBOfficer
        HDBOfficer officer = (HDBOfficer) user; // Assuming user is of type HDBOfficer

        // Get registrations by officer
        return RegistrationDB.getRegistrationsByOfficer(officer);
    }

    /**
     * Retrieves all registrations made by all officers.
     * 
     * @param user The HDB Manager requesting the registrations.
     * 
     * @return List of OfficerRegistration objects associated with all officers.
     */
    public static List<OfficerRegistration> getAllRegistrations(User user) {
        return RegistrationDB.getAllRegistrations();
    }
}
