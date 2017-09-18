package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Utils.Utils;
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

import com.SampleApp.row.Adapter.ServiceDirectoryCategoryRVAdapter;
import com.SampleApp.row.Data.ServiceDirectoryCategoryData;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;

/**
 * Created by user on 21-03-2017.
 */
public class ServiceCattegoryList extends Activity {
    TextView tv_title;
    ImageView iv_backbutton;
    private ImageView iv_actionbtn;
    boolean isSearchVisible = false;
    RelativeLayout rl_search;
    String grpId;
    String moduleId;
    ArrayList<ServiceDirectoryCategoryData> categoryList;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    ServiceDirectoryCategoryRVAdapter rv_adapter;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_cattegory_list);

        grpId = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID);
        moduleId = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MODULE_ID);

        Utils.log("Service category list activity");
        actionbarfuncton();
        init();
        if (InternetConnection.checkConnection(getApplicationContext())) {
            webservices(grpId, moduleId);

        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (InternetConnection.checkConnection(getApplicationContext())){
//            webservices(grpId,moduleId);
//
//        } else {
//            Toast.makeText(getApplicationContext(), "No internet connection. unable to get update from server", Toast.LENGTH_LONG).show();
//        }
//    }

    private void init() {
        rl_search = (RelativeLayout) findViewById(R.id.rl_search);
        categoryList = new ArrayList<ServiceDirectoryCategoryData>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(ServiceCattegoryList.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public void actionbarfuncton() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        title = getIntent().getExtras().getString("moduleName");
        tv_title.setText(title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn.setImageResource(R.drawable.search_btn);
        // iv_actionbtn.setVisibility(View.VISIBLE);
        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSearchVisible) {
                    rl_search.setVisibility(View.GONE);
                    isSearchVisible = false;
                } else {
                    rl_search.setVisibility(View.VISIBLE);
                    isSearchVisible = true;
                }

            }
        });
    }

    private void webservices(String groupId, String moduleId) {
        Hashtable<String, String> params = new Hashtable<>();
        params.put("groupId", groupId);
        params.put("moduleId", moduleId);
        try {
            final ProgressDialog progressDialog = new ProgressDialog(ServiceCattegoryList.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject(new Gson().toJson(params));
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constant.GetServiceDirectoryCattegory,
                    requestData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();

                            JSONObject result = null;
                            try {
                                result = new JSONObject(response.toString());
                                final String status = result.getString("status");
                                if (status.equals("0")) {
                                    JSONArray jsonServiceCategory = result.getJSONArray("ServiceCategoriesResult");
                                    int categoryCount = jsonServiceCategory.length();
                                    if (categoryCount > 0) {
                                        categoryList.clear();

                                        for (int i = 0; i < categoryCount; i++) {
                                            ServiceDirectoryCategoryData data = new ServiceDirectoryCategoryData();

                                            JSONObject jsondata = jsonServiceCategory.getJSONObject(i);
                                            data.setCategoryId(Integer.parseInt(jsondata.get("ID").toString()));
                                            data.setCategoryName(jsondata.get("CategoryName").toString());
                                            data.setTotalCount(Integer.parseInt(jsondata.get("TotalCount").toString()));
                                            categoryList.add(data);
                                        }
                                        rv_adapter = new ServiceDirectoryCategoryRVAdapter(ServiceCattegoryList.this, categoryList, title);
                                        mRecyclerView.setAdapter(rv_adapter);
                                    } else{
                                        Intent i = new Intent(ServiceCattegoryList.this,ServiceDirectoryList.class);
                                        i.putExtra("categoryId","0");
                                        i.putExtra("moduleName",title);
                                        startActivity(i);
                                        finish();

                                    }
                                } else{
                                    Intent i = new Intent(ServiceCattegoryList.this,ServiceDirectoryList.class);
                                    i.putExtra("categoryId","0");
                                    i.putExtra("moduleName",title);
                                    startActivity(i);
                                    finish();

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
                            Toast.makeText(ServiceCattegoryList.this, "Failed to receive category from server . Please try again.", Toast.LENGTH_LONG).show();

                        }
                    }
            );
            Utils.log("API : " + Constant.GetServiceDirectoryCattegory + " Params : " + params);
            AppController.getInstance().addToRequestQueue(ServiceCattegoryList.this, request);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
