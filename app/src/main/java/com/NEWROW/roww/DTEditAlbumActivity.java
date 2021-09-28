package com.NEWROW.row;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.SpinnerAdapter;
import com.NEWROW.row.Adapter.SpinnerAdapter_subcategoryupdate;
import com.NEWROW.row.Adapter.SpinnerAdapter_subtosubcategoryupdate;
import com.NEWROW.row.Adapter.SpinnerFundingAdapter;
import com.NEWROW.row.Data.AlbumData;
import com.NEWROW.row.Data.DirectoryData;
import com.NEWROW.row.Data.FundingData;
import com.NEWROW.row.Data.SubGoupData;
import com.NEWROW.row.Data.UploadPhotoData;
import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.ImageCompression;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.MarshMallowPermission;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.Utils;
import com.NEWROW.row.sql.UploadedPhotoModel;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.NEWROW.row.Gallery.maxBeneficiaries;

/**
 * Created by user on 18-11-2016.
 */
public class


DTEditAlbumActivity extends Activity {
    TextView tv_title, tv_add, tv_getCount, tv_cancel, tv_clubServiceInfo;
    String albumId, albumName, albumDescription, header;
    String albumImage = "";
    ImageView iv_backbutton, iv_image, iv_edit, iv_album_photo, iv_album_photo2, iv_album_photo3, iv_album_photo4, iv_album_photo_auth;
    ImageView close1, close2, close3, close4, close5, close6;
    EditText edt_title, edt_description;
    EditText et_categoryName, et_subcategoryName, et_subtosubcategoryName, et_noOfRotarians, et_COP, et_Beneficiary, et_manPower;
    Spinner sp_category, sp_subcategory, sp_subtosubcategory, et_currency, sp_timeType;
    TextView et_DOP;
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
    ProgressDialog pd;
    private String uploadedimgid = "0";
    String galleryType = "0";
    RadioButton d_radio0, d_radio1, d_radio2;
    private RadioButton rbInClub, rbPublic;
    ArrayList<DirectoryData> listaddmemberdata = new ArrayList<>();
    private String edit_gallery_selectedids = "0";
    ArrayList<SubGoupData> listaddsubgrp = new ArrayList<>();
    String inputids = "";
    ArrayList<String> selectedsubgrp;
    private String edit_album_selectedids = "0";
    ArrayList<AlbumData> categoryList = new ArrayList<>();
    //added By Gaurav
    ArrayList<AlbumData> subcategoryList = new ArrayList<>();
    ArrayList<AlbumData> subtosubcategoryList = new ArrayList<>();
    ArrayList<AlbumData> subcategoryListupdate = new ArrayList<>();
    ArrayList<AlbumData> subtosubcategoryListupdate = new ArrayList<>();

    AlbumData newdataforsubcategory, newdataforsubtosubcategory, updateDataForSubcategory, updateDataForSubtosubcategory;
    //closed  added
    ArrayList<String> currencyList = new ArrayList<>();
    ArrayList<String> timeType = new ArrayList<>();
    String categoryID, subcategoryID, subtosubcategoryID, subcategoryIDupdate, subtosubcategoryIDupdate, projectDate;
    SpinnerAdapter adapter;
    SpinnerAdapter_subcategoryupdate adapter_subcategory_update;
    SpinnerAdapter_subtosubcategoryupdate adapter_subtosubcategory_update;
    private Context context;
    //private RadioButton rbInClub, rbPublic;
    SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    LinearLayout ll_rotaryServicecontent, ll_category, ll_subcategory, ll_subtosubcategory;
    int imgFlag = 0, deletePhotoflag = 0;
    EditText et_coverPhoto, et_album_photo3, et_album_photo2, et_album_photo1, et_album_photo1_auth, et_album_photo4;
    int flag = 1, flagforcategoryothertext = 0, flagforsubcategoryothertext = 0;
    String groupId = "";
    String createdBy = "";
    String isdelete = "false";
    String photoId1 = "", photoId2 = "", photoId3 = "", photoId4 = "", photoId5 = "";
    ArrayList<String> imageList = new ArrayList<>();
    ArrayList<String> descList = new ArrayList<>();
    ArrayList<String> PhotoIdList = new ArrayList<>();
    UploadPhotoData data;
    UploadedPhotoModel addPhotoModel;
    LinearLayout ll_photos;


    //this is added By Gaurav
    String serviceproject = "1", shareType = "", Ismedia = "", beneficiary = "", temp_beneficiary = "0", temp_beneficiary_flag = "0";
    LinearLayout attendance_layout, meeting_layout, rotarctors_layout, media_layout, mediaradiolayot, authlayout;
    EditText et_rotractors;
    RadioButton radio_mediaprint_yes, radio_mediaprint_No;
    String districtevent, selectvalue, mediaPhotoPath = "", MediaphotoID = "0", subtosubcategoryIdtest = "0";
    TextView mediatitle;

    String sp_blankselect_SubCategory = "0", sp_blankselect_SubtosubCategory = "0", subcategoryselectedtext = "", subtosubcategoryselectedtext = "";
    String updatedtitle = "";

    //Added By Gaurav for funding option on 9th Oct 2020
    LinearLayout ll_funding;
    private Spinner sp_funding;
    ArrayList<FundingData> fundingList = new ArrayList<>();

    private String fundingID;
    private EditText et_fundingName;
    SpinnerFundingAdapter fundingadapter;
    int flagforfundingothertext;
    private String fundingIsSelect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dt_edit_album);
        context = this;

        addPhotoModel = new UploadedPhotoModel(this);

        groupId = PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID);
        createdBy = PreferenceManager.getPreference(this, PreferenceManager.GRP_PROFILE_ID);

        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title = (TextView) findViewById(R.id.tv_title);
        edt_title = (EditText) findViewById(R.id.et_galleryTitle);
        edt_description = (EditText) findViewById(R.id.et_evetDesc);
        iv_image = (ImageView) findViewById(R.id.iv_event_photo);
        tv_add = (TextView) findViewById(R.id.tv_done);
        iv_edit = (ImageView) findViewById(R.id.iv_edit);
        tv_getCount = (TextView) findViewById(R.id.getCount);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);

        iv_album_photo = (ImageView) findViewById(R.id.iv_album_photo);
        iv_album_photo_auth = (ImageView) findViewById(R.id.iv_album_photo_auth);
        iv_album_photo2 = (ImageView) findViewById(R.id.iv_album_photo2);
        iv_album_photo3 = (ImageView) findViewById(R.id.iv_album_photo3);
        iv_album_photo4 = (ImageView) findViewById(R.id.iv_album_photo4);

        et_coverPhoto = (EditText) findViewById(R.id.et_coverPhoto);
        String img_des_val = et_coverPhoto.getText().toString();
        System.out.println("new_value111" + img_des_val);
        et_album_photo1 = (EditText) findViewById(R.id.et_album_photo1);
        et_album_photo1_auth = (EditText) findViewById(R.id.et_album_photo1_auth);
        et_album_photo2 = (EditText) findViewById(R.id.et_album_photo2);
        et_album_photo3 = (EditText) findViewById(R.id.et_album_photo3);
        et_album_photo4 = (EditText) findViewById(R.id.et_album_photo4);

        close1 = (ImageView) findViewById(R.id.close1);
        close2 = (ImageView) findViewById(R.id.close2);
        close3 = (ImageView) findViewById(R.id.close3);
        close4 = (ImageView) findViewById(R.id.close4);
        close5 = (ImageView) findViewById(R.id.close5);
        close6 = (ImageView) findViewById(R.id.close2_auth);

        d_radio0 = (RadioButton) findViewById(R.id.d_radio0);
        d_radio1 = (RadioButton) findViewById(R.id.d_radio1);
        d_radio2 = (RadioButton) findViewById(R.id.d_radio2);
        // d_radio0.setChecked(true);
        rbInClub = (RadioButton) findViewById(R.id.rbInClub);
        rbPublic = (RadioButton) findViewById(R.id.rbPublic);


        //this code is added By Gaurav
        attendance_layout = findViewById(R.id.attendance_layout);
        meeting_layout = findViewById(R.id.meeting_layout);
        rotarctors_layout = findViewById(R.id.rotarctors_layout);
        media_layout = findViewById(R.id.media_layout);
        mediaradiolayot = findViewById(R.id.mediaradiolayot);
        authlayout = findViewById(R.id.authlayout);
        et_rotractors = findViewById(R.id.et_rotractors);
        radio_mediaprint_yes = (RadioButton) findViewById(R.id.radio_mediaprint_yes);
        radio_mediaprint_No = (RadioButton) findViewById(R.id.radio_mediaprint_No);
        mediatitle = (TextView) findViewById(R.id.mediatitle);
        //finding Layout
        ll_funding = (LinearLayout) findViewById( R.id.ll_funding );
        sp_funding = (Spinner) findViewById( R.id.sp_funding );
        et_fundingName = (EditText) findViewById( R.id.et_fundingName );


        //closed the added value

        Intent i = getIntent();
        albumId = i.getStringExtra("albumId");
        albumName = i.getStringExtra("albumname");
        albumDescription = i.getStringExtra("description");
        albumImage = i.getStringExtra("albumImage");
        header = i.getStringExtra("header");

        //updatedtitle = method(albumName);
        tv_title.setText("Edit " + header);

        //loadFromServer();

        edt_title.setText(header);
        edt_description.setText(albumDescription);
        et_categoryName = (EditText) findViewById(R.id.et_categoryName);
        et_subcategoryName = (EditText) findViewById(R.id.et_subcategoryName);
        et_subtosubcategoryName = (EditText) findViewById(R.id.et_subtosubcategoryName);

        et_DOP = (TextView) findViewById(R.id.et_DOP);
        et_DOP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker(et_DOP);
            }
        });
        et_COP = (EditText) findViewById(R.id.et_COP);
        et_Beneficiary = (EditText) findViewById(R.id.et_Beneficiary);
        et_manPower = (EditText) findViewById(R.id.et_manPower);
        et_noOfRotarians = (EditText) findViewById(R.id.et_noOfRotarians);

        tv_clubServiceInfo = (TextView) findViewById(R.id.tv_clubServiceInfo);
        ll_rotaryServicecontent = (LinearLayout) findViewById(R.id.ll_rotaryServicecontent);
        ll_category = (LinearLayout) findViewById(R.id.ll_category);
        ll_subcategory = (LinearLayout) findViewById(R.id.ll_subcategory);
        ll_subtosubcategory = (LinearLayout) findViewById(R.id.ll_subtosubcategory);
        ll_photos = (LinearLayout) findViewById(R.id.ll_photos);
        sp_category = (Spinner) findViewById(R.id.sp_category);
        sp_subcategory = (Spinner) findViewById(R.id.sp_subcategory);
        sp_subtosubcategory = (Spinner) findViewById(R.id.sp_subtosubcategory);


        //District Events Data Display
        if (header.equalsIgnoreCase("District Event")) {
            //District Events Data Display

            rbInClub.setChecked(true);
            rbPublic.setChecked(false);


            rotarctors_layout.setVisibility(View.GONE);
            et_rotractors.setVisibility(View.GONE);
            media_layout.setVisibility(View.GONE);
            media_layout.setVisibility(View.GONE);
            ll_funding.setVisibility(View.GONE);


        } else {
            //District  Projects Data display

            rbPublic.setChecked(true);
            rbInClub.setChecked(false);

            rotarctors_layout.setVisibility(View.VISIBLE);
            et_rotractors.setVisibility(View.VISIBLE);
            media_layout.setVisibility(View.VISIBLE);
            ll_funding.setVisibility(View.VISIBLE);


        }


        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AlbumData data = categoryList.get(position);
                AlbumData subdata = subcategoryList.get(position);
                AlbumData subtosubdata = subtosubcategoryList.get(position);


                categoryID = data.getDistrict_id();
                //Called Filter Method for SubCategory
                filterData_SubCategory(categoryID);


                // subcategoryListupdate.remove(0);
                //this code end here
                //close
                if (data.getDistrict_Name().equalsIgnoreCase("others")) {
                    et_categoryName.setVisibility(View.VISIBLE);
                    ll_subcategory.setVisibility(View.GONE);
                    ll_subtosubcategory.setVisibility(View.GONE);
                    flagforcategoryothertext = 1;
                    sp_blankselect_SubCategory = "0";
                    sp_blankselect_SubtosubCategory = "0";
                } else {
                    et_categoryName.setVisibility(View.GONE);
                    flagforcategoryothertext = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Spinner Funding added by Gaurav

        sp_funding.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FundingData data = fundingList.get( position );

                fundingID = data.getPk_Fund_ID();

                //this code end here

                if (data.getFund_Name().equalsIgnoreCase( "Select" ) || data.getPk_Fund_ID().equalsIgnoreCase( "0" )){
                    fundingIsSelect="1";
                }else{
                    fundingIsSelect="0";
                }
                //close
                if (data.getFund_Name().equalsIgnoreCase( "others" )) {
                    et_fundingName.setVisibility( View.VISIBLE );
                    flagforfundingothertext = 1;
                } else {
                    et_fundingName.setVisibility( View.GONE );
                    flagforfundingothertext = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );



        //Added By Gaurav two spinner

        sp_subcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                AlbumData data = subcategoryListupdate.get(position);
                subcategoryID = data.getPk_subcategoryIdupdate();
                AlbumData subtosubdata = subtosubcategoryList.get(position);
                subtosubcategoryID = subtosubdata.getPk_subtosubcategoryId();

                filterData_SubtosubCategory(subcategoryID);


                //this code end here

                if (subcategoryListupdate.get(position).getSubcategoryNameupdate().equalsIgnoreCase("Select")) {
                    ll_subtosubcategory.setVisibility(View.GONE);
                    sp_blankselect_SubCategory = "1";
                    sp_blankselect_SubtosubCategory = "0";


                } else {
                    sp_blankselect_SubCategory = "0";
                }

                if (subcategoryListupdate.get(position).getSubcategoryNameupdate().equalsIgnoreCase("others")) {
                    et_subcategoryName.setVisibility(View.VISIBLE);
                    ll_subtosubcategory.setVisibility(View.GONE);
                    flagforsubcategoryothertext = 1;
                    sp_blankselect_SubCategory = "0";
                    sp_blankselect_SubtosubCategory = "0";

                } else {
                    et_subcategoryName.setVisibility(View.GONE);
                    flagforsubcategoryothertext = 0;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_subtosubcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                AlbumData data = subtosubcategoryListupdate.get(position);
                //   subtosubcategoryID = data.getFk_subcategoryidupdate();
                subtosubcategoryID = data.getPk_subtosubcategoryIdupdate();

                //this condition for blank select validation
                if (subtosubcategoryListupdate.get(position).getSubtosubcategorynameupdate().equalsIgnoreCase("Select")) {
                    sp_blankselect_SubtosubCategory = "1";
                } else {
                    sp_blankselect_SubtosubCategory = "0";

                }

          /*      if (!(subtosubcategoryIDupdate.equals("") || subtosubcategoryIDupdate == null)) {
                   // subtosubcategoryID = subtosubcategoryIDupdate;
                    subtosubcategoryIdtest = subtosubcategoryIDupdate;
                    subtosubcategoryIDupdate = "";

                } else {
                   // subtosubcategoryID = data.getFk_subcategoryidupdate();
                    subtosubcategoryIdtest = data.getPk_subtosubcategoryIdupdate();
                }*/

                if (subtosubcategoryListupdate.get(position).getSubtosubcategorynameupdate().equalsIgnoreCase("others")) {
                    et_subtosubcategoryName.setVisibility(View.VISIBLE);
                } else {
                    et_subtosubcategoryName.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (rbInClub.isChecked()) {
            ll_rotaryServicecontent.setVisibility(View.GONE);
            ll_category.setVisibility(View.GONE);
            tv_clubServiceInfo.setText(getString(R.string.distEventInfo_new));
        }

        rbInClub.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ll_rotaryServicecontent.setVisibility(View.VISIBLE);
                ll_category.setVisibility(View.VISIBLE);

                if (isChecked) {
                    tv_clubServiceInfo.setText(getString(R.string.distEventInfo_new));
                }
            }

        });

        if (rbPublic.isChecked()) {
            tv_clubServiceInfo.setText(getString(R.string.distProjectInfo_new));
        }

        rbPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ll_rotaryServicecontent.setVisibility(View.GONE);
                ll_category.setVisibility(View.GONE);


                if (isChecked) {
                    tv_clubServiceInfo.setText(getString(R.string.distProjectInfo_new));
                }
            }
        });

        for (int j = 0; j < 5; j++) {
            imageList.add("");
            PhotoIdList.add("0");
            descList.add("");
        }


        et_currency = (Spinner) findViewById(R.id.et_currency);
        currencyList.add("\u20B9");
        currencyList.add("$");
        ArrayAdapter adapter = new ArrayAdapter(DTEditAlbumActivity.this, android.R.layout.simple_spinner_dropdown_item, currencyList);
        et_currency.setAdapter(adapter);

        sp_timeType = (Spinner) findViewById(R.id.sp_timeType);
        timeType.add("Hours");
        timeType.add("Days");
        timeType.add("Months");
        timeType.add("Years");
        ArrayAdapter adapter1 = new ArrayAdapter(DTEditAlbumActivity.this, android.R.layout.simple_spinner_dropdown_item, timeType);
        sp_timeType.setAdapter(adapter1);


        init();
        getCategoryList();


    }

    private void filterData_SubCategory(String categoryID) {


        ArrayList<AlbumData> subcategoryListchangeData = new ArrayList<>();

        updateDataForSubcategory = new AlbumData();

        updateDataForSubcategory.setPk_subcategoryIdupdate("0");
        updateDataForSubcategory.setFk_CategoryIDupdate("0");

        updateDataForSubcategory.setSubcategoryNameupdate("Select");

        subcategoryListchangeData.add(updateDataForSubcategory);

        for (int i = 0; i < subcategoryList.size(); i++) {

            if (categoryID.equals(subcategoryList.get(i).getFk_CategoryID())) {
                updateDataForSubcategory = new AlbumData();

                updateDataForSubcategory.setPk_subcategoryIdupdate(subcategoryList.get(i).getPk_subcategoryId());
                updateDataForSubcategory.setFk_CategoryIDupdate(subcategoryList.get(i).getFk_categoryID());

                updateDataForSubcategory.setSubcategoryNameupdate(subcategoryList.get(i).getSubcategoryName());

                subcategoryListchangeData.add(updateDataForSubcategory);

            }


        }

        //setAdapter base on categoryselection

       /* for (int i = 0; i < subcategoryList.size(); i++) {

            if (categoryID.equals(subcategoryList.get(i).getFk_CategoryID())) {
                subcategoryListchangeData.add(subcategoryList.get(i));

            }
        }*/


        //Added Spinner here to set subcategory Data

        subcategoryListupdate = subcategoryListchangeData;
        adapter_subcategory_update = new SpinnerAdapter_subcategoryupdate(context, subcategoryListupdate);
        sp_subcategory.setAdapter(adapter_subcategory_update);
        ll_subcategory.setVisibility(View.VISIBLE);

        if (subcategoryListupdate.size() == 0 || subcategoryListupdate.size() == 1) {
            ll_subcategory.setVisibility(View.GONE);
            sp_blankselect_SubCategory = "0";
            sp_blankselect_SubtosubCategory = "0";

        } else {
            ll_subcategory.setVisibility(View.VISIBLE);
            ll_subtosubcategory.setVisibility(View.GONE);
            sp_blankselect_SubtosubCategory = "0";


        }

        //   sp_subcategory.setSelection(Integer.parseInt(subcategoryIDupdate) - 1);

        sp_subcategory.setSelection(getIndexforsubcategory(subcategoryListupdate, subcategoryselectedtext));


        //End Data here for subcategory

    }

    private void filterData_SubtosubCategory(String subcategoryID) {

        //Filter data for subtosubCategory

        ArrayList<AlbumData> subtosubcategoryListchangeData = new ArrayList<>();

        updateDataForSubtosubcategory = new AlbumData();


        updateDataForSubtosubcategory.setPk_subtosubcategoryIdupdate("0");
        updateDataForSubtosubcategory.setFk_subcategoryidupdate("0");
        updateDataForSubtosubcategory.setFk_categoryIDupdate("0");
        updateDataForSubtosubcategory.setSubtosubcategorynameupdate("Select");
        subtosubcategoryListchangeData.add(0, updateDataForSubtosubcategory);


        for (int i = 0; i < subtosubcategoryList.size(); i++) {

            if (subcategoryID.equals(subtosubcategoryList.get(i).getFk_subcategoryid())) {
                updateDataForSubtosubcategory = new AlbumData();


                updateDataForSubtosubcategory.setPk_subtosubcategoryIdupdate(subtosubcategoryList.get(i).getPk_subtosubcategoryId());
                updateDataForSubtosubcategory.setFk_subcategoryidupdate(subtosubcategoryList.get(i).getFk_subcategoryid());
                updateDataForSubtosubcategory.setFk_categoryIDupdate(subtosubcategoryList.get(i).getFk_categoryID());
                updateDataForSubtosubcategory.setSubtosubcategorynameupdate(subtosubcategoryList.get(i).getSubtosubcategoryname());
                subtosubcategoryListchangeData.add(updateDataForSubtosubcategory);

            }


        }

        //Added Spinner for subtosub Category

        subtosubcategoryListupdate = subtosubcategoryListchangeData;
        adapter_subtosubcategory_update = new SpinnerAdapter_subtosubcategoryupdate(context, subtosubcategoryListupdate);
        sp_subtosubcategory.setAdapter(adapter_subtosubcategory_update);

        if (subtosubcategoryListupdate.size() == 0 || subtosubcategoryListupdate.size() == 1) {
            ll_subtosubcategory.setVisibility(View.GONE);
            sp_blankselect_SubtosubCategory = "0";

        } else {

            ll_subtosubcategory.setVisibility(View.VISIBLE);
            sp_subtosubcategory.setSelection(getIndexforsubtosubcategory(subtosubcategoryListupdate, subtosubcategoryselectedtext));

        }


        //End Code Here

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
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
                            Date newDate = format.parse(dayOfMonth + " " + (monthOfYear + 1) + " " + year);

                            format = new SimpleDateFormat("dd MMM yyyy");
                            String date = format.format(newDate);

                            projectDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            setdatetext.setText(date);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
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

            //this is added By Gaurav
            case R.id.radio_mediaprint_No:

                if (checked) {
                    //set inch button to unchecked
                    Ismedia = "0";
                    authlayout.setVisibility(View.GONE);
                }
                //this condition for media photo clear if user click on no button
                if (iv_album_photo_auth.getDrawable() != null) {

                    iv_album_photo_auth.setBackground(getResources().getDrawable(R.drawable.asset));
                    iv_album_photo_auth.setImageResource(0);
                    //  radio_mediaprint_No.setChecked(true);
                    // radio_mediaprint_yes.setChecked(false);
                    Ismedia = "0";
                    MediaphotoID = "0";
                    // mediaPhotoPath="";
                    et_album_photo1_auth.setText("");


                    close6.setVisibility(View.GONE);

                }

                radio_mediaprint_yes.setChecked(false);
                radio_mediaprint_No.setChecked(true);
                break;
            case R.id.radio_mediaprint_yes:
                //then auth of media displayed and text
                if (checked) {
                    //set inch button to unchecked
                    //  Ismedia = "1";
                    authlayout.setVisibility(View.VISIBLE);
                }
                radio_mediaprint_No.setChecked(false);
                radio_mediaprint_yes.setChecked(true);
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
//                        selectImage();

                        flag = 1;
                        selectAlbumImages();
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


        //this is added new image
        iv_album_photo_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                } else {
                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    } else {
                        // flag = 6;
                        selectAlbumImagesForMediaPhoto();
                        //  Ismedia = "1";
                    }
                }

            }
        });


        iv_album_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                } else {
                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    } else {
                        flag = 2;
                        selectAlbumImages();
                    }
                }

            }
        });
        iv_album_photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                } else {
                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    } else {
                        flag = 3;
                        selectAlbumImages();
                    }
                }

            }
        });
        iv_album_photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                } else {

                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    } else {
                        flag = 4;
                        selectAlbumImages();
                    }
                }

            }
        });

        iv_album_photo4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                } else {
                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    } else {
                        flag = 5;
                        selectAlbumImages();
                    }
                }
            }
        });

        tv_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                hideKeyboard(DTEditAlbumActivity.this);


                if (validation()) {


                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        webservices();

/*
                        Thread t = new Thread() {
                            public void run() {
                                //This class Added By gaurav for hide soft keyboaard
                                CloseSoftKeyboard.hideSoftKeyboard(DTEditAlbumActivity.this);
                            }
                        };
                        t.start();*/
                    } else {
                        Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    }
                }
            }
        });


        //media image close

        close6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(DTEditAlbumActivity.this, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_confrm_delete);
                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
                tv_line1.setText("Are you sure you want to delete this Photo");

                tv_no.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "false";
                        Ismedia = "1";

                    }
                });

                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "true";
                        // mediaPhotoPath="";
                        //  deletePhotoflag = 6;
                        iv_album_photo_auth.setBackground(getResources().getDrawable(R.drawable.asset));
                        iv_album_photo_auth.setImageResource(0);
                        //  radio_mediaprint_No.setChecked(true);
                        // radio_mediaprint_yes.setChecked(false);
                        Ismedia = "0";
                        MediaphotoID = "0";
                        // mediaPhotoPath="";
                        et_album_photo1_auth.setText("");


                        close6.setVisibility(View.GONE);

                        /*if (!imageList.get(0).isEmpty()) {
                            imageList.remove(0);
                            imageList.add(0, "");
                            iv_album_photo_auth.setBackground(getResources().getDrawable(R.drawable.asset));
                            iv_album_photo_auth.setImageResource(0);
                            close6.setVisibility(View.GONE);
                        }*/

                    }
                });

                dialog.show();
            }
        });


        close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(DTEditAlbumActivity.this, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_confrm_delete);
                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
                tv_line1.setText("Are you sure you want to delete this Photo");
                tv_no.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "false";

                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "true";

                        if (!photoId1.isEmpty() && !photoId1.equalsIgnoreCase("0")) {

                            if (InternetConnection.checkConnection(DTEditAlbumActivity.this)) {
                                deletePhotoflag = 1;
                                deletePhoto(albumId, photoId1);
                            } else {
                                Toast.makeText(DTEditAlbumActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                            }

                        } else {

                            imageList.remove(0);
                            imageList.add(0, "");

                            descList.remove(0);
                            descList.add(0, "");

                            PhotoIdList.remove(0);
                            PhotoIdList.add(0, "0");
                            photoId1 = "0";
                            iv_image.setBackground(getResources().getDrawable(R.drawable.asset));
                            iv_image.setImageResource(0);
                            close1.setVisibility(View.GONE);
                        }


/*                        if (InternetConnection.checkConnection(EditAlbumActivity.this)) {
                            deletePhotoflag = 1;
//                            if (!imageList.get(0).isEmpty()) {
//                                imageList.remove(0);
//                                imageList.add(0, "");
//
//                            }
                            deletePhoto(albumId, photoId1);
                        } else {
                            Toast.makeText(EditAlbumActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                        }*/
                    }
                });

                dialog.show();
            }
        });


        close2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(DTEditAlbumActivity.this, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_confrm_delete);
                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
                tv_line1.setText("Are you sure you want to delete this Photo");
                tv_no.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "false";

                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "true";

                        if (!photoId2.isEmpty() && !photoId2.equalsIgnoreCase("0")) {
                            if (InternetConnection.checkConnection(DTEditAlbumActivity.this)) {
                                deletePhotoflag = 2;
                                deletePhoto(albumId, photoId2);
                            } else {
                                Toast.makeText(DTEditAlbumActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            imageList.remove(1);
                            imageList.add(1, "");

                            descList.remove(1);
                            descList.add(1, "");

                            PhotoIdList.remove(1);
                            PhotoIdList.add(1, "0");
                            photoId2 = "0";
                            iv_album_photo.setBackground(getResources().getDrawable(R.drawable.asset));
                            iv_album_photo.setImageResource(0);
                            close2.setVisibility(View.GONE);
                        }

                    }
                });

                dialog.show();
            }
        });

        close3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(DTEditAlbumActivity.this, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_confrm_delete);
                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
                tv_line1.setText("Are you sure you want to delete this Photo");
                tv_no.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "false";

                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "true";

                        if (!photoId3.isEmpty() && !photoId3.equalsIgnoreCase("0")) {
                            if (InternetConnection.checkConnection(DTEditAlbumActivity.this)) {
                                deletePhotoflag = 3;
                                deletePhoto(albumId, photoId3);
                            } else {
                                Toast.makeText(DTEditAlbumActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            imageList.remove(2);
                            imageList.add(2, "");

                            descList.remove(2);
                            descList.add(2, "");

                            PhotoIdList.remove(2);
                            PhotoIdList.add(2, "0");
                            photoId3 = "0";
                            iv_album_photo2.setBackground(getResources().getDrawable(R.drawable.asset));
                            iv_album_photo2.setImageResource(0);
                            close3.setVisibility(View.GONE);
                        }

                    }
                });

                dialog.show();
            }
        });

        close4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(DTEditAlbumActivity.this, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_confrm_delete);
                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
                tv_line1.setText("Are you sure you want to delete this Photo");
                tv_no.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "false";

                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "true";

                        if (!photoId4.isEmpty() && !photoId4.equalsIgnoreCase("0")) {
                            if (InternetConnection.checkConnection(DTEditAlbumActivity.this)) {
                                deletePhotoflag = 4;
                                deletePhoto(albumId, photoId4);
                            } else {
                                Toast.makeText(DTEditAlbumActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            imageList.remove(3);
                            imageList.add(3, "");

                            descList.remove(3);
                            descList.add(3, "");

                            PhotoIdList.remove(3);
                            PhotoIdList.add(3, "0");
                            photoId4 = "0";
                            iv_album_photo3.setBackground(getResources().getDrawable(R.drawable.asset));
                            iv_album_photo3.setImageResource(0);
                            close4.setVisibility(View.GONE);
                        }

                    }
                });

                dialog.show();
            }
        });

        close5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(DTEditAlbumActivity.this, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_confrm_delete);
                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
                tv_line1.setText("Are you sure you want to delete this Photo");
                tv_no.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "false";

                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "true";

                        if (!photoId5.isEmpty() && !photoId5.equalsIgnoreCase("0")) {
                            if (InternetConnection.checkConnection(DTEditAlbumActivity.this)) {
                                deletePhotoflag = 5;
                                deletePhoto(albumId, photoId5);
                            } else {
                                Toast.makeText(DTEditAlbumActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            imageList.remove(4);
                            imageList.add(4, "");

                            descList.remove(4);
                            descList.add(4, "");

                            PhotoIdList.remove(4);
                            PhotoIdList.add(4, "0");
                            photoId5 = "0";
                            iv_album_photo4.setBackground(getResources().getDrawable(R.drawable.asset));
                            iv_album_photo4.setImageResource(0);
                            close5.setVisibility(View.GONE);
                        }

                    }
                });

                dialog.show();
            }
        });


    }

    private void selectAlbumImages() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 6);
    }

    private void selectAlbumImagesForMediaPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 7);
    }


    public void loadFromServer() {
        if (InternetConnection.checkConnection(this))
            loadData();
        else
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
    }

    public void loadData() {

        Log.e("Touchbase", "------ loadData() called");
        String url = Constant.GetAlbumDetails_New;
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("albumId", albumId));

        Log.d("Request", "PARAMETERS " + Constant.GetAlbumDetails_New + " :- " + arrayList.toString());
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


