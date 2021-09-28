package com.NEWROW.row;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.AddEventDateTimeListAdapter;
import com.NEWROW.row.Data.AddEventDateTimeData;
import com.NEWROW.row.Data.DirectoryData;
import com.NEWROW.row.Data.SubGoupData;
import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.DateHelper;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.ImageCompression;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.MarshMallowPermission;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.Utils;
import com.NEWROW.row.croputility.Crop;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by USER on 24-12-2015.
 */
public class AddEvent extends Activity {
    View rsvpPanel, reminderPanel;
    ArrayList<String> selectedsubgrp;
    private static double longitute, latitude;
    RadioButton d_radio1, d_radio2;
    RadioButton d_radio0;
    View addQuestionWrapper;
    TextView getCount, tv_yes;
 	public static int count_write_events = 0;
    String finalImagePath;
    String moduleName = "";
    TextView tv_eventDate, tv_eventTime, tv_publishDate, tv_publishTime, tv_notificationDate, tv_NotificationTime, tv_expiryDate, tv_expiryTime;
    CheckBox cb_display;
    TextView tv_title, date, tv_addquestion, tv_question;
    ImageView iv_backbutton, iv_edit, iv_add_que_toggle;
    ImageView call_button, iv_event_photo, iv_toggle, iv_enableRSVP;
    ListView listView;
    TextView tv_addSign, tv_no, smscount,emailcount,tv_sms_per_user,tv_desc_char_count;
    EditText et_evetTitle, et_eventDesc, et_eventVenue, et_question_single, et_question_open, et_option1, et_option2,et_link;
    ArrayList<DirectoryData> listaddmemberdata = new ArrayList<>();
    ArrayList<SubGoupData> listaddsubgrp = new ArrayList<>();
    String eventType = "0";
    String inputids = "";
    ScrollView scrollview;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    String repeat_date = "";
    String repeat_time = "";
    LinearLayout linear_repeatnotification,ll_link;
    private ArrayList<AddEventDateTimeData> list_datetime = new ArrayList<AddEventDateTimeData>();
    private AddEventDateTimeListAdapter adapter_datetime;
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
    private String uploadedimgid = "0";
    ProgressDialog pd;
    private String event_id;
    private String flag_callwebsercie = "0";
    private String eventID = "0";
    private String edit_event_selectedids = "0";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;
    private String hasimageflag = "0";
    CheckBox cb_noti_all, cb_noti_nonsmart,cb_send_sms,cb_send_email;
    String sendSMSAll = "0"; // 0- Off 1- On
    String sendSMSNonSmartPh = "0"; // 0- Off 1- On
    String toggle_flag_onoff = "0";
    String questionType = "0";
    String popupflag = "0";
    String toggle_que_flag = "0";
    String question, option1, option2;
    LinearLayout linear_question;
    ImageView edit;
    String type;
    String que, opt1, opt2;
    TextView tv_add, tv_answer1, tv_answer2;
    String mode = "";
    int flag;
    boolean setPublishDateDisable = false;
    boolean setEventDateDisable = false;
    private String publishDate="", eventDate="",expiryDate="";
    private static Uri mCapturedImageURI;
   // SimpleDateFormat sdf=new SimpleDateFormat("dd MMM yyyy | HH:mm");
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SingleDateAndTimePickerDialog datetimePicker,datetimeForReminder;
    // AddEventDateTimeData data;
    Boolean isPublish = false;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_event);

        context = AddEvent.this;

        try {
            mode = getIntent().getExtras().getString("edit");
        } catch(Exception e) {
            e.printStackTrace();
        }

        addQuestionWrapper = findViewById(R.id.addQuestionWrapper);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);

        // iv_backbutton.setVisibility(View.GONE);
       // moduleName = PreferenceManager.getPreference(this, PreferenceManager.MODUEL_NAME, "Events");

        tv_title.setText("Events");

        d_radio0 = (RadioButton) findViewById(R.id.d_radio0);
        d_radio1 = (RadioButton) findViewById(R.id.d_radio1);
        d_radio2 = (RadioButton) findViewById(R.id.d_radio2);
        cb_display = (CheckBox) findViewById(R.id.cb_display);
        iv_event_photo = (ImageView) findViewById(R.id.iv_event_photo);
        getCount = (TextView) findViewById(R.id.getCount);
        date = (TextView) findViewById(R.id.date);
        tv_yes = (TextView) findViewById(R.id.tv_yes);
        listView = (ListView) findViewById(R.id.listView);
        getCount = (TextView) findViewById(R.id.getCount);
        iv_edit = (ImageView) findViewById(R.id.iv_edit);
        iv_enableRSVP = (ImageView) findViewById(R.id.iv_enableRSVP);
        iv_toggle = (ImageView) findViewById(R.id.iv_toggle);
        listView = (ListView) findViewById(R.id.listView);
        tv_addSign = (TextView) findViewById(R.id.tv_addSign);
        tv_eventDate = (TextView) findViewById(R.id.tv_eventDate);
        tv_eventTime = (TextView) findViewById(R.id.tv_eventTime);
        tv_publishDate = (TextView) findViewById(R.id.tv_publishDate);
        tv_publishTime = (TextView) findViewById(R.id.tv_publishTime);
        tv_expiryDate = (TextView) findViewById(R.id.tv_expiryDate);
        tv_expiryTime = (TextView) findViewById(R.id.tv_expiryTime);
        tv_addquestion = (TextView) findViewById(R.id.tv_addquestion);
        tv_no = (TextView) findViewById(R.id.tv_no);
        tv_notificationDate = (TextView) findViewById(R.id.tv_notificationDate);
        tv_NotificationTime = (TextView) findViewById(R.id.tv_NotificationTime);
        et_evetTitle = (EditText) findViewById(R.id.et_eventTitle);
        et_eventDesc = (EditText) findViewById(R.id.et_evetDesc);
        et_eventVenue = (EditText) findViewById(R.id.et_eventVenue);
        et_link=(EditText)findViewById(R.id.et_eventLink);
        ll_link = (LinearLayout) findViewById(R.id.ll_link);
        linear_repeatnotification = (LinearLayout) findViewById(R.id.linear_repeatnotification);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        cb_noti_nonsmart = (CheckBox) findViewById(R.id.cb_noti_nonsmart);
        cb_send_sms = (CheckBox) findViewById(R.id.cb_send_sms);
        cb_send_email = (CheckBox) findViewById(R.id.cb_send_email);
        cb_noti_all = (CheckBox) findViewById(R.id.cb_noti_all);
        iv_add_que_toggle = (ImageView) findViewById(R.id.iv_add_que_toggle);
        tv_question = (TextView) findViewById(R.id.tv_question);
        smscount = (TextView) findViewById(R.id.smscount);
        emailcount = (TextView) findViewById(R.id.emailcount);
        tv_answer1 = (TextView) findViewById(R.id.tv_answer1);
        tv_answer2 = (TextView) findViewById(R.id.tv_answer2);
        edit = (ImageView) findViewById(R.id.edit);
        linear_question = (LinearLayout) findViewById(R.id.linear_question);
        listView = (ListView) findViewById(R.id.listView);
        rsvpPanel = findViewById(R.id.rsvpPanel);
        reminderPanel = findViewById(R.id.reminderPanel);
        tv_sms_per_user = (TextView) findViewById(R.id.tv_sms_per_user);
        tv_desc_char_count = (TextView) findViewById(R.id.tv_desc_char_count);

        // listItems.add("First Item - added on Activity Create");
        /*adapter_datetime = new AddEventDateTimeListAdapter(this,R.layout.add_event_datetime_list_item, list_datetime);
        listView.setAdapter(adapter_datetime);*/

        // MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);

        d_radio0.setChecked(true);

        clearselectedtext();

        init();

        Bundle intent = getIntent().getExtras();

        if (intent != null) {
            event_id = intent.getString("event_id"); // Created Group ID
            tv_yes.setText("Update");
            webservices_getdata();
        }

        loadAddress();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void loadAddress() {

        Hashtable<String, String> params = new Hashtable<>();

        params.put("grpID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID));

        try {

            JSONObject requestData = new JSONObject(new Gson().toJson(params));

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constant.GETClubDetails,
                    requestData,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                getResult(response);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ROW", "♦Error : " + error);
                            error.printStackTrace();
                            Toast.makeText(context, "Failed to receive Data from server . Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
            );

            AppController.getInstance().addToRequestQueue(context, request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getResult(JSONObject response) {

        JSONObject json = null;

        try {

            Utils.log("Response:" + response);

            JSONObject jsonTBGetGroupResult = response.getJSONObject("TBGetGroupResult");

            final String status = jsonTBGetGroupResult.getString("status");

            if (status.equals("0")) {

                JSONObject jsonObject = jsonTBGetGroupResult.getJSONObject("getGroupDetailResult");
                String meetingPlace = jsonObject.getString("MeetingPlace");
                et_eventVenue.setText(meetingPlace);

                if(jsonObject.has("SMSCount") && jsonObject.getString("SMSCount")!=null) {
                    Utils.smsCount = jsonObject.getString("SMSCount");
                    smscount.setText(Utils.smsCount);
                }
            }

        } catch (JSONException e){
            Utils.log("Error is : "+e);
            e.printStackTrace();
        } catch (Exception e) {
            Utils.log("Error is : "+e);
            e.printStackTrace();
        }

    }

    private void clearselectedtext() {
        getCount.setText("");
        iv_edit.setVisibility(View.GONE);
    }

    public void init() {

        smscount.setText(Utils.smsCount);

        iv_backbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Utils.popupback(context);
            }

        });


        tv_addquestion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        et_eventDesc.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                 int length = charSequence.toString().trim().length();

                 if(length>0)
                    tv_desc_char_count.setText(String.valueOf(length));
                 else
                     tv_desc_char_count.setText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

/*
        cb_noti_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sendSMSAll = "1";
                if(cb_noti_nonsmart.isChecked()){
                    cb_noti_nonsmart.setChecked(false);
                    cb_noti_all.setChecked(true);
                }
            }
        });*/

          cb_noti_all.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (cb_noti_all.isChecked()) {

                    String smsCount = smscount.getText().toString();

                    if(smsCount!=null && !smsCount.trim().isEmpty()) {

                        if(Integer.parseInt(smsCount)!=0 ){

                            sendSMSAll = "1";
                            sendSMSNonSmartPh = "0";
                            cb_noti_nonsmart.setChecked(false);

                        } else {
                            popup_sms();
                            cb_noti_all.setChecked(false);
                        }

                    } else {
                        popup_sms();
                        cb_noti_all.setChecked(false);
                    }

                } else {
                    sendSMSAll = "0";
                }

//                if (cb_noti_nonsmart.isChecked()) {
//                    sendSMSNonSmartPh = "0";
//                    cb_noti_nonsmart.setChecked(false);
//                    cb_noti_all.setChecked(true);
//                }
            }
        });

        cb_noti_nonsmart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (cb_noti_nonsmart.isChecked()) {

                    String smsCount = smscount.getText().toString();

                    if(smsCount!=null && !smsCount.trim().isEmpty()){

                        if(Integer.parseInt(smsCount)!=0 ){
                            sendSMSNonSmartPh = "1";
                            sendSMSAll = "0";
                            cb_noti_all.setChecked(false);
                        } else {
                            popup_sms();
                            cb_noti_nonsmart.setChecked(false);
                        }

                    } else {
                        popup_sms();
                        cb_noti_nonsmart.setChecked(false);
                    }

                } else {
                    sendSMSNonSmartPh = "0";
                }
