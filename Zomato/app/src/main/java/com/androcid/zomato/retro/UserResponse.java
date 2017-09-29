package com.androcid.zomato.retro;

import com.androcid.zomato.model.User;
import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Androcid on 4/23/2015.
 */
public class UserResponse {
    @SerializedName(Constant.MESSAGE)
    String message;

    @SerializedName(Constant.SUCCESS)
    boolean success;

    @SerializedName(Constant.DETAILS)
    User user;


    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public User getUser() {
        return user;
    }
}
