package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Data.DirectoryData;
import com.SampleApp.row.Data.SubGoupData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.MarshMallowPermission;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 18-11-2016.
 */
public class DTEditAlbumActivity extends Activity {
    TextView tv_title, tv_add, tv_getCount, tv_cancel;
    String albumId, albumName, albumDescription;
    String albumImage = "";
    ImageView iv_backbutton, iv_image, iv_edit;
    EditText edt_title, edt_description;
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
    ProgressDialog pd;
    private String uploadedimgid = "0";
    String galleryType = "0";
    RadioButton d_radio0, d_radio1, d_radio2;
    ArrayList<DirectoryData> listaddmemberdata = new ArrayList<>();
    private String edit_gallery_selectedids = "0";
    ArrayList<SubGoupData> listaddsubgrp = new ArrayList<>();
    String inputids = "";
    ArrayList<String> selectedsubgrp;
    private String edit_album_selectedids = "0";
    //private RadioButton rbInClub, rbPublic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dt_edit_album);

        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("Edit Album");
        edt_title = (EditText) findViewById(R.id.et_galleryTitle);
        edt_description = (EditText) findViewById(R.id.et_evetDesc);
        iv_image = (ImageView) findViewById(R.id.iv_event_photo);
        tv_add = (TextView) findViewById(R.id.tv_done);
        iv_edit = (ImageView) findViewById(R.id.iv_edit);
        tv_getCount = (TextView) findViewById(R.id.getCount);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);

        d_radio0 = (RadioButton) findViewById(R.id.d_radio0);
        d_radio1 = (RadioButton) findViewById(R.id.d_radio1);
        d_radio2 = (RadioButton) findViewById(R.id.d_radio2);
        // d_radio0.setChecked(true);


        Intent i = getIntent();
        albumId = i.getStringExtra("albumId");
        albumName = i.getStringExtra("albumname");
        albumDescription = i.getStringExtra("description");
        albumImage = i.getStringExtra("albumImage");
        loadFromServer();
        edt_title.setText(albumName);
        edt_description.setText(albumDescription);


        if (albumImage.equalsIgnoreCase("") || albumImage.equalsIgnoreCase("null")) {

        } else {
            Picasso.with(DTEditAlbumActivity.this).load(albumImage)
                    .placeholder(R.drawable.dashboardplaceholder)
                    .into(iv_image);
        }
        init();

    }

    public void onRadioButtonClicked(View view) {

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.d_radio0:
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
                Intent subgrp = new Intent(DTEditAlbumActivity.this, NewGroupSelectionActivity.class);
                subgrp.putExtra("flag_addsubgrp", "1");
                startActivityForResult(subgrp, 1);
                //startActivity(subgrp);
                break;

            case R.id.d_radio2:
                if (checked) {
                    d_radio2.setChecked(false);
                    Intent i = new Intent(DTEditAlbumActivity.this, AddMembers.class);
                    startActivityForResult(i, 3);
                }
                // d_radio2.setEnabled(false);
             /* d_radio1.setEnabled(true);
                d_radio0.setChecked(false);
                d_radio1.setChecked(false);*/
                break;
        }
    }

    public void init() {
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                finish();
            }
        });
        iv_image.setOnClickListener(new View.OnClickListener() {
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

        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (galleryType.equals("2")) {
                    Intent i = new Intent(DTEditAlbumActivity.this, AddMembers.class);
                    //i.putParcelableArrayListExtra("name1", memberData);
                    i.putParcelableArrayListExtra("selected_memberdata", listaddmemberdata);
                    i.putExtra("edit_announcement_selectedids", edit_album_selectedids);
                    startActivityForResult(i, 3);
                } else if (galleryType.equals("1")) {
//                    Intent i = new Intent(EditAlbumActivity.this, SubGroupList.class);
//                    //i.putParcelableArrayListExtra("name1", memberData);
//                    i.putParcelableArrayListExtra("selected_subgrpdata", listaddsubgrp);
//                    i.putExtra("edit_gallery_selectedids", edit_gallery_selectedids);
//                    startActivityForResult(i, 1);

                    Intent subgrp = new Intent(DTEditAlbumActivity.this, NewGroupSelectionActivity.class);
                    subgrp.putExtra("flag_addsubgrp", "1");
                    subgrp.putExtra("selected", selectedsubgrp);
                    subgrp.putExtra("edit", "1");
                    startActivityForResult(subgrp, 1);
                }

            }
        });


        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation() == true) {

                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        webservices();
                    } else {
                        Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    }
                }
            }
        });

    }

    public void loadFromServer() {
        if (InternetConnection.checkConnection(this))
            loadData();
        else
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
    }

    public void loadData() {

        Log.e("Touchbase", "------ loadData() called");
        String url = Constant.GetAlbumDetails;
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("albumId", albumId));

        Log.d("Request", "PARAMETERS " + Constant.GetAlbumDetails + " :- " + arrayList.toString());
        GetAlbumDetailsAsynctask task = new GetAlbumDetailsAsynctask(url, arrayList, this);
        task.execute();

    }


    @Override
    public void onBackPressed() {
        Utils.popupback(DTEditAlbumActivity.this);
    }

    public class GetAlbumDetailsAsynctask extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(DTEditAlbumActivity.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;


        public GetAlbumDetailsAsynctask(String url, List<NameValuePair> argList, Context ctx) {
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
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (result != "") {
                Log.d("Response", "calling GetAlbumDetails");

                getAlbumDetails(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }
    }

    public void getAlbumDetails(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);

            JSONObject jsonTBAlbumsListResult = jsonObj.getJSONObject("TBAlbumDetailResult");
            final String status = jsonTBAlbumsListResult.getString("status");

            if (status.equals("0")) {

                JSONArray jsonNewAlbumList = jsonTBAlbumsListResult.getJSONArray("AlbumDetailResult");

                JSONObject object = jsonNewAlbumList.getJSONObject(0);
                JSONObject jsonAlbumDetail = object.getJSONObject("AlbumDetail");
//                    String id = jsonAlbumDetail.getString("albumId");
//                    String groupId = jsonAlbumDetail.getString("groupId");
//                    String tle = jsonAlbumDetail.getString("albumTitle");
//                    String desc = jsonAlbumDetail.getString("albumDescription");
                String type = jsonAlbumDetail.getString("type");


                if (type.equalsIgnoreCase("0")) {
                    d_radio0.setChecked(true);
                    galleryType = "0";
                    clearselectedtext();
                } else if (type.equalsIgnoreCase("1")) {
                    galleryType = "1";
                    d_radio1.setChecked(true);
                    edit_album_selectedids = jsonAlbumDetail.getString("memberIds").toString();
                    String[] ids = edit_album_selectedids.split(",");
                    selectedsubgrp = new ArrayList<String>();
                    for (String id : ids) {
                        selectedsubgrp.add(id);
                    }
                    inputids = jsonAlbumDetail.getString("memberIds").toString();
                    String[] mystring = edit_album_selectedids.split(",");
                    int size = mystring.length;
                    tv_getCount.setText("You have added " + size + " sub groups");
                    iv_edit.setVisibility(View.VISIBLE);


                } else {
                    galleryType = "2";
                    d_radio2.setChecked(true);
                    edit_album_selectedids = jsonAlbumDetail.getString("memberIds").toString();
                    inputids = jsonAlbumDetail.getString("memberIds").toString();
                    String[] mystring = edit_album_selectedids.split(",");
                    int size = mystring.length;
                    tv_getCount.setText("You have added " + size + " members");
                    iv_edit.setVisibility(View.VISIBLE);

                }


            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
            e.printStackTrace();
        }
    }


    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        String isSubGroupAdmin = "0";  // means no
        String isAdmin = PreferenceManager.getPreference(DTEditAlbumActivity.this, PreferenceManager.IS_GRP_ADMIN, "No");
        if (isAdmin.equalsIgnoreCase("partial")) {
            isSubGroupAdmin = "1";  // means yes
        }
        arrayList.add(new BasicNameValuePair("isSubGrpAdmin", isSubGroupAdmin));
        arrayList.add(new BasicNameValuePair("albumId", albumId));
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("moduleId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MODULE_ID)));
        arrayList.add(new BasicNameValuePair("type", galleryType));
        arrayList.add(new BasicNameValuePair("memberIds", inputids));
        arrayList.add(new BasicNameValuePair("albumTitle", edt_title.getText().toString()));
        arrayList.add(new BasicNameValuePair("albumDescription", edt_description.getText().toString()));
        arrayList.add(new BasicNameValuePair("albumImage", uploadedimgid));
        arrayList.add(new BasicNameValuePair("createdBy", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));
        String shareType = "0";

        arrayList.add(new BasicNameValuePair("shareType", shareType));
        Log.d("Response", "PARAMETERS " + Constant.AddAlbum + " :- " + arrayList.toString());
        new EditAlbumAsyncTask(Constant.AddAlbum, arrayList, DTEditAlbumActivity.this).execute();
    }


    public class EditAlbumAsyncTask extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(DTEditAlbumActivity.this, R.style.TBProgressBar);

        public EditAlbumAsyncTask(String url, List<NameValuePair> argList, Context ctx) {
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

    private void getresult(String val) {
        try {
            JSONObject jsonObj = new JSONObject(val);
            JSONObject ActivityResult = jsonObj.getJSONObject("TBAddGalleryResult");
            final String status = ActivityResult.getString("status");
            if (status.equals("0")) {
                String msg = ActivityResult.getString("message");

                Intent intent = new Intent();
                intent.putExtra("resultForEditAlbum", val);
                setResult(1, intent);
                finish();//finishing activity

                if (tv_add.getText().equals("Done")) {
                    Toast.makeText(DTEditAlbumActivity.this, "Album Updated successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DTEditAlbumActivity.this, "Album Updated successfully.", Toast.LENGTH_SHORT).show();
                }

                Log.d("Touchbase", "*************** " + status);
                Log.d("Touchbase", "*************** " + msg);

            } else {
                Toast.makeText(DTEditAlbumActivity.this, "Failed to update album. Please retry.", Toast.LENGTH_LONG).show();
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

                galleryType = "2";
                Log.d("Touchnase", "Arrat " + inputids);
                tv_getCount.setText("You have added " + count + " members");
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

                Log.d("Touchnase", "Arrat " + inputids);
                galleryType = "1";
                tv_getCount.setText("You have added " + count + " sub groups");
                iv_edit.setVisibility(View.VISIBLE);
            }

        }
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
                    Bitmap bt = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
                    iv_image.setImageBitmap(bt);

                    String path = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    // f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

                    Log.d("TOUCHBASE", "FILE PATH " + f.toString());
                    ///-------------------------------------------------------------------
                    pd = ProgressDialog.show(DTEditAlbumActivity.this, "", "Loading...", false);
                    final File finalF = f;
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            uploadedimgid = Utils.doFileUpload(new File(finalF.toString()), "gallery"); // Upload File to server
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (pd.isShowing())
                                        pd.dismiss();
                                    Log.d("TOUCHBASE", "FILE UPLOAD ID InnerThread  " + uploadedimgid);
                                    if (uploadedimgid.equals("0")) {
                                        Toast.makeText(DTEditAlbumActivity.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();
                                        iv_image.setImageResource(R.drawable.edit_image);
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

                ///-------------------------------------------------------------------
                pd = ProgressDialog.show(DTEditAlbumActivity.this, "", "Loading...", false);
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        uploadedimgid = Utils.doFileUpload(new File(picturePath), "gallery"); // Upload File to server
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (pd.isShowing())
                                    pd.dismiss();
                                if (uploadedimgid.equals("0")) {
                                    Toast.makeText(DTEditAlbumActivity.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();
                                    iv_image.setImageResource(R.drawable.edit_image);
                                } else {
                                    iv_image.setImageBitmap(thumbnail);
                                }
                                //Toast.makeText(Register.this, "Verify your account through the registered email id", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                thread.start();
            }
        }
    }


    private void selectImage() {

        final CharSequence[] options;
        options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(DTEditAlbumActivity.this);
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
                }
            }
        });
        builder.show();
    }

    public boolean validation() {
        if (edt_title.getText().toString().trim().matches("") || edt_title.getText().toString().trim() == null) {
            Toast.makeText(DTEditAlbumActivity.this, "Please enter the Album “Title”", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void clearselectedtext() {
        tv_getCount.setText("");
        iv_edit.setVisibility(View.GONE);
    }


    /*public void onRadioButtonClicked(View view) {

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.d_radio0:
                if (checked)
                    //set inch button to unchecked
                    galleryType = "0";
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
               *//*     d_radio0.setChecked(false);
                d_radio2.setChecked(false);
                d_radio1.setEnabled(false);
                d_radio2.setEnabled(true);*//*
                    d_radio1.setChecked(false);

                Intent subgrp = new Intent(EditAlbumActivity.this, NewGroupSelectionActivity.class);
                subgrp.putExtra("flag_addsubgrp", "1");
                startActivityForResult(subgrp, 1);
                //startActivity(subgrp);
                break;

            case R.id.d_radio2:
                if (checked) {
                    d_radio2.setChecked(false);
                    Intent i = new Intent(EditAlbumActivity.this, AddMembers.class);
                    startActivityForResult(i, 3);


                }

                // d_radio2.setEnabled(false);
             *//*   d_radio1.setEnabled(true);
                d_radio0.setChecked(false);
                d_radio1.setChecked(false);*//*
                break;
        }
    }*/


    public void hideKeyboard() {
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }


}
