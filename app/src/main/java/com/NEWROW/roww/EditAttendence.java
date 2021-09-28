package com.NEWROW.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Data.AttendanceData;
import com.NEWROW.row.Data.DependentData;
import com.NEWROW.row.Data.DirectoryData;
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
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EditAttendence extends AppCompatActivity {

    TextView tv_title,txt_add,txt_cancel,txt_memberCount,txt_annsCount,txt_annetsCount,txt_visitorsCount,txt_rotarianCount,txt_delegatesCount;
    String title;
    EditText et_eventName,et_desc,et_eventDate,et_eventTime;
    LinearLayout ll_members,ll_anns,ll_Annets,ll_visitors,ll_rotarians,ll_districtDelegates;
    String attendance_id="0",event_id="0",grpID="0",memberProfileID="0",type="";
    Context context;
    SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat ampm=new SimpleDateFormat("dd MMMM yyyy hh:mm:ss a");
    SimpleDateFormat sdf=new SimpleDateFormat("dd MMM yyyy HH:mm");
    SingleDateAndTimePickerDialog datetimePicker;
    ProgressDialog progressDialog;
    final int MEMBER_REQ=1,ANNS_REQ=2,ANNETS_REQ=3,VISITOR_REQ=4,ROTARIAN_REQ=5,DELEGATES_REQ=6;
    int memberCount=0,annsCount=0,annetsCount=0,visitorCount=0,rotarianCount=0,delegatesCount=0;
    ArrayList<DependentData> memberList=new ArrayList<>();
    ArrayList<DependentData> selectedList=new ArrayList<>();
    ArrayList<DependentData> selectedAnnsList=new ArrayList<>();
    ArrayList<DependentData> annsList=new ArrayList<>();
    ArrayList<DependentData> selectedAnnetsList=new ArrayList<>();
    ArrayList<DependentData> annetsList=new ArrayList<>();
    ArrayList<DependentData> selectedVisitorList=new ArrayList<>();
    ArrayList<DependentData> visitorList=new ArrayList<>();
    ArrayList<DependentData> selectedRotarianList=new ArrayList<>();
    ArrayList<DependentData> rotarianList=new ArrayList<>();
    ArrayList<DependentData> selectedDelegatesList=new ArrayList<>();
    ArrayList<DependentData> delegatesList=new ArrayList<>();
    ArrayList<DependentData> deletedList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_edit_attendence);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        context=this;

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("Attendance");

        grpID = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID);
        memberProfileID = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID);

        init();

        Bundle b = getIntent().getExtras();

        if(b!=null){

            Intent i = getIntent();

            if(i.hasExtra("id")){
                attendance_id = i.getStringExtra("id");
                txt_add.setText("UPDATE");
                //et_eventDate.setEnabled(false);
                getAttendanceDetails();
            } else if(i.hasExtra("eventId")){
                event_id=i.getStringExtra("eventId");
               // et_eventDate.setEnabled(false);
                getEventDetails();
            }
        }

    }

    private void init() {
        et_eventName = (EditText)findViewById(R.id.et_eventName);
        et_desc = (EditText)findViewById(R.id.et_desc);
        et_eventDate = (EditText)findViewById(R.id.et_eventDate);
        et_eventTime = (EditText)findViewById(R.id.et_eventTime);

        ll_members = (LinearLayout) findViewById(R.id.ll_members);
        ll_anns = (LinearLayout) findViewById(R.id.ll_anns);
        ll_Annets = (LinearLayout) findViewById(R.id.ll_Annets);
        ll_visitors = (LinearLayout) findViewById(R.id.ll_visitors);
        ll_rotarians = (LinearLayout) findViewById(R.id.ll_rotarians);
        ll_districtDelegates = (LinearLayout) findViewById(R.id.ll_districtDelegates);

        txt_add = (TextView) findViewById(R.id.txt_submit);
        txt_cancel = (TextView) findViewById(R.id.txt_cancel);

        txt_memberCount = (TextView) findViewById(R.id.memberCount);
        txt_annsCount = (TextView) findViewById(R.id.annsCount);
        txt_annetsCount = (TextView) findViewById(R.id.annetsCount);
        txt_visitorsCount = (TextView) findViewById(R.id.visitorsCount);
        txt_rotarianCount = (TextView) findViewById(R.id.rotarianCount);
        txt_delegatesCount = (TextView) findViewById(R.id.delegatesCount);

        ll_members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberCount= Integer.parseInt(txt_memberCount.getText().toString());


                if(selectedList.size() <= 0 && memberCount>0){
                    type=Constant.Dependent.MEMBER;
                    getDependent();

                }else {
                    Intent i = new Intent(context, AddDependentMemberActivity.class);
                    i.putExtra("id", attendance_id);
                    i.putExtra("list",selectedList);
                    //i.putParcelableArrayListExtra("name1", memberData);
//                i.putParcelableArrayListExtra("selected_memberdata", listaddmemberdata);

//                i.putExtra("parentId", "0");
//                i.putExtra("subgroupname", "Sub Groups");
                    //i.putExtra("moduleName","Sub Groups");
                    startActivityForResult(i, MEMBER_REQ);
                }

            }
        });

        ll_anns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                annsCount= Integer.parseInt(txt_annsCount.getText().toString());