//                if (type.equalsIgnoreCase("0")) {
//                    d_radio0.setChecked(true);
//                    galleryType = "0";
//                    clearselectedtext();
//                } else if (type.equalsIgnoreCase("1")) {
//                    galleryType = "1";
//                    d_radio1.setChecked(true);
//                    edit_album_selectedids = jsonAlbumDetail.getString("memberIds").toString();
//                    String[] ids = edit_album_selectedids.split(",");
//                    selectedsubgrp = new ArrayList<String>();
//                    for (String id : ids) {
//                        selectedsubgrp.add(id);
//                    }
//                    inputids = jsonAlbumDetail.getString("memberIds").toString();
//                    String[] mystring = edit_album_selectedids.split(",");
//                    int size = mystring.length;
//                    tv_getCount.setText("You have added " + size + " sub groups");
//                    iv_edit.setVisibility(View.VISIBLE);
//                } else {
//                    galleryType = "2";
//                    d_radio2.setChecked(true);
//                    edit_album_selectedids = jsonAlbumDetail.getString("memberIds").toString();
//                    inputids = jsonAlbumDetail.getString("memberIds").toString();
//                    String[] mystring = edit_album_selectedids.split(",");
//                    int size = mystring.length;
//                    tv_getCount.setText("You have added " + size + " members");
//                    iv_edit.setVisibility(View.VISIBLE);
//                }

                d_radio0.setChecked(true);
                galleryType = "0";
                shareType = jsonAlbumDetail.getString("shareType");

                Ismedia = jsonAlbumDetail.getString("Ismedia");


                sp_category.setSelection(Integer.parseInt(jsonAlbumDetail.getString("albumCategoryID")) - 1);
                sp_category.setSelection(getIndex(categoryList, jsonAlbumDetail.getString("albumCategoryText")));
                Log.d("Index", getIndex(categoryList, jsonAlbumDetail.getString("albumCategoryText")) + "");


                //funding fetched Data

                sp_funding.setSelection( Integer.parseInt( jsonAlbumDetail.getString( "Fk_FundingID" ) ) - 1 );
                sp_funding.setSelection( getIndexFunding( fundingList, jsonAlbumDetail.getString( "FundingText" ) ) );
                Log.d( "Index", getIndexFunding( fundingList, jsonAlbumDetail.getString( "FundingText" ) ) + "" );


                //Added UpdatedId

                subcategoryID = jsonAlbumDetail.getString("Fk_SubcategoryID");
                subcategoryselectedtext = jsonAlbumDetail.getString("SubcategoryText");
                sp_subcategory.setSelection(getIndexforsubcategory(subcategoryListupdate, subcategoryselectedtext));

                subtosubcategoryID = jsonAlbumDetail.getString("Fk_SubTosubcategoryID");
                subtosubcategoryselectedtext = jsonAlbumDetail.getString("SubtosubcategoryText");
                sp_subtosubcategory.setSelection(getIndexforsubtosubcategory(subtosubcategoryListupdate, subtosubcategoryselectedtext));


                if (shareType.equals("0")) {
                    //Club Service of data should be displayed
                  /*  rbInClub.setChecked(true);
                    rbPublic.setChecked(false);

                    //  attendance_layout.setVisibility(View.VISIBLE);
                    // meeting_layout.setVisibility(View.VISIBLE);
                    rotarctors_layout.setVisibility(View.GONE);
                    et_rotractors.setVisibility(View.GONE);
                    media_layout.setVisibility(View.GONE);*/
                    //  mediatitle.setVisibility(View.GONE);
                    // mediaradiolayot.setVisibility(View.GONE);
                } else if (shareType.equals("1")) {
                  /*  rbPublic.setChecked(true);
                    rbInClub.setChecked(false);
                    //Rotary service of data should de displayed

                    //  attendance_layout.setVisibility(View.GONE);
                    //  meeting_layout.setVisibility(View.GONE);
                    rotarctors_layout.setVisibility(View.VISIBLE);
                    et_rotractors.setVisibility(View.VISIBLE);
                    media_layout.setVisibility(View.VISIBLE);
                    //  mediatitle.setVisibility(View.VISIBLE);
                    // mediaradiolayot.setVisibility(View.VISIBLE);*/
                }

                if (Ismedia.equals("1")) {
                    //Yes check box is on
                    authlayout.setVisibility(View.VISIBLE);
                    // mediaradiolayot.setVisibility(View.VISIBLE);
                    radio_mediaprint_No.setChecked(false);
                    radio_mediaprint_yes.setChecked(true);

                    //this two is added By Gaurav for set MediaPhoto Description and Pic

                    mediaPhotoPath = jsonAlbumDetail.getString("Mediaphoto");
                    //this is added new
                    MediaphotoID = jsonAlbumDetail.getString("MediaphotoID");
                    if (mediaPhotoPath.equals("")) {
                        //Image is deleted
                    } else {

                        Picasso.with(DTEditAlbumActivity.this).load(jsonAlbumDetail.getString("Mediaphoto").toString())
                                //.fit()
                                //.resize(200, 200)
                                .placeholder(R.drawable.placeholder_new)
                                .into(iv_album_photo_auth);
                        iv_album_photo_auth.setBackground(null);
                        et_album_photo1_auth.setText(jsonAlbumDetail.getString("MediaDesc").toString());
                        if (iv_album_photo_auth.getDrawable() != null) {
                            close6.setVisibility(View.VISIBLE);
                        }
                    }


                    getAlbumPhotos();

                } else {
                    //No check box is on
                    authlayout.setVisibility(View.GONE);
                    // mediaradiolayot.setVisibility(View.GONE);
                    radio_mediaprint_yes.setChecked(false);
                    radio_mediaprint_No.setChecked(true);
                    //clear if previous data displayed
                    if (iv_album_photo_auth.getDrawable() != null) {

                        iv_album_photo_auth.setBackground(getResources().getDrawable(R.drawable.asset));
                        iv_album_photo_auth.setImageResource(0);
                        //  radio_mediaprint_No.setChecked(true);
                        // radio_mediaprint_yes.setChecked(false);
                        Ismedia = "0";
                        MediaphotoID = "0";
                        // mediaPhotoPath="";
                        et_album_photo1_auth.setText("");


                        close6.setVisibility(View.GONE);

                    }

                    getAlbumPhotos();

                }


                edt_title.setText(jsonAlbumDetail.getString("albumTitle"));
                edt_description.setText(jsonAlbumDetail.getString("albumDescription"));
                et_categoryName.setText(jsonAlbumDetail.getString("othercategorytext"));
                et_subcategoryName.setText(jsonAlbumDetail.getString("OtherSubCategory"));
                et_Beneficiary.setText(jsonAlbumDetail.getString("beneficiary"));
                et_COP.setText(jsonAlbumDetail.getString("project_cost"));
                et_rotractors.setText(jsonAlbumDetail.getString("Rotaractors"));

                projectDate = jsonAlbumDetail.getString("project_date");
                Date date = format1.parse(projectDate);
                et_DOP.setText(format.format(date));
                //et_DOP.setText(jsonAlbumDetail.getString("project_date"));

                et_manPower.setText(jsonAlbumDetail.getString("working_hour"));
                et_noOfRotarians.setText(jsonAlbumDetail.getString("NumberOfRotarian"));


                //this is spinner subcategory selected text & index
                //  sp_subcategory.setSelection(Integer.parseInt(subcategoryIDupdate) - 1);


                //  sp_subcategory.setSelection(getIndexforsubcategory(subcategoryList, jsonAlbumDetail.getString("SubcategoryText")));
                //this is spinner subtosubcategory selected text & index

                //  sp_subtosubcategory.setSelection(Integer.parseInt(jsonAlbumDetail.getString("Fk_SubTosubcategoryID")) - 1);

                // sp_subtosubcategory.setSelection(getIndexforsubtosubcategory(subtosubcategoryList, jsonAlbumDetail.getString("SubtosubcategoryText")));


                if (!jsonAlbumDetail.getString("cost_of_project_type").isEmpty()) {
                    et_currency.setSelection(Integer.parseInt(jsonAlbumDetail.getString("cost_of_project_type")) - 1);
                } else {
                    et_currency.setSelection(0);
                }
                sp_timeType.setSelection(timeType.indexOf(jsonAlbumDetail.getString("working_hour_type")));
