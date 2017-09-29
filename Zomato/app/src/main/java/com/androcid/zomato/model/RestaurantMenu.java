package com.androcid.zomato.model;

import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Androcid on 18-01-2017.
 */
public class RestaurantMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName(Constant.ID)
    Long id;

    @SerializedName(Constant.RESTAURANT_BRANCH_ID)
    String restaurant_branch_id;

    @SerializedName(Constant.MENU_ID)
    String menu_id;

    @SerializedName(Constant.IMAGE)
    String image;

    @SerializedName(Constant.NAME)
    String name;

    public Long getId() {
        return id;
    }

    public String getRestaurant_branch_id() {
        return restaurant_branch_id;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}

