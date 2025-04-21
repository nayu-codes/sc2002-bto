package bto.controller;

import java.util.List;
import java.util.stream.Collectors;

import bto.database.BTOProjectDB;
import bto.model.project.BTOProject;
import bto.model.project.FlatType;
import bto.model.user.MaritalStatus;
import bto.model.user.UserFilter;

public class ProjectFilter {
    private ProjectFilter(){} // Prevents Instantiation
    /**
     * Getting available projects based on the age and marital status filter
     * 
     * @param age The age of the user.
     * @param maritalStatus The marital status of the user (e.g., "Single", "Married").
     * 
     * @return A list of BTOProject objects that are eligible for the user based on the filters.
     */
    public static List<BTOProject> applyUserFilters(int age, MaritalStatus maritalStatus){
        List<BTOProject> eligibleProjects = BTOProjectDB.getVisibleProjects();

        if (maritalStatus == MaritalStatus.SINGLE && age >= 35) {    
            return eligibleProjects.stream()
            .filter(project -> project.getFlatType().contains(FlatType.TWO_ROOM) && project.getFlatCountRemaining(FlatType.TWO_ROOM) != 0)
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
     * 
     * @return The filtered list of projects after applying the additional filters.
     */
    public static List<BTOProject> applyAdditionalFilters(List<BTOProject> filteredProjects) {
        if (UserFilter.getProjectName() != null) {
            filteredProjects = filteredProjects.stream()
                .filter(project -> project.getName().toLowerCase().contains(UserFilter.getProjectName().toLowerCase()))
                .collect(Collectors.toList());
        }
        if (UserFilter.getNeighbourhood() != null) {
            filteredProjects = filteredProjects.stream()
                .filter(project -> project.getNeighbourhood().toLowerCase().contains(UserFilter.getNeighbourhood().toLowerCase()))
                .collect(Collectors.toList());
        }
        if (UserFilter.getFlatType() != null) {
            filteredProjects = filteredProjects.stream()
                .filter(project -> project.getFlatType().contains(UserFilter.getFlatType())) // Check if the project has the specified flat type
                .filter(project -> project.getFlatCountRemaining(UserFilter.getFlatType()) > 0) // Check if the project has remaining flats of the specified type
                .collect(Collectors.toList());
        }
        if (UserFilter.getMinPrice() != null) {
            if (UserFilter.getFlatType() == FlatType.TWO_ROOM) {
                filteredProjects = filteredProjects.stream()
                    .filter(project -> project.getFlatPrices().get(FlatType.TWO_ROOM) >= UserFilter.getMinPrice())
                    .collect(Collectors.toList());
            } else if (UserFilter.getFlatType() == FlatType.THREE_ROOM) {
                filteredProjects = filteredProjects.stream()
                    .filter(project -> project.getFlatPrices().get(FlatType.THREE_ROOM) >= UserFilter.getMinPrice())
                    .collect(Collectors.toList());
            } else {
                List<BTOProject> filteredProjects1 = filteredProjects.stream()
                    .filter(project -> project.getFlatPrices().get(project.getFlatType().get(0)) >= UserFilter.getMinPrice())
                    .collect(Collectors.toList());
                List<BTOProject> filteredProjects2 = filteredProjects.stream()
                        .filter(project -> project.getFlatPrices().get(project.getFlatType().get(1)) >= UserFilter
                                .getMinPrice())
                        .collect(Collectors.toList());
                // Combine both filtered lists and remove duplicates
                filteredProjects1.addAll(filteredProjects2);
                filteredProjects = filteredProjects1.stream()
                        .distinct()
                        .sorted((p1, p2) -> {
                            // Sort by project name
                            return p1.getName().compareToIgnoreCase(p2.getName());
                        })
                        .collect(Collectors.toList());
            }

        }
        if (UserFilter.getMaxPrice() != null) {
            if (UserFilter.getFlatType() == FlatType.TWO_ROOM) {
                filteredProjects = filteredProjects.stream()
                    .filter(project -> project.getFlatPrices().get(FlatType.TWO_ROOM) <= UserFilter.getMaxPrice())
                    .collect(Collectors.toList());
            } else if (UserFilter.getFlatType() == FlatType.THREE_ROOM) {
                filteredProjects = filteredProjects.stream()
                    .filter(project -> project.getFlatPrices().get(FlatType.THREE_ROOM) <= UserFilter.getMaxPrice())
                    .collect(Collectors.toList());
            } else {
                List<BTOProject> filteredProjects1 = filteredProjects.stream()
                        .filter(project -> project.getFlatPrices().get(project.getFlatType().get(0)) <= UserFilter
                                .getMaxPrice())
                        .collect(Collectors.toList());
                List<BTOProject> filteredProjects2 = filteredProjects.stream()
                        .filter(project -> project.getFlatPrices().get(project.getFlatType().get(1)) <= UserFilter
                                .getMaxPrice())
                        .collect(Collectors.toList());
                // Combine both filtered lists and remove duplicates
                filteredProjects1.addAll(filteredProjects2);
                filteredProjects = filteredProjects1.stream()
                        .distinct()
                        .sorted((p1, p2) -> {
                            // Sort by project name
                            return p1.getName().compareToIgnoreCase(p2.getName());
                        })
                        .collect(Collectors.toList());
                System.out.println("Filtered Projects: " + filteredProjects.size());
            }
        }

        return filteredProjects;
    }
}