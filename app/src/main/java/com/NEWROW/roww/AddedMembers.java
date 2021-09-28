package com.NEWROW.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.NEWROW.row.Adapter.AddedMemberListAdapter;
import com.NEWROW.row.Data.DirectoryData;
import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AddedMembers extends Activity {

    AddedMemberListAdapter boxAdapter;
    TextView ib_continue;
    ArrayAdapter<String> adapter;
    EditText et_serach_directory;
    TextView tv_title;
    ImageView iv_backbutton;
    ListView lvMain;
    String attendanceId = "0",moduleName="",count="",type="";
    ArrayList<DirectoryData> directorylist = new ArrayList<>();
    String isAdmin = "";
    Context context;
    ProgressDialog progressDialog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_members);
        context=this;
        ib_continue = (TextView) findViewById(R.id.ib_continue);
        lvMain = (ListView) findViewById(R.id.listView);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        et_serach_directory = (EditText) findViewById(R.id.et_serach_directory);
        tv_title.setText("Members");
        isAdmin = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN);
        // boxAdapter = new MemberListAdapter(this, directorylist);
        // lvMain.setAdapter(boxAdapter);
        //  lvMain.setTextFilterEnabled(true);
        Bundle bundelintent = getIntent().getExtras();

        init();

        if (bundelintent != null) {
            attendanceId=bundelintent.getString("attendanceId");
            moduleName=bundelintent.getString("moduleName");
            count=bundelintent.getString("count");
            type=bundelintent.getString("type");
            tv_title.setText(moduleName);

            getMemberList();


            //txt_module.setText(count+" "+moduleName);
        }
    }

    private void init() {

    }

    private void getMemberList(){
        if (InternetConnection.checkConnection(context)) {
            try {
                JSONObject requestObject=new JSONObject();
                requestObject.put("AttendanceID",attendanceId);
                requestObject.put("type",type);

                Utils.log(Constant.GetAttendanceMemberDetails+" / "+requestObject.toString());

                showDialog();

                JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, Constant.GetAttendanceMemberDetails, requestObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.log(""+response);
                        setMemeberList(response);
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

    private void setMemeberList(JSONObject response){
        try {
            JSONObject TBAttendanceMemberDetailsResult=response.getJSONObject("TBAttendanceMemberDetailsResult");
            String status=TBAttendanceMemberDetailsResult.getString("status");
            if(status.equalsIgnoreCase("0")){
                JSONArray AttendanceMemberResult=TBAttendanceMemberDetailsResult.getJSONArray("AttendanceMemberResult");
                if(AttendanceMemberResult!=null && AttendanceMemberResult.length()>0){
                    for(int i=0;i<AttendanceMemberResult.length();i++){
                        JSONObject object = AttendanceMemberResult.getJSONObject(i);


                    }
                }
            }else {

                Utils.showToastWithTitleAndContext(context,getString(R.string.msg_no_records_found));
            }
            hideDialog();
        } catch (JSONException e) {
            hideDialog();
            e.printStackTrace();
            Utils.log(""+e);
            Utils.showToastWithTitleAndContext(context,getString(R.string.msgRetry));
        }

    }

//    private void webservices() {
//        //{"masterUID":"1","grpID":"","searchText":"","page":""}
//        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
//        arrayList.add(new BasicNameValuePair("masterUID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));
//        arrayList.add(new BasicNameValuePair("grpID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
//
//        arrayList.add(new BasicNameValuePair("searchText", et_serach_directory.getText().toString()));
//        arrayList.add(new BasicNameValuePair("page", ""));
//
//        if ( isAdmin.equalsIgnoreCase("Partial")) {
//            String isSubGroupAdmin = "1";
//            arrayList.add(new BasicNameValuePair("isSubGrpAdmin", isSubGroupAdmin));
//            String profileId = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID);
//            arrayList.add(new BasicNameValuePair("profileId", profileId));
//        }
//
//        //arrayList.add(new BasicNameValuePair("isSubGrpAdmin", ))
//        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
//        Log.d("Response", "PARAMETERS " + Constant.GetDirectoryList + " :- " + arrayList.toString());
//        new WebConnectionAsyncDirectory(Constant.GetDirectoryList, arrayList, AddedMembers.this).execute();
//    }
//
//    public class WebConnectionAsyncDirectory extends AsyncTask<String, Object, Object> {
//
//        String val = null;
//        final ProgressDialog progressDialog = new ProgressDialog(AddedMembers.this, R.style.TBProgressBar);
//        Context context = null;
//        String url = null;
//        List<NameValuePair> argList = null;
//
//
//        public WebConnectionAsyncDirectory(String url, List<NameValuePair> argList, Context ctx) {
//            this.url = url;
//            this.argList = argList;
//            context = ctx;
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//            //	dialog.show();
//
//            progressDialog.setCancelable(false);
//            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//            progressDialog.show();
//
//        }
//
//        @Override
//        protected Object doInBackground(String... params) {
//
//
//            try {
//
//                val = HttpConnection.postData(url, argList);
//                val = val.toString();
//
//                Log.d("Response", "we" + val);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return val;
//        }
//
//        @Override
//        protected void onPostExecute(Object result) {
//            super.onPostExecute(result);
//
//            progressDialog.dismiss();
//            //	Log.d("response","Do post"+ result.toString());
//
//            if (result != "") {
//                Log.d("Response", "calling getDirectorydetails");
//
//                getDirectoryItems(result.toString());
//
//            } else {
//                Log.d("Response", "Null Resposnse");
//            }
//
//        }
//
//    }
//
//    private void getDirectoryItems(String result) {
//        try {
//            JSONObject jsonObj = new JSONObject(result);
//            JSONObject DirectoryResult = jsonObj.getJSONObject("TBMemberResult");
//            final String status = DirectoryResult.getString("status");
//            if (status.equals("0")) {
//                JSONArray DirectoryListResdult = DirectoryResult.getJSONArray("MemberListResults");
//                directorylist.clear();
//                for (int i = 0; i < DirectoryListResdult.length(); i++) {
//                    JSONObject object = DirectoryListResdult.getJSONObject(i);
//                    JSONObject objects = object.getJSONObject("MemberListResult");
//
//                    DirectoryData data = new DirectoryData();
//
//                    data.setMasterUID(objects.getString("masterUID").toString());
//                    data.setGrpID(objects.getString("grpID").toString());
//                    data.setProfileID(objects.getString("profileID").toString());
//                    data.setGroupName(objects.getString("groupName").toString());
//                    data.setMemberName(objects.getString("memberName").toString());
//                    data.setPic(objects.getString("pic").toString());
//                    data.setMembermobile(objects.getString("membermobile").toString());
//                    data.setGrpCount(objects.getString("grpCount").toString());
//
//                    if (edit_announcement_selectedids.equals("0")) {
//                        data.setBox(false);
//                    } else {
//                        //data.setBox(false);
//                        String[] parts = edit_announcement_selectedids.split(",");
//                        for (String part : parts) {
//                            Log.d("TOUCHBASE", "##### :-" + part);
//                            if (objects.getString("profileID").toString().equals(part)) {
//                                Log.d("TOUCHBASE", "TRUE ");
//                                data.setBox(true);
//                            } else {
//                                Log.d("TOUCHBASE", "FALSE ");
//                                // data.setBox(false);
//                            }
//                        }
//                    }
//
//                    directorylist.add(data);
//
//
//                }
//                // directoryAdapter = new DirectoryAdapter(Directory.this, R.layout.directory_list_item, directorylist);
//                // listview.setAdapter(directoryAdapter);
//
//
//                boxAdapter = new AddedMemberListAdapter(this, directorylist);
//                lvMain.setAdapter(boxAdapter);
//                lvMain.setTextFilterEnabled(true);
//            }
//
//        } catch (Exception e) {
//            Log.d("exec", "Exception :- " + e.toString());
//        }
//    }

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