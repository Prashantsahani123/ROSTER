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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.SampleApp.row.Utils.PreferenceManager.MY_CATEGORY;

/**
 * Created by user on 06-09-2016.
 */
public class DTAddAlbum extends Activity {
    ArrayList<String> selectedsubgrp;
    TextView tv_title, tv_cancel;
    ImageView iv_backbutton, iv_edit, iv_event_photo;
    RadioButton d_radio0, d_radio1, d_radio2;
    TextView tv_getCount, tv_add;
    String galleryType = "0";
    ArrayList<DirectoryData> listaddmemberdata = new ArrayList<>();
    ArrayList<SubGoupData> listaddsubgrp = new ArrayList<>();
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);

    private String groupType = "1";
    private RadioGroup rgShare;
    //private RadioButton rbInClub, rbPublic;

    private String hasimageflag = "0";
    private String galleryId = "0";
    private String edit_gallery_selectedids = "0";
    ProgressDialog pd;
    private String uploadedimgid = "0";
    EditText et_gallery_title, et_description;
    String inputids = "";
    private String flag_callwebsercie = "0";
    private String moduleName = "";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dt_add_album);
        context = this;
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);

        moduleName = PreferenceManager.getPreference(this, PreferenceManager.MODUEL_NAME, "Album");
        tv_title.setText("New " + moduleName);

        d_radio0 = (RadioButton) findViewById(R.id.d_radio0);
        d_radio1 = (RadioButton) findViewById(R.id.d_radio1);
        d_radio2 = (RadioButton) findViewById(R.id.d_radio2);
        d_radio0.setChecked(true);

        tv_getCount = (TextView) findViewById(R.id.getCount);
        iv_edit = (ImageView) findViewById(R.id.iv_edit);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        iv_event_photo = (ImageView) findViewById(R.id.iv_event_photo);
        tv_add = (TextView) findViewById(R.id.tv_done);

        et_gallery_title = (EditText) findViewById(R.id.et_galleryTitle);
        et_description = (EditText) findViewById(R.id.et_evetDesc);

        rgShare = (RadioGroup) findViewById(R.id.rgShare);

        groupType = PreferenceManager.getPreference(context, MY_CATEGORY, "" + Constant.GROUP_CATEGORY_CLUB);
        Utils.log("Group type is : " + groupType);
        clearselectedtext();
        init();
    }

    private void clearselectedtext() {
        tv_getCount.setText("");
        iv_edit.setVisibility(View.GONE);
    }

    private void init() {
        /*iv_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i  = new Intent(AddAlbum.this,Gallery.class);
                startActivity(i);
            }

        });*/

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (galleryType.equals("2")) {
                    Intent i = new Intent(DTAddAlbum.this, AddMembers.class);
                    //i.putParcelableArrayListExtra("name1", memberData);
                    i.putParcelableArrayListExtra("selected_memberdata", listaddmemberdata);
                    i.putExtra("edit_gallery_selectedids", edit_gallery_selectedids);
                    startActivityForResult(i, 3);
                } else if (galleryType.equals("1")) {
                    /*Intent i = new Intent(AddAlbum.this, SubGroupList.class);
                    //i.putParcelableArrayListExtra("name1", memberData);
                    i.putParcelableArrayListExtra("selected_subgrpdata", listaddsubgrp);
                    i.putExtra("edit_gallery_selectedids", edit_gallery_selectedids);
                    startActivityForResult(i, 1);*/

                    Intent subgrp = new Intent(DTAddAlbum.this, NewGroupSelectionActivity.class);
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

                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        webservices();
                    } else {
                        Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                        // Not Available...
                    }
                }
            }
        });

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
                       /*d_radio0.setChecked(false);
                        d_radio2.setChecked(false);
                        d_radio1.setEnabled(false);
                        d_radio2.setEnabled(true);*/
                    d_radio1.setChecked(false);

                Intent subgrp = new Intent(DTAddAlbum.this, NewGroupSelectionActivity.class);
                subgrp.putExtra("flag_addsubgrp", "1");
                startActivityForResult(subgrp, 1);
                //startActivity(subgrp);

                break;

            case R.id.d_radio2:
                if (checked) {
                    d_radio2.setChecked(false);
                    Intent i = new Intent(DTAddAlbum.this, AddMembers.class);
                    startActivityForResult(i, 3);
                }

                // d_radio2.setEnabled(false);
                /*
                d_radio1.setEnabled(true);
                d_radio0.setChecked(false);
                d_radio1.setChecked(false);*/
                break;
        }

    }

    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        String isSubGroupAdmin = "0";  // means no
        String isAdmin = PreferenceManager.getPreference(DTAddAlbum.this, PreferenceManager.IS_GRP_ADMIN, "No");
        if (isAdmin.equalsIgnoreCase("partial")) {
            isSubGroupAdmin = "1";  // means yes
        }
        arrayList.add(new BasicNameValuePair("isSubGrpAdmin", isSubGroupAdmin));
        arrayList.add(new BasicNameValuePair("albumId", "0"));
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("moduleId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MODULE_ID)));
        arrayList.add(new BasicNameValuePair("type", galleryType));
        arrayList.add(new BasicNameValuePair("memberIds", inputids));
        arrayList.add(new BasicNameValuePair("albumTitle", et_gallery_title.getText().toString()));
        arrayList.add(new BasicNameValuePair("albumDescription", et_description.getText().toString()));
        arrayList.add(new BasicNameValuePair("albumImage", uploadedimgid));
        arrayList.add(new BasicNameValuePair("createdBy", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));
        String shareType = "0";  // 0 means within the district only always

        arrayList.add(new BasicNameValuePair("shareType", shareType));


        flag_callwebsercie = "0";

        Log.d("Response", "PARAMETERS " + Constant.AddAlbum + " :- " + arrayList.toString());
        new WebConnectionAsyncLogin(Constant.AddAlbum, arrayList, DTAddAlbum.this).execute();
    }


    public class WebConnectionAsyncLogin extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(DTAddAlbum.this, R.style.TBProgressBar);

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
                    // getresult_addeddata(result.toString());
                } else if (flag_callwebsercie.equals("4")) {
                    //getresultOfRemovephoto(result.toString());
                }
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
                setResult(1, intent);

                if (tv_add.getText().equals("Done"))
                    Toast.makeText(DTAddAlbum.this, "Album Added successfully.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(DTAddAlbum.this, "Album updated successfully.", Toast.LENGTH_SHORT).show();

                Log.d("Touchbase", "*************** " + status);
                Log.d("Touchbase", "*************** " + msg);
                finish();//finishing activity

            } else {
                if (tv_add.getText().equals("Done"))
                    Toast.makeText(DTAddAlbum.this, "Failed to add album.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(DTAddAlbum.this, "Failed to update album.", Toast.LENGTH_SHORT).show();

            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }


    public boolean validation() {
        if (et_gallery_title.getText().toString().trim().matches("") || et_gallery_title.getText().toString().trim() == null) {
            Toast.makeText(DTAddAlbum.this, "Please enter the Album “Title”", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void selectImage() {

        final CharSequence[] options;

        if (hasimageflag.equals("0")) {
            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
            hasimageflag = "1";
        } else {
            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Remove Photo", "Cancel"};

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(DTAddAlbum.this);
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
                    if (galleryId.equals("0")) {
                        iv_event_photo.setImageResource(R.drawable.edit_image);

                    } else {
                        //remove_photo_webservices();
                    }

                }
            }
        });
        builder.show();
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
                galleryType = "1";
                tv_getCount.setText("You have added " + count + " sub groups");
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
                    Bitmap bt = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
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
                    pd = ProgressDialog.show(DTAddAlbum.this, "", "Loading...", false);
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
                                        Toast.makeText(DTAddAlbum.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();
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
                pd = ProgressDialog.show(DTAddAlbum.this, "", "Loading...", false);
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        uploadedimgid = Utils.doFileUpload(new File(picturePath), "gallery"); // Upload File to server
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (pd.isShowing())
                                    pd.dismiss();
                                if (uploadedimgid.equals("0")) {
                                    Toast.makeText(DTAddAlbum.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();
                                    iv_event_photo.setImageResource(R.drawable.edit_image);
                                } else {
                                    iv_event_photo.setImageBitmap(thumbnail);
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
}
