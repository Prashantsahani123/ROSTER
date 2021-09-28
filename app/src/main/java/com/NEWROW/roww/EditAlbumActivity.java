package com.NEWROW.row;

import android.annotation.SuppressLint;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import androidx.appcompat.app.AlertDialog;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.MetricAffectingSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.NEWROW.row.Gallery.MemberCount;
import static com.NEWROW.row.Gallery.maxBeneficiaries;


/**
 * Created by user on 18-11-2016.
 */
public class EditAlbumActivity extends Activity {
    TextView tv_title, tv_add, tv_getCount, tv_cancel, tv_clubServiceInfo, txt_fileName, txt_fileName_mom;
    String albumId, albumName, albumDescription, header;
    String albumImage = "";
    int tt = 0;

    ImageView iv_backbutton, iv_image, iv_edit, iv_album_photo, iv_album_photo_auth, iv_album_photo2, iv_album_photo3, iv_album_photo4, iv_delete_agenda, iv_delete_mom;
    ImageView close1, close2, close3, close4, close5, close6;
    EditText edt_title, edt_description;
    EditText et_categoryName, et_subcategoryName, et_subtosubcategoryName, et_noOfRotarians, et_COP, et_Beneficiary, et_manPower, et_attendacne, et_attendance_per;
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission( this );
    ProgressDialog pd;
    TextView et_DOP;
    Spinner sp_category, sp_subcategory, sp_subtosubcategory, et_currency, sp_timeType, sp_meeting;
    private String uploadedimgid = "0";
    String galleryType = "0";
    RadioButton d_radio0, d_radio1, d_radio2;
    ArrayList<DirectoryData> listaddmemberdata = new ArrayList<>();
    private String edit_gallery_selectedids = "0";
    ArrayList<SubGoupData> listaddsubgrp = new ArrayList<>();
    String inputids = "";
    ArrayList<String> selectedsubgrp;
    private String edit_album_selectedids = "0";
    private RadioButton rbInClub, rbPublic;
    private LinearLayout llShareWrapper, ll_subcategory, ll_subtosubcategory;
    ArrayList<AlbumData> categoryList = new ArrayList<>();
    ArrayList<AlbumData> subcategoryList = new ArrayList<>();
    ArrayList<AlbumData> subtosubcategoryList = new ArrayList<>();
    ArrayList<AlbumData> subcategoryListupdate = new ArrayList<>();
    ArrayList<AlbumData> subtosubcategoryListupdate = new ArrayList<>();

    ArrayList<String> currencyList = new ArrayList<>();
    ArrayList<String> timeType = new ArrayList<>();
    String categoryID, subcategoryID, subtosubcategoryID, subcategoryIDupdate, subtosubcategoryIDupdate, projectDate;
    SpinnerAdapter adapter;
    SpinnerAdapter_subcategoryupdate adapter_subcategory_update;
    SpinnerAdapter_subtosubcategoryupdate adapter_subtosubcategory_update;
    private Context context;



    String TtlOfNew ;


    TextView tv_TimeCountType;
    LinearLayout ll_rotaryServicecontent, ll_category, ll_photos, ll_clubService, ll_attach, ll_attach_mom, ll_attach_agenda, ll_agenda_file, ll_mom_file;
    ScrollView scrollview;

    SimpleDateFormat format = new SimpleDateFormat( "dd MMM yyyy" );
    SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
    int imgFlag = 0;
    EditText et_coverPhoto, et_album_photo3, et_album_photo2, et_album_photo1, et_album_photo1_auth, et_album_photo4;
    ArrayList<String> imageList = new ArrayList<>();
    ArrayList<String> descList = new ArrayList<>();
    ArrayList<String> photoIdList = new ArrayList<>();
    int flag = 1, deletePhotoflag = 0, flagforcategoryothertext = 0, flagforsubcategoryothertext = 0;
    UploadPhotoData data;
    UploadedPhotoModel addPhotoModel;
    String groupId = "";
    String createdBy = "";
    String isdelete = "false";
    String photoId1 = "", photoId2 = "", photoId3 = "", photoId4 = "", photoId5 = "", photoId6 = "";

    private static final DecimalFormat decimalformat = new DecimalFormat( "#.##" );
    private String agenda_file_id = "0", mom_file_id = "0";

    private final int AGENDA = 10, MOM = 11;
    String selectedImagePath;

    int isFirstLoad = 0;
    boolean loadflag = false;

    //this is added By Gaurav
    String serviceproject = "1", shareType = "", Ismedia = "", beneficiary = "", temp_beneficiary = "", temp_beneficiary_flag = "";
    LinearLayout attendance_layout, meeting_layout, rotarctors_layout, media_layout, mediaradiolayot, authlayout;
    EditText et_rotractors;
    RadioButton radio_mediaprint_yes, radio_mediaprint_No;
    TextView mediatitle;
    //this is added By Gaurav for spinner select
    AlbumData newdataforsubcategory, newdataforsubtosubcategory, updateDataForSubcategory, updateDataForSubtosubcategory;

    String subcategoryselectedtext = "", subtosubcategoryselectedtext = "";

    //This is added By Gaurav for API parameters
    String MaxNumber, Approvedbeneficiary, ApprovedFlag = "", subtosubcategoryIdtest = "0", sp_blankselect_SubCategory = "0", sp_blankselect_SubtosubCategory = "0";
    //this is for Media Photo
    String MediaphotoID = "0", mediaPhotoPath = "";
    String updatedtitle = "";

    //This is for Images sent on Mail during benificiary approve mail
    private String galleryid_mail, ClubName_mail, secretaryEmailIds_mail, districtNumber_mail, TempBeneficiary_mail, president_mail, secretary_mail, presidentEmailIds_mail;
    ProgressDialog progressDialog;


    //Added By Gaurav for funding option on 9th Oct 2020
    LinearLayout ll_funding;
    private Spinner sp_funding;
    ArrayList<FundingData> fundingList = new ArrayList<>();

    private String fundingID;
    private EditText et_fundingName;
    SpinnerFundingAdapter fundingadapter;
    int flagforfundingothertext;
    private String fundingIsSelect;

    ///--- add by prashant sahani

    String ontime = "0";
    String ontimeyes = "0" ;
    String ong_clubrole = "0";
    String crt_newyes = "0";
    String ong_neww = "0" ;



    EditText et_newong;

    RadioGroup radioGroup;
    RadioGroup radioGroup1;
    RadioGroup radioGroup2;
    RadioGroup radioGroupong;
    RadioGroup radioGroupong_new;
    RadioGroup  radioGroupong2;

    RadioButton radioMale,radioFemale,radioMale1,radioFemale1,radioMaleong2,radioFemaleong2,radioMale2,radioFemale2, radioMaleong, radioFemaleong;
    RadioButton radioMaleongnew,radioFemaleongnew;



    LinearLayout ont_ong_selct;
    LinearLayout onetime ;
    LinearLayout onetime1 ;
    LinearLayout ongoing;
    LinearLayout ong_new;
    LinearLayout ong_jnt;
    LinearLayout ong_ntlt;
    LinearLayout selectp;

    TextView cohost, cohost2;

    public ArrayList<String> proj ;
    private Spinner ong_p;

    EditText et_COPf;



    //et_newong  //et_COPf
//    String ontime = "0";
//    String ontimeyes = "0" ;
//    String ong_clubrole = "0";
//    String crt_newyes = "0";
//    String ong_neww = "0" ;

    public  static HashMap<String, String> D_id ;

    public  static HashMap<String, String> D_id1 ;
    View v_1,v2;


    //---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.layout_edit_album );
        context = this;


        tt = 0;


        v_1 = findViewById(R.id.v_1);
        v2 = findViewById(R.id.v2);

        initradio_b();


        et_COPf = findViewById(R.id.et_newong );

        ong_p = findViewById(R.id.ong_p);

        iv_backbutton = (ImageView) findViewById( R.id.iv_backbutton );
        tv_title = (TextView) findViewById( R.id.tv_title );
        //  tv_title.setText("Edit Activity");
        edt_title = (EditText) findViewById( R.id.et_galleryTitle );

        edt_description = (EditText) findViewById( R.id.et_evetDesc );

        addPhotoModel = new UploadedPhotoModel( this );
        groupId = PreferenceManager.getPreference( this, PreferenceManager.GROUP_ID );
        createdBy = PreferenceManager.getPreference( this, PreferenceManager.GRP_PROFILE_ID );

        iv_image = (ImageView) findViewById( R.id.iv_event_photo );
        tv_add = (TextView) findViewById( R.id.tv_done );
        iv_edit = (ImageView) findViewById( R.id.iv_edit );
        tv_getCount = (TextView) findViewById( R.id.getCount );
        tv_cancel = (TextView) findViewById( R.id.tv_cancel );
        tv_clubServiceInfo = (TextView) findViewById( R.id.tv_clubServiceInfo );
        ll_rotaryServicecontent = (LinearLayout) findViewById( R.id.ll_rotaryServicecontent );
        ll_category = (LinearLayout) findViewById( R.id.ll_category );
        ll_subcategory = (LinearLayout) findViewById( R.id.ll_subcategory );
        ll_subtosubcategory = (LinearLayout) findViewById( R.id.ll_subtosubcategory );
        ll_photos = (LinearLayout) findViewById( R.id.ll_photos );
        ll_clubService = (LinearLayout) findViewById( R.id.ll_clubService );
        ll_agenda_file = findViewById( R.id.ll_agenda_file );
        ll_mom_file = findViewById( R.id.ll_mom_file );

        txt_fileName = (TextView) findViewById( R.id.txt_fileName );
        txt_fileName_mom = (TextView) findViewById( R.id.txt_fileName_mom );
        iv_delete_agenda = findViewById( R.id.iv_delete_agenda );
        iv_delete_mom = findViewById( R.id.iv_delete_mom );


        iv_album_photo = (ImageView) findViewById( R.id.iv_album_photo );
        iv_album_photo_auth = (ImageView) findViewById( R.id.iv_album_photo_auth );
        iv_album_photo2 = (ImageView) findViewById( R.id.iv_album_photo2 );
        iv_album_photo3 = (ImageView) findViewById( R.id.iv_album_photo3 );
        iv_album_photo4 = (ImageView) findViewById( R.id.iv_album_photo4 );

        et_coverPhoto = (EditText) findViewById( R.id.et_coverPhoto );
        String img_des_val = et_coverPhoto.getText().toString();
        System.out.println( "new_value1" + img_des_val );
        et_album_photo1_auth = (EditText) findViewById( R.id.et_album_photo1_auth );
        et_album_photo1 = (EditText) findViewById( R.id.et_album_photo1 );
        et_album_photo2 = (EditText) findViewById( R.id.et_album_photo2 );
        et_album_photo3 = (EditText) findViewById( R.id.et_album_photo3 );
        et_album_photo4 = (EditText) findViewById( R.id.et_album_photo4 );
        et_attendacne = (EditText) findViewById( R.id.et_attendance );
        et_attendance_per = (EditText) findViewById( R.id.et_attendance_per );

        close1 = (ImageView) findViewById( R.id.close1 );
        close2 = (ImageView) findViewById( R.id.close2 );
        close3 = (ImageView) findViewById( R.id.close3 );
        close4 = (ImageView) findViewById( R.id.close4 );
        close5 = (ImageView) findViewById( R.id.close5 );
        close6 = (ImageView) findViewById( R.id.close2_auth );


        d_radio0 = (RadioButton) findViewById( R.id.d_radio0 );
        d_radio1 = (RadioButton) findViewById( R.id.d_radio1 );
        d_radio2 = (RadioButton) findViewById( R.id.d_radio2 );
        // d_radio0.setChecked(true);
        rbInClub = (RadioButton) findViewById( R.id.rbInClub );
        rbPublic = (RadioButton) findViewById( R.id.rbPublic );


        //this code is added By Gaurav
        attendance_layout = findViewById( R.id.attendance_layout );
        meeting_layout = findViewById( R.id.meeting_layout );
        rotarctors_layout = findViewById( R.id.rotarctors_layout );
        media_layout = findViewById( R.id.media_layout );
        mediatitle = findViewById( R.id.mediatitle );
        mediaradiolayot = findViewById( R.id.mediaradiolayot );
        authlayout = findViewById( R.id.authlayout );
        et_rotractors = findViewById( R.id.et_rotractors );
        radio_mediaprint_yes = (RadioButton) findViewById( R.id.radio_mediaprint_yes );
        radio_mediaprint_No = (RadioButton) findViewById( R.id.radio_mediaprint_No );
        //finding Layout
        ll_funding = (LinearLayout) findViewById( R.id.ll_funding );
        sp_funding = (Spinner) findViewById( R.id.sp_funding );
        et_fundingName = (EditText) findViewById( R.id.et_fundingName );

      //  Toast.makeText(getApplicationContext(),"fkfhlkfsdgkf",Toast.LENGTH_LONG).show();
        Intent i1 = getIntent();
        albumId = i1.getStringExtra( "albumId" );
        albumName = i1.getStringExtra( "albumname" );
        albumDescription = i1.getStringExtra( "description" );
        albumImage = i1.getStringExtra( "albumImage" );
        header = i1.getStringExtra( "header" );

        tv_title.setText( "Edit " + header );
        if(header.contains("Meeting"))
        {
            ont_ong_selct.setVisibility(View.GONE);
        }
        else
        {
            if( tv_title.getText().toString().contains("Project"))
            {
                ont_ong_selct.setVisibility(View.VISIBLE);
            }

        }

        //this is added by Gaurav
        //  updatedtitle = method(albumName);

        serviceproject = i1.getExtras().getString( "serviceproject", "0" );

        //clubMeetings Data Display
        if (header.equalsIgnoreCase( "Club Meeting" )) {
            //clubMeetings Data Display

            rbInClub.setChecked( true );
            rbPublic.setChecked( false );

            attendance_layout.setVisibility( View.VISIBLE );
            meeting_layout.setVisibility( View.VISIBLE );
            rotarctors_layout.setVisibility( View.GONE );
            et_rotractors.setVisibility( View.GONE );
            media_layout.setVisibility( View.GONE );
            //funding visibility
            ll_funding.setVisibility( View.GONE );

        } else {
            //service Project Data display

            rbPublic.setChecked( true );
            rbInClub.setChecked( false );
            //Rotary service of data should de displayed

            attendance_layout.setVisibility( View.GONE );
            meeting_layout.setVisibility( View.GONE );
            rotarctors_layout.setVisibility( View.VISIBLE );
            et_rotractors.setVisibility( View.VISIBLE );
            media_layout.setVisibility( View.VISIBLE );
            //funding visibility
            ll_funding.setVisibility( View.VISIBLE );


        }


