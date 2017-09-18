package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.PopupCallRVAdapter;
import com.SampleApp.row.Adapter.PopupEmailRVAdapter;
import com.SampleApp.row.Adapter.PopupMsgRVAdapter;
import com.SampleApp.row.Data.profiledata.PopupEmailData;
import com.SampleApp.row.Data.profiledata.PopupPhoneNumberData;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.CircleTransform;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;


/**
 * Created by admin on 25-04-2017.
 */

public class RotarianBusinessDetails_ProfileActivity extends Activity {
    private TextView tv_title, tv_name, tv_mobile, tv_email, tv_address,
            tv_businessName, tv_designation, tv_bussiness_phoneNo, tv_faxNo, tv_clubname;
    Context context;
    private ImageView ivCallButton, ivMessageButton;
    private ImageView iv_backbutton, iv_email, iv_profileimage;
    public String memberProfileId;
    public String clubName;
    // varible declared to see if email is present
    public String isEmailAvailable = "0";
    private LinearLayout ll_mobile, ll_email, ll_address, ll_business_Name, ll_designation, ll_business_phoneNo, ll_faxNo;

    private ProgressBar progressbar;
    private ArrayList<PopupPhoneNumberData> myCallList = new ArrayList<>();
    private ArrayList<PopupPhoneNumberData> myMsgList = new ArrayList<>();
    private ArrayList<PopupEmailData> myMailList = new ArrayList<>();

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
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_businessName = (TextView) findViewById(R.id.tv_businessName);
        tv_designation = (TextView) findViewById(R.id.tv_designation);
        tv_bussiness_phoneNo = (TextView) findViewById(R.id.tv_bussiness_phoneNo);
        tv_faxNo = (TextView) findViewById(R.id.tv_faxNo);

        ll_mobile = (LinearLayout) findViewById(R.id.ll_mobile);
        ll_email = (LinearLayout) findViewById(R.id.ll_email);
        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        ll_business_Name = (LinearLayout) findViewById(R.id.ll_businessName);
        ll_designation = (LinearLayout) findViewById(R.id.ll_designation);
        ll_business_phoneNo = (LinearLayout) findViewById(R.id.ll_business_phoneNo);
        ll_faxNo = (LinearLayout) findViewById(R.id.ll_faxNo);
        iv_email = (ImageView) findViewById(R.id.iv_email);
        ivCallButton = (ImageView) findViewById(R.id.call_button);
        ivMessageButton = (ImageView) findViewById(R.id.iv_sms);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        iv_profileimage = (ImageView) findViewById(R.id.iv_profileimage);
        tv_clubname = (TextView) findViewById(R.id.tv_clubname);

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

    }

    private void webservices() {
        Hashtable<String, String> params = new Hashtable<>();
        params.put("memberProfileId", memberProfileId);//memberProfileId
        try {
            final ProgressDialog progressDialog = new ProgressDialog(RotarianBusinessDetails_ProfileActivity.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

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
                    },
                    new Response.ErrorListener() {
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
                final String email = jsonResult.get("Email").toString();
                String address = jsonResult.get("BusinessAddress").toString();
                final String businessName = jsonResult.get("BusinessName").toString();
                String designation = jsonResult.get("designation").toString();
                final String businessPhone = jsonResult.get("phoneNo").toString();
                String faxNo = jsonResult.get("Fax").toString();
                String city = jsonResult.get("city").toString();
                String state = jsonResult.get("state").toString();
                String country = jsonResult.get("country").toString();

                if (jsonResult.has("pic")) {
                    if (jsonResult.getString("pic").toString().trim().equals("") || jsonResult.getString("pic").toString() == null || jsonResult.getString("pic").toString().trim().isEmpty()) {
                        progressbar.setVisibility(View.GONE);
                    } else {
                        String picturePath = jsonResult.getString("pic").toString().trim().replaceAll(" ", "%20");
                        progressbar.setVisibility(View.VISIBLE);
                        Utils.log("Profile path is : "+picturePath);

                        Picasso.with(RotarianBusinessDetails_ProfileActivity.this).load(picturePath).transform(new CircleTransform()).into(iv_profileimage);

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
                if (email.trim().equalsIgnoreCase("") || email.trim().equalsIgnoreCase("null")) {
                    ll_email.setVisibility(View.GONE);
                    isEmailAvailable = "0";

                } else {
                    tv_email.setText(email);
                    isEmailAvailable = "1";
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

                if (isEmailAvailable.equalsIgnoreCase("1")) {
                    iv_email.setImageResource(R.drawable.blue_mail);
                    PopupEmailData data = new PopupEmailData(email, "Email Id");
                    myMailList.add(data);
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
                    address = address + country;
                }
//                 address = address + city + state+country;

                if (address.trim().equalsIgnoreCase("") || address.trim().equalsIgnoreCase("null")) {
                    ll_address.setVisibility(View.GONE);
                } else {

                    tv_address.setText(address);
                }

                if (myCallList.size() > 0) {
                    ivCallButton.setImageDrawable(getResources().getDrawable(R.drawable.blue_call));
                }

                if (myMsgList.size() > 0) {
                    ivMessageButton.setImageDrawable(getResources().getDrawable(R.drawable.blue_message));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMsgPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = getLayoutInflater().inflate(R.layout.popup_message, null);
        builder.setView(view);

        final AlertDialog msgDialog = builder.create();
        view.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgDialog.hide();
            }
        });

        view.findViewById(R.id.ivSend).setOnClickListener(new View.OnClickListener() {
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

        view.findViewById(R.id.ivSend).setOnClickListener(new View.OnClickListener() {
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

    public void showEmailPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = getLayoutInflater().inflate(R.layout.popup_email, null);
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
