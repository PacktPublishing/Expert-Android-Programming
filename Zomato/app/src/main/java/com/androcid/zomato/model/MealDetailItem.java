package com.androcid.zomato.model;

import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class MealDetailItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName(Constant.ID)
    int id;
    @SerializedName(Constant.NAME)
    String name;
    @SerializedName(Constant.IMAGE)
    String image;
    @SerializedName(Constant.DESCRIPTION)
    String description;

    @SerializedName(Constant.DATA)
    List<RestaurantItem> restaurants;

   /* public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }*/

    public List<RestaurantItem> getRestaurants() {
        return restaurants;
    }

    public MealTypeItem getMealTypeItem() {
        return new MealTypeItem(id, name, image, description);
    }

    public MealTypeItem getMoreTypeItem() {
        return new MealTypeItem(id, "More", image, description);
    }
}