        if (rbInClub.isChecked()) {
            ll_clubService.setVisibility( View.VISIBLE );
            ll_rotaryServicecontent.setVisibility( View.GONE );
            ll_category.setVisibility( View.GONE );
            // tv_clubServiceInfo.setVisibility(View.VISIBLE);
            tv_clubServiceInfo.setText( getString( R.string.clubServiceText_new ) );
        }
        rbInClub.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ll_clubService.setVisibility( View.GONE );
                ll_rotaryServicecontent.setVisibility( View.VISIBLE );
                ll_category.setVisibility( View.VISIBLE );
                //   tv_clubServiceInfo.setVisibility(View.GONE);
                if (isChecked) {
                    tv_clubServiceInfo.setText( getString( R.string.clubServiceText_new ) );
                }

            }
        } );

        if (rbPublic.isChecked()) {
            tv_clubServiceInfo.setText( getString( R.string.rotaryServiceText_new ) );
        }
        rbPublic.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ll_clubService.setVisibility( View.VISIBLE );
                ll_rotaryServicecontent.setVisibility( View.GONE );
                ll_category.setVisibility( View.GONE );
                //  tv_clubServiceInfo.setVisibility(View.VISIBLE);
                if (isChecked) {
                    tv_clubServiceInfo.setText( getString( R.string.rotaryServiceText_new ) );
                }

                ll_attach.setVisibility( View.GONE );

                mom_file_id = "0";
                txt_fileName_mom.setText( "" );
                ll_mom_file.setVisibility( View.GONE );
                ll_attach_mom.setVisibility( View.VISIBLE );

                agenda_file_id = "0";
                txt_fileName.setText( "" );
                ll_agenda_file.setVisibility( View.GONE );
                Utils.log( "first" );
                ll_attach_agenda.setVisibility( View.VISIBLE );

            }
        } );

        scrollview = (ScrollView) findViewById( R.id.scrollview );

        for (int i = 0; i < 5; i++) {
            imageList.add( "" );
            photoIdList.add( "0" );
            descList.add( "" );
        }


        llShareWrapper = (LinearLayout) findViewById( R.id.llShareWrapper );
        ll_clubService = (LinearLayout) findViewById( R.id.ll_clubService );
        ll_attach_agenda = (LinearLayout) findViewById( R.id.ll_attach_agenda );
        ll_attach_mom = (LinearLayout) findViewById( R.id.ll_attach_mom );
        ll_attach = (LinearLayout) findViewById( R.id.ll_attach );

        edt_title.setText( header );
        edt_description.setText( albumDescription );
        et_categoryName = (EditText) findViewById( R.id.et_categoryName );
        et_subcategoryName = (EditText) findViewById( R.id.et_subcategoryName );
        et_subtosubcategoryName = (EditText) findViewById( R.id.et_subtosubcategoryName );
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

        sp_meeting = (Spinner) findViewById( R.id.sp_meet_type );


        sp_meeting.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {


                Utils.log( " " + isFirstLoad );

                if (isFirstLoad > 1) {
                    if (pos == 0 ||pos==3) {
                        ll_attach.setVisibility( View.GONE );
                    } else {
                        ll_attach.setVisibility( View.VISIBLE );
                        String tlt = tv_title.getText().toString();
                        if(tlt == null)
                        {
                            tlt = "";
                        }
                        Log.d("tittleofedit",tlt);

                        if (tlt.equalsIgnoreCase("Edit Service Project")) {
                            ll_attach.setVisibility(View.GONE);
                            //Toast.makeText(getApplicationContext(),"jjj"+tlt,Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                          //  ll_attach.setVisibility(View.GONE);
                           // Toast.makeText(getApplicationContext(),"not"+tlt,Toast.LENGTH_LONG).show();
                        }




                        mom_file_id = "0";
                        txt_fileName_mom.setText( "" );
                        ll_mom_file.setVisibility( View.GONE );
                        ll_attach_mom.setVisibility( View.VISIBLE );

                        agenda_file_id = "0";
                        txt_fileName.setText( "" );
                        ll_agenda_file.setVisibility( View.GONE );
                        Utils.log( "Second" );
                        ll_attach_agenda.setVisibility( View.VISIBLE );

                    }
                }

                isFirstLoad++;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        } );

        sp_category = (Spinner) findViewById( R.id.sp_category );
        sp_subcategory = (Spinner) findViewById( R.id.sp_subcategory );
        sp_subtosubcategory = (Spinner) findViewById( R.id.sp_subtosubcategory );

        sp_category.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AlbumData data = categoryList.get( position );
                AlbumData subdata = subcategoryList.get( position );
                AlbumData subtosubdata = subtosubcategoryList.get( position );


                categoryID = data.getDistrict_id();
                //Called Filter Method for SubCategory
                filterData_SubCategory( categoryID );

                //this code end here
                //close
                if (data.getDistrict_Name().equalsIgnoreCase( "others" )) {
                    et_categoryName.setVisibility( View.VISIBLE );
                    ll_subcategory.setVisibility( View.GONE );
                    ll_subtosubcategory.setVisibility( View.GONE );
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


        //Added By Gaurav two spinner

        sp_subcategory.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                AlbumData data = subcategoryListupdate.get( position );
                subcategoryID = data.getPk_subcategoryIdupdate();
                AlbumData subtosubdata = subtosubcategoryList.get( position );
                subtosubcategoryID = subtosubdata.getPk_subtosubcategoryId();

                filterData_SubtosubCategory( subcategoryID );

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
                //   subtosubcategoryID = data.getFk_subcategoryidupdate();
                subtosubcategoryID = data.getPk_subtosubcategoryIdupdate();

                //this condition for blank select validation
                if (subtosubcategoryListupdate.get( position ).getSubtosubcategorynameupdate().equalsIgnoreCase( "Select" )) {
                    sp_blankselect_SubtosubCategory = "1";
                } else {
                    sp_blankselect_SubtosubCategory = "0";

                }

                //others option not available in subtosubcategory
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

        //Spinner Funding added by Gaurav

        sp_funding.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FundingData data = fundingList.get( position );
                //   AlbumData subdata = subcategoryList.get( position );
                // AlbumData subtosubdata = subtosubcategoryList.get( position );


                fundingID = data.getPk_Fund_ID();

                if (data.getFund_Name().equalsIgnoreCase( "Select" ) || data.getPk_Fund_ID().equalsIgnoreCase( "0" )){
                    fundingIsSelect="1";
                }else{
                    fundingIsSelect="0";
                }
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


        et_currency = (Spinner) findViewById( R.id.et_currency );
        currencyList.add( "\u20B9" );
        currencyList.add( "$" );
        ArrayAdapter adapter = new ArrayAdapter( EditAlbumActivity.this, android.R.layout.simple_spinner_dropdown_item, currencyList );
        et_currency.setAdapter( adapter );

        sp_timeType = (Spinner) findViewById( R.id.sp_timeType );
        timeType.add( "Hours" );
        timeType.add( "Days" );
        timeType.add( "Months" );
        timeType.add( "Years" );
        ArrayAdapter adapter1 = new ArrayAdapter( EditAlbumActivity.this, android.R.layout.simple_spinner_dropdown_item, timeType );
        sp_timeType.setAdapter( adapter1 );


        init();
        // loadFromServer();

        getCategoryList();
        //   loadFromServer();
        getAlbumPhotos();
        validateDirectBeneficiaries();


        //   getAlbumPhotos();


        //this code added By Gaurav


        et_attendacne.addTextChangedListener( new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    String attendance = et_attendacne.getText().toString();
                    float percentage = getAttendancePercentage( attendance );
                    int twodigitpercentage = (int) Math.round( percentage );

                    String out_percentage = String.valueOf( twodigitpercentage );

                    et_attendance_per.setText( out_percentage );

                } else {
                    et_attendance_per.setText( "" );

                }
            }
        } );


        //add by prashant sahani-----------------------------------------------------------------------------
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        radioGroup1 = (RadioGroup)findViewById(R.id.radioGroup1);
        radioGroup2 = (RadioGroup)findViewById(R.id.radioGroup2);
        radioGroupong = (RadioGroup)findViewById(R.id.radioGroupong);
        radioGroupong2 = (RadioGroup)findViewById(R.id.radioGroupong2);
        radioGroupong_new  = (RadioGroup)findViewById(R.id.radioGroupongnew);

       // popup1();
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {

                    //  Toast.makeText(AddAlbum.this, rb.getText(), Toast.LENGTH_SHORT).show();


                    String text = (String) rb.getText();
                    text = text.replaceAll("\\s", "");

//text = "r";
                    if(text.equals("OneTimeProject"))// One Time Project
                    {
                        ong_new.setVisibility(View.GONE);


                        ontime = "1";


                        onetime.setVisibility(View.VISIBLE);
                        cohost.setVisibility(View.GONE);
                        radioGroup1.clearCheck();
                        ongoing.setVisibility(View.GONE);
                        onetime1.setVisibility(View.GONE);
                        ong_jnt.setVisibility(View.GONE);
                        ong_new.setVisibility(View.GONE);


                        ong_ntlt.setVisibility(View.GONE);
                        selectp.setVisibility(View.GONE);

                    }
                    else if(text.equalsIgnoreCase("Ongoing/RepeatProject"))
                    {
                        // radioGroup1.clearCheck();



                        ontime = "2";

                      //  popup2();


                        onetime.setVisibility(View.GONE);
                        onetime1.setVisibility(View.GONE);
                        cohost.setVisibility(View.GONE);


                        ongoing.setVisibility(View.VISIBLE);
                        radioGroupong.clearCheck();
                        ong_new.setVisibility(View.GONE);
                        ong_jnt.setVisibility(View.GONE);


                        ong_ntlt.setVisibility(View.GONE);
                        selectp.setVisibility(View.GONE);

                    }

                }

            }
        });
        radioGroup1.clearCheck();
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {

                    //  Toast.makeText(AddAlbum.this, rb.getText(), Toast.LENGTH_SHORT).show();

                    radioGroup2.clearCheck();
                    String text = (String) rb.getText();
                    text = text.replaceAll("\\s", "");

//text = "r";
                    if(text.equals("Yes"))// One Time Project
                    {

                        crt_newyes = "1";
                        onetime1.setVisibility(View.VISIBLE);
                        cohost.setVisibility(View.GONE);
                    }
                    else if(text.equalsIgnoreCase("No"))
                    {
                        crt_newyes = "2";

                        onetime1.setVisibility(View.GONE);
                        cohost.setVisibility(View.GONE);
                    }


                }

            }
        });
        radioGroup2.clearCheck();
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {

                    //  Toast.makeText(AddAlbum.this, rb.getText(), Toast.LENGTH_SHORT).show();

                    String text = (String) rb.getText();
                    text = text.replaceAll("\\s", "");

//text = "r";
                    if(text.equals("LeadClub"))// One Time Project
                    {

                        ong_clubrole = "1" ;
                        //onetime1.setVisibility(View.VISIBLE);
                        cohost.setVisibility(View.GONE);
                    }
                    else if(text.equalsIgnoreCase("Co-Host"))
                    {
                        //  onetime1.setVisibility(View.GONE);
                        ong_clubrole = "2"  ;

                        cohost.setVisibility(View.VISIBLE);
                    }


                }

            }
        });


        radioGroupong.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);

                if (null != rb && checkedId > -1) {

                    //  Toast.makeText(AddAlbum.this, rb.getText(), Toast.LENGTH_SHORT).show();

                    String text = (String) rb.getText();
                    text = text.replaceAll("\\s", "");

//text = "r";
                    if(text.equals("Createnewongoing/repeatproject"))// One Time Project
                    {
                        //onetime1.setVisibility(View.VISIBLE);


                        ong_neww = "1" ;

                        radioGroupong_new.clearCheck();
                        ong_new.setVisibility(View.VISIBLE);
                        ong_jnt.setVisibility(View.GONE);

                        ong_ntlt.setVisibility(View.VISIBLE);
                        selectp.setVisibility(View.GONE);
                       // ont_ong_selct.setVisibility(View.INVISIBLE);

//                         radioGroup.setVisibility(View.GONE);
//                         radioGroupong.setVisibility(View.GONE);
//                         v_1.setVisibility(View.GONE);
//                         v2.setVisibility(View.GONE);
                        String ttt = "";

                        try
                        {
                           ttt = TtlOfNew ;

                        }catch (Exception e)
                        {
                             ttt = "" ;
                        }

                        if(ttt.equalsIgnoreCase("1")){

                           // Toast.makeText(getApplicationContext(),"sds"+ttt,Toast.LENGTH_LONG).show();
                            radioGroup.setVisibility(View.GONE);
                            radioGroupong.setVisibility(View.GONE);
                            v_1.setVisibility(View.GONE);
                            v2.setVisibility(View.GONE);
                        }


//
//                        int rr = 2;
//
//                         if(tt == 0){
//                             Toast.makeText(getApplicationContext(),"sds"+tt,Toast.LENGTH_LONG).show();
//                             radioGroup.setVisibility(View.GONE);
//                             radioGroupong.setVisibility(View.GONE);
//                             v_1.setVisibility(View.GONE);
//                             v2.setVisibility(View.GONE);
//                             tt = 2 ;
//                         }
//                         if(tt == 2)
//                         {
//
//                         }
//












                        // Toast.makeText(getApplicationContext(),"jjj",Toast.LENGTH_LONG).show();

                      //  ont_ong_selct.setVisibility(View.GONE);
                       // radioGroupong.setEnabled(false);

                       // radioGroup.setEnabled(false);

                    }
                    else if(text.equalsIgnoreCase("Selectfromexistingongoing/repeatproject"))
                    {
                        //  onetime1.setVisibility(View.GONE);
                        //  ong_new.setVisibility(View.GONE);

                        ong_neww = "2" ;


                        ong_jnt.setVisibility(View.GONE);
                        radioGroupong_new.clearCheck();
                        ong_new.setVisibility(View.VISIBLE);
                        ong_jnt.setVisibility(View.GONE);


                        ong_ntlt.setVisibility(View.GONE);
                        selectp.setVisibility(View.VISIBLE);
                        String ss = "not";
                        getong_pro(ss);



                    }


                }

            }
        });


        radioGroupong_new.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {

                    //  Toast.makeText(AddAlbum.this, rb.getText(), Toast.LENGTH_SHORT).show();

                    radioGroup2.clearCheck();
                    String text = (String) rb.getText();
                    text = text.replaceAll("\\s", "");

                    radioGroupong2.clearCheck();

//text = "r";
                    if(text.equals("Yes"))// One Time Project
                    {

                        crt_newyes = "1" ;


                        ong_jnt.setVisibility(View.VISIBLE);
                        // cohost.setVisibility(View.GONE);
                    }
                    else if(text.equalsIgnoreCase("No"))
                    {
                        crt_newyes = "2" ;





                        // cohost2.setVisibility(View.VISIBLE);
                        ong_jnt.setVisibility(View.GONE);
                        cohost.setVisibility(View.GONE);
                    }


                }

            }
        });

        radioGroupong2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {

                    //  Toast.makeText(AddAlbum.this, rb.getText(), Toast.LENGTH_SHORT).show();

                    String text = (String) rb.getText();
                    text = text.replaceAll("\\s", "");

//text = "r";
                    if(text.equals("LeadClub"))// One Time Project
                    {
                        //onetime1.setVisibility(View.VISIBLE);


                        ong_clubrole = "1";

                        cohost2.setVisibility(View.GONE);
                    }
                    else if(text.equalsIgnoreCase("Co-Host"))
                    {
                        //  onetime1.setVisibility(View.GONE);
                        ong_clubrole = "2";

                        cohost2.setVisibility(View.VISIBLE);
                    }


                }

            }
        });

        ///----------------------------------------------------------------------------------------



        String tlt = tv_title.getText().toString();
        if(tlt == null)
        {
            tlt = "";
        }
        Log.d("tittleofedit",tlt);

        if (tlt.equalsIgnoreCase("Edit Service Project")) {
            ll_attach.setVisibility(View.GONE);
         //   Toast.makeText(getApplicationContext(),"jjj"+tlt,Toast.LENGTH_LONG).show();
        }
        else
        {
          //
            //  ll_attach.setVisibility(View.GONE);
           // Toast.makeText(getApplicationContext(),"not"+tlt,Toast.LENGTH_LONG).show();
        }

