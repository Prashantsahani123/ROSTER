package com.SampleApp.row;

import android.app.Activity;
import android.app.Dialog;
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
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
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

import com.SampleApp.row.Adapter.FamilyDetailsAdapter;
import com.SampleApp.row.Adapter.ProfileAdapter;
import com.SampleApp.row.Adapter.SpinnerAdapter_country;
import com.SampleApp.row.Data.AddressData;
import com.SampleApp.row.Data.CountryData;
import com.SampleApp.row.Data.FamilyData;
import com.SampleApp.row.Data.ProfileData;
import com.SampleApp.row.Utils.CircleTransform;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.ImageCompression;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.MarshMallowPermission;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.croputility.Crop;
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
public class Profile extends Activity {
    Context context;
    Dialog dialog;
    TextView tv_title;
    ImageView iv_backbutton;
    ImageView call_button, iv_sms, iv_email, iv_profileimage, iv_imageedit;
    ListView listView;
    LinearLayout linear_personal, linear_business, linear_family, linear_address;
    // String flag_tab = "0";
    private ArrayList<ProfileData> profileDataArrayList_personal = new ArrayList<ProfileData>();



    private ArrayList<ProfileData> profileDataArrayList_business = new ArrayList<ProfileData>();
    private ProfileAdapter profileAdapter;
    private FamilyDetailsAdapter FamilyDetailsAdapter;
    ProgressBar progressbar;
    TextView  tv_personalbtn, tv_businessbtn, tv_familybtn, tv_addrss,tv_name;
    ImageView tv_editaddress;
    String value_mobile, value_email;

    ImageView tv_edit;
    private String memberprofileid, groupId;
    String flag_edit = "0"; //0-Personal , 1- business , 2- family

