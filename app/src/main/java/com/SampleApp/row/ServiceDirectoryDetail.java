package com.SampleApp.row;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.SampleApp.row.Data.ServiceDirectoryListData;
import com.SampleApp.row.Utils.CircleTransform;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.sql.ServiceDirectoryDataModel;

/**
 * Created by USER on 21-07-2016.
 */
public class ServiceDirectoryDetail extends Activity implements View.OnLongClickListener{
    String moduleName = "";
    TextView tv_name, tv_desc, tv_countryCode1, tv_countryCode2, tv_contactNo1, tv_contactNo2, tv_pax, et_city, et_state, et_country, et_zip;
    TextView tv_email, tv_address;
    /*de.hdodenhof.circleimageview.CircleImageView iv_pic;*/
    ImageView iv_pic;
    ImageView iv_actionbtn2;
    String serviceDirId = null;
    TextView tv_keywords;
    TextView tv_title, tv_done;
    ImageView iv_backbutton, iv_call_button, iv_message_button, iv_call_button2, iv_message_button2, iv_mail_button;
    private ImageView iv_actionbtn;
    //  private ProgressBar progressbar;
    RelativeLayout rl_image,rl_contactNo2;
    String serviceImage;
    private String isAdmin = "No";
    private String memberProfileID = "0";
    ServiceDirectoryDataModel serviceDirectoryDataModel;
    private ArrayList<ServiceDirectoryListData> serviceDirectoryListDatas = new ArrayList<ServiceDirectoryListData>();

    private long masterUid, grpId;

    String mobile, mobile2, mail, message, city, country, state, zip;

    String desc, pax, email_add, addre, name, img;
    int detailFlag = 1;

    ImageView iv_addIcon;
    private double latitude = 0.0;
    private double longitude = 0.0;

    LinearLayout ll_description,
            ll_contactNo1,
            ll_pax,
            ll_email,
            ll_address, ll_detailAddress,
            ll_keywords;
    String updatedOn = "";

