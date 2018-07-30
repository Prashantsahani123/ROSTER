package com.SampleApp.row;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Data.DirectoryData;
import com.SampleApp.row.Data.SubGoupData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.DateHelper;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.MarshMallowPermission;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by USER on 04-01-2016.
 */
public class Documents_upload extends Activity {
    ArrayList<String> selectedsubgrp;
    ListView listview;
    ArrayAdapter<String> adapter;
    TextView tv_title, tv_getCount;
    ImageView iv_backbutton, iv_edit;
    LinearLayout ll_delete;
    RadioButton d_radio1, d_radio2;
    RadioButton d_radio0,radioAccessDownload,radioAccessView;
    TextView getCount, ib_upload, tv_file_name,smscount,tv_upload_pdf;
    String documentType = "0";
    String inputids = "";
    EditText et_title;
    ProgressDialog pd;
    String accessFlag="0";
    String moduleName = "";
    public static int count_write_documents = 0;
    private static final DecimalFormat format = new DecimalFormat("#.##");
    ArrayList<DirectoryData> listaddmemberdata = new ArrayList<>();
    ArrayList<SubGoupData> listaddsubgrp = new ArrayList<>();

    private SingleDateAndTimePickerDialog datetimePicker,datetimeForReminder;
    SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String publishDate="",expiryDate="";

    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
    private String uploadedpdfid = "0";
    String displayName = null;
    String selectedImagePath;
      CheckBox cb_noti_all, cb_noti_nonsmart;
    String sendSMSAll = "0"; // 0- Off 1- On
    String sendSMSNonSmartPh = "0"; // 0- Off 1- On

    // Code for publish date and expiry date

