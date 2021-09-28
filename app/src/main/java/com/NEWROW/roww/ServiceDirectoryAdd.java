package com.NEWROW.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.MarshMallowPermission;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER1 on 19-07-2016.
 */
public class ServiceDirectoryAdd extends Activity {

    String moduleName = "";
    private int mRequestCode1 = 100;
    private int mRequestCode2 = 200;
    String service_Id = null;

    TextView tv_title;
    ImageView iv_backbutton,iv_directory_photo;
    private ImageView iv_actionbtn;
    private String hasimageflag = "0";

    LinearLayout ll_photo,ll_title,ll_description,ll_contactNo1,ll_contactNo2,ll_paxNo,ll_address,ll_keyword;
    EditText et_title,et_desc,et_contactNo1;
    EditText et_contactNo2,et_paxNo,et_email,et_address,et_keywords;
    ImageView et_photo;
    TextView tv_add,tv_cancel,tv_countryCode1,tv_countryCode2;
    int flag = 1;
    String serviceDir_Id,serId ;
    private String flag_callwebsercie = "0";
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.service_directory_add);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn.setVisibility(View.VISIBLE);
        // iv_backbutton.setVisibility(View.GONE);
        moduleName = PreferenceManager.getPreference(this, PreferenceManager.MODUEL_NAME, "Service Directory");
        tv_title.setText(moduleName);

        et_photo = (ImageView) findViewById(R.id.iv_directory_photo);
        et_title = (EditText)findViewById(R.id.et_directoryTitle);
        et_desc = (EditText)findViewById(R.id.et_directoryDesc);
        et_contactNo1 = (EditText)findViewById(R.id.et_directoryContactNumber1);
        et_contactNo2 = (EditText)findViewById(R.id.et_directoryContactNumber2);
        et_paxNo = (EditText)findViewById(R.id.et_pax);
        et_email = (EditText)findViewById(R.id.et_email);
        et_address = (EditText)findViewById(R.id.et_address);
        et_keywords = (EditText)findViewById(R.id.et_keyword);
        iv_directory_photo = (ImageView)findViewById(R.id.iv_directory_photo);

        tv_countryCode1 = (TextView)findViewById(R.id.tv_directoryCountryCode1);
        tv_countryCode2 = (TextView)findViewById(R.id.tv_directoryCountryCode2);
        tv_add = (TextView)findViewById(R.id.tv_add);
        tv_cancel = (TextView)findViewById(R.id.tv_cancel);

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            serviceDir_Id = intent.getString("serviceDirId"); // Created Group ID
            serId = serviceDir_Id;
            Log.d("======","11111111111111......."+serviceDir_Id);
            if (InternetConnection.checkConnection(getApplicationContext())) {
                // Avaliable
                tv_add.setText("Update");
                webservices_update();
            } else {
                Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                // Not Available...
            }


        }

        init();
    }

    private void init() {
        iv_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.popupback(ServiceDirectoryAdd.this);
            }

        });

        tv_countryCode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ServiceDirectoryAdd.this, SelectCountry.class);
                startActivityForResult(i, mRequestCode1);
            }
        });

        tv_countryCode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ServiceDirectoryAdd.this, SelectCountry.class);
                startActivityForResult(i, mRequestCode2);
            }
        });

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*    webservices();
                Log.d("=========","=====Web Service gets called ======");
