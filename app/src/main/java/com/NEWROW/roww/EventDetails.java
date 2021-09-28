package com.NEWROW.row;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.CalendarContract;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Data.CalendarData;
import com.NEWROW.row.NotificationDataBase.DatabaseHelper;
import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.MarshMallowPermission;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.ScreenshotUtils;
import com.NEWROW.row.Utils.Utils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.NEWROW.row.Utils.PreferenceManager.isRIadminModule;

/**
 * Created by USER on 17-12-2015.
 */
public class EventDetails extends Activity implements  View.OnClickListener{
    String moduleName = "", title = "";
    TextView tv_title;
    TextView tv_yes, tv_maybe, txt_reglink;
    TextView tv_no, tvAddCalender;
    TextView event_title;
    TextView event_desc;
    TextView event_venue;
    TextView event_datetime, event_date, event_time;
    TextView total_count;
    TextView tv_yes_count;
    TextView tv_no_count;
    TextView tv_maybe_count, tv_question;
    ImageView iv_backbutton;
    View yesNopanel, rsvpResponsePanel;
    EditText et_answer;
    TextView tv_question_attending, tv_members_with_you;
    LinearLayout tv_green, tv_red, tv_gray, ll_main, ll_eventDateTime, ll_eventTime, ll_eventLoc, ll_link;
    String flag_callwebsercie = "0";
    String getIsQuesEnable = "";
    String questionType = "";
    //  String Response ;
    String questionText;
    String grpId, eventId, joiningStatus, questionId, answerByme;
    String option1, option2;
    private String eventid, shortLink = "0";
    ImageView iv_eventimg, iv_actionbtn, iv_actionbtn2, iv_share;
    private ProgressBar progressbar;
    LinearLayout linear_image;
    String imageurl = "";
    private String grpID = "0";
    private String memberProfileID = "0";
    private String isAdmin = "No";
    CalendarData data;
    SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat oldFormat1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
    SimpleDateFormat newFormat = new SimpleDateFormat("dd MMM yyyy");
    SimpleDateFormat newFormat1 = new SimpleDateFormat("dd MMM yyyy KK:mm a");
    MarshMallowPermission marshMallowPermission;
    //LinearLayout ll_actionbtn,ll_actionbtn2;
    LinearLayout ll_root;
    ScrollView scroll;
    boolean isFromNotification = false;
    String groupCategory = "", eventDate = "", expiryDate = "";
    // variable to track event time
    private long mLastClickTime = 0;
    private String messageId_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.events_detail);

        ll_root = (LinearLayout) findViewById(R.id.root);
        scroll = (ScrollView) findViewById(R.id.scroll);
        yesNopanel = findViewById(R.id.YesNoPanel);
        rsvpResponsePanel = findViewById(R.id.rsvpResponsePanel);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        // iv_backbutton.setVisibility(View.GONE);
//        ll_actionbtn = (LinearLayout) findViewById(R.id.ll_actionbtn);
//        ll_actionbtn2 = (LinearLayout) findViewById(R.id.ll_actionbtn2);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_share.setVisibility(View.VISIBLE);
        //this is added by Gaurav
        iv_share.setOnClickListener(this);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn.setVisibility(View.VISIBLE); // EDIT ANNOUNCEMEBT
        //ll_actionbtn.setVisibility(View.VISIBLE);
        iv_actionbtn.setImageResource(R.drawable.edit); // EDIT ANNOUNCEMEBT
        iv_actionbtn2 = (ImageView) findViewById(R.id.iv_actionbtn2);
        iv_actionbtn.setVisibility(View.VISIBLE); // EDIT ANNOUNCEMEBT
        //ll_actionbtn.setVisibility(View.VISIBLE);
        iv_actionbtn.setImageResource(R.drawable.edit); //EDIT ANNOUNCEMEBT

        iv_actionbtn2.setVisibility(View.VISIBLE); // EDIT ANNOUNCEMEBT
        // ll_actionbtn2.setVisibility(View.VISIBLE);
        iv_actionbtn2.setImageResource(R.drawable.delete); // EDIT ANNOUNCEMEBT

        moduleName = PreferenceManager.getPreference(this, PreferenceManager.MODUEL_NAME, "Events");

        tv_title.setText("Events");

        tv_yes = (TextView) findViewById(R.id.tv_yes);
        tv_no = (TextView) findViewById(R.id.tv_no);
        event_title = (TextView) findViewById(R.id.event_title);
        event_desc = (TextView) findViewById(R.id.event_desc);
        event_venue = (TextView) findViewById(R.id.event_venue);
        event_datetime = (TextView) findViewById(R.id.event_datetime);
        total_count = (TextView) findViewById(R.id.total_count);
        tv_yes_count = (TextView) findViewById(R.id.tv_yes_count);
        tv_no_count = (TextView) findViewById(R.id.tv_no_count);
        tv_maybe_count = (TextView) findViewById(R.id.tv_maybe_count);
        tv_maybe = (TextView) findViewById(R.id.tv_maybe);
        event_date = (TextView) findViewById(R.id.event_date);
        event_time = (TextView) findViewById(R.id.event_time);
        txt_reglink = (TextView) findViewById(R.id.txt_reglink);
        tv_question_attending = (TextView) findViewById(R.id.tv_question_attending);
        tvAddCalender = (TextView) findViewById(R.id.tvAddCalender);
        tv_green = (LinearLayout) findViewById(R.id.tv_green);
        tv_red = (LinearLayout) findViewById(R.id.tv_red);
        tv_gray = (LinearLayout) findViewById(R.id.tv_gray);
        iv_eventimg = (ImageView) findViewById(R.id.iv_eventimg);
        linear_image = (LinearLayout) findViewById(R.id.linear_image);
        ll_main = (LinearLayout) findViewById(R.id.ll_main);
        ll_eventDateTime = (LinearLayout) findViewById(R.id.ll_evntDateTime);
        ll_eventTime = (LinearLayout) findViewById(R.id.ll_evntTime);
        ll_eventLoc = (LinearLayout) findViewById(R.id.ll_eventLoc);
        ll_link = (LinearLayout) findViewById(R.id.ll_link);

        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        grpID = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID);
        memberProfileID = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID);
        isAdmin = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN, "No");

        Log.d("Touchbase", "ID ID ID :- " + grpID + " - " + memberProfileID + " isAdmin=> " + isAdmin);

        Intent intenti = getIntent();

        if (intenti.hasExtra("memID")) {
            memberProfileID = intenti.getStringExtra("memID");

            grpID = intenti.getStringExtra("grpID");
            isAdmin = intenti.getStringExtra("isAdmin");
        }

        Log.d("Touchbase", "ID ID ID AFTER :- " + grpID + " - " + memberProfileID);

