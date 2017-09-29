package com.androcid.zomato.dao;

import android.content.Context;

import com.androcid.zomato.model.PhotoItem;
import com.androcid.zomato.model.RestaurantImage;

import java.util.List;

/**
 * Created by spm3 on 5/29/2015.
 */
public class DaoController {

    private static final String TAG = DaoController.class.getSimpleName();

    public static void addRestaurantImages(Context context, List<RestaurantImage> item) {
        DaoFunctions functions = new DaoFunctions(context);
        functions.updateRestaurantImages(item);
        functions.close();
    }

    public static List<RestaurantImage> getRestaurantImages(Context context) {
        DaoFunctions functions = new DaoFunctions(context);
        List<RestaurantImage> items = functions.getRestaurantImages();
        functions.close();
        return items;
    }


    public static void addPhotoItems(Context context, List<PhotoItem> item) {
        DaoFunctions functions = new DaoFunctions(context);
        functions.deletePhotoItem();
        functions.updatePhotoItems(item);
        functions.close();
    }

    public static void deletePhotoItem(Context context) {
        DaoFunctions functions = new DaoFunctions(context);
        functions.deletePhotoItem();
        functions.close();
    }


    public static void addPhotoItem(Context context, PhotoItem item) {
        DaoFunctions functions = new DaoFunctions(context);
        functions.updatePhotoItem(item);
        functions.close();
    }

    public static List<PhotoItem> getPhotoItems(Context context) {
        DaoFunctions functions = new DaoFunctions(context);
        List<PhotoItem> items = functions.getPhotoItems();
        functions.close();
        return items;
    }

    public static List<PhotoItem> getRemainingPhotoItems(Context context) {
        DaoFunctions functions = new DaoFunctions(context);
        List<PhotoItem> items = functions.getRemainingPhotoItems();
        functions.close();
        return items;
    }

}
