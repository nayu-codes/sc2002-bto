package bto.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bto.model.enquiry.Enquiry;
import bto.model.enquiry.EnquiryMessage;

public class EnquiryDB {
    /**
     * UserDB class to manage the user database.
     * This class is responsible for adding, removing, and retrieving users from the database.
     * It uses a HashMap to store users with their userId as the key.
     * 
     * @see Enquiry
     */
    private static ArrayList<Enquiry> enquiryList = new ArrayList<>(); // List to store Enquiry objects
    private static final String CSV_FILE_PATH = "resources/EnquiryList.csv"; // Path to the CSV file

    /**
     * Initializes the EnquiryDB by loading data from the CSV file.
     * This method reads the CSV file and populates the EnquiryList HashMap with Enquiry objects.
     */
    public static void init() {
        // Read from CSV file and populate the enquiryList HashMap
        readFromCSV();
        // Print the total number of enquiries loaded from the CSV file
        System.out.println("Total enquiries loaded: " + enquiryList.size()); // TODO: Remove in production

        // Print the details of each enquiry loaded from the CSV file
        for (Enquiry enquiry : enquiryList) {
            System.out.println("Enquiry ID: " + enquiry.getEnquiryId() + ", Applicant Name: " + enquiry.getApplicantName() + ", Project Name: " + enquiry.getProjectName() + ", Applicant Message: " + enquiry.getApplicantMessage().getMessage() + ", Reply Message: " + (enquiry.getReplyMessage() != null ? enquiry.getReplyMessage().getMessage() : "No reply yet")); // TODO: Remove in production
        }
        // Export the enquiryList HashMap to a CSV file
        exportToCSV();
    }

    /**
     * Read from CSV file and populate EnquiryList HashMap.
     * This method reads the CSV file and populates the EnquiryList HashMap with Enquiry objects.
     */
    public static void readFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            // Skip the header line
            br.readLine();
            // Format: enquiryId, applicantName, projectName, applicantMessage, applicantMessageDate, replyName, replyMessage, replyMessageDate
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", 8); // Split into 8 parts by comma, but ignore commas inside quotes
                // Parse the values from the CSV line
                int enquiryId = Integer.parseInt(values[0].trim());
                String applicantName = values[1].trim().replace("\"", ""); // Remove quotes
                String projectName = values[2].trim().replace("\"", "");
                String applicantMessage = values[3].trim().replace("\"", "");
                Date applicantMessageDate;
                try {
                    applicantMessageDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(values[4].trim().replace("\"", ""));
                } catch (Exception e) {
                    // Default to current date if parsing fails
                    applicantMessageDate = new Date();
                }
                String replyName = values[5].trim().replace("\"", "");
                String replyMessage = values[6].trim().replace("\"", "");
                Date replyMessageDate;
                if (replyMessage.equals("")) {
                    replyMessage = null; // Set to null if empty
                    replyMessageDate = null; // Set to null if empty
                } else {
                    try {
                        replyMessageDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(values[7].trim().replace("\"", ""));
                    } catch (Exception e) {
                        // Default to current date if parsing fails
                        replyMessageDate = new Date();
                    }
                }

                // Parse messages into EnquiryMessage objects
                EnquiryMessage applicantMessageObj = new EnquiryMessage(applicantName, applicantMessage, applicantMessageDate);
                EnquiryMessage replyMessageObj = new EnquiryMessage(replyName, replyMessage, replyMessageDate);

                // Create an Enquiry object with the parsed values
                Enquiry project = new Enquiry(enquiryId, applicantName, projectName, applicantMessageObj, replyMessageObj);

                // Add the Enquiry object to the enquiryList HashMap               
                enquiryList.add(project);
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
     * Exports the EnquiryList HashMap to a CSV file.
     * This method writes the data from the EnquiryList HashMap to a CSV file.
     */
    public static void exportToCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            // Write the header line
            bw.write("enquiryId, applicantName, projectName, applicantMessage, applicantMessageDate, replyName, replyMessage, replyMessageDate\n");
            // Write each enquiry to the CSV file
            for (Enquiry enquiry : enquiryList) {
                StringBuilder sb = new StringBuilder();
                sb.append(enquiry.getEnquiryId()).append(", ");
                sb.append("\"").append(enquiry.getApplicantName()).append("\", ");
                sb.append("\"").append(enquiry.getProjectName()).append("\", ");
                sb.append("\"").append(enquiry.getApplicantMessage().getMessage()).append("\", ");
                sb.append(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(enquiry.getApplicantMessage().getDateTime())).append(", ");
                if (enquiry.getReplyMessage() != null) {
                    sb.append("\"").append(enquiry.getReplyMessage().getAuthorName()).append("\", ");
                    sb.append("\"").append(enquiry.getReplyMessage().getMessage()).append("\", ");
                    sb.append(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(enquiry.getReplyMessage().getDateTime())).append("\n");
                } else {
                    sb.append(", , , \n"); // Empty reply fields
                }
                bw.write(sb.toString());
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage()); // TODO: Remove in production
        }
    }

    /**
     * Adds a new enquiry to the database.
     * @param enquiry The enquiry to be added.
     * 
     * @return true if the enquiry was added successfully, false otherwise.
     */
    public static boolean addEnquiry(Enquiry enquiry) {
        // Check if the enquiry already exists in the list
        for (Enquiry existingEnquiry : enquiryList) {
            if (existingEnquiry.getEnquiryId() == enquiry.getEnquiryId()) {
                return false; // Enquiry already exists, do not add
            }
        }
        // Add the new enquiry to the list
        enquiryList.add(enquiry);
        // Export the updated list to CSV
        exportToCSV();
        return true; // Enquiry added successfully
    }

    /**
     * Gets the list of all enquiries.
     * 
     * @return The list of all enquiries.
     */
    public static ArrayList<Enquiry> getEnquiryList() {
        return enquiryList;
    }

    /**
     * Gets the enquiry by its ID.
     * 
     * @param enquiryId The ID of the enquiry to be retrieved.
     * 
     * @return The enquiry with the specified ID, or null if not found.
     */
    public static Enquiry getEnquiryById(int enquiryId) {
        for (Enquiry enquiry : enquiryList) {
            if (enquiry.getEnquiryId() == enquiryId) {
                return enquiry; // Return the found enquiry
            }
        }
        return null; // Enquiry not found
    }

    /**
     * Updates the enquiry in the database.
     * @param enquiry The enquiry to be updated.
     * 
     * @return true if the enquiry was updated successfully, false otherwise.
     */
    public static boolean updateEnquiry(Enquiry enquiry) {
        // Find the existing enquiry in the list
        for (int i = 0; i < enquiryList.size(); i++) {
            if (enquiryList.get(i).getEnquiryId() == enquiry.getEnquiryId()) {
                // Update the enquiry in the list
                enquiryList.set(i, enquiry);
                // Export the updated list to CSV
                exportToCSV();
                return true; // Enquiry updated successfully
            }
        }
        return false; // Enquiry not found, update failed
    }
}
