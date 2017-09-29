package com.androcid.zomato.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

/**
 * Common Functions required across all projects
 */

public class CommonFunctions {

    public static final String Date_yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String Date_yyyyMMdd = "yyyy-MM-dd";
    public static final String Date_ddMMMyyyy = "dd MMM yyyy";
    public static final String Date_ddMMMMyyyy = "dd MMMM yyyy";
    public static final String Date_ddMMM = "dd MMM";
    public static final String Date_HHmm = "hh:mm a";
    public static final String Date_HHmm_ = "HH:mm";
    private static final String TAG = CommonFunctions.class.getSimpleName();
    private static String SERVER_TZ = "GMT";

    public static int randInt(int min, int max) {
        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static void colorizeToolbar(Toolbar toolbarView, int toolbarIconsColor) {
        final PorterDuffColorFilter colorFilter
                = new PorterDuffColorFilter(toolbarIconsColor, PorterDuff.Mode.MULTIPLY);

        for (int i = 0; i < toolbarView.getChildCount(); i++) {
            final View v = toolbarView.getChildAt(i);

            //Step 1 : Changing the color of back button (or open drawer button).
            if (v instanceof ImageButton) {
                //Action Bar back button
                ((ImageButton) v).getDrawable().setColorFilter(colorFilter);
            }
        }
    }

    public static String capitaliseFirst(String name) {
        try {
            name = name.toLowerCase();
            String[] test = name.split(" ");
            String result = "";
            String cap = "";
            for (int i = 0; i < test.length; i++) {
                cap = test[i];
                result += makeCapital(cap, i != 0) + " ";
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    private static String makeCapital(String cap, boolean b) {
        if (b) {
            String[] noCap = {"since", "because", "to", "it", "that", "at", "or", "is", "a", "in", "the", "of", "an"};
            List<String> dontCap = Arrays.asList(noCap);

            if (dontCap.contains(cap.toLowerCase())) {
                return cap;
            }
        }
        return cap.substring(0, 1).toUpperCase(Locale.US) + cap.substring(1).toLowerCase(Locale.US);
    }

    /**
     * formateDateFromstring(String inputFormat, String outputFormat, String inputDate)
     *
     * @param inputFormat  specifies input format of date
     * @param outputFormat format of the output
     * @param inputDate    current value of date with input format
     * @return date in output format
     */
    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            MyLg.e("TAG", "ParseException - dateFormat");
        }

        return outputDate;
    }

    /**
     * formateDateFromstring(String inputFormat, String outputFormat, String inputDate)
     *
     * @param outputFormat format of the output
     * @param inputDate    current value of date with input format
     * @return date in output format
     */
    public static String formateDate(String outputFormat, Date inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = inputDate;
            outputDate = df_output.format(parsed);

        } catch (Exception e) {
            MyLg.e("TAG", "ParseException - dateFormat");
        }

        return outputDate;
    }

    /**
     * formateDateFromstring(String inputFormat, String outputFormat, String inputDate)
     *
     * @param outputFormat format of the output
     * @param inputDate    current value of date with input format
     * @return date in output format
     */
    public static String formateDateTZ(String outputFormat, Date inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());
        try {
            parsed = inputDate;
            df_output.setTimeZone(TimeZone.getTimeZone("SERVER_TZ"));
            outputDate = df_output.format(parsed);

        } catch (Exception e) {
            MyLg.e("TAG", "ParseException - dateFormat");
        }

        return outputDate;
    }

