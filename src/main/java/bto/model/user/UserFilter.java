package bto.model.user;

import bto.model.project.FlatType;

public class UserFilter {
    private static String projectName = null;
    private static String neighbourhood = null;
    private static FlatType flatType = null;
    private static Integer minPrice = null;
    private static Integer maxPrice = null;

    // Constructor removed as it's not needed for static access

    public static String getProjectName() {
        return projectName;
    }
    public static void setProjectName(String projectName) {
        UserFilter.projectName = projectName;
    }

    public static String getNeighbourhood() {
        return neighbourhood;
    }

    public static void setNeighbourhood(String neighbourhood) {
        UserFilter.neighbourhood = neighbourhood;
    }

    public static FlatType getFlatType() {
        return flatType;
    }
    public static void setFlatType(FlatType flatType) {
        UserFilter.flatType = flatType;
    }

    public static Integer getMinPrice() {
        return minPrice;
    }
    public static void setMinPrice(Integer minPrice) {
        UserFilter.minPrice = minPrice;
    }
    
    public static Integer getMaxPrice() {
        return maxPrice;
    }
    public static void setMaxPrice(Integer maxPrice) {
        UserFilter.maxPrice = maxPrice;
    }

    public static void printFiltersApplied() {
        String sb = " Filters Applied: ";
        if (projectName != null) {
            sb += "Project Name: " + projectName + ", ";
        }
        if (neighbourhood != null) {
            sb += "Neighbourhood: " + neighbourhood + ", ";
        }
        if (flatType != null) {
            sb += "Flat Type: " + flatType.getDisplayName() + ", ";
        }
        if (minPrice != null) {
            sb += "Min Price: $" + minPrice + ", ";
        }
        if (maxPrice != null) {
            sb += "Max Price: $" + maxPrice + ", ";
        }
        if (sb.equals(" Filters Applied: ")) {
            return; // No filters applied, do not print anything
        } else {
            sb = sb.substring(0, sb.length() - 2); // Remove the last comma and space
            System.out.println(sb); // Print the filters applied
        }
    }
}
