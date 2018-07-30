package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.PastPresidentAdapter;
import com.SampleApp.row.Data.PastPresidentData;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.TBPrefixes;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.sql.PastPresidentMasterModel;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by admin on 28-04-2017.
 */

public class PastPresidentActivity extends Activity {
    TextView tv_title;
    private ImageView iv_backbutton;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutmanager;
    private EditText edt_search;
    public String search = "";
    public PastPresidentAdapter rv_adapter;
    private ArrayList<PastPresidentData> pastPresidentList = new ArrayList<>();
    String updatedOn = "";
    private String grpId;
    private PastPresidentMasterModel model;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.past_president);
        init();
        actionbarfunction();
        loadFromDB();

    }

    public void loadFromDB(){
        Log.d("Touchbase", "Trying to load past president from local db");

        pastPresidentList = model.getPastPresidentList(grpId);
        boolean isDataAvailable = model.isDataAvailable(Long.parseLong(grpId));

        if (!isDataAvailable) {
            Log.d("Touchbase---@@@@@@@@", "Loading from server");
            if (InternetConnection.checkConnection(this))
                webservices();

            else
                Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
       } else {
            rv_adapter = new PastPresidentAdapter(PastPresidentActivity.this, pastPresidentList);
            mRecyclerView.setAdapter(rv_adapter);

            if (InternetConnection.checkConnection(this)) {
                webservices();
                Log.d("---------------", "Check for update gets called------");
            } else {
               // Toast.makeText(this, "No internet connection to get Updated Records", Toast.LENGTH_LONG).show();
            }
       }
    }

    private void init(){
        grpId = PreferenceManager.getPreference(PastPresidentActivity.this,PreferenceManager.GROUP_ID);
        model = new PastPresidentMasterModel(this);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutmanager = new LinearLayoutManager(PastPresidentActivity.this);
        mRecyclerView.setLayoutManager(mLayoutmanager);
        edt_search = (EditText)findViewById(R.id.edt_search);
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                search = edt_search.getText().toString();
                try{
                    pastPresidentList = new ArrayList<>();
                    pastPresidentList = model.search(search,grpId);
                    rv_adapter = new PastPresidentAdapter(PastPresidentActivity.this, pastPresidentList);
                    mRecyclerView.setAdapter(rv_adapter);

//                    if (InternetConnection.checkConnection(PastPresidentActivity.this)) {
//                        searchOnline();
//                    } else {
//                        Toast.makeText(PastPresidentActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
//                    }

                }catch(Exception e){
                    Log.e("ROW", "♦♦♦♦Error on search is : "+e.getMessage());
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText("Past Presidents");
    }

    private void webservices() {
        Hashtable<String, String> params = new Hashtable<>();
        params.put("GroupId", grpId);
        params.put("SearchText",search);
        updatedOn = PreferenceManager.getPreference(this, TBPrefixes.PASTPRESIDENT_UPDATED_ON+"_"+grpId,"1970/01/01 00:00:00");
        params.put("updateOn",updatedOn);

        try {
            final ProgressDialog progressDialog = new ProgressDialog(PastPresidentActivity.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject(new Gson().toJson(params));

            Utils.log("Parameters : "+Constant.GETPastPresident+"    "+requestData);
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constant.GETPastPresident,
                    requestData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();

                            try {
                                getResult(response.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Log.e("ROW", "♦Error : " + error);
                            error.printStackTrace();
                            Toast.makeText(PastPresidentActivity.this, "Failed to receive Data from server . Please try again.", Toast.LENGTH_LONG).show();

                        }
                    }
            );
            AppController.getInstance().addToRequestQueue(PastPresidentActivity.this, request);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void getResult(String result) {

        JSONObject json = null;
        try{
            Utils.log("Response : "+result);
            json = new JSONObject(result);
            JSONObject jsonTBPastPresidentListResult = json.getJSONObject("TBPastPresidentListResult");
            final String status = jsonTBPastPresidentListResult.getString("status");
            if (status.equals("0")) {
                updatedOn = jsonTBPastPresidentListResult.getString("updatedOn");
                JSONObject jsonTBPastPresidentList = jsonTBPastPresidentListResult.getJSONObject("TBPastPresidentList");
                final ArrayList<PastPresidentData> newRecords = new ArrayList<PastPresidentData>();

                JSONArray jsonNewPastPresidentList = jsonTBPastPresidentList.getJSONArray("newRecords");
                int newPastPresidentListCount = jsonNewPastPresidentList.length();

                for (int i = 0; i < newPastPresidentListCount; i++) {

                    PastPresidentData data = new PastPresidentData();

                    JSONObject result_object = jsonNewPastPresidentList.getJSONObject(i);

                    data.setTenureYear(result_object.getString("TenureYear").toString());
                    data.setPhotopath(result_object.getString("PhotoPath").toString());
                    data.setPastPresidentId(result_object.getString("PastPresidentId").toString());
                    data.setMemberName(result_object.getString("MemberName").toString());
                    data.setGrpID(grpId);
                    newRecords.add(data);
                }

                final ArrayList<PastPresidentData> updatedRecords = new ArrayList<PastPresidentData>();

                JSONArray jsonUpdatedPastPresidentList = jsonTBPastPresidentList.getJSONArray("updatedRecords");
                int UpdatedPastPresidentListCount = jsonUpdatedPastPresidentList.length();

                for (int i = 0; i < UpdatedPastPresidentListCount; i++) {

                    PastPresidentData data = new PastPresidentData();

                    JSONObject result_object = jsonUpdatedPastPresidentList.getJSONObject(i);

                    data.setTenureYear(result_object.getString("TenureYear").toString());
                    data.setPhotopath(result_object.getString("PhotoPath").toString());
                    data.setPastPresidentId(result_object.getString("PastPresidentId").toString());
                    data.setMemberName(result_object.getString("MemberName").toString());
                    data.setGrpID(grpId);
                    updatedRecords.add(data);

                }

                final ArrayList<PastPresidentData> deletedRecords = new ArrayList<PastPresidentData>();
                String jsonDeletedPastPresidentList = jsonTBPastPresidentList.getString("deletedRecords");
                int deleteListCount = 0;
                if(!jsonDeletedPastPresidentList.equalsIgnoreCase("")){

                    String[]deletedArray = jsonDeletedPastPresidentList.split(",");
                    deleteListCount = deletedArray.length;

                    for (int i = 0; i < deleteListCount; i++) {
                        PastPresidentData data = new PastPresidentData();
                        data.setPastPresidentId(String.valueOf(deletedArray[i].toString()));
                        deletedRecords.add(data);

                    }

                }

                Handler PastPresidentdatahandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        boolean saved = model.syncData(Long.parseLong(grpId), newRecords, updatedRecords, deletedRecords);
                        if (!saved) {
                            Log.e("SyncFailed------->", "Failed to update data in local db. Retrying in 2 seconds");
                            sendEmptyMessageDelayed(0, 2000);
                        } else {
                            PreferenceManager.savePreference(PastPresidentActivity.this, TBPrefixes.PASTPRESIDENT_UPDATED_ON+"_"+grpId, updatedOn);
                            pastPresidentList = new ArrayList<>();
                            pastPresidentList = model.getPastPresidentList(grpId);
                            rv_adapter = new PastPresidentAdapter(PastPresidentActivity.this, pastPresidentList);
                            mRecyclerView.setAdapter(rv_adapter);
                        }
                    }
                };


                int overAllCount = newPastPresidentListCount + UpdatedPastPresidentListCount + deleteListCount;

                System.out.println("Number of records received for pastPresidentList  : " + overAllCount);
                if (newPastPresidentListCount + UpdatedPastPresidentListCount + deleteListCount != 0) {

                    PastPresidentdatahandler.sendEmptyMessageDelayed(0, 1000);
                } else {
                    Log.e("NoUpdate", "No updates found");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    private void searchOnline() {
//        Hashtable<String, String> params = new Hashtable<>();
//        params.put("GroupId", grpId);
//        params.put("SearchText",search);
//        updatedOn = PreferenceManager.getPreference(this, TBPrefixes.PASTPRESIDENT_UPDATED_ON+"_"+grpId,"1970/01/01 00:00:00");
//        params.put("updateOn",updatedOn);
//
//
//        try {
//            final ProgressDialog progressDialog = new ProgressDialog(PastPresidentActivity.this, R.style.TBProgressBar);
//            progressDialog.setCancelable(false);
//            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//            progressDialog.show();
//
//            JSONObject requestData = new JSONObject(new Gson().toJson(params));
//            JsonObjectRequest request = new JsonObjectRequest(
//                    Request.Method.POST,
//                    Constant.GETPastPresident,
//                    requestData,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            progressDialog.dismiss();
//
//                            try {
//                                getSearchResults(response.toString());
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            progressDialog.dismiss();
//                            Log.e("ROW", "♦Error : " + error);
//                            error.printStackTrace();
//                            Toast.makeText(PastPresidentActivity.this, "Failed to receive Data from server . Please try again.", Toast.LENGTH_LONG).show();
//
//                        }
//                    }
//            );
//
//            AppController.getInstance().addToRequestQueue(PastPresidentActivity.this, request);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }

//    public void getSearchResults(String result){
//        JSONObject json = null;
//        try{
//            json = new JSONObject(result);
//            JSONObject jsonTBGetRotarianResult = json.getJSONObject("TBGetBODResult");
//            final String status = jsonTBGetRotarianResult.getString("status");
//            if (status.equals("0")) {
//                JSONArray jsonBODresult = jsonTBGetRotarianResult.getJSONArray("BODResult");
//                int bodCount = jsonBODresult.length();
//                if (bodCount > 0) {
//                    pastPresidentList.clear();
//                    for (int i = 0; i < bodCount; i++) {
//
//                        PastPresidentData data = new PastPresidentData();
//                        JSONObject jsonData = jsonBODresult.getJSONObject(i);
////                        data.setMasterUID(jsonData.get("masterUID").toString());
////                        data.setGrpID(jsonData.get("grpID").toString());
////                        data.setProfileID(jsonData.get("profileID").toString());
////                        data.setMemberName(jsonData.get("memberName").toString());
////                        data.setPic(jsonData.get("pic").toString());
////                        data.setMemberMobile(jsonData.get("membermobile").toString());
////                        data.setMemeberDesignation(jsonData.get("MemberDesignation").toString());
//
//                        pastPresidentList.add(data);
//                    }
//
//                    rv_adapter = new PastPresidentAdapter(PastPresidentActivity.this, pastPresidentList);
//                    mRecyclerView.setAdapter(rv_adapter);
//                    rv_adapter.notifyDataSetChanged();
//
//                }else{
//                    pastPresidentList.clear();
//                    rv_adapter = new PastPresidentAdapter(PastPresidentActivity.this, pastPresidentList);
//                    mRecyclerView.setAdapter(rv_adapter);
//                    rv_adapter.notifyDataSetChanged();
//                }
//
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }
}
