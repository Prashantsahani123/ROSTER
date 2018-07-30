package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.SubGroupAdapter;
import com.SampleApp.row.Data.SubGoupData;
import com.SampleApp.row.Data.SubGroupDetailsData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 10-02-2016.
 */
public class SubGroupList extends Activity {

    ListView listview;
    TextView tv_title, tv_addbtn, tv_no_records_found;
    ImageView iv_backbutton, iv_actionbtn;
    LinearLayout linr_addbtn;
    Context context;
    private ArrayList<SubGoupData> list_subGroup = new ArrayList<SubGoupData>();
    private SubGroupAdapter adapter_subGroup;
    private String flag_addsubgrp = "0";
    private String edit_announcement_selectedids = "0";
    private String parentId = "", profileId = "", groupName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subgroup_list);
        context  = this;

        listview = (ListView) findViewById(R.id.listView);

        tv_addbtn = (TextView) findViewById(R.id.tv_addbtn);

        tv_no_records_found = (TextView) findViewById(R.id.tv_no_records_found);

        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        tv_title = (TextView) findViewById(R.id.tv_title);
        linr_addbtn = (LinearLayout) findViewById(R.id.linr_addbtn);

        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn.setVisibility(View.VISIBLE);

        parentId = getIntent().getExtras().getString("parentId");
        profileId = PreferenceManager.getPreference(SubGroupList.this, PreferenceManager.GRP_PROFILE_ID, "");
        groupName = getIntent().getExtras().getString("subgroupname", "Sub Groups");
        tv_title.setText(groupName);


        Log.e("ParentID", "Parent id is  : "+parentId);
        //  webservices();


        Bundle intent = getIntent().getExtras();
        Intent intenti = getIntent();
        if (intent != null) {

            //linr_addbtn.setVisibility(View.VISIBLE);// Change the layout as per call from Select grp
            iv_actionbtn.setVisibility(View.GONE);

            if (intent.getString("flag_addsubgrp") == null) {
                flag_addsubgrp = "0";
            } else {
                flag_addsubgrp = intent.getString("flag_addsubgrp"); // radio buttonmm
            }
            if (intent.containsKey("selected_subgrpdata")) {
                list_subGroup = intent.getParcelableArrayList("selected_subgrpdata");
                adapter_subGroup = new SubGroupAdapter(SubGroupList.this, R.layout.subgroup_list_item, list_subGroup, "1");
                listview.setAdapter(adapter_subGroup);

                if (intenti.hasExtra("edit_announcement_selectedids")) {

                    if (intenti.getStringExtra("edit_announcement_selectedids").equals("0")) {
                        list_subGroup = intent.getParcelableArrayList("selected_subgrpdata");
                        adapter_subGroup = new SubGroupAdapter(SubGroupList.this, R.layout.subgroup_list_item, list_subGroup, "1");
                        listview.setAdapter(adapter_subGroup);
                    } else {
                        edit_announcement_selectedids = intenti.getStringExtra("edit_announcement_selectedids");
                        flag_addsubgrp = "1";
                        Utils.log("first");
                        webservices();
                    }
                }
            } else {
                Utils.log("two");
                webservices();
            }
          /*  if(list_subGroup.isEmpty()){
             webservices();
            }else {
                adapter_subGroup = new SubGroupAdapter(SubGroupList.this, R.layout.subgroup_list_item, list_subGroup, "1");
                listview.setAdapter(adapter_subGroup);
            }*/
        } else {
            //fillData();
            Utils.log("three");
            webservices();
        }

        init();
        checkadminrights();
    }

    private void checkadminrights() {
        if (PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN).equals("No")) {
            iv_actionbtn.setVisibility(View.GONE);
        } else {
            iv_actionbtn.setVisibility(View.VISIBLE);
        }
    }

    public void init() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent i = new Intent(SubGroupList.this, SubGroupDetails.class);
                Intent i = new Intent(SubGroupList.this, SubGroupList.class);
                i.putExtra("parentId", list_subGroup.get(position).getSubgrpId());
                i.putExtra("subgroupname", list_subGroup.get(position).getSubgroup_name());
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }
        });


        tv_addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "Selected Product are :";
                // int totalAmount = 0;
                int count = 0;
                // Array[] arr = new Array[0];

                for (SubGoupData p : adapter_subGroup.getBox()) {
                    if (p.box) {
                        result += "\n" + p.getSubgrpId();
                        count = count + 1;
                    }
                }
                //Toast.makeText(this, result+"\n"+"Total Amount:="+totalAmount, Toast.LENGTH_LONG).show();

                //   Toast.makeText(AddMembers.this, result + "\n" + "Count:=" + count, Toast.LENGTH_LONG).show();
                Log.d("TOUCHBASE", "@@@@@@@@@--" + count);
                if (count <= 0) {
                    Toast.makeText(SubGroupList.this, "Please Select at least 1 Group ", Toast.LENGTH_LONG).show();
                } else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", list_subGroup);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SubGroupList.this, CreateSubGroup.class);
                i.putExtra("subgrpId", parentId);
                startActivityForResult(i, 1);
            }
        });

    }

    public void finishActivity(View v) {
        finish();
    }


    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("parentID", parentId));
        arrayList.add(new BasicNameValuePair("profileId", profileId));

        Log.d("Response", "PARAMETERS " + Constant.GetSubGrpDirectoryList + " :- " + arrayList.toString());
        if (InternetConnection.checkConnection(context)){
            new WebConnectionAsyncAnnouncement(Constant.GetSubGrpDirectoryList, arrayList, SubGroupList.this).execute();
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    public class WebConnectionAsyncAnnouncement extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(SubGroupList.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;


        public WebConnectionAsyncAnnouncement(String url, List<NameValuePair> argList, Context ctx) {
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


            //	Log.d("response","Do post"+ result.toString());

            if (result != "") {
                Log.d("Response", "@@ " + result.toString());
                list_subGroup.clear();
                getSubGroup(result.toString());
                progressDialog.dismiss();

            } else {
                progressDialog.dismiss();
                Log.d("Response", "Null Resposnse");
            }

        }

    }


    private void getSubGroup(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject SubGroupListResult = jsonObj.getJSONObject("result");
            final String status = jsonObj.getString("status");
            if (status.equals("0")) ;
            {
                JSONArray SubGroupResult = SubGroupListResult.getJSONArray("subGrpList");
                int n = SubGroupResult.length();
                if ( n == 0 ) {

                    groupDetaisWebServices();

                }
                else {
                    for (int i = 0; i < n; i++) {
                        JSONObject objects = SubGroupResult.getJSONObject(i);
                        //JSONObject objects = object.getJSONObject("Subgroup");

                        SubGoupData data = new SubGoupData();

                        data.setSubgroup_name(objects.getString("subgrpTitle").toString());
                        data.setNo_of_members(objects.getString("noOfmem").toString());
                        data.setSubgrpId(objects.getString("subgrpId").toString());

                        if (edit_announcement_selectedids.equals("0")) {
                            data.setBox(false);
                        } else {
                            //data.setBox(false);
                            Log.d("TOUCHBASE", "ID ID ID :- " + edit_announcement_selectedids);
                            String[] parts = edit_announcement_selectedids.split(",");
                            for (String part : parts) {
                                Log.d("TOUCHBASE", "##### :-" + part);
                                if (objects.getString("subgrpId").toString().equals(part)) {
                                    Log.d("TOUCHBASE", "TRUE ");
                                    data.setBox(true);
                                    Log.d("TOUCHBASE", "TRUE1111 ");
                                } else {

                                    // data.setBox(false);
                                }
                            }
                        }


                        //Log.d("TOUCHBASE","*************** 1 ");
                        list_subGroup.add(data);
                        // Log.d("TOUCHBASE", "*************** 2 ");
                    }
                    // Log.d("TOUCHBASE","*************** 3 ");
                    if (list_subGroup.isEmpty()) {
                        //  Log.d("TOUCHBASE","*************** 4 ");
                        tv_no_records_found.setVisibility(View.VISIBLE);
                        tv_addbtn.setVisibility(View.GONE);

                    } else {
                        //  Log.d("TOUCHBASE","*************** 5 ");
                        tv_no_records_found.setVisibility(View.GONE);
                        tv_addbtn.setVisibility(View.VISIBLE);
                        if (flag_addsubgrp.equals("0"))
                            adapter_subGroup = new SubGroupAdapter(SubGroupList.this, R.layout.subgroup_list_item, list_subGroup, "0");
                        else
                            adapter_subGroup = new SubGroupAdapter(SubGroupList.this, R.layout.subgroup_list_item, list_subGroup, "1");
                        listview.setAdapter(adapter_subGroup);
                    }
                }

            }
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if (resultCode == Activity.RESULT_OK) {
               // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)‌​;
                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                webservices();
            }
        }
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if ( resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                //start of code updation pp
                if (!InternetConnection.checkConnection(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
                    return;
                }

                // end of code updation pp
                Utils.log("four");
                webservices();
                //String message=data.getStringExtra("MESSAGE");
                // textView1.setText(message);
            }
        }
    }

    private void groupDetaisWebServices() {
        Log.e("webservice", "Starting of webservice");
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("subgrpId", parentId));

        Log.d("Response", "PARAMETERS " + Constant.GetSubGroupDetail + " :- " + arrayList.toString());
        if (InternetConnection.checkConnection(context)) {
            new WebConnectionAsyncGroup(Constant.GetSubGroupDetail, arrayList, SubGroupList.this).execute();
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
        Log.e("webservice", "end of web service");
    }

    public class WebConnectionAsyncGroup extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(SubGroupList.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;


        public WebConnectionAsyncGroup(String url, List<NameValuePair> argList, Context ctx) {
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
                //  list_announcmentdata.clear();
                //  getAnnouncementItems(result.toString());
                getsubgroupdetails(result.toString());


            } else {
                Log.d("Response", "Null Resposnse");
            }

        }
    }

    private void getsubgroupdetails(String result) {

        try {
            final ArrayList<SubGroupDetailsData> list_data = new ArrayList<>();
            JSONObject jsonObj = new JSONObject(result);
            JSONObject AnnouncementResult = jsonObj.getJSONObject("TBGetSubGroupDetailListResult");
            final String status = AnnouncementResult.getString("status");
            if (status.equals("0")) ;
            {

                final JSONArray AnnouncementListResdult = AnnouncementResult.getJSONArray("SubGroupResult");
                for (int i = 0; i < AnnouncementListResdult.length(); i++) {
                    JSONObject object = AnnouncementListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("SubgrpMemberDetail");

                    SubGroupDetailsData data = new SubGroupDetailsData();

                    data.setProfileId(objects.getString("profileId").toString());
                    data.setMemname(objects.getString("memname").toString());
                    data.setMobile(objects.getString("mobile").toString());

                    list_data.add(data);

                }

                Intent intent = new Intent(SubGroupList.this, SubGroupDetails.class);
                Bundle data=new Bundle();
                data.putSerializable("list",(Serializable)list_data);
                intent.putExtra("data",data);
                intent.putExtra("subgroupid", parentId);
                intent.putExtra("subgroupname", groupName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

                finish();

            }


        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }




}
