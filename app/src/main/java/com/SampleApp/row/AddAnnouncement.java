package com.SampleApp.row;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import com.SampleApp.row.Adapter.AddAnnouncementDateTimeListAdapter;
import com.SampleApp.row.Data.AddAnnouncementDateTimeData;
import com.SampleApp.row.Data.DirectoryData;
import com.SampleApp.row.Data.SubGoupData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.DateHelper;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.ImageCompression;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.MarshMallowPermission;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.croputility.Crop;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by USER on 15-01-2016.
 */
public class AddAnnouncement extends FragmentActivity {
    ArrayList<String> selectedsubgrp;
    RadioButton d_radio1, d_radio2;
    RadioButton d_radio0;
    TextView tv_getCount, tv_add;
    TextView tv_title, tv_publish_date, tv_expiry_date, tv_expiretime, tv_no;
    ImageView iv_backbutton, iv_edit;
    ImageView call_button, iv_event_photo,iv_toggle;
    public static int count_write_announcement = 0;
    String finalImagePath;
//    CheckBox cb_display;
    DatePicker publish_date1;
    TextView tv_publishtime,smscount;

    EditText et_announce_title, et_announce_desc;
    ArrayList<DirectoryData> listaddmemberdata = new ArrayList<>();
    ArrayList<SubGoupData> listaddsubgrp = new ArrayList<>();
    //String selectedmemberidstr = "";
    //String selectedsubgrpstr = "";
    String announcmentType = "0";
    String inputids = "";
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
    private String uploadedimgid = "0";
    private String announcemet_id;
    private String flag_callwebsercie = "0";
    private String edit_announcement_selectedids = "0";
    private String announID = "0";
    ProgressDialog pd;
    private String hasimageflag = "0";
    CheckBox cb_noti_all, cb_noti_nonsmart;
    String sendSMSAll = "0"; // 0- Off 1- On
    String sendSMSNonSmartPh = "0"; // 0- Off 1- On
    String moduleName = "", moduleId = "";

    // Code for Reminder starts
    String toggle_flag_onoff = "0";
    LinearLayout linear_repeatnotification;
    private ArrayList<AddAnnouncementDateTimeData> list_datetime = new ArrayList<AddAnnouncementDateTimeData>();
    private AddAnnouncementDateTimeListAdapter adapter_datetime;
    ListView listView;
    ScrollView scrollview;
    String repeat_date = "";
    String repeat_time = "";
    TextView tv_addSign;
    private static Uri mCapturedImageURI;
    boolean isPublishdateDisabled = false;
    private SingleDateAndTimePickerDialog datetimePicker,datetimeForReminder;
    SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String publishDate="",expiryDate="";

    Context context;
    // Code for Reminder ends
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_announcement);
        context=this;
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        // iv_backbutton.setVisibility(View.GONE);
        moduleName = PreferenceManager.getPreference(this, PreferenceManager.MODUEL_NAME, "Announcements");
        moduleId = PreferenceManager.getPreference(this, PreferenceManager.MODULE_ID);
        tv_title.setText("Announcements");
