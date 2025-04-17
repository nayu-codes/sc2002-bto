package bto.ui;

import bto.model.user.MaritalStatus;

@FunctionalInterface
public interface ProjectFilter {
    /**
     * Getting available projects based on the age and marital status filter
     * 
     * @param age The age of the user.
     * @param maritalStatus The marital status of the user (e.g., "Single", "Married").
     */
    void applyUserFilters(int age, MaritalStatus maritalStatus);
}