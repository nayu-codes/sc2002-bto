package bto.ui.project;

import bto.database.ApplicationDB;
import bto.database.BTOProjectDB;
import bto.database.EnquiryDB;
import bto.database.RegistrationDB;
import bto.model.application.BTOApplication;
import bto.model.enquiry.Enquiry;
import bto.model.project.BTOProject;
import bto.model.project.FlatType;
import bto.model.registration.OfficerRegistration;
import bto.model.user.HDBManager;
import bto.model.user.User;
import bto.ui.TerminalUtils;

import java.util.Scanner;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.InputMismatchException;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProjectCreatorWizard {
    private ProjectCreatorWizard(){} // Prevents instantiation

    /**
     * Starts the project creation wizard for the given user.
     * 
     * @param user The HDB Manager who is creating the project.
     */
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
        int availableOfficerSlots = 0;
        boolean visibility = true; // Default visibility to true

        HDBManager manager = (HDBManager) user;
        // Clear the screen and display the welcome message
        TerminalUtils.clearScreen();
        System.out.println("\n+---------------------------------+\n" +
                            "|   BTO Project Creation Wizard   |\n" +
                            "+---------------------------------+");

        Scanner scanner = new Scanner(System.in);

        // Step 1: Get project name
        projectName = handleProjectName(scanner, projectName);
        if (projectName == null) {
            System.out.println("Project creation cancelled.");
            return; // Exit the wizard if project name is not created
        }

        // Print current creation status
        TerminalUtils.clearScreen();
        System.out.println("\n+---------------------------------+\n" +
                            "|   BTO Project Creation Wizard   |\n" +
                            "+---------------------------------+");
        System.out.println("Current Project Details:");
        System.out.println("  Project Name: " + projectName);
        System.out.println("+---------------------------------+\n");

        // Step 2: Get neighbourhood
        neighbourhood = handleNeighbourhood(scanner, neighbourhood);
        if (neighbourhood == null) {
            System.out.println("Project creation cancelled.");
            return; // Exit the wizard if neighbourhood is not created
        }

        // Print current creation status
        TerminalUtils.clearScreen();
        System.out.println("\n+---------------------------------+\n" +
                            "|   BTO Project Creation Wizard   |\n" +
                            "+---------------------------------+");
        System.out.println("Current Project Details:");
        System.out.println("  Project Name: " + projectName);
        System.out.println("  Neighbourhood: " + neighbourhood);
        System.out.println("+---------------------------------+\n");

        // Step 3: Get flat types, prices and counts
        do {
            if (flatTypes.size() >= 2) {
                break; // Exit the loop if 2 flat types are already added
            }
            System.out.println("Select flat type to add to project ('2' for 2-room, '3' for 3-room, '0' when done): ");
            try {
                option = scanner.nextInt(); // Read the user's choice
                scanner.nextLine(); // Consume the newline character
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
                option = -1; // Reset option to -1 for re-entry
                continue;
            }

            // If no flat types are added, prompt the user to add a flat type
            if (option == 0 && flatTypes.size() == 0) {
                System.out.println("Please add at least one flat type before proceeding.");
                option = -1; // Reset option to -1 for re-entry
                continue; // Skip to the next iteration of the loop
            }

            switch(option) {
                case 2:
                    if (flatTypes.contains(FlatType.TWO_ROOM)) {
                        System.out.println("2-room flat type already added. Please choose another flat type.");
                        break; // Skip to the next iteration of the loop
                    }
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
                    if (flatTypes.contains(FlatType.THREE_ROOM)) {
                        System.out.println("3-room flat type already added. Please choose another flat type.");
                        break; // Skip to the next iteration of the loop
                    }
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
        System.out.println("\n+---------------------------------+\n" +
                            "|   BTO Project Creation Wizard   |\n" +
                            "+---------------------------------+");
        System.out.println("Current Project Details:");
        System.out.println("  Project Name: " + projectName);
        System.out.println("  Neighbourhood: " + neighbourhood);
        System.out.println("  Flat Types: ");
        for (FlatType flatType : flatTypes) {
            System.out.println("    - " + flatType.getDisplayName() + ": $" + flatPrice.get(flatType) + " (" + flatCount.get(flatType) + " units)");
        }
        System.out.println("+---------------------------------+\n");

        // Step 4: Get application opening and closing dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        List<Date> applicationDates = handleApplicationDates(scanner, manager, applicationOpeningDate, applicationClosingDate);
        applicationOpeningDate = applicationDates.get(0); // Get the opening date from the list
        applicationClosingDate = applicationDates.get(1); // Get the closing date from the list

        // Print current creation status
        TerminalUtils.clearScreen();
        System.out.println("\n+---------------------------------+\n" +
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
        System.out.println("+---------------------------------+\n");

        // Step 5: Get officer slots
        availableOfficerSlots = handleOfficerSlots(scanner, availableOfficerSlots);

        // Print current creation status
        TerminalUtils.clearScreen();
        System.out.println("\n+---------------------------------+\n" +
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
        System.out.println("+---------------------------------+\n");

        // Last Step: Get project visibility
        visibility = handleProjectVisibility(scanner, visibility);

        // Print current creation status
        TerminalUtils.clearScreen();
        System.out.println("\n+---------------------------------+\n" +
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
        System.out.println("+---------------------------------+\n");

        // Step 6: Confirm project creation
        System.out.println("Are you sure you want to create this project? (Y for Yes, N for No): ");
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

    /**
     * Starts the project editing wizard for the given project.
     * 
     * @param manager The HDB Manager who is editing the project.
     * @param project The BTOProject to be edited.
     */
    public static void editProject(HDBManager manager, BTOProject project) {
        Scanner scanner = new Scanner(System.in);
        int option = -2;

        // Variables for project editing
        int projectId = project.getProjectId(); // Get the project ID
        String originalProjectName = project.getName(); // Store the original project name
        String projectName = project.getName();
        String neighbourhood = project.getNeighbourhood();
        List<FlatType> flatTypes = project.getFlatType();
        HashMap<FlatType, Integer> flatCount = project.getFlatCounts();
        HashMap<FlatType, Integer> flatPrice = project.getFlatPrices();
        Date applicationOpeningDate = project.getApplicationOpeningDate();
        Date applicationClosingDate = project.getApplicationClosingDate();
        int availableOfficerSlots = project.getAvailableOfficerSlots();
        boolean visibility = project.getVisibility();

        do {
            // Clear the screen and display the welcome message
            TerminalUtils.clearScreen();
            System.out.println("\n+---------------------------------+\n" +
                                "|   BTO Project Edit Wizard       |\n" +
                                "+---------------------------------+");
            System.out.println("Current Project Details:");
            System.out.println("  Project Name: " + projectName);
            System.out.println("  Neighbourhood: " + neighbourhood);
            System.out.println("  Flat Types: ");
            for (FlatType flatType : flatTypes) {
                System.out.println("    - " + flatType.getDisplayName() + ": $" + flatPrice.get(flatType) + " (" + flatCount.get(flatType) + " units)");
            }
            System.out.println("  Application Opening Date: " + applicationOpeningDate);
            System.out.println("  Application Closing Date: " + applicationClosingDate);
            System.out.println("  Officer Slots: " + availableOfficerSlots);
            System.out.println("  Project Visibility: " + (visibility ? "Visible" : "Not Visible"));
            System.out.println("+---------------------------------+\n");

            System.out.println("Select an option to edit:");
            System.out.println(" 1. Project Name");
            System.out.println(" 2. Neighbourhood");
            System.out.println(" 3. Application Opening/Closing Date");
            System.out.println(" 4. Number of Officer Slots");
            System.out.println(" 5. Project Visibility");
            System.out.println(" 0. Save and exit");
            System.out.println("-1. Exit without saving");
            System.out.print("Enter your choice: ");

            try {
                option = scanner.nextInt(); // Read the user's choice
                scanner.nextLine(); // Consume the newline character
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
                option = -2; // Reset option to -2 for re-entry
                continue;
            }
            switch (option) {
                case 1:
                    projectName = handleProjectName(scanner, projectName); // Get new project name
                    break;
                case 2:
                    neighbourhood = handleNeighbourhood(scanner, neighbourhood); // Get new neighbourhood
                    break;
                case 3:
                    List<Date> applicationDates = handleApplicationDates(scanner, manager, applicationOpeningDate, applicationClosingDate); // Get new application dates
                    applicationOpeningDate = applicationDates.get(0); // Get the opening date from the list
                    applicationClosingDate = applicationDates.get(1); // Get the closing date from the list

                    break;
                case 4:
                    availableOfficerSlots = handleOfficerSlots(scanner, availableOfficerSlots); // Get new officer slots
                    break;
                case 5:
                    visibility = handleProjectVisibility(scanner, visibility); // Get new project visibility
                    break;
                case 0:
                    // Save changes and exit
                    try {
                        // Create a new BTOProject object with the updated details
                        BTOProject updatedProject = new BTOProject(projectId, projectName, neighbourhood, flatTypes, flatCount, flatPrice, applicationOpeningDate, applicationClosingDate, manager, project.getAssignedOfficers(), availableOfficerSlots, visibility);

                        // If project name changes, update ApplicationDB, RegistrationDB, and EnquiryDB
                        for (BTOApplication application : ApplicationDB.getApplicationsByProjectName(originalProjectName)) {
                            application.setProject(updatedProject);
                        }
                        for (OfficerRegistration registration : RegistrationDB.getAllRegistrations()) {
                            if (registration.getProject().getName().equals(originalProjectName)) {
                                registration.setProject(updatedProject);
                            }
                        }
                        for (Enquiry enquiry : EnquiryDB.getEnquiryList()) {
                            if (enquiry.getProjectName().equals(originalProjectName)) {
                                enquiry.setProjectName(projectName);
                            }
                        }

                        BTOProjectDB.updateBTOProject(originalProjectName, updatedProject); // Update the project in the database
                        System.out.println("Project updated successfully!");
                    } catch (Exception e) {
                        System.out.println("Error updating project: " + e.getMessage());
                    }
                    break;
                case -1:
                    System.out.println("Exiting without saving changes.");
                    return; // Exit without saving changes
                default:
                    System.out.println("Invalid input. Please enter a number between -1 and 5.");
            }

        } while (option != 0); // Continue until user enters '0'
    }

    /**
     * Delete the project after confirmation
     * 
     * @param manager     The HDB Manager who is deleting the project.
     * @param project     The BTOProject to be deleted. 
     * 
     * @return
     */
    public static void deleteProject(HDBManager manager, BTOProject project) {
        Scanner scanner = new Scanner(System.in);
        String confirmInput = null;
        TerminalUtils.clearScreen();
        System.out.println("\n+---------------------------------+\n" +
                            "|   BTO Project Deletion Wizard   |\n" +
                            "+---------------------------------+");
        System.out.println("Current Project Details:");
        System.out.println("  Project Name: " + project.getName());
        System.out.println("  Neighbourhood: " + project.getNeighbourhood());
        System.out.println("  Flat Types: ");
        for (FlatType flatType : project.getFlatType()) {
            System.out.println("    - " + flatType.getDisplayName() + ": $" + project.getFlatPrices().get(flatType) + " (" + project.getFlatCounts().get(flatType) + " units)");
        }
        System.out.println("  Application Opening Date: " + project.getApplicationOpeningDate());
        System.out.println("  Application Closing Date: " + project.getApplicationClosingDate());
        System.out.println("  Officer Slots: " + project.getAvailableOfficerSlots());
        System.out.println("  Project Visibility: " + (project.getVisibility() ? "Visible" : "Not Visible"));
        System.out.println("+---------------------------------+\n");

        System.out.println("Are you sure you want to delete this project? (Y for Yes, Anything else for No): ");
        confirmInput = scanner.nextLine().trim();
        if (confirmInput.equalsIgnoreCase("Y")) {
            // Check if there are any existing applications for the project
            if (ApplicationDB.getApplicationsByProjectName(project.getName()).size() > 0) {
                System.out.println("Cannot delete project with existing applications. Please remove all applications before deleting the project.");
                System.out.println("Consider setting the project to not visible instead.");
                System.out.println("Press any key to continue...");
                scanner.nextLine(); // Wait for user input
                TerminalUtils.clearScreen(); // Clear the screen after user input
                return; // Exit the method if there are existing applications
            }

            // Check if there are any existing officer registrations for the project
            ArrayList<OfficerRegistration> officerRegistrations = RegistrationDB.getAllRegistrations().stream()
                    .filter(registration -> registration.getProject().getName().equals(project.getName()))
                    .collect(Collectors.toCollection(ArrayList::new));
            if (officerRegistrations.size() > 0) {
                System.out.println("Cannot delete project with existing officer registrations. Please remove all registrations before deleting the project.");
                System.out.println("Press any key to continue...");
                scanner.nextLine(); // Wait for user input
                TerminalUtils.clearScreen(); // Clear the screen after user input
                return; // Exit the method if there are existing registrations
            }

            // Delete the project
            try {
                BTOProjectDB.deleteBTOProject(project); // Delete the project from the database
                System.out.println("Project deleted successfully!");
                System.out.println("Press any key to continue...");
                scanner.nextLine(); // Wait for user input
                TerminalUtils.clearScreen(); // Clear the screen after user input
                return; // Exit the method after successful deletion
            } catch (Exception e) {
                System.out.println("Error deleting project: " + e.getMessage());
            }
        } else {
            System.out.println("Project deletion cancelled.");
            System.out.println("Press any key to continue...");
            scanner.nextLine(); // Wait for user input
            TerminalUtils.clearScreen(); // Clear the screen after user input
            return; // Exit the method if deletion is cancelled
        }
        return;
    }
    /**
     * Handle project name creation/editing.
     * 
     * @param scanner The Scanner object for user input.
     * @param projectName null if project name is not yet created, otherwise the current project name.
     * 
     * @return The project name entered by the user.
     */
    private static String handleProjectName(Scanner scanner, String projectName) {
        String newProjectName = null;
        do {
            if (projectName != null) {
                System.out.println("Current project name: " + projectName);
                System.out.print("Enter the new project name (type 'exit' to go back): ");
            }
            else {
                System.out.print("Enter the project name (type 'exit' to go back): ");
            }
            newProjectName = scanner.nextLine().trim();

            if (newProjectName.isEmpty()) {
                System.out.println("Project name cannot be empty. Please try again.");
                newProjectName = null; // Reset project name to null for re-entry
            } else if (BTOProjectDB.getBTOProjectByName(newProjectName) != null) {
                System.out.println("Project name already exists. Please choose a different name.");
                newProjectName = null; // Reset project name to null for re-entry
            }else if(newProjectName.toLowerCase().contains("exit")){
                TerminalUtils.clearScreen();
                if (projectName != null) {
                    return projectName; // Return the current project name if it exists
                }
                return null;
            }
        } while (newProjectName == null);
        return newProjectName;
    }

    /**
     * Handle project neighbourhood creation/editing.
     * 
     * @param scanner The Scanner object for user input.
     * @param neighbourhood null if neighbourhood is not yet created, otherwise the current neighbourhood.
     * 
     * @return The neighbourhood entered by the user.
     */
    private static String handleNeighbourhood(Scanner scanner, String neighbourhood) {
        String newNeighbourhood = null;
        do {
            if (neighbourhood != null) {
                System.out.println("Current neighbourhood: " + neighbourhood);
                System.out.print("Enter the new neighbourhood (type 'exit' to go cancel creation): ");
            }
            else {
                System.out.print("Enter the neighbourhood (type 'exit' to go cancel creation): ");
            }
            newNeighbourhood = scanner.nextLine().trim();

            if (newNeighbourhood.isEmpty()) {
                System.out.println("Neighbourhood cannot be empty. Please try again.");
                newNeighbourhood = null; // Reset neighbourhood to null for re-entry
            } else if (BTOProjectDB.getBTOProjectByName(newNeighbourhood) != null) {
                System.out.println("Neighbourhood already exists. Please choose a different name.");
                newNeighbourhood = null; // Reset neighbourhood to null for re-entry
            }else if(newNeighbourhood.toLowerCase().contains("exit")){
                TerminalUtils.clearScreen();
                if (neighbourhood != null) {
                    return neighbourhood; // Return the current neighbourhood if it exists
                }
                return null;
            }
        } while (newNeighbourhood == null);
        return newNeighbourhood;
    }

    /**
     * Handle project application opening/closing date creation/editing.
     * 
     * @param scanner The Scanner object for user input.
     * @param manager The HDB Manager who is creating/editing the project.
     * @param applicationOpeningDate null if application opening date is not yet created, otherwise the current application opening date.
     * @param applicationClosingDate null if application closing date is not yet created, otherwise the current application closing date.
     * 
     * @return The application opening and closing date entered by the user.
     */
    private static List<Date> handleApplicationDates(Scanner scanner, HDBManager manager, Date applicationOpeningDate, Date applicationClosingDate) {
        Date newApplicationOpeningDate = null;
        Date newApplicationClosingDate = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        do {
            if (applicationOpeningDate != null) {
                System.out.println("Current application opening date: " + dateFormat.format(applicationOpeningDate));
                System.out.print("Enter the new application opening date (dd/MM/yyyy): ");
            } else {
                System.out.print("Enter the application opening date (dd/MM/yyyy): ");
            }
            String openingDateInput = scanner.nextLine().trim();
            try {
                newApplicationOpeningDate = dateFormat.parse(openingDateInput);

                // Check that applicationOpeningDate is not within application period of any existing project the manager is managing
                for (BTOProject existingProject : BTOProjectDB.getBTOProjectsByManager(manager)) {
                    if (existingProject.getApplicationClosingDate() != null &&
                            (
                                newApplicationOpeningDate.before(existingProject.getApplicationClosingDate()) &&
                                newApplicationOpeningDate.after(existingProject.getApplicationOpeningDate())
                            ) || 
                            (
                                newApplicationOpeningDate.equals(existingProject.getApplicationClosingDate())
                                || newApplicationOpeningDate.equals(existingProject.getApplicationOpeningDate())
                            )
                        ) {
                        System.out.println("Application opening date cannot be within the application period of an existing project you are managing ("+ existingProject.getName() + ", " + dateFormat.format(existingProject.getApplicationOpeningDate()) + " - " + dateFormat.format(existingProject.getApplicationClosingDate()) + "). Please try again.");
                        newApplicationOpeningDate = null; // Reset opening date to null for re-entry
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Please enter the date in dd/MM/yyyy format.");
                newApplicationOpeningDate = null; // Reset opening date to null for re-entry
            }
        } while (newApplicationOpeningDate == null);

        do {
            if (applicationClosingDate != null) {
                System.out.println("Current application closing date: " + dateFormat.format(applicationClosingDate));
                System.out.print("Enter the new application closing date (dd/MM/yyyy): ");
            } else {
                System.out.print("Enter the application closing date (dd/MM/yyyy): ");
            }
            String closingDateInput = scanner.nextLine().trim();
            try {
                newApplicationClosingDate = dateFormat.parse(closingDateInput);
                if (newApplicationClosingDate.before(newApplicationOpeningDate)) {
                    System.out.println("Application closing date cannot be before the opening date. Please try again.");
                    newApplicationClosingDate = null; // Reset closing date to null for re-entry
                }

                // Check that applicationClosingDate is not within application period of any existing project the manager is managing
                for (BTOProject existingProject : BTOProjectDB.getBTOProjectsByManager(manager)) {
                    if (existingProject.getApplicationClosingDate() != null &&
                            (newApplicationClosingDate.before(existingProject.getApplicationClosingDate()) &&
                                    newApplicationClosingDate.after(existingProject.getApplicationOpeningDate()))
                            ||
                            (newApplicationOpeningDate.equals(existingProject.getApplicationClosingDate())
                                    || newApplicationOpeningDate.equals(existingProject.getApplicationOpeningDate()))) {
                        System.out.println(
                                "Application opening date cannot be within the application period of an existing project you are managing ("
                                        + existingProject.getName() + ", "
                                        + dateFormat.format(existingProject.getApplicationOpeningDate()) + " - "
                                        + dateFormat.format(existingProject.getApplicationClosingDate())
                                        + "). Please try again.");
                        newApplicationOpeningDate = null; // Reset opening date to null for re-entry
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Please enter the date in dd/MM/yyyy format.");
                newApplicationClosingDate = null; // Reset closing date to null for re-entry
            }
        } while (newApplicationClosingDate == null);

        return List.of(newApplicationOpeningDate, newApplicationClosingDate); // Return the new application opening and closing dates as a list
    }

    /**
     * Handle number of officer slots creation/editing.
     * 
     * @param scanner The Scanner object for user input.
     * @param availableOfficerSlots null if officer slots is not yet created, otherwise the current officer slots.
     * 
     * @return The number of officer slots entered by the user.
     */
    private static int handleOfficerSlots(Scanner scanner, int availableOfficerSlots) {
        int newAvailableOfficerSlots = -1;
        do {
            if (availableOfficerSlots <= 0) {
                System.out.println("Current officer slots: " + availableOfficerSlots);
                System.out.print("Enter the new number of officer slots: ");
            } else {
                System.out.print("Enter the number of officer slots: ");
            }
            try {
                newAvailableOfficerSlots = scanner.nextInt(); // Read the user's choice
                scanner.nextLine(); // Consume the newline character
                if (newAvailableOfficerSlots <= 0) {
                    System.out.println("Number of officer slots must be a positive number. Please try again.");
                    newAvailableOfficerSlots = -1; // Reset officer slots to -1 for re-entry
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
                newAvailableOfficerSlots = -1; // Reset officer slots to -1 for re-entry
            }
        } while (newAvailableOfficerSlots == -1);
        return newAvailableOfficerSlots;
    }

    /**
     * Handle project visibility creation/editing.
     */
    private static boolean handleProjectVisibility(Scanner scanner, boolean visibility) {
        String visibilityInput = null;
        do {
            System.out.print("Make this project visible to the public? (Y for Yes, N for No): ");
            visibilityInput = scanner.nextLine().trim();
            if (visibilityInput.equalsIgnoreCase("Y")) {
                visibility = true;
            } else if (visibilityInput.equalsIgnoreCase("N")) {
                visibility = false;
            } else {
                System.out.println("Invalid input. Please enter either Y or N.");
            }
        } while (!visibilityInput.equalsIgnoreCase("Y") && !visibilityInput.equalsIgnoreCase("N"));
        return visibility;
    }
}
