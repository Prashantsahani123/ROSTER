package com.SampleApp.row.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by user on 11-12-2015.
 */
public class PreferenceManager {
    public static final String APPLICATION_PREFERENCE = "Touchbase";
    public static final String MASTER_USER_ID = "masteruid";
    public static final String GROUP_ID = "grpid";
    public static final String GRP_PROFILE_ID = "grpprofileid";
    public static final String IS_GRP_ADMIN = "isgrpadmin";
    public static final String GROUP_NAME = "grpname";
    public static final String UDID = "udid";
    public static final String MODULE_ID = "moduleId";
    public static final String MODUEL_NAME = "moduleName";
    public static final String isGroupEdited = "No";
    public static final String MY_CATEGORY = "myCategory";
    public static final String LOGIN_TYPE = "loginType";
    public static final String MOBILE_NUMBER = "mobileNo";
    public static final String COUNTRY_CODE = "countryCode";
    public static final String GROUP_EXPIRY_DATE = "groupExpiryDate_";
    // public static final String KEY_AUTH_TOKEN = "authType";
   // public static final String KEY_OPENFIRE_PWD = "openFirePwd";


    public static boolean savePreference(Context context, String key, String value) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(APPLICATION_PREFERENCE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(key, value);
            return editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getPreference(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(APPLICATION_PREFERENCE, Context.MODE_PRIVATE);
        String value = sharedPref.getString(key, null);
        return value;
    }

    // added by Prasad. To get the shared preference with some default value
    public static String getPreference(Context context, String key, String defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(APPLICATION_PREFERENCE, Context.MODE_PRIVATE);
        String value = sharedPref.getString(key, defaultValue);
        return value;
    }

}
