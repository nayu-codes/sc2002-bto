package bto.model.project;

import bto.database.UserDB;
import bto.model.user.HDBManager;
import bto.model.user.UserType;

import java.util.HashMap;
import java.util.List;
import java.util.Date;

public class BTOProject {
    private int projectId; // Unique identifier for the BTO project
    private String name;
    private String neighbourhood;
    private List<FlatType> flatType;
    private HashMap<FlatType, Integer> flatCount;
    private HashMap<FlatType, Integer> flatCountRemaining; // Backup of flat count for rollback
    private HashMap<FlatType, Integer> flatPrice;
    private Date applicationOpeningDate;
    private Date applicationClosingDate;
    private HDBManager projectManager;
    private List<String> assignedOfficers;
    private int availableOfficerSlots;
    private boolean visibility;

    /**
     * Constructor to initialise the BTOProject object with common attributes.
     * 
     * @param projectId The unique identifier for the BTO project.
     * @param name The name of the BTO project.
     * @param neighbourhood The neighbourhood where the BTO project is located.
     * @param flatType A List containing the type of flats available in the BTO project (e.g., "2 Room Flat", "3 Room Flat").
     * @param flatCount A HashMap containing the count of each flat type available/remaining in the project.
     * @param flatPrice A HashMap containing the price of each flat type available in the project.
     * @param applicationOpeningDate The date when applications for the BTO project open.
     * @param applicationClosingDate The date when applications for the BTO project close.
     * @param projectManager The HDBManager responsible for managing the BTO project.
     * @param assignedOfficers A list of HDBOfficers assigned to assist with the BTO project.
     * @param availableOfficerSlots The number of available slots for HDBOfficers to be assigned to the project.
     * @param visibility Indicates whether the project is visible to users or not.
     */
    public BTOProject(int projectId, String name, String neighbourhood, List<FlatType> flatType, HashMap<FlatType, Integer> flatCount,
                        HashMap<FlatType, Integer> flatPrice, Date applicationOpeningDate, Date applicationClosingDate,
                        HDBManager projectManager, List<String> assignedOfficers, int availableOfficerSlots, boolean visibility) {
        this.projectId = projectId;
        this.name = name;
        this.neighbourhood = neighbourhood;
        this.flatType = flatType;
        this.flatCount = flatCount;
        this.flatCountRemaining = new HashMap<>(flatCount); // initialize flatCountRemaining with the same values as flatCount
        this.flatPrice = flatPrice;
        this.applicationOpeningDate = applicationOpeningDate;
        this.applicationClosingDate = applicationClosingDate;
        this.projectManager = projectManager;
        this.assignedOfficers = assignedOfficers;
        this.availableOfficerSlots = availableOfficerSlots;
        this.visibility = visibility;
    }

    /**
     * Constructor for importing BTOProject objects from CSV file. Should ONLY be called from BTOProjectDB.readFromCSV().
     * 
     * @param projectId The unique identifier for the BTO project.
     * @param name The name of the BTO project.
     * @param neighbourhood The neighbourhood where the BTO project is located.
     * @param flatType A List containing the type of flats available in the BTO project (e.g., "2 Room Flat", "3 Room Flat").
     * @param flatCount A HashMap containing the count of each flat type available in the project.
     * @param flatCountRemaining A HashMap containing the count of each flat type remaining in the project.
     * @param flatPrice A HashMap containing the price of each flat type available in the project.
     * @param applicationOpeningDate The date when applications for the BTO project open.
     * @param applicationClosingDate The date when applications for the BTO project close.
     * @param projectManager The HDBManager responsible for managing the BTO project.
     * @param assignedOfficers A list of HDBOfficers assigned to assist with the BTO project.
     * @param availableOfficerSlots The number of available slots for HDBOfficers to be assigned to the project.
     * @param visibility Indicates whether the project is visible to users or not.
     */
    public BTOProject(int projectId, String name, String neighbourhood, List<FlatType> flatType, HashMap<FlatType, Integer> flatCount,
                        HashMap<FlatType, Integer> flatCountRemaining, HashMap<FlatType, Integer> flatPrice,
                        Date applicationOpeningDate, Date applicationClosingDate, HDBManager projectManager,
                        List<String> assignedOfficers, int availableOfficerSlots, boolean visibility) {
        this.projectId = projectId;
        this.name = name;
        this.neighbourhood = neighbourhood;
        this.flatType = flatType;
        this.flatCount = flatCount;
        this.flatCountRemaining = flatCountRemaining;
        this.flatPrice = flatPrice;
        this.applicationOpeningDate = applicationOpeningDate;
        this.applicationClosingDate = applicationClosingDate;
        this.projectManager = projectManager;
        this.assignedOfficers = assignedOfficers;
        this.availableOfficerSlots = availableOfficerSlots;
        this.visibility = visibility;
    }

