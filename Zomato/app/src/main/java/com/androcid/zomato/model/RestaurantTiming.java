package com.androcid.zomato.model;

import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

public class RestaurantTiming {

    @SerializedName(Constant.ID)
    int id;
    @SerializedName(Constant.RESTAURANT_BRANCH_ID)
    String restaurant_branch_id;
    @SerializedName(Constant.DAY)
    String day;
    @SerializedName(Constant.FROM_TIME)
    String from_time;
    @SerializedName(Constant.TO_TIME)
    String to_time;
    @SerializedName(Constant.CLOSED)
    int closed;

    public int getId() {
        return id;
    }

    public String getRestaurant_branch_id() {
        return restaurant_branch_id;
    }

    public String getDay() {
        return day;
    }

    public String getFrom_time() {
        return from_time;
    }

    public String getTo_time() {
        return to_time;
    }

    public int getClosed() {
        return closed;
    }


    public String getTiming() {
       return from_time+" to "+to_time;
    }
}