//                if (cb_noti_all.isChecked()) {
//                    sendSMSAll = "0";
//                    cb_noti_all.setChecked(false);
//                    cb_noti_nonsmart.setChecked(true);
//                }
            }
        });

     /*   cb_noti_nonsmart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sendSMSNonSmartPh = "1";
                if(cb_noti_all.isChecked()){
                    cb_noti_all.setChecked(false);
                    cb_noti_nonsmart.setChecked(true);
                }
            }
        });*/


        cb_send_sms.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


             if (cb_send_sms.isChecked()) {

                String smsCount = smscount.getText().toString();

                    if(!smsCount.trim().isEmpty()){

                        if(Integer.parseInt(smsCount)!=0 ){
                            sendSMSAll = "1";
                            sendSMSNonSmartPh = "0";
                            cb_send_sms.setChecked(true);
                        } else {
                            popup_sms();
                            cb_send_sms.setChecked(false);
                        }

                    } else {
                        popup_sms();
                        cb_send_sms.setChecked(false);
                    }

                 }

                /*if (cb_send_sms.isChecked() && validationForSendSMS()) {

                    webservicesForSendSMS(false);

                    Log.e("cn send sms","***** calling send sms ******");

                } else {
                    sendSMSAll = "0";
                }*/

            }

        });

        et_eventVenue.setFocusableInTouchMode(true);

        et_eventVenue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d("TOUCHBASE", "CLICK CLICK");

              /*  if (et_eventVenue.getText().toString().trim().length() <= 0) {
                    Intent i = new Intent(getApplicationContext(), EventVenueSearchAddress.class);
                    startActivityForResult(i, 5);
                    et_eventVenue.setFocusableInTouchMode(false);
                } else*/

            }
        });

        /*et_eventVenue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });*/
        tv_no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideKeyboard();
                finish();
            }
        });

        iv_toggle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (toggle_flag_onoff.equals("1")) {

                    //iv_toggle.setImageResource(getResources().getDrawable(R.drawable.off_toggle_btn));
                    iv_toggle.setImageResource(R.drawable.off_toggle_btn);
                    linear_repeatnotification.setVisibility(View.GONE);
                    list_datetime.clear();
                    adapter_datetime = new AddEventDateTimeListAdapter(AddEvent.this, R.layout.add_event_datetime_list_item, list_datetime);
                    listView.setAdapter(adapter_datetime);
                    toggle_flag_onoff = "0";

                } else {

                    iv_toggle.setImageResource(R.drawable.on_toggle_btn);
                    toggle_flag_onoff = "1";
                    linear_repeatnotification.setVisibility(View.VISIBLE);

                    scrollview.post(new Runnable() {

                        @Override
                        public void run() {
                            scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                        }

                    });
                }
            }
        });

        iv_enableRSVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("RSVP", "Init first for rsvp");
                String status = iv_enableRSVP.getTag().toString();
                if (status.equals("0")) {  // enabled
                    iv_enableRSVP.setTag("1");
                    iv_enableRSVP.setImageResource(R.drawable.on_toggle_btn);
                    addQuestionWrapper.setVisibility(View.VISIBLE);
                } else {   // disabled
                    if(isPublish){
                        popup_warning();
                    }else {
                        iv_enableRSVP.setTag("0");
                        iv_enableRSVP.setImageResource(R.drawable.off_toggle_btn);
                        addQuestionWrapper.setVisibility(View.GONE);
                    }

                }
            }
        });

        iv_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (eventType.equals("2")) {

                    Intent i = new Intent(AddEvent.this, AddMembers.class);
                    //i.putParcelableArrayListExtra("name1", memberData);
                    i.putParcelableArrayListExtra("selected_memberdata", listaddmemberdata);
                    i.putExtra("edit_announcement_selectedids", edit_event_selectedids);
                    i.putExtra("parentId", "0");
                    i.putExtra("subgroupname", "Sub Groups");
                    i.putExtra("moduleName","Sub Groups");
                    startActivityForResult(i, 3);

                } else if (eventType.equals("1")) {

                    /*Intent i = new Intent(AddEvent.this, SubGroupSelectionList.class);
                    //i.putParcelableArrayListExtra("name1", memberData);
                    i.putParcelableArrayListExtra("selected_subgrpdata", listaddsubgrp);
                    i.putExtra("edit_announcement_selectedids", edit_event_selectedids);
                    startActivityForResult(i, 1);*/

                    //Intent subgrp = new Intent(AddEvent.this, SubGroupSelectionList.class);
                    Intent subgrp = new Intent(AddEvent.this, NewGroupSelectionActivity.class);
                    subgrp.putExtra("flag_addsubgrp", "1");
                    subgrp.putExtra("selected", selectedsubgrp);
                    subgrp.putExtra("edit", "1");
                    startActivityForResult(subgrp, 1);
                }

            }
        });

        iv_event_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!marshMallowPermission.checkPermissionForCamera()) {

                    marshMallowPermission.requestPermissionForCamera();

                } else {

                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    } else {
                        selectImage();
                    }
                }
            }

        });

        tv_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(tv_yes.getText().equals("Update")){

                    // method created for validation during update

                    if(updateValidation()){

                        if (InternetConnection.checkConnection(getApplicationContext())) {

                            /*if(cb_send_sms.isChecked()) {

                                String desc = et_eventDesc.getText().toString().trim();

                                if(desc.length()>2000) {
                                    Toast.makeText(context, " “Description” cannot be more than 2,000 characters for SMS", Toast.LENGTH_LONG).show();
                                }else{
                                    webservicesForSendSMS(true);
                                }

                            } else {
                                webservices();
                            }*/

                            webservices();

                        } else {
                            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {

                    if (validation()) {

                        if (InternetConnection.checkConnection(getApplicationContext())) {

                            /*if(cb_send_sms.isChecked()) {

                                String desc = et_eventDesc.getText().toString().trim();

                                if(desc.length()>2000) {
                                    Toast.makeText(context, " “Description” cannot be more than 2,000 characters for SMS", Toast.LENGTH_LONG).show();
                                }else{
                                    webservicesForSendSMS(true);
                                }

                            } else {
                                webservices();
                            }*/

                            webservices();

                        } else {
                            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        tv_eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyBoard(AddEvent.this,tv_eventDate);
                datepicker(tv_eventDate);
            }
        });

        tv_eventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  if (valid(0))

//                new SingleDateAndTimePickerDialog.Builder(AddEvent.this)
//                        .bottomSheet()
//                        .curved()
//                        //.minutesStep(15)
//
//                        //.displayHours(false)
//                        //.displayMinutes(false)
//
//                        //.todayText("aujourd'hui")
//
//                        .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
//                            @Override
//                            public void onDisplayed(SingleDateAndTimePicker picker) {
//                                //retrieve the SingleDateAndTimePicker
//                            }
//                        })
//
//                        .title("Event Date Time")
//                        .listener(new SingleDateAndTimePickerDialog.Listener() {
//                            @Override
//                            public void onDateSelected(Date date) {
//
//                            }
//                        }).display();

                Utils.hideKeyBoard(AddEvent.this,tv_eventTime);
                ShowTimePicker(tv_eventTime,1);

            }
        });

        tv_publishDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  if (valid(1))
                Utils.hideKeyBoard(AddEvent.this,tv_publishDate);
                datepicker(tv_publishDate);

            }
        });

        tv_publishTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyBoard(AddEvent.this,tv_publishTime);
                ShowTimePicker(tv_publishTime,2);
            }
        });

        tv_expiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.hideKeyBoard(AddEvent.this,tv_expiryDate);
                datepicker(tv_expiryDate);
            }
        });

        tv_expiryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyBoard(AddEvent.this,tv_expiryTime);
                ShowTimePicker(tv_expiryTime,3);
            }
        });

        tv_notificationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker(tv_notificationDate);
            }
        });

        tv_NotificationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTimePicker(tv_NotificationTime,4);
            }
        });

        tv_addSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // if (validation() == true)
                   // datepicker_repeate_notification();
