package com.autodesk.tct.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesUtil {

	private final static String PREFERENCE_NAME = "autodesk.tct";
	
	private final static String USER_ACCESS_TOKEN = "user.access.token";
	private final static String USER_ID = "user.id";
	private final static String USER_NAME = "user.name";
	private final static String USER_LOGIN_TIME = "user.login.time";
	
	
	private static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
	}
	
	private static Editor getEditor(Context context) {
		return getPreferences(context).edit();
	}
	
	private static void saveBooleanValue(Context context, String key, boolean value) {
        Editor editor = getEditor(context);
        editor.putBoolean(key, value);
        editor.commit();
    }

    private static boolean getBooleanValue(Context context, String key, boolean defaultValue) {
        return getPreferences(context).getBoolean(key, defaultValue);
    }
	
    private static void saveIntValue(Context context, String key, int value) {
        Editor editor = getEditor(context);
        editor.putInt(key, value);
        editor.commit();
    }

    private static int getIntValue(Context context, String key, int defaultValue) {
        return getPreferences(context).getInt(key, defaultValue);
    }

    private static void saveLongValue(Context context, String key, long value) {
        Editor editor = getEditor(context);
        editor.putLong(key, value);
        editor.commit();
    }
    
    private static long getLongValue(Context context, String key, long defaultValue) {
        return getPreferences(context).getLong(key, defaultValue);
    }
    
	private static void saveStringValue(Context context, String key, String value) {
		Editor editor = getEditor(context);
		editor.putString(key, value);
		editor.commit();
	}
	
	private static String getStringValue(Context context, String key, String defaultValue) {
		return getPreferences(context).getString(key, defaultValue);
	}
	
	public static void saveUserAccessToken(Context context, String accessToken) {
		saveStringValue(context, USER_ACCESS_TOKEN, accessToken);
	}
	
	public static String getUserAccessToken(Context context) {
		return getStringValue(context, USER_ACCESS_TOKEN, null);
	}
	
	public static void saveUserId(Context context, String id) {
		saveStringValue(context, USER_ID, id);
	}
	
	public static String getUserId(Context context) {
		return getStringValue(context, USER_ID, null);
	}
	
	public static void saveUserName(Context context, String name) {
		saveStringValue(context, USER_NAME, name);
	}
	
	public static String getUserName(Context context) {
		return getStringValue(context, USER_NAME, null);
	}
	
	public static void saveUserLoginTime(Context context, long loginTime) {
		saveLongValue(context, USER_LOGIN_TIME, loginTime);
	}
	
	public static long getUserLoginTime(Context context) {
		return getLongValue(context, USER_LOGIN_TIME, 0);
	}
}
