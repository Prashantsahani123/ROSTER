package com.NEWROW.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.PopupCallRVAdapter;
import com.NEWROW.row.Adapter.PopupEmailRVAdapter;
import com.NEWROW.row.Adapter.PopupMsgRVAdapter;
import com.NEWROW.row.Data.profiledata.PopupEmailData;
import com.NEWROW.row.Data.profiledata.PopupPhoneNumberData;
import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.CircleTransform;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.MarshMallowPermission;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;


/**
 * Created by admin on 25-04-2017.
 */

public class RotarianBusinessDetails_ProfileActivity extends Activity {

    private TextView tv_title, tv_name, tv_mobile,tv_mobile_secondary, tv_email,tv_email_secondary, tv_address, tv_businessName, tv_designation,
            tv_bussiness_phoneNo, tv_faxNo, tv_clubname,tv_classification,tv_keywords,tv_club_designation,tv_blood_group,tv_donor_recognition,tv_rotaryId,tv_birthday,tv_anniversary,tv_club_name;
    Context context;
    private ImageView ivCallButton, ivMessageButton,ivWhatsapp,iv_share;
    private ImageView iv_backbutton, iv_email, iv_profileimage;
    public String memberProfileId;
    public String clubName,picUrl="";
    // varible declared to see if email is present
    public String isEmailAvailable = "0",classification="",keywords="",businessAddress="";
    private LinearLayout ll_mobile,ll_mobile_secondary,ll_classification, ll_email,ll_email_secondary, ll_address, ll_business_Name, ll_designation, ll_business_phoneNo, ll_faxNo,ll_keywords;
    private LinearLayout ll_whatsapp,ll_club_designation,ll_blood_group,ll_donor_recognition,ll_rotary_id,ll_birthday,ll_anniversary,ll_club_name;
    private ProgressBar progressbar;
    private ArrayList<PopupPhoneNumberData> myCallList = new ArrayList<>();
    private ArrayList<PopupPhoneNumberData> myMsgList = new ArrayList<>();
    private ArrayList<PopupEmailData> myMailList = new ArrayList<>();
    private boolean isFromDistrictClubs=false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rotarian_businessdetails_profile);

        context = this;

        actionbarfunction();

        init();

        Intent i = getIntent();

        if (i.hasExtra("memberProfileId")) {
            memberProfileId = i.getStringExtra("memberProfileId");
        }

        if (i.hasExtra("clubname")) {
            clubName = i.getStringExtra("clubname");
            tv_clubname.setText(clubName);
        }

        if (i.hasExtra("From District Clubs")) {
             isFromDistrictClubs = i.getBooleanExtra("From District Clubs",false);
        }

        if (InternetConnection.checkConnection(getApplicationContext())) {
            webservices();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
        }

    }

    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText("Profile");
    }

    private void init() {
        tv_name = (TextView) findViewById(R.id.tv_member_name);
        tv_mobile = (TextView) findViewById(R.id.tv_mobile);
        tv_mobile_secondary = (TextView) findViewById(R.id.tv_mobile_secondary);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_email_secondary = (TextView) findViewById(R.id.tv_email_secondary);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_businessName = (TextView) findViewById(R.id.tv_businessName);
        tv_designation = (TextView) findViewById(R.id.tv_designation);
        tv_bussiness_phoneNo = (TextView) findViewById(R.id.tv_bussiness_phoneNo);
        tv_faxNo = (TextView) findViewById(R.id.tv_faxNo);
        tv_classification = (TextView) findViewById(R.id.tv_classification);
        tv_keywords = (TextView) findViewById(R.id.tv_keywords);
        tv_club_designation = (TextView) findViewById(R.id.tv_club_designation);
        tv_blood_group = (TextView) findViewById(R.id.tv_blood_group);
        tv_donor_recognition = (TextView) findViewById(R.id.tv_donor_recognition);
        tv_rotaryId = (TextView) findViewById(R.id.tv_rotaryId);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        tv_anniversary = (TextView) findViewById(R.id.tv_anniversary);
        tv_club_name = (TextView) findViewById(R.id.tv_club_name);

        iv_share = (ImageView) findViewById(R.id.iv_share);
        ivWhatsapp = (ImageView) findViewById(R.id.ivWhatsApp);
        ll_whatsapp=(LinearLayout)findViewById(R.id.ll_whatsapp) ;
        ll_classification = (LinearLayout) findViewById(R.id.ll_classification);
        ll_mobile = (LinearLayout) findViewById(R.id.ll_mobile);
        ll_mobile_secondary = (LinearLayout) findViewById(R.id.ll_mobile_secondary);
        ll_email = (LinearLayout) findViewById(R.id.ll_email);
        ll_email_secondary = (LinearLayout) findViewById(R.id.ll_email_secondary);
        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        ll_business_Name = (LinearLayout) findViewById(R.id.ll_businessName);
        ll_designation = (LinearLayout) findViewById(R.id.ll_designation);
        ll_business_phoneNo = (LinearLayout) findViewById(R.id.ll_business_phoneNo);
        ll_keywords = (LinearLayout) findViewById(R.id.ll_keywords);
        ll_club_designation = (LinearLayout) findViewById(R.id.ll_club_designation);
        ll_blood_group = (LinearLayout) findViewById(R.id.ll_blood_group);
        ll_donor_recognition = (LinearLayout) findViewById(R.id.ll_donor_recognition);
        ll_rotary_id = (LinearLayout) findViewById(R.id.ll_rotary_id);
        ll_birthday = (LinearLayout) findViewById(R.id.ll_birthday);
        ll_anniversary = (LinearLayout) findViewById(R.id.ll_anniversary);
        ll_club_name = (LinearLayout) findViewById(R.id.ll_club_name);

        ll_faxNo = (LinearLayout) findViewById(R.id.ll_faxNo);
        iv_email = (ImageView) findViewById(R.id.iv_email);
        ivCallButton = (ImageView) findViewById(R.id.call_button);
        ivMessageButton = (ImageView) findViewById(R.id.iv_sms);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        iv_profileimage = (ImageView) findViewById(R.id.iv_profileimage);
        tv_clubname = (TextView) findViewById(R.id.tv_clubname);

        iv_profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent zoomImage = new Intent(context, ImageZoom.class);
                zoomImage.putExtra("imgageurl",picUrl);
                startActivity(zoomImage);
            }
        });

        ivCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myCallList.size() > 0) {
                    showCallPopup();
                }
            }
        });

        ivMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myMsgList.size() > 0) {
                    showMsgPopup();
                }
            }
        });

        ivWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utils.appInstalledOrNot(context)){
                    String mobileNo="";
                    for(PopupPhoneNumberData data:myMsgList){
                        if(data.getName().equalsIgnoreCase("Mobile")){
                            mobileNo=data.getNumber();
                            break;
                        }
                    }
                    String url = "https://api.whatsapp.com/send?phone="+mobileNo;//"https://wa.me/"+profileData.getMobile()+"/?text=hii";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else  if(Utils.whatsBusinessAppInstalledOrNot(context)){
                    String mobileNo="";
                    for(PopupPhoneNumberData data:myMsgList){
                        if(data.getName().equalsIgnoreCase("Mobile")){
                            mobileNo=data.getNumber();
                            break;
                        }
                    }
                    String url = "https://api.whatsapp.com/send?phone="+mobileNo;//"https://wa.me/"+profileData.getMobile()+"/?text=hii";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }

                else {
                    Utils.showToastWithTitleAndContext(context,"WhatsApp is not installed");
                }


            }
        });

        iv_share.setVisibility(View.VISIBLE);

        final MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);

        iv_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                } else {
                    shareContact();
                }
            }
        });

    }


    private void shareContact(){

        String saveFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(); //getExternalStorageDirectory().toString();//ScreenshotUtils.getMainDirectoryName(NewProfileActivity.this);

        File dir = new File(saveFilePath+"/Row_vcf");

        if (!dir.exists()) {
            boolean isCreated = dir.mkdirs();
            Log.d("sa",isCreated+" directory created path=>"+dir.getAbsolutePath());
        }


//        Log.d("sa","created path=>"+dir.getAbsolutePath());

        String today = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());

        String fileName = "ROW_"+today+".vcf";

        File vcfFile = new File(dir, fileName);

        try {

            FileWriter fw = null;
            fw = new FileWriter(vcfFile);
            fw.write("BEGIN:VCARD\r\n");
            fw.write("VERSION:3.0\r\n");
            // fw.write("N:" + p.getSurname() + ";" + p.getFirstName() + "\r\n");
            fw.write("FN:" + tv_name.getText().toString() + "\r\n");
            //  fw.write("ORG:" + p.getCompanyName() + "\r\n");
            //  fw.write("TITLE:" + p.getTitle() + "\r\n");
            fw.write("TEL;TYPE=CELL:" + tv_mobile.getText().toString() + "\r\n");
            //   fw.write("TEL;TYPE=HOME,VOICE:" + p.getHomePhone() + "\r\n");
            //   fw.write("ADR;TYPE=WORK:;;" + p.getStreet() + ";" + p.getCity() + ";" + p.getState() + ";" + p.getPostcode() + ";" + p.getCountry() + "\r\n");
            fw.write("EMAIL;TYPE=PREF,HOME:" + tv_email.getText().toString() + "\r\n");

            businessAddress = tv_address.getText().toString();

            Log.e("row","bussniss add="+businessAddress);

            if(!businessAddress.equalsIgnoreCase("")) {
                fw.write("ADR;TYPE=WORK:;;" + businessAddress + "\r\n");
            }

            fw.write("NOTE:" + classification +" , "+keywords+ "\r\n");

            fw.write("END:VCARD\r\n");
            fw.close();

            Intent i = new Intent(); //this will import vcf in contact list

            i.setAction(Intent.ACTION_SEND);

//            i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(vcfFile));

            Uri apkURI = FileProvider.getUriForFile(
                    context,
                    "com.SampleApp.row.fileprovider", vcfFile);

            Log.d("sa","selected uri=>"+apkURI.toString());

            i.putExtra(Intent.EXTRA_STREAM,apkURI);

            i.setType("application/vcf");//("text/x-vcard");

            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(i);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void webservices() {

        Hashtable<String, String> params = new Hashtable<>();
        params.put("memberProfileId", memberProfileId);//memberProfileId

        try {
            final ProgressDialog progressDialog = new ProgressDialog(RotarianBusinessDetails_ProfileActivity.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            Log.d("row", Constant.GETRotarianDetails+" params = "+params.toString());

            JSONObject requestData = new JSONObject(new Gson().toJson(params));
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constant.GETRotarianDetails,
                    requestData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            progressDialog.dismiss();

                            try {
                                getResult(response.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Log.e("ROW", "â™¦Error : " + error);
                            error.printStackTrace();
                            Toast.makeText(RotarianBusinessDetails_ProfileActivity.this, "Failed to receive Rotarian from server . Please try again.", Toast.LENGTH_LONG).show();

                        }
                    }
            );
            Utils.log("API : " + Constant.GETRotarianDetails + " : " + params);
            AppController.getInstance().addToRequestQueue(RotarianBusinessDetails_ProfileActivity.this, request);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void getResult(String result) {

        JSONObject json = null;

        try {

            json = new JSONObject(result);

            JSONObject jsonTBGetRotarianResult = json.getJSONObject("TBGetRotarianResult");
            final String status = jsonTBGetRotarianResult.getString("status");

            if (status.equals("0")) {

                JSONObject jsonResult = jsonTBGetRotarianResult.getJSONObject("Result");

                String name = jsonResult.get("memberName").toString();
                final String mobile = jsonResult.get("memberMobile").toString();
                final String mobileSecondary = jsonResult.get("SecondaryMobile").toString();
                final String email = jsonResult.get("Email").toString();
                final String memberEmail = jsonResult.get("memberEmail").toString();
                String address = jsonResult.get("BusinessAddress").toString();
                final String businessName = jsonResult.get("BusinessName").toString();
                String designation = jsonResult.get("designation").toString();
                final String businessPhone = jsonResult.get("phoneNo").toString();
                String faxNo = jsonResult.get("Fax").toString();
                String city = jsonResult.get("city").toString();
                String state = jsonResult.get("state").toString();
                String pinCode = jsonResult.get("pincode").toString();
                String country = jsonResult.get("country").toString();
                String classification=jsonResult.getString("Classification");
                String keywords = jsonResult.getString("Keywords");
                String clubDesignation = jsonResult.getString("clubDesignation");
                String clubName = jsonResult.getString("clubName");
                String bloodGroup=jsonResult.getString("blood_Group");
                String donorRecognition=jsonResult.getString("Donor_Recognition");
                String rotaryId = jsonResult.getString("rotaryid");
                String birthday = jsonResult.getString("member_date_of_birth");
                String anniversary = jsonResult.getString("member_date_of_wedding");

                this.classification = clubDesignation;
                this.keywords = keywords;


                if (jsonResult.has("pic")) {

                    if (jsonResult.getString("pic").toString().trim().equals("") || jsonResult.getString("pic").toString() == null || jsonResult.getString("pic").toString().trim().isEmpty()) {

                        progressbar.setVisibility(View.GONE);

                    } else {

                        String picturePath = jsonResult.getString("pic").toString().trim().replaceAll(" ", "%20");
                        progressbar.setVisibility(View.VISIBLE);

                        Utils.log("Profile path is : "+picturePath);

                        picUrl = jsonResult.getString("pic").toString().trim().replaceAll(" ", "%20");

                       // Picasso.with(RotarianBusinessDetails_ProfileActivity.this).load(picturePath).transform(new CircleTransform()).into(iv_profileimage);

                        Picasso.with(RotarianBusinessDetails_ProfileActivity.this).load(picturePath).transform(new CircleTransform())
                                .placeholder(R.drawable.b_profile_pic)
                                .into(iv_profileimage, new Callback() {

                                    @Override
                                    public void onSuccess() {
                                        progressbar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError() {
                                        progressbar.setVisibility(View.GONE);
                                    }
                                }

                            );
                    }
                }

                if (name.trim().equalsIgnoreCase("") || name.trim().equalsIgnoreCase("null")) {
                    ll_mobile.setVisibility(View.GONE);
                } else {
                    tv_name.setText(name);
                }

                if (mobile.trim().equalsIgnoreCase("") || mobile.trim().equalsIgnoreCase("null")) {
                    ll_mobile.setVisibility(View.GONE);
                } else {
                    myCallList.add(new PopupPhoneNumberData(mobile, "Mobile"));
                    myMsgList.add(new PopupPhoneNumberData(mobile, "Mobile"));
                    tv_mobile.setText(mobile);
                    tv_mobile.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + mobile));
                            startActivity(intent);
                        }
                    });
                }

                if (mobileSecondary.trim().equalsIgnoreCase("") || mobileSecondary.trim().equalsIgnoreCase("null")) {
                    ll_mobile_secondary.setVisibility(View.GONE);
                } else {
                    ll_mobile_secondary.setVisibility(View.VISIBLE);
                    myCallList.add(new PopupPhoneNumberData(mobileSecondary, "Mobile"));
                    myMsgList.add(new PopupPhoneNumberData(mobileSecondary, "Mobile"));
                    tv_mobile_secondary.setText(mobileSecondary);
                    tv_mobile_secondary.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + mobileSecondary));
                            startActivity(intent);
                        }
                    });
                }


                if (memberEmail.trim().equalsIgnoreCase("") || memberEmail.trim().equalsIgnoreCase("null")) {
                    ll_email.setVisibility(View.GONE);
                } else {
                    tv_email.setText(memberEmail);
                    PopupEmailData data = new PopupEmailData(memberEmail, "Email Id");
                    myMailList.add(data);
                }

                if (email.trim().equalsIgnoreCase("") || email.trim().equalsIgnoreCase("null")) {
                    ll_email_secondary.setVisibility(View.GONE);
                } else {
                    ll_email_secondary.setVisibility(View.VISIBLE);
                    tv_email_secondary.setText(email);
                    PopupEmailData data = new PopupEmailData(email, "Email Id");
                    myMailList.add(data);
                }

                if (businessName.trim().equalsIgnoreCase("") || businessName.trim().equalsIgnoreCase("null")) {
                    ll_business_Name.setVisibility(View.GONE);
                } else {
                    tv_businessName.setText(businessName);
                }

                if (designation.trim().equalsIgnoreCase("") || designation.trim().equalsIgnoreCase("null")) {
                    ll_designation.setVisibility(View.GONE);
                } else {
                    tv_designation.setText(designation);
                }

                if (businessPhone.trim().equalsIgnoreCase("") || businessPhone.trim().equalsIgnoreCase("null")) {
                    ll_business_phoneNo.setVisibility(View.GONE);
                } else {
                    myCallList.add(new PopupPhoneNumberData(businessPhone, "Business Contact"));
                    myMsgList.add(new PopupPhoneNumberData(businessPhone, "Business Contact"));
                    tv_bussiness_phoneNo.setText(businessPhone);
                    tv_bussiness_phoneNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + businessPhone));
                            startActivity(intent);
                        }
                    });
                }

                if (faxNo.trim().equalsIgnoreCase("") || faxNo.trim().equalsIgnoreCase("null")) {
                    ll_faxNo.setVisibility(View.GONE);
                } else {
                    tv_faxNo.setText(faxNo);
                }

                if (myMailList.size()>0) {
                    iv_email.setImageResource(R.drawable.blue_mail);

                    iv_email.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showEmailPopup();
                        }
                    });
                } else {
                    iv_email.setImageResource(R.drawable.gray_mail);
                }

                if (city.trim().equalsIgnoreCase("") || city.trim().equalsIgnoreCase("null")) {
                    address = address;
                } else {
                    city = "," + city + ",";
                    address = address + city;
                }

                if (state.trim().equalsIgnoreCase("") || state.trim().equalsIgnoreCase("null")) {
                    address = address;
                } else {
                    state = state + ",";
                    address = address + state;
                }

                if (country.trim().equalsIgnoreCase("") || country.trim().equalsIgnoreCase("null")) {
                    address = address;
                } else {
                    address = address + country+"  "+ pinCode;
                }
