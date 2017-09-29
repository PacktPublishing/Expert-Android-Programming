package com.androcid.zomato.model;

import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 *
 */
public class RestaurantItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName(Constant.ID)
    Long id;
    @SerializedName(Constant.RESTAURANT_ID)
    String restaurant_id;
    @SerializedName(Constant.NAME)
    String name;
    @SerializedName(Constant.IMAGE)
    String image;
    @SerializedName(Constant.LOCATION)
    String location;
    @SerializedName(Constant.ADDRESS)
    String address;
    @SerializedName(Constant.PRICE)
    String price;
    @SerializedName(Constant.PAYMENT)
    String payment;
    @SerializedName(Constant.AVG_DELIVERY)
    int avg_delivery;
    @SerializedName(Constant.MIN_ORDER)
    String min_order;

    @SerializedName(Constant.ONLINE_PAYMENT)
    int online_payment;
    @SerializedName(Constant.VEG)
    int veg;

    @SerializedName(Constant.DESCRIPTION)
    String description;
    @SerializedName(Constant.LATITUDE)
    double latitude;
    @SerializedName(Constant.LONGITUDE)
    double longitude;
    @SerializedName(Constant.DISTANCE)
    double distance;
    @SerializedName(Constant.MEAL_TYPE_ID)
    String meal_type_id;
    @SerializedName(Constant.MEAL_TYPE)
    String meal_type;
    @SerializedName(Constant.TYPE)
    String type;
    @SerializedName(Constant.CUISINE)
    String cuisine;
    @SerializedName(Constant.RATING)
    double rating;

    @SerializedName(Constant.BOOKMARK)
    boolean bookmark;

    boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCuisine() {
        return cuisine;
    }

    public int getOnline_payment() {
        return online_payment;
    }

    public void setOnline_payment(int online_payment) {
        this.online_payment = online_payment;
    }

    public int getVeg() {
        return veg;
    }

    public void setVeg(int veg) {
        this.veg = veg;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getMeal_type_id() {
        return meal_type_id;
    }

    public void setMeal_type_id(String meal_type_id) {
        this.meal_type_id = meal_type_id;
    }

    public String getMeal_type() {
        return meal_type;
    }

    public void setMeal_type(String meal_type) {
        this.meal_type = meal_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public int getAvg_delivery() {
        return avg_delivery;
    }

    public void setAvg_delivery(int avg_delivery) {
        this.avg_delivery = avg_delivery;
    }

    public String getMin_order() {
        return min_order;
    }

    public void setMin_order(String min_order) {
        this.min_order = min_order;
    }

    public boolean isBookmark() {
        return bookmark;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }
}
