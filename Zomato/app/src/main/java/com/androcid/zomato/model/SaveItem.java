package com.androcid.zomato.model;

import com.androcid.zomato.util.Constant;
import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class SaveItem {

    @SerializedName(Constant.COLLECTION_ID)
    int collection_id;

    @SerializedName(Constant.COUNT)
    int count;

    @SerializedName(Constant.STATUS)
    boolean status;

    public int getCollection_id() {
        return collection_id;
    }

    public int getCount() {
        return count;
    }

    public boolean isStatus() {
        return status;
    }
}