//                if(!attendance_id.equalsIgnoreCase("0") && annsCount>0){
//                    type=Constant.Dependent.ANNS;
//                    getDependent();
//
//                }else {
//                    Intent i = new Intent(context, AddDependent.class);
//                    i.putExtra("id", attendance_id);
//                    i.putExtra("list",selectedAnnsList);
//                    i.putExtra("title","Anns");
//                    i.putExtra("count",annsCount);
//                    i.putExtra("type",Constant.Dependent.ANNS);
//                    startActivityForResult(i, ANNS_REQ);
//                }

                if(selectedAnnsList.size()<=0 && annsCount>0){
                    type=Constant.Dependent.ANNS;
                    getDependent();

                }else {
                    Intent i = new Intent(context, AddDependent.class);
                    i.putExtra("id", attendance_id);
                    i.putExtra("list",selectedAnnsList);
                    i.putExtra("title","Anns");
                    i.putExtra("count",annsCount);
                    i.putExtra("type",Constant.Dependent.ANNS);
                    i.putExtra("deletedList",deletedList);
                    startActivityForResult(i, ANNS_REQ);
                }
            }
        });

        ll_Annets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                annetsCount= Integer.parseInt(txt_annetsCount.getText().toString());

                if(selectedAnnetsList.size()<=0 && annetsCount>0){
                    type=Constant.Dependent.ANNETS;
                    getDependent();

                }else {
                    Intent i = new Intent(context, AddDependent.class);
                    i.putExtra("id", attendance_id);
                    i.putExtra("list",selectedAnnetsList);
                    i.putExtra("title","Annets");
                    i.putExtra("count",annetsCount);
                    i.putExtra("type",Constant.Dependent.ANNETS);
                    i.putExtra("deletedList",deletedList);
                    startActivityForResult(i, ANNETS_REQ);
                }
            }
        });

        ll_visitors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visitorCount= Integer.parseInt(txt_visitorsCount.getText().toString());

                if(selectedVisitorList.size()<=0 && visitorCount>0){
                    type=Constant.Dependent.VISITORS;
                    getDependent();

                }else {
                    Intent i = new Intent(context, AddDependent.class);
                    i.putExtra("id", attendance_id);
                    i.putExtra("list",selectedVisitorList);
                    i.putExtra("title","Visitors");
                    i.putExtra("count",visitorCount);
                    i.putExtra("type",Constant.Dependent.VISITORS);
                    i.putExtra("deletedList",deletedList);
                    startActivityForResult(i, VISITOR_REQ);
                }
            }
        });

        ll_rotarians.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotarianCount= Integer.parseInt(txt_rotarianCount.getText().toString());

                if(selectedRotarianList.size()<=0 && rotarianCount>0){
                    type=Constant.Dependent.ROTARIAN;
                    getDependent();

                }else {
                    Intent i = new Intent(context, AddDependent.class);
                    i.putExtra("id", attendance_id);
                    i.putExtra("list",selectedRotarianList);
                    i.putExtra("title","Rotarian's (Other Club)");
                    i.putExtra("count",rotarianCount);
                    i.putExtra("type",Constant.Dependent.ROTARIAN);
                    i.putExtra("deletedList",deletedList);
                    startActivityForResult(i, ROTARIAN_REQ);
                }
            }
        });

        ll_districtDelegates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegatesCount= Integer.parseInt(txt_delegatesCount.getText().toString());

                if(selectedDelegatesList.size()<=0 && delegatesCount>0){
                    type=Constant.Dependent.DELEGETS;
                    getDependent();

                }else {
                    Intent i = new Intent(context, AddDependent.class);
                    i.putExtra("id", attendance_id);
                    i.putExtra("list",selectedDelegatesList);
                    i.putExtra("title","Delegates");
                    i.putExtra("count",delegatesCount);
                    i.putExtra("type",Constant.Dependent.DELEGETS);
                    i.putExtra("deletedList",deletedList);
                    startActivityForResult(i, DELEGATES_REQ);
                }
            }
        });

        txt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()){
                    addAttendance();
                }
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et_eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTimePicker(et_eventDate);
            }
        });

    }

    private boolean validation(){
        if(et_eventName.getText().toString().trim().isEmpty()){
            et_eventName.setError("Please enter event name");
            return false;
        }else {
            et_eventName.setError(null);
        }

        if(et_desc.getText().toString().trim().isEmpty()){
            et_desc.setError("Please enter event description");
            return false;
        }else {
            et_desc.setError(null);
        }

        if(et_eventDate.getText().toString().trim().isEmpty()){
            et_eventDate.setError("Please enter event date & time");
            Utils.showToastWithTitleAndContext(context,"Please enter event date & time");
            return false;
        }else {
            et_eventDate.setError(null);

        }

        return true;

    }

    private void getAttendanceDetails(){

        if (InternetConnection.checkConnection(context)) {

            try {

                JSONObject requestObject=new JSONObject();
//                requestObject.put("groupProfileID",memberProfileID);
//                requestObject.put("groupID",grpID);

                requestObject.put("AttendanceID",attendance_id);


                Utils.log(Constant.GetAttendanceDetails+" / "+requestObject.toString());

                showDialog();

                JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, Constant.GetAttendanceDetails, requestObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        setAttendanceDetails(response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        hideDialog();
                        Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                        Utils.log("VollyError:- " + error);
                        finish();
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
                        AttendanceData data=new AttendanceData();
                        attendance_id = AttendanceResult.getString("AttendanceID");
                        et_eventName.setText(AttendanceResult.getString("AttendanceName"));
                        String sdate=AttendanceResult.getString("AttendanceDate")+" "+AttendanceResult.getString("Attendancetime");
                        Date date;
                        try {
                            date=ampm.parse(sdate);
                            et_eventDate.setText(sdf.format(date));
                        } catch (ParseException e) {
                            et_eventDate.setText(sdate);
                            e.printStackTrace();
                        }

                        //et_eventTime.setText(AttendanceResult.getString("Attendancetime"));
                        et_desc.setText(AttendanceResult.getString("AttendanceDesc"));

                        txt_memberCount.setText(AttendanceResult.getString("MemberCount"));
                        txt_annsCount.setText(AttendanceResult.getString("AnnsCount"));
                        txt_annetsCount.setText(AttendanceResult.getString("AnnetsCount"));
                        txt_visitorsCount.setText(AttendanceResult.getString("VisitorsCount"));
                        txt_rotarianCount.setText(AttendanceResult.getString("RotarianCount"));
                        txt_delegatesCount.setText(AttendanceResult.getString("DistrictDelegatesCount"));
                    }

                    getMember();

                }else {
                    hideDialog();
                    Utils.showToastWithTitleAndContext(context,getString(R.string.msg_no_records_found));
                    finish();
                }
            }else {
                hideDialog();
                Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                finish();
            }


        } catch (JSONException e) {
            hideDialog();
            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
            Utils.log("VollyError:- " + e.getMessage());
            e.printStackTrace();
            finish();
        }
    }

    private void getEventDetails() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("grpId", grpID));//PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)
        arrayList.add(new BasicNameValuePair("eventID", event_id));//eventid
        arrayList.add(new BasicNameValuePair("groupProfileID", memberProfileID));//PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)
      
        Log.d("Response", "PARAMETERS " + Constant.GetEventDetails + " :- " + arrayList.toString());
        if ( InternetConnection.checkConnection(getApplicationContext())) {
            new WebConnectionEventDetail(Constant.GetEventDetails, arrayList, context).execute();
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    public class WebConnectionEventDetail extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.TBProgressBar);

        String url = null;
        List<NameValuePair> argList = null;

        public WebConnectionEventDetail(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //	dialog.show();


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
                getEventDetailsItems(result.toString());

            } else {
                Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getEventDetailsItems(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("EventsListDetailResult");
            final String status = EventResult.getString("status");
            if (status.equals("0"))
            {
                JSONArray EventListResdult = EventResult.getJSONArray("EventsDetailResult");
                for (int i = 0; i < EventListResdult.length(); i++) {

                    JSONObject object = EventListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("EventsDetail");

                    et_eventName.setText(objects.getString("eventTitle").toString());
                    et_desc.setText(objects.getString("eventDesc").toString());

                    et_eventDate.setText(objects.getString("eventDateTime").toString());

                }

            }else{
                Utils.showToastWithTitleAndContext(getApplicationContext(), getString(R.string.msgRetry));
                finish();
            }

        } catch (Exception e) {
            Utils.showToastWithTitleAndContext(getApplicationContext(), getString(R.string.msgRetry));
            Log.d("exec", "Exception :- " + e.toString());
            finish();
        }
    }

    public void ShowTimePicker(final EditText tv_time) {

        Utils.log(et_eventDate.getText().toString());
        String title="";
        Date date=new Date();
        try {
            title="Event Date & Time";
            if(!et_eventDate.getText().toString().isEmpty()){
                date = sdf.parse(et_eventDate.getText().toString());
            }

        }
        catch (ParseException e){
            date=new Date();
        }

//

        datetimePicker= new SingleDateAndTimePickerDialog.Builder(context).build();
//                .bottomSheet()
//                .curved()
        datetimePicker.setDefaultDate(date);

        // datetimePicker.setMinDateRange(new Date());
        datetimePicker .setMinutesStep(1);
        //.displayHours(false)
        //.displayMinutes(false)

        //.todayText("aujourd'hui")

        datetimePicker.setTitle(title);
        datetimePicker.setListener(new SingleDateAndTimePickerDialog.Listener() {
            @Override
            public void onDateSelected(Date date) {

                tv_time.setTag(sdf1.format(date));
                tv_time.setText(sdf.format(date));
                tv_time.setError(null);
            }
        }).display();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if(datetimePicker!=null && datetimePicker.isDisplaying()){
            datetimePicker.dismiss();
        }else {
            super.onBackPressed();
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

    private void getDependent(){
        if (InternetConnection.checkConnection(context)) {
            try {

                String url="";
                JSONObject requestObject=new JSONObject();
                requestObject.put("AttendanceID",attendance_id);
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
                hideDialog();
            }
        }else {
            Utils.showToastWithTitleAndContext(context,getString(R.string.noInternet));
        }
    }

    private void setAttendance(JSONObject response){

        try {

            hideDialog();

            if(type.equalsIgnoreCase(Constant.Dependent.MEMBER)){
                memberList.clear();
                JSONObject TBAttendanceMemberDetailsResult=response.getJSONObject("TBAttendanceMemberDetailsResult");
                String status=TBAttendanceMemberDetailsResult.getString("status");
                if(status.equalsIgnoreCase("0")){
                    if(TBAttendanceMemberDetailsResult.has("AttendanceMemberResult")){
                        JSONArray AttendanceMemberResult=TBAttendanceMemberDetailsResult.getJSONArray("AttendanceMemberResult");
                        if(AttendanceMemberResult!=null && AttendanceMemberResult.length()>0){
                            for(int i=0;i<AttendanceMemberResult.length();i++){
                                JSONObject object=AttendanceMemberResult.getJSONObject(i);
                                DependentData data=new DependentData();
                                data.setMemberID(object.getString("FK_MemberID"));
                                memberList.add(data);
                            }

                        }
                    }

                    selectedList.addAll(memberList);
                    Intent i = new Intent(context, AddDependentMemberActivity.class);
                    i.putExtra("id", attendance_id);
                    i.putExtra("list",selectedList);
                    //i.putParcelableArrayListExtra("name1", memberData);
//                i.putParcelableArrayListExtra("selected_memberdata", listaddmemberdata);

//                i.putExtra("parentId", "0");
//                i.putExtra("subgroupname", "Sub Groups");
                    //i.putExtra("moduleName","Sub Groups");
                    startActivityForResult(i, MEMBER_REQ);

                }else {
                    Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                }
            }else if(type.equalsIgnoreCase(Constant.Dependent.ANNS)){
                annsList.clear();
                JSONObject TBAttendanceAnnsDetailsResult=response.getJSONObject("TBAttendanceAnnsDetailsResult");
                String status=TBAttendanceAnnsDetailsResult.getString("status");
                if(status.equalsIgnoreCase("0")){
                    JSONArray AttendanceAnnsResult=TBAttendanceAnnsDetailsResult.getJSONArray("AttendanceAnnsResult");
                    if(AttendanceAnnsResult!=null && AttendanceAnnsResult.length()>0){
                        for(int i=0;i<AttendanceAnnsResult.length();i++){
                            JSONObject object=AttendanceAnnsResult.getJSONObject(i);
                            DependentData data=new DependentData();
                            data.setAnnsName(object.getString("AnnsName"));
                            data.setTitle("Anns");
                            annsList.add(data);
                        }

                        selectedAnnsList.addAll(annsList);

                        Intent i = new Intent(context, AddDependent.class);
                        i.putExtra("id", attendance_id);
                        i.putExtra("list",selectedAnnsList);
                        i.putExtra("title","Anns");
                        i.putExtra("count",annsCount);
                        i.putExtra("type",Constant.Dependent.ANNS);
                        startActivityForResult(i, ANNS_REQ);

                    }else {
                        Utils.showToastWithTitleAndContext(context,getString(R.string.msg_no_records_found));

                    }
                }else {
                    Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                }
            }else if(type.equalsIgnoreCase(Constant.Dependent.ANNETS)){
                annetsList.clear();

                JSONObject TBAttendanceAnnetsDetailsResult=response.getJSONObject("TBAttendanceAnnetsDetailsResult");
                String status=TBAttendanceAnnetsDetailsResult.getString("status");
                if(status.equalsIgnoreCase("0")){
                    JSONArray AttendanceAnnetsResult=TBAttendanceAnnetsDetailsResult.getJSONArray("AttendanceAnnetsResult");
                    if(AttendanceAnnetsResult!=null && AttendanceAnnetsResult.length()>0){
                        for(int i=0;i<AttendanceAnnetsResult.length();i++){
                            JSONObject object=AttendanceAnnetsResult.getJSONObject(i);
                            DependentData data=new DependentData();
                            data.setAnnetsName(object.getString("AnnetsName"));
                            data.setTitle("Annets");
                            annetsList.add(data);
                        }

                        selectedAnnetsList.addAll(annetsList);

                        Intent i = new Intent(context, AddDependent.class);
                        i.putExtra("id", attendance_id);
                        i.putExtra("list",selectedAnnetsList);
                        i.putExtra("title","Annets");
                        i.putExtra("count",annetsCount);
                        i.putExtra("type",Constant.Dependent.ANNETS);
                        startActivityForResult(i, ANNETS_REQ);
                    }else {
                        Utils.showToastWithTitleAndContext(context,getString(R.string.msg_no_records_found));

                    }
                }else {
                    Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                }
            }else if(type.equalsIgnoreCase(Constant.Dependent.VISITORS)){
                visitorList.clear();
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
                            data.setTitle("Visitor");
                            visitorList.add(data);
                        }

                        selectedVisitorList.addAll(visitorList);

                        Intent i = new Intent(context, AddDependent.class);
                        i.putExtra("id", attendance_id);
                        i.putExtra("list",selectedVisitorList);
                        i.putExtra("title","Visitors");
                        i.putExtra("count",visitorCount);
                        i.putExtra("type",Constant.Dependent.VISITORS);
                        startActivityForResult(i, VISITOR_REQ);

                    }else {
                        Utils.showToastWithTitleAndContext(context,getString(R.string.msg_no_records_found));

                    }
                }else {
                    Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                }
            }else if(type.equalsIgnoreCase(Constant.Dependent.ROTARIAN)){
                rotarianList.clear();
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
                            data.setTitle("Rotarian");
                            rotarianList.add(data);
                        }

                        selectedRotarianList.addAll(rotarianList);

                        Intent i = new Intent(context, AddDependent.class);
                        i.putExtra("id", attendance_id);
                        i.putExtra("list",selectedRotarianList);
                        i.putExtra("title","Rotarian");
                        i.putExtra("count",rotarianCount);
                        i.putExtra("type",Constant.Dependent.ROTARIAN);
                        startActivityForResult(i, ROTARIAN_REQ);

                    }else {
                        Utils.showToastWithTitleAndContext(context,getString(R.string.msg_no_records_found));

                    }
                }else {
                    Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                }
            }else if(type.equalsIgnoreCase(Constant.Dependent.DELEGETS)){
                delegatesList.clear();
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
                            data.setTitle("Delegates");
                            delegatesList.add(data);
                        }
                        selectedDelegatesList.addAll(delegatesList);

                        Intent i = new Intent(context, AddDependent.class);
                        i.putExtra("id", attendance_id);
                        i.putExtra("list",selectedDelegatesList);
                        i.putExtra("title","Delegates");
                        i.putExtra("count",delegatesCount);
                        i.putExtra("type",Constant.Dependent.DELEGETS);
                        startActivityForResult(i, DELEGATES_REQ);

                    }else {
                        Utils.showToastWithTitleAndContext(context,getString(R.string.msg_no_records_found));

                    }
                }else {
                    Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                }
            }


        } catch (JSONException e) {
            hideDialog();
            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
            Utils.log("VollyError:- " + e.getMessage());
            e.printStackTrace();
        }

    }

    private void getMember(){
        if (InternetConnection.checkConnection(context)) {
            memberCount= Integer.parseInt(txt_memberCount.getText().toString());
            if(memberCount>0){
                try {
                    JSONObject requestObject=new JSONObject();
                    requestObject.put("AttendanceID",attendance_id);
                    requestObject.put("type",Constant.Dependent.MEMBER);


                    Utils.log(Constant.GetAttendanceMemberDetails+" / "+requestObject.toString());

//                    showDialog();

                    JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, Constant.GetAttendanceMemberDetails, requestObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Utils.log(""+response);
                            setMember(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            hideDialog();
                            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                            Utils.log("VollyError:- " + error);
                            finish();
                        }
                    });

                    AppController.getInstance().addToRequestQueue(context, request);


                }catch (Exception e){
                    hideDialog();
                }
            }else {
                getAnns();
            }

        }else {
            hideDialog();
            Utils.showToastWithTitleAndContext(context,getString(R.string.noInternet));
            finish();
        }
    }

    private void setMember(JSONObject response){

        try {

            selectedList.clear();
            JSONObject TBAttendanceMemberDetailsResult=response.getJSONObject("TBAttendanceMemberDetailsResult");
            String status=TBAttendanceMemberDetailsResult.getString("status");
            if(status.equalsIgnoreCase("0")){
                if(TBAttendanceMemberDetailsResult.has("AttendanceMemberResult")){
                    JSONArray AttendanceMemberResult=TBAttendanceMemberDetailsResult.getJSONArray("AttendanceMemberResult");
                    if(AttendanceMemberResult!=null && AttendanceMemberResult.length()>0){
                        for(int i=0;i<AttendanceMemberResult.length();i++){
                            JSONObject object=AttendanceMemberResult.getJSONObject(i);
                            DependentData data=new DependentData();
                            data.setType(Constant.Dependent.MEMBER);
                            data.setMemberID(object.getString("FK_MemberID"));
                            selectedList.add(data);
                        }

                    }
                }

                getAnns();

            }else {
                hideDialog();
                Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                finish();
            }


        } catch (JSONException e) {
            hideDialog();
            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
            Utils.log("VollyError:- " + e.getMessage());
            e.printStackTrace();
            finish();
        }

    }

    private void getAnns(){
        if (InternetConnection.checkConnection(context)) {
            annsCount= Integer.parseInt(txt_annsCount.getText().toString());

            if(annsCount>0){
                try {
                    JSONObject requestObject=new JSONObject();
                    requestObject.put("AttendanceID",attendance_id);
                    requestObject.put("type",Constant.Dependent.ANNS);


                    Utils.log(Constant.GetAttendanceAnnsDetails+" / "+requestObject.toString());


                    JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, Constant.GetAttendanceAnnsDetails, requestObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Utils.log(""+response);
                            setAnns(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            hideDialog();
                            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                            Utils.log("VollyError:- " + error);
                            finish();
                        }
                    });

                    AppController.getInstance().addToRequestQueue(context, request);


                }catch (Exception e){
                    hideDialog();
                }
            }else {
                getAnnets();
            }

        }else {
            hideDialog();
            Utils.showToastWithTitleAndContext(context,getString(R.string.noInternet));
            finish();
        }
    }

    private void setAnns(JSONObject response){

        try {



            selectedAnnsList.clear();
            JSONObject TBAttendanceAnnsDetailsResult=response.getJSONObject("TBAttendanceAnnsDetailsResult");
            String status=TBAttendanceAnnsDetailsResult.getString("status");
            if(status.equalsIgnoreCase("0")){
                JSONArray AttendanceAnnsResult=TBAttendanceAnnsDetailsResult.getJSONArray("AttendanceAnnsResult");
                if(AttendanceAnnsResult!=null && AttendanceAnnsResult.length()>0){
                    for(int i=0;i<AttendanceAnnsResult.length();i++){
                        JSONObject object=AttendanceAnnsResult.getJSONObject(i);
                        DependentData data=new DependentData();
                        data.setType(Constant.Dependent.ANNS);
                        data.setMemberID(object.getString("PK_AttendanceAnnsID"));
                        data.setAnnsName(object.getString("AnnsName"));
                        data.setTitle("Anns");
                        selectedAnnsList.add(data);
                    }


                }else {

                    Utils.showToastWithTitleAndContext(context,getString(R.string.msg_no_records_found));
                }

                getAnnets();
            }else {
                hideDialog();
                Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                finish();
            }


        } catch (JSONException e) {
            hideDialog();
            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
            Utils.log("VollyError:- " + e.getMessage());
            e.printStackTrace();
            finish();
        }

    }

    private void getAnnets(){
        if (InternetConnection.checkConnection(context)) {

            annetsCount= Integer.parseInt(txt_annetsCount.getText().toString());
            if(annetsCount>0){
                try {


                    JSONObject requestObject=new JSONObject();
                    requestObject.put("AttendanceID",attendance_id);
                    requestObject.put("type",Constant.Dependent.ANNETS);


                    Utils.log(Constant.GetAttendanceAnnetsDetails+" / "+requestObject.toString());
//
//                    showDialog();

                    JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, Constant.GetAttendanceAnnetsDetails, requestObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Utils.log(""+response);
                            setAnnets(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            hideDialog();
                            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                            Utils.log("VollyError:- " + error);
                            finish();
                        }
                    });

                    AppController.getInstance().addToRequestQueue(context, request);


                }catch (Exception e){
                    hideDialog();
                }
            }else {
                getVisitor();
            }

        }else {
            hideDialog();
            Utils.showToastWithTitleAndContext(context,getString(R.string.noInternet));
            finish();
        }
    }

    private void setAnnets(JSONObject response){

        try {


            selectedAnnetsList.clear();

            JSONObject TBAttendanceAnnetsDetailsResult=response.getJSONObject("TBAttendanceAnnetsDetailsResult");
            String status=TBAttendanceAnnetsDetailsResult.getString("status");
            if(status.equalsIgnoreCase("0")){
                JSONArray AttendanceAnnetsResult=TBAttendanceAnnetsDetailsResult.getJSONArray("AttendanceAnnetsResult");
                if(AttendanceAnnetsResult!=null && AttendanceAnnetsResult.length()>0){
                    for(int i=0;i<AttendanceAnnetsResult.length();i++){
                        JSONObject object=AttendanceAnnetsResult.getJSONObject(i);
                        DependentData data=new DependentData();
                        data.setType(Constant.Dependent.ANNETS);
                        data.setMemberID(object.getString("PK_AttendanceAnnetsID"));
                        data.setAnnetsName(object.getString("AnnetsName"));
                        data.setTitle("Annets");
                        selectedAnnetsList.add(data);
                    }


                }else {
                    Utils.showToastWithTitleAndContext(context,getString(R.string.msg_no_records_found));

                }

                getVisitor();
            }else {
                hideDialog();
                Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                finish();
            }


        } catch (JSONException e) {
            hideDialog();
            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
            Utils.log("VollyError:- " + e.getMessage());
            e.printStackTrace();
            finish();
        }

    }

    private void getVisitor(){
        if (InternetConnection.checkConnection(context)) {

            visitorCount= Integer.parseInt(txt_visitorsCount.getText().toString());
            if(visitorCount>0){
                try {


                    JSONObject requestObject=new JSONObject();
                    requestObject.put("AttendanceID",attendance_id);
                    requestObject.put("type",Constant.Dependent.VISITORS);


                    Utils.log(Constant.GetAttendanceVisitorsDetails+" / "+requestObject.toString());

//                    showDialog();

                    JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, Constant.GetAttendanceVisitorsDetails, requestObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Utils.log(""+response);
                            setVisitor(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            hideDialog();
                            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                            Utils.log("VollyError:- " + error);
                            finish();
                        }
                    });

                    AppController.getInstance().addToRequestQueue(context, request);


                }catch (Exception e){
                    hideDialog();
                }
            }else {
                getRotarian();
            }

        }else {
            hideDialog();
            Utils.showToastWithTitleAndContext(context,getString(R.string.noInternet));
            finish();
        }
    }

    private void setVisitor(JSONObject response){

        try {


            selectedVisitorList.clear();
            JSONObject TBAttendanceVisitorsDetailsResult=response.getJSONObject("TBAttendanceVisitorsDetailsResult");
            String status=TBAttendanceVisitorsDetailsResult.getString("status");
            if(status.equalsIgnoreCase("0")){
                JSONArray AttendanceVisitorsResult=TBAttendanceVisitorsDetailsResult.getJSONArray("AttendanceVisitorsResult");
                if(AttendanceVisitorsResult!=null && AttendanceVisitorsResult.length()>0){
                    for(int i=0;i<AttendanceVisitorsResult.length();i++){
                        JSONObject object=AttendanceVisitorsResult.getJSONObject(i);
                        DependentData data=new DependentData();
                        data.setType(Constant.Dependent.VISITORS);
                        data.setMemberID(object.getString("PK_AttendanceVisitorID"));
                        data.setVisitorName(object.getString("VisitorsName"));
                        data.setBrought(object.getString("Rotarian_whohas_Brought"));
                        data.setTitle("Visitor");
                        selectedVisitorList.add(data);
                    }


                }else {
                    Utils.showToastWithTitleAndContext(context,getString(R.string.msg_no_records_found));

                }
                getRotarian();
            }else {
                hideDialog();
                Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                finish();
            }

        } catch (JSONException e) {
            hideDialog();
            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
            Utils.log("VollyError:- " + e.getMessage());
            e.printStackTrace();
            finish();
        }

    }

    private void getRotarian(){
        if (InternetConnection.checkConnection(context)) {

            rotarianCount= Integer.parseInt(txt_rotarianCount.getText().toString());
            if (rotarianCount > 0) {

                try {

                    JSONObject requestObject=new JSONObject();
                    requestObject.put("AttendanceID",attendance_id);
                    requestObject.put("type",Constant.Dependent.ROTARIAN);


                    Utils.log(Constant.GetAttendanceRotariansDetails+" / "+requestObject.toString());

//                    showDialog();

                    JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, Constant.GetAttendanceRotariansDetails, requestObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Utils.log(""+response);
                            setRotarian(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            hideDialog();
                            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                            Utils.log("VollyError:- " + error);
                            finish();
                        }
                    });

                    AppController.getInstance().addToRequestQueue(context, request);


                }catch (Exception e){
                    hideDialog();
                }
            }else {
                getDelegates();
            }

        }else {
            hideDialog();
            Utils.showToastWithTitleAndContext(context,getString(R.string.noInternet));
            finish();
        }
    }

    private void setRotarian(JSONObject response){

        try {

//            hideDialog();

            selectedRotarianList.clear();
            JSONObject TBAttendanceRotariansDetailsResult=response.getJSONObject("TBAttendanceRotariansDetailsResult");
            String status=TBAttendanceRotariansDetailsResult.getString("status");
            if(status.equalsIgnoreCase("0")){
                JSONArray AttendanceRotariansResult=TBAttendanceRotariansDetailsResult.getJSONArray("AttendanceRotariansResult");
                if(AttendanceRotariansResult!=null && AttendanceRotariansResult.length()>0){

                    for(int i=0;i<AttendanceRotariansResult.length();i++){
                        JSONObject object=AttendanceRotariansResult.getJSONObject(i);
                        DependentData data=new DependentData();
                        data.setType(Constant.Dependent.ROTARIAN);
                        data.setMemberID(object.getString("PK_AttendanceRotarianID"));
                        data.setRotarianID(object.getString("RotarianID"));
                        data.setRotarianName(object.getString("RotarianName"));
                        data.setClubName(object.getString("ClubName"));
                        data.setTitle("Rotarian");
                        selectedRotarianList.add(data);
                    }



                }else {
                    Utils.showToastWithTitleAndContext(context,getString(R.string.msg_no_records_found));

                }
                getDelegates();
            }else {
                hideDialog();
                Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                finish();
            }

        } catch (JSONException e) {
            hideDialog();
            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
            Utils.log("VollyError:- " + e.getMessage());
            e.printStackTrace();
            finish();
        }

    }

    private void getDelegates(){
        if (InternetConnection.checkConnection(context)) {

            delegatesCount= Integer.parseInt(txt_delegatesCount.getText().toString());

            if(delegatesCount>0){
                try {


                    JSONObject requestObject=new JSONObject();
                    requestObject.put("AttendanceID",attendance_id);
                    requestObject.put("type",Constant.Dependent.DELEGETS);


                    Utils.log(Constant.GetAttendanceDistrictDeleagateDetails+" / "+requestObject.toString());

//                    showDialog();

                    JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, Constant.GetAttendanceDistrictDeleagateDetails, requestObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Utils.log(""+response);
                            setDelegates(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            hideDialog();
                            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                            Utils.log("VollyError:- " + error);
                            finish();
                        }
                    });

                    AppController.getInstance().addToRequestQueue(context, request);


                }catch (Exception e){
                    hideDialog();
                }
            }else {
                hideDialog();
            }

        }else {
            hideDialog();
            Utils.showToastWithTitleAndContext(context,getString(R.string.noInternet));
            finish();
        }
    }

    private void setDelegates(JSONObject response){

        try {

            hideDialog();

            selectedDelegatesList.clear();
            JSONObject TBAttendanceDistrictDeleagateDetailsResult=response.getJSONObject("TBAttendanceDistrictDeleagateDetailsResult");
            String status=TBAttendanceDistrictDeleagateDetailsResult.getString("status");
            if(status.equalsIgnoreCase("0")){
                JSONArray AttendanceDistrictDeleagateResult=TBAttendanceDistrictDeleagateDetailsResult.getJSONArray("AttendanceDistrictDeleagateResult");
                if(AttendanceDistrictDeleagateResult!=null && AttendanceDistrictDeleagateResult.length()>0){

                    for(int i=0;i<AttendanceDistrictDeleagateResult.length();i++){
                        JSONObject object=AttendanceDistrictDeleagateResult.getJSONObject(i);
                        DependentData data=new DependentData();
                        data.setType(Constant.Dependent.DELEGETS);
                        data.setMemberID(object.getString("PK_AttendanceDelegateID"));
                        data.setDistrictDesignation(object.getString("DistrictDesignation"));
                        data.setRotarianName(object.getString("RotarianName"));
                        data.setClubName(object.getString("ClubName"));
                        data.setTitle("Delegates");
                        selectedDelegatesList.add(data);
                    }


                }else {
                    Utils.showToastWithTitleAndContext(context,getString(R.string.msg_no_records_found));

                }
            }else {
                Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
                finish();
            }
        } catch (JSONException e) {
            hideDialog();
            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
            Utils.log("VollyError:- " + e.getMessage());
            e.printStackTrace();
            finish();
        }

    }

    private void addAttendance(){

        if (InternetConnection.checkConnection(context)) {

            try {

                JSONObject requestObject=new JSONObject();

                JSONObject AttendanceAddEdit_Input=new JSONObject();
                AttendanceAddEdit_Input.put("AttendanceID",attendance_id);
                AttendanceAddEdit_Input.put("AttendanceName",et_eventName.getText().toString());
                AttendanceAddEdit_Input.put("AttendanceDate",et_eventDate.getText().toString());
                AttendanceAddEdit_Input.put("AttendanceDesc",et_desc.getText().toString());
                AttendanceAddEdit_Input.put("Fk_group_id",grpID);
                AttendanceAddEdit_Input.put("fk_module_id",Constant.Module.ATTENDANCE);
                AttendanceAddEdit_Input.put("created_by",memberProfileID);
                AttendanceAddEdit_Input.put("modification_by",memberProfileID);
                AttendanceAddEdit_Input.put("deleted_by","0");
                AttendanceAddEdit_Input.put("FK_eventID",event_id);
                AttendanceAddEdit_Input.put("MemberCount",txt_memberCount.getText().toString());
                AttendanceAddEdit_Input.put("AnnsCount",txt_annsCount.getText().toString());
                AttendanceAddEdit_Input.put("AnnetsCount",txt_annetsCount.getText().toString());
                AttendanceAddEdit_Input.put("VisitorsCount",txt_visitorsCount.getText().toString());
                AttendanceAddEdit_Input.put("RotarianCount",txt_rotarianCount.getText().toString());
                AttendanceAddEdit_Input.put("DistrictDelegatesCount",txt_delegatesCount.getText().toString());

                requestObject.put("AttendanceAddEdit_Input",AttendanceAddEdit_Input);

                JSONArray newMembersArray=new JSONArray();
                JSONArray deletedMembers =new JSONArray();

                for(DependentData data : selectedList){

                    if (data.getEdited()) {

                        JSONObject object=new JSONObject();
                        object.put("FK_MemberID",data.getMemberID());
                        newMembersArray.put(object);
                    }

                }


                //Anns
                JSONArray newMemberAnns =new JSONArray();
                JSONArray updatedAnns =new JSONArray();
                JSONArray deletedAnns =new JSONArray();

                for(DependentData data : selectedAnnsList){

                    if(data.getEdited()){
                        JSONObject object=new JSONObject();
                        object.put("PK_AttendanceAnnsID",data.getMemberID());
                        object.put("AnnsName",data.getAnnsName());

                        if(!data.getMemberID().equalsIgnoreCase("0")){
                            updatedAnns.put(object);
                        }else {
                            newMemberAnns.put(object);
                        }
                    }
                }


                //Annets
                JSONArray newMemberAnnets =new JSONArray();
                JSONArray updatedAnnets =new JSONArray();
                JSONArray deletedAnnets =new JSONArray();

                for(DependentData data : selectedAnnetsList){
                    if(data.getEdited()){
                        JSONObject object=new JSONObject();
                        object.put("PK_AttendanceAnnetsID",data.getMemberID());
                        object.put("AnnetsName",data.getAnnetsName());

                        if(!data.getMemberID().equalsIgnoreCase("0")){
                            updatedAnnets.put(object);
                        }else {
                            newMemberAnnets.put(object);
                        }
                    }

                }

                //Visitors
                JSONArray newMemberVisitors =new JSONArray();
                JSONArray updatedVisitors =new JSONArray();
                JSONArray deletedVisitors =new JSONArray();

                for(DependentData data : selectedVisitorList){
                    if(data.getEdited()){
                        JSONObject object=new JSONObject();
                        object.put("PK_AttendanceVisitorID",data.getMemberID());
                        object.put("VisitorsName",data.getVisitorName());
                        object.put("Rotarian_whohas_Brought",data.getBrought());

                        if(!data.getMemberID().equalsIgnoreCase("0")){
                            updatedVisitors.put(object);
                        }else {
                            newMemberVisitors.put(object);
                        }
                    }

                }

                //Rotarian
                JSONArray newMemberRotarian =new JSONArray();
                JSONArray updatedRotarian =new JSONArray();
                JSONArray deletedRotarian =new JSONArray();

                for(DependentData data : selectedRotarianList){

                    if(data.getEdited()){
                        JSONObject object=new JSONObject();
                        object.put("PK_AttendanceRotarianID",data.getMemberID());
                        object.put("RotarianID",data.getRotarianID());
                        object.put("RotarianName",data.getRotarianName());
                        object.put("ClubName",data.getClubName());

                        if(!data.getMemberID().equalsIgnoreCase("0")){
                            updatedRotarian.put(object);
                        }else {
                            newMemberRotarian.put(object);
                        }
                    }

                }

                //Delegates
                JSONArray newMemberDelegates =new JSONArray();
                JSONArray updatedDelegates =new JSONArray();
                JSONArray deletedDelegates =new JSONArray();

                for(DependentData data : selectedDelegatesList){

                    if(data.getEdited()){
                        JSONObject object=new JSONObject();
                        object.put("PK_AttendanceDelegateID",data.getMemberID());
                        object.put("RotarianName",data.getRotarianName());
                        object.put("DistrictDesignation",data.getDistrictDesignation());
                        object.put("ClubName",data.getClubName());

                        if(!data.getMemberID().equalsIgnoreCase("0")){
                            updatedDelegates.put(object);
                        }else {
                            newMemberDelegates.put(object);
                        }
                    }

                }

                ArrayList<DependentData> deletedMember=new ArrayList<>();
                deletedMember=deletedList;
                for(int i=0;i<deletedMember.size();i++){
                    DependentData data=deletedMember.get(i);
                    if(data.getType().equalsIgnoreCase(Constant.Dependent.MEMBER)){
                        for(DependentData selectedData:selectedList){
                            if(data.getMemberID().equalsIgnoreCase(selectedData.getMemberID())){
                                deletedList.remove(i);
                            }
                        }
                    }
                }

                //All deleted members
                for(DependentData data:deletedList){
                    if(!data.getMemberID().equalsIgnoreCase("0")) {

                        if (data.getType().equalsIgnoreCase(Constant.Dependent.ANNS)) {
                            JSONObject object = new JSONObject();
                            object.put("PK_AttendanceAnnsID", data.getMemberID());
                            deletedAnns.put(object);
                        } else if (data.getType() .equalsIgnoreCase(Constant.Dependent.ANNETS)) {
                            JSONObject object = new JSONObject();
                            object.put("PK_AttendanceAnnetsID", data.getMemberID());
                            deletedAnnets.put(object);
                        } else if (data.getType() .equalsIgnoreCase(Constant.Dependent.VISITORS)) {
                            JSONObject object = new JSONObject();
                            object.put("PK_AttendanceVisitorID", data.getMemberID());
                            deletedVisitors.put(object);
                        } else if (data.getType() .equalsIgnoreCase(Constant.Dependent.ROTARIAN)) {
                            JSONObject object = new JSONObject();
                            object.put("PK_AttendanceRotarianID", data.getMemberID());
                            deletedRotarian.put(object);
                        } else if (data.getType() .equalsIgnoreCase(Constant.Dependent.DELEGETS)) {
                            JSONObject object = new JSONObject();
                            object.put("PK_AttendanceDelegateID", data.getMemberID());
                            deletedDelegates.put(object);
                        }
                        else if (data.getType() .equalsIgnoreCase(Constant.Dependent.MEMBER)) {
                            JSONObject object = new JSONObject();
                            object.put("FK_MemberID", data.getMemberID());
                            deletedMembers.put(object);
                        }
                    }
                }

                JSONArray AttendanceMembers=new JSONArray();
                JSONObject memberObject=new JSONObject();
                memberObject.put("newMembers",newMembersArray);
                memberObject.put("deletedMembers",deletedMembers);
                AttendanceMembers.put(memberObject);

                JSONArray AttendanceAnns=new JSONArray();
                JSONObject annsObject=new JSONObject();
                annsObject.put("newAnns",newMemberAnns);
                annsObject.put("UpdateAnns",updatedAnns);
                annsObject.put("deletedAnns",deletedAnns);
                AttendanceAnns.put(annsObject);

                JSONArray AttendanceAnnets=new JSONArray();
                JSONObject annetsObject=new JSONObject();
                annetsObject.put("newAnnets",newMemberAnnets);
                annetsObject.put("UpdateAnnets",updatedAnnets);
                annetsObject.put("deletedAnnets",deletedAnnets);
                AttendanceAnnets.put(annetsObject);

                JSONArray AttendanceVisitors=new JSONArray();
                JSONObject visitorObject=new JSONObject();
                visitorObject.put("newVisitors",newMemberVisitors);
                visitorObject.put("UpdateVisitors",updatedVisitors);
                visitorObject.put("deletedVisitors",deletedVisitors);
                AttendanceVisitors.put(visitorObject);

                JSONArray AttendanceRotarians=new JSONArray();
                JSONObject rotarianObject=new JSONObject();
                rotarianObject.put("newRotarians",newMemberRotarian);
                rotarianObject.put("UpdateRotarians",updatedRotarian);
                rotarianObject.put("deletedRotarians",deletedRotarian);
                AttendanceRotarians.put(rotarianObject);

                JSONArray AttendanceDistrictDelegate=new JSONArray();
                JSONObject delegateObject=new JSONObject();
                delegateObject.put("newDistrictDelegate",newMemberDelegates);
                delegateObject.put("UpdateDistrictDelegate",updatedDelegates);
                delegateObject.put("deletedDistrictDelegate",deletedDelegates);
                AttendanceDistrictDelegate.put(delegateObject);

                requestObject.put("AttendanceMembers",AttendanceMembers);
                requestObject.put("AttendanceAnns",AttendanceAnns);
                requestObject.put("AttendanceAnnets",AttendanceAnnets);
                requestObject.put("AttendanceVisitors",AttendanceVisitors);
                requestObject.put("AttendanceRotarians",AttendanceRotarians);
                requestObject.put("AttendanceDistrictDelegate",AttendanceDistrictDelegate);

                Utils.log(Constant.AttendanceAddEdit+" / "+requestObject.toString());

                showDialog();

                JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, Constant.AttendanceAddEdit, requestObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        addAttendanceResult(response);

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

    private void addAttendanceResult(JSONObject response){

        try {

            hideDialog();

            Utils.log("add atend =>"+response.toString());

            JSONObject TBAttendanceDetailsResult=response.getJSONObject("TBAttendanceDetailsResult");

            String status = TBAttendanceDetailsResult.getString("status");

            if(status.equalsIgnoreCase("0")){

                attendance_id = TBAttendanceDetailsResult.getString("Result");

                if(txt_add.getText().toString().equalsIgnoreCase("UPDATE")){
                    Utils.showToastWithTitleAndContext(context,"Attendance updated successfully");
                }else{
                    Utils.showToastWithTitleAndContext(context,"Attendance added successfully");
                }

                finish();

            }else {
                Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
            }

        } catch (JSONException e) {
            hideDialog();
            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {

            deletedList.addAll((ArrayList<DependentData>) data.getSerializableExtra("deletedList"));

            if (requestCode == MEMBER_REQ) {

                ArrayList<DirectoryData> memberList = new ArrayList<>();

                memberList = data.getParcelableArrayListExtra("result");

                selectedList.clear();

                for (DirectoryData d : memberList) {

                    if (d.box) {
                        DependentData dependentData=new DependentData();
                        dependentData.setEdited(d.isEdited());
                        dependentData.setMemberID(d.getProfileID());
                        selectedList.add(dependentData);
                    }
                    //something here
                }

                txt_memberCount.setText(String.valueOf(selectedList.size()));

                Utils.log(selectedList.toString());

            }else if (requestCode == ANNS_REQ) {

                selectedAnnsList = (ArrayList<DependentData>) data.getSerializableExtra("result");
                String count=data.getStringExtra("count");
                txt_annsCount.setText(count);

                Utils.log(selectedAnnsList.toString());

            }else if (requestCode == ANNETS_REQ) {

                selectedAnnetsList = (ArrayList<DependentData>) data.getSerializableExtra("result");
                String count=data.getStringExtra("count");
                txt_annetsCount.setText(count);

            }else if (requestCode == VISITOR_REQ) {

                selectedVisitorList = (ArrayList<DependentData>) data.getSerializableExtra("result");
                String count=data.getStringExtra("count");
                txt_visitorsCount.setText(count);

            }else if (requestCode == ROTARIAN_REQ) {

                selectedRotarianList = (ArrayList<DependentData>) data.getSerializableExtra("result");
                String count=data.getStringExtra("count");
                txt_rotarianCount.setText(count);

            }else if (requestCode == DELEGATES_REQ) {

                selectedDelegatesList = (ArrayList<DependentData>) data.getSerializableExtra("result");
                String count=data.getStringExtra("count");
                txt_delegatesCount.setText(count);

            }
        }
    }
}
