package bto.ui;

import java.util.ArrayList;

import bto.model.user.MaritalStatus;
import bto.model.project.BTOProject;

import bto.database.BTOProjectDB;

public class ProjectDashboard {
    public ProjectDashboard() {
        // Constructor for the ProjectDashboard class
    }

    /**
     * Starts the project dashboard for the user to see available projects.
     * The method only shows the projects available for a user depending on their age and maritalStatus
     */
    public static void start(int age, MaritalStatus maritalStatus) {
        ProjectFilter filter = (age, maritalStatus) -> {
            ArrayList<BTOProject> visibleProjects = new ArrayList<>();
            visibleProjects = BTOProjectDB.getVisibleProjects();
            for(BTOProject project:visibleProjects){
                //TODO: implement the filter of age and marital status
            }
        };
    }
}
