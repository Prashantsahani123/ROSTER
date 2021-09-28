package com.NEWROW.row;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.CountrySpAdapter;
import com.NEWROW.row.Data.CountryData;
import com.NEWROW.row.Data.profiledata.AddressData;
import com.NEWROW.row.Data.profiledata.BusinessMemberDetails;
import com.NEWROW.row.Data.profiledata.CompleteProfile;
import com.NEWROW.row.Data.profiledata.DynamicFieldData;
import com.NEWROW.row.Data.profiledata.FamilyMemberData;
import com.NEWROW.row.Data.profiledata.PersonalMemberDetails;
import com.NEWROW.row.Data.profiledata.ProfileMasterData;
import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.MarshMallowPermission;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.TBPrefixes;
import com.NEWROW.row.Utils.UnzipUtility;
import com.NEWROW.row.Utils.Utils;
import com.NEWROW.row.services.DirectoryUpdateService;
import com.NEWROW.row.sql.ProfileModel;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;


/**
 * Created by USER on 18-12-2015.
 */
public class EditProfileActivity extends Activity {
    private static final boolean NO_ANIMATE = false, ANIMATE = true;
    private String zipUrl = "";
    private MarshMallowPermission permission;
    private File downloadDir, tempFile;
    private DownloadManager downloadManager;
    private long downloadId;
    private String firstTime = "no";
    private Context context;
    private ProfileModel profileModel;
    private String grpId = "";
    private String profileId = "";
    private ArrayList<CountryData> personalCountryList = new ArrayList<>();
    private ArrayList<CountryData> businessCountryList = new ArrayList<>();
    private CountrySpAdapter countryAdapterPersonal, countryAdapterBusiness;
    private ArrayList<DynamicFieldData> dynamicFieldDatas;
    private Hashtable<String, EditText> componentTable;
    private LinearLayout llFamilyMembers, llDynamicFields, llWrapper,bottomLayout;
    // Action Bar component
    private TextView tvTitle;
    private ImageView ivEdit;
    private String groupId, memberProfileId;
    private String isAdmin = "", sessionProfileId = "";
    // Data from database
    private ProfileMasterData masterData;
    private Hashtable<String, String> otherDetails;
    private Hashtable<String, String> businessDetails;
    private ArrayList<FamilyMemberData> familyList;
    private Hashtable<String, DynamicFieldData> staticDataTable;
    private Hashtable<String, AddressData> addressesTable;
    private Hashtable<String, LinearLayout> familyLayoutTable;
    private String updatedOn = "";
    // Start of all component declaration
    private EditText etMemberName;
    private TextView tvPrimaryMobNo;
    private TextView tvDoa;
    private TextView tvDob;
    private TextView tvAddFamilyMember;
    private TextView tvDone;
    private TextView tvheader;
    private ImageView ivEditDob;
    private ImageView ivDeleteDob;
    private ImageView ivEditDoa;
    private ImageView ivDeleteDoa;
    private ImageView ivAddFamilyMember;
    private ImageView ivDeleteSecondaryMobile;
    private Spinner spBloodGroup;
    private Spinner spPersonalCountry;
    private EditText etCountryCodeSecMob;
    private EditText etSecondaryMobNo;
    private EditText etMemberEmail;
    private EditText etPersonalAddress;
    private EditText etResidentialCity;
    private EditText etResidentialPinCode;
    private EditText etResidentialState;
    private EditText etResidentialCountryCode;
    private EditText etResidentialContactNo;
    //private EditText etFax;
    private EditText etBusinessEmail;
    private EditText etBusinessDesignation;
    private EditText etBusinessName;
    private EditText etBusinessAddress;
    private EditText etBusinessCity;
    private EditText etBusinessPincode;
    private EditText etBusinessState;
    private EditText etClassification, etKeywords, etRotaryID, etClubDesignation, etDTDesignation, etDonarRecognition;
    private ScrollView scrollView;
    private EditText etBusinessPhoneCountryCode;
    private EditText etBusinessContact;
    private EditText etBusinessFax;
    private Spinner spBusinessCountry;
    //private Spinner spSecMobCountryCode
    private ArrayList<String> deletedFamilyMembers = new ArrayList<>();
    private String residential_addressId="";
    private String bussiness_addressId="";
    private static final String[] MONTHS = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    // End of all component declaration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_edit_profile);

        context = this;

        grpId = getIntent().getStringExtra("groupId");//         intent1.putExtra( "profileId", pp);

        updatedOn = PreferenceManager.getPreference(this, TBPrefixes.DIRECTORY_PREFIX + grpId, "1970/01/01 00:00:00");

        profileId = getIntent().getStringExtra("profileId");

        profileModel = new ProfileModel(context);

        componentTable = new Hashtable<>();
        staticDataTable = new Hashtable<>();
        familyList = new ArrayList<>();
        addressesTable = new Hashtable<>();
        otherDetails = new Hashtable<>();
        businessDetails = new Hashtable<>();
        familyLayoutTable = new Hashtable<>();




        isAdmin = PreferenceManager.getPreference(context, PreferenceManager.IS_GRP_ADMIN, "No");



        Utils.log("IsAdmin : "+isAdmin);

        init();

        loadDynamicFields();
        loadPersonalDetails();
        loadFamilyDetails();
        loadStaticFields();

        personalCountryList = Utils.getCountryList(context);

        businessCountryList.addAll(personalCountryList);
        spPersonalCountry.setAdapter(countryAdapterPersonal);
        countryAdapterPersonal = new CountrySpAdapter(context, R.layout.select_country_item_sp, personalCountryList);
        spPersonalCountry.setAdapter(countryAdapterPersonal);

        countryAdapterBusiness = new CountrySpAdapter(context, R.layout.select_country_item_sp, businessCountryList);
        spBusinessCountry.setAdapter(countryAdapterBusiness);

        loadAddresses();

        loadPersonalAddress();

        loadBusinessAddress();

         countryAdapterPersonal = new CountrySpAdapter(context, R.layout.select_country_item_sp, personalCountryList);

        //actvPersonalCountry.setAdapter(countryAdapterPersonal);

      /*  etMemberName.requestFocus();
        etMemberName.setFocusable(true);
        etMemberName.setInputType(InputType.TYPE_NULL);*/

        initAllCountrySelection();
        initEvents();
        checkAdminRights();


        if(isAdmin.equalsIgnoreCase("Yes"))
        {
            etBusinessDesignation.setEnabled(true);
            etRotaryID.setEnabled(true);
        }
        else{

            etBusinessDesignation.setEnabled(false);
            etRotaryID.setEnabled(false);
        }


        fillsponsore();