//      webservices();

        init();

        adminsettings();

        Bundle intent = getIntent().getExtras();

        if (intent != null) {

            if (intent.containsKey("data")) {

                data = (CalendarData) intent.getSerializable("data");
                ll_eventTime.setVisibility(View.GONE);
                ll_eventDateTime.setVisibility(View.VISIBLE);

                setEventData(data);

            } else if (intent.containsKey("shortLink")) {

                shortLink = intent.getString("shortLink");
                eventid = intent.getString("eventid");

                iv_actionbtn.setVisibility(View.GONE);
                iv_actionbtn2.setVisibility(View.GONE);

                getEventForShare(eventid);

            } else {

                eventid = intent.getString("eventid");

                if (intent.containsKey("grpID")) {
                    grpID = intent.getString("grpID");
                }

                if (InternetConnection.checkConnection(getApplicationContext())) {

                    // Avaliable
                    ll_eventTime.setVisibility(View.VISIBLE);
                    ll_eventDateTime.setVisibility(View.GONE);





                    if (intent.containsKey("fromNotification")) {


                       // String noti = String.valueOf(getIntent().hasExtra("notificationkey"));

                        String noti=  intent.getString("notificationkey");

                        //   w.putExtra("notificationkey","yes");


                        if (noti != null) {
                            webservices();
                        } else {


//
                            isFromNotification = true;

                            event_title.setText(intent.getString("eventTitle"));
                            event_desc.setText(intent.getString("eventDesc"));
                            total_count.setText(intent.getString("totalCount"));
                            tv_yes_count.setText(intent.getString("goingCount"));
                            tv_maybe_count.setText(intent.getString("maybeCount"));
                            tv_no_count.setText(intent.getString("notgoingCount"));
                            event_venue.setText(intent.getString("venue"));

                            getIsQuesEnable = intent.getString("isQuesEnable");

                            String eventDateTime = intent.getString("eventDate");

                            title = intent.getString("entity name");

                            groupCategory = intent.getString("group_category");

                            eventDate = eventDateTime;


                            try {
                                Date eventDtTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(eventDateTime);
                                event_datetime.setText(new SimpleDateFormat("dd MMM yyyy hh:mm a").format(eventDtTime));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            String link = intent.getString("reglink");

                            if (link != null && !link.isEmpty()) {
                                ll_link.setVisibility(View.VISIBLE);
                                txt_reglink.setText(link);
                            } else {
                                txt_reglink.setText("");
                                ll_link.setVisibility(View.GONE);
                            }

                            String rsvpEnabled = intent.getString("rsvpEnable");

                            Log.e("rsvpEnabled", "Value of rsvpEnabled : " + rsvpEnabled + " isQuesEnable : " + getIsQuesEnable);

                            if (rsvpEnabled.equalsIgnoreCase("1")) {

                                yesNopanel.setVisibility(View.VISIBLE);
                                rsvpResponsePanel.setVisibility(View.VISIBLE);

                                questionType = intent.getString("questionType");
                                questionText = intent.getString("questionText");
                                questionId = intent.getString("questionID");

                                option1 = intent.getString("option1");
                                option2 = intent.getString("option2");

                                //clearselectedtext(intent.getString("myResponse"));

                        /*JSONArray questionArray = objects.getJSONArray("questionArray");

                        for (int j = 0; j < questionArray.length(); j++) {

                            JSONObject qobject = questionArray.getJSONObject(j);
                            JSONObject questionList = qobject.getJSONObject("QuestionList");

                            questionType = intent.getString("questionType");
                            questionText = intent.getString("questionText");
                            questionId = intent.getString("questionID");

                            option1 = intent.getString("option1").toString();
                            option2 = intent.getString("option2").toString();

                            clearselectedtext(intent.getString("myResponse"));

                        }*/

                            } else {
                                yesNopanel.setVisibility(View.GONE);
                                rsvpResponsePanel.setVisibility(View.GONE);
                                tv_gray.setVisibility(View.GONE);
                                tv_green.setVisibility(View.GONE);
                                tv_red.setVisibility(View.GONE);
                            }

                  /*  String thisGrpId = PreferenceManager.getPreference(EventDetails.this,PreferenceManager.GROUP_ID,"");

                    Log.e("sa", "Value of this GrpId=> "+thisGrpId+" grpId=> "+grpId);

                    if(!thisGrpId.equalsIgnoreCase(grpId)) {
                        yesNopanel.setVisibility(View.GONE);
                        rsvpResponsePanel.setVisibility(View.GONE);
                        tv_gray.setVisibility(View.GONE);
                        tv_green.setVisibility(View.GONE);
                        tv_red.setVisibility(View.GONE);
                        iv_actionbtn.setVisibility(View.GONE);
                        iv_actionbtn2.setVisibility(View.GONE);
                    }*/

                            if (intent.getString("eventImg").length() == 0 || intent.getString("eventImg") == null || intent.getString("eventImg").equalsIgnoreCase("''")) {

                                linear_image.setVisibility(View.GONE);

                            } else {

                                linear_image.setVisibility(View.VISIBLE);
                                imageurl = intent.getString("eventImg");
                                progressbar.setVisibility(View.VISIBLE);

                                Log.e("event details", "satish event img url=>" + intent.getString("eventImg"));

                                Picasso.with(EventDetails.this).load(intent.getString("eventImg").replaceAll("\'", ""))
                                        .placeholder(R.drawable.edit_image)
                                        .into(iv_eventimg, new Callback() {

                                            @Override
                                            public void onSuccess() {
                                                progressbar.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onError() {
                                                progressbar.setVisibility(View.GONE);
                                            }

                                        });
                            }

                            //Update Records notification count

                            //update Data into Database
                            messageId_temp = intenti.getStringExtra("messageId");
                            if (messageId_temp != null) {
                                //Create Database Helper Class Object
                                DatabaseHelper databaseHelpers = new DatabaseHelper(this);

                                boolean notificationInsert = databaseHelpers.updateData(messageId_temp);
                                Log.d("messageId_temp", "messageID ID ID AFTER :- " + messageId_temp);
                                Log.d("messageId_temp", "Is Data Updated :- " + notificationInsert);


                            }


                        }
                    }else {
                        webservices();
                    }

                } else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }

            }
        }

        //  Log.d("@@@@@@@@", "My Response --- " + Response);

    }

    private void adminsettings() {

        Log.e("isAdmin", "Value of isAdmin is : " + isAdmin);

        if (isAdmin.equals("No")) {
            iv_actionbtn.setVisibility(View.GONE);
            iv_actionbtn2.setVisibility(View.GONE);
//            ll_actionbtn.setVisibility(View.GONE);
//            ll_actionbtn2.setVisibility(View.GONE);
            Log.e("In no : ", "We are in is admin no");
        } else if (isAdmin.equals("Yes")) {
            iv_actionbtn.setVisibility(View.VISIBLE);
            iv_actionbtn2.setVisibility(View.VISIBLE);
//            ll_actionbtn.setVisibility(View.VISIBLE);
//            ll_actionbtn2.setVisibility(View.VISIBLE);
            Log.e("Inside yes : ", "We are in is admin yes");
        } else if (PreferenceManager.getPreference(getApplicationContext(), isRIadminModule).equals("Yes")) {
            iv_actionbtn.setVisibility(View.GONE);
            iv_actionbtn2.setVisibility(View.GONE);


        }
    }

    private void clearselectedtext(String myResponse) {

        if (myResponse.equals("")) {
            //tv_question_attending.setText("");
            tv_question_attending.setVisibility(View.VISIBLE);
            tv_green.setVisibility(View.GONE);
            tv_red.setVisibility(View.GONE);
            tv_gray.setVisibility(View.GONE);
        } else if (myResponse.equals("yes")) {
            tv_question_attending.setVisibility(View.GONE);
            tv_green.setVisibility(View.VISIBLE);
            tv_red.setVisibility(View.GONE);
            tv_gray.setVisibility(View.GONE);
        } else if (myResponse.equals("no")) {
            tv_question_attending.setVisibility(View.GONE);
            tv_red.setVisibility(View.VISIBLE);
            tv_green.setVisibility(View.GONE);
            tv_gray.setVisibility(View.GONE);
        } else if (myResponse.equals("maybe")) {
            tv_question_attending.setVisibility(View.GONE);
            tv_gray.setVisibility(View.VISIBLE);
            tv_green.setVisibility(View.GONE);
            tv_red.setVisibility(View.GONE);
        }
    }

    private void init() {

//        ll_actionbtn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Dialog dialog = new Dialog(EventDetails.this, android.R.style.Theme_Translucent);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.popup_confrm_delete);
//                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
//                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
//                tv_no.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//
//                tv_yes.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//
//                        if (InternetConnection.checkConnection(getApplicationContext())) {
//                            //Utils.showToastWithTitleAndContext(getApplicationContext(), "Delete");
//                            deletewebservices();
//                        } else {
//                            Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
//
//                        }
//                    }
//                });
//
//                dialog.show();
//            }
//        });


        iv_actionbtn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(EventDetails.this, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_confrm_delete);
                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);

                tv_no.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });

                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (InternetConnection.checkConnection(getApplicationContext())) {
                            //Utils.showToastWithTitleAndContext(getApplicationContext(), "Delete");
                            deletewebservices();
                            //Base on event Id delete Data from NotificationList
                            //Added By Gaurav
                            //Create Database Helper Class Object
                            DatabaseHelper databaseHelpers = new DatabaseHelper(EventDetails.this);
                            //Delete Data into Database
                            Integer notificationDelete = databaseHelpers.deleteDataBaseOnEventId(eventId);

                            if (notificationDelete == 1) {

                                Log.d("PARAMETERS", "Notification Data Deleted from List successfully");


                            }
                        } else {
                            Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");

                        }
                    }
                });

                dialog.show();
            }

        });

