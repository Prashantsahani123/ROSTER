package com.NEWROW.row.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.util.Log;

import com.NEWROW.row.Announcement_details;
import com.NEWROW.row.CalenderActivity;
import com.NEWROW.row.DashboardActivity;
import com.NEWROW.row.Documents;
import com.NEWROW.row.E_Bulletin;
import com.NEWROW.row.EventDetails;
import com.NEWROW.row.GalleryDescription;
import com.NEWROW.row.GroupInfo_New;
import com.NEWROW.row.Improvement_details;
import com.NEWROW.row.NotificationDataBase.DatabaseHelper;
import com.NEWROW.row.Profile;
import com.NEWROW.row.R;
import com.NEWROW.row.Splash;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;


/**
 * Created by satish patel on 07/05/2019.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String messageId;
    private Context context;

    public static final String TAG = "MyFirebaseMessagingService";

    //Expired Date and Current Date,current Time

    public static String expiredDate, notification_Date, notification_Time;

    //DataBase class object
    DatabaseHelper databaseHelpers;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        context = MyFirebaseMessagingService.this;

        Log.e("TOUCHBASE22222", "Notification onMessageReceived called");

        messageId = remoteMessage.getMessageId();

        Map<String, String> data = remoteMessage.getData();

        if (data.size() > 0) {
            Log.d("TOUCHBASE", "Notification Data=> " + data.toString());
            //Save the Notification Data in Database
            inserNotificationInDB(data);
            //Create the Notification
            createNotification(data);
        }
    }

    private void inserNotificationInDB(Map<String, String> data) {

        //SET Notification  Date and TIME

        setCurrentAndExpiredDate();


        Log.d("datamessage",data.toString());

        //Create Database Helper Class Object
        databaseHelpers = new DatabaseHelper(this);
        //Inserted Data into Database
        boolean notificationInsert = databaseHelpers.insertNotificationDetailsWithDate(notification_Date, notification_Time, "1", "0", messageId, expiredDate, data);

        if (notificationInsert == true) {


            Log.d("PARAMETERS", "Notification Data Inserted successfully");

        }

    }

    @SuppressLint("LongLogTag")
    public static void setCurrentAndExpiredDate() {

        //Notification Sent  Date(Current Day)

        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        notification_Date = format.format(today);   //notification sent date

        Log.d(TAG, "Notification Date is" + notification_Date);


        //Notification Sent  Time(Current time)

        DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        notification_Time = dateFormat.format(new Date()).toString();

        Log.d(TAG, "Notification  sent Time :" + notification_Time);

        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DATE, 3);// 3 days of Notification

        int day = cal.get(Calendar.DAY_OF_MONTH); // 17
        int month = cal.get(Calendar.MONTH) + 1; // 5
        int year = cal.get(Calendar.YEAR); // 2016


        if (day <= 9 && month <= 9) {
            //both months less than 9
            expiredDate = "0" + String.valueOf(day) + "-" + "0" + String.valueOf(month) + "-" + String.valueOf(year);
        } else if (day > 9 && month <= 9) {
            //date is more than 9 and months less than 9

            expiredDate = String.valueOf(day) + "-" + "0" + String.valueOf(month) + "-" + String.valueOf(year);
        } else if (day <= 9 && month > 9) {
            //date is less than 9 and months more than 9
            expiredDate = "0" + String.valueOf(day) + "-" + String.valueOf(month) + "-" + String.valueOf(year);
        } else {
            //both are more than 9
            expiredDate = String.valueOf(day) + "-" + String.valueOf(month) + "-" + String.valueOf(year);

        }

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    public void createNotification(Map<String, String> data) {

        String message = data.get("Message");

        String entityName = "";

        if (data.containsKey("entityName")) {
            entityName = data.get("entityName");
        }

        /*String finalEntity = "";

        if (entityName.equals("")) {
            finalEntity = "";
        } else {
            finalEntity = " - " + entityName;
        }*/

        // Intent intent = new Intent(this, PushNotificationService.class);
        Intent intent = new Intent();

        String type = data.get("type");

        //savePreference(PushNotificationService.this, IS_GRP_ADMIN, "No");
        if (type.equalsIgnoreCase("Event")) {

            intent = new Intent(this, EventDetails.class);

            intent.putExtra("eventid", data.get("typeID"));
            intent.putExtra("grpID", data.get("grpID"));
            //  intent.putExtra("memID", data.get("memID"));
            intent.putExtra("isAdmin", "No");
            intent.putExtra("fromNotification", "yes");
            intent.putExtra("eventImg", data.get("eventImg"));
            intent.putExtra("eventTitle", data.get("eventTitle"));
            intent.putExtra("eventDesc", data.get("eventDesc"));
            intent.putExtra("venue", data.get("venue"));
            intent.putExtra("reglink", data.get("reglink"));
            intent.putExtra("eventDate", data.get("eventDate"));
            intent.putExtra("rsvpEnable", data.get("rsvpEnable"));
            intent.putExtra("goingCount", data.get("goingCount"));
            intent.putExtra("maybeCount", data.get("maybeCount"));
            intent.putExtra("notgoingCount", data.get("notgoingCount"));
            intent.putExtra("totalCount", data.get("totalCount"));
            intent.putExtra("myResponse", data.get("myResponse"));
            intent.putExtra("questionType", data.get("questionType"));
            intent.putExtra("questionText", data.get("questionText"));
            intent.putExtra("questionID", data.get("questionID"));
            intent.putExtra("isQuesEnable", data.get("isQuesEnable"));
            intent.putExtra("option1", data.get("option1"));
            intent.putExtra("option2", data.get("option2"));
            intent.putExtra("entity name", entityName);
            intent.putExtra("group_category", data.get("group_category"));

            //Extra Field add by Gaurav
            intent.putExtra("messageId", messageId);


            try {

                PreferenceManager.savePreference(getBaseContext(), PreferenceManager.MODUEL_NAME, "Event");
                //This code is added by Gaurav
                //  if grpID="-1".then data comes from Rotary India module i.e Call API of Rotary India NewsLetter API
                if (data.get("grpID").equalsIgnoreCase("-1")) {
                    PreferenceManager.savePreference(getBaseContext(), PreferenceManager.isRIadminModule, "Yes");
                    //Consider MasterUId as profile id in Rotary India admin  module
                    Long masterUid = Long.parseLong(PreferenceManager.getPreference(getBaseContext(), PreferenceManager.MASTER_USER_ID));

                    String profile_id = String.valueOf(PreferenceManager.savePreference(getBaseContext(), PreferenceManager.GRP_PROFILE_ID, String.valueOf(masterUid)));
                    intent.putExtra("memID", profile_id);


                } else {
                    intent.putExtra("memID", data.get("memID"));

                    PreferenceManager.savePreference(getBaseContext(), PreferenceManager.isRIadminModule, "No");

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (type.equalsIgnoreCase("ann")) {

            intent = new Intent(this, Announcement_details.class);

            intent.putExtra("announcemet_id", data.get("typeID"));
            intent.putExtra("grpID", data.get("grpID"));
            intent.putExtra("memID", data.get("memID"));
            intent.putExtra("isAdmin", "No");
            intent.putExtra("moduleID", data.get("moduleID"));
            intent.putExtra("fromNotification", "yes");
            intent.putExtra("ann_img", data.get("ann_img"));
            intent.putExtra("ann_title", data.get("ann_title"));
            intent.putExtra("Ann_date", data.get("Ann_date"));
            intent.putExtra("ann_lnk", data.get("ann_lnk"));
            intent.putExtra("ann_desc", data.get("ann_desc"));
            intent.putExtra("entity name", entityName);
            intent.putExtra("group_category", data.get("group_category"));

            try {
                intent.putExtra("moduleName", data.get("moduleName"));
                intent.putExtra("messageId", messageId);
                PreferenceManager.savePreference(getBaseContext(), PreferenceManager.MODUEL_NAME, "Announcement");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (type.equalsIgnoreCase("Ebulletin")) {

            intent = new Intent(this, E_Bulletin.class);
            intent.putExtra("grpID", data.get("grpID"));
            //This below intent added base on grpID if grpID="-1" then pass memID=profile id

            //  intent.putExtra("memID", data.get("memID"));
            intent.putExtra("isAdmin", data.get("isAdmin"));
            //Extra field added by Gaurav
            intent.putExtra("messageId", messageId);

            try {
                PreferenceManager.savePreference(getBaseContext(), PreferenceManager.MODUEL_NAME, "Newsletters");
                //This code is added by Gaurav
                //  if grpID="-1".then data comes from Rotary India module i.e Call API of Rotary India NewsLetter API
                if (data.get("grpID").equalsIgnoreCase("-1")) {
                    PreferenceManager.savePreference(getBaseContext(), PreferenceManager.isRIadminModule, "Yes");
                    //Consider MasterUId as profile id in Rotary India admin  module
                    Long masterUid = Long.parseLong(PreferenceManager.getPreference(getBaseContext(), PreferenceManager.MASTER_USER_ID));

                    String profile_id = String.valueOf(PreferenceManager.savePreference(getBaseContext(), PreferenceManager.GRP_PROFILE_ID, String.valueOf(masterUid)));
                    intent.putExtra("memID", profile_id);


                } else {

                    PreferenceManager.savePreference(getBaseContext(), PreferenceManager.GRP_PROFILE_ID,data.get("memID"));

                    intent.putExtra("memID", data.get("memID"));

                    PreferenceManager.savePreference(getBaseContext(), PreferenceManager.isRIadminModule, "No");

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (type.equalsIgnoreCase("AddMember")) {

            intent = new Intent(this, Profile.class);
            intent.putExtra("typeID", data.get("typeID"));
            intent.putExtra("memID", data.get("memID"));
            intent.putExtra("grpID", data.get("grpID"));
            intent.putExtra("isAdmin", "No");

            //Extra field added by Gaurav
            intent.putExtra("messageId", messageId);


        } else if (type.equalsIgnoreCase("RemoveMember")) {

            intent = new Intent(this, Splash.class); // Check it

        } else if (type.equalsIgnoreCase("Doc")) {

            intent = new Intent(this, Documents.class);
            intent.putExtra("grpID", data.get("grpID"));
            //This below intent added base on grpID if grpID="-1" then pass memID=profile id
            // intent.putExtra("memID", data.get("memID"));
            intent.putExtra("isAdmin", data.get("isAdmin"));
            //Extra field added by Gaurav
            intent.putExtra("messageId", messageId);


            try {
                PreferenceManager.savePreference(getBaseContext(), PreferenceManager.MODUEL_NAME, "Documents");
                //This code is added by Gaurav
                //  if grpID="-1".then data comes from Rotary India module i.e Call API of Rotary India NewsLetter API
                if (data.get("grpID").equalsIgnoreCase("-1")) {
                    PreferenceManager.savePreference(getBaseContext(), PreferenceManager.isRIadminModule, "Yes");
                    //Consider MasterUId as profile id in Rotary India admin  module
                    Long masterUid = Long.parseLong(PreferenceManager.getPreference(getBaseContext(), PreferenceManager.MASTER_USER_ID));

                    String profile_id = String.valueOf(PreferenceManager.savePreference(getBaseContext(), PreferenceManager.GRP_PROFILE_ID, String.valueOf(masterUid)));
                    intent.putExtra("memID", profile_id);


                } else {


                    PreferenceManager.savePreference(getBaseContext(), PreferenceManager.GRP_PROFILE_ID,data.get("memID"));
                    intent.putExtra("memID", data.get("memID"));

                    PreferenceManager.savePreference(getBaseContext(), PreferenceManager.isRIadminModule, "No");

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (type.equalsIgnoreCase("EditGroup")) {

            intent = new Intent(this, GroupInfo_New.class);
            intent.putExtra("grpID", data.get("grpID"));
            intent.putExtra("memID", data.get("memID"));
            intent.putExtra("isAdmin", "No");
            //Extra field added by Gaurav
            intent.putExtra("messageId", messageId);


        } else if (type.equalsIgnoreCase("imp")) {

            intent = new Intent(this, Improvement_details.class);
            intent.putExtra("improvementId", data.get("typeID"));
            intent.putExtra("grpID", data.get("grpID"));
            intent.putExtra("memID", data.get("memID"));
            intent.putExtra("isAdmin", "No");
            //Extra field added by Gaurav
            intent.putExtra("messageId", messageId);


        } else if (type.equalsIgnoreCase("Calender")) {
//            intent = new Intent(this, CalendarNotificationDetails.class);
//            intent.putExtra("grpID", data.getString("grpID"));
//            intent.putExtra("memID", data.getString("memID"));

            intent = new Intent(this, CalenderActivity.class);

            PreferenceManager.savePreference(getBaseContext(), PreferenceManager.GROUP_ID, data.get("grpID"));
            PreferenceManager.savePreference(getBaseContext(), PreferenceManager.GRP_PROFILE_ID, data.get("memID"));
            PreferenceManager.savePreference(getBaseContext(), PreferenceManager.MY_CATEGORY, data.get("GroupType"));

            intent.putExtra("fromNoti", "1");
            intent.putExtra("date", data.get("Todays"));
            intent.putExtra("type", data.get("CelebrationType"));
            //Extra field added by Gaurav
            intent.putExtra("messageId", messageId);


        } else if (type.equalsIgnoreCase("Activity")) {// For club
            intent = new Intent(this, GalleryDescription.class);
            intent.putExtra("albumID", data.get("typeID"));
            intent.putExtra("fromNoti", "1");
            //Extra field added by Gaurav
            intent.putExtra("messageId", messageId);

        } else if (type.equalsIgnoreCase("Gallery")) {// For district
            intent = new Intent(this, GalleryDescription.class);
            intent.putExtra("albumID", data.get("typeID"));
            intent.putExtra("fromNoti", "1");
            //Extra field added by Gaurav
            intent.putExtra("messageId", messageId);

        } else if (type.equalsIgnoreCase("PopupNoti")) {
            intent = new Intent(this, DashboardActivity.class);
            intent.putExtra("title", data.get("title"));
            intent.putExtra("message", message);
            intent.putExtra("fromNotification", "yes");
            intent.putExtra("BAType", data.get("BAType"));// 1-birthday , 2-Annivasary

            //Extra field added by Gaurav
            intent.putExtra("messageId", messageId);

        }

        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);


        Notification noti = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String CHANNEL_ID = "" + this.getApplicationContext().getResources().getString(R.string.app_name);//+System.currentTimeMillis();
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setSound(Utils.notiUri, attributes);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.app_icon_new)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.app_icon_new))
                    .setColor(Color.parseColor("#00AEEF"))
                    .setContentTitle(this.getApplicationContext().getResources().getString(R.string.app_name))
                    .setContentText("" + message)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("" + message))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pIntent)
                    .setVibrate(new long[]{1000, 0, 1000, 0, 1000})
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

            Random r = new Random();
            int notificationId = r.nextInt(99 - 1 + 1) + 1;

            if (PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID) != null) {
                notificationManagerCompat.notify(notificationId, mBuilder.build());
            }

        } else {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                noti = new Notification.Builder(this)
                        .setContentTitle(this.getApplicationContext().getResources().getString(R.string.app_name))
                        .setContentText("" + message)
                        .setSound(Utils.notiUri)
                        .setStyle(new Notification.BigTextStyle().bigText(message))
                        .setSmallIcon(R.drawable.app_icon_new)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                                R.drawable.app_icon_new))
                        .setColor(getResources().getColor(R.color.colorPrimary))
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pIntent)
                        .setVibrate(new long[]{1000, 0, 1000, 0, 1000})
                        .build();
            } else {

                noti = new Notification.Builder(this)
                        .setContentTitle(this.getApplicationContext().getResources().getString(R.string.app_name))
                        .setContentText("" + message)
                        .setSound(Utils.notiUri)
                        .setStyle(new Notification.BigTextStyle().bigText(message))
                        .setSmallIcon(R.drawable.app_icon_new)
                        .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.app_icon_new))
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pIntent)
                        .setVibrate(new long[]{1000, 0, 1000, 0, 1000})
                        .build();

            }

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

}
