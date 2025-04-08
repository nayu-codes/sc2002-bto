package bto.model.project;

import bto.model.user.HDBManager;
import bto.model.user.HDBOfficer;

import java.util.HashMap;
import java.util.List;
import java.util.Date;

public class BTOProject {
    private int projectId; // Unique identifier for the BTO project
    private String name;
    private String neighbourhood;
    private List<FlatType> flatType;
    private HashMap<FlatType, Integer> flatCount;
    private Date applicationOpeningDate;
    private Date applicationClosingDate;
    private HDBManager projectManager;
    private List<HDBOfficer> assignedOfficers;
    private int availableOfficerSlots;
    private boolean visibility;

    /**
     * Constructor to initialise the BTOProject object with common attributes.
     * 
     * @param projectId The unique identifier for the BTO project.
     * @param name The name of the BTO project.
     * @param neighbourhood The neighbourhood where the BTO project is located.
     * @param flatType A List containing the type of flats available in the BTO project (e.g., "2 Room Flat", "3 Room Flat").
     * @param flatCount A HashMap containing the count of each flat type available in the project.
     * @param applicationOpeningDate The date when applications for the BTO project open.
     * @param applicationClosingDate The date when applications for the BTO project close.
     * @param projectManager The HDBManager responsible for managing the BTO project.
     * @param assignedOfficers A list of HDBOfficers assigned to assist with the BTO project.
     * @param availableOfficerSlots The number of available slots for HDBOfficers to be assigned to the project.
     * @param visibility Indicates whether the project is visible to users or not.
     */
    public BTOProject(int projectId, String name, String neighbourhood, List<FlatType> flatType, HashMap<FlatType, Integer> flatCount,
                      Date applicationOpeningDate, Date applicationClosingDate, HDBManager projectManager,
                      List<HDBOfficer> assignedOfficers, int availableOfficerSlots, boolean visibility) {
        this.projectId = projectId;
        this.name = name;
        this.neighbourhood = neighbourhood;
        this.flatType = flatType;
        this.flatCount = flatCount;
        this.applicationOpeningDate = applicationOpeningDate;
        this.applicationClosingDate = applicationClosingDate;
        this.projectManager = projectManager;
        this.assignedOfficers = assignedOfficers;
        this.availableOfficerSlots = availableOfficerSlots;
        this.visibility = visibility;
    }
}
