package com.NEWROW.row;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.Settings_Group_Adapter;
import com.NEWROW.row.Data.SettingsData;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DistrictSettingActivity extends AppCompatActivity {

    ListView lv;
    TextView tv_title;
    ImageView iv_backbutton;
    private String flag_webservicecall = "0";
    private ArrayList<SettingsData> settinglist = new ArrayList<>();
    Settings_Group_Adapter dataAdapter = null;
    ImageView iv_toggle_isphone_myclub, iv_toggle_isphone_allclub, iv_email_myclub,iv_toggle_isemail_allclub;
    String isphone_myclub, isphone_allclub, isemail_myclub,isemail_allclub,title,grpId,grpProfileId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        title = intent.getStringExtra("grpName");
        grpId = intent.getStringExtra("grpId");
        grpProfileId = intent.getStringExtra("grpProfileId");

        setContentView(R.layout.activity_district_setting);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText(title);

        init();
        webservices();
    }

    private void init() {

        lv = (ListView) findViewById(R.id.listView);

    }
    private void webservices() {
        //{"groupProfileID":"43","grpId":"74","Type":"0","Admin":"0","searchText":""}
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("GroupId", grpId));
        arrayList.add(new BasicNameValuePair("GroupProfileId", grpProfileId));
        flag_webservicecall = "0";
        Log.d("Response", "PARAMETERS " + Constant.GetGroupSetting + " :- " + arrayList.toString());
        new WebConnectionAsyncDirectory(Constant.GetGroupSetting, arrayList, DistrictSettingActivity.this).execute();
    }

    public class WebConnectionAsyncDirectory extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(DistrictSettingActivity.this, R.style.TBProgressBar);
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
                // eventListDatas.clear();
                if(flag_webservicecall.equals("0")) {
                    getsetting(result.toString());
                }else{
                    /*{"TBGroupSettingResult":{"status":"0","message":"success"}}*/

                    try {
                        JSONObject jsonObject = new JSONObject(result.toString());
                        if ( jsonObject.getJSONObject("TBGroupSettingResult").get("status").equals("0")) {
                            Toast.makeText(context, "Settings updated successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Failed to update settings", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Failed to update settings", Toast.LENGTH_LONG).show();
                    }
                    webservices();
                }
            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getsetting(String result) {
        try {
            settinglist.clear();
            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("TBGroupSettingResult");
            final String status = EventResult.getString("status");
            if (status.equals("0")) {

                //Setdata_topersonaldetails(EventResult.getString("isMobileSelf"), EventResult.getString("isMobileOther"), EventResult.getString("isEmailSelf"),EventResult.getString("isEmailOther"));
                JSONArray EventListResdult = EventResult.getJSONArray("GRpSettingResult");
                for (int i = 0; i < EventListResdult.length(); i++) {
                    JSONObject object = EventListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("GRpSettingDetails");

                    SettingsData data = new SettingsData();
                    data.setGrpId(objects.getString("moduleId").toString());
                    data.setGrpVal(objects.getString("modVal").toString());
                    data.setGrpName(objects.getString("modName").toString());
                    settinglist.add(data);

                }

                Log.d("TOUCHBASE", "SETTING :- " + settinglist.toString());

                dataAdapter = new Settings_Group_Adapter(DistrictSettingActivity.this, R.layout.settings_list_item, settinglist,"2",isphone_myclub,isphone_allclub,isemail_myclub,isemail_allclub);
                lv.setAdapter(dataAdapter);

            } else {

                lv.setEmptyView(((TextView) findViewById(R.id.tv_no_records_found)));
            }
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());

        }
    }
    private void Setdata_topersonaldetails(String phone_myclub,String phone_allclub, String email_myclub, String email_allclub) {

        isphone_myclub = phone_myclub;
        isphone_allclub = phone_allclub;
        isemail_myclub = email_myclub;
        isemail_allclub = email_allclub;

    }
}
