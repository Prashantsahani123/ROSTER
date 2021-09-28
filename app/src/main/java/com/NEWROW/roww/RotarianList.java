package com.NEWROW.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.RotarianListAdapter;
import com.NEWROW.row.Data.RotarianData;
import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.Utils;
import com.android.volley.DefaultRetryPolicy;
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
 * Created by admin on 25-04-2017.
 */

public class RotarianList extends Activity {

    private TextView tv_title;
    private ImageView iv_backbutton;
    public String name, classification, city, districtNo, mobileNo = "";
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutmanager;
    private ArrayList<RotarianData> rotarianList = new ArrayList<>();
    public RotarianListAdapter rv_adapter;
    private EditText edt_search;
    public String search = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rotarianlist);
        rotarianList = new ArrayList<>();
        actionbarfunction();
        init();

        Intent i = getIntent();
        name = i.getStringExtra("name");
        classification = i.getStringExtra("classification");
        city = i.getStringExtra("club");
        districtNo = i.getStringExtra("district_number");
        //Add By Gaurav
        mobileNo = i.getStringExtra("mobile_number");

        if (InternetConnection.checkConnection(getApplicationContext())) {
            webservices();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText("Find a Rotarian");
    }

    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutmanager = new LinearLayoutManager(RotarianList.this);
        mRecyclerView.setLayoutManager(mLayoutmanager);
        edt_search = (EditText) findViewById(R.id.edt_search);

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search = edt_search.getText().toString();

                if (edt_search.getText().toString().trim().equalsIgnoreCase("")) {
                    rv_adapter = new RotarianListAdapter(RotarianList.this, rotarianList);
                    mRecyclerView.setAdapter(rv_adapter);
                    return;
                }

                ArrayList<RotarianData> temp = new ArrayList<RotarianData>();

                int listcount = rotarianList.size();

                for (int j = 0; j < listcount; j++) {
                    String name = rotarianList.get(j).getMemberName();
                    String designation = rotarianList.get(j).getDesignation();
                    if (name.toLowerCase().contains(search.toLowerCase()) || designation.toLowerCase().contains(search.toLowerCase())) {
                        RotarianData data = new RotarianData();
                        data.setMasterUID(rotarianList.get(j).getMasterUID());
                        data.setProfileId(rotarianList.get(j).getProfileId());
                        data.setMemberName(rotarianList.get(j).getMemberName());
                        data.setMemberMobile(rotarianList.get(j).getMemberMobile());
                        data.setDesignation(rotarianList.get(j).getDesignation());
                        data.setClubName(rotarianList.get(j).getClubName());
                        data.setPic(rotarianList.get(j).getPic().replaceAll(" ", "%20"));
                        temp.add(data);
                    }
                    rv_adapter = new RotarianListAdapter(RotarianList.this, temp);
                    mRecyclerView.setAdapter(rv_adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void webservices() {
        Hashtable<String, String> params = new Hashtable<>();
        params.put("name", name);
        params.put("classification", classification);
        params.put("club", city);
        params.put("district_number", districtNo);
        params.put("memberMobile", mobileNo);//Extra_field Add by Gaurav
        Log.d("Response", "PARAMETERS " + Constant.GETRotarianList + " :- " + params.toString());


        try {
            final ProgressDialog progressDialog = new ProgressDialog(RotarianList.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject(new Gson().toJson(params));
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constant.GETRotarianList,
                    requestData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();

                            JSONObject result = null;
                            try {
                                result = new JSONObject(response.toString());
                                JSONObject jsonTBGetRotarianResult = result.getJSONObject("TBGetRotarianResult");
                                final String status = jsonTBGetRotarianResult.getString("status");
                                if (status.equals("0")) {
                                    JSONArray jsonRotarianResult = jsonTBGetRotarianResult.getJSONArray("RotarianResult");
                                    int rotarianCount = jsonRotarianResult.length();
                                    if (rotarianCount > 0) {
                                        rotarianList.clear();
                                        for (int i = 0; i < rotarianCount; i++) {

                                            RotarianData data = new RotarianData();
                                            JSONObject jsonData = jsonRotarianResult.getJSONObject(i);
                                            data.setMasterUID(jsonData.get("masterUID").toString());
                                            data.setProfileId(jsonData.get("profileID").toString());
                                            data.setMemberName(jsonData.get("memberName").toString());
                                            data.setMemberMobile(jsonData.get("memberMobile").toString());
                                            data.setDesignation(jsonData.get("designation").toString());
                                            data.setClubName(jsonData.get("clubName").toString());
                                            data.setPic(jsonData.get("pic").toString().replaceAll(" ", "%20"));
                                            rotarianList.add(data);
                                        }

                                        rv_adapter = new RotarianListAdapter(RotarianList.this, rotarianList);
                                        mRecyclerView.setAdapter(rv_adapter);


                                    } else {
                                        rv_adapter = new RotarianListAdapter(RotarianList.this, rotarianList);
                                        mRecyclerView.setAdapter(rv_adapter);
                                    }
                                } else if (status.equals("1")) {
                                    rv_adapter = new RotarianListAdapter(RotarianList.this, rotarianList);
                                    mRecyclerView.setAdapter(rv_adapter);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Log.e("Touchbase", "♦Error : " + error);
                            error.printStackTrace();
                            Toast.makeText(RotarianList.this, "Failed to receive data from server . Please try again.", Toast.LENGTH_LONG).show();

                        }
                    }
            );
            request.setRetryPolicy(new DefaultRetryPolicy(
                    Constant.VOLLEY_MAX_REQUEST_TIMEOUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            Utils.log("API is : " + Constant.GETRotarianList + " Parameters : " + params);
            AppController.getInstance().addToRequestQueue(RotarianList.this, request);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
