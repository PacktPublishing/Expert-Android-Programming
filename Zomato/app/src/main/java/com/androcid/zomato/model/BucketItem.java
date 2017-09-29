package com.androcid.zomato.model;

import java.io.Serializable;

/**
 *
 */
public class BucketItem implements Serializable {


    private static final long serialVersionUID = 1L;

    String id;
    String name;
    String path;
    int count;
    int selected;

    public BucketItem(String id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }


    public String getPath() {
        return path;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
