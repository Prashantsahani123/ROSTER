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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.LeaderBoardAdapter;
import com.SampleApp.row.Data.LeaderBoardData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LeaderBoardActivity extends AppCompatActivity {

    private TextView tv_title;
    private LinearLayout ll_dop,ll_cop,ll_bene,ll_timespent,ll_noOfRotarians,ll_members,ll_trf;
    private RecyclerView rvClubs;
    private Context ctx;
    private LeaderBoardAdapter leaderBoardAdapter;
    private ArrayList<LeaderBoardData> leaderBoardDataArrayList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private ArrayList<String> filtertype = new ArrayList<>();
    private ArrayList<String> filtertypeValue = new ArrayList<>();
    private String title="";
    private LinearLayout leader_layout,leader_mainlayout;
    private Spinner spinner_year;
    private TextView tv_no_records_found,tv_no_citation_found,tv_projects,tv_cost,tv_beneficiary,tv_manHrSpent,tv_noOfRotarians,tv_members,tv_trf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_leader_board);

        findViewById();

        ctx = LeaderBoardActivity.this;

        title = "Leader Board";//getIntent().getStringExtra("moduleName");

        init();

      /*  for(int i=0;i<10;i++) {

            LeaderBoardData leaderBoardData = new LeaderBoardData();
            leaderBoardData.setId(""+i);
            leaderBoardData.setName("Club name "+i);
            leaderBoardData.setScore(""+((10-i)*12));
            leaderBoardDataArrayList.add(leaderBoardData);
        }

        leaderBoardAdapter = new LeaderBoardAdapter(ctx,leaderBoardDataArrayList);
        rvClubs.setAdapter(leaderBoardAdapter);*/

    }

    private void init() {

        layoutManager = new LinearLayoutManager(ctx);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvClubs.setLayoutManager(layoutManager);

        tv_title.setText(title);

/*        ((GradientDrawable)ll_dop.getBackground()).setColor(getResources().getColor(R.color.lb_project));
        ((GradientDrawable)ll_cop.getBackground()).setColor(getResources().getColor(R.color.lb_cost));
        ((GradientDrawable)ll_bene.getBackground()).setColor(getResources().getColor(R.color.lb_beneficiaries));
        ((GradientDrawable)ll_timespent.getBackground()).setColor(getResources().getColor(R.color.lb_menhr));
        ((GradientDrawable)ll_noOfRotarians.getBackground()).setColor(getResources().getColor(R.color.lb_rotarians));
        ((GradientDrawable)ll_members.getBackground()).setColor(getResources().getColor(R.color.lb_members));
        ((GradientDrawable)ll_trf.getBackground()).setColor(getResources().getColor(R.color.lb_trf));*/

        ((GradientDrawable)ll_dop.getBackground()).setColor(getResources().getColor(R.color.view_color_blue));
        ((GradientDrawable)ll_cop.getBackground()).setColor(getResources().getColor(R.color.view_color_blue));
        ((GradientDrawable)ll_bene.getBackground()).setColor(getResources().getColor(R.color.view_color_blue));
        ((GradientDrawable)ll_timespent.getBackground()).setColor(getResources().getColor(R.color.view_color_blue));
        ((GradientDrawable)ll_noOfRotarians.getBackground()).setColor(getResources().getColor(R.color.view_color_blue));
        ((GradientDrawable)ll_members.getBackground()).setColor(getResources().getColor(R.color.lb_members));
        ((GradientDrawable)ll_trf.getBackground()).setColor(getResources().getColor(R.color.lb_beneficiaries));


        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

        Utils.log("Month: " + month + " year : " + year);

        int flag = 0;

        for (int i = year; i >= 2015; i--) {

            String filterYear,filterYearHint;
            int modInt = i % 100;

            if (month > 6 && i == year) {
                filterYear = (i) + "-" + (i + 1);
                filterYearHint = modInt + "-"+(modInt+1);
                flag=1;
            } else if (month <= 6 && i == year) {
                filterYear = (i-1) + "-" + (i);
                filterYearHint = (modInt-1) + "-"+(modInt);
                flag=2;
            } else {

                if(flag==1){
                    filterYear = (i) + "-" + (i+1);
                    filterYearHint = modInt + "-"+(modInt+1);
                }else {
                    filterYear = (i-1) + "-" + (i);
                    filterYearHint = (modInt-1) + "-"+(modInt);
                }

            }

            filtertype.add(filterYearHint);
            filtertypeValue.add(filterYear);

        }

        if(flag!=1){
            filtertype.remove(filtertype.size()-1);
            filtertypeValue.remove(filtertypeValue.size()-1);
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_leader_board, filtertype);

        spinner_year.setAdapter(spinnerArrayAdapter);

        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedYear = filtertypeValue.get(position);//(filtertype.indexOf(spinner_year.getSelectedItem()));
               /* String array[] = selectedYear.split("-");
                fromYear = array[0];
                toYear = array[1];
                Utils.log(fromYear + " " + toYear);*/
                webservices(selectedYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void findViewById() {
        leader_layout = (LinearLayout) findViewById(R.id.leader_layout);
        leader_mainlayout = (LinearLayout) findViewById(R.id.leader_mainlayout);
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
        tv_no_records_found = (TextView) findViewById(R.id.tv_no_records_found);
        tv_no_citation_found = (TextView) findViewById(R.id.tv_no_citation_found);
        spinner_year = (Spinner) findViewById(R.id.spinner_year);
    }

    private void webservices(String year) {

        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        arrayList.add(new BasicNameValuePair("ProfileID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));
        arrayList.add(new BasicNameValuePair("RowYear",year));
//        arrayList.add(new BasicNameValuePair("DistrictID","2"));

        arrayList.add(new BasicNameValuePair("GroupID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));

        //arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));

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

            JSONObject jsonObjectMain = new JSONObject(str);

            JSONObject jsonObject = jsonObjectMain.getJSONObject("TBLeaderBoardResult");

            String status = jsonObject.getString("status");

            if(status.equalsIgnoreCase("0")) {

                String projects = jsonObject.getString("TotalProjects");
                String cost = jsonObject.getString("ProjectCost");
                String beneficiary = jsonObject.getString("BeneficiaryCount");
                String man_hour = jsonObject.getString("ManHoursCount");
                String rotarians = jsonObject.getString("RotariansCount");
                String members = jsonObject.getString("MembersCount");
                String trf = jsonObject.getString("TRFCount");

                tv_projects.setText(projects);
                tv_cost.setText(cost);
                tv_beneficiary.setText(beneficiary);
                tv_manHrSpent.setText(man_hour);
                tv_noOfRotarians.setText(rotarians);
                tv_members.setText(members);
                tv_trf.setText("$ "+trf);

                JSONArray jsonArray = jsonObject.getJSONArray("LeaderBoardResult");

                leaderBoardDataArrayList.clear();

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject resultObject = jsonArray.getJSONObject(i);

                    JSONObject clubObject = resultObject.getJSONObject("LeaderBoardResult");

                    String id = clubObject.getString("clubId");
                    String clubName = clubObject.getString("clubName");
                    String clubScore = clubObject.getString("Points");
                    String Srno = clubObject.getString("Srno");

                    LeaderBoardData leaderBoardData = new LeaderBoardData();
                    leaderBoardData.setId(id);
                    leaderBoardData.setName(clubName);
                    leaderBoardData.setScore(clubScore);
                    leaderBoardData.setSrNo(Srno);

                    leaderBoardDataArrayList.add(leaderBoardData);
                }

                leader_mainlayout.setVisibility(View.VISIBLE);
                tv_no_records_found.setVisibility(View.GONE);


                if(leaderBoardDataArrayList.size()>0){
                    leader_layout.setVisibility(View.VISIBLE);
                    tv_no_citation_found.setVisibility(View.GONE);
                }else{
                    tv_no_citation_found.setVisibility(View.VISIBLE);
                    leader_layout.setVisibility(View.GONE);
                }

                leaderBoardAdapter = new LeaderBoardAdapter(ctx,leaderBoardDataArrayList);
                rvClubs.setAdapter(leaderBoardAdapter);

            } else {
//                Utils.showToastWithTitleAndContext(ctx,"No Data available");
                //This place is reserved for Clubs to show their citation points
                tv_no_records_found.setVisibility(View.VISIBLE);
                leader_mainlayout.setVisibility(View.GONE);
                leaderBoardDataArrayList.clear();
                leaderBoardAdapter = new LeaderBoardAdapter(ctx,leaderBoardDataArrayList);
                rvClubs.setAdapter(leaderBoardAdapter);
                tv_projects.setText("");
                tv_cost.setText("");
                tv_beneficiary.setText("");
                tv_manHrSpent.setText("");
                tv_noOfRotarians.setText("");
                tv_members.setText("");
                tv_trf.setText("");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
