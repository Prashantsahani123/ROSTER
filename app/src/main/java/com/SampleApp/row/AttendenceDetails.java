package com.SampleApp.row;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.SampleApp.row.Adapter.AttendenceDetailsAdapter;
import com.SampleApp.row.Data.AttendenceDetailsData;
import com.SampleApp.row.Inteface.AttendanceItemClick;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AttendenceDetails extends AppCompatActivity implements AttendanceItemClick {

    TextView tv_title,tv_eventName,tv_attendenceDate,tv_attendenceTime,tv_description;
    String title,attendance_id="0";
    private String isAdmin = "No",grpID = "0",memberProfileID = "0";
    ImageView iv_backbutton,iv_actionbtn,iv_actionbtn2;
    RecyclerView rv_attendenceDetails;
    ArrayList<AttendenceDetailsData> attendenceDetailsList = new ArrayList<>();
    AttendenceDetailsAdapter detailsAdapter;
    Context context;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_attendence_details);

        context=this;

//        Intent i = getIntent();
//        title = i.getStringExtra("albumname");
        tv_title = (TextView) findViewById(R.id.tv_title);

        title=getIntent().getStringExtra("moduleName");
        tv_title.setText(title);

        isAdmin = PreferenceManager.getPreference(context, PreferenceManager.IS_GRP_ADMIN,"No");

        Bundle b=getIntent().getExtras();

        init();
        checkAdminRight();

        if(b!=null){
            Intent i=getIntent();

            if(i.hasExtra("id")){
                attendance_id=i.getStringExtra("id");
//                getAttendanceDetails();
            }

        }

    }

    private void init() {
        tv_eventName = (TextView)findViewById(R.id.tvEventName);
        tv_attendenceDate = (TextView)findViewById(R.id.attendenceDate);
        tv_attendenceTime = (TextView)findViewById(R.id.attendenceTime);
        tv_description = (TextView)findViewById(R.id.tv_description);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn.setImageDrawable(getResources().getDrawable(R.drawable.edit));
        iv_actionbtn2 = (ImageView) findViewById(R.id.iv_actionbtn2);
        iv_actionbtn2.setImageDrawable(getResources().getDrawable(R.drawable.delete));

        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,EditAttendence.class);
                intent.putExtra("id",attendance_id);
                startActivity(intent);
            }
        });

        iv_actionbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_confrm_delete);
                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                tv_no.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        deleteAttendance();
                    }
                });

                dialog.show();

            }
        });

        rv_attendenceDetails = (RecyclerView)findViewById(R.id.rv_attendenceDetails);
        rv_attendenceDetails.setLayoutManager(new LinearLayoutManager(AttendenceDetails.this));


    }

    private void checkAdminRight(){
        if(isAdmin.equalsIgnoreCase("Yes")){

            iv_actionbtn.setVisibility(View.VISIBLE);
            iv_actionbtn2.setVisibility(View.VISIBLE);
        }else {
            iv_actionbtn.setVisibility(View.GONE);
            iv_actionbtn2.setVisibility(View.GONE);
        }
    }

    private void getAttendanceDetails(){

        if (InternetConnection.checkConnection(context)) {
            try {
                JSONObject requestObject=new JSONObject();

                requestObject.put("AttendanceID",attendance_id);


                Utils.log(Constant.GetAttendanceDetails+" / "+requestObject.toString());

                showDialog();

                JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, Constant.GetAttendanceDetails, requestObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Utils.log(""+response);
                        setAttendanceDetails(response);
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


            }catch (Exception e){

            }
        }else {
            Utils.showToastWithTitleAndContext(context,getString(R.string.noInternet));
        }

    }

    private void setAttendanceDetails(JSONObject response){
        try {

            JSONObject TBAttendanceDetailsResult = response.getJSONObject("TBAttendanceDetailsResult");
            String status=TBAttendanceDetailsResult.getString("status");
            if(status.equalsIgnoreCase("0")){

                JSONArray AttendanceDetailsResult=TBAttendanceDetailsResult.getJSONArray("AttendanceDetailsResult");
                if(AttendanceDetailsResult!=null && AttendanceDetailsResult.length()>0){
                    for(int i=0;i<AttendanceDetailsResult.length();i++){
                        JSONObject object=AttendanceDetailsResult.getJSONObject(i);
                        JSONObject AttendanceResult=object.getJSONObject("AttendanceResult");

                        attendance_id = AttendanceResult.getString("AttendanceID");
                        tv_eventName.setText(AttendanceResult.getString("AttendanceName"));
                        tv_attendenceDate.setText(AttendanceResult.getString("AttendanceDate"));
                        tv_attendenceTime.setText(AttendanceResult.getString("Attendancetime"));
                        tv_description.setText(AttendanceResult.getString("AttendanceDesc"));

                        attendenceDetailsList.clear();

                        AttendenceDetailsData data = new AttendenceDetailsData();
                        data.setCount(AttendanceResult.getString("MemberCount"));
                        data.setTitle("Members");
                        data.setType(Constant.Dependent.MEMBER);
                        data.setAttendanceID(attendance_id);
                        attendenceDetailsList.add(data);

                        AttendenceDetailsData data1 = new AttendenceDetailsData();
                        data1.setCount(AttendanceResult.getString("AnnsCount"));
                        data1.setTitle("Anns");
                        data1.setType(Constant.Dependent.ANNS);
                        data1.setAttendanceID(attendance_id);
                        attendenceDetailsList.add(data1);

                        AttendenceDetailsData data2 = new AttendenceDetailsData();
                        data2.setCount(AttendanceResult.getString("AnnetsCount"));
                        data2.setType(Constant.Dependent.ANNETS);
                        data2.setTitle("Annets");
                        data2.setAttendanceID(attendance_id);
                        attendenceDetailsList.add(data2);

                        AttendenceDetailsData data3 = new AttendenceDetailsData();
                        data3.setCount(AttendanceResult.getString("VisitorsCount"));
                        data3.setType(Constant.Dependent.VISITORS);
                        data3.setTitle("Visitors");
                        data3.setAttendanceID(attendance_id);
                        attendenceDetailsList.add(data3);

                        AttendenceDetailsData data4 = new AttendenceDetailsData();
                        data4.setCount(AttendanceResult.getString("RotarianCount"));
                        data4.setType(Constant.Dependent.ROTARIAN);
                        data4.setTitle("Rotarian's (Other Club)");
                        data4.setAttendanceID(attendance_id);
                        attendenceDetailsList.add(data4);

                        AttendenceDetailsData data5 = new AttendenceDetailsData();
                        data5.setCount(AttendanceResult.getString("DistrictDelegatesCount"));
                        data5.setType(Constant.Dependent.DELEGETS);
                        data5.setTitle("District Delegates");
                        data5.setAttendanceID(attendance_id);
                        attendenceDetailsList.add(data5);

                        detailsAdapter = new AttendenceDetailsAdapter(attendenceDetailsList,AttendenceDetails.this);
                        rv_attendenceDetails.setAdapter(detailsAdapter);
                        detailsAdapter.setClickListener(this);
                    }



                }else {
                    Utils.showToastWithTitleAndContext(context,getString(R.string.msg_no_records_found));
                }
            }else {
                Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
            }

            hideDialog();
        } catch (JSONException e) {
            hideDialog();
            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
            Utils.log("VollyError:- " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!attendance_id.equalsIgnoreCase("0")){
            getAttendanceDetails();
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

    private void deleteAttendance(){
        if (InternetConnection.checkConnection(context)) {
            try {
                JSONObject requestObject=new JSONObject();

                requestObject.put("AttendanceID",attendance_id);
                requestObject.put("createdBy",PreferenceManager.getPreference(context,PreferenceManager.GRP_PROFILE_ID));

                Utils.log(Constant.AttendanceDelete+" / "+requestObject.toString());

                showDialog();

                JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, Constant.AttendanceDelete, requestObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Utils.log(""+response);
                        getDeleteAttendanceResult(response);
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


            }catch (Exception e){

            }
        }else {
            Utils.showToastWithTitleAndContext(context,getString(R.string.noInternet));
        }
    }

    private void getDeleteAttendanceResult (JSONObject response){

        hideDialog();
        try {
            String status=response.getString("status");
            if(status.equalsIgnoreCase("0")){
                Utils.showToastWithTitleAndContext(context,"Deleted Successfully");
                finish();
            }else {
                Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));

            }
        } catch (JSONException e) {
            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view, int position) {


         AttendenceDetailsData data=attendenceDetailsList.get(position);


         if(data.getCount()!=null && !data.getCount().isEmpty() && !data.getCount().equalsIgnoreCase("0")) {


             if (data.getType().equalsIgnoreCase(Constant.Dependent.MEMBER)) {
                 Intent intent = new Intent(context, AddedDependentActivity.class);
                 intent.putExtra("attendanceId", data.getAttendanceID());
                 intent.putExtra("moduleName", data.getTitle());
                 intent.putExtra("count", data.getCount());
                 intent.putExtra("type", data.getType());
                 context.startActivity(intent);
             } else if (data.getType().equalsIgnoreCase(Constant.Dependent.ANNS)) {
                 Intent intent = new Intent(context, AddedDependentActivity.class);
                 intent.putExtra("attendanceId", data.getAttendanceID());
                 intent.putExtra("moduleName", data.getTitle());
                 intent.putExtra("count", data.getCount());
                 intent.putExtra("type", data.getType());
                 context.startActivity(intent);
             } else if (data.getType().equalsIgnoreCase(Constant.Dependent.ANNETS)) {
                 Intent intent = new Intent(context, AddedDependentActivity.class);
                 intent.putExtra("attendanceId", data.getAttendanceID());
                 intent.putExtra("moduleName", data.getTitle());
                 intent.putExtra("count", data.getCount());
                 intent.putExtra("type", data.getType());
                 context.startActivity(intent);
             } else if (data.getType().equalsIgnoreCase(Constant.Dependent.VISITORS)) {
                 Intent intent = new Intent(context, AddedDependentActivity.class);
                 intent.putExtra("attendanceId", data.getAttendanceID());
                 intent.putExtra("moduleName", data.getTitle());
                 intent.putExtra("count", data.getCount());
                 intent.putExtra("type", data.getType());
                 context.startActivity(intent);
             } else if (data.getType().equalsIgnoreCase(Constant.Dependent.ROTARIAN)) {
                 Intent intent = new Intent(context, AddedDependentActivity.class);
                 intent.putExtra("attendanceId", data.getAttendanceID());
                 intent.putExtra("moduleName", data.getTitle());
                 intent.putExtra("count", data.getCount());
                 intent.putExtra("type", data.getType());
                 context.startActivity(intent);
             } else if (data.getType().equalsIgnoreCase(Constant.Dependent.DELEGETS)) {
                 Intent intent = new Intent(context, AddedDependentActivity.class);
                 intent.putExtra("attendanceId", data.getAttendanceID());
                 intent.putExtra("moduleName", data.getTitle());
                 intent.putExtra("count", data.getCount());
                 intent.putExtra("type", data.getType());
                 context.startActivity(intent);
             }
         }else {
             Utils.showToastWithTitleAndContext(context,"No "+data.getTitle()+" Added");
         }
    }
}
