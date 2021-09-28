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
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.NEWROW.row.Gallery.maxBeneficiaries;
import static com.NEWROW.row.Utils.PreferenceManager.MY_CATEGORY;

/**
 * Created by user on 06-09-2016.
 */
public class AddAlbum extends Activity {
    ArrayList<String> selectedsubgrp;
    TextView tv_title, tv_cancel, tv_clubServiceInfo;
    ImageView iv_backbutton, iv_edit, iv_event_photo, iv_album_photo, iv_album_photo_auth, iv_album_photo2, iv_album_photo3, iv_album_photo4, iv_delete_agenda, iv_delete_mom;
    RadioButton d_radio0, d_radio1, d_radio2, radio_mediaprint_yes, radio_mediaprint_No;
    TextView tv_getCount, tv_add, et_DOP, txt_fileName, txt_fileName_mom;
    String galleryType = "0";
    ArrayList<DirectoryData> listaddmemberdata = new ArrayList<>();
    ArrayList<SubGoupData> listaddsubgrp = new ArrayList<>();
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
    EditText et_coverPhoto, et_album_photo3, et_album_photo2, et_album_photo1, et_album_photo1_auth, et_album_photo4, et_attendacne, et_attendance_per;
    private String groupType = "1";
    private LinearLayout llShareWrapper, ll_rotaryServicecontent, ll_category, ll_subcategory, ll_subtosubcategory, ll_funding, ll_photos, ll_clubService, ll_attach, ll_attach_mom, ll_attach_agenda, ll_agenda_file, ll_mom_file;
    private RadioGroup rgShare;
    private RadioButton rbInClub, rbPublic;
    String selectedImagePath;
    private String hasimageflag = "0";
    private String galleryId = "0";
    private String edit_gallery_selectedids = "0";
    ProgressDialog pd;
    private String uploadedimgid = "0";
    EditText et_gallery_title, et_description, et_categoryName, et_subcategoryName, et_subtosubcategoryName, et_noOfRotarians, et_COP, et_Beneficiary, et_manPower;
    String inputids = "", albumId = "";
    private String flag_callwebsercie = "0";
    private String moduleName = "";
    private Context context;
    Spinner sp_category, sp_subcategory, sp_subtosubcategory, et_currency, sp_timeType, sp_meeting;
    ArrayList<AlbumData> categoryList = new ArrayList<>();
    ArrayList<AlbumData> subcategoryList = new ArrayList<>();
    ArrayList<AlbumData> subtosubcategoryList = new ArrayList<>();
    ArrayList<AlbumData> subcategoryListupdate = new ArrayList<>();
    ArrayList<AlbumData> subtosubcategoryListupdate = new ArrayList<>();
    ProgressDialog progressDialog;
    SpinnerAdapter adapter, adapter_subcategory;
    SpinnerFundingAdapter fundingadapter;
    SpinnerAdapter_subcategoryupdate adapter_subcategory_update;
    SpinnerAdapter_subtosubcategoryupdate adapter_subtosubcategory_update;
    ArrayList<String> currencyList = new ArrayList<>();
    ArrayList<String> timeType = new ArrayList<>();
    String categoryID, subcategoryID, projectDate, subtosubcategoryID;
    TextView tv_TimeCountType;
    int imgFlag = 0;
    String groupId = "";
    String createdBy = "";
    ArrayList<String> imageList = new ArrayList<>();
    ArrayList<String> descList = new ArrayList<>();
    int flag = 1, flagforcategoryothertext = 0, flagforsubcategoryothertext = 0, flagforfundingothertext = 0, deletePhotoflag = 0;
    UploadPhotoData data;
    UploadedPhotoModel addPhotoModel;
    ImageView close1, close2, close3, close4, close5, close6;
    String isdelete = "false";
    private static final DecimalFormat format = new DecimalFormat("#.##");
    private String agenda_file_id = "0", mom_file_id = "0";
    private final int AGENDA = 10, MOM = 11;

    int i = 0;
    private long mLastClickTime = 0;

    //Add By Gaurav
    String sp_blankselect_SubCategory = "0", subtosubcategoryIdtest = "0";

    String MemberCount = "", clubservice = "1", Ismedia = "0";
    // public static String maxBeneficiaries = "";
    public boolean confirmbuttonclick = false;

    LinearLayout rotarctors_layout, media_layout, authlayout;
    EditText et_rotractors;

    String beneficiary, temp_beneficiary, temp_beneficiary_flag = "";
    LinearLayout attendance_layout, meeting_layout;
    //this is added By Gaurav for Media Photo
    private String MediaphotoID = "0", sp_blankselect_SubtosubCategory = "0";

    //this code is added for select item added
    public AlbumData updateDataForSubcategory, updateDataForSubtosubcategory, newdataforsubcategory, newdataforsubtosubcategory;
    private String galleryid_mail, ClubName_mail, secretaryEmailIds_mail, districtNumber_mail, TempBeneficiary_mail, president_mail, secretary_mail, presidentEmailIds_mail;

    //funding
    private Spinner sp_funding;
    ArrayList<FundingData> fundingList = new ArrayList<>();

    private String fundingID;
    private EditText et_fundingName;
    private String fundingIsSelect;

    RadioButton genderradioButton;
    RadioGroup radioGroup;
    RadioGroup radioGroup1;
    RadioGroup radioGroup2;
    RadioGroup radioGroupong;
    RadioGroup radioGroupong_new;
    RadioGroup radioGroupong2;


    LinearLayout ont_ong_selct;
    LinearLayout onetime;
    LinearLayout onetime1;
    LinearLayout ongoing;
    LinearLayout ong_new;
    LinearLayout ong_jnt;
    LinearLayout ong_ntlt;
    LinearLayout selectp;

    TextView cohost, cohost2;

    public ArrayList<String> proj;
    public Spinner ong_p;

    EditText et_COPf;

    String ontime = "0";
    String ontimeyes = "0";
    String ong_clubrole = "0";
    String crt_newyes = "0";
    String ong_neww = "0";

    public static HashMap<String, String> D_id;


