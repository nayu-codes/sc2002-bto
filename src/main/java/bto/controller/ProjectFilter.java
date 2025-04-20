package bto.controller;

import java.util.List;
import java.util.stream.Collectors;

import bto.database.BTOProjectDB;
import bto.model.project.BTOProject;
import bto.model.project.FlatType;
import bto.model.user.MaritalStatus;
import bto.ui.project.UserFilter;

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

    /**
     * Apply additional filters to the list of projects based on user input.
     * 
     * @param filteredProjects The list of filtered projects to apply additional filters to.
     * @param userFilter The user filter object containing the selected filters.
     * 
     * @return The filtered list of projects after applying the additional filters.
     */
    public static List<BTOProject> applyAdditionalFilters(List<BTOProject> filteredProjects, UserFilter userFilter) {
        if (userFilter.getProjectName() != null) {
            filteredProjects = filteredProjects.stream()
                .filter(project -> project.getName().toLowerCase().contains(userFilter.getProjectName().toLowerCase()))
                .collect(Collectors.toList());
        }
        if (userFilter.getNeighbourhood() != null) {
            filteredProjects = filteredProjects.stream()
                .filter(project -> project.getNeighbourhood().equalsIgnoreCase(userFilter.getNeighbourhood()))
                .collect(Collectors.toList());
        }
        if (userFilter.getFlatType() != null) {
            filteredProjects = filteredProjects.stream()
                .filter(project -> project.getFlatType().contains(userFilter.getFlatType()))
                .collect(Collectors.toList());
        }
        if (userFilter.getMinPrice() != null) {
            List<BTOProject> filteredProjects1 = filteredProjects.stream()
                .filter(project -> project.getFlatPrices().get(project.getFlatType().get(0)) >= userFilter.getMinPrice())
                .collect(Collectors.toList());
            List<BTOProject> filteredProjects2 = filteredProjects.stream()
                    .filter(project -> project.getFlatPrices().get(project.getFlatType().get(1)) >= userFilter
                            .getMinPrice())
                    .collect(Collectors.toList());
            // Union of both filtered lists
            filteredProjects = filteredProjects1.stream()
                    .filter(project -> !filteredProjects2.contains(project))
                    .collect(Collectors.toList());

        }
        if (userFilter.getMaxPrice() != null) {
            List<BTOProject> filteredProjects1 = filteredProjects.stream()
                    .filter(project -> project.getFlatPrices().get(project.getFlatType().get(0)) <= userFilter
                            .getMaxPrice())
                    .collect(Collectors.toList());
            List<BTOProject> filteredProjects2 = filteredProjects.stream()
                    .filter(project -> project.getFlatPrices().get(project.getFlatType().get(1)) <= userFilter
                            .getMaxPrice())
                    .collect(Collectors.toList());
            // Union of both filtered lists
            filteredProjects = filteredProjects1.stream()
                    .filter(project -> !filteredProjects2.contains(project))
                    .collect(Collectors.toList());
        }

        return filteredProjects;
    }
}