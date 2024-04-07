package com.sutd.t4app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import io.realm.RealmObject;
import io.realm.annotations.*;
import org.bson.types.ObjectId;
public class Restaurant extends RealmObject implements Parcelable {
    @PrimaryKey @Required private ObjectId _id;
    private String Address;
    private String Cuisine;
    private String Name;
    private Double Ratings;
    private String Status;
    private String Type;
    private String ClosestLandmark;
    private String imgMainURL;
    // Standard getters & setters

    public Restaurant(){}

    protected Restaurant(Parcel in) {
        Address = in.readString();
        Cuisine = in.readString();
        Name = in.readString();
        if (in.readByte() == 0) {
            Ratings = null;
        } else {
            Ratings = in.readDouble();
        }
        Status = in.readString();
        Type = in.readString();
        ClosestLandmark = in.readString();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };
    public String getImgMainURL(){
        return this.imgMainURL;
    }
    public void setImgMainURL(String url){
        this.imgMainURL = url;
    }
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(Address);
        dest.writeString(Cuisine);
        dest.writeString(Name);
        if (Ratings == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(Ratings);
        }
        dest.writeString(Status);
        dest.writeString(Type);
        dest.writeString(ClosestLandmark);
    }
}
