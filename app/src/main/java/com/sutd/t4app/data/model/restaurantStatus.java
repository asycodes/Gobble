package com.sutd.t4app.data.model;

// This Java code snippet defines an enum called `restaurantStatus` inside the
// `com.sutd.t4app.data.model` package. The enum has three constants: `Open`, `InProgress`, and
// `Complete`, each with a corresponding display name. The constructor of the enum takes a
// `displayName` parameter and assigns it to the `displayName` field of each enum constant. This enum
// can be used to represent the status of a restaurant in an application.
public enum restaurantStatus {
    Open("Open"),
    InProgress("In Progress"),
    Complete("Complete");
    String displayName;
    restaurantStatus(String displayName) {
        this.displayName = displayName;
    }
}
