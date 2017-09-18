package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.SampleApp.row.Adapter.Settings_Group_Adapter;
import com.SampleApp.row.Data.SettingsData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;

/**
 * Created by user on 14-03-2016.
 */
public class Settings extends Activity {

    ListView lv;
    Context context;
    TextView tv_title;
    ImageView iv_backbutton;
    ArrayList prgmName;
    Settings_Group_Adapter dataAdapter = null;
    private ArrayList<SettingsData> settinglist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applevel_settings);

        lv = (ListView) findViewById(R.id.listView);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);

        tv_title.setText("Settings");

        //displayListView();
        if (InternetConnection.checkConnection(getApplicationContext())) {
            // Avaliable
            webservices();
        } else {
            Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
            // Not Available...
        }

    }

    private void webservices() {
        //{"groupProfileID":"43","grpId":"74","Type":"0","Admin":"0","searchText":""}
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("mainMasterId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));

        Log.d("Response", "PARAMETERS " + Constant.GetTouchbaseSetting + " :- " + arrayList.toString());
        new WebConnectionAsyncDirectory(Constant.GetTouchbaseSetting, arrayList, Settings.this).execute();
    }

    public class WebConnectionAsyncDirectory extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(Settings.this, R.style.TBProgressBar);
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
                getsetting(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getsetting(String result) {
        try {
            settinglist.clear();
            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("TBSettingResult");
            final String status = EventResult.getString("status");
            if (status.equals("0")) {
                JSONArray EventListResdult = EventResult.getJSONArray("AllTBSettingResults");
                for (int i = 0; i < EventListResdult.length(); i++) {
                    JSONObject object = EventListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("SettingResult");
                    SettingsData data = new SettingsData();
                    data.setGrpId(objects.getString("grpId").toString());
                    data.setGrpVal(objects.getString("grpVal").toString());
                    data.setGrpName(objects.getString("grpName").toString());
                    settinglist.add(data);
                }

                Log.d("TOUCHBASE", "SETTING :- " + settinglist.toString());

                dataAdapter = new Settings_Group_Adapter(Settings.this, R.layout.settings_list_item, settinglist,"1","","","","");
                lv.setAdapter(dataAdapter);

            } else {

                lv.setEmptyView(((TextView) findViewById(R.id.tv_no_records_found)));
            }
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());

        }
    }

}