    /**
     * Get the project ID.
     * @return The project ID.
     */
    public int getProjectId() {
        return projectId;
    }

    /**
     * Set the project ID.
     * @param projectId The project ID to set.
     */
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    /**
     * Get the name of the project.
     * @return The name of the project.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the project.
     * @param name The name of the project to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the neighbourhood of the project.
     * @return The neighbourhood of the project.
     */
    public String getNeighbourhood() {
        return neighbourhood;
    }

    /**
     * Set the neighbourhood of the project.
     * @param neighbourhood The neighbourhood to set.
     */
    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    /**
     * Get the list of flat types available in the project.
     * @return The list of flat types.
     */
    public List<FlatType> getFlatType() {
        return flatType;
    }

    /**
     * Set the list of flat types available in the project.
     * @param flatType The list of flat types to set.
     */
    public void setFlatType(List<FlatType> flatType) {
        this.flatType = flatType;
    }

    /**
     * Get the count of the queried flat type available in the project.
     * @param flatType The flat type to get the count for.
     * 
     * @return The count of the flat type.
     */
    public Integer getFlatCount(FlatType flatType) {
        return this.flatCount.get(flatType);
    }

    /**
     * Get the count of each flat type available in the project.
     * @return The HashMap {@link flatCount} containing the count of each flat type.
     */
    public HashMap<FlatType, Integer> getFlatCounts() {
        return flatCount;
    }

    /**
     * Set the count of each flat type available in the project.
     * @param flatType The flat type to set the count for.
     * @param newFlatCount The new count of the flat type.
     */
    public void setFlatCount(FlatType flatType, int newFlatCount) {
        if (this.flatCount.containsKey(flatType)) {
            this.flatCount.put(flatType, newFlatCount);
        } else {
            System.out.println("Flat type not found in the project.");
        }
    }

    /**
     * Get the price of each flat type in the project.
     * @return The HashMap {@link flatPrice} containing the price of each flat type.
     */
    public HashMap<FlatType, Integer> getFlatPrices() {
        return flatPrice;
    }

    /**
     * Get the price of the queried flat type in the project.
     * @param flatType The flat type to get the price for.
     * @return The price of the flat type.
     */
    public Integer getFlatPrice(FlatType flatType) {
        return this.flatPrice.get(flatType);
    }

    /**
     * Set the price of the queried flat type in the project.
     * @param flatType The flat type to set the price for.
     * @param newFlatPrice The new price of the flat type.
     */
    public void setFlatPrice(FlatType flatType, int newFlatPrice) {
        if (this.flatPrice.containsKey(flatType)) {
            this.flatPrice.put(flatType, newFlatPrice);
        } else {
            System.out.println("Flat type not found in the project.");
        }
    }

    /**
     * Get the application opening date for the project.
     * @return The application opening date.
     */
    public Date getApplicationOpeningDate() {
        return applicationOpeningDate;
    }

    /**
     * Set the application opening date for the project.
     * @param applicationOpeningDate The application opening date to set.
     */
    public void setApplicationOpeningDate(Date applicationOpeningDate) {
        this.applicationOpeningDate = applicationOpeningDate;
    }

    /**
     * Get the application closing date for the project.
     * @return The application closing date.
     */
    public Date getApplicationClosingDate() {
        return applicationClosingDate;
    }

    /**
     * Set the application closing date for the project.
     * @param applicationClosingDate The application closing date to set.
     */
    public void setApplicationClosingDate(Date applicationClosingDate) {
        this.applicationClosingDate = applicationClosingDate;
    }

    /**
     * Get the project manager for the project.
     * @return The HDBManager managing the project.
     */
    public HDBManager getProjectManager() {
        return projectManager;
    }

    /**
     * Set the project manager for the project.
     * @param projectManager The HDBManager to set as the project manager.
     */
    public void setProjectManager(HDBManager projectManager) {
        this.projectManager = projectManager;
    }

