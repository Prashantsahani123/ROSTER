package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.SampleApp.row.Adapter.SubGroupSelectionAdapter;
import com.SampleApp.row.Data.SubGoupData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;

/**
 * Created by USER on 10-02-2016.
 */

public class SubGroupSelectionList extends Activity {

    public static final HashSet<String> selected = new HashSet<String>();

    ListView listview;
    TextView tv_title, tv_addbtn, tv_no_records_found;
    ImageView iv_backbutton, iv_actionbtn;
    LinearLayout linr_addbtn;
    Context context;
    private ArrayList<SubGoupData> list_subGroup = new ArrayList<SubGoupData>();
    private SubGroupSelectionAdapter adapter_subGroup;
    private String flag_addsubgrp = "0";
    private String edit_announcement_selectedids = "0";
    private String parentId = "", profileId = "", groupName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subgroup_selection_list);
        context  = this;
        listview = (ListView) findViewById(R.id.listView);

        tv_addbtn = (TextView) findViewById(R.id.tv_addbtn);

        tv_no_records_found = (TextView) findViewById(R.id.tv_no_records_found);

        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        tv_title = (TextView) findViewById(R.id.tv_title);
        linr_addbtn = (LinearLayout) findViewById(R.id.linr_addbtn);

        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn.setVisibility(View.VISIBLE);


        parentId = getIntent().getExtras().getString("parentId", "0");

        profileId = PreferenceManager.getPreference(SubGroupSelectionList.this, PreferenceManager.GRP_PROFILE_ID, "");
        groupName = getIntent().getExtras().getString("subgroupname", "Sub Groups");
        tv_title.setText(groupName);

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
                adapter_subGroup = new SubGroupSelectionAdapter(SubGroupSelectionList.this, R.layout.subgroup_selection_list_item, list_subGroup, "1");
                listview.setAdapter(adapter_subGroup);

                if (intenti.hasExtra("edit_announcement_selectedids")) {

                    if (intenti.getStringExtra("edit_announcement_selectedids").equals("0")) {
                        list_subGroup = intent.getParcelableArrayList("selected_subgrpdata");
                        adapter_subGroup = new SubGroupSelectionAdapter(SubGroupSelectionList.this, R.layout.subgroup_selection_list_item, list_subGroup, "1");
                        listview.setAdapter(adapter_subGroup);
                    } else {
                        edit_announcement_selectedids = intenti.getStringExtra("edit_announcement_selectedids");
                        flag_addsubgrp = "1";
                        webservices();
                    }
                }
            } else {
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
                Intent i = new Intent(SubGroupSelectionList.this, SubGroupSelectionList.class);
                i.putExtra("parentId", list_subGroup.get(position).getSubgrpId());
                i.putExtra("subgroupname", list_subGroup.get(position).getSubgroup_name());
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

                /*for (SubGoupData p : adapter_subGroup.getBox()) {
                    if (p.box) {
                        result += "\n" + p.getSubgrpId();
                        count = count + 1;
                    }
                }*/
                //Toast.makeText(this, result+"\n"+"Total Amount:="+totalAmount, Toast.LENGTH_LONG).show();

                //   Toast.makeText(AddMembers.this, result + "\n" + "Count:=" + count, Toast.LENGTH_LONG).show();
                Log.d("TOUCHBASE", "@@@@@@@@@--" + count);
                if (selected.size() <= 0) {
                    Toast.makeText(SubGroupSelectionList.this, "Please Select at least 1 Group ", Toast.LENGTH_LONG).show();
                } else {

                    ArrayList<String> finalSelection = new ArrayList<String>(selected);

                    Intent returnIntent = new Intent();
                    //returnIntent.putExtra("result", list_subGroup);
                    returnIntent.putExtra("result", finalSelection);

                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                }
            }
        });
        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateSubGroup.class);

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

        Log.e("Response", "PARAMETERS " + Constant.GetSubGrpDirectoryList + " :- " + arrayList.toString());
        if (InternetConnection.checkConnection(context)){
            new WebConnectionAsyncAnnouncement(Constant.GetSubGrpDirectoryList, arrayList, SubGroupSelectionList.this).execute();
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    public class WebConnectionAsyncAnnouncement extends AsyncTask<String, Object, Object> {
        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(SubGroupSelectionList.this, R.style.TBProgressBar);
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

            progressDialog.dismiss();
            //	Log.d("response","Do post"+ result.toString());

            if (result != "") {
                Log.d("Response", "@@ " + result.toString());
                list_subGroup.clear();
                getSubGroup(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }
        }

    }


    private void getSubGroup(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject SubGroupListResult = jsonObj.getJSONObject("result");
            final String status = jsonObj.getString("status");
            if (status.equals("0"))
            {
                JSONArray SubGroupResult = SubGroupListResult.getJSONArray("subGrpList");
                int n = SubGroupResult.length();
                /*if ( n == 0 ) {
                    Intent intent = new Intent(SubGroupSelectionList.this, SubGroupDetails.class);
                    intent.putExtra("subgroupid", parentId);
                    intent.putExtra("subgroupname", groupName);
                    startActivity(intent);
                    finish();
                }*/
                for (int i = 0; i < n; i++) {
                    JSONObject objects = SubGroupResult.getJSONObject(i);
                    //JSONObject objects = object.getJSONObject("Subgroup");

                    SubGoupData data = new SubGoupData();

                    data.setSubgroup_name(objects.getString("subgrpTitle").toString());
                    data.setNo_of_members(objects.getString("noOfmem").toString());
                    data.setSubgrpId(objects.getString("subgrpId").toString());
                    data.setHasSubgroups(objects.getString("hasSubgroup").toString());

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
                        adapter_subGroup = new SubGroupSelectionAdapter(SubGroupSelectionList.this, R.layout.subgroup_selection_list_item, list_subGroup, "0");
                    else
                        adapter_subGroup = new SubGroupSelectionAdapter(SubGroupSelectionList.this, R.layout.subgroup_selection_list_item, list_subGroup, "1");
                    listview.setAdapter(adapter_subGroup);
                }

                if ( selected.contains(parentId)) {
                    for(int i=0;i<list_subGroup.size();i++) {
                        list_subGroup.get(i).setBox(true);
                    }
                } else {

                    boolean allSelected = true;

                    for (int i = 0; i < list_subGroup.size(); i++) {
                        String subGroup = list_subGroup.get(i).getSubgrpId();
                        //selected.remove(subGroup);
                        if ( ! selected.contains(subGroup)) {
                            allSelected = false;
                            break;
                        }
                    }

                    if ( allSelected ) {
                        for (int i = 0; i < list_subGroup.size(); i++) {
                            String subGroup = list_subGroup.get(i).getSubgrpId();
                            selected.remove(subGroup);
                            list_subGroup.get(i).setBox(false);
                        }
                    } else {
                        for (int i = 0; i < list_subGroup.size(); i++) {
                            String subGroup = list_subGroup.get(i).getSubgrpId();
                            //selected.remove(subGroup);
                            if (selected.contains(subGroup)) {
                                list_subGroup.get(i).setBox(true);
                            }
                        }
                    }
                }
                adapter_subGroup.notifyDataSetChanged();

            } else {
                Toast.makeText(SubGroupSelectionList.this, "Sorry. Something went wrong", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter_subGroup.notifyDataSetChanged();
        Log.e("AdapterRefreshed", "Adapter is refreshed");
        //if ( parentId.equals("0") ) return;

        for (int i = 0; i < list_subGroup.size(); i++) {
            String subGroup = list_subGroup.get(i).getSubgrpId();
            boolean isPresent = selected.contains(subGroup);
            Log.e("isPresent", "Value of is present : "+isPresent);
            list_subGroup.get(i).setBox(isPresent);
            if (selected.contains(subGroup)) {
                list_subGroup.get(i).setBox(true);
            } else {
                list_subGroup.get(i).setBox(false);
            }
        }

    }

    public String getParentId() {
        return parentId;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if (resultCode == Activity.RESULT_CANCELED) {
               // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)‌​;
                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                webservices();
            } else if ( resultCode == RESULT_OK) {
                ArrayList<String> finalSelection = new ArrayList<String>(selected);

                Intent returnIntent = new Intent();
                //returnIntent.putExtra("result", list_subGroup);
                returnIntent.putExtra("result", finalSelection);

                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                finish();
                //Toast.makeText(context, "Here we come", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /*@Override
    public void onBackPressed() {
        //super.onBackPressed();
        if ( grandParentIdsStk.isEmpty() ) {
            //Utils.popupback(SubGroupSelectionList.this);
        } else {
            Intent intent = new Intent(SubGroupSelectionList.this, SubGroupSelectionList.class);
            String[] fields = grandParentIdsStk.pop().split("@@@");
            intent.putExtra("parentId", fields[0]);  // fields of 0 means grandParentId

            intent.putExtra("subgroupname", fields[1]); // fields of 1 means name of grand parent subgroup
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            try {
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0);
            } catch (ClassCastException cc) {
                cc.printStackTrace();
            }
        }
    }*/
}
