package com.NEWROW.row;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.AttendanceEventAdapter;
import com.NEWROW.row.Adapter.AttendenceAdapter;
import com.NEWROW.row.Data.AttendanceData;
import com.NEWROW.row.Data.EventListData;
import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AttendenceActivity extends AppCompatActivity {

    AttendenceAdapter attendenceAdapter;
    ArrayList<AttendanceData> attendenceList;
    TextView tv_title,txt_noRecord;
    ImageView iv_backbutton,iv_actionbtn,iv_actionbtn2;
    RecyclerView rv_attendence;
    ListView rv_eventList;
    Context context;
    String type_filter_flag = "0";
    private String isAdmin = "No",grpID = "0",memberProfileID = "0";
    public static String title="";
    private ArrayList<EventListData> eventListDatas = new ArrayList<EventListData>();
    private AttendanceEventAdapter eventListAdapter;
    ProgressDialog progressDialog;
    public static int EVENT=1,ATTENDENCE=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_attendence);

        context = this;

        tv_title = (TextView) findViewById(R.id.tv_title);
        txt_noRecord = (TextView) findViewById(R.id.txt_noRecord);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn2 = (ImageView) findViewById(R.id.iv_actionbtn2);
        rv_attendence = (RecyclerView)findViewById(R.id.rv_attendenceList);

        rv_attendence.setLayoutManager(new LinearLayoutManager(AttendenceActivity.this));

        rv_eventList = (ListView) findViewById(R.id.rv_eventList);

        Intent i= getIntent();
        title = i.getStringExtra("moduleName");
        tv_title.setText(title);

        grpID = PreferenceManager.getPreference(context, PreferenceManager.GROUP_ID);
        memberProfileID = PreferenceManager.getPreference(context, PreferenceManager.GRP_PROFILE_ID);
        isAdmin =PreferenceManager.getPreference(context, PreferenceManager.IS_GRP_ADMIN,"No");

        attendenceList = new ArrayList<>();

        checkAdminRight();

        init();

