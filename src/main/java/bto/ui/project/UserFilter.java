package bto.ui.project;

import bto.model.project.FlatType;

public class UserFilter {
    private String projectName;
    private String neighbourhood;
    private FlatType flatType;
    private Integer minPrice;
    private Integer maxPrice;

    public UserFilter(String projectName, String neighbourhood, FlatType flatType, Integer minPrice, Integer maxPrice) {
        this.neighbourhood = neighbourhood;
        this.flatType = flatType;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public FlatType getFlatType() {
        return flatType;
    }
    public void setFlatType(FlatType flatType) {
        this.flatType = flatType;
    }

    public Integer getMinPrice() {
        return minPrice;
    }
    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }
    
    public Integer getMaxPrice() {
        return maxPrice;
    }
    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }
}
