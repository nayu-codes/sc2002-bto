package bto.ui.project;

import bto.model.project.FlatType;
import bto.model.user.UserFilter;

import java.util.Scanner;

public class ProjectFilterMenu {
    private ProjectFilterMenu(){} // Prevents Instantiation

    /**
     * Starts the project filter menu for the user to select filters.
     * The method only shows the projects available for a user depending on their age and maritalStatus
     * 
     * @param age The age of the user.
     * @param maritalStatus The marital status of the user.
     */
    public static void updateFilter() {
        int option = -1;
        int minPrice = -1;
        int maxPrice = -1;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Current View Filters:");
            System.out.println("  Project Name: " + (UserFilter.getProjectName() != null ? UserFilter.getProjectName() : "None"));
            System.out.println("  Neighbourhood: " + (UserFilter.getNeighbourhood() != null ? UserFilter.getNeighbourhood() : "None"));
            System.out.println("  Flat Type: " + (UserFilter.getFlatType() != null ? UserFilter.getFlatType().getDisplayName() : "None"));
            System.out.println("  Min Price: " + (UserFilter.getMinPrice() != null ? UserFilter.getMinPrice() : "None"));
            System.out.println("  Max Price: " + (UserFilter.getMaxPrice() != null ? UserFilter.getMaxPrice() : "None"));

            // Display the filter options to the user
            System.out.println("\nSelect a filter to apply:");
            System.out.println(" 1. Project Name");
            System.out.println(" 2. Neighbourhood");
            System.out.println(" 3. Flat Type");
            System.out.println(" 4. Min Price");
            System.out.println(" 5. Max Price");
            System.out.println(" 0. Back to Main Menu");
            System.out.print("Please enter your choice: ");

            try {
                option = scanner.nextInt(); // Read the user's choice
                scanner.nextLine(); // Consume the newline character
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
                continue; // Skip to the next iteration of the loop
            }

            switch(option) {
                case 1:
                    System.out.print("Enter Project Name (Leave blank to reset filter): ");
                    String projectName = scanner.nextLine();
                    if (projectName.isEmpty()) {
                        UserFilter.setProjectName(null); // Reset the filter if input is empty
                    } else {
                        UserFilter.setProjectName(projectName);
                    }
                    break;
                case 2:
                    System.out.print("Enter Neighbourhood (Leave blank to reset filter): ");
                    String neighbourhood = scanner.nextLine();
                    if (neighbourhood.isEmpty()) {
                        UserFilter.setNeighbourhood(null); // Reset the filter if input is empty
                    } else {
                        UserFilter.setNeighbourhood(neighbourhood);
                    }
                    break;
                case 3:
                    UserFilter.setFlatType(selectFlatType(scanner));
                    break;
                case 4:
                    do {
                        System.out.print("Enter Min Price (-1 to reset to no filter): ");
                        try {
                            minPrice = scanner.nextInt();
                            if (minPrice == -1) {
                                UserFilter.setMinPrice(null);
                                break; // Exit back to the filter menu
                            } else if (minPrice < 0) {
                                System.out.println("Min Price cannot be negative. Please try again.");
                                continue; // Skip to the next iteration of the loop
                            }
                            UserFilter.setMinPrice(minPrice);
                        } catch (Exception e) {
                            System.out.println("Invalid input. Please enter a number.");
                            scanner.nextLine(); // Clear the invalid input
                            continue; // Skip to the next iteration of the loop
                        }
                    } while (minPrice < 0); // Ensure minPrice is non-negative
                    break;
                case 5:
                    do {
                        System.out.print("Enter Max Price (-1 to reset to no filter): ");
                        try {
                            maxPrice = scanner.nextInt();
                            if (maxPrice == -1) {
                                UserFilter.setMinPrice(null);
                                break; // Exit back to the filter menu
                            } else if (maxPrice < 0) {
                                System.out.println("Max Price cannot be negative. Please try again.");
                                continue; // Skip to the next iteration of the loop
                            }
                            UserFilter.setMaxPrice(maxPrice);
                        } catch (Exception e) {
                            System.out.println("Invalid input. Please enter a number.");
                            scanner.nextLine(); // Clear the invalid input
                            continue; // Skip to the next iteration of the loop
                        }
                    } while (maxPrice < 0); // Ensure minPrice is non-negative
                    break;
                case 0:
                    System.out.println("Exiting filter menu.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 0);
        return; // Return the updated user filter
    }

    /**
     * Selects the flat type for the user filter.
     * 
     * @param scanner The scanner to read user input.
     * 
     * @return The updated user filter with the selected flat type.
     */
    private static FlatType selectFlatType(Scanner scanner) {
        System.out.println("Select Flat Type:");
        System.out.println(" 1. 2 Room");
        System.out.println(" 2. 3 Room");
        System.out.println(" -1. Reset to no filter");
        System.out.println(" 0. Back to Filter Menu");

        int flatTypeOption = -2;
        do {
            System.out.print("Please enter your choice: ");
            try {
                flatTypeOption = scanner.nextInt(); // Read the user's choice
                scanner.nextLine(); // Consume the newline character
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
                continue; // Skip to the next iteration of the loop
            }

            switch(flatTypeOption) {
                case -1:
                    return null;
                case 1:
                    return FlatType.TWO_ROOM;
                case 2:
                    return FlatType.THREE_ROOM;
                case 0:
                    System.out.println("Exiting flat type selection.");
                    return null; // Exit back to the filter menu
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (flatTypeOption != 0); // Exit back to the filter menu

        return null;
    }
}