//        AttendanceData data1 = new AttendanceData();
//        data1.setName("Club Meeting");
//        data1.setDate("11 MAR 2018");
//        data1.setTime("11.23 AM");
//        attendenceList.add(data1);
//
//        AttendanceData data2 = new AttendanceData();
//        data2.setName("Club Meeting");
//        data2.setDate("11 MAR 2018");
//        data2.setTime("11.23 AM");
//        attendenceList.add(data2);
//        attendenceAdapter = new AttendenceAdapter(AttendenceActivity.this,attendenceList);
//        rv_attendence.setAdapter(attendenceAdapter);

    }

    private void checkAdminRight(){
        if(isAdmin.equalsIgnoreCase("Yes")){
            iv_actionbtn.setVisibility(View.VISIBLE);
            iv_actionbtn2.setVisibility(View.GONE);
        }else {
            iv_actionbtn.setVisibility(View.GONE);
            iv_actionbtn2.setVisibility(View.GONE);
        }
    }

    private void init(){

        iv_actionbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // change by satish on 05-07-2019 as neha told
                //addAttendancePopup();
                Intent intent = new Intent(context,EditAttendence.class);
                startActivity(intent);
            }
        });
    }

    private void getAttendanceList(){

        if (InternetConnection.checkConnection(context)) {

            try {

                JSONObject requestObject=new JSONObject();

                requestObject.put("groupProfileID",memberProfileID);
                requestObject.put("groupID",grpID);

//                // TODO: 11-06-2018 Remove Hardcode values
//                requestObject.put("groupProfileID","1");
//                requestObject.put("groupID","11111");

                Utils.log(Constant.GetAttendanceListNew + " / "+requestObject.toString());

                showDialog();

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.GetAttendanceListNew, requestObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.log(""+response);
                        setAttendanceList(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                        Utils.log("VollyError:- " + error);
                    }
                });

                AppController.getInstance().addToRequestQueue(context, request);

            } catch (Exception e){
                e.printStackTrace();
            }

        } else {
            Utils.showToastWithTitleAndContext(context,getString(R.string.noInternet));
        }

    }

    private void setAttendanceList(JSONObject response){

        try {

            JSONObject TBAttendanceListResult = response.getJSONObject("TBAttendanceListResult");

            String status = TBAttendanceListResult.getString("status");

            if(status.equalsIgnoreCase("0")){

                attendenceList.clear();

                JSONArray AttendanceListResult=TBAttendanceListResult.getJSONArray("AttendanceListResult");

                if(AttendanceListResult!=null && AttendanceListResult.length()>0){
//                    txt_noRecord.setVisibility(View.GONE);
//                    changeView(ATTENDENCE);
                    for(int i=0;i<AttendanceListResult.length();i++){
                        JSONObject object=AttendanceListResult.getJSONObject(i);
                        JSONObject AttendanceResult=object.getJSONObject("AttendanceResult");
                        AttendanceData data=new AttendanceData();
                        data.setId(AttendanceResult.getString("AttendanceID"));
                        data.setName(AttendanceResult.getString("AttendanceName"));
                        data.setDate(AttendanceResult.getString("AttendanceDate"));
                        data.setTime(AttendanceResult.getString("Attendancetime"));
                        attendenceList.add(data);
                    }

                    attendenceAdapter = new AttendenceAdapter(AttendenceActivity.this,attendenceList);
                    rv_attendence.setAdapter(attendenceAdapter);
                    changeView(ATTENDENCE);
                }else {
                    txt_noRecord.setText(R.string.No_Results);
                    txt_noRecord.setVisibility(View.VISIBLE);
                    rv_attendence.setVisibility(View.GONE);
                    rv_eventList.setVisibility(View.GONE);
                    checkAdminRight();
                }

            } else {
                Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                rv_eventList.setVisibility(View.GONE);
                checkAdminRight();
            }

            hideDialog();

        } catch (JSONException e) {
            hideDialog();
            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
            rv_eventList.setVisibility(View.GONE);
            checkAdminRight();
            Utils.log("VollyError:- " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void getEvent(){

        if (InternetConnection.checkConnection(context)) {

            try {

                JSONObject requestObject=new JSONObject();
                requestObject.put("searchText","");
                requestObject.put("groupID",grpID);
                requestObject.put("type", type_filter_flag);
//                // TODO: 11-06-2018 Remove Hardcode values
//                requestObject.put("groupProfileID","1");
//                requestObject.put("groupID","11111");

                Utils.log(Constant.GetAttendanceEventsListNew+" / "+requestObject.toString());

                showDialog();

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.GetAttendanceEventsListNew, requestObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Utils.log(""+response);

                        progressDialog.dismiss();

                        //	Log.d("response","Do post"+ result.toString());
                        if (response != null) {
                            Log.d("Response", "calling getDirectorydetails");
                            changeView(EVENT);
                            eventListDatas.clear();
                            getEventItems(response.toString());
                        } else {
                            changeView(ATTENDENCE);
                            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                            Log.d("Response", "Null Resposnse");
                        }
                       // setAttendanceList(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        hideDialog();
                        Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                        Utils.log("VollyError:- " + error);
                    }
                });

                AppController.getInstance().addToRequestQueue(context, request);

            } catch (Exception e){
               e.printStackTrace();
            }

        } else {
            Utils.showToastWithTitleAndContext(context,getString(R.string.noInternet));
        }

    }

    private void addAttendancePopup(){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = getLayoutInflater().inflate(R.layout.popup_add_attendance, null);
        builder.setView(view);

        final AlertDialog msgDialog = builder.create();

        TextView txt_fromEvent,txt_addNew;
        txt_addNew = (TextView)view.findViewById(R.id.txt_addNew);
        txt_fromEvent = (TextView)view.findViewById(R.id.txt_fromEvent);

        txt_addNew.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,EditAttendence.class);
                startActivity(intent);
                msgDialog.dismiss();