    /**
     * Get the list of assigned officers for the project.
     * @return The list of assigned officers.
     */
    public List<String> getAssignedOfficers() {
        return assignedOfficers;
    }

    /**
     * Set the list of assigned officers for the project. If just adding or removing, use {@link addAssignedOfficer} or {@link #removeAssignedOfficer} instead.
     * 
     * @param assignedOfficers The list of assigned officers to set.
     */
    public void setAssignedOfficers(List<String> assignedOfficers) {
        this.assignedOfficers = assignedOfficers;
    }

    /**
     * Add an officer to the list of assigned officers for the project.
     * @param officer The officer to add.
     */
    public void addAssignedOfficer(String officer) {
        if (assignedOfficers.size() >= availableOfficerSlots) {
            System.out.println("No available slots for officers in this project.");
            return;
        }
        // Ensure that officer exists
        if (UserDB.getUserByName(officer) == null) {
            System.out.println("Officer does not exist in the system.");
            return;
        }
        // Ensure that officer is of type HDBOfficer
        if (UserDB.getUserByName(officer).getUserType() != UserType.HDB_OFFICER) {
            System.out.println("Officer is not of type HDBOfficer.");
            return;
        }
        // Ensure that officer is not already assigned to the project
        if (assignedOfficers.contains(officer)) {
            System.out.println("Officer is already assigned to the project.");
            return;
        }
        assignedOfficers.add(officer);
    }

    /**
     * Remove an officer from the list of assigned officers for the project.
     * @param officer The officer to remove.
     */
    public void removeAssignedOfficer(String officer) {
        // Ensure that officer exists
        if(UserDB.getUserByName(officer) == null) {
            System.out.println("Officer does not exist in the system.");
            return;
        }
        // Ensure that officer is assigned to the project
        assignedOfficers.remove(officer);
    }

    /**
     * Get the number of available officer slots for the project.
     * @return The number of available officer slots.
     */
    public int getAvailableOfficerSlots() {
        return availableOfficerSlots;
    }

    /**
     * Set the number of available officer slots for the project.
     * @param availableOfficerSlots The number of available officer slots to set.
     */
    public void setAvailableOfficerSlots(int availableOfficerSlots) {
        this.availableOfficerSlots = availableOfficerSlots;
    }

    /**
     * Get the visibility status of the project.
     * @return The visibility status of the project.
     */
    public boolean getVisibility() {
        return visibility;
    }

    /**
     * Set the visibility status of the project.
     * @param visibility The visibility status to set.
     */
    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    /**
     * Get the remaining flat count for the queried flat type.
     * @param flatType The flat type to get the remaining count for.
     * 
     * @return The remaining count of the flat type.
     * @throws IllegalArgumentException if the flat type is not found in the project.
     */
    public int getFlatCountRemaining(FlatType flatType) throws IllegalArgumentException {
        if (!flatCountRemaining.containsKey(flatType)) {
            throw new IllegalArgumentException("Flat type not found in the project.");
        }
        return flatCountRemaining.get(flatType);
    }

    /**
     * Decrease the remaining flat count for the specified flat type by 1. Called when a flat is booked.
     * 
     * @param flatType The flat type to decrease the remaining count for.
     * 
     * @throws IllegalArgumentException if the flat type is not found in the project.
     */
    public void decreaseFlatCountRemaining(FlatType flatType) throws IllegalArgumentException {
        if (!flatCountRemaining.containsKey(flatType)) {
            throw new IllegalArgumentException("Flat type not found in the project.");
        }
        int currentCount = flatCountRemaining.get(flatType);
        if (currentCount > 0) {
            flatCountRemaining.put(flatType, currentCount - 1);
        } else {
            System.out.println("No more flats of type " + flatType.getDisplayName() + " available for booking.");
        }
    }

    /**
     * Increase the remaining flat count for the specified flat type by 1. Called when a flat is unbooked.
     * 
     * @param flatType The flat type to increase the remaining count for.
     * 
     * @throws IllegalArgumentException if the flat type is not found in the project.
     */
    public void increaseFlatCountRemaining(FlatType flatType) throws IllegalArgumentException {
        if (!flatCountRemaining.containsKey(flatType)) {
            throw new IllegalArgumentException("Flat type not found in the project.");
        }
        int currentCount = flatCountRemaining.get(flatType);
        flatCountRemaining.put(flatType, currentCount + 1);
    }
}