//


            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
            e.printStackTrace();
        }
    }

    private int getIndex(ArrayList<AlbumData> list, String myString) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDistrict_Name().toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }
    private int getIndexFunding(ArrayList<FundingData> list, String myString) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getFund_Name().toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    private int getIndexforsubcategory(ArrayList<AlbumData> list, String myString) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSubcategoryNameupdate().toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    private int getIndexforsubtosubcategory(ArrayList<AlbumData> list, String myString) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSubtosubcategorynameupdate().toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
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
        String shareType = "";
        arrayList.add(new BasicNameValuePair("dateofproject", projectDate));


        if (rbInClub.isChecked()) {
            shareType = "0";
            arrayList.add(new BasicNameValuePair("shareType", shareType));

            arrayList.add(new BasicNameValuePair("costofproject", ""));
            arrayList.add(new BasicNameValuePair("beneficiary", ""));
            arrayList.add(new BasicNameValuePair("manhourspent", ""));
            arrayList.add(new BasicNameValuePair("categoryId", ""));
            arrayList.add(new BasicNameValuePair("manhourspenttype", ""));
            arrayList.add(new BasicNameValuePair("NumberofRotarian", ""));
            arrayList.add(new BasicNameValuePair("OtherCategorytext", ""));
            arrayList.add(new BasicNameValuePair("costofprojecttype", ""));

        } else if (rbPublic.isChecked()) {
            shareType = "1";
            arrayList.add(new BasicNameValuePair("shareType", shareType));
            arrayList.add(new BasicNameValuePair("costofproject", et_COP.getText().toString()));

            arrayList.add(new BasicNameValuePair("beneficiary", et_Beneficiary.getText().toString()));
            arrayList.add(new BasicNameValuePair("TempBeneficiary", temp_beneficiary));
            arrayList.add(new BasicNameValuePair("TempBeneficiary_flag", temp_beneficiary_flag));

            //this is the below condition for subcategoryid,subtosubcategoryid  and othertext passing
            if (flagforcategoryothertext == 0) {
                //when other option disable on category spinner

                arrayList.add(new BasicNameValuePair("Fk_SubcategoryID", subcategoryID));
                arrayList.add(new BasicNameValuePair("OtherCategorytext", ""));

            } else {
                //when other option enable on category spinner
                arrayList.add(new BasicNameValuePair("Fk_SubcategoryID", "0"));
                arrayList.add(new BasicNameValuePair("OtherCategorytext", et_categoryName.getText().toString()));

            }

            if (flagforsubcategoryothertext == 0) {
                //when other option disable on subcategory spinner


                arrayList.add(new BasicNameValuePair("Fk_SubTosubcategoryID", subtosubcategoryID));
                arrayList.add(new BasicNameValuePair("OtherSubCategory", ""));

            } else {
                //when other option enable on subcategory spinner
                arrayList.add(new BasicNameValuePair("Fk_SubTosubcategoryID", "0"));
                arrayList.add(new BasicNameValuePair("OtherSubCategory", et_subcategoryName.getText().toString()));

            }


            arrayList.add(new BasicNameValuePair("manhourspent", et_manPower.getText().toString()));
            arrayList.add(new BasicNameValuePair("categoryId", categoryID));
            arrayList.add( new BasicNameValuePair( "Fk_Funding_Id", fundingID ) );
            arrayList.add(new BasicNameValuePair("manhourspenttype", "Hours"));
            arrayList.add(new BasicNameValuePair("NumberofRotarian", et_noOfRotarians.getText().toString()));
            //  arrayList.add(new BasicNameValuePair("OtherCategorytext", et_categoryName.getText().toString()));
            arrayList.add(new BasicNameValuePair("costofprojecttype", (et_currency.getSelectedItemPosition() + 1) + ""));
        }
        arrayList.add(new BasicNameValuePair("Attendance", "0"));
        arrayList.add(new BasicNameValuePair("AttendancePer", "0"));
        arrayList.add(new BasicNameValuePair("MeetingType", "0"));
        arrayList.add(new BasicNameValuePair("AgendaDocID", "0"));
        arrayList.add(new BasicNameValuePair("MOMDocID", "0"));

        arrayList.add(new BasicNameValuePair("Rotaractors", et_rotractors.getText().toString()));
        arrayList.add(new BasicNameValuePair("Ismedia", Ismedia));
        //this is added By new
        //  arrayList.add(new BasicNameValuePair("Mediaphoto", mediaPhotoPath));
        arrayList.add(new BasicNameValuePair("MediaphotoID", MediaphotoID));


        if (Ismedia.equals("1") && radio_mediaprint_yes.isChecked()) {
            arrayList.add(new BasicNameValuePair("MediaDesc", et_album_photo1_auth.getText().toString()));

        }


        Utils.log("Currency item position:  " + et_currency.getSelectedItemPosition() + "");

        Log.d("Response", "PARAMETERS " + Constant.AddUpdateAlbum_New + " :- " + arrayList.toString());
        new EditAlbumAsyncTask(Constant.AddUpdateAlbum_New, arrayList, DTEditAlbumActivity.this).execute();
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

                albumId = ActivityResult.getString("galleryid");

                if (iv_image.getDrawable() != null) {
                    descList.remove(0);
                    descList.add(0, et_coverPhoto.getText().toString());
                    String img_des_val1 = et_coverPhoto.getText().toString();
                    System.out.println("new_value111" + img_des_val1);
                }

                if (iv_album_photo.getDrawable() != null) {
                    descList.remove(1);
                    descList.add(1, et_album_photo1.getText().toString());
                }

                if (iv_album_photo2.getDrawable() != null) {
                    descList.remove(2);
                    descList.add(2, et_album_photo2.getText().toString());
                }

                if (iv_album_photo3.getDrawable() != null) {
                    descList.remove(3);
                    descList.add(3, et_album_photo3.getText().toString());
                }

                if (iv_album_photo4.getDrawable() != null) {
                    descList.remove(4);
                    descList.add(4, et_album_photo4.getText().toString());
                }

                getAlbumImagesData();

