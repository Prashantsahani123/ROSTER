package com.SampleApp.row;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.LeaderBoardAdapter;
import com.SampleApp.row.Data.LeaderBoardData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoardActivity extends AppCompatActivity {

    private TextView tv_title;
    private LinearLayout ll_dop,ll_cop,ll_bene,ll_timespent,ll_noOfRotarians,ll_members,ll_trf;
    private RecyclerView rvClubs;
    private Context ctx;
    private LeaderBoardAdapter leaderBoardAdapter;
    private ArrayList<LeaderBoardData> leaderBoardDataArrayList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private String title="";
    private TextView tv_projects,tv_cost,tv_beneficiary,tv_manHrSpent,tv_noOfRotarians,tv_members,tv_trf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_leader_board);

        findViewById();

        ctx = LeaderBoardActivity.this;

        title = "District Leader Board";//getIntent().getStringExtra("moduleName");

        init();

        for(int i=0;i<10;i++) {

            LeaderBoardData leaderBoardData = new LeaderBoardData();
            leaderBoardData.setId(""+i);
            leaderBoardData.setName("Club name "+i);
            leaderBoardData.setScore(""+((10-i)*12));
            leaderBoardDataArrayList.add(leaderBoardData);
        }

        leaderBoardAdapter = new LeaderBoardAdapter(ctx,leaderBoardDataArrayList);
        rvClubs.setAdapter(leaderBoardAdapter);

    }

    private void init() {

        layoutManager = new LinearLayoutManager(ctx);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvClubs.setLayoutManager(layoutManager);

        tv_title.setText(title);

        ((GradientDrawable)ll_dop.getBackground()).setColor(getResources().getColor(R.color.lb_project));
        ((GradientDrawable)ll_cop.getBackground()).setColor(getResources().getColor(R.color.lb_cost));
        ((GradientDrawable)ll_bene.getBackground()).setColor(getResources().getColor(R.color.lb_beneficiaries));
        ((GradientDrawable)ll_timespent.getBackground()).setColor(getResources().getColor(R.color.lb_menhr));
        ((GradientDrawable)ll_noOfRotarians.getBackground()).setColor(getResources().getColor(R.color.lb_rotarians));
        ((GradientDrawable)ll_members.getBackground()).setColor(getResources().getColor(R.color.lb_members));
        ((GradientDrawable)ll_trf.getBackground()).setColor(getResources().getColor(R.color.lb_trf));

    }

    private void findViewById() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_dop = (LinearLayout) findViewById(R.id.ll_dop);
        ll_cop = (LinearLayout) findViewById(R.id.ll_cop);
        ll_bene = (LinearLayout) findViewById(R.id.ll_bene);
        ll_timespent = (LinearLayout) findViewById(R.id.ll_timespent);
        ll_noOfRotarians = (LinearLayout) findViewById(R.id.ll_noOfRotarians);
        ll_members = (LinearLayout) findViewById(R.id.ll_members);
        ll_trf = (LinearLayout) findViewById(R.id.ll_trf);
        rvClubs = (RecyclerView) findViewById(R.id.rvClubs);
        tv_projects = (TextView) findViewById(R.id.tv_projects);
        tv_cost = (TextView) findViewById(R.id.tv_cost);
        tv_beneficiary = (TextView) findViewById(R.id.tv_beneficiary);
        tv_manHrSpent = (TextView) findViewById(R.id.tv_manHrSpent);
        tv_noOfRotarians = (TextView) findViewById(R.id.tv_noOfRotarians);
        tv_members = (TextView) findViewById(R.id.tv_members);
        tv_trf = (TextView) findViewById(R.id.tv_trf);
    }

    private void webservices() {

        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        arrayList.add(new BasicNameValuePair("profileId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));

        Log.d("Response", "PARAMETERS " + Constant.GetLeaderBoardDetail + " :- " + arrayList.toString());

        if ( InternetConnection.checkConnection(getApplicationContext())) {
            new WebConnectionAsyncDirectory(Constant.GetLeaderBoardDetail, arrayList, ctx).execute();
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

                Log.d("Response", "leaderboard satish=> " + val);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return val;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            if (result != "") {
                Log.d("Response", "calling get leader board details");
                // eventListDatas.clear();
                getClubItems(result.toString());
            } else {
                Log.d("Response", "Null Resposnse");
            }

        }
    }

    private void getClubItems(String str){

        try {

            JSONObject jsonObject = new JSONObject(str);

            String status = jsonObject.getString("status");

            if(status.equalsIgnoreCase("0")) {

                String projects = jsonObject.getString("projects");
                String cost = jsonObject.getString("cost");
                String beneficiary = jsonObject.getString("beneficiary");
                String man_hour = jsonObject.getString("manHr");
                String rotarians = jsonObject.getString("rotarians");
                String members = jsonObject.getString("members");
                String trf = jsonObject.getString("trf");

                tv_projects.setText(projects);
                tv_cost.setText(cost);
                tv_beneficiary.setText(beneficiary);
                tv_manHrSpent.setText(man_hour);
                tv_noOfRotarians.setText(rotarians);
                tv_members.setText(members);
                tv_trf.setText(trf);

                JSONArray jsonArray = jsonObject.getJSONArray("ClubListResult");

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject clubObject = jsonArray.getJSONObject(i);
                    String id = clubObject.getString("clubId");
                    String clubName = clubObject.getString("clubName");
                    String clubScore = clubObject.getString("clubScore");

                    LeaderBoardData leaderBoardData = new LeaderBoardData();
                    leaderBoardData.setId(id);
                    leaderBoardData.setName(clubName);
                    leaderBoardData.setScore(clubScore);
                    leaderBoardDataArrayList.add(leaderBoardData);
                }

            }else{

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
