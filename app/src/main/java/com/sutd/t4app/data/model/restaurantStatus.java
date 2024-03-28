package com.sutd.t4app.data.model;

public enum restaurantStatus {
    Open("Open"),
    InProgress("In Progress"),
    Complete("Complete");
    String displayName;
    restaurantStatus(String displayName) {
        this.displayName = displayName;
    }
}