    /**
     * This function is used to check if the time supplied has already occurred or not
     *
     * @param time Time to check against current time format yyyy-MM-dd HH:mm:ss
     * @return true is already passed false if not occurred
     */
    public static Boolean hasTimepassed(String time) {
        boolean hasPassed = false;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date;
        try {
            date = simpleDateFormat.parse(time);

            long check_time = date.getTime();
            long current_time = new Date().getTime();

            long difference = current_time - check_time;        //POSITIVE IF ALREADY OCCURED
            MyLg.e(TAG, current_time + "  " + check_time);
            hasPassed = (difference > 0);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return hasPassed;
    }

    /**
     * This function is used to check if the time supplied has already occurred or not
     *
     * @param time Time to check against current time format yyyy-MM-dd HH:mm:ss
     * @return true is already passed false if not occurred
     */
    public static Date GetDate(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    /**
     * This function is used to check if the time supplied has already occurred or not
     *
     * @param time Time to check against current time format yyyy-MM-dd HH:mm:ss
     * @return true is already passed false if not occurred
     */
    public static Date GetDate(String time, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(time);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    /**
     * This function is used to check if the time supplied has already occurred or not
     *
     * @param time Time to check against current time format yyyy-MM-dd HH:mm:ss
     * @return true is already passed false if not occurred
     */
    public static Boolean hasTimepassed(Date time) {
        boolean hasPassed = false;
        Date date;

        date = time;

        long check_time = date.getTime();
        long current_time = new Date().getTime();

        long difference = current_time - check_time;        //POSITIVE IF ALREADY OCCURED
        MyLg.e(TAG, current_time + "  " + check_time);
        hasPassed = (difference > 0);
        return hasPassed;
    }

    /**
     * This function if d2 more than d1
     *
     * @return true is already passed false if not occurred
     */
    public static boolean hasTimepassed(String d1, String d2) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date1 = null;
        Date date2 = null;
        boolean hasPassed = true;
        try {
            date1 = simpleDateFormat.parse(d1);
            date2 = simpleDateFormat.parse(d2);

            long check_time = date1.getTime();
            long current_time = date2.getTime();

            long difference = current_time - check_time;        //POSITIVE IF ALREADY OCCURED
            MyLg.e(TAG, current_time + "  " + check_time);
            hasPassed = (difference >= 0);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return hasPassed;
    }

    /**
     * This function if d2 more than d1
     *
     * @param d1     start
     * @param d2     end
     * @param format chosen format
     * @return
     */
    public static boolean hasTimepassed(String d1, String d2, String format) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        Date date1 = null;
        Date date2 = null;
        boolean hasPassed = true;
        try {
            date1 = simpleDateFormat.parse(d1);
            date2 = simpleDateFormat.parse(d2);

            long check_time = date1.getTime();
            long current_time = date2.getTime();

            long difference = current_time - check_time;        //POSITIVE IF ALREADY OCCURED
            MyLg.e(TAG, current_time + "  " + check_time);
            hasPassed = (difference > 0);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        return hasPassed;
    }

    public static Date getDateFromJulean(Long due) {
        try {

            double jd = due;
            double z, f, a, b, c, d, e, m, aux;
            Date date = new Date();
            jd += 0.5;
            z = Math.floor(jd);
            f = jd - z;

            if (z >= 2299161.0) {
                a = Math.floor((z - 1867216.25) / 36524.25);
                a = z + 1 + a - Math.floor(a / 4);
            } else {
                a = z;
            }

            b = a + 1524;
            c = Math.floor((b - 122.1) / 365.25);
            d = Math.floor(365.25 * c);
            e = Math.floor((b - d) / 30.6001);
            aux = b - d - Math.floor(30.6001 * e) + f;
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, (int) aux);

            double hhd = aux - calendar.get(Calendar.DAY_OF_MONTH);
            aux = ((aux - calendar.get(Calendar.DAY_OF_MONTH)) * 24);


            calendar.set(Calendar.HOUR_OF_DAY, (int) aux);

            calendar.set(Calendar.MINUTE, (int) ((aux - calendar.get(Calendar.HOUR_OF_DAY)) * 60));


            // Calcul secondes
            double mnd = (24 * hhd) - calendar.get(Calendar.HOUR_OF_DAY);
            double ssd = (60 * mnd) - calendar.get(Calendar.MINUTE);
            int ss = (int) (60 * ssd);
            calendar.set(Calendar.SECOND, ss);


            if (e < 13.5) {
                m = e - 1;
            } else {
                m = e - 13;
            }
            // Se le resta uno al mes por el manejo de JAVA, donde los meses empiezan en 0.
            calendar.set(Calendar.MONTH, (int) m - 1);
            if (m > 2.5) {
                calendar.set(Calendar.YEAR, (int) (c - 4716));
            } else {
                calendar.set(Calendar.YEAR, (int) (c - 4715));
            }


            return calendar.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;

        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static String printTimeDifference(long different) {

        //milliseconds

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String res = "";
        if (elapsedDays != 0) {
            res = String.format("%d days ago",/*, %d h, %d m, %d s%n*/
                    elapsedDays/*,elapsedHours, elapsedMinutes, elapsedSeconds*/);
        } else if (elapsedHours != 0) {
            res = String.format("%d hours ago" /*, %d m, %d s%n*/, elapsedHours/*, elapsedMinutes, elapsedSeconds*/);
        } else if (elapsedMinutes != 0) {
            res = String.format("%d minutes ago"/*, %d s%n*/, elapsedMinutes/*, elapsedSeconds*/);
        } else {
            res = String.format("%d seconds ago", elapsedSeconds);
        }

		/*System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
				elapsedDays,
				elapsedHours, elapsedMinutes, elapsedSeconds);*/
        MyLg.e(TAG, "Final " + res);
        return res;
    }

    public static String getTimePassed(String format, String date) {

        SimpleDateFormat parse = new SimpleDateFormat(format, Locale.getDefault());

        Date d1 = new Date();
        Date d2 = null;

        long diff = 0;
        try {

            d2 = parse.parse(date);

            MyLg.e(TAG, "d1 " + d1);
            MyLg.e(TAG, "d2 " + d2);

            //in milliseconds
            diff = d1.getTime() - d2.getTime();
            if (diff > 0) {

                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                long diffDays = diff / (24 * 60 * 60 * 1000);

                MyLg.e("diff", diffDays + " days, ");
                MyLg.e("diff", diffHours + " hours, ");
                MyLg.e("diff", diffMinutes + " minutes, ");
                MyLg.e("diff", diffSeconds + " seconds.");
            } else {


                MyLg.e("Times up", "times up");
            }

        } catch (Exception e) {
            e.printStackTrace();
            MyLg.e("dssp", "format" + e.getMessage());
        }
        return printTimeDifference(diff);
    }

    //FOR TIMEZONE

    public static String getFirstDayMonth(String outputFormat, Date inputDate) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        inputDate = calendar.getTime();

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());
        try {
            parsed = inputDate;
            outputDate = df_output.format(parsed);

        } catch (Exception e) {
            MyLg.e("TAG", "ParseException - dateFormat");
        }

        return outputDate;
    }

    public static String getTimeByIndianTimeZone(String outputFormat, String inputDate) {

        TimeZone timeZone = TimeZone.getDefault();
        TimeZone timeZoneOther = TimeZone.getTimeZone(SERVER_TZ);

        MyLg.e(TAG, "TimeZone Device " + timeZone.getDisplayName());
        MyLg.e(TAG, "TimeZone Other " + timeZoneOther.getDisplayName());


        SimpleDateFormat sourceFormat = new SimpleDateFormat(outputFormat);
        sourceFormat.setTimeZone(timeZone);
        Date parsed = null; // => Date is in UTC now
        try {
            parsed = sourceFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat destFormat = new SimpleDateFormat(outputFormat);
        destFormat.setTimeZone(timeZoneOther);

        String result = destFormat.format(parsed);

        return result;
    }

    public static String getTimeByCurrentTimeZone(String outputFormat, String inputDate) {


        TimeZone timeZoneOther = TimeZone.getDefault();
        TimeZone timeZone = TimeZone.getTimeZone(SERVER_TZ);

        MyLg.e(TAG, "TimeZone Device " + timeZone.getDisplayName());
        MyLg.e(TAG, "TimeZone Other " + timeZoneOther.getDisplayName());


        SimpleDateFormat sourceFormat = new SimpleDateFormat(outputFormat);
        sourceFormat.setTimeZone(timeZone);
        Date parsed = null; // => Date is in UTC now
        try {
            parsed = sourceFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat destFormat = new SimpleDateFormat(outputFormat);
        destFormat.setTimeZone(timeZoneOther);

        String result = destFormat.format(parsed);

        return result;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static double makeRound(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / (double) scale;
    }

    public static String formatRating(double rating) {
        return String.format("%.01f", rating);
    }

    public static String formatDistance(double distance) {

        if(distance < 1000){
            return String.format("%.0f", distance)+" m";
        }
        distance = distance/1000f;
        return String.format("%.01f", distance)+" Km";
    }

    public static String checkNull(String text) {
        return text!= null ? text : "";
    }
    public static String checkNull(Uri uri) {
        return uri!= null ? uri.toString() : "";
    }

    public static Map<String, String> getHashMap(Object o) {
        Gson gson = new Gson();
        String json = gson.toJson(o);

       // MyLg.e(TAG, "Json "+json);

        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public static Bitmap scaleDownBitmap(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }



}
