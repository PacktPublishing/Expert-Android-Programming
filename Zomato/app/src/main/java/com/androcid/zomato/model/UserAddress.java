package com.androcid.zomato.model;

import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserAddress implements Serializable {
    private static final long serialVersionUID = 1L;


    @SerializedName(Constant.ID)
    int id;
    @SerializedName(Constant.USER_ID)
    int user_id;
    @SerializedName(Constant.LOCATION_ID)
    int location_id;
    @SerializedName(Constant.LOCATION_NAME)
    String location_name;
    @SerializedName(Constant.ADDRESS)
    String address;
    @SerializedName(Constant.INSTRUCTIONS)
    String instruction;
    @SerializedName(Constant.TYPE)
    String type;

    public UserAddress(int id, int user_id, int location_id, String location_name, String address, String instruction, String type) {
        this.id = id;
        this.user_id = user_id;
        this.location_id = location_id;
        this.location_name = location_name;
        this.address = address;
        this.instruction = instruction;
        this.type = type;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
