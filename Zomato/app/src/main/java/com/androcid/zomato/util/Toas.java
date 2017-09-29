package com.androcid.zomato.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;


public class Toas {

    /**
     * Custom Toast
     *
     * @param context
     * @param message
     */
    public static void show(Context context, String message) {


        Toast toast = Toast.makeText(context, "" + message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 100);
        toast.show();

    }

}
