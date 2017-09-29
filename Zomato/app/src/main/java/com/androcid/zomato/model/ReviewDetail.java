package com.androcid.zomato.model;

import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Androcid on 06-02-2017.
 */
public class ReviewDetail {

    @SerializedName(Constant.REVIEW_COUNT)
    int review_count;


    public int getReview_count() {
        return review_count;
    }
}
