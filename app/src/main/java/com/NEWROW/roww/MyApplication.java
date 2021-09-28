package com.NEWROW.row;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;
import android.util.Log;

import com.facebook.stetho.Stetho;


/**
 * Created by USER on 26-07-2016.
 */
public class MyApplication extends Application  {

    public static Context context;
//    public static boolean isAppBackGround;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("AppStarted", "application is started bro");
        context = getApplicationContext();
        MultiDex.install(this);

//        isAppBackGround=false;
        Stetho.initializeWithDefaults(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.e("Terminated", "Application is terminated");
    }

    public static Context getContext() {
       return context;
    }

/*    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded() {
        //App in background
        isAppBackGround=true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded() {
        // App in foreground
        isAppBackGround=false;
    }*/

}
