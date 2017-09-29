package com.androcid.zomato.model;

import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class ReviewItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName(Constant.ID)
    Long id;

    @SerializedName(Constant.RESTAURANT_BRANCH_ID)
    String restaurant_id;

    @SerializedName(Constant.DESCRIPTION)
    String description;

    @SerializedName(Constant.RATING)
    double rating;

    @SerializedName(Constant.CREATED_DATE)
    String created_date;

    @SerializedName(Constant.USER)
    User user;

    @SerializedName(Constant.RESTAURANT)
    RestaurantItem restaurantItem;

    @SerializedName(Constant.IMAGE)
    List<RestaurantImage> restaurantImages;


    public Long getId() {
        return id;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public String getDescription() {
        return description;
    }

    public double getRating() {
        return rating;
    }

    public User getUser() {
        return user;
    }

    public RestaurantItem getRestaurantItem() {
        return restaurantItem;
    }

    public String getCreated_date() {
        return created_date;
    }

    public List<RestaurantImage> getRestaurantImages() {
        return restaurantImages;
    }
}
