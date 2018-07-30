package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.SampleApp.row.Utils.PreferenceManager.GROUP_NAME;
import static com.SampleApp.row.Utils.PreferenceManager.savePreference;

/**
 * Created by user on 23-02-2016.
 */
public class GroupInfo extends Activity {

    TextView tv_title, tv_group_name, tv_group_city, tv_group_admin_name, tv_address, tv_email, tv_mobile, tv_created_date;
    ImageView iv_backbutton;
    ImageView iv_announcementimg;
    ProgressBar progressbar;
    private ImageView iv_actionbtn;
    private String isAdmin = "0";
    private String grpID="0";
    String moduleName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.group_info);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        // iv_backbutton.setVisibility(View.GONE);
        moduleName = PreferenceManager.getPreference(this, PreferenceManager.MODUEL_NAME, "Info");
        tv_title.setText(moduleName);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn.setVisibility(View.VISIBLE); // EDIT ANNOUNCEMEBT
        iv_actionbtn.setImageResource(R.drawable.edit); // EDIT ANNOUNCEMEBT

        tv_group_name = (TextView) findViewById(R.id.tv_group_name);
        tv_group_city = (TextView) findViewById(R.id.tv_group_city);
        tv_group_admin_name = (TextView) findViewById(R.id.tv_group_admin_name);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_mobile = (TextView) findViewById(R.id.tv_mobile);
        tv_created_date = (TextView) findViewById(R.id.tv_created_date);
        iv_announcementimg = (ImageView) findViewById(R.id.iv_announcementimg);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        isAdmin = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN);
        grpID=PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID);


        Intent intenti = getIntent();
        if (intenti.hasExtra("memID")) {

            grpID = intenti.getStringExtra("grpID");
            isAdmin = intenti.getStringExtra("isAdmin");
        }

        webservices();
        init();
        adminsettings();
    }

    private void adminsettings() {
        if (isAdmin.equals("No")) {
            iv_actionbtn.setVisibility(View.GONE);

        } else {
            iv_actionbtn.setVisibility(View.VISIBLE);
        }
    }

    private void init() {

        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateGroup.class);
                i.putExtra("groupid",grpID);
                startActivityForResult(i,1);
            }
        });

        tv_group_admin_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_group_admin_name.getTag().toString().length() != 0) {

                    Intent i = new Intent(getApplicationContext(), Profile.class);
                    i.putExtra("memberprofileid", tv_group_admin_name.getTag().toString());
                    i.putExtra("groupId", grpID);

                    startActivity(i);
                }
            }
        });
        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + tv_address.getText().toString()));
                startActivity(intent);
            }
        });
        tv_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", tv_mobile.getText().toString(), null));
                startActivity(intent);
            }
        });
        tv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setType("plain/text");
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{tv_email.getText().toString()});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                GroupInfo.this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });
    }

    public void finishActivity(View v) {
        finish();
    }

    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("memberMainId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));
        arrayList.add(new BasicNameValuePair("groupId", grpID));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        //arrayList.add(new BasicNameValuePair("memberProfileID", "1"));

        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();

        Log.d("Response", "PARAMETERS " + Constant.GetGroupDetail + " :- " + arrayList.toString());
        new WebConnectionAsyncAnnouncement(Constant.GetGroupDetail, arrayList, GroupInfo.this).execute();
    }

    public class WebConnectionAsyncAnnouncement extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(GroupInfo.this, R.style.TBProgressBar);
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
                Log.d("Response", "calling getGroupInfoItem");
                getGroupInfoItem(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }
        }
    }

    private void getGroupInfoItem(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject groupResult = jsonObj.getJSONObject("TBGetGroupResult");
            final String status = groupResult.getString("status");
            if (status.equals("0")) ;
            {
                JSONArray groupDetailResdult = groupResult.getJSONArray("getGroupDetailResult");
                for (int i = 0; i < groupDetailResdult.length(); i++) {
                    JSONObject object = groupDetailResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("GetGroupInfo");

                    tv_group_name.setText(objects.getString("grpNAme").toString());
                    tv_group_city.setText(objects.getString("cityCountry").toString());
                    tv_group_admin_name.setText(objects.getString("grpAdmin").toString());
                    tv_address.setText(objects.getString("grpAddress").toString());
                    tv_email.setText(objects.getString("grpEmail").toString());
                    tv_mobile.setText(objects.getString("grpMobile").toString());
                    tv_created_date.setText(objects.getString("createdDateTime").toString());
                    if (objects.has("grpAdminProfileId")) {
                        tv_group_admin_name.setTag(objects.getString("grpAdminProfileId").toString());
                    }

                    savePreference(GroupInfo.this, GROUP_NAME, objects.getString("grpNAme").toString()); //Addded

                    if (objects.has("grpImg")) {
                        if (objects.getString("grpImg").toString().equals("") || objects.getString("grpImg").toString() == null) {
                            //  linear_image.setVisibility(View.GONE);
                        } else {
                            progressbar.setVisibility(View.VISIBLE);
                            Picasso.with(GroupInfo.this).load(objects.getString("grpImg").toString())
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 1) {
            webservices();

        }
    }
}