    ArrayList<AddressData> addresslist = new ArrayList<>();
    //   / ArrayList<AddressData> selectedaddress = new ArrayList<>();
    private String flag_callwebsercie = "0";
    ArrayList<CountryData> listcounty = new ArrayList<CountryData>();
    ArrayList<FamilyData> familylist = new ArrayList<FamilyData>();
    Spinner spinner_group_country;
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
    View vwPersonalTop,vwPersonalBottom,vwBusinessTop,vwBusinessBottom,vwFamilyTop,vwFamilyBottom;
    String picturePath;
    String titleName;
    String memberName,memberNumber,nameLabel,numberLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.profile);
        context = getApplicationContext();
        Log.e("TouchBase", "♦♦♦♦Profile activity");
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
        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        linear_personal = (LinearLayout) findViewById(R.id.linear_personal);
        linear_business = (LinearLayout) findViewById(R.id.linear_business);
        linear_family = (LinearLayout) findViewById(R.id.linear_family);
        linear_address = (LinearLayout) findViewById(R.id.linear_address);


        tv_edit = (ImageView) findViewById(R.id.tv_edit);
        tv_personalbtn = (TextView) findViewById(R.id.tv_personalbtn);
        tv_businessbtn = (TextView) findViewById(R.id.tv_businessbtn);
        tv_familybtn = (TextView) findViewById(R.id.tv_familybtn);
        tv_addrss = (TextView) findViewById(R.id.tv_addrss);
        tv_editaddress = (ImageView) findViewById(R.id.tv_editaddress);


        vwPersonalTop = (View)findViewById(R.id.vw_personal_top);
        vwPersonalBottom = (View)findViewById(R.id.vw_personal_bottom);
        vwBusinessTop = (View)findViewById(R.id.vw_business_top);
        vwBusinessBottom = (View)findViewById(R.id.vw_business_bottom);
        vwFamilyTop = (View)findViewById(R.id.vw_family_top);
        vwFamilyBottom = (View)findViewById(R.id.vw_family_bottom);

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



            profileAdapter = new ProfileAdapter(Profile.this, R.layout.profile_item_list, profileDataArrayList_personal);
            listView.setAdapter(profileAdapter);


            ProfileData dataName = new ProfileData();
            dataName.setKey(nameLabel);
            dataName.setValue(memberName);

            profileDataArrayList_personal.add(dataName);

            ProfileData dataNumber = new ProfileData();
            dataNumber.setKey(numberLabel);
            dataNumber.setValue(memberNumber);

            profileDataArrayList_personal.add(dataNumber);

            profileAdapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(listView);
        }



        Intent intenti = getIntent();

        titleName = intenti.getStringExtra("tvMemberName");
        tv_name.setText(titleName);
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
        adminsettings();
    }

    private void adminsettings() {
        if (isAdmin.equals("No")) {
            tv_edit.setVisibility(View.GONE);
            iv_imageedit.setVisibility(View.GONE);
            iv_actionbtn2.setVisibility(View.GONE);
            tv_editaddress.setVisibility(View.GONE);
            if (myProfileID != null) {
                if (myProfileID.equals(memberprofileid)) {
                    tv_edit.setVisibility(View.VISIBLE);
                    iv_imageedit.setVisibility(View.VISIBLE);
                    // iv_actionbtn2.setVisibility(View.VISIBLE);
                    tv_editaddress.setVisibility(View.VISIBLE);
                }
            }
        } else {
            tv_edit.setVisibility(View.VISIBLE);
            iv_imageedit.setVisibility(View.VISIBLE);
            iv_actionbtn2.setVisibility(View.VISIBLE);
            tv_editaddress.setVisibility(View.VISIBLE);
        }
    }

    public void init() {
        iv_actionbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (memberprofileid.equals(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID))) {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "You are the " + PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_NAME) + "'s Admin, you cannot delete yourself.");
                } else {
                    dialog = new Dialog(Profile.this, android.R.style.Theme_Translucent);
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
                //String phone = "+34666777888";

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", value_mobile, null));
                startActivity(intent);

                /*if(InternetConnection.checkConnection(getApplicationContext()))
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", value_mobile, null));
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", memberNumber, null));
                    startActivity(intent);
                }*/
            }
        });

        iv_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", value_mobile);
                // smsIntent.putExtra("","");
                startActivity(smsIntent);
            }
        });

        iv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setType("plain/text");
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{value_email});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                Profile.this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });

        linear_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_edit = "0";
                profileAdapter = new ProfileAdapter(Profile.this, R.layout.profile_item_list, profileDataArrayList_personal);
                listView.setAdapter(profileAdapter);
                setListViewHeightBasedOnChildren(listView);
                tv_personalbtn.setTextColor(getResources().getColor(R.color.blueonclickcolor));
                tv_businessbtn.setTextColor(getResources().getColor(R.color.black));
                tv_familybtn.setTextColor(getResources().getColor(R.color.black));

                vwPersonalTop.setVisibility(View.VISIBLE);
                vwPersonalBottom.setVisibility(View.VISIBLE);
                vwBusinessTop.setVisibility(View.GONE);
                vwBusinessBottom.setVisibility(View.GONE);
                vwFamilyTop.setVisibility(View.GONE);
                vwFamilyBottom.setVisibility(View.GONE);

                linear_address.setVisibility(View.VISIBLE);
                //tv_edit.setText("Edit");
                setAddess("Residence");
            }
        });
        linear_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_edit = "1";
                profileAdapter = new ProfileAdapter(Profile.this, R.layout.profile_item_list, profileDataArrayList_business);
                listView.setAdapter(profileAdapter);
                setListViewHeightBasedOnChildren(listView);
                tv_personalbtn.setTextColor(getResources().getColor(R.color.black));
                tv_businessbtn.setTextColor(getResources().getColor(R.color.blueonclickcolor));
                tv_familybtn.setTextColor(getResources().getColor(R.color.black));
                linear_address.setVisibility(View.VISIBLE);

                vwPersonalTop.setVisibility(View.GONE);
                vwPersonalBottom.setVisibility(View.GONE);
                vwBusinessTop.setVisibility(View.VISIBLE);
                vwBusinessBottom.setVisibility(View.VISIBLE);
                vwFamilyTop.setVisibility(View.GONE);
                vwFamilyBottom.setVisibility(View.GONE);

              //  tv_edit.setText("Edit");
                setAddess("Business");
            }
        });
        linear_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_edit = "2";
                FamilyDetailsAdapter = new FamilyDetailsAdapter(Profile.this, R.layout.profile_family_item_list, familylist, memberprofileid);
                listView.setAdapter(FamilyDetailsAdapter);
                setListViewHeightBasedOnChildren(listView);
                linear_address.setVisibility(View.GONE);
               // tv_edit.setText("Add");
                tv_personalbtn.setTextColor(getResources().getColor(R.color.black));
                tv_businessbtn.setTextColor(getResources().getColor(R.color.black));
                tv_familybtn.setTextColor(getResources().getColor(R.color.blueonclickcolor));

                vwPersonalTop.setVisibility(View.GONE);
                vwPersonalBottom.setVisibility(View.GONE);
                vwBusinessTop.setVisibility(View.GONE);
                vwBusinessBottom.setVisibility(View.GONE);
                vwFamilyTop.setVisibility(View.VISIBLE);
                vwFamilyBottom.setVisibility(View.VISIBLE);
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
                        Intent i = new Intent(Profile.this, Edit_Screen_Listview.class);
                        i.putExtra("memberprofileid", memberprofileid);
                        startActivityForResult(i, 3);
                    } else if (flag_edit.equals("1")) {
                        Utils.profilearraylist = profileDataArrayList_business;
                        Intent i = new Intent(Profile.this, Edit_Screen_Listview.class);
                        i.putExtra("memberprofileid", memberprofileid);
                        startActivityForResult(i, 3);
                    } else if (flag_edit.equals("2")) {
                        Intent i = new Intent(Profile.this, AddFamilyDetailToProfile.class);
                        i.putExtra("memberprofileid", memberprofileid);
                        startActivityForResult(i, 3);
                    }
                } else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }
            }
        });
        tv_editaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnection.checkConnection(getApplicationContext())) {
                    if (flag_edit.equals("0")) {
                        EditAddressPOPUP("Residence");
                    } else if (flag_edit.equals("1")) {
                        EditAddressPOPUP("Business");

                    }
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
            new WebConnectionAsyncDirectory(Constant.DeleteByModuleName, arrayList, Profile.this).execute();
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
            new WebConnectionAsyncDirectory(Constant.DeleteImage, arrayList, Profile.this).execute();
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
            new WebConnectionAsyncDirectory(Constant.GetMember, arrayList, Profile.this).execute();
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
            new WebConnectionAsyncDirectory(Constant.GetAllCountriesAndCategories, arrayList, Profile.this).execute();
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
            new WebConnectionAsyncDirectory(Constant.UpdateAddressDetails, arrayList, Profile.this).execute();
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public class WebConnectionAsyncDirectory extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(Profile.this, R.style.TBProgressBar);
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
                            Picasso.with(Profile.this).load(objects.getString("profilePic").toString()).transform(new CircleTransform())
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
                        String myKey = personalMemberDetailsList.getString("key").toString();
                        String myValue = personalMemberDetailsList.getString("value").toString();
                        String myUniqueKey = personalMemberDetailsList.getString("uniquekey").toString();
                        String myColType = personalMemberDetailsList.getString("colType").toString();
                        String mydate = myValue;
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
                        data.setDate(mydate);

                        profileDataArrayList_personal.add(data);




                        Gson gson = new Gson();
                        json = gson.toJson(profileDataArrayList_personal);
                        // String json = new Gson().toJson(profileDataArrayList.toString());
                        //Log.d("TOUCHBASE", "ARRAYLIST" + json);
                        //test(json);

                    }

                    profileAdapter = new ProfileAdapter(Profile.this, R.layout.profile_item_list, profileDataArrayList_personal);
                    listView.setAdapter(profileAdapter);
                    setListViewHeightBasedOnChildren(listView);

                    //----------------------Family-----------------------------------
                    familylist.clear();

                    JSONArray familyMemberDetailsArray = objects.getJSONArray("familyMemberDetails");
                    for (int j = 0; j < familyMemberDetailsArray.length(); j++) {
                        JSONObject familyMemberDetailsobject = familyMemberDetailsArray.getJSONObject(j);
                        JSONObject familyMemberDetailsList = familyMemberDetailsobject.getJSONObject("FamilyMemberDetil");

                        FamilyData data = new FamilyData();
                        data.setFamilyMemberId(familyMemberDetailsList.getString("familyMemberId").toString());
                        data.setMemberName(familyMemberDetailsList.getString("memberName").toString());
                        data.setRelationship(familyMemberDetailsList.getString("relationship").toString());
                        data.setdOB(familyMemberDetailsList.getString("dOB").toString());
                        data.setEmailID(familyMemberDetailsList.getString("emailID").toString());
                        data.setAnniversary(familyMemberDetailsList.getString("anniversary").toString());
                        data.setContactNo(familyMemberDetailsList.getString("contactNo").toString());
                        data.setParticulars(familyMemberDetailsList.getString("particulars").toString());
                        data.setBloodGroup(familyMemberDetailsList.getString("bloodGroup").toString());

                        familylist.add(data);


                    }
                    // profileAdapter = new ProfileAdapter(Profile.this, R.layout.profile_item_list, profileDataArrayList_personal);
                    // listView.setAdapter(profileAdapter);
                    // setListViewHeightBasedOnChildren(listView);
                    //----------------------Family-----------------------------------

                    profileDataArrayList_business.clear();
                    JSONArray businessMemberDetailsArray = objects.getJSONArray("BusinessMemberDetails");
                    for (int j = 0; j < businessMemberDetailsArray.length(); j++) {
                        JSONObject businessMemberDetailsobject = businessMemberDetailsArray.getJSONObject(j);
                        JSONObject businessMemberDetailsList = businessMemberDetailsobject.getJSONObject("BusinessMemberDetail");

                        ProfileData data1 = new ProfileData();
                        data1.setKey(businessMemberDetailsList.getString("key").toString());
                        data1.setValue(businessMemberDetailsList.getString("value").toString());
                        data1.setUniquekey(businessMemberDetailsList.getString("uniquekey").toString());
                        data1.setColType(businessMemberDetailsList.getString("colType").toString());
                        profileDataArrayList_business.add(data1);
                       /* Gson gson = new Gson();
                        profileDataArrayList_business = (ArrayList<ProfileData>) gson.fromJson(businessMemberDetailsList.toString(), new TypeToken<List<ProfileData>>() {
                        }.getType());*/

                        //Log.d("-------", "datalist------" + listView);

                    }
                    addresslist.clear();
                    JSONArray AddressMemberDetailsArray = objects.getJSONArray("addressDetails");
                    for (int j = 0; j < AddressMemberDetailsArray.length(); j++) {
                        JSONObject AddresssMemberDetailsobject = AddressMemberDetailsArray.getJSONObject(j);
                        JSONObject AddressMemberDetailsList = AddresssMemberDetailsobject.getJSONObject("Address");

                        Log.d("TOUCHBASE", "@@@@@@@@@@@:- " + AddressMemberDetailsList.toString());

                        AddressData data1 = new AddressData();
                        data1.setAddressID(AddressMemberDetailsList.getString("addressID").toString());
                        data1.setAddressType(AddressMemberDetailsList.getString("addressType").toString());
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
                    setAddess("Residence");
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
                   /* profileAdapter = new ProfileAdapter(Profile.this, R.layout.profile_item_list, profileDataArrayList_business);
                    listView.setAdapter(profileAdapter);
                    setListViewHeightBasedOnChildren(listView);*//* profileAdapter = new ProfileAdapter(Profile.this, R.layout.profile_item_list, profileDataArrayList_business);
                    listView.setAdapter(profileAdapter);
                    setListViewHeightBasedOnChildren(listView);*/
                    //scrollview.fullScroll(ScrollView.FOCUS_UP);
                    // scrollview.scrollTo(0, 0);
                }
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

            vwPersonalTop.setVisibility(View.VISIBLE);
            vwPersonalBottom.setVisibility(View.VISIBLE);
            vwBusinessTop.setVisibility(View.GONE);
            vwBusinessBottom.setVisibility(View.GONE);
            vwFamilyTop.setVisibility(View.GONE);
            vwFamilyBottom.setVisibility(View.GONE);

        } else {
            linear_personal.setVisibility(View.GONE);
            if (isBusinDetVisible.equals("yes")) {
                flag_edit = "1";
                profileAdapter = new ProfileAdapter(Profile.this, R.layout.profile_item_list, profileDataArrayList_business);
                listView.setAdapter(profileAdapter);
                setListViewHeightBasedOnChildren(listView);
                tv_personalbtn.setTextColor(getResources().getColor(R.color.black));
                tv_businessbtn.setTextColor(getResources().getColor(R.color.blueonclickcolor));
                tv_familybtn.setTextColor(getResources().getColor(R.color.black));

                vwPersonalTop.setVisibility(View.GONE);
                vwPersonalBottom.setVisibility(View.GONE);
                vwBusinessTop.setVisibility(View.VISIBLE);
                vwBusinessBottom.setVisibility(View.VISIBLE);
                vwFamilyTop.setVisibility(View.GONE);
                vwFamilyBottom.setVisibility(View.GONE);

                linear_address.setVisibility(View.VISIBLE);
               // tv_edit.setText("Edit");
                setAddess("Business");
            } else {
                flag_edit = "2";
                FamilyDetailsAdapter = new FamilyDetailsAdapter(Profile.this, R.layout.profile_family_item_list, familylist, memberprofileid);
                listView.setAdapter(FamilyDetailsAdapter);
                setListViewHeightBasedOnChildren(listView);
                linear_address.setVisibility(View.GONE);
               // tv_edit.setText("Add");
                tv_personalbtn.setTextColor(getResources().getColor(R.color.black));
                tv_businessbtn.setTextColor(getResources().getColor(R.color.black));
                tv_familybtn.setTextColor(getResources().getColor(R.color.blueonclickcolor));

                vwPersonalTop.setVisibility(View.GONE);
                vwPersonalBottom.setVisibility(View.GONE);
                vwBusinessTop.setVisibility(View.GONE);
                vwBusinessBottom.setVisibility(View.GONE);
                vwFamilyTop.setVisibility(View.VISIBLE);
                vwFamilyBottom.setVisibility(View.VISIBLE);
            }
        }

        if (isBusinDetVisible.equals("yes")) {
            linear_business.setVisibility(View.VISIBLE);
        } else {
            linear_business.setVisibility(View.GONE);
            if (isFamilDetailVisible.equals("yes")) {
                flag_edit = "2";
                FamilyDetailsAdapter = new FamilyDetailsAdapter(Profile.this, R.layout.profile_family_item_list, familylist, memberprofileid);
                listView.setAdapter(FamilyDetailsAdapter);
                setListViewHeightBasedOnChildren(listView);
                linear_address.setVisibility(View.GONE);
               // tv_edit.setText("Add");
                tv_personalbtn.setTextColor(getResources().getColor(R.color.black));
                tv_businessbtn.setTextColor(getResources().getColor(R.color.black));
                tv_familybtn.setTextColor(getResources().getColor(R.color.blueonclickcolor));

                vwPersonalTop.setVisibility(View.GONE);
                vwPersonalBottom.setVisibility(View.GONE);
                vwBusinessTop.setVisibility(View.GONE);
                vwBusinessBottom.setVisibility(View.GONE);
                vwFamilyTop.setVisibility(View.VISIBLE);
                vwFamilyBottom.setVisibility(View.VISIBLE);

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
                listcounty.add(new CountryData("0", "0", "Select Country"));
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

    public void setAddess(String type) {
        if (addresslist.isEmpty()) {
            tv_addrss.setText("");

        } else {
            breakLoop:
            for (int i = 0; i < addresslist.size(); i++) {
                //Residence
                if (addresslist.get(i).getAddressType().equals(type)) {
                    //addresslist.get(i).getAddressType();
                    linear_address.setVisibility(View.VISIBLE);
                    if (addresslist.get(i).getAddress().equalsIgnoreCase("") && addresslist.get(i).getCity().equalsIgnoreCase("") && addresslist.get(i).getPincode().equalsIgnoreCase("") && addresslist.get(i).getState().equalsIgnoreCase("") && addresslist.get(i).getCountry().equalsIgnoreCase("")) {
                        tv_addrss.setText("");
                    }else {
                        tv_addrss.setText("" + addresslist.get(i).getAddress() + " , " + addresslist.get(i).getCity() + " , " + addresslist.get(i).getPincode() + " , " + addresslist.get(i).getState() + " , " + addresslist.get(i).getCountry());
                    }
                    country_id = addresslist.indexOf(addresslist.get(i).getCountry().toString());
                    break breakLoop;

                } else {
                    tv_addrss.setText("");
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


                tv_personalbtn.setTextColor(getResources().getColor(R.color.blueonclickcolor));
                tv_businessbtn.setTextColor(getResources().getColor(R.color.black));
                tv_familybtn.setTextColor(getResources().getColor(R.color.black));
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
                    dialog.hide();
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
                iv_profileimage.setImageResource(R.drawable.b_profile_pic);
                hasimageflag = "0";
            } else {
                Utils.showToastWithTitleAndContext(getApplicationContext(), "Failed to DELETE, please Try Again");
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
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

    public void EditAddressPOPUP(final String type) {
        final Dialog dialog = new Dialog(Profile.this, android.R.style.Theme_Translucent);
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

        spinner_group_country.setAdapter(new SpinnerAdapter_country(Profile.this, listcounty));
        spinner_group_country.setSelection(0);
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
                    Log.e("TouchBase", "♦♦♦♦Address id : "+AddressId);
                    et_address1.setText(addresslist.get(i).getAddress());
                    et_city.setText(addresslist.get(i).getCity());
                    et_pincode.setText(addresslist.get(i).getPincode());
                    et_state.setText(addresslist.get(i).getState());
                    String countryName = addresslist.get(i).getCountry();
                    if ( countryName.equals(""))
                        countryName = "Select Country";
                    int position = getPosition(countryName);

                    if ( position != -1 ){
                        spinner_group_country.setSelection(position);
                    }
                    else {
                        Log.e("NoCountrySet", "No country set");
                    }
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
                if ( spinner_group_country.getSelectedItemPosition() == 0 )
                    arrayList.add(new BasicNameValuePair("country", ""));
                else
                    arrayList.add(new BasicNameValuePair("country", selected_countryname[0]));

                arrayList.add(new BasicNameValuePair("pincode", et_pincode.getText().toString()));
                arrayList.add(new BasicNameValuePair("phoneNo", ""));
                arrayList.add(new BasicNameValuePair("fax", ""));
                arrayList.add(new BasicNameValuePair("profileID", memberprofileid));
                arrayList.add(new BasicNameValuePair("groupID", groupId));

                flag_callwebsercie = "2";
                //  arrayList.add(new BasicNameValuePair("groupId", groupId));


                Log.d("Response", "PARAMETERS " + Constant.UpdateAddressDetails + " :- " + arrayList.toString());
                if ( InternetConnection.checkConnection(context)) {
                    new WebConnectionAsyncDirectory(Constant.UpdateAddressDetails, arrayList, Profile.this).execute();
                } else {
                    Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                }




                dialog.dismiss();
            }
        });

        spinner_group_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Touchbase", "♦♦♦♦Selected Country :- " + listcounty.get(position).getCountryName());
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

        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
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
                    Crop.pickImage(Profile.this);

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
        if (requestCode == 3) {

            //profileDataArrayList_personal.clear();
            tv_personalbtn.setTextColor(getResources().getColor(R.color.black));
            tv_businessbtn.setTextColor(getResources().getColor(R.color.black));
            tv_familybtn.setTextColor(getResources().getColor(R.color.black));

            vwPersonalTop.setVisibility(View.VISIBLE);
            vwPersonalBottom.setVisibility(View.VISIBLE);
            vwBusinessTop.setVisibility(View.GONE);
            vwBusinessBottom.setVisibility(View.GONE);
            vwFamilyTop.setVisibility(View.GONE);
            vwFamilyBottom.setVisibility(View.GONE);

            webservices();
            flag_edit = "0";
            //tv_edit.setText("Edit");
                  /*String message=data.getStringExtra("MESSAGE");
                    textView1.setText(message);*/
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

                    responsefromimageupload = Utils.doFileUploadForProfilePic(Profile.this,new File(picturePath.toString()), memberprofileid, groupId,"profile"); // Upload File to server
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
                iv_profileimage.setImageDrawable(Drawable.createFromPath(picturePath));
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

    public int getPosition(String countryName) {

        int n = listcounty.size();
        Log.e("Search for", countryName);
        for(int i=0;i<n;i++) {
            String myCountryName = listcounty.get(i).getCountryName();
            Log.e("myCountryName", myCountryName+" <---> "+countryName);
            if ( myCountryName.equalsIgnoreCase(countryName)) {
                return i;
            }
        }
        return -1;
    }


}
