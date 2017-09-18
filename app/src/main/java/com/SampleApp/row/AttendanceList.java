package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.SampleApp.row.Adapter.AttendanceListAdapter;
import com.SampleApp.row.Data.AttendanceData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.sql.AttendanceMasterModel;

/**
 * Created by USER on 06-10-2016.
 */
public class AttendanceList extends Activity {
    ArrayList<String> years = new ArrayList<String>();
    int thisYear;
    ListView attendance_list;
    Spinner spinMonth, spYear;
    String year, month;
    TextView tv_title;
    ImageView iv_backbutton;
    private long masterUid, memberProfileID;
    int count = 0;
    String moduleId;
    TextView tvNA;
    String moduleName = "";
    // private String memberProfileID="0";
    private AttendanceListAdapter adapterAttendance;
    AttendanceMasterModel attendanceMasterModel;
    String selectedMonth,selectedYear;

    private ArrayList<AttendanceData> attendanceDataArrayList = new ArrayList<AttendanceData>();
    Calendar c;

    String flag = "0"; // arraylist is null & flag = "1" data is available in arraalist


    static final String[] Months = new String[]{"January", "February",
            "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.attendance_list);

        attendance_list = (ListView) findViewById(R.id.attendance_list);
        tvNA = (TextView)findViewById(R.id.tv_NA);


        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        moduleName = PreferenceManager.getPreference(this, PreferenceManager.MODUEL_NAME, "Attendance");
        tv_title.setText(moduleName);

        masterUid = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)); // line added by lekha
        memberProfileID = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID));

        spinMonth = (Spinner) findViewById(R.id.sp_month);
        spYear = (Spinner) findViewById(R.id.sp_year);

        c = Calendar.getInstance();
        month = String.valueOf((c.get(Calendar.MONTH)-1));


        final String[] Months = new String[]{"January", "February",
                "March", "April", "May", "June", "July", "August", "September",
                "October", "November", "December"};


        thisYear = Calendar.getInstance().get(Calendar.YEAR);
        years.add(""+(thisYear-1));
        years.add(""+thisYear);

        Log.d("----","--THIS YEAR--"+thisYear);
        /*for (int i = 1900; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }*/
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.year_spinner_item, years);
        YearAdapter adapter = new YearAdapter(this, years);

        spYear.setAdapter(adapter);


        spYear.setSelection(1);  // By default selecting current year which at index 1.

        ArrayAdapter<String> adapterMonths = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Months);
        adapterMonths.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //spinMonth = (Spinner) findViewById(R.id.sp_month);
        spinMonth.setAdapter(adapterMonths);

        if ( Integer.parseInt(month) == Calendar.JANUARY ) {
            month = ""+Calendar.DECEMBER;
            thisYear = thisYear - 1;
            spYear.setSelection(0);
        }

        spinMonth.setSelection(Integer.parseInt(month));
        selectedMonth = spinMonth.getSelectedItem().toString();

        selectedYear = ""+thisYear;

        attendanceMasterModel = new AttendanceMasterModel(getApplicationContext());
        attendanceMasterModel.printTable();

        Log.e("-ON ONCREATE METHOD-","--"+attendanceMasterModel.getAttendanceList(masterUid, memberProfileID,spinMonth.getSelectedItem().toString(),"2015"));

       // loadFromDB();
        // webservices();


        spinMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if(count>0) {
                   // Log.d("----","--SPINNER VALUE--"+spMonth.getSelectedItem().toString());
                    loadFromDB();

                } else {
                    count++;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if(count>0) {
                    loadFromDB();
                    /*if (InternetConnection.checkConnection(getApplicationContext())) {
                        webservices();
                    } else {
                        Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                    }*/
                } else {
                    count++;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        Intent i = getIntent();
        moduleId = i.getStringExtra("moduleId");
        Log.d("----","--MODULE ID---"+moduleId);

    }


    //=============== Websercice ==================================

    private void webservices() {
        //{"masterUID":"1","grpID":"","searchText":"","page":""}

        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("groupProfileID",""+memberProfileID));
        arrayList.add(new BasicNameValuePair("month", spinMonth.getSelectedItem().toString()));
        arrayList.add(new BasicNameValuePair("year", spYear.getSelectedItem().toString()));
        arrayList.add(new BasicNameValuePair("moduleID", moduleId));


        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();

        Log.d("Response", "PARAMETERS " + Constant.GetAttendanceList + " :- " + arrayList.toString());
        new WebConnectionAsyncAttendance(Constant.GetAttendanceList, arrayList, AttendanceList.this).execute();
    }

    public class WebConnectionAsyncAttendance extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(AttendanceList.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;


        public WebConnectionAsyncAttendance(String url, List<NameValuePair> argList, Context ctx) {
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

                getAttendanceListItems(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getAttendanceListItems(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject AttendanceResult = jsonObj.getJSONObject("TBAttendanceListResult");
            final String status = AttendanceResult.getString("status");

            if (status.equals("0")) {
                JSONArray AttendanceListResult = AttendanceResult.getJSONArray("AttendanceListResult");

                Log.d("@@@@@@@@@", "@@@@@@@@@@" + AttendanceListResult.length());
                attendanceDataArrayList.clear();
                if ( adapterAttendance!= null ) adapterAttendance.notifyDataSetChanged();
                attendanceDataArrayList = new ArrayList<AttendanceData>();
               // attendanceDataArrayList.clear();

                for (int i = 0; i < AttendanceListResult.length(); i++) {
                    JSONObject object = AttendanceListResult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("AttendanceResult");

                    AttendanceData data = new AttendanceData();

                    data.setId(objects.getString("idno").toString());
                    data.setName(objects.getString("name").toString());
                    data.setAttendance(objects.getString("attendence").toString());
                    data.setMonth(spinMonth.getSelectedItem().toString());
                    data.setYear(spYear.getSelectedItem().toString());
                    data.setMemberId(String.valueOf(memberProfileID));
                    data.setModuleId(moduleId);

                    attendanceDataArrayList.add(data);


                }

                if(attendanceDataArrayList != null && !attendanceDataArrayList.isEmpty()) {
                    adapterAttendance = new AttendanceListAdapter(AttendanceList.this, R.layout.attendance_list_item, attendanceDataArrayList);
                    attendance_list.setEmptyView(getLayoutInflater().inflate(R.layout.attendance_empty_list_item,null));
                    attendance_list.setAdapter(adapterAttendance);
                    attendanceDataHandler.sendEmptyMessageDelayed(0, 1000);
                    //Toast.makeText(AttendanceList.this, "Data available", Toast.LENGTH_LONG).show();
                    /*attendance_list.setVisibility(View.VISIBLE);
                      tvNA.setVisibility(View.GONE);*/
                }
                else
                {
                  /*  adapterAttendance = new AttendanceListAdapter(AttendanceList.this, R.layout.attendance_list_item, attendanceDataArrayList,"0");
                    attendance_list.setAdapter(adapterAttendance);
                    attendanceDataHandler.sendEmptyMessageDelayed(0, 1000);*/
                    attendanceDataArrayList = new ArrayList<>();
                    attendanceDataArrayList.clear();
                    AttendanceData data = new AttendanceData("","","","","","","");
                    attendanceDataArrayList.add(data);

                    adapterAttendance = new AttendanceListAdapter(AttendanceList.this, R.layout.attendance_list_item, attendanceDataArrayList);
                    adapterAttendance.notifyDataSetChanged();
                    attendance_list.setAdapter(adapterAttendance);

                    attendance_list.setEmptyView(getLayoutInflater().inflate(R.layout.attendance_empty_list_item,null));
                    //Toast.makeText(AttendanceList.this, "No data", Toast.LENGTH_LONG).show();

                 /*   attendance_list.setVisibility(View.GONE);
                    tvNA.setVisibility(View.VISIBLE);*/
                }
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
            e.printStackTrace();
        }
    }

    //=================== Local Database ===========================


    public void loadFromDB() {

        Log.d("Touchbase", "Trying to load from local db");


        attendanceDataArrayList = new ArrayList<AttendanceData>();
        attendanceDataArrayList.clear();
        attendanceDataArrayList = attendanceMasterModel.getAttendanceList(masterUid, memberProfileID,spinMonth.getSelectedItem().toString(),spYear.getSelectedItem().toString());
        Log.e("@@@@@@@@@@@@@", "ATTENDANCE_LIST : " + attendanceDataArrayList);

        if(attendanceDataArrayList != null && !attendanceDataArrayList.isEmpty()) {
            /*attendance_list.setVisibility(View.VISIBLE);
            tvNA.setVisibility(View.GONE);*/
            Log.e("Touchbase....DATABASE", "Loaded from local db");
            adapterAttendance = new AttendanceListAdapter(AttendanceList.this, R.layout.attendance_list_item, attendanceDataArrayList);
            attendance_list.setAdapter(adapterAttendance);
            Log.d("---","ATTENDANCE_LIST----------"+attendance_list);
        } else {
            attendanceDataArrayList = new ArrayList<>();
            attendanceDataArrayList.add(new AttendanceData("","","","","","",""));
            adapterAttendance = new AttendanceListAdapter(AttendanceList.this, R.layout.attendance_list_item, attendanceDataArrayList);
            attendance_list.setAdapter(adapterAttendance);
        }

        //Checking for latest updates in attendance
        if (InternetConnection.checkConnection(getApplicationContext()))
            loadFromServer();
        else
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
    }


    public void refreshFromDb() {

        Log.d("Touchbase", "Trying to load from local db");


        attendanceDataArrayList = new ArrayList<AttendanceData>();
        attendanceDataArrayList.clear();
        attendanceDataArrayList = attendanceMasterModel.getAttendanceList(masterUid, memberProfileID,spinMonth.getSelectedItem().toString(),spYear.getSelectedItem().toString());
        Log.e("@@@@@@@@@@@@@", "ATTENDANCE_LIST : " + attendanceDataArrayList);

        if(attendanceDataArrayList != null && !attendanceDataArrayList.isEmpty()) {
            /*attendance_list.setVisibility(View.VISIBLE);
            tvNA.setVisibility(View.GONE);*/
            Log.e("Touchbase....DATABASE", "Loaded from local db");
            adapterAttendance = new AttendanceListAdapter(AttendanceList.this, R.layout.attendance_list_item, attendanceDataArrayList);
            Log.e("Touchbase", "Attendence list : " + attendanceDataArrayList);
            attendance_list.setAdapter(adapterAttendance);
            Log.d("---","ATTENDANCE_LIST----------"+attendance_list);
        }
    }

    public void loadFromServer() {
        Log.e("Touchbase....DATABASE", "Loaded from SERVER");
        webservices();
    }


    Handler attendanceDataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("DBError", "Handler is called");
            boolean saved = attendanceMasterModel.insert(masterUid, attendanceDataArrayList);
            if (!saved) {
                Log.d("Touchbase", "Failed to save offlline. Retrying in 2 seconds");
                sendEmptyMessageDelayed(0, 2000);
            } else {
                Log.e("-----------", "----SAVED----");
                refreshFromDb();
            }
        }
    };


    public class YearAdapter extends ArrayAdapter<String> {
        public YearAdapter(Context _context,
                               List<String> _items) {

            super(_context, R.layout.year_spinner_item, _items);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //You can use the new tf here.
            if ( convertView == null ) {
                convertView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.year_spinner_item, null);
            }
            TextView spinner_text=(TextView)convertView.findViewById(R.id.text1);
            spinner_text.setText(getItem(position));
            return convertView;
        }
    }

}
