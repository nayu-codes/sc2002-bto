package bto.ui.report;

import bto.controller.ReportController;
import bto.model.user.HDBManager;
import bto.model.user.MaritalStatus;
import bto.model.project.FlatType;
import bto.ui.TerminalUtils;

import java.util.Scanner;

public class ReportDashboard {
    private ReportDashboard(){} // Prevents Instantiation

    public static void start(HDBManager manager) {
        int option = -1;

        Scanner scanner = new Scanner(System.in);

        do {
            // Clear the screen and display the welcome message
            TerminalUtils.clearScreen();
            System.out.println("\n+--------------------------------+\n" +
                                "|         Booking Reports         |\n" +
                                "+---------------------------------+");

            // Display the menu options
            System.out.println("1. View Overall BTO Booking Report");
            System.out.println("2. View BTO Booking Report with filters");
            System.out.println("0. Exit");
            System.out.println("+---------------------------------+");

            // Get user input for the menu option
            System.out.print("Please select an option: ");
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue; // Skip to the next iteration of the loop
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                continue; // Skip to the next iteration of the loop
            }

            // Handle the selected option
            switch (option) {
                case 1:
                    TerminalUtils.clearScreen();
                    ReportController.generateReport(manager);
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine(); // Wait for user to press Enter
                    break;

                case 2:
                    int filterOption = -1;
                    int filterNumberInput = -2;
                    Integer minAge = null;
                    Integer maxAge = null;
                    String projectName = null;
                    MaritalStatus maritalStatus = null;
                    FlatType flatType = null;
                    // Clear the screen and display the filter options
                    TerminalUtils.clearScreen();
                    do {
                        System.out.println("Current filters applied:");
                        if (minAge != null) {
                            System.out.println("  Min Age: " + minAge);
                        } else {
                            System.out.println("  Min Age: Not set");
                        }
                        if (maxAge != null) {
                            System.out.println("  Max Age: " + maxAge);
                        } else {
                            System.out.println("  Max Age: Not set");
                        }
                        if (projectName != null) {
                            System.out.println("  Project Name: " + projectName);
                        } else {
                            System.out.println("  Project Name: Not set");
                        }
                        if (maritalStatus != null) {
                            System.out.println("  Marital Status: " + maritalStatus.getStatus());
                        } else {
                            System.out.println("  Marital Status: Not set");
                        }
                        if (flatType != null) {
                            System.out.println("  Flat Type: " + flatType.getDisplayName());
                        } else {
                            System.out.println("  Flat Type: Not set");
                        }

                        System.out.println("Please enter the filters you want to apply:");
                        System.out.println("1. Min Age");
                        System.out.println("2. Max Age");
                        System.out.println("3. Project Name");
                        System.out.println("4. Marital Status");
                        System.out.println("5. Flat Type");
                        System.out.println("0. Generate Report with applied filters");
                        System.out.println("+---------------------------------+");
                        System.out.print("Please select an option: ");
                        try {
                            filterOption = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a number.");
                            continue; // Skip to the next iteration of the loop
                        } catch (Exception e) {
                            System.out.println("An unexpected error occurred: " + e.getMessage());
                            continue; // Skip to the next iteration of the loop
                        }

                        switch (filterOption) {
                            case 1:
                                System.out.print("Enter Min Age (-1 to reset): ");
                                try {
                                    filterNumberInput = Integer.parseInt(scanner.nextLine());
                                    if (filterNumberInput == -1) {
                                        minAge = null; // Reset Min Age
                                        break;
                                    }
                                    if (filterNumberInput < 0) {
                                        System.out.println("Age cannot be negative. Please try again.");
                                        continue;
                                    }
                                    if (maxAge != null && filterNumberInput > maxAge) {
                                        System.out.println("Min Age cannot be greater than Max Age. Please try again.");
                                        continue;
                                    }
                                    minAge = filterNumberInput;
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input. Please enter a number.");
                                } catch (Exception e) {
                                    System.out.println("An unexpected error occurred: " + e.getMessage());
                                }
                                break;

                            case 2:
                                System.out.print("Enter Max Age (-1 to reset): ");
                                try {
                                    filterNumberInput = Integer.parseInt(scanner.nextLine());
                                    if (filterNumberInput == -1) {
                                        maxAge = null; // Reset Max Age
                                        break;
                                    }
                                    if (filterNumberInput < 0) {
                                        System.out.println("Age cannot be negative. Please try again.");
                                        continue;
                                    }
                                    if (minAge != null && filterNumberInput < minAge) {
                                        System.out.println("Max Age cannot be less than Min Age. Please try again.");
                                        continue;
                                    }
                                    maxAge = filterNumberInput;
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input. Please enter a number.");
                                } catch (Exception e) {
                                    System.out.println("An unexpected error occurred: " + e.getMessage());
                                }
                                break;

                            case 3:
                                System.out.print("Enter Project Name (leave blank to reset): ");
                                projectName = scanner.nextLine();
                                if (projectName.isEmpty()) {
                                    projectName = null; // Reset Project Name
                                }
                                break;

                            case 4:
                                System.out.print("Enter Marital Status ('Single', 'Married', or leave blank to reset): ");
                                String maritalStatusInput = scanner.nextLine().toUpperCase();
                                if (maritalStatusInput.isEmpty()) {
                                    maritalStatus = null; // Reset Marital Status
                                    break;
                                }
                                try {
                                    maritalStatus = MaritalStatus.valueOf(maritalStatusInput);
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Invalid marital status. Please try again.");
                                } catch (Exception e) {
                                    System.out.println("An unexpected error occurred: " + e.getMessage());
                                }
                                break;

                            case 5:
                                System.out.print("Enter Flat Type ('2' for 2-Room, '3' for 3-Room, or '0' to reset): ");
                                try {
                                    filterNumberInput = Integer.parseInt(scanner.nextLine());
                                    if (filterNumberInput == 0) {
                                        flatType = null; // Reset Flat Type
                                        break;
                                    }
                                    if (filterNumberInput < 2 || filterNumberInput > 3) {
                                        System.out.println("Invalid Flat Type. Please try again.");
                                        continue;
                                    }
                                    if (filterNumberInput == 2) {
                                        flatType = FlatType.TWO_ROOM;
                                    } else if (filterNumberInput == 3) {
                                        flatType = FlatType.THREE_ROOM;
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input. Please enter a number.");
                                } catch (Exception e) {
                                    System.out.println("An unexpected error occurred: " + e.getMessage());
                                }
                                break;

                            case 0:
                                // Clear the screen and generate report
                                TerminalUtils.clearScreen();
                                break;

                            default:
                                System.out.println("Invalid option. Please try again.");
                        }

                    } while (filterOption != 0);

                    ReportController.generateFilteredReport(manager, minAge, maxAge, maritalStatus, projectName, flatType);
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine(); // Wait for user to press Enter
                    break;

                case 0:
                    System.out.println("Exiting the report dashboard.");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }

        } while (option != 0);
    }
}
