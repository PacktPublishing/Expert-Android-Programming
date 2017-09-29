package com.androcid.zomato.model;

import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class ProfileDetailItem{


    @SerializedName(Constant.BEENTHERE_COUNT)
    int beenthere_count;

    @SerializedName(Constant.COLLECTION_COUNT)
    int collection_count;

    @SerializedName(Constant.FOLLOWER_COUNT)
    int follower_count;

    @SerializedName(Constant.USER_LEVEL)
    int user_level;

    @SerializedName(Constant.USER)
    User user;

    public int getBeenthere_count() {
        return beenthere_count;
    }

    public int getCollection_count() {
        return collection_count;
    }

    public int getFollower_count() {
        return follower_count;
    }

    public int getUser_level() {
        return user_level;
    }

    public User getUser() {
        return user;
    }
}
