package bto.ui;

import java.util.List;

import bto.model.project.BTOProject;
import bto.model.user.*;

public class ProjectDashboard{
    public ProjectDashboard() {
        // Constructor for the ProjectDashboard class
    }

    /**
     * Starts the project dashboard for the user to see available projects.
     * The method only shows the projects available for a user depending on their age and maritalStatus
     * 
     * @param age The age of the user.
     * @param maritalStatus The marital status of the user.
     */
    public static void start(User user) {
        List<BTOProject> filteredProjects = ProjectFilter.applyUserFilters(user.getAge(), user.getMaritalStatus());
        System.out.println("Here are the available projects: ");
        filteredProjects.stream()
        .forEach(System.out::println);
    };
}