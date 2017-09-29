package com.androcid.zomato.model;

import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class CollectionItem implements Serializable {
    private static final long serialVersionUID = 1L;


    @SerializedName(Constant.ID)
    int id;

    @SerializedName(Constant.USER_ID)
    int user_id;

    @SerializedName(Constant.NAME)
    String name;

    @SerializedName(Constant.IMAGE)
    String image;

    @SerializedName(Constant.DESCRIPTION)
    String description;

    @SerializedName(Constant.TAGS)
    String tags;

    @SerializedName(Constant.DATA)
    List<String> restaurants;

    //NEW
    @SerializedName(Constant.SAVED)
    boolean saved;

    @SerializedName(Constant.COUNT)
    int count;

    public boolean isSaved() {
        return saved;
    }

    public int getCount() {
        return count;
    }

    public CollectionItem(int id, int user_id, String name, String description, String tags, List<String> restaurants) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.restaurants = restaurants;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<String> restaurants) {
        this.restaurants = restaurants;
    }
}
