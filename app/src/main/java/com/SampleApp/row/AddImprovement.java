package com.SampleApp.row;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.SampleApp.row.Data.DirectoryData;
import com.SampleApp.row.Data.SubGoupData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.ImageCompression;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.MarshMallowPermission;
import com.SampleApp.row.Utils.MyTimePicker;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by USER on 15-01-2016.
 */
public class AddImprovement extends FragmentActivity {
    ArrayList<String> selectedsubgrp;
    RadioButton d_radio1, d_radio2;
    RadioButton d_radio0;
    TextView tv_getCount, tv_add;
    TextView tv_title, tv_publish_date, tv_expiry_date, tv_expiretime, tv_no;
    ImageView iv_backbutton, iv_edit;
    ImageView call_button, iv_event_photo;

    String finalImagePath;

    DatePicker publish_date1;
    TextView tv_publishtime,smscount;

    EditText et_announce_title, et_announce_desc, et_announce_venue;
    ArrayList<DirectoryData> listaddmemberdata = new ArrayList<>();
    ArrayList<SubGoupData> listaddsubgrp = new ArrayList<>();
    //String selectedmemberidstr = "";
    //String selectedsubgrpstr = "";
    String moduleName = "";
    String announcmentType = "0";
    String inputids = "";
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
    private String uploadedimgid = "0";
    private String improvementId;
    private String flag_callwebsercie = "0";
    private String edit_announcement_selectedids = "0";
    private String announID = "0";
    ProgressDialog pd;
    private String hasimageflag = "0";
    CheckBox cb_noti_all, cb_noti_nonsmart;
    String sendSMSAll = "0"; // 0- Off 1- On
    String sendSMSNonSmartPh = "0"; // 0- Off 1- On

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_announcement);


        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        // iv_backbutton.setVisibility(View.GONE);
        moduleName = PreferenceManager.getPreference(this, PreferenceManager.MODUEL_NAME, "Improvement");
        tv_title.setText(moduleName);

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

        tv_getCount = (TextView) findViewById(R.id.getCount);
        smscount = (TextView) findViewById(R.id.smscount);
        iv_edit = (ImageView) findViewById(R.id.iv_edit);
        cb_noti_nonsmart = (CheckBox) findViewById(R.id.cb_noti_nonsmart);
        cb_noti_all = (CheckBox) findViewById(R.id.cb_noti_all);

        d_radio0.setChecked(true);
        clearselectedtext();
        init();


        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            improvementId = intent.getString("improvementId"); // Created Group ID
            Log.e("MyImprovementId", "My improvement id is : "+improvementId);
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
                Utils.popupback(AddImprovement.this);
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
                finish();
            }
        });
        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (announcmentType.equals("2")) {
                    Intent i = new Intent(AddImprovement.this, AddMembers.class);
                    //i.putParcelableArrayListExtra("name1", memberData);
                    i.putParcelableArrayListExtra("selected_memberdata", listaddmemberdata);
                    i.putExtra("edit_announcement_selectedids", edit_announcement_selectedids);
                    startActivityForResult(i, 3);
                } else if (announcmentType.equals("1")) {
                    /*Intent i = new Intent(AddImprovement.this, SubGroupList.class);
                    //i.putParcelableArrayListExtra("name1", memberData);
                    i.putParcelableArrayListExtra("selected_subgrpdata", listaddsubgrp);
                    i.putExtra("edit_announcement_selectedids", edit_announcement_selectedids);
                    startActivityForResult(i, 1);*/
                    Intent subgrp = new Intent(AddImprovement.this, NewGroupSelectionActivity.class);
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
        });

        tv_publish_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  showDatePicker(tv_publish_date);
                datepicker(tv_publish_date);
            }
        });
        tv_publishtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTimePicker(tv_publishtime);
            }
        });
        tv_expiry_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker(tv_expiry_date);
            }
        });
        tv_expiretime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTimePicker(tv_expiretime);
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
                /*Intent subgrp = new Intent(AddImprovement.this, SubGroupList.class);
                subgrp.putExtra("flag_addsubgrp", "1");
                startActivityForResult(subgrp, 1);*/

                Intent subgrp = new Intent(AddImprovement.this, NewGroupSelectionActivity.class);
                subgrp.putExtra("flag_addsubgrp", "1");
                startActivityForResult(subgrp, 1);
                //startActivity(subgrp);
                break;

            case R.id.d_radio2:
                if (checked) {
                    d_radio2.setChecked(false);
                    Intent i = new Intent(AddImprovement.this, AddMembers.class);

                    startActivityForResult(i, 3);


                }

                // d_radio2.setEnabled(false);
             /*   d_radio1.setEnabled(true);
                d_radio0.setChecked(false);
                d_radio1.setChecked(false);*/
                break;
        }
    }

    public void ShowTimePicker(final TextView time_text) {
        MyTimePicker myTimePicker = new MyTimePicker(this);
        myTimePicker.show();

        myTimePicker.setTimeListener(new MyTimePicker.onTimeSet() {

            @Override
            public void onTime(TimePicker view, int hourOfDay, int minute) {
                //Toast.makeText(AddAnnouncement.this,"time is " + hourOfDay + ":" + minute, Toast.LENGTH_LONG).show();

                time_text.setText(utilTime(hourOfDay) + ":" + utilTime(minute));
            }
        });
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

        if (hasimageflag.equals("0")) {
            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
            hasimageflag = "1";
        } else {
            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Remove Photo", "Cancel"};

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(AddImprovement.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                    startActivityForResult(intent, 4);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


                    startActivityForResult(intent, 2);

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
        String isAdmin = PreferenceManager.getPreference(AddImprovement.this, PreferenceManager.IS_GRP_ADMIN, "No");
        if (isAdmin.equalsIgnoreCase("partial")) {
            isSubGroupAdmin = "1";  // means yes
        }

        arrayList.add(new BasicNameValuePair("isSubGrpAdmin", isSubGroupAdmin));

        arrayList.add(new BasicNameValuePair("improvementID", announID));
        arrayList.add(new BasicNameValuePair("imprType", announcmentType));
        arrayList.add(new BasicNameValuePair("improvementTitle", et_announce_title.getText().toString()));
        arrayList.add(new BasicNameValuePair("improvementDesc", et_announce_desc.getText().toString()));

        arrayList.add(new BasicNameValuePair("memID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));
        arrayList.add(new BasicNameValuePair("grpID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("inputIDs", inputids));
        arrayList.add(new BasicNameValuePair("improvementImg", uploadedimgid));
        arrayList.add(new BasicNameValuePair("sendSMSNonSmartPh", sendSMSNonSmartPh));
        arrayList.add(new BasicNameValuePair("sendSMSAll", sendSMSAll));
        arrayList.add(new BasicNameValuePair("publishDate", tv_publish_date.getText().toString() + " " + tv_publishtime.getText().toString()));
        // arrayList.add(new BasicNameValuePair("publishTime", "10:20:00"));
        arrayList.add(new BasicNameValuePair("expiryDate", tv_expiry_date.getText().toString() + " " + tv_expiretime.getText().toString()));

        flag_callwebsercie = "0";
        Log.d("Response", "PARAMETERS " + Constant.AddImprovement + " :- " + arrayList.toString());
        new WebConnectionAsyncLogin(Constant.AddImprovement, arrayList, AddImprovement.this).execute();
    }

    private void webservices_getdata() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("improvementID", improvementId));
        arrayList.add(new BasicNameValuePair("grpID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        arrayList.add(new BasicNameValuePair("memberProfileID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));

        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        flag_callwebsercie = "1";
        Log.d("Response", "PARAMETERS " + Constant.GetImprovementDetails + " :- " + arrayList.toString());
        new WebConnectionAsyncLogin(Constant.GetImprovementDetails , arrayList, AddImprovement.this).execute();
    }

    private void remove_photo_webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("typeID", improvementId));
        arrayList.add(new BasicNameValuePair("type", "Announcement"));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        arrayList.add(new BasicNameValuePair("grpID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));

        flag_callwebsercie = "4";
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "PARAMETERS " + Constant.DeleteImage + " :- " + arrayList.toString());
        new WebConnectionAsyncLogin(Constant.DeleteImage, arrayList, AddImprovement.this).execute();
    }

    public class WebConnectionAsyncLogin extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(AddImprovement.this, R.style.TBProgressBar);

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
            JSONObject ActivityResult = jsonObj.getJSONObject("TBAddImprovementResult");
            final String status = ActivityResult.getString("status");
            if (status.equals("0")) {
                String msg = ActivityResult.getString("message");

                Intent intent = new Intent();
                setResult(1, intent);

                if(tv_add.getText().equals("Add"))
                    Toast.makeText(AddImprovement.this, "Added successfully.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(AddImprovement.this, "Updated successfully.", Toast.LENGTH_SHORT).show();

                Log.d("Touchbase", "*************** " + status);
                Log.d("Touchbase", "*************** " + msg);
                finish();//finishing activity
                finish();
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }

    private void getresult_addeddata(String val) {
        try {
            JSONObject jsonObj = new JSONObject(val);
            JSONObject EventResult = jsonObj.getJSONObject("TBImprovementListResult");
            final String status = EventResult.getString("status");
            if (status.equals("0")) ;
            {
                JSONArray EventListResdult = EventResult.getJSONArray("ImprovementListResult");
                for (int i = 0; i < EventListResdult.length(); i++) {
                    JSONObject object = EventListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("ImprovementList");


                    et_announce_title.setText(objects.getString("improvementTitle").toString());
                    et_announce_desc.setText(objects.getString("improvementDesc").toString());
                    announID = objects.getString("improvementID").toString();
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
                    //----------------------SMS FLAG

                    String[] separated_publish = objects.getString("publishDate").toString().split(" ");
                    tv_publish_date.setText(separated_publish[0]);
                    tv_publishtime.setText(separated_publish[1]);

                    String[] separated_expire = objects.getString("expiryDate").toString().split(" ");
                    tv_expiry_date.setText(separated_expire[0]);
                    tv_expiretime.setText(separated_expire[1]);

                    uploadedimgid = "";


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


                        edit_announcement_selectedids = objects.getString("profileIds").toString();

                        /*String[] mystring = edit_announcement_selectedids.split(",");
                        int size = mystring.length;
                        tv_getCount.setText("You have added " + size + " sub groups");*/

                        String[] ids = edit_announcement_selectedids.split(",");
                        selectedsubgrp = new ArrayList<String>();
                        for (String id: ids) {
                            selectedsubgrp.add(id);
                        }
                        //inputids = objects.getString("inputIds").toString();
                        //String[] mystring = edit_announcement_selectedids.split(",");
                        int size = ids.length;
                        tv_getCount.setText("You have added " + size + " sub groups");
                        Log.e("Here", "Here we come with subgroups 2");
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
                    if (objects.has("improvementImg")) {
                        if (objects.getString("improvementImg").toString().equals("") || objects.getString("improvementImg").toString() == null) {
                            iv_event_photo.setVisibility(View.VISIBLE);
                        } else {
                            hasimageflag = "1";
                            // progressbar.setVisibility(View.VISIBLE);
                            Picasso.with(AddImprovement.this).load(objects.getString("improvementImg").toString())
                                    .placeholder(R.drawable.imageplaceholder)
                                    .resize(400,400)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
// Member Select
        if (requestCode == 3) {


            if (resultCode == Activity.RESULT_OK) {

                listaddmemberdata = data.getParcelableArrayListExtra("result");
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
                /*ArrayList<String> selectedmemberid = new ArrayList<>();

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

                }*/
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
                Log.e("Here", "Here we come with subgroups 1");
                iv_edit.setVisibility(View.VISIBLE);

            }

        }

        /***************Image Capture***********/
        if (requestCode == 4) {


            if (resultCode == Activity.RESULT_OK) {

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
                    Bitmap bt=Bitmap.createScaledBitmap(bitmap, 400, 400, false);
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
                    pd = ProgressDialog.show(AddImprovement.this, "", "Loading...", false);
                    final File finalF = f;
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            uploadedimgid = Utils.doFileUpload(new File(finalF.toString()), "event"); // Upload File to server
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (pd.isShowing())
                                        pd.dismiss();
                                    Log.d("TOUCHBASE", "FILE UPLOAD ID InnerThread  " + uploadedimgid);
                                    if (uploadedimgid.equals("0")) {
                                        Toast.makeText(AddImprovement.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();
                                        iv_event_photo.setImageResource(R.drawable.edit_image);
                                    }
                                    //Toast.makeText(Register.this, "Verify your account through the registered email id", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                    thread.start();
                    ///-------------------------------------------------------------------
                    //uploadedimgid = Utils.doFileUpload(new File(f.toString()), "announcement"); // Upload File to server


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
        } else if (requestCode == 2) {

            ImageCompression imageCompression = new ImageCompression();
            String path = "";

            if (resultCode == Activity.RESULT_OK) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                final String picturePath = c.getString(columnIndex);
                c.close();
                final Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.d("TOUCHBASE", "FILE PATH " + picturePath.toString());

                //-----------Image with reduced size -------------------------------------


                path = Utils.getRealPathFromURI(selectedImage, getApplicationContext());
                Log.d("==== Path ===", "======" + path);

                imageCompression = new ImageCompression();
                finalImagePath = imageCompression.compressImage(path, getApplicationContext());
                Log.d("==picturePath====","0000...."+finalImagePath);



                ///-------------------------------------------------------------------
                pd = ProgressDialog.show(AddImprovement.this, "", "Loading...", false);
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        uploadedimgid = Utils.doFileUpload(new File(finalImagePath), "event"); // Upload File to server
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (pd.isShowing())
                                    pd.dismiss();
                                if (uploadedimgid.equals("0")) {
                                    Toast.makeText(AddImprovement.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();
                                    iv_event_photo.setImageResource(R.drawable.edit_image);
                                } else {
                                    //  iv_event_photo.setImageBitmap(thumbnail);

                                    iv_event_photo.setImageDrawable(Drawable.createFromPath(finalImagePath));

                                }
                                //Toast.makeText(Register.this, "Verify your account through the registered email id", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                thread.start();
                ///-------------------------------------------------------------------
                // uploadedimgid = Utils.doFileUpload(new File(picturePath), "announcement"); // Upload File to server
                // Log.d("TOUCHBASE", "FILE UPLOAD ID  " + uploadedimgid);


            }
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
            Toast.makeText(AddImprovement.this, "Please enter the “Title”", Toast.LENGTH_LONG).show();
            return false;
        }

        if (et_announce_desc.getText().toString().trim().matches("") || et_announce_desc.getText().toString().trim() == null) {
            Toast.makeText(AddImprovement.this, "Please enter the “Description”", Toast.LENGTH_LONG).show();
            return false;
        }

        if (tv_publish_date.getText().toString().trim().matches("") || tv_publish_date.getText().toString().trim() == null) {
            Toast.makeText(AddImprovement.this, "Please enter a Publish Date", Toast.LENGTH_LONG).show();
            return false;
        }

        if (tv_publishtime.getText().toString().trim().matches("") || tv_publishtime.getText().toString().trim() == null) {
            Toast.makeText(AddImprovement.this, "Please enter a Publish Time", Toast.LENGTH_LONG).show();
            return false;
        }
        if (tv_expiry_date.getText().toString().trim().matches("") || tv_expiry_date.getText().toString().trim() == null) {
            Toast.makeText(AddImprovement.this, "Please enter an Expiry Date", Toast.LENGTH_LONG).show();
            return false;
        }
        if (tv_expiretime.getText().toString().trim().matches("") || tv_expiretime.getText().toString().trim() == null) {
            Toast.makeText(AddImprovement.this, "Please enter an Expiry Time", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!(compare_date(tv_expiry_date.getText().toString() + " " + tv_expiretime.getText().toString(),
                tv_publish_date.getText().toString() + " " + tv_publishtime.getText().toString())).equals("1")) {
            Toast.makeText(AddImprovement.this, "Please make the Expiry Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
            return false;
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
        Utils.popupback(AddImprovement.this);
        //
        // super.onBackPressed();
    }
}
