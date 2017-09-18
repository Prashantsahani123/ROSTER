package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.SampleApp.row.Data.ServiceDirectoryListData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.ImageCompression;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.MarshMallowPermission;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.croputility.Crop;
import com.SampleApp.row.sql.ServiceDirectoryDataModel;

/**
 * Created by USER on 22-07-2016.
 */
public class ServiceDirectoryAdd_new extends Activity {

    long time1, time2;

    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;

    String finalImagePath;
    String picturePath;
    String moduleId = "-1";
    int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));

    final Pattern MOBILE_PATTERN = Pattern.compile("[0-9]{3,20}");

    final Pattern EMAIL_PATTERN = Pattern.compile( "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+");

    final Pattern COMMA_SEPARATED_VALUES = Pattern.compile("(?<=,|^)bears(?=,|$)");

    static final int LAT_LONG_REQUEST = 1000;
    static Uri mCapturedImageURI;
    static int Capture_Camera = 300;
    private int mRequestCode1 = 100;
    private int mRequestCode2 = 200;
    String service_Id = null;

    TextView tv_title;
    ImageView iv_backbutton;
    private ImageView iv_actionbtn;
    private String uploadedimgid = "0";
    boolean isGetLanLon=false;

    String updatedOn = "";
    LinearLayout ll_photo,ll_title,ll_description,ll_contactNo1,ll_contactNo2,ll_paxNo,ll_address,ll_keyword;
    EditText et_title,et_desc,et_contactNo1;
    EditText et_contactNo2,et_paxNo,et_email,et_address,et_keywords,et_zip;
    ImageView et_photo,iv_addIcon;
    TextView tv_add,tv_cancel,tv_countryCode1,tv_countryCode2;
    int flag = 1;
    ProgressDialog pd;
    String serviceDir_Id = "0" ;
    private String hasimageflag = "0";
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
   // private String flag_callwebsercie = "0";
   String add ;

    private String location = "", sportName = "";
    private double latitude=0.0;
    private double longitude=0.0;
    String img;
    long masterUid, grpId ;

    int countryId ;
    Bitmap updatedProfileImage;
    boolean updateProfileImage = false;
    EditText et_city, et_state, et_country;
    String moduleName = "Service";
    private int orientation;
    private Bitmap rotatedBitmap,sourceBitmap;
    private String filePath;
    String categoryId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.service_directory_add);
        if(getIntent().hasExtra("categoryId")) {
            categoryId = getIntent().getExtras().getString("categoryId");
        }
        moduleId = PreferenceManager.getPreference(this, PreferenceManager.MODULE_ID, "-1");
        moduleName = PreferenceManager.getPreference(this, PreferenceManager.MODUEL_NAME, "Service");
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn.setVisibility(View.GONE);
        // iv_backbutton.setVisibility(View.GONE);
        tv_title.setText("New "+moduleName);

        et_photo = (ImageView) findViewById(R.id.iv_directory_photo);
        et_title = (EditText)findViewById(R.id.et_directoryTitle);
        et_desc = (EditText)findViewById(R.id.et_directoryDesc);
        et_contactNo1 = (EditText)findViewById(R.id.et_directoryContactNumber1);
        et_contactNo2 = (EditText)findViewById(R.id.et_directoryContactNumber2);
        et_paxNo = (EditText)findViewById(R.id.et_pax);
        et_email = (EditText)findViewById(R.id.et_email);
        et_address = (EditText)findViewById(R.id.et_address);
        et_keywords = (EditText)findViewById(R.id.et_keyword);

        et_city = (EditText) findViewById(R.id.et_city);
        et_state = (EditText) findViewById(R.id.et_state);
        et_country = (EditText) findViewById(R.id.et_country);
        et_zip = (EditText)findViewById(R.id.et_zip);


        tv_countryCode1 = (TextView)findViewById(R.id.tv_directoryCountryCode1);
        tv_countryCode2 = (TextView)findViewById(R.id.tv_directoryCountryCode2);
        tv_add = (TextView)findViewById(R.id.tv_add);
        tv_cancel = (TextView)findViewById(R.id.tv_cancel);
        iv_addIcon = (ImageView)findViewById(R.id.iv_addIcon);


        tv_countryCode1.setTag("");
        tv_countryCode2.setTag("");
        masterUid = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)); // line added by lekha
        grpId = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID));

        updatedOn = PreferenceManager.getPreference(ServiceDirectoryAdd_new.this, "ServiceDirectoryUpdatedOn_"+moduleId+"_"+grpId, "1970/01/01 0:0:0");

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            if (getIntent().hasExtra("serviceDirId")){
                serviceDir_Id = intent.getString("serviceDirId"); // Created Group ID

            ServiceDirectoryListData myservice = new ServiceDirectoryDataModel(ServiceDirectoryAdd_new.this).serviceDirectoryDetail(Long.parseLong(serviceDir_Id));

            System.out.println("Myservice : " + myservice);


            et_title.setText(myservice.getMemberName());
            img = myservice.getImage();
            if (img.trim().length() == 0 || img.equals("") || img == null || img.isEmpty()) {
                hasimageflag = "0";
            } else {
                hasimageflag = "1";

                Log.d("@@@@@", "0000" + picturePath);

                Log.d("==img====", "0000...." + img);

                Picasso.with(ServiceDirectoryAdd_new.this).load(img)
                        .placeholder(R.drawable.edit_image)
                        .error(R.drawable.edit_image)

                        .into(et_photo);


            }

            //et_contactNo1.setText(myservice.getContactNo());

            et_desc.setText(myservice.getDescription());

            //et_contactNo2.setText(myservice.getContactNo2());

            et_paxNo.setText(myservice.getPax());
            et_address.setText(myservice.getAddress());
            et_email.setText(myservice.getEmail());
            //latitude = Double.parseDouble(myservice.getLat());
            //longitude = Double.parseDouble(myservice.getLng());

            tv_countryCode1.setTag("" + myservice.getCountryId1());
            countryId = myservice.getCountryId1();
            tv_countryCode2.setTag("" + myservice.getCountryId2());


            try {
                String s = myservice.getContactNo();
                String before = s.split("-")[0]; // "Before"
                String after = s.split("-")[1];

                Log.d("===BEFORE=======", "=========" + before);
                Log.d("=====AFTER=====", "==========" + after);
                et_contactNo1.setText(after);
                tv_countryCode1.setText(before);
            } catch (ArrayIndexOutOfBoundsException arrayIndex) {
                arrayIndex.printStackTrace();
            }


            //================ mob 2 ================
            try {
                String s1 = myservice.getContactNo2();
                String before1 = s1.split("-")[0]; // "Before"
                String after1 = s1.split("-")[1];

                et_contactNo2.setText(after1);
                tv_countryCode2.setText(before1);
            } catch (ArrayIndexOutOfBoundsException array) {
                array.printStackTrace();
            }


            et_keywords.setText(myservice.getCsv());
            et_city.setText(myservice.getCity());
            et_state.setText(myservice.getState());
            et_country.setText(myservice.getCountry());
            et_zip.setText(myservice.getZip());
            tv_add.setText("Update");
            //tv_title.setText("Edit");
            tv_title.setText("Update " + moduleName);

        }
        }
        init();
    }




    private void init() {



        tv_countryCode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (InternetConnection.checkConnection(getApplicationContext())) {
                    // Avaliable
                    Intent i = new Intent(ServiceDirectoryAdd_new.this, SelectCountry.class);
                    startActivityForResult(i, mRequestCode1);
                    //webservices_update();
                } else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }

            }
        });

        tv_countryCode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (InternetConnection.checkConnection(getApplicationContext())) {
                    // Avaliable
                    Intent i = new Intent(ServiceDirectoryAdd_new.this, SelectCountry.class);
                    startActivityForResult(i, mRequestCode2);
                    //webservices_update();
                } else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }

            }
        });





        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                finish();
            }
        });

        iv_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.popupback(ServiceDirectoryAdd_new.this);


            }

        });


        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               //webservices();

                Log.e("/////////","**********"+tv_countryCode1.getTag().toString());
                Log.e("/////////","**********"+tv_countryCode1.getText().toString());

                if (validation()) {


                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        // Avaliable
                        webservices();
                    } else {
                        Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                        // Not Available...
                    }

                   /* if(isGetLanLon)
                    {

                        if (InternetConnection.checkConnection(getApplicationContext())) {
                            // Avaliable
                            webservices();
                        } else {
                            Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                            // Not Available...
                        }
                    }else {
                        new GetLatLonFromCity(et_city.getText().toString());
                    }
                    //   webservices();*/

                }

            }
        });

        et_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("--------","---Photo clicked----");

                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                } else {
                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    } else {
                        if (InternetConnection.checkConnection(getApplicationContext())) {
                        selectImage();
                        } else {
                            Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                            // Not Available...
                        }
                    }
                }
            }
        });


        et_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        iv_addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              /*  add = et_city.getText().toString();
                Intent intent = new Intent(ServiceDirectoryAdd_new.this, EventVenueSearchAddress.class);
                startActivityForResult(intent, LAT_LONG_REQUEST);*/

                new GetLatLonFromAddress(et_city.getText().toString()).execute();



          }
        });
        time1 = 0;
        et_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String city = et_city.getText().toString();
                Log.e("Span", "Time span is : "+(time1-time2));
                time2 = new java.util.Date().getTime();
                if (city.length()>2) {
                    Log.e("API called", "Api is called");
                    new GetStateCountryTask(et_city.getText().toString()).execute();

                } else {
                    Log.e("Not called", "Api not called");
                }
                time1 = time2;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void selectImage() {

        final CharSequence[] options;

       // options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};

        if (hasimageflag.equals("0")) {
            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
            hasimageflag = "1";
        } else {
            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Remove Photo", "Cancel"};

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ServiceDirectoryAdd_new.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                    startActivityForResult(intent, 4);*/

                    String fileName = "temp.jpg";
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, fileName);
                    mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                    startActivityForResult(intent, Capture_Camera);

                }

                else if (options[item].equals("Choose from Gallery")) {

                    Crop.pickImage(ServiceDirectoryAdd_new.this);

                   /* Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);*/

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                } else if (options[item].equals("Remove Photo")) {
                    if(serviceDir_Id.equals("0")){
                        et_photo.setImageResource(R.drawable.edit_image);

                    }else{
                         remove_photo_webservices();
                    }
                    //dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 2251 && resultCode == RESULT_OK) {
            if (data != null) {
                location = data.getStringExtra(Constant.LOCATION);
                latitude = data.getDoubleExtra(Constant.LATITUDE, 0.0);
                longitude = data.getDoubleExtra(Constant.LONGITUDE, 0.0);
                location.toString();
                isGetLanLon=true;
                Log.e("************","**************"+longitude);
                Log.e("************","************"+latitude);
             //   tvLocation.setText("" + location);
            }
        }

        else if ( requestCode == LAT_LONG_REQUEST && resultCode == RESULT_OK) {
            Log.e("Lat   : ", ""+data.getDoubleExtra("latitude", 0.0));
            Log.e("Longi : ", ""+data.getDoubleExtra("longitude", 0.0));
            Log.e("Address : ", data.getStringExtra("address"));
        }
        if (requestCode == mRequestCode1 && resultCode == RESULT_OK) {


            tv_countryCode1.setText(data.getStringExtra("countryCode"));
            tv_countryCode1.setTag(data.getStringExtra("countryid"));

            Log.e("===========","==== Code 1"+tv_countryCode1.getTag().toString());

           /* else {

                tv_countryCode2.setText(data.getStringExtra("countryCode"));
                tv_countryCode2.setTag(data.getStringExtra("countryid"));
            }*/

        }
        else if(requestCode == mRequestCode2 && resultCode == RESULT_OK)
        {
            tv_countryCode2.setText(data.getStringExtra("countryCode"));
            tv_countryCode2.setTag(data.getStringExtra("countryid"));

            Log.e("===========","==== Code 1"+tv_countryCode2.getTag().toString());
        }

        else if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        } else if (requestCode == Capture_Camera && resultCode == RESULT_OK) {
            //----- Correct Image Rotation ----//
            ImageCompression imageCompression = new ImageCompression();
            Uri correctedUri = null;
            String path = "";
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mCapturedImageURI);

                //imageBitmap = imageOrientationValidator(imageBitmap, getRealPathFromURI(mCapturedImageURI));
                correctedUri = Utils.getImageUri(imageBitmap, getApplicationContext());

             /*   path = Utils.getRealPathFromURI(correctedUri, getApplicationContext());

                imageCompression = new ImageCompression();
                picturePath = imageCompression.compressImage(path, getApplicationContext());
                Log.d("==picturePath====","0000...."+picturePath);*/

                //correctedUri = Utils.getImageUri(imageBitmap, getApplicationContext());

                Log.d("==== Uri ===", "======" + correctedUri);




            } catch (IOException e) {
                e.printStackTrace();
            }
            beginCrop(correctedUri);

        }


        /***************Image Capture***********/
        /*else if (requestCode == 4) {


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
                    et_photo.setImageBitmap(bt);

                    String path = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    // f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

                    Log.d("TOUCHBASE", "FILE PATH " + f.toString());
                    ///-------------------------------------------------------------------
                    pd = ProgressDialog.show(ServiceDirectoryAdd_new.this, "", "Loading...", false);
                    final File finalF = f;




                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            uploadedimgid = Utils.doFileUpload(new File(finalF.toString()), "ServiceDirectory"); // Upload File to server
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (pd.isShowing())
                                        pd.dismiss();
                                    Log.d("TOUCHBASE", "FILE UPLOAD ID InnerThread  " + uploadedimgid);
                                    if (uploadedimgid.equals("0")) {
                                        Toast.makeText(ServiceDirectoryAdd_new.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();
                                        et_photo.setImageResource(R.drawable.edit_image);
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
        }*/


        /* else if (requestCode == 2) {

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
                pd = ProgressDialog.show(ServiceDirectoryAdd_new.this, "", "Loading...", false);
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        uploadedimgid = Utils.doFileUpload(new File(picturePath), "ServiceDirectory"); // Upload File to server
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (pd.isShowing())
                                    pd.dismiss();
                                if (uploadedimgid.equals("0")) {
                                    Toast.makeText(ServiceDirectoryAdd_new.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();
                                    et_photo.setImageResource(R.drawable.edit_image);
                                } else {
                                    et_photo.setImageBitmap(thumbnail);
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
        }*/



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
        arrayList.add(new BasicNameValuePair("image", uploadedimgid));
        arrayList.add(new BasicNameValuePair("countryCode1", tv_countryCode1.getTag().toString()));
        arrayList.add(new BasicNameValuePair("mobileNo1", et_contactNo1.getText().toString()));
        arrayList.add(new BasicNameValuePair("countryCode2", tv_countryCode2.getTag().toString()));
        arrayList.add(new BasicNameValuePair("mobileNo2", et_contactNo2.getText().toString()));
        arrayList.add(new BasicNameValuePair("paxNo", et_paxNo.getText().toString()));

        arrayList.add(new BasicNameValuePair("email", et_email.getText().toString()));
        arrayList.add(new BasicNameValuePair("keywords", et_keywords.getText().toString()));
        arrayList.add(new BasicNameValuePair("address", et_address.getText().toString()));

        arrayList.add(new BasicNameValuePair("latitude", ""+latitude));
        arrayList.add(new BasicNameValuePair("longitude", ""+longitude));
        arrayList.add(new BasicNameValuePair("createdBy", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));

        arrayList.add(new BasicNameValuePair("city",et_city.getText().toString()));
        arrayList.add(new BasicNameValuePair("addressCountry",et_country.getText().toString()));
        arrayList.add(new BasicNameValuePair("state",et_state.getText().toString()));
        arrayList.add(new BasicNameValuePair("zipCode",et_zip.getText().toString()));
        arrayList.add(new BasicNameValuePair("moduleId",moduleId));

        arrayList.add(new BasicNameValuePair("updatedOn",updatedOn));
        arrayList.add(new BasicNameValuePair("categoryId",categoryId));

        //  flag_callwebsercie = "1";
        Log.d("Response", "PARAMETERS " + Constant.AddServiceDirectory + " :- " + arrayList.toString());
        new WebConnectionAsyncAdd(Constant.AddServiceDirectory, arrayList, ServiceDirectoryAdd_new.this).execute();
    }

    public class WebConnectionAsyncAdd extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        ProgressDialog progressDialog = new ProgressDialog(ServiceDirectoryAdd_new.this, R.style.TBProgressBar);


        public WebConnectionAsyncAdd(String url, List<NameValuePair> argList, Context ctx) {
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
           // progressDialog.hide();
            //Log.d("response","Do post"+ result.toString());
            if (result != "") {
                //getresult(result.toString());
                getUpdatedServiceDirectoryItems(result.toString());
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

                if(tv_add.getText().toString().equals("Add")) {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    hideKeyboard();
                    finish();//finishing activity
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("Title",et_title.getText().toString());
                    intent.putExtra("Desc",et_desc.getText().toString());
                    intent.putExtra("Contact1",et_contactNo1.getText().toString());
                    intent.putExtra("Contact2",et_contactNo2.getText().toString());
                    intent.putExtra("Pax",et_paxNo.getText().toString());
                    intent.putExtra("Email",et_email.getText().toString());
                    intent.putExtra("Address",et_address.getText().toString());
                    //intent.putExtra("CountryId1",tv_countryCode1.getTag().toString());
                    //intent.putExtra("CountryId2",tv_countryCode2.getTag().toString());
                    intent.putExtra("CountryCode1",tv_countryCode1.getText().toString());
                    intent.putExtra("CountryCode2",tv_countryCode2.getText().toString());

                    intent.putExtra("city",et_city.getText().toString());
                    intent.putExtra("state",et_country.getText().toString());
                    intent.putExtra("country",et_country.getText().toString());
                    intent.putExtra("zip",et_zip.getText().toString());
                    intent.putExtra("keywords",et_keywords.getText().toString());

                     intent.putExtra("img",img);

                    setResult(RESULT_OK, intent);
                    finish();
                }
            }else{
                Toast.makeText(ServiceDirectoryAdd_new.this, "Fail to Update", Toast.LENGTH_SHORT).show();
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



        Log.d("Response", "PARAMETERS " + Constant.GetServiceDirectoryDetails + " :- " + arrayList.toString());
        new WebConnectionAsyncUpdate(Constant.GetServiceDirectoryDetails, arrayList, ServiceDirectoryAdd_new.this).execute();
    }


    public class WebConnectionAsyncUpdate extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(ServiceDirectoryAdd_new.this, R.style.TBProgressBar);

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
            if (status.equals("0"))
            {
                Toast.makeText(ServiceDirectoryAdd_new.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(ServiceDirectoryAdd_new.this, "We are sorry. Failed to update the service info due to unknown reason. Please try again", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }



    @Override
    public void onBackPressed() {
        Utils.popupback(ServiceDirectoryAdd_new.this);
        //
        // super.onBackPressed();
    }


    //===================== Validation ============================================

    public boolean validation() {

        if (et_title.getText().toString().trim().matches("") || et_title.getText().toString().trim() == null) {
            Toast.makeText(ServiceDirectoryAdd_new.this, "Please enter “Title”", Toast.LENGTH_LONG).show();
            return false;
        }

         if(et_email.getText().toString().length()!=0 )
        {
            if(EMAIL_PATTERN.matcher(et_email.getText().toString()).matches()==false){

                Toast.makeText(getApplicationContext(), "Enter Valid Email Address ", Toast.LENGTH_LONG).show();
                return false;

            }
        }


        //============================== tv_countryCode1 ========================================
        boolean code1Present = true;
        boolean number1Present = true;
        if(tv_countryCode1.getText() == null || tv_countryCode1.getText().toString().trim() == null || tv_countryCode1.getText().toString().length()==0 || tv_countryCode1.getText().toString().trim().matches("")) {
            code1Present = false;
        }

        if(et_contactNo1.getText() == null || et_contactNo1.getText().toString().trim() == null || et_contactNo1.getText().toString().length()==0 || et_contactNo1.getText().toString().trim().matches("")) {
            number1Present = false;
        }

        Log.e("Code1", "Code 1 present : "+code1Present);
        Log.e("Country1", "Country 1 present : "+number1Present);


        if ( code1Present && ! number1Present ) {
            Toast.makeText(getApplicationContext(), "Please Enter Contact Number 1", Toast.LENGTH_LONG).show();
            return false;
        }

        if ( !code1Present && number1Present ) {
            Toast.makeText(getApplicationContext(), "Please enter a country code 1", Toast.LENGTH_LONG).show();
            return false;
        }


        //============================== tv_countryCode2 ========================================
        boolean code2Present = true;
        boolean number2Present = true;
        if(tv_countryCode2.getText() == null || tv_countryCode2.getText().toString().trim() == null || tv_countryCode2.getText().toString().length()==0 || tv_countryCode2.getText().toString().trim().matches("")) {
            code2Present = false;
        }

        if(et_contactNo2.getText() == null || et_contactNo2.getText().toString().trim() == null || et_contactNo2.getText().toString().length()==0 || et_contactNo2.getText().toString().trim().matches("")) {
            number2Present = false;
        }


        Log.e("Code2", "Code 2 present : "+code2Present);
        Log.e("Country2", "Country 2 present : "+number2Present);

        if ( code2Present && ! number2Present ) {
            Toast.makeText(getApplicationContext(), "Please Enter Contact Number 2", Toast.LENGTH_LONG).show();
            return false;
        }

        if ( !code2Present && number2Present ) {
            Toast.makeText(getApplicationContext(), "Please enter a country code 2", Toast.LENGTH_LONG).show();
            return false;
        }


        //============================================ address field ===================================


        boolean address = true;
        boolean city = true;
        if(et_address.getText() == null || et_address.getText().toString().trim() == null || et_address.getText().toString().length()==0 || et_address.getText().toString().trim().matches("")) {
            address = false;
        }

        if(et_city.getText() == null || et_city.getText().toString().trim() == null || et_city.getText().toString().length()==0 || et_city.getText().toString().trim().matches("")) {
            city = false;
        }


        Log.e("Code2", "Code 2 present : "+address);
        Log.e("Country2", "Country 2 present : "+city);

        if ( address && ! city ) {
            Toast.makeText(getApplicationContext(), "Please Enter City", Toast.LENGTH_LONG).show();
            return false;
        }

        if ( !address && city ) {
            Toast.makeText(getApplicationContext(), "Please enter Address", Toast.LENGTH_LONG).show();
            return false;
        }


            return true;
    }

//============================================Map code==============================================================================

    public void getLatLongFromGivenCity(String youraddress) {

        String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
                youraddress + "&sensor=false";
        HttpGet httpGet = new HttpGet(uri);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());

            longitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            Log.d("latitude", ""+latitude);
            Log.d("longitude", ""+longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


//=============================== get GetLatLonFromAddress =======================
    class GetLatLonFromAddress extends AsyncTask<Void, Void, String>
    {
        String adddress;
        ProgressDialog pdialog;
        public GetLatLonFromAddress(String address)
        {
            this.adddress=address;
        }
        @Override
        protected void onPreExecute()
        {
            pdialog=new ProgressDialog(ServiceDirectoryAdd_new.this);
            pdialog.setMessage("Loading...");
            pdialog.setCancelable(false);
            pdialog.show();
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... params)
        {
            //String tokenID=pref.getString(TAG_TOKEN, "");

            StringBuilder stringBuilder = new StringBuilder();
            try {
                String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
                        URLEncoder.encode(adddress.trim(), "UTF-8") + "&sensor=false";
                HttpGet httpGet = new HttpGet(uri);
                HttpClient client = new DefaultHttpClient();
                HttpResponse response;

                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                InputStream stream = entity.getContent();
                int b;
                while ((b = stream.read()) != -1) {
                    stringBuilder.append((char) b);
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return stringBuilder.toString();
        }
        @Override
        protected void onPostExecute(String result)
        {
            if(pdialog!=null)
            {
                if (pdialog.isShowing())
                {
                    pdialog.dismiss();


                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject = new JSONObject(result.toString());

                        longitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                                .getJSONObject("geometry").getJSONObject("location")
                                .getDouble("lng");

                        latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                                .getJSONObject("geometry").getJSONObject("location")
                                .getDouble("lat");

                        Log.d("latitude", ""+latitude);
                        Log.d("longitude", ""+longitude);


                        Intent intent = new Intent(ServiceDirectoryAdd_new.this, SearchAddress.class);
                        Bundle bundle = new Bundle();
                        bundle.putDouble(Constant.LATITUDE,latitude);
                        bundle.putDouble(Constant.LONGITUDE,longitude);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 2251);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            super.onPostExecute(result);
        }
    }


    class GetLatLonFromCity extends AsyncTask<Void, Void, String>
    {
        String adddress;
        ProgressDialog pdialog;
        public GetLatLonFromCity(String address)
        {
            this.adddress=address;
        }
        @Override
        protected void onPreExecute()
        {
            pdialog=new ProgressDialog(ServiceDirectoryAdd_new.this);
            pdialog.setMessage("Loading...");
            pdialog.setCancelable(false);
            pdialog.show();
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... params)
        {
            //String tokenID=pref.getString(TAG_TOKEN, "");
            String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
                    adddress + "&sensor=false";
            HttpGet httpGet = new HttpGet(uri);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            StringBuilder stringBuilder = new StringBuilder();

            try {
                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                InputStream stream = entity.getContent();
                int b;
                while ((b = stream.read()) != -1) {
                    stringBuilder.append((char) b);
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return stringBuilder.toString();
        }
        @Override
        protected void onPostExecute(String result)
        {
            if(pdialog!=null)
            {
                if (pdialog.isShowing())
                {
                    pdialog.dismiss();


                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject = new JSONObject(result.toString());

                        longitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                                .getJSONObject("geometry").getJSONObject("location")
                                .getDouble("lng");

                        latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                                .getJSONObject("geometry").getJSONObject("location")
                                .getDouble("lat");

                        Log.d("latitude", ""+latitude);
                        Log.d("longitude", ""+longitude);
                        if (InternetConnection.checkConnection(getApplicationContext())) {
                            // Avaliable
                            webservices();
                        } else {
                            Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                            // Not Available...
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
            super.onPostExecute(result);
        }
    }



    //==============================================================================================

    public class GetStateCountryTask extends AsyncTask<Void, Void, String> {

        String city;
        public GetStateCountryTask(String city) {
            this.city = city;
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                String encodedCity = java.net.URLEncoder.encode(city, "UTF-8");
                String url = Constant.FULL_CONTACT_API+encodedCity;
                Log.e("Url", url);
                HttpGet get = new HttpGet(url);
                get.setHeader(new BasicHeader(Constant.FULLCONTACT_HEADER_NAME, Constant.FULLCONTACT_API_KEY_VALUE));
                DefaultHttpClient client = new DefaultHttpClient();

                HttpResponse response = client.execute(get);
                if ( response.getStatusLine().getStatusCode() == 200 ) {
                    String output = EntityUtils.toString(response.getEntity());
                    return output;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.e("Fullcontact response", s);
                JSONObject json = new JSONObject(s);
                JSONArray locations = json.getJSONArray("locations");
                String state = (json.getJSONArray("locations")).getJSONObject(0).getJSONObject("state").getString("name");
                String country = (json.getJSONArray("locations")).getJSONObject(0).getJSONObject("country").getString("name");
                et_state.setText(state);
                et_country.setText(country);
            } catch(JSONException ese) {
                //ese.printStackTrace();
                System.out.println("Error : "+ ese.toString());
                et_state.setText("");
                et_country.setText("");
            } catch(NullPointerException npe) {
                //npe.printStackTrace();
                System.out.println("Error : "+ npe.toString());
                et_state.setText("");
                et_country.setText("");
            } catch(Exception e) {
                System.out.println("Error : "+ e.toString());
                //e.printStackTrace();
                et_state.setText("");
                et_country.setText("");
            }

        }
    }


    //================ Remove Photo =========================================

    private void remove_photo_webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("typeID", serviceDir_Id));
        Log.e("======","======"+serviceDir_Id);
        arrayList.add(new BasicNameValuePair("type", "ServiceDirectory"));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        arrayList.add(new BasicNameValuePair("grpID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));

        //flag_callwebsercie = "4";
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "PARAMETERS " + Constant.DeleteImage + " :- " + arrayList.toString());
        new WebConnectionAsyncLogin(Constant.DeleteImage, arrayList, ServiceDirectoryAdd_new.this).execute();
    }

    public class WebConnectionAsyncLogin extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(ServiceDirectoryAdd_new.this, R.style.TBProgressBar);

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

                getresultOfRemovephoto(result.toString());
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

                et_photo.setImageResource(R.drawable.edit_image);
                hasimageflag = "0";
            } else {
                //Utils.showToastWithTitleAndContext(getApplicationContext(), "Failed to Remove Photo...");
                Utils.showToastWithTitleAndContext(getApplicationContext(), "Photo Remove failed, Please try Again!");

            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    public void getUpdatedServiceDirectoryItems(String result) {

        try {
            final ArrayList<ServiceDirectoryListData> newDirectoryData = new ArrayList<ServiceDirectoryListData>();
            JSONObject jsonObj = new JSONObject(result);
            JSONObject DirectoryResult = jsonObj.getJSONObject("TBAddServiceResult");

            final String status = DirectoryResult.getString("status");

            if (status.equals("0")) {


                // final ArrayList<DirectoryData> newDirectoryData = new ArrayList<DirectoryData>();
                JSONArray newDirectoryListResult = DirectoryResult.getJSONObject("res").getJSONArray("newMembers");
                int newCount = newDirectoryListResult.length();


                for (int i = 0; i < newCount; i++) {
                    // JSONObject object = newDirectoryListResult.getJSONObject(i);
                    //JSONObject objects = object.getJSONObject("MemberListResult");

                    // ServiceDirectoryListData data = new ServiceDirectoryListData();

                    JSONObject result_object = newDirectoryListResult.getJSONObject(i);

                    String serviceDirId = result_object.getString("serviceDirId").toString();
                    String groupId = result_object.getString("groupId").toString();
                    String memberName = result_object.getString("memberName");
                    String image  = result_object.getString("image");
                    String contactNo = result_object.getString("contactNo");
                    String isdeleted = result_object.getString("isdeleted");
                    String description = result_object.getString("descriptn");
                    String contactNo2 = result_object.getString("contactNo2");
                    String pax = result_object.getString("pax_no");
                    String email = result_object.getString("email");
                    String address = result_object.getString("address");
                    String lat = result_object.getString("latitude");
                    String lng = result_object.getString("longitude");

                    String city = result_object.getString("city");
                    String state = result_object.getString("state");
                    String country = result_object.getString("addressCountry");
                    String zip = result_object.getString("zipCode");

                    int countryId1 = -1;
                    int countryId2 = -1;

                    try {
                        countryId1 = Integer.parseInt(result_object.getString("countryId1"));
                        Log.e("Country ID 1", "Country id 1 is : "+countryId1);
                    } catch(NumberFormatException npe) {
                        Log.e("NoCountryCode1", "Country code1 not found");
                    }

                    try {
                        countryId2 = Integer.parseInt(result_object.getString("countryId2"));
                        Log.e("Country ID 2", "Country id 2 is : "+countryId2);
                    } catch(NumberFormatException npe) {
                        Log.e("NoCountryCode2", "Country code2 not found");
                    }

                    String csv = result_object.getString("keywords");
                    Log.e("CSV" , "Value of csv : "+csv);
                    String moduleId = result_object.getString("moduleId");
                    int categoryId = Integer.parseInt(result_object.getString("categoryId"));
                    String website = result_object.getString("website");
                    ServiceDirectoryListData data = new ServiceDirectoryListData(serviceDirId, groupId, memberName, image, contactNo, isdeleted, description, contactNo2, pax, email, address,lat,lng, countryId1, countryId2, csv,city,state,country,zip, moduleId,categoryId,website);

                    data.setServiceDirId(result_object.getString("serviceDirId").toString());
                    data.setGroupId(result_object.getString("groupId").toString());
                    data.setMemberName(result_object.getString("memberName").toString());
                    data.setImage(result_object.getString("image").toString());
                    data.setContactNo(result_object.getString("contactNo").toString());
                    //  data.setPic("https://rockyourcareer.files.wordpress.com/2012/11/george_blomgren_med-pic.jpg");
                    data.setIsdeleted(result_object.getString("isdeleted").toString());

                    newDirectoryData.add(data);
                    //directoryData.add(data);
                }
                final ArrayList<ServiceDirectoryListData> updatedServiceDirectoryData = new ArrayList<ServiceDirectoryListData>();
                JSONArray updatedServiceDirectoryListResult = DirectoryResult.getJSONObject("res").getJSONArray("updatedMembers");
                int updateCount = updatedServiceDirectoryListResult.length();
                for (int i = 0; i < updateCount; i++) {
                    /*JSONObject object = updatedServiceDirectoryListResult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("MemberListResult");*/

                    //  ServiceDirectoryListData data = new ServiceDirectoryListData();
                    JSONObject updated_object = updatedServiceDirectoryListResult.getJSONObject(i);

                    String serviceDirId = updated_object.getString("serviceDirId").toString();
                    String groupId = updated_object.getString("groupId").toString();
                    String memberName = updated_object.getString("memberName");
                    String image  = updated_object.getString("image");
                    String contactNo = updated_object.getString("contactNo");
                    String isdeleted = updated_object.getString("isdeleted");
                    String description = updated_object.getString("descriptn");
                    String contactNo2 = updated_object.getString("contactNo2");
                    String pax = updated_object.getString("pax_no");
                    String email = updated_object.getString("email");
                    String address = updated_object.getString("address");
                    String lat = updated_object.getString("latitude");
                    String lng = updated_object.getString("longitude");

                    String city = updated_object.getString("city");
                    String state = updated_object.getString("state");
                    String country = updated_object.getString("addressCountry");
                    String zip = updated_object.getString("zipCode");

                    int countryId1 = -1;
                    int countryId2 = -1;

                    try {
                        countryId1 = Integer.parseInt(updated_object.getString("countryId1"));
                        Log.e("Country ID 1", "Country id 1 is : "+countryId1);
                    } catch(NumberFormatException npe) {
                        Log.e("NoCountryCode1", "Country code1 not found");
                    }

                    try {
                        countryId2 = Integer.parseInt(updated_object.getString("countryId2"));
                        Log.e("Country ID 2", "Country id 2 is : "+countryId2);
                    } catch(NumberFormatException npe) {
                        Log.e("NoCountryCode2", "Country code2 not found");
                    }

                    String csv = updated_object.getString("keywords");
                    //String csv = "abc, xyz";
                    String moduleId = updated_object.getString("moduleId");
                    int categoryId = Integer.parseInt(updated_object.getString("categoryId"));
                    String website = updated_object.getString("website");

                    ServiceDirectoryListData data = new ServiceDirectoryListData(serviceDirId, groupId, memberName, image, contactNo, isdeleted, description, contactNo2, pax, email, address,lat,lng, countryId1, countryId2, csv,city,state,country,zip, moduleId,categoryId,website);

                    data.setServiceDirId(updated_object.getString("serviceDirId").toString());
                    data.setGroupId(updated_object.getString("groupId").toString());
                    data.setMemberName(updated_object.getString("memberName").toString());
                    data.setImage(updated_object.getString("image").toString());
                    data.setContactNo(updated_object.getString("contactNo").toString());
                    //  data.setPic("https://rockyourcareer.files.wordpress.com/2012/11/george_blomgren_med-pic.jpg");
                    data.setIsdeleted(updated_object.getString("isdeleted").toString());

                    updatedServiceDirectoryData.add(data);
                }

                final ArrayList<ServiceDirectoryListData> deletedServiceDirectoryData = new ArrayList<ServiceDirectoryListData>();
                JSONArray deletedServiceDirectoryListResult = DirectoryResult.getJSONObject("res").getJSONArray("deletedMembers");
                int deleteCount = deletedServiceDirectoryListResult.length();
                for (int i = 0; i < deleteCount; i++) {
                /*    JSONObject object = deletedServiceDirectoryListResult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("MemberListResult");*/

                    //  ServiceDirectoryListData data = new ServiceDirectoryListData();
                    JSONObject deleted_object = deletedServiceDirectoryListResult.getJSONObject(i);

                    String serviceDirId = deleted_object.getString("serviceDirId").toString();
                    String groupId = deleted_object.getString("groupId").toString();
                    String memberName = deleted_object.getString("memberName");
                    String image  = deleted_object.getString("image");
                    String contactNo = deleted_object.getString("contactNo");
                    String isdeleted = deleted_object.getString("isdeleted");
                    String description = deleted_object.getString("descriptn");
                    String contactNo2 = deleted_object.getString("contactNo2");
                    String pax = deleted_object.getString("pax_no");
                    String email = deleted_object.getString("email");
                    String address = deleted_object.getString("address");
                    String lat = deleted_object.getString("latitude");
                    String lng = deleted_object.getString("longitude");

                    String city = deleted_object.getString("city");
                    String state = deleted_object.getString("state");
                    String country = deleted_object.getString("addressCountry");
                    String zip = deleted_object.getString("zipCode");


                    int countryId1 = -1;
                    int countryId2 = -1;

                    try {
                        countryId1 = Integer.parseInt(deleted_object.getString("countryId1"));
                    } catch(NumberFormatException npe) {
                        Log.e("NoCountryCode1", "Country code1 not found");
                    }

                    try {
                        countryId2 = Integer.parseInt(deleted_object.getString("countryId2"));
                    } catch(NumberFormatException npe) {
                        Log.e("NoCountryCode2", "Country code2 not found");
                    }
                    String csv = deleted_object.getString("keywords");
                    //String csv = "abc, xyz";
                    String moduleId = deleted_object.getString("moduleId");
                    int categoryId = Integer.parseInt(deleted_object.getString("categoryId"));
                    String website = deleted_object.getString("website");
                    ServiceDirectoryListData data = new ServiceDirectoryListData(serviceDirId, groupId, memberName, image, contactNo, isdeleted, description, contactNo2, pax, email, address,lat,lng, countryId1, countryId2, csv,city,state,country,zip, moduleId,categoryId,website);


                    data.setServiceDirId(deleted_object.getString("serviceDirId").toString());
                    data.setGroupId(deleted_object.getString("groupId").toString());
                    data.setMemberName(deleted_object.getString("memberName").toString());
                    data.setImage(deleted_object.getString("image").toString());
                    data.setContactNo(deleted_object.getString("contactNo").toString());
                    //  data.setPic("https://rockyourcareer.files.wordpress.com/2012/11/george_blomgren_med-pic.jpg");
                    data.setIsdeleted(deleted_object.getString("isdeleted").toString());

                    deletedServiceDirectoryData.add(data);
                }

                Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        ServiceDirectoryDataModel serviceDirectoryDataModel = new ServiceDirectoryDataModel(ServiceDirectoryAdd_new.this);
                        Log.e("♦♦♦♦♦", "Upading in local db");
                        boolean saved = serviceDirectoryDataModel.syncData(masterUid, grpId, newDirectoryData, updatedServiceDirectoryData, deletedServiceDirectoryData);
                        if ( ! saved ) {
                            Log.e("SyncFailed------->", "Failed to update data in local db. Retrying in 2 seconds");
                            sendEmptyMessageDelayed(0, 2000);
                        } else {
                            // updating last updated date in shared preferences.
                            PreferenceManager.savePreference(getApplicationContext(), "ServiceDirectoryUpdatedOn_"+moduleId+"_"+grpId, updatedOn);
                            setResult(RESULT_OK);
                            hideKeyboard();
                            finish();

                           // added code to display message based on Function performed such as add or edit.
                            if(tv_add.getText().equals("Update")){
                                Utils.showToastWithTitleAndContext(getApplication(),"Updated Successfully");
                            }else{
                                Utils.showToastWithTitleAndContext(getApplication(),"Added Successfully");
                            }
                            // Reloading all data for display purpose.
                            //serviceDirectoryListDatas = serviceDirectoryDataModel.getServiceDirectoryData(masterUid, grpId);
                            //serviceDirectoryListAdapter = new ServiceDirectoryListAdapter(ServiceDirectoryList.this, R.layout.directory_list_item, serviceDirectoryListDatas, "0");
                            //directoryAdapter.notifyDataSetChanged();
                            //listview.setAdapter(serviceDirectoryListAdapter);
                            //Log.d("-----------","----Updated data------"+serviceDirectoryListAdapter);
                        }
                    }
                };
                int overAllCount = newCount + updateCount + deleteCount;
                System.out.println("Number of records for update are : "+overAllCount);
                if ( newCount + updateCount + deleteCount != 0) {
                    updatedOn = DirectoryResult.getString("updatedOn");
                    handler.sendEmptyMessageDelayed(0, 1000);
                } else {
                    Log.e("NoUpdate", "No updates found");
                }
            }
            else
                Toast.makeText(ServiceDirectoryAdd_new.this, "Fail to Update", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
            e.printStackTrace();
        }


    }
    /**************************************
     * Image Crop Methods
     *****************************************/

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

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
                    picturePath = imageCompression.compressImage(path, getApplicationContext());
                    Log.d("==picturePath====","0000...."+picturePath);

                    //String finalPath = ImageCompression.addImageToGallery(picturePath,getApplicationContext());

                    uploadedimgid = Utils.doFileUpload(new File(picturePath.toString()), "servicedirectory"); // Upload File to server


                    Log.d("@@@@@","0000"+picturePath);


                    Log.d("TOUCHBASE", "RESPONSE FILE UPLOAD " + uploadedimgid);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                updatedProfileImage = csBitmap;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                csBitmap = Bitmap.createScaledBitmap(csBitmap, 500, 500, true);
//                csBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                // et_photo.setImageBitmap(picturePath);

                picturePath = imageCompression.compressImage(path, getApplicationContext());
                et_photo.setImageDrawable(Drawable.createFromPath(picturePath));


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

    /************************************************************************************************/
    public void hideKeyboard() {
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

}

// Note : When webservice is called for update or add new service directory, then after adding/updating the service directory info,
// api internally calls to Sync Data service for service directory