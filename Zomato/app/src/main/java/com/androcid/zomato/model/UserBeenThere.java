package com.androcid.zomato.model;

import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Androcid on 06-02-2017.
 */
public class UserBeenThere {

    @SerializedName(Constant.BEENTHERE_COUNT)
    int beenthere_count;

    @SerializedName(Constant.USER_BEENTHERE)
    boolean user_beenthere;

    public int getBeenthere_count() {
        return beenthere_count;
    }

    public boolean isUser_beenthere() {
        return user_beenthere;
    }
}
