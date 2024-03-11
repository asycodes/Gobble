package com.sutd.t4app.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;
import org.bson.types.ObjectId;
public class restaurant extends RealmObject {

    @PrimaryKey
    private ObjectId _id = new ObjectId();
    @Required private String name= "restaurantlorh";
    @Required private String status = restaurantStatus.Open.name();
    public void setStatus(restaurantStatus status) { this.status = status.name(); }
    public String getStatus() {
        return this.status;
    }
    public ObjectId get_id() {
        return _id;
    }
    public void set_id(ObjectId _id) {
        this._id = _id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public restaurant(String _name) { this.name = _name; }
    public restaurant() {}
}
