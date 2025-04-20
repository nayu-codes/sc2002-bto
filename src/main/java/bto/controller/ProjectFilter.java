package bto.controller;

import java.util.List;
import java.util.stream.Collectors;

import bto.database.BTOProjectDB;
import bto.model.project.BTOProject;
import bto.model.project.FlatType;
import bto.model.user.MaritalStatus;

public class ProjectFilter {
    private ProjectFilter(){} // Prevents Instantiation
    /**
     * Getting available projects based on the age and marital status filter
     * 
     * @param age The age of the user.
     * @param maritalStatus The marital status of the user (e.g., "Single", "Married").
     */
    public static List<BTOProject> applyUserFilters(int age, MaritalStatus maritalStatus){
        List<BTOProject> eligibleProjects = BTOProjectDB.getVisibleProjects();

        if (maritalStatus == MaritalStatus.SINGLE && age >= 35) {    
            return eligibleProjects.stream()
            .filter(project -> project.getFlatType().contains(FlatType.TWO_ROOM))
            .collect(Collectors.toList());
        }

        if (maritalStatus == MaritalStatus.MARRIED && age >= 21) {
            return eligibleProjects;
        }

        return List.of();
    }
}