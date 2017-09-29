package com.androcid.zomato.util;


import java.util.Calendar;

/**
 * All Functions required across the app
 */
public class AppUtils {

    public static String getPlaceType(int i) {
        int type = i % 2;
        switch (type) {
            case 0:
                return "SPONSORED";
            case 1:
                return "RECOMMENDED";
        }
        return "SPONSORED";
    }

    public static int getDay(String day) {

        if(day.equals("Mon"))
            return Calendar.MONDAY;
        if(day.equals("Tue"))
            return Calendar.TUESDAY;
        if(day.equals("Wed"))
            return Calendar.WEDNESDAY;
        if(day.equals("Thu"))
            return Calendar.THURSDAY;
        if(day.equals("Fri"))
            return Calendar.FRIDAY;
        if(day.equals("Sat"))
            return Calendar.SATURDAY;
        if(day.equals("Sun"))
            return Calendar.SUNDAY;

        return Calendar.MONDAY;
    }
}