*/
                webservices();
               /* if (validation() == true) {
                    //   webservices();
                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        // Avaliable
                        webservices();
                    } else {
                        Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                        // Not Available...
                    }
                }*/

                //Toast.makeText(AddMemberToGroup.this, "Done", Toast.LENGTH_LONG).show();



                    Intent intent = new Intent();
                    setResult(1, intent);
                    finish();

            }
        });


        iv_directory_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("--------","---Photo clicked----");

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
    }

    private void selectImage() {

        final CharSequence[] options;

        if (hasimageflag.equals("0")) {
            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
            hasimageflag = "1";
        } else {
            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Remove Photo", "Cancel"};

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ServiceDirectoryAdd.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
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
                    if(serviceDir_Id.equals("0")){
                        iv_directory_photo.setImageResource(R.drawable.edit_image);

                    }else{
                       // remove_photo_webservices();
                    }
                    //dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mRequestCode1 && resultCode == RESULT_OK) {
                tv_countryCode1.setText(data.getStringExtra("countryCode"));
                tv_countryCode1.setTag(data.getStringExtra("countryid"));

           /* else {
                tv_countryCode2.setText(data.getStringExtra("countryCode"));
                tv_countryCode2.setTag(data.getStringExtra("countryid"));
            }*/

        }
        else if(requestCode == mRequestCode2 && requestCode == RESULT_OK)
        {
            tv_countryCode2.setText(data.getStringExtra("countryCode"));
            tv_countryCode2.setTag(data.getStringExtra("countryid"));
        }
    }


    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        if (tv_add.getText().toString().equals("Update")) {
            arrayList.add(new BasicNameValuePair("serviceId", serviceDir_Id));
            Log.d("@@@@@@@@@", "@@@@@@@@@" + serviceDir_Id);
        } else {
            arrayList.add(new BasicNameValuePair("serviceId", "0"));
        }

        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("memberName", et_title.getText().toString()));
        arrayList.add(new BasicNameValuePair("description", et_desc.getText().toString()));
        arrayList.add(new BasicNameValuePair("image", ""));
        arrayList.add(new BasicNameValuePair("countryCode1",tv_countryCode1.getText().toString()));
        arrayList.add(new BasicNameValuePair("mobileNo1", et_contactNo1.getText().toString()));
        arrayList.add(new BasicNameValuePair("countryCode2", tv_countryCode2.getText().toString()));
        arrayList.add(new BasicNameValuePair("mobileNo2", et_contactNo2.getText().toString()));
        arrayList.add(new BasicNameValuePair("paxNo", et_paxNo.getText().toString()));

        arrayList.add(new BasicNameValuePair("email", et_email.getText().toString()));
        arrayList.add(new BasicNameValuePair("keywords", et_keywords.getText().toString()));
        arrayList.add(new BasicNameValuePair("address", et_address.getText().toString()));

        arrayList.add(new BasicNameValuePair("latitude", ""));
        arrayList.add(new BasicNameValuePair("longitude", ""));
        arrayList.add(new BasicNameValuePair("createdBy", "1"));

        //  flag_callwebsercie = "1";
        Log.d("Response", "PARAMETERS " + Constant.AddServiceDirectory + " :- " + arrayList.toString());
        new WebConnectionAsyncAdd(Constant.AddServiceDirectory, arrayList, ServiceDirectoryAdd.this).execute();
    }

    public class WebConnectionAsyncAdd extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
         ProgressDialog progressDialog = new ProgressDialog(ServiceDirectoryAdd.this, R.style.TBProgressBar);


        public WebConnectionAsyncAdd(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
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
            progressDialog.hide();
            //Log.d("response","Do post"+ result.toString());
            if (result != "") {
                getresult(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }
    }

    private void getresult(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject ActivityResult = jsonObj.getJSONObject("TBAddServiceResult");
            final String status = ActivityResult.getString("status");
            if (status.equals("0")) {
                String msg = ActivityResult.getString("message");

                Log.d("Touchbase", "*************** " + status);
                Log.d("Touchbase", "*************** " + msg);

                Intent intent = new Intent();
                setResult(1, intent);
                finish();//finishing activity

            }else{
                Toast.makeText(ServiceDirectoryAdd.this, "Adding Member Failed please Contact Administrator ", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }


    //==================================Update=======================================


    private void webservices_update() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("serviceDirId", serviceDir_Id));
        Log.d("@@@@@@@@@","@@@@@@@@@"+serviceDir_Id);
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
       /* arrayList.add(new BasicNameValuePair("memberProfileID", memberProfileID));
        flag_webservice = "1";*/
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();

        flag_callwebsercie="2";

        Log.d("Response", "PARAMETERS " + Constant.GetServiceDirectoryDetails + " :- " + arrayList.toString());
        new WebConnectionAsyncUpdate(Constant.GetServiceDirectoryDetails, arrayList, ServiceDirectoryAdd.this).execute();
    }


    public class WebConnectionAsyncUpdate extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(ServiceDirectoryAdd.this, R.style.TBProgressBar);

        public WebConnectionAsyncUpdate(String url, List<NameValuePair> argList, Context ctx) {
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
            if (progressDialog !=null && progressDialog.isShowing())
            progressDialog.dismiss();

            Log.d("response","Do post"+ result.toString());
            if (result != "") {

                update_result(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }


    private  void update_result(String result)
    {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("TBServiceDirectoryListResult");
            final String status = EventResult.getString("status");
            if (status.equals("0")) ;
            {
                JSONArray EventListResdult = EventResult.getJSONArray("ServiceDirListResult");
                for (int i = 0; i < EventListResdult.length(); i++) {
                    JSONObject object = EventListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("ServiceDirList");


                    et_title.setText(objects.getString("serviceMemberName").toString());
                    et_desc.setText(objects.getString("serviceDescription").toString());
                    et_contactNo1.setText(objects.getString("serviceMobile1").toString());
                    et_contactNo2.setText(objects.getString("serviceMobile2").toString());
                    et_paxNo.setText(objects.getString("servicePaxNo").toString());
                    et_email.setText(objects.getString("serviceEmail").toString());
                    et_address.setText(objects.getString("serviceAddress").toString());


                   /* if (objects.has("announImg")) {
                        if (objects.getString("announImg").toString().equals("") || objects.getString("announImg").toString() == null) {
                            linear_image.setVisibility(View.GONE);
                        } else {
                            progressbar.setVisibility(View.VISIBLE);
                            Picasso.with(Announcement_details.this).load(objects.getString("announImg").toString())
                                    .placeholder(R.drawable.imageplaceholder)
                                    .into(iv_announcementimg, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            progressbar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });
                        }
                    }*/

                }


            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

//=====================================Validation ==================================


    /* public boolean validation() {


        if (name.getText().toString().trim().matches("") || name.getText().toString().trim() == null) {
            Toast.makeText(AddMemberToGroup.this, "Please enter a Name ", Toast.LENGTH_LONG).show();
            return false;
        }
        if(et_mobile.getText().toString().trim().matches("") || et_mobile.getText().toString().trim()== null ) {

            Toast.makeText(getApplicationContext(), "Please enter a Mobile Number ", Toast.LENGTH_LONG).show();
            return false;
        }
        if(MOBILE_PATTERN.matcher(et_mobile.getText().toString()).matches()==false)
        {
            Toast.makeText(getApplicationContext(), "Please enter a valid Mobile Number", Toast.LENGTH_LONG).show();
            return false;
        }

        if(et_email.getText().toString().length()!=0 )
        {
            if(EMAIL_PATTERN.matcher(et_email.getText().toString()).matches()==false){

                Toast.makeText(getApplicationContext(), "Enter Valid Email Address ", Toast.LENGTH_LONG).show();
                return false;

            }else{

                return true;
            }
        }
        return true;
    }*/
    @Override
    public void onBackPressed() {
        Utils.popupback(ServiceDirectoryAdd.this);
        //
        // super.onBackPressed();
    }


}
