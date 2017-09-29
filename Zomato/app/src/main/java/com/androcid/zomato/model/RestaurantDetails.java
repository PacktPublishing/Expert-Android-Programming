package com.androcid.zomato.model;

import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 */
public class RestaurantDetails{

    @SerializedName(Constant.IMAGE)
    List<RestaurantImage> images;

    @SerializedName(Constant.PHOTO)
    List<RestaurantImage> photo;

    @SerializedName(Constant.MENU)
    List<RestaurantMenu> menu;

    @SerializedName(Constant.TIMING)
    List<RestaurantTiming> timings;

    @SerializedName(Constant.REVIEW)
    List<ReviewItem> reviews;

    @SerializedName(Constant.REVIEW_DETAIL)
    ReviewDetail reviewDetail;

    @SerializedName(Constant.BOOKMARK)
    UserBookmark bookmark;

    @SerializedName(Constant.BEENTHERE)
    UserBeenThere beenThere;

    public ReviewDetail getReviewDetail() {
        return reviewDetail;
    }

    public List<RestaurantImage> getImages() {
        return images;
    }

    public List<RestaurantImage> getPhoto() {
        return photo;
    }

    public List<RestaurantTiming> getTimings() {
        return timings;
    }

    public List<ReviewItem> getReviews() {
        return reviews;
    }

    public UserBookmark getBookmark() {
        return bookmark;
    }

    public UserBeenThere getBeenThere() {
        return beenThere;
    }

    public List<RestaurantMenu> getMenu() {
        return menu;
    }
}
