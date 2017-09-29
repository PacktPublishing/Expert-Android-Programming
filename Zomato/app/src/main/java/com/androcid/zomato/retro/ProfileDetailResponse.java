package com.androcid.zomato.retro;

import com.androcid.zomato.model.ProfileDetailItem;
import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

/**
 * Created by spm3 on 4/23/2015.
 */
public class ProfileDetailResponse {
    @SerializedName(Constant.MESSAGE)
    String message;

    @SerializedName(Constant.SUCCESS)
    boolean success;

    @SerializedName(Constant.DETAILS)
    ProfileDetailItem items;

    public ProfileDetailItem getItems() {
        return items;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