//        cb_display=(CheckBox)findViewById(R.id.cb_display);
        et_announce_title = (EditText) findViewById(R.id.et_announce_title);
        et_announce_desc = (EditText) findViewById(R.id.et_announce_desc);
        tv_add = (TextView) findViewById(R.id.tv_add);
        tv_publish_date = (TextView) findViewById(R.id.publish_date);
        tv_publishtime = (TextView) findViewById(R.id.tv_publishtime);
        tv_expiry_date = (TextView) findViewById(R.id.expiry_date);
        tv_expiretime = (TextView) findViewById(R.id.tv_expiretime);
        tv_no = (TextView) findViewById(R.id.tv_no);
        iv_event_photo = (ImageView) findViewById(R.id.iv_event_photo);
        d_radio0 = (RadioButton) findViewById(R.id.d_radio0);
        d_radio1 = (RadioButton) findViewById(R.id.d_radio1);
        d_radio2 = (RadioButton) findViewById(R.id.d_radio2);
       // et_link=(EditText)findViewById(R.id.et_eventLink);
        tv_getCount = (TextView) findViewById(R.id.getCount);
        smscount = (TextView) findViewById(R.id.smscount);
        iv_edit = (ImageView) findViewById(R.id.iv_edit);
        cb_noti_nonsmart = (CheckBox) findViewById(R.id.cb_noti_nonsmart);
        cb_noti_all = (CheckBox) findViewById(R.id.cb_noti_all);

        d_radio0.setChecked(true);

        // Code for Reminder starts
        iv_toggle = (ImageView) findViewById(R.id.iv_toggle);
        linear_repeatnotification = (LinearLayout) findViewById(R.id.linear_repeatnotification);
        listView = (ListView) findViewById(R.id.listView);

        scrollview = (ScrollView) findViewById(R.id.scrollview);
        tv_addSign = (TextView) findViewById(R.id.tv_addSign);

        // Code for Reminder starts

        clearselectedtext();
        init();


        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            announcemet_id = intent.getString("announcemet_id"); // Created Group ID
            if (InternetConnection.checkConnection(getApplicationContext())) {
                // Avaliable
                tv_add.setText("Update");
                webservices_getdata();
            } else {
                Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                // Not Available...
            }

        }

    }

    private void clearselectedtext() {
        tv_getCount.setText("");
        iv_edit.setVisibility(View.GONE);
    }


    private void init() {

        smscount.setText(Utils.smsCount);
        iv_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.popupback(AddAnnouncement.this);
            }

        });
        cb_noti_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_noti_all.isChecked()) {
                    sendSMSAll = "1";
                } else {
                    sendSMSAll = "0";
                }

                if (cb_noti_nonsmart.isChecked()) {
                    sendSMSNonSmartPh = "0";
                    cb_noti_nonsmart.setChecked(false);
                    cb_noti_all.setChecked(true);
                }
            }
        });
        cb_noti_nonsmart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cb_noti_nonsmart.isChecked()) {
                    sendSMSNonSmartPh = "1";
                } else {
                    sendSMSNonSmartPh = "0";
                }
                if (cb_noti_all.isChecked()) {
                    sendSMSAll = "0";
                    cb_noti_all.setChecked(false);
                    cb_noti_nonsmart.setChecked(true);
                }
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                finish();
            }
        });
        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (announcmentType.equals("2")) {
                    Intent i = new Intent(AddAnnouncement.this, AddMembers.class);
                    //i.putParcelableArrayListExtra("name1", memberData);
                    i.putParcelableArrayListExtra("selected_memberdata", listaddmemberdata);
                    i.putExtra("edit_announcement_selectedids", edit_announcement_selectedids);
                    startActivityForResult(i, 3);
                } else if (announcmentType.equals("1")) {
                    //Intent i = new Intent(AddAnnouncement.this, SubGroupList.class);
                    //i.putParcelableArrayListExtra("name1", memberData);
                    /*i.putParcelableArrayListExtra("selected_subgrpdata", listaddsubgrp);
                    i.putExtra("edit_announcement_selectedids", edit_announcement_selectedids);
                    startActivityForResult(i, 1);*/
                    Intent subgrp = new Intent(AddAnnouncement.this, NewGroupSelectionActivity.class);
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

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Log.d("-----------------", "add is clicked");
                if(tv_add.getText().equals("Update")){
                    if(updateValidation()== true){
                        if (InternetConnection.checkConnection(getApplicationContext())) {
                            webservices();
                        } else {
                            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    //  Log.d("-----------------", "add is clicked");
                    if (validation() == true) {
                        // webservices();
                        if (InternetConnection.checkConnection(getApplicationContext())) {
                            // Avaliable
                            webservices();
                        } else {
                            Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                            // Not Available...
                        }
                    }
                }
            }
        });

        tv_publish_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  showDatePicker(tv_publish_date);
                Utils.hideKeyBoard(AddAnnouncement.this,tv_publish_date);
                datepicker(tv_publish_date);
            }
        });
        tv_publishtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyBoard(AddAnnouncement.this,tv_publishtime);
                ShowTimePicker(tv_publishtime,2);
            }
        });
        tv_expiry_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.hideKeyBoard(AddAnnouncement.this,tv_expiry_date);
                datepicker(tv_expiry_date);
            }
        });
        tv_expiretime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyBoard(AddAnnouncement.this,tv_expiretime);
                ShowTimePicker(tv_expiretime,3);
            }
        });

        // Code for reminder toggle

        iv_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggle_flag_onoff.equals("1")) {
                    //iv_toggle.setImageResource(getResources().getDrawable(R.drawable.off_toggle_btn));
                    iv_toggle.setImageResource(R.drawable.off_toggle_btn);
                    linear_repeatnotification.setVisibility(View.GONE);
                    list_datetime.clear();
                    adapter_datetime = new AddAnnouncementDateTimeListAdapter(AddAnnouncement.this, R.layout.add_announcement_datetime_list_item, list_datetime);
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

        tv_addSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                //if (validation() == true)
//                    datepicker_repeate_notification();

//                if(validationForReminder()){
//                    ShowTimePicker_repeate_notification();
//                }

                ShowTimePicker_repeate_notification();
            }
        });


    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.d_radio0:
                if (checked)
                    //set inch button to unchecked
                    announcmentType = "0";
                inputids = "";
                d_radio1.setChecked(false);
                d_radio2.setChecked(false);
                d_radio2.setEnabled(true);

                d_radio1.setEnabled(true);
                clearselectedtext();
                break;
            case R.id.d_radio1:
                if (checked)
                    //set MM button to unchecked
               /*     d_radio0.setChecked(false);
                d_radio2.setChecked(false);
                d_radio1.setEnabled(false);
                d_radio2.setEnabled(true);*/
                    d_radio1.setChecked(false);
                /*Intent subgrp = new Intent(AddAnnouncement.this, SubGroupList.class);
                subgrp.putExtra("flag_addsubgrp", "1");
                startActivityForResult(subgrp, 1);*/

                Intent subgrp = new Intent(AddAnnouncement.this, NewGroupSelectionActivity.class);
                subgrp.putExtra("flag_addsubgrp", "1");
                startActivityForResult(subgrp, 1);
                //startActivity(subgrp);
                break;

            case R.id.d_radio2:
                if (checked) {
                    d_radio2.setChecked(false);
                    Intent i = new Intent(AddAnnouncement.this, AddMembers.class);
                    startActivityForResult(i, 3);
                }

                // d_radio2.setEnabled(false);
             /*   d_radio1.setEnabled(true);
                d_radio0.setChecked(false);
                d_radio1.setChecked(false);*/
                break;
        }
    }

    public void ShowTimePicker(final TextView time_text, final int module) {
//        MyTimePicker myTimePicker = new MyTimePicker(this);
//        myTimePicker.show();
//
//        myTimePicker.setTimeListener(new MyTimePicker.onTimeSet() {
//
//            @Override
//            public void onTime(TimePicker view, int hourOfDay, int minute) {
//                //Toast.makeText(AddAnnouncement.this,"time is " + hourOfDay + ":" + minute, Toast.LENGTH_LONG).show();
//
//                time_text.setText(utilTime(hourOfDay) + ":" + utilTime(minute));
//            }
//        });

        String title="";
        Date date=new Date();
        try {
            if(module==2){
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

        datetimePicker= new SingleDateAndTimePickerDialog.Builder(AddAnnouncement.this).build();
//                .bottomSheet()
//                .curved()
        datetimePicker.setDefaultDate(date);

        //datetimePicker.setMinDateRange(new Date());
        datetimePicker .setMinutesStep(1);
        //.displayHours(false)
        //.displayMinutes(false)

        //.todayText("aujourd'hui")

        datetimePicker.setTitle(title);
        datetimePicker.setListener(new SingleDateAndTimePickerDialog.Listener() {
            @Override
            public void onDateSelected(Date date) {

                time_text.setText(sdf1.format(date));
                if(module==2){
                    publishDate=sdf1.format(date);
                }else if(module==3){
                    expiryDate=sdf1.format(date);
                }else if(module==4){
//                            title="Notification Date & Time";
                }
            }
        }).display();


    }

    private static String utilTime(int value) {

        if (value < 10)
            return "0" + String.valueOf(value);
        else
            return String.valueOf(value);
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


        AlertDialog.Builder builder = new AlertDialog.Builder(AddAnnouncement.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options,  new DialogInterface.OnClickListener() {
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
                    Crop.pickImage(AddAnnouncement.this);

                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();
                } else if (options[item].equals("Remove Photo")) {
                    if(announID.equals("0")){
                        iv_event_photo.setImageResource(R.drawable.edit_image);

                    }else{
                        remove_photo_webservices();
                    }
                    //dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        String isSubGroupAdmin = "0";  // means no
        String isAdmin = PreferenceManager.getPreference(AddAnnouncement.this, PreferenceManager.IS_GRP_ADMIN, "No");
        if (isAdmin.equalsIgnoreCase("partial")) {
            isSubGroupAdmin = "1";  // means yes
        }
        arrayList.add(new BasicNameValuePair("isSubGrpAdmin", isSubGroupAdmin));

        arrayList.add(new BasicNameValuePair("announID", announID));
        arrayList.add(new BasicNameValuePair("annType", announcmentType));
        arrayList.add(new BasicNameValuePair("announTitle", et_announce_title.getText().toString()));
        arrayList.add(new BasicNameValuePair("announceDEsc", et_announce_desc.getText().toString()));

        arrayList.add(new BasicNameValuePair("memID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));
        arrayList.add(new BasicNameValuePair("grpID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("inputIDs", inputids));
        arrayList.add(new BasicNameValuePair("announImg", uploadedimgid));
        arrayList.add(new BasicNameValuePair("sendSMSNonSmartPh", sendSMSNonSmartPh));
        arrayList.add(new BasicNameValuePair("sendSMSAll", sendSMSAll));
//        arrayList.add(new BasicNameValuePair("publishDate", tv_publish_date.getText().toString() + " " + tv_publishtime.getText().toString()));
        arrayList.add(new BasicNameValuePair("publishDate", publishDate));

        // arrayList.add(new BasicNameValuePair("publishTime", "10:20:00"));
//        arrayList.add(new BasicNameValuePair("expiryDate", tv_expiry_date.getText().toString() + " " + tv_expiretime.getText().toString()));
        arrayList.add(new BasicNameValuePair("expiryDate", expiryDate));
        arrayList.add(new BasicNameValuePair("moduleId", moduleId));
        arrayList.add(new BasicNameValuePair("AnnouncementRepeatDates", list_datetime.toString().replace("[", "").replace("]", "")));
//        if(cb_display.isChecked()){
//            arrayList.add(new BasicNameValuePair("displayonbanner","1"));
//        }else {
//            arrayList.add(new BasicNameValuePair("displayonbanner","0"));
//
//        }
      //  arrayList.add(new BasicNameValuePair("link",et_link.getText().toString()));
        flag_callwebsercie = "0";
        Log.d("Response", "PARAMETERS " + Constant.AddAnnouncement + " :- " + arrayList.toString());
        new WebConnectionAsyncLogin(Constant.AddAnnouncement, arrayList, AddAnnouncement.this).execute();
    }

    private void webservices_getdata() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("announID", announcemet_id));
        arrayList.add(new BasicNameValuePair("grpID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        arrayList.add(new BasicNameValuePair("memberProfileID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));

        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        flag_callwebsercie = "1";
        Log.d("Response", "PARAMETERS " + Constant.GetAnnouncementDetails + " :- " + arrayList.toString());
        new WebConnectionAsyncLogin(Constant.GetAnnouncementDetails, arrayList, AddAnnouncement.this).execute();
    }

    private void remove_photo_webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("typeID", announcemet_id));
        arrayList.add(new BasicNameValuePair("type", "Announcement"));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        arrayList.add(new BasicNameValuePair("grpID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("moduleId", moduleId));

        flag_callwebsercie = "4";
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "PARAMETERS " + Constant.DeleteImage + " :- " + arrayList.toString());
        new WebConnectionAsyncLogin(Constant.DeleteImage, arrayList, AddAnnouncement.this).execute();
    }

    public class WebConnectionAsyncLogin extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(AddAnnouncement.this, R.style.TBProgressBar);

        public WebConnectionAsyncLogin(String url, List<NameValuePair> argList, Context ctx) {
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
                val = val.toString();
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
            //Log.d("response","Do post"+ result.toString());
            if (result != "") {
                if (flag_callwebsercie.equals("0")) {
                    getresult(result.toString());

                    count_write_announcement++;
                } else if (flag_callwebsercie.equals("1")) {
                    getresult_addeddata(result.toString());
                } else if (flag_callwebsercie.equals("4")) {
                    getresultOfRemovephoto(result.toString());
                }

            } else {
                Log.d("Response", "Null Resposnse");
            }

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
                //Utils.showToastWithTitleAndContext(getApplicationContext(), "Failed to Remove Photo...");
                Utils.showToastWithTitleAndContext(getApplicationContext(), "Photo Remove failed, Please try Again!");

            }


        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    private void getresult(String val) {
        try {
            JSONObject jsonObj = new JSONObject(val);
            JSONObject ActivityResult = jsonObj.getJSONObject("TBAddAnnouncementResult");
            final String status = ActivityResult.getString("status");
            if (status.equals("0")) {
                String msg = ActivityResult.getString("message");

                Intent intent = new Intent();
                setResult(1, intent);
                hideKeyboard();
                finish();//finishing activity

                if(tv_add.getText().equals("Add"))
                    Toast.makeText(AddAnnouncement.this, "Announcement  Added successfully.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(AddAnnouncement.this, "Announcement Updated successfully.", Toast.LENGTH_SHORT).show();

                Log.d("Touchbase", "*************** " + status);
                Log.d("Touchbase", "*************** " + msg);

            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }

    private void getresult_addeddata(String val) {
        try {
            JSONObject jsonObj = new JSONObject(val);
            JSONObject EventResult = jsonObj.getJSONObject("TBAnnounceListResult");
            final String status = EventResult.getString("status");
            if (status.equals("0")) ;
            {
                JSONArray EventListResdult = EventResult.getJSONArray("AnnounListResult");
                for (int i = 0; i < EventListResdult.length(); i++) {
                    JSONObject object = EventListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("AnnounceList");


                    et_announce_title.setText(objects.getString("announTitle").toString());
                    et_announce_desc.setText(objects.getString("announceDEsc").toString());
                    announID = objects.getString("announID").toString();
                    //----------------------SMS FLAG
                    if (objects.getString("sendSMSNonSmartPh").toString().equals("0")) {
                        cb_noti_nonsmart.setChecked(false);
                    } else {
                        cb_noti_nonsmart.setChecked(true);
                    }

                    if (objects.getString("sendSMSAll").toString().equals("0")) {
                        cb_noti_all.setChecked(false);
                    } else {
                        cb_noti_all.setChecked(true);
                    }

//                    String link=objects.getString("link");
//                    if(link!=null && !link.isEmpty()){
//                        et_link.setText(link);
//                    }

//                    if(objects.getString("displayonbanner").equals("1")){
//                        cb_display.setChecked(true);
//                    }else {
//                        cb_display.setChecked(false);
//                    }



                    //----------------------SMS FLAG

//                    String[] separated_publish = objects.getString("publishDate").toString().split(" ");
//                    tv_publish_date.setText(separated_publish[0]);
                    tv_publishtime.setText(objects.getString("publishDate").toString());
                    publishDate=objects.getString("publishDate").toString();
                    String publishDateTime = objects.getString("publishDate").toString();

//                    String[] separated_expire = objects.getString("expiryDate").toString().split(" ");
//                    tv_expiry_date.setText(separated_expire[0]);
                    tv_expiretime.setText(objects.getString("expiryDate").toString());
                    expiryDate=objects.getString("expiryDate").toString();

                    uploadedimgid = "";

                    Date cd = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String currentDate = sdf.format(cd);

//                    if(DateHelper.compareDate(publishDateTime,currentDate)<=0){
//                        tv_publish_date.setClickable(false);
//                        tv_publishtime.setClickable(false);
//                        isPublishdateDisabled = true;
//                    }



                    // RADIO BUTTONS FOR TYPE
                    if (objects.getString("type").toString().equals("0")) {
                        announcmentType = "0";
                        d_radio0.setChecked(true);
                        clearselectedtext();

                    } else if (objects.getString("type").toString().equals("1")) {

                        d_radio0.setChecked(false);
                        d_radio1.setChecked(true);
                        d_radio2.setChecked(false);

                        d_radio0.setEnabled(true);
                        d_radio1.setEnabled(false);
                        d_radio2.setEnabled(true);

                        Log.d("Touchnase", "IN subgroup ");
                        announcmentType = "1";
                        //   tv_getCount.setText("You have added " + " sub groups");
                        iv_edit.setVisibility(View.VISIBLE);


                       /* edit_announcement_selectedids = objects.getString("profileIds").toString();
                        inputids = objects.getString("profileIds").toString();
                        String[] mystring = edit_announcement_selectedids.split(",");
                        int size = mystring.length;
                        tv_getCount.setText("You have added " + size + " sub groups");*/

                        //--------------------
                        edit_announcement_selectedids = objects.getString("profileIds").toString();
                        String[] ids = edit_announcement_selectedids.split(",");
                        selectedsubgrp = new ArrayList<String>();
                        for (String id: ids) {
                            selectedsubgrp.add(id);
                        }
                        inputids = objects.getString("profileIds").toString();
                        String[] mystring = edit_announcement_selectedids.split(",");
                        int size = mystring.length;
                        tv_getCount.setText("You have added " + size + " sub groups");


                    } else if (objects.getString("type").toString().equals("2")) {

                        d_radio0.setChecked(false);
                        d_radio1.setChecked(false);
                        d_radio2.setChecked(true);

                        d_radio0.setEnabled(true);
                        d_radio1.setEnabled(true);
                        d_radio2.setEnabled(false);

                        Log.d("Touchnase", "IN MEMBER ");
                        announcmentType = "2";
                        // tv_getCount.setText("You have added " +  " sub groups");
                        iv_edit.setVisibility(View.VISIBLE);

                        edit_announcement_selectedids = objects.getString("profileIds").toString();
                        inputids = objects.getString("profileIds").toString();
                        String[] mystring = edit_announcement_selectedids.split(",");
                        int size = mystring.length;
                        tv_getCount.setText("You have added " + size + " members");
                    }


                    //  iv_announcementimg
                  /*  Picasso.with(Announcement_details.this).load("http://suezuniv.edu.eg/pme-en/media/k2/items/cache/b84c5756f6a889fa332015e4458021f9_XL.jpg")
                            .placeholder(R.drawable.imageplaceholder)
                            .into(iv_announcementimg);*/
                    if (objects.has("announImg")) {
                        if (objects.getString("announImg").toString().equals("") || objects.getString("announImg").toString() == null) {
                            iv_event_photo.setVisibility(View.VISIBLE);
                        } else {
                            hasimageflag = "1";
                            // progressbar.setVisibility(View.VISIBLE);
                            Picasso.with(AddAnnouncement.this).load(objects.getString("announImg").toString())
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

                                AddAnnouncementDateTimeData data = new AddAnnouncementDateTimeData();
                                data.setDate(separated_repeatedateDateTime[0]);
                                data.setTime(separated_repeatedateDateTime[1]);
                                list_datetime.add(data);
                       /* scrollview.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });*/


                                linear_repeatnotification.setVisibility(View.VISIBLE);
                                setListViewHeightBasedOnChildren(listView);
                                adapter_datetime = new AddAnnouncementDateTimeListAdapter(AddAnnouncement.this, R.layout.add_event_datetime_list_item, list_datetime);
                                listView.setAdapter(adapter_datetime);

                            }
                        }
                    }


                }

            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }

    @Override
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
                    if (d.isBox() == true) {
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
                d_radio0.setChecked(false);
                d_radio1.setChecked(false);
                d_radio2.setChecked(true);

                d_radio0.setEnabled(true);
                d_radio1.setEnabled(true);
                d_radio2.setEnabled(false);

                announcmentType = "2";
                Log.d("Touchnase", "Arrat " + inputids);
                tv_getCount.setText("You have added " + count + " members");
                iv_edit.setVisibility(View.VISIBLE);
            }

        }
// SubGroup Select
        if (requestCode == 1) {


            if (resultCode == Activity.RESULT_OK) {

                listaddsubgrp = data.getParcelableArrayListExtra("result");
                Log.d("Touchnase", "@@@ " + listaddsubgrp);
                String result = "";
                int count = 0;
                /*ArrayList<String> selectedsubgrp = new ArrayList<>();

                selectedsubgrp.clear();
                inputids = "";

                for (SubGoupData d : listaddsubgrp) {
                    if (d.isBox() == true) {
                        result = result + d.getSubgrpId();
                        count = count + 1;
                        selectedsubgrp.add(d.getSubgrpId());
                    }
                    //something here
                }
                for (int i = 0; i < selectedsubgrp.size(); i++) {
                    //commaSepValueBuilder.append(n.get(i));
                    inputids = inputids + selectedsubgrp.get(i);

                    if (i != selectedsubgrp.size() - 1) {

                        // commaSepValueBuilder.append(", ");
                        inputids = inputids + ", ";
                    }

                }*/

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

                Log.d("Touchnase", "Arrat " + inputids);
                announcmentType = "1";
                tv_getCount.setText("You have added " + count + " sub groups");
                iv_edit.setVisibility(View.VISIBLE);
            }

        }

        /***************Image Capture***********/
        if (requestCode == 4) {
            if (resultCode == Activity.RESULT_OK) {


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


                beginCrop(data.getData());

            }
        }else if(requestCode == Crop.REQUEST_CROP){
            if(resultCode==RESULT_OK){
                uploadPic(data);
            }else {
                Toast.makeText(this, Crop.getError(data).getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void beginCrop(Uri source) {

        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
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
                final ProgressDialog pd = ProgressDialog.show(AddAnnouncement.this, "", "Uploading...", false);

                Thread thread = new Thread(new Runnable() {
                    public void run() {

                        uploadedimgid = Utils.doFileUpload(new File(finalImagePath), "event"); // Upload File to server
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (pd.isShowing())
                                    pd.dismiss();
                                Log.d("TOUCHBASE", "FILE UPLOAD ID  " + uploadedimgid);
                                if (uploadedimgid.equals("0")) {
                                    Toast.makeText(AddAnnouncement.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();
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
        datePickerDialog.show();
    }


    public boolean validation() {

        if (et_announce_title.getText().toString().trim().matches("") || et_announce_title.getText().toString().trim() == null) {
            Toast.makeText(AddAnnouncement.this, "Please enter the “Title”", Toast.LENGTH_LONG).show();
            return false;
        }

        if (et_announce_desc.getText().toString().trim().matches("") || et_announce_desc.getText().toString().trim() == null) {
            Toast.makeText(AddAnnouncement.this, "Please enter the “Description”", Toast.LENGTH_LONG).show();
            return false;
        }

//        if (tv_publish_date.getText().toString().trim().matches("") || tv_publish_date.getText().toString().trim() == null) {
//            Toast.makeText(AddAnnouncement.this, "Please enter a Publish Date", Toast.LENGTH_LONG).show();
//            return false;
//        }

        if (tv_publishtime.getText().toString().trim().matches("") || tv_publishtime.getText().toString().trim() == null) {
            Toast.makeText(AddAnnouncement.this, "Please enter a Publish Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }

//        if (tv_expiry_date.getText().toString().trim().matches("") || tv_expiry_date.getText().toString().trim() == null) {
//            Toast.makeText(AddAnnouncement.this, "Please enter an Expiry Date", Toast.LENGTH_LONG).show();
//            return false;
//        }
        if (tv_expiretime.getText().toString().trim().matches("") || tv_expiretime.getText().toString().trim() == null) {
            Toast.makeText(AddAnnouncement.this, "Please enter an Expiry Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!(compare_date(tv_expiry_date.getText().toString() + " " + tv_expiretime.getText().toString(),
                tv_publish_date.getText().toString() + " " + tv_publishtime.getText().toString())).equals("1")) {
            Toast.makeText(AddAnnouncement.this, "Please make the Expiry Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }


//        String expiryDate = tv_expiry_date.getText().toString() + " " + tv_expiretime.getText().toString();
//        String publishDate = tv_publish_date.getText().toString() + " " + tv_publishtime.getText().toString();

//        try {
//            if ( DateHelper.compareDate(publishDate, expiryDate) >= 0 ) {
//                Toast.makeText(AddAnnouncement.this, "Please make the Expiry Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
//                return false;
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//            Toast.makeText(AddAnnouncement.this, "Dates are not in proper format", Toast.LENGTH_LONG).show();
//        }

        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
//        try {
//            if (DateHelper.compareDate(publishDate, currentDate) <= 0 ) {
//                // Toast.makeText(AddAnnouncement.this, "Please make the Expiry Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
//                Toast.makeText(AddAnnouncement.this, "Please make the publish date & time greater than the current date & time", Toast.LENGTH_LONG).show();
//                return false;
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//            Toast.makeText(AddAnnouncement.this, "Dates are not in proper format", Toast.LENGTH_LONG).show();
//        }
        return true;
    }

    public boolean validationForReminder() {


//        if (tv_publishDate.getText().toString().trim().matches("") || tv_publishDate.getText().toString().trim() == null) {
//            Toast.makeText(AddAnnouncement.this, "Please enter a Publish Date", Toast.LENGTH_LONG).show();
//            return false;
//        }
        if (tv_publishtime.getText().toString().trim().matches("") || tv_publishtime.getText().toString().trim() == null) {
            Toast.makeText(AddAnnouncement.this, "Please enter a Publish Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }
//        if (tv_expiryDate.getText().toString().trim().matches("") || tv_expiryDate.getText().toString().trim() == null) {
//            Toast.makeText(AddAnnouncement.this, "Please enter an Expiry Date", Toast.LENGTH_LONG).show();
//            return false;
//        }
        if (tv_expiretime.getText().toString().trim().matches("") || tv_expiretime.getText().toString().trim() == null) {
            Toast.makeText(AddAnnouncement.this, "Please enter an Expiry Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }

//        String expiryDate = tv_expiryDate.getText().toString() + " " + tv_expiryTime.getText().toString();
//        String eventDate = tv_eventDate.getText().toString() + " " + tv_eventTime.getText().toString();
//
//        String publishDate = tv_publishDate.getText().toString() + " " + tv_publishTime.getText().toString();

        try {
            if ( DateHelper.compareDate(publishDate, expiryDate) >= 0 ) {
                Toast.makeText(AddAnnouncement.this, "Please make the Expiry Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(AddAnnouncement.this, "Dates are not in proper format", Toast.LENGTH_LONG).show();
        }

        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        try {
            if (DateHelper.compareDate(publishDate, currentDate) <= 0 ) {
                // Toast.makeText(AddAnnouncement.this, "Please make the Expiry Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
                Toast.makeText(AddAnnouncement.this, "Please make the publish date & time greater than the current date & time", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(AddAnnouncement.this, "Dates are not in proper format", Toast.LENGTH_LONG).show();
        }

        return true;
    }

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
    @Override
    public void onBackPressed() {

        if(datetimePicker!=null && datetimePicker.isDisplaying()){
            datetimePicker.dismiss();
        }else if(datetimeForReminder!=null && datetimeForReminder.isDisplaying()){
            datetimeForReminder.dismiss();
        }else {
            Utils.popupback(AddAnnouncement.this);

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

    private void repeate_notification_valueadd() {
        AddAnnouncementDateTimeData data = new AddAnnouncementDateTimeData();
        data.setDate(repeat_date);
        data.setTime(repeat_time);

//        Toast.makeText(AddAnnouncement.this, "Reminder added Successfully!", Toast.LENGTH_LONG).show();
//        list_datetime.add(data);
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        if (repeatenotification_validation()) {
            Toast.makeText(AddAnnouncement.this, "Reminder added SUCCESSFULLY!", Toast.LENGTH_LONG).show();
            list_datetime.add(data);
            scrollview.post(new Runnable() {
                @Override
                public void run() {
                    scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }

        setListViewHeightBasedOnChildren(listView);

        adapter_datetime = new AddAnnouncementDateTimeListAdapter(AddAnnouncement.this, R.layout.add_event_datetime_list_item, list_datetime);
        listView.setAdapter(adapter_datetime);

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

    public boolean repeatenotification_validation() {
       // String publishDateTime = tv_publish_date.getText().toString() + " " + tv_publishtime.getText().toString();

        try {
            if ( DateHelper.compareDate(repeat_date + " " + repeat_time, publishDate) == 0) {
                Toast.makeText(AddAnnouncement.this, "Repeat Notification Date & Time should not be same as Publish Date & Time", Toast.LENGTH_LONG).show();
                return false;
            }

            // Checking if repeat date time is less than public date and time
            if ( DateHelper.compareDate(repeat_date + " " + repeat_time, publishDate) <= 0) {
                Toast.makeText(AddAnnouncement.this, "Please make the Repeat notification Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
                //Toast.makeText(AddAnnouncement.this, "Reminder Date & Time should be greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(AddAnnouncement.this, "Please enter a Publish Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }

      //  String expiryDateTime = tv_expiry_date.getText().toString() + " " + tv_expiretime.getText().toString();
        try {
            // Checking if repeat date time is greater than expiry date and time
            if ( DateHelper.compareDate(repeat_date + " " + repeat_time , expiryDate) > 0 ) {
                Toast.makeText(AddAnnouncement.this, "Please make the Repeat notification Date & Time less than Expiry Date & Time", Toast.LENGTH_LONG).show();
               // Toast.makeText(AddAnnouncement.this, "Reminder Date & Time should be less than Expiry Date & Time", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(AddAnnouncement.this, "Please enter an Expiry Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }
        /*if (!(compare_date(tv_publish_date.getText().toString() + " " + tv_publishtime.getText().toString(),
                repeat_date + " " + repeat_time)).equals("2")) {
            Toast.makeText(AddAnnouncement.this, "Reminder Date & Time should be greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!(compare_date(tv_expiry_date.getText().toString() + " " + tv_expiretime.getText().toString(),
                repeat_date + " " + repeat_time)).equals("1")) {
            Toast.makeText(AddAnnouncement.this, "Reminder Date & Time should be less than Expiry Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }*/
        return true;
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

    public void ShowTimePicker_repeate_notification() {
//        MyTimePicker myTimePicker = new MyTimePicker(this);
//        myTimePicker.show();
//        myTimePicker.setTimeListener(new MyTimePicker.onTimeSet() {
//
//            @Override
//            public void onTime(TimePicker view, int hourOfDay, int minute) {
//                //Toast.makeText(AddAnnouncement.this,"time is " + hourOfDay + ":" + minute, Toast.LENGTH_LONG).show();
//
//                // time_text.setText(utilTime(hourOfDay) + ":" + utilTime(minute));
//                repeat_time = utilTime(hourOfDay) + ":" + utilTime(minute);
//                repeate_notification_valueadd();
//            }
//        });

        datetimeForReminder= new SingleDateAndTimePickerDialog.Builder(AddAnnouncement.this).build();
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

    public boolean updateValidation(){
        if (et_announce_title.getText().toString().trim().matches("") || et_announce_title.getText().toString().trim() == null) {
            Toast.makeText(AddAnnouncement.this, "Please enter the “Title”", Toast.LENGTH_LONG).show();
            return false;
        }

        if (et_announce_desc.getText().toString().trim().matches("") || et_announce_desc.getText().toString().trim() == null) {
            Toast.makeText(AddAnnouncement.this, "Please enter the “Description”", Toast.LENGTH_LONG).show();
            return false;
        }

//        if (tv_publish_date.getText().toString().trim().matches("") || tv_publish_date.getText().toString().trim() == null) {
//            Toast.makeText(AddAnnouncement.this, "Please enter a Publish Date", Toast.LENGTH_LONG).show();
//            return false;
//        }

        if (tv_publishtime.getText().toString().trim().matches("") || tv_publishtime.getText().toString().trim() == null) {
            Toast.makeText(AddAnnouncement.this, "Please enter a Publish Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }
//        if (tv_expiry_date.getText().toString().trim().matches("") || tv_expiry_date.getText().toString().trim() == null) {
//            Toast.makeText(AddAnnouncement.this, "Please enter an Expiry Date", Toast.LENGTH_LONG).show();
//            return false;
//        }
        if (tv_expiretime.getText().toString().trim().matches("") || tv_expiretime.getText().toString().trim() == null) {
            Toast.makeText(AddAnnouncement.this, "Please enter an Expiry Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }

//        String publishDate = tv_publish_date.getText().toString() + " " + tv_publishtime.getText().toString();
//        String expiryDate = tv_expiry_date.getText().toString() + " " + tv_expiretime.getText().toString();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());


        //        if(!isPublishdateDisabled) {
//            try {
//                if (DateHelper.compareDate(publishDate, currentDate) <= 0) {
//                    // Toast.makeText(AddAnnouncement.this, "Please make the Expiry Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
//                    Toast.makeText(AddAnnouncement.this, "Please make the publish date & time greater than the current date & time", Toast.LENGTH_LONG).show();
//                    return false;
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//                Toast.makeText(AddAnnouncement.this, "Dates are not in proper format", Toast.LENGTH_LONG).show();
//            }
//        }

        try {
            if (DateHelper.compareDate(expiryDate, currentDate) <= 0) {
                Toast.makeText(AddAnnouncement.this, "Please make the expiry date & time greater than the current date & time", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(AddAnnouncement.this, "Dates are not in proper format", Toast.LENGTH_LONG).show();
        }

        try {
            if (DateHelper.compareDate(expiryDate, publishDate) <= 0) {
                Toast.makeText(AddAnnouncement.this, "Please make the expiry date & time greater than the publish date & time", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(AddAnnouncement.this, "Dates are not in proper format", Toast.LENGTH_LONG).show();
        }
        return true;
    }

}
