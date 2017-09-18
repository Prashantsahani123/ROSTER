package com.SampleApp.row.calendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.SampleApp.row.Adapter.CalendarNotificationAdapter;
import com.SampleApp.row.Data.CalendarNotifiationData;
import com.SampleApp.row.Data.LabelData;
import com.SampleApp.row.Data.profiledata.Separator;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;

/**
 * Created by user on 17-02-2017.
 */
public class CalendarNotificationDetails extends Activity {
    String groupId = "";
    ImageView iv_backbutton;
    TextView tv_title, tv_birthdayCount, tv_anniversaryCount;
    Context context;
    ArrayList<Object> list = new ArrayList<>();
    ArrayList<CalendarNotifiationData> birthdayList = new ArrayList<>();
    ArrayList<CalendarNotifiationData> anniversaryList = new ArrayList<>();
    private RecyclerView mRecyclerView_birthday;
    private RecyclerView mRecyclerView_anniversary;
    private RecyclerView.LayoutManager mLayoutManager_birthday;
    private RecyclerView.LayoutManager mLayoutManager_anniversary;

    CalendarNotificationAdapter recylerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_details);
        context = CalendarNotificationDetails.this;
        actionbarfunction();

        Intent i = getIntent();
        groupId = i.getStringExtra("grpID");

        if (InternetConnection.checkConnection(context)) {
            webservices();
        } else {
            Log.d("♦♦♦♦ TB Calendar", "No Internet Connection");
            Toast.makeText(context, "No Internet Connection.Cannot Display Record", Toast.LENGTH_LONG).show();
        }

        tv_birthdayCount = (TextView) findViewById(R.id.birthdaycount);
        tv_anniversaryCount = (TextView) findViewById(R.id.anniversarycount);
        mRecyclerView_birthday = (RecyclerView) findViewById(R.id.recycleView_birthday);
        mRecyclerView_anniversary = (RecyclerView) findViewById(R.id.recycleView_anniversary);
        mRecyclerView_birthday.setHasFixedSize(true);
        mRecyclerView_anniversary.setHasFixedSize(true);
        mLayoutManager_birthday = new LinearLayoutManager(context);
        mLayoutManager_anniversary = new LinearLayoutManager(context);
        mRecyclerView_birthday.setLayoutManager(mLayoutManager_birthday);
        mRecyclerView_anniversary.setLayoutManager(mLayoutManager_anniversary);

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void actionbarfunction() {
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("B'day & Anniversary");
    }

    public void webservices() {
        Log.d("♦♦♦♦TB Calendar", "webservices() is called");
        String url = Constant.GET_TODAYS_BIRTHDAY;
        ArrayList<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("groupID", groupId));
        Log.d("♦♦♦♦ Request Parameters", list.toString());

        GetTodayBirthDayAsynctask task = new GetTodayBirthDayAsynctask(url, list, context);
        task.execute();
    }

    public class GetTodayBirthDayAsynctask extends AsyncTask<String, Object, Object> {

        String url;
        ArrayList<NameValuePair> list;
        Context con;
        final ProgressDialog dialog = new ProgressDialog(context, R.style.TBProgressBar);
        String val;

        public GetTodayBirthDayAsynctask(String url, ArrayList<NameValuePair> list, Context con) {
            this.url = url;
            this.list = list;
            this.con = con;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            dialog.show();
        }

        @Override
        protected Object doInBackground(String... params) {

            try {
                val = HttpConnection.postData(url, list);
                val = val.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return val;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (result != "" && result != null) {
                getData(result.toString());
            }
        }
    }

    public void getData(String result) {
        try {
            JSONObject json = new JSONObject(result);
            JSONObject json_member = json.getJSONObject("TBMemberListResult");
            String status = json_member.getString("status").toString();
            if (status.equalsIgnoreCase("0")) {
                String message = json_member.getString("message");
                JSONArray jsonresult = json_member.getJSONArray("Result");
                for (int i = 0; i < jsonresult.length(); i++) {
                    CalendarNotifiationData data = new CalendarNotifiationData();
                    JSONObject result_object = jsonresult.getJSONObject(i);
                    data.setProfileId(result_object.getString("profileId"));
                    data.setGroupId(result_object.getString("groupID"));
                    data.setMembername(result_object.getString("memberName"));
                    data.setMemberMobile(result_object.getString("memberMobile"));
                    try {
                        data.setRelation(result_object.getString("relation"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    data.setMsg(result_object.getString("msg"));
                    if (data.getMsg().equalsIgnoreCase("BirthDay")) {
                        birthdayList.add(data);
                    } else {
                        anniversaryList.add(data);
                    }
                }

            } else {
                Log.e("Response is", "Response:-" + result);
            }
            /*recylerAdapter = new CalendarNotificationAdapter(context, birthdayList);
            mRecyclerView_birthday.setAdapter(recylerAdapter);*/
            if (birthdayList != null && birthdayList.size() > 0) {
                list.add(new LabelData("Today's Birthday", ""+birthdayList.size()));
                list.addAll(birthdayList);
                /*tv_birthdayCount.setVisibility(View.VISIBLE);
                tv_birthdayCount.setText(String.valueOf(birthdayList.size()));*/
            }

            if (anniversaryList != null && anniversaryList.size() > 0) {
                /*tv_anniversaryCount.setVisibility(View.VISIBLE);
                tv_anniversaryCount.setText(String.valueOf(anniversaryList.size()));*/
                if ( birthdayList.size() > 0 ) {
                    list.add(new Separator());
                }
                list.add(new LabelData("Today's Anniversary", ""+anniversaryList.size()));
                list.addAll(anniversaryList);
            }
            recylerAdapter = new CalendarNotificationAdapter(context, list);
            mRecyclerView_birthday.setAdapter(recylerAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