    TextView tv_publishDate,tv_publishTime,tv_expiryDate,tv_expiryTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document_upload);

        listview = (ListView) findViewById(R.id.listView);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        // iv_backbutton.setVisibility(View.GONE);
        moduleName = PreferenceManager.getPreference(this, PreferenceManager.MODUEL_NAME, "Document");
        tv_title.setText("Upload "+moduleName);
        ll_delete=(LinearLayout)findViewById(R.id.ll_delete);
        d_radio0 = (RadioButton) findViewById(R.id.d_radio0);
        d_radio1 = (RadioButton) findViewById(R.id.d_radio1);
        d_radio2 = (RadioButton) findViewById(R.id.d_radio2);

        radioAccessView = (RadioButton) findViewById(R.id.access_radio0);
        radioAccessDownload = (RadioButton) findViewById(R.id.access_radio1);

        getCount = (TextView) findViewById(R.id.getCount);

        iv_edit = (ImageView) findViewById(R.id.iv_edit);
        et_title = (EditText) findViewById(R.id.et_title);

        tv_getCount = (TextView) findViewById(R.id.getCount);
        tv_file_name = (TextView) findViewById(R.id.tv_file_name);
        smscount = (TextView) findViewById(R.id.smscount);
        tv_upload_pdf = (TextView) findViewById(R.id.tv_upload_pdf);


        ib_upload = (TextView) findViewById(R.id.ib_upload);
        cb_noti_nonsmart = (CheckBox) findViewById(R.id.cb_noti_nonsmart);
        cb_noti_all = (CheckBox) findViewById(R.id.cb_noti_all);



        d_radio0.setChecked(true);

        // code for Publish and expiry date time

        tv_publishDate = (TextView) findViewById(R.id.tv_publishDate);
        tv_publishTime = (TextView) findViewById(R.id.tv_publishTime);
        tv_expiryDate = (TextView) findViewById(R.id.tv_expiryDate);
        tv_expiryTime = (TextView) findViewById(R.id.tv_expiryTime);
        clearselectedtext();
        init();


    }

    private void clearselectedtext() {
        getCount.setText("");
        iv_edit.setVisibility(View.GONE);
    }

    private void init() {

      /* radioAccessDownload.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(radioAccessDownload.isChecked())
               {
                   accessFlag="1";
               }
               else
                    radioAccessView.setChecked(false);
                   accessFlag = "0";
           }
       });

        radioAccessView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioAccessView.isChecked())
                {
                    accessFlag="1";
                }
                else
                    radioAccessDownload.setChecked(false);
                accessFlag = "0";
            }
        });*/


        smscount.setText(Utils.smsCount);
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
        tv_upload_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                } else {
                    showFileChooser();
                }
            }
        });
        ib_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation() == true) {
                 //   webservices();
                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        // Avaliable
                        webservices();
                    } else {
                        Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                        // Not Available...
                    }
                }

                //Toast.makeText(Documents_upload.this, "Done", Toast.LENGTH_LONG).show();
            }
        });

        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (documentType.equals("2")) {
                    Intent i = new Intent(Documents_upload.this, AddMembers.class);
                    //i.putParcelableArrayListExtra("name1", memberData);
                    i.putParcelableArrayListExtra("selected_memberdata", listaddmemberdata);
                    startActivityForResult(i, 3);
                } else if (documentType.equals("1")) {
                    /*Intent i = new Intent(Documents_upload.this, SubGroupList.class);
                    //i.putParcelableArrayListExtra("name1", memberData);
                    i.putParcelableArrayListExtra("selected_subgrpdata", listaddsubgrp);
                    startActivityForResult(i, 1);*/

                    Intent subgrp = new Intent(Documents_upload.this, NewGroupSelectionActivity.class);
                    subgrp.putExtra("flag_addsubgrp", "1");
                    subgrp.putExtra("selected", selectedsubgrp);
                    subgrp.putExtra("edit", "1");
                    startActivityForResult(subgrp, 1);
                }

            }
        });

        tv_publishDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  if (valid(1))
                datepicker(tv_publishDate);

            }
        });

        tv_publishTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTimePicker(tv_publishTime,2);
            }
        });

        tv_expiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker(tv_expiryDate);
            }
        });

        tv_expiryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTimePicker(tv_expiryTime,3);
            }
        });

        ll_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadedpdfid = "0";
                tv_file_name.setText("");
                ll_delete.setVisibility(View.GONE);
            }
        });
    }



    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.d_radio0:
                if (checked) {
                    //set inch button to unchecked
                    documentType = "0";
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
                    Intent subgrp = new Intent(Documents_upload.this, NewGroupSelectionActivity.class);
                    subgrp.putExtra("flag_addsubgrp", "1");
                    startActivityForResult(subgrp, 1);
                }
                //startActivity(subgrp);
                break;

            case R.id.d_radio2:
                if (checked) {
                    d_radio2.setChecked(false);
                    Intent i = new Intent(Documents_upload.this, AddMembers.class);
                    startActivityForResult(i, 3);
                 /*   d_radio2.setEnabled(false);
                    d_radio1.setEnabled(true);
                    d_radio0.setChecked(false);
                    d_radio1.setChecked(false);*/
                }
                // d_radio2.setEnabled(false);

                break;

            case R.id.access_radio0:
                if(checked)
                {
                    accessFlag = "0";
                    radioAccessDownload.setChecked(false);
                    Log.e("*********","---- ACCESS FLAG ----"+accessFlag);
                }

                break;

            case R.id.access_radio1:
                if(checked)
                {
                    accessFlag = "1";
                    radioAccessView.setChecked(false);

                    Log.e("*********","---- ACCESS FLAG ----"+accessFlag);
                }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void webservices() {
        if ( !radioAccessView.isChecked() && !radioAccessDownload.isChecked()) {
            Toast.makeText(Documents_upload.this, "Please select document access type", Toast.LENGTH_LONG).show();
            return;
        }

        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("docID", "0"));
        arrayList.add(new BasicNameValuePair("docType", documentType));
        arrayList.add(new BasicNameValuePair("docAccessType", accessFlag));
        arrayList.add(new BasicNameValuePair("publishDate",publishDate));
        arrayList.add(new BasicNameValuePair("expiryDate",expiryDate));
        arrayList.add(new BasicNameValuePair("docTitle", et_title.getText().toString()));
        arrayList.add(new BasicNameValuePair("memID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));
        arrayList.add(new BasicNameValuePair("grpID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("inputIDs", inputids));
        arrayList.add(new BasicNameValuePair("documentFileId", uploadedpdfid));

        String isSubGroupAdmin = "0";  // means no
        String isAdmin = PreferenceManager.getPreference(Documents_upload.this, PreferenceManager.IS_GRP_ADMIN, "No");
        if (isAdmin.equalsIgnoreCase("partial")) {
            isSubGroupAdmin = "1";  // means yes
        }
        arrayList.add(new BasicNameValuePair("isSubGrpAdmin", isSubGroupAdmin));


        //arrayList.add(new BasicNameValuePair("memID", "1"));
        // arrayList.add(new BasicNameValuePair("grpID","1"));

//        arrayList.add(new BasicNameValuePair("sendSMSNonSmartPh", sendSMSNonSmartPh));
//        arrayList.add(new BasicNameValuePair("sendSMSAll", sendSMSAll));

        Log.d("Response", "PARAMETERS " + Constant.AddDocument + " :- " + arrayList.toString());
        new WebConnectionAsyncLogin(Constant.AddDocument, arrayList, Documents_upload.this).execute();
    }

    public class WebConnectionAsyncLogin extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(Documents_upload.this, R.style.TBProgressBar);

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
                count_write_documents++;
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
                getresult(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }
    }

    private void getresult(String val) {
        try {
            JSONObject jsonObj = new JSONObject(val);
            JSONObject ActivityResult = jsonObj.getJSONObject("TBAddDocumentResult");
            final String status = ActivityResult.getString("status");
            if (status.equals("0")) {
                String msg = ActivityResult.getString("message");

                Log.d("Touchbase", "*************** " + status);
                Log.d("Touchbase", "*************** " + msg);
                Intent intent = new Intent();
                setResult(2, intent);
                Toast.makeText(Documents_upload.this, "Uploaded successfully", Toast.LENGTH_LONG).show();
                finish(); //finishing activity
            } else {
                Toast.makeText(Documents_upload.this, "Failed to upload. Please try again", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3) {

           /* String cnt = String.valueOf(data.getIntExtra("getcount", 3));
            tv_getCount.setText("You have added " + cnt + " members");
            memberData = data.getParcelableArrayListExtra("name");
            // Log.d("array", "******************" + memberData + "\n");*/


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

                documentType = "2";
                Log.d("Touchnase", "Arrat " + inputids);
                tv_getCount.setText("You have added " + count + " members");
                iv_edit.setVisibility(View.VISIBLE);
            }


        }

        if (requestCode == 1) {

            if (resultCode == Activity.RESULT_OK) {

                listaddsubgrp = data.getParcelableArrayListExtra("result");
                if (listaddsubgrp.size() == 0)
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
                documentType = "1";
                tv_getCount.setText("You have added " + count + " sub groups");
                iv_edit.setVisibility(View.VISIBLE);
            }
        } else if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                new sendFile().execute(data);
//                Uri uri = data.getData();
//                String uriString = uri.toString();
//
//                //MEDIA GALLERY
//                selectedImagePath = Utils.getPath(getApplicationContext(), uri);
//                File myFile = new File(selectedImagePath);
//
//                final double length = myFile.length();
//
//                double size=Double.parseDouble(format.format(length / 1048576));
//                Log.d("***********", "-----" + String.valueOf(size));
//                if(size>10){
//                    Utils.showToastWithTitleAndContext(Documents_upload.this,"File size must not be greater than 10 MB");
//                }else {
//                    if (uriString.startsWith("content://")) {
//                        Cursor cursor = null;
//                        try {
//                            cursor = Documents_upload.this.getContentResolver().query(uri, null, null, null, null);
//                            if (cursor != null && cursor.moveToFirst()) {
//                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
//                                Log.d("-----", "-----" + displayName);
//
//                                //link.setText(wpath);
//
//                                //--------
//                                // uploadedpdfid = Utils.doPdfFileUpload(new File(selectedImagePath), "documentsafe"); // Upload File to server
//                            }
//                        } finally {
//                            cursor.close();
//                        }
//                    } else if (uriString.startsWith("file://")) {
//                        displayName = myFile.getName();
//                    }
//                    if(displayName!=null && !displayName.isEmpty()){
//                        tv_file_name.setText(displayName);
//                        Log.d("TOUCHBASE", "Uploaded file name---" + displayName);
//                        Log.d("TOUCHBASE", "PATH TO UPLOAD FILE---" + selectedImagePath);
//                        pd = ProgressDialog.show(Documents_upload.this, "", "Loading...", false);
//                        Thread thread = new Thread(new Runnable() {
//                            public void run() {
//                                uploadedpdfid = Utils.doPdfFileUpload(new File(selectedImagePath), "event"); // Upload File to server
//                                runOnUiThread(new Runnable() {
//                                    public void run() {
//                                        if (pd.isShowing())
//                                            pd.dismiss();
//
//                                        Log.d("TOUCHBASE", "FILE UPLOAD ID  " + uploadedpdfid);
//                                        if (uploadedpdfid == "0") {
//                                            Toast.makeText(Documents_upload.this, "File upload failed, Please Try Again!", Toast.LENGTH_LONG).show();
//                                            tv_file_name.setText("");
//                                        } else {
//                                            Toast.makeText(Documents_upload.this, "Uploaded Successfully", Toast.LENGTH_LONG).show();
//                                        }
//                                        //Toast.makeText(Register.this, "Verify your account through the registered email id", Toast.LENGTH_LONG).show();
//                                    }
//                                });
//                            }
//                        });
//                        thread.start();
//                    }
//                }

            }

        }


    }

    public class sendFile extends AsyncTask<Intent,Void,String>{

        ProgressDialog progressDialog;
        String displayName="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog= new ProgressDialog(Documents_upload.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Intent... param) {
            Intent data=param[0];
            double length = 0;

            Uri uri = data.getData();
            String uriString = uri.toString();

            //MEDIA GALLERY
            selectedImagePath = Utils.getPath(getApplicationContext(), uri);
            File myFile = new File(selectedImagePath);
            //  Log.d("***********", "-----" + myFile);
//
            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = Documents_upload.this.getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        Log.d("-----", "-----" + displayName);

                        length = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));

                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
                length = myFile.length();
            }

            double size=Double.parseDouble(format.format(length / 1048576));
            Log.d("***********", "-----" + String.valueOf(size));
            if(size>10){
                return "filesize";
            }else {
                String result="";
                if(displayName!=null && !displayName.isEmpty()){
                    //link.setText(wpath);
//                    tv_name_pdf.setText(displayName);

                        /*    Intent i = new Intent(getApplicationContext(), E_BulletineAdapter.class);
                            i.putExtra("file name", displayName);
                            startActivity(i);*/


                    String filenameArray[] = displayName.split("\\.");
                    String extension = filenameArray[filenameArray.length-1];
                    Log.d("***********", "-----" + extension);

                        uploadedpdfid = Utils.doPdfFileUpload(new File(selectedImagePath), "ebulletin"); // Upload File to server
                        Log.d("TOUCHBASE", "FILE UPLOAD ID  " + uploadedpdfid);
                        if (uploadedpdfid != "0") {
                            result= uploadedpdfid;
//                            Toast.makeText(AddE_bulletin.this, "Uploaded Successfully", Toast.LENGTH_LONG).show();

                        }
//                        Toast.makeText(AddE_bulletin.this, "Only “Pdf” files can be shared as an e-Bulletin.", Toast.LENGTH_LONG).show();
                }
                return result;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            if(s!=null && !s.isEmpty()){
                if(s.equalsIgnoreCase("filesize")){
                    Utils.showToastWithTitleAndContext(Documents_upload.this,"File size must not be greater than 10 MB");
                }
                else {
                    ll_delete.setVisibility(View.VISIBLE);
                    tv_file_name.setText(displayName);
                    uploadedpdfid=s;
                }
            }else {
                tv_file_name.setText("");
                Utils.showToastWithTitleAndContext(Documents_upload.this,getString(R.string.msgRetry));
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //   File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/example.pdf");
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"), 10);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Could not connect to the File Manager",
                    Toast.LENGTH_SHORT).show();
        }
    }


    public boolean validation() {

        if (et_title.getText().toString().trim().matches("") || et_title.getText().toString().trim() == null) {
            Toast.makeText(Documents_upload.this, "Please enter a Document “Title”", Toast.LENGTH_LONG).show();
            return false;
        }

        if (tv_file_name.getText().toString().trim().matches("") || tv_file_name.getText().toString().trim() == null) {
            Toast.makeText(Documents_upload.this, "Select a Document to Share!", Toast.LENGTH_LONG).show();
            return false;
        }

//        if (tv_publishDate.getText().toString().trim().matches("") || tv_publishDate.getText().toString().trim() == null) {
//            Toast.makeText(Documents_upload.this, "Please enter a Publish Date", Toast.LENGTH_LONG).show();
//            return false;
//        }

        if (tv_publishTime.getText().toString().trim().matches("") || tv_publishTime.getText().toString().trim() == null) {
            Toast.makeText(Documents_upload.this, "Please enter a Publish Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }

//        if (tv_expiryDate.getText().toString().trim().matches("") || tv_expiryDate.getText().toString().trim() == null) {
//            Toast.makeText(Documents_upload.this, "Please enter an Expiry Date", Toast.LENGTH_LONG).show();
//            return false;
//        }

        if (tv_expiryTime.getText().toString().trim().matches("") || tv_expiryTime.getText().toString().trim() == null) {
            Toast.makeText(Documents_upload.this, "Please enter an Expiry Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }

        /*if (!(compare_date(tv_expiryDate.getText().toString() + " " + tv_expiryTime.getText().toString(),
                tv_publishDate.getText().toString() + " " + tv_publishTime.getText().toString())).equals("1")) {
            // Toast.makeText(AddEvent.this, "Please make the Expiry Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
            Toast.makeText(Documents_upload.this, "Please make the Expiry Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
            return false;
        }*/

//        String expiryDate = tv_expiryDate.getText().toString() + " " + tv_expiryTime.getText().toString();
//
//        String publishDate = tv_publishDate.getText().toString() + " " + tv_publishTime.getText().toString();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

        try {
            if (DateHelper.compareDate(publishDate, currentDate) <= 0 ) {
                // Toast.makeText(AddEvent.this, "Please make the Expiry Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
                Toast.makeText(Documents_upload.this, "Please make the publish date & time greater than the current date & time", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(Documents_upload.this, "Dates are not in proper format", Toast.LENGTH_LONG).show();
        }
        try {
            if ( DateHelper.compareDate(publishDate, expiryDate) >= 0 ) {
                Toast.makeText(Documents_upload.this, "Please make the Expiry Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(Documents_upload.this, "Dates are not in proper format", Toast.LENGTH_LONG).show();
        }


        try {
            if (DateHelper.compareDate(publishDate, currentDate) <= 0 ) {
                // Toast.makeText(AddEvent.this, "Please make the Expiry Date & Time greater than the Publish Date & Time", Toast.LENGTH_LONG).show();
                Toast.makeText(Documents_upload.this, "Please make the publish date & time greater than the current date & time", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(Documents_upload.this, "Dates are not in proper format", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    // Code for publish and expry date

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

    public void ShowTimePicker(final TextView time_text,final int module) {
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

        datetimePicker= new SingleDateAndTimePickerDialog.Builder(Documents_upload.this).build();
//                .bottomSheet()
//                .curved()
        datetimePicker.setDefaultDate(date);

        datetimePicker.setMinDateRange(new Date());
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
            Utils.popupback(Documents_upload.this);

        }
    }
}