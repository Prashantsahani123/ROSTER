package com.NEWROW.row;

import android.app.Activity;
import android.app.Dialog;
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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.FamilyDetailsAdapter_new;
import com.NEWROW.row.Adapter.ProfileActivityV4Adapter;
import com.NEWROW.row.Adapter.SpinnerAdapter_country;
import com.NEWROW.row.Data.AddressData;
import com.NEWROW.row.Data.AddressDataBus;
import com.NEWROW.row.Data.ContactCallInfo;
import com.NEWROW.row.Data.CountryData;
import com.NEWROW.row.Data.FamilyData;
import com.NEWROW.row.Data.ProfileActivitySubListData;
import com.NEWROW.row.Data.ProfileData;
import com.NEWROW.row.Utils.CircleTransform;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.ImageCompression;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.MarshMallowPermission;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.Utils;
import com.NEWROW.row.croputility.Crop;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
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
 * Created by USER on 18-12-2015.
 */
public class ProfileActivityV4 extends Activity {
    Context context;
    Dialog dialog;
    TextView tv_title, tv_name;

    ImageView iv_backbutton;
    ImageView call_button, iv_sms, iv_email, iv_profileimage, iv_imageedit;
    ListView listView,listView_professional,listView_family;
    LinearLayout linear_personal, linear_business, linear_family, linear_address,linear_address_bus;
    // String flag_tab = "0";
    private ArrayList<ProfileData> profileDataArrayList_personal = new ArrayList<ProfileData>();
    private ArrayList<ProfileData> profileDataArrayList_business = new ArrayList<ProfileData>();

    private ArrayList<ProfileActivitySubListData> profileDataArrayList_personal_popup = new ArrayList<ProfileActivitySubListData>();
    private ArrayList<ProfileActivitySubListData> profileDataArrayList_business_popop = new ArrayList<ProfileActivitySubListData>();
    ArrayList<ProfileActivitySubListData> familylist_popup = new ArrayList<ProfileActivitySubListData>();

    private ProfileActivityV4Adapter profileAdapter;
    private com.NEWROW.row.Adapter.FamilyDetailsAdapter FamilyDetailsAdapter;
    private com.NEWROW.row.Adapter.FamilyDetailsAdapter_new FamilyDetailsAdapter_new;

    ProgressBar progressbar;
    TextView tv_edit, tv_personalbtn, tv_businessbtn, tv_familybtn, tv_addrss,tv_addrss_bus, tv_editaddress,tv_editaddress_bus;
    String value_mobile, value_email;
    public static String memberprofileid, groupId;
    String flag_edit = "0"; //0-Personal , 1- business , 2- family
    String flag_country = "0";
    TextView tv_edit_bus;
    String addressType;
    ArrayList<AddressData> addresslist = new ArrayList<>();
    ArrayList<AddressDataBus> addresslistBus = new ArrayList<>();
    //   / ArrayList<AddressData> selectedaddress = new ArrayList<>();
    private String flag_callwebsercie = "0";
    ArrayList<CountryData> listcounty = new ArrayList<CountryData>();
    ArrayList<FamilyData> familylist = new ArrayList<FamilyData>();
    Spinner spinner_group_country,spinner_group_country_bus;
    private int country_id;
    private int addresss_selectedid;
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
    ProgressDialog pd;
    String responsefromimageupload = "0";
    String isPersonalDetVisible, isBusinDetVisible, isFamilDetailVisible;
    private ImageView iv_actionbtn2;
    private String hasimageflag = "0";
    private String isAdmin = "No";
    private String myProfileID = "0";

    boolean updateProfileImage = false;
    static int Capture_Camera = 100;
    Bitmap updatedProfileImage;
    static Uri mCapturedImageURI;
    ScrollView scrollView;
    TextView tvMemberName;
    ImageView iv_actionbtn;
    String isEmailAvailable = "0";
    String picturePath;
    String businessKey,personalKey,businessValue,personalValue,familyEmail;
    String p_email,b_email,f_email = "";


  /*  String[] KeyNames = {"Name", "Mobile Number", "Secondary Mobile No", "Email ID", "Date Of Birth","Date Of Anniversary","Blood Group","Business Email","Designation"};//fruit names array
    int[] ValueImages = {R.drawable.add_invites, R.drawable.add_invites, R.drawable.add_invites, R.drawable.add_invites, R.drawable.add_invites,R.drawable.add_invites, R.drawable.add_invites, R.drawable.add_invites, R.drawable.add_invites, R.drawable.add_invites};
*/
    String memberName,memberNumber,nameLabel,numberLabel;
    public  static ArrayList<ContactCallInfo> contactInfoArrayList = new ArrayList<ContactCallInfo>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.profile_activity_v4);
        context = getApplicationContext();
        Log.e("Demo", "♦♦♦♦Demo Message");
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn2 = (ImageView) findViewById(R.id.iv_actionbtn2);
        iv_actionbtn2.setImageResource(R.drawable.delete); // Delete

        // iv_backbutton.setVisibility(View.GONE);
        tv_title.setText("Profile");
        //tv_title.setText("Profile");
        call_button = (ImageView) findViewById(R.id.call_button);
        iv_sms = (ImageView) findViewById(R.id.iv_sms);
        iv_email = (ImageView) findViewById(R.id.iv_email);
        iv_profileimage = (ImageView) findViewById(R.id.iv_profileimage);
        iv_imageedit = (ImageView) findViewById(R.id.iv_imageedit);
        tv_name = (TextView) findViewById(R.id.tv_member_name);
        // tv_address = (TextView) findViewById(R.id.tv_address);
        listView = (ListView) findViewById(R.id.listView);
        listView_professional = (ListView) findViewById(R.id.listView_profession);
        listView_family = (ListView) findViewById(R.id.listView_famlily);

        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        linear_personal = (LinearLayout) findViewById(R.id.linear_personal);
        linear_business = (LinearLayout) findViewById(R.id.linear_business);
        linear_family = (LinearLayout) findViewById(R.id.linear_family);
        linear_address = (LinearLayout) findViewById(R.id.linear_address);
        linear_address_bus = (LinearLayout) findViewById(R.id.linear_address_bus);


        tv_edit = (TextView) findViewById(R.id.tv_edit);
        tv_personalbtn = (TextView) findViewById(R.id.tv_personalbtn);
        tv_businessbtn = (TextView) findViewById(R.id.tv_businessbtn);
        tv_familybtn = (TextView) findViewById(R.id.tv_familybtn);
        tv_addrss = (TextView) findViewById(R.id.tv_addrss);
        tv_addrss_bus = (TextView)findViewById(R.id.tv_addrss_bus);
        tv_editaddress = (TextView) findViewById(R.id.tv_editaddress);
        tv_editaddress_bus = (TextView)findViewById(R.id.tv_editaddress_bus);
        tvMemberName = (TextView)findViewById(R.id.tv_member_name);

        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn.setVisibility(View.VISIBLE); // EDIT Profile
        iv_actionbtn.setImageResource(R.drawable.edit); // EDIT Profile
        tv_edit_bus = (TextView)findViewById(R.id.tv_edit_bus);

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        isAdmin = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN);
        myProfileID = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID);
        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            memberprofileid = intent.getString("memberprofileid");
            groupId = intent.getString("groupId");

            nameLabel = intent.getString("nameLabel");
            numberLabel = intent.getString("numberLabel");
            memberName = intent.getString("memberName");
            memberNumber = intent.getString("memberMobile");
            value_mobile = memberNumber;

            Log.e("@@@@@@@@@","---- Name----"+nameLabel+numberLabel+memberName+memberNumber);

            tvMemberName.setText(memberName);

            profileAdapter = new ProfileActivityV4Adapter(ProfileActivityV4.this, R.layout.profile_item_list_v4, profileDataArrayList_personal,"personal");
            listView.setAdapter(profileAdapter);


            ProfileData dataName = new ProfileData();
            dataName.setKey(nameLabel);
            dataName.setValue(memberName);

           // profileDataArrayList_personal.add(dataName);

            ProfileData dataNumber = new ProfileData();
            dataNumber.setKey(numberLabel);
            dataNumber.setValue(memberNumber);

            profileDataArrayList_personal.add(dataNumber);


            profileAdapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(listView);



        }

        //------------------- Hash map ----------------------------------------
