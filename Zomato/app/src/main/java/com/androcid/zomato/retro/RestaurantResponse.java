package com.androcid.zomato.retro;

import com.androcid.zomato.model.RestaurantItem;
import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by spm3 on 4/23/2015.
 */
public class RestaurantResponse {
    @SerializedName(Constant.MESSAGE)
    String message;

    @SerializedName(Constant.SUCCESS)
    boolean success;

    @SerializedName(Constant.DETAILS)
    List<RestaurantItem> items;

    public List<RestaurantItem> getItems() {
        return items;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