//      if(ll_attach.getVisibility() == View.VISIBLE)
//      {
//
//          ll_attach.setVisibility(View.GONE);
//
//      }

    }

    //this method is added by Gaurav for calculating attendance % base on membercount

    private float getAttendancePercentage(String attendance) {
        double attendancevalue = Double.parseDouble( attendance );
        double MemberCountvalue = Double.parseDouble( MemberCount );


        float output = (float) ((attendancevalue * 100) / MemberCountvalue);
        return output;

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

        // TODO Hide Future Date Here
        datePickerDialog.getDatePicker().setMaxDate( System.currentTimeMillis() );
        datePickerDialog.show();
    }

    //Add this Two Method By Gaurav for sub and subtosub category

    private void filterData_SubCategory(String categoryID) {


        ArrayList<AlbumData> subcategoryListchangeData = new ArrayList<>();

        updateDataForSubcategory = new AlbumData();

        updateDataForSubcategory.setPk_subcategoryIdupdate( "0" );
        updateDataForSubcategory.setFk_CategoryIDupdate( "0" );

        updateDataForSubcategory.setSubcategoryNameupdate( "Select" );

        subcategoryListchangeData.add( updateDataForSubcategory );

        for (int i = 0; i < subcategoryList.size(); i++) {

            if (categoryID.equals( subcategoryList.get( i ).getFk_CategoryID() )) {
                updateDataForSubcategory = new AlbumData();

                updateDataForSubcategory.setPk_subcategoryIdupdate( subcategoryList.get( i ).getPk_subcategoryId() );
                updateDataForSubcategory.setFk_CategoryIDupdate( subcategoryList.get( i ).getFk_categoryID() );

                updateDataForSubcategory.setSubcategoryNameupdate( subcategoryList.get( i ).getSubcategoryName() );

                subcategoryListchangeData.add( updateDataForSubcategory );

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
        adapter_subcategory_update = new SpinnerAdapter_subcategoryupdate( context, subcategoryListupdate );
        sp_subcategory.setAdapter( adapter_subcategory_update );
        ll_subcategory.setVisibility( View.VISIBLE );

        if (subcategoryListupdate.size() == 0 || subcategoryListupdate.size() == 1) {
            ll_subcategory.setVisibility( View.GONE );
            sp_blankselect_SubCategory = "0";
            sp_blankselect_SubtosubCategory = "0";
        } else {
            ll_subcategory.setVisibility( View.VISIBLE );
            ll_subtosubcategory.setVisibility( View.GONE );
            sp_blankselect_SubtosubCategory = "0";

        }

        //  sp_subcategory.setSelection(Integer.parseInt(subcategoryIDupdate) - 1);
        sp_subcategory.setSelection( getIndexforsubcategory( subcategoryListupdate, subcategoryselectedtext ) );


        //End Data here for subcategory

    }

    private void filterData_SubtosubCategory(String subcategoryID) {

        //Filter data for subtosubCategory

        ArrayList<AlbumData> subtosubcategoryListchangeData = new ArrayList<>();

        updateDataForSubtosubcategory = new AlbumData();


        updateDataForSubtosubcategory.setPk_subtosubcategoryIdupdate( "0" );
        updateDataForSubtosubcategory.setFk_subcategoryidupdate( "0" );
        updateDataForSubtosubcategory.setFk_categoryIDupdate( "0" );
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

        //Added Spinner for subtosub Category

        subtosubcategoryListupdate = subtosubcategoryListchangeData;
        adapter_subtosubcategory_update = new SpinnerAdapter_subtosubcategoryupdate( context, subtosubcategoryListupdate );
        sp_subtosubcategory.setAdapter( adapter_subtosubcategory_update );

        if (subtosubcategoryListupdate.size() == 0 || subtosubcategoryListupdate.size() == 1) {
            ll_subtosubcategory.setVisibility( View.GONE );
            sp_blankselect_SubtosubCategory = "0";
        } else {

            ll_subtosubcategory.setVisibility( View.VISIBLE );
            sp_subtosubcategory.setSelection( getIndexforsubtosubcategory( subtosubcategoryListupdate, subtosubcategoryselectedtext ) );

        }


        //End Code Here

    }

    private int getIndexforsubcategory(ArrayList<AlbumData> list, String myString) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get( i ).getSubcategoryNameupdate().toString().equalsIgnoreCase( myString )) {
                return i;
            }
        }
        return 0;
    }

    private int getIndexforsubtosubcategory(ArrayList<AlbumData> list, String myString) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get( i ).getSubtosubcategorynameupdate().toString().equalsIgnoreCase( myString )) {
                return i;
            }
        }
        return 0;
    }


    public void onRadioButtonClicked(View view) {

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.d_radio0:
                if (checked) {
                    //set inch button to unchecked
                    galleryType = "0";
                    llShareWrapper.setVisibility( View.VISIBLE );
                } else {
                    llShareWrapper.setVisibility( View.VISIBLE );
                }
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
               /*     d_radio0.setChecked(false);
                d_radio2.setChecked(false);
                d_radio1.setEnabled(false);
                d_radio2.setEnabled(true);*/
                    d_radio1.setChecked( false );
                llShareWrapper.setVisibility( View.GONE );
                Intent subgrp = new Intent( EditAlbumActivity.this, NewGroupSelectionActivity.class );
                subgrp.putExtra( "flag_addsubgrp", "1" );
                startActivityForResult( subgrp, 1 );
                //startActivity(subgrp);
                break;

            case R.id.d_radio2:

                if (checked) {
                    d_radio2.setChecked( false );
                    Intent i = new Intent( EditAlbumActivity.this, AddMembers.class );
                    startActivityForResult( i, 3 );
                }

                llShareWrapper.setVisibility( View.GONE );
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
                    authlayout.setVisibility( View.GONE );
                }

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
                radio_mediaprint_yes.setChecked( false );
                radio_mediaprint_No.setChecked( true );
                break;
            case R.id.radio_mediaprint_yes:
                //then auth of media displayed and text
                if (checked) {
                    //set inch button to unchecked
                    Ismedia = "1";
                    authlayout.setVisibility( View.VISIBLE );
                }
                radio_mediaprint_No.setChecked( false );
                radio_mediaprint_yes.setChecked( true );
                break;
        }
    }

    public void init() {
//
//        edt_description.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (v.getId() == R.id.et_evetDesc) {
//                    v.getParent().requestDisallowInterceptTouchEvent(true);
//                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
//                        case MotionEvent.ACTION_UP:
//                            v.getParent().requestDisallowInterceptTouchEvent(false);
//                            break;
//                    }
//                }
//                return false;
//            }
//        });

        tv_cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard( EditAlbumActivity.this );
                finish();
            }
        } );

        iv_image.setOnClickListener( new View.OnClickListener() {

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

        iv_edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (galleryType.equals( "2" )) {
                    Intent i = new Intent( EditAlbumActivity.this, AddMembers.class );
                    //i.putParcelableArrayListExtra("name1", memberData);
                    i.putParcelableArrayListExtra( "selected_memberdata", listaddmemberdata );
                    i.putExtra( "edit_announcement_selectedids", edit_album_selectedids );
                    startActivityForResult( i, 3 );
                } else if (galleryType.equals( "1" )) {
//                    Intent i = new Intent(EditAlbumActivity.this, SubGroupList.class);
//                    //i.putParcelableArrayListExtra("name1", memberData);
//                    i.putParcelableArrayListExtra("selected_subgrpdata", listaddsubgrp);
//                    i.putExtra("edit_gallery_selectedids", edit_gallery_selectedids);
//                    startActivityForResult(i, 1);

                    Intent subgrp = new Intent( EditAlbumActivity.this, NewGroupSelectionActivity.class );
                    subgrp.putExtra( "flag_addsubgrp", "1" );
                    subgrp.putExtra( "selected", selectedsubgrp );
                    subgrp.putExtra( "edit", "1" );
                    startActivityForResult( subgrp, 1 );
                }

            }
        } );

        //this is added new image
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
                        Ismedia = "1";
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

