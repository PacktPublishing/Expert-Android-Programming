package com.androcid.zomato.model;

import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Androcid on 06-02-2017.
 */
public class DinelineItem {

    @SerializedName(Constant.ID)
    int id;

    @SerializedName(Constant.USER_ID)
    int user_id;

    @SerializedName(Constant.TYPE)
    int type;

    @SerializedName(Constant.CREATED_DATE)
    String created_date;


    @SerializedName(Constant.REVIEW)
    ReviewItem review;

    @SerializedName(Constant.IMAGE)
    RestaurantImage image;

    @SerializedName(Constant.BEENTHERE)
    RestaurantItem beenthere;

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getType() {
        return type;
    }

    public String getCreated_date() {
        return created_date;
    }

    public ReviewItem getReview() {
        return review;
    }

    public RestaurantImage getImage() {
        return image;
    }

    public RestaurantItem getBeenthere() {
        return beenthere;
    }
}