/*

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        for (int i = 0; i < KeyNames.length; i++) {
            HashMap<String, String> hashMap = new HashMap<>();//create a hashmap to store the data in key value pair
            hashMap.put("key", KeyNames[i]);
            hashMap.put("image", ValueImages[i] + "");
            arrayList.add(hashMap);//add the hashmap into arrayList
        }
        String[] from = {"key", "image"};//string array
       // int[] to = {R.id.textView, R.id.imageView};//int array of views id's
        ProfileActivityV4Adapter simpleAdapter = new ProfileActivityV4Adapter(this, arrayList, R.layout.profile_item_list, from);//Create object and set the parameters for simpleAdapter
        simpleListView.setAdapter(simpleAdapter);//sets the adapter for listView
*/


        Intent intenti = getIntent();

        if (intenti.hasExtra("isAdmin")) {
            if (intenti.getStringExtra("isAdmin").equals("1"))
                tv_title.setText("Admin");
        }

        if (intenti.hasExtra("memID")) {
            //memberprofileid = intenti.getStringExtra("memID");
            memberprofileid = intenti.getStringExtra("typeID");
            groupId = intenti.getStringExtra("grpID");
            isAdmin = intenti.getStringExtra("isAdmin");
            myProfileID = intenti.getStringExtra("memID");
        }




        Log.d("Touchbase", "ID ID ID AFTER :- " + groupId + " - " + memberprofileid);
        webservices();

        init();
        if(isEmailAvailable.equals("1"))
        {
            iv_email.setImageResource(R.drawable.blue_mail);
        }
        else
            iv_email.setImageResource(R.drawable.p_g_mail);

        Log.e("---ARRAYIST SIZE----","----------"+profileDataArrayList_personal_popup.size());


        adminsettings();


    }

    @Override
    protected void onResume() {
        super.onResume();
        webservices();
    }



    private void adminsettings() {
        if (isAdmin.equals("No")) {
            tv_edit.setVisibility(View.GONE);
            iv_imageedit.setVisibility(View.GONE);
            iv_actionbtn2.setVisibility(View.GONE);
            tv_editaddress.setVisibility(View.GONE);
            iv_actionbtn.setVisibility(View.GONE);
            tv_editaddress_bus.setVisibility(View.GONE);
            if (myProfileID != null) {
                if (myProfileID.equals(memberprofileid)) {
                    tv_edit.setVisibility(View.VISIBLE);
                    //----------changed by lekha ----------------------
                    iv_imageedit.setVisibility(View.GONE);
                    // iv_actionbtn2.setVisibility(View.VISIBLE);
                    tv_editaddress.setVisibility(View.GONE);
                    tv_editaddress_bus.setVisibility(View.GONE);
                }
            }
        } else {
            tv_edit.setVisibility(View.VISIBLE);
            iv_actionbtn.setVisibility(View.VISIBLE);
            //----------changed by lekha ----------------------
            iv_imageedit.setVisibility(View.GONE);
            iv_actionbtn2.setVisibility(View.VISIBLE);
            tv_editaddress.setVisibility(View.GONE);
            tv_editaddress_bus.setVisibility(View.GONE);
        }
    }

    public void init() {
        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(ProfileActivityV4.this,Profile.class);
                i.putExtra("memberprofileid",memberprofileid);
                i.putExtra("groupId",groupId);
                i.putExtra("tvMemberName",tvMemberName.getText().toString());
              //  startActivity(i);
                startActivityForResult(i,1);
            }
        });

        iv_actionbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (memberprofileid.equals(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID))) {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "You are the " + PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_NAME) + "'s Admin, you cannot delete yourself.");
                } else {
                    dialog = new Dialog(ProfileActivityV4.this, android.R.style.Theme_Translucent);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.popup_confrm_delete);
                    TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                    TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                    tv_no.setText("Cancel");
                    tv_yes.setText("Delete");
                    tv_no.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    tv_yes.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (InternetConnection.checkConnection(getApplicationContext())) {
                                //Utils.showToastWithTitleAndContext(getApplicationContext(), "Delete");
                                deletewebservices();
                            } else {
                                Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                            }
                        }
                    });

                    dialog.show();
                }
            }

        });

        iv_imageedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetConnection.checkConnection(getApplicationContext())) {
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
                else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }
            }
        });

        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPopup();

            }
        });

        iv_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               smsPopup();
            }
        });

        iv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    mailPopup();

            }
        });

        linear_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_edit = "0";
                profileAdapter = new ProfileActivityV4Adapter(ProfileActivityV4.this, R.layout.profile_item_list_v4, profileDataArrayList_personal,"personal");
                listView_professional.setAdapter(profileAdapter);
                setListViewHeightBasedOnChildren(listView);
                tv_personalbtn.setTextColor(getResources().getColor(R.color.black));
                tv_businessbtn.setTextColor(getResources().getColor(R.color.dark_gray));
                tv_familybtn.setTextColor(getResources().getColor(R.color.dark_gray));
                linear_address.setVisibility(View.VISIBLE);
                tv_edit.setText("Edit");
                setAddess("Residence");
            }
        });
        linear_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_edit = "1";
                profileAdapter = new ProfileActivityV4Adapter(ProfileActivityV4.this, R.layout.profile_item_list_v4, profileDataArrayList_business,"business");
                listView_professional.setAdapter(profileAdapter);
                setListViewHeightBasedOnChildren(listView_professional);
                tv_personalbtn.setTextColor(getResources().getColor(R.color.dark_gray));
                tv_businessbtn.setTextColor(getResources().getColor(R.color.black));
                tv_familybtn.setTextColor(getResources().getColor(R.color.dark_gray));
                linear_address_bus.setVisibility(View.VISIBLE);
                tv_edit.setText("Edit");
                setAddess("Business");
            }
        });


        linear_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_edit = "2";
                FamilyDetailsAdapter_new = new FamilyDetailsAdapter_new(ProfileActivityV4.this, R.layout.profile_family_item_list_new, familylist, memberprofileid);
                listView_family.setAdapter(FamilyDetailsAdapter_new);
                setListViewHeightBasedOnChildren(listView_family);
                linear_address.setVisibility(View.GONE);
                tv_edit.setText("Add");
                tv_personalbtn.setTextColor(getResources().getColor(R.color.dark_gray));
                tv_businessbtn.setTextColor(getResources().getColor(R.color.dark_gray));
                tv_familybtn.setTextColor(getResources().getColor(R.color.black));
            }
        });

        //linear_business.setClickable(false);

        // linear_family.setClickable(false);
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnection.checkConnection(getApplicationContext())) {

                    //Utils.profilearraylist.clear();
                    if (flag_edit.equals("0")) {
                        Utils.profilearraylist = profileDataArrayList_personal;
                        Intent i = new Intent(ProfileActivityV4.this, Edit_Screen_Listview.class);
                        i.putExtra("memberprofileid", memberprofileid);
                        startActivityForResult(i, 3);
                    } /*else if (flag_edit.equals("1")) {
                        Utils.profilearraylist = profileDataArrayList_business;
                        Intent i = new Intent(ProfileActivityV4.this, Edit_Screen_Listview.class);
                        i.putExtra("memberprofileid", memberprofileid);
                        startActivityForResult(i, 3);
                    } */else if (flag_edit.equals("2")) {

                        Intent i = new Intent(ProfileActivityV4.this, AddFamilyDetailToProfile.class);
                        i.putExtra("memberprofileid", memberprofileid);
                        startActivityForResult(i, 3);
                    }
                }

                else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }


            }
        });

        tv_edit_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.profilearraylist = profileDataArrayList_business;
                Intent i = new Intent(ProfileActivityV4.this, Edit_Screen_Listview.class);
                i.putExtra("memberprofileid", memberprofileid);
                startActivityForResult(i, 3);
            }
        });



        tv_editaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnection.checkConnection(getApplicationContext())) {
                    flag_country = "0";
                    EditAddressPOPUP("Residence");

                   /* if (flag_edit.equals("0")) {

                    } else if (flag_edit.equals("1")) {
                        EditAddressPOPUP("Business");

                    }*/
                }else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }
            }
        });


        tv_editaddress_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InternetConnection.checkConnection(getApplicationContext())) {

                    flag_country = "1";
                    EditAddressPOPUP("Business");

                   /* if (flag_edit.equals("0")) {
                        EditAddressPOPUP("Residence");
                    } else if (flag_edit.equals("1")) {


                    }*/
                }else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }
            }
        });
    }

    public void finishActivity(View v) {
        finish();
    }

    private void deletewebservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("typeID", memberprofileid));
        arrayList.add(new BasicNameValuePair("type", "Member"));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        arrayList.add(new BasicNameValuePair("profileID", myProfileID));

        flag_callwebsercie = "3";
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "PARAMETERS " + Constant.DeleteByModuleName + " :- " + arrayList.toString());
        if ( InternetConnection.checkConnection(context)) {
            new WebConnectionAsyncDirectory(Constant.DeleteByModuleName, arrayList, ProfileActivityV4.this).execute();
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void remove_photo_webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("typeID", memberprofileid));
        arrayList.add(new BasicNameValuePair("type", "Member"));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        arrayList.add(new BasicNameValuePair("grpID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));

        flag_callwebsercie = "4";
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "PARAMETERS " + Constant.DeleteImage + " :- " + arrayList.toString());
        if (InternetConnection.checkConnection(context)) {
            new WebConnectionAsyncDirectory(Constant.DeleteImage, arrayList, ProfileActivityV4.this).execute();
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void webservices() {

        //{"masterUID":"1","grpID":"","searchText":"","page":""}
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("memberProfileId", memberprofileid));
        arrayList.add(new BasicNameValuePair("groupId", groupId));

        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        flag_callwebsercie = "0";

        Log.d("Response", "PARAMETERS " + Constant.GetMember + " :- " + arrayList.toString());
        if ( InternetConnection.checkConnection(context)) {
            new WebConnectionAsyncDirectory(Constant.GetMember, arrayList, ProfileActivityV4.this).execute();
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void webservices_getdata() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        //arrayList.add(new BasicNameValuePair("masterUID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));
        flag_callwebsercie = "1";
        Log.d("Response", "PARAMETERS " + Constant.GetAllCountriesAndCategories + " :- " + arrayList.toString());
        if ( InternetConnection.checkConnection(context)) {
            new WebConnectionAsyncDirectory(Constant.GetAllCountriesAndCategories, arrayList, ProfileActivityV4.this).execute();
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void webservcie_updateaddress(String addressID, String addressType, String address, String city, String state, String country, String pincode, String phoneNo, String fax, String profileID) {
        //{"masterUID":"1","grpID":"","searchText":"","page":""}

        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("addressID", addresslist.get(addresss_selectedid).getAddressID()));
        arrayList.add(new BasicNameValuePair("addressType", addresslist.get(addresss_selectedid).getAddressType()));
        arrayList.add(new BasicNameValuePair("address", addresslist.get(addresss_selectedid).getAddress()));
        arrayList.add(new BasicNameValuePair("city", addresslist.get(addresss_selectedid).getCity()));
        arrayList.add(new BasicNameValuePair("state", addresslist.get(addresss_selectedid).getState()));
        arrayList.add(new BasicNameValuePair("country", addresslist.get(addresss_selectedid).getCountry()));
        arrayList.add(new BasicNameValuePair("pincode", addresslist.get(addresss_selectedid).getPincode()));
        arrayList.add(new BasicNameValuePair("phoneNo", addresslist.get(addresss_selectedid).getPhoneNo()));
        arrayList.add(new BasicNameValuePair("fax", addresslist.get(addresss_selectedid).getFax()));
        arrayList.add(new BasicNameValuePair("profileID", addresslist.get(addresss_selectedid).getProfileID()));
        arrayList.add(new BasicNameValuePair("groupID", addresslist.get(addresss_selectedid).getProfileID()));

        flag_callwebsercie = "2";
        //  arrayList.add(new BasicNameValuePair("groupId", groupId));


        Log.d("Response", "PARAMETERS " + Constant.UpdateAddressDetails + " :- " + arrayList.toString());
        if ( InternetConnection.checkConnection(context)) {
            new WebConnectionAsyncDirectory(Constant.UpdateAddressDetails, arrayList, ProfileActivityV4.this).execute();
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public class WebConnectionAsyncDirectory extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(ProfileActivityV4.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;


        public WebConnectionAsyncDirectory(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //	dialog.show();
            if (flag_callwebsercie.equals("1")) {
            } else {
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                progressDialog.show();
            }
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
            if (this.progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            //	Log.d("response","Do post"+ result.toString());
            if (result != "") {
                Log.d("Response", "calling getDirectorydetails");
                  if (flag_callwebsercie.equals("0")) {
                    getProfileItems(result.toString());
                } else if (flag_callwebsercie.equals("1")) {
                    getresult(result.toString());
                } else if (flag_callwebsercie.equals("2")) {
                    getrestult_addressupdate(result.toString());
                } else if (flag_callwebsercie.equals("3")) {

                    getdata(result.toString());
                } else if (flag_callwebsercie.equals("4")) {

                    getresultOfRemovephoto(result.toString());
                }

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getProfileItems(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("MemberListDetailResult");
            final String status = EventResult.getString("status");
            String json = "";
            if (status.equals("0")) {
                JSONArray EventListResdult = EventResult.getJSONArray("MemberDetails");
                for (int i = 0; i < EventListResdult.length(); i++) {
                    JSONObject object = EventListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("MemberListDetail");

                    tv_name.setText(objects.getString("memberName").toString());
                    value_mobile = objects.getString("memberMobile").toString();
                    value_email = objects.getString("memberEmail").toString();
                    if (objects.has("profilePic")) {
                        Log.d("TOUCHBASE", "Profile Path :- " + objects.getString("profilePic").toString());
                        if (objects.getString("profilePic").toString().equals("") || objects.getString("profilePic").toString() == null || objects.getString("profilePic").toString().isEmpty()) {
                            progressbar.setVisibility(View.GONE);
                        } else {
                            hasimageflag = "1";
                            progressbar.setVisibility(View.VISIBLE);

                            Picasso.with(ProfileActivityV4.this).load(picturePath).transform(new CircleTransform()).into(iv_profileimage);

                            Picasso.with(ProfileActivityV4.this).load(objects.getString("profilePic").toString().trim()).transform(new CircleTransform())
                                    .placeholder(R.drawable.b_profile_pic)
                                    //q.centerCrop()
                                    .into(iv_profileimage, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            progressbar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });
                        }
                    }

                    profileDataArrayList_personal.clear();
                    JSONArray personalMemberDetailsArray = objects.getJSONArray("personalMemberDetails");
                    for (int j = 0; j < personalMemberDetailsArray.length(); j++) {
                        JSONObject personalMemberDetailsobject = personalMemberDetailsArray.getJSONObject(j);
                        JSONObject personalMemberDetailsList = personalMemberDetailsobject.getJSONObject("PersonalMemberDetil");

                        ProfileData data = new ProfileData();
                       // String fieldID = personalMemberDetailsList.getString("fieldID").toString();
                        String myUniqueKey = personalMemberDetailsList.getString("uniquekey").toString();
                        String myKey = personalMemberDetailsList.getString("key").toString();
                        String myValue = personalMemberDetailsList.getString("value").toString();
                        String myColType = personalMemberDetailsList.getString("colType").toString();
                        //String isEditable = personalMemberDetailsList.getString("isEditable").toString();
                        //String isVisible = personalMemberDetailsList.getString("isVisible").toString();
                        data.setKey(myKey);

                        if ( myColType.equalsIgnoreCase("Date") && (myUniqueKey.equalsIgnoreCase("member_date_of_birth") || myUniqueKey.equalsIgnoreCase("member_date_of_wedding") )) {
                            try {
                                String[] fields = myValue.split("/");
                                String myMonth = fields[1];
                                String monthInWord = Utils.getMonth(myMonth);
                                myValue = fields[0] +" "+monthInWord;
                            } catch(Exception e) {
                                Log.e("TouchBase", "♦♦♦♦Error is : "+e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        Log.e("ToucBase", "♦♦♦♦Value of myValue = "+myValue );
                        data.setValue(myValue);
                        data.setUniquekey(myUniqueKey);
                        data.setColType(myColType);
                        profileDataArrayList_personal.add(data);


                        ProfileActivitySubListData personalPopup = new ProfileActivitySubListData();
                        personalPopup.setKey(personalMemberDetailsList.getString("key").toString());
                        personalPopup.setValue(personalMemberDetailsList.getString("value").toString());
                        personalPopup.setUniquekey(personalMemberDetailsList.getString("uniquekey").toString());
                        personalPopup.setColType(personalMemberDetailsList.getString("colType").toString());

                        profileDataArrayList_personal_popup.add(personalPopup);
                        Log.e("--------","---profileDataArrayList_personal_popup-----"+profileDataArrayList_personal_popup);

                        if(profileDataArrayList_personal.get(j).getKey().equalsIgnoreCase("Email ID"))
                        {
                            p_email = profileDataArrayList_personal.get(j).getValue();
                        }
                        Gson gson = new Gson();
                        json = gson.toJson(profileDataArrayList_personal);
                        // String json = new Gson().toJson(profileDataArrayList.toString());
                        //Log.d("TOUCHBASE", "ARRAYLIST" + json);
                        //test(json);

                    }

                    profileAdapter = new ProfileActivityV4Adapter(ProfileActivityV4.this, R.layout.profile_item_list_v4, profileDataArrayList_personal,"personal");
                    listView.setAdapter(profileAdapter);
                    setListViewHeightBasedOnChildren(listView);

                    //----------------------Family-----------------------------------
                    familylist.clear();
                    familylist_popup.clear();

                    JSONArray familyMemberDetailsArray = objects.getJSONArray("familyMemberDetails");
                    for (int j = 0; j < familyMemberDetailsArray.length(); j++) {
                        JSONObject familyMemberDetailsobject = familyMemberDetailsArray.getJSONObject(j);
                        JSONObject familyMemberDetailsList = familyMemberDetailsobject.getJSONObject("FamilyMemberDetil");

                        FamilyData data = new FamilyData();
                        data.setFamilyMemberId(familyMemberDetailsList.getString("familyMemberId").toString());
                        data.setMemberName(familyMemberDetailsList.getString("memberName").toString());
                        data.setRelationship(familyMemberDetailsList.getString("relationship").toString());
                        data.setdOB(familyMemberDetailsList.getString("dOB").toString());
                        familyEmail = data.setEmailID(familyMemberDetailsList.getString("emailID").toString());
                        data.setAnniversary(familyMemberDetailsList.getString("anniversary").toString());
                        data.setContactNo(familyMemberDetailsList.getString("contactNo").toString());
                        data.setParticulars(familyMemberDetailsList.getString("particulars").toString());
                        data.setBloodGroup(familyMemberDetailsList.getString("bloodGroup").toString());
                        familylist.add(data);


                        ProfileActivitySubListData familyData = new ProfileActivitySubListData();
                        familyData.setFamilyMemberId(familyMemberDetailsList.getString("familyMemberId").toString());
                        familyData.setMemberName(familyMemberDetailsList.getString("memberName").toString());
                        familyData.setRelationship(familyMemberDetailsList.getString("relationship").toString());
                        familyData.setdOB(familyMemberDetailsList.getString("dOB").toString());
                        familyData.setEmailID(familyMemberDetailsList.getString("emailID").toString());
                        familyData.setAnniversary(familyMemberDetailsList.getString("anniversary").toString());
                        familyData.setContactNo(familyMemberDetailsList.getString("contactNo").toString());
                        familyData.setParticulars(familyMemberDetailsList.getString("particulars").toString());
                        familyData.setBloodGroup(familyMemberDetailsList.getString("bloodGroup").toString());
                        familylist_popup.add(familyData);
                        Log.e("--------","---familylist_popup-----"+familylist_popup);


                           f_email = familylist.get(j).getEmailID();




                    }

                  /*  profileAdapter = new ProfileActivityV4Adapter(Profile.this, R.layout.profile_family_item_list, profileDataArrayList_personal);
                    listView_family.setAdapter(profileAdapter);
                    setListViewHeightBasedOnChildren(listView_family);
*/
                    FamilyDetailsAdapter_new = new FamilyDetailsAdapter_new(ProfileActivityV4.this, R.layout.profile_family_item_list_new, familylist, memberprofileid);
                    listView_family.setAdapter(FamilyDetailsAdapter_new);
                    setListViewHeightBasedOnChildren(listView_family);




                    // profileAdapter = new ProfileActivityV4Adapter(Profile.this, R.layout.profile_item_list, profileDataArrayList_personal);
                    // listView.setAdapter(profileAdapter);
                    // setListViewHeightBasedOnChildren(listView);
                    //----------------------Family-----------------------------------

                    profileDataArrayList_business.clear();
                    JSONArray businessMemberDetailsArray = objects.getJSONArray("BusinessMemberDetails");
                    for (int j = 0; j < businessMemberDetailsArray.length(); j++) {
                        JSONObject businessMemberDetailsobject = businessMemberDetailsArray.getJSONObject(j);
                        JSONObject businessMemberDetailsList = businessMemberDetailsobject.getJSONObject("BusinessMemberDetail");

                        ProfileData data1 = new ProfileData();
                        businessKey = data1.setKey(businessMemberDetailsList.getString("key").toString());
                        businessValue = data1.setValue(businessMemberDetailsList.getString("value").toString());
                        data1.setUniquekey(businessMemberDetailsList.getString("uniquekey").toString());
                        data1.setColType(businessMemberDetailsList.getString("colType").toString());
                        profileDataArrayList_business.add(data1);

                        ProfileActivitySubListData popupdata = new ProfileActivitySubListData();
                        popupdata.setKey(businessMemberDetailsList.getString("key").toString());
                        popupdata.setValue(businessMemberDetailsList.getString("value").toString());
                        popupdata.setUniquekey(businessMemberDetailsList.getString("uniquekey").toString());
                        popupdata.setColType(businessMemberDetailsList.getString("colType").toString());
                        profileDataArrayList_business_popop.add(popupdata);

                        Log.e("--------","---profileDataArrayList_business_popop-----"+profileDataArrayList_business_popop);
                       /* Gson gson = new Gson();
                        profileDataArrayList_business = (ArrayList<ProfileData>) gson.fromJson(businessMemberDetailsList.toString(), new TypeToken<List<ProfileData>>() {
                        }.getType());*/

                        //Log.d("-------", "datalist------" + listView);


                        if(profileDataArrayList_business.get(j).getKey().equalsIgnoreCase("Business Email"))
                        {
                            b_email = profileDataArrayList_business.get(j).getValue();
                        }

                    }
                    addresslist.clear();
                    addresslistBus.clear();
                    JSONArray AddressMemberDetailsArray = objects.getJSONArray("addressDetails");
                    for (int j = 0; j < AddressMemberDetailsArray.length(); j++) {
                        JSONObject AddresssMemberDetailsobject = AddressMemberDetailsArray.getJSONObject(j);
                        JSONObject AddressMemberDetailsList = AddresssMemberDetailsobject.getJSONObject("Address");

                        Log.d("TOUCHBASE", "@@@@@@@@@@@:- " + AddressMemberDetailsList.toString());

                        AddressData data1 = new AddressData();
                        data1.setAddressID(AddressMemberDetailsList.getString("addressID").toString());
                        addressType = data1.setAddressType(AddressMemberDetailsList.getString("addressType").toString());
                        data1.setAddress(AddressMemberDetailsList.getString("address").toString());
                        data1.setCity(AddressMemberDetailsList.getString("city").toString());
                        data1.setState(AddressMemberDetailsList.getString("state").toString());
                        data1.setPincode(AddressMemberDetailsList.getString("pincode").toString());
                        data1.setProfileID(AddressMemberDetailsList.getString("profileID").toString());
                        data1.setCountry(AddressMemberDetailsList.getString("country").toString());

                        addresslist.add(data1);

                        /* Gson gson = new Gson();
                        profileDataArrayList_business = (ArrayList<ProfileData>) gson.fromJson(businessMemberDetailsList.toString(), new TypeToken<List<ProfileData>>() {
                        }.getType());*/

                        //Log.d("-------", "datalist------" + listView);

                    }


                    profileAdapter = new ProfileActivityV4Adapter(ProfileActivityV4.this, R.layout.profile_item_list_v4, profileDataArrayList_business,"business");
                    listView_professional.setAdapter(profileAdapter);
                    setListViewHeightBasedOnChildren(listView_professional);


                    setAddess("Residence");
                    setAddess("Business");
                    webservices_getdata();


                    //permissions
                    isPersonalDetVisible = objects.getString("isPersonalDetVisible").toString();
                    isBusinDetVisible = objects.getString("isBusinDetVisible").toString();
                    isFamilDetailVisible = objects.getString("isFamilDetailVisible").toString();

                    Log.d("TOUCHBASE", "PROFILE IDS" + memberprofileid + " PROFILE ID" + objects.getString("profileID").toString());
                    if (myProfileID.equals(objects.getString("profileID").toString()))//profileID
                    {

                    } else {
                        setVisibilityAccordingtoPermissions();
                    }
                    // perermissions
                   /* profileAdapter = new ProfileActivityV4Adapter(Profile.this, R.layout.profile_item_list, profileDataArrayList_business);
                    listView.setAdapter(profileAdapter);
                    setListViewHeightBasedOnChildren(listView);*//* profileAdapter = new ProfileActivityV4Adapter(Profile.this, R.layout.profile_item_list, profileDataArrayList_business);
                    listView.setAdapter(profileAdapter);
                    setListViewHeightBasedOnChildren(listView);*/
                    //scrollview.fullScroll(ScrollView.FOCUS_UP);
                    // scrollview.scrollTo(0, 0);
                }

                if(p_email.equalsIgnoreCase("") && b_email.equalsIgnoreCase("")&& f_email.equalsIgnoreCase(""))
                {
                    iv_email.setImageResource(R.drawable.p_g_mail);
                }
                else
                    iv_email.setImageResource(R.drawable.blue_mail);

            } else {
                Toast.makeText(getApplicationContext(), "An error occured.", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }


    private void setVisibilityAccordingtoPermissions() {
        if (isPersonalDetVisible.equals("yes")) {
            linear_personal.setVisibility(View.VISIBLE);
        } else {
            linear_personal.setVisibility(View.GONE);
            if (isBusinDetVisible.equals("yes")) {
                flag_edit = "1";
                profileAdapter = new ProfileActivityV4Adapter(ProfileActivityV4.this, R.layout.profile_item_list_v4, profileDataArrayList_business,"business");
                listView.setAdapter(profileAdapter);
                setListViewHeightBasedOnChildren(listView);
                tv_personalbtn.setTextColor(getResources().getColor(R.color.dark_gray));
                tv_businessbtn.setTextColor(getResources().getColor(R.color.black));
                tv_familybtn.setTextColor(getResources().getColor(R.color.dark_gray));
                linear_address_bus.setVisibility(View.VISIBLE);
                tv_edit.setText("Edit");
                setAddess("Business");
            } else {
                flag_edit = "2";
                FamilyDetailsAdapter_new = new FamilyDetailsAdapter_new(ProfileActivityV4.this, R.layout.profile_family_item_list_new, familylist, memberprofileid);
                listView.setAdapter(FamilyDetailsAdapter_new);
                setListViewHeightBasedOnChildren(listView);
                linear_address.setVisibility(View.GONE);
                tv_edit.setText("Add");
                tv_personalbtn.setTextColor(getResources().getColor(R.color.dark_gray));
                tv_businessbtn.setTextColor(getResources().getColor(R.color.dark_gray));
                tv_familybtn.setTextColor(getResources().getColor(R.color.black));
            }
        }

        if (isBusinDetVisible.equals("yes")) {
            linear_business.setVisibility(View.VISIBLE);
        } else {
            linear_business.setVisibility(View.GONE);
            if (isFamilDetailVisible.equals("yes")) {
                flag_edit = "2";
                FamilyDetailsAdapter_new = new FamilyDetailsAdapter_new(ProfileActivityV4.this, R.layout.profile_family_item_list_new, familylist, memberprofileid);
                listView.setAdapter(FamilyDetailsAdapter_new);
                setListViewHeightBasedOnChildren(listView);
                linear_address.setVisibility(View.GONE);
                tv_edit.setText("Add");
                tv_personalbtn.setTextColor(getResources().getColor(R.color.dark_gray));
                tv_businessbtn.setTextColor(getResources().getColor(R.color.dark_gray));
                tv_familybtn.setTextColor(getResources().getColor(R.color.black));
            }
        }

        if (isFamilDetailVisible.equals("yes")) {
            linear_family.setVisibility(View.VISIBLE);
        } else {
            linear_family.setVisibility(View.GONE);
        }

        if (isFamilDetailVisible.equals("no") && isBusinDetVisible.equals("no") && isPersonalDetVisible.equals("no")) {
            linear_family.setVisibility(View.GONE);
            linear_business.setVisibility(View.GONE);
            linear_personal.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            tv_edit.setVisibility(View.GONE);
        }
    }

    private void getresult(String val) {
        try {
            JSONObject jsonObj = new JSONObject(val);
            JSONObject ActivityResult = jsonObj.getJSONObject("TBCountryResult");
            final String status = ActivityResult.getString("status");
            if (status.equals("0")) {

                JSONArray grpsarray = ActivityResult.getJSONArray("CountryLists");
                for (int i = 0; i < grpsarray.length(); i++) {
                    JSONObject object = grpsarray.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("GrpCountryList");

                    CountryData gd = new CountryData();
                    gd.setCountryId(objects.getString("countryId").toString());
                    gd.setCountryCode(objects.getString("countryCode").toString());
                    gd.setCountryName(objects.getString("countryName").toString());

                    listcounty.add(gd);

                }
                Utils.countryarraylist = listcounty;
                /*spinner_group_country.setAdapter(new SpinnerAdapter_country(Profile.this, listcounty));
                ArrayAdapter myAdap = (ArrayAdapter) spinner_group_country.getAdapter(); //cast to an ArrayAdapter
               // Log.d("TOUCHBASE","COUNTRY CODE --"+addresslist.get(i).getCountry().toString());
                int spinnerPosition = myAdap.getPosition("India");
                spinner_group_country.setSelection(spinnerPosition);*/

            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }


    //--------------------------- Set Address -----------------------------------
    public void setAddess(String type) {

        if(type.equals("Residence")) {

            if (addresslist.isEmpty()) {
                linear_address.setVisibility(View.GONE);
                tv_addrss.setText("");

            } else {
                breakLoop:
                for (int i = 0; i < addresslist.size(); i++) {
                    //Residence
                    if (addresslist.get(i).getAddressType().equals("Residence")) {
                        //addresslist.get(i).getAddressType();
                        linear_address.setVisibility(View.VISIBLE);
                        if (addresslist.get(i).getAddress().equalsIgnoreCase("") && addresslist.get(i).getCity().equalsIgnoreCase("") && addresslist.get(i).getPincode().equalsIgnoreCase("") && addresslist.get(i).getState().equalsIgnoreCase("") && addresslist.get(i).getCountry().equalsIgnoreCase("")) {
                            tv_addrss.setText("");
                        }else {
                            tv_addrss.setText("" + addresslist.get(i).getAddress() + " , " + addresslist.get(i).getCity() + " , " + addresslist.get(i).getPincode() + " , " + addresslist.get(i).getState() + " , " + addresslist.get(i).getCountry());
                        }
                        country_id = addresslist.indexOf(addresslist.get(i).getCountry().toString());
                        break breakLoop;

                    }

                    else {
                        tv_addrss.setText("");
                    }
                }
            }

        }

       else if(type.equals("Business")) {

            if (addresslist.isEmpty() ) {
                linear_address_bus.setVisibility(View.GONE);
                tv_addrss_bus.setText("");

            } else {
                breakLoop:
                for (int i = 0; i < addresslist.size(); i++) {

                 if (addresslist.get(i).getAddressType().equals("Business")) {
                    //addresslist.get(i).getAddressType();
                    linear_address_bus.setVisibility(View.VISIBLE);
                    tv_addrss_bus.setText("" + addresslist.get(i).getAddress() + " , " + addresslist.get(i).getCity() + " , " + addresslist.get(i).getPincode() + " , " + addresslist.get(i).getState() + " , " + addresslist.get(i).getCountry());
                    country_id = addresslist.indexOf(addresslist.get(i).getCountry().toString());
                    break breakLoop;

                }
                    else {
                        tv_addrss_bus.setText("");
                    }
                }
            }

        }
    }

    private void getrestult_addressupdate(String result) {
        try {

            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("UpdateAddressResult");
            final String status = EventResult.getString("status");
            String json = "";
            if (status.equals("0")) {


                tv_personalbtn.setTextColor(getResources().getColor(R.color.black));
                tv_businessbtn.setTextColor(getResources().getColor(R.color.dark_gray));
                tv_familybtn.setTextColor(getResources().getColor(R.color.dark_gray));
                webservices();

            }
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    private void getdata(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("DeleteResult");
            final String status = EventResult.getString("status");
            if (status.equals("0")) {
                //Intent i = new Intent(Announcement_details.this,Announcement.class);
                //startActivityForResult(i,1);
                Intent intent = new Intent();
                setResult(1, intent);
                //finish();//finishing activity
                if ( dialog != null ) {
                    //dialog.hide();
                    dialog.dismiss();
                }
                finish();
            } else {
                Utils.showToastWithTitleAndContext(getApplicationContext(), "Failed to Delete...");
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    private void getresultOfRemovephoto(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("DeleteResult");
            final String status = EventResult.getString("status");
            if (status.equals("0")) {
                //Intent i = new Intent(Announcement_details.this,Announcement.class);
                //startActivityForResult(i,1);
               // iv_profileimage.setImageResource(R.drawable.profile_pic);
                Picasso.with(ProfileActivityV4.this).load(picturePath).transform(new CircleTransform()).into(iv_profileimage);
                hasimageflag = "0";
            } else {
                Utils.showToastWithTitleAndContext(getApplicationContext(), "Failed to DELETE, please Try Again");
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }



  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2) {
            //profileDataArrayList_personal.clear();
            tv_personalbtn.setTextColor(getResources().getColor(R.color.black));
            tv_businessbtn.setTextColor(getResources().getColor(R.color.dark_gray));
            tv_familybtn.setTextColor(getResources().getColor(R.color.dark_gray));
            webservices();
            *//*String message=data.getStringExtra("MESSAGE");
            textView1.setText(message);*//*

        }
    }*/


    //----------------- New methods --------------------------------------------------

    public void EditAddressPOPUP(final String type) {
        final Dialog dialog = new Dialog(ProfileActivityV4.this, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_edit_address);
        final EditText et_address1 = (EditText) dialog.findViewById(R.id.et_address1);
        // EditText et_address2 = (EditText) dialog.findViewById(R.id.et_address2);
        final EditText et_city = (EditText) dialog.findViewById(R.id.et_city);
        final EditText et_pincode = (EditText) dialog.findViewById(R.id.et_pincode);
        final EditText et_state = (EditText) dialog.findViewById(R.id.et_state);
        TextView tv_update = (TextView) dialog.findViewById(R.id.tv_update);
        TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        spinner_group_country = (Spinner) dialog.findViewById(R.id.group_country);


        final String[] selected_countryname = {""};
        String AddressId = "0";


        spinner_group_country.setAdapter(new SpinnerAdapter_country(ProfileActivityV4.this, listcounty));
        SpinnerAdapter_country myAdap = (SpinnerAdapter_country) spinner_group_country.getAdapter(); //cast to an ArrayAdapter
        if (addresslist.isEmpty()) {

        } else {
            Log.d("TOUCHBASE", "ELSE LOOP " + type);
            for (int i = 0; i < addresslist.size(); i++) {
                Log.d("TOUCHBASE", "TYPE " + addresslist.get(i).getAddressType());
                //Residence
                if (addresslist.get(i).getAddressType().equals(type)) {
                    //selectedaddress.add(addresslist.get(i));
                    AddressId = addresslist.get(i).getAddressID();
                    et_address1.setText(addresslist.get(i).getAddress());
                    et_city.setText(addresslist.get(i).getCity());
                    et_pincode.setText(addresslist.get(i).getPincode());
                    et_state.setText(addresslist.get(i).getState());


                }
            }
        }
        final String finalAddressId = AddressId;
        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
                arrayList.add(new BasicNameValuePair("addressID", finalAddressId));
                arrayList.add(new BasicNameValuePair("addressType", type));
                arrayList.add(new BasicNameValuePair("address", et_address1.getText().toString()));
                arrayList.add(new BasicNameValuePair("city", et_city.getText().toString()));
                arrayList.add(new BasicNameValuePair("state", et_state.getText().toString()));
                if ( spinner_group_country.getSelectedItemPosition() > 0 )
                    arrayList.add(new BasicNameValuePair("country", selected_countryname[0]));
                else
                    arrayList.add(new BasicNameValuePair("country", ""));
                arrayList.add(new BasicNameValuePair("pincode", et_pincode.getText().toString()));
                arrayList.add(new BasicNameValuePair("phoneNo", ""));
                arrayList.add(new BasicNameValuePair("fax", ""));
                arrayList.add(new BasicNameValuePair("profileID", memberprofileid));
                arrayList.add(new BasicNameValuePair("groupID", groupId));

                flag_callwebsercie = "2";
                //  arrayList.add(new BasicNameValuePair("groupId", groupId));


                Log.d("Response", "PARAMETERS " + Constant.UpdateAddressDetails + " :- " + arrayList.toString());
                if ( InternetConnection.checkConnection(context)) {
                    new WebConnectionAsyncDirectory(Constant.UpdateAddressDetails, arrayList, ProfileActivityV4.this).execute();
                } else {
                    Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        spinner_group_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Touchbase", "Selected Country :- " + listcounty.get(position).getCountryName());

                selected_countryname[0] = listcounty.get(position).getCountryName().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    private int getCategoryPos(String category) {
        return addresslist.indexOf(category);
    }

    private void selectImage() {

        final CharSequence[] options;
        if (hasimageflag.equals("0")) {
            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Cancel"};
            hasimageflag = "1";
        } else {
            options = new CharSequence[]{"Take Photo", "Choose from Gallery", "Remove Photo", "Cancel"};

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivityV4.this);
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
                    Crop.pickImage(ProfileActivityV4.this);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                } else if (options[item].equals("Remove Photo")) {
                    remove_photo_webservices();
                    //dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TouchBase", "ON ACTIVITY RESULT --");
        if (resultCode == RESULT_OK) {
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
                    correctedUri = Utils.getImageUri(imageBitmap, getApplicationContext());
                    Log.d("==== Uri ===", "======" + correctedUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                beginCrop(correctedUri);
            }
        }
        else if(resultCode == 1)
        {
           finish();
        }
        if (requestCode == 3) {

            //profileDataArrayList_personal.clear();
            tv_personalbtn.setTextColor(getResources().getColor(R.color.black));
            tv_businessbtn.setTextColor(getResources().getColor(R.color.dark_gray));
            tv_familybtn.setTextColor(getResources().getColor(R.color.dark_gray));
            webservices();
            flag_edit = "0";
            tv_edit.setText("Edit");
                  /*String message=data.getStringExtra("MESSAGE");
                    textView1.setText(message);*/
        }
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);

            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, GridLayout.LayoutParams.MATCH_PARENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + ((listView.getDividerHeight()) * (listAdapter.getCount()));

        listView.setLayoutParams(params);
        listView.requestLayout();

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

                    responsefromimageupload = Utils.doFileUploadForProfilePic(ProfileActivityV4.this,new File(picturePath.toString()), memberprofileid, groupId,"profile"); // Upload File to server
                    Log.d("TOUCHBASE", "RESPONSE FILE UPLOAD " + responsefromimageupload);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                updatedProfileImage = csBitmap;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // csBitmap = Bitmap.createScaledBitmap(csBitmap, 500, 500, true);
                //  csBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                //   iv_profileimage.setImageBitmap(csBitmap);

                picturePath = imageCompression.compressImage(path, getApplicationContext());
                //iv_profileimage.setImageDrawable(Drawable.createFromPath(picturePath));
                Picasso.with(ProfileActivityV4.this).load(picturePath).transform(new CircleTransform()).into(iv_profileimage);
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


    /**************************************************
     * Call/Message/Mail Popups
     **************************************************/

    private void callPopup() {

        contactInfoArrayList.clear();

        Intent intent = new Intent(ProfileActivityV4.this, CallPopupActivity.class);
        intent.putExtra("CommunicationType", "Call");
        intent.putExtra("PopupCategoryPersonal", "Personal");
        intent.putExtra("PopupCategoryBussiness", "Bussiness");
        intent.putExtra("PopupCategoryFamily", "Family");
        startActivity(intent);

        String popupmob = null, popupname = null, popupemail = null, popupcategory = null, popupsecondarymob = null;
        int posmob = 0, posname = 0, posemail = 0, pospersonal,posmob2 = 0;
        int i;
        Boolean flag = false;


        //---------------------------------------------------------------------------

        if(flag == false) {
            for (i = 0; i < profileDataArrayList_personal_popup.size(); i++) {
                popupcategory = "Personal:";

                String key = profileDataArrayList_personal_popup.get(i).getKey();
                Log.d("@@@@", "--KEYYYYYYY -- " + key);


                if (profileDataArrayList_personal_popup.get(i).getKey().equals("Mobile Number")) {
                    posmob = i;
                    Log.d("@@@@", "--posmob -- " + posmob);
                }

                if (profileDataArrayList_personal_popup.get(i).getKey().equals("Name")) {
                    posname = i;
                    Log.d("@@@@", "--posname -- " + posname);
                }

                if (profileDataArrayList_personal_popup.get(i).getKey().equals("Secondary Mobile No")) {
                    posmob2 = i;
                    Log.d("@@@@", "--Secondary Mobile No -- " + posname);
                }

                if (profileDataArrayList_personal_popup.get(posmob).getKey().equals("Mobile Number") && !profileDataArrayList_personal_popup.get(posmob).getValue().equals("") || !profileDataArrayList_personal_popup.get(posmob).getValue().equals(null)) {

                    popupmob = profileDataArrayList_personal_popup.get(posmob).getValue();
                    Log.d("@@@@", "--mobile -- " + popupmob);
                    Log.d("@@@@", "***************************** " + profileDataArrayList_personal_popup.get(posmob).getKey());

                }
                if (profileDataArrayList_personal_popup.get(posname).getKey().equals("Name") && !profileDataArrayList_personal_popup.get(posname).getValue().equals("") || !profileDataArrayList_personal_popup.get(posname).getValue().equals(null)) {
                    popupname = profileDataArrayList_personal_popup.get(posname).getValue();
                    Log.d("@@@@", "--Name -- " + popupname);
                }

                if (profileDataArrayList_personal_popup.get(posmob2).getKey().equals("Secondary Mobile No") && !profileDataArrayList_personal_popup.get(posmob2).getValue().equals("") || !profileDataArrayList_personal_popup.get(posmob2).getValue().equals(null)) {
                    popupsecondarymob = profileDataArrayList_personal_popup.get(posmob2).getValue();
                    Log.d("@@@@", "--Name -- " + popupname);
                }

            }

            ContactCallInfo contactInfo = new ContactCallInfo(popupname, popupmob,popupsecondarymob, popupemail, popupcategory);

            if (contactInfoArrayList.size() == 0) {
                contactInfoArrayList.add(0, contactInfo);
            } else {

                contactInfoArrayList.add(contactInfoArrayList.size(), contactInfo);

            }

            Log.d("----", "---List---" + contactInfoArrayList);
            flag = true;


        /*    if (profileDataArrayList_personal.get(i).getKey().equals("Secondary Mobile No")) {
                posname = i;
                Log.d("@@@@", "--posmob -- " + posmob);
            }
*/
           /* if (profileDataArrayList_personal.get(i).getKey().equals("Email ID")) {
                posemail = i;
                Log.d("@@@@", "--posmob -- " + posmob);
            }*/

          /*  if (profileDataArrayList_personal.get(posemail).getKey().equals("Email ID") && !profileDataArrayList_personal.get(posemail).getValue().equals("") || !profileDataArrayList_personal.get(posemail).getValue().equals(null)) {
                email = profileDataArrayList_personal.get(posemail).getValue();
                Log.d("@@@@", "--Email -- " + popupname);
            }*/



        }



        //-----------------------------------------------------------------------------------------
      /*  for (i = 0;i<profileDataArrayList_business.size();i++)
        {

            popupcategory = "Business:";
            if(profileDataArrayList_business.get(i).getKey().equals("Mobile Number")&&!profileDataArrayList_business.get(i).getValue().equals("")||!profileDataArrayList_business.get(i).getValue().equals(null))
            {
                popupmob = profileDataArrayList_personal.get(i).getValue();
            }

            if(profileDataArrayList_personal.get(i).getKey().equals("Name")&&!profileDataArrayList_personal.get(i).getValue().equals("")||!profileDataArrayList_personal.get(i).getValue().equals(null))
            {
                popupname = profileDataArrayList_personal.get(i).getValue();
            }

            if(profileDataArrayList_business.get(i).getKey().equals("Email ID")&&!profileDataArrayList_business.get(i).getValue().equals("")||!profileDataArrayList_business.get(i).getValue().equals(null))
            {
                popupemail = profileDataArrayList_business.get(i).getValue();
            }
            ContactCallInfo contactInfo = new ContactCallInfo(popupname,popupmob,popupsecondarymob,popupemail,popupcategory);
            if(contactInfoArrayList.size()==0)
            {
                contactInfoArrayList.add(0,contactInfo);
            }else {
                contactInfoArrayList.add(contactInfoArrayList.size(),contactInfo);
            }

        }
*/

        for ( i = 0;i<familylist_popup.size();i++)
        {


            popupcategory = "Family:";
            popupmob = familylist_popup.get(i).getContactNo();
            popupemail = familylist_popup.get(i).getEmailID();
            popupname = familylist_popup.get(i).getMemberName();

            if(!popupmob.equals("") && !popupmob.equals(null)) {

                ContactCallInfo contactInfo = new ContactCallInfo(popupname, popupmob, popupsecondarymob, popupemail, popupcategory);

                if (contactInfoArrayList.size() == 0) {
                    contactInfoArrayList.add(0, contactInfo);
                } else {
                    contactInfoArrayList.add(contactInfoArrayList.size(), contactInfo);
                }
            }

        }


    }

    private  void  smsPopup()
    {

       contactInfoArrayList.clear();
      //  familylist_popup.clear();

        Intent intent = new Intent(ProfileActivityV4.this, CallPopupActivity.class);
        intent.putExtra("CommunicationType", "Message");
        intent.putExtra("PopupCategoryPersonal", "Personal");
        intent.putExtra("PopupCategoryBussiness", "Bussiness");
        intent.putExtra("PopupCategoryFamily", "Family");
        startActivity(intent);

        String popupmob = null, popupname = null, popupemail = null, popupcategory = null, popupsecondarymob = null;
        int posmob = 0, posname = 0, posemail = 0, pospersonal,posmob2 = 0;
        int i;
        Boolean flag = false;


        //---------------------------------------------------------------------------

        if(flag == false) {
            for (i = 0; i < profileDataArrayList_personal_popup.size(); i++) {
                popupcategory = "Personal:";

                String key = profileDataArrayList_personal_popup.get(i).getKey();
                Log.d("@@@@", "--KEYYYYYYY -- " + key);


                if (profileDataArrayList_personal_popup.get(i).getKey().equals("Mobile Number")) {
                    posmob = i;
                    Log.d("@@@@", "--posmob -- " + posmob);
                }

                if (profileDataArrayList_personal_popup.get(i).getKey().equals("Name")) {
                    posname = i;
                    Log.d("@@@@", "--posname -- " + posname);
                }

                if (profileDataArrayList_personal_popup.get(i).getKey().equals("Secondary Mobile No")) {
                    posmob2 = i;
                    Log.d("@@@@", "--Secondary Mobile No -- " + posname);
                }

                if (profileDataArrayList_personal_popup.get(posmob).getKey().equals("Mobile Number") && !profileDataArrayList_personal_popup.get(posmob).getValue().equals("") || !profileDataArrayList_personal_popup.get(posmob).getValue().equals(null)) {

                    popupmob = profileDataArrayList_personal_popup.get(posmob).getValue();
                    Log.d("@@@@", "--mobile -- " + popupmob);
                }
                if (profileDataArrayList_personal_popup.get(posname).getKey().equals("Name") && !profileDataArrayList_personal_popup.get(posname).getValue().equals("") || !profileDataArrayList_personal_popup.get(posname).getValue().equals(null)) {
                    popupname = profileDataArrayList_personal_popup.get(posname).getValue();
                    Log.d("@@@@", "--Name -- " + popupname);
                }

                if (profileDataArrayList_personal_popup.get(posmob2).getKey().equals("Secondary Mobile No") && !profileDataArrayList_personal_popup.get(posmob2).getValue().equals("") || !profileDataArrayList_personal_popup.get(posmob2).getValue().equals(null)) {
                    popupsecondarymob = profileDataArrayList_personal_popup.get(posmob2).getValue();
                    Log.d("@@@@", "--Name -- " + popupname);
                }

            }

                ContactCallInfo contactInfo = new ContactCallInfo(popupname, popupmob, popupsecondarymob, popupemail, popupcategory);



                if (contactInfoArrayList.size() == 0) {
                    contactInfoArrayList.add(0, contactInfo);
                } else {
                    contactInfoArrayList.add(contactInfoArrayList.size(), contactInfo);


                }

                Log.d("----", "---List---" + contactInfoArrayList);
                flag = true;


        }

        //================= 16 Nov =======================================

        if(flag == false) {
            for (i = 0; i < profileDataArrayList_personal_popup.size(); i++) {
                popupcategory = "Personal:";

                String key = profileDataArrayList_personal_popup.get(i).getKey();
                Log.d("@@@@", "--KEYYYYYYY -- " + key);


                if (profileDataArrayList_personal_popup.get(i).getKey().equals("Mobile Number")) {
                    posmob = i;
                    Log.d("@@@@", "--posmob -- " + posmob);
                }

                if (profileDataArrayList_personal_popup.get(i).getKey().equals("Name")) {
                    posname = i;
                    Log.d("@@@@", "--posname -- " + posname);
                }

                if (profileDataArrayList_personal_popup.get(i).getKey().equals("Secondary Mobile No")) {
                    posmob2 = i;
                    Log.d("@@@@", "--Secondary Mobile No -- " + posname);
                }

                if (profileDataArrayList_personal_popup.get(posmob).getKey().equals("Mobile Number") && !profileDataArrayList_personal_popup.get(posmob).getValue().equals("") || !profileDataArrayList_personal_popup.get(posmob).getValue().equals(null)) {

                   // popupmob = profileDataArrayList_personal_popup.get(posmob).getValue();
                    Log.d("@@@@", "--mobile -- " + popupmob);
                }
                if (profileDataArrayList_personal_popup.get(posname).getKey().equals("Name") && !profileDataArrayList_personal_popup.get(posname).getValue().equals("") || !profileDataArrayList_personal_popup.get(posname).getValue().equals(null)) {
                    popupname = profileDataArrayList_personal_popup.get(posname).getValue();
                    Log.d("@@@@", "--Name -- " + popupname);
                }

                if (profileDataArrayList_personal_popup.get(posmob2).getKey().equals("Secondary Mobile No") && !profileDataArrayList_personal_popup.get(posmob2).getValue().equals("") || !profileDataArrayList_personal_popup.get(posmob2).getValue().equals(null)) {
                    popupmob = profileDataArrayList_personal_popup.get(posmob2).getValue();
                    Log.d("@@@@", "--Name -- " + popupname);
                }

            }

            ContactCallInfo contactInfo = new ContactCallInfo(popupname, popupmob, popupsecondarymob, popupemail, popupcategory);



            if (contactInfoArrayList.size() == 0) {
                contactInfoArrayList.add(0, contactInfo);
            } else {
                contactInfoArrayList.add(contactInfoArrayList.size(), contactInfo);


            }

            Log.d("----", "---List---" + contactInfoArrayList);
            flag = true;


        }

        //==================16 Nov =========================================


        for ( i = 0;i<familylist_popup.size();i++)
        {
            popupcategory = "Family:";
            popupmob = familylist_popup.get(i).getContactNo();
            popupemail = familylist_popup.get(i).getEmailID();
            popupname = familylist_popup.get(i).getMemberName();

            if(!popupmob.equals("") && !popupmob.equals(null)) {

                ContactCallInfo contactInfo = new ContactCallInfo(popupname, popupmob, popupsecondarymob, popupemail, popupcategory);

                if (contactInfoArrayList.size() == 0) {
                    contactInfoArrayList.add(0, contactInfo);
                } else {
                    contactInfoArrayList.add(contactInfoArrayList.size(), contactInfo);
                }
            }
        }


    }

    private void mailPopup() {

    contactInfoArrayList.clear();

        String popupmob = null, popupname = null, popupemail = null, popupcategory = null, popupsecondarymob = null;
        int posmob = 0, posname = 0, posemail = 0, pospersonal, posmob2 = 0;
        int i;
        Boolean flag = false;


        //---------------------------------------------------------------------------

        if (flag == false) {
            for (i = 0; i < profileDataArrayList_personal_popup.size(); i++) {
                popupcategory = "Personal:";

                String key = profileDataArrayList_personal_popup.get(i).getKey();
                Log.d("@@@@", "--KEYYYYYYY -- " + key);


                if (profileDataArrayList_personal_popup.get(i).getKey().equals("Email ID")) {
                    posemail = i;
                    Log.d("@@@@", "--posmob -- " + posmob);
                }

                if (profileDataArrayList_personal_popup.get(posemail).getKey().equals("Email ID") && !profileDataArrayList_personal_popup.get(posemail).getValue().equals("") || !profileDataArrayList_personal_popup.get(posemail).getValue().equals(null)) {
                    popupemail = profileDataArrayList_personal_popup.get(posemail).getValue();
                    Log.d("@@@@", "--Email -- " + popupname);
                }
            }

            if(!popupemail.equals("")&&!popupemail.equals(null)) {

              //  iv_email.setImageResource(R.drawable.blue_mail);
                ContactCallInfo contactInfo = new ContactCallInfo(popupname, popupmob, popupsecondarymob, popupemail, popupcategory,"Email ID");

                if (contactInfoArrayList.size() == 0) {
                    contactInfoArrayList.add(0, contactInfo);
                } else {
                    contactInfoArrayList.add(contactInfoArrayList.size(), contactInfo);

                }
            }
            else
            {
               // iv_email.setImageResource(R.drawable.p_g_mail);
                Toast.makeText(ProfileActivityV4.this,"No Email available", Toast.LENGTH_SHORT).show();
            }


            Log.d("----", "---List---" + contactInfoArrayList);
            flag = true;

        }

        //-------------------Bussiness--------------------------------------------------------

        boolean flag_bus = false;
        if (flag_bus == false) {
            for (i = 0; i < profileDataArrayList_business_popop.size(); i++) {
                popupcategory = "Bussiness:";

                String key = profileDataArrayList_business_popop.get(i).getKey();
                Log.d("@@@@", "--KEYYYYYYY -- " + key);


                if (profileDataArrayList_business_popop.get(i).getKey().equals("Business Email")) {
                    posemail = i;
                    Log.d("@@@@", "--posmob -- " + posmob);
                }

                if (profileDataArrayList_business_popop.get(posemail).getKey().equals("Business Email") && !profileDataArrayList_business_popop.get(posemail).getValue().equals("") || !profileDataArrayList_business_popop.get(posemail).getValue().equals(null)) {
                    popupemail = profileDataArrayList_business_popop.get(posemail).getValue();
                    Log.d("@@@@", "--Email -- " + popupname);
                }
            }

            if(!popupemail.equals("")&&!popupemail.equals(null)) {
                ContactCallInfo contactInfo = new ContactCallInfo(popupname, popupmob, popupsecondarymob, popupemail, popupcategory,"Business Email");

                if (contactInfoArrayList.size() == 0) {
                    contactInfoArrayList.add(0, contactInfo);
                } else {
                    contactInfoArrayList.add(contactInfoArrayList.size(), contactInfo);

                }
            }
            Log.d("----", "---List---" + contactInfoArrayList);
            flag_bus = true;

        }

        for ( i = 0;i<familylist.size();i++)
        {


            popupcategory = "Family:";
            popupmob = familylist.get(i).getContactNo();
            popupemail = familylist.get(i).getEmailID();
            popupname = familylist.get(i).getMemberName();

            if(!popupemail.equals("")&&!popupemail.equals(null)) {
                ContactCallInfo contactInfo = new ContactCallInfo(popupname, popupmob, popupsecondarymob, popupemail, popupcategory,"Email");

                if (contactInfoArrayList.size() == 0) {
                    contactInfoArrayList.add(0, contactInfo);
                } else {
                    contactInfoArrayList.add(contactInfoArrayList.size(), contactInfo);
                }
            }
        }

        Log.e("***************","*********"+contactInfoArrayList.size());

        if(contactInfoArrayList.size() == 0)
        {

          //  iv_email.setImageResource(R.drawable.p_g_mail);
            Toast.makeText(ProfileActivityV4.this,"No Email available", Toast.LENGTH_SHORT).show();
        }
        else {
          //  iv_email.setImageResource(R.drawable.blue_mail);
            Intent intent = new Intent(ProfileActivityV4.this, CallPopupActivity.class);
            intent.putExtra("CommunicationType", "Email");
            intent.putExtra("PopupCategoryPersonal", "Personal");
            intent.putExtra("PopupCategoryBussiness", "Bussiness");
            intent.putExtra("PopupCategoryFamily", "Family");
            startActivity(intent);
        }

    }




}
