package com.autodesk.tct.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.common.logger.Log;

public class Utility {

    private final static String DB_DATE_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private final static SimpleDateFormat DB_DATE_FORMATTER = new SimpleDateFormat(DB_DATE_FORMAT_STRING);
    private final static SimpleDateFormat DISPLAY_DATE_FORMATTER = new SimpleDateFormat("MMM.dd HH:mm");

    /**
     * DateFormat: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     * 
     * @param dateString
     * @return
     */
    public static Date getDateFromString(String dateString) {
        try {
            return DB_DATE_FORMATTER.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new Date();
    }
    
    public static String parseDateToString(Date date) {
        return DB_DATE_FORMATTER.format(date);
    }

    public static String getCurrentDateString() {
        return DB_DATE_FORMATTER.format(new Date());
    }

    public static String getDisplayDateString(String dateString) {
        Date date = new Date();
        try {
            date = DB_DATE_FORMATTER.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return DISPLAY_DATE_FORMATTER.format(date);
    }

    /**
     * Get a diff between two dates
     * 
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static JSONArray getJSONArray(String profile, String name) {
        if (profile == null) {
            return null;
        }

        JSONArray objects = null;
        try {
            JSONObject objResult = new JSONObject(profile);
            objects = objResult.getJSONArray(name);
        } catch (JSONException e) {
            Log.w("getJSONArray", e.getLocalizedMessage());
        }
        return objects;
    }

}