//                if(validationForReminder()){
//                    ShowTimePicker_repeate_notification();
//
//                }
                ShowTimePicker_repeate_notification();

            }
        });


        //-------------------------------Question Toggle -------------------------------------------------

        iv_add_que_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (toggle_que_flag.equals("1")) {
                    //iv_toggle.setImageResource(getResources().getDrawable(R.drawable.off_toggle_btn));
                    iv_add_que_toggle.setImageResource(R.drawable.off_toggle_btn);
                    // linear_question.setVisibility(View.GONE);
                    edit.setVisibility(View.GONE);
                    linear_question.setVisibility(View.GONE);
                    toggle_que_flag = "0";
                    questionType = "0";
                    Log.d("---------", "-------" + questionType);


                } else {
                    iv_add_que_toggle.setImageResource(R.drawable.on_toggle_btn);

                    // linear_question.setVisibility(View.VISIBLE);

                    toggle_que_flag = "1";

                    final Dialog dialog = new Dialog(AddEvent.this, android.R.style.Theme_Translucent);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.popup_select_question_type);

                    TextView tv_single_select = (TextView) dialog.findViewById(R.id.tv_single_select);
                    TextView tv_open_ended = (TextView) dialog.findViewById(R.id.tv_open_ended);
                    TextView tv_next = (TextView) dialog.findViewById(R.id.tv_next);

                    ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);

                    LinearLayout linear_sinleselect = (LinearLayout) dialog.findViewById(R.id.linear_sinleselect);
                    LinearLayout linear_openended = (LinearLayout) dialog.findViewById(R.id.linear_openended);

                    final RadioButton rb_singleSelect = (RadioButton) dialog.findViewById(R.id.rb_singleSelect);
                    final RadioButton rb_openedended = (RadioButton) dialog.findViewById(R.id.rb_openedended);

                    rb_singleSelect.setEnabled(false);
                    rb_openedended.setEnabled(false);

                    final int blueColor = Color.parseColor("#4868D2");
                    final int greyColor = Color.parseColor("#747474");

                    if ((tv_question.getText().toString().trim().matches("") || tv_question.getText().toString().trim() == null)) {
                        iv_close.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                iv_add_que_toggle.setImageResource(R.drawable.off_toggle_btn);
                                toggle_que_flag = "1";
                                questionType = "0";
                                linear_question.setVisibility(View.GONE);
                                dialog.hide();
                                Log.d("---------", "-------" + questionType);
                            }
                        });

                        linear_sinleselect.setOnClickListener(new View.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onClick(View v) {
                                // Log.d("Touchbase", "*********** CLICK");
                                rb_singleSelect.setChecked(true);
                                rb_openedended.setChecked(false);

                                if (Build.VERSION.SDK_INT >= 21) {
                                    rb_singleSelect.setButtonTintList(ColorStateList.valueOf(blueColor));
                                    rb_openedended.setButtonTintList(ColorStateList.valueOf(greyColor));
                                }
                            }
                        });

                        linear_openended.setOnClickListener(new View.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onClick(View v) {
                                // Log.d("Touchbase", "*********** CLICK****************");
                                rb_singleSelect.setChecked(false);
                                rb_openedended.setChecked(true);

                                if (Build.VERSION.SDK_INT >= 21) {
                                    rb_singleSelect.setButtonTintList(ColorStateList.valueOf(greyColor));
                                    rb_openedended.setButtonTintList(ColorStateList.valueOf(blueColor));
                                }
                            }
                        });

                        tv_next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (rb_singleSelect.isChecked()) {
                                    dialog.hide();
                                    singleSelectPopup();
                                } else if (rb_openedended.isChecked()) {
                                    dialog.hide();
                                    openEndedPopup();
                                }
                            }
                        });
                      /*  tv_open_ended.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                openEndedPopup();
                            }
                        });

                        tv_single_select.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();

                                singleSelectPopup();
                            }
                        });*/


                        dialog.show();
                    } else {
                        iv_add_que_toggle.setImageResource(R.drawable.off_toggle_btn);
                        toggle_que_flag = "0";

                        tv_question.setText("");
                        tv_answer1.setText("");
                        tv_answer2.setText("");

                        //linear_question.setVisibility(View.GONE);
                        edit.setVisibility(View.GONE);
                        if ((tv_question.getText().toString().trim().matches("") || tv_question.getText().toString().trim() == null)) {
                            questionType = "0";
                            linear_question.setVisibility(View.GONE);
                        }

                    }

                }

            }

        });

        //------------------------------------------------------------------------------------------------

    }


    //-------------------------------Methods for Add Question ----------------------------------

    public void openEndedPopup() {

        // view_line.setVisibility(View.GONE);
        final Dialog dialog_open_end = new Dialog(AddEvent.this, android.R.style.Theme_Translucent);
        dialog_open_end.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_open_end.setContentView(R.layout.popup_open_ended);

        ImageView iv_close = (ImageView) dialog_open_end.findViewById(R.id.iv_close);
        tv_add = (TextView) dialog_open_end.findViewById(R.id.tv_add);
        TextView tv_back = (TextView) dialog_open_end.findViewById(R.id.tv_back);
        et_question_open = (EditText) dialog_open_end.findViewById(R.id.et_question);
        et_question_open.setSelection(et_question_open.getText().length());
        /*EditText et_recepients_response = (EditText) dialog_open_end.findViewById(R.id.et_recepients_response);
        et_recepients_response.setSelected(false);

        et_recepients_response.setEnabled(false);
        et_recepients_response.setClickable(false);*/
        tv_add.setText("Add");
        popupflag = "1";


        et_question_open.setText(tv_question.getText().toString());

        if (!(tv_question.getText().toString().trim().matches("") || tv_question.getText().toString().trim() == null)) {
            tv_add.setText("Update");
            et_question_open.setText(que);
            et_question_open.setText(tv_question.getText().toString());

        }


        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // question = et_question_open.getText().toString();
            //  Log.d("..............","........"+question);
            if (!(et_question_open.getText().toString().trim().matches("") || et_question_open.getText().toString().trim() == null)) {
                //dialog_open_end.dismiss();


                tv_question.setText(et_question_open.getText().toString());
                et_question_open.setText(tv_question.getText().toString());
                //tv_answer1.setText(et_option1.getText().toString());
                //  tv_answer2.setText(et_option2.getText().toString());
                linear_question.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
                edit.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        tv_add.setText("Update");
                        if (popupflag.equals("1")) {

                            openEndedPopup();

                        } else if (popupflag.equals("0")) {
                            singleSelectPopup();

                        }
                    }

                });


                dialog_open_end.hide();

            } else {
                Toast.makeText(context, "Please enter question", Toast.LENGTH_LONG).show();
            }
            }
        });


        questionType = "1";
        // linear_question.setVisibility(View.VISIBLE);
        Log.d("---------", "-------" + questionType);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );
                if (tv_question.getText().toString().trim().matches("") || tv_question.getText().toString().trim() == null) {
                    questionType = "0";
                    linear_question.setVisibility(View.GONE);
                    iv_add_que_toggle.setImageResource(R.drawable.off_toggle_btn);
                }
                dialog_open_end.hide();
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_question.getText().toString().trim().matches("") || tv_question.getText().toString().trim() == null) {
                    questionType = "0";
                    linear_question.setVisibility(View.GONE);
                    iv_add_que_toggle.setImageResource(R.drawable.off_toggle_btn);

                }
                Log.d("---------", "-------" + questionType);
                dialog_open_end.hide();

            }
        });

        dialog_open_end.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Utils.log("Dialog is dismissed");
                questionType = "0";
                linear_question.setVisibility(View.GONE);
                iv_add_que_toggle.setImageResource(R.drawable.off_toggle_btn);
            }
        });

        dialog_open_end.show();
    }


    public void singleSelectPopup() {
        final Dialog dialog_singleSelect = new Dialog(AddEvent.this, android.R.style.Theme_Translucent);
        dialog_singleSelect.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_singleSelect.setContentView(R.layout.popup_yes_no_type_question);

        TextView tv_back = (TextView) dialog_singleSelect.findViewById(R.id.tv_back);
        tv_add = (TextView) dialog_singleSelect.findViewById(R.id.tv_add);
        et_question_single = (EditText) dialog_singleSelect.findViewById(R.id.et_question);
        et_option1 = (EditText) dialog_singleSelect.findViewById(R.id.et_option1);
        et_option2 = (EditText) dialog_singleSelect.findViewById(R.id.et_option2);
        ImageView iv_close = (ImageView) dialog_singleSelect.findViewById(R.id.iv_close);

        LinearLayout linear_option1 = (LinearLayout) dialog_singleSelect.findViewById(R.id.linear_option1);
        LinearLayout linear_option2 = (LinearLayout) dialog_singleSelect.findViewById(R.id.linear_option2);

        final RadioButton rb_option1 = (RadioButton) dialog_singleSelect.findViewById(R.id.rb_option1);
        final RadioButton rb_option2 = (RadioButton) dialog_singleSelect.findViewById(R.id.rb_option2);


        et_question_single.setSelection(et_question_single.getText().length());
        et_option1.setSelection(et_option1.getText().length());
        et_option2.setSelection(et_option2.getText().length());

        et_question_single.setSelection(et_question_single.getText().length());


        final int blueColor = Color.parseColor("#4868D2");
        final int greyColor = Color.parseColor("#747474");

        tv_add.setText("Add");
        popupflag = "0";


        et_option1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count> 0) {
                    rb_option1.setChecked(true);
                    //rb_option2.setChecked(false);
                    if (Build.VERSION.SDK_INT >= 21) {
                        rb_option1.setButtonTintList(ColorStateList.valueOf(blueColor));
                        //  rb_option2.setButtonTintList(ColorStateList.valueOf(greyColor));
                    }
                }else {
                    rb_option1.setChecked(false);
                    //rb_option2.setChecked(true);
                    if (Build.VERSION.SDK_INT >= 21) {
                        rb_option1.setButtonTintList(ColorStateList.valueOf(greyColor));
                        //  rb_option2.setButtonTintList(ColorStateList.valueOf(blueColor));
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        et_option2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("TouchBase","************** IF"+count);
                if (count > 0) {
                    Log.d("TouchBase","************** IF");
                    // rb_option1.setChecked(false);
                    rb_option2.setChecked(true);
                    if (Build.VERSION.SDK_INT >= 21) {
                        //   rb_option1.setButtonTintList(ColorStateList.valueOf(greyColor));
                        rb_option2.setButtonTintList(ColorStateList.valueOf(blueColor));
                    }
                }else {
                    Log.d("TouchBase","************** Else  ");
                    // rb_option1.setChecked(true);
                    rb_option2.setChecked(false);
                    if (Build.VERSION.SDK_INT >= 21) {
                        //   rb_option1.setButtonTintList(ColorStateList.valueOf(blueColor));
                        rb_option2.setButtonTintList(ColorStateList.valueOf(greyColor));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if (!(questionType.equals("0"))) {
            et_question_single.setText(que);
            et_option1.setText(opt1);
            et_option2.setText(opt2);

            et_question_single.setText(tv_question.getText().toString());
            et_option1.setText(tv_answer1.getText().toString());
            et_option2.setText(tv_answer2.getText().toString());

        }


        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(et_question_single.getText().toString().trim().matches("") || et_question_single.getText().toString().trim() == null
                        || et_option1.getText().toString().trim().matches("") || et_option1.getText().toString().trim() == null
                        || et_option2.getText().toString().trim().matches("") || et_option2.getText().toString().trim() == null
                )) {
                    //questionType = "1";
                    question = tv_add.getText().toString();
                    Log.d("..............", "........" + question);
                    tv_question.setText(et_question_single.getText().toString());
                    tv_answer1.setText(et_option1.getText().toString());
                    tv_answer2.setText(et_option2.getText().toString());

                    et_question_single.setText(tv_question.getText().toString());
                    et_option1.setText(tv_answer1.getText().toString());
                    et_option2.setText(tv_answer2.getText().toString());

                    edit.setVisibility(View.VISIBLE);
                    edit.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            tv_add.setText("Update");

                            if (popupflag.equals("1")) {
                                openEndedPopup();

                            } else if (popupflag.equals("0")) {
                                singleSelectPopup();

                            }
                        }
                    });
                    linear_question.setVisibility(View.VISIBLE);
                    dialog_singleSelect.hide();
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_LONG).show();
                }
            }
        });


        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );
                if (tv_question.getText().toString().trim().matches("") || tv_question.getText().toString().trim() == null) {
                    questionType = "0";
                    linear_question.setVisibility(View.GONE);
                    iv_add_que_toggle.setImageResource(R.drawable.off_toggle_btn);

                }
                dialog_singleSelect.hide();
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tv_question.getText().toString().trim().matches("") || tv_question.getText().toString().trim() == null) {
                    questionType = "0";
                    linear_question.setVisibility(View.GONE);
                    iv_add_que_toggle.setImageResource(R.drawable.off_toggle_btn);
                } else
                    edit.setVisibility(View.VISIBLE);
                dialog_singleSelect.hide();
            }
        });

        if (!(tv_question.getText().toString().trim().matches("") || tv_question.getText().toString().trim() == null)) {
            tv_add.setText("Update");
            et_question_single.setText(que);
            et_option1.setText(opt1);
            et_option2.setText(opt2);

            et_question_single.setText(tv_question.getText().toString());
            et_option1.setText(tv_answer1.getText().toString());
            et_option2.setText(tv_answer2.getText().toString());

            edit.setVisibility(View.VISIBLE);
        }


        questionType = "2";
        //  linear_question.setVisibility(View.VISIBLE);
        Log.d("---------", "-------" + questionType);
        dialog_singleSelect.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                questionType = "0";
                linear_question.setVisibility(View.GONE);
                iv_add_que_toggle.setImageResource(R.drawable.off_toggle_btn);
            }
        });
        dialog_singleSelect.show();
    }

    //------------------------------------------------------------------------------------------


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {

            case R.id.d_radio0:

                if (checked) {
                    //set inch button to unchecked
                    eventType = "0";
                    inputids = "";
                    d_radio1.setChecked(false);
                    d_radio2.setChecked(false);
                    d_radio2.setEnabled(true);
                    d_radio1.setEnabled(true);
                    clearselectedtext();
                }

                break;

            case R.id.d_radio1:

                if (checked) {
                    //set MM button to unchecked
                   /* d_radio0.setChecked(false);
                    d_radio2.setChecked(false);
                    d_radio1.setEnabled(false);
                    d_radio2.setEnabled(true);*/
                    d_radio1.setChecked(false);
                    //Intent subgrp = new Intent(AddEvent.this, SubGroupList.class);

                    //Intent subgrp = new Intent(AddEvent.this, SubGroupSelectionList.class);
                    Intent subgrp = new Intent(AddEvent.this, NewGroupSelectionActivity.class);
                    subgrp.putExtra("flag_addsubgrp", "1");
                    startActivityForResult(subgrp, 1);
                }
                //startActivity(subgrp);
                break;

            case R.id.d_radio2:

                if (checked) {

                    d_radio2.setChecked(false);

                    Intent i = new Intent(AddEvent.this, AddMembers.class);
                    i.putExtra("isAddCreatorByDefault",true);
                    startActivityForResult(i, 3);

                 /*   d_radio2.setEnabled(false);
                    d_radio1.setEnabled(true);
                    d_radio0.setChecked(false);
                    d_radio1.setChecked(false);*/
                }
                // d_radio2.setEnabled(false);

                break;
        }
    }

    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myAppSettings);
    }

    private void selectImage() {

        final CharSequence[] options;
        Drawable.ConstantState constantState;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            constantState = context.getResources()
                    .getDrawable(R.drawable.edit_image, context.getTheme())
                    .getConstantState();
        } else {
            constantState = context.getResources().getDrawable(R.drawable.edit_image)
                    .getConstantState();
        }

        if (iv_event_photo.getDrawable().getConstantState() == constantState) {
            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
            hasimageflag = "1";
        }else {
            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Remove Photo", "Cancel"};
        }



