package com.SampleApp.row;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.MonthlyReportAdapter;
import com.SampleApp.row.Data.MonthlyReportData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MonthlyReportActivity extends AppCompatActivity {

    RecyclerView rvClubs;
    private MonthlyReportAdapter monthlyReportAdapter;
    private ArrayList<MonthlyReportData> monthlyReportDataArrayList = new ArrayList<>();
    private HashMap<String,String> monthNames = new HashMap<>();
    TextView tv_title,tv_no_records_found;
    ImageView iv_backbutton, iv_actionbtn;
    EditText et_search;
    String moduleName = "";
    Spinner spinner_filter_type;
    String type_filter_flag = "1";
    String filtertype[] = {"Submitted", "Not Submitted"};
    private String grpID = "0";
    private LinearLayoutManager layoutManager;
    private String memberProfileID = "0";
    private String moduleId;
    private Context ctx;
    private String month="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_monthly_report);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_no_records_found = (TextView) findViewById(R.id.tv_no_records_found);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        et_search = (EditText) findViewById(R.id.et_search_club);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        rvClubs = (RecyclerView) findViewById(R.id.rvClubs);

        String title = getIntent().getExtras().getString("month name", "");

        tv_title.setText(title);

        getMonthValue(title);

        ctx = MonthlyReportActivity.this;

        layoutManager = new LinearLayoutManager(ctx);

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvClubs.setLayoutManager(layoutManager);

        spinner_filter_type = (Spinner) findViewById(R.id.spinner_filter_type);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, filtertype);
        spinner_filter_type.setAdapter(spinnerArrayAdapter);

        //-------------------------------
        grpID = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID);
        memberProfileID = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID);
        moduleId = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MODULE_ID);

        init();

        Log.d("Touchbase", "ID ID ID :- " + grpID + " - " + memberProfileID);

    }

    private void getMonthValue(String value){

        monthNames.put("January","01");
        monthNames.put("February","02");
        monthNames.put("March","03");
        monthNames.put("April","04");
        monthNames.put("May","05");
        monthNames.put("June","06");
        monthNames.put("July","07");
        monthNames.put("August","08");
        monthNames.put("September","09");
        monthNames.put("October","10");
        monthNames.put("November","11");
        monthNames.put("December","12");

        String[] splitStr = value.split("\\s+");
        String mm = splitStr[0];
        String yy = splitStr[1];

        month = monthNames.get(mm)+"-"+yy;

    }

    private void init(){

        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                String text = et_search.getText().toString().toLowerCase();

                if(monthlyReportAdapter!=null) {
                    monthlyReportAdapter.filter(text);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }

        });

        spinner_filter_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.d("TouchBase", "@@@@@@@@@@@@@ " + spinner_filter_type.getSelectedItem().toString());

               if (spinner_filter_type.getSelectedItem().toString().equals("Submitted")) {
                    type_filter_flag = "1";
                } else  {
                    type_filter_flag = "2";
                }

                if (InternetConnection.checkConnection(getApplicationContext())) {
                    //Available
                    webservices();
                } else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            monthlyReportAdapter.unregisterFileDownloadReceiver();
        } catch(NullPointerException npe) {
          npe.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void webservices() {

        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        arrayList.add(new BasicNameValuePair("profileId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("month",month));
        arrayList.add(new BasicNameValuePair("Type", type_filter_flag));

        Log.d("Response", "PARAMETERS " + Constant.GetMonthlyReportList + " :- " + arrayList.toString());

        if ( InternetConnection.checkConnection(getApplicationContext())) {
            new WebConnectionAsyncDirectory(Constant.GetMonthlyReportList, arrayList, ctx).execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public class WebConnectionAsyncDirectory extends AsyncTask<String, Object, Object> {

        final ProgressDialog progressDialog = new ProgressDialog(ctx, R.style.TBProgressBar);
        String val = null;
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

                Log.d("Response", "we satish=> " + val);

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
                getClubItems(result.toString());
            } else {
                Log.d("Response", "Null Resposnse");
            }

        }
    }

    private void getClubItems(String result) {

        try {

            JSONObject jsonObj = new JSONObject(result);

            JSONObject jsonObject = jsonObj.getJSONObject("TBClubMonthlyReportListResult");

            final String status = jsonObject.getString("status");

            monthlyReportDataArrayList.clear();

            if (status.equals("0")) {

                JSONArray clubMonthlyReportListArray = jsonObject.getJSONArray("Result");//jsonObject.getJSONArray("ClubMonthlyReportListResult");

                if(clubMonthlyReportListArray.length()==0){

                    tv_no_records_found.setVisibility(View.VISIBLE);

                } else {

                    tv_no_records_found.setVisibility(View.GONE);

                    for (int i = 0; i < clubMonthlyReportListArray.length(); i++) {

                        JSONObject attendanceResult = clubMonthlyReportListArray.getJSONObject(i);

                      //  JSONObject attendanceResult = object.getJSONObject("AttendanceResult");

                        MonthlyReportData data = new MonthlyReportData();

                        String clubId = attendanceResult.getString("ClubId1");

                        data.setId(clubId);

                        data.setName(attendanceResult.getString("clubName")+" (Club "+clubId+")");

                        if(attendanceResult.has("SendToDistrictDate")){
                            data.setDate(attendanceResult.getString("SendToDistrictDate"));
                        }

                        if(attendanceResult.has("SendToDistrictTime")) {
                            data.setTime(attendanceResult.getString("SendToDistrictTime"));
                        }

                        if(attendanceResult.has("clubAG")){
                            data.setClubAG(attendanceResult.getString("clubAG"));
                        }

                        if(attendanceResult.has("reportUrl")){
                            data.setReportURL(attendanceResult.getString("reportUrl"));
                        }

                        monthlyReportDataArrayList.add(data);

                    }

                    monthlyReportAdapter = new MonthlyReportAdapter(ctx, monthlyReportDataArrayList,type_filter_flag,this);
                    rvClubs.setAdapter(monthlyReportAdapter);
                }

                } else {

               /*eventListDatas.clear();
                eventListAdapter = new EventListAdapter(Events.this, R.layout.events_list_item, eventListDatas);
                listview.setAdapter(eventListAdapter);*/

                tv_no_records_found.setVisibility(View.VISIBLE);

                monthlyReportAdapter = new MonthlyReportAdapter(ctx,monthlyReportDataArrayList,type_filter_flag,this);
                rvClubs.setAdapter(monthlyReportAdapter);
             }

            // listview.setEmptyView(((TextView) findViewById(R.id.tv_no_records_found)));

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
           /* eventListDatas.clear();
            eventListAdapter = new EventListAdapter(Events.this, R.layout.events_list_item, eventListDatas);
            listview.setAdapter(eventListAdapter);*/
        }
    }

    public void noRecordFound(int isVisible){
        tv_no_records_found.setVisibility(isVisible);
    }
}