//                Intent intent=new Intent(context,EditDependentActivity.class);
//                startActivity(intent);
//                msgDialog.dismiss();
            }
        });

        txt_fromEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                getEvent();
                //getEventList();
                msgDialog.dismiss();
            }
        });

        msgDialog.show();
    }

    private void getEventList() {
        //{"groupProfileID":"43","grpId":"74","Type":"0","Admin":"0","searchText":""}
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
//        arrayList.add(new BasicNameValuePair("groupProfileID", PreferenceManager.getPreference(context, PreferenceManager.GRP_PROFILE_ID)));
        arrayList.add(new BasicNameValuePair("groupID", PreferenceManager.getPreference(context, PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("searchText", ""));
        arrayList.add(new BasicNameValuePair("type", type_filter_flag));
//        if (PreferenceManager.getPreference(context, PreferenceManager.IS_GRP_ADMIN).equals("No")) {
//
//        }else{
//
//        }
//        //arrayList.add(new BasicNameValuePair("Type", type_filter_flag));
//        if( PreferenceManager.getPreference(context, PreferenceManager.IS_GRP_ADMIN).equals("No")){
//            arrayList.add(new BasicNameValuePair("Admin","0"));
//        } else {
//            arrayList.add(new BasicNameValuePair("Admin","1"));
//        }
//
//
//

        Log.d("Response", "PARAMETERS " + Constant.GetAttendanceEventsListNew + " :- " + arrayList.toString());

        if ( InternetConnection.checkConnection(getApplicationContext())) {
            new WebConnectionAsyncDirectory(Constant.GetAttendanceEventsListNew, arrayList, context).execute();
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public class WebConnectionAsyncDirectory extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.TBProgressBar);

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
                changeView(EVENT);
                eventListDatas.clear();
                getEventItems(result.toString());
            } else {
                changeView(ATTENDENCE);
                Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getEventItems(String result) {

        try {

            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("TBAttendanceEventsListResult");

            final String status = EventResult.getString("status");

            if (EventResult.has("SMSCount")) {
                Utils.smsCount = EventResult.getString("SMSCount");
            }

            if (status.equals("0")) {

                JSONArray EventListResdult = EventResult.getJSONArray("AttendanceEventsListResult");

                if(EventListResdult.length()>0){
                    txt_noRecord.setVisibility(View.GONE);
                    rv_eventList.setVisibility(View.VISIBLE);
                    rv_attendence.setVisibility(View.GONE);
                    for (int i = 0; i < EventListResdult.length(); i++) {

                        JSONObject object = EventListResdult.getJSONObject(i);
                        JSONObject objects = object.getJSONObject("AttendanceResult");

                        EventListData data = new EventListData();

                        data.setEventID(objects.getString("eventID").toString());
                        data.setEventTitle(objects.getString("eventTitle").toString());
                        data.setEventDateTime(objects.getString("eventDateTime").toString());


                        eventListDatas.add(data);
                    }

                    eventListAdapter = new AttendanceEventAdapter(context, R.layout.attendence_item, eventListDatas);
                    rv_eventList.setAdapter(eventListAdapter);
                }else {
                    txt_noRecord.setText(R.string.no_events);
                    txt_noRecord.setVisibility(View.VISIBLE);
                    rv_eventList.setVisibility(View.GONE);
                    rv_attendence.setVisibility(View.GONE);

                }


            } else {
//                Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
//                rv_attendence.setVisibility(View.GONE);
//                eventListDatas.clear();
//                eventListAdapter = new AttendanceEventAdapter(context, R.layout.attendence_item, eventListDatas);
//                rv_eventList.setAdapter(eventListAdapter);
                txt_noRecord.setText(R.string.no_events);
                txt_noRecord.setVisibility(View.VISIBLE);
                rv_eventList.setVisibility(View.GONE);
                rv_attendence.setVisibility(View.GONE);
            }

          // rv_eventList.setEmptyView(((TextView) findViewById(R.id.tv_no_records_found)));
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
            changeView(ATTENDENCE);
            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
//            rv_attendence.setVisibility(View.GONE);
//            eventListDatas.clear();
//            eventListAdapter = new AttendanceEventAdapter(context, R.layout.attendence_item, eventListDatas);
//            rv_eventList.setAdapter(eventListAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        if(rv_eventList.getVisibility()==View.VISIBLE){
            changeView(ATTENDENCE);
        }
        else {
            finish();
        }

    }

    private void changeView(int mode){
        if(mode==EVENT){
            rv_attendence.setVisibility(View.GONE);
            rv_eventList.setVisibility(View.VISIBLE);
            tv_title.setText("Select Event");
            iv_actionbtn.setVisibility(View.GONE);
        }else {
            if(attendenceList.size()>0){
                rv_attendence.setVisibility(View.VISIBLE);
                rv_eventList.setVisibility(View.GONE);
                txt_noRecord.setVisibility(View.GONE);
                tv_title.setText(title);
                checkAdminRight();
            }else {
                txt_noRecord.setText(R.string.No_Results);
                txt_noRecord.setVisibility(View.VISIBLE);
                rv_attendence.setVisibility(View.GONE);
                rv_eventList.setVisibility(View.GONE);
                tv_title.setText(title);
                checkAdminRight();
            }



        }
    }


    private void showDialog(){
        progressDialog = new ProgressDialog(context, R.style.TBProgressBar);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.show();
    }

    private void hideDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAttendanceList();
    }
}
