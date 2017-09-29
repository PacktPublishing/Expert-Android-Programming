package com.androcid.zomato.model;

import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 *{"description": "Pay with Freecharge. Valid on orders above Rs 150 until 31 Jan",
 * "id": 1, "image": "", "restaurant_branch_id": 1, "title": "Get Rs. 75 cashback on 2 orders"}
 */
public class OfferItem implements Serializable {
    private static final long serialVersionUID = 1L;


    @SerializedName(Constant.ID)
    int id;

    @SerializedName(Constant.IMAGE)
    String image;

    @SerializedName(Constant.TITLE)
    String title;

    @SerializedName(Constant.DESCRIPTION)
    String description;

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