//                finish();//finishing activity
//
//                if (tv_add.getText().equals("Done")) {
//                    Toast.makeText(DTEditAlbumActivity.this, "Album Updated successfully.", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(DTEditAlbumActivity.this, "Album Updated successfully.", Toast.LENGTH_SHORT).show();
//                }

                Log.d("Touchbase", "*************** " + status);
                Log.d("Touchbase", "*************** " + msg);

            } else {

                Toast.makeText(DTEditAlbumActivity.this, "Failed to update " + header + " image. Please retry.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }

    private void getAlbumImagesData() {

        ArrayList<UploadPhotoData> uploadPhotoDataArrayList = new ArrayList<>();

        for (int i = 0; i < imageList.size(); i++) {

            String photoID = PhotoIdList.get(i);

            /*if(photoID.equalsIgnoreCase("0")){

                if(!imageList.get(i).toString().isEmpty()){
                    data = new UploadPhotoData(PhotoIdList.get(i).toString(), imageList.get(i).toString(), descList.get(i).toString(), albumId, groupId, createdBy, "0");
                    long id = addPhotoModel.insert(data);
                }

            } else {
                data = new UploadPhotoData(PhotoIdList.get(i).toString(), imageList.get(i).toString(), descList.get(i).toString(), albumId, groupId, createdBy, "0");
                long id = addPhotoModel.insert(data);
            }*/

            if (photoID.equalsIgnoreCase("0")) {

                if (!imageList.get(i).isEmpty()) {
                    data = new UploadPhotoData(PhotoIdList.get(i), imageList.get(i), descList.get(i), albumId, groupId, createdBy, "0");
                    uploadPhotoDataArrayList.add(data);
                }

            } else {
                data = new UploadPhotoData(PhotoIdList.get(i), imageList.get(i), descList.get(i), albumId, groupId, createdBy, "0");
                uploadPhotoDataArrayList.add(data);
            }

        }

        if (uploadPhotoDataArrayList.size() == 0) {
            Intent intent1 = new Intent();
            setResult(Activity.RESULT_OK, intent1);
            Toast.makeText(DTEditAlbumActivity.this, header + " updated successfully.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            new UploadPhotoAsyncTask(uploadPhotoDataArrayList).execute();
        }

       /* Intent intent1 = new Intent();
        setResult(Activity.RESULT_OK, intent1);
        Toast.makeText(DTEditAlbumActivity.this, "Activity updated successfully.", Toast.LENGTH_SHORT).show();
        finish();

        Log.d("UploadPhotoService", "UploadPhotoService is Called");
        Intent intent = new Intent(DTEditAlbumActivity.this, UploadPhotoService.class);
        startService(intent);*/
    }

    public class UploadPhotoAsyncTask extends AsyncTask<String, Object, Object> {

        ArrayList<UploadPhotoData> allImagesList;

        final ProgressDialog progressDialog = new ProgressDialog(DTEditAlbumActivity.this, R.style.TBProgressBar);

        public UploadPhotoAsyncTask(ArrayList<UploadPhotoData> pList) {
            allImagesList = pList;
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

            String val = null;

            for (UploadPhotoData uploadPhotoData : allImagesList) {

                String url = Constant.AddUpdateAlbumPhoto;

                try {

                    url = url + "?photoId=" + uploadPhotoData.getPhotoId() + "&desc=" + URLEncoder.encode(uploadPhotoData.getDescription(), "UTF-8") + "&albumId=" + uploadPhotoData.getAlbumId() + "&groupId=" + uploadPhotoData.getGroupd() + "&createdBy=" + uploadPhotoData.getCreatedBy();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                val = UploadPhotoToServer(new File(uploadPhotoData.getPhotoPath()), url);
                System.out.println("new value1----" + val);

                Log.d("UploadPhotoService", "UploadPhotoService file path =>" + uploadPhotoData.getPhotoPath());

                Log.e("UploadPhotoService", "url=>" + url + "\nresult=>" + val);

                Log.e("UploadPhotoService1", "result1=>" + val);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {

            super.onPostExecute(result);

            if (!DTEditAlbumActivity.this.isFinishing()) {
                progressDialog.dismiss();
            }

            Intent intent1 = new Intent();
            setResult(Activity.RESULT_OK, intent1);
            Toast.makeText(DTEditAlbumActivity.this, header + "  updated successfully.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    public String UploadPhotoToServer(File file_path, String url) {

        String isUploaded = "";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.d("Uri", "Do file path" + file_path);

        try {

            HttpClient client = new DefaultHttpClient();
            //use your server path of php file
            org.apache.http.entity.mime.MultipartEntity reqEntity = new org.apache.http.entity.mime.MultipartEntity();
            HttpPost post = new HttpPost(url);
            if (file_path.exists()) {
                FileBody bin1 = new FileBody(file_path);
                //  Log.d("Enter", "Filebody complete " + bin1);
                reqEntity.addPart("uploaded_file", bin1);
                //reqEntity.addPart("email", new StringBody(useremail));
            }
            post.setEntity(reqEntity);
            //  Log.d("Enter", "Image send complete");

            HttpResponse response = client.execute(post);
            HttpEntity resEntity = response.getEntity();
            //  Log.d("Enter", "Get Response");
            try {

                final String response_str = EntityUtils.toString(resEntity);
                JSONObject jsonObj = new JSONObject(response_str);
                JSONObject ActivityResult = jsonObj.getJSONObject("LoadImageResult");
                final String status = ActivityResult.getString("status");
                if (status.equals("0")) {
                    isUploaded = ActivityResult.getString("message");
                }
            } catch (Exception ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            }
        } catch (Exception e) {
            Log.e("Upload Exception", "");
            e.printStackTrace();
        }
        Log.d("TOUCHBASE", "ID IN FILE UPLOAD CALL --" + isUploaded);
        return isUploaded;
    }

    private void getCategoryList() {

        try {
//            progressDialog=new ProgressDialog(AddAlbum.this,R.style.TBProgressBar);
//            progressDialog.setCancelable(false);
//            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//            progressDialog.show();
            final ProgressDialog progressDialog = new ProgressDialog(DTEditAlbumActivity.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject();
            requestData.put("DistrictID", "0");

            Log.d("Response", "PARAMETERS " + Constant.GetShowcaseDetails + " :- " + requestData.toString());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.GetShowcaseDetails, requestData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    JSONObject result;
                    setAllShowcaseDetails(response);
                    Utils.log(response.toString());
                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    if(progressDialog!=null){
//                        progressDialog.dismiss();
//                    }
                    progressDialog.dismiss();
                    Utils.log("VollyError:- " + error.toString());
                    //showErrorDialog();
                    //Utils.showMsg(context, "Something went wrong");
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            });

            request.setRetryPolicy(
                    new DefaultRetryPolicy(120000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(this, request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setAllShowcaseDetails(JSONObject response) {

        try {

            JSONObject ShowcaseDetails = response.getJSONObject("ShowcaseDetails");
            String status = ShowcaseDetails.getString("status");

            if (status.equalsIgnoreCase("0")) {

                JSONObject result = ShowcaseDetails.getJSONObject("Result");
                JSONArray Categories = result.getJSONArray("Categories");
                JSONArray subCategories = result.getJSONArray("subcat");
                JSONArray subtosubCategories = result.getJSONArray("subtosubcat");
                JSONArray funding = result.getJSONArray( "Fundinglist" );



                for (int i = 0; i < Categories.length(); i++) {

                    JSONObject categoryObj = Categories.getJSONObject(i);

                    AlbumData data = new AlbumData();
                    data.setDistrict_id(categoryObj.getString("ID"));
                    data.setDistrict_Name(categoryObj.getString("Name"));
                    categoryList.add(i, data);
                }

                // categoryList.remove(0);
                adapter = new SpinnerAdapter(this, categoryList);
                sp_category.setAdapter(adapter);

                //Add this for subcategory data for Gaurav


                for (int i = 0; i < subCategories.length(); i++) {

                    // AlbumData data = new AlbumData();
                    newdataforsubcategory = new AlbumData();
                    //First time added select value

                    JSONObject subcategoryObj = subCategories.getJSONObject(i);
                    newdataforsubcategory.setPk_subcategoryId(subcategoryObj.getString("pk_subcategoryId"));
                    newdataforsubcategory.setFk_CategoryID(subcategoryObj.getString("fk_CategoryID"));
                    newdataforsubcategory.setSubcategoryName(subcategoryObj.getString("SubcategoryName"));


                    subcategoryList.add(i, newdataforsubcategory);
                }
                //Add this for subtosubcategory data for Gaurav comes from server Data


                for (int i = 0; i < subtosubCategories.length(); i++) {
                    newdataforsubtosubcategory = new AlbumData();
                    JSONObject subtosubcategoryObj = subtosubCategories.getJSONObject(i);
                    newdataforsubtosubcategory.setPk_subtosubcategoryId(subtosubcategoryObj.getString("pk_subtosubcategoryId"));
                    newdataforsubtosubcategory.setFk_subcategoryid(subtosubcategoryObj.getString("fk_subcategoryid"));
                    newdataforsubtosubcategory.setFk_categoryID(subtosubcategoryObj.getString("fk_categoryID"));
                    newdataforsubtosubcategory.setSubtosubcategoryname(subtosubcategoryObj.getString("subtosubcategoryname"));


                    subtosubcategoryList.add(i, newdataforsubtosubcategory);
                }

                //Add this for funding data for Gaurav

                FundingData dataDefault=new FundingData(  );
                dataDefault.setPk_Fund_ID( "0" );
                dataDefault.setFund_Name( "Select" );
                fundingList.add( 0,dataDefault );

                for (int i = 0; i < funding.length(); i++) {
                    JSONObject fundingObj = funding.getJSONObject( i );
                    FundingData data = new FundingData();
                    data.setPk_Fund_ID( fundingObj.getString( "Pk_Fund_ID" ) );
                    data.setFund_Name( fundingObj.getString( "Fund_Name" ) );
                    fundingList.add( i+1, data );
                }
                fundingadapter = new SpinnerFundingAdapter( context, fundingList );
                sp_funding.setAdapter( fundingadapter );


                loadFromServer();
                //  getAlbumPhotos();
                //progressDialog.dismiss();
            } else {
                //progressDialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //progressDialog.dismiss();
        }

    }

    private void getAlbumPhotos() {

        try {

            JSONObject requestData = new JSONObject();
            requestData.put("albumId", albumId);
            requestData.put("groupId", groupId);

            Log.d("Response", "PARAMETERS " + Constant.GetAlbumPhotoList_New + " :- " + requestData.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.GetAlbumPhotoList_New, requestData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    JSONObject result;
                    //globalResponse=response;
                    getPhotos(response);
                    //loadRssBlogs();
                    Utils.log(response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.log("VollyError:- " + error.toString());
                    //Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_LONG).show();
                }
            });

            request.setRetryPolicy(
                    new DefaultRetryPolicy(120000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(DTEditAlbumActivity.this, request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getPhotos(JSONObject response) {
        try {

            JSONObject jsonTBAlbumPhotoListResult = response.getJSONObject("TBAlbumPhotoListResult");
            final String status = jsonTBAlbumPhotoListResult.getString("status");

            if (status.equals("0")) {

                // updatedOn = jsonTBAlbumPhotoListResult.getString("updatedOn");


                //JSONObject jsonResult = jsonTBAlbumPhotoListResult.getJSONObject("Result");

                JSONArray jsonNewAlbumPhotoList = jsonTBAlbumPhotoListResult.getJSONArray("Result");

                int newAlbumPhotoListCount = jsonNewAlbumPhotoList.length();


                //Media is OFF

                for (int i = 0; i < newAlbumPhotoListCount; i++) {

                    //  AlbumPhotoData data = new AlbumPhotoData();

                    JSONObject result_object = jsonNewAlbumPhotoList.getJSONObject(i);


                    if (i == 0) {
                        close1.setVisibility(View.VISIBLE);
                        photoId1 = result_object.getString("photoId").toString();

                        PhotoIdList.remove(0);
                        PhotoIdList.add(0, photoId1);


                        Picasso.with(DTEditAlbumActivity.this).load(result_object.getString("url").toString())
                                //.fit()
                                //.resize(200, 200)
                                .placeholder(R.drawable.placeholder_new)
                                .into(iv_image);
                        iv_image.setBackground(null);
                        et_coverPhoto.setText(result_object.getString("description").toString());
                        imgFlag = 1;

                    } else if (i == 1) {
                        close2.setVisibility(View.VISIBLE);
                        photoId2 = result_object.getString("photoId").toString();

                        PhotoIdList.remove(1);
                        PhotoIdList.add(1, photoId2);


                        Picasso.with(DTEditAlbumActivity.this).load(result_object.getString("url").toString())
                                //.fit()
                                //.resize(200, 200)
                                .placeholder(R.drawable.placeholder_new)
                                .into(iv_album_photo);
                        iv_album_photo.setBackground(null);
                        et_album_photo1.setText(result_object.getString("description").toString());
                    } else if (i == 2) {
                        close3.setVisibility(View.VISIBLE);
                        photoId3 = result_object.getString("photoId").toString();

                        PhotoIdList.remove(2);
                        PhotoIdList.add(2, photoId3);


                        Picasso.with(DTEditAlbumActivity.this).load(result_object.getString("url").toString())
                                //.fit()
                                //.resize(200, 200)
                                .placeholder(R.drawable.placeholder_new)
                                .into(iv_album_photo2);
                        iv_album_photo2.setBackground(null);
                        et_album_photo2.setText(result_object.getString("description").toString());
                    } else if (i == 3) {
                        close4.setVisibility(View.VISIBLE);
                        photoId4 = result_object.getString("photoId").toString();
                        PhotoIdList.remove(3);
                        PhotoIdList.add(3, photoId4);
                        Picasso.with(DTEditAlbumActivity.this).load(result_object.getString("url").toString())
                                //.fit()
                                //.resize(200, 200)
                                .placeholder(R.drawable.placeholder_new)
                                .into(iv_album_photo3);
                        iv_album_photo3.setBackground(null);
                        et_album_photo3.setText(result_object.getString("description").toString());

                    } else if (i == 4) {
                        close5.setVisibility(View.VISIBLE);
                        photoId5 = result_object.getString("photoId").toString();
                        PhotoIdList.remove(4);
                        PhotoIdList.add(4, photoId5);
                        Picasso.with(DTEditAlbumActivity.this).load(result_object.getString("url").toString())
                                //.fit()
                                //.resize(200, 200)
                                .placeholder(R.drawable.placeholder_new)
                                .into(iv_album_photo4);
                        iv_album_photo4.setBackground(null);
                        et_album_photo4.setText(result_object.getString("description").toString());

                    }

                }


            }

        } catch (Exception e) {
            e.printStackTrace();
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
                    final Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    Bitmap bt = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
                    iv_image.setImageBitmap(bt);
                    iv_image.setBackground(null);


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
                            imgFlag = 1;
                            uploadedimgid = Utils.doFileUpload(new File(finalF.toString()), "gallery"); // Upload File to server
                            imageList.remove(0);
                            imageList.add(0, finalF.toString());
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (pd.isShowing())
                                        pd.dismiss();
                                    Log.d("TOUCHBASE", "FILE UPLOAD ID InnerThread  " + uploadedimgid);
                                    if (uploadedimgid.equals("0")) {
                                        Toast.makeText(DTEditAlbumActivity.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();
                                        //iv_image.setImageResource(R.drawable.edit_image);
                                        iv_image.setBackground(getResources().getDrawable(R.drawable.asset));
                                        imgFlag = 0;
                                        close1.setVisibility(View.GONE);
                                    } else {
                                        imgFlag = 1;
                                        iv_image.setImageBitmap(bitmap);
                                        iv_image.setBackground(null);
                                        close1.setVisibility(View.VISIBLE);

                                        flag = 1;
                                        ll_photos.setVisibility(View.VISIBLE);
                                        //  ll_photos.setVisibility(View.GONE);
                                    }
                                    //ll_photos.setVisibility(View.GONE);
                                    //  close1.setVisibility(View.VISIBLE);
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
                ImageCompression imageCompression = new ImageCompression();
                final String finalImagePath = imageCompression.compressImage(picturePath, getApplicationContext());

                final Bitmap thumbnail = (BitmapFactory.decodeFile(finalImagePath));
                Log.d("TOUCHBASE", "FILE PATH " + finalImagePath.toString());
                imageList.remove(0);
                imageList.add(0, finalImagePath.toString());
                ///-------------------------------------------------------------------
                pd = ProgressDialog.show(DTEditAlbumActivity.this, "", "Loading...", false);
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        uploadedimgid = Utils.doFileUpload(new File(finalImagePath), "gallery"); // Upload File to server
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (pd.isShowing())
                                    pd.dismiss();
                                if (uploadedimgid.equals("0")) {
                                    imgFlag = 0;
                                    Toast.makeText(DTEditAlbumActivity.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();
                                    //iv_image.setImageResource(R.drawable.edit_image);
                                    iv_image.setBackground(getResources().getDrawable(R.drawable.asset));
                                    close1.setVisibility(View.GONE);
                                } else {
                                    imgFlag = 1;
                                    iv_image.setImageBitmap(thumbnail);
                                    iv_image.setBackground(null);
                                    close1.setVisibility(View.VISIBLE);

                                    flag = 1;
                                    ll_photos.setVisibility(View.VISIBLE);
                                    //  ll_photos.setVisibility(View.GONE);
                                }
                                //Toast.makeText(Register.this, "Verify your account through the registered email id", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                thread.start();
            }

        } else if (requestCode == 6) {

            if (resultCode == Activity.RESULT_OK) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                final String picturePath = c.getString(columnIndex);
                c.close();
                ImageCompression imageCompression = new ImageCompression();
                final String finalImagePath = imageCompression.compressImage(picturePath, getApplicationContext());

                final Bitmap thumbnail = (BitmapFactory.decodeFile(finalImagePath));
                Log.d("TOUCHBASE", "FILE PATH " + finalImagePath.toString());
                //imageList.add(picturePath.toString());

                Utils.log("Images " + imageList.toString());

                if (flag == 1) {
                    close1.setVisibility(View.VISIBLE);
                    imageList.remove(0);
                    imageList.add(0, finalImagePath);
                    iv_image.setImageBitmap(thumbnail);
                    iv_image.setBackground(null);
                    ll_photos.setVisibility(View.VISIBLE);
                } else if (flag == 2) {
                    close2.setVisibility(View.VISIBLE);
                    imageList.remove(1);
                    imageList.add(1, finalImagePath.toString());
                    iv_album_photo.setImageBitmap(thumbnail);
                    iv_album_photo.setBackground(null);
                } else if (flag == 3) {
                    close3.setVisibility(View.VISIBLE);
                    imageList.remove(2);
                    imageList.add(2, finalImagePath.toString());
                    iv_album_photo2.setImageBitmap(thumbnail);
                    iv_album_photo2.setBackground(null);
                } else if (flag == 4) {
                    close4.setVisibility(View.VISIBLE);
                    imageList.remove(3);
                    imageList.add(3, finalImagePath.toString());
                    iv_album_photo3.setImageBitmap(thumbnail);
                    iv_album_photo3.setBackground(null);
                } else if (flag == 5) {
                    close5.setVisibility(View.VISIBLE);
                    imageList.remove(4);
                    imageList.add(4, finalImagePath.toString());
                    iv_album_photo4.setImageBitmap(thumbnail);
                    iv_album_photo4.setBackground(null);
                } else if (flag == 6) {
                    //  imageList.remove(0);
                    // imageList.add(0, finalImagePath);
                   /* iv_album_photo_auth.setImageBitmap(thumbnail);
                    iv_album_photo_auth.setBackground(null);
                    close6.setVisibility(View.VISIBLE);*/
                }

            }

        } else if (requestCode == 7) {
            //This is code Added By Gaurav
            if (resultCode == Activity.RESULT_OK) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                final String picturePath = c.getString(columnIndex);
                c.close();
                ImageCompression imageCompression = new ImageCompression();
                final String finalImagePath = imageCompression.compressImage(picturePath, getApplicationContext());

                final Bitmap thumbnail = (BitmapFactory.decodeFile(finalImagePath));
                Log.d("TOUCHBASE", "FILE PATH " + finalImagePath.toString());
                // imageList.remove(0);
                // imageList.add(0,finalImagePath.toString());
                //imgFlag = 1;
                ///-------------------------------------------------------------------
                pd = ProgressDialog.show(DTEditAlbumActivity.this, "", "Loading...", false);

                Thread thread = new Thread(new Runnable() {

                    public void run() {

                        MediaphotoID = Utils.doFileUpload(new File(finalImagePath), "gallery"); // Upload File to server
                        // MediaphotoID=uploadedimgid;
                        runOnUiThread(new Runnable() {

                            public void run() {

                                if (pd.isShowing())
                                    pd.dismiss();
                                if (MediaphotoID.equals("0")) {
                                    Toast.makeText(DTEditAlbumActivity.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();
                                    //iv_event_photo.setImageResource(R.drawable.edit_image);
                                    // imageList.remove(0);
                                    // imageList.add(0,"");
                                    iv_album_photo_auth.setBackground(getResources().getDrawable(R.drawable.asset));
                                    // imgFlag = 0;
                                    Ismedia = "0";
                                } else {
                                    ll_photos.setVisibility(View.VISIBLE);
                                    iv_album_photo_auth.setImageBitmap(thumbnail);
                                    iv_album_photo_auth.setBackground(null);
                                    close6.setVisibility(View.VISIBLE);
                                    Ismedia = "1";
                                }
                                //Toast.makeText(Register.this, "Verify your account through the registered email id", Toast.LENGTH_LONG).show();
                            }
                        });

                        // Utils.log("Images " + imageList.toString());
                    }
                });
                thread.start();
            }
        }
    }

    public void deletePhoto(String albumId, String photoId) {

        Log.e("Touchbase", "------ deletePhoto() is called");

        String url = Constant.DeletePhoto;
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("photoId", photoId));
        arrayList.add(new BasicNameValuePair("albumId", albumId));
        arrayList.add(new BasicNameValuePair("deletedBy", PreferenceManager.getPreference(DTEditAlbumActivity.this, PreferenceManager.GRP_PROFILE_ID)));

        DeletePhotoAsyncTask task = new DeletePhotoAsyncTask(url, arrayList, DTEditAlbumActivity.this);
        task.execute();

        Log.d("Request", "PARAMETERS " + Constant.DeletePhoto + " :- " + arrayList.toString());
    }


    public class DeletePhotoAsyncTask extends AsyncTask<String, Object, Object> {

        String val = null;
        ProgressDialog progressDialog = new ProgressDialog(DTEditAlbumActivity.this, R.style.TBProgressBar);
        Context con = null;
        String url = null;
        List<NameValuePair> argList = null;


        public DeletePhotoAsyncTask(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            con = ctx;
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

            progressDialog.dismiss();

            if (result != "") {
                getdeltedresult(result.toString());
                Log.d("Response", "calling DeleteAlbum");
            } else {
                Log.d("Response", "Null Resposnse");
            }

        }
    }

    private void getdeltedresult(String val) {

        try {

            JSONObject jsonObj = new JSONObject(val);
            JSONObject ActivityResult = jsonObj.getJSONObject("TBDelteAlbumPhoto");
            final String status = ActivityResult.getString("status");

            if (status.equals("0")) {

                String msg = ActivityResult.getString("message");

                if (deletePhotoflag == 1) {

                    imageList.remove(0);
                    imageList.add(0, "");
                    descList.remove(0);
                    descList.add(0, "");
                    PhotoIdList.remove(0);
                    PhotoIdList.add(0, "0");
                    photoId1 = "0";
                    imgFlag = 0;
                    iv_image.setBackground(getResources().getDrawable(R.drawable.asset));
                    iv_image.setImageResource(0);
                    close1.setVisibility(View.GONE);

                } else if (deletePhotoflag == 2) {

                    imageList.remove(1);
                    imageList.add(1, "");

                    descList.remove(1);
                    descList.add(1, "");

                    PhotoIdList.remove(1);
                    PhotoIdList.add(1, "0");
                    photoId2 = "0";
                    iv_album_photo.setBackground(getResources().getDrawable(R.drawable.asset));
                    iv_album_photo.setImageResource(0);
                    close2.setVisibility(View.GONE);
                } else if (deletePhotoflag == 3) {
                    imageList.remove(2);
                    imageList.add(2, "");

                    descList.remove(2);
                    descList.add(2, "");

                    PhotoIdList.remove(2);
                    PhotoIdList.add(2, "0");
                    photoId3 = "0";
                    iv_album_photo2.setBackground(getResources().getDrawable(R.drawable.asset));
                    iv_album_photo2.setImageResource(0);
                    close3.setVisibility(View.GONE);
                } else if (deletePhotoflag == 4) {

                    imageList.remove(3);
                    imageList.add(3, "");

                    descList.remove(3);
                    descList.add(3, "");

                    PhotoIdList.remove(3);
                    PhotoIdList.add(3, "0");
                    photoId4 = "0";
                    iv_album_photo3.setBackground(getResources().getDrawable(R.drawable.asset));
                    iv_album_photo3.setImageResource(0);
                    close4.setVisibility(View.GONE);

                } else if (deletePhotoflag == 5) {

                    imageList.remove(4);
                    imageList.add(4, "");

                    descList.remove(4);
                    descList.add(4, "");

                    PhotoIdList.remove(4);
                    PhotoIdList.add(4, "0");
                    photoId5 = "0";
                    iv_album_photo4.setBackground(getResources().getDrawable(R.drawable.asset));
                    iv_album_photo4.setImageResource(0);
                    close5.setVisibility(View.GONE);
                }
                Toast.makeText(DTEditAlbumActivity.this, header + " Photo deleted successfully.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);


        final CharSequence[] options;
        options = new CharSequence[]{"Choose from Gallery"};
        //options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
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
        // builder.show();
    }

    public boolean validation() {
        if (rbInClub.isChecked()) {
            if (edt_title.getText().toString().trim().matches("") || edt_title.getText().toString().trim() == null) {
                Toast.makeText(DTEditAlbumActivity.this, "Please enter Title", Toast.LENGTH_LONG).show();
                return false;
            }
//            if (edt_description.getText().toString().trim().matches("") || edt_description.getText().toString().trim() == null) {
//                Toast.makeText(DTEditAlbumActivity.this, "Please enter Description.", Toast.LENGTH_LONG).show();
//                return false;
//            }

//            if (imgFlag == 0) {
//                Toast.makeText(DTEditAlbumActivity.this, "Please select atleast one photo.", Toast.LENGTH_LONG).show();
//                return false;
//            }

            if (et_DOP.getText().toString().trim().matches("") || et_DOP.getText().toString().trim() == null) {
                Toast.makeText(DTEditAlbumActivity.this, "Please select date.", Toast.LENGTH_LONG).show();
                return false;
            }

            return true;
        } else if (rbPublic.isChecked()) {
            if (et_DOP.getText().toString().trim().matches("") || et_DOP.getText().toString().trim() == null) {
                Toast.makeText(DTEditAlbumActivity.this, "Please select date.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (et_COP.getText().toString().trim().matches("") || et_COP.getText().toString().trim() == null) {
                Toast.makeText(DTEditAlbumActivity.this, "Please enter cost.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (et_Beneficiary.getText().toString().trim().matches("") || et_Beneficiary.getText().toString().trim() == null) {
                Toast.makeText(DTEditAlbumActivity.this, "Please enter Direct Beneficiaries.", Toast.LENGTH_LONG).show();
                return false;
            }
            if (et_manPower.getText().toString().trim().matches("") || et_manPower.getText().toString().trim() == null) {
                Toast.makeText(DTEditAlbumActivity.this, "Please enter Man hours.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (et_noOfRotarians.getText().toString().trim().matches("") || et_noOfRotarians.getText().toString().trim() == null) {
                Toast.makeText(DTEditAlbumActivity.this, "Please enter Rotarians Involved.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (edt_title.getText().toString().trim().matches("") || edt_title.getText().toString().trim() == null) {
                Toast.makeText(DTEditAlbumActivity.this, "Please enter Title", Toast.LENGTH_LONG).show();
                return false;
            }

//            if (edt_description.getText().toString().trim().matches("") || edt_description.getText().toString().trim() == null) {
//                Toast.makeText(DTEditAlbumActivity.this, "Please enter Description.", Toast.LENGTH_LONG).show();
//                return false;
//            }

//            if (imgFlag == 0) {
//                Toast.makeText(DTEditAlbumActivity.this, "Please select atleast one photo.", Toast.LENGTH_LONG).show();
//                return false;
//            }


            if (sp_blankselect_SubCategory.equals("1")) {

                Toast.makeText(DTEditAlbumActivity.this, "Please select Category", Toast.LENGTH_LONG).show();

                return false;


            }

            if (sp_blankselect_SubtosubCategory.equals("1")) {

                Toast.makeText(DTEditAlbumActivity.this, "Please select SubCategory", Toast.LENGTH_LONG).show();

                return false;


            }

            if (flagforcategoryothertext == 1) {

                if (et_categoryName.getText().toString().trim().matches("") || et_categoryName.getText().toString().trim() == null) {

                  //  Toast.makeText(DTEditAlbumActivity.this, "Please enter Area of focus description.", Toast.LENGTH_LONG).show();
                    Toast.makeText(DTEditAlbumActivity.this, "Please specify Area of focus.", Toast.LENGTH_LONG).show();

                    return false;
                }

            }


            if (flagforsubcategoryothertext == 1) {

                if (et_subcategoryName.getText().toString().trim().matches("") || et_subcategoryName.getText().toString().trim() == null) {

                    Toast.makeText(DTEditAlbumActivity.this, "Please specify the category.", Toast.LENGTH_LONG).show();

                    return false;
                }


            }


            if (fundingIsSelect.equalsIgnoreCase( "1" )){

                Toast.makeText( DTEditAlbumActivity.this, "Please select the Source of Funding.", Toast.LENGTH_LONG ).show();

                return false;


            }


         /*   int benificiaryvalue = Integer.parseInt(et_Beneficiary.getText().toString());
            int maxbenificiaryValue = Integer.parseInt(maxBeneficiaries);

            if (benificiaryvalue<=maxbenificiaryValue){
                beneficiary=et_Beneficiary.getText().toString();
                temp_beneficiary="0";
                temp_beneficiary_flag="0";
                return true;


            }if(benificiaryvalue>maxbenificiaryValue){
                show_Popup_MaxBenificiary();
                return false;
            }*/
            if (radio_mediaprint_yes.isChecked()) {
                if (iv_album_photo_auth.getDrawable() == null) {
                    Toast.makeText(DTEditAlbumActivity.this, "Please select print media photo.", Toast.LENGTH_LONG).show();
                    return false;
                }
            }


            return true;
        } else {
            return false;
        }
    }

    private void clearselectedtext() {
        tv_getCount.setText("");
        iv_edit.setVisibility(View.GONE);
    }


    //this method is added By Gaurav for MaxBenificiary

    public void show_Popup_MaxBenificiary() {
        final Dialog dialog = new Dialog(DTEditAlbumActivity.this, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_for_maxbenificiary_new);
        TextView confirm = (TextView) dialog.findViewById(R.id.tv_ok);
        TextView cancel = (TextView) dialog.findViewById(R.id.tv_cancel);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                beneficiary = et_Beneficiary.getText().toString();
                temp_beneficiary = "0";
                temp_beneficiary_flag = "0";

                dialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show latter
                beneficiary = maxBeneficiaries.toString();
                temp_beneficiary = et_Beneficiary.getText().toString();
                temp_beneficiary_flag = "1";
                webservices();
                dialog.dismiss();

            }
        });
        dialog.show();

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

    //Added By Gaurav for remove the last character from the String eg.'s'

    public String method(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 's') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