//        if (hasimageflag.equals("0")) {
//            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
//            hasimageflag = "1";
//        } else {
//            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Remove Photo", "Cancel"};
//
//        }

        AlertDialog.Builder builder = new AlertDialog.Builder(AddEvent.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    String fileName = "temp.jpg";
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, fileName);

                    mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

                    startActivityForResult(intent, 4);

                } else if (options[item].equals("Choose from Gallery")) {
//                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, 2);
                    Crop.pickImage(AddEvent.this);
                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();
                } else if (options[item].equals("Remove Photo")) {
                    if (eventID.equals("0")) {
                        hasimageflag="0";
                        iv_event_photo.setImageResource(R.drawable.edit_image);
                    } else {
                        remove_photo_webservices();
                    }
                    //dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    /*************
     * web Service
     ********/

    private void webservices_getdata() {

        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("grpId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));//PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)
        arrayList.add(new BasicNameValuePair("eventID", event_id));//eventid
        arrayList.add(new BasicNameValuePair("groupProfileID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));//PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)

        flag_callwebsercie = "1";

        Log.d("Response", "PARAMETERS " + Constant.GetEventDetails + " :- " + arrayList.toString());

        if ( InternetConnection.checkConnection(getApplicationContext())) {
            new WebConnectionAsyncLogin(Constant.GetEventDetails, arrayList, AddEvent.this,false).execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void remove_photo_webservices() {

        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        arrayList.add(new BasicNameValuePair("typeID", eventID));
        arrayList.add(new BasicNameValuePair("type", "Event"));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        arrayList.add(new BasicNameValuePair("grpID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));

        flag_callwebsercie = "4";
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "PARAMETERS " + Constant.DeleteImage + " :- " + arrayList.toString());

        new WebConnectionAsyncLogin(Constant.DeleteImage, arrayList, AddEvent.this,false).execute();
    }

    private void webservices() {

        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        String isSubGroupAdmin = "0";  // means no

        String isAdmin = PreferenceManager.getPreference(AddEvent.this, PreferenceManager.IS_GRP_ADMIN, "No");

        if (isAdmin.equalsIgnoreCase("partial")) {
            isSubGroupAdmin = "1"; //means yes
        }

        arrayList.add(new BasicNameValuePair("isSubGrpAdmin", isSubGroupAdmin));
        arrayList.add(new BasicNameValuePair("eventID", eventID));
        arrayList.add(new BasicNameValuePair("questionEnable", questionType));
        arrayList.add(new BasicNameValuePair("eventType", eventType));
        arrayList.add(new BasicNameValuePair("membersIDs", inputids));
        arrayList.add(new BasicNameValuePair("eventImageID", uploadedimgid));
        arrayList.add(new BasicNameValuePair("evntTitle", et_evetTitle.getText().toString()));
        arrayList.add(new BasicNameValuePair("evntDesc", et_eventDesc.getText().toString()));

        //et_address1.getText().toString().trim().replaceAll("(\\r|\\n)", "").replaceAll("[()]", ""))
        //arrayList.add(new BasicNameValuePair("eventVenue", et_eventVenue.getText().toString()));
        arrayList.add(new BasicNameValuePair("eventVenue", et_eventVenue.getText().toString().trim().replaceAll("(\\r|\\n)", "").replaceAll("[()]", "")));
        //LAT LONG
        //Log.d("TOUCH BASE", " @---------------- " + getLatLong(getLocationInfo(et_eventVenue.getText().toString())));

//        if (!getLatLong(getLocationInfo(et_eventVenue.getText().toString()))) {
//            latitude = 0.00;
//            longitute = 0.00;
//        }
        //LAT LONG

        arrayList.add(new BasicNameValuePair("venueLat", String.valueOf(0.00)));
        arrayList.add(new BasicNameValuePair("venueLong", String.valueOf(0.00)));

//        arrayList.add(new BasicNameValuePair("evntDate", tv_eventDate.getText().toString() + " " + tv_eventTime.getText().toString()));
        arrayList.add(new BasicNameValuePair("evntDate", eventDate));
        arrayList.add(new BasicNameValuePair("evntTime", ""));

//        arrayList.add(new BasicNameValuePair("publishDate", tv_publishDate.getText().toString() + " " + tv_publishTime.getText().toString()));
        arrayList.add(new BasicNameValuePair("publishDate", publishDate));


        arrayList.add(new BasicNameValuePair("publishTime", ""));
//        arrayList.add(new BasicNameValuePair("expiryDate", tv_expiryDate.getText().toString() + " " + tv_expiryTime.getText().toString()));
        arrayList.add(new BasicNameValuePair("expiryDate", expiryDate));
        arrayList.add(new BasicNameValuePair("expiryTime", ""));

//        arrayList.add(new BasicNameValuePair("notifyDate", tv_publishDate.getText().toString() + " " + tv_publishTime.getText().toString()));
        arrayList.add(new BasicNameValuePair("notifyDate", publishDate));
        arrayList.add(new BasicNameValuePair("notifyTime", ""));
        arrayList.add(new BasicNameValuePair("userID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));

        arrayList.add(new BasicNameValuePair("grpID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("RepeatDateTime", list_datetime.toString().replace("[", "").replace("]", "")));

        /*if(cb_noti_nonsmart.isChecked()){
            arrayList.add(new BasicNameValuePair("sendSMSNonSmartPh", "1"));
        }else {
            arrayList.add(new BasicNameValuePair("sendSMSNonSmartPh", "0"));
        }*/

        /*if(cb_noti_all.isChecked()){
            arrayList.add(new BasicNameValuePair("sendSMSAll", "1"));
        }else {
            arrayList.add(new BasicNameValuePair("sendSMSAll", "0"));
        }*/

        if(cb_send_sms.isChecked()){
            arrayList.add(new BasicNameValuePair("sendSMSAll", "1"));
        } else {
            arrayList.add(new BasicNameValuePair("sendSMSAll", "0"));
        }

    //  arrayList.add(new BasicNameValuePair("sendSMSAll", sendSMSAll));

        arrayList.add(new BasicNameValuePair("questionType", questionType));

        arrayList.add(new BasicNameValuePair("questionText", tv_question.getText().toString()));

        arrayList.add(new BasicNameValuePair("option1", tv_answer1.getText().toString()));
        arrayList.add(new BasicNameValuePair("option2", tv_answer2.getText().toString()));

        // iv_enableRSVP -> Tag -> contains 0 or 1. 0 Means RSVP
        arrayList.add(new BasicNameValuePair("rsvpEnable", iv_enableRSVP.getTag().toString()));

        if(cb_display.isChecked()){
            arrayList.add(new BasicNameValuePair("displayonbanner","1"));
        } else {
            arrayList.add(new BasicNameValuePair("displayonbanner","0"));
        }

        arrayList.add(new BasicNameValuePair("reglink",et_link.getText().toString()));

        //Log.d("@@@@@@@@@@@@@@@@@@@@@@@","################### :- "+list_datetime.toString().replace("[","").replace("]",""));
        flag_callwebsercie = "0";

        Log.d("Response", "PARAMETERS " + Constant.AddEvent + " :- " + arrayList.toString());

        new WebConnectionAsyncLogin(Constant.AddEvent, arrayList, AddEvent.this,false).execute();
    }


    private void webservicesForSendSMS(boolean isFromAddUpdate) {

        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        arrayList.add(new BasicNameValuePair("p_RecipientType", eventType));
        arrayList.add(new BasicNameValuePair("p_InputIDs", inputids));
        arrayList.add(new BasicNameValuePair("P_fk_Group_masterid", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("p_DescriptionCount", ""+et_eventDesc.getText().toString().length()));
        arrayList.add(new BasicNameValuePair("p_TransType", "E"));

        String flag = "1";

        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

            try {

                if (DateHelper.compareDate(publishDate, currentDate) <=0) {
                     flag = "0";
                }

            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(context, "Dates are not in proper format", Toast.LENGTH_LONG).show();
            }


        arrayList.add(new BasicNameValuePair("p_PublishDateFlag",flag));

         int reminderCount = 0;

         // check reminder date whether it is next or previous date
         for(AddEventDateTimeData dateTimeData : list_datetime){

                String reminderDate = dateTimeData.getDate();
                String reminderTime = dateTimeData.getTime();

                try {

                    if (DateHelper.compareDate(reminderDate + " " + reminderTime, currentDate) > 0) {
                        //Toast.makeText(AddEvent.this, "Please make the Repeat notification Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
                       reminderCount ++;
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
          }

        arrayList.add(new BasicNameValuePair("p_Remindercount", ""+reminderCount));

        flag_callwebsercie = "5";

        Log.d("Response", "PARAMETERS " + Constant.GetSMSCountDetail + " :- " + arrayList.toString());

        new WebConnectionAsyncLogin(Constant.GetSMSCountDetail, arrayList, AddEvent.this,isFromAddUpdate).execute();

    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client2.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "AddEvent Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.kaizen.tmc/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client2, viewAction);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "AddEvent Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.kaizen.tmc/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client2, viewAction);
//        client2.disconnect();
//    }

    public class WebConnectionAsyncLogin extends AsyncTask<String, Object, Object> {
        String val = null;
        //ProgressDialog dialog;
        Context context = null;
        String url = null;
        boolean flag;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(AddEvent.this, R.style.TBProgressBar);

        public WebConnectionAsyncLogin(String url, List<NameValuePair> argList, Context ctx, boolean f) {
            this.url = url;
            this.argList = argList;
            context = ctx;
            this.flag = f;
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

            if (this.progressDialog != null && progressDialog.isShowing()) {

                try {
                    progressDialog.dismiss();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            //Log.d("response","Do post"+ result.toString());
            if (result != "") {

                if (flag_callwebsercie.equals("0")) {
                    getresult(result.toString());
                    count_write_events++;
                } else if (flag_callwebsercie.equals("1")) {
                    getEventDetailsItems(result.toString());
                } else if (flag_callwebsercie.equals("4")) {
                    getresultOfRemovephoto(result.toString());
                } else if(flag_callwebsercie.equalsIgnoreCase("5")){
                    getResultSMSCount(result.toString(),flag);
                }

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getresult(String val) {

        try {

            JSONObject jsonObj = new JSONObject(val);
            JSONObject ActivityResult = jsonObj.getJSONObject("AddEventResult");

            final String status = ActivityResult.getString("status");

            if (status.equals("0")) {

                String msg = ActivityResult.getString("message");

               /* Intent i = new Intent(getApplicationContext(),Events.class);
                startActivity(i);
                finish();*/

                Intent intent = new Intent();
                setResult(1, intent);
                hideKeyboard();
                finish();//finishing activity

                if(tv_yes.getText().equals("Update")) {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "Event Updated Successfully");
                }else{
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "Event Added Successfully");
                }

                Log.d("Touchbase", "*************** " + status);
                Log.d("Touchbase", "*************** " + msg);

            } else {
               // showErrorDialog();
                Toast.makeText(context, getString(R.string.msgRetry), Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }

    private void getResultSMSCount(String val,boolean flag){

        try {

            JSONObject jsonObj = new JSONObject(val);

            JSONObject result = jsonObj.getJSONObject("TBsmscountResult");

            String status = result.getString("status");

            if (status.equals("0")) {

                String smsResult = result.getString("smsResult");
                String smsPerUser = result.getString("smsperuser");

                if(smsResult.equalsIgnoreCase("")){

                    if(flag){
                        webservices();
                    } else {
                        sendSMSAll = "1";
                        sendSMSNonSmartPh = "0";
                        cb_send_sms.setChecked(true);
                    }

                } else {
                    cb_send_sms.setChecked(false);
                    popup_waring_send_sms(smsResult);
                }

                tv_sms_per_user.setText("No. of messages per user : "+smsPerUser);

                tv_sms_per_user.setVisibility(View.VISIBLE);

            } else {
                // showErrorDialog();
                Toast.makeText(context, getString(R.string.msgRetry), Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }

    private void   getEventDetailsItems(String result) {

        try {

            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("EventsListDetailResult");
            final String status = EventResult.getString("status");

            if (status.equals("0")) {

                JSONArray EventListResdult = EventResult.getJSONArray("EventsDetailResult");

                for (int i = 0; i < EventListResdult.length(); i++) {

                    JSONObject object = EventListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("EventsDetail");

                    et_evetTitle.setText(objects.getString("eventTitle").toString());
                    et_eventDesc.setText(objects.getString("eventDesc").toString());
                    eventID = objects.getString("eventID").toString();
                    et_eventVenue.setText(objects.getString("venue").toString());

                    String link=objects.getString("reglink");
                    if(link!=null && !link.isEmpty()){
                        et_link.setText(link);
                    }else {
                        et_link.setText("");

                    }

                    if(objects.getString("displayonbanner").equals("1")){
                        cb_display.setChecked(true);
                    }else {
                        cb_display.setChecked(false);
                    }

//----------------------------------toggle Question--------------------------

                    questionType = objects.getString("questionType").toString();
                    que = objects.getString("questionText").toString();
                    opt1 = objects.getString("option1").toString();
                    opt2 = objects.getString("option2").toString();


                    if (que.equals("") || que.equals(null)) {
                        iv_add_que_toggle.setImageResource(R.drawable.off_toggle_btn);
                        // linear_question.setVisibility(View.GONE);
                        edit.setVisibility(View.GONE);

                    } else {
                        iv_add_que_toggle.setImageResource(R.drawable.on_toggle_btn);
                        linear_question.setVisibility(View.VISIBLE);
                        tv_question.setText(objects.getString("questionText").toString());
                        tv_answer1.setText(objects.getString("option1").toString());
                        tv_answer2.setText(objects.getString("option2").toString());


                        if (!(que.equals("") || que.equals(null))) {
                            edit.setVisibility(View.VISIBLE);
                            edit.setOnClickListener(new View.OnClickListener() {


                                @Override
                                public void onClick(View v) {
                                    if (opt1.equals("") || opt1.equals(null)) {

                                        openEndedPopup();

                                    } else {
                                        singleSelectPopup();
                                    }

                                }
                            });
                        }
                    }

                    //----------------------SMS FLAG
                    /*if (objects.getString("sendSMSNonSmartPh").toString().equals("0")) {
                        cb_noti_nonsmart.setChecked(false);
                    } else {
                        cb_noti_nonsmart.setChecked(true);
                    }*/

                    /*if (objects.getString("sendSMSAll").toString().equals("0")) {
                        cb_noti_all.setChecked(false);
                    } else {
                        cb_noti_all.setChecked(true);
                    }*/

                    if (objects.getString("sendSMSAll").equals("0")) {
                        cb_send_sms.setChecked(false);
                    } else {
                        cb_send_sms.setChecked(true);
                    }

                    //---------------------- SMS FLAG

                    // -------------------- DATE TIME
                  //  String[] separated_publish = objects.getString("pubDate").toString().split(" ");
                  //  tv_publishDate.setText(separated_publish[0]);
                    tv_publishTime.setText(objects.getString("pubDate").toString());
                    publishDate=objects.getString("pubDate").toString();
                    String publishDateTime = objects.getString("pubDate").toString();

                    Date cd = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String currentDate = sdf.format(cd);

                    Log.e("TouchBase", "♦♦♦♦Current Date : " + currentDate);
                    if ( DateHelper.compareDate(publishDateTime, currentDate) <= 0 ) {
                        isPublish=true;
                        //rsvpPanel.setVisibility(View.GONE);
                    } else {
                        isPublish=false;
                        //rsvpPanel.setVisibility(View.VISIBLE);
                    }

                    String[] separated_expiryDate = objects.getString("expiryDate").toString().split(" ");
                  //  tv_expiryDate.setText(separated_expiryDate[0]);
                    tv_expiryTime.setText(objects.getString("expiryDate").toString());
                    expiryDate=objects.getString("expiryDate").toString();

                   // String[] separated_eventDate = objects.getString("eventDate").toString().split(" ");

//                    tv_eventDate.setText(separated_eventDate[0]);
                    tv_eventTime.setText(objects.getString("eventDate").toString());
                    eventDate=objects.getString("eventDate").toString();
                    String eventDateTime = objects.getString("eventDate").toString();

                    if(DateHelper.compareDate(eventDateTime,currentDate)<=0){
                        tv_eventDate.setClickable(true);
                        tv_eventTime.setClickable(true);
                        setEventDateDisable = true;
                    }

                    if(DateHelper.compareDate(publishDateTime,currentDate)<=0){
//                        tv_publishDate.setClickable(false);
//                        tv_publishTime.setClickable(false);
//                        setPublishDateDisable = true;
                    }


                    // Enable/disable RSVP
                    if ( objects.has("rsvpEnable")){
                        String rsvpStatus = objects.getString("rsvpEnable");
                        Log.e("RSVP", "We are here : RSVP : "+rsvpStatus);

                        iv_enableRSVP.setTag(rsvpStatus);
                        if (rsvpStatus.equals("0")) {  // enabled
                            iv_enableRSVP.setTag("0");
                            iv_enableRSVP.setImageResource(R.drawable.off_toggle_btn);
                            addQuestionWrapper.setVisibility(View.GONE);

                        } else {   // disabled
                            iv_enableRSVP.setTag("1");
                            iv_enableRSVP.setImageResource(R.drawable.on_toggle_btn);
                            addQuestionWrapper.setVisibility(View.VISIBLE);
                        }
                    }



                    // -------------------- DATE TIME

                    //------------------ REPEATE DATE TIME
                    String repeateDates = objects.getString("repeatDateTime").toString();
                    Log.e("RepeateDates", "Value of repeate dates is : "+repeateDates);
                    if (repeateDates.equals("")) {
                        Log.e("NoRepeateDate", "No repeate date and time is set for reminder");
                        iv_toggle.setImageResource(R.drawable.off_toggle_btn);
                    } else {
                        String[] repeate_eventDateTime = repeateDates.split(",");
                        //iv_toggle.setImageDrawable(getResources().getDrawable(R.drawable.));
                        Log.d("TOCUHBASE", "SIZE :- " + objects.getString("repeatDateTime"));
                        iv_toggle.setImageResource(R.drawable.on_toggle_btn);
                        Log.e("TOUCHBASE", "Set the new icon");

                        if (repeateDates != null && repeateDates.length() > 0) {

                            for (String part : repeate_eventDateTime) {

                                toggle_flag_onoff = "0";

                                Log.d("TOCUHBASE", "@@@@@@@@@@@@@@ :- " + part);

                                String[] separated_repeatedateDateTime = part.split(" ");

                                AddEventDateTimeData data = new AddEventDateTimeData();
                                data.setDate(separated_repeatedateDateTime[0]);
                                data.setTime(separated_repeatedateDateTime[1]);
                                list_datetime.add(data);
                                linear_repeatnotification.setVisibility(View.VISIBLE);
                                setListViewHeightBasedOnChildren(listView);

                                adapter_datetime = new AddEventDateTimeListAdapter(AddEvent.this, R.layout.add_event_datetime_list_item, list_datetime);

                                listView.setAdapter(adapter_datetime);

                            }
                        }
                    }
                    //------------------ REPEATE DATE TIME
                    // -------------- RADIO BUTTONS
                    // RADIO BUTTONS FOR TYPE

                    if (objects.getString("eventType").toString().equals("0")) {
                        eventType = "0";
                        d_radio0.setChecked(true);
                        clearselectedtext();
                    } else if (objects.getString("eventType").toString().equals("1")) {

                        d_radio0.setChecked(false);
                        d_radio1.setChecked(true);
                        d_radio2.setChecked(false);

                        d_radio0.setEnabled(true);
                        d_radio1.setEnabled(false);
                        d_radio2.setEnabled(true);

                        Log.d("Touchnase", "IN subgroup ");

                        eventType = "1";
                        //   tv_getCount.setText("You have added " + " sub groups");
                        iv_edit.setVisibility(View.VISIBLE);

                        edit_event_selectedids = objects.getString("inputIds");

                        String[] ids = edit_event_selectedids.split(",");
                        selectedsubgrp = new ArrayList<String>();

                        for (String id: ids) {
                            selectedsubgrp.add(id);
                        }

                        inputids = objects.getString("inputIds");

                        Log.d("Touchnase", "IN subgroup input id=>"+inputids);

                        String[] mystring = edit_event_selectedids.split(",");

                        int size = mystring.length;

                        getCount.setText("You have added " + size + " sub groups");

                    } else if (objects.getString("eventType").toString().equals("2")) {

                        d_radio0.setChecked(false);
                        d_radio1.setChecked(false);
                        d_radio2.setChecked(true);

                        d_radio0.setEnabled(true);
                        d_radio1.setEnabled(true);
                        d_radio2.setEnabled(false);

                        Log.d("Touchnase", "IN MEMBER ");

                        eventType = "2";

                        // tv_getCount.setText("You have added " +  " sub groups");

                        iv_edit.setVisibility(View.VISIBLE);

                        edit_event_selectedids = objects.getString("inputIds").toString();
                        inputids = objects.getString("inputIds").toString();
                        String[] mystring = edit_event_selectedids.split(",");
                        int size = mystring.length;

                        getCount.setText("You have added " + size + " members");
                    }

                    //-------------- RADIO BUTTONS

                    if (objects.has("eventImg")) {

                        String a = objects.getString("eventImg").toString();

                        if (objects.getString("eventImg").toString().equals("") || objects.getString("eventImg").toString() == null) {
                            // linear_image.setVisibility(View.GONE);
                        } else {
                            //  imageurl = objects.getString("eventImg").toString();
                            //progressbar.setVisibility(View.VISIBLE);
                            hasimageflag = "1";

                            Picasso.with(AddEvent.this).load(objects.getString("eventImg").toString())
                                    .placeholder(R.drawable.edit_image)
//                                    .resize(400,400)
                                    .into(iv_event_photo, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            //  progressbar.setVisibility(View.GONE);
                                        }
                                        @Override
                                        public void onError() {

                                        }
                                    });
                        }
                    }
                }
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    private void getresultOfRemovephoto(String result) {

        try {

            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("DeleteResult");
            final String status = EventResult.getString("status");

            if (status.equals("0")) {
                //Intent i = new Intent(Announcement_details.this,Announcement.class);
                //startActivityForResult(i,1);
                iv_event_photo.setImageResource(R.drawable.edit_image);
                hasimageflag = "0";

            } else {
                Utils.showToastWithTitleAndContext(getApplicationContext(), "Image Remove failed, Please try Again!");
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Member Select
        if (requestCode == 3) {

            if (resultCode == Activity.RESULT_OK) {

                listaddmemberdata = data.getParcelableArrayListExtra("result");
                String result = "";
                int count = 0;
                ArrayList<String> selectedmemberid = new ArrayList<>();

                selectedmemberid.clear();
                inputids = "";

                for (DirectoryData d : listaddmemberdata) {

                    if (d.isBox()) {
                        result = result + d.getProfileID();
                        count = count + 1;
                        selectedmemberid.add(d.getProfileID());
                    }

                    //something here
                }

                for (int i = 0; i < selectedmemberid.size(); i++) {

                    //commaSepValueBuilder.append(n.get(i));
                    inputids = inputids + selectedmemberid.get(i);

                    if (i != selectedmemberid.size() - 1) {
                        // commaSepValueBuilder.append(", ");
                        inputids = inputids + ", ";
                    }

                }

                //inputids = inputids + selectedmemberid.get(selectedmemberid.size() - 1);
                d_radio0.setChecked(false);
                d_radio1.setChecked(false);
                d_radio2.setChecked(true);

                d_radio0.setEnabled(true);
                d_radio1.setEnabled(true);
                d_radio2.setEnabled(false);

                eventType = "2";
                Log.d("Touchnase", "Arrat " + inputids);
                Log.d("----------", "------" + inputids);
                getCount.setText("You have added " + count + " members");
                iv_edit.setVisibility(View.VISIBLE);
            }

        }

        // SubGroup Select

        if (requestCode == 1) {

            if (resultCode == Activity.RESULT_OK) {

                listaddsubgrp = data.getParcelableArrayListExtra("result");
                if (listaddsubgrp.size() == 0)
                    Log.d("Touchnase", "@@@ " + listaddsubgrp);
                String result = "";
                int count = 0;
                selectedsubgrp = new ArrayList<>();


                selectedsubgrp.clear();
                selectedsubgrp = data.getStringArrayListExtra("result");
                count = selectedsubgrp.size();

                inputids = "";


                for (int i = 0; i < selectedsubgrp.size(); i++) {
                    //commaSepValueBuilder.append(n.get(i));
                    inputids = inputids + selectedsubgrp.get(i);

                    if (i != selectedsubgrp.size() - 1) {
                        // commaSepValueBuilder.append(", ");
                        inputids = inputids + ", ";

                    }
                }

                d_radio0.setChecked(false);
                d_radio1.setChecked(true);
                d_radio2.setChecked(false);

                d_radio0.setEnabled(true);
                d_radio1.setEnabled(false);
                d_radio2.setEnabled(true);

                Log.d("TouchBase", "Array " + inputids);
                eventType = "1";
                getCount.setText("You have added " + count + " sub groups");
                iv_edit.setVisibility(View.VISIBLE);
            }
        }

        /***************Image Capture***********/

        if (requestCode == 4) {

            if (resultCode == Activity.RESULT_OK) {

                flag = 0;

                Uri correctedUri = null;

                try {

                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mCapturedImageURI);
                    //imageBitmap = imageOrientationValidator(imageBitmap, getRealPathFromURI(mCapturedImageURI));
                    correctedUri = Utils.getImageUri(imageBitmap, getApplicationContext());

                    Log.d("==== Uri ===", "======" + correctedUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                beginCrop(correctedUri);
            }

        } if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK)  {
            
            if (resultCode == Activity.RESULT_OK) {
                flag=1;
                beginCrop(data.getData());
            }

        }else if(requestCode == Crop.REQUEST_CROP){

            if(resultCode==RESULT_OK){
                uploadPic(data);
            }else {
               // Toast.makeText(this, Crop.getError(data).getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == 5) {

            if (data != null) {
                String result = data.getStringExtra("address");
                et_eventVenue.setText(result);
            }
        }
    }

    private void beginCrop(Uri source) {

        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void cameraImage(){
        File f = new File(Environment.getExternalStorageDirectory().toString());
        for (File temp : f.listFiles()) {
            if (temp.getName().equals("temp.jpg")) {
                f = temp;
                break;
            }
        }
        try {
            Bitmap bitmap;
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

            bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                    bitmapOptions);
            Bitmap bt = Bitmap.createScaledBitmap(bitmap, 400, 400, false);

            //photo_image.setImageBitmap(bt)

            iv_event_photo.setImageBitmap(bt);

            String path = Environment
                    .getExternalStorageDirectory()
                    + File.separator
                    + "Phoenix" + File.separator + "default";
            // f.delete();
            OutputStream outFile = null;
            File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

            Log.d("TOUCHBASE", "FILE PATH " + f.toString());

            ///-------------------------------------------------------------------
            pd = ProgressDialog.show(AddEvent.this, "", "Uploading...", false);
            final File finalF = f;
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    uploadedimgid = Utils.doFileUpload(new File(finalF.toString()), "event"); // Upload File to server
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (pd.isShowing())
                                pd.dismiss();
                            Log.d("TOUCHBASE", "FILE UPLOAD ID  " + uploadedimgid);
                            if (uploadedimgid.equals("0")) {
                                Toast.makeText(context, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();
                                iv_event_photo.setImageResource(R.drawable.edit_image);
                            }
                            //Toast.makeText(Register.this, "Verify your account through the registered email id", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            thread.start();
            ///-------------------------------------------------------------------
            // uploadedimgid = Utils.doFileUpload(new File(f.toString()), "event"); // Upload File to server

            try {
                outFile = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                outFile.flush();
                outFile.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void pickerImage(Intent data){

        ImageCompression imageCompression = new ImageCompression();
        String path = "";
        
        Uri selectedImage = data.getData();
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePath[0]);
        final String picturePath = c.getString(columnIndex);
        c.close();
        final Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
        Log.d("TOUCHBASE", "BEFORE UPLOAD");

        //-----------Image with reduced size -------------------------------------


        path = Utils.getRealPathFromURI(selectedImage, getApplicationContext());
        Log.d("==== Path ===", "======" + path);

        imageCompression = new ImageCompression();
        finalImagePath = imageCompression.compressImage(path, getApplicationContext());
        Log.d("==picturePath====","0000...."+finalImagePath);


        ///-------------------------------------------------------------------
        pd = ProgressDialog.show(AddEvent.this, "", "Loading...", false);
        Thread thread = new Thread(new Runnable() {
            public void run() {

                uploadedimgid = Utils.doFileUpload(new File(finalImagePath), "event"); // Upload File to server
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (pd.isShowing())
                            pd.dismiss();
                        Log.d("TOUCHBASE", "FILE UPLOAD ID  " + uploadedimgid);
                        if (uploadedimgid.equals("0")) {
                            Toast.makeText(context, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();
                            iv_event_photo.setImageResource(R.drawable.edit_image);
                        } else {
                            //iv_event_photo.setImageBitmap(thumbnail);

                            iv_event_photo.setImageDrawable(Drawable.createFromPath(finalImagePath));
                        }
                        //Toast.makeText(Register.this, "Verify your account through the registered email id", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        thread.start();
        ///-------------------------------------------------------------------


        //uploadedimgid = Utils.doFileUpload(new File(picturePath),"event"); // Upload File to server


        //iv_event_photo.setImageBitmap(thumbnail);
    }
    
    private void uploadPic(Intent result){
        Uri croppedUri = Crop.getOutput(result);

        if (croppedUri != null) {

            Bitmap csBitmap = null;
            ImageCompression imageCompression = new ImageCompression();
            String path = "";
            try {
                csBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), croppedUri);
                path = Utils.getRealPathFromURI(croppedUri, getApplicationContext());
                Log.d("==== Path ===", "======" + path);

                imageCompression = new ImageCompression();
                finalImagePath = imageCompression.compressImage(path, getApplicationContext());
//                    picturePath = imageCompression.getImage(path, getApplicationContext());

                Log.d("==picturePath====", "0000...." + finalImagePath);
                final ProgressDialog pd = ProgressDialog.show(AddEvent.this, "", "Uploading...", false);

                Thread thread = new Thread(new Runnable() {
                    public void run() {

                        uploadedimgid = Utils.doFileUpload(new File(finalImagePath), "event"); // Upload File to server
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (pd.isShowing())
                                    pd.dismiss();
                                Log.d("TOUCHBASE", "FILE UPLOAD ID  " + uploadedimgid);
                                if (uploadedimgid.equals("0")) {
                                    Toast.makeText(context, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();
                                    iv_event_photo.setImageResource(R.drawable.edit_image);
                                } else {
                                    //iv_event_photo.setImageBitmap(thumbnail);

                                    iv_event_photo.setImageDrawable(Drawable.createFromPath(finalImagePath));
                                }
                                //Toast.makeText(Register.this, "Verify your account through the registered email id", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                thread.start();


            } catch (IOException e) {
                e.printStackTrace();
            }


            //  ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // csBitmap = Bitmap.createScaledBitmap(csBitmap, 500, 500, true);
            //  csBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            //   iv_profileimage.setImageBitmap(csBitmap);

            // picturePath = imageCompression.compressImage(path, getApplicationContext());
            //ivNewProfileImage.setImageDrawable(Drawable.createFromPath(picturePath));
            //Utils.log(picturePath);



        }
    }
    /********
     * Page Scrollable with listView
     **********/

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);

            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, GridLayout.LayoutParams.MATCH_PARENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + ((listView.getDividerHeight()) * (listAdapter.getCount()));

        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    public void datepicker(final TextView setdatetext) {
        // Get Current Date
        int mYear, mMonth, mDay, mHour, mMinute;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        setdatetext.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }


    public void ShowTimePicker(final TextView tv_time, final int module) {

        // 1 for event, 2 for publish, 3 for expire, 4 for notification
//        final MyTimePicker myTimePicker = new MyTimePicker(this);
//
//        myTimePicker.show();
//        myTimePicker.setTimeListener(new MyTimePicker.onTimeSet() {
//
//            @Override
//            public void onTime(TimePicker view, int hourOfDay, int minute) {
//              /*  Toast.makeText(AddEvent.this, "time is " + hourOfDay + ":" + minute, Toast.LENGTH_LONG).show();*/
//
//
//
//                tv_time.setText(utilTime(hourOfDay) + ":" + utilTime(minute));
//            }
//        });

        String title="";
        Date date=new Date();

        try {
            if(module==1){
                title="Event Date & Time";
               date = sdf1.parse(eventDate);
            }else if(module==2){
                title="Publish Date & Time";
                date = sdf1.parse(publishDate);
            }else if(module==3){
                title="Expiry Date & Time";
                date = sdf1.parse(expiryDate);
            }else if(module==4){
                title="Notification Date & Time";
            }
        }
        catch (ParseException e){
            date=new Date();
        }

//

       datetimePicker= new SingleDateAndTimePickerDialog.Builder(AddEvent.this).build();
//                .bottomSheet()
//                .curved()
        datetimePicker.setDefaultDate(date);

       // datetimePicker.setMinDateRange(new Date());
        datetimePicker .setMinutesStep(1);
                //.displayHours(false)
                //.displayMinutes(false)

                //.todayText("aujourd'hui")

        datetimePicker.setTitle(title);
        datetimePicker.setListener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {

                        tv_time.setText(sdf1.format(date));
                        if(module==1){
                            eventDate=sdf1.format(date);
                        }else if(module==2){
                            publishDate=sdf1.format(date);
                        }else if(module==3){
                            expiryDate=sdf1.format(date);
                        }else if(module==4){
//                            title="Notification Date & Time";
                        }
                    }
                }).display();
    }

    public void datepicker_repeate_notification() {
        // Get Current Date
        int mYear, mMonth, mDay, mHour, mMinute;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        repeat_date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        //  setdatetext.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        ShowTimePicker_repeate_notification();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void ShowTimePicker_repeate_notification() {
//        MyTimePicker myTimePicker = new MyTimePicker(this);
//        myTimePicker.show();
//        myTimePicker.setTimeListener(new MyTimePicker.onTimeSet() {
//
//            @Override
//            public void onTime(TimePicker view, int hourOfDay, int minute) {
//                //Toast.makeText(AddEvent.this,"time is " + hourOfDay + ":" + minute, Toast.LENGTH_LONG).show();
//
//                // time_text.setText(utilTime(hourOfDay) + ":" + utilTime(minute));
//                repeat_time = utilTime(hourOfDay) + ":" + utilTime(minute);
//                repeate_notification_valueadd();
//            }
//        });


        datetimeForReminder= new SingleDateAndTimePickerDialog.Builder(AddEvent.this).build();
//                .bottomSheet()
//                .curved()
        datetimeForReminder.setDefaultDate(new Date());

        datetimeForReminder.setMinDateRange(new Date());
        datetimeForReminder .setMinutesStep(1);
        //.displayHours(false)
        //.displayMinutes(false)

        //.todayText("aujourd'hui")

        datetimeForReminder.setTitle("Reminder Date & Time");

        datetimeForReminder.setListener(new SingleDateAndTimePickerDialog.Listener() {

            @Override
            public void onDateSelected(Date date) {

                String s=sdf1.format(date);
                String dateArray[]=s.split(" ");
                repeat_date=dateArray[0];
                repeat_time=dateArray[1];
                repeate_notification_valueadd();
            }

        }).display();
    }

    private void repeate_notification_valueadd() {

        AddEventDateTimeData data = new AddEventDateTimeData();
        data.setDate(repeat_date);
        data.setTime(repeat_time);

//        Toast.makeText(AddEvent.this, "Reminder added Successfully!", Toast.LENGTH_LONG).show();
//        list_datetime.add(data);
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        if (repeatenotification_validation()) {
            Toast.makeText(context, "Reminder added Successfully!", Toast.LENGTH_LONG).show();
            list_datetime.add(data);
            scrollview.post(new Runnable() {
                @Override
                public void run() {
                    scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }

        setListViewHeightBasedOnChildren(listView);

        adapter_datetime = new AddEventDateTimeListAdapter(AddEvent.this, R.layout.add_event_datetime_list_item, list_datetime);
        listView.setAdapter(adapter_datetime);

    }

    private static String utilTime(int value) {

        if (value < 10)
            return "0" + String.valueOf(value);
        else
            return String.valueOf(value);
    }


    private boolean validationForSendSMS(){

        String desc = et_eventDesc.getText().toString().trim();

        if (desc.equalsIgnoreCase("") ) {
            Toast.makeText(context, "Please enter the event “Description”", Toast.LENGTH_LONG).show();
            cb_send_sms.setChecked(false);
            return false;
        }else if(desc.length()>2000){
            Toast.makeText(context, " “Description” cannot be more than 2,000 characters for SMS", Toast.LENGTH_LONG).show();
            cb_send_sms.setChecked(false);
            return false;
        }else if(publishDate.equalsIgnoreCase("")){
            Toast.makeText(context, "Please enter a Publish Date & Time", Toast.LENGTH_LONG).show();
            cb_send_sms.setChecked(false);
            return false;
        }

        return true;
    }

    public boolean repeatenotification_validation() {

       // String publishDateTime = tv_publishDate.getText().toString() + " " + tv_publishTime.getText().toString();
        try {
            // Checking if repeat date time is less than public date and time
            if ( DateHelper.compareDate(repeat_date + " " + repeat_time, publishDate) == 0) {
                Toast.makeText(context, "Repeat Notification Date & Time should not be same as Publish Date & Time", Toast.LENGTH_LONG).show();
                return false;
            }

            if ( DateHelper.compareDate(repeat_date + " " + repeat_time, publishDate) <= 0) {
                Toast.makeText(context, "Please make the Repeat notification Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(context, "Please enter a Publish Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }

      //  String expiryDateTime = tv_expiryDate.getText().toString() + " " + tv_expiryTime.getText().toString();
        try {
            // Checking if repeat date time is greater than expiry date and time
            if ( DateHelper.compareDate(repeat_date + " " + repeat_time , expiryDate) > 0 ) {
                Toast.makeText(context, "Please make the Repeat notification Date & Time less than Expiry Date & Time", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(context, "Please enter an Expiry Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public boolean validation() {

        if (et_evetTitle.getText().toString().trim().matches("") || et_evetTitle.getText().toString().trim() == null) {
            Toast.makeText(context, "Please enter the event “Title”", Toast.LENGTH_LONG).show();
            return false;
        }
        if (et_eventDesc.getText().toString().trim().matches("") || et_eventDesc.getText().toString().trim() == null) {
            Toast.makeText(context, "Please enter the event “Description”", Toast.LENGTH_LONG).show();
            return false;
        }
        if (et_eventVenue.getText().toString().trim().matches("") || et_eventVenue.getText().toString().trim() == null) {
            Toast.makeText(context, "Please enter the event “Venue”", Toast.LENGTH_LONG).show();
            return false;
        }
//        if (tv_eventDate.getText().toString().trim().matches("") || tv_eventDate.getText().toString().trim() == null) {
//            Toast.makeText(AddEvent.this, "Please Enter an Event Date", Toast.LENGTH_LONG).show();
//            return false;
//        }
        if (tv_eventTime.getText().toString().trim().matches("") || tv_eventTime.getText().toString().trim() == null) {
            Toast.makeText(context, "Please Enter an Event Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }
//        if (tv_publishDate.getText().toString().trim().matches("") || tv_publishDate.getText().toString().trim() == null) {
//            Toast.makeText(AddEvent.this, "Please enter a Publish Date", Toast.LENGTH_LONG).show();
//            return false;
//        }
        if (tv_publishTime.getText().toString().trim().matches("") || tv_publishTime.getText().toString().trim() == null) {
            Toast.makeText(context, "Please enter a Publish Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }
//        if (tv_expiryDate.getText().toString().trim().matches("") || tv_expiryDate.getText().toString().trim() == null) {
//            Toast.makeText(AddEvent.this, "Please enter an Expiry Date", Toast.LENGTH_LONG).show();
//            return false;
//        }
        if (tv_expiryTime.getText().toString().trim().matches("") || tv_expiryTime.getText().toString().trim() == null) {
            Toast.makeText(context, "Please enter an Expiry Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }

//        String expiryDate = tv_expiryDate.getText().toString() + " " + tv_expiryTime.getText().toString();
//        String eventDate = tv_eventDate.getText().toString() + " " + tv_eventTime.getText().toString();
//
//        String publishDate = tv_publishDate.getText().toString() + " " + tv_publishTime.getText().toString();

        try {

            if ( DateHelper.compareDate(publishDate, eventDate) >= 0 ) {
                Toast.makeText(context, "Publish Date & Time should be less than the Event Date & Time", Toast.LENGTH_LONG).show();
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(context, "Dates are not in proper format", Toast.LENGTH_LONG).show();
            return false;
        }

        try {

            if (DateHelper.compareDate(expiryDate, eventDate) <= 0 ) {
                // Toast.makeText(AddEvent.this, "Please make the Expiry Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
                Toast.makeText(context, "Please make the Expiry Date & Time greater than the Event Date & Time", Toast.LENGTH_LONG).show();
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(context, "Dates are not in proper format", Toast.LENGTH_LONG).show();
            return false;
        }

        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

        try {

            if (DateHelper.compareDate(eventDate, currentDate) <= 0 ) {
                // Toast.makeText(AddEvent.this, "Please make the Expiry Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
                Toast.makeText(context, "Please make the event date & time greater than the current date & time", Toast.LENGTH_LONG).show();
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(context, "Dates are not in proper format", Toast.LENGTH_LONG).show();
        }

//        try {
//            if (DateHelper.compareDate(publishDate, currentDate) <= 0 ) {
//                // Toast.makeText(AddEvent.this, "Please make the Expiry Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
//                Toast.makeText(AddEvent.this, "Please make the publish date & time greater than the current date & time", Toast.LENGTH_LONG).show();
//                return false;
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//            Toast.makeText(AddEvent.this, "Dates are not in proper format", Toast.LENGTH_LONG).show();
//        }
        //new SimpleDateFormat("")

        return true;
    }

    public boolean validationForReminder() {

//        if (tv_eventDate.getText().toString().trim().matches("") || tv_eventDate.getText().toString().trim() == null) {
//            Toast.makeText(context, "Please Enter an Event Date", Toast.LENGTH_LONG).show();
//            return false;
//        }
        if (tv_eventTime.getText().toString().trim().matches("") || tv_eventTime.getText().toString().trim() == null) {
            Toast.makeText(context, "Please Enter an Event Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }
//        if (tv_publishDate.getText().toString().trim().matches("") || tv_publishDate.getText().toString().trim() == null) {
//            Toast.makeText(context, "Please enter a Publish Date", Toast.LENGTH_LONG).show();
//            return false;
//        }
        if (tv_publishTime.getText().toString().trim().matches("") || tv_publishTime.getText().toString().trim() == null) {
            Toast.makeText(context, "Please enter a Publish Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }
//        if (tv_expiryDate.getText().toString().trim().matches("") || tv_expiryDate.getText().toString().trim() == null) {
//            Toast.makeText(context, "Please enter an Expiry Date", Toast.LENGTH_LONG).show();
//            return false;
//        }
        if (tv_expiryTime.getText().toString().trim().matches("") || tv_expiryTime.getText().toString().trim() == null) {
            Toast.makeText(context, "Please enter an Expiry Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }

//        String expiryDate = tv_expiryDate.getText().toString() + " " + tv_expiryTime.getText().toString();
//        String eventDate = tv_eventDate.getText().toString() + " " + tv_eventTime.getText().toString();
//
//        String publishDate = tv_publishDate.getText().toString() + " " + tv_publishTime.getText().toString();

        try {
            if ( DateHelper.compareDate(publishDate, eventDate) >= 0 ) {
                Toast.makeText(context, "Publish Date & Time should be less than the Event Date & Time", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(context, "Dates are not in proper format", Toast.LENGTH_LONG).show();
            return false;
        }

        try {
            if (DateHelper.compareDate(expiryDate, eventDate) <= 0 ) {
                // Toast.makeText(context, "Please make the Expiry Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
                Toast.makeText(context, "Please make the Expiry Date & Time greater than the Event Date & Time", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(context, "Dates are not in proper format", Toast.LENGTH_LONG).show();
            return false;
        }


        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

        try {
            if (DateHelper.compareDate(eventDate, currentDate) <= 0 ) {
                // Toast.makeText(context, "Please make the Expiry Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
                Toast.makeText(context, "Please make the event date & time greater than the current date & time", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(context, "Dates are not in proper format", Toast.LENGTH_LONG).show();
        }

        try {
            if (DateHelper.compareDate(publishDate, currentDate) <= 0 ) {
                // Toast.makeText(context, "Please make the Expiry Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
                Toast.makeText(context, "Please make the publish date & time greater than the current date & time", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(context, "Dates are not in proper format", Toast.LENGTH_LONG).show();
        }
        //new SimpleDateFormat("")

        return true;
    }



//    public boolean valid(int i) {
//        switch (i) {
//            case 0:
//                if (tv_eventDate.getText().toString().isEmpty()) {
//                    Toast.makeText(AddEvent.this, "Please Enter an Event Date", Toast.LENGTH_LONG).show();
//                    return false;
//                } else {
//                    return true;
//                }
//
//            case 1:
//                if (tv_eventDate.getText().toString().isEmpty() || tv_eventTime.getText().toString().isEmpty()) {
//                    Toast.makeText(AddEvent.this, "Please Enter an Event Date & Time", Toast.LENGTH_LONG).show();
//                    //compare_date(tv_eventDate.getText().toString()+" "+tv_eventTime.getText().toString().isEmpty(), tv_publishDate.getText().toString()+" "+ tv_publishTime.getText().toString())
//                    return false;
//                } else {
//                    return true;
//                }
//
//            default:
//
//
//        }
//
//        return true;
//    }


    public String compare_date(String dateone, String datetwo) {
        String value = "";
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date date1 = sdf.parse(dateone);
            Date date2 = sdf.parse(datetwo);

            System.out.println(sdf.format(date1));
            System.out.println(sdf.format(date2));

            if (date1.compareTo(date2) > 0) {
                System.out.println("Date1 is after Date2");
                value = "1";
            } else if (date1.compareTo(date2) < 0) {
                System.out.println("Date1 is before Date2");
                value = "2";
            } else if (date1.compareTo(date2) == 0) {
                System.out.println("Date1 is equal to Date2");
                value = "3";
            } else {
                System.out.println("How to get here?");
                value = "0";
            }

        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return value;
    }


    public static JSONObject getLocationInfo(String address) {
        StringBuilder stringBuilder = new StringBuilder();
        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            address = address.replaceAll("(\\r|\\n)", "");
            address = URLEncoder.encode(address.trim().replaceAll(" ", "+"), "UTF-8");

            HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            stringBuilder = new StringBuilder();


            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static boolean getLatLong(JSONObject jsonObject) {

        try {
            Log.d("TOUCHBASE ", "JSON OBJECT-" + jsonObject.toString());
            longitute = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            latitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            Log.d("TOUCHBASE ", "LAT-" + latitude + " LONG-" + longitute);
        } catch (JSONException e) {
            return false;

        }

        return true;
    }

    @Override
    public void onBackPressed() {

        if(datetimePicker!=null && datetimePicker.isDisplaying()){
            datetimePicker.dismiss();
        }
        else if(datetimeForReminder!=null && datetimeForReminder.isDisplaying()){
            datetimeForReminder.dismiss();
        }
        else {
            Utils.popupback(AddEvent.this);

        }
        //
        // super.onBackPressed();
    }

    public void hideKeyboard() {
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public boolean updateValidation(){

        if (et_evetTitle.getText().toString().trim().matches("") || et_evetTitle.getText().toString().trim() == null) {
            Toast.makeText(context, "Please enter the event “Title”", Toast.LENGTH_LONG).show();
            return false;
        }

        if (et_eventDesc.getText().toString().trim().matches("") || et_eventDesc.getText().toString().trim() == null) {
            Toast.makeText(context, "Please enter the event “Description”", Toast.LENGTH_LONG).show();
            return false;
        }
        if (et_eventVenue.getText().toString().trim().matches("") || et_eventVenue.getText().toString().trim() == null) {
            Toast.makeText(context, "Please enter the event “Venue”", Toast.LENGTH_LONG).show();
            return false;
        }
//        if (tv_eventDate.getText().toString().trim().matches("") || tv_eventDate.getText().toString().trim() == null) {
//            Toast.makeText(context, "Please Enter an Event Date", Toast.LENGTH_LONG).show();
//            return false;
//        }
        if (tv_eventTime.getText().toString().trim().matches("") || tv_eventTime.getText().toString().trim() == null) {
            Toast.makeText(context, "Please Enter an Event Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }
//        if (tv_publishDate.getText().toString().trim().matches("") || tv_publishDate.getText().toString().trim() == null) {
//            Toast.makeText(context, "Please enter a Publish Date", Toast.LENGTH_LONG).show();
//            return false;
//        }
        if (tv_publishTime.getText().toString().trim().matches("") || tv_publishTime.getText().toString().trim() == null) {
            Toast.makeText(context, "Please enter a Publish Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }
//        if (tv_expiryDate.getText().toString().trim().matches("") || tv_expiryDate.getText().toString().trim() == null) {
//            Toast.makeText(context, "Please enter an Expiry Date", Toast.LENGTH_LONG).show();
//            return false;
//        }
        if (tv_expiryTime.getText().toString().trim().matches("") || tv_expiryTime.getText().toString().trim() == null) {
            Toast.makeText(context, "Please enter an Expiry Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }

//        String expiryDate = tv_expiryDate.getText().toString() + " " + tv_expiryTime.getText().toString();
//        String eventDate = tv_eventDate.getText().toString() + " " + tv_eventTime.getText().toString();
//        String publishDate = tv_publishDate.getText().toString() + " " + tv_publishTime.getText().toString();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

        if(!setEventDateDisable) {

            try {

                if (DateHelper.compareDate(eventDate, currentDate) <= 0) {
                    // Toast.makeText(context, "Please make the Expiry Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
                    Toast.makeText(context, "Please make the event date & time greater than the current date & time", Toast.LENGTH_LONG).show();
                    return false;
                }

            } catch (ParseException e) {
                e.printStackTrace();

                Toast.makeText(context, "Dates are not in proper format", Toast.LENGTH_LONG).show();
            }
        }

//        try {
//            if ( DateHelper.compareDate(publishDate, eventDate) >= 0 ) {
//                Toast.makeText(context, "Publish Date & Time should be less than the Event Date & Time", Toast.LENGTH_LONG).show();
//                return false;
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//            Toast.makeText(context, "Dates are not in proper format", Toast.LENGTH_LONG).show();
//        }

        try {
            if (DateHelper.compareDate(eventDate, publishDate) <= 0 ) {
                Toast.makeText(context, "Please make the event Date & Time greater than the publsh Date & Time", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(context, "Dates are not in proper format", Toast.LENGTH_LONG).show();
        }

//        if(!setPublishDateDisable) {
//            try {
//                if (DateHelper.compareDate(publishDate, currentDate) <= 0) {
//                    // Toast.makeText(AddEvent.this, "Please make the Expiry Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
//                    Toast.makeText(AddEvent.this, "Please make the publish date & time greater than the current date & time", Toast.LENGTH_LONG).show();
//                    return false;
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//                Toast.makeText(AddEvent.this, "Dates are not in proper format", Toast.LENGTH_LONG).show();
//            }
//        }


        try {
            if (DateHelper.compareDate(expiryDate, eventDate) <= 0 ) {
                // Toast.makeText(AddEvent.this, "Please make the Expiry Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
                Toast.makeText(context, "Please make the Expiry Date & Time greater than the Event Date & Time", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(context, "Dates are not in proper format", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    public void popup_warning() {
        final Dialog dialog = new Dialog(AddEvent.this, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_confrm_delete);
        TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
        TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
        TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);

        tv_yes.setText("OK");
        tv_no.setText("CANCEL");
        tv_line1.setText("RSVP result will be reset to 0");

        tv_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                iv_enableRSVP.setTag("0");
                iv_enableRSVP.setImageResource(R.drawable.off_toggle_btn);
                addQuestionWrapper.setVisibility(View.GONE);

                //iv_toggle.setImageResource(getResources().getDrawable(R.drawable.off_toggle_btn));
                iv_add_que_toggle.setImageResource(R.drawable.off_toggle_btn);
                // linear_question.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                linear_question.setVisibility(View.GONE);
                toggle_que_flag = "0";
                questionType = "0";

                Log.d("---------", "-------" + questionType);

                dialog.dismiss();

                // TODO: 07-05-2018 add code of reset rsvp
            }
        });

        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.show();

    }

    private void popup_sms(){
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setMessage(getString(R.string.sms_warning))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })

                .show();
    }

    private void popup_waring_send_sms(String message){

        final AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })

                .show();
    }
}

