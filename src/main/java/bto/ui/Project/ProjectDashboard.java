package bto.ui.project;

import java.util.List;
import java.util.Scanner;
import java.text.SimpleDateFormat;

import bto.controller.ProjectFilter;
import bto.model.project.BTOProject;
import bto.model.user.User;
import bto.model.user.UserFilter;
import bto.ui.TerminalUtils;

public class ProjectDashboard{
    private ProjectDashboard(){} // Prevents instantiation

    /**
     * Starts the project dashboard for the user to see available projects.
     * For applicants, the method only shows the projects available for a user depending on their age and maritalStatus
     * 
     * @param user The user object representing the logged-in user.
     */
    public static void start(User user) {
        String choice = "-1"; // Initialize choice to an invalid value
        int choiceIndex = -1; // Initialize choiceIndex to an invalid value
        Scanner scanner = new Scanner(System.in);

        TerminalUtils.clearScreen();
        do {
            List<BTOProject> filteredProjects = ProjectFilter.applyUserFilters(user.getAge(), user.getMaritalStatus());
            // Apply additional filters based on UserFilter object
            filteredProjects = ProjectFilter.applyAdditionalFilters(filteredProjects);

            System.out.println(" \nAvailable Projects for: " + user.getName());
            
            // Print user filters applied, if any
            UserFilter.printFiltersApplied();

            System.out.println("-".repeat(68));
            System.out.printf(" %5s | %15s | %15s | %20s\n", "Index", "Project Name", "Neighbourhood", "Application Period");
            System.out.println("-".repeat(68));
            
            // Check if there are available projects
            if (filteredProjects.isEmpty()) {
                System.out.println("No available projects for you at the moment.");
                System.out.println("Enter any key to go back to the main menu.");
                scanner.nextLine();
                TerminalUtils.clearScreen();
                return;
            }

            else{
                // Count for list indexing
                int i = 1;

                // Print each project in the filtered list
                for (BTOProject project : filteredProjects) {
                    // Print in table format, with consistent spacing
                    // i | Project Name | Neighbourhood: Location | Application Opening - Closing Date
                    System.out.printf("  %3d. | %15s | %15s | %10s - %10s\n", 
                        i, project.getName(), project.getNeighbourhood(),
                        new SimpleDateFormat("dd/MM/yyyy").format(project.getApplicationOpeningDate()),
                        new SimpleDateFormat("dd/MM/yyyy").format(project.getApplicationClosingDate())
                    );
                    i++;
                };
                System.out.println("-".repeat(68));
                System.out.println("  Please enter the index of the project you want to view more information about, 'filter' to select filters, or '0' to go back to the main menu.");
                System.out.print("  Enter your choice: ");
                choice = scanner.nextLine().trim(); // Read the user's choice and remove leading/trailing whitespace
                try {
                    // Parse the choice as an integer
                    choiceIndex = Integer.parseInt(choice);
                } catch (NumberFormatException e) {
                    // if input is 'filter', go to filter menu
                    if (choice.equalsIgnoreCase("filter")) {
                        ProjectFilterMenu.updateFilter();
                    }
                    else{
                    System.out.println("  Invalid input. Please enter a number.");
                    }
                    continue; // Skip to the next iteration of the loop
                }

                if (choiceIndex < 0 || choiceIndex > filteredProjects.size()) {
                    System.out.println("  Invalid choice. Please try again.");
                } else if (choiceIndex == 0) {
                    TerminalUtils.clearScreen();
                    return; // Exit the loop and return to the main menu
                } else {
                    // Get project details for the selected project
                    BTOProject selectedProject = filteredProjects.get(choiceIndex - 1);

                    // Pass the selected project to ProjectDetails
                    ProjectDetails.start(user, selectedProject);
                }
            }
        } while (choice != "0"); // Continue until the user chooses to go back to the main menu
    }
}