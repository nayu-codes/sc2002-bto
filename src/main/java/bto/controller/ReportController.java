package bto.controller;

import bto.database.BTOProjectDB;
import bto.model.user.HDBOfficer;
import bto.model.user.HDBManager;
import bto.model.user.User;
import bto.model.user.UserType;
import bto.model.user.MaritalStatus;
import bto.model.application.BTOApplication;
import bto.model.project.BTOProject;
import bto.model.project.FlatType;

public class ReportController {
    /**
     * Generate a receipt for a flat booking.
     * 
     * @param user The HDB Officer generating the receipt.
     * @param application The BTOApplication for which the receipt is generated.
     */
    public static void generateReceipt(User user, BTOApplication application) {
        // Check if application is null or not
        if (application == null) {
            System.out.println("Application cannot be null.");
            return;
        }

        // Check if user is a HDB Officer
        if (user.getUserType() != UserType.HDB_OFFICER) {
            System.out.println("Only HDB Officers can generate receipts.");
            return;
        }

        // Check if HDB Officer is generating for a project they manage
        HDBOfficer officer = (HDBOfficer) user;
        boolean isManaged = false;
        for (BTOProject project : BTOProjectDB.getBTOProjectsByOfficer(officer)) {
            if (project.getName().equals(application.getProject().getName()) && project.getAssignedOfficers().contains(officer.getName())) {
                isManaged = true;
                break;
            }
        }
        if (!isManaged) {
            System.out.println("You are not authorized to generate receipts for this application.");
            return;
        }

        // Generate receipt
        officer.generateReceiptForApplicant(application.getApplicant());
    }

    /**
     * Generate a report for all booked applications.
     * 
     * @param user The HDB Manager generating the report.
     */
    public static void generateReport(User user) {
        // Check if user is a HDB Manager
        if (user.getUserType() != UserType.HDB_MANAGER) {
            System.out.println("Only HDB Managers can generate reports.");
            return;
        }

        // Generate report for all booked applications
        HDBManager manager = (HDBManager) user;
        manager.generateReport();
    }

    /**
     * Generate a report for booked applications with filters. Setting the respective filter values to null will ignore the filter.
     * 
     * @param user The HDB Manager generating the report.
     * @param minAge The minimum age of applicants.
     * @param maxAge The maximum age of applicants.
     * @param maritalStatus The marital status of applicants.
     * @param projectName The name of the project.
     * @param flatType The type of flat booked.
     */
    public static void generateFilteredReport(User user, Integer minAge, Integer maxAge, MaritalStatus maritalStatus, String projectName, FlatType flatType) {
        // Check if user is a HDB Manager
        if (user.getUserType() != UserType.HDB_MANAGER) {
            System.out.println("Only HDB Managers can generate reports.");
            return;
        }

        // Generate report for booked applications with filters
        HDBManager manager = (HDBManager) user;
        manager.generateReport(minAge, maxAge, maritalStatus, projectName, flatType);
    }
}