   public int p_ext = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_add_album_new);

        context = this;

        //added by prashant sahani
        et_COPf = findViewById(R.id.et_COPf);
        D_id = new HashMap<>();


        ///

        ont_ong_selct = findViewById(R.id.ont_ong_selct);

        onetime = findViewById(R.id.onetime);

        onetime1 = findViewById(R.id.onetime2);

        ongoing = findViewById(R.id.ongoing);

        ong_new = findViewById(R.id.ong_new);

        cohost = findViewById(R.id.cohost);

        cohost2 = findViewById(R.id.cohost2);


        ong_jnt = findViewById(R.id.ong_jnt);

        ong_ntlt = findViewById(R.id.ong_ntlt);

        selectp = findViewById(R.id.selectp);


        ong_p = findViewById(R.id.ong_p);

        //add by prashant sahani
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        radioGroupong = (RadioGroup) findViewById(R.id.radioGroupong);
        radioGroupong2 = (RadioGroup) findViewById(R.id.radioGroupong2);
        radioGroupong_new = (RadioGroup) findViewById(R.id.radioGroupongnew);


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
                    if (text.equals("OneTimeProject"))// One Time Project
                    {


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

                    } else if (text.equalsIgnoreCase("Ongoing/RepeatProject"))//OneTimeProject//Ongoing/RepeatProject
                    {
                        // radioGroup1.clearCheck();
                        ong_new.setVisibility(View.GONE);

                        ontime = "2";

                        popup2();


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
                    if (text.equals("Yes"))// One Time Project
                    {

                        crt_newyes = "1";
                        onetime1.setVisibility(View.VISIBLE);
                        cohost.setVisibility(View.GONE);
                    } else if (text.equalsIgnoreCase("No")) {
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
                    if (text.equals("LeadClub"))// One Time Project
                    {

                        ong_clubrole = "1";
                        //onetime1.setVisibility(View.VISIBLE);
                        cohost.setVisibility(View.GONE);
                    } else if (text.equalsIgnoreCase("Co-Host")) {
                        //  onetime1.setVisibility(View.GONE);
                        ong_clubrole = "2";

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
                    if (text.equalsIgnoreCase("CreatenewOngoing/RepeatProject"))// One Time Project
                    {
                        //onetime1.setVisibility(View.VISIBLE);


                        ong_neww = "1";

                        radioGroupong_new.clearCheck();
                        ong_new.setVisibility(View.VISIBLE);
                        ong_jnt.setVisibility(View.GONE);

                        ong_ntlt.setVisibility(View.VISIBLE);
                        selectp.setVisibility(View.GONE);


                    } else if (text.equalsIgnoreCase("Selectfromexistingongoing/repeatproject")) {
                        //  onetime1.setVisibility(View.GONE);
                        //  ong_new.setVisibility(View.GONE);

                        ong_neww = "2";


                        ong_jnt.setVisibility(View.GONE);
                        radioGroupong_new.clearCheck();
                        ong_new.setVisibility(View.VISIBLE);
                        ong_jnt.setVisibility(View.GONE);


                        ong_ntlt.setVisibility(View.GONE);
                        selectp.setVisibility(View.VISIBLE);

                        getong_pro();


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


                    cohost.setVisibility(View.GONE);
                    cohost2.setVisibility(View.GONE);


//text = "r";
                    if (text.equals("Yes"))// One Time Project
                    {

                        crt_newyes = "1";


                        ong_jnt.setVisibility(View.VISIBLE);
                        // cohost.setVisibility(View.GONE);
                    } else if (text.equalsIgnoreCase("No")) {
                        crt_newyes = "2";

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
                    if (text.equals("LeadClub"))// One Time Project
                    {
                        //onetime1.setVisibility(View.VISIBLE);


                        ong_clubrole = "1";

                        cohost2.setVisibility(View.GONE);
                    } else if (text.equalsIgnoreCase("Co-Host")) {
                        //  onetime1.setVisibility(View.GONE);
                        ong_clubrole = "2";

                        cohost2.setVisibility(View.VISIBLE);
                    }


                }

            }
        });


/////----------------------------------------------------------------


        //Project Uploaded By co-host Club will be displayed in the club website(Id subcribed through rotary India) and club monthely report only


        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);

        groupId = PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID);
        createdBy = PreferenceManager.getPreference(this, PreferenceManager.GRP_PROFILE_ID);

        moduleName = PreferenceManager.getPreference(this, PreferenceManager.MODUEL_NAME, "Album");
        moduleName = "Activity";

        //  tv_title.setText("New " + moduleName);

        d_radio0 = (RadioButton) findViewById(R.id.d_radio0);
        d_radio1 = (RadioButton) findViewById(R.id.d_radio1);
        d_radio2 = (RadioButton) findViewById(R.id.d_radio2);
        radio_mediaprint_yes = (RadioButton) findViewById(R.id.radio_mediaprint_yes);
        radio_mediaprint_No = (RadioButton) findViewById(R.id.radio_mediaprint_No);

        //  radio_mediaprint_No.setChecked(true);
        d_radio0.setChecked(true);

        addPhotoModel = new UploadedPhotoModel(this);

        ll_agenda_file = findViewById(R.id.ll_agenda_file);
        ll_mom_file = findViewById(R.id.ll_mom_file);

        txt_fileName = (TextView) findViewById(R.id.txt_fileName);
        txt_fileName_mom = (TextView) findViewById(R.id.txt_fileName_mom);
        tv_getCount = (TextView) findViewById(R.id.getCount);

        iv_delete_agenda = findViewById(R.id.iv_delete_agenda);
        iv_delete_mom = findViewById(R.id.iv_delete_mom);

        iv_edit = (ImageView) findViewById(R.id.iv_edit);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);

        iv_event_photo = (ImageView) findViewById(R.id.iv_event_photo);
        iv_album_photo = (ImageView) findViewById(R.id.iv_album_photo);
        iv_album_photo_auth = (ImageView) findViewById(R.id.iv_album_photo_auth);
        iv_album_photo2 = (ImageView) findViewById(R.id.iv_album_photo2);
        iv_album_photo3 = (ImageView) findViewById(R.id.iv_album_photo3);
        iv_album_photo4 = (ImageView) findViewById(R.id.iv_album_photo4);

        et_coverPhoto = (EditText) findViewById(R.id.et_coverPhoto);
        et_album_photo1 = (EditText) findViewById(R.id.et_album_photo1);
        et_album_photo1_auth = (EditText) findViewById(R.id.et_album_photo1_auth);
        et_album_photo2 = (EditText) findViewById(R.id.et_album_photo2);
        et_album_photo3 = (EditText) findViewById(R.id.et_album_photo3);
        et_album_photo4 = (EditText) findViewById(R.id.et_album_photo4);
        et_attendacne = (EditText) findViewById(R.id.et_attendance);
        et_attendance_per = (EditText) findViewById(R.id.et_attendance_per);

        //this layout added By Gaurav

        rotarctors_layout = (LinearLayout) findViewById(R.id.rotarctors_layout);
        attendance_layout = (LinearLayout) findViewById(R.id.attendance_layout);
        meeting_layout = (LinearLayout) findViewById(R.id.meeting_layout);
        et_rotractors = (EditText) findViewById(R.id.et_rotractors);
        media_layout = (LinearLayout) findViewById(R.id.media_layout);
        authlayout = (LinearLayout) findViewById(R.id.authlayout);


        //this attendance formulla added By Gaurav


        et_attendacne.addTextChangedListener(new TextWatcher() {

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
                    float percentage = getAttendancePercentage(attendance);

                    int twodigitpercentage = (int) Math.round(percentage);

                    String out_percentage = String.valueOf(twodigitpercentage);
                    et_attendance_per.setText(out_percentage);

                } else {
                    et_attendance_per.setText("");

                }
            }
        });

        close1 = (ImageView) findViewById(R.id.close1);
        close2 = (ImageView) findViewById(R.id.close2);
        close3 = (ImageView) findViewById(R.id.close3);
        close4 = (ImageView) findViewById(R.id.close4);
        close5 = (ImageView) findViewById(R.id.close5);
        close6 = (ImageView) findViewById(R.id.close2_auth);


        tv_add = (TextView) findViewById(R.id.tv_done);
        tv_clubServiceInfo = (TextView) findViewById(R.id.tv_clubServiceInfo);

        et_gallery_title = (EditText) findViewById(R.id.et_galleryTitle);


        et_description = (EditText) findViewById(R.id.et_evetDesc);

        et_categoryName = (EditText) findViewById(R.id.et_categoryName);
        et_subcategoryName = (EditText) findViewById(R.id.et_subcategoryName);
        et_subtosubcategoryName = (EditText) findViewById(R.id.et_subtosubcategoryName);
        et_fundingName = (EditText) findViewById(R.id.et_fundingName);
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
        et_manPower.setKeyListener(DigitsKeyListener.getInstance(true, true));
        et_noOfRotarians = (EditText) findViewById(R.id.et_noOfRotarians);
        sp_category = (Spinner) findViewById(R.id.sp_category);
        sp_subcategory = (Spinner) findViewById(R.id.sp_subcategory);
        sp_subtosubcategory = (Spinner) findViewById(R.id.sp_subtosubcategory);
        sp_funding = (Spinner) findViewById(R.id.sp_funding);


        //this code Add By Gaurav
        Intent intent = getIntent();
        clubservice = intent.getExtras().getString("serviceproject", "0");

        MemberCount = intent.getExtras().getString("MemberCount");

        Log.i("clubservice", clubservice + "     /    " + MemberCount);


        // maxBeneficiaries = intent.getExtras().getString("maxBeneficiaries");


        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                AlbumData data = categoryList.get(position);
                AlbumData subdata = subcategoryList.get(position);

                categoryID = data.getDistrict_id();
                //  subcategoryListupdate.clear();

                ArrayList<AlbumData> subcategoryListchangeData = new ArrayList<>();

                //this is added By Gaurav

                updateDataForSubcategory = new AlbumData();

                updateDataForSubcategory.setPk_subcategoryIdupdate("0");
                updateDataForSubcategory.setFk_CategoryIDupdate("0");

                updateDataForSubcategory.setSubcategoryNameupdate("Select");


                subcategoryListchangeData.add(0, updateDataForSubcategory);


                for (int i = 0; i < subcategoryList.size(); i++) {

                    if (categoryID.equals(subcategoryList.get(i).getFk_CategoryID())) {
                        updateDataForSubcategory = new AlbumData();
                        updateDataForSubcategory.setPk_subcategoryIdupdate(subcategoryList.get(i).getPk_subcategoryId());
                        updateDataForSubcategory.setFk_CategoryIDupdate(subcategoryList.get(i).getFk_categoryID());
                        updateDataForSubcategory.setSubcategoryNameupdate(subcategoryList.get(i).getSubcategoryName());
                        subcategoryListchangeData.add(updateDataForSubcategory);

                    }


                }
                subcategoryListupdate = subcategoryListchangeData;
                adapter_subcategory_update = new SpinnerAdapter_subcategoryupdate(context, subcategoryListupdate);
                sp_subcategory.setAdapter(adapter_subcategory_update);

                if (subcategoryListupdate.size() == 0 || subcategoryListupdate.size() == 1) {
                    ll_subcategory.setVisibility(View.GONE);
                    ll_subtosubcategory.setVisibility(View.GONE);
                    sp_blankselect_SubCategory = "0";
                    sp_blankselect_SubtosubCategory = "0";

                } else {
                    ll_subcategory.setVisibility(View.VISIBLE);
                }


                //this code end here
                if (categoryList.get(position).getDistrict_Name().equalsIgnoreCase("others")) {
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
        sp_funding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                FundingData data = fundingList.get(position);

                fundingID = data.getPk_Fund_ID();

                if (data.getFund_Name().equalsIgnoreCase("Select") || data.getPk_Fund_ID().equalsIgnoreCase("0")) {
                    fundingIsSelect = "1";
                } else {
                    fundingIsSelect = "0";
                }
                if (fundingList.get(position).getFund_Name().equalsIgnoreCase("others")) {
                    et_fundingName.setVisibility(View.VISIBLE);
                    flagforfundingothertext = 1;

                } else {
                    et_fundingName.setVisibility(View.GONE);
                    flagforfundingothertext = 0;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_subcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                AlbumData data = subcategoryListupdate.get(position);
                subcategoryID = data.getPk_subcategoryIdupdate();
                AlbumData subdata = subtosubcategoryList.get(position);


                ArrayList<AlbumData> subtosubcategoryListchangeData = new ArrayList<>();

                //this code is added By gaurav for select option
                updateDataForSubtosubcategory = new AlbumData();
                updateDataForSubtosubcategory.setPk_subtosubcategoryIdupdate(subtosubcategoryList.get(0).getPk_subcategoryId());
                updateDataForSubtosubcategory.setFk_subcategoryidupdate(subtosubcategoryList.get(0).getFk_subcategoryid());
                updateDataForSubtosubcategory.setFk_categoryIDupdate(subtosubcategoryList.get(0).getFk_categoryID());
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
                subtosubcategoryListupdate = subtosubcategoryListchangeData;
                adapter_subtosubcategory_update = new SpinnerAdapter_subtosubcategoryupdate(context, subtosubcategoryListupdate);
                sp_subtosubcategory.setAdapter(adapter_subtosubcategory_update);

                if (subtosubcategoryListupdate.size() == 0 || subtosubcategoryListupdate.size() == 1) {
                    ll_subtosubcategory.setVisibility(View.GONE);
                    sp_blankselect_SubtosubCategory = "0";

                } else {
                    ll_subtosubcategory.setVisibility(View.VISIBLE);
                }


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
                // subtosubcategoryID = data.getFk_subcategoryidupdate();

                subtosubcategoryIdtest = data.getPk_subtosubcategoryIdupdate();

                if (subtosubcategoryListupdate.get(position).getSubtosubcategorynameupdate().equalsIgnoreCase("Select")) {
                    sp_blankselect_SubtosubCategory = "1";
                } else {
                    sp_blankselect_SubtosubCategory = "0";

                }

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

        sp_meeting = (Spinner) findViewById(R.id.sp_meet_type);

        sp_meeting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                if (pos == 0 || pos == 3) {
                    ll_attach.setVisibility(View.GONE);
                } else {

                    ll_attach.setVisibility(View.VISIBLE);

                    mom_file_id = "0";
                    txt_fileName_mom.setText("");
                    ll_mom_file.setVisibility(View.GONE);
                    ll_attach_mom.setVisibility(View.VISIBLE);

                    agenda_file_id = "0";
                    txt_fileName.setText("");
                    ll_agenda_file.setVisibility(View.GONE);
                    ll_attach_agenda.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        et_currency = (Spinner) findViewById(R.id.et_currency);
        currencyList.add("\u20B9");
        currencyList.add("$");
        ArrayAdapter adapter = new ArrayAdapter(AddAlbum.this, android.R.layout.simple_spinner_dropdown_item, currencyList);
        et_currency.setAdapter(adapter);

        sp_timeType = (Spinner) findViewById(R.id.sp_timeType);
        timeType.add("Hours");
        timeType.add("Days");
        timeType.add("Months");
        timeType.add("Years");
        ArrayAdapter adapter1 = new ArrayAdapter(AddAlbum.this, android.R.layout.simple_spinner_dropdown_item, timeType);
        sp_timeType.setAdapter(adapter1);

        ll_attach_agenda = (LinearLayout) findViewById(R.id.ll_attach_agenda);
        ll_attach_mom = (LinearLayout) findViewById(R.id.ll_attach_mom);
        ll_attach = (LinearLayout) findViewById(R.id.ll_attach);

        llShareWrapper = (LinearLayout) findViewById(R.id.llShareWrapper);
        ll_rotaryServicecontent = (LinearLayout) findViewById(R.id.ll_rotaryServicecontent);
        ll_clubService = (LinearLayout) findViewById(R.id.ll_clubService);
        ll_category = (LinearLayout) findViewById(R.id.ll_category);
        ll_subcategory = (LinearLayout) findViewById(R.id.ll_subcategory);
        ll_subtosubcategory = (LinearLayout) findViewById(R.id.ll_subtosubcategory);
        ll_funding = (LinearLayout) findViewById(R.id.ll_funding);
        ll_photos = (LinearLayout) findViewById(R.id.ll_photos);
        rbInClub = (RadioButton) findViewById(R.id.rbInClub);

        rbPublic = (RadioButton) findViewById(R.id.rbPublic);

        //this code is added By Gaurav
        if (clubservice.equals("0")) {
            //Club Service of data should be displayed

            tv_title.setText("Add Club Meeting");

            ont_ong_selct.setVisibility(View.GONE);

            ll_funding.setVisibility(View.GONE);


            rbInClub.setChecked(true);
            rbPublic.setChecked(false);
            //meeting type
            meeting_layout.setVisibility(View.VISIBLE);

            //this is rotaractor field added
            rotarctors_layout.setVisibility(View.GONE);
            et_rotractors.setVisibility(View.GONE);
            attendance_layout.setVisibility(View.VISIBLE);

            media_layout.setVisibility(View.GONE);


        } else if (clubservice.equals("1")) {

            //Rotary service of data should de displayed

            tv_title.setText("Add Service Project");
      //      popup1();

            ont_ong_selct.setVisibility(View.VISIBLE);


            ll_funding.setVisibility(View.VISIBLE);


            rbPublic.setChecked(true);
            rbInClub.setChecked(false);

            //this is rotaractor field added
            rotarctors_layout.setVisibility(View.VISIBLE);
            et_rotractors.setVisibility(View.VISIBLE);
            attendance_layout.setVisibility(View.GONE);

            //medialayout

            media_layout.setVisibility(View.VISIBLE);
            //meeting type
            meeting_layout.setVisibility(View.GONE);


        }
        if (rbInClub.isChecked()) {
            ll_clubService.setVisibility(View.VISIBLE);
            ll_rotaryServicecontent.setVisibility(View.GONE);
            ll_category.setVisibility(View.GONE);
            tv_clubServiceInfo.setText(getString(R.string.clubServiceText_new));
        }

        rbInClub.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                ll_clubService.setVisibility(View.GONE);
                ll_rotaryServicecontent.setVisibility(View.VISIBLE);
                ll_category.setVisibility(View.VISIBLE);

                if (isChecked) {
                    tv_clubServiceInfo.setText(getString(R.string.clubServiceText_new));
                }

            }
        });


        if (rbPublic.isChecked()) {
            tv_clubServiceInfo.setText(getString(R.string.rotaryServiceText_new));
        }

        rbPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ll_clubService.setVisibility(View.VISIBLE);
                ll_rotaryServicecontent.setVisibility(View.GONE);
                ll_category.setVisibility(View.GONE);

                if (isChecked) {
                    tv_clubServiceInfo.setText(getString(R.string.rotaryServiceText_new));
                }

                ll_attach.setVisibility(View.GONE);

                mom_file_id = "0";
                txt_fileName_mom.setText("");
                ll_mom_file.setVisibility(View.GONE);
                ll_attach_mom.setVisibility(View.VISIBLE);

                agenda_file_id = "0";
                txt_fileName.setText("");
                ll_agenda_file.setVisibility(View.GONE);
                ll_attach_agenda.setVisibility(View.VISIBLE);
            }
        });

        rgShare = (RadioGroup) findViewById(R.id.rgShare);

        for (int i = 0; i < 5; i++) {
            imageList.add("");
            descList.add("");
        }

        groupType = PreferenceManager.getPreference(context, MY_CATEGORY, "" + Constant.GROUP_CATEGORY_CLUB);
        Utils.log("Group type is : " + groupType);
        clearselectedtext();
        init();
        getCategoryList();




