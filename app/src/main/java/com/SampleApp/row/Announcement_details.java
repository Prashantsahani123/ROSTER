package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 21-12-2015.
 */
public class Announcement_details extends Activity {

    TextView tv_title;
    ImageView iv_backbutton, iv_announcementimg, iv_actionbtn, iv_actionbtn2;
    EditText et_serach_announcement;
    TextView announce_title, tv_announDAte, tv_announTime, announce_desc,txt_link;
    String announcment_id;
    private ProgressBar progressbar;
    LinearLayout linear_image,ll_link;
    private String grpID = "0";
    private String memberProfileID = "0";
    private String flag_webservice = "1";
    private String isAdmin = "No";
    String moduleName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement_details);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn2 = (ImageView) findViewById(R.id.iv_actionbtn2);

        iv_actionbtn.setVisibility(View.VISIBLE); // EDIT ANNOUNCEMEBT
        iv_actionbtn.setImageResource(R.drawable.edit); // EDIT ANNOUNCEMEBT

        iv_actionbtn.setVisibility(View.VISIBLE); // Delete ANNOUNCEMEBT
        iv_actionbtn2.setImageResource(R.drawable.delete); // Delete ANNOUNCEMEBT
        //moduleName = PreferenceManager.getPreference(this, PreferenceManager.MODUEL_NAME, "Announcement");
        moduleName = getIntent().getExtras().getString("moduleName", "Announcement");

