package com.timeTrackerAPI.timeTrackerAPI.models;

//Skapar en DTO för statistik
public class WeeklyCategoryStat {

    private String categoryName;
    private long minutes;

    //Constructor
    public WeeklyCategoryStat( String categoryName, long minutes) {
        this.categoryName = categoryName;
        this.minutes = minutes;
    }

    //Getters
    public long getMinutes() {
        return minutes;
    }

    public String getCategoryName() {
        return categoryName;
    } 
        
}
