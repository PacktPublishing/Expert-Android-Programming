package com.androcid.zomato.model;

import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName(Constant.ID)
    int id;
    @SerializedName(Constant.UID)
    String uid;
    @SerializedName(Constant.NAME)
    String name;
    @SerializedName(Constant.EMAIL)
    String email;
    @SerializedName(Constant.IMAGE)
    String image;
    @SerializedName(Constant.PASSWORD)
    String password;

    @SerializedName(Constant.HANDLE)
    String handle;
    @SerializedName(Constant.LOCATION)
    String location;

    @SerializedName(Constant.PHONE_NO)
    String phone_no;
    @SerializedName(Constant.DESCRIPTION)
    String description;

    @SerializedName(Constant.FOLLOWING)
    boolean following;

    @SerializedName(Constant.FOLLOWER_COUNT)
    int follower_count;

    @SerializedName(Constant.REVIEW_COUNT)
    int review_count;

    public User(int id, String uid, String name, String email, String image) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public boolean isFollowing() {
        return following;
    }

    public int getFollower_count() {
        return follower_count;
    }

    public int getReview_count() {
        return review_count;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
