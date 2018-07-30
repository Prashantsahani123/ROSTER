package com.SampleApp.row.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.SampleApp.row.Announcement_details;
import com.SampleApp.row.Documents;
import com.SampleApp.row.E_Bulletin;
import com.SampleApp.row.EventDetails;
import com.SampleApp.row.GroupInfo_New;
import com.SampleApp.row.Improvement_details;
import com.SampleApp.row.Profile;
import com.SampleApp.row.R;
import com.SampleApp.row.Splash;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.calendar.CalendarNotificationDetails;
import com.google.android.gms.gcm.GcmListenerService;

import java.util.Random;

/**
 * Created by kundan on 10/22/2015.
 */
public class PushNotificationService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("Message");
        Log.d("TOUCHBASE", "NOTIFICATION " + message);
        Log.d("TOUCHBASE", "ARRAY " + data.toString());
        createNotification(data);
    }

    public void createNotification(Bundle data) {
        // Prepare intent which is triggered if the
        // notification is selected
        Log.e("TouchBase", "♦♦♦♦Notification data" + data);
        String message = data.getString("Message");
        String entityName = "";
        if (data.containsKey("entityName")) {
            entityName = data.getString("entityName");
        }
        String finalEntity = "";
        if (entityName.equals("")) {
            finalEntity = "";
        } else {
            finalEntity = " - " + entityName;
        }
        // Intent intent = new Intent(this, PushNotificationService.class);
        Intent intent = new Intent();
        //savePreference(PushNotificationService.this, IS_GRP_ADMIN, "No");
        if (data.getString("type").equalsIgnoreCase("Event")) {
            intent = new Intent(this, EventDetails.class);
            intent.putExtra("eventid", data.getString("typeID"));
            intent.putExtra("grpID", data.getString("grpID"));
            intent.putExtra("memID", data.getString("memID"));
            intent.putExtra("isAdmin", "No");
            try {
                PreferenceManager.savePreference(getBaseContext(), PreferenceManager.MODUEL_NAME, "Event");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (data.getString("type").equalsIgnoreCase("ann")) {
            intent = new Intent(this, Announcement_details.class);
            intent.putExtra("announcemet_id", data.getString("typeID"));
            intent.putExtra("grpID", data.getString("grpID"));
            intent.putExtra("memID", data.getString("memID"));
            intent.putExtra("isAdmin", "No");
            intent.putExtra("moduleID", data.getString("moduleID"));
            try {
                intent.putExtra("moduleName", data.getString("moduleName"));
                PreferenceManager.savePreference(getBaseContext(), PreferenceManager.MODUEL_NAME, "Announcement");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (data.getString("type").equalsIgnoreCase("Ebulletin")) {
            intent = new Intent(this, E_Bulletin.class);
            intent.putExtra("grpID", data.getString("grpID"));
            intent.putExtra("memID", data.getString("memID"));
            intent.putExtra("isAdmin", data.getString("isAdmin"));
            try {
                PreferenceManager.savePreference(getBaseContext(), PreferenceManager.MODUEL_NAME, "Newsletters");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (data.getString("type").equalsIgnoreCase("AddMember")) {
            intent = new Intent(this, Profile.class);
            intent.putExtra("typeID", data.getString("typeID"));
            intent.putExtra("memID", data.getString("memID"));
            intent.putExtra("grpID", data.getString("grpID"));
            intent.putExtra("isAdmin", "No");

        } else if (data.getString("type").equalsIgnoreCase("RemoveMember")) {
            intent = new Intent(this, Splash.class); // Check it
        } else if (data.getString("type").equalsIgnoreCase("Doc")) {
            intent = new Intent(this, Documents.class);
            intent.putExtra("grpID", data.getString("grpID"));
            intent.putExtra("memID", data.getString("memID"));
            intent.putExtra("isAdmin", data.getString("isAdmin"));
            try {
                PreferenceManager.savePreference(getBaseContext(), PreferenceManager.MODUEL_NAME, "Documents");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (data.getString("type").equalsIgnoreCase("EditGroup")) {
            intent = new Intent(this, GroupInfo_New.class);
            intent.putExtra("grpID", data.getString("grpID"));
            intent.putExtra("memID", data.getString("memID"));
            intent.putExtra("isAdmin", "No");
        } else if (data.getString("type").equalsIgnoreCase("imp")) {
            intent = new Intent(this, Improvement_details.class);
            intent.putExtra("improvementId", data.getString("typeID"));
            intent.putExtra("grpID", data.getString("grpID"));
            intent.putExtra("memID", data.getString("memID"));
            intent.putExtra("isAdmin", "No");
        } else if (data.getString("type").equalsIgnoreCase("Calender")) {
            intent = new Intent(this, CalendarNotificationDetails.class);
            intent.putExtra("grpID", data.getString("grpID"));
            intent.putExtra("memID", data.getString("memID"));
        }

        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

       // Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle("ROW" + finalEntity)
                .setContentText("" + message)
                .setStyle(new Notification.BigTextStyle().bigText(message))
                .setSmallIcon(R.drawable.touchbase_icon_transparent_one)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pIntent)
                .setSound(Utils.notiUri)
                .build();
        // .addAction(R.mipmap.ic_launcher, "Call", pIntent)
        // .addAction(R.mipmap.ic_launcher, "More", pIntent)
        /// .addAction(R.mipmap.ic_launcher, "And more", pIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;


        Random r = new Random();
        int i1 = r.nextInt(99 - 1 + 1) + 1;
        if (PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID) != null) {
            notificationManager.notify(i1, noti);
        }


    }
}
