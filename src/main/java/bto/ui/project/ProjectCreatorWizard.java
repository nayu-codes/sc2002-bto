package bto.ui.project;

import bto.database.BTOProjectDB;
import bto.controller.ProjectController;
import bto.model.project.BTOProject;
import bto.model.project.FlatType;
import bto.model.user.HDBManager;
import bto.model.user.User;
import bto.ui.TerminalUtils;

import java.util.Scanner;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

public class ProjectCreatorWizard {
    private ProjectCreatorWizard(){} // Prevents instantiation

    public static void start(User user) {
        int option = -1;
        int numberInput = -1;
        String visibilityInput = "r"; // Variable to store visibility input
        // Variables for project creation
        String projectName = null;
        String neighbourhood = null;
        List<FlatType> flatTypes = new ArrayList<>();
        HashMap<FlatType, Integer> flatCount = new HashMap<>();
        HashMap<FlatType, Integer> flatPrice = new HashMap<>();
        Date applicationOpeningDate = null;
        Date applicationClosingDate = null;
        HDBManager manager = (HDBManager) user;
        int availableOfficerSlots = 0;
        boolean visibility = true; // Default visibility to true

        // Clear the screen and display the welcome message
        TerminalUtils.clearScreen();
        System.out.println("\n+--------------------------------+\n" +
                            "|   BTO Project Creation Wizard   |\n" +
                            "+---------------------------------+");

        Scanner scanner = new Scanner(System.in);

        // Step 1: Get project name
        do {
            System.out.print("Enter the project name: ");
            projectName = scanner.nextLine().trim();
            if (projectName.isEmpty()) {
                System.out.println("Project name cannot be empty. Please try again.");
                projectName = null; // Reset project name to null for re-entry
            } else if (BTOProjectDB.getBTOProjectByName(projectName) != null) {
                System.out.println("Project name already exists. Please choose a different name.");
                projectName = null; // Reset project name to null for re-entry
            }
        } while (projectName == null);

        // Print current creation status
        TerminalUtils.clearScreen();
        System.out.println("\n+--------------------------------+\n" +
                            "|   BTO Project Creation Wizard   |\n" +
                            "+---------------------------------+");
        System.out.println("Current Project Details:");
        System.out.println("  Project Name: " + projectName);
        System.out.println("+--------------------------------+\n");

        // Step 2: Get neighbourhood
        do {
            System.out.print("Enter the neighbourhood: ");
            neighbourhood = scanner.nextLine().trim();
            if (neighbourhood.isEmpty()) {
                System.out.println("Neighbourhood cannot be empty. Please try again.");
                neighbourhood = null; // Reset neighbourhood to null for re-entry
            }
        } while (neighbourhood == null);

        // Print current creation status
        TerminalUtils.clearScreen();
        System.out.println("\n+--------------------------------+\n" +
                            "|   BTO Project Creation Wizard   |\n" +
                            "+---------------------------------+");
        System.out.println("Current Project Details:");
        System.out.println("  Project Name: " + projectName);
        System.out.println("  Neighbourhood: " + neighbourhood);
        System.out.println("+--------------------------------+\n");

        // Step 3: Get flat types, prices and counts
        do {
            if (flatTypes.size() >= 2) {
                break; // Exit the loop if 2 flat types are already added
            }
            System.out.print("Select flat type to add to project ('2' for 2-room, '3' for 3-room, '0' when done): ");
            option = scanner.nextInt(); // Read the user's choice
            scanner.nextLine(); // Consume the newline character
            switch(option) {
                case 2:
                    flatTypes.add(FlatType.TWO_ROOM);
                    // Get price and count for 2-room flat
                    do {
                        System.out.print("Enter the price for 2-room flat: ");
                        try {
                            numberInput = scanner.nextInt(); // Read the user's choice
                            scanner.nextLine(); // Consume the newline character
                        } catch (Exception e) {
                            System.out.println("Invalid input. Please enter a number.");
                            scanner.nextLine(); // Clear the invalid input
                            numberInput = -1; // Reset numberInput to -1 for re-entry
                            continue;
                        }
                        if (numberInput <= 0) {
                            System.out.println("Price must be a positive number. Please try again.");
                        }
                    } while (numberInput <= 0);
                    flatPrice.put(FlatType.TWO_ROOM, numberInput); // Store the price for 2-room flat

                    do {
                        System.out.print("Enter the number of 2-room flats: ");
                        try {
                            numberInput = scanner.nextInt(); // Read the user's choice
                            scanner.nextLine(); // Consume the newline character
                        } catch (Exception e) {
                            System.out.println("Invalid input. Please enter a number.");
                            scanner.nextLine(); // Clear the invalid input
                            numberInput = -1; // Reset numberInput to -1 for re-entry
                            continue;
                        }
                        if (numberInput <= 0) {
                            System.out.println("Count must be a positive number. Please try again.");
                        }
                    } while (numberInput <= 0);
                    flatCount.put(FlatType.TWO_ROOM, numberInput); // Store the count for 2-room flat
                    break;
                case 3:
                    flatTypes.add(FlatType.THREE_ROOM);
                    // Get price and count for 3-room flat
                    do {
                        System.out.print("Enter the price for 3-room flat: ");
                        numberInput = scanner.nextInt(); // Read the user's choice
                        scanner.nextLine(); // Consume the newline character
                        if (numberInput <= 0) {
                            System.out.println("Price must be a positive number. Please try again.");
                        }
                    } while (numberInput <= 0);
                    flatPrice.put(FlatType.THREE_ROOM, numberInput); // Store the price for 3-room flat

                    do {
                        System.out.print("Enter the number of 3-room flats: ");
                        numberInput = scanner.nextInt(); // Read the user's choice
                        scanner.nextLine(); // Consume the newline character
                        if (numberInput <= 0) {
                            System.out.println("Count must be a positive number. Please try again.");
                        }
                    } while (numberInput <= 0);
                    flatCount.put(FlatType.THREE_ROOM, numberInput); // Store the count for 3-room flat
                    break;
                case 0:
                    break; // Exit the loop
                default:
                    System.out.println("Invalid input. Please enter '2', '3', or '0'.");
            }
        } while (option != 0); // Continue until user enters '0' or 2 flat types are added

        // Print current creation status
        TerminalUtils.clearScreen();
        System.out.println("\n+--------------------------------+\n" +
                            "|   BTO Project Creation Wizard   |\n" +
                            "+---------------------------------+");
        System.out.println("Current Project Details:");
        System.out.println("  Project Name: " + projectName);
        System.out.println("  Neighbourhood: " + neighbourhood);
        System.out.println("  Flat Types: ");
        for (FlatType flatType : flatTypes) {
            System.out.println("    - " + flatType.getDisplayName() + ": $" + flatPrice.get(flatType) + " (" + flatCount.get(flatType) + " units)");
        }
        System.out.println("+--------------------------------+\n");

        // Step 4: Get application opening and closing dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        do {
            System.out.print("Enter the application opening date (dd/MM/yyyy): ");
            String openingDateInput = scanner.nextLine().trim();
            try {
                applicationOpeningDate = dateFormat.parse(openingDateInput);
                // Check that applicationOpeningDate is after applicationClosingDate of any existing project the manager is managing
                for (BTOProject existingProject : BTOProjectDB.getBTOProjectsByManager(manager)) {
                    if (existingProject.getApplicationClosingDate() != null &&
                            (applicationOpeningDate.before(existingProject.getApplicationClosingDate()) || 
                            applicationOpeningDate.equals(existingProject.getApplicationClosingDate()))
                        ) {
                        System.out.println("Application opening date cannot be before the closing date of an existing project you are managing ("+ existingProject.getName() + ").");
                        applicationOpeningDate = null; // Reset opening date to null for re-entry
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Please enter the date in dd/MM/yyyy format.");
                applicationOpeningDate = null; // Reset opening date to null for re-entry
            }
        } while (applicationOpeningDate == null);

        do {
            System.out.print("Enter the application closing date (dd/MM/yyyy): ");
            String closingDateInput = scanner.nextLine().trim();
            try {
                applicationClosingDate = dateFormat.parse(closingDateInput);
                if (applicationClosingDate.before(applicationOpeningDate)) {
                    System.out.println("Application closing date cannot be before the opening date. Please try again.");
                    applicationClosingDate = null; // Reset closing date to null for re-entry
                }
                // Check that applicationClosingDate is before applicationOpeningDate of any existing project the manager is managing
                for (BTOProject existingProject : BTOProjectDB.getBTOProjectsByManager(manager)) {
                    if (existingProject.getApplicationOpeningDate() != null &&
                            (applicationClosingDate.after(existingProject.getApplicationOpeningDate()) || 
                            applicationClosingDate.equals(existingProject.getApplicationOpeningDate()))
                        ) {
                        System.out.println("Application closing date cannot be before the opening date of an existing project you are managing ("+ existingProject.getName() + ").");
                        applicationOpeningDate = null; // Reset opening date to null for re-entry
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Please enter the date in dd/MM/yyyy format.");
                applicationClosingDate = null; // Reset closing date to null for re-entry
            }
        } while (applicationClosingDate == null);

        // Print current creation status
        TerminalUtils.clearScreen();
        System.out.println("\n+--------------------------------+\n" +
                            "|   BTO Project Creation Wizard   |\n" +
                            "+---------------------------------+");
        System.out.println("Current Project Details:");
        System.out.println("  Project Name: " + projectName);
        System.out.println("  Neighbourhood: " + neighbourhood);
        System.out.println("  Flat Types: ");
        for (FlatType flatType : flatTypes) {
            System.out.println("    - " + flatType.getDisplayName() + ": $" + flatPrice.get(flatType) + " (" + flatCount.get(flatType) + " units)");
        }
        System.out.println("  Application Opening Date: " + dateFormat.format(applicationOpeningDate));
        System.out.println("  Application Closing Date: " + dateFormat.format(applicationClosingDate));
        System.out.println("+--------------------------------+\n");

        // Step 5: Get officer slots
        do {
            System.out.print("Enter the number of officer slots available for this project: ");
            numberInput = scanner.nextInt(); // Read the user's choice
            scanner.nextLine(); // Consume the newline character
            if (numberInput <= 0) {
                System.out.println("Number of officer slots must be a positive number. Please try again.");
            }
        } while (numberInput <= 0);
        availableOfficerSlots = numberInput;

        // Print current creation status
        TerminalUtils.clearScreen();
        System.out.println("\n+--------------------------------+\n" +
                            "|   BTO Project Creation Wizard   |\n" +
                            "+---------------------------------+");
        System.out.println("Current Project Details:");
        System.out.println("  Project Name: " + projectName);
        System.out.println("  Neighbourhood: " + neighbourhood);
        System.out.println("  Flat Types: ");
        for (FlatType flatType : flatTypes) {
            System.out.println("    - " + flatType.getDisplayName() + ": $" + flatPrice.get(flatType) + " (" + flatCount.get(flatType) + " units)");
        }
        System.out.println("  Application Opening Date: " + dateFormat.format(applicationOpeningDate));
        System.out.println("  Application Closing Date: " + dateFormat.format(applicationClosingDate));
        System.out.println("  Officer Slots: " + availableOfficerSlots);
        System.out.println("+--------------------------------+\n");

        // Last Step: Get project visibility
        do {
            System.out.print("Is the project visible to the public? (Y for Yes, N for No): ");
            visibilityInput = scanner.nextLine().trim();
            if (visibilityInput.equalsIgnoreCase("Y")) {
                visibility = true;
            } else if (visibilityInput.equalsIgnoreCase("N")) {
                visibility = false;
            } else {
                System.out.println("Invalid input. Please enter either Y or N.");
            }
        } while (!visibilityInput.equalsIgnoreCase("Y") && !visibilityInput.equalsIgnoreCase("N"));

        // Print current creation status
        TerminalUtils.clearScreen();
        System.out.println("\n+--------------------------------+\n" +
                            "|   BTO Project Creation Wizard   |\n" +
                            "+---------------------------------+");
        System.out.println("Current Project Details:");
        System.out.println("  Project Name: " + projectName);
        System.out.println("  Neighbourhood: " + neighbourhood);
        System.out.println("  Flat Types: ");
        for (FlatType flatType : flatTypes) {
            System.out.println("    - " + flatType.getDisplayName() + ": $" + flatPrice.get(flatType) + " (" + flatCount.get(flatType) + " units)");
        }
        System.out.println("  Application Opening Date: " + dateFormat.format(applicationOpeningDate));
        System.out.println("  Application Closing Date: " + dateFormat.format(applicationClosingDate));
        System.out.println("  Officer Slots: " + availableOfficerSlots);
        System.out.println("  Project Visibility: " + (visibility ? "Visible" : "Not Visible"));
        System.out.println("+--------------------------------+\n");

        // Step 6: Confirm project creation
        System.out.print("Are you sure you want to create this project? (Y for Yes, N for No): ");
        String confirmInput = scanner.nextLine().trim();
        if (confirmInput.equalsIgnoreCase("Y")) {
            // Create the project
            BTOProject newProject = new BTOProject(123, projectName, neighbourhood, flatTypes, flatCount, flatPrice, applicationOpeningDate, applicationClosingDate, manager, new ArrayList<>(), availableOfficerSlots, visibility);
            int newProjectId = BTOProjectDB.addBTOProject(newProject); // Add the project to the database
            System.out.println("Project created successfully! Project ID: " + newProjectId);

            // Await keypress to continue
            System.out.println("Press any key to continue...");
            scanner.nextLine(); // Wait for user input
        } else {
            System.out.println("Project creation cancelled.");
        }
    }
}
