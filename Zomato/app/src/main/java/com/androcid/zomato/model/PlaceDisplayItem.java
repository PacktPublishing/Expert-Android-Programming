package com.androcid.zomato.model;

/**
 *
 */
public class PlaceDisplayItem {

    RestaurantItem restaurantItem;
    MealTypeItem mealTypeItem;
    int type;

    public PlaceDisplayItem(MealTypeItem mealTypeItem, int type) {
        this.mealTypeItem = mealTypeItem;
        this.type = type;
    }

    public PlaceDisplayItem(RestaurantItem restaurantItem, int type) {
        this.restaurantItem = restaurantItem;
        this.type = type;
    }

    public MealTypeItem getMealTypeItem() {
        return mealTypeItem;
    }

    public RestaurantItem getRestaurantItem() {
        return restaurantItem;
    }

    public int getType() {
        return type;
    }
}