//        if(ong_ntlt.getVisibility() == true)
//        {
//
//        }
    }

    //this method is added by Gaurav for calculating attendance % base on membercount

    private float getAttendancePercentage(String attendance) {
        double attendancevalue = Double.parseDouble(attendance);
        double MemberCountvalue = Double.parseDouble(MemberCount);
        ;

        float output = (float) ((attendancevalue * 100) / MemberCountvalue);
        return output;

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

                    Intent i = new Intent(AddAlbum.this, AddMembers.class);
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

                    Intent subgrp = new Intent(AddAlbum.this, NewGroupSelectionActivity.class);
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
                        // selectImage();
                        flag = 1;
                        selectAlbumImages();
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

        iv_album_photo_auth.setOnClickListener(new View.OnClickListener() {
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
        });


        tv_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //  Log.d("-----------------", "add is clicked");


                String tlt = tv_title.getText().toString();
                if(tlt == null)
                {
                    tlt = "";
                }
                Log.d("tittleofedit",tlt);

                if (tlt.equalsIgnoreCase("Add Club Meeting")) {
                    if (validation()) {

                        if (InternetConnection.checkConnection(getApplicationContext())) {
                            webservices();
                        } else {
                            Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                        }

                    }

                }
                else {


                    if (validation1() && validation2() && validation3()) {
                        if (validation()) {



                            if (InternetConnection.checkConnection(getApplicationContext())) {
                                webservices();
                            } else {
                                Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                            }

                        }
                    }
                }


//                    if (InternetConnection.checkConnection( getApplicationContext() )) {
//
                // webservices();
//
//
//                        Thread t = new Thread() {
//                            public void run() {
//                                //This class Added By gaurav for hide soft keyboaard
//                                CloseSoftKeyboard.hideSoftKeyboard( AddAlbum.this );
//                            }
//                        };
//                        t.start();
//                    } else {
//                        Utils.showToastWithTitleAndContext( getApplicationContext(), "No Internet Connection!" );
//                        // Not Available...
//                    }

            }
        });


        close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AddAlbum.this, android.R.style.Theme_Translucent);
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
                        deletePhotoflag = 1;
                        if (!imageList.get(0).isEmpty()) {
                            imageList.remove(0);
                            imageList.add(0, "");
                            iv_event_photo.setBackground(getResources().getDrawable(R.drawable.asset));
                            iv_event_photo.setImageResource(0);
                            close1.setVisibility(View.GONE);
                        }

                    }
                });

                dialog.show();
            }
        });

        close2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AddAlbum.this, android.R.style.Theme_Translucent);
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
                        deletePhotoflag = 1;
                        if (!imageList.get(1).isEmpty()) {
                            imageList.remove(1);
                            imageList.add(1, "");
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
                final Dialog dialog = new Dialog(AddAlbum.this, android.R.style.Theme_Translucent);
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

                        deletePhotoflag = 1;
                        if (!imageList.get(2).isEmpty()) {
                            imageList.remove(2);
                            imageList.add(2, "");
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
                final Dialog dialog = new Dialog(AddAlbum.this, android.R.style.Theme_Translucent);
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

                        deletePhotoflag = 1;
                        if (!imageList.get(3).isEmpty()) {
                            imageList.remove(3);
                            imageList.add(3, "");
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
                final Dialog dialog = new Dialog(AddAlbum.this, android.R.style.Theme_Translucent);
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

                        deletePhotoflag = 1;
                        if (!imageList.get(4).isEmpty()) {
                            imageList.remove(4);
                            imageList.add(4, "");
                            iv_album_photo4.setBackground(getResources().getDrawable(R.drawable.asset));
                            iv_album_photo4.setImageResource(0);
                            close5.setVisibility(View.GONE);
                        }
                    }
                });

                dialog.show();
            }
        });


        close6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(AddAlbum.this, android.R.style.Theme_Translucent);
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
                        Ismedia = "1";
                        isdelete = "false";

                    }
                });

                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        isdelete = "true";
                        deletePhotoflag = 1;
                        iv_album_photo_auth.setBackground(getResources().getDrawable(R.drawable.asset));
                        iv_album_photo_auth.setImageResource(0);

                        Ismedia = "0";
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


        ll_attach_agenda.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                } else {
                    showFileChooser(AGENDA);
                }
            }
        });

        ll_attach_mom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                } else {
                    showFileChooser(MOM);
                }
            }
        });


        iv_delete_agenda.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                agenda_file_id = "0";
                txt_fileName.setText("");
                ll_agenda_file.setVisibility(View.GONE);
                ll_attach_agenda.setVisibility(View.VISIBLE);
            }
        });


        iv_delete_mom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mom_file_id = "0";
                txt_fileName_mom.setText("");
                ll_mom_file.setVisibility(View.GONE);
                ll_attach_mom.setVisibility(View.VISIBLE);
            }
        });

    }

    private void showFileChooser(int module) {

        String[] mimeTypes = {"application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //   File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/example.pdf");
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), module);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Could not connect to the File Manager", Toast.LENGTH_SHORT).show();
        }
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
                    llShareWrapper.setVisibility(View.VISIBLE);
                } else {
                    llShareWrapper.setVisibility(View.VISIBLE);
                }

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
                llShareWrapper.setVisibility(View.GONE);
                Intent subgrp = new Intent(AddAlbum.this, NewGroupSelectionActivity.class);
                subgrp.putExtra("flag_addsubgrp", "1");
                startActivityForResult(subgrp, 1);
                //startActivity(subgrp);

                break;

            case R.id.radio_mediaprint_No:
                Ismedia = "0";
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

                authlayout.setVisibility(View.GONE);
                radio_mediaprint_yes.setChecked(false);

                break;
            case R.id.radio_mediaprint_yes:
                //then auth of media displayed and text
                authlayout.setVisibility(View.VISIBLE);
                radio_mediaprint_No.setChecked(false);

                // Ismedia = "1";
                break;

            case R.id.d_radio2:

                if (checked) {
                    d_radio2.setChecked(false);
                    Intent i = new Intent(AddAlbum.this, AddMembers.class);
                    startActivityForResult(i, 3);
                }

                llShareWrapper.setVisibility(View.GONE);

                // d_radio2.setEnabled(false);
                /*
                d_radio1.setEnabled(true);
                d_radio0.setChecked(false);
                d_radio1.setChecked(false);*/
                break;
        }

        if (groupType.equals("" + Constant.GROUP_CATEGORY_DT)) {
            llShareWrapper.setVisibility(View.GONE);
        }
    }

    private void webservices() {

        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        String isSubGroupAdmin = "0";  // means no
        String isAdmin = PreferenceManager.getPreference(AddAlbum.this, PreferenceManager.IS_GRP_ADMIN, "No");

        if (isAdmin.equalsIgnoreCase("partial")) {
            isSubGroupAdmin = "1";  // means yes
        }

        //arrayList.add(new BasicNameValuePair("isSubGrpAdmin", isSubGroupAdmin));
        arrayList.add(new BasicNameValuePair("albumId", "0"));
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("moduleId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MODULE_ID)));
       // arrayList.add(new BasicNameValuePair("type", galleryType));
       // arrayList.add(new BasicNameValuePair("memberIds", inputids));
        arrayList.add(new BasicNameValuePair("albumTitle", et_gallery_title.getText().toString()));
        arrayList.add(new BasicNameValuePair("albumDescription", et_description.getText().toString()));
        arrayList.add(new BasicNameValuePair("albumImage", uploadedimgid));
        arrayList.add(new BasicNameValuePair("createdBy", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));
        String shareType = "";
        arrayList.add(new BasicNameValuePair("dateofproject", projectDate));

        if (rbInClub.isChecked()) {
            shareType = "0";
            arrayList.add(new BasicNameValuePair("shareType", shareType));
            arrayList.add(new BasicNameValuePair("costofproject", "0"));
            arrayList.add(new BasicNameValuePair("beneficiary", "0"));
            arrayList.add(new BasicNameValuePair("manhourspent", "0"));
            arrayList.add(new BasicNameValuePair("categoryId", "0"));
            arrayList.add(new BasicNameValuePair("manhourspenttype", ""));
            arrayList.add(new BasicNameValuePair("NumberofRotarian", ""));
            arrayList.add(new BasicNameValuePair("OtherCategorytext", ""));
            arrayList.add(new BasicNameValuePair("costofprojecttype", ""));
            arrayList.add(new BasicNameValuePair("Attendance", et_attendacne.getText().toString()));
            arrayList.add(new BasicNameValuePair("AttendancePer", et_attendance_per.getText().toString()));
            int pos = sp_meeting.getSelectedItemPosition();

            arrayList.add(new BasicNameValuePair("MeetingType", "" + pos));
            if (pos == 0 || pos == 3) {
                arrayList.add(new BasicNameValuePair("AgendaDocID", "0"));
                arrayList.add(new BasicNameValuePair("MOMDocID", "0"));
            } else {
                arrayList.add(new BasicNameValuePair("AgendaDocID", agenda_file_id));
                arrayList.add(new BasicNameValuePair("MOMDocID", mom_file_id));
            }
//            arrayList.add(new BasicNameValuePair("AgendaDocID",agenda_file_id));
//            arrayList.add(new BasicNameValuePair("MOMDocID",mom_file_id));

        } else if (rbPublic.isChecked()) {
            shareType = "1";
            arrayList.add(new BasicNameValuePair("shareType", shareType));
            arrayList.add(new BasicNameValuePair("costofproject", et_COP.getText().toString()));

          /*  //In this case you have to added popup message
            int benificiary = Integer.parseInt(et_Beneficiary.getText().toString());
            int maxbenificiaryValue = Integer.parseInt(maxBeneficiaries);

            if (benificiary <= maxbenificiaryValue) {
                arrayList.add(new BasicNameValuePair("beneficiary", et_Beneficiary.getText().toString()));
                arrayList.add(new BasicNameValuePair("TempBeneficiary", "0"));
                arrayList.add(new BasicNameValuePair("TempBeneficiary_flag", "0"));


            } else {
                show_Popup_MaxBenificiary();
                if (confirmbuttonclick) {

                    arrayList.add(new BasicNameValuePair("beneficiary", maxBeneficiaries.toString()));
                    arrayList.add(new BasicNameValuePair("TempBeneficiary", et_Beneficiary.getText().toString()));
                    arrayList.add(new BasicNameValuePair("TempBeneficiary_flag", "1"));


                }

            }*/


            arrayList.add(new BasicNameValuePair("beneficiary", beneficiary));
            arrayList.add(new BasicNameValuePair("TempBeneficiary", temp_beneficiary));
            arrayList.add(new BasicNameValuePair("TempBeneficiary_flag", temp_beneficiary_flag));

            arrayList.add(new BasicNameValuePair("manhourspent", et_manPower.getText().toString()));

            arrayList.add(new BasicNameValuePair("categoryId", categoryID));
            arrayList.add(new BasicNameValuePair("Fk_Funding_Id", fundingID));

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


                arrayList.add(new BasicNameValuePair("Fk_SubTosubcategoryID", subtosubcategoryIdtest));
                arrayList.add(new BasicNameValuePair("OtherSubCategory", ""));

            } else {
                //when other option enable on subcategory spinner
                arrayList.add(new BasicNameValuePair("Fk_SubTosubcategoryID", "0"));
                arrayList.add(new BasicNameValuePair("OtherSubCategory", et_subcategoryName.getText().toString()));

            }
            arrayList.add(new BasicNameValuePair("manhourspenttype", "Hours"));
            arrayList.add(new BasicNameValuePair("NumberofRotarian", et_noOfRotarians.getText().toString()));
            arrayList.add(new BasicNameValuePair("costofprojecttype", (et_currency.getSelectedItemPosition() + 1) + ""));
            arrayList.add(new BasicNameValuePair("Attendance", ""));
            arrayList.add(new BasicNameValuePair("AttendancePer", ""));
            arrayList.add(new BasicNameValuePair("MeetingType", ""));
            arrayList.add(new BasicNameValuePair("AgendaDocID", ""));
            arrayList.add(new BasicNameValuePair("MOMDocID", ""));
            arrayList.add(new BasicNameValuePair("Rotaractors", et_rotractors.getText().toString()));
            arrayList.add(new BasicNameValuePair("Ismedia", Ismedia));
            arrayList.add(new BasicNameValuePair("MediaphotoID", MediaphotoID));

            if (Ismedia.equals("1")) {
                arrayList.add(new BasicNameValuePair("MediaDesc", et_album_photo1_auth.getText().toString()));

            }
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


            arrayList.add(new BasicNameValuePair("OnetimeOrOngoing", ontime));//
            arrayList.add(new BasicNameValuePair("NewOrExisting", ong_neww));

            arrayList.add(new BasicNameValuePair("JoinedOrNot", crt_newyes));//
            arrayList.add(new BasicNameValuePair("ClubRole", ong_clubrole));
           // String t_new = et_COPf.getText().toString().trim();
            try {
                arrayList.add(new BasicNameValuePair("TtlOfNewOngoingProj", t_new));

            } catch (Exception e) {
                arrayList.add(new BasicNameValuePair("TtlOfNewOngoingProj", ""));
            }

//            String sel_item = String.valueOf(ong_p.getSelectedItem());
//            String slt_id = D_id.get(sel_item);

            try {

                arrayList.add(new BasicNameValuePair("ddl_selectProject", slt_id));
            } catch (Exception e) {
                arrayList.add(new BasicNameValuePair("ddl_selectProject", ""));
            }


        }


        flag_callwebsercie = "0";

        Log.d("Requestpara", "PARAMETERS " + Constant.AddUpdateAlbum_New + " :- " + arrayList.toString());

        new WebConnectionAsyncLogin(Constant.AddUpdateAlbum_New, arrayList, AddAlbum.this).execute();

//        Log.d("Response", "PARAMETERS " + Constant.AddAlbum + " :- " + arrayList.toString());
//        new WebConnectionAsyncLogin(Constant.AddAlbum, arrayList, AddAlbum.this).execute();
    }


    public class WebConnectionAsyncLogin extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(AddAlbum.this, R.style.TBProgressBar);

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
                Log.d("Response", "Null Resposnse" + result);
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
                //  String TempBeneficiary_flag_mail = ActivityResult.getString("TempBeneficiary_flag");

                Intent intent = new Intent();

                setResult(1, intent);

                albumId = ActivityResult.getString("galleryid");

                if (iv_event_photo.getDrawable() != null) {
                    descList.remove(0);
                    descList.add(0, et_coverPhoto.getText().toString());
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

                if (iv_album_photo_auth.getDrawable() != null) {
                   /* descList.remove(0);
                    descList.add(0,et_album_photo1_auth.getText().toString());*/
                }
                getAlbumImagesData();

                //This code is added for Images share on Maill during benificiary approve added by Gaurav

              /*  if (TempBeneficiary_flag_mail.equalsIgnoreCase("1") && msg.equalsIgnoreCase("success")) {
                    //Garry
                    //Extra parameter Added By Gaurav for Sharing Images on Mail During Benificiary approvement request


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

//                if (tv_add.getText().equals("Done"))
//                    Toast.makeText(AddAlbum.this, "Album Added successfully.", Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(AddAlbum.this, "Album updated successfully.", Toast.LENGTH_SHORT).show();

                Log.d("Touchbase", "*************** " + status);
                Log.d("Touchbase", "*************** " + msg);
                //finish();//finishing activity

            } else {

                if (tv_add.getText().equals("Add"))
                    Toast.makeText(AddAlbum.this, "Failed to add activity.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(AddAlbum.this, "Failed to update activity.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }

    private void getAlbumImagesData() {

        final ArrayList<UploadPhotoData> uploadPhotoDataArrayList = new ArrayList<>();

        for (int i = 0; i < imageList.size(); i++) {

            if (!imageList.get(i).equals("")) {

                // Log.e("gallery"," image data path="+imageList.get(i) +" desc="+descList.get(i)+" album id ="+albumId+" group id ="+groupId+" createdBy="+createdBy);

                data = new UploadPhotoData("0", imageList.get(i), descList.get(i), albumId, groupId, createdBy, "0");

                //long id = addPhotoModel.insert(data);

                uploadPhotoDataArrayList.add(data);
            }
        }

        if (uploadPhotoDataArrayList.size() == 0) {


            if (clubservice.equals("0")) {
                Toast.makeText(AddAlbum.this, "Club Meeting added successfully.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddAlbum.this, "Service Project added successfully.", Toast.LENGTH_SHORT).show();
                finish();
            }

        } else {

            new UploadPhotoAsyncTask(uploadPhotoDataArrayList).execute();

           /* final ProgressDialog progressDialog = new ProgressDialog(AddAlbum.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            final Handler handler = new Handler();

            Runnable runnable = new Runnable() {

                @Override
                public void run() {

                    UploadPhotoData uploadPhotoData = uploadPhotoDataArrayList.get(i);

                    String url = Constant.AddUpdateAlbumPhoto;

                    try {

                        url = url + "?photoId=" + uploadPhotoData.getPhotoId()+"&desc="+ URLEncoder.encode(uploadPhotoData.getDescription(), "UTF-8")+"&albumId="+uploadPhotoData.getAlbumId()+"&groupId="+uploadPhotoData.getGroupd()+"&createdBy="+uploadPhotoData.getCreatedBy();

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    Log.e("UploadPhotoService", "UploadPhotoToServer "+i);

                    String val = UploadPhotoToServer(new File(uploadPhotoData.getPhotoPath()),url);

                    Log.d("UploadPhotoService", "UploadPhotoService file path => "+uploadPhotoData.getPhotoPath());

                    //uploadImageToServer(new File(uploadPhotoData.getPhotoPath()),uploadPhotoData);

                    Log.e("UploadPhotoService", "url=>"+url+"\nresult=>"+val);

                    if(i<=3) {
                        // do something
                        handler.postDelayed(this, 5000L);  // 1 second delay
                        i++;
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(AddAlbum.this, "Activity Added successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            };

            runnable.run();*/
        }

       /* Toast.makeText(AddAlbum.this, "Activity Added successfully.", Toast.LENGTH_SHORT).show();
        finish();
        Log.d("UploadPhotoService", "UploadPhotoService is Called");
        Intent intent = new Intent(AddAlbum.this, UploadPhotoService.class);
        startService(intent);*/
    }

    public class UploadPhotoAsyncTask extends AsyncTask<String, Object, Object> {

        ArrayList<UploadPhotoData> allImagesList;

        final ProgressDialog progressDialog = new ProgressDialog(AddAlbum.this, R.style.TBProgressBar);

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

            for (int i = 0; i < allImagesList.size(); i++) {

                UploadPhotoData uploadPhotoData = allImagesList.get(i);

                String url = Constant.AddUpdateAlbumPhoto;

                try {

                    url = url + "?photoId=" + uploadPhotoData.getPhotoId() + "&desc=" + URLEncoder.encode(uploadPhotoData.getDescription(), "UTF-8") + "&albumId=" + uploadPhotoData.getAlbumId() + "&groupId=" + uploadPhotoData.getGroupd() + "&createdBy=" + uploadPhotoData.getCreatedBy()  + "&Financeyear=" + Gallery.year;

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Log.e("UploadPhotoService", "UploadPhotoToServer " + i);

                val = UploadPhotoToServer(new File(uploadPhotoData.getPhotoPath()), url);
                System.out.println("new value111----" + val);

                Log.d("UploadPhotoService", "UploadPhotoService file path => " + uploadPhotoData.getPhotoPath()+"RESPONE ---"+val);

                //uploadImageToServer(new File(uploadPhotoData.getPhotoPath()),uploadPhotoData);

                Log.e("UploadPhotoService", "url=>" + url + "\nresult=>" + val);
                Log.e("UploadPhotoService", "result=>" + val);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {

            super.onPostExecute(result);

            if (!AddAlbum.this.isFinishing()) {
                progressDialog.dismiss();
            }

            if (clubservice.equals("0")) {
                Toast.makeText(AddAlbum.this, "Club Meeting added successfully.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddAlbum.this, "Service Project added successfully.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public String UploadPhotoToServer(File file_path, String url) {

        String isUploaded = "";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Log.d("galley", "UploadPhotoService Do file path " + file_path);

        try {

            Log.d("galley", "Do file path satish111" + file_path + " " + url);

            HttpClient client = new DefaultHttpClient();

            //use your server path of php file
            HttpPost post = new HttpPost(url);

            //   Log.d("ServerPath", "Path" + Constant.UploadImage + type);
            //  Log.d("ServerPath", "Path" + Constant.UploadImage+"MemberProfile");

            FileBody bin1 = new FileBody(file_path);

            //  Log.d("Enter", "Filebody complete " + bin1);

            org.apache.http.entity.mime.MultipartEntity reqEntity = new org.apache.http.entity.mime.MultipartEntity();

            reqEntity.addPart("uploaded_file", bin1);
            //reqEntity.addPart("email", new StringBody(useremail));


            post.setEntity(reqEntity);
            //  Log.d("Enter", "Image send complete");

            HttpResponse response = client.execute(post);
            HttpEntity resEntity = response.getEntity();

            //  Log.d("Enter", "Get Response");
            Log.d("galley", "Do file path satish411" + file_path + " " + url);

            try {

                Log.d("galley", "Do file path satish511" + file_path + " " + url);

                final String response_str = EntityUtils.toString(resEntity);

                JSONObject jsonObj = new JSONObject(response_str);
                JSONObject ActivityResult = jsonObj.getJSONObject("LoadImageResult");

                final String status = ActivityResult.getString("status");

                if (status.equals("0")) {
                    isUploaded = response_str;//ActivityResult.getString("message");
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
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

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

        // TODO Hide Future Date Here
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());


        datePickerDialog.show();
    }

    public boolean validation() {


        if (rbInClub.isChecked()) {
            if (et_gallery_title.getText().toString().trim().matches("") || et_gallery_title.getText().toString().trim() == null) {
                Toast.makeText(AddAlbum.this, "Please enter Title", Toast.LENGTH_LONG).show();
                return false;
            }
//            if (et_description.getText().toString().trim().matches("") || et_description.getText().toString().trim() == null) {
//                Toast.makeText(AddAlbum.this, "Please enter Description.", Toast.LENGTH_LONG).show();
//                return false;
//            }

//            if (imgFlag == 0) {
//                Toast.makeText(AddAlbum.this, "Please select atleast one photo.", Toast.LENGTH_LONG).show();
//                return false;
//            }
            if (et_DOP.getText().toString().trim().matches("") || et_DOP.getText().toString().trim() == null) {
                Toast.makeText(AddAlbum.this, "Please select date.", Toast.LENGTH_LONG).show();
                return false;
            }

           /* if (!et_attendance_per.getText().toString().isEmpty() && Double.valueOf(et_attendance_per.getText().toString()) > 100) {

                Toast.makeText(AddAlbum.this, "Attendance percentage should not be greater than 100. ", Toast.LENGTH_LONG).show();

                return false;
            }*/

            return true;

        } else if (rbPublic.isChecked()) {

            if (sp_blankselect_SubCategory.equals("1")) {

                Toast.makeText(AddAlbum.this, "Please select Category", Toast.LENGTH_LONG).show();

                return false;


            }

            if (sp_blankselect_SubtosubCategory.equals("1")) {

                Toast.makeText(AddAlbum.this, "Please select SubCategory", Toast.LENGTH_LONG).show();

                return false;


            }

            if (flagforcategoryothertext == 1) {

                if (et_categoryName.getText().toString().trim().matches("") || et_categoryName.getText().toString().trim() == null) {

                    //  Toast.makeText(AddAlbum.this, "Please enter Area of focus description.", Toast.LENGTH_LONG).show();
                    Toast.makeText(AddAlbum.this, "Please specify Area of focus.", Toast.LENGTH_LONG).show();

                    return false;
                }

            }


            if (flagforsubcategoryothertext == 1) {

                if (et_subcategoryName.getText().toString().trim().matches("") || et_subcategoryName.getText().toString().trim() == null) {

                    Toast.makeText(AddAlbum.this, "Please specify the category.", Toast.LENGTH_LONG).show();

                    return false;
                }


            }

            if (fundingIsSelect.equalsIgnoreCase("1")) {

                Toast.makeText(AddAlbum.this, "Please select the Source of Funding.", Toast.LENGTH_LONG).show();

                return false;


            }


            if (et_DOP.getText().toString().trim().matches("") || et_DOP.getText().toString().trim() == null) {
                Toast.makeText(AddAlbum.this, "Please select date.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (et_COP.getText().toString().trim().matches("") || et_COP.getText().toString().trim() == null) {
                Toast.makeText(AddAlbum.this, "Please enter cost.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (et_Beneficiary.getText().toString().trim().matches("") || et_Beneficiary.getText().toString().trim() == null) {
                Toast.makeText(AddAlbum.this, "Please enter Direct Beneficiaries.", Toast.LENGTH_LONG).show();
                return false;
            }
            if (et_manPower.getText().toString().trim().matches("") || et_manPower.getText().toString().trim() == null) {
                Toast.makeText(AddAlbum.this, "Please enter Man hours.", Toast.LENGTH_LONG).show();
                return false;
            }

            if (et_noOfRotarians.getText().toString().trim().matches("") || et_noOfRotarians.getText().toString().trim() == null) {
                Toast.makeText(AddAlbum.this, "Please enter Rotarians Involved.", Toast.LENGTH_LONG).show();
                return false;
            }
            if (et_gallery_title.getText().toString().trim().matches("") || et_gallery_title.getText().toString().trim() == null) {
                Toast.makeText(AddAlbum.this, "Please enter Title", Toast.LENGTH_LONG).show();
                return false;
            }

//            if (et_description.getText().toString().trim().matches("") || et_description.getText().toString().trim() == null) {
//                Toast.makeText(AddAlbum.this, "Please enter Description.", Toast.LENGTH_LONG).show();
//                return false;
//            }

            //Rotractors not compulssory


            if (radio_mediaprint_yes.isChecked()) {
                if (iv_album_photo_auth.getDrawable() == null) {
                    Toast.makeText(AddAlbum.this, "Please upload Print media photo..", Toast.LENGTH_LONG).show();
                    return false;
                }
            }


            int benificiaryvalue = Integer.parseInt(et_Beneficiary.getText().toString());
            int maxbenificiaryValue = Integer.parseInt(maxBeneficiaries);

            if (benificiaryvalue <= maxbenificiaryValue) {
                beneficiary = et_Beneficiary.getText().toString();
                temp_beneficiary = "0";
                temp_beneficiary_flag = "0";
                return true;


            }
            if (benificiaryvalue >= maxbenificiaryValue) {
                show_Popup_MaxBenificiary();
                return false;
            }


           /* if (et_rotractors.getText().toString().trim().matches("") || et_rotractors.getText().toString().trim() == null) {
                Toast.makeText(AddAlbum.this, "Please enter Rotractors.", Toast.LENGTH_LONG).show();
                return false;
            }*/


//            if (imgFlag == 0) {
//                Toast.makeText(AddAlbum.this, "Please select atleast one photo.", Toast.LENGTH_LONG).show();
//                return false;
//            }

            /*if (iv_album_photo_auth.getDrawable() == null){
                Toast.makeText(AddAlbum.this, "Please select Image Album.", Toast.LENGTH_LONG).show();

            }*/


        }
        return true;
    }

    private void selectImage() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);

        final CharSequence[] options;

        options = new CharSequence[]{"Choose from Gallery"};

//        if (hasimageflag.equals("0")) {
//            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
//            hasimageflag = "1";
//        } else {
//            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Remove Photo", "Cancel"};
//        }

        AlertDialog.Builder builder = new AlertDialog.Builder(AddAlbum.this);
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
                        imgFlag = 0;
                    } else {
                        //remove_photo_webservices();
                    }

                }
            }
        });

        //builder.show();
    }

    private void selectAlbumImages() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 6);
    }

    private void selectAlbumImagesForMediaPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 7);
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

                    if (d.isBox()) {
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
                    iv_event_photo.setBackground(null);

                    String path = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    // f.delete();
                    OutputStream outFile = null;

                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

                    Log.d("TOUCHBASE", "FILE PATH satish 18-07 " + f.toString());
                    ///-------------------------------------------------------------------
                    pd = ProgressDialog.show(AddAlbum.this, "", "Loading...", false);
                    final File finalF = f;

                    Thread thread = new Thread(new Runnable() {

                        public void run() {

                            imgFlag = 1;
                            uploadedimgid = Utils.doFileUpload(new File(finalF.toString()), "gallery"); // Upload File to server
                            imageList.remove(0);
                            imageList.add(0, finalF.toString());
                            Utils.log(imageList.toString());
                            ll_photos.setVisibility(View.VISIBLE);

                            Utils.log("URL : " + finalF.toString());

                            runOnUiThread(new Runnable() {

                                public void run() {

                                    if (pd.isShowing())
                                        pd.dismiss();

                                    Log.d("TOUCHBASE", "FILE UPLOAD ID InnerThread  " + uploadedimgid);

                                    if (uploadedimgid.equals("0")) {
                                        Toast.makeText(AddAlbum.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();
                                        //iv_event_photo.setImageResource(R.drawable.edit_image);
                                        iv_event_photo.setBackground(getResources().getDrawable(R.drawable.asset));
                                        imgFlag = 0;
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
                } catch (OutOfMemoryError err) {
                    err.printStackTrace();
                    Toast.makeText(context, "Failed to load captured image. Please try again.", Toast.LENGTH_LONG).show();
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

                Log.d("TOUCHBASE", "UploadPhotoService FILE PATH " + finalImagePath);

                Utils.log(imageList.toString());

                ///-------------------------------------------------------------------
                pd = ProgressDialog.show(AddAlbum.this, "", "Loading...", false);

                Thread thread = new Thread(new Runnable() {

                    public void run() {

                        uploadedimgid = Utils.doFileUpload(new File(finalImagePath), "gallery"); // Upload File to server

                        runOnUiThread(new Runnable() {

                            public void run() {

                                if (pd.isShowing())
                                    pd.dismiss();

                                if (uploadedimgid.equals("0")) {
                                    Toast.makeText(AddAlbum.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();
                                    //iv_event_photo.setImageResource(R.drawable.edit_image);
                                    iv_event_photo.setBackground(getResources().getDrawable(R.drawable.asset));
                                    //ll_photos.setVisibility(View.GONE);
                                } else {
                                    imageList.remove(0);
                                    imageList.add(0, finalImagePath);
                                    iv_event_photo.setImageBitmap(thumbnail);
                                    iv_event_photo.setBackground(null);
                                    ll_photos.setVisibility(View.VISIBLE);
                                    close1.setVisibility(View.VISIBLE);
                                    imgFlag = 1;
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

                String finalImagePath = imageCompression.compressImage(picturePath, getApplicationContext());

                Log.e("TOUCHBASE", "UploadPhotoService FILE PATH " + picturePath + " final path=" + finalImagePath);

                final Bitmap thumbnail = (BitmapFactory.decodeFile(finalImagePath));

                //imageList.add(picturePath.toString());

                Utils.log("Images list " + imageList.toString());

                if (flag == 1) {
                    imageList.remove(0);
                    imageList.add(0, finalImagePath);
                    iv_event_photo.setImageBitmap(thumbnail);
                    iv_event_photo.setBackground(null);
                    ll_photos.setVisibility(View.VISIBLE);
                    close1.setVisibility(View.VISIBLE);
                } else if (flag == 2) {
                    imageList.remove(1);
                    imageList.add(1, finalImagePath);
                    iv_album_photo.setImageBitmap(thumbnail);
                    iv_album_photo.setBackground(null);
                    close2.setVisibility(View.VISIBLE);
                } else if (flag == 3) {
                    imageList.remove(2);
                    imageList.add(2, finalImagePath);
                    iv_album_photo2.setImageBitmap(thumbnail);
                    iv_album_photo2.setBackground(null);
                    close3.setVisibility(View.VISIBLE);
                } else if (flag == 4) {
                    imageList.remove(3);
                    imageList.add(3, finalImagePath);
                    iv_album_photo3.setImageBitmap(thumbnail);
                    iv_album_photo3.setBackground(null);
                    close4.setVisibility(View.VISIBLE);
                } else if (flag == 5) {
                    imageList.remove(4);
                    imageList.add(4, finalImagePath);
                    iv_album_photo4.setImageBitmap(thumbnail);
                    iv_album_photo4.setBackground(null);
                    close5.setVisibility(View.VISIBLE);
                } else if (flag == 6) {
                    //  imageList.remove(0);
                    //imageList.add(0, finalImagePath);
                    iv_album_photo_auth.setImageBitmap(thumbnail);
                    iv_album_photo_auth.setBackground(null);
                    close6.setVisibility(View.VISIBLE);
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
                pd = ProgressDialog.show(AddAlbum.this, "", "Loading...", false);

                Thread thread = new Thread(new Runnable() {

                    public void run() {

                        MediaphotoID = Utils.doFileUpload(new File(finalImagePath), "gallery"); // Upload File to server
                        // MediaphotoID=uploadedimgid;
                        runOnUiThread(new Runnable() {

                            public void run() {

                                if (pd.isShowing())
                                    pd.dismiss();
                                if (MediaphotoID.equals("0")) {
                                    Toast.makeText(AddAlbum.this, "Image Upload failed, Please try Again!", Toast.LENGTH_SHORT).show();
                                    //iv_event_photo.setImageResource(R.drawable.edit_image);
                                    // imageList.remove(0);
                                    // imageList.add(0,"");
                                    iv_album_photo_auth.setBackground(getResources().getDrawable(R.drawable.asset));
                                    imgFlag = 0;
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
        } else if (requestCode == AGENDA) {

            if (resultCode == RESULT_OK) {
                new sendFile().execute(data);
            }

        } else if (requestCode == MOM) {

            if (resultCode == RESULT_OK) {
                new sendFileMOM().execute(data);
            }

        }

        if (groupType.equals("2")) {
            llShareWrapper.setVisibility(View.GONE);
        } else if (d_radio0.isChecked()) {
            llShareWrapper.setVisibility(View.VISIBLE);
        }
    }


    public class sendFile extends AsyncTask<Intent, Void, String> {

        ProgressDialog progressDialog;
        String displayName = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddAlbum.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Intent... param) {

            Intent data = param[0];
            double length = 0;

            Uri uri = data.getData();

            String uriString = uri.toString();

            Log.e("sa", "bg uri=> " + uriString);

            //MEDIA GALLERY

           /* selectedImagePath = Utils.getPath(getApplicationContext(), uri);

            File myFile = new File(selectedImagePath);

            if (uriString.startsWith("content://")) {

                Cursor cursor = null;

                try {

                    cursor = context.getContentResolver().query(uri, null, null, null, null);

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

            if (uriString.startsWith("content://")) {

                selectedImagePath = Utils.getPathLocal(uri, AddAlbum.this);//Utils.getPath(getApplicationContext(), uri);

                Log.d("***********", "----- path=> " + selectedImagePath);

                myFile = new File(selectedImagePath);

                Cursor cursor = null;

                try {

                    cursor = AddAlbum.this.getContentResolver().query(uri, null, null, null, null);

                    if (cursor != null && cursor.moveToFirst()) {

                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                        Log.d("-----", "-----" + displayName);

                        length = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));

                    }

                } finally {
                    cursor.close();
                }

            } else if (uriString.startsWith("file://")) {

                displayName = uriString.substring(uriString.lastIndexOf("/") + 1);

                File cacheDir = Utils.getDocumentCacheDir(AddAlbum.this);
                myFile = Utils.generateFileName("row_gallery" + System.currentTimeMillis(), cacheDir);
                String destinationPath = null;

                if (myFile != null) {
                    destinationPath = myFile.getAbsolutePath();
                    Utils.saveFileFromUri(AddAlbum.this, uri, destinationPath);
                }

                length = myFile.length();

            }

            double size = Double.parseDouble(format.format(length / 1048576));

            Log.d("***********", "-----" + String.valueOf(size));

            String filenameArray[] = displayName.split("\\.");

            String extension = filenameArray[filenameArray.length - 1];

            Log.d("***********", "-----" + extension);

            if (!extension.equalsIgnoreCase("pdf") && !extension.equalsIgnoreCase("doc") && !extension.equalsIgnoreCase("docx")) {
                return "fileExt";
            } else if (size > 10) {
                return "filesize";
            } else {

                String result = "";

                if (displayName != null && !displayName.isEmpty()) {

                    //link.setText(wpath);

//                  tv_name_pdf.setText(displayName);

                        /*    Intent i = new Intent(getApplicationContext(), E_BulletineAdapter.class);
                            i.putExtra("file name", displayName);
                            startActivity(i);*/

//                    agenda_file_id = Utils.doPdfFileUpload(new File(selectedImagePath), "gallery"); // Upload File to server

                    agenda_file_id = Utils.doPdfFileUpload(myFile, "gallery"); // Upload File to server

                    Log.d("TOUCHBASE", "FILE UPLOAD ID  " + agenda_file_id);

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
            super.onPostExecute(s);

            progressDialog.dismiss();

            if (s != null && !s.isEmpty()) {
                if (s.equalsIgnoreCase("filesize")) {
                    Utils.showToastWithTitleAndContext(context, "File size must not be greater than 10 MB");
                } else if (s.equalsIgnoreCase("fileExt")) {
                    Utils.showToastWithTitleAndContext(context, "Only pdf, doc & docx files are allowed");
                } else {
                    ll_agenda_file.setVisibility(View.VISIBLE);
                    ll_attach_agenda.setVisibility(View.GONE);
                    txt_fileName.setText(displayName);
                    agenda_file_id = s;
                }

            } else {
                txt_fileName.setText("");
                Utils.showToastWithTitleAndContext(context, getString(R.string.msgRetry));
            }
        }
    }

    public class sendFileMOM extends AsyncTask<Intent, Void, String> {

        ProgressDialog progressDialog;
        String displayName = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddAlbum.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
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
                    cursor = context.getContentResolver().query(uri, null, null, null, null);
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

            if (uriString.startsWith("content://")) {

                selectedImagePath = Utils.getPathLocal(uri, AddAlbum.this);//Utils.getPath(getApplicationContext(), uri);

                Log.d("***********", "----- path=> " + selectedImagePath);

                myFile = new File(selectedImagePath);

                Cursor cursor = null;

                try {

                    cursor = AddAlbum.this.getContentResolver().query(uri, null, null, null, null);

                    if (cursor != null && cursor.moveToFirst()) {

                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                        Log.d("-----", "-----" + displayName);

                        length = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));

                    }

                } finally {
                    cursor.close();
                }

            } else if (uriString.startsWith("file://")) {

                displayName = uriString.substring(uriString.lastIndexOf("/") + 1);

                File cacheDir = Utils.getDocumentCacheDir(AddAlbum.this);
                myFile = Utils.generateFileName("row_gallery" + System.currentTimeMillis(), cacheDir);
                String destinationPath = null;

                if (myFile != null) {
                    destinationPath = myFile.getAbsolutePath();
                    Utils.saveFileFromUri(AddAlbum.this, uri, destinationPath);
                }

                length = myFile.length();

            }

            double size = Double.parseDouble(format.format(length / 1048576));
            Log.d("***********", "-----" + String.valueOf(size));

            String filenameArray[] = displayName.split("\\.");
            String extension = filenameArray[filenameArray.length - 1];
            Log.d("***********", "-----" + extension);

            if (!extension.equalsIgnoreCase("pdf") && !extension.equalsIgnoreCase("doc") && !extension.equalsIgnoreCase("docx")) {
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


                    mom_file_id = Utils.doPdfFileUpload(myFile, "gallery"); // Upload File to server

                    Log.d("TOUCHBASE", "FILE UPLOAD ID  " + mom_file_id);

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
            super.onPostExecute(s);

            progressDialog.dismiss();

            if (s != null && !s.isEmpty()) {
                if (s.equalsIgnoreCase("filesize")) {
                    Utils.showToastWithTitleAndContext(context, "File size must not be greater than 10 MB");
                } else if (s.equalsIgnoreCase("fileExt")) {
                    Utils.showToastWithTitleAndContext(context, "Only pdf, doc & docx files are allowed");
                } else {
                    ll_mom_file.setVisibility(View.VISIBLE);
                    ll_attach_mom.setVisibility(View.GONE);
                    txt_fileName_mom.setText(displayName);
                    mom_file_id = s;
                }
            } else {
                txt_fileName_mom.setText("");
                Utils.showToastWithTitleAndContext(context, getString(R.string.msgRetry));
            }
        }
    }

    private void sendMailBeneficiaries() {

        try {

            progressDialog = new ProgressDialog(AddAlbum.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject();
            //Parameters added for benificiary images sharing on President mail
            requestData.put("AlbumID", galleryid_mail);
            requestData.put("ClubName", ClubName_mail);
            requestData.put("districtNumber", districtNumber_mail);
            requestData.put("TempBeneficiary", TempBeneficiary_mail);
            requestData.put("president", president_mail);
            requestData.put("secretary", secretary_mail);
            requestData.put("presidentEmailIds", presidentEmailIds_mail);
            requestData.put("secretaryEmailIds", secretaryEmailIds_mail);
            //  requestData.put("ClubName", clubname);

            Log.d("Response", "PARAMETERS " + Constant.SendMailBeneficiaries + " :- " + requestData.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.SendMailBeneficiaries, requestData, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    JSONObject result;
                    //setAllShowcaseDetails(response);
                    Log.d("SendMailBeneficiaries", response.toString());

                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }

                    Utils.log("VollyError:- " + error.toString());

                    //showErrorDialog();
                    //Utils.showMsg(context, "Something went wrong");
                    //Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_LONG).show();
                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(120000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(context, request);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getCategoryList() {

        try {

            progressDialog = new ProgressDialog(AddAlbum.this, R.style.TBProgressBar);
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

                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }

                    Utils.log("VollyError:- " + error.toString());

                    //showErrorDialog();
                    //Utils.showMsg(context, "Something went wrong");
                    //Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_LONG).show();
                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(120000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(context, request);

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
                JSONArray funding = result.getJSONArray("Fundinglist");

                for (int i = 0; i < Categories.length(); i++) {
                    JSONObject categoryObj = Categories.getJSONObject(i);
                    AlbumData data = new AlbumData();
                    data.setDistrict_id(categoryObj.getString("ID"));
                    data.setDistrict_Name(categoryObj.getString("Name"));
                    categoryList.add(i, data);
                }
                adapter = new SpinnerAdapter(context, categoryList);
                sp_category.setAdapter(adapter);

                //Add this for subcategory data for Gaurav
                for (int i = 0; i < subCategories.length(); i++) {
                    JSONObject subcategoryObj = subCategories.getJSONObject(i);
                    AlbumData data = new AlbumData();
                    data.setPk_subcategoryId(subcategoryObj.getString("pk_subcategoryId"));
                    data.setFk_CategoryID(subcategoryObj.getString("fk_CategoryID"));
                    data.setSubcategoryName(subcategoryObj.getString("SubcategoryName"));
                    subcategoryList.add(i, data);
                }

                //Add this for subtosubcategory data for Gaurav
                for (int i = 0; i < subtosubCategories.length(); i++) {
                    JSONObject subtosubcategoryObj = subtosubCategories.getJSONObject(i);
                    AlbumData data = new AlbumData();
                    data.setPk_subtosubcategoryId(subtosubcategoryObj.getString("pk_subtosubcategoryId"));
                    data.setFk_subcategoryid(subtosubcategoryObj.getString("fk_subcategoryid"));
                    data.setFk_categoryID(subtosubcategoryObj.getString("fk_categoryID"));
                    data.setSubtosubcategoryname(subtosubcategoryObj.getString("subtosubcategoryname"));
                    subtosubcategoryList.add(i, data);
                }
                //Add this for funding data for Gaurav
                FundingData dataDefault = new FundingData();
                dataDefault.setPk_Fund_ID("0");
                dataDefault.setFund_Name("Select");
                fundingList.add(0, dataDefault);

                for (int i = 0; i < funding.length(); i++) {
                    JSONObject fundingObj = funding.getJSONObject(i);
                    FundingData data = new FundingData();
                    data.setPk_Fund_ID(fundingObj.getString("Pk_Fund_ID"));
                    data.setFund_Name(fundingObj.getString("Fund_Name"));
                    fundingList.add(i + 1, data);
                }
                fundingadapter = new SpinnerFundingAdapter(context, fundingList);
                sp_funding.setAdapter(fundingadapter);


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
        final Dialog dialog = new Dialog(AddAlbum.this, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_for_maxbenificiary_new);
        TextView confirm = (TextView) dialog.findViewById(R.id.tv_ok);
        TextView cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        TextView descriptiontxt = (TextView) dialog.findViewById(R.id.descriptiontxt);

        String text = "A limit of " + maxBeneficiaries + " beneficiaries has been set for each project. As this project has more than " + maxBeneficiaries + " beneficiaries,it will be sent to the Zonal Head of Rotary India for approval. Your project will be added with " + maxBeneficiaries + " beneficiaries at the moment. Once the zonal head approves your project, the beneficiaries will be updated as per your request. Click on confirm to continue or cancel to edit the number of beneficiaries";
        descriptiontxt.setText(text);

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

    public void popup1() {

        final Dialog dialog = new Dialog(AddAlbum.this, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.serv_pop);
        dialog.setCancelable(false);


        dialog.setCanceledOnTouchOutside(false);

        Button tv_yes = (Button) dialog.findViewById(R.id.serbtn2);
        tv_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void popup2() {

        final Dialog dialog = new Dialog(AddAlbum.this, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.serv_popup);


        Button tv_yes = (Button) dialog.findViewById(R.id.serbtn);
        tv_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void getong_pro() {

        try {

            progressDialog = new ProgressDialog(AddAlbum.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            //  Log.d("Responseeeeee", "PARAMETERS " + Constant.GetAlbumsList_New + " :- " + requestData.toString());
            JSONObject requestData = new JSONObject();
            requestData.put("fk_group_id", PreferenceManager.getPreference(AddAlbum.this, PreferenceManager.GROUP_ID));
            requestData.put("financeyear", Gallery.year);
            Log.d("load response===",requestData.toString());



            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.ongoingproject, requestData, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }

                    JSONObject result;
                    Log.d("getongoingproject", "PARAMETERS " + Constant.ongoingproject + " :- " + response.toString());

                    holidayresponse(response);
                    Utils.log(response.toString());

                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }

                    Utils.log("VollyError:- " + error.toString());

                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(120000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(AddAlbum.this, request);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void holidayresponse(JSONObject response) {

        try {
            String status = response.getString("status");


            if (status.equalsIgnoreCase("0")) {

                JSONObject businessObject = response.getJSONObject("tblExistingResult");

                JSONArray detail = businessObject.getJSONArray("Table");


                proj = new ArrayList<>();


                if(detail.length() == 0)
                {
                    p_ext = 2;
                }



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
                        p_ext = 1 ;
                        D_id.put(TtlOfNewOngoingProj, pk_gallery_id);

                        proj.add(TtlOfNewOngoingProj);

                    }

//                    D_id.put(TtlOfNewOngoingProj, pk_gallery_id);
//
//                    proj.add(TtlOfNewOngoingProj);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item
                        , proj);

                ong_p.setAdapter(arrayAdapter);

            } else {
                //progressDialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //progressDialog.dismiss();
        }

    }


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
            Toast.makeText(AddAlbum.this, "Please select Projects", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(AddAlbum.this, "Please select it is joint Project or not with any other rotary club", Toast.LENGTH_LONG).show();
                    return false;
                } else if (y_n.equalsIgnoreCase(" Yes") || y_n.equalsIgnoreCase(" No")) {

                    int clubR1 = radioGroup2.getCheckedRadioButtonId();
                    if (clubR1 == -1) {
                        Toast.makeText(AddAlbum.this, "Please select club role", Toast.LENGTH_LONG).show();
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
            Toast.makeText(AddAlbum.this, "Please Select New or Existing Ongoing/Repeat Project", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(AddAlbum.this, "Enter Title New  Ongoing/Repeat Project", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (joint1 == -1) {
                    //radioGroupongnew
                    Toast.makeText(AddAlbum.this, "Please select it is joint Project or not with any other rotary club", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (y_n.equalsIgnoreCase("Yes")) {

                    int clubR1 = radioGroupong2.getCheckedRadioButtonId();
                    if (clubR1 == -1) {
                        Toast.makeText(AddAlbum.this, "Please select club role", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(AddAlbum.this, "Please Select New or Existing Ongoing/Repeat Project", Toast.LENGTH_SHORT).show();
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

                if(p_ext == 2)
                {
                    Toast.makeText(AddAlbum.this, "You can't add this project because you have not any Existing ongoing/repeat project", Toast.LENGTH_SHORT).show();
                    return  false ;
                }

              else if (joint1 == -1) {
                    //radioGroupongnew
                    Toast.makeText(AddAlbum.this, "Please select it is joint Project or not with any other rotary club", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (y_n.equalsIgnoreCase("Yes")) {

                    int clubR1 = radioGroupong2.getCheckedRadioButtonId();
                    if (clubR1 == -1) {
                        Toast.makeText(AddAlbum.this, "Please select club role", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }

            }

            // return true;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}




