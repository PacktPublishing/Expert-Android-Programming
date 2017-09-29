package com.androcid.zomato.retro;

import com.androcid.zomato.model.RestaurantDetails;
import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

/**
 * Created by spm3 on 4/23/2015.
 */
public class RestaurantDetailResponse {
    @SerializedName(Constant.MESSAGE)
    String message;

    @SerializedName(Constant.SUCCESS)
    boolean success;

    @SerializedName(Constant.DETAILS)
    RestaurantDetails details;

    public RestaurantDetails getDetails() {
        return details;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
