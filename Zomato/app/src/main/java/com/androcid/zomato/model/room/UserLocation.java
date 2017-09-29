package com.androcid.zomato.model.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

@Entity
public class UserLocation {

    @PrimaryKey(autoGenerate = true)
    @SerializedName(Constant.ID)
    int id;
    @SerializedName(Constant.NAME)
    String name;
    @SerializedName(Constant.LATITUDE)
    float latitude;
    @SerializedName(Constant.LONGITUDE)
    float longitude;

    public UserLocation(int id, String name, float latitude, float longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
