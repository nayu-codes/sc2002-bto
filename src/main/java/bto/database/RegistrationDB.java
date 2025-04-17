package bto.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import bto.model.project.BTOProject;
import bto.model.registration.OfficerRegistration;
import bto.model.registration.RegistrationStatus;
import bto.model.user.HDBOfficer;

public class RegistrationDB {
    private static final String CSV_FILE_PATH = "resources/OfficerRegistrationList.csv"; // Path to the CSV file
    private static ArrayList<OfficerRegistration> registrationList = new ArrayList<>(); // List to store Registration objects

    /**
     * Initializes the RegistrationDB by loading data from the CSV file.
     * This method reads the CSV file and populates the registrationList with OfficerRegistration objects.
     */
    public static void init() {
        // Read from CSV file and populate the registrationList
        readFromCSV();
        // Print the total number of registrations loaded from the CSV file
        System.out.println("Total registrations loaded: " + registrationList.size()); // TODO: Remove in production

        // Print the details of each registration loaded from the CSV file
        for (OfficerRegistration registration : registrationList) {
            System.out.println("Registration ID: " + registration.getRegistrationId() + ", Officer Name: " + registration.getOfficer().getName() + "Project Name" + registration.getProject().getName() + ", Registration Status: " + registration.getRegistrationStatus().toString()); // TODO: Remove in production
        }
        // Export the registrationList to a CSV file
        exportToCSV();
    }

    /**
     * Read from CSV file and populate registrationList.
     * This method reads the CSV file and populates the registrationList with OfficerRegistration objects.
     */
    public static void readFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            // Skip the header line
            br.readLine();
            // Format: registrationId, officerName, projectName, status

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", 4); // Split into 4 parts by comma, but ignore commas inside quotes
                // Parse the values from the CSV line
                int enquiryId = Integer.parseInt(values[0].trim());

                String officerName = values[1].trim().replace("\"", ""); // Remove quotes
                HDBOfficer officer = (HDBOfficer) UserDB.getUserByName(officerName); // Create HDBOfficer object

                String projectName = values[2].trim().replace("\"", "");
                BTOProject project = BTOProjectDB.getBTOProjectByName(projectName); // Create BTOProject object

                String status = values[3].trim().replace("\"", ""); // Remove quotes
                RegistrationStatus registrationStatus = RegistrationStatus.valueOf(status.toUpperCase()); // Convert string to RegistrationStatus enum

                // Create an OfficerRegistration object with the parsed values
                OfficerRegistration registration = new OfficerRegistration(enquiryId, officer, registrationStatus, project);

                // Add the Enquiry object to the enquiryList HashMap               
                registrationList.add(registration);
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
     * Exports the registrationList to a CSV file.
     * This method writes the contents of the registrationList to a CSV file.
     */
    public static void exportToCSV(){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            // Write the header line to the CSV file
            bw.write("registrationId, officerName, projectName, status\n");
            // Write each registration to the CSV file
            for (OfficerRegistration registration : registrationList) {
                bw.write(registration.getRegistrationId() + ", " +
                registration.getOfficer().getName() + ", " +
                registration.getProject().getName() + ", " +
                registration.getRegistrationStatus().toString() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage()); // TODO: Remove in production
        }
    }

    /**
     * Get the list of registrations by a specific officer.
     * @param officer The HDBOfficer whose registrations are to be retrieved.
     * 
     * @return A list of OfficerRegistration objects associated with the specified officer.
     */
    public static ArrayList<OfficerRegistration> getRegistrationsByOfficer(HDBOfficer officer) {
        ArrayList<OfficerRegistration> officerRegistrations = new ArrayList<>();
        for (OfficerRegistration registration : registrationList) {
            if (registration.getOfficer().equals(officer)) {
                officerRegistrations.add(registration);
            }
        }
        return officerRegistrations;
    }

    /**
     * Get the list of all registrations.
     * 
     * @return A list of all OfficerRegistration objects.
     */
    public static ArrayList<OfficerRegistration> getAllRegistrations() {
        return registrationList;
    }

    /**
     * Adds a new registration to the database.
     * @param registration The registration to be added.
     * 
     * @return true if the registration was added successfully, false otherwise.
     */
    public static boolean addRegistration(OfficerRegistration registration) {
        // Check if the registration already exists in the list
        for (OfficerRegistration existingRegistration : registrationList) {
            if (existingRegistration.getRegistrationId() == registration.getRegistrationId()) {
                return false; // Registration already exists, do not add
            }
        }
        // Add the new registration to the list
        registrationList.add(registration);
        // Export the updated list to CSV
        exportToCSV();
        return true; // Registration added successfully
    }

    /**
     * Updates the registration in the database.
     * @param registration The registration to be updated.
     * 
     * @return true if the registration was updated successfully, false otherwise.
     */
    public static boolean updateRegistration(OfficerRegistration registration) {
        // Find the existing registration in the list
        for (int i = 0; i < registrationList.size(); i++) {
            if (registrationList.get(i).getRegistrationId() == registration.getRegistrationId()) {
                // Update the existing registration with the new values
                registrationList.set(i, registration);
                // Export the updated list to CSV
                exportToCSV();
                return true; // Registration updated successfully
            }
        }
        return false; // Registration not found, update failed
    }
}
