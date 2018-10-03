package com.SampleApp.row.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.SampleApp.row.Data.DashboardData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 11-12-2015.
 */
public class PreferenceManager {
    public static final String APPLICATION_PREFERENCE = "Touchbase";
    public static final String MASTER_USER_ID = "masteruid";
    public static final String GROUP_ID = "grpid";
    public static final String GRP_PROFILE_ID = "grpprofileid";
    public static final String IS_GRP_ADMIN = "isgrpadmin";
    public static final String IS_AG = "isAG"; // add by satish
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
    public static final String REQUESTED = "requested_";
    public static final String eventList="eventList";
    // public static final String KEY_AUTH_TOKEN = "authType";
   // public static final String KEY_OPENFIRE_PWD = "openFirePwd";

    public static void saveListPreference(Context context, String key, ArrayList<DashboardData> value) {
        try {
            Gson gson = new Gson();
            Type listOfObjects = new TypeToken<List<DashboardData>>(){}.getType();
            String strObject = gson.toJson(value, listOfObjects);

            SharedPreferences sharedPref = context.getSharedPreferences(APPLICATION_PREFERENCE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            Utils.log("Jso Array: "+strObject);
            editor.putString(key, strObject);
            editor.commit();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<DashboardData> getListPreference(Context context, String key) {
        try {
            Gson gson = new Gson();
            Type listOfObjects = new TypeToken<List<DashboardData>>(){}.getType();
            SharedPreferences sharedPref = context.getSharedPreferences(APPLICATION_PREFERENCE, Context.MODE_PRIVATE);

            String json = sharedPref.getString(key, "");
            ArrayList<DashboardData> dashboardDataList = gson.fromJson(json, listOfObjects);
            return dashboardDataList;
            //list= (ArrayList<DashboardData>) ObjectSerializer.deserialize(sharedPref.getString(key, ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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

//    public static boolean saveListPreference(Context context, String key, ArrayList<DashboardData> value) {
//        try {
//            SharedPreferences sharedPref = context.getSharedPreferences(APPLICATION_PREFERENCE, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putString(key, ObjectSerializer.serialize(value));
//            return editor.commit();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public static ArrayList<DashboardData> getListPreference(Context context, String key) {
//        SharedPreferences sharedPref = context.getSharedPreferences(APPLICATION_PREFERENCE, Context.MODE_PRIVATE);
//        ArrayList<DashboardData> list=null;
//        try {
//            list= (ArrayList<DashboardData>) ObjectSerializer.deserialize(sharedPref.getString(key, ObjectSerializer.serialize(new ArrayList<String>())));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }


}
