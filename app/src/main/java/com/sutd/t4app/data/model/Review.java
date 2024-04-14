package com.sutd.t4app.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

import org.bson.types.ObjectId;

public class Review extends RealmObject {
    @PrimaryKey
    @Required
    private ObjectId _id;

    private Integer Likes;

    private double Rating;

    private String img_post_link;

    private String Restaurant_id;
    private String Address;
    private String Cuisine;
    private String Type;
    private String ClosestLandmark;
    private String DietaryOptions;
    private String PriceRange;
    private String Ambience;

    private String Review;

    private String User_id;
    private String user_img_link;

    private String username;
    private String source;

    // Standard getters & setters
    public ObjectId getId() { return _id; }
    public void setId(ObjectId _id) { this._id = _id; }

    public Integer getLikes() { return Likes; }
    public void setLikes(Integer Likes) { this.Likes = Likes; }

    public double getRating() { return this.Rating; }
    public void setRating(double Rating) { this.Rating = Rating; }

    public String getRestaurantId() { return Restaurant_id; }
    public void setRestaurantId(String Restaurant_id) { this.Restaurant_id = Restaurant_id; }

    public String getReview() { return Review; }
    public void setReview(String Review) { this.Review = Review; }

    public String getUserId() { return User_id; }
    public void setUserId(String User_id) { this.User_id = User_id; }
    public String getAddress() { return this.Address; }
    public void setAddress(String Address) { this.Address = Address; }
    public String getCuisine() { return this.Cuisine; }
    public void setCuisine(String Cuisine) { this.Cuisine = Cuisine; }
    public String getType() { return this.Type; }
    public void setType(String Type) { this.Type = Type; }
    public String getClosestLandmark(){return this.ClosestLandmark;}
    public void setClosestLandmark(String ClosestLandmark){this.ClosestLandmark = ClosestLandmark;}
    public String getDietaryOptions() {
        return DietaryOptions;
    }

    public void setDietaryOptions(String dietaryOptions) {
        DietaryOptions = dietaryOptions;
    }

    public String getPriceRange() {
        return PriceRange;
    }

    public void setPriceRange(String priceRange) {
        PriceRange = priceRange;
    }
    public String getAmbience() {
        return Ambience;
    }

    public void setAmbience(String ambience) {
        Ambience = ambience;
    }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
        public void setImg_post_link(String newLink){this.img_post_link = newLink;}
        public String getImg_post_link(){return this.img_post_link;}

    public String getUser_img_link(){return this.user_img_link;}
    public void setUser_img_link(String userimglink){this.user_img_link=userimglink;}


    public String getSource(){return this.source;}
        public void setSource(String newSource){this.source = newSource;}


    }
