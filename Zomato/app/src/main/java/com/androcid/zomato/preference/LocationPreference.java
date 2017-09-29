package com.androcid.zomato.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.androcid.zomato.model.room.UserLocation;


public class LocationPreference {

    private static final String KEY = "LocationPreference";

    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String HAS_LOCATION = "HAS_LOCATION";


    /*SET SESSION PARAMS*/
    public static boolean setLocationParams(Context context, UserLocation user) {

        Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.putInt(ID, user.getId());
        editor.putString(NAME, user.getName());
        editor.putFloat(LATITUDE, user.getLatitude());
        editor.putFloat(LONGITUDE, user.getLongitude());
        editor.putBoolean(HAS_LOCATION, true);
        return editor.commit();
    }

    public static boolean hasLocation(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return preferences.getBoolean(HAS_LOCATION, false);
    }

    public static UserLocation getUserLocation(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return new UserLocation(
                preferences.getInt(ID, 0),
                preferences.getString(NAME, ""),
                preferences.getFloat(LATITUDE, 0f),
                preferences.getFloat(LONGITUDE, 0f)
        );
    }

    public static boolean clear(Context context) {
        Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.clear();
        return editor.commit();
    }
}
