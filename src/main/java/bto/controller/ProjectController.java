package bto.controller;

import bto.model.project.BTOProject;
import bto.model.user.HDBManager;
import bto.model.user.User;
import bto.model.user.UserType;

import java.util.Date;

public class ProjectController {
    private ProjectController(){} // Prevents Instantiation

    /**
     * Creates a new project submitted by an HDB Manager.
     * 
     * @param user The HDB Manager creating the project.
     * @param project The BTOProject to be created.
     */
    public static void createProject(User user, BTOProject project) {
        // Check if project is null or not
        if (project == null) {
            System.out.println("Project cannot be null.");
            return;
        }

        // Check if user is an HDB Manager
        if (user.getUserType() != UserType.HDB_MANAGER) {
            System.out.println("Only HDB Managers can create projects.");
            return;
        }

        HDBManager manager = (HDBManager) user;
        try {
            manager.createProject(project);
            System.out.println("Project created successfully!");
        } catch (IllegalStateException e) {
            System.out.println("Failed to create project: " + e.getMessage());
            return;
        }
        
    }

    /**
     * Edits an existing project submitted by an HDB Manager.
     * 
     * @param user The HDB Manager editing the project.
     * @param project The BTOProject to be edited.
     */
    public static void editProject(User user, BTOProject project) {
        // Check if project is null or not
        if (project == null) {
            System.out.println("Project cannot be null.");
            return;
        }

        // Check if user is an HDB Manager
        if (user.getUserType() != UserType.HDB_MANAGER) {
            System.out.println("Only HDB Managers can edit projects.");
            return;
        }

        HDBManager manager = (HDBManager) user;
        try {
            manager.editProject(project);
            System.out.println("Project edited successfully!");
        } catch (IllegalStateException e) {
            System.out.println("Failed to edit project: " + e.getMessage());
        } catch (IllegalAccessException e) {
            System.out.println("Failed to edit project: " + e.getMessage());
        }
    }

    /**
     * Deletes an existing project submitted by an HDB Manager.
     * 
     * @param user The HDB Manager deleting the project.
     * @param project  The BTOProject to be deleted.
     */
    public static void deleteProject(User user, BTOProject project) {
        // Check if project is null or not
        if (project == null) {
            System.out.println("Project cannot be null.");
            return;
        }

        // Check if user is an HDB Manager
        if (user.getUserType() != UserType.HDB_MANAGER) {
            System.out.println("Only HDB Managers can delete projects.");
            return;
        }

        HDBManager manager = (HDBManager) user;
        try {
            if (manager.deleteProject(project)) {
                System.out.println("Project deleted successfully!");
            } else {
                System.out.println("Failed to delete project. Please try again.");
                return;
            }
        } catch (IllegalStateException e) {
            System.out.println("Failed to delete project: " + e.getMessage());
        } catch (IllegalAccessException e) {
            System.out.println("Failed to delete project: " + e.getMessage());
        }
    }

    /**
     * Toggles the visibility of a project submitted by an HDB Manager.
     * 
     * @param user The HDB Manager toggling the visibility of the project.
     * @param project The BTOProject whose visibility is to be toggled.
     */
    public static void toggleVisibility(User user, BTOProject project) {
        // Check if project is null or not
        if (project == null) {
            System.out.println("Project cannot be null.");
            return;
        }

        // Check if user is an HDB Manager
        if (user.getUserType() != UserType.HDB_MANAGER) {
            System.out.println("Only HDB Managers can toggle project visibility.");
            return;
        }

        HDBManager manager = (HDBManager) user;
        try {
            boolean newVisibility = manager.toggleProjectVisibility(project);
            System.out.println("Project visibility toggled successfully!");
            System.out.println("New visibility for " + project.getName() + ": " + (newVisibility ? "Visible" : "Hidden"));
        } catch (IllegalStateException e) {
            System.out.println("Failed to toggle project visibility: " + e.getMessage());
        } catch (IllegalAccessException e) {
            System.out.println("Failed to toggle project visibility: " + e.getMessage());
        }
    }

    /**
     * Gets the current "status" of a project.
     * 
     * @param project The BTOProject whose status is to be retrieved.
     * 
     * @return The current status of the project as a String. ("Past", "Current", "Upcoming")
     *         Returns null if status cannot be retrieved.
     */
    public static String getProjectStatus(BTOProject project) {
        if (project == null) {
            System.out.println("Project cannot be null.");
            return null;
        }

        String currentStatus = null;

        if (project.getApplicationClosingDate().before(new Date())) {
            currentStatus = "Past";
        } else if (project.getApplicationOpeningDate().after(new Date())) {
            currentStatus = "Upcoming";
        } else {
            currentStatus = "Current";
        }

        return currentStatus;
    }
}
