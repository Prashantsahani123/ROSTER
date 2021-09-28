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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.NEWROW.row.Utils.CloseSoftKeyboard;
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

import static com.NEWROW.row.Utils.PreferenceManager.MY_CATEGORY;

/**
 * Created by user on 06-09-2016.
 */
public class DTAddAlbum extends Activity {
    ArrayList<String> selectedsubgrp;
    TextView tv_title, tv_cancel, tv_clubServiceInfo;
    ImageView iv_backbutton, iv_edit, iv_event_photo, iv_album_photo, iv_album_photo_auth, iv_album_photo2, iv_album_photo3, iv_album_photo4;
    EditText et_coverPhoto, et_album_photo3, et_album_photo2, et_album_photo1, et_album_photo1_auth, et_album_photo4;
    RadioButton d_radio0, d_radio1, d_radio2, radio_mediaprint_yes, radio_mediaprint_No;
    TextView tv_getCount, tv_add, et_DOP;
    String galleryType = "0";
    ArrayList<DirectoryData> listaddmemberdata = new ArrayList<>();
    ArrayList<SubGoupData> listaddsubgrp = new ArrayList<>();
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission( this );
    SpinnerAdapter adapter;
    SpinnerAdapter_subcategoryupdate adapter_subcategory_update;
    SpinnerAdapter_subtosubcategoryupdate adapter_subtosubcategory_update;

    private String groupType = "1";
    private RadioGroup rgShare;
    private RadioButton rbInClub, rbPublic;

    private String hasimageflag = "0";
    private String galleryId = "0", albumId = "";
    ;
    private String edit_gallery_selectedids = "0";
    ProgressDialog pd;
    private String uploadedimgid = "0";
    //this is added By Gaurav for Media Photo
    private String MediaphotoID = "0";
    EditText et_gallery_title, et_description, et_categoryName, et_subcategoryName, et_subtosubcategoryName, et_noOfRotarians, et_COP, et_Beneficiary, et_manPower;
    String inputids = "";
    private String flag_callwebsercie = "0";
    private String moduleName = "";
    private Context context;
    Spinner sp_category, sp_subcategory, sp_subtosubcategory, et_currency, sp_timeType;
    ArrayList<AlbumData> categoryList = new ArrayList<>();
    ArrayList<AlbumData> subcategoryList = new ArrayList<>();
    ArrayList<AlbumData> subtosubcategoryList = new ArrayList<>();
    ArrayList<AlbumData> subcategoryListupdate = new ArrayList<>();
    ArrayList<AlbumData> subtosubcategoryListupdate = new ArrayList<>();


    String categoryID, subcategoryID, subtosubcategoryID, projectDate;
    ArrayList<String> currencyList = new ArrayList<>();
    ArrayList<String> timeType = new ArrayList<>();
    ProgressDialog progressDialog;
    TextView tv_TimeCountType;
    LinearLayout ll_rotaryServicecontent, ll_category, ll_subcategory, ll_subtosubcategory, ll_photos;
    int imgFlag = 0;
    int flag = 1, flagforcategoryothertext = 0, flagforsubcategoryothertext = 0, deletePhotoflag = 0;
    ArrayList<String> imageList = new ArrayList<>();
    ArrayList<String> descList = new ArrayList<>();
    UploadPhotoData data;
    UploadedPhotoModel addPhotoModel;
    String groupId = "";
    String createdBy = "";
    ImageView close1, close2, close3, close4, close5, close6;
    String isdelete = "false";

    //Added By Gaurav

    public AlbumData updateDataForSubcategory, updateDataForSubtosubcategory, newdataforsubcategory, newdataforsubtosubcategory;


    String maxBeneficiaries = "", District_Event = "1", Ismedia = "0";
    LinearLayout rotarctors_layout, media_layout, mediaradiolayot, authlayout;
    EditText et_rotractors;
    TextView mediatitle;

    String beneficiary, MediaDesc = "";
    String temp_beneficiary = "0", temp_beneficiary_flag = "0", sp_blankselect_SubCategory = "0", subtosubcategoryIdtest = "0", sp_blankselect_SubtosubCategory = "0";


    //funding
    private Spinner sp_funding;
    ArrayList<FundingData> fundingList = new ArrayList<>();

