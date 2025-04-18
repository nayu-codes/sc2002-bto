package bto.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bto.model.project.BTOProject;
import bto.model.project.FlatType;
import bto.model.user.Applicant;
import bto.model.application.BTOApplication;
import bto.model.application.ApplicationStatus;

public class ApplicationDB implements CsvDatabase {
    private static final String CSV_FILE_PATH = "resources/ApplicationList.csv"; // Path to the CSV file
    private static ArrayList<BTOApplication> applicationList = new ArrayList<>(); // List to store Application objects

    /**
     * Initializes the ApplicationDB by loading data from the CSV file.
     * This method reads the CSV file and populates the ApplicationList with
     * OfficerApplication objects.
     */
    public static void init() {
        // Read from CSV file and populate the ApplicationList
        readFromCSV();
        // Print the total number of Applications loaded from the CSV file
        System.out.println("Total Applications loaded: " + applicationList.size()); // TODO: Remove in production

        // Print the details of each Application loaded from the CSV file
        for (BTOApplication application : applicationList) {
            System.out.println("Application ID: " + application.getApplicationId() + ", Applicant Name: "
                    + application.getApplicant().getName() + ", Application Status: "
                    + application.getStatus().toString()
                    + application.getApplicationDate()); // TODO: Remove in production
        }
        // Export the ApplicationList to a CSV file
        exportToCSV();
    }

    /**
     * Read from CSV file and populate ApplicationList.
     * This method reads the CSV file and populates the ApplicationList with
     * OfficerApplication objects.
     */
    public static void readFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            // Skip the header line
            br.readLine();
            // Format: applicationId, applicant, projectName, flatType, applicationDate, bookingDate, status


            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", 7); // Split into 7 parts by comma,
                                                                                      // but ignore commas inside quotes
                // Parse the values from the CSV line
                int applicationId = Integer.parseInt(values[0].trim());

                String applicantName = values[1].trim().replace("\"", ""); // Remove quotes
                Applicant applicant = (Applicant) UserDB.getUserByName(applicantName); // Create Applicant object

                String projectName = values[2].trim().replace("\"", "");
                BTOProject project = BTOProjectDB.getBTOProjectByName(projectName); // Create BTOProject object

                String flatTypeString = values[3].trim().replace("\"", ""); // Remove quotes
                FlatType flatType = FlatType.fromString(flatTypeString); // Convert string to FlatType enum

                Date applicationDate;
                try {
                    applicationDate = new SimpleDateFormat("MM/dd/yyyy").parse(values[4].trim().replace("\"", "")); // Parse date string
                    
                } catch (ParseException e) {
                    applicationDate = null; // Set to null if parsing fails
                }

                Date bookingDate;
                try {
                    bookingDate = new SimpleDateFormat("MM/dd/yyyy").parse(values[5].trim().replace("\"", "")); // Parse date string
                } catch (ParseException e) {
                    bookingDate = null; // Set to null if parsing fails
                }

                String statusString = values[6].trim().replace("\"", ""); // Remove quotes
                ApplicationStatus status = ApplicationStatus.valueOf(statusString.toUpperCase()); // Convert string to ApplicationStatus enum

                // Create an BTOApplication object with the parsed values
                BTOApplication Application = new BTOApplication(applicationId, applicant, status, project, flatType, applicationDate, bookingDate);

                // Add the Enquiry object to the enquiryList HashMap
                applicationList.add(Application);
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage()); // TODO: Remove in production
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number: " + e.getMessage()); // TODO: Remove in production
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error parsing CSV line: " + e.getMessage()); // TODO: Remove in production
        }
    }

    /**
     * Exports the ApplicationList to a CSV file.
     * This method writes the contents of the ApplicationList to a CSV file.
     */
    public static void exportToCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            // Write the header line to the CSV file
            bw.write("applicationId, applicant, projectName, flatType, applicationDate, bookingDate, status\n");
            // Write each Application to the CSV file
            for (BTOApplication Application : applicationList) {
                StringBuilder sb = new StringBuilder();
                sb.append(Application.getApplicationId()).append(", ")
                        .append("\"").append(Application.getApplicant().getName()).append("\", ")
                        .append("\"").append(Application.getProject().getName()).append("\", ")
                        .append("\"").append(Application.getFlatType().getDisplayName()).append("\", ")
                        .append("\"").append(new SimpleDateFormat("MM/dd/yyyy").format(Application.getApplicationDate())).append("\", ");
                if (Application.getBookingDate() != null) {
                    sb.append("\"").append(new SimpleDateFormat("MM/dd/yyyy").format(Application.getBookingDate())).append("\", ");
                } else {
                    sb.append(", "); // Leave empty if booking date is null
                }
                sb.append("\"").append(Application.getStatus().toString()).append("\"\n");
                bw.write(sb.toString());
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage()); // TODO: Remove in production
        }
    }

    /**
     * Get the list of Applications by a specific Applicant.
     * 
     * @param userId The userId of the Applicant whose Applications are to be retrieved.
     * 
     * @return A list of Application objects associated with the specified
     *         Applicant.
     */
    public static ArrayList<BTOApplication> getApplicationsByApplicant(String userId) {
        ArrayList<BTOApplication> applicantApplications = new ArrayList<>();
        for (BTOApplication Application : applicationList) {
            if (Application.getApplicant().getUserId().equals(userId)) {
                applicantApplications.add(Application);
            }
        }
        return applicantApplications;
    }

    /**
     * Get the list of Applications by a specific BTOProject name.
     * 
     * @param projectName The name of the BTOProject whose Applications are to be
     *                   retrieved.
     * 
     * @return A list of Application objects associated with the specified
     *         BTOProject.
     */
    public static ArrayList<BTOApplication> getApplicationsByProjectName(String projectName) {
        ArrayList<BTOApplication> projectApplications = new ArrayList<>();
        for (BTOApplication Application : applicationList) {
            if (Application.getProject().getName().equals(projectName)) {
                projectApplications.add(Application);
            }
        }
        return projectApplications;
    }

    /**
     * Get the list of all Applications.
     * 
     * @return A list of all Application objects.
     */
    public static ArrayList<BTOApplication> getAllApplications() {
        return applicationList;
    }

    /**
     * Gets the number of Applications in the database.
     * 
     * @return The number of Applications in the database.
     */
    public static int getApplicationCount() {
        return applicationList.size();
    }

    /**
     * Adds a new Application to the database.
     * 
     * @param Application The Application to be added.
     * 
     * @return true if the Application was added successfully, false otherwise.
     */
    public static boolean addApplication(BTOApplication Application) {
        // Check if the Application already exists in the list
        for (BTOApplication existingApplication : applicationList) {
            if (existingApplication.getApplicationId() == Application.getApplicationId()) {
                return false; // Application already exists, do not add
            }
        }
        // Add the new Application to the list
        applicationList.add(Application);
        // Export the updated list to CSV
        exportToCSV();
        return true; // Application added successfully
    }

    /**
     * Updates the Application in the database.
     * 
     * @param Application The Application to be updated.
     * 
     * @return true if the Application was updated successfully, false otherwise.
     */
    public static boolean updateApplication(BTOApplication Application) {
        // Find the existing Application in the list
        for (int i = 0; i < applicationList.size(); i++) {
            if (applicationList.get(i).getApplicationId() == Application.getApplicationId()) {
                // Update the existing Application with the new values
                applicationList.set(i, Application);
                // Export the updated list to CSV
                exportToCSV();
                return true; // Application updated successfully
            }
        }
        return false; // Application not found, update failed
    }
}