//
//
//        String tlt = tv_title.getText().toString();
//        if(tlt == null)
//        {
//            tlt = "";
//        }
//        Log.d("tittleofedit",tlt);
//
//        if (!tlt.equalsIgnoreCase("Edit Service Project")) {
//            ll_attach.setVisibility(View.GONE);
//
//
//        }


        tv_add.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

              /*  Thread t = new Thread() {
                    public void run() {
                        //This class Added By gaurav for hide soft keyboaard
                        CloseSoftKeyboard.hideSoftKeyboard(EditAlbumActivity.this);
                    }
                };
                t.start();*/

                hideKeyboard( EditAlbumActivity.this );

                try {


                   // serviceprojecttt
//                    String moveflag = getIntent().getStringExtra("serviceprojecttt");
//                    if(moveflag == null)
//                    {
//                        moveflag ="2";
//                    }
//                    else if(moveflag.isEmpty() || moveflag.equalsIgnoreCase(""))
//                    {
//                        moveflag ="2";
//                    }
//                    else
//                    {
//
//                    }
                    String tlt = tv_title.getText().toString();
                    if(tlt == null)
                    {
                        tlt = "";
                    }
                    Log.d("tittleofedit",tlt);

                    if (!tlt.equalsIgnoreCase("Edit Service Project")) {
                        if (validation()) {





                            if (InternetConnection.checkConnection(getApplicationContext())) {
                                webservices();

                            } else {
                                Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                            }
                        }
                    } else if (validation1() && validation2() && validation3()) {


                        if (validation()) {


                            if (InternetConnection.checkConnection(getApplicationContext())) {
                                webservices();

                            } else {
                                Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                            }
                        }

                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }






            }
        } );

        //media image close

        close6.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog( EditAlbumActivity.this, android.R.style.Theme_Translucent );
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
                        Ismedia = "1";

                    }
                } );

                tv_yes.setOnClickListener( new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "true";
                        // deletePhotoflag = 6;

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
                } );

                dialog.show();
            }
        } );

        close1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog( EditAlbumActivity.this, android.R.style.Theme_Translucent );
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

                        if (!photoId1.isEmpty() && !photoId1.equalsIgnoreCase( "0" )) {

                            if (InternetConnection.checkConnection( EditAlbumActivity.this )) {
                                deletePhotoflag = 1;
                                deletePhoto( albumId, photoId1 );
                            } else {
                                Toast.makeText( EditAlbumActivity.this, "No internet connection", Toast.LENGTH_LONG ).show();
                            }

                        } else {


                            //mediaphoto is OFF
                            imageList.remove( 0 );
                            imageList.add( 0, "" );

                            descList.remove( 0 );
                            descList.add( 0, "" );

                            photoIdList.remove( 0 );
                            photoIdList.add( 0, "0" );
                            photoId1 = "0";
                            iv_image.setBackground( getResources().getDrawable( R.drawable.asset ) );
                            iv_image.setImageResource( 0 );
                            close1.setVisibility( View.GONE );


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
                } );

                dialog.show();
            }
        } );

        close2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog( EditAlbumActivity.this, android.R.style.Theme_Translucent );
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

                        if (!photoId2.isEmpty() && !photoId2.equalsIgnoreCase( "0" )) {
                            if (InternetConnection.checkConnection( EditAlbumActivity.this )) {
                                deletePhotoflag = 2;
                                deletePhoto( albumId, photoId2 );
                            } else {
                                Toast.makeText( EditAlbumActivity.this, "No internet connection", Toast.LENGTH_LONG ).show();
                            }
                        } else {

                            imageList.remove( 1 );
                            imageList.add( 1, "" );

                            descList.remove( 1 );
                            descList.add( 1, "" );

                            photoIdList.remove( 1 );
                            photoIdList.add( 1, "0" );
                            photoId2 = "0";
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
                final Dialog dialog = new Dialog( EditAlbumActivity.this, android.R.style.Theme_Translucent );
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

                        if (!photoId3.isEmpty() && !photoId3.equalsIgnoreCase( "0" )) {
                            if (InternetConnection.checkConnection( EditAlbumActivity.this )) {
                                deletePhotoflag = 3;
                                deletePhoto( albumId, photoId3 );
                            } else {
                                Toast.makeText( EditAlbumActivity.this, "No internet connection", Toast.LENGTH_LONG ).show();
                            }
                        } else {


                            imageList.remove( 2 );
                            imageList.add( 2, "" );

                            descList.remove( 2 );
                            descList.add( 2, "" );

                            photoIdList.remove( 2 );
                            photoIdList.add( 2, "0" );
                            photoId3 = "0";
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
                final Dialog dialog = new Dialog( EditAlbumActivity.this, android.R.style.Theme_Translucent );
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

                        if (!photoId4.isEmpty() && !photoId4.equalsIgnoreCase( "0" )) {
                            if (InternetConnection.checkConnection( EditAlbumActivity.this )) {
                                deletePhotoflag = 4;
                                deletePhoto( albumId, photoId4 );
                            } else {
                                Toast.makeText( EditAlbumActivity.this, "No internet connection", Toast.LENGTH_LONG ).show();
                            }
                        } else {

                            imageList.remove( 3 );
                            imageList.add( 3, "" );

                            descList.remove( 3 );
                            descList.add( 3, "" );

                            photoIdList.remove( 3 );
                            photoIdList.add( 3, "0" );
                            photoId4 = "0";
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
                final Dialog dialog = new Dialog( EditAlbumActivity.this, android.R.style.Theme_Translucent );
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

                        if (!photoId5.isEmpty() && !photoId5.equalsIgnoreCase( "0" )) {
                            if (InternetConnection.checkConnection( EditAlbumActivity.this )) {
                                deletePhotoflag = 5;
                                deletePhoto( albumId, photoId5 );
                            } else {
                                Toast.makeText( EditAlbumActivity.this, "No internet connection", Toast.LENGTH_LONG ).show();
                            }
                        } else {

                            imageList.remove( 4 );
                            imageList.add( 4, "" );

                            descList.remove( 4 );
                            descList.add( 4, "" );

                            photoIdList.remove( 4 );
                            photoIdList.add( 4, "0" );
                            photoId5 = "0";
                            iv_album_photo4.setBackground( getResources().getDrawable( R.drawable.asset ) );
                            iv_album_photo4.setImageResource( 0 );
                            close5.setVisibility( View.GONE );


                        }

                    }
                } );

                dialog.show();
            }
        } );

        ll_attach_agenda.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                } else {
                    showFileChooser( AGENDA );
                }
            }
        } );

        ll_attach_mom.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                } else {
                    showFileChooser( MOM );
                }
            }
        } );


        iv_delete_agenda.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agenda_file_id = "0";
                txt_fileName.setText( "" );
                ll_agenda_file.setVisibility( View.GONE );
                Utils.log( "Third" );
                ll_attach_agenda.setVisibility( View.VISIBLE );
            }
        } );


        iv_delete_mom.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mom_file_id = "0";
                txt_fileName_mom.setText( "" );
                ll_mom_file.setVisibility( View.GONE );
                ll_attach_mom.setVisibility( View.VISIBLE );
            }
        } );

        edt_description.addTextChangedListener( new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            public void afterTextChanged(Editable s) {
                CharacterStyle[] toBeRemovedSpans =
                        s.getSpans( 0, s.length(), MetricAffectingSpan.class );

                for (int i = 0; i < toBeRemovedSpans.length; i++)
                    s.removeSpan( toBeRemovedSpans[i] );

            }

        } );
    }

    private void showFileChooser(int module) {

        String[] mimeTypes = {"application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};

        Intent intent = new Intent( Intent.ACTION_GET_CONTENT );
        //   File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/example.pdf");
        intent.setType( "application/pdf" );
        intent.addCategory( Intent.CATEGORY_OPENABLE );
        intent.putExtra( Intent.EXTRA_LOCAL_ONLY, true );
        intent.putExtra( Intent.EXTRA_MIME_TYPES, mimeTypes );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION );
        }

        try {
            startActivityForResult(
                    Intent.createChooser( intent, "Select a File to Upload" ), module );
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText( this, "Could not connect to the File Manager",
                    Toast.LENGTH_SHORT ).show();
        }
    }

    public class sendFile extends AsyncTask<Intent, Void, String> {

        ProgressDialog progressDialog;
        String displayName = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog( EditAlbumActivity.this, R.style.TBProgressBar );
            progressDialog.setCancelable( false );
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Intent... param) {
            Intent data = param[0];
            double length = 0;

            Uri uri = data.getData();
            String uriString = uri.toString();

            //MEDIA GALLERY
           /* selectedImagePath = Utils.getPath(getApplicationContext(), uri);

            Utils.log(selectedImagePath +" / "+uriString);

            File myFile = new File(selectedImagePath);

            //  Log.d("***********", "-----" + myFile);
//
            if (uriString.startsWith("content://")) {

                Cursor cursor = null;

                try {

                    cursor = EditAlbumActivity.this.getContentResolver().query(uri, null, null, null, null);

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
            }*/

            File myFile = null;

            if (uriString.startsWith( "content://" )) {

                selectedImagePath = Utils.getPathLocal( uri, EditAlbumActivity.this );//Utils.getPath(getApplicationContext(), uri);

                Log.d( "***********", "----- path=> " + selectedImagePath );

                myFile = new File( selectedImagePath );

                Cursor cursor = null;

                try {

                    cursor = EditAlbumActivity.this.getContentResolver().query( uri, null, null, null, null );

                    if (cursor != null && cursor.moveToFirst()) {

                        displayName = cursor.getString( cursor.getColumnIndex( OpenableColumns.DISPLAY_NAME ) );

                        Log.d( "-----", "-----" + displayName );

                        length = cursor.getLong( cursor.getColumnIndex( OpenableColumns.SIZE ) );

                    }

                } finally {
                    cursor.close();
                }

            } else if (uriString.startsWith( "file://" )) {

                displayName = uriString.substring( uriString.lastIndexOf( "/" ) + 1 );

                File cacheDir = Utils.getDocumentCacheDir( EditAlbumActivity.this );
                myFile = Utils.generateFileName( "row_gallery" + System.currentTimeMillis(), cacheDir );
                String destinationPath = null;

                if (myFile != null) {
                    destinationPath = myFile.getAbsolutePath();
                    Utils.saveFileFromUri( EditAlbumActivity.this, uri, destinationPath );
                }

                length = myFile.length();

            }

            double size = Double.parseDouble( decimalformat.format( length / 1048576 ) );

            Log.d( "***********", "-----" + String.valueOf( size ) );

            String filenameArray[] = displayName.split( "\\." );
            String extension = filenameArray[filenameArray.length - 1];

            Log.d( "***********", "-----" + extension );

            if (!extension.equalsIgnoreCase( "pdf" ) && !extension.equalsIgnoreCase( "doc" ) && !extension.equalsIgnoreCase( "docx" )) {
                return "fileExt";
            } else if (size > 10) {
                return "filesize";
            } else {
                String result = "";
                if (displayName != null && !displayName.isEmpty()) {
                    //link.setText(wpath);
//                    tv_name_pdf.setText(displayName);

                        /*    Intent i = new Intent(getApplicationContext(), E_BulletineAdapter.class);
                            i.putExtra("file name", displayName);
                            startActivity(i);*/

//                    agenda_file_id = Utils.doPdfFileUpload(new File(selectedImagePath), "gallery"); // Upload File to server

                    agenda_file_id = Utils.doPdfFileUpload( myFile, "gallery" ); // Upload File to server

                    Log.d( "TOUCHBASE", "FILE UPLOAD ID  " + agenda_file_id );

                    if (agenda_file_id != "0") {
                        result = agenda_file_id;
//                            Toast.makeText(AddE_bulletin.this, "Uploaded Successfully", Toast.LENGTH_LONG).show();

                    }
//                        Toast.makeText(AddE_bulletin.this, "Only “Pdf” files can be shared as an e-Bulletin.", Toast.LENGTH_LONG).show();
                }
                return result;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute( s );

            progressDialog.dismiss();

            if (s != null && !s.isEmpty()) {
                if (s.equalsIgnoreCase( "filesize" )) {
                    Utils.showToastWithTitleAndContext( EditAlbumActivity.this, "File size must not be greater than 10 MB" );
                } else if (s.equalsIgnoreCase( "fileExt" )) {
                    Utils.showToastWithTitleAndContext( EditAlbumActivity.this, "Only pdf, doc & docx files are allowed" );
                } else {
                    ll_agenda_file.setVisibility( View.VISIBLE );
                    ll_attach_agenda.setVisibility( View.GONE );
                    txt_fileName.setText( displayName );
                    agenda_file_id = s;
                }
            } else {
                txt_fileName.setText( "" );
                Utils.showToastWithTitleAndContext( EditAlbumActivity.this, getString( R.string.msgRetry ) );
            }
        }
    }

    public class sendFileMOM extends AsyncTask<Intent, Void, String> {

        ProgressDialog progressDialog;
        String displayName = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog( EditAlbumActivity.this, R.style.TBProgressBar );
            progressDialog.setCancelable( false );
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Intent... param) {

            Intent data = param[0];
            double length = 0;

            Uri uri = data.getData();
            String uriString = uri.toString();

            //MEDIA GALLERY
           /* selectedImagePath = Utils.getPath(getApplicationContext(), uri);
            File myFile = new File(selectedImagePath);
            //  Log.d("***********", "-----" + myFile);
//
            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = EditAlbumActivity.this.getContentResolver().query(uri, null, null, null, null);
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
            }*/

            File myFile = null;

            if (uriString.startsWith( "content://" )) {

                selectedImagePath = Utils.getPathLocal( uri, EditAlbumActivity.this );//Utils.getPath(getApplicationContext(), uri);

                Log.d( "***********", "----- path=> " + selectedImagePath );

                myFile = new File( selectedImagePath );

                Cursor cursor = null;

                try {

                    cursor = EditAlbumActivity.this.getContentResolver().query( uri, null, null, null, null );

                    if (cursor != null && cursor.moveToFirst()) {

                        displayName = cursor.getString( cursor.getColumnIndex( OpenableColumns.DISPLAY_NAME ) );

                        Log.d( "-----", "-----" + displayName );

                        length = cursor.getLong( cursor.getColumnIndex( OpenableColumns.SIZE ) );

                    }

                } finally {
                    cursor.close();
                }

            } else if (uriString.startsWith( "file://" )) {

                displayName = uriString.substring( uriString.lastIndexOf( "/" ) + 1 );

                File cacheDir = Utils.getDocumentCacheDir( EditAlbumActivity.this );
                myFile = Utils.generateFileName( "row_gallery" + System.currentTimeMillis(), cacheDir );
                String destinationPath = null;

                if (myFile != null) {
                    destinationPath = myFile.getAbsolutePath();
                    Utils.saveFileFromUri( EditAlbumActivity.this, uri, destinationPath );
                }

                length = myFile.length();

            }

            double size = Double.parseDouble( decimalformat.format( length / 1048576 ) );
            Log.d( "***********", "-----" + String.valueOf( size ) );

            String filenameArray[] = displayName.split( "\\." );
            String extension = filenameArray[filenameArray.length - 1];
            Log.d( "***********", "-----" + extension );

            if (!extension.equalsIgnoreCase( "pdf" ) && !extension.equalsIgnoreCase( "doc" ) && !extension.equalsIgnoreCase( "docx" )) {
                return "fileExt";
            } else if (size > 10) {
                return "filesize";
            } else {
                String result = "";
                if (displayName != null && !displayName.isEmpty()) {
                    //link.setText(wpath);
//                    tv_name_pdf.setText(displayName);

                        /*    Intent i = new Intent(getApplicationContext(), E_BulletineAdapter.class);
                            i.putExtra("file name", displayName);
                            startActivity(i);*/


//                    mom_file_id = Utils.doPdfFileUpload(new File(selectedImagePath), "gallery"); // Upload File to server

                    mom_file_id = Utils.doPdfFileUpload( myFile, "gallery" ); // Upload File to server

                    Log.d( "TOUCHBASE", "FILE UPLOAD ID  " + mom_file_id );
                    if (mom_file_id != "0") {
                        result = mom_file_id;
//                            Toast.makeText(AddE_bulletin.this, "Uploaded Successfully", Toast.LENGTH_LONG).show();

                    }
//                        Toast.makeText(AddE_bulletin.this, "Only “Pdf” files can be shared as an e-Bulletin.", Toast.LENGTH_LONG).show();
                }
                return result;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute( s );

            progressDialog.dismiss();

            if (s != null && !s.isEmpty()) {
                if (s.equalsIgnoreCase( "filesize" )) {
                    Utils.showToastWithTitleAndContext( EditAlbumActivity.this, "File size must not be greater than 10 MB" );
                } else if (s.equalsIgnoreCase( "fileExt" )) {
                    Utils.showToastWithTitleAndContext( EditAlbumActivity.this, "Only pdf, doc & docx files are allowed" );
                } else {
                    ll_mom_file.setVisibility( View.VISIBLE );
                    ll_attach_mom.setVisibility( View.GONE );
                    txt_fileName_mom.setText( displayName );
                    mom_file_id = s;
                }
            } else {
                txt_fileName_mom.setText( "" );
                Utils.showToastWithTitleAndContext( EditAlbumActivity.this, getString( R.string.msgRetry ) );
            }
        }
    }

    private void selectAlbumImages() {
        Intent intent = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        startActivityForResult( intent, 6 );
    }

    private void selectAlbumImagesForMediaPhoto() {
        Intent intent = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        startActivityForResult( intent, 7 );
    }


    public void loadFromServer() {
        if (InternetConnection.checkConnection( this )) {
            loadData();




            //Added By Gaurav
        } else {
            Toast.makeText( this, "No internet connection", Toast.LENGTH_LONG ).show();
        }
    }

    public void loadData() {

        Log.e( "Touchbase", "------ loadData() called" );
        String url = Constant.GetAlbumDetails_New;
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add( new BasicNameValuePair( "albumId", albumId ) );
        arrayList.add( new BasicNameValuePair( "Financeyear",  Gallery.year) );

        Log.d( "Album Data", "PARAMETERS " + Constant.GetAlbumDetails_New + " :- " + arrayList.toString() );
        GetAlbumDetailsAsynctask task = new GetAlbumDetailsAsynctask( url, arrayList, this );
        task.execute();

    }

    public void validateDirectBeneficiaries() {

        Log.e( "Touchbase", "------ validateDirectBeneficiaries() called" );
        String url = Constant.GetValidateDirectBeneficiaries;
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add( new BasicNameValuePair( "albumId", albumId ) );
        arrayList.add( new BasicNameValuePair( "Financeyear", Gallery.year ) );




        Log.d( "Album Data", "PARAMETERS " + Constant.GetValidateDirectBeneficiaries + " :- " + arrayList.toString() );
        GetValidateDirectBeneficiariesAsynctask task = new GetValidateDirectBeneficiariesAsynctask( url, arrayList, this );
        task.execute();

    }


    @Override
    public void onBackPressed() {
        Utils.popupback( EditAlbumActivity.this );
    }

    public class GetAlbumDetailsAsynctask extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog( EditAlbumActivity.this, R.style.TBProgressBar );
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
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (result != "") {
                Log.d( "Response", "calling GetAlbumDetails" );



                getAlbumDetails( result.toString() );


                Log.d( "GetAlbumDetails", result.toString() );
            } else {
                Log.d( "Response", "Null Resposnse" );
                Toast.makeText( EditAlbumActivity.this, "Something went wrong", Toast.LENGTH_SHORT ).show();
            }

        }
    }

    public class GetValidateDirectBeneficiariesAsynctask extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog( EditAlbumActivity.this, R.style.TBProgressBar );
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;


        public GetValidateDirectBeneficiariesAsynctask(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
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
            try {
                val = HttpConnection.postData( url, argList );
                val = val.toString();
                Log.d( "benefeceryrequest", "we" + val +"Requset ===   "+ argList.toString() );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return val;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute( result );
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (result != "") {
                Log.d( "Response", "calling GetValidateDirectBeneficiaries" +result.toString());

                getValidateDirectBeneficiariesDetails( result.toString() );

                Log.d( "B_ficiaries", result.toString() );
            } else {
                Log.d( "Response", "Null Resposnse" );
                Toast.makeText( EditAlbumActivity.this, "Something went wrong", Toast.LENGTH_SHORT ).show();
            }

        }
    }

    public void getAlbumDetails(String result) {
        try {
            JSONObject jsonObj = new JSONObject( result );

            JSONObject jsonTBAlbumsListResult = jsonObj.getJSONObject( "TBAlbumDetailResult" );
            final String status = jsonTBAlbumsListResult.getString( "status" );

            if (status.equals( "0" )) {

                JSONArray jsonNewAlbumList = jsonTBAlbumsListResult.getJSONArray( "AlbumDetailResult" );

                if (jsonNewAlbumList.length() < 1) {
                    return;
                }

                JSONObject object = jsonNewAlbumList.getJSONObject( 0 );
                JSONObject jsonAlbumDetail = object.getJSONObject( "AlbumDetail" );
                String type = jsonAlbumDetail.getString( "type" );

                d_radio0.setChecked( true );
                galleryType = "0";
                shareType = jsonAlbumDetail.getString( "shareType" );


                Ismedia = jsonAlbumDetail.getString( "Ismedia" );

                sp_category.setSelection( Integer.parseInt( jsonAlbumDetail.getString( "albumCategoryID" ) ) - 1 );
                sp_category.setSelection( getIndex( categoryList, jsonAlbumDetail.getString( "albumCategoryText" ) ) );
                Log.d( "Index", getIndex( categoryList, jsonAlbumDetail.getString( "albumCategoryText" ) ) + "" );

                //funding fetched Data

                sp_funding.setSelection( Integer.parseInt( jsonAlbumDetail.getString( "Fk_FundingID" ) ) - 1 );
                sp_funding.setSelection( getIndexFunding( fundingList, jsonAlbumDetail.getString( "FundingText" ) ) );
                Log.d( "Index", getIndexFunding( fundingList, jsonAlbumDetail.getString( "FundingText" ) ) + "" );


                //Added UpdatedId

                subcategoryID = jsonAlbumDetail.getString( "Fk_SubcategoryID" );
                //   sp_subcategory.setSelection(Integer.parseInt(jsonAlbumDetail.getString("Fk_SubcategoryID")));
                subcategoryselectedtext = jsonAlbumDetail.getString( "SubcategoryText" );
                sp_subcategory.setSelection( getIndexforsubcategory( subcategoryListupdate, subcategoryselectedtext ) );


                subtosubcategoryID = jsonAlbumDetail.getString( "Fk_SubTosubcategoryID" );
                //  sp_subtosubcategory.setSelection(Integer.parseInt(jsonAlbumDetail.getString("Fk_SubTosubcategoryID")));
                subtosubcategoryselectedtext = jsonAlbumDetail.getString( "SubtosubcategoryText" );
                sp_subtosubcategory.setSelection( getIndexforsubtosubcategory( subtosubcategoryListupdate, subtosubcategoryselectedtext ) );

                if (shareType.equals( "0" )) {



                 //   ont_ong_selct.setVisibility(View.GONE);
                    //Club Service of data should be displayed
                    // rbInClub.setChecked(true);
                    // rbPublic.setChecked(false);

                  /*  attendance_layout.setVisibility(View.VISIBLE);
                    meeting_layout.setVisibility(View.VISIBLE);
                    rotarctors_layout.setVisibility(View.GONE);
                    et_rotractors.setVisibility(View.GONE);
                    media_layout.setVisibility(View.GONE);*/

                } else if (shareType.equals( "1" )) {
                  //  ont_ong_selct.setVisibility(View.VISIBLE);
                    //  rbPublic.setChecked(true);
                    //  rbInClub.setChecked(false);
                    //Rotary service of data should de displayed

                  /*  attendance_layout.setVisibility(View.GONE);
                    meeting_layout.setVisibility(View.GONE);
                    rotarctors_layout.setVisibility(View.VISIBLE);
                    et_rotractors.setVisibility(View.VISIBLE);
                    media_layout.setVisibility(View.VISIBLE);*/

                }
                if (Ismedia.equals( "1" )) {
                    //Yes check box is on
                    authlayout.setVisibility( View.VISIBLE );
                    // mediaradiolayot.setVisibility(View.VISIBLE);
                    radio_mediaprint_No.setChecked( false );
                    radio_mediaprint_yes.setChecked( true );

                    //this two is added By Gaurav for set MediaPhoto Description and Pic
                    //this is added new
                    MediaphotoID = jsonAlbumDetail.getString( "MediaphotoID" );

                    mediaPhotoPath = jsonAlbumDetail.getString( "Mediaphoto" );
                    if (mediaPhotoPath.equals( "" )) {
                        //Image is deleted
                    } else {

                        Picasso.with( EditAlbumActivity.this ).load( jsonAlbumDetail.getString( "Mediaphoto" ).toString() )
                                //.fit()
                                //.resize(200, 200)
                                .placeholder( R.drawable.placeholder_new )
                                .into( iv_album_photo_auth );
                        iv_album_photo_auth.setBackground( null );
                        et_album_photo1_auth.setText( jsonAlbumDetail.getString( "MediaDesc" ).toString() );
                        if (iv_album_photo_auth.getDrawable() != null) {
                            close6.setVisibility( View.VISIBLE );
                        }
                    }


                } else {
                    //No check box is on
                    authlayout.setVisibility( View.GONE );
                    // mediaradiolayot.setVisibility(View.GONE);
                    radio_mediaprint_yes.setChecked( false );
                    radio_mediaprint_No.setChecked( true );
                    //media clear if previous image is deleted
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

                }


                edt_title.setText( jsonAlbumDetail.getString( "albumTitle" ) );
                edt_description.setText( jsonAlbumDetail.getString( "albumDescription" ) );
                et_categoryName.setText( jsonAlbumDetail.getString( "othercategorytext" ) );
                et_Beneficiary.setText( jsonAlbumDetail.getString( "beneficiary" ) );
                et_COP.setText( jsonAlbumDetail.getString( "project_cost" ) );
                et_rotractors.setText( jsonAlbumDetail.getString( "Rotaractors" ) );

                projectDate = jsonAlbumDetail.getString( "project_date" );


                if (projectDate != null && !projectDate.equalsIgnoreCase( "" )) {
                    Date date = format1.parse( projectDate );
                    et_DOP.setText( format.format( date ) );
                }
                et_manPower.setText( jsonAlbumDetail.getString( "working_hour" ) );
                et_noOfRotarians.setText( jsonAlbumDetail.getString( "NumberOfRotarian" ) );


                //this is spinner subcategory selected text & index
                //   sp_subcategory.setSelection(Integer.parseInt(subcategoryIDupdate) - 1);


                //    sp_subcategory.setSelection(getIndexforsubcategory(subcategoryListupdate, jsonAlbumDetail.getString("SubcategoryText")));

                // sp_subcategory.setSelection(getIndexforsubcategory(subcategoryList, jsonAlbumDetail.getString("SubcategoryText")));
                //this is spinner subtosubcategory selected text & index

                // sp_subtosubcategory.setSelection(Integer.parseInt(jsonAlbumDetail.getString("Fk_SubTosubcategoryID")) - 1);


                //  sp_subtosubcategory.setSelection(getIndexforsubtosubcategory(subtosubcategoryListupdate, subtosubcategoryselectedtext));

                //sp_subtosubcategory.setSelection(getIndexforsubtosubcategory(subtosubcategoryList, jsonAlbumDetail.getString("SubtosubcategoryText")));


                if (!jsonAlbumDetail.getString( "cost_of_project_type" ).isEmpty()) {
                    et_currency.setSelection( Integer.parseInt( jsonAlbumDetail.getString( "cost_of_project_type" ) ) - 1 );
                } else {
                    et_currency.setSelection( 0 );
                }

                sp_timeType.setSelection( timeType.indexOf( jsonAlbumDetail.getString( "working_hour_type" ) ) );
                et_attendacne.setText( jsonAlbumDetail.getString( "Attendance" ) );
                et_attendance_per.setText( jsonAlbumDetail.getString( "AttendancePer" ) );
                int pos = Integer.parseInt( jsonAlbumDetail.getString( "MeetingType" ) );
                sp_meeting.setSelection( pos );

                if (pos == 0) {
                    isFirstLoad++;
                }
                if (Integer.parseInt( jsonAlbumDetail.getString( "MeetingType" ) ) == 0 || Integer.parseInt( jsonAlbumDetail.getString( "MeetingType" ) ) == 3) {
                    ll_attach.setVisibility( View.GONE );
                } else {
                    ll_attach.setVisibility( View.VISIBLE );
                    String tlt = tv_title.getText().toString();
                    if(tlt == null)
                    {
                        tlt = "";
                    }
                    Log.d("tittleofedit",tlt);

                    if (tlt.equalsIgnoreCase("Edit Service Project")) {
                        ll_attach.setVisibility(View.GONE);
                       // Toast.makeText(getApplicationContext(),"jjj"+tlt,Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                       // ll_attach.setVisibility(View.GONE);
                      //  Toast.makeText(getApplicationContext(),"not"+tlt,Toast.LENGTH_LONG).show();
                    }




                }
                String agendaDoc = jsonAlbumDetail.getString( "AgendaDoc" );
                String momDoc = jsonAlbumDetail.getString( "MOMDoc" );

                if (!agendaDoc.isEmpty()) {
                    ll_agenda_file.setVisibility( View.VISIBLE );
                    ll_attach_agenda.setVisibility( View.GONE );
                    txt_fileName.setText( URLUtil.guessFileName( agendaDoc, null, null ) );
                } else {
                    ll_agenda_file.setVisibility( View.GONE );
                    ll_attach_agenda.setVisibility( View.VISIBLE );
                    txt_fileName.setText( "" );
                }

                if (!momDoc.isEmpty()) {
                    ll_mom_file.setVisibility( View.VISIBLE );
                    ll_attach_mom.setVisibility( View.GONE );
                    txt_fileName_mom.setText( URLUtil.guessFileName( momDoc, null, null ) );
                } else {
                    ll_mom_file.setVisibility( View.GONE );
                    ll_attach_mom.setVisibility( View.VISIBLE );
                    txt_fileName_mom.setText( "" );
                }

                agenda_file_id = jsonAlbumDetail.getString( "AgendaDocID" );
                mom_file_id = jsonAlbumDetail.getString( "MOMDocID" );


                et_categoryName.setText( jsonAlbumDetail.getString( "othercategorytext" ) );
                et_subcategoryName.setText( jsonAlbumDetail.getString( "OtherSubCategory" ) );

           // add by prashant sahani-----------------------------------

                String ontm_or_rept = jsonAlbumDetail.getString( "OnetimeOrOngoing" );//NewOrExisting
               Log.d("OnetimeOrOngoing",ontm_or_rept);
                String NewOrExisting = jsonAlbumDetail.getString( "NewOrExisting" );//NewOrExisting//JoinedOrNot


                String JoinedOrNot = jsonAlbumDetail.getString( "JoinedOrNot");
                String ClubRole = jsonAlbumDetail.getString( "ClubRole");
                String TtlOfNewOngoingProj = jsonAlbumDetail.getString( "TtlOfNewOngoingProj");

                TtlOfNew = jsonAlbumDetail.getString( "NewOrExisting" );



                String FK_SelectExistingProject = jsonAlbumDetail.getString( "FK_SelectExistingProject");



//                String ontime = "0";
//                String ontimeyes = "0" ;
//                String ong_clubrole = "0";
//                String crt_newyes = "0";
//                String ong_neww = "0" ;




                if(ontm_or_rept.equalsIgnoreCase("0"))
                {
                    //hide
                 //   ont_ong_selct.setVisibility(View.GONE);
                }
                else if(ontm_or_rept.equalsIgnoreCase("1") || ontm_or_rept.equalsIgnoreCase("2")  ){
                    // show
                   // ont_ong_selct.setVisibility(View.VISIBLE);

                    if(ontm_or_rept.equalsIgnoreCase("1"))//  onetime
                    {
                        ontime = "1";
                        radioMale.setChecked(true);
                        radioFemale.setChecked(false);

                        if(JoinedOrNot.equalsIgnoreCase("1")){
                            crt_newyes= "2";

                            radioMale1.setChecked(true);
                            radioFemale1.setChecked(false);

                        //    radioMaleong2,radioFemaleong2;

                            if(ClubRole.equalsIgnoreCase("1"))
                            {

                                ong_clubrole = "1";
                                radioMale2.setChecked(true);
                                radioFemale2.setChecked(false);
                                cohost.setVisibility(View.GONE);
                            }
                            else  if(ClubRole.equalsIgnoreCase("2"))
                            {

                                ong_clubrole = "2";
                                radioMale2.setChecked(false);
                                radioFemale2.setChecked(true);

                                cohost.setVisibility(View.VISIBLE);
                            }

                         }
                        else  if(JoinedOrNot.equalsIgnoreCase("2")){
                            crt_newyes= "2";

                            radioMale1.setChecked(false);
                            radioFemale1.setChecked(true);

                        }


                    }
                    else if((ontm_or_rept.equalsIgnoreCase("2")))// ongoing
                    {
                        ontime = "2";
                        radioFemale.setChecked(true);
                         if(NewOrExisting.equalsIgnoreCase("1"))
                         {

                             ong_neww="1";
                             radioMaleong.setChecked(true);
                             radioFemaleong.setChecked(false);

                             if(TtlOfNewOngoingProj == null)
                             {

                             }
                             else
                             {
                                 et_newong.setText(TtlOfNewOngoingProj);
                             }


                              if(JoinedOrNot.equalsIgnoreCase("1")){
                                  crt_newyes ="1";

                                  radioMaleongnew.setChecked(true);
                                  radioFemaleongnew.setChecked(false);


                                  if(ClubRole.equalsIgnoreCase("1")) {

                                      ong_clubrole = "1";
                                      radioMaleong2.setChecked(true);
                                      radioFemaleong2.setChecked(false);
                                      cohost2.setVisibility(View.GONE);
                                  }
                                  else   if(ClubRole.equalsIgnoreCase("2")) {
                                      ong_clubrole = "2";

                                      radioMaleong2.setChecked(false);
                                      radioFemaleong2.setChecked(true);
                                      cohost2.setVisibility(View.VISIBLE);

                                  }

                              }
                              else if(JoinedOrNot.equalsIgnoreCase("2")){
                                  crt_newyes = "2";
                                  radioMaleongnew.setChecked(false);
                                  radioFemaleongnew.setChecked(true);

                              }




                         }
                         else if(NewOrExisting.equalsIgnoreCase("2"))
                         {
                             ong_neww="2";
                             radioFemaleong.setChecked(true);
                             radioMaleong.setChecked(false);
                            // Toast.makeText(getApplicationContext(),"tttt",Toast.LENGTH_LONG).show();

                             try
                             {

                                String fkid = (FK_SelectExistingProject);
                                 getong_pro(fkid);
                              //   Toast.makeText(getApplicationContext(),fkselect,Toast.LENGTH_LONG).show();

                             //    ong_p.setSelection(4);

                             }catch (Exception e)
                             {

                                 Log.d("selectionisues",e.getMessage());
                              //   Toast.makeText(getApplicationContext(),e.printStackTrace(),Toast.LENGTH_LONG).show();
                             }


                          //   ong_p.setSelection(1);

                            // getong_pro();
                             String fk_id = FK_SelectExistingProject;
                             if(fk_id == null)
                             {

                             }
                             else {

//                                 String sd = D_id1.get(FK_SelectExistingProject);
//
//                                 Log.d("gallaryid",sd);
//                                 Toast.makeText(getApplicationContext(),"tttt"+sd,Toast.LENGTH_LONG).show();
//                               //  ong_p.setSelection(Integer.parseInt(sd));
//                                 ong_p.setSelection(3);
                             }


                             if(JoinedOrNot.equalsIgnoreCase("1")){

                                 crt_newyes ="1";

                                 radioMaleongnew.setChecked(true);
                                 radioFemaleongnew.setChecked(false);

                              if(ClubRole.equalsIgnoreCase("1")) {

                                  ong_clubrole = "1";
                                  radioMaleong2.setChecked(true);
                                  radioFemaleong2.setChecked(false);
                                  cohost2.setVisibility(View.GONE);
                              }
                               else   if(ClubRole.equalsIgnoreCase("2")) {
                                  ong_clubrole = "2";
                                  radioMaleong2.setChecked(false);
                                  radioFemaleong2.setChecked(true);
                                  cohost2.setVisibility(View.VISIBLE);

                                  }

                             }
                             else if(JoinedOrNot.equalsIgnoreCase("2")){
                                 crt_newyes ="2";
                                 radioMaleongnew.setChecked(false);
                                 radioFemaleongnew.setChecked(true);


//                                 radioMaleong2.setChecked(false);
//                                 radioFemaleong2.setChecked(true);


                                 cohost2.setVisibility(View.GONE);
                             }


                         }


                    }




                }







          //--------------------------

            }

        } catch (Exception e) {
            Log.d( "execssssss", "Exception :- " + e.toString() );
            Toast.makeText( EditAlbumActivity.this, "Something went wrong", Toast.LENGTH_SHORT ).show();
            e.printStackTrace();
        }
    }

    public void getValidateDirectBeneficiariesDetails(String result) {
        try {
            JSONObject jsonObj = new JSONObject( result );

            JSONObject jsonTBDirectBeneficiariesResult = jsonObj.getJSONObject( "TBDirectBeneficiariesResult" );
            final String status = jsonTBDirectBeneficiariesResult.getString( "status" );

            if (status.equals( "0" )) {

                MaxNumber = jsonTBDirectBeneficiariesResult.getString( "MaxNumber" );
                Approvedbeneficiary = jsonTBDirectBeneficiariesResult.getString( "Approvedbeneficiary" );
                ApprovedFlag = jsonTBDirectBeneficiariesResult.getString( "ApprovedFlag" );

            }

        } catch (Exception e) {
            Log.d( "exec", "Exception :- " + e.toString() );
            Toast.makeText( EditAlbumActivity.this, "Something went wrong", Toast.LENGTH_SHORT ).show();
            e.printStackTrace();
        }
    }

    private int getIndex(ArrayList<AlbumData> list, String myString) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get( i ).getDistrict_Name().toString().equalsIgnoreCase( myString )) {
                return i;
            }
        }
        return 0;
    }

    private int getIndexFunding(ArrayList<FundingData> list, String myString) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get( i ).getFund_Name().toString().equalsIgnoreCase( myString )) {
                return i;
            }
        }
        return 0;
    }

    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        String isSubGroupAdmin = "0";  // means no
        String isAdmin = PreferenceManager.getPreference( EditAlbumActivity.this, PreferenceManager.IS_GRP_ADMIN, "No" );
        if (isAdmin.equalsIgnoreCase( "partial" )) {
            isSubGroupAdmin = "1";  // means yes
        }


        arrayList.add( new BasicNameValuePair( "isSubGrpAdmin", isSubGroupAdmin ) );
        arrayList.add( new BasicNameValuePair( "albumId", albumId ) );
        arrayList.add( new BasicNameValuePair( "groupId", PreferenceManager.getPreference( getApplicationContext(), PreferenceManager.GROUP_ID ) ) );

        if(serviceproject.equalsIgnoreCase("0"))
        {



            String tlt = tv_title.getText().toString();
            if(tlt == null)
            {
                tlt = "";
            }
            Log.d("tittleofedit",tlt);

            if (!tlt.equalsIgnoreCase("Edit Service Project")) {
                arrayList.add( new BasicNameValuePair("moduleId","8" ));
            }
            else if (tlt.equalsIgnoreCase("Edit Service Project")) {

                arrayList.add( new BasicNameValuePair("moduleId","52" ));
            }

        }
        else
        {
            String tlt = tv_title.getText().toString();
            if(tlt == null)
            {
                tlt = "";
            }
            Log.d("tittleofedit",tlt);

            if (!tlt.equalsIgnoreCase("Edit Service Project")) {
                arrayList.add( new BasicNameValuePair("moduleId","8" ));
            }
            else if (tlt.equalsIgnoreCase("Edit Service Project")) {

                arrayList.add( new BasicNameValuePair("moduleId","52" ));
            }
        }

       // arrayList.add( new BasicNameValuePair( "moduleId", PreferenceManager.getPreference( getApplicationContext(), PreferenceManager.MODULE_ID ) ) );
        arrayList.add( new BasicNameValuePair( "type", galleryType ) );
        arrayList.add( new BasicNameValuePair( "memberIds", inputids ) );
        arrayList.add( new BasicNameValuePair( "albumTitle", edt_title.getText().toString() ) );
        arrayList.add( new BasicNameValuePair( "albumDescription", edt_description.getText().toString().trim() ) );
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

            arrayList.add( new BasicNameValuePair( "Attendance", et_attendacne.getText().toString() ) );
            arrayList.add( new BasicNameValuePair( "AttendancePer", et_attendance_per.getText().toString() ) );
            int pos = sp_meeting.getSelectedItemPosition();

            arrayList.add( new BasicNameValuePair( "MeetingType", "" + pos ) );
            if (pos == 0||pos==3) {
                arrayList.add( new BasicNameValuePair( "AgendaDocID", "0" ) );
                arrayList.add( new BasicNameValuePair( "MOMDocID", "0" ) );
            } else {
                arrayList.add( new BasicNameValuePair( "AgendaDocID", agenda_file_id ) );
                arrayList.add( new BasicNameValuePair( "MOMDocID", mom_file_id ) );
            }


        } else if (rbPublic.isChecked()) {
            shareType = "1";
            arrayList.add( new BasicNameValuePair( "shareType", shareType ) );

            arrayList.add( new BasicNameValuePair( "costofproject", et_COP.getText().toString() ) );
            //New parameters Added By Gaurav

            arrayList.add( new BasicNameValuePair( "beneficiary", et_Beneficiary.getText().toString().trim() )); //"beneficiary",
            arrayList.add( new BasicNameValuePair( "TempBeneficiary", temp_beneficiary ) );
            arrayList.add( new BasicNameValuePair( "TempBeneficiary_flag", temp_beneficiary_flag ) );

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


                arrayList.add( new BasicNameValuePair( "Fk_SubTosubcategoryID", subtosubcategoryID ) );
                arrayList.add( new BasicNameValuePair( "OtherSubCategory", "" ) );

            } else {
                //when other option enable on subcategory spinner
                arrayList.add( new BasicNameValuePair( "Fk_SubTosubcategoryID", "0" ) );
                arrayList.add( new BasicNameValuePair( "OtherSubCategory", et_subcategoryName.getText().toString() ) );

            }

            arrayList.add( new BasicNameValuePair( "Rotaractors", et_rotractors.getText().toString() ) );
            arrayList.add( new BasicNameValuePair( "Ismedia", Ismedia ) );
            // arrayList.add(new BasicNameValuePair("Mediaphoto", mediaPhotoPath));

            arrayList.add( new BasicNameValuePair( "MediaphotoID", MediaphotoID ) );
            if (Ismedia.equals( "1" )) {
                arrayList.add( new BasicNameValuePair( "MediaDesc", et_album_photo1_auth.getText().toString() ) );

            }


            //closed the parameters
            arrayList.add( new BasicNameValuePair( "manhourspent", et_manPower.getText().toString() ) );
            arrayList.add( new BasicNameValuePair( "categoryId", categoryID ) );
            arrayList.add( new BasicNameValuePair( "Fk_Funding_Id", fundingID ) );
            arrayList.add( new BasicNameValuePair( "manhourspenttype", "Hours" ) );
            arrayList.add( new BasicNameValuePair( "NumberofRotarian", et_noOfRotarians.getText().toString() ) );
            // arrayList.add(new BasicNameValuePair("OtherCategorytext", et_categoryName.getText().toString()));
            arrayList.add( new BasicNameValuePair( "costofprojecttype", (et_currency.getSelectedItemPosition() + 1) + "" ) );
            arrayList.add( new BasicNameValuePair( "Attendance", "0" ) );
            arrayList.add( new BasicNameValuePair( "AttendancePer", "0" ) );
            arrayList.add( new BasicNameValuePair( "MeetingType", "0" ) );
            arrayList.add( new BasicNameValuePair( "AgendaDocID", "0" ) );
            arrayList.add( new BasicNameValuePair( "MOMDocID", "0" ) );