//
//        ll_actionbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if ( InternetConnection.checkConnection(getApplicationContext())) {
//                    Intent i = new Intent(getApplicationContext(), AddEvent.class);
//                    i.putExtra("event_id", eventid);
//                    i.putExtra("edit", "yes");
//                    startActivityForResult(i, 1);
//                } else {
//                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        iv_actionbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (InternetConnection.checkConnection(getApplicationContext())) {
                    Intent i = new Intent(getApplicationContext(), AddEvent.class);
                    i.putExtra("event_id", eventid);
                    i.putExtra("edit", "yes");
                    startActivityForResult(i, 1);
                } else {
                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

        ll_link.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String link = txt_reglink.getText().toString();

                if (link != null && !link.trim().isEmpty()) {
                    Intent intent = new Intent(EventDetails.this, OpenLinkActivity.class);
                    intent.putExtra("link", link);
                    startActivity(intent);
                }
            }
        });

        tv_yes.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View arg0) {

                joiningStatus = "yes";

                if (getIsQuesEnable.equals("2") || getIsQuesEnable.equals("1")) {

                    final Dialog dialog = new Dialog(EventDetails.this, android.R.style.Theme_Translucent);

                    Log.d("@@@@@@@@", "questionType --- " + questionType);

                    if (questionType.equals("1")) {

                        // final Dialog dialog = new Dialog(EventDetails.this, android.R.style.Theme_Translucent);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.event_alert);

                        tv_members_with_you = (TextView) dialog.findViewById(R.id.tv_members_with_you);
                        et_answer = (EditText) dialog.findViewById(R.id.et_member);

                        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);

                        iv_close.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        tv_members_with_you.setText(questionText);

                        final TextView tv_submit = (TextView) dialog.findViewById(R.id.tv_submit);

                        tv_submit.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                answerByme = et_answer.getText().toString();
                                AnsweringEventwebservices();
                                Utils.hideKeyBoard(EventDetails.this, tv_submit);
                                dialog.dismiss();

                            }
                        });

                        dialog.show();

                    } else if (questionType.equals("2")) {

                        // final Dialog dialog = new Dialog(EventDetails.this, android.R.style.Theme_Translucent);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                        dialog.setContentView(R.layout.event_alert_objective_que);

                        TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                        TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);

                        tv_question = (TextView) dialog.findViewById(R.id.tv_question);
                        tv_question.setText(questionText);

                        tv_question.setText(questionText);
                        tv_yes.setText(option1);
                        tv_no.setText(option2);

                        tv_question.setMovementMethod(new ScrollingMovementMethod());
                        tv_yes.setMovementMethod(new ScrollingMovementMethod());
                        tv_no.setMovementMethod(new ScrollingMovementMethod());

                        iv_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        tv_yes.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                // dialog.dismiss();
                                answerByme = option1;
                                AnsweringEventwebservices();
                                dialog.dismiss();

                            }
                        });

                        tv_no.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                answerByme = option2;
                                AnsweringEventwebservices();
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                    }

                } else if (getIsQuesEnable.equals("0")) {
                    AnsweringEventwebservices();
                }

            }
        });

        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                joiningStatus = "no";
                answerByme = "";

                AnsweringEventwebservices();
            }
        });

        tv_maybe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                joiningStatus = "maybe";
                answerByme = "";
                AnsweringEventwebservices();
            }
        });

        iv_eventimg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventDetails.this, ImageZoom.class);
                i.putExtra("imgageurl", imageurl);
                startActivity(i);
            }
        });

        ll_eventLoc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!event_venue.getText().toString().isEmpty()) {

                    marshMallowPermission = new MarshMallowPermission(EventDetails.this);

                    /*Intent i = new Intent(mContext, Map.class);
                    Bundle bundle = new Bundle();
                    //Add your data from getFactualResults method to bundle
                    bundle.putString("Long", item.getVenueLon());
                    bundle.putString("Lat", item.getVenueLat());
                    i.putExtras(bundle);*/
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + event_venue.getText().toString()));

                    if (!marshMallowPermission.checkPermissionForLocation()) {
                        marshMallowPermission.requestPermissionForLocation();
                    } else {
                        // mContext.startActivity(i);
                        startActivity(intent);
                    }

               /* if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    mContext.startActivity(i);
                }*/

                }

            }
        });


      /*  iv_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                createDynamicUrl();

                Uri shareUri = takeScreenshot();

                if (shareUri != null) {

                    Intent shareIntent = new Intent();

                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, shareUri);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    shareIntent.setType("application/pdf");

                    startActivity(shareIntent);
                    finishActivity(view);

                } else {
                    Utils.showToastWithTitleAndContext(EventDetails.this, getString(R.string.msgRetry));
                }
            }
        });

*/

        tvAddCalender.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addEventToCalender();
            }
        });

    }

    private void addEventToCalender() {

        Intent intent = new Intent(Intent.ACTION_INSERT);

        intent.setType("vnd.android.cursor.item/event");

        try {

            /*Calendar startTimeCal = Calendar.getInstance();
            String stYear = new SimpleDateFormat("yyyy").format(startDate);
            String stMonth = new SimpleDateFormat("MM").format(startDate);
            String stDay = new SimpleDateFormat("dd").format(startDate);
            String stHr = new SimpleDateFormat("HH").format(startDate);
            String stMin = new SimpleDateFormat("mm").format(startDate);

            startTimeCal.set(Integer.parseInt(stYear),Integer.parseInt(stMonth),Integer.parseInt(stDay),Integer.parseInt(stHr),Integer.parseInt(stMin));


            Calendar endTimeCal = Calendar.getInstance();
            String endYear = new SimpleDateFormat("yyyy").format(endDate);
            String endMonth = new SimpleDateFormat("MM").format(endDate);
            String endDay = new SimpleDateFormat("dd").format(endDate);
            String endHr = new SimpleDateFormat("HH").format(endDate);
            String endMin = new SimpleDateFormat("mm").format(endDate);

            endTimeCal.set(Integer.parseInt(endYear),Integer.parseInt(endMonth),Integer.parseInt(endDay),Integer.parseInt(endHr),Integer.parseInt(endMin));
*/

           /* long startTime = startTimeCal.getTimeInMillis();
            long endTime =  endTimeCal.getTimeInMillis();*/

            //Log.d("row","satish start Date="+stYear+"/"+stMonth+"/"+stDay+" "+stHr+":"+stMin);


            if (!eventDate.equalsIgnoreCase("")) {
                Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(eventDate);
                long startTime = startDate.getTime();
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
            }


            if (!expiryDate.equalsIgnoreCase("")) {
                Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(expiryDate);
                long endTime = endDate.getTime();
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime);
            }

            //intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
            intent.putExtra(CalendarContract.Events.TITLE, event_title.getText().toString());
            intent.putExtra(CalendarContract.Events.DESCRIPTION, event_desc.getText().toString());
            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, event_venue.getText().toString());

            //intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Utils.showToastWithTitleAndContext(EventDetails.this, "No calender app found");
            }

        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }

    public void finishActivity(View v) {
        finish();
    }

    private void deletewebservices() {

        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("typeID", eventid));
        arrayList.add(new BasicNameValuePair("type", "Event"));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        arrayList.add(new BasicNameValuePair("profileID", memberProfileID));

        flag_callwebsercie = "2";

        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "PARAMETERS " + Constant.DeleteByModuleName + " :- " + arrayList.toString());
        new WebConnectionEventDetail(Constant.DeleteByModuleName, arrayList, EventDetails.this).execute();
    }

    private void webservices() {

        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        Intent intenti = getIntent();

        if(memberProfileID == null)
        {
            memberProfileID = intenti.getStringExtra("memIDnew");
            if(memberProfileID == null)
            {

            }
        }

        else  if(memberProfileID.equalsIgnoreCase("True") || memberProfileID.equalsIgnoreCase("false")|| memberProfileID.equalsIgnoreCase("")|| memberProfileID.isEmpty())
        {
            memberProfileID = intenti.getStringExtra("memIDnew");
        }

        arrayList.add(new BasicNameValuePair("grpId", grpID));//PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)
        arrayList.add(new BasicNameValuePair("eventID", eventid));//eventid
        arrayList.add(new BasicNameValuePair("groupProfileID", memberProfileID));//PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)

        flag_callwebsercie = "0";

        Log.d("Response", "PARAMETERS " + Constant.GetEventDetails + " :- " + arrayList.toString());

        if (InternetConnection.checkConnection(getApplicationContext())) {
            new WebConnectionEventDetail(Constant.GetEventDetails, arrayList, EventDetails.this).execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void AnsweringEventwebservices() {

        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("grpId", grpID));
        arrayList.add(new BasicNameValuePair("eventId", eventid));
        // arrayList.add(new BasicNameValuePair("profileID", memberProfileID));
        //this is addded by Gaurav for the data cames from RI admin module
        if (PreferenceManager.getPreference(getApplicationContext(), isRIadminModule).equals("Yes")) {

            Long masterUid = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID));

            String profile_id = String.valueOf(masterUid);

            arrayList.add(new BasicNameValuePair("profileID", profile_id));

        } else {
            arrayList.add(new BasicNameValuePair("profileID", memberProfileID));
        }
        arrayList.add(new BasicNameValuePair("joiningStatus", joiningStatus));
        arrayList.add(new BasicNameValuePair("questionId", questionId));
        arrayList.add(new BasicNameValuePair("answerByme", answerByme));

        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        flag_callwebsercie = "1";
        Log.d("Response", "PARAMETERS " + Constant.AnsweringEvent + " :- " + arrayList.toString());
        if (InternetConnection.checkConnection(getApplicationContext())) {
            new WebConnectionEventDetail(Constant.AnsweringEvent, arrayList, EventDetails.this).execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    //this is added By Gaurav For Multipule click not occured during the click on share button
    @Override
    public void onClick(View v) {


        // Preventing multiple clicks, using threshold of 1 second
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        Log.d("Clicked", "LastClickedTime "+mLastClickTime);


        switch (v.getId()) {

            case R.id.iv_share:
                // do your code
                Uri shareUri = takeScreenshot();

                if (shareUri != null) {

                    Intent shareIntent = new Intent();

                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, shareUri);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    shareIntent.setType("application/pdf");

                    startActivity(shareIntent);

                } else {
                    Utils.showToastWithTitleAndContext(EventDetails.this, getString(R.string.msgRetry));
                }

                break;
        }

    }

    public class WebConnectionEventDetail extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(EventDetails.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;

        public WebConnectionEventDetail(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //	dialog.show();

            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(String... params) {

            try {

                val = HttpConnection.postData(url, argList);
                Log.d("Response", "we" + val);
            } catch (Exception e) {
                e.printStackTrace();

            }

            return val;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            //	Log.d("response","Do post"+ result.toString());

            if (result != "") {

                if (flag_callwebsercie.equals("0")) {
                    getEventDetailsItems(result.toString());
                } else if (flag_callwebsercie.equals("1")) {
                    getAnsweringItems(result.toString());
                } else if (flag_callwebsercie.equals("2")) {
                    getdata(result.toString());
                }

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getEventDetailsItems(String result) {

        try {

            JSONObject jsonObj = new JSONObject(result);

            JSONObject EventResult = jsonObj.getJSONObject("EventsListDetailResult");

            Log.d("sa", "satish event details=>" + result);

            final String status = EventResult.getString("status");

            if (status.equals("0")) {

                JSONArray EventListResdult = EventResult.getJSONArray("EventsDetailResult");

                for (int i = 0; i < EventListResdult.length(); i++) {

                    JSONObject object = EventListResdult.getJSONObject(i);

                    JSONObject objects = object.getJSONObject("EventsDetail");

                    title = objects.getString("grpname");

                    event_title.setText(objects.getString("eventTitle"));
                    event_desc.setText(objects.getString("eventDesc"));
                    event_datetime.setText(objects.getString("eventDateTime"));
                    total_count.setText(objects.getString("totalCount"));
                    tv_yes_count.setText(objects.getString("goingCount"));
                    tv_maybe_count.setText(objects.getString("maybeCount"));
                    tv_no_count.setText(objects.getString("notgoingCount"));
                    event_venue.setText(objects.getString("venue"));
                    getIsQuesEnable = objects.getString("isQuesEnable");
                    grpId = objects.getString("grpID");
                    eventId = objects.getString("eventID");
                    String link = objects.getString("reglink");

                    eventDate = objects.getString("eventDate");
                    expiryDate = objects.getString("expiryDate");

                    if (link != null && !link.isEmpty()) {
                        ll_link.setVisibility(View.VISIBLE);
                        txt_reglink.setText(link);
                    } else {
                        txt_reglink.setText("");
                        ll_link.setVisibility(View.GONE);
                    }

                    String rsvpEnabled = objects.getString("rsvpEnable").toString();

                    Log.e("rsvpEnabled", "Value of rsvpEnabled : " + rsvpEnabled);

                    if (rsvpEnabled.equals("1")) {

                        yesNopanel.setVisibility(View.VISIBLE);
                        rsvpResponsePanel.setVisibility(View.VISIBLE);

                        String Response = objects.getString("myResponse").toString();
                        clearselectedtext(objects.getString("myResponse").toString());

                   /* JSONArray questionArray = objects.getJSONArray("questionArray");
                    for (int j = 0; j < questionArray.length(); j++) {
                        JSONObject qobject = questionArray.getJSONObject(j);
                        JSONObject questionList = qobject.getJSONObject("QuestionList");*/

                        questionType = objects.getString("questionType").toString();
                        questionText = objects.getString("questionText").toString();
                        questionId = objects.getString("questionId").toString();
                        option1 = objects.getString("option1").toString();
                        option2 = objects.getString("option2").toString();

                        clearselectedtext(objects.getString("myResponse").toString());

                    } else {
                        yesNopanel.setVisibility(View.GONE);
                        rsvpResponsePanel.setVisibility(View.GONE);
                        tv_gray.setVisibility(View.GONE);
                        tv_green.setVisibility(View.GONE);
                        tv_red.setVisibility(View.GONE);
                    }

                    if (objects.getString("filterType").toString().equals("3")) {

                        iv_actionbtn.setVisibility(View.GONE);
                        // ll_actionbtn.setVisibility(View.GONE);
                        yesNopanel.setVisibility(View.GONE);
                        rsvpResponsePanel.setVisibility(View.GONE);
                        tv_gray.setVisibility(View.GONE);
                        tv_green.setVisibility(View.GONE);
                        tv_red.setVisibility(View.GONE);
                    }

                    String thisGrpId = PreferenceManager.getPreference(EventDetails.this, PreferenceManager.GROUP_ID, "");

                    if (!thisGrpId.equalsIgnoreCase(grpId)) {
                        yesNopanel.setVisibility(View.GONE);
                        rsvpResponsePanel.setVisibility(View.GONE);
                        tv_gray.setVisibility(View.GONE);
                        tv_green.setVisibility(View.GONE);
                        tv_red.setVisibility(View.GONE);
                        iv_actionbtn.setVisibility(View.GONE);
                        iv_actionbtn2.setVisibility(View.GONE);
                    }

                    if (objects.has("eventImg")) {

                        if (objects.getString("eventImg").length() == 0 || objects.getString("eventImg") == null) {

                            linear_image.setVisibility(View.GONE);

                        } else {

                            linear_image.setVisibility(View.VISIBLE);
                            imageurl = objects.getString("eventImg").toString();
                            progressbar.setVisibility(View.VISIBLE);

                            Picasso.with(EventDetails.this).load(objects.getString("eventImg").toString())
                                    .placeholder(R.drawable.edit_image)
                                    .into(iv_eventimg, new Callback() {

                                        @Override
                                        public void onSuccess() {
                                            progressbar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError() {
                                            progressbar.setVisibility(View.GONE);
                                        }
                                    });
                        }
                    }

                    //Picasso.with(this).load("").placeholder()

                    // Log.d("@@@@@@@@@", "---------" + questionType);

                    /* }*/

                }

            } else {
                Utils.showToastWithTitleAndContext(getApplicationContext(), "Service Failed");
                finish();
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    private void getAnsweringItems(String result) {

        try {

            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("EventJoinResult");
            final String status = EventResult.getString("status");

            if (status.equals("0")) {

                String msg = EventResult.getString("message");
                String gngCnt = EventResult.getString("goingCount");
                int yes = 0;
                int no = 0;
                int maybe = 0;
                int total = 0;

                tv_yes_count.setText(EventResult.getString("goingCount"));
                tv_maybe_count.setText(EventResult.getString("maybeCount"));
                tv_no_count.setText(EventResult.getString("notgoingCount"));

                yes = Integer.parseInt(EventResult.getString("goingCount"));
                no = Integer.parseInt(EventResult.getString("notgoingCount"));
                maybe = Integer.parseInt(EventResult.getString("maybeCount"));
                total = yes + no + maybe;

                total_count.setText("" + String.valueOf(total));

                //Integer.parseInt(EventResult.getString("goingCount").toString())
                clearselectedtext(EventResult.getString("myResponse"));
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 1) {
            webservices();
        }
    }

    private void getdata(String result) {

        try {

            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("DeleteResult");

            final String status = EventResult.getString("status");

            if (status.equals("0")) {
                //Intent i = new Intent(Announcement_details.this,Announcement.class);
                //startActivityForResult(i,1);
                Intent intent = new Intent();
                setResult(1, intent);
                finish();//finishing activity
                finish();
                Utils.showToastWithTitleAndContext(getApplicationContext(), "Event Deleted Successfully");
            } else {
                Utils.showToastWithTitleAndContext(getApplicationContext(), "Could not DELETE, please Try Again!");
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    private void setEventData(CalendarData data) {

        imageurl = data.getEventImg();

        if (imageurl != null && !imageurl.isEmpty()) {

            linear_image.setVisibility(View.VISIBLE);
            progressbar.setVisibility(View.VISIBLE);

            Picasso.with(EventDetails.this).load(imageurl)
                    .placeholder(R.drawable.imageplaceholder)
                    .into(iv_eventimg, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressbar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            progressbar.setVisibility(View.GONE);
                        }
                    });
        } else {
            linear_image.setVisibility(View.GONE);
        }

        event_title.setText(data.getTitle());
        event_desc.setText(data.getDescription());
        event_time.setText(data.getEventTime());

        Date date = null;

        try {
            date = oldFormat.parse(data.getEventDate());
            event_date.setText(newFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void createDynamicUrl() {

        final ProgressDialog progressDialog = new ProgressDialog(EventDetails.this);
        progressDialog.setMessage("Creating share link....");
        progressDialog.show();

        Uri.Builder builder = new Uri.Builder();

        builder.scheme("https")// "http"
                .authority("www.row.com")//("www.rosteronwheels.com")//"365salads.xyz"
                .appendPath("event") // "salads"
                .appendQueryParameter("eventID", eventid);

        Uri linkUri = builder.build();

        Utils.log("share link=> " + linkUri.toString());

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(linkUri)
                .setDomainUriPrefix("https://rosteronwheel.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().setFallbackUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.SampleApp.row")).build())
                .setSocialMetaTagParameters(new DynamicLink.SocialMetaTagParameters.Builder().setTitle(event_title.getText().toString()).setDescription(event_desc.getText().toString()).setImageUrl(Uri.parse(imageurl)).build())
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {

                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {

                        if (task.isSuccessful()) {

                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();

                            Log.d("demo", shortLink.toString());

                            Uri flowchartLink = task.getResult().getPreviewLink();

                            URL url = null;

                            try {
                                url = new URL(URLDecoder.decode(shortLink.toString(), "UTF-8"));
                            } catch (MalformedURLException e) {
                                progressDialog.dismiss();
                                Utils.showToastWithTitleAndContext(EventDetails.this, getString(R.string.msgRetry));
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                progressDialog.dismiss();
                                Utils.showToastWithTitleAndContext(EventDetails.this, getString(R.string.msgRetry));
                                e.printStackTrace();
                            }

                            Uri shareUri = takeScreenshot();

                            if (shareUri != null) {

                                Intent shareIntent = new Intent();

                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "(ROW) Click on link to know more");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, event_title.getText().toString() + "\n" + url.toString());
                                shareIntent.putExtra(Intent.EXTRA_STREAM, shareUri);
                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                //shareIntent.setType("image/jpeg");
                                //shareIntent.setType("*/*");

                                shareIntent.setType("application/pdf");

                                progressDialog.dismiss();

                                startActivity(shareIntent);

                            } else {
                                progressDialog.dismiss();
                                Utils.showToastWithTitleAndContext(EventDetails.this, getString(R.string.msgRetry));
                            }

                        } else {
                            progressDialog.dismiss();
                            Utils.showToastWithTitleAndContext(EventDetails.this, getString(R.string.msgRetry));
                        }
                    }
                });

    }

    /*private Uri takeScreenshot() {

          Bitmap screenBitmap = ScreenshotUtils.getScreenShot(ll_root);

//        Bitmap waterMarkPic = ScreenshotUtils.addWatermark(screenBitmap,EventDetails.this,"Thane Club");

        Bitmap waterMarkPic = ScreenshotUtils.addWaterMark(screenBitmap,EventDetails.this,PreferenceManager.getPreference(EventDetails.this,PreferenceManager.MY_CATEGORY),title);

        File imageFile = ScreenshotUtils.store(waterMarkPic,"shared_image.png",ScreenshotUtils.getMainDirectoryName(EventDetails.this));

        return FileProvider.getUriForFile(this, "com.SampleApp.row.fileprovider", imageFile);
    }*/

    private Uri takeScreenshot() {

        Bitmap screenBitmap = ScreenshotUtils.getScreenShot(ll_root);


        Bitmap waterMarkPic = null;//ScreenshotUtils.addWaterMark(screenBitmap,EventDetails.this,PreferenceManager.getPreference(EventDetails.this,PreferenceManager.MY_CATEGORY),title);

        if (PreferenceManager.getPreference(getApplicationContext(), isRIadminModule).equals("Yes")) {
            waterMarkPic = ScreenshotUtils.addWaterMark(screenBitmap, EventDetails.this, "0", " ");

        } else {

            if (isFromNotification) {
                waterMarkPic = ScreenshotUtils.addWaterMark(screenBitmap, EventDetails.this, groupCategory, title);
            } else {
                waterMarkPic = ScreenshotUtils.addWaterMark(screenBitmap, EventDetails.this, PreferenceManager.getPreference(EventDetails.this, PreferenceManager.MY_CATEGORY), title);
            }
        }

        File imageFile = createPdf(waterMarkPic);//ScreenshotUtils.store(waterMarkPic,"shared_image.png",ScreenshotUtils.getMainDirectoryName(EventDetails.this));

        // Log.d("sa","file path=>"+imageFile.getPath());
        //return Uri.parse(imageFile.getPath());

        return FileProvider.getUriForFile(this, "com.SampleApp.row.fileprovider", imageFile);
    }

    private File createPdf(Bitmap b) {

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(b.getWidth(), b.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();

        paint.setColor(Color.parseColor("#ffffff"));

        canvas.drawPaint(paint);

        Bitmap bitmap = Bitmap.createScaledBitmap(b, b.getWidth(), b.getHeight(), true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        //ScreenshotUtils.getMainDirectoryName(EventDetails.this)

        File saveFilePath = ScreenshotUtils.getMainDirectoryName(EventDetails.this);

        File dir = new File(saveFilePath.getAbsolutePath());

        if (!dir.exists()) {
            dir.mkdirs();
        }

        String today = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());

        //String fileName = "ROW_Event_"+today+".pdf";

        //---***Add By Nivedita***---//
        String fileName = event_title.getText().toString() + "_" + today + ".pdf";

        File file = new File(saveFilePath.getAbsolutePath(), fileName);

        try {
            document.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

       /* String extr = Environment.getExternalStorageDirectory() + "/CreatePDF/";

        File file = new File(extr);

        if (!file.exists()) {
            file.mkdir();
        }

        String fileName = "ROW"+System.currentTimeMillis()+".pdf";

       File filePath = new File(extr, fileName);

       try {
            document.writeTo(new FileOutputStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }*/
        // close the document

        document.close();

        return file;
    }

    private void getEventForShare(String shareID) {

        try {

            JSONObject requestData = new JSONObject();
            requestData.put("TransactionType", "Event");
            requestData.put("TransactionID", shareID);

            Utils.log("sat" + requestData);

            final ProgressDialog progressDialog = new ProgressDialog(EventDetails.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.GetSharedTransactionData, requestData, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                    setEventShareData(response);
                    Utils.log(response.toString());

                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Utils.showToastWithTitleAndContext(EventDetails.this, getString(R.string.msgRetry));
                    Utils.log("VollyError:- " + error);
                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(120000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(EventDetails.this, request);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setEventShareData(JSONObject response) {

        try {

            JSONObject SharedTransactionDetailResult = response.getJSONObject("SharedTransactionDetailResult");

            String status = SharedTransactionDetailResult.getString("status");

            if (status.equalsIgnoreCase("0")) {

                JSONObject object = SharedTransactionDetailResult.getJSONObject("obj");

                title = object.getString("grpname");

                eventid = object.getString("TransactionID");

                imageurl = object.getString("TransactionImg");

                if (imageurl != null && !imageurl.isEmpty()) {

                    linear_image.setVisibility(View.VISIBLE);
                    progressbar.setVisibility(View.VISIBLE);

                    Picasso.with(EventDetails.this).load(imageurl)
                            .placeholder(R.drawable.imageplaceholder)
                            .into(iv_eventimg, new Callback() {

                                @Override
                                public void onSuccess() {
                                    progressbar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {
                                    progressbar.setVisibility(View.GONE);
                                }
                            });

                } else {
                    linear_image.setVisibility(View.GONE);
                }

                event_title.setText(object.getString("TransactionTitle"));
                event_desc.setText(object.getString("TransationDesc"));
                event_venue.setText(object.getString("TransactionVenue"));
                //event_datetime.setText(object.getString("TransactionEventDate"));

                String link = object.getString("TransactionLink");

                if (link != null && !link.isEmpty()) {
                    ll_link.setVisibility(View.VISIBLE);
                    txt_reglink.setText(link);
                } else {
                    txt_reglink.setText("");
                    ll_link.setVisibility(View.GONE);
                }

                Date date = null;

                try {

                    date = oldFormat1.parse(object.getString("TransactionEventDate"));
                    event_datetime.setText(newFormat1.format(date));

                } catch (ParseException e) {
                    event_datetime.setText(object.getString("TransactionEventDate"));
                    e.printStackTrace();
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Utils.showToastWithTitleAndContext(EventDetails.this, getString(R.string.msgRetry));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle bundle = intent.getExtras();

        if (bundle.containsKey("shortLink")) {
            shortLink = bundle.getString("shortLink");
            eventid = bundle.getString("eventid");
            iv_actionbtn.setVisibility(View.GONE);
            iv_actionbtn2.setVisibility(View.GONE);
            getEventForShare(eventid);
        }
    }

}
