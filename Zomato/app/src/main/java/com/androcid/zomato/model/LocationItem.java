package com.androcid.zomato.model;

/**
 *
 */
public class LocationItem {


    String name;
    String address;

    public LocationItem(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
