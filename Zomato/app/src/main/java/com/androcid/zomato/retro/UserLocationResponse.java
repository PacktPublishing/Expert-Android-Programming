package com.androcid.zomato.retro;

import com.androcid.zomato.model.room.UserLocation;
import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by spm3 on 4/23/2015.
 */
public class UserLocationResponse {
    @SerializedName(Constant.MESSAGE)
    String message;

    @SerializedName(Constant.SUCCESS)
    boolean success;

    @SerializedName(Constant.DETAILS)
    List<UserLocation> userLocations;

    public List<UserLocation> getUserLocations() {
        return userLocations;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
