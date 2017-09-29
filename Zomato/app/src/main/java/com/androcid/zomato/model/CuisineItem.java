package com.androcid.zomato.model;

import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 *
 */
public class CuisineItem implements Serializable {
    private static final long serialVersionUID = 1L;


    @SerializedName(Constant.ID)
    int id;

    @SerializedName(Constant.NAME)
    String name;

    @SerializedName(Constant.IMAGE)
    String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