//            arrayList.add( new BasicNameValuePair( "OnetimeOrOngoing", "" ) );
//            arrayList.add( new BasicNameValuePair( "NewOrExisting", "" ) );
//            arrayList.add( new BasicNameValuePair( "JoinedOrNot", "" ) );
//            arrayList.add( new BasicNameValuePair( "ClubRole", "" ) );
//            arrayList.add( new BasicNameValuePair( "TtlOfNewOngoingProj", "" ) );
//            arrayList.add( new BasicNameValuePair( "ddl_selectProject", "" ) );

            // add by prashant sahanii

           // String t_new = et_COPf.getText().toString().trim();
//            if(ont_ong_selct.getVisibility() == View.GONE)
//            {
//                ontime= "0";
//                ong_neww= "0";
//                crt_newyes = "0";
//                ong_clubrole = "0";
//                t_new = "";
//            }


            // added by prashant sahani

            String sel_item = String.valueOf(ong_p.getSelectedItem());
            String slt_id = D_id.get(sel_item);

            String t_new = et_COPf.getText().toString().trim();
            if(ontime.equalsIgnoreCase("1"))
            {
                ong_neww = "0";
                t_new = "";
                slt_id = "";

                if(crt_newyes.equalsIgnoreCase("2"))
                {
                    ong_clubrole = "0";
                }

            }
            if(crt_newyes.equalsIgnoreCase("2"))
            {
                ong_clubrole = "0";
            }

            if(ong_neww.equalsIgnoreCase("1"))
            {
                slt_id = "";
            }

            if(ong_neww.equalsIgnoreCase("2"))
            {
                t_new = "";
            }

            arrayList.add( new BasicNameValuePair( "OnetimeOrOngoing", ontime ) );//
            arrayList.add( new BasicNameValuePair( "NewOrExisting", ong_neww ) );
            arrayList.add( new BasicNameValuePair( "JoinedOrNot", crt_newyes ) );//
            arrayList.add( new BasicNameValuePair( "ClubRole", ong_clubrole ) );


            try {
                arrayList.add( new BasicNameValuePair( "TtlOfNewOngoingProj", t_new ) );

            }catch (Exception e)
            {
                arrayList.add( new BasicNameValuePair( "TtlOfNewOngoingProj", "" ) );
            }
