package com.SampleApp.row;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.SampleApp.row.Adapter.DependentAdapter;
import com.SampleApp.row.Data.DependentData;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddedDependentActivity extends AppCompatActivity {

    Context context;
    TextView txt_module,txt_title;
    RecyclerView rv_memberList;
    ImageView iv_add,iv_delete;
    String attendanceID="",moduleName="",count="",type="";
    ProgressDialog progressDialog;
    ArrayList<DependentData> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_dependent);
        context=this;
        txt_title = (TextView) findViewById(R.id.tv_title);

        txt_module=(TextView)findViewById(R.id.txt_module);
        iv_add = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_delete = (ImageView) findViewById(R.id.iv_actionbtn2);
        iv_add.setVisibility(View.GONE);
        iv_delete.setVisibility(View.GONE);

        rv_memberList=(RecyclerView)findViewById(R.id.rv_memberList);
        rv_memberList.setLayoutManager(new LinearLayoutManager(context));

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            attendanceID=bundle.getString("attendanceId");
            moduleName=bundle.getString("moduleName");
            count=bundle.getString("count");
            type=bundle.getString("type");

            txt_title.setText(moduleName);
            txt_module.setText(count+" "+moduleName);
            getDependent();

        }

    }

    private void getDependent(){
        if (InternetConnection.checkConnection(context)) {
            try {
                String url="";
                JSONObject requestObject=new JSONObject();

                requestObject.put("AttendanceID",attendanceID);
                requestObject.put("type",type);

                if(type.equalsIgnoreCase(Constant.Dependent.MEMBER)){
                    url=Constant.GetAttendanceMemberDetails;
                }else if(type.equalsIgnoreCase(Constant.Dependent.ANNS)){
                    url=Constant.GetAttendanceAnnsDetails;
                }else if(type.equalsIgnoreCase(Constant.Dependent.ANNETS)){
                    url=Constant.GetAttendanceAnnetsDetails;
                }else if(type.equalsIgnoreCase(Constant.Dependent.VISITORS)){
                    url=Constant.GetAttendanceVisitorsDetails;
                }else if(type.equalsIgnoreCase(Constant.Dependent.ROTARIAN)){
                    url=Constant.GetAttendanceRotariansDetails;
                }else if(type.equalsIgnoreCase(Constant.Dependent.DELEGETS)){
                    url=Constant.GetAttendanceDistrictDeleagateDetails;
                }
                Utils.log(url+" / "+requestObject.toString());

                showDialog();

                JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, requestObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Utils.log(""+response);
                        setAttendance(response);
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

    private void setAttendance(JSONObject response){

        try {
            list.clear();
            if(type.equalsIgnoreCase(Constant.Dependent.MEMBER)){
                JSONObject TBAttendanceMemberDetailsResult=response.getJSONObject("TBAttendanceMemberDetailsResult");
                String status=TBAttendanceMemberDetailsResult.getString("status");
                if(status.equalsIgnoreCase("0")){
                    JSONArray AttendanceMemberResult=TBAttendanceMemberDetailsResult.getJSONArray("AttendanceMemberResult");
                    if(AttendanceMemberResult!=null && AttendanceMemberResult.length()>0){
                        for(int i=0;i<AttendanceMemberResult.length();i++){
                            JSONObject object=AttendanceMemberResult.getJSONObject(i);
                            DependentData data=new DependentData();
                            data.setMemberID(object.getString("FK_MemberID"));
                            data.setMemberName(object.getString("MemberName"));
                            data.setMemberDesignation("");
                            data.setPic(object.getString("image"));
                            list.add(data);
                            DependentAdapter adapter=new DependentAdapter(context,list,type);
                            rv_memberList.setAdapter(adapter);
                        }
                    }else {
                        Utils.showToastWithTitleAndContext(context,getString(R.string.msg_no_records_found));

                    }
                }else {
                    Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                }
            }else if(type.equalsIgnoreCase(Constant.Dependent.ANNS)){
                JSONObject TBAttendanceAnnsDetailsResult=response.getJSONObject("TBAttendanceAnnsDetailsResult");
                String status=TBAttendanceAnnsDetailsResult.getString("status");
                if(status.equalsIgnoreCase("0")){
                    JSONArray AttendanceAnnsResult=TBAttendanceAnnsDetailsResult.getJSONArray("AttendanceAnnsResult");
                    if(AttendanceAnnsResult!=null && AttendanceAnnsResult.length()>0){
                        for(int i=0;i<AttendanceAnnsResult.length();i++){
                            JSONObject object=AttendanceAnnsResult.getJSONObject(i);
                            DependentData data=new DependentData();
                            data.setAnnsName(object.getString("AnnsName"));
                            if(data.getAnnsName()!=null && !data.getAnnsName().isEmpty()){
                                list.add(data);
                            }

                        }

                        DependentAdapter adapter=new DependentAdapter(context,list,type);
                        rv_memberList.setAdapter(adapter);
                    }else {
                        Utils.showToastWithTitleAndContext(context,getString(R.string.msg_no_records_found));

                    }
                }else {
                    Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                }
            }else if(type.equalsIgnoreCase(Constant.Dependent.ANNETS)){
                JSONObject TBAttendanceAnnetsDetailsResult=response.getJSONObject("TBAttendanceAnnetsDetailsResult");
                String status=TBAttendanceAnnetsDetailsResult.getString("status");
                if(status.equalsIgnoreCase("0")){
                    JSONArray AttendanceAnnetsResult=TBAttendanceAnnetsDetailsResult.getJSONArray("AttendanceAnnetsResult");
                    if(AttendanceAnnetsResult!=null && AttendanceAnnetsResult.length()>0){
                        for(int i=0;i<AttendanceAnnetsResult.length();i++){
                            JSONObject object=AttendanceAnnetsResult.getJSONObject(i);
                            DependentData data=new DependentData();
                            data.setAnnetsName(object.getString("AnnetsName"));
                            if(data.getAnnetsName()!=null && !data.getAnnetsName().isEmpty()){
                                list.add(data);
                            }

                        }

                        DependentAdapter adapter=new DependentAdapter(context,list,type);
                        rv_memberList.setAdapter(adapter);
                    }else {
                        Utils.showToastWithTitleAndContext(context,getString(R.string.msg_no_records_found));

                    }
                }else {
                    Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                }
            }else if(type.equalsIgnoreCase(Constant.Dependent.VISITORS)){
                JSONObject TBAttendanceVisitorsDetailsResult=response.getJSONObject("TBAttendanceVisitorsDetailsResult");
                String status=TBAttendanceVisitorsDetailsResult.getString("status");
                if(status.equalsIgnoreCase("0")){
                    JSONArray AttendanceVisitorsResult=TBAttendanceVisitorsDetailsResult.getJSONArray("AttendanceVisitorsResult");
                    if(AttendanceVisitorsResult!=null && AttendanceVisitorsResult.length()>0){
                        for(int i=0;i<AttendanceVisitorsResult.length();i++){
                            JSONObject object=AttendanceVisitorsResult.getJSONObject(i);
                            DependentData data=new DependentData();
                            data.setVisitorName(object.getString("VisitorsName"));
                            data.setBrought(object.getString("Rotarian_whohas_Brought"));
                            if(data.getVisitorName()!=null && !data.getVisitorName().isEmpty()){
                                list.add(data);
                            }

                        }

                        DependentAdapter adapter=new DependentAdapter(context,list,type);
                        rv_memberList.setAdapter(adapter);
                    }else {
                        Utils.showToastWithTitleAndContext(context,getString(R.string.msg_no_records_found));

                    }
                }else {
                    Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                }
            }else if(type.equalsIgnoreCase(Constant.Dependent.ROTARIAN)){
                JSONObject TBAttendanceRotariansDetailsResult=response.getJSONObject("TBAttendanceRotariansDetailsResult");
                String status=TBAttendanceRotariansDetailsResult.getString("status");
                if(status.equalsIgnoreCase("0")){
                    JSONArray AttendanceRotariansResult=TBAttendanceRotariansDetailsResult.getJSONArray("AttendanceRotariansResult");
                    if(AttendanceRotariansResult!=null && AttendanceRotariansResult.length()>0){

                        for(int i=0;i<AttendanceRotariansResult.length();i++){
                            JSONObject object=AttendanceRotariansResult.getJSONObject(i);
                            DependentData data=new DependentData();
                            data.setRotarianID(object.getString("RotarianID"));
                            data.setRotarianName(object.getString("RotarianName"));
                            data.setClubName(object.getString("ClubName"));
                            if(data.getRotarianName()!=null && !data.getRotarianName().isEmpty()){
                                list.add(data);
                            }
                        }

                        DependentAdapter adapter=new DependentAdapter(context,list,type);
                        rv_memberList.setAdapter(adapter);
                    }else {
                        Utils.showToastWithTitleAndContext(context,getString(R.string.msg_no_records_found));

                    }
                }else {
                    Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                }
            }else if(type.equalsIgnoreCase(Constant.Dependent.DELEGETS)){
                JSONObject TBAttendanceDistrictDeleagateDetailsResult=response.getJSONObject("TBAttendanceDistrictDeleagateDetailsResult");
                String status=TBAttendanceDistrictDeleagateDetailsResult.getString("status");
                if(status.equalsIgnoreCase("0")){
                    JSONArray AttendanceDistrictDeleagateResult=TBAttendanceDistrictDeleagateDetailsResult.getJSONArray("AttendanceDistrictDeleagateResult");
                    if(AttendanceDistrictDeleagateResult!=null && AttendanceDistrictDeleagateResult.length()>0){

                        for(int i=0;i<AttendanceDistrictDeleagateResult.length();i++){
                            JSONObject object=AttendanceDistrictDeleagateResult.getJSONObject(i);
                            DependentData data=new DependentData();
                            data.setDistrictDesignation(object.getString("DistrictDesignation"));
                            data.setRotarianName(object.getString("RotarianName"));
                            data.setClubName(object.getString("ClubName"));
                            if(data.getRotarianName()!=null && !data.getRotarianName().isEmpty()){
                                list.add(data);
                            }
                        }

                        DependentAdapter adapter=new DependentAdapter(context,list,type);
                        rv_memberList.setAdapter(adapter);
                    }else {
                        Utils.showToastWithTitleAndContext(context,getString(R.string.msg_no_records_found));

                    }
                }else {
                    Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                }
            }

            hideDialog();

        } catch (JSONException e) {
            hideDialog();
            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
            Utils.log("VollyError:- " + e.getMessage());
            e.printStackTrace();
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

}
