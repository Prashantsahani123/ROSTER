package com.NEWROW.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Utils.CircleTransform;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.MarshMallowPermission;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.Utils;
import com.NEWROW.row.croputility.Crop;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 15-12-2015.
 */
public class InitialProfile extends Activity {

    ImageView iv_backbutton, iv_prof_pic;

    TextView tv_done, tv_title;
    EditText et_name, et_email, et_mobile;

    Bitmap bm;
    String ba1;
    private String uploadedimgid = "0";
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
    ProgressDialog pd;

    boolean updateProfileImage = false;
    static int Capture_Camera = 100;
    Bitmap updatedProfileImage;
    static Uri mCapturedImageURI;
    private String memberprofileid, groupId;
    String responsefromimageupload = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.initialprofile);

        iv_prof_pic = (ImageView) findViewById(R.id.iv_prof_pic);
        iv_prof_pic = (ImageView) findViewById(R.id.iv_prof_pic);

        tv_done = (TextView) findViewById(R.id.tv_done);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_backbutton.setVisibility(View.GONE);
        tv_title.setText("Profile");

        et_mobile = (EditText) findViewById(R.id.et_mobile);
        et_email = (EditText) findViewById(R.id.et_email);
        et_name = (EditText) findViewById(R.id.et_name);
       /* StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);*/


        init();

        webservices();

    }


    private void init() {
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent i = new Intent(InitialProfile.this, GroupsListingDashboard.class);
                startActivity(i);*/
                if(et_name.getText().toString().trim().length() <=0){
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "Please Enter Name ");
                }else{
                    webservices_addprofile();
                }
            }
        });
        iv_prof_pic.setOnClickListener(new View.OnClickListener() {
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
    }

    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("masterUID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));
        // arrayList.add(new BasicNameValuePair("masterUID", "1"));

        Log.d("Response", "PARAMETERS " + Constant.GetMemberDetails + " :- " + arrayList.toString());
        new WebConnectionAsync(Constant.GetMemberDetails, arrayList, InitialProfile.this).execute();
    }


    private void webservices_addprofile() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("ProfileId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));
        //  arrayList.add(new BasicNameValuePair("ProfileId", "1"));
        arrayList.add(new BasicNameValuePair("memberMobile", et_mobile.getText().toString()));
        arrayList.add(new BasicNameValuePair("memberName", et_name.getText().toString()));
        arrayList.add(new BasicNameValuePair("memberEmailid", et_email.getText().toString()));
        arrayList.add(new BasicNameValuePair("ProfilePic_Path", ""));
        arrayList.add(new BasicNameValuePair("ImageId", uploadedimgid));


        Log.d("Response", "PARAMETERS " + Constant.UpdateProfile + " :- " + arrayList.toString());
        if (InternetConnection.checkConnection(getApplicationContext())) {
            new WebConnectionAsyncupdatedata(Constant.UpdateProfile, arrayList, InitialProfile.this).execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public class WebConnectionAsync extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(InitialProfile.this, R.style.TBProgressBar);

        public WebConnectionAsync(String url, List<NameValuePair> argList, Context ctx) {
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
                getresult(result.toString());
            } else {
                Log.d("Response", "Null Resposnse");
            }
        }
    }

    public class WebConnectionAsyncupdatedata extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(InitialProfile.this, R.style.TBProgressBar);

        public WebConnectionAsyncupdatedata(String url, List<NameValuePair> argList, Context ctx) {
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
            Log.d("response", "Do post" + result.toString());
            if (result != "") {
                getresultupdate(result.toString());
            } else {
                Log.d("Response", "Null Resposnse");
            }
        }
    }


    private void getresult(String val) {
        try {
            JSONObject jsonObj = new JSONObject(val);
            JSONObject ActivityResult = jsonObj.getJSONObject("MemberListDetailResult");
            final String status = ActivityResult.getString("status");
            if (status.equals("0")) {
                JSONArray grpsarray = ActivityResult.getJSONArray("MemberDetails");
                for (int i = 0; i < grpsarray.length(); i++) {
                    JSONObject object = grpsarray.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("MemberDetail");
                    // pd.setProduct_master_id(objects.getString("productMasterId").toString());

                    et_name.setText(objects.getString("memberName").toString());
                    et_email.setText(objects.getString("memberEmailId").toString());
                    et_mobile.setText(objects.getString("mobileNo").toString());
                    et_mobile.setEnabled(false);

                    /*if (objects.getString("profilePicPath").toString().equals("")) {
                    } else {
                        Picasso.with(InitialProfile.this).load(objects.getString("profilePicPath").toString()).transform(new CircleTransform()).into(iv_prof_pic);
                    }*/

                    if(objects.has("profilePicPath")) {

                        if (objects.getString("profilePicPath").toString().equals("") || objects.getString("profilePicPath").toString() == null) {

                        } else {

                            Picasso.with(InitialProfile.this).load(objects.getString("profilePicPath").toString())
                                    .transform(new CircleTransform())
                                    .placeholder(R.drawable.profile_pic)
                                    .into(iv_prof_pic);
                            Log.d("@@@@@@@@@@@", "--------5 ");
                        }
                    }
                }

            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }

    private void getresultupdate(String val) {
        try {
            JSONObject jsonObj = new JSONObject(val);
            JSONObject ActivityResult = jsonObj.getJSONObject("UserResult");
            final String status = ActivityResult.getString("status");
            if (status.equals("0")) {
                // JSONArray grpsarray = ActivityResult.getJSONArray("MemberDetails");

                Intent ii = new Intent(InitialProfile.this, DashboardActivity.class);
                startActivity(ii);
                finish();

            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(InitialProfile.this);
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
                    startActivityForResult(intent, Capture_Camera);

                } else if (options[item].equals("Choose from Gallery")) {


                    Crop.pickImage(InitialProfile.this);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        } else if (requestCode == Capture_Camera && resultCode == RESULT_OK) {
            //----- Correct Image Rotation ----//
            Uri correctedUri = null;
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mCapturedImageURI);

                //imageBitmap = imageOrientationValidator(imageBitmap, getRealPathFromURI(mCapturedImageURI));
                correctedUri = Utils.getImageUri(imageBitmap,getApplicationContext());
                Log.d("==== Uri ===", "======" + correctedUri);

            } catch (IOException e) {
                e.printStackTrace();
            }
            beginCrop(correctedUri);
        }
    }

    /**************************************Image Crop Methods*****************************************/

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

            Uri croppedUri = Crop.getOutput(result);

            if (croppedUri != null) {

                Bitmap csBitmap = null;
                try {
                    csBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), croppedUri);


                    final String path = Utils.getRealPathFromURI(croppedUri, getApplicationContext());
                    Log.d("==== Path ===", "======" + path);

                    responsefromimageupload = Utils.doFileUploadForProfilePic(InitialProfile.this,new File(path.toString()), memberprofileid, groupId,"profile"); // Upload File to server
                    Log.d("TOUCHBASE", "RESPONSE FILE UPLOAD " + responsefromimageupload);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                updatedProfileImage = csBitmap;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                csBitmap = Bitmap.createScaledBitmap(csBitmap, 500, 500, true);
                csBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                iv_prof_pic.setImageBitmap(csBitmap);
                updateProfileImage = true;
            }

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void beginCrop(Uri source) {

        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);

    }


}

