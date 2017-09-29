package com.androcid.zomato.retro;

import com.androcid.zomato.model.UserAddress;
import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by spm3 on 4/23/2015.
 */
public class UserAddressResponse {
    @SerializedName(Constant.MESSAGE)
    String message;

    @SerializedName(Constant.SUCCESS)
    boolean success;

    @SerializedName(Constant.DETAILS)
    List<UserAddress> userAddresses;

    public List<UserAddress> getUserAddresses() {
        return userAddresses;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