//            String sel_item = String.valueOf(ong_p.getSelectedItem());
//            String slt_id = D_id.get(sel_item);

            try {

                arrayList.add( new BasicNameValuePair( "ddl_selectProject", slt_id) );
            }catch (Exception e)
            {
                arrayList.add( new BasicNameValuePair( "ddl_selectProject", "" ) );
            }






        }

        Log.d( "requestpara", "PARAMETERS " + Constant.AddUpdateAlbum_New + " :- " + arrayList.toString() );
        new EditAlbumAsyncTask( Constant.AddUpdateAlbum_New, arrayList, EditAlbumActivity.this ).execute();
    }


    public class EditAlbumAsyncTask extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog( EditAlbumActivity.this, R.style.TBProgressBar );

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
            progressDialog.setCancelable( false );
            progressDialog.setProgressStyle( android.R.style.Widget_ProgressBar_Small );
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(String... params) {

            try {

                val = HttpConnection.postData( url, argList );
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
                getresult( result.toString() );
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
                //   String TempBeneficiary_flag_mail = ActivityResult.getString("TempBeneficiary_flag");


                Intent intent = new Intent();
                intent.putExtra( "resultForEditAlbum", val );
                setResult( 1, intent );

                albumId = ActivityResult.getString( "galleryid" );


                //When Media Photo is off

                if (iv_image.getDrawable() != null) {
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

                getAlbumImagesData();


                //This code is added for Images share on Maill during benificiary approve added by Gaurav

              /*  if(TempBeneficiary_flag_mail.equalsIgnoreCase("1")&& msg.equalsIgnoreCase("success")){
                    //Garry

                    galleryid_mail = ActivityResult.getString("galleryid");
                    ClubName_mail = ActivityResult.getString("ClubName");
                    districtNumber_mail = ActivityResult.getString("districtNumber");
                    TempBeneficiary_mail = ActivityResult.getString("TempBeneficiary");
                    president_mail = ActivityResult.getString("president");
                    secretary_mail = ActivityResult.getString("secretary");
                    presidentEmailIds_mail = ActivityResult.getString("presidentEmailIds");
                    secretaryEmailIds_mail = ActivityResult.getString("secretaryEmailIds");

                    sendMailBeneficiaries();


                }
*/

                // finish();

//                finish();//finishing activity
//
//                if (tv_add.getText().equals("Done")) {
//                    Toast.makeText(EditAlbumActivity.this, "Album Updated successfully.", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(EditAlbumActivity.this, "Album Updated successfully.", Toast.LENGTH_SHORT).show();
//                }

                Log.d( "Touchbase", "*************** " + status );
                Log.d( "Touchbase", "*************** " + msg );

            } else {
                Toast.makeText( EditAlbumActivity.this, "Failed to update " + header + ". Please retry.", Toast.LENGTH_LONG ).show();
            }

        } catch (Exception e) {
            Log.d( "exec", "Exception :- " + e.toString() );
        }

    }


    private void sendMailBeneficiaries() {

        try {

            progressDialog = new ProgressDialog( EditAlbumActivity.this, R.style.TBProgressBar );
            progressDialog.setCancelable( false );
            progressDialog.setProgressStyle( android.R.style.Widget_ProgressBar_Small );
            progressDialog.show();

            JSONObject requestData = new JSONObject();
            //Golu
            requestData.put( "AlbumID", galleryid_mail );
            requestData.put( "ClubName", ClubName_mail );
            requestData.put( "districtNumber", districtNumber_mail );
            requestData.put( "TempBeneficiary", TempBeneficiary_mail );
            requestData.put( "president", president_mail );
            requestData.put( "secretary", secretary_mail );
            requestData.put( "presidentEmailIds", presidentEmailIds_mail );
            requestData.put( "secretaryEmailIds", secretaryEmailIds_mail );
            //  requestData.put("ClubName", clubname);

            Log.d( "Response", "PARAMETERS " + Constant.SendMailBeneficiaries + " :- " + requestData.toString() );

            JsonObjectRequest request = new JsonObjectRequest( Request.Method.POST, Constant.SendMailBeneficiaries, requestData, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    JSONObject result;
                    //setAllShowcaseDetails(response);
                    Log.d( "SendMailBeneficiaries", response.toString() );

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

            request.setRetryPolicy( new DefaultRetryPolicy( 120000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ) );

            AppController.getInstance().addToRequestQueue( context, request );

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getAlbumImagesData() {

        ArrayList<UploadPhotoData> uploadPhotoDataArrayList = new ArrayList<>();

        for (int i = 0; i < imageList.size(); i++) {

            String photoID = photoIdList.get( i );

               /* if(photoID.equalsIgnoreCase("0")){

                    if(!imageList.get(i).isEmpty()){
                        data = new UploadPhotoData(photoIdList.get(i).toString(), imageList.get(i).toString(), descList.get(i).toString(), albumId, groupId, createdBy, "0");
                        long id = addPhotoModel.insert(data);
                    }

                } else {
                    data = new UploadPhotoData(photoIdList.get(i).toString(), imageList.get(i).toString(), descList.get(i).toString(), albumId, groupId, createdBy, "0");
                    long id = addPhotoModel.insert(data);
                }*/

            if (photoID.equalsIgnoreCase( "0" )) {

                if (!imageList.get( i ).isEmpty()) {
                    data = new UploadPhotoData( photoIdList.get( i ), imageList.get( i ), descList.get( i ), albumId, groupId, createdBy, "0" );
                    uploadPhotoDataArrayList.add( data );
                }

            } else {
                data = new UploadPhotoData( photoIdList.get( i ), imageList.get( i ), descList.get( i ), albumId, groupId, createdBy, "0" );
                uploadPhotoDataArrayList.add( data );
            }

        }

        if (uploadPhotoDataArrayList.size() == 0) {
            Intent intent1 = new Intent();
            setResult( Activity.RESULT_OK, intent1 );
            finish();
            if (header.equals( "Club Meeting" )) {

                Toast.makeText( EditAlbumActivity.this, "Club Meeting updated successfully.", Toast.LENGTH_SHORT ).show();

            } else {
                Toast.makeText( EditAlbumActivity.this, "Service Project updated successfully.", Toast.LENGTH_SHORT ).show();

            }
        } else {
            new UploadPhotoAsyncTask( uploadPhotoDataArrayList ).execute();
        }


       /* Intent intent1 = new Intent();
        setResult(Activity.RESULT_OK, intent1);
        finish();
        Toast.makeText(EditAlbumActivity.this, "Activity updated successfully.", Toast.LENGTH_SHORT).show();

        Log.d("UploadPhotoService", "UploadPhotoService is Called");
        Intent intent = new Intent(EditAlbumActivity.this, UploadPhotoService.class);
        startService(intent);*/

    }


    public class UploadPhotoAsyncTask extends AsyncTask<String, Object, Object> {

        ArrayList<UploadPhotoData> allImagesList;

        final ProgressDialog progressDialog = new ProgressDialog( EditAlbumActivity.this, R.style.TBProgressBar );

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

                    url = url + "?photoId=" + uploadPhotoData.getPhotoId() + "&desc=" + URLEncoder.encode( uploadPhotoData.getDescription(), "UTF-8" ) + "&albumId=" + uploadPhotoData.getAlbumId() + "&groupId=" + uploadPhotoData.getGroupd() + "&createdBy=" + uploadPhotoData.getCreatedBy() + "&Financeyear=" + Gallery.year;

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                val = UploadPhotoToServer( new File( uploadPhotoData.getPhotoPath() ), url );
                System.out.println( "new value11----" + val );

                Log.d( "UploadPhotoServicetest", "UploadPhotoService file path =>" + uploadPhotoData.getPhotoPath() +"result -- "+val);

                Log.e( "UploadPhotoServicetest", "url=>" + url + "\nresult=>" + val );
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {

            super.onPostExecute( result );

            if (!EditAlbumActivity.this.isFinishing()) {
                progressDialog.dismiss();
            }

            Intent intent1 = new Intent();
            setResult( Activity.RESULT_OK, intent1 );
            if (header.equals( "Club Meeting" )) {

                Toast.makeText( EditAlbumActivity.this, "Club Meeting updated successfully.", Toast.LENGTH_SHORT ).show();

            } else {
                Toast.makeText( EditAlbumActivity.this, "Service Project updated successfully.", Toast.LENGTH_SHORT ).show();

            }

            finish();
        }
    }

    public String UploadPhotoToServer(File file_path, String url) {

        String isUploaded = "";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy( policy );
        Log.d( "Uri", "Do file path" +url +"////"+ file_path );

        try {

            HttpClient client = new DefaultHttpClient();
            //use your server path of php file
            org.apache.http.entity.mime.MultipartEntity reqEntity = new org.apache.http.entity.mime.MultipartEntity();
            HttpPost post = new HttpPost( url );
            if (file_path.exists()) {
                FileBody bin1 = new FileBody( file_path );
                //  Log.d("Enter", "Filebody complete " + bin1);
                reqEntity.addPart( "uploaded_file", bin1 );
                //reqEntity.addPart("email", new StringBody(useremail));
            }
            post.setEntity( reqEntity );
            //  Log.d("Enter", "Image send complete");

            HttpResponse response = client.execute( post );
            Log.i("LoadImageResultresponse", String.valueOf(response));

            HttpEntity resEntity = response.getEntity();
            //  Log.d("Enter", "Get Response");
            try {

                final String response_str = EntityUtils.toString( resEntity );
                JSONObject jsonObj = new JSONObject( response_str );
                JSONObject ActivityResult = jsonObj.getJSONObject( "LoadImageResult" );

                Log.i("LoadImageResult", String.valueOf(ActivityResult));

                final String status = ActivityResult.getString( "status" );
                if (status.equals( "0" )) {
                    isUploaded = ActivityResult.getString( "message" );
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

    private void getCategoryList() {

        try {
//            progressDialog=new ProgressDialog(AddAlbum.this,R.style.TBProgressBar);
//            progressDialog.setCancelable(false);
//            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//            progressDialog.show();

            final ProgressDialog progressDialog = new ProgressDialog( EditAlbumActivity.this, R.style.TBProgressBar );
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
                    loadFromServer();


                    // getAlbumPhotos();
                    Utils.log( response.toString() );
                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    if(progressDialog!=null){
//                        progressDialog.dismiss();
//                    }
                    progressDialog.dismiss();
                    Utils.log( "VollyError:- " + error.toString() );
                    //showErrorDialog();
                    //Utils.showMsg(context, "Something went wrong");
                    Toast.makeText( getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG ).show();
                }
            } );

            request.setRetryPolicy(
                    new DefaultRetryPolicy( 120000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ) );

            AppController.getInstance().addToRequestQueue( this, request );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getAlbumPhotos() {
        try {
            JSONObject requestData = new JSONObject();
            requestData.put( "albumId", albumId );
            requestData.put( "groupId", groupId );
            requestData.put( "Financeyear",  Gallery.year );

            Log.d( "Response", "PARAMETERS " + Constant.GetAlbumPhotoList_New + " :- " + requestData.toString() );
            JsonObjectRequest request = new JsonObjectRequest( Request.Method.POST, Constant.GetAlbumPhotoList_New, requestData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    JSONObject result;
                    //globalResponse=response;
                    getPhotos( response );
                    //loadRssBlogs();
                    Utils.log( response.toString() );
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.log( "VollyError:- " + error.toString() );
                    //Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_LONG).show();
                }
            } );

            request.setRetryPolicy(
                    new DefaultRetryPolicy( 120000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ) );

            AppController.getInstance().addToRequestQueue( EditAlbumActivity.this, request );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getPhotos(JSONObject response) {
        try {

            JSONObject jsonTBAlbumPhotoListResult = response.getJSONObject( "TBAlbumPhotoListResult" );
            final String status = jsonTBAlbumPhotoListResult.getString( "status" );

            if (status.equals( "0" )) {

                // updatedOn = jsonTBAlbumPhotoListResult.getString("updatedOn");


                //JSONObject jsonResult = jsonTBAlbumPhotoListResult.getJSONObject("Result");

                JSONArray jsonNewAlbumPhotoList = jsonTBAlbumPhotoListResult.getJSONArray( "Result" );

                int newAlbumPhotoListCount = jsonNewAlbumPhotoList.length();
                Thread.sleep( 1000 );


                //Media is OFF

                for (int i = 0; i < newAlbumPhotoListCount; i++) {

                    //  AlbumPhotoData data = new AlbumPhotoData();

                    JSONObject result_object = jsonNewAlbumPhotoList.getJSONObject( i );


                    if (i == 0) {
                        close1.setVisibility( View.VISIBLE );
                        photoId1 = result_object.getString( "photoId" ).toString();

                        photoIdList.remove( 0 );
                        photoIdList.add( 0, photoId1 );


                        Picasso.with( EditAlbumActivity.this ).load( result_object.getString( "url" ).toString() )
                                //.fit()
                                //.resize(200, 200)
                                .placeholder( R.drawable.placeholder_new )
                                .into( iv_image );
                        iv_image.setBackground( null );
                        et_coverPhoto.setText( result_object.getString( "description" ).toString() );
                        imgFlag = 1;

                    } else if (i == 1) {
                        close2.setVisibility( View.VISIBLE );
                        photoId2 = result_object.getString( "photoId" ).toString();

                        photoIdList.remove( 1 );
                        photoIdList.add( 1, photoId2 );


                        Picasso.with( EditAlbumActivity.this ).load( result_object.getString( "url" ).toString() )
                                //.fit()
                                //.resize(200, 200)
                                .placeholder( R.drawable.placeholder_new )
                                .into( iv_album_photo );
                        iv_album_photo.setBackground( null );
                        et_album_photo1.setText( result_object.getString( "description" ).toString() );
                    } else if (i == 2) {
                        close3.setVisibility( View.VISIBLE );
                        photoId3 = result_object.getString( "photoId" ).toString();

                        photoIdList.remove( 2 );
                        photoIdList.add( 2, photoId3 );


                        Picasso.with( EditAlbumActivity.this ).load( result_object.getString( "url" ).toString() )
                                //.fit()
                                //.resize(200, 200)
                                .placeholder( R.drawable.placeholder_new )
                                .into( iv_album_photo2 );
                        iv_album_photo2.setBackground( null );
                        et_album_photo2.setText( result_object.getString( "description" ).toString() );
                    } else if (i == 3) {
                        close4.setVisibility( View.VISIBLE );
                        photoId4 = result_object.getString( "photoId" ).toString();
                        photoIdList.remove( 3 );
                        photoIdList.add( 3, photoId4 );
                        Picasso.with( EditAlbumActivity.this ).load( result_object.getString( "url" ).toString() )
                                //.fit()
                                //.resize(200, 200)
                                .placeholder( R.drawable.placeholder_new )
                                .into( iv_album_photo3 );
                        iv_album_photo3.setBackground( null );
                        et_album_photo3.setText( result_object.getString( "description" ).toString() );
                    } else if (i == 4) {
                        close5.setVisibility( View.VISIBLE );
                        photoId5 = result_object.getString( "photoId" ).toString();
                        photoIdList.remove( 4 );
                        photoIdList.add( 4, photoId5 );
                        Picasso.with( EditAlbumActivity.this ).load( result_object.getString( "url" ).toString() )
                                //.fit()
                                //.resize(200, 200)
                                .placeholder( R.drawable.placeholder_new )
                                .into( iv_album_photo4 );
                        iv_album_photo4.setBackground( null );
                        et_album_photo4.setText( result_object.getString( "description" ).toString() );
                    }
                }

            }


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


                for (int i = 0; i < Categories.length(); i++) {
                    JSONObject categoryObj = Categories.getJSONObject( i );
                    AlbumData data = new AlbumData();
                    data.setDistrict_id( categoryObj.getString( "ID" ) );
                    data.setDistrict_Name( categoryObj.getString( "Name" ) );
                    categoryList.add( i, data );
                }
                // categoryList.remove(0);
                adapter = new SpinnerAdapter( this, categoryList );
                sp_category.setAdapter( adapter );



                //Add this for subcategory data for Gaurav
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


                //progressDialog.dismiss();
            } else {
                //progressDialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //progressDialog.dismiss();
        }

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
                if (listaddsubgrp.size() == 0)
                    Log.d( "Touchnase", "@@@ " + listaddsubgrp );
                String result = "";
                int count = 0;

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
                    iv_image.setImageBitmap( bt );
                    iv_image.setBackground( null );

                    String path = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    // f.delete();
                    OutputStream outFile = null;
                    File file = new File( path, String.valueOf( System.currentTimeMillis() ) + ".jpg" );

                    Log.d( "TOUCHBASE", "FILE PATH " + f.toString() );
                    ///-------------------------------------------------------------------
                    pd = ProgressDialog.show( EditAlbumActivity.this, "", "Loading...", false );
                    final File finalF = f;
                    Thread thread = new Thread( new Runnable() {
                        public void run() {
                            imgFlag = 1;
                            uploadedimgid = Utils.doFileUpload( new File( finalF.toString() ), "gallery" ); // Upload File to server
                            imageList.remove( 0 );
                            imageList.add( 0, finalF.toString() );
                            runOnUiThread( new Runnable() {
                                public void run() {
                                    if (pd.isShowing())
                                        pd.dismiss();
                                    Log.d( "TOUCHBASE", "FILE UPLOAD ID InnerThread  " + uploadedimgid );
                                    if (uploadedimgid.equals( "0" )) {
                                        Toast.makeText( EditAlbumActivity.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT ).show();
                                        //iv_image.setImageResource(R.drawable.edit_image);
                                        iv_image.setBackground( getResources().getDrawable( R.drawable.asset ) );
                                        imgFlag = 0;
                                    }
                                    ll_photos.setVisibility( View.VISIBLE );
                                    //Toast.makeText(Register.this, "Verify your account through the registered email id", Toast.LENGTH_LONG).show();
                                }
                            } );
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
                Utils.log( imageList.toString() );
                ///-------------------------------------------------------------------
                pd = ProgressDialog.show( EditAlbumActivity.this, "", "Loading...", false );
                Thread thread = new Thread( new Runnable() {
                    public void run() {
                        uploadedimgid = Utils.doFileUpload( new File( finalImagePath ), "gallery" ); // Upload File to server
                        runOnUiThread( new Runnable() {
                            public void run() {
                                if (pd.isShowing())
                                    pd.dismiss();
                                if (uploadedimgid.equals( "0" )) {
                                    Toast.makeText( EditAlbumActivity.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT ).show();
                                    //iv_image.setImageResource(R.drawable.edit_image);
                                    iv_image.setBackground( getResources().getDrawable( R.drawable.asset ) );
                                } else {
                                    close1.setVisibility( View.VISIBLE );
                                    iv_image.setImageBitmap( thumbnail );
                                    iv_image.setBackground( null );
                                    imgFlag = 1;
                                    flag = 1;
                                    ll_photos.setVisibility( View.VISIBLE );
                                }
                                //Toast.makeText(Register.this, "Verify your account through the registered email id", Toast.LENGTH_LONG).show();
                            }
                        } );
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

                Log.e( "TOUCHBASE", "UploadPhotoService FILE PATH " + picturePath + " final path=" + finalImagePath );

                //imageList.add(picturePath.toString());

                if (flag == 1) {
                    close1.setVisibility( View.VISIBLE );
                    imageList.remove( 0 );
                    imageList.add( 0, finalImagePath );
                    iv_image.setImageBitmap( thumbnail );
                    iv_image.setBackground( null );
                    ll_photos.setVisibility( View.VISIBLE );
                } else if (flag == 2) {
                    close2.setVisibility( View.VISIBLE );
                    imageList.remove( 1 );
                    imageList.add( 1, finalImagePath.toString() );
                    iv_album_photo.setImageBitmap( thumbnail );
                    iv_album_photo.setBackground( null );
                } else if (flag == 3) {
                    close3.setVisibility( View.VISIBLE );
                    imageList.remove( 2 );
                    imageList.add( 2, finalImagePath.toString() );
                    iv_album_photo2.setImageBitmap( thumbnail );
                    iv_album_photo2.setBackground( null );
                } else if (flag == 4) {
                    close4.setVisibility( View.VISIBLE );
                    imageList.remove( 3 );
                    imageList.add( 3, finalImagePath.toString() );
                    iv_album_photo3.setImageBitmap( thumbnail );
                    iv_album_photo3.setBackground( null );
                } else if (flag == 5) {
                    close5.setVisibility( View.VISIBLE );
                    imageList.remove( 4 );
                    imageList.add( 4, finalImagePath.toString() );
                    iv_album_photo4.setImageBitmap( thumbnail );
                    iv_album_photo4.setBackground( null );
                } else if (flag == 6) {
                    //  imageList.remove(0);
                    // imageList.add(0, finalImagePath);
                    /*iv_album_photo_auth.setImageBitmap(thumbnail);
                    iv_album_photo_auth.setBackground(null);
                    close6.setVisibility(View.VISIBLE);*/
                }

                Utils.log( "UploadPhotoService Images " + imageList.toString() );
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
                pd = ProgressDialog.show( EditAlbumActivity.this, "", "Loading...", false );

                Thread thread = new Thread( new Runnable() {

                    public void run() {

                        MediaphotoID = Utils.doFileUpload( new File( finalImagePath ), "gallery" ); // Upload File to server
                        // MediaphotoID=uploadedimgid;
                        runOnUiThread( new Runnable() {

                            public void run() {

                                if (pd.isShowing())
                                    pd.dismiss();
                                if (MediaphotoID.equals( "0" )) {
                                    Toast.makeText( EditAlbumActivity.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT ).show();
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
        } else if (requestCode == AGENDA) {

            if (resultCode == RESULT_OK) {
                new sendFile().execute( data );
            }

        } else if (requestCode == MOM) {

            if (resultCode == RESULT_OK) {
                new sendFileMOM().execute( data );
            }
        }

        if (d_radio0.isChecked()) {
            llShareWrapper.setVisibility( View.VISIBLE );
        }
    }


    private void selectImage() {

        Intent intent = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        startActivityForResult( intent, 2 );

        final CharSequence[] options;
        //options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
        options = new CharSequence[]{"Choose from Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder( EditAlbumActivity.this );
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
                    dialog.dismiss();
                }
            }
        } );
        //builder.show();
    }

    public boolean validation() {
        if (rbInClub.isChecked()) {
            if (edt_title.getText().toString().trim().matches( "" ) || edt_title.getText().toString().trim() == null) {
                Toast.makeText( EditAlbumActivity.this, "Please enter Title", Toast.LENGTH_LONG ).show();
                return false;
            }
//            if (edt_description.getText().toString().trim().matches( "" ) || edt_description.getText().toString().trim() == null) {
//                Toast.makeText( EditAlbumActivity.this, "Please enter Description.", Toast.LENGTH_LONG ).show();
//                return false;
//            }

//            if (imgFlag == 0) {
//                Toast.makeText(EditAlbumActivity.this, "Please select atleast one photo.", Toast.LENGTH_LONG).show();
//                return false;
//            }

            if (et_DOP.getText().toString().trim().matches( "" ) || et_DOP.getText().toString().trim() == null) {
                Toast.makeText( EditAlbumActivity.this, "Please select Project date.", Toast.LENGTH_LONG ).show();
                return false;
            }

           /* if (!et_attendance_per.getText().toString().isEmpty() && Double.valueOf(et_attendance_per.getText().toString()) > 100) {
                Toast.makeText(EditAlbumActivity.this, "Attendance percentage should not be greater than 100. ", Toast.LENGTH_LONG).show();
                return false;
            }*/

            return true;
        } else if (rbPublic.isChecked()) {


            if (sp_blankselect_SubCategory.equals( "1" )) {

                Toast.makeText( EditAlbumActivity.this, "Please select Category", Toast.LENGTH_LONG ).show();

                return false;


            }

            if (sp_blankselect_SubtosubCategory.equals( "1" )) {

                Toast.makeText( EditAlbumActivity.this, "Please select SubCategory", Toast.LENGTH_LONG ).show();

                return false;


            }

            //this added By Gaurav
            if (flagforcategoryothertext == 1) {

                if (et_categoryName.getText().toString().trim().matches( "" ) || et_categoryName.getText().toString().trim() == null) {

                    //  Toast.makeText(EditAlbumActivity.this, "Please enter Area of focus description.", Toast.LENGTH_LONG).show();
                    Toast.makeText( EditAlbumActivity.this, "Please specify Area of focus.", Toast.LENGTH_LONG ).show();

                    return false;
                }

            }


            if (flagforsubcategoryothertext == 1) {

                if (et_subcategoryName.getText().toString().trim().matches( "" ) || et_subcategoryName.getText().toString().trim() == null) {

                    Toast.makeText( EditAlbumActivity.this, "Please enter category description.", Toast.LENGTH_LONG ).show();

                    return false;
                }


            }

            if (fundingIsSelect.equalsIgnoreCase( "1" )){

                Toast.makeText( EditAlbumActivity.this, "Please select the Source of Funding.", Toast.LENGTH_LONG ).show();

                return false;


            }


            if (et_DOP.getText().toString().trim().matches( "" ) || et_DOP.getText().toString().trim() == null) {
                Toast.makeText( EditAlbumActivity.this, "Please select Project date.", Toast.LENGTH_LONG ).show();
                return false;
            }
            if (et_COP.getText().toString().trim().matches( "" ) || et_COP.getText().toString().trim() == null) {
                Toast.makeText( EditAlbumActivity.this, "Please enter cost.", Toast.LENGTH_LONG ).show();
                return false;
            }

            if (et_Beneficiary.getText().toString().trim().matches( "" ) || et_Beneficiary.getText().toString().trim() == null) {
                Toast.makeText( EditAlbumActivity.this, "Please enter Direct Beneficiaries.", Toast.LENGTH_LONG ).show();
                return false;
            }
            if (et_manPower.getText().toString().trim().matches( "" ) || et_manPower.getText().toString().trim() == null) {
                Toast.makeText( EditAlbumActivity.this, "Please enter Man hours.", Toast.LENGTH_LONG ).show();
                return false;
            }

            if (et_noOfRotarians.getText().toString().trim().matches( "" ) || et_noOfRotarians.getText().toString().trim() == null) {
                Toast.makeText( EditAlbumActivity.this, "Please enter Rotarians Involved.", Toast.LENGTH_LONG ).show();
                return false;
            }


            if (edt_title.getText().toString().trim().matches( "" ) || edt_title.getText().toString().trim() == null) {
                Toast.makeText( EditAlbumActivity.this, "Please enter Title", Toast.LENGTH_LONG ).show();
                return false;
            }

//            if (edt_description.getText().toString().trim().matches( "" ) || edt_description.getText().toString().trim() == null) {
//                Toast.makeText( EditAlbumActivity.this, "Please enter Description.", Toast.LENGTH_LONG ).show();
//                return false;
//            }
            //Rotractors not compulssory


            if (radio_mediaprint_yes.isChecked()) {
                if (iv_album_photo_auth.getDrawable() == null) {
                    Toast.makeText( EditAlbumActivity.this, "Please upload Print media photo..", Toast.LENGTH_LONG ).show();
                    return false;
                }
            }



            try {

                Log.i("MaxNumber", MaxNumber);


                int benificiaryvalue = Integer.parseInt(et_Beneficiary.getText().toString());
                int maxbenificiaryValue = Integer.parseInt(maxBeneficiaries);
                int MaxNumberValue = Integer.parseInt(MaxNumber);
                int ApprovedFlagValue = Integer.parseInt(ApprovedFlag);
                int ApprovedbeneficiaryValue = Integer.parseInt(Approvedbeneficiary);



            if (benificiaryvalue <= maxbenificiaryValue) {
                beneficiary = et_Beneficiary.getText().toString();
                temp_beneficiary = "0";
                temp_beneficiary_flag = "0";
                return true;


            }
            //this is condition added By Gaurav for DirectBenificiary
            if (benificiaryvalue > MaxNumberValue) {
                if ((ApprovedFlagValue == 1) && (ApprovedbeneficiaryValue != benificiaryvalue)) {
                    show_Popup_MaxBenificiary();
                    return false;
                } else if ((ApprovedFlagValue == 2) && (ApprovedbeneficiaryValue != benificiaryvalue)) {
                    show_Popup_MaxBenificiary();
                    return false;
                } else if ((ApprovedFlagValue == 2) && (ApprovedbeneficiaryValue == benificiaryvalue)) {
                    //This condition will check when user not changed value and clicked on update button 2nd time
                    beneficiary = et_Beneficiary.getText().toString();
                    temp_beneficiary_flag = "2";
                    return true;
                } else {

                    beneficiary = et_Beneficiary.getText().toString();
                    temp_beneficiary = "0";
                    temp_beneficiary_flag = "0";
                    return true;
                }

            }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


            return true;

//            if (imgFlag == 0) {
//                Toast.makeText(EditAlbumActivity.this, "Please select atleast one photo.", Toast.LENGTH_LONG).show();
//                return false;
//            }

        }
        return false;
    }

    private void clearselectedtext() {
        tv_getCount.setText( "" );
        iv_edit.setVisibility( View.GONE );
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


    public void deletePhoto(String albumId, String photoId) {
        Log.e( "Touchbase", "------ deletePhoto() is called" );
        String url = Constant.DeletePhoto;
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add( new BasicNameValuePair( "photoId", photoId ) );
        arrayList.add( new BasicNameValuePair( "albumId", albumId ) );
        arrayList.add( new BasicNameValuePair( "deletedBy", PreferenceManager.getPreference( EditAlbumActivity.this, PreferenceManager.GRP_PROFILE_ID ) ) );
        arrayList.add( new BasicNameValuePair( "Financeyear", Gallery.year) );

        DeletePhotoAsyncTask task = new DeletePhotoAsyncTask( url, arrayList, EditAlbumActivity.this );
        task.execute();

        Log.d( "Request", "PARAMETERS " + Constant.DeletePhoto + " :- " + arrayList.toString() );
    }


    public class DeletePhotoAsyncTask extends AsyncTask<String, Object, Object> {

        String val = null;
        ProgressDialog progressDialog = new ProgressDialog( EditAlbumActivity.this, R.style.TBProgressBar );
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
            if (result != "") {
                getdeltedresult( result.toString() );
                Log.d( "Response", "calling DeleteAlbum" );
            } else {
                Log.d( "Response", "Null Resposnse" );
            }

        }
    }

    private void getdeltedresult(String val) {

        try {

            JSONObject jsonObj = new JSONObject( val );
            JSONObject ActivityResult = jsonObj.getJSONObject( "TBDelteAlbumPhoto" );

            final String status = ActivityResult.getString( "status" );

            if (status.equals( "0" )) {

                String msg = ActivityResult.getString( "message" );


                //Ismedia is OFF

                if (deletePhotoflag == 1) {

                    imageList.remove( 0 );
                    imageList.add( 0, "" );

                    descList.remove( 0 );
                    descList.add( 0, "" );

                    photoIdList.remove( 0 );
                    photoIdList.add( 0, "0" );
                    photoId1 = "0";
                    imgFlag = 0;
                    iv_image.setBackground( getResources().getDrawable( R.drawable.asset ) );
                    iv_image.setImageResource( 0 );

                    close1.setVisibility( View.GONE );

                } else if (deletePhotoflag == 2) {

                    imageList.remove( 1 );
                    imageList.add( 1, "" );

                    descList.remove( 1 );
                    descList.add( 1, "" );

                    photoIdList.remove( 1 );
                    photoIdList.add( 1, "0" );
                    photoId2 = "0";
                    iv_album_photo.setBackground( getResources().getDrawable( R.drawable.asset ) );
                    iv_album_photo.setImageResource( 0 );
                    close2.setVisibility( View.GONE );
                } else if (deletePhotoflag == 3) {
                    imageList.remove( 2 );
                    imageList.add( 2, "" );

                    descList.remove( 2 );
                    descList.add( 2, "" );

                    photoIdList.remove( 2 );
                    photoIdList.add( 2, "0" );
                    photoId3 = "0";
                    iv_album_photo2.setBackground( getResources().getDrawable( R.drawable.asset ) );
                    iv_album_photo2.setImageResource( 0 );
                    close3.setVisibility( View.GONE );
                } else if (deletePhotoflag == 4) {

                    imageList.remove( 3 );
                    imageList.add( 3, "" );

                    descList.remove( 3 );
                    descList.add( 3, "" );

                    photoIdList.remove( 3 );
                    photoIdList.add( 3, "0" );
                    photoId4 = "0";
                    iv_album_photo3.setBackground( getResources().getDrawable( R.drawable.asset ) );
                    iv_album_photo3.setImageResource( 0 );
                    close4.setVisibility( View.GONE );
                } else if (deletePhotoflag == 5) {

                    imageList.remove( 4 );
                    imageList.add( 4, "" );

                    descList.remove( 4 );
                    descList.add( 4, "" );

                    photoIdList.remove( 4 );
                    photoIdList.add( 4, "0" );
                    photoId5 = "0";
                    iv_album_photo4.setBackground( getResources().getDrawable( R.drawable.asset ) );
                    iv_album_photo4.setImageResource( 0 );
                    close5.setVisibility( View.GONE );
                }
                Toast.makeText( EditAlbumActivity.this, header + " Photo deleted successfully.", Toast.LENGTH_SHORT ).show();


            }


        } catch (Exception e) {
            Log.d( "exec", "Exception :- " + e.toString() );
        }

    }


   /* public void hideKeyboard() {
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }*/

    //this method is added By Gaurav for MaxBenificiary

    public void show_Popup_MaxBenificiary() {
        final Dialog dialog = new Dialog( EditAlbumActivity.this, android.R.style.Theme_Translucent );
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        dialog.setContentView( R.layout.popup_for_maxbenificiary_new );
        TextView confirm = (TextView) dialog.findViewById( R.id.tv_ok );
        TextView cancel = (TextView) dialog.findViewById( R.id.tv_cancel );
        TextView descriptiontxt = (TextView) dialog.findViewById( R.id.descriptiontxt );

        String text = "A limit of " + maxBeneficiaries + " beneficiaries has been set for each project. As this project has more than " + maxBeneficiaries + " beneficiaries,it will be sent to the Zonal Head of Rotary India for approval. Your project will be added with " + maxBeneficiaries + " beneficiaries at the moment. Once the zonal head approves your project, the beneficiaries will be updated as per your request. Click on confirm to continue or cancel to edit the number of beneficiaries";
        descriptiontxt.setText( text );

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
                hideKeyboard( EditAlbumActivity.this );

              /*  Thread t = new Thread() {
                    public void run() {
                        CloseSoftKeyboard.hideSoftKeyboard(EditAlbumActivity.this);
                    }
                };
                t.start();*/
                //show latter
                beneficiary = maxBeneficiaries.toString();
                temp_beneficiary = et_Beneficiary.getText().toString();
                temp_beneficiary_flag = "1";

                if (InternetConnection.checkConnection( getApplicationContext() )) {
                    webservices();

                } else {
                    Utils.showToastWithTitleAndContext( getApplicationContext(), "No Internet Connection!" );
                }


                dialog.dismiss();

            }
        } );
        dialog.show();

    }
    //Added By Gaurav for remove the last character from the String eg.'s'

    public String method(String str) {
        if (str != null && str.length() > 0 && str.charAt( str.length() - 1 ) == 's') {
            str = str.substring( 0, str.length() - 1 );
        }
        return str;
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService( Activity.INPUT_METHOD_SERVICE );
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View( activity );
        }
        imm.hideSoftInputFromWindow( view.getWindowToken(), 0 );
    }
    public void initradio_b()
    {
        et_COPf = findViewById(R.id.et_COPf);
        D_id = new HashMap<>();
        D_id1 = new HashMap<>();




        et_newong = findViewById(R.id.et_newong);

        ont_ong_selct = findViewById(R.id.ont_ong_selct);

        radioMale = findViewById(R.id.radioMale);


        radioFemale = findViewById(R.id.radioFemale);



        radioMale.setEnabled(true);
        radioFemale.setEnabled(true);

        radioMale1 = findViewById(R.id.radioMale1);

        radioFemale1 = findViewById(R.id.radioFemale1);

        radioMaleong2 = findViewById(R.id.radioMaleong2);

        radioFemaleong2 = findViewById(R.id.radioFemaleong2);

        radioMale2 = findViewById(R.id.radioMale2);

        radioFemale2 = findViewById(R.id.radioFemale2);

        radioMaleong = findViewById(R.id.radioMaleong);

        radioFemaleong = findViewById(R.id.radioFemaleong);



        radioMaleongnew = findViewById(R.id.radioMaleongnew);

        radioFemaleongnew = findViewById(R.id.radioFemaleongnew);


        onetime = findViewById(R.id.onetime);

        onetime1 = findViewById(R.id.onetime2);

        ongoing  = findViewById(R.id.ongoing);

        ong_new  = findViewById(R.id.ong_new);

        cohost = findViewById(R.id.cohost);

        cohost2 = findViewById(R.id.cohost2);


        ong_jnt = findViewById(R.id.ong_jnt);

        ong_ntlt = findViewById(R.id.ong_ntlt);

        selectp = findViewById(R.id.selectp);


      //  ong_p = findViewById(R.id.ong_p);
    }

    private void getong_pro(final String c) {

        try {

//            progressDialog = new ProgressDialog(EditAlbumActivity.this, R.style.TBProgressBar);
//            progressDialog.setCancelable(false);
//            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//            progressDialog.show();

            //  Log.d("Responseeeeee", "PARAMETERS " + Constant.GetAlbumsList_New + " :- " + requestData.toString());
            JSONObject requestData = new JSONObject();
            requestData.put("fk_group_id", PreferenceManager.getPreference(EditAlbumActivity.this, PreferenceManager.GROUP_ID));
            requestData.put("financeyear", Gallery.year);

            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.ongoingproject,requestData , new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
//                    if (progressDialog != null) {
//                        progressDialog.dismiss();
//                    }

                    JSONObject result;
                    Log.d("getongoingproject", "PARAMETERS " + Constant.ongoingproject + " :- " + response.toString());

                    holidayresponse(response, c);
                    Utils.log(response.toString());

                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

//                    if (progressDialog != null) {
//                        progressDialog.dismiss();
//                    }

                    Utils.log("VollyError:- " + error.toString());

                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(120000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(EditAlbumActivity.this, request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void holidayresponse(JSONObject response, String c) {

        try {
            String status = response.getString("status");


            if (status.equalsIgnoreCase("0")) {

                JSONObject businessObject = response.getJSONObject("tblExistingResult");

                JSONArray detail = businessObject.getJSONArray("Table");


                proj = new ArrayList<>();
                int cnt = 0 ;

                D_id1 = new HashMap<>();
                for (int i = 0; i < detail.length(); i++) {

                    JSONObject categoryObj = detail.getJSONObject(i);

                    String pk_gallery_id = (categoryObj.getString("pk_gallery_id"));
                    String TtlOfNewOngoingProj = (categoryObj.getString("TtlOfNewOngoingProj"));



                    if(TtlOfNewOngoingProj == null)
                    {

                    }
                   else if(TtlOfNewOngoingProj.equalsIgnoreCase("null"))
                    {


                    }

                    else if(TtlOfNewOngoingProj.isEmpty() || TtlOfNewOngoingProj.equalsIgnoreCase(""))
                    {

                    }
                    else {

                        D_id.put(TtlOfNewOngoingProj, pk_gallery_id);

                        String keyy = String.valueOf(cnt);

                        D_id1.put(pk_gallery_id, keyy);


                        cnt++;
                        proj.add(TtlOfNewOngoingProj);
                    }
                }

                Log.d("D_id1",D_id1.toString());



                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item
                            , proj);

                    ong_p.setAdapter(arrayAdapter);


                    if(c.equalsIgnoreCase("not"))
                    {
                        ong_p.setSelection(0);
                    }
                    else
                    {

                        try {
                          //  Toast.makeText(getApplicationContext(),"yyyyyy"+D_id1.toString(),Toast.LENGTH_LONG).show();
                           // Log.i("yyyyyy",D_id1.toString()+"-------"+c+"-----"+D_id1.get(c));
                            int ss = Integer.parseInt(D_id1.get(c));
                            ong_p.setSelection(ss);

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }


                 //   ong_p.setSelection(c);


            } else {
                //progressDialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //
            // .dismiss();
        }

    }
    ////------ validation added by prashant sahani

    public boolean validation1() {

        int rdbtid = radioGroup.getCheckedRadioButtonId();

        RadioButton radioButton = (RadioButton) radioGroup.findViewById(rdbtid);

        String selectedText = "";

        try {
            selectedText = (String) radioButton.getText();

            Log.i("rdbtid", selectedText);
        } catch (Exception e) {
            selectedText = "";
        }


        if (rdbtid == -1) {
            Toast.makeText(EditAlbumActivity.this, "Please select Projects", Toast.LENGTH_LONG).show();
            return false;
        } else if (selectedText.equalsIgnoreCase(" One Time Project")) {
//            radioGroupong.clearCheck();


            if (selectedText.equalsIgnoreCase(" One Time Project")) {
                int joint1 = radioGroup1.getCheckedRadioButtonId();
                String y_n = "";
                try {
                    RadioButton rd_Y_N = (RadioButton) radioGroup1.findViewById(joint1);

                    y_n = (String) rd_Y_N.getText();

                } catch (Exception e) {
                    y_n = "";
                }
                if (joint1 == -1) {
                    Toast.makeText(EditAlbumActivity.this, "Please select it is joint Project or not with any other rotary club", Toast.LENGTH_LONG).show();
                    return false;
                } else if (y_n.equalsIgnoreCase(" Yes") || y_n.equalsIgnoreCase(" No")) {

                    int clubR1 = radioGroup2.getCheckedRadioButtonId();
                    if (clubR1 == -1) {
                        Toast.makeText(EditAlbumActivity.this, "Please select club role", Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean validation2() {


        int rdbtid1 = radioGroup.getCheckedRadioButtonId();

        RadioButton radioButton1 = (RadioButton) radioGroup.findViewById(rdbtid1);

        String selectedText1 = "";

        try {
            selectedText1 = (String) radioButton1.getText();

            Log.i("rdbtid", selectedText1);
        } catch (Exception e) {
            selectedText1 = "";
        }

        if(selectedText1.equalsIgnoreCase(" Ongoing/Repeat Project "))
        {



            ///
            int rdbtid = radioGroupong.getCheckedRadioButtonId();

            RadioButton radioButton = (RadioButton) radioGroupong.findViewById(rdbtid);

            String selectedText = "";

            try {
                selectedText = (String) radioButton.getText();

                Log.i("rdbtid", selectedText);
            } catch (Exception e) {
                selectedText = "";
            }



            if ( selectedText1.equalsIgnoreCase(" Ongoing/Repeat Project ") && rdbtid == -1) {
                Toast.makeText(EditAlbumActivity.this, "Please Select New or Existing Ongoing/Repeat Project", Toast.LENGTH_LONG).show();
                return false;
            } else if (selectedText.equalsIgnoreCase("Create new ongoing/repeat project ")) {
                String y_n = "";
                if (selectedText.equalsIgnoreCase("Create new ongoing/repeat project ")) {


                    int joint1 = radioGroupong_new.getCheckedRadioButtonId();
                    y_n = "";
                    try {
                        RadioButton rd_Y_N = (RadioButton) radioGroupong_new.findViewById(joint1);

                        y_n = (String) rd_Y_N.getText();

                        y_n = y_n.replaceAll("\\s", "");

                    } catch (Exception e) {
                        y_n = "";

                    }


                    String tlt = et_COPf.getText().toString();

                    if (tlt.equalsIgnoreCase("") || tlt.isEmpty() || tlt == null) {
                        Toast.makeText(EditAlbumActivity.this, "Enter Title New  Ongoing/Repeat Project", Toast.LENGTH_SHORT).show();
                        return false;
                    } else if (joint1 == -1) {
                        //radioGroupongnew
                        Toast.makeText(EditAlbumActivity.this, "Please select it is joint Project or not with any other rotary club", Toast.LENGTH_SHORT).show();
                        return false;
                    } else if (y_n.equalsIgnoreCase("Yes")) {

                        int clubR1 = radioGroupong2.getCheckedRadioButtonId();
                        if (clubR1 == -1) {
                            Toast.makeText(EditAlbumActivity.this, "Please select club role", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                }

            }

            // return true;
        }

        return true;
    }

    public boolean validation3() {


        int rdbtid1 = radioGroup.getCheckedRadioButtonId();

        RadioButton radioButton1 = (RadioButton) radioGroup.findViewById(rdbtid1);

        String selectedText1 = "";

        try {
            selectedText1 = (String) radioButton1.getText();

            Log.i("rdbtid", selectedText1);
        } catch (Exception e) {
            selectedText1 = "";
        }


        ///
        int rdbtid = radioGroupong.getCheckedRadioButtonId();

        RadioButton radioButton = (RadioButton) radioGroupong.findViewById(rdbtid);

        String selectedText = "";

        try {
            selectedText = (String) radioButton.getText();

            Log.i("rdbtid", selectedText);
        } catch (Exception e) {
            selectedText = "";
        }

        if ( selectedText1.equalsIgnoreCase(" Ongoing/Repeat Project ") && rdbtid == -1) {
            Toast.makeText(EditAlbumActivity.this, "Please Select New or Existing Ongoing/Repeat Project", Toast.LENGTH_SHORT).show();
            return false;
        } else if (selectedText.equalsIgnoreCase("Select from existing ongoing/repeat project ")) {
            String y_n = "";
            if (selectedText.equalsIgnoreCase("Select from existing ongoing/repeat project ")) {


                int joint1 = radioGroupong_new.getCheckedRadioButtonId();
                y_n = "";
                try {
                    RadioButton rd_Y_N = (RadioButton) radioGroupong_new.findViewById(joint1);

                    y_n = (String) rd_Y_N.getText();

                    y_n = y_n.replaceAll("\\s", "");

                } catch (Exception e) {
                    y_n = "";

                }


//                String tlt = ong_p
//
//                if (tlt.equalsIgnoreCase("") || tlt.isEmpty() || tlt == null) {
//                    Toast.makeText(AddAlbum.this, "Enter Title New  Ongoing/Repeat Project", Toast.LENGTH_LONG).show();
//                    return false;
                if (joint1 == -1) {
                    //radioGroupongnew
                    Toast.makeText(EditAlbumActivity.this, "Please select it is joint Project or not with any other rotary club", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (y_n.equalsIgnoreCase("Yes")) {

                    int clubR1 = radioGroupong2.getCheckedRadioButtonId();
                    if (clubR1 == -1) {
                        Toast.makeText(EditAlbumActivity.this, "Please select club role", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }

            }

            // return true;
        }

        return true;
    }

    public void showw()
    {
        radioGroup.setVisibility(View.GONE);
        radioGroupong.setVisibility(View.GONE);
        v_1.setVisibility(View.GONE);
        v2.setVisibility(View.GONE);
    }



}
