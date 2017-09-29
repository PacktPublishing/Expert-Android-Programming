package com.androcid.zomato.util;

public class MyLg {

    public static boolean DEBUG = Constant.DEBUG;


    public static boolean DUMMY = true;

    public static void d(String tag, String message) {
        if (DEBUG) {
            android.util.Log.d(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (DEBUG) {
            android.util.Log.e(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (DEBUG) {
            android.util.Log.i(tag, message);
        }
    }

    public static void v(String tag, String message) {
        if (DEBUG) {
            android.util.Log.v(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (DEBUG) {
            android.util.Log.w(tag, message);
        }
    }
}
