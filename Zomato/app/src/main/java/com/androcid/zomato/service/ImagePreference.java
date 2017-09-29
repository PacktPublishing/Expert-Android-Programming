package com.androcid.zomato.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ImagePreference {

    public static final String KEY = "ImagePreference";
    private static final String PATH = "path";
    private static final String NAME = "name";

    private static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
    }

    public static boolean clear(Context context) {

        Editor editor = getSharedPreference(context).edit();
        editor.clear();
        return editor.commit();
    }

    public static String getImageName(Context context) {
        SharedPreferences preferences = getSharedPreference(context);
        return preferences.getString(NAME, "");
    }

    public static String getImagePath(Context context) {
        SharedPreferences preferences = getSharedPreference(context);
        return preferences.getString(PATH, "");
    }

    public static boolean setParams(Context context,
                                    String name,
                                    String path) {
        Editor editor = getSharedPreference(context).edit();
        editor.putString(NAME, name);
        editor.putString(PATH, path);
        return editor.commit();
    }
}
