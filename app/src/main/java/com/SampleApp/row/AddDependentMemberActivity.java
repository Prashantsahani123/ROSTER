package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.SampleApp.row.Adapter.MemberListAdapter;
import com.SampleApp.row.Data.DependentData;
import com.SampleApp.row.Data.DirectoryData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddDependentMemberActivity extends AppCompatActivity {

    MemberListAdapter boxAdapter;
    TextView ib_continue;
    ArrayAdapter<String> adapter;
    EditText et_serach_directory;
    TextView tv_title;
    ImageView iv_backbutton;
    ListView lvMain;
    String edit_announcement_selectedids = "0";
    ArrayList<DirectoryData> directorylist = new ArrayList<>();
    String isAdmin = "";
    Context context;
    ProgressDialog progressDialog;
    ArrayList<DependentData> selectedList=new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dependent_member);
        context=this;
        ib_continue = (TextView) findViewById(R.id.ib_continue);
        lvMain = (ListView) findViewById(R.id.listView);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        et_serach_directory = (EditText) findViewById(R.id.et_serach_directory);
        tv_title.setText("Member");
        isAdmin = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN);
        // boxAdapter = new MemberListAdapter(this, directorylist);
        // lvMain.setAdapter(boxAdapter);
        //  lvMain.setTextFilterEnabled(true);
        Bundle bundelintent = getIntent().getExtras();
        init();

        if (bundelintent != null) {

            selectedList= (ArrayList<DependentData>) bundelintent.getSerializable("list");
            webservices();

        }else {
            webservices();
        }
    }

