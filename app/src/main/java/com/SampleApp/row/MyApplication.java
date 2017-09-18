package com.SampleApp.row;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

/**
 * Created by USER on 26-07-2016.
 */
public class MyApplication extends Application {
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("AppStarted", "application is started bro");
        context = getApplicationContext();
        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.e("Terminated", "Application is terminated");

    }

    public static Context getContext() {
       return context;
    }

}
