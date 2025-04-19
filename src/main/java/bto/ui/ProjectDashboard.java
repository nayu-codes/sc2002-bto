package bto.ui;

import java.util.List;
import java.text.SimpleDateFormat;

import bto.model.project.BTOProject;
import bto.model.user.User;

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
        if (filteredProjects.isEmpty()) {
            System.out.println(" " + "-".repeat(65));
            System.out.println("  Available Projects for: " + user.getName());
            System.out.printf(" %5s | %15s | %15s | %20s\n", "Index", "Project Name", "Neighbourhood",
                    "Application Period");
            System.out.println(" " + "-".repeat(65));
            System.out.println("  No available projects for you at the moment.");
            return; // Return to previous screen if no projects are available
        }

        // Print the header for the project dashboard
        int choice = -1; // Initialize choice to an invalid value
        do {
            System.out.println(" " + "-".repeat(65));
            System.out.println("  Available Projects for: " + user.getName());
            System.out.printf(" %5s | %15s | %15s | %20s\n", "Index", "Project Name", "Neighbourhood", "Application Period");
            System.out.println(" " + "-".repeat(65));

            // Display the filtered projects to the user
            if (filteredProjects.isEmpty()) {
                System.out.println("  No available projects for you at the moment.");
                return;
            }

            // Count for list indexing
            int i = 1;

            // Print each project in the filtered list
            for (BTOProject project : filteredProjects) {
                // Print in table format, with consistent spacing
                // i, Project Name (Neighbourhood: Location) | Application Opening - Closing Date
                System.out.printf("  %3d. | %15s | %15s | %10s - %10s\n", 
                    i, project.getName(), project.getNeighbourhood(),
                    new SimpleDateFormat("dd/MM/yyyy").format(project.getApplicationOpeningDate()),
                    new SimpleDateFormat("dd/MM/yyyy").format(project.getApplicationClosingDate())
                );
                i++;
            };
            System.out.println(" " + "-".repeat(65));
            System.out.println("  Please enter the index of the project you want to view more information about, or '0' to go back to the main menu.");
            System.out.print("  Enter your choice: ");
            choice = -1; // Reset choice to an invalid value
            try {
                choice = Integer.parseInt(System.console().readLine()); // Read the user's choice
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Please enter a number.");
                continue; // Skip to the next iteration of the loop
            }
            if (choice < 0 || choice > filteredProjects.size()) {
                System.out.println("  Invalid choice. Please try again.");
            } else if (choice == 0) {
                System.out.println("  Returning to the main menu...");
                return; // Exit the loop and return to the main menu
            } else {
                // Get project details for the selected project
                BTOProject selectedProject = filteredProjects.get(choice - 1);

                // temp to debug
                System.out.println(" User selected project: " + selectedProject.getName());

                // Pass the selected project to the ProjectDetailsScreen
                // ProjectDetailsScreen.showProjectDetails(selectedProject);
            }
        } while (choice != 0); // Continue until the user chooses to go back to the main menu
    };
}