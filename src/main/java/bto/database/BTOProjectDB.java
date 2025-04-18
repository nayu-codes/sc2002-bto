package bto.database;

import bto.model.project.BTOProject;
import bto.model.project.FlatType;
import bto.model.user.HDBManager;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class BTOProjectDB implements CsvDatabase {
    private static final String CSV_FILE_PATH = "resources/BTOProjectList.csv"; // Path to the CSV file
    public static HashMap<Integer, BTOProject> btoProjectList = new HashMap<>();
    public static Integer totalProjects = 0;

    public BTOProjectDB() {
    }

    public static void init() {
        try {
            // Read from CSV file and populate the btoProjectList
            readFromCsv();
        } catch (IOException e) {
            System.out.println("Error reading from CSV file: " + e.getMessage());
            readFromDefaultCsv();
        }
        exportToCsv();
        System.out.println("Total BTO projects loaded: " + totalProjects); // TODO: Remove in production
    }

    public static void readFromDefaultCsv() {
        // Read the BTO projects from the CSV file and populate the btoProjectList
        String filename = "resources/ProjectList.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            // Skip the header line
            br.readLine();
            // Format: Project Name,Neighborhood,Type 1,Number of units for Type 1,Selling price for Type 1,Type 2,Number of units for Type 2,Selling price for Type 2,Application opening date,Application closing date,Manager,Officer Slot,Officer
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); // Split by comma, but ignore commas inside quotes
                if (values.length == 13) {
                    String name = values[0].trim();
                    String neighbourhood = values[1].trim();
                    List<FlatType> flatType = List.of(FlatType.fromString(values[2].trim()), FlatType.fromString(values[5].trim()));
                    HashMap<FlatType, Integer> flatCount = new HashMap<>();
                    flatCount.put(FlatType.fromString(values[2].trim()), Integer.parseInt(values[3].trim()));
                    flatCount.put(FlatType.fromString(values[5].trim()), Integer.parseInt(values[6].trim()));
                    HashMap<FlatType, Integer> flatPrice = new HashMap<>();
                    flatPrice.put(FlatType.fromString(values[2].trim()), Integer.parseInt(values[4].trim()));
                    flatPrice.put(FlatType.fromString(values[5].trim()), Integer.parseInt(values[7].trim()));
                    Date applicationOpeningDate = new SimpleDateFormat("MM/dd/yyyy").parse(values[8].trim());
                    Date applicationClosingDate = new SimpleDateFormat("MM/dd/yyyy").parse(values[9].trim());
                    HDBManager projectManager = (HDBManager) UserDB.getUserByName(values[10].trim());
                    List<String> assignedOfficers = List.of(values[12].trim().split(","));
                    int availableOfficerSlots = Integer.parseInt(values[11].trim());
                    boolean visibility = true; // Default visibility

                    // Create a new BTOProject object and add it to the list
                    BTOProject project = new BTOProject(totalProjects, name, neighbourhood, flatType, flatCount, flatPrice,
                            applicationOpeningDate, applicationClosingDate, projectManager, assignedOfficers,
                            availableOfficerSlots, visibility);
                    btoProjectList.put(totalProjects, project);
                    totalProjects++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        } catch (java.text.ParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Error processing flat type: " + e.getMessage());
        }
    }

    /**
     * * Reads BTO projects from a CSV file and populates the btoProjectList.
     * The CSV file should be in the format:
     * projectName,neighborhood,type1,type1Count,type1Price,typ1CountRemaining,type2,type2Count,type2Price,
     * type2CountRemaining,applicationOpeningDate,applicationClosingDate,manager,officerSlot,officer
     * where Type 1 and Type 2 are the flat types (e.g., "2 Room Flat", "3 Room
     * Flat").
     */
    public static void readFromCsv() throws IOException {
        // Read the BTO projects from the CSV file and populate the btoProjectList
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            // Skip the header line
            br.readLine();
            // Format:
            // projectName,neighborhood,type1,type1Count,type1Price,typ1CountRemaining,type2,type2Count,type2Price,
            // type2CountRemaining,applicationOpeningDate,applicationClosingDate,manager,officerSlot,officer,visibility
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); // Split by comma, but ignore commas inside quotes
                if (values.length == 16) {
                    String name = values[0].trim().replace("\"", "");
                    String neighbourhood = values[1].trim().replace("\"", "");
                    
                    List<FlatType> flatType = List.of(FlatType.fromString(values[2].trim()), FlatType.fromString(values[6].trim()));
                    HashMap<FlatType, Integer> flatCount = new HashMap<>();
                    flatCount.put(FlatType.fromString(values[2].trim()), Integer.parseInt(values[3].trim()));
                    flatCount.put(FlatType.fromString(values[6].trim()), Integer.parseInt(values[7].trim()));

                    HashMap<FlatType, Integer> flatCountRemaining = new HashMap<>();
                    flatCountRemaining.put(FlatType.fromString(values[2].trim()), Integer.parseInt(values[5].trim()));
                    flatCountRemaining.put(FlatType.fromString(values[6].trim()), Integer.parseInt(values[9].trim()));

                    HashMap<FlatType, Integer> flatPrice = new HashMap<>();
                    flatPrice.put(FlatType.fromString(values[2].trim()), Integer.parseInt(values[4].trim()));
                    flatPrice.put(FlatType.fromString(values[6].trim()), Integer.parseInt(values[8].trim()));

                    Date applicationOpeningDate = new SimpleDateFormat("MM/dd/yyyy").parse(values[10].trim());
                    Date applicationClosingDate = new SimpleDateFormat("MM/dd/yyyy").parse(values[11].trim());
                    HDBManager projectManager = (HDBManager) UserDB.getUserByName(values[12].trim());
                    List<String> assignedOfficers = List.of(values[14].trim().split(","));
                    int availableOfficerSlots = Integer.parseInt(values[13].trim());
                    boolean visibility = Boolean.parseBoolean(values[15].trim());

                    // Create a new BTOProject object and add it to the list
                    BTOProject project = new BTOProject(totalProjects, name, neighbourhood, flatType, flatCount, flatCountRemaining, 
                            flatPrice, applicationOpeningDate, applicationClosingDate, projectManager,
                            assignedOfficers, availableOfficerSlots, visibility);
                    btoProjectList.put(totalProjects, project);
                    totalProjects++;
                }
            }
        } catch (IOException e) {
            throw e;
        } catch (java.text.ParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Error processing flat type: " + e.getMessage());
        }
    }

    /**
     * Exports the BTO project list to a CSV file.
     * The CSV file will be in the format:
     * projectName,neighborhood,type1,type1Count,type1Price,typ1CountRemaining,type2,type2Count,type2Price,
     * type2CountRemaining,applicationOpeningDate,applicationClosingDate,manager,officerSlot,officer,visibility
     * where Type 1 and Type 2 are the flat types (e.g., "2-Room", "3-Room").
     */
    public static void exportToCsv() {
        // Export the BTO projects to a CSV file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            // Write the header line
            bw.write("projectName,neighborhood,type1,type1Count,type1Price,typ1CountRemaining,type2,type2Count,type2Price, type2CountRemaining,applicationOpeningDate,applicationClosingDate,manager,officerSlot,officer,visibility");
            bw.newLine();
            for (BTOProject project : btoProjectList.values()) {
                StringBuilder sb = new StringBuilder();
                sb.append("\"").append(project.getName()).append("\",")
                        .append("\"").append(project.getNeighbourhood()).append("\",")
                        .append(project.getFlatType().get(0).getDisplayName()).append(",")
                        .append(project.getFlatCount(project.getFlatType().get(0))).append(",")
                        .append(project.getFlatPrice(project.getFlatType().get(0))).append(",")
                        .append(project.getFlatCountRemaining(project.getFlatType().get(0))).append(",")
                        .append(project.getFlatType().get(1).getDisplayName()).append(",")
                        .append(project.getFlatCount(project.getFlatType().get(1))).append(",")
                        .append(project.getFlatPrice(project.getFlatType().get(1))).append(",")
                        .append(project.getFlatCountRemaining(project.getFlatType().get(1))).append(",")
                        .append(new SimpleDateFormat("MM/dd/yyyy").format(project.getApplicationOpeningDate())).append(",")
                        .append(new SimpleDateFormat("MM/dd/yyyy").format(project.getApplicationClosingDate())).append(",")
                        .append(project.getProjectManager().getName()).append(",")
                        .append(project.getAvailableOfficerSlots()).append(",")
                        .append(String.join(",", project.getAssignedOfficers())).append(",")
                        .append(project.getVisibility());
                bw.write(sb.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to the CSV file: " + e.getMessage());
        }
    }

    public static HashMap<Integer, BTOProject> getBTOProjectList() {
        return btoProjectList;
    }

    /**
     * Returns the total number of BTO projects.
     * 
     * @return The total number of BTO projects.
     */
    public static int getTotalProjects() {
        return totalProjects;
    }

    /**
     * Returns the BTO project with the specified ID.
     * 
     * @param id The ID of the BTO project.
     * @return The BTO project with the specified ID, or null if not found.
     */
    public static BTOProject getBTOProjectById(int id) {
        return btoProjectList.get(id);
    }

    /**
     * Returns the BTO project with the specified name.
     * 
     * @param name The name of the BTO project.
     * @return The BTO project with the specified name, or null if not found.
     */
    public static BTOProject getBTOProjectByName(String name) {
        for (BTOProject project : btoProjectList.values()) {
            if (project.getName().equalsIgnoreCase(name)) {
                return project;
            }
        }
        return null;
    }

    /**
     * Returns BTO projects with visibility 'on'.
     * 
     * @return The BTO projects with the visibility 'on', or null if not found.
     */
    public static ArrayList<BTOProject> getVisibleProjects() {
        ArrayList<BTOProject> visibleProjects = new ArrayList<>();
        for (BTOProject project : btoProjectList.values()) {
            if (project.getVisibility()) {
                visibleProjects.add(project);
            }
            return visibleProjects;
        }
        return null;
    }

    /**
     * Adds a new BTO project to the list.
     * @param project The BTO project to add.
     * 
     * @return The ID of the newly added BTO project.
     */
    public static int addBTOProject(BTOProject project) {
        int newId = totalProjects++;
        btoProjectList.put(newId, project);
        return newId;
    }

    /**
     * Updates the BTO project with the specified ID.
     * 
     * @param id      The ID of the BTO project to update.
     * @param project The updated BTO project.
     * @return True if the update was successful, false otherwise.
     */
    public static boolean updateBTOProject(int id, BTOProject project) {
        if (btoProjectList.containsKey(id)) {
            btoProjectList.put(id, project);
            return true;
        }
        return false;
    }

    /**
     * Deletes the BTO project with the specified ID.
     * 
     * @param id The ID of the BTO project to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    public static boolean deleteBTOProject(int id) {
        if (btoProjectList.containsKey(id)) {
            btoProjectList.remove(id);
            return true;
        }
        return false;
    }

    /**
     * Prints the BTO project list to the console.
     */
    public static void printBTOProjectList() {
        for (BTOProject project : btoProjectList.values()) {
            System.out.println("ID: " + project.getProjectId() + ", Name: " + project.getName() +
                    ", Neighbourhood: " + project.getNeighbourhood() +
                    ", Flat Type: " + project.getFlatType() +
                    ", Flat Count: " + project.getFlatCounts() +
                    ", Flat Price: " + project.getFlatPrices() +
                    ", Application Opening Date: " + new SimpleDateFormat("MM/dd/yyyy").format(project.getApplicationOpeningDate()) +
                    ", Application Closing Date: " + new SimpleDateFormat("MM/dd/yyyy").format(project.getApplicationClosingDate()) +
                    ", Manager: " + project.getProjectManager().getName() +
                    ", Officer Slots: " + project.getAvailableOfficerSlots() +
                    ", Assigned Officers: " + String.join(", ", project.getAssignedOfficers()));
        }
    }
}