//        if (getIntent().getExtras().containsKey("moduleName")) {
//            moduleName = getIntent().getExtras().getString("moduleName");
//        }

        tv_title.setText(moduleName);

        announce_title = (TextView) findViewById(R.id.announce_title);
        txt_link = (TextView) findViewById(R.id.txt_reglink);
        ll_link=findViewById(R.id.ll_link);
        tv_announDAte = (TextView) findViewById(R.id.tv_announDAte);
        tv_announTime = (TextView) findViewById(R.id.tv_announTime);
        announce_desc = (TextView) findViewById(R.id.announce_desc);
        iv_announcementimg = (ImageView) findViewById(R.id.iv_announcementimg);
        et_serach_announcement = (EditText) findViewById(R.id.et_serach_announcement);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        linear_image = (LinearLayout) findViewById(R.id.linear_image);

        grpID = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID);
        memberProfileID = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID);
        isAdmin = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN);
        Log.d("Touchbase", "ID ID ID :- " + grpID + " - " + memberProfileID);

        Intent intent = getIntent();
        announcment_id = intent.getStringExtra("announcemet_id");
        if (intent.hasExtra("memID")) {
            memberProfileID = intent.getStringExtra("memID");
            grpID = intent.getStringExtra("grpID");
            isAdmin = intent.getStringExtra("isAdmin");
        }
        Log.d("Touchbase", "ID ID ID AFTER :- " + grpID + " - " + memberProfileID);

        //webservices();
        if (InternetConnection.checkConnection(getApplicationContext())) {
            // Avaliable
            webservices();
        } else {
            Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
            // Not Available...
        }
        init();
        adminsettings();
    }

    private void adminsettings() {
        if (isAdmin.equals("No")) {
            iv_actionbtn.setVisibility(View.GONE);
            iv_actionbtn2.setVisibility(View.GONE);
        } else {
            iv_actionbtn.setVisibility(View.VISIBLE);
            iv_actionbtn2.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        iv_actionbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Announcement_details.this);
                View view = getLayoutInflater().inflate(R.layout.popup_confrm_delete, null);
                builder.setView(view);

                TextView tvYes = (TextView) view.findViewById(R.id.tv_yes);
                TextView tvNo = (TextView) view.findViewById(R.id.tv_no);

                final AlertDialog dialog = builder.create();

                tvYes.setOnClickListener(new View.OnClickListener() {
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

                tvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

//                final Dialog dialog = new Dialog(Announcement_details.this, android.R.style.Theme_Translucent);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.popup_confrm_delete);
//                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
//                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
//                tv_no.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//
//                    }
//                });
//                tv_yes.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//
//                        if (InternetConnection.checkConnection(getApplicationContext())) {
//                            //Utils.showToastWithTitleAndContext(getApplicationContext(), "Delete");
//                            deletewebservices();
//                        } else {
//                            Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
//
//                        }
//                    }
//                });
//
//                dialog.show();
            }

        });

        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddAnnouncement.class);
                i.putExtra("announcemet_id", announcment_id);
                startActivityForResult(i, 1);
            }
        });

        ll_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link=txt_link.getText().toString();

                if(link!=null && !link.trim().isEmpty()){
                    Intent intent=new Intent(Announcement_details.this,OpenLinkActivity.class);
                    intent.putExtra("link",link);
                    startActivity(intent);
                }
            }
        });

    }

    public void finishActivity(View v) {
        finish();
    }


    private void deletewebservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("typeID", announcment_id));
        arrayList.add(new BasicNameValuePair("type", "Announcement "));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        arrayList.add(new BasicNameValuePair("profileID", memberProfileID));

        flag_webservice = "2";
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "PARAMETERS " + Constant.DeleteByModuleName + " :- " + arrayList.toString());
        new WebConnectionAsyncAnnouncement(Constant.DeleteByModuleName, arrayList, Announcement_details.this).execute();
    }


    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("announID", announcment_id));
        arrayList.add(new BasicNameValuePair("grpID", grpID));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        arrayList.add(new BasicNameValuePair("memberProfileID", memberProfileID));
        flag_webservice = "1";
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();

        Log.d("Response", "PARAMETERS " + Constant.GetAnnouncementDetails + " :- " + arrayList.toString());
        new WebConnectionAsyncAnnouncement(Constant.GetAnnouncementDetails, arrayList, Announcement_details.this).execute();
    }

    public class WebConnectionAsyncAnnouncement extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(Announcement_details.this, R.style.TBProgressBar);
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
                if (flag_webservice.equals("1")) {
                    getAnnouncementItems(result.toString());
                } else {
                    getdata(result.toString());
                }

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getAnnouncementItems(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("TBAnnounceListResult");
            final String status = EventResult.getString("status");
            if (status.equals("0")) ;
            {
                JSONArray EventListResdult = EventResult.getJSONArray("AnnounListResult");
                for (int i = 0; i < EventListResdult.length(); i++) {
                    JSONObject object = EventListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("AnnounceList");


                    announce_title.setText(objects.getString("announTitle").toString());
                    announce_desc.setText(objects.getString("announceDEsc").toString());
                    tv_announDAte.setText(objects.getString("publishDateTime").toString());

                    String link=objects.getString("reglink");
                    if(link!=null && !link.isEmpty()){
                        ll_link.setVisibility(View.VISIBLE);
                        txt_link.setText(link);
                    }else {
                        txt_link.setText("");
                        ll_link.setVisibility(View.GONE);
                    }


                    if(objects.getString("filterType").toString().equals("3")){
                        iv_actionbtn.setVisibility(View.GONE);
                    }


                    if (objects.has("announImg")) {
                        if (objects.getString("announImg").toString().equals("") || objects.getString("announImg").toString() == null) {
                            linear_image.setVisibility(View.GONE);
                        } else {
                            linear_image.setVisibility(View.VISIBLE);
                            progressbar.setVisibility(View.VISIBLE);
                            Picasso.with(Announcement_details.this).load(objects.getString("announImg").toString())
                                    .placeholder(R.drawable.imageplaceholder)
                                    .into(iv_announcementimg, new Callback() {
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

                }


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
                finish();//finishing activity
                // finish();
                Utils.showToastWithTitleAndContext(getApplicationContext(), "Deleted Successfully");
            } else {
                Utils.showToastWithTitleAndContext(getApplicationContext(), "Delete failed, Please try again!");
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 1) {
            //webservices();
            if (InternetConnection.checkConnection(getApplicationContext())) {
                // Avaliable
                webservices();
            } else {
                Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                // Not Available...
            }
            //String message=data.getStringExtra("MESSAGE");
            // textView1.setText(message);
        }
    }

}