        //added by satish for fixed issue #43 of 'ROW Issues 16/1'
        scrollView.post(new Runnable() {

            public void run() {
                scrollView.smoothScrollTo(0,-10);
            }
        });
    }

    public void checkAdminRights() {
        if ( isAdmin.equalsIgnoreCase("no") ) {
            etMemberName.setEnabled(false);
            etClubDesignation.setEnabled(false);
            etDTDesignation.setEnabled(false);
        } else {
            etMemberName.setEnabled(true);
            etClubDesignation.setEnabled(true);
//            etDTDesignation.setEnabled(true);
        }
    }

    public void init() {

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        llFamilyMembers = (LinearLayout) findViewById(R.id.llFamilyMembers);
        llDynamicFields = (LinearLayout) findViewById(R.id.llDynamicFields);
        llWrapper = (LinearLayout) findViewById(R.id.llWrapper);
        bottomLayout = (LinearLayout)findViewById(R.id.bottomLayout);

        etMemberName = (EditText) findViewById(R.id.etMemberName);

        tvPrimaryMobNo = (TextView) findViewById(R.id.tvPrimaryMobNo);
        tvDoa = (TextView) findViewById(R.id.tvDoa);
        tvDob = (TextView) findViewById(R.id.tvDob);
        tvAddFamilyMember = (TextView) findViewById(R.id.tvAddMember);
        ivEditDob = (ImageView) findViewById(R.id.ivEditDob);
        ivDeleteDob = (ImageView) findViewById(R.id.ivDeleteDob);
        ivEditDoa = (ImageView) findViewById(R.id.ivEditDoa);
        ivDeleteDoa = (ImageView) findViewById(R.id.ivDeleteDoa);
        ivAddFamilyMember = (ImageView) findViewById(R.id.ivAddFamilyMember);
        ivDeleteSecondaryMobile = (ImageView) findViewById(R.id.ivDeleteSecondaryMobile);

        spBloodGroup = (Spinner) findViewById(R.id.spBloodGroup);
        spPersonalCountry = (Spinner) findViewById(R.id.spPersonalCountry);

        etCountryCodeSecMob = (EditText) findViewById(R.id.etCountryCodeSecMob);
        etSecondaryMobNo = (EditText) findViewById(R.id.etSecondaryMobNo);
        etMemberEmail = (EditText) findViewById(R.id.etMemberEmail);
        etPersonalAddress = (EditText) findViewById(R.id.etPersonalAddress);
        etResidentialCity = (EditText) findViewById(R.id.etResidentialCity);
        etResidentialPinCode = (EditText) findViewById(R.id.etResidentialPinCode);
        etResidentialState = (EditText) findViewById(R.id.etResidentialState);
        etResidentialCountryCode = (EditText) findViewById(R.id.etResidentialCountryCode);
        etResidentialContactNo = (EditText) findViewById(R.id.etResidentialContactNo);
        //etFax = (EditText) findViewById(R.id.etFax);

        etBusinessEmail = (EditText) findViewById(R.id.etBusinessEmail);
        etBusinessDesignation = (EditText) findViewById(R.id.etBusinessDesignation);
        etBusinessName = (EditText) findViewById(R.id.etBusinessName);
        etBusinessAddress = (EditText) findViewById(R.id.etBusinessAddress);
        etBusinessCity = (EditText) findViewById(R.id.etBusinessCity);
        etBusinessPincode = (EditText) findViewById(R.id.etBinessPincode);
        etBusinessState = (EditText) findViewById(R.id.etBusinessState);
        etBusinessPhoneCountryCode = (EditText) findViewById(R.id.etBusinessPhoneCountryCode);
        etBusinessContact = (EditText) findViewById(R.id.etBusinessContact);
        etBusinessFax = (EditText) findViewById(R.id.etBusinessFax);

        etClassification = (EditText) findViewById(R.id.etClassification);
        etKeywords = (EditText) findViewById(R.id.etKeywords);
        etRotaryID = (EditText) findViewById(R.id.etRotaryID);
        etClubDesignation = (EditText) findViewById(R.id.etClubDesignation);
        etDTDesignation = (EditText) findViewById(R.id.etDTDesignation);
        etDonarRecognition = (EditText) findViewById(R.id.etDonarRecognition);

        spBusinessCountry = (Spinner) findViewById(R.id.spBusinessCountry);

        tvDone = (TextView) findViewById(R.id.btnDone);
        tvheader = (TextView) findViewById(R.id.tv_title);

        tvheader.setText("Edit Profile");

        tvDob.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (tvDob.getText().toString().trim().equals("")) {
                    ivDeleteDob.setVisibility(View.GONE);
                } else {
                    ivDeleteDob.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvDoa.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (tvDob.getText().toString().trim().equals("")) {
                    ivDeleteDoa.setVisibility(View.GONE);
                } else {
                    ivDeleteDoa.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //add by satish on 07-06-2019 because tester report issue 'Unable to scroll address if address is of 4 lines'
        etBusinessAddress.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (etBusinessAddress.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });

        //add by satish on 07-06-2019 because tester report issue 'Unable to scroll address if address is of 4 lines'
        etPersonalAddress.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                if (etPersonalAddress.hasFocus()) {

                    v.getParent().requestDisallowInterceptTouchEvent(true);

                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });
    }

    public void initEvents() {
        initAddFamilyMemberEvent();
        countryCodeSelection();
        initDobSelection();
        initDoaSelection();
        initUpdateEvent();
        initSecondaryMobileDelete();

    }

    public void initAllCountrySelection() {
        spBusinessCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    etBusinessPhoneCountryCode.setText(businessCountryList.get(position).getCountryCode());
                }else {
                    etBusinessPhoneCountryCode.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPersonalCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    etResidentialCountryCode.setText(personalCountryList.get(position).getCountryCode());

                }else {
                    etResidentialCountryCode.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void initSecondaryMobileDelete()  {
        ivDeleteSecondaryMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCountryCodeSecMob.setText("");
                etSecondaryMobNo.setText("");
            }
        });
    }

    public void initUpdateEvent() {
//        tvDone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateMember();
//            }
//        });

        bottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMember();
            }
        });
    }

    public void countryCodeSelection() {
        etCountryCodeSecMob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                ArrayList<CountryData> countryList = Utils.getCountryList(context);
                countryList.remove(0);
                final CountrySpAdapter adapter = new CountrySpAdapter(context, R.layout.select_country_item_sp, countryList, true);
                View view = getLayoutInflater().inflate(R.layout.popup_country_selection, null);

                builder.setView(view);
                final AlertDialog countryDialog = builder.create();

                final ListView lvCountries = (ListView) view.findViewById(R.id.lvCountries);
                lvCountries.setAdapter(adapter);
                lvCountries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        etCountryCodeSecMob.setText(adapter.getItem(position).getCountryCode());
                        countryDialog.dismiss();
                    }
                });

                ((ImageView) view.findViewById(R.id.iv_close)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        countryDialog.dismiss();
                    }
                });
                countryDialog.show();
            }
        });


        etBusinessPhoneCountryCode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                ArrayList<CountryData> countryList = Utils.getCountryList(context);
                countryList.remove(0);
                final CountrySpAdapter adapter = new CountrySpAdapter(context, R.layout.select_country_item_sp, countryList, true);
                View view = getLayoutInflater().inflate(R.layout.popup_country_selection, null);

                builder.setView(view);
                final AlertDialog countryDialog = builder.create();

                final ListView lvCountries = (ListView) view.findViewById(R.id.lvCountries);
                lvCountries.setAdapter(adapter);
                lvCountries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        etBusinessPhoneCountryCode.setText(adapter.getItem(position).getCountryCode());
                        spBusinessCountry.setSelection((getIndex(adapter.getList(),adapter.getItem(position).getCountryCode()))+1);
                        countryDialog.dismiss();
                    }
                });

                ((ImageView) view.findViewById(R.id.iv_close)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        countryDialog.dismiss();
                    }
                });
                countryDialog.show();
            }
        });

        etResidentialCountryCode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                ArrayList<CountryData> countryList = Utils.getCountryList(context);
                countryList.remove(0);
                final CountrySpAdapter adapter = new CountrySpAdapter(context, R.layout.select_country_item_sp, countryList, true);
                View view = getLayoutInflater().inflate(R.layout.popup_country_selection, null);

                builder.setView(view);
                final AlertDialog countryDialog = builder.create();

                final ListView lvCountries = (ListView) view.findViewById(R.id.lvCountries);
                lvCountries.setAdapter(adapter);
                lvCountries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        etResidentialCountryCode.setText(adapter.getItem(position).getCountryCode());
                        spPersonalCountry.setSelection((getIndex(adapter.getList(),adapter.getItem(position).getCountryCode()))+1);
                        countryDialog.dismiss();
                    }
                });

                ((ImageView) view.findViewById(R.id.iv_close)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        countryDialog.dismiss();
                    }
                });
                countryDialog.show();
            }
        });
    }

    public void initAddFamilyMemberEvent() {

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                familyCount--;
                FamilyMemberData data = new FamilyMemberData(sessionProfileId, "" + familyCount, "", "", "", "", "", "", "", "", "");
                familyList.add(data);
                renderFamilyMember(data, View.VISIBLE);
            }
        };

        ivAddFamilyMember.setOnClickListener(clickListener);
        tvAddFamilyMember.setOnClickListener(clickListener);
    }

    public void initDobSelection() {

        ivEditDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
                int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(context,android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear + 1;
                        tvDob.setText(dayOfMonth + " " + MONTHS[monthOfYear]);
                        tvDob.setTag(year + "/" + monthOfYear + "/" + dayOfMonth);
                    }
                }, currentYear, currentMonth, currentDay);

                dpd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dpd.setTitle("Date of Birth");
                //dpd.getDatePicker().findViewById(getResources().getIdentifier("year","id","android")).setVisibility(View.GONE);

                dpd.show();
            }
        });

        ivDeleteDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDob.setText("");
                tvDob.setTag("");
            }
        });
    }

    public void initDoaSelection() {
        ivEditDoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
                int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(context,android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear + 1;
                        tvDoa.setText(dayOfMonth + " " + MONTHS[monthOfYear] );
                        tvDoa.setTag(year + "/" + monthOfYear + "/" + dayOfMonth);
                        ivDeleteDoa.setVisibility(View.VISIBLE);
                    }
                }, currentYear, currentMonth, currentDay);
                dpd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dpd.setTitle("Date of Anniversary");
                //dpd.getDatePicker().findViewById(getResources().getIdentifier("year","id","android")).setVisibility(View.GONE);
                dpd.show();
            }
        });

        ivDeleteDoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDoa.setText("");
                tvDoa.setTag("");
            }
        });
    }

    // Loading dynamic fields
    /*Loading dynamic search fields*/
    public void loadDynamicFields() {
        dynamicFieldDatas = profileModel.getDynamicFieldValues(Long.parseLong(profileId));
        if (dynamicFieldDatas.size() == 0) {
            llWrapper.setVisibility(View.GONE);
            return;
        } else {
            llWrapper.setVisibility(View.VISIBLE);
            renderFields();
        }
    }

    public void renderFields() {
        try {
            int n = dynamicFieldDatas.size();

            llDynamicFields.removeAllViews();
            componentTable.clear();


            for (int i = 0; i < n; i++) {
                DynamicFieldData dfData = dynamicFieldDatas.get(i);

                /*String filterID = jsonField.getString("filterID") ,
                        fieldID = jsonField.getString("fieldID"),
                        dbColumnName = jsonField.getString("dbColumnName"),
                        displayName = jsonField.getString("displayName"),
                        ColType = jsonField.getString("ColType"),
                        fieldType = jsonField.getString("fieldType");

                SearchFilter filter = new SearchFilter(filterID, fieldID, dbColumnName, displayName, ColType, fieldType);

                list.add(filter);*/
                String displayName = dfData.getKey();

                String fieldUniqueName = dfData.getUniquekey();
                String fieldType = dfData.getColType();
                String isEditable = dfData.getIsEditable();

                String value = dfData.getValue();
                TextView tvFieldTitle = new TextView(context);
                tvFieldTitle.setText(displayName);
                EditText etFieldValue = new EditText(context);
                etFieldValue.setText(value);
                etFieldValue.setBackground(getResources().getDrawable(R.drawable.border));
                llDynamicFields.addView(tvFieldTitle);
                llDynamicFields.addView(etFieldValue);

                int sizeInDp = 16;

                float scale = getResources().getDisplayMetrics().density;
                int bottomMargin = (int) (sizeInDp * scale + 0.5f);
                int topMargin = (int) (4 * scale + 0.5f);

                int leftPadding = (int) (10 * scale + 0.5f);
                int rightPadding = (int) (10 * scale + 0.5f);
                int topPadding = (int) (5 * scale + 0.5f);
                int bottomPadding = (int) (5 * scale + 0.5f);

                LinearLayout.LayoutParams etParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                etParams.setMargins(0, topMargin, 0, bottomMargin);

                etFieldValue.setLayoutParams(etParams);
                etFieldValue.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
                etFieldValue.setHint(displayName);
                etFieldValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constant.NORMAL_TEXT_SIZE);
                etFieldValue.setSingleLine(true);
                etFieldValue.setTag(dfData);
                etFieldValue.setTag(dfData.getFieldID());
                if (isEditable.equals("0")) {
                    if (!isAdmin.equals("y")) {
                        etFieldValue.setKeyListener(null);



                    }
                }
                componentTable.put(fieldUniqueName, etFieldValue);
            }
        } catch (Exception jse) {
            Utils.log("Error : " + jse);
            jse.printStackTrace();
        }
    }

    public void loadPersonalDetails() {
        try {


            masterData = profileModel.getMasterProfile(profileId);//581429  //profileId
            etMemberName.setText(masterData.getMemberName());
            tvPrimaryMobNo.setText(masterData.getMemberMobile());
            //etSecondaryMobNo.setText(masterData.get);
            etMemberEmail.setText(masterData.getMemberEmail());

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        //if (masterData.getFamilyPic().trim().length() == 0 || masterData.getFamilyPic() == "null" || masterData.getFamilyPic().isEmpty()) {
    }

    public void loadBusinessDetails() {

    }

    int familyCount = -1;

    public void loadFamilyDetails() {

        familyList = profileModel.getFamilyMembers(Long.parseLong(profileId));

        /*if ( familyList.size() == 0 ) {
            FamilyMemberData data = new FamilyMemberData(sessionProfileId, ""+familyCount, "Family Member Name","","","","","","","");
            familyList.add(data);
            familyCount--;
        }*/

        int n = familyList.size();

        for (int i = 0; i < n; i++) {
            final FamilyMemberData familyData = familyList.get(i);
            renderFamilyMember(familyData, View.GONE);
        }

    }

    public void renderFamilyMember(final FamilyMemberData familyData, int layoutVisibility) {

        final LinearLayout familyLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.family_member_layout, null);

        llFamilyMembers.addView(familyLayout);

        RelativeLayout rl_family_details=(RelativeLayout) familyLayout.findViewById(R.id.rl_family_details);
        LinearLayout ll_family_delete=(LinearLayout)familyLayout.findViewById(R.id.ll_family_delete);
        final ImageView ivEditFamilyMember = (ImageView) familyLayout.findViewById(R.id.ivEditFamilyMember);
        ImageView ivDeleteFamilyMember = (ImageView) familyLayout.findViewById(R.id.ivDeleteFamilyMember);
        final TextView tvMemberTitle = (TextView) familyLayout.findViewById(R.id.tvFamilyMemberTitle);
        tvMemberTitle.setText(familyData.getMemberName());

        final EditText etFamilyMemberName = (EditText) familyLayout.findViewById(R.id.etFamilyMemberName);
        etFamilyMemberName.setText(familyData.getMemberName());
        etFamilyMemberName.setTag(familyData.getFamilyMemberId());

        etFamilyMemberName.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    etFamilyMemberName.selectAll();
                }
            }
        });

        etFamilyMemberName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvMemberTitle.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditText etFamilyEmailId = (EditText) familyLayout.findViewById(R.id.etFamilyEmailId);

        etFamilyEmailId.setText(familyData.getEmailID());

        if (layoutVisibility == View.GONE) {
            etFamilyMemberName.requestFocus();
            ivEditFamilyMember.setImageDrawable(context.getResources().getDrawable(R.drawable.down_arrow_01));
        } else {
            ivEditFamilyMember.setImageDrawable(context.getResources().getDrawable(R.drawable.top_arrow));
        }

        final LinearLayout llFamilyWrapper = (LinearLayout) familyLayout.findViewById(R.id.llFamilyDetails);
        llFamilyWrapper.setVisibility(layoutVisibility);

        ivEditFamilyMember.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (llFamilyWrapper.getVisibility() == View.GONE) {
                    llFamilyWrapper.setVisibility(View.VISIBLE);
                    etFamilyMemberName.requestFocus();
                    ivEditFamilyMember.setImageDrawable(context.getResources().getDrawable(R.drawable.top_arrow));
                } else {
                    llFamilyWrapper.setVisibility(View.GONE);
                    ivEditFamilyMember.setImageDrawable(context.getResources().getDrawable(R.drawable.down_arrow_01));
                }
            }
        });

        rl_family_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llFamilyWrapper.getVisibility() == View.GONE) {
                    llFamilyWrapper.setVisibility(View.VISIBLE);
                    etFamilyMemberName.requestFocus();
                    ivEditFamilyMember.setImageDrawable(context.getResources().getDrawable(R.drawable.top_arrow));
                } else {
                    llFamilyWrapper.setVisibility(View.GONE);
                    ivEditFamilyMember.setImageDrawable(context.getResources().getDrawable(R.drawable.down_arrow_01));
                }
            }
        });

        ll_family_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = getLayoutInflater().inflate(R.layout.popup_confrm_delete, null);
                builder.setView(view);

                TextView tvYes = (TextView) view.findViewById(R.id.tv_yes);
                TextView tvNo = (TextView) view.findViewById(R.id.tv_no);
                final AlertDialog dialog = builder.create();

                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deletedFamilyMembers.add(etFamilyMemberName.getTag().toString());
                        LinearLayout removeView = familyLayoutTable.get(familyData.getFamilyMemberId());
                        LinearLayout parent = (LinearLayout) removeView.getParent();

                        parent.removeView(removeView);
                        parent.invalidate();
                        familyLayoutTable.remove(familyData.getFamilyMemberId());
                        dialog.dismiss();
                    }
                });

                tvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        final EditText etFamilyCountry = (EditText) familyLayout.findViewById(R.id.etCountryCodeFamily);

        etFamilyCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                ArrayList<CountryData> countryList = Utils.getCountryList(context);
                countryList.remove(0);
                final CountrySpAdapter adapter = new CountrySpAdapter(context, R.layout.select_country_item_sp, countryList, true);
                View view = getLayoutInflater().inflate(R.layout.popup_country_selection, null);

                builder.setView(view);
                final AlertDialog countryDialog = builder.create();

                final ListView lvCountries = (ListView) view.findViewById(R.id.lvCountries);
                lvCountries.setAdapter(adapter);
                lvCountries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        etFamilyCountry.setText(adapter.getItem(position).getCountryCode());
                        etFamilyCountry.setTag(adapter.getItem(position).getCountryId());
                        countryDialog.dismiss();
                    }
                });

                ((ImageView) view.findViewById(R.id.iv_close)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        countryDialog.dismiss();
                    }
                });
                countryDialog.show();
            }
        });
        EditText etFamilyMobNo = (EditText) familyLayout.findViewById(R.id.etFamilyMobNo);

        try {
            String[] mobileData = familyData.getContactNo().split(" ");
            etFamilyCountry.setTag(familyData.getCountryID());
            etFamilyCountry.setText(mobileData[0]);
            etFamilyMobNo.setText(mobileData[1]);
        } catch (ArrayIndexOutOfBoundsException ioe) {
            Utils.log("Error is : " + ioe);
            ioe.printStackTrace();
            etFamilyCountry.setText("");
            etFamilyMobNo.setText(familyData.getContactNo());
        }

        Spinner spRelationship = (Spinner) familyLayout.findViewById(R.id.spRelationship);
        String relationship = familyData.getRelationship();
        String[] relations = context.getResources().getStringArray(R.array.relation);

        for (int j = 0; j < relations.length; j++) {

            if (relations[j].equals(relationship)) {
                spRelationship.setSelection(j);
                break;
            }
        }

        final TextView tvDob = (TextView) familyLayout.findViewById(R.id.tvFamilyDob);

        ImageView ivEditFamilyDob = (ImageView) familyLayout.findViewById(R.id.ivEditFamilyDob);
        final ImageView ivDeleteFamilyDob = (ImageView) familyLayout.findViewById(R.id.ivDeleteFamilyDob);


        ivEditFamilyDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
                int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(context,android.R.style.Theme_Holo_Light_Dialog,  new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear + 1;
                        tvDob.setText(dayOfMonth + " " + MONTHS[monthOfYear]);
                        tvDob.setTag(year + "/" + monthOfYear + "/" + dayOfMonth);
                    }
                }, currentYear, currentMonth, currentDay);
                dpd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dpd.setTitle("Date of Birth");
               // dpd.getDatePicker().findViewById(getResources().getIdentifier("year","id","android")).setVisibility(View.GONE);

                dpd.show();
            }
        });

        ivDeleteFamilyDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDob.setText("");
                tvDob.setTag("");
            }
        });


        tvDob.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tvDob.getText().toString().trim().equals("")) {
                    ivDeleteFamilyDob.setVisibility(View.GONE);
                } else {
                    ivDeleteFamilyDob.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
       // tvDob.setText(familyData.getDob());
        tvDob.setTag(familyData.getDob());
        try {
            String[] dateFields = familyData.getDob().split("/");
            tvDob.setText(dateFields[0] + " " + MONTHS[Integer.parseInt(dateFields[1])]);
            tvDob.setTag(dateFields[2] + "/" + dateFields[1] + "/" + dateFields[0]);
        } catch (ArrayIndexOutOfBoundsException iae) {
            tvDob.setTag("");
            tvDob.setText(familyData.getDob());
        }
        final TextView tvDoa = (TextView) familyLayout.findViewById(R.id.tvFamilyDoa);

        ImageView ivEditFamilyDoa = (ImageView) familyLayout.findViewById(R.id.ivEditFamilyDoa);
        final ImageView ivDeleteFamilyDoa = (ImageView) familyLayout.findViewById(R.id.ivDeleteFamilyDoa);

        tvDoa.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (tvDoa.getText().toString().trim().equals("")) {
                    ivDeleteFamilyDoa.setVisibility(View.GONE);
                } else {
                    ivDeleteFamilyDoa.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        try {
            String[] dateFields = familyData.getAnniversary().split("/");
            tvDoa.setTag(dateFields[2] + "/" + dateFields[1] + "/" + dateFields[0]);
            tvDoa.setText(dateFields[0]+" "+MONTHS[Integer.parseInt(dateFields[1])]);
        } catch (ArrayIndexOutOfBoundsException iae) {
            tvDoa.setTag("");
            tvDoa.setText(familyData.getAnniversary());
        }

        ivEditFamilyDoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
                int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(context,android.R.style.Theme_Holo_Light_Dialog,  new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear + 1;
                        tvDoa.setText(dayOfMonth + " " + MONTHS[monthOfYear]);
                        tvDoa.setTag(year + "/" + monthOfYear + "/" + dayOfMonth);
                    }
                }, currentYear, currentMonth, currentDay);
                dpd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dpd.setTitle("Date of Anniversary");
               // dpd.getDatePicker().findViewById(getResources().getIdentifier("year","id","android")).setVisibility(View.GONE);

                dpd.show();
            }
        });

        ivDeleteFamilyDoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDoa.setText("");
                tvDoa.setTag("");
            }
        });

        Spinner spBloodGroup = (Spinner) familyLayout.findViewById(R.id.spFamilyBloodGroup);
        String bloodGroup = familyData.getBloodGroup();

        String[] bloodGroups = context.getResources().getStringArray(R.array.bloodGroup);

        for (int j = 0; j < bloodGroups.length; j++) {
            if (bloodGroups[j].equals(bloodGroup)) {
                spBloodGroup.setSelection(j);
                break;
            }
        }

        familyLayoutTable.put(familyData.getFamilyMemberId(), familyLayout);
    }

    public void loadStaticFields() {
        staticDataTable = profileModel.getStaticFieldValues(Long.parseLong(profileId));
        Utils.log("Static Field Data : " + staticDataTable);
        Utils.log(new Gson().toJson(staticDataTable).toString());
        try {
            String secondaryMobileNo = staticDataTable.get("secondry_mobile_no").getValue();
            String[] phoneFields = secondaryMobileNo.split(" ");
            try {
                etCountryCodeSecMob.setText(phoneFields[0]);
                etSecondaryMobNo.setText(phoneFields[1]);
                ivDeleteSecondaryMobile.setVisibility(View.VISIBLE);

            } catch (ArrayIndexOutOfBoundsException aie) {
                Utils.log("Error while loading secondary mobile no. : " + aie);
                etCountryCodeSecMob.setText("");

                if (secondaryMobileNo.trim().equals("") || secondaryMobileNo.trim().isEmpty()) {
                    ivDeleteSecondaryMobile.setVisibility(View.GONE);
                } else {
                    etSecondaryMobNo.setText(secondaryMobileNo);
                    ivDeleteSecondaryMobile.setVisibility(View.VISIBLE);
                }
            }

        } catch (NullPointerException npe) {
            //etCountryCodeSecMob.setText("");
            etSecondaryMobNo.setText("");
            Utils.log("No value for secondary_mob_no in table. Error while loading is : " + npe);
            ivDeleteSecondaryMobile.setVisibility(View.GONE);
        }
        try {
            String dob = staticDataTable.get("member_date_of_birth").getValue();

            try {
                String[] dateFields = dob.split("/");
                tvDob.setText(dateFields[0] + " " + MONTHS[Integer.parseInt(dateFields[1])]);
                tvDob.setTag(dateFields[2] + "/" + dateFields[1] + "/" + dateFields[0]);
            } catch (ArrayIndexOutOfBoundsException iae) {

            }
        } catch (NullPointerException npe) {
            Utils.log("No value for member_date_of_birth. Error is : " + npe);
            tvDob.setText("");
            tvDob.setTag("");
        }

        try {
            String doa = staticDataTable.get("member_date_of_wedding").getValue();
            //tvDoa.setText(doa);
            try {
                String[] dateFields = doa.split("/");
                tvDoa.setTag(dateFields[2] + "/" + dateFields[1] + "/" + dateFields[0]);
                tvDoa.setText(dateFields[0] + " " + MONTHS[Integer.parseInt(dateFields[1])] );
                ivDeleteDoa.setVisibility(View.VISIBLE);
            } catch (ArrayIndexOutOfBoundsException iae) {
                tvDoa.setText("");
                tvDoa.setTag("");
                ivDeleteDoa.setVisibility(View.GONE);
            }
        } catch (NullPointerException npe) {
            Utils.log("No value for member_date_of_wedding. Error is : " + npe);
            tvDoa.setText("");
            tvDoa.setTag("");
        }
        try {
            String bloodGroup = staticDataTable.get("blood_Group").getValue();
            String[] bloodGroupAr = context.getResources().getStringArray(R.array.bloodGroup);
            int n = bloodGroupAr.length;
            for (int i = 0; i < n; i++) {
                if (bloodGroup.equalsIgnoreCase(bloodGroupAr[i])) {
                    spBloodGroup.setSelection(i);
                    break;
                }
            }

        } catch (NullPointerException npe) {
            Utils.log("No value for . Error is : " + npe);
            spBloodGroup.setSelection(0);
        }

        try {
            String member_buss_email = staticDataTable.get("member_buss_email").getValue();
            etBusinessEmail.setText(member_buss_email);
            etBusinessEmail.setTag(staticDataTable.get("member_buss_email").getFieldID());
        } catch (NullPointerException npe) {
            Utils.log("No value for member_buss_email. Error is : " + npe);
            etBusinessEmail.setText("");
        }
        try {
            String BusinessName = staticDataTable.get("BusinessName").getValue();
            etBusinessName.setText(BusinessName);
            etBusinessName.setTag(staticDataTable.get("BusinessName").getFieldID());
        } catch (NullPointerException npe) {
            Utils.log("No value for BusinessName. Error is : " + npe);
            etBusinessName.setText("");
        }

        // Needs to modify keys
        try {
            String designation = staticDataTable.get("businessPosition").getValue();
            etBusinessDesignation.setText(designation);
            etBusinessDesignation.setTag(staticDataTable.get("businessPosition").getFieldID());
        } catch (NullPointerException npe) {
            Utils.log("No value for designation. Error is : " + npe);
            etBusinessDesignation.setText("");
            etBusinessDesignation.setTag("");
        }
        try {
            String address = staticDataTable.get("Address").getValue();
            etBusinessAddress.setText(address);

        } catch (NullPointerException npe) {
            Utils.log("No value for business address. Error is : " + npe);
            etBusinessAddress.setText("");
        }

        try {
            String city = staticDataTable.get("fk_city_id").getValue();
            etBusinessCity.setText(city);

        } catch (NullPointerException npe) {
            Utils.log("No value for business city. Error is : " + npe);
            etBusinessCity.setText("");
        }
        try {
            String pincode = staticDataTable.get("pincode").getValue();
            etBusinessPincode.setText(pincode);
        } catch (NullPointerException npe) {
            Utils.log("No value for business pincode. Error is : " + npe);
            etBusinessPincode.setText("");
        }

        try {
            String state = staticDataTable.get("fk_state_id").getValue();
            etBusinessState.setText(state);
        } catch (NullPointerException npe) {
            Utils.log("No value for business pincode. Error is : " + npe);
            etBusinessState.setText("");
        }


        /*try {
            String phoneNo = staticDataTable.get("phone_no").getValue();
            try {
                String[] phoneAr = phoneNo.split(" ");
                etBusinessPhoneCountryCode.setText(phoneAr[0]);
                etBusinessContact.setText(phoneAr[1]);
            } catch (ArrayIndexOutOfBoundsException aie) {
                etBusinessPhoneCountryCode.setText("");
                etBusinessContact.setText(phoneNo);
            }

        } catch (NullPointerException npe) {
            Utils.log("No value for business phone number. Error is : " + npe);
            etBusinessContact.setText("");
        }*/

        try {
            String fax = staticDataTable.get("fax").getValue();
            etBusinessFax.setText(fax);

        } catch (NullPointerException npe) {
            Utils.log("No value for business fax. Error is : " + npe);
            etBusinessFax.setText("");
        }

        try {
            String classification = staticDataTable.get("designation").getValue();
            etClassification.setText(classification);
        } catch (Exception e) {
            Utils.log("No value for classification. Error is : " + e);
        }

        try {
            String keywords = staticDataTable.get("Keywords").getValue();
            etKeywords.setText(keywords);
        } catch (Exception e) {
            Utils.log("No value for Keywords. Error is : " + e);
        }

        try {
            String rotaryID = staticDataTable.get("member_rotary_id").getValue();
            etRotaryID.setText(rotaryID);
        } catch (Exception e) {
            Utils.log("No value for rotary ID. Error is : " + e);
        }

        try {
            String clubDesignation = staticDataTable.get("member_master_designation").getValue();
            etClubDesignation.setText(clubDesignation);
        } catch (Exception e) {
            Utils.log("No value for club designation. Error is : " + e);
        }

        try {
            String districtDesignation = staticDataTable.get("dg_master_designation").getValue();
            etDTDesignation.setText(districtDesignation);
        } catch (Exception e) {
            Utils.log("No value for district Designation. Error is : " + e);
        }

        try {
            String donarRecognition = staticDataTable.get("rotary_donar_recognation").getValue();
            etDonarRecognition.setText(donarRecognition);
        } catch (Exception e) {
            Utils.log("No value for donar recognition. Error is : " + e);
        }
    }

    public void loadAddresses() {
        addressesTable = profileModel.getAddresses(Long.parseLong(profileId));
        Utils.log("Address table is : " + addressesTable);
    }

    public void loadPersonalAddress() {
        try {
            AddressData address = addressesTable.get("Residence");
            etPersonalAddress.setTag(address.getAddressID());
            etPersonalAddress.setText(address.getAddress());
            etResidentialCity.setText(address.getCity());
            etResidentialState.setText(address.getState());
            etResidentialPinCode.setText(address.getPincode());
            //actvPersonalCountry.setText(address.getCountry());
            int n = personalCountryList.size();
            for (int i = 0; i < n; i++) {
                String name = personalCountryList.get(i).getCountryName();
                if (name.equalsIgnoreCase(address.getCountry())) {
                    //etResidentialCountryCode.setText(personalCountryList.get(i).getCountryCode());
                    spPersonalCountry.setSelection(i, true);
                    break;
                }
            }
            String[] residentialContactNo = address.getPhoneNo().split(" ");
            try {
                etResidentialCountryCode.setText(residentialContactNo[0]);
                etResidentialContactNo.setText(residentialContactNo[1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                etResidentialCountryCode.setText("");
                etResidentialContactNo.setText(address.getPhoneNo());
            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            etPersonalAddress.setTag("0");
        }
    }

    public void loadBusinessAddress() {
        try {
            AddressData address = addressesTable.get("Business");
            etBusinessAddress.setTag(address.getAddressID());
            etBusinessAddress.setText(address.getAddress());
            etBusinessCity.setText(address.getCity());
            etBusinessState.setText(address.getState());
            etBusinessPincode.setText(address.getPincode());
            //actvBusinessCountry.setText(address.getCountry());
            int n = personalCountryList.size();
            for (int i = 0; i < n; i++) {
                String name = businessCountryList.get(i).getCountryName();
                if (name.equalsIgnoreCase(address.getCountry())) {
                    //etBusinessPhoneCountryCode.setText(businessCountryList.get(i).getCountryCode());
                    spBusinessCountry.setSelection(i, true);
                    break;
                }
            }
            String[] businessPhoneNo = address.getPhoneNo().split(" ");
            try {
                etBusinessPhoneCountryCode.setText(businessPhoneNo[0]);
                etBusinessContact.setText(businessPhoneNo[1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                etBusinessPhoneCountryCode.setText("");
                etBusinessContact.setText(address.getPhoneNo());
            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            etBusinessAddress.setTag("0");
        }
    }

    public void updateMember() {

        if (!isValidForm()) {
            return;
        }

        Hashtable<String, Object> params = new Hashtable<>();

        params.put("profileID", profileId);

        Hashtable<String, String> personalMemberDetails = new Hashtable<>();
        personalMemberDetails.put("memberName", etMemberName.getText().toString());
        personalMemberDetails.put("memberEmail", etMemberEmail.getText().toString());

        try {
            personalMemberDetails.put("memberDOB", tvDob.getTag().toString());
        } catch (NullPointerException npe) {
            personalMemberDetails.put("memberDOB", "");
        }
        try {
            personalMemberDetails.put("memberDOA", tvDoa.getTag().toString());
        } catch (NullPointerException npe) {
            personalMemberDetails.put("memberDOA", "");
        }

        personalMemberDetails.put("profilePic", "");
        personalMemberDetails.put("classification", etClassification.getText().toString());
        personalMemberDetails.put("keywords", etKeywords.getText().toString());
        personalMemberDetails.put("rotaryId", etRotaryID.getText().toString());
        personalMemberDetails.put("clubDesignation", etClubDesignation.getText().toString());
        personalMemberDetails.put("districtDesignation", etDTDesignation.getText().toString());
        personalMemberDetails.put("donarRecognition", etDonarRecognition.getText().toString());

        if(etSecondaryMobNo.getText().toString().trim().isEmpty()){
            personalMemberDetails.put("secondaryMobileNo", "");

        }else {
            String secondaryMobileNo = etSecondaryMobNo.getText().toString();
            try {
                while (secondaryMobileNo.charAt(0) == '0') {
                    secondaryMobileNo = secondaryMobileNo.substring(1);
                }
            } catch (ArrayIndexOutOfBoundsException aie) {
                aie.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            personalMemberDetails.put("secondaryMobileNo", etCountryCodeSecMob.getText().toString() + " " + secondaryMobileNo);

        }

        if (spBloodGroup.getSelectedItemPosition() == 0) {
            personalMemberDetails.put("bloodGroup", "");
        } else {
            personalMemberDetails.put("bloodGroup", spBloodGroup.getSelectedItem().toString());
        }
        // etPersonalAddress contains addressID as tag value
        String residenceNumber = etResidentialContactNo.getText().toString();
        try {
            while (residenceNumber.charAt(0) == '0') {
                residenceNumber = residenceNumber.substring(1);
            }
        } catch (ArrayIndexOutOfBoundsException aie) {
            aie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Utils.log(""+spPersonalCountry.getSelectedItemPosition());
//        if(etPersonalAddress.getText().toString().trim().isEmpty() && etResidentialCity.getText().toString().trim().isEmpty() && etResidentialState.getText().toString().trim().isEmpty() && etResidentialPinCode.getText().toString().trim().isEmpty() && etResidentialCountryCode.getText().toString().trim().isEmpty() && etResidentialContactNo.getText().toString().trim().isEmpty() && spPersonalCountry.getSelectedItemPosition()<=0 ){
//            residential_addressId=etPersonalAddress.getTag().toString();
//        }else if(etPersonalAddress.getText().toString().trim().isEmpty() && etResidentialCity.getText().toString().trim().isEmpty() && etResidentialState.getText().toString().trim().isEmpty() ){
//            residential_addressId="-1";
//        }
//        else {
//            residential_addressId=etPersonalAddress.getTag().toString();
//        }

        residential_addressId=etPersonalAddress.getTag().toString();
        AddressData personalAddress = new AddressData(
                profileId,
                residential_addressId,
                "Residence",
                etPersonalAddress.getText().toString(),
                etResidentialCity.getText().toString(),
                etResidentialState.getText().toString(),
                ((CountryData) spPersonalCountry.getSelectedItem()).getCountryId(),
                etResidentialPinCode.getText().toString(),
                residenceNumber,
                "");

        String businessNumber = etBusinessContact.getText().toString();
        try {
            while (businessNumber.charAt(0) == '0') {
                businessNumber = businessNumber.substring(1);
            }
        } catch (ArrayIndexOutOfBoundsException aie) {
            aie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // etBusinessAddress contains addressID as tag value

//        if(etBusinessAddress.getText().toString().trim().isEmpty() && etBusinessCity.getText().toString().trim().isEmpty() && etBusinessState.getText().toString().trim().isEmpty() ){
//            bussiness_addressId="-1";
//        }
//        else {
//            bussiness_addressId=etBusinessAddress.getTag().toString();
//        }

        bussiness_addressId=etBusinessAddress.getTag().toString();

        AddressData businessAddress = new AddressData(
                profileId,

                bussiness_addressId,
                "Business",
                etBusinessAddress.getText().toString(),
                etBusinessCity.getText().toString(),
                etBusinessState.getText().toString(),
                ((CountryData) spBusinessCountry.getSelectedItem()).getCountryId(),
                etBusinessPincode.getText().toString(),
                businessNumber,
                etBusinessFax.getText().toString());

        ArrayList<AddressData> addressList = new ArrayList<>();
        addressList.add(personalAddress);
        addressList.add(businessAddress);

        Enumeration<String> dfEnum = componentTable.keys();

        ArrayList<Hashtable<String, String>> dynamicFieldsData = new ArrayList<>();
        while (dfEnum.hasMoreElements()) {
            String uniqueKey = dfEnum.nextElement();
            EditText etValue = componentTable.get(uniqueKey);
            Hashtable<String, String> fieldValue = new Hashtable<>();
            fieldValue.put("fieldID", etValue.getTag().toString());
            fieldValue.put("uniqueKey", uniqueKey);
            fieldValue.put("value", etValue.getText().toString());
            Utils.log("Field : " + uniqueKey + " value : " + etValue.getText().toString());
            dynamicFieldsData.add(fieldValue);
        }
        // Family details
        Enumeration<String> familyEnum = familyLayoutTable.keys();
        ArrayList<FamilyMemberData> familyList = new ArrayList<>();

        while (familyEnum.hasMoreElements()) {
            String familyMemberId = familyEnum.nextElement();
            LinearLayout familyLayout = familyLayoutTable.get(familyMemberId);
            String memberName = ((EditText) familyLayout.findViewById(R.id.etFamilyMemberName)).getText().toString();
            String relationship = ((Spinner) familyLayout.findViewById(R.id.spRelationship)).getSelectedItem().toString();
            String dob = ((TextView) familyLayout.findViewById(R.id.tvFamilyDob)).getTag().toString();
            String emailID = ((EditText) familyLayout.findViewById(R.id.etFamilyEmailId)).getText().toString();
            String anniversary = ((TextView) familyLayout.findViewById(R.id.tvFamilyDoa)).getTag().toString();
            String familyNumber = ((EditText) familyLayout.findViewById(R.id.etFamilyMobNo)).getText().toString();
            try {
                while (familyNumber.charAt(0) == '0') {
                    familyNumber = familyNumber.substring(1);
                }
            } catch (ArrayIndexOutOfBoundsException aie) {
                aie.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String contactNo = familyNumber;
            String particulars = "";
            Spinner spFamilyMemberBloodGroup = (Spinner) familyLayout.findViewById(R.id.spFamilyBloodGroup);
            String bloodGroup = "";
            String countryId = ((EditText) familyLayout.findViewById(R.id.etCountryCodeFamily)).getTag().toString();
            if (spFamilyMemberBloodGroup.getSelectedItemPosition() != 0) {
                bloodGroup = spFamilyMemberBloodGroup.getSelectedItem().toString();
            }

            if (Long.parseLong(familyMemberId) < 0) {
                familyMemberId = "0";
            }
            FamilyMemberData familyData = new FamilyMemberData(profileId, familyMemberId, memberName, relationship, dob, emailID, anniversary, contactNo, particulars, bloodGroup, countryId);
            familyList.add(familyData);
        }

        ArrayList<Hashtable<String, String>> businessList = new ArrayList<>();

        Hashtable<String, String> businessEmail = new Hashtable<>();
        businessEmail.put("fieldID", etBusinessEmail.getTag().toString());
        businessEmail.put("uniquekey", "member_buss_email");
        businessEmail.put("value", etBusinessEmail.getText().toString());

        Hashtable<String, String> businessName = new Hashtable<>();

        businessName.put("uniquekey", "BusinessName");
        businessName.put("value", etBusinessName.getText().toString());
        businessName.put("fieldID", etBusinessName.getTag().toString());

        Hashtable<String, String> businessDesignation = new Hashtable<>();
        businessDesignation.put("uniquekey", "businessPosition");
        businessDesignation.put("value", etBusinessDesignation.getText().toString());
        businessDesignation.put("fieldID", etBusinessDesignation.getTag().toString());

        businessList.add(businessEmail);
        businessList.add(businessName);
        businessList.add(businessDesignation);

        params.put("personalMemberDetails", personalMemberDetails);
        params.put("addressDetails", addressList);
        params.put("dynamicFields", dynamicFieldsData);
        params.put("familyMemberDetail", familyList);
        params.put("businessMemberDetails", businessList);
        params.put("deletedFamilyMemberIds", Utils.implode(",", deletedFamilyMembers));
        params.put("updatedOn", updatedOn);
        params.put("grpID", "" + grpId);
        try {
            JSONObject requestData = new JSONObject(new Gson().toJson(params));
            final ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("Updating profile. Please wait.");
            pd.setCancelable(false);

            Utils.log("Requesttt " + Constant.NewUpdateProfile + " [ data is ] : " + requestData);
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constant.NewUpdateProfile, requestData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            pd.dismiss();
                            handleSyncInfo(response);

                            String pro = getIntent().getStringExtra("pro");//    intent1.putExtra( "profileId", pp);

                            if(pro == null)
                            {

                            }
                            else
                            {
                                Intent in = new Intent(getApplicationContext(),NewProfileActivity.class);
                                in.putExtra("myproflag","1");
                                startActivity(in);
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.dismiss();
                            Utils.log("Error is : " + error);
                        }
                    }
            );
            request.setRetryPolicy(new DefaultRetryPolicy(
                    Constant.VOLLEY_MAX_REQUEST_TIMEOUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            AppController.getInstance().addToRequestQueue(context, request);
            pd.show();
        } catch (JSONException jsone) {
            Utils.log("Exception is : " + jsone);
            jsone.printStackTrace();
        }
    }

    public boolean isValidForm() {

        if (etCountryCodeSecMob.getText().toString().trim().equals("") && etSecondaryMobNo.getText().toString().trim().length() > 0) {
            Toast.makeText(context, "Please select country code for secondary mobile number", Toast.LENGTH_LONG).show();
            etCountryCodeSecMob.requestFocus();
            showKeyboard(etCountryCodeSecMob);
            return false;
        }

//        if (etSecondaryMobNo.getText().toString().trim().length() == 0) {
//            Toast.makeText(context, "Please enter secondary mobile number", Toast.LENGTH_LONG).show();
//            etSecondaryMobNo.requestFocus();
//            showKeyboard(etSecondaryMobNo);
//            return false;
//        }

        String emailId = etMemberEmail.getText().toString().trim();
        if (emailId.length() > 0) {
            if (!(Utils.isValidEmailId(emailId))) {
                Toast.makeText(context, "Please enter valid email id", Toast.LENGTH_LONG).show();
                etMemberEmail.requestFocus();
                showKeyboard(etMemberEmail);
                return false;
            }
        }

        String bemailId = etBusinessEmail.getText().toString().trim();
        if (bemailId.length() > 0) {
            if (!(Utils.isValidEmailId(bemailId))) {
                Toast.makeText(context, "Please enter valid email id", Toast.LENGTH_LONG).show();
                etBusinessEmail.requestFocus();
                showKeyboard(etBusinessEmail);
                return false;
            }
        }

        if (etResidentialCountryCode.getText().toString().trim().equals("") && etResidentialContactNo.getText().toString().trim().length() > 0) {
            Toast.makeText(context, "Please select country code for residential contact number", Toast.LENGTH_LONG).show();
            etResidentialCountryCode.requestFocus();
            showKeyboard(etResidentialCountryCode);
            return false;
        }

//        if (etResidentialCountryCode.getText().toString().trim().length() > 0 && etResidentialContactNo.getText().toString().trim().length() == 0) {
//            Toast.makeText(context, "Please enter residential contact number", Toast.LENGTH_LONG).show();
//            etResidentialContactNo.requestFocus();
//            showKeyboard(etResidentialContactNo);
//            return false;
//        }

        if (!etPersonalAddress.getText().toString().trim().equals("") && spPersonalCountry.getSelectedItemPosition() == 0) {
            spPersonalCountry.setFocusable(true);
            spPersonalCountry.setFocusableInTouchMode(true);
            spPersonalCountry.requestFocus();
            Toast.makeText(context, "Please select country for residential address", Toast.LENGTH_LONG).show();
            return false;
        }

        if (etBusinessPhoneCountryCode.getText().toString().trim().equals("") && etBusinessContact.getText().toString().trim().length() > 0) {
            Toast.makeText(context, "Please select country code for business contact number", Toast.LENGTH_LONG).show();
            etBusinessPhoneCountryCode.requestFocus();
            showKeyboard(etBusinessPhoneCountryCode);
            return false;
        }

//        if (etBusinessPhoneCountryCode.getText().toString().trim().length() > 0 && etBusinessContact.getText().toString().trim().length() == 0) {
//            Toast.makeText(context, "Please enter business contact number", Toast.LENGTH_LONG).show();
//            etBusinessContact.requestFocus();
//            showKeyboard(etBusinessContact);
//            return false;
//        }

        if (!etBusinessAddress.getText().toString().trim().equals("") && spBusinessCountry.getSelectedItemPosition() == 0) {
            spBusinessCountry.setFocusable(true);
            spBusinessCountry.setFocusableInTouchMode(true);
            spBusinessCountry.requestFocus();
            Toast.makeText(context, "Please select country for business address", Toast.LENGTH_LONG).show();

            return false;
        }

        Enumeration<String> familyMembers = familyLayoutTable.keys();
        while (familyMembers.hasMoreElements()) {
            String member = familyMembers.nextElement();
            LinearLayout familyLayout = familyLayoutTable.get(member);
            if (((EditText) familyLayout.findViewById(R.id.etFamilyMemberName)).getText().toString().trim().equals("")) {
                ((EditText) familyLayout.findViewById(R.id.etFamilyMemberName)).requestFocus();
                showKeyboard((EditText) familyLayout.findViewById(R.id.etFamilyMemberName));
                Toast.makeText(context, "Please enter name of family member", Toast.LENGTH_LONG).show();
                return false;
            }

//            if (((Spinner) familyLayout.findViewById(R.id.spRelationship)).getSelectedItemPosition() == 0) {
//                ((Spinner) familyLayout.findViewById(R.id.spRelationship)).requestFocus();
//                Toast.makeText(context, "Please select relation with family member", Toast.LENGTH_LONG).show();
//                return false;
//            }

//            EditText etFamilyEmail = (EditText)familyLayout.findViewById(R.id.etFamilyEmailId);
//            String familyEmail = etFamilyEmail.getText().toString().trim();
//
//            if ( ! (familyEmail.length() > 0  && Utils.isValidEmailId(familyEmail))) {
//                etFamilyEmail.requestFocus();
//                showKeyboard(etFamilyEmail);
//                Toast.makeText(context, "Please enter valid email id of family member", Toast.LENGTH_LONG).show();
//                return false;
//            }

            EditText etCountryCode = (EditText) familyLayout.findViewById(R.id.etCountryCodeFamily);
            EditText etFamilyMobNo = (EditText) familyLayout.findViewById(R.id.etFamilyMobNo);
            if (etCountryCode.getText().toString().trim().equals("") && etFamilyMobNo.getText().toString().trim().length() > 0) {
                Toast.makeText(context, "Please select country code for family member contact number", Toast.LENGTH_LONG).show();
                etCountryCode.requestFocus();
                showKeyboard(etCountryCode);
                return false;
            }

            if (etCountryCode.getText().toString().trim().length() > 0 && etFamilyMobNo.getText().toString().trim().length() == 0) {
                Toast.makeText(context, "Please enter family member contact number", Toast.LENGTH_LONG).show();
                etFamilyMobNo.requestFocus();
                showKeyboard(etFamilyMobNo);
                return false;
            }
        }
        return true;
    }

    /*Syncing local database for profile data with server updated record*/
    public void getProfileSyncInfo() {
        //final ProgressDialog pd = new ProgressDialog(context);
        //pd.setCancelable(false);
        if (firstTime.equalsIgnoreCase("yes")) {
            //pd.setMessage();
            manageCenterMessage(
                    View.VISIBLE,
                    "Downloading contacts for the first time.\nPlease wait",
                    View.VISIBLE,
                    "",
                    View.GONE,
                    null, ANIMATE);
        } else {
            //pd.setMessage("Loading please wait");
            manageCenterMessage(
                    View.GONE,
                    "Checking for updates. Please wait",
                    View.VISIBLE,
                    "",
                    View.GONE,
                    null, ANIMATE);
        }

        Utils.log("Started getProfileSyncInfo");
        Hashtable<String, String> paramTable = new Hashtable<>();
        updatedOn = PreferenceManager.getPreference(this, TBPrefixes.DIRECTORY_PREFIX + grpId, "1970/01/01 00:00:00");

        paramTable.put("updatedOn", updatedOn);
        paramTable.put("grpID", "" + grpId);

        JSONObject jsonRequestData = null;
        try {
            jsonRequestData = new JSONObject(new Gson().toJson(paramTable));
            Utils.log("Url : " + Constant.GetMemberListSync + " Data : " + jsonRequestData);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    Constant.GetMemberListSync,
                    jsonRequestData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            manageCenterMessage(View.GONE, "", View.GONE, "", View.GONE, null, NO_ANIMATE);
                            //Utils.log("Success : " + response);
                            handleSyncInfo(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            /*if (list.size()==0) {
                                manageCenterMessage(
                                        View.VISIBLE,
                                        "Something went wrong. Please try again after sometime.",
                                        View.VISIBLE,
                                        "Try Now",
                                        View.VISIBLE,
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                llCenterMessage.setVisibility(View.GONE);
                                                loadFromDB();
                                            }
                                        }, NO_ANIMATE);
                            }*/
                            Utils.log("Error is : " + error);
                            error.printStackTrace();
                        }
                    });
            request.setRetryPolicy(new DefaultRetryPolicy(
                    Constant.VOLLEY_MAX_REQUEST_TIMEOUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            AppController.getInstance().addToRequestQueue(context, request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void handleSyncInfo(JSONObject response) {
        try {
            Utils.log("Response is : " + response);
            String status = response.getString("status");
            if (status.equals("0")) {

                if(bussiness_addressId.equals("-1")){

                    new ProfileModel(context).deleteAddress(profileId,"Business");

                }

                if(residential_addressId.equals("-1")){

                    new ProfileModel(context).deleteAddress(profileId,"Residence");

                }

                updatedOn = response.getString("curDate");
                try {
                    zipUrl = response.getString("zipFilePath");
                } catch (JSONException jsone) {
                    zipUrl = "";
                }

                Utils.log("Zip File Path : " + zipUrl);
                PreferenceManager.savePreference(context, TBPrefixes.TEMP_UPDATED_ON + grpId, updatedOn);
                if (zipUrl.trim().equals("")) {
                    processDirectRecords(response);
                } else { // means zip file is available for download
                    if (permission.checkPermissionForExternalStorage()) {
                        startDownload();
                    } else {
                        permission.requestPermissionForExternalStorage();
                    }
                }
            }
        } catch (JSONException je) {
            Utils.log("Error is : " + je);
            je.printStackTrace();
            /*manageCenterMessage(
                    View.VISIBLE,
                    "Something went wrong. Please try again after sometime.",
                    View.VISIBLE,
                    "Try Now",
                    View.VISIBLE,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            llCenterMessage.setVisibility(View.GONE);
                            loadFromDB();
                        }
                    }, NO_ANIMATE);*/
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MarshMallowPermission.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            if (permission.checkPermissionForExternalStorage()) {
                startDownload();
            } else {
                Toast.makeText(context, "Operation cannot be completed because of permission.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public void startDownload() {
        Utils.log("Starting downloading file");
        File sdCard = Environment.getExternalStorageDirectory();
        downloadDir = new File(sdCard, "Touchbase/temp");
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }
        tempFile = new File(downloadDir, "directory_" + new Date().getTime() + ".zip");
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(zipUrl));
        request.setVisibleInDownloadsUi(false);
        request.setDestinationUri(Uri.fromFile(tempFile));
        downloadId = downloadManager.enqueue(request);
        Utils.log("Files is added to the download queue");
        /*manageCenterMessage(
                View.VISIBLE,
                "Processing and storing offline",
                View.VISIBLE,
                "",
                View.GONE,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llCenterMessage.setVisibility(View.GONE);
                        loadFromDB();
                    }
                }, ANIMATE);*/
        //loadDynamicFieldsFromServer();
    }

    public class DownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String actionString = intent.getAction();
            if (actionString.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                try {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor cur = downloadManager.query(query);

                    if (cur.moveToNext()) {
                        int status = cur.getInt(cur.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            UnzipUtility uu = new UnzipUtility();
                            try {
                                File unzipDir = new File(downloadDir.getPath() + File.separator + tempFile.getName().replaceAll(".zip", ""));
                                unzipDir.mkdir();
                                uu.unzip(tempFile.getPath(), unzipDir.getPath());
                                tempFile.delete();  // delete zip file after extracting
                                PreferenceManager.savePreference(context, Constant.UPDATE_FILE_NAME, unzipDir.getPath());





                                Intent serviceIntent = new Intent(context, DirectoryUpdateService.class);
                                serviceIntent.putExtra("directoryPath", unzipDir.getPath());
                                context.startService(serviceIntent);
                                Utils.log("Download completed and services is processing the updates");
                                deleteServerFile();
                            } catch (IOException e) {
                                Utils.log("Error is : " + e);
                                e.printStackTrace();
                                showRetryDialog();
                            }
                        } else {
                            Utils.log("Failed to download update file");
                            /*manageCenterMessage(
                                    View.VISIBLE,
                                    "Failed to download update file",
                                    View.VISIBLE,
                                    "Try Now",
                                    View.VISIBLE,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            llCenterMessage.setVisibility(View.GONE);
                                            loadFromDB();
                                        }
                                    }, NO_ANIMATE);*/
                        }
                    }
                } catch (NullPointerException npe) {
                    Utils.log("Error is : " + npe);
                    npe.printStackTrace();
                    /*manageCenterMessage(
                            View.VISIBLE,
                            "Failed to download update file",
                            View.VISIBLE,
                            "Try Now",
                            View.VISIBLE,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    llCenterMessage.setVisibility(View.GONE);
                                    loadFromDB();
                                }
                            }, NO_ANIMATE);*/
                } catch (Exception e) {
                    Utils.log("Error is : " + e);
                    e.printStackTrace();
                    /*manageCenterMessage(
                            View.VISIBLE,
                            "Failed to download update file",
                            View.VISIBLE,
                            "Try Now",
                            View.VISIBLE,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    llCenterMessage.setVisibility(View.GONE);
                                    loadFromDB();
                                }
                            }, NO_ANIMATE);*/
                }
            }
        }
    }

    public void showRetryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = getLayoutInflater().inflate(R.layout.popup_retry, null);
        builder.setView(view);

        final Dialog retryDialog = builder.create();

        TextView tvYes = (TextView) view.findViewById(R.id.tv_yes);
        TextView tvNo = (TextView) view.findViewById(R.id.tv_no);

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retryDialog.hide();
            }
        });

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getProfileSyncInfo();
                updateMember();
                retryDialog.hide();
            }
        });

        retryDialog.show();
    }

    public void deleteServerFile() {
        try {
            String zipFileDelete = new URL(zipUrl).getFile();
            Hashtable<String, String> params = new Hashtable<>();
            params.put("folderPath", zipFileDelete);
            JSONObject jsonRequest = new JSONObject(new Gson().toJson(params));

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.DELETE_ZIP_FILE, jsonRequest,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.log(response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
            AppController.getInstance().addToRequestQueue(context, request);
        } catch (MalformedURLException mfe) {
            Utils.log("Error is : " + mfe);
            mfe.printStackTrace();
        } catch (JSONException jse) {
            Utils.log("Error is : " + jse);
            jse.printStackTrace();
        } catch (Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }

    }

    public class UpdateStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Utils.log("Inside UpdateStatusReceiver");
            String action = intent.getAction();
            Utils.log("Value of action is : " + action);
            if (action.equals(DirectoryUpdateService.ACTION_DIRECTORY_SYNC)) {
                String message = intent.getExtras().getString("ExtraMessage");
                Utils.log("Value of message : " + message);
                if (message.equals(DirectoryUpdateService.ACTION_DIRECTORY_SYNC_COMPLETED)) {
                    Utils.log("Processing is completed and fetching data from local data");
                    PreferenceManager.savePreference(context, TBPrefixes.DIRECTORY_PREFIX + grpId, updatedOn);
                    refreshData("");
                }
            }
        }
    }

    //Processing records when records are not provided in zip file
    public void processDirectRecords(JSONObject response) {
        try {
            final ProfileModel model = new ProfileModel(getBaseContext());
            //final ProgressDialog pd = new ProgressDialog(context);
            //pd.setMessage("Processing contacts & Storing offline");
            manageCenterMessage(
                    View.VISIBLE,
                    "Processing contacts & Storing offline",
                    View.VISIBLE,
                    "",
                    View.GONE,
                    null, ANIMATE);
            Utils.log("Is very first time : " + firstTime);
            if (firstTime.equals("yes")) {  // Means process only new records.
                final JSONArray newRecords = response.getJSONObject("memberListSyncResult").getJSONArray("NewMemberList");
                Utils.log("Found records : " + newRecords.length());


                final ArrayList<CompleteProfile> newRecordsList = processRecords(newRecords);
                final int numberOfRecords = newRecordsList.size();

                Utils.log("Parsed number of records : " + newRecordsList.size());

                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        boolean success = model.addProfiles(newRecordsList);

                        if (!success) {
                            Utils.log("Failed to insert new records in local db. Retrying in 2 seconds");
                            sendEmptyMessageDelayed(0, 2000);
                        } else {
                            PreferenceManager.savePreference(context, TBPrefixes.DIRECTORY_PREFIX + grpId, updatedOn);
                            //pd.hide();
                            manageCenterMessage(
                                    View.GONE,
                                    "",
                                    View.VISIBLE,
                                    "",
                                    View.VISIBLE,
                                    null, NO_ANIMATE);
                            Toast.makeText(context, numberOfRecords + " contacts stored", Toast.LENGTH_LONG).show();
                            refreshData("");
                        }
                    }
                };
                handler.sendEmptyMessage(0);
                //pd.show();
            } else {  // means process all new, updated and deleted records
                JSONArray newRecords = response.getJSONObject("MemberDetail").getJSONArray("NewMemberList");
                JSONArray updatedRecords = response.getJSONObject("MemberDetail").getJSONArray("UpdatedMemberList");
                final String deletedRecords = response.getJSONObject("MemberDetail").getString("DeletedMemberList");
                final ArrayList<CompleteProfile> newRecordsList = processRecords(newRecords);
                final ArrayList<CompleteProfile> updatedRecordsList = processRecords(updatedRecords);

                final int numberOfRecordsProcessed;


                if (!deletedRecords.equals("")) {
                    numberOfRecordsProcessed = newRecordsList.size() + updatedRecordsList.size() + deletedRecords.split(",").length;
                } else {
                    numberOfRecordsProcessed = newRecordsList.size() + updatedRecordsList.size();
                }
                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);

                        boolean success = model.updateProfiles(newRecordsList, updatedRecordsList, deletedRecords);
                        if (!success) {
                            Utils.log("Failed to update updated records in local db. Retrying in 2 seconds");
                            sendEmptyMessageDelayed(0, 2000);
                        } else {
                            PreferenceManager.savePreference(context, TBPrefixes.DIRECTORY_PREFIX + grpId, updatedOn);
                            //if ( numberOfRecordsProcessed != 0 ) {
//                            Toast.makeText(context, numberOfRecordsProcessed + " contacts processed", Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                            //}
                            refreshData("");
                        }
                    }
                };
                handler.sendEmptyMessage(0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //loadDynamicFields();
    }


    public ArrayList<CompleteProfile> processRecords(JSONArray memberProfiles) {
        ArrayList<CompleteProfile> profileList = new ArrayList<>();
        try {
            int n = memberProfiles.length();

            for (int i = 0; i < n; i++) {
                JSONObject profile = memberProfiles.getJSONObject(i);
                Utils.log(profile.toString());
                // start of ProfileMasterData
                String masterId = profile.getString("masterID");
                String grpId = profile.getString("grpID");
                String profileId = profile.getString("profileID");
                String isAdmin = profile.getString("isAdmin");
                String memberName = profile.getString("memberName");
                String memberEmail = profile.getString("memberEmail");
                String memberMobile = profile.getString("memberMobile");
                String memberCountry = profile.getString("memberCountry");
                String profilePic = profile.getString("profilePic");
                String familyPic = profile.getString("familyPic");
                String isPersonalDetVisible = profile.getString("isPersonalDetVisible");
                String isBussinessDetVisible = profile.getString("isBusinDetVisible");
                String isFamilyDetVisible = profile.getString("isFamilDetailVisible");
                String isResidanceAddrVisible = profile.getJSONObject("addressDetails").getString("isResidanceAddrVisible");
                String isBusinessAddrVisible = profile.getJSONObject("addressDetails").getString("isBusinessAddrVisible");
                ProfileMasterData profileData = new ProfileMasterData(
                        masterId,
                        grpId,
                        profileId,
                        isAdmin,
                        memberName,
                        memberEmail,
                        memberMobile,
                        memberCountry,
                        profilePic,
                        familyPic,
                        isPersonalDetVisible,
                        isBussinessDetVisible,
                        isFamilyDetVisible,
                        isResidanceAddrVisible,
                        isBusinessAddrVisible);
                // end of ProfileMasterData

                // start of PersonalMemberDetails
                ArrayList<PersonalMemberDetails> personalDetailsList = new ArrayList<>();
                try {
                    JSONArray personalMemberDetails = profile.getJSONArray("personalMemberDetails");
                    int m = personalMemberDetails.length();

                    for (int j = 0; j < m; j++) {
                        JSONObject personalDetail = personalMemberDetails.getJSONObject(j);
                        String fieldID = personalDetail.getString("fieldID");
                        String uniquekey = personalDetail.getString("uniquekey");
                        String key = personalDetail.getString("key");
                        String value = personalDetail.getString("value");
                        String colType = personalDetail.getString("colType");
                        String isEditable = personalDetail.getString("isEditable");
                        String isVisible = personalDetail.getString("isVisible");

                        PersonalMemberDetails data = new PersonalMemberDetails(profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible);
                        personalDetailsList.add(data);
                    }
                } catch (JSONException jsone) {
                    Utils.log("Error is : " + jsone);
                    jsone.printStackTrace();
                }
                // end of PersonalMemberDetails

                // start of BusinessMemberDetails

                ArrayList<BusinessMemberDetails> businessDetailsList = new ArrayList<>();
                try {
                    JSONArray businessMemberDetails = profile.getJSONArray("businessMemberDetails");
                    int m = businessMemberDetails.length();
                    for (int j = 0; j < m; j++) {

                        JSONObject personalDetail = businessMemberDetails.getJSONObject(j);
                        String fieldID = personalDetail.getString("fieldID");
                        String uniquekey = personalDetail.getString("uniquekey");
                        String key = personalDetail.getString("key");
                        String value = personalDetail.getString("value");
                        String colType = personalDetail.getString("colType");
                        String isEditable = personalDetail.getString("isEditable");
                        String isVisible = personalDetail.getString("isVisible");

                        BusinessMemberDetails data = new BusinessMemberDetails(profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible);
                        businessDetailsList.add(data);
                    }
                } catch (JSONException jsone) {
                    Utils.log("Error is : " + jsone);
                    jsone.printStackTrace();
                }
                // end of BusinessMemberDetails

                // Start of family MemberDetails

                ArrayList<FamilyMemberData> familyMemberList = new ArrayList<>();
                try {
                    JSONArray familyMemberDetails = profile.getJSONObject("familyMemberDetails").getJSONArray("familyMemberDetail");

                    int m = familyMemberDetails.length();

                    for (int j = 0; j < m; j++) {
                        JSONObject familyMember = familyMemberDetails.getJSONObject(j);
                        String familyMemberId = familyMember.getString("familyMemberId");
                        String familyMemberName = familyMember.getString("memberName");
                        String relationship = familyMember.getString("relationship");
                        String dob = familyMember.getString("dOB");
                        String emailID = familyMember.getString("emailID");
                        String anniversary = familyMember.getString("anniversary");
                        String contactNo = familyMember.getString("contactNo");
                        String particulars = familyMember.getString("particulars");
                        String bloodGroup = familyMember.getString("bloodGroup");
                        String[] contactFields = contactNo.split(" "); // contact number is in the form "countryId contactNumber" e.g. 1 8877665544 here 1 is country code and 8877665544 is mobile number
                        String countryId = "0";

                        try {
                            countryId = contactFields[0];
                            String countryCode = Utils.getCountryCode(getBaseContext(), countryId);
                            contactNo = countryCode + " " +contactFields[1];
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        FamilyMemberData data = new FamilyMemberData(profileId, familyMemberId, familyMemberName, relationship, dob, emailID, anniversary, contactNo, particulars, bloodGroup, countryId);
                        familyMemberList.add(data);
                    }
                } catch (JSONException jsone) {
                    Utils.log("Error is : " + jsone);
                    jsone.printStackTrace();
                }
                // End of family member details

                // Start of address details
                ArrayList<AddressData> addressList = new ArrayList<>();
                try {
                    JSONArray addressDetails = profile.getJSONObject("addressDetails").getJSONArray("addressResult");
                    int m = addressDetails.length();

                    for (int j = 0; j < m; j++) {
                        JSONObject address = addressDetails.getJSONObject(j);
                        String addressID = address.getString("addressID");
                        String addressType = address.getString("addressType");
                        String addressText = address.getString("address");
                        String city = address.getString("city");
                        String state = address.getString("state");
                        String country = address.getString("country");
                        String pincode = address.getString("pincode");
                        String phoneNo = address.getString("phoneNo");
                        String fax = address.getString("fax");

                        AddressData data = new AddressData(profileId, addressID, addressType, addressText, city, state, country, pincode, phoneNo, fax);
                        addressList.add(data);
                    }
                } catch (JSONException jsone) {
                    Utils.log("Error is : " + jsone);
                    jsone.printStackTrace();
                }
                // end of address details
                CompleteProfile cp = new CompleteProfile(profileData, personalDetailsList, businessDetailsList, familyMemberList, addressList);
                profileList.add(cp);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return profileList;
    }

    public void manageCenterMessage(int visibility, final String message, int msgVisibility, String btnLabel, int btnVisibility, View.OnClickListener onClickListener, boolean animateMessage) {
        /*llCenterMessage.setVisibility(visibility);
        tvCenterMessage.setText(message);
        tvCenterMessage.setVisibility(msgVisibility);

        if ( animateMessage ) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
        tvCenterButton.setText(btnLabel);
        tvCenterButton.setVisibility(btnVisibility);
        if (onClickListener!=null) {
            tvCenterButton.setOnClickListener(onClickListener);
        }*/
    }

    public void refreshData(String msg) {

        setResult(RESULT_OK);
        finish();
        /*list = profileModel.getMembers(grpId);
        if ( list.size() > 0 ) {
            Utils.log("MyList Size : " + list.size());
            adapter = new DirectoryRVAdapter(context, list, "0");
            rvDirectory.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.setOnMemberSelectedListener(classificationSeletedListener);
            rvDirectory.setVisibility(View.VISIBLE);
            Utils.log("Loaded from local db");
            manageCenterMessage(View.GONE, "", View.VISIBLE, "", View.GONE, null, NO_ANIMATE);
        } else {
            manageCenterMessage(View.VISIBLE, "No new updates", View.VISIBLE, "", View.GONE, null, NO_ANIMATE);
        }*/
    }

    public void showKeyboard(View view) {
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (getCurrentFocus() != null) {
                inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        }
    }

    @Override
    public void onBackPressed() {

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_confrm_delete);
        TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
        TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
        TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
        tv_line1.setText("Are you sure you want to go back? All your changes will not be saved.");
        tv_no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();

//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        View view = getLayoutInflater().inflate(R.layout.popup_confrm_delete, null);
//        builder.setView(view);
//
//        TextView tvYes = (TextView) view.findViewById(R.id.tv_yes);
//        TextView tvNo = (TextView) view.findViewById(R.id.tv_no);
//        TextView tv_line1 = (TextView) view.findViewById(R.id.tv_line1);
//        tv_line1.setText("Are you sure you want to go back? All your changes will not be saved.");
//        final AlertDialog dialog = builder.create();
//
//        tvYes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                finish();
//            }
//        });
//
//        tvNo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();

    }

    private int getIndex(ArrayList<CountryData> list, String myString){
        for (int i=0;i<list.size();i++){
            if (list.get(i).getCountryCode().toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }

    public void fillsponsore()
    {
        try {

            TextView RIadm, Spon_Mem, Spon_nam, Spon_str, Spon_ref;
            LinearLayout spon_star, RD, RDN;

            RIadm = findViewById(R.id.RII);
            Spon_Mem = findViewById(R.id.sp_mid);
            Spon_nam = findViewById(R.id.sp_fulnn);
            Spon_str = findViewById(R.id.sp_strr);
            Spon_ref = findViewById(R.id.ref_strr);

            RD = findViewById(R.id.RDD);
            RDN = findViewById(R.id.spon_nn);
            spon_star = findViewById(R.id.spon_starr);

            String RIAdmissionDate = "";
            String SponsorMemberId = "";
            String SponsorFullName = "";
            String SponserCount = "";
            String ReferalCount = "";

            RIAdmissionDate = getIntent().getStringExtra("RIAdmissionDate");//
            // SponsorMemberId = getIntent().getStringExtra("SponsorMemberId");//
            SponsorFullName = getIntent().getStringExtra("SponsorFullName");//
            SponserCount = getIntent().getStringExtra("SponserCount");//
            ReferalCount = getIntent().getStringExtra("ReferalCount");//


                 if(RIAdmissionDate != null)

                     {
                        RIadm.setText(RIAdmissionDate);
                     }
            if(RIAdmissionDate != null)

            {
                Spon_nam.setText(SponsorFullName);
            }
            //  Spon_Mem.setText(SponsorMemberId);

         //   Spon_nam.setText(SponsorFullName);
            Spon_str.setText(SponserCount);
            Spon_ref.setText(ReferalCount);

            //spon_star, RD, RID, RDN ;
            if (RIadm.getText().toString().trim().equalsIgnoreCase("")) {
                RD.setVisibility(View.GONE);
            } else {
                RD.setVisibility(View.VISIBLE);
            }

            if (Spon_nam.getText().toString().trim().equalsIgnoreCase("")) {
                RDN.setVisibility(View.GONE);
            } else {
                RDN.setVisibility(View.VISIBLE);
            }


            if (Spon_str.getText().toString().trim().equalsIgnoreCase("0") && Spon_ref.getText().toString().trim().equalsIgnoreCase("0")) {
                spon_star.setVisibility(View.GONE);
            } else {
                spon_star.setVisibility(View.VISIBLE);

                if(Spon_str.getText().toString().trim().equalsIgnoreCase("0"))
                {
                    Spon_str.setVisibility(View.GONE);
                }
                if(Spon_ref.getText().toString().trim().equalsIgnoreCase("0"))
                {
                    Spon_ref.setVisibility(View.GONE);
                }


            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }
}
