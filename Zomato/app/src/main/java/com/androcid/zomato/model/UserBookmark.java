package com.androcid.zomato.model;

import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Androcid on 06-02-2017.
 */
public class UserBookmark {

    @SerializedName(Constant.BOOKMARK_COUNT)
    int bookmark_count;

    @SerializedName(Constant.USER_BOOKMARK)
    boolean user_bookmark;


    public int getBookmark_count() {
        return bookmark_count;
    }

    public boolean isUser_bookmark() {
        return user_bookmark;
    }
}
