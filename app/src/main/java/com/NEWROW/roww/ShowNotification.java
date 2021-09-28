package com.NEWROW.row;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NEWROW.row.NotificationDataBase.DatabaseHelper;
import com.NEWROW.row.sql.DBConnection;

import java.util.ArrayList;

public class ShowNotification extends AppCompatActivity {

    public static final String TAG = "ShowNotification";

    //Bundle
    public Bundle fetchData;

    //Notification Count

    public static String notificationCount;

    //RecyclerView

    private static RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager mLayoutManager;

    //List of CheckInOut

    static ArrayList<NotificationList> notificationLists;

    //String Value
    String title, body;

    //context
    private Context mcontext;

    //DataBase class object
    DatabaseHelper databaseHelpers;

    //Expired Date and Current Date,current Time

    public static String expiredDate, notification_Date, notification_Time, messageId;

    //Text view

    LinearLayout no_notificationlayout;

    //Added By Gaurav
    TextView tv_title;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notification);

        tv_title = (TextView) findViewById(R.id.tv_title);
    //    tv_title.setText("Notification");

        no_notificationlayout = (LinearLayout) findViewById(R.id.no_notificationlayout);
        mcontext = ShowNotification.this;
        //Create Database Helper Class Object
        databaseHelpers = new DatabaseHelper(this);

        notificationLists = new ArrayList<>();

        imageView = findViewById(R.id.iv_backbton)  ;                  //iv_backbton

        //this display only when you click on bell icon

       // deletrow();

       Long rowscnt =  databaseHelpers.getProfilesCount();

        Long limit = Long.valueOf(100);

       if(rowscnt > limit)
       {
           databaseHelpers.deletrow();
       }


        displayNotification();


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
//                startActivity(intent);
                finish();

            }
        });
    }

    private void displayNotification() {


        //Fetch notification from Database last 3 days record

        ArrayList<String> getrecords = databaseHelpers.getNotificationRecords();


        Log.i("tablevalue", String.valueOf(getrecords));


        if (getrecords.size() == 0) {

            //Toast.makeText(getApplicationContext(),"No Notification",Toast.LENGTH_SHORT).show();
            no_notificationlayout.setVisibility(View.VISIBLE);

        } else {

            int noOfData = getrecords.size() / 35;
            notificationCount = String.valueOf(noOfData);
            int count = 0;

            for (int i = 0; i < noOfData; i++) {

                String eventtitle = getrecords.get(count);
                String eventdescription = getrecords.get(++count);
                String notification_date = getrecords.get(++count);
                String notification_time = getrecords.get(++count);
                String count_notification = getrecords.get(++count);
                String flagfornotificationbackground = getrecords.get(++count);
                String messageId1 = getrecords.get(++count);
                String eventid = getrecords.get(++count);
                String groupid = getrecords.get(++count);
                String memid = getrecords.get(++count);
                String isadmin = getrecords.get(++count);
                String fromnotification = getrecords.get(++count);
                String eventimg = getrecords.get(++count);
                String venue = getrecords.get(++count);
                String reglink = getrecords.get(++count);
                String eventdate = getrecords.get(++count);
                String resvpenable = getrecords.get(++count);
                String goingcount = getrecords.get(++count);
                String maybecount = getrecords.get(++count);
                String notgoingcount = getrecords.get(++count);
                String totalcount = getrecords.get(++count);
                String myresponse = getrecords.get(++count);
                String questiontype = getrecords.get(++count);
                String questiontext = getrecords.get(++count);
                String questionid = getrecords.get(++count);
                String isquestionenable = getrecords.get(++count);
                String option1 = getrecords.get(++count);
                String option2 = getrecords.get(++count);
                String entityname = getrecords.get(++count);
                String groupcategory = getrecords.get(++count);
                String type = getrecords.get(++count);
                String moduleid = getrecords.get(++count);
                String modulename = getrecords.get(++count);
                String grouptype = getrecords.get(++count);
                String notificationexpireddate = getrecords.get(++count);


                notificationLists.add(new NotificationList(eventtitle, eventdescription, notification_date, notification_time, count_notification, flagfornotificationbackground, messageId1, eventid, groupid, memid, isadmin, fromnotification, eventimg, venue, reglink, eventdate, resvpenable, goingcount, maybecount, notgoingcount, totalcount, myresponse, questiontype, questiontext, questionid, isquestionenable, option1, option2, entityname, groupcategory, type, moduleid, modulename, grouptype, notificationexpireddate));


                ++count;

            }

            buildRecyclerView();


        }


    }

    public void buildRecyclerView() {

        //RecyclerView

        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        mAdapter = new ShowNotificationAdapter(notificationLists, mcontext);

        //set
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }
    public  void deletrow() {
        try {

            SQLiteDatabase db;
            db = DBConnection.getInstance(getApplicationContext());
            db.execSQL("delete from  NotificationDetails where _id IN (SELECT _id from NotificationDetails order by _id desc limit 2 )");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
