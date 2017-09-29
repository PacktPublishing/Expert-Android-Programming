package com.androcid.zomato.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.androcid.zomato.model.User;


public class SessionPreference {

    private static final String KEY = "SessionPreference";

    private static final String IS_LOGGED_IN = "IS_LOGGED_IN";
    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String EMAIL = "EMAIL";
    private static final String IMAGE = "IMAGE";
    private static final String UID = "UID";


    /*SET SESSION PARAMS*/
    public static boolean setUserParams(Context context, User user) {

        Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.putInt(ID, user.getId());
        editor.putString(NAME, user.getName());
        editor.putString(EMAIL, user.getEmail());
        editor.putString(IMAGE, user.getImage());
        editor.putString(UID, user.getUid());
        editor.putBoolean(IS_LOGGED_IN, true);
        return editor.commit();
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return preferences.getBoolean(IS_LOGGED_IN, false);
    }

    public static User getUser(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return new User(
                preferences.getInt(ID, 0),
                preferences.getString(UID, ""),
                preferences.getString(NAME, ""),
                preferences.getString(EMAIL, ""),
                preferences.getString(IMAGE, "")
        );
    }



    public static boolean clear(Context context) {
        Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.clear();
        return editor.commit();
    }


    public static int getUserId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return preferences.getInt(ID, 0);
    }
}
