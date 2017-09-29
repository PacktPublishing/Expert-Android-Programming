package com.androcid.zomato.model;

/**
 *
 */
public class MealTypeItem {

    int id;
    String name;
    String image;
    String description;

    public MealTypeItem(int id, String name, String image, String description) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
