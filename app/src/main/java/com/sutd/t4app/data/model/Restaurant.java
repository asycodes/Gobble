package com.sutd.t4app.data.model;

import androidx.annotation.NonNull;

import io.realm.RealmObject;
import io.realm.annotations.*;
import org.bson.types.ObjectId;
public class Restaurant extends RealmObject {
    @PrimaryKey @Required private ObjectId _id;
    private String Address;
    private String Cuisine;
    private String Name;
    private Double Ratings;
    private String Status;
    private String Type;
    private String ClosestLandmark;
    // Standard getters & setters

    public Restaurant(){}

    public ObjectId getId() { return this._id; }
    public void setId(ObjectId _id) { this._id = _id; }
    public String getAddress() { return this.Address; }
    public void setAddress(String Address) { this.Address = Address; }
    public String getCuisine() { return this.Cuisine; }
    public void setCuisine(String Cuisine) { this.Cuisine = Cuisine; }
    public String getName() { return this.Name; }
    public void setName(String Name) { this.Name = Name; }
    public Double getRatings() { return this.Ratings; }
    public void setRatings(Double Ratings) { this.Ratings = Ratings; }
    public String getStatus() { return this.Status; }
    public void setStatus(String Status) { this.Status = Status; }
    public String getType() { return this.Type; }
    public void setType(String Type) { this.Type = Type; }
    public String getClosestLandmark(){return this.ClosestLandmark;}
    public void setClosestLandmark(String ClosestLandmark){this.ClosestLandmark = ClosestLandmark;}
}