    private String fundingID;
    private EditText et_fundingName;
    SpinnerFundingAdapter fundingadapter;
    int flagforfundingothertext;
    private LinearLayout ll_funding;
    private String fundingIsSelect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.dt_add_album );
        context = this;
        tv_title = (TextView) findViewById( R.id.tv_title );
        iv_backbutton = (ImageView) findViewById( R.id.iv_backbutton );

        addPhotoModel = new UploadedPhotoModel( this );
        groupId = PreferenceManager.getPreference( this, PreferenceManager.GROUP_ID );
        createdBy = PreferenceManager.getPreference( this, PreferenceManager.GRP_PROFILE_ID );

        moduleName = PreferenceManager.getPreference( this, PreferenceManager.MODUEL_NAME, "Album" );

        d_radio0 = (RadioButton) findViewById( R.id.d_radio0 );
        d_radio1 = (RadioButton) findViewById( R.id.d_radio1 );
        d_radio2 = (RadioButton) findViewById( R.id.d_radio2 );
        d_radio0.setChecked( true );

        tv_getCount = (TextView) findViewById( R.id.getCount );
        iv_edit = (ImageView) findViewById( R.id.iv_edit );
        tv_cancel = (TextView) findViewById( R.id.tv_cancel );
        iv_event_photo = (ImageView) findViewById( R.id.iv_event_photo );
        iv_album_photo = (ImageView) findViewById( R.id.iv_album_photo );
        iv_album_photo2 = (ImageView) findViewById( R.id.iv_album_photo2 );
        iv_album_photo3 = (ImageView) findViewById( R.id.iv_album_photo3 );
        iv_album_photo4 = (ImageView) findViewById( R.id.iv_album_photo4 );

        et_coverPhoto = (EditText) findViewById( R.id.et_coverPhoto );
        et_album_photo1 = (EditText) findViewById( R.id.et_album_photo1 );
        et_album_photo2 = (EditText) findViewById( R.id.et_album_photo2 );
        et_album_photo3 = (EditText) findViewById( R.id.et_album_photo3 );
        et_album_photo4 = (EditText) findViewById( R.id.et_album_photo4 );
        et_album_photo1_auth = (EditText) findViewById( R.id.et_album_photo1_auth );


        tv_add = (TextView) findViewById( R.id.tv_done );

        et_gallery_title = (EditText) findViewById( R.id.et_galleryTitle );
        et_description = (EditText) findViewById( R.id.et_evetDesc );

        close1 = (ImageView) findViewById( R.id.close1 );
        close2 = (ImageView) findViewById( R.id.close2 );
        close3 = (ImageView) findViewById( R.id.close3 );
        close4 = (ImageView) findViewById( R.id.close4 );
        close5 = (ImageView) findViewById( R.id.close5 );
        close6 = (ImageView) findViewById( R.id.close2_auth );


        et_categoryName = (EditText) findViewById( R.id.et_categoryName );
        et_subcategoryName = (EditText) findViewById( R.id.et_subcategoryName );
        et_subtosubcategoryName = (EditText) findViewById( R.id.et_subtosubcategoryName );
        et_fundingName = (EditText) findViewById( R.id.et_fundingName );

        et_DOP = (TextView) findViewById( R.id.et_DOP );
        et_DOP.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker( et_DOP );
            }
        } );
        et_COP = (EditText) findViewById( R.id.et_COP );
        et_Beneficiary = (EditText) findViewById( R.id.et_Beneficiary );
        et_manPower = (EditText) findViewById( R.id.et_manPower );
        et_noOfRotarians = (EditText) findViewById( R.id.et_noOfRotarians );

        tv_clubServiceInfo = (TextView) findViewById( R.id.tv_clubServiceInfo );
        ll_rotaryServicecontent = (LinearLayout) findViewById( R.id.ll_rotaryServicecontent );
        ll_category = (LinearLayout) findViewById( R.id.ll_category );
        ll_subcategory = (LinearLayout) findViewById( R.id.ll_subcategory );
        ll_subtosubcategory = (LinearLayout) findViewById( R.id.ll_subtosubcategory );
        ll_funding = (LinearLayout) findViewById( R.id.ll_funding );

        ll_photos = (LinearLayout) findViewById( R.id.ll_photos );

        sp_category = (Spinner) findViewById( R.id.sp_category );
        sp_subcategory = (Spinner) findViewById( R.id.sp_subcategory );
        sp_subtosubcategory = (Spinner) findViewById( R.id.sp_subtosubcategory );
        sp_funding = (Spinner) findViewById( R.id.sp_funding );


        //this code Add By Gaurav
        Intent intent = getIntent();
        District_Event = intent.getExtras().getString( "serviceproject", "0" );
        maxBeneficiaries = intent.getExtras().getString( "maxBeneficiaries" );

        rotarctors_layout = (LinearLayout) findViewById( R.id.rotarctors_layout );
        et_rotractors = (EditText) findViewById( R.id.et_rotractors );
        media_layout = (LinearLayout) findViewById( R.id.media_layout );
        mediaradiolayot = (LinearLayout) findViewById( R.id.mediaradiolayot );
        mediatitle = (TextView) findViewById( R.id.mediatitle );
        authlayout = (LinearLayout) findViewById( R.id.authlayout );
        iv_album_photo_auth = (ImageView) findViewById( R.id.iv_album_photo_auth );
        radio_mediaprint_yes = (RadioButton) findViewById( R.id.radio_mediaprint_yes );
        radio_mediaprint_No = (RadioButton) findViewById( R.id.radio_mediaprint_No );


        sp_category.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AlbumData data = categoryList.get( position );
                AlbumData subdata = subcategoryList.get( position );

                categoryID = data.getDistrict_id();
                //  subcategoryListupdate.clear();

                ArrayList<AlbumData> subcategoryListchangeData = new ArrayList<>();

                //this is added By Gaurav

                updateDataForSubcategory = new AlbumData();

                updateDataForSubcategory.setPk_subcategoryIdupdate( "0" );
                updateDataForSubcategory.setFk_CategoryIDupdate( "0" );

                updateDataForSubcategory.setSubcategoryNameupdate( "Select" );


                subcategoryListchangeData.add( 0, updateDataForSubcategory );


                for (int i = 0; i < subcategoryList.size(); i++) {

                    if (categoryID.equals( subcategoryList.get( i ).getFk_CategoryID() )) {
                        updateDataForSubcategory = new AlbumData();
                        updateDataForSubcategory.setPk_subcategoryIdupdate( subcategoryList.get( i ).getPk_subcategoryId() );
                        updateDataForSubcategory.setFk_CategoryIDupdate( subcategoryList.get( i ).getFk_categoryID() );
                        updateDataForSubcategory.setSubcategoryNameupdate( subcategoryList.get( i ).getSubcategoryName() );
                        subcategoryListchangeData.add( updateDataForSubcategory );


                    }


                }
                subcategoryListupdate = subcategoryListchangeData;
                adapter_subcategory_update = new SpinnerAdapter_subcategoryupdate( context, subcategoryListupdate );
                sp_subcategory.setAdapter( adapter_subcategory_update );

                if (subcategoryListupdate.size() == 0 || subcategoryListupdate.size() == 1) {
                    ll_subcategory.setVisibility( View.GONE );
                    ll_subtosubcategory.setVisibility( View.GONE );
                    sp_blankselect_SubCategory = "0";
                    sp_blankselect_SubtosubCategory = "0";

                } else {
                    ll_subcategory.setVisibility( View.VISIBLE );
                }


                //this code end here
                if (categoryList.get( position ).getDistrict_Name().equalsIgnoreCase( "others" )) {
                    et_categoryName.setVisibility( View.VISIBLE );
                    flagforcategoryothertext = 1;
                    sp_blankselect_SubCategory = "0";
                    sp_blankselect_SubtosubCategory = "0";
                } else {
                    et_categoryName.setVisibility( View.GONE );
                    flagforcategoryothertext = 0;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );

        //funding spinner added by Gaurav on 9th Oct 2020 in District Project

        sp_funding.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                FundingData data = fundingList.get( position );

                fundingID = data.getPk_Fund_ID();

                if (data.getFund_Name().equalsIgnoreCase( "Select" ) || data.getPk_Fund_ID().equalsIgnoreCase( "0" )){
                    fundingIsSelect="1";
                }else{
                    fundingIsSelect="0";
                }
                if (fundingList.get( position ).getFund_Name().equalsIgnoreCase( "others" )) {
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


        //Added by sub and sub to sub category By Gaurav in district module

        sp_subcategory.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                AlbumData data = subcategoryListupdate.get( position );
                subcategoryID = data.getPk_subcategoryIdupdate();
                AlbumData subdata = subtosubcategoryList.get( position );

                AlbumData subtosubdata = subtosubcategoryList.get( position );
                subtosubcategoryIdtest = subtosubdata.getPk_subtosubcategoryId();


                ArrayList<AlbumData> subtosubcategoryListchangeData = new ArrayList<>();

                //this code is added By gaurav for select option
                updateDataForSubtosubcategory = new AlbumData();
                updateDataForSubtosubcategory.setPk_subtosubcategoryIdupdate( subtosubcategoryList.get( 0 ).getPk_subcategoryId() );
                updateDataForSubtosubcategory.setFk_subcategoryidupdate( subtosubcategoryList.get( 0 ).getFk_subcategoryid() );
                updateDataForSubtosubcategory.setFk_categoryIDupdate( subtosubcategoryList.get( 0 ).getFk_categoryID() );
                updateDataForSubtosubcategory.setSubtosubcategorynameupdate( "Select" );


                subtosubcategoryListchangeData.add( 0, updateDataForSubtosubcategory );


                for (int i = 0; i < subtosubcategoryList.size(); i++) {

                    if (subcategoryID.equals( subtosubcategoryList.get( i ).getFk_subcategoryid() )) {
                        updateDataForSubtosubcategory = new AlbumData();


                        updateDataForSubtosubcategory.setPk_subtosubcategoryIdupdate( subtosubcategoryList.get( i ).getPk_subtosubcategoryId() );
                        updateDataForSubtosubcategory.setFk_subcategoryidupdate( subtosubcategoryList.get( i ).getFk_subcategoryid() );
                        updateDataForSubtosubcategory.setFk_categoryIDupdate( subtosubcategoryList.get( i ).getFk_categoryID() );
                        updateDataForSubtosubcategory.setSubtosubcategorynameupdate( subtosubcategoryList.get( i ).getSubtosubcategoryname() );
                        subtosubcategoryListchangeData.add( updateDataForSubtosubcategory );

                    }


                }
                subtosubcategoryListupdate = subtosubcategoryListchangeData;
                adapter_subtosubcategory_update = new SpinnerAdapter_subtosubcategoryupdate( context, subtosubcategoryListupdate );
                sp_subtosubcategory.setAdapter( adapter_subtosubcategory_update );

                if (subtosubcategoryListupdate.size() == 0 || subtosubcategoryListupdate.size() == 1) {
                    ll_subtosubcategory.setVisibility( View.GONE );
                    sp_blankselect_SubtosubCategory = "0";

                } else {
                    ll_subtosubcategory.setVisibility( View.VISIBLE );
                }

                //this code end here

                if (subcategoryListupdate.get( position ).getSubcategoryNameupdate().equalsIgnoreCase( "Select" )) {
                    ll_subtosubcategory.setVisibility( View.GONE );
                    sp_blankselect_SubCategory = "1";
                    sp_blankselect_SubtosubCategory = "0";

                } else {
                    sp_blankselect_SubCategory = "0";
                }

                if (subcategoryListupdate.get( position ).getSubcategoryNameupdate().equalsIgnoreCase( "others" )) {
                    et_subcategoryName.setVisibility( View.VISIBLE );
                    ll_subtosubcategory.setVisibility( View.GONE );
                    flagforsubcategoryothertext = 1;
                    sp_blankselect_SubCategory = "0";
                    sp_blankselect_SubtosubCategory = "0";

                } else {
                    et_subcategoryName.setVisibility( View.GONE );
                    flagforsubcategoryothertext = 0;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );

        sp_subtosubcategory.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                AlbumData data = subtosubcategoryListupdate.get( position );
                // subtosubcategoryID = data.getFk_subcategoryidupdate();
                subtosubcategoryIdtest = data.getPk_subtosubcategoryIdupdate();

                if (subtosubcategoryListupdate.get( position ).getSubtosubcategorynameupdate().equalsIgnoreCase( "Select" )) {
                    sp_blankselect_SubtosubCategory = "1";
                } else {
                    sp_blankselect_SubtosubCategory = "0";

                }


                if (subtosubcategoryListupdate.get( position ).getSubtosubcategorynameupdate().equalsIgnoreCase( "others" )) {
                    et_subtosubcategoryName.setVisibility( View.VISIBLE );
                } else {
                    et_subtosubcategoryName.setVisibility( View.GONE );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );

        et_currency = (Spinner) findViewById( R.id.et_currency );
        currencyList.add( "\u20B9" );
        currencyList.add( "$" );
        ArrayAdapter adapter = new ArrayAdapter( DTAddAlbum.this, android.R.layout.simple_spinner_dropdown_item, currencyList );
        et_currency.setAdapter( adapter );

        sp_timeType = (Spinner) findViewById( R.id.sp_timeType );
        timeType.add( "Hours" );
        timeType.add( "Days" );
        timeType.add( "Months" );
        timeType.add( "Years" );
        ArrayAdapter adapter1 = new ArrayAdapter( DTAddAlbum.this, android.R.layout.simple_spinner_dropdown_item, timeType );
        sp_timeType.setAdapter( adapter1 );

        rgShare = (RadioGroup) findViewById( R.id.rgShare );
        rbInClub = (RadioButton) findViewById( R.id.rbInClub );

        rbPublic = (RadioButton) findViewById( R.id.rbPublic );


        //this code is added By Gaurav
        if (District_Event.equals( "0" )) {
            //District Event of data should be displayed
            tv_title.setText( "Add District Event" );
            rbInClub.setChecked( true );
            rbPublic.setChecked( false );

            //this is rotaractor field added
            rotarctors_layout.setVisibility( View.GONE );
            et_rotractors.setVisibility( View.GONE );

            media_layout.setVisibility( View.GONE );
            ll_funding.setVisibility( View.GONE );
            // mediaradiolayot.setVisibility(View.GONE);
            // mediatitle.setVisibility(View.GONE);


        } else if (District_Event.equals( "1" )) {
            //District Project of data should de displayed
            tv_title.setText( "Add District Project" );
            rbPublic.setChecked( true );
            rbInClub.setChecked( false );

            //this is rotaractor field added
            rotarctors_layout.setVisibility( View.VISIBLE );
            et_rotractors.setVisibility( View.VISIBLE );

            //medialayout

            media_layout.setVisibility( View.VISIBLE );
            ll_funding.setVisibility( View.VISIBLE );
            //  mediaradiolayot.setVisibility(View.VISIBLE);
            // mediatitle.setVisibility(View.VISIBLE);


        }

        if (rbInClub.isChecked()) {
            ll_rotaryServicecontent.setVisibility( View.GONE );
            ll_category.setVisibility( View.GONE );
            tv_clubServiceInfo.setText( getString( R.string.distEventInfo_new ) );
        }

        rbInClub.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ll_rotaryServicecontent.setVisibility( View.VISIBLE );
                ll_category.setVisibility( View.VISIBLE );
                if (isChecked) {
                    tv_clubServiceInfo.setText( getString( R.string.distEventInfo_new ) );
                }
            }
        } );


        if (rbPublic.isChecked()) {
            tv_clubServiceInfo.setText( getString( R.string.distProjectInfo_new ) );
        }

        rbPublic.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ll_rotaryServicecontent.setVisibility( View.GONE );
                ll_category.setVisibility( View.GONE );

                if (isChecked) {
                    tv_clubServiceInfo.setText( getString( R.string.distProjectInfo_new ) );
                }
            }
        } );

        for (int i = 0; i < 5; i++) {
            imageList.add( "" );
            descList.add( "" );
        }

        groupType = PreferenceManager.getPreference( context, MY_CATEGORY, "" + Constant.GROUP_CATEGORY_CLUB );
        Utils.log( "Group type is : " + groupType );
        clearselectedtext();
        init();
        getCategoryList();
    }

    private void clearselectedtext() {
        tv_getCount.setText( "" );
        iv_edit.setVisibility( View.GONE );
    }

    private void init() {
        /*iv_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i  = new Intent(AddAlbum.this,Gallery.class);
                startActivity(i);
            }

        });*/

        tv_cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );

        iv_edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (galleryType.equals( "2" )) {
                    Intent i = new Intent( DTAddAlbum.this, AddMembers.class );
                    //i.putParcelableArrayListExtra("name1", memberData);
                    i.putParcelableArrayListExtra( "selected_memberdata", listaddmemberdata );
                    i.putExtra( "edit_gallery_selectedids", edit_gallery_selectedids );
                    startActivityForResult( i, 3 );
                } else if (galleryType.equals( "1" )) {
                    /*Intent i = new Intent(AddAlbum.this, SubGroupList.class);
                    //i.putParcelableArrayListExtra("name1", memberData);
                    i.putParcelableArrayListExtra("selected_subgrpdata", listaddsubgrp);
                    i.putExtra("edit_gallery_selectedids", edit_gallery_selectedids);
                    startActivityForResult(i, 1);*/

                    Intent subgrp = new Intent( DTAddAlbum.this, NewGroupSelectionActivity.class );
                    subgrp.putExtra( "flag_addsubgrp", "1" );
                    subgrp.putExtra( "selected", selectedsubgrp );
                    subgrp.putExtra( "edit", "1" );
                    startActivityForResult( subgrp, 1 );
                }

            }
        } );

        iv_event_photo.setOnClickListener( new View.OnClickListener() {
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

        } );


        iv_album_photo.setOnClickListener( new View.OnClickListener() {
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
        } );


        iv_album_photo2.setOnClickListener( new View.OnClickListener() {
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
        } );
        iv_album_photo3.setOnClickListener( new View.OnClickListener() {
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
        } );

        iv_album_photo4.setOnClickListener( new View.OnClickListener() {
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
        } );
        // media photo


        iv_album_photo_auth.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!marshMallowPermission.checkPermissionForCamera()) {
                    marshMallowPermission.requestPermissionForCamera();
                } else {
                    if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                        marshMallowPermission.requestPermissionForExternalStorage();
                    } else {
                        flag = 6;
                        selectAlbumImagesForMediaPhoto();
                    }
                }

            }
        } );


        tv_add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Log.d("-----------------", "add is clicked");
                if (validation()) {

                    if (InternetConnection.checkConnection( getApplicationContext() )) {
                        webservices();


                        Thread t = new Thread() {
                            public void run() {
                                //This class Added By gaurav for hide soft keyboaard
                                CloseSoftKeyboard.hideSoftKeyboard( DTAddAlbum.this );
                            }
                        };
                        t.start();

                    } else {
                        Utils.showToastWithTitleAndContext( getApplicationContext(), "No Internet Connection!" );
                        // Not Available...
                    }
                }
            }
        } );

        close1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog( DTAddAlbum.this, android.R.style.Theme_Translucent );
                dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
                dialog.setContentView( R.layout.popup_confrm_delete );
                TextView tv_no = (TextView) dialog.findViewById( R.id.tv_no );
                TextView tv_yes = (TextView) dialog.findViewById( R.id.tv_yes );
                TextView tv_line1 = (TextView) dialog.findViewById( R.id.tv_line1 );
                tv_line1.setText( "Are you sure you want to delete this Photo" );

                tv_no.setOnClickListener( new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "false";

                    }
                } );

                tv_yes.setOnClickListener( new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        isdelete = "true";
                        deletePhotoflag = 1;
                        if (!imageList.get( 0 ).isEmpty()) {
                            imageList.remove( 0 );
                            imageList.add( 0, "" );
                            iv_event_photo.setBackground( getResources().getDrawable( R.drawable.asset ) );
                            iv_event_photo.setImageResource( 0 );
                            close1.setVisibility( View.GONE );
                        }

                    }
                } );

                dialog.show();
            }
        } );

        close2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog( DTAddAlbum.this, android.R.style.Theme_Translucent );
                dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
                dialog.setContentView( R.layout.popup_confrm_delete );
                TextView tv_no = (TextView) dialog.findViewById( R.id.tv_no );
                TextView tv_yes = (TextView) dialog.findViewById( R.id.tv_yes );
                TextView tv_line1 = (TextView) dialog.findViewById( R.id.tv_line1 );
                tv_line1.setText( "Are you sure you want to delete this Photo" );
                tv_no.setOnClickListener( new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "false";

                    }
                } );
                tv_yes.setOnClickListener( new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "true";
                        deletePhotoflag = 1;
                        if (!imageList.get( 1 ).isEmpty()) {
                            imageList.remove( 1 );
                            imageList.add( 1, "" );
                            iv_album_photo.setBackground( getResources().getDrawable( R.drawable.asset ) );
                            iv_album_photo.setImageResource( 0 );
                            close2.setVisibility( View.GONE );
                        }

                    }
                } );

                dialog.show();
            }
        } );

        close3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog( DTAddAlbum.this, android.R.style.Theme_Translucent );
                dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
                dialog.setContentView( R.layout.popup_confrm_delete );
                TextView tv_no = (TextView) dialog.findViewById( R.id.tv_no );
                TextView tv_yes = (TextView) dialog.findViewById( R.id.tv_yes );
                TextView tv_line1 = (TextView) dialog.findViewById( R.id.tv_line1 );
                tv_line1.setText( "Are you sure you want to delete this Photo" );
                tv_no.setOnClickListener( new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "false";

                    }
                } );
                tv_yes.setOnClickListener( new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "true";

                        deletePhotoflag = 1;
                        if (!imageList.get( 2 ).isEmpty()) {
                            imageList.remove( 2 );
                            imageList.add( 2, "" );
                            iv_album_photo2.setBackground( getResources().getDrawable( R.drawable.asset ) );
                            iv_album_photo2.setImageResource( 0 );
                            close3.setVisibility( View.GONE );
                        }
                    }
                } );

                dialog.show();
            }
        } );

        close4.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog( DTAddAlbum.this, android.R.style.Theme_Translucent );
                dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
                dialog.setContentView( R.layout.popup_confrm_delete );
                TextView tv_no = (TextView) dialog.findViewById( R.id.tv_no );
                TextView tv_yes = (TextView) dialog.findViewById( R.id.tv_yes );
                TextView tv_line1 = (TextView) dialog.findViewById( R.id.tv_line1 );
                tv_line1.setText( "Are you sure you want to delete this Photo" );
                tv_no.setOnClickListener( new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "false";

                    }
                } );
                tv_yes.setOnClickListener( new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "true";

                        deletePhotoflag = 1;
                        if (!imageList.get( 3 ).isEmpty()) {
                            imageList.remove( 3 );
                            imageList.add( 3, "" );
                            iv_album_photo3.setBackground( getResources().getDrawable( R.drawable.asset ) );
                            iv_album_photo3.setImageResource( 0 );
                            close4.setVisibility( View.GONE );
                        }
                    }
                } );

                dialog.show();
            }
        } );

        close5.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog( DTAddAlbum.this, android.R.style.Theme_Translucent );
                dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
                dialog.setContentView( R.layout.popup_confrm_delete );
                TextView tv_no = (TextView) dialog.findViewById( R.id.tv_no );
                TextView tv_yes = (TextView) dialog.findViewById( R.id.tv_yes );
                TextView tv_line1 = (TextView) dialog.findViewById( R.id.tv_line1 );
                tv_line1.setText( "Are you sure you want to delete this Photo" );
                tv_no.setOnClickListener( new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "false";

                    }
                } );
                tv_yes.setOnClickListener( new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "true";

                        deletePhotoflag = 1;
                        if (!imageList.get( 4 ).isEmpty()) {
                            imageList.remove( 4 );
                            imageList.add( 4, "" );
                            iv_album_photo4.setBackground( getResources().getDrawable( R.drawable.asset ) );
                            iv_album_photo4.setImageResource( 0 );
                            close5.setVisibility( View.GONE );
                        }
                    }
                } );

                dialog.show();
            }
        } );


        close6.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog( DTAddAlbum.this, android.R.style.Theme_Translucent );
                dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
                dialog.setContentView( R.layout.popup_confrm_delete );
                TextView tv_no = (TextView) dialog.findViewById( R.id.tv_no );
                TextView tv_yes = (TextView) dialog.findViewById( R.id.tv_yes );
                TextView tv_line1 = (TextView) dialog.findViewById( R.id.tv_line1 );
                tv_line1.setText( "Are you sure you want to delete this Photo" );

                tv_no.setOnClickListener( new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Ismedia = "1";
                        isdelete = "false";

                    }
                } );

                tv_yes.setOnClickListener( new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "true";
                        deletePhotoflag = 1;
                        iv_album_photo_auth.setBackground( getResources().getDrawable( R.drawable.asset ) );
                        iv_album_photo_auth.setImageResource( 0 );

                        Ismedia = "0";
                        close6.setVisibility( View.GONE );
                        /*if (!imageList.get(0).isEmpty()) {
                            imageList.remove(0);
                            imageList.add(0, "");
                            iv_album_photo_auth.setBackground(getResources().getDrawable(R.drawable.asset));
                            iv_album_photo_auth.setImageResource(0);
                            close6.setVisibility(View.GONE);
                        }*/

                    }
                } );

                dialog.show();
            }
        } );


    }

    public void onRadioButtonClicked(View view) {

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.d_radio0:

                inputids = "";
                d_radio1.setChecked( false );
                d_radio2.setChecked( false );
                d_radio2.setEnabled( true );
                d_radio1.setEnabled( true );

                clearselectedtext();
                break;
            case R.id.d_radio1:
                if (checked)
                    //set MM button to unchecked
                       /*d_radio0.setChecked(false);
                        d_radio2.setChecked(false);
                        d_radio1.setEnabled(false);
                        d_radio2.setEnabled(true);*/
                    d_radio1.setChecked( false );

                Intent subgrp = new Intent( DTAddAlbum.this, NewGroupSelectionActivity.class );
                subgrp.putExtra( "flag_addsubgrp", "1" );
                startActivityForResult( subgrp, 1 );
                //startActivity(subgrp);

                break;

            case R.id.d_radio2:
                if (checked) {
                    d_radio2.setChecked( false );
                    Intent i = new Intent( DTAddAlbum.this, AddMembers.class );
                    startActivityForResult( i, 3 );
                }

                // d_radio2.setEnabled(false);
                /*
                d_radio1.setEnabled(true);
                d_radio0.setChecked(false);
                d_radio1.setChecked(false);*/
                break;

            case R.id.radio_mediaprint_No:
                Ismedia = "0";

                //this condition for media photo clear if user click on no button
                if (iv_album_photo_auth.getDrawable() != null) {

                    iv_album_photo_auth.setBackground( getResources().getDrawable( R.drawable.asset ) );
                    iv_album_photo_auth.setImageResource( 0 );
                    //  radio_mediaprint_No.setChecked(true);
                    // radio_mediaprint_yes.setChecked(false);
                    Ismedia = "0";
                    MediaphotoID = "0";
                    // mediaPhotoPath="";
                    et_album_photo1_auth.setText( "" );


                    close6.setVisibility( View.GONE );

                }
                authlayout.setVisibility( View.GONE );
                radio_mediaprint_yes.setChecked( false );

                break;
            case R.id.radio_mediaprint_yes:
                //then auth of media displayed and text
                authlayout.setVisibility( View.VISIBLE );
                radio_mediaprint_No.setChecked( false );

                // Ismedia="1";
                break;
        }

    }

    public void datepicker(final TextView setdatetext) {
        // Get Current Date
        int mYear, mMonth, mDay, mHour, mMinute;
        final Calendar c = Calendar.getInstance();
        mYear = c.get( Calendar.YEAR );
        mMonth = c.get( Calendar.MONTH );
        mDay = c.get( Calendar.DAY_OF_MONTH );

        DatePickerDialog datePickerDialog = new DatePickerDialog( this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        try {
                            SimpleDateFormat format = new SimpleDateFormat( "dd MM yyyy" );
                            Date newDate = format.parse( dayOfMonth + " " + (monthOfYear + 1) + " " + year );

                            format = new SimpleDateFormat( "dd MMM yyyy" );
                            String date = format.format( newDate );
                            projectDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            setdatetext.setText( date );

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay );
        datePickerDialog.getDatePicker().setMaxDate( System.currentTimeMillis() );
        datePickerDialog.show();
    }

    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        String isSubGroupAdmin = "0";  // means no
        String isAdmin = PreferenceManager.getPreference( DTAddAlbum.this, PreferenceManager.IS_GRP_ADMIN, "No" );
        if (isAdmin.equalsIgnoreCase( "partial" )) {
            isSubGroupAdmin = "1";  // means yes
        }
        arrayList.add( new BasicNameValuePair( "isSubGrpAdmin", isSubGroupAdmin ) );
        arrayList.add( new BasicNameValuePair( "albumId", "0" ) );
        arrayList.add( new BasicNameValuePair( "groupId", PreferenceManager.getPreference( getApplicationContext(), PreferenceManager.GROUP_ID ) ) );
        arrayList.add( new BasicNameValuePair( "moduleId", PreferenceManager.getPreference( getApplicationContext(), PreferenceManager.MODULE_ID ) ) );
        arrayList.add( new BasicNameValuePair( "type", galleryType ) );
        arrayList.add( new BasicNameValuePair( "memberIds", inputids ) );
        arrayList.add( new BasicNameValuePair( "albumTitle", et_gallery_title.getText().toString() ) );
        arrayList.add( new BasicNameValuePair( "albumDescription", et_description.getText().toString() ) );
        arrayList.add( new BasicNameValuePair( "albumImage", uploadedimgid ) );
        arrayList.add( new BasicNameValuePair( "createdBy", PreferenceManager.getPreference( getApplicationContext(), PreferenceManager.GRP_PROFILE_ID ) ) );
        String shareType = "";
        arrayList.add( new BasicNameValuePair( "dateofproject", projectDate ) );
        if (rbInClub.isChecked()) {
            shareType = "0";
            arrayList.add( new BasicNameValuePair( "shareType", shareType ) );
            arrayList.add( new BasicNameValuePair( "costofproject", "" ) );
            arrayList.add( new BasicNameValuePair( "beneficiary", "" ) );
            arrayList.add( new BasicNameValuePair( "manhourspent", "" ) );
            arrayList.add( new BasicNameValuePair( "categoryId", "" ) );
            arrayList.add( new BasicNameValuePair( "manhourspenttype", "" ) );
            arrayList.add( new BasicNameValuePair( "NumberofRotarian", "" ) );
            arrayList.add( new BasicNameValuePair( "OtherCategorytext", "" ) );
            arrayList.add( new BasicNameValuePair( "costofprojecttype", "" ) );

        } else if (rbPublic.isChecked()) {
            shareType = "1";
            arrayList.add( new BasicNameValuePair( "shareType", shareType ) );
            arrayList.add( new BasicNameValuePair( "costofproject", et_COP.getText().toString() ) );
            arrayList.add( new BasicNameValuePair( "beneficiary", et_Beneficiary.getText().toString() ) );
            arrayList.add( new BasicNameValuePair( "TempBeneficiary", temp_beneficiary ) );
            arrayList.add( new BasicNameValuePair( "TempBeneficiary_flag", temp_beneficiary_flag ) );

            arrayList.add( new BasicNameValuePair( "manhourspent", et_manPower.getText().toString() ) );
            arrayList.add( new BasicNameValuePair( "categoryId", categoryID ) );
            arrayList.add( new BasicNameValuePair( "Fk_Funding_Id", fundingID ) );
            arrayList.add( new BasicNameValuePair( "manhourspenttype", "Hours" ) );
            arrayList.add( new BasicNameValuePair( "NumberofRotarian", et_noOfRotarians.getText().toString() ) );


            //this is the below condition for subcategoryid,subtosubcategoryid  and othertext passing
            if (flagforcategoryothertext == 0) {
                //when other option disable on category spinner

                arrayList.add( new BasicNameValuePair( "Fk_SubcategoryID", subcategoryID ) );
                arrayList.add( new BasicNameValuePair( "OtherCategorytext", "" ) );

            } else {
                //when other option enable on category spinner
                arrayList.add( new BasicNameValuePair( "Fk_SubcategoryID", "0" ) );
                arrayList.add( new BasicNameValuePair( "OtherCategorytext", et_categoryName.getText().toString() ) );

            }

            if (flagforsubcategoryothertext == 0) {
                //when other option disable on subcategory spinner


                arrayList.add( new BasicNameValuePair( "Fk_SubTosubcategoryID", subtosubcategoryIdtest ) );
                arrayList.add( new BasicNameValuePair( "OtherSubCategory", "" ) );

            } else {
                //when other option enable on subcategory spinner
                arrayList.add( new BasicNameValuePair( "Fk_SubTosubcategoryID", "0" ) );
                arrayList.add( new BasicNameValuePair( "OtherSubCategory", et_subcategoryName.getText().toString() ) );

            }
            // arrayList.add(new BasicNameValuePair("OtherCategorytext", et_categoryName.getText().toString()));
            arrayList.add( new BasicNameValuePair( "costofprojecttype", (et_currency.getSelectedItemPosition() + 1) + "" ) );
            arrayList.add( new BasicNameValuePair( "Rotaractors", et_rotractors.getText().toString() ) );
            arrayList.add( new BasicNameValuePair( "Ismedia", Ismedia ) );
            arrayList.add( new BasicNameValuePair( "MediaphotoID", MediaphotoID ) );
            if (Ismedia.equals( "1" )) {
                arrayList.add( new BasicNameValuePair( "MediaDesc", et_album_photo1_auth.getText().toString() ) );

            }


        }

        arrayList.add( new BasicNameValuePair( "Attendance", "" ) );
        arrayList.add( new BasicNameValuePair( "AttendancePer", "" ) );
        arrayList.add( new BasicNameValuePair( "MeetingType", "" ) );
        arrayList.add( new BasicNameValuePair( "AgendaDocID", "" ) );
        arrayList.add( new BasicNameValuePair( "MOMDocID", "" ) );


        flag_callwebsercie = "0";

        Log.d( "Response", "PARAMETERS " + Constant.AddUpdateAlbum_New + " :- " + arrayList.toString() );
        new WebConnectionAsyncLogin( Constant.AddUpdateAlbum_New, arrayList, DTAddAlbum.this ).execute();

//        Log.d("Response", "PARAMETERS " + Constant.AddAlbum + " :- " + arrayList.toString());
//        new WebConnectionAsyncLogin(Constant.AddAlbum, arrayList, DTAddAlbum.this).execute();
    }


    public class WebConnectionAsyncLogin extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog( DTAddAlbum.this, R.style.TBProgressBar );

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
            progressDialog.setCancelable( false );
            progressDialog.setProgressStyle( android.R.style.Widget_ProgressBar_Small );
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(String... params) {
            try {
                val = HttpConnection.postData( url, argList );
                val = val.toString();
                Log.d( "Response", "we" + val );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return val;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute( result );
            progressDialog.dismiss();
            //Log.d("response","Do post"+ result.toString());
            if (result != "") {
                if (flag_callwebsercie.equals( "0" )) {
                    getresult( result.toString() );
                } else if (flag_callwebsercie.equals( "1" )) {
                    // getresult_addeddata(result.toString());
                } else if (flag_callwebsercie.equals( "4" )) {
                    //getresultOfRemovephoto(result.toString());
                }
            } else {
                Log.d( "Response", "Null Resposnse" );
            }

        }

    }

    private void getresult(String val) {
        try {
            JSONObject jsonObj = new JSONObject( val );
            JSONObject ActivityResult = jsonObj.getJSONObject( "TBAddGalleryResult" );
            final String status = ActivityResult.getString( "status" );
            if (status.equals( "0" )) {
                String msg = ActivityResult.getString( "message" );

                Intent intent = new Intent();
                setResult( 1, intent );


                albumId = ActivityResult.getString( "galleryid" );

                if (iv_event_photo.getDrawable() != null) {
                    descList.remove( 0 );
                    descList.add( 0, et_coverPhoto.getText().toString() );
                }

                if (iv_album_photo.getDrawable() != null) {
                    descList.remove( 1 );
                    descList.add( 1, et_album_photo1.getText().toString() );
                }

                if (iv_album_photo2.getDrawable() != null) {
                    descList.remove( 2 );
                    descList.add( 2, et_album_photo2.getText().toString() );
                }

                if (iv_album_photo3.getDrawable() != null) {
                    descList.remove( 3 );
                    descList.add( 3, et_album_photo3.getText().toString() );
                }

                if (iv_album_photo4.getDrawable() != null) {
                    descList.remove( 4 );
                    descList.add( 4, et_album_photo4.getText().toString() );
                }

                if (iv_album_photo_auth.getDrawable() != null) {
                   /* descList.remove(0);
                    descList.add(0,et_album_photo1_auth.getText().toString());*/
                }

                getAlbumImagesData();


                //Toast.makeText(DTAddAlbum.this, "Album Added successfully.", Toast.LENGTH_SHORT).show();

                Log.d( "Touchbase", "*************** " + status );
                Log.d( "Touchbase", "*************** " + msg );
                finish();//finishing activity

            } else {
                if (tv_add.getText().equals( "Done" ))
                    Toast.makeText( DTAddAlbum.this, "Failed to add album.", Toast.LENGTH_SHORT ).show();
                else
                    Toast.makeText( DTAddAlbum.this, "Failed to update album.", Toast.LENGTH_SHORT ).show();
            }

        } catch (Exception e) {
            Log.d( "exec", "Exception :- " + e.toString() );
        }

    }

    private void getAlbumImagesData() {

        ArrayList<UploadPhotoData> uploadPhotoDataArrayList = new ArrayList<>();

        for (int i = 0; i < imageList.size(); i++) {

            if (!imageList.get( i ).equals( "" )) {
                data = new UploadPhotoData( "0", imageList.get( i ), descList.get( i ), albumId, groupId, createdBy, "0" );
                //long id = addPhotoModel.insert(data);
                uploadPhotoDataArrayList.add( data );
            }
        }


        if (uploadPhotoDataArrayList.size() == 0) {
            if (tv_title.getText().toString().contains( "Event" )) {
                Toast.makeText( DTAddAlbum.this, "District Event added successfully.", Toast.LENGTH_SHORT ).show();
                finish();
            } else {
                Toast.makeText( DTAddAlbum.this, "District Project added successfully.", Toast.LENGTH_SHORT ).show();
                finish();
            }

        } else {
            new UploadPhotoAsyncTask( uploadPhotoDataArrayList ).execute();
        }

        /*Toast.makeText(DTAddAlbum.this, "Activity Added successfully.", Toast.LENGTH_SHORT).show();
        finish();
showFileChooser
        Log.d("UploadPhotoService", "UploadPhotoService is Called");
        Intent intent = new Intent(DTAddAlbum.this, UploadPhotoService.class);
        startService(intent);*/
    }

    public class UploadPhotoAsyncTask extends AsyncTask<String, Object, Object> {

        ArrayList<UploadPhotoData> allImagesList;

        final ProgressDialog progressDialog = new ProgressDialog( DTAddAlbum.this, R.style.TBProgressBar );

        public UploadPhotoAsyncTask(ArrayList<UploadPhotoData> pList) {
            allImagesList = pList;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            progressDialog.setCancelable( false );
            progressDialog.setProgressStyle( android.R.style.Widget_ProgressBar_Small );
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(String... params) {

            String val = null;

            for (UploadPhotoData uploadPhotoData : allImagesList) {

                String url = Constant.AddUpdateAlbumPhoto;

                try {

                    url = url + "?photoId=" + uploadPhotoData.getPhotoId() + "&desc=" + URLEncoder.encode( uploadPhotoData.getDescription(), "UTF-8" ) + "&albumId=" + uploadPhotoData.getAlbumId() + "&groupId=" + uploadPhotoData.getGroupd() + "&createdBy=" + uploadPhotoData.getCreatedBy();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                val = UploadPhotoToServer( new File( uploadPhotoData.getPhotoPath() ), url );

                Log.d( "UploadPhotoService", "UploadPhotoService file path =>" + uploadPhotoData.getPhotoPath() );

                Log.e( "UploadPhotoService", "url=>" + url + "\nresult=>" + val );

            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {

            super.onPostExecute( result );

            if (!DTAddAlbum.this.isFinishing()) {
                progressDialog.dismiss();
            }

            if (tv_title.getText().toString().contains( "Event" )) {
                Toast.makeText( DTAddAlbum.this, "District Event added successfully.", Toast.LENGTH_SHORT ).show();
                finish();
            } else {
                Toast.makeText( DTAddAlbum.this, "District Project added successfully.", Toast.LENGTH_SHORT ).show();
                finish();
            }

        }
    }


    public String UploadPhotoToServer(File file_path, String url) {

        String isUploaded = "";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy( policy );

        Log.d( "galley", "Do file path satish" + file_path + " " + url );

        try {

            HttpClient client = new DefaultHttpClient();

            //use your server path of php file
            HttpPost post = new HttpPost( url );

            //   Log.d("ServerPath", "Path" + Constant.UploadImage + type);
            //  Log.d("ServerPath", "Path" + Constant.UploadImage+"MemberProfile");

            FileBody bin1 = new FileBody( file_path );
            //  Log.d("Enter", "Filebody complete " + bin1);

            org.apache.http.entity.mime.MultipartEntity reqEntity = new org.apache.http.entity.mime.MultipartEntity();
            reqEntity.addPart( "uploaded_file", bin1 );
            //reqEntity.addPart("email", new StringBody(useremail));

            post.setEntity( reqEntity );
            //  Log.d("Enter", "Image send complete");

            HttpResponse response = client.execute( post );
            HttpEntity resEntity = response.getEntity();

            //  Log.d("Enter", "Get Response");
            Log.d( "galley", "Do file path satish41111" + file_path + " " + url );
            try {

                Log.d( "galley", "Do file path satish51111" + file_path + " " + url );

                final String response_str = EntityUtils.toString( resEntity );
                JSONObject jsonObj = new JSONObject( response_str );
                JSONObject ActivityResult = jsonObj.getJSONObject( "LoadImageResult" );

                final String status = ActivityResult.getString( "status" );

                if (status.equals( "0" )) {
                    isUploaded = response_str;//ActivityResult.getString("message");
                }

            } catch (Exception ex) {
                Log.e( "Debug", "error: " + ex.getMessage(), ex );
            }

        } catch (Exception e) {
            Log.e( "Upload Exception", "" );
            e.printStackTrace();
        }

        Log.d( "TOUCHBASE", "ID IN FILE UPLOAD CALL --" + isUploaded );

        return isUploaded;
    }


    public boolean validation() {

        if (rbInClub.isChecked()) {

            if (et_gallery_title.getText().toString().trim().matches( "" ) || et_gallery_title.getText().toString().trim() == null) {
                Toast.makeText( DTAddAlbum.this, "Please enter Title", Toast.LENGTH_LONG ).show();
                return false;
            }
//
//            if (et_description.getText().toString().trim().matches( "" ) || et_description.getText().toString().trim() == null) {
//                Toast.makeText( DTAddAlbum.this, "Please enter Description.", Toast.LENGTH_LONG ).show();
//                return false;
//            }

//            if (imgFlag == 0) {
//                Toast.makeText(DTAddAlbum.this, "Please select atleast one photo.", Toast.LENGTH_LONG).show();
//                return false;
//            }

            if (et_DOP.getText().toString().trim().matches( "" ) || et_DOP.getText().toString().trim() == null) {
                Toast.makeText( DTAddAlbum.this, "Please select date.", Toast.LENGTH_LONG ).show();
                return false;
            }

            return true;

        } else if (rbPublic.isChecked()) {

            if (sp_blankselect_SubCategory.equals( "1" )) {

                Toast.makeText( DTAddAlbum.this, "Please select Category", Toast.LENGTH_LONG ).show();

                return false;


            }

            if (sp_blankselect_SubtosubCategory.equals( "1" )) {

                Toast.makeText( DTAddAlbum.this, "Please select SubCategory", Toast.LENGTH_LONG ).show();

                return false;


            }

            if (flagforcategoryothertext == 1) {

                if (et_categoryName.getText().toString().trim().matches( "" ) || et_categoryName.getText().toString().trim() == null) {

                    // Toast.makeText(DTAddAlbum.this, "Please enter Area of focus description.", Toast.LENGTH_LONG).show();
                    Toast.makeText( DTAddAlbum.this, "Please specify Area of focus.", Toast.LENGTH_LONG ).show();

                    return false;
                }

            }


            if (flagforsubcategoryothertext == 1) {

                if (et_subcategoryName.getText().toString().trim().matches( "" ) || et_subcategoryName.getText().toString().trim() == null) {

                    Toast.makeText( DTAddAlbum.this, "Please specify the category", Toast.LENGTH_LONG ).show();

                    return false;
                }


            }

            if (fundingIsSelect.equalsIgnoreCase( "1" )){

                Toast.makeText( DTAddAlbum.this, "Please select the Source of Funding.", Toast.LENGTH_LONG ).show();

                return false;


            }


            if (et_DOP.getText().toString().trim().matches( "" ) || et_DOP.getText().toString().trim() == null) {
                Toast.makeText( DTAddAlbum.this, "Please select date.", Toast.LENGTH_LONG ).show();
                return false;
            }

            if (et_COP.getText().toString().trim().matches( "" ) || et_COP.getText().toString().trim() == null) {
                Toast.makeText( DTAddAlbum.this, "Please enter cost.", Toast.LENGTH_LONG ).show();
                return false;
            }

            if (et_Beneficiary.getText().toString().trim().matches( "" ) || et_Beneficiary.getText().toString().trim() == null) {
                Toast.makeText( DTAddAlbum.this, "Please enter Direct Beneficiaries.", Toast.LENGTH_LONG ).show();
                return false;
            }
            if (et_manPower.getText().toString().trim().matches( "" ) || et_manPower.getText().toString().trim() == null) {
                Toast.makeText( DTAddAlbum.this, "Please enter Man hours.", Toast.LENGTH_LONG ).show();
                return false;
            }

            if (et_noOfRotarians.getText().toString().trim().matches( "" ) || et_noOfRotarians.getText().toString().trim() == null) {
                Toast.makeText( DTAddAlbum.this, "Please enter Rotarians Involved.", Toast.LENGTH_LONG ).show();
                return false;
            }
            if (et_gallery_title.getText().toString().trim().matches( "" ) || et_gallery_title.getText().toString().trim() == null) {
                Toast.makeText( DTAddAlbum.this, "Please enter Title", Toast.LENGTH_LONG ).show();
                return false;
            }

//            if (et_description.getText().toString().trim().matches( "" ) || et_description.getText().toString().trim() == null) {
//                Toast.makeText( DTAddAlbum.this, "Please enter Description.", Toast.LENGTH_LONG ).show();
//                return false;
//            }

//            if (imgFlag == 0) {
//                Toast.makeText(DTAddAlbum.this, "Please select atleast one photo.", Toast.LENGTH_LONG).show();
//                return false;
//            }





            if (radio_mediaprint_yes.isChecked()) {
                if (iv_album_photo_auth.getDrawable() == null) {
                    Toast.makeText( DTAddAlbum.this, "Please upload Print media photo..", Toast.LENGTH_LONG ).show();
                    return false;
                }
            }


          /*  int benificiaryvalue = Integer.parseInt(et_Beneficiary.getText().toString());
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


            return true;
        }
        return false;
    }

    private void selectImage() {

        Intent intent = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        startActivityForResult( intent, 2 );

        final CharSequence[] options;
        options = new CharSequence[]{"Choose from Gallery"};

//        if (hasimageflag.equals("0")) {
//            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
//            hasimageflag = "1";
//        } else {
//            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Remove Photo", "Cancel"};
//
//        }

        AlertDialog.Builder builder = new AlertDialog.Builder( DTAddAlbum.this );
        builder.setTitle( "Add Photo!" );
        builder.setItems( options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals( "Take Photo" )) {
                    Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
                    File f = new File( Environment.getExternalStorageDirectory(), "temp.jpg" );
                    intent.putExtra( MediaStore.EXTRA_OUTPUT, Uri.fromFile( f ) );

                    startActivityForResult( intent, 4 );
                } else if (options[item].equals( "Choose from Gallery" )) {
                    Intent intent = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                    startActivityForResult( intent, 2 );
                } else if (options[item].equals( "Cancel" )) {
                    imgFlag = 0;
                    dialog.dismiss();
                } else if (options[item].equals( "Remove Photo" )) {
                    if (galleryId.equals( "0" )) {
                        iv_event_photo.setImageResource( R.drawable.edit_image );
                        imgFlag = 0;
                    } else {
                        //remove_photo_webservices();
                    }
                }
            }
        } );
        // builder.show();
    }

    private void selectAlbumImages() {
        Intent intent = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        startActivityForResult( intent, 6 );
    }

    private void selectAlbumImagesForMediaPhoto() {
        Intent intent = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        // flag=7;

        startActivityForResult( intent, 7 );


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );


        // Member Select
        if (requestCode == 3) {


            if (resultCode == Activity.RESULT_OK) {

                listaddmemberdata = data.getParcelableArrayListExtra( "result" );
                String result = "";
                int count = 0;
                ArrayList<String> selectedmemberid = new ArrayList<>();

                selectedmemberid.clear();
                inputids = "";

                for (DirectoryData d : listaddmemberdata) {
                    if (d.isBox() == true) {
                        result = result + d.getProfileID();
                        count = count + 1;
                        selectedmemberid.add( d.getProfileID() );
                    }
                    //something here
                }
                for (int i = 0; i < selectedmemberid.size(); i++) {
                    //commaSepValueBuilder.append(n.get(i));
                    inputids = inputids + selectedmemberid.get( i );

                    if (i != selectedmemberid.size() - 1) {

                        // commaSepValueBuilder.append(", ");
                        inputids = inputids + ", ";
                    }

                }
                d_radio0.setChecked( false );
                d_radio1.setChecked( false );
                d_radio2.setChecked( true );

                d_radio0.setEnabled( true );
                d_radio1.setEnabled( true );
                d_radio2.setEnabled( false );

                galleryType = "2";
                Log.d( "Touchnase", "Arrat " + inputids );
                tv_getCount.setText( "You have added " + count + " members" );
                iv_edit.setVisibility( View.VISIBLE );
            }
        }
        // SubGroup Select
        if (requestCode == 1) {


            if (resultCode == Activity.RESULT_OK) {

                listaddsubgrp = data.getParcelableArrayListExtra( "result" );
                Log.d( "Touchnase", "@@@ " + listaddsubgrp );
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
                selectedsubgrp = data.getStringArrayListExtra( "result" );
                count = selectedsubgrp.size();

                inputids = "";

                for (int i = 0; i < selectedsubgrp.size(); i++) {
                    //commaSepValueBuilder.append(n.get(i));
                    inputids = inputids + selectedsubgrp.get( i );

                    if (i != selectedsubgrp.size() - 1) {
                        // commaSepValueBuilder.append(", ");
                        inputids = inputids + ", ";

                    }
                }

                d_radio0.setChecked( false );
                d_radio1.setChecked( true );
                d_radio2.setChecked( false );

                d_radio0.setEnabled( true );
                d_radio1.setEnabled( false );
                d_radio2.setEnabled( true );

                Log.d( "Touchnase", "Arrat " + inputids );
                galleryType = "1";
                tv_getCount.setText( "You have added " + count + " sub groups" );
                iv_edit.setVisibility( View.VISIBLE );
            }

        }


        /***************Image Capture***********/
        if (requestCode == 4) {


            if (resultCode == Activity.RESULT_OK) {

                File f = new File( Environment.getExternalStorageDirectory().toString() );
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals( "temp.jpg" )) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile( f.getAbsolutePath(),
                            bitmapOptions );
                    Bitmap bt = Bitmap.createScaledBitmap( bitmap, 400, 400, false );
                    iv_event_photo.setImageBitmap( bt );
                    iv_event_photo.setBackground( null );
                    String path = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    // f.delete();
                    OutputStream outFile = null;
                    File file = new File( path, String.valueOf( System.currentTimeMillis() ) + ".jpg" );

                    Log.d( "TOUCHBASE", "FILE PATH " + f.toString() );
                    ///-------------------------------------------------------------------
                    pd = ProgressDialog.show( DTAddAlbum.this, "", "Loading...", false );
                    final File finalF = f;
                    Thread thread = new Thread( new Runnable() {
                        public void run() {
                            uploadedimgid = Utils.doFileUpload( new File( finalF.toString() ), "gallery" ); // Upload File to server
                            imageList.remove( 0 );
                            imageList.add( 0, finalF.toString() );
                            Utils.log( "URL : " + finalF.toString() );
                            imgFlag = 1;
                            runOnUiThread( new Runnable() {
                                public void run() {
                                    if (pd.isShowing())
                                        pd.dismiss();
                                    Log.d( "TOUCHBASE", "FILE UPLOAD ID InnerThread  " + uploadedimgid );
                                    if (uploadedimgid.equals( "0" )) {
                                        Toast.makeText( DTAddAlbum.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT ).show();
                                        //iv_event_photo.setImageResource(R.drawable.edit_image);
                                        iv_event_photo.setBackground( getResources().getDrawable( R.drawable.asset ) );
                                        imgFlag = 0;
                                        imageList.add( 0, "" );
                                    }
                                    //Toast.makeText(Register.this, "Verify your account through the registered email id", Toast.LENGTH_LONG).show();
                                }
                            } );
                            ll_photos.setVisibility( View.VISIBLE );
                            Utils.log( imageList.toString() );
                        }
                    } );
                    thread.start();
                    ///-------------------------------------------------------------------
                    //uploadedimgid = Utils.doFileUpload(new File(f.toString()), "announcement"); // Upload File to server


                    try {
                        outFile = new FileOutputStream( file );
                        bitmap.compress( Bitmap.CompressFormat.JPEG, 85, outFile );
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
                Cursor c = getContentResolver().query( selectedImage, filePath, null, null, null );
                c.moveToFirst();
                int columnIndex = c.getColumnIndex( filePath[0] );
                final String picturePath = c.getString( columnIndex );
                c.close();
                ImageCompression imageCompression = new ImageCompression();
                final String finalImagePath = imageCompression.compressImage( picturePath, getApplicationContext() );

                final Bitmap thumbnail = (BitmapFactory.decodeFile( finalImagePath ));
                Log.d( "TOUCHBASE", "FILE PATH " + finalImagePath.toString() );
                imageList.remove( 0 );
                imageList.add( 0, finalImagePath.toString() );
                imgFlag = 1;
                ///-------------------------------------------------------------------
                pd = ProgressDialog.show( DTAddAlbum.this, "", "Loading...", false );

                Thread thread = new Thread( new Runnable() {

                    public void run() {

                        uploadedimgid = Utils.doFileUpload( new File( finalImagePath ), "gallery" ); // Upload File to server

                        runOnUiThread( new Runnable() {

                            public void run() {

                                if (pd.isShowing())
                                    pd.dismiss();
                                if (uploadedimgid.equals( "0" )) {
                                    Toast.makeText( DTAddAlbum.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT ).show();
                                    //iv_event_photo.setImageResource(R.drawable.edit_image);
                                    imageList.remove( 0 );
                                    imageList.add( 0, "" );
                                    iv_event_photo.setBackground( getResources().getDrawable( R.drawable.asset ) );
                                    imgFlag = 0;
                                } else {
                                    ll_photos.setVisibility( View.VISIBLE );
                                    iv_event_photo.setImageBitmap( thumbnail );
                                    iv_event_photo.setBackground( null );
                                    close1.setVisibility( View.VISIBLE );
                                }
                                //Toast.makeText(Register.this, "Verify your account through the registered email id", Toast.LENGTH_LONG).show();
                            }
                        } );

                        Utils.log( "Images " + imageList.toString() );
                    }
                } );
                thread.start();
            }
        } else if (requestCode == 6) {

            if (resultCode == Activity.RESULT_OK) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query( selectedImage, filePath, null, null, null );
                c.moveToFirst();


                int columnIndex = c.getColumnIndex( filePath[0] );
                final String picturePath = c.getString( columnIndex );
                c.close();

                ImageCompression imageCompression = new ImageCompression();
                final String finalImagePath = imageCompression.compressImage( picturePath, getApplicationContext() );

                final Bitmap thumbnail = (BitmapFactory.decodeFile( finalImagePath ));
                Log.d( "TOUCHBASE", "FILE PATH " + finalImagePath.toString() );
                //imageList.add(picturePath.toString());

                if (flag == 1) {
                    imageList.remove( 0 );
                    imageList.add( 0, finalImagePath );
                    iv_event_photo.setImageBitmap( thumbnail );
                    iv_event_photo.setBackground( null );
                    ll_photos.setVisibility( View.VISIBLE );
                    close1.setVisibility( View.VISIBLE );
                } else if (flag == 2) {
                    imageList.remove( 1 );
                    imageList.add( 1, finalImagePath.toString() );
                    iv_album_photo.setImageBitmap( thumbnail );
                    iv_album_photo.setBackground( null );
                    close2.setVisibility( View.VISIBLE );
                } else if (flag == 3) {
                    imageList.remove( 2 );
                    imageList.add( 2, finalImagePath.toString() );
                    iv_album_photo2.setImageBitmap( thumbnail );
                    iv_album_photo2.setBackground( null );
                    close3.setVisibility( View.VISIBLE );
                } else if (flag == 4) {
                    imageList.remove( 3 );
                    imageList.add( 3, finalImagePath.toString() );
                    iv_album_photo3.setImageBitmap( thumbnail );
                    iv_album_photo3.setBackground( null );
                    close4.setVisibility( View.VISIBLE );
                } else if (flag == 5) {
                    imageList.remove( 4 );
                    imageList.add( 4, finalImagePath.toString() );
                    iv_album_photo4.setImageBitmap( thumbnail );
                    iv_album_photo4.setBackground( null );
                    close5.setVisibility( View.VISIBLE );
                } else if (flag == 6) {
                    /*imageList.remove(0);
                    imageList.add(0, finalImagePath);
                    iv_album_photo_auth.setImageBitmap(thumbnail);
                    iv_album_photo_auth.setBackground(null);
                    close6.setVisibility(View.VISIBLE);*/
                }
                Utils.log( "Images " + imageList.toString() );
            }
        } else if (requestCode == 7) {
            //This is code Added By Gaurav
            if (resultCode == Activity.RESULT_OK) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query( selectedImage, filePath, null, null, null );
                c.moveToFirst();
                int columnIndex = c.getColumnIndex( filePath[0] );
                final String picturePath = c.getString( columnIndex );
                c.close();
                ImageCompression imageCompression = new ImageCompression();
                final String finalImagePath = imageCompression.compressImage( picturePath, getApplicationContext() );

                final Bitmap thumbnail = (BitmapFactory.decodeFile( finalImagePath ));
                Log.d( "TOUCHBASE", "FILE PATH " + finalImagePath.toString() );
                // imageList.remove(0);
                // imageList.add(0,finalImagePath.toString());
                //imgFlag = 1;
                ///-------------------------------------------------------------------
                pd = ProgressDialog.show( DTAddAlbum.this, "", "Loading...", false );

                Thread thread = new Thread( new Runnable() {

                    public void run() {

                        MediaphotoID = Utils.doFileUpload( new File( finalImagePath ), "gallery" ); // Upload File to server
                        // MediaphotoID=uploadedimgid;
                        runOnUiThread( new Runnable() {

                            public void run() {

                                if (pd.isShowing())
                                    pd.dismiss();
                                if (MediaphotoID.equals( "0" )) {
                                    Toast.makeText( DTAddAlbum.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT ).show();
                                    //iv_event_photo.setImageResource(R.drawable.edit_image);
                                    // imageList.remove(0);
                                    // imageList.add(0,"");
                                    iv_album_photo_auth.setBackground( getResources().getDrawable( R.drawable.asset ) );
                                    imgFlag = 0;
                                    Ismedia = "0";
                                } else {
                                    ll_photos.setVisibility( View.VISIBLE );
                                    iv_album_photo_auth.setImageBitmap( thumbnail );
                                    iv_album_photo_auth.setBackground( null );
                                    close6.setVisibility( View.VISIBLE );
                                    Ismedia = "1";
                                }
                                //Toast.makeText(Register.this, "Verify your account through the registered email id", Toast.LENGTH_LONG).show();
                            }
                        } );

                        // Utils.log("Images " + imageList.toString());
                    }
                } );
                thread.start();
            }
        }
    }

    private void getCategoryList() {

        try {
            progressDialog = new ProgressDialog( DTAddAlbum.this, R.style.TBProgressBar );
            progressDialog.setCancelable( false );
            progressDialog.setProgressStyle( android.R.style.Widget_ProgressBar_Small );
            progressDialog.show();

            JSONObject requestData = new JSONObject();
            requestData.put( "DistrictID", "0" );

            Log.d( "Response", "PARAMETERS " + Constant.GetShowcaseDetails + " :- " + requestData.toString() );
            JsonObjectRequest request = new JsonObjectRequest( Request.Method.POST, Constant.GetShowcaseDetails, requestData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    JSONObject result;
                    setAllShowcaseDetails( response );
                    Utils.log( response.toString() );
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Utils.log( "VollyError:- " + error.toString() );
                    //showErrorDialog();
                    //Utils.showMsg(context, "Something went wrong");
                    //Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_LONG).show();
                }
            } );

            request.setRetryPolicy(
                    new DefaultRetryPolicy( 120000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ) );

            AppController.getInstance().addToRequestQueue( context, request );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setAllShowcaseDetails(JSONObject response) {

        try {

            JSONObject ShowcaseDetails = response.getJSONObject( "ShowcaseDetails" );
            String status = ShowcaseDetails.getString( "status" );

            if (status.equalsIgnoreCase( "0" )) {

                JSONObject result = ShowcaseDetails.getJSONObject( "Result" );
                JSONArray Categories = result.getJSONArray( "Categories" );
                JSONArray subCategories = result.getJSONArray( "subcat" );
                JSONArray subtosubCategories = result.getJSONArray( "subtosubcat" );
                JSONArray funding = result.getJSONArray( "Fundinglist" );


                if (Categories.length() > 0) {

                    for (int i = 0; i < Categories.length(); i++) {
                        JSONObject categoryObj = Categories.getJSONObject( i );
                        AlbumData data = new AlbumData();
                        data.setDistrict_id( categoryObj.getString( "ID" ) );
                        data.setDistrict_Name( categoryObj.getString( "Name" ) );
                        categoryList.add( i, data );
                    }
                }
                adapter = new SpinnerAdapter( context, categoryList );
                sp_category.setAdapter( adapter );

                //Add this for subcategory data for Gaurav

                //first load data
                newdataforsubcategory = new AlbumData();
                newdataforsubcategory.setPk_subcategoryId( "0" );
                newdataforsubcategory.setFk_CategoryID( "0" );
                newdataforsubcategory.setSubcategoryName( "Select" );
                subcategoryList.add( 0, newdataforsubcategory );

                for (int i = 0; i < subCategories.length(); i++) {
                    JSONObject subcategoryObj = subCategories.getJSONObject( i );
                    AlbumData data = new AlbumData();
                    data.setPk_subcategoryId( subcategoryObj.getString( "pk_subcategoryId" ) );
                    data.setFk_CategoryID( subcategoryObj.getString( "fk_CategoryID" ) );
                    data.setSubcategoryName( subcategoryObj.getString( "SubcategoryName" ) );
                    subcategoryList.add( i, data );
                }

                //Add this for subtosubcategory data for Gaurav
                for (int i = 0; i < subtosubCategories.length(); i++) {
                    JSONObject subtosubcategoryObj = subtosubCategories.getJSONObject( i );
                    AlbumData data = new AlbumData();
                    data.setPk_subtosubcategoryId( subtosubcategoryObj.getString( "pk_subtosubcategoryId" ) );
                    data.setFk_subcategoryid( subtosubcategoryObj.getString( "fk_subcategoryid" ) );
                    data.setFk_categoryID( subtosubcategoryObj.getString( "fk_categoryID" ) );
                    data.setSubtosubcategoryname( subtosubcategoryObj.getString( "subtosubcategoryname" ) );
                    subtosubcategoryList.add( i, data );
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


                progressDialog.dismiss();
            } else {
                progressDialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

    }


    //this method is added By Gaurav for MaxBenificiary

    public void show_Popup_MaxBenificiary() {
        final Dialog dialog = new Dialog( DTAddAlbum.this, android.R.style.Theme_Translucent );
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        dialog.setContentView( R.layout.popup_for_maxbenificiary_new );
        TextView confirm = (TextView) dialog.findViewById( R.id.tv_ok );
        TextView cancel = (TextView) dialog.findViewById( R.id.tv_cancel );


        cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                beneficiary = et_Beneficiary.getText().toString();
                temp_beneficiary = "0";
                temp_beneficiary_flag = "0";

                dialog.dismiss();
            }
        } );

        confirm.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show latter
                beneficiary = maxBeneficiaries.toString();
                temp_beneficiary = et_Beneficiary.getText().toString();
                temp_beneficiary_flag = "1";
                webservices();
                dialog.dismiss();

            }
        } );
        dialog.show();

    }


}