//    private void getDependent(){
//        if (InternetConnection.checkConnection(context)) {
//            try {
//
//                JSONObject requestObject=new JSONObject();
//
//                requestObject.put("AttendanceID",attendanceID);
//                requestObject.put("type",Constant.Dependent.MEMBER);
//
//                Utils.log(Constant.GetAttendanceMemberDetails+" / "+requestObject.toString());
//
//                showDialog();
//
//                JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, Constant.GetAttendanceMemberDetails, requestObject, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        Utils.log(""+response);
//                        setAttendance(response);
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        hideDialog();
//                        Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
//                        Utils.log("VollyError:- " + error);
//                    }
//                });
//
//                AppController.getInstance().addToRequestQueue(context, request);
//
//
//            }catch (Exception e){
//                hideDialog();
//            }
//        }else {
//            Utils.showToastWithTitleAndContext(context,getString(R.string.noInternet));
//        }
//    }
//
//    private void setAttendance(JSONObject response){
//
//        try {
//
//            hideDialog();
//            selectedList.clear();
//
//            JSONObject TBAttendanceMemberDetailsResult=response.getJSONObject("TBAttendanceMemberDetailsResult");
//            String status=TBAttendanceMemberDetailsResult.getString("status");
//            if(status.equalsIgnoreCase("0")){
//                JSONArray AttendanceMemberResult=TBAttendanceMemberDetailsResult.getJSONArray("AttendanceMemberResult");
//                if(AttendanceMemberResult!=null && AttendanceMemberResult.length()>0){
//                    for(int i=0;i<AttendanceMemberResult.length();i++){
//                        JSONObject object=AttendanceMemberResult.getJSONObject(i);
//                        DependentData data=new DependentData();
//                        data.setMemberID(object.getString("FK_MemberID"));
//                        selectedList.add(data);
//                    }
//
//                    webservices();
//                }else {
//                    Utils.showToastWithTitleAndContext(context,getString(R.string.msg_no_records_found));
//
//                }
//            }else {
//
//                Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
//            }
//
//        } catch (JSONException e) {
//            hideDialog();
//            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
//            Utils.log("VollyError:- " + e.getMessage());
//            e.printStackTrace();
//        }
//
//    }

    private void init() {
        et_serach_directory.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                //adapter.getFilter().filter(cs);
                //adapter.getFilter().filter(cs.toString());

                int textlength = cs.length();
                ArrayList<DirectoryData> tempArrayList = new ArrayList<DirectoryData>();
                for (DirectoryData c : directorylist) {
                    if (textlength <= c.getMemberName().length()) {
                        if (c.getMemberName().toLowerCase().contains(cs.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                //Data_array= tempArrayList;
                final MemberListAdapter adapter = new MemberListAdapter(context, tempArrayList);
                lvMain.setAdapter(adapter);
                if ( tempArrayList.size() <= 0 ) {
                    lvMain.setEmptyView(findViewById(R.id.tv_no_records_found));
                    adapter.notifyDataSetChanged();
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


        ib_continue.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                // int totalAmount = 0;
                int count = 0;
                // Array[] arr = new Array[0];
               ArrayList<DependentData> deletedList=new ArrayList<>();
                if(boxAdapter!=null){
                    for (DirectoryData p : boxAdapter.getObjects()) {
//                        if (p.box) {
////
////                            count = count + 1;
////                        }else {
////
////                        }

                        if(p.isEdited() && !p.box){
                            DependentData data=new DependentData();
                            data.setType(Constant.Dependent.MEMBER);
                            data.setMemberID(p.getProfileID());
                            deletedList.add(data);
                        }
                    }
                    //Toast.makeText(this, result+"\n"+"Total Amount:="+totalAmount, Toast.LENGTH_LONG).show();

                    //   Toast.makeText(context, result + "\n" + "Count:=" + count, Toast.LENGTH_LONG).show();
                    Log.d("TOUCHBASE", "@@@@@@@@@--" + count);
//                    if (count <= 0) {
//                        Toast.makeText(context, "Please Select at least 1 Member ", Toast.LENGTH_LONG).show();
//                    } else {
//                        Intent returnIntent = new Intent();
//                        returnIntent.putExtra("result", directorylist);
//                        setResult(Activity.RESULT_OK, returnIntent);
//                        finish();
//                    }

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", boxAdapter.getObjects());
                    returnIntent.putExtra("deletedList",deletedList );
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }else {
                    Utils.showToastWithTitleAndContext(context,getString(R.string.msg_no_records_found));
                }


            }
        });
    }

    private void webservices() {
        //{"masterUID":"1","grpID":"","searchText":"","page":""}
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("masterUID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));
        arrayList.add(new BasicNameValuePair("grpID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));

        arrayList.add(new BasicNameValuePair("searchText", et_serach_directory.getText().toString()));
        arrayList.add(new BasicNameValuePair("page", ""));

        if ( isAdmin.equalsIgnoreCase("Partial")) {
            String isSubGroupAdmin = "1";
            arrayList.add(new BasicNameValuePair("isSubGrpAdmin", isSubGroupAdmin));
            String profileId = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID);
            arrayList.add(new BasicNameValuePair("profileId", profileId));
        }

        //arrayList.add(new BasicNameValuePair("isSubGrpAdmin", ))
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "PARAMETERS " + Constant.GetDirectoryList + " :- " + arrayList.toString());
        new WebConnectionAsyncDirectory(Constant.GetDirectoryList, arrayList, context).execute();
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

                getDirectoryItems(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getDirectoryItems(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject DirectoryResult = jsonObj.getJSONObject("TBMemberResult");
            final String status = DirectoryResult.getString("status");
            if (status.equals("0")) {
                JSONArray DirectoryListResdult = DirectoryResult.getJSONArray("MemberListResults");
                directorylist.clear();
                for (int i = 0; i < DirectoryListResdult.length(); i++) {
                    JSONObject object = DirectoryListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("MemberListResult");

                    DirectoryData data = new DirectoryData();
                    data.setType("Attendance");
                    data.setMasterUID(objects.getString("masterUID").toString());
                    data.setGrpID(objects.getString("grpID").toString());
                    data.setProfileID(objects.getString("profileID").toString());
                    data.setGroupName(objects.getString("groupName").toString());
                    data.setMemberName(objects.getString("memberName").toString());
                    data.setPic(objects.getString("pic").toString());
                    data.setMembermobile(objects.getString("membermobile").toString());
                    data.setGrpCount(objects.getString("grpCount").toString());
                    String designation=objects.getString("designation");
                    if(designation!=null && !designation.equalsIgnoreCase("null") && !designation.isEmpty()){
                        data.setDesignation(designation);
                    }else {
                        data.setDesignation("");
                    }
                    if (selectedList.size()<=0) {
                        data.setBox(false);
                    } else {
                        //data.setBox(false);
//                        String[] parts = edit_announcement_selectedids.split(",");
                        for (DependentData part : selectedList) {
                            Log.d("TOUCHBASE", "##### :-" + part.getMemberID());
                            if (objects.getString("profileID").toString().equalsIgnoreCase(part.getMemberID())) {
                                Log.d("TOUCHBASE", "TRUE ");
                                data.setBox(true);
                            } else {
                                Log.d("TOUCHBASE", "FALSE ");
                                // data.setBox(false);
                            }
                            data.setEdited(part.getEdited());
                        }
                    }



                    directorylist.add(data);


                }
                // directoryAdapter = new DirectoryAdapter(Directory.this, R.layout.directory_list_item, directorylist);
                // listview.setAdapter(directoryAdapter);


                boxAdapter = new MemberListAdapter(this, directorylist);
                lvMain.setAdapter(boxAdapter);
                lvMain.setTextFilterEnabled(true);
            }


        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }


}