    String categoryId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_directory_detail);
        serviceDirectoryDataModel = new ServiceDirectoryDataModel(ServiceDirectoryDetail.this);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        tv_title.setText("Details");

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_desc = (TextView) findViewById(R.id.tv_desc);


       /* tv_countryCode1 = (TextView)findViewById(R.id.tv_country_code);
        tv_countryCode2 = (TextView)findViewById(R.id.tv_country_code2);*/
        tv_contactNo1 = (TextView) findViewById(R.id.tv_contactNumber1);
        tv_contactNo2 = (TextView) findViewById(R.id.tv_contactNumber2);
        tv_pax = (TextView) findViewById(R.id.tv_pax);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_done = (TextView) findViewById(R.id.tv_done);
        tv_done.setVisibility(View.GONE);
        //  progressbar   = (ProgressBar) findViewById(R.id.progressbar);

        iv_actionbtn.setVisibility(View.VISIBLE); // EDIT ANNOUNCEMEBT
        iv_actionbtn.setImageResource(R.drawable.edit); // EDIT ANNOUNCEMEBT
        rl_image = (RelativeLayout) findViewById(R.id.rl_image);
       /* iv_pic = (de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.profile_pic);*/

        iv_pic = (ImageView) findViewById(R.id.profile_pic);

        iv_call_button = (ImageView) findViewById(R.id.call_button);
        iv_message_button = (ImageView) findViewById(R.id.message_button);
        iv_call_button2 = (ImageView) findViewById(R.id.call_button2);
        iv_message_button2 = (ImageView) findViewById(R.id.message_button2);

        iv_mail_button = (ImageView) findViewById(R.id.mail_button);
        iv_addIcon = (ImageView) findViewById(R.id.iv_addIcon);
        Intent i = getIntent();
        serviceDirId = i.getStringExtra("serviceDirId");
        Log.d("===========", "@@@@@@@@@@" + serviceDirId);
        //grpId = i.getStringExtra("groupId");

        et_city = (TextView) findViewById(R.id.et_city);
        et_country = (TextView) findViewById(R.id.et_country);
        et_state = (TextView) findViewById(R.id.et_state);
        et_zip = (TextView) findViewById(R.id.et_zip);
        tv_keywords = (TextView) findViewById(R.id.tv_keywords);


        iv_actionbtn2 = (ImageView) findViewById(R.id.iv_actionbtn2);
        iv_actionbtn.setVisibility(View.VISIBLE); // Delete ANNOUNCEMEBT
        iv_actionbtn2.setImageResource(R.drawable.delete); // Delete ANNOUNCEMEBT
        isAdmin = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN);
        memberProfileID = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID);

        masterUid = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)); // line added by lekha
        grpId = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID));

        ll_description = (LinearLayout) findViewById(R.id.ll_description);
        ll_contactNo1 = (LinearLayout) findViewById(R.id.ll_contactNo1);
      // ll_contactNo2 = (LinearLayout) findViewById(R.id.ll_contactNo2);
        ll_pax = (LinearLayout) findViewById(R.id.ll_paxNo);
        ll_email = (LinearLayout) findViewById(R.id.ll_emailadd);
        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        ll_detailAddress = (LinearLayout) findViewById(R.id.ll_detailAddress);
        ll_keywords = (LinearLayout) findViewById(R.id.ll_keywords);
        rl_contactNo2 = (RelativeLayout)findViewById(R.id.rl_contactno2);
        // webservices();
        serviceDirectoryDataModel.printTable();
        loadFromDB();
        init();
        adminsettings();
        initClickLsitner();

        if(getIntent().hasExtra("categoryId")) {
            categoryId = getIntent().getExtras().getString("categoryId");
        }
    }


    /* @Override
     protected void onResume() {
         loadFromDB();
         super.onResume();
     }
 */
    private void initClickLsitner() {
       /* tv_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyTextToClipBoard(tv_desc.getText().toString());
            }
        });*/

      //  tv_desc.setOnLongClickListener(this);
        tv_contactNo1.setOnLongClickListener(this);
        tv_contactNo2.setOnLongClickListener(this);
        tv_pax.setOnLongClickListener(this);
        tv_email.setOnLongClickListener(this);
        tv_address.setOnLongClickListener(this);
      //  tv_keywords.setOnLongClickListener(this);

        /*tv_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyTextToClipBoard(tv_desc.getText().toString());
            }
        });

        tv_contactNo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyTextToClipBoard(tv_contactNo1.getText().toString());
            }
        });

        tv_contactNo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyTextToClipBoard(tv_contactNo2.getText().toString());
            }
        });


        tv_pax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyTextToClipBoard(tv_pax.getText().toString());
            }
        });


        tv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyTextToClipBoard(tv_email.getText().toString());
            }
        });

        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyTextToClipBoard(tv_address.getText().toString());
            }
        });*/

    }

    @Override
    public boolean onLongClick(View v) {
        if ( v instanceof TextView ) {
            copyTextToClipBoard(((TextView) v).getText().toString());
        }
        return false;
    }

    //====================================================================

    private void adminsettings() {
        if (isAdmin.equals("No")) {
            iv_actionbtn.setVisibility(View.GONE);
            iv_actionbtn2.setVisibility(View.GONE);
        } else {
            iv_actionbtn.setVisibility(View.VISIBLE);
            iv_actionbtn2.setVisibility(View.VISIBLE);
        }
    }


    public void init() {
        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetConnection.checkConnection(getApplicationContext())) {
                    // Avaliable
                    Intent i = new Intent(getApplicationContext(), ServiceDirectoryAdd_new.class);
                    Log.e("=========", "---- WEB SERVICE ID ------" + serviceDirId);
                    i.putExtra("serviceDirId", serviceDirId);
                    i.putExtra("categoryId", categoryId);
                    i.putExtra("edit", "yes");
                    /*i.putExtra("name",name);
                    i.putExtra("pax",pax);
                    i.putExtra("mobile",mobile);
                    i.putExtra("mobile2",mobile2);
                    i.putExtra("desc",desc);
                    i.putExtra("mail",mail);
                    i.putExtra("addre",addre);
                    i.putExtra("countryId1", tv_contactNo1.getTag().toString());
                    i.putExtra("countryId2", tv_contactNo2.getTag().toString());
                    i.putExtra("Image",img);

                    i.putExtra("city",city);
                    i.putExtra("state",state);
                    i.putExtra("country",country);
                    i.putExtra("zip",zip);*/

                    //i.putExtra("keywords", tv_k)

                    startActivityForResult(i, 1);
                } else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection !!!");
                    // Not Available...
                }
            }
        });

        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();

                Intent intent = new Intent();
                setResult(1, intent);
                finish();
            }
        });

        iv_addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              /*  add = et_city.getText().toString();
                Intent intent = new Intent(ServiceDirectoryAdd_new.this, EventVenueSearchAddress.class);
                startActivityForResult(intent, LAT_LONG_REQUEST);*/

                //new GetLatLonFromAddress(tv_address.getText().toString()).execute();
                Intent intent = new Intent(ServiceDirectoryDetail.this, ViewAddress.class);

                intent.putExtra(Constant.LATITUDE, latitude);
                intent.putExtra(Constant.LONGITUDE, longitude);
                intent.putExtra("address",addre);
                startActivity(intent);



              /*  Uri uri = Uri.parse("geo: "+latitude+","+longitude);
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);*/


            }
        });

        iv_actionbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ServiceDirectoryDetail.this, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_confrm_delete);
                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
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
                            Intent intent = new Intent();
                            intent.putExtra("deleted","yes");
                            setResult(1, intent);
                            finish();
                        } else {
                            Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");

                        }
                    }
                });

                dialog.show();
            }

        });

        iv_call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + mobile));
                startActivity(intent);
            }
        });

        iv_call_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + mobile2));
                startActivity(intent);
            }
        });

        iv_message_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + mobile));
                intent.putExtra("sms_body", message);
                startActivity(intent);
            }
        });

        iv_message_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + mobile2));
                intent.putExtra("sms_body", message);
                startActivity(intent);
            }
        });

        iv_mail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setType("plain/text");
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mail});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                ServiceDirectoryDetail.this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });
    }

    //-----------------------Offline Database ----------------------

    public void loadFromDB() {

        Log.d("Touchbase", "Trying to load from local db");

        //directoryData.clear();
        // directoryData = directoryDataModel.getGroups(masterUid);


        ServiceDirectoryListData data = serviceDirectoryDataModel.serviceDirectoryDetail(Long.parseLong(serviceDirId));
        if (data != null) {
            showServiceDirectoryData(data);
        } else {
            Toast.makeText(ServiceDirectoryDetail.this, "Sorry. No data found", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void showServiceDirectoryData(ServiceDirectoryListData data) {

        if (data.getDescription().equalsIgnoreCase("")) {
            ll_description.setVisibility(View.GONE);
        } else {
            ll_description.setVisibility(View.VISIBLE);
        }
        if (data.getContactNo().equalsIgnoreCase("")) {
            ll_contactNo1.setVisibility(View.GONE);
        } else {
            ll_contactNo1.setVisibility(View.VISIBLE);
        }
//        if (data.getContactNo2().equalsIgnoreCase("")) {
//            ll_contactNo2.setVisibility(View.GONE);
//        } else {
//            ll_contactNo2.setVisibility(View.VISIBLE);
//        }


        // code written to show/ hide contact no 2 based on data available.
        //if contactno2 has data. it will set layout visibility to visible or else visibility will be gone
        if (data.getContactNo2().equalsIgnoreCase("")) {
            rl_contactNo2.setVisibility(View.GONE);
        } else {
            rl_contactNo2.setVisibility(View.VISIBLE);
        }

        if (data.getPax().equalsIgnoreCase("")) {
            ll_pax.setVisibility(View.GONE);
        } else {
            ll_pax.setVisibility(View.VISIBLE);
        }
        if (data.getEmail().equalsIgnoreCase("")) {
            ll_email.setVisibility(View.GONE);
        } else {
            ll_email.setVisibility(View.VISIBLE);
        }
        if (data.getAddress().equalsIgnoreCase("")) {
            ll_address.setVisibility(View.GONE);
        } else {
            ll_address.setVisibility(View.VISIBLE);
        }



        /*if(data.getImage().equalsIgnoreCase(""))
        {
            Picasso.with(ServiceDirectoryDetail.this).load(R.drawable.profile_pic)
                    .placeholder(R.drawable.profile_pic)
                    .into(iv_pic);
        }*/
        /*if(data.getCity().equalsIgnoreCase("") && data.getCountry().equalsIgnoreCase("")&& data.getState().equalsIgnoreCase("") && data.getZip().equalsIgnoreCase(""))
        {ll_detailAddress.setVisibility(View.GONE);}else {ll_detailAddress.setVisibility(View.VISIBLE);}*/

        if (data.getCsv().equalsIgnoreCase("")) {
            ll_keywords.setVisibility(View.GONE);
        } else {
            ll_keywords.setVisibility(View.VISIBLE);
        }

        tv_name.setText(data.getMemberName());
        tv_desc.setText(data.getDescription());
        Log.d("================", "=======@@@@@@@@@======" + tv_desc.getText().toString());
        tv_contactNo1.setText(data.getContactNo());
        tv_contactNo2.setText(data.getContactNo2());
        tv_pax.setText(data.getPax());
        tv_email.setText(data.getEmail());
        String fullAddress = data.getAddress();


        if (data.getCity() == null || data.getCity().equals("")) {

        } else {
            fullAddress = fullAddress + ", " + data.getCity();
        }

        if (data.getState() == null || data.getState().equals("")) {

        } else {
            fullAddress = fullAddress + ", " + data.getState();
        }

        if (data.getCountry() == null || data.getCountry().equals("")) {

        } else {
            fullAddress = fullAddress + ", " + data.getCountry();
        }

        if (data.getZip() == null || data.getZip().equals("")) {

        } else {
            fullAddress = fullAddress + ", " + data.getZip();
        }

        tv_address.setText(fullAddress);

        /*et_city.setText(data.getCity());
        et_state.setText(data.getState());
        et_country.setText(data.getCountry());
        et_zip.setText(data.getZip());*/
        tv_keywords.setText(data.getCsv());

        if (data.getImage().trim().length() == 0 || data.getImage().equals("") || data.getImage() == null || data.getImage().isEmpty()) {

            Picasso.with(ServiceDirectoryDetail.this).load(R.drawable.profile_pic)
                    .placeholder(R.drawable.profile_pic)
                    .into(iv_pic);

        } else {

            Picasso.with(ServiceDirectoryDetail.this).load(data.getImage()).transform(new CircleTransform())
                    .placeholder(R.drawable.profile_pic)
                    .into(iv_pic);
        }
        img = data.getImage();

        Log.e("======", "===IMAGE======" + img);

        name = tv_name.getText().toString();
        pax = tv_pax.getText().toString();
        mobile = tv_contactNo1.getText().toString();
        Log.d("================", "==@@@@@@@@@===========" + mobile);
        mobile2 = tv_contactNo2.getText().toString();
        Log.d("================", "=====@@@@@@@@@@========" + mobile2);
        desc = tv_desc.getText().toString();
        mail = tv_email.getText().toString();
        addre = tv_address.getText().toString();
        city = et_city.getText().toString();
        Log.d("================", "==@@@@@@@@@===========" + city);

        state = et_state.getText().toString();
        country = et_country.getText().toString();
        zip = et_zip.getText().toString();


        serviceImage = data.getImage();

        try {
            latitude = Double.parseDouble(data.getLat());
            longitude = Double.parseDouble(data.getLng());
        } catch(NumberFormatException nfe) {

        }

        tv_contactNo1.setTag("" + data.getCountryId1());
        tv_contactNo2.setTag("" + data.getCountryId2());

        iv_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!serviceImage.equalsIgnoreCase("")) {
                    Intent i = new Intent(ServiceDirectoryDetail.this, ServiceDirectoryImageActivity.class);
                    i.putExtra("serviceImage", serviceImage);
                    startActivity(i);
                }
            }
        });
    }

    public void finishActivity(View v) {
        finish();
    }

    /*private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("serviceDirId", serviceDirId));
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
       *//* arrayList.add(new BasicNameValuePair("memberProfileID", memberProfileID));
        flag_webservice = "1";*//*
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();

        Log.d("Response", "PARAMETERS " + Constant.GetServiceDirectoryDetails + " :- " + arrayList.toString());
        new WebConnectionAsyncAnnouncement(Constant.GetServiceDirectoryDetails, arrayList, ServiceDirectoryDetail.this).execute();
    }

    public class WebConnectionAsyncAnnouncement extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(ServiceDirectoryDetail.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;

        public WebConnectionAsyncAnnouncement(String url, List<NameValuePair> argList, Context ctx) {
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
            //	Log.d("response","Do post"+ result.toString());

            if (result != "") {
                Log.d("Response", "calling getDirectorydetails");
                getServiceDirectoryDataItems(result.toString());
                *//*if (flag_webservice.equals("1")) {
                    getAnnouncementItems(result.toString());
                } else {
                    getdata(result.toString());
                }*//*

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getServiceDirectoryDataItems(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("TBServiceDirectoryListResult");
            final String status = EventResult.getString("status");
            if (status.equals("0"))
            {
                JSONArray EventListResdult = EventResult.getJSONArray("ServiceDirListResult");
                for (int i = 0; i < EventListResdult.length(); i++) {
                    JSONObject object = EventListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("ServiceDirList");


                    tv_name.setText(objects.getString("serviceMemberName").toString());
                    tv_desc.setText(objects.getString("serviceDescription").toString());
                    tv_contactNo1.setText(objects.getString("serviceMobile1").toString());


                    mobile = tv_contactNo1.getText().toString();
                    tv_contactNo2.setText(objects.getString("serviceMobile2").toString());
                    mobile2 = tv_contactNo2.getText().toString();
                    tv_pax.setText(objects.getString("servicePaxNo").toString());
                    tv_email.setText(objects.getString("serviceEmail").toString());
                    mail = tv_email.getText().toString();
                    tv_address.setText(objects.getString("serviceAddress").toString());
                    Picasso.with(ServiceDirectoryDetail.this).load(objects.getString("serviceThumbimage").toString())
                            .placeholder(R.drawable.imageplaceholder)
                            .into(iv_pic);

                  serviceImage = objects.getString("serviceImage").toString();

                    iv_pic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(ServiceDirectoryDetail.this,ServiceDirectoryImageActivity.class);
                              i.putExtra("serviceImage",serviceImage);
                              startActivity(i);
                        }
                    });


                   *//* if (objects.has("announImg")) {
                        if (objects.getString("announImg").toString().equals("") || objects.getString("announImg").toString() == null) {
                            rl_image.setVisibility(View.GONE);
                        } else {
                            progressbar.setVisibility(View.VISIBLE);
                            Picasso.with(ServiceDirectoryDetail.this).load(objects.getString("announImg").toString())
                                    .placeholder(R.drawable.imageplaceholder)
                                    .into(iv_pic, new Callback() {
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
*//*
                }


            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }
*/


    //=============================== Delete Webservice ============================================

    private void deletewebservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("typeID", serviceDirId));
        arrayList.add(new BasicNameValuePair("type", "ServiceDirectory"));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        arrayList.add(new BasicNameValuePair("profileID", memberProfileID));

        //flag_webservice = "2";
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "PARAMETERS " + Constant.DeleteByModuleName + " :- " + arrayList.toString());
        new DeleteServiceDirectoryAsyncTask(Constant.DeleteByModuleName, arrayList, ServiceDirectoryDetail.this).execute();
    }


    public class DeleteServiceDirectoryAsyncTask extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(ServiceDirectoryDetail.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;

        public DeleteServiceDirectoryAsyncTask(String url, List<NameValuePair> argList, Context ctx) {
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
            // progressDialog.dismiss();
            progressDialog.hide();
            //	Log.d("response","Do post"+ result.toString());

            if (result != "") {
                Log.d("Response", "calling getDirectorydetails");
                getdata(result.toString());


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
                    finish();//finishing activity
                    // finish();
                    Utils.showToastWithTitleAndContext(getApplicationContext(),"Deleted Successfully");
                } else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "Delete failed, Please try again!");
                }

            } catch (Exception e) {
                Log.d("exec", "Exception :- " + e.toString());
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            //tv_done.setVisibility(View.VISIBLE);
            //Bundle intent = data.getExtras();

            loadFromDB();

            /*if(intent.getString("Desc").equalsIgnoreCase(""))
            {ll_description.setVisibility(View.GONE);} else {ll_description.setVisibility(View.VISIBLE);}
            if(intent.getString("Contact1").equalsIgnoreCase(""))
            {ll_contactNo1.setVisibility(View.GONE);}else {ll_contactNo1.setVisibility(View.VISIBLE);}
            if(intent.getString("Contact2").equalsIgnoreCase(""))
            {ll_contactNo2.setVisibility(View.GONE);}else {ll_contactNo2.setVisibility(View.VISIBLE);}
            if(intent.getString("Pax").equalsIgnoreCase(""))
            {ll_pax.setVisibility(View.GONE);}else {ll_pax.setVisibility(View.VISIBLE);}
            if(intent.getString("Email").equalsIgnoreCase(""))
            {ll_email.setVisibility(View.GONE);}else {ll_email.setVisibility(View.VISIBLE);}
            if(intent.getString("Address").equalsIgnoreCase(""))
            {ll_address.setVisibility(View.GONE);}else {ll_address.setVisibility(View.VISIBLE);}

            if(intent.getString("city").equalsIgnoreCase("") && intent.getString("country").equalsIgnoreCase("")&& intent.getString("state").equalsIgnoreCase("") && intent.getString("zip").equalsIgnoreCase(""))
            {ll_detailAddress.setVisibility(View.GONE);}else {ll_detailAddress.setVisibility(View.VISIBLE);}

            if(intent.getString("keywords").equalsIgnoreCase(""))
            {ll_keywords.setVisibility(View.GONE);}else {ll_keywords.setVisibility(View.VISIBLE);}




            tv_name.setText(intent.getString("Title"));
            tv_desc.setText(intent.getString("Desc"));
            //tv_contactNo1.setText((intent.getString("Contact1"))+intent.getString("CountryCode1"));
           // tv_contactNo2.setText((intent.getString("Contact2"))+intent.getString("CountryCode2"));
            tv_contactNo1.setText(intent.getString("Contact1"));
            tv_contactNo2.setText(intent.getString("Contact2"));

            tv_pax.setText(intent.getString("Pax"));
            tv_email.setText(intent.getString("Email"));
            tv_address.setText(intent.getString("Address"));
            et_city.setText(intent.getString("city"));
            et_country.setText(intent.getString("country"));
            et_state.setText(intent.getString("state"));
            et_zip.setText(intent.getString("zip"));
            tv_keywords.setText(intent.getString("keywords"));


            if(tv_name.getText().toString().equals(""))
            {ll_description.setVisibility(View.GONE);}
            else
                tv_name.setText(intent.getString("Title"));


            if(tv_contactNo1.getText().toString().equals(""))
            {ll_contactNo1.setVisibility(View.GONE);}
            else
                tv_contactNo1.setText(intent.getString("Contact1"));


            if(tv_contactNo2.getText().toString().equals(""))
            {ll_contactNo2.setVisibility(View.GONE);}
            tv_contactNo2.setText(intent.getString("Contact2"));

            if(tv_pax.getText().toString().equals(""))
            {ll_pax.setVisibility(View.GONE);}
            else
                tv_pax.setText(intent.getString("Pax"));

            if(tv_email.getText().toString().equalsIgnoreCase(""))
            {ll_email.setVisibility(View.GONE);}
            else
                tv_email.setText(intent.getString("Email"));


            if(tv_address.getText().toString().equals(""))
            {ll_address.setVisibility(View.GONE);}
            else
                tv_address.setText(intent.getString("Address"));*/

           /* if(tv_keywords.getText().toString().equalsIgnoreCase(""))
            {ll_keywords.setVisibility(View.GONE);}*/

            // tv_countryCode1.setTag(intent.getString("CountryId1"));
            //tv_countryCode2.setTag(intent.getString("CountryId2"));

        }


    }
    /*public void loadFromServer() {
            webservices();
        }*/

        /*Handler serviceDirectoryDataHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.e("DBError", "Handler is called");
                boolean saved = serviceDirectoryDataModel.insert(masterUid, serviceDirectoryListDatas);
                if (!saved) {
                    Log.d("Touchbase", "Failed to save offlline. Retrying in 2 seconds");
                    sendEmptyMessageDelayed(0, 2000);
                } else {

                    Log.d("Touchbase", "SAVED offlline.........................");
                    PreferenceManager pm = new PreferenceManager();
                     Log.d("-----------", "----Directory data Offline----");
                    //syncModel.update(masterUid, Utils.)
                }
            }
        };
    */


    //=============================== get GetLatLonFromAddress =======================
    class GetLatLonFromAddress extends AsyncTask<Void, Void, String> {
        String adddress;
        ProgressDialog pdialog;

        public GetLatLonFromAddress(String address) {
            try {
                this.adddress = URLEncoder.encode(address, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            pdialog = new ProgressDialog(ServiceDirectoryDetail.this);
            pdialog.setMessage("Loading...");
            pdialog.setCancelable(false);
            pdialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
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
        protected void onPostExecute(String result) {
            if (pdialog != null) {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();


                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject = new JSONObject(result.toString());

                        longitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                                .getJSONObject("geometry").getJSONObject("location")
                                .getDouble("lng");

                        latitude = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                                .getJSONObject("geometry").getJSONObject("location")
                                .getDouble("lat");

                        Log.d("latitude", "" + latitude);
                        Log.d("longitude", "" + longitude);


                        Intent intent = new Intent(ServiceDirectoryDetail.this, ViewAddress.class);
                        Bundle bundle = new Bundle();


                        bundle.putDouble(Constant.LATITUDE, latitude);
                        bundle.putDouble(Constant.LONGITUDE, longitude);
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


    private void copyTextToClipBoard(String chatMessage) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(chatMessage);
        } else {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", chatMessage);
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_LONG).show();
    }


}