//                 address = address + city + state+country;

                if (address.trim().equalsIgnoreCase("") || address.trim().equalsIgnoreCase("null")) {
                    ll_address.setVisibility(View.GONE);
                } else {
                    tv_address.setText(address);
                }


                if(isFromDistrictClubs){

                    if (rotaryId.trim().equalsIgnoreCase("") || rotaryId.trim().equalsIgnoreCase("null")) {
                        ll_rotary_id.setVisibility(View.GONE);
                    } else {
                        ll_rotary_id.setVisibility(View.VISIBLE);
                        tv_rotaryId.setText(rotaryId);
                    }

                    if (birthday.trim().equalsIgnoreCase("") || birthday.trim().equalsIgnoreCase("null")) {
                        ll_birthday.setVisibility(View.GONE);
                    } else {
                        ll_birthday.setVisibility(View.VISIBLE);
                        Date dob = new SimpleDateFormat("dd/MM/yyyy").parse(birthday);
                        tv_birthday.setText(new SimpleDateFormat("dd MMM").format(dob));
                    }

                    if (anniversary.trim().equalsIgnoreCase("") || anniversary.trim().equalsIgnoreCase("null")) {
                        ll_anniversary.setVisibility(View.GONE);
                    } else {
                        ll_anniversary.setVisibility(View.VISIBLE);
                        Date doa = new SimpleDateFormat("dd/MM/yyyy").parse(anniversary);
                        tv_anniversary.setText(new SimpleDateFormat("dd MMM").format(doa));
                    }
                }

                if (classification.trim().equalsIgnoreCase("") || classification.trim().equalsIgnoreCase("null")) {
                    ll_classification.setVisibility(View.GONE);
                } else {
                    ll_classification.setVisibility(View.VISIBLE);
                    tv_classification.setText(classification);
                }

                if (keywords.trim().equalsIgnoreCase("") || keywords.trim().equalsIgnoreCase("null")) {
                    ll_keywords.setVisibility(View.GONE);
                } else {
                    ll_keywords.setVisibility(View.VISIBLE);
                    tv_keywords.setText(keywords);
                }

                if (clubDesignation.trim().equalsIgnoreCase("") || clubDesignation.trim().equalsIgnoreCase("null")) {
                    ll_club_designation.setVisibility(View.GONE);
                } else {
                    ll_club_designation.setVisibility(View.VISIBLE);
                    tv_club_designation.setText(clubDesignation);
                }

                Log.e("find","club name =>"+clubName);

                if (clubName.trim().equalsIgnoreCase("") || clubName.trim().equalsIgnoreCase("null")) {
                    ll_club_name.setVisibility(View.GONE);
                } else {
                    ll_club_name.setVisibility(View.VISIBLE);
                    tv_club_name.setText(clubName);
                }

                // add by satish on 28-05-2019
               /* if (bloodGroup.trim().equalsIgnoreCase("") || bloodGroup.trim().equalsIgnoreCase("null")) {
                    ll_blood_group.setVisibility(View.GONE);
                } else {
                    ll_blood_group.setVisibility(View.VISIBLE);
                    tv_blood_group.setText(bloodGroup);
                }*/

                if (donorRecognition.trim().equalsIgnoreCase("") || donorRecognition.trim().equalsIgnoreCase("null")) {
                    ll_donor_recognition.setVisibility(View.GONE);
                } else {
                    ll_donor_recognition.setVisibility(View.VISIBLE);
                    tv_donor_recognition.setText(donorRecognition);
                }


                if (myCallList.size() > 0) {
                    ivCallButton.setImageDrawable(getResources().getDrawable(R.drawable.blue_call));
                }

                if (myMsgList.size() > 0) {
                    ivMessageButton.setImageDrawable(getResources().getDrawable(R.drawable.blue_message));
                }

                String mobileNo = PreferenceManager.getPreference(context,PreferenceManager.MOBILE_NUMBER);

                for(PopupPhoneNumberData data:myMsgList){

                    String dataNumber=data.getNumber();

                    if(dataNumber.length()>10){
                        dataNumber=dataNumber.substring(dataNumber.length()-10);
                    }

                    Utils.log(dataNumber);

                    if(data.getName().equalsIgnoreCase("Mobile") && mobileNo.equalsIgnoreCase(dataNumber)){
                        ll_whatsapp.setVisibility(View.GONE);
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMsgPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = getLayoutInflater().inflate(R.layout.new_popup_msg, null);
        builder.setView(view);

        final AlertDialog msgDialog = builder.create();

        view.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int n = myMsgList.size();

                for (int i = 0; i < n; i++) {
                    PopupPhoneNumberData pnd = myMsgList.get(i);
                    pnd.setSelected(false);
                }

                msgDialog.hide();
            }
        });

        view.findViewById(R.id.ll_send).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        Utils.log("Phone numbers are : " + myCallList);

        PopupMsgRVAdapter popupMsgRVAdapter = new PopupMsgRVAdapter(context, myMsgList);

        RecyclerView rvPhoneNumbers = (RecyclerView) view.findViewById(R.id.rvCallList);
        rvPhoneNumbers.setLayoutManager(new LinearLayoutManager(context));
        popupMsgRVAdapter.setOnPhoneNumberClickedListener(new PopupMsgRVAdapter.OnPhoneNumberClickedListener() {
            @Override
            public void phoneNumberClicked(PopupPhoneNumberData pnd, int position) {
                Intent msgIntent = new Intent(Intent.ACTION_VIEW);
                //msgIntent.setType("vnd.android-dir/mms-sms");
                //msgIntent.putExtra("address", pnd.getNumber());
                msgIntent.setData(Uri.parse("smsto: " + Uri.encode(pnd.getNumber())));
                startActivity(msgIntent);
                msgDialog.hide();
            }
        });

        view.findViewById(R.id.ll_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                int n = myMsgList.size();
                for (int i = 0; i < n; i++) {
                    PopupPhoneNumberData pnd = myMsgList.get(i);

                    if (pnd.isSelected()) {
                        count++;
                    }
                }

                if (count == 0) {
                    Toast.makeText(context, "Please select at least one mobile number to send message", Toast.LENGTH_LONG).show();
                    return;
                }
                msgDialog.hide();
                sendMessage();
            }
        });

        rvPhoneNumbers.setAdapter(popupMsgRVAdapter);
        msgDialog.show();
    }

    /*public void showEmailPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = getLayoutInflater().inflate(R.layout.new_mail_popup, null);
        builder.setView(view);

        final AlertDialog mailDialog = builder.create();
        view.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailDialog.hide();
            }
        });

        PopupEmailRVAdapter popupMailRVAdapter = new PopupEmailRVAdapter(context, myMailList);
        popupMailRVAdapter.setOnEmailIdClickedListener(new PopupEmailRVAdapter.OnEmailIdClickedListener() {
            @Override
            public void onEmailIdClickListener(PopupEmailData pnd, int position) {
                final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setType("plain/text");
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{pnd.getEmailId().toString()});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(emailIntent);
            }
        });
        RecyclerView rvMail = (RecyclerView) view.findViewById(R.id.rvMail);
        rvMail.setLayoutManager(new LinearLayoutManager(context));
        rvMail.setAdapter(popupMailRVAdapter);
        mailDialog.show();
    }*/
    public void showEmailPopup() {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = getLayoutInflater().inflate(R.layout.new_mail_popup, null);
        builder.setView(view);

        final AlertDialog mailDialog = builder.create();

        final RecyclerView rvMail = (RecyclerView) view.findViewById(R.id.rvMail);
        rvMail.setLayoutManager(new LinearLayoutManager(context));


        view.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<PopupEmailData> myMsgList =((PopupEmailRVAdapter)rvMail.getAdapter()).getList();
                int n = myMsgList.size();
                ArrayList<String> selectedList = new ArrayList<>();

                for (int i = 0; i < n; i++) {
                    PopupEmailData pnd = myMsgList.get(i);
                   pnd.setSelected(false);
                }

                mailDialog.hide();
            }
        });

        PopupEmailRVAdapter popupMailRVAdapter = new PopupEmailRVAdapter(context, myMailList);
        popupMailRVAdapter.setOnEmailIdClickedListener(new PopupEmailRVAdapter.OnEmailIdClickedListener() {
            @Override
            public void onEmailIdClickListener(PopupEmailData pnd, int position) {
                final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setType("plain/text");
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{pnd.getEmailId().toString()});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(emailIntent);
            }
        });


        view.findViewById(R.id.ll_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                ArrayList<PopupEmailData> myMsgList =((PopupEmailRVAdapter)rvMail.getAdapter()).getList();
                int n = myMsgList.size();
                ArrayList<String> selectedList = new ArrayList<>();

                for (int i = 0; i < n; i++) {
                    PopupEmailData pnd = myMsgList.get(i);
                    if (pnd.isSelected()) {
                        selectedList.add(pnd.getEmailId());
                        count++;
                    }
                }

                if (count == 0) {
                    Toast.makeText(context, "Please select at least one email id to send mail", Toast.LENGTH_LONG).show();
                    return;
                }else {
                    String address = Utils.implode(", ", selectedList);

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setType("plain/text");
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                    try {
                        context.startActivity(emailIntent);
                    } catch (Exception e) {
                        Toast.makeText(context, "Sorry. Something went wrong.", Toast.LENGTH_LONG).show();
                    }

                    int m = myMsgList.size();
                    for (int i = 0; i < m; i++) {
                        myMsgList.get(i).setSelected(false);
                    }
                }
                mailDialog.hide();

            }
        });


        rvMail.setAdapter(popupMailRVAdapter);
        mailDialog.show();
    }
    public void sendMessage() {

        ArrayList<String> selectedList = new ArrayList<>();

        Intent msgIntent = new Intent(Intent.ACTION_VIEW);
        //msgIntent.setType("vnd.android-dir/mms-sms");
        int n = myMsgList.size();
        int count = 0;
        for (int i = 0; i < n; i++) {
            PopupPhoneNumberData pnd = myMsgList.get(i);
            if (pnd.isSelected()) {
                selectedList.add(pnd.getNumber());
                count++;
            }
        }

        if (count == 0) {
            Toast.makeText(context, "Please select at least one mobile number to send message", Toast.LENGTH_LONG).show();
            return;
        }

        String address = Utils.implode(", ", selectedList);
        msgIntent.setData(Uri.parse("smsto: " + Uri.encode(address)));
        //msgIntent.putExtra("address", address);
        startActivity(msgIntent);
        clearMsgSelection();
    }

    public void clearMsgSelection() {
        int n = myMsgList.size();
        for (int i = 0; i < n; i++) {
            myMsgList.get(i).setSelected(false);
        }
    }

    public void showCallPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = getLayoutInflater().inflate(R.layout.popup_call, null);
        builder.setView(view);

        final AlertDialog callDialog = builder.create();
        view.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDialog.hide();
            }
        });
        Utils.log("Phone numbers are : " + myCallList);
        PopupCallRVAdapter popupCallRVAdapter = new PopupCallRVAdapter(context, myCallList);
        popupCallRVAdapter.setOnPhoneNumberClickedListener(new PopupCallRVAdapter.OnPhoneNumberClickedListener() {
            @Override
            public void phoneNumberClicked(PopupPhoneNumberData pnd, int position) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + myCallList.get(position).getNumber()));
                callDialog.hide();
                startActivity(callIntent);
            }
        });
        RecyclerView rvPhoneNumbers = (RecyclerView) view.findViewById(R.id.rvCallList);
        rvPhoneNumbers.setLayoutManager(new LinearLayoutManager(context));
        rvPhoneNumbers.setAdapter(popupCallRVAdapter);
        callDialog.show();
    }

}
