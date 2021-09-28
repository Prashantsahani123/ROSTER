package com.NEWROW.row;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.NEWROW.row.Utils.Constant;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;

/**
 * Created by USER on 15-12-2015.
 */
public class Splash extends Activity {

    private static int SPLASH_TIME_OUT = 3000;

    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash);

        tracker = getDefaultTracker();

        //ReplicaInfoModel model = new ReplicaInfoModel(this);
        //model.printTable();

        /*Intent intent = new Intent(Splash.this, ReplicaInfoSyncerService.class);
        startService(intent);*/

        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {

                // This method will be executed once the timer is over
                // Start your app main activity

                 Intent i = new Intent(getApplicationContext(), com.NEWROW.row.SplashImageSlider.class);

                 i.putExtra("fromApp", "yes");  // fromApp is added to SplashImageSlider is launched from this point.

                 //SplashImageSlider activity can also be opened by clicking on Invite Link
                 // Intent i = new Intent(getApplicationContext(), CreateGroupModule.class);
                 startActivity(i);

                 finish();
            }

        }, SPLASH_TIME_OUT);

        new UpdateServicesTask().execute();
    }

    public class UpdateServicesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            updateAndroidSecurityProvider(Splash.this);
            return null;
        }
    }

    private void updateAndroidSecurityProvider(Activity callingActivity) {

        try {

            ProviderInstaller.installIfNeeded(this);

        } catch (GooglePlayServicesRepairableException e) {
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0);

        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e("SecurityException", "Google Play Services not available.");
        }
    }

    synchronized public Tracker getDefaultTracker() {

        if (tracker == null) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);

            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG

            analytics.setLocalDispatchPeriod(Constant.DISPATCH_PERIOD);
            tracker = analytics.newTracker(Constant.ANALYTICS_TRACKER_ID);
            tracker.enableExceptionReporting(true);
            tracker.enableAutoActivityTracking(true);

            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("UX")
                    .setAction("click")
                    .setLabel("submit")
                    .build());
        }

        return tracker;
    }
}
