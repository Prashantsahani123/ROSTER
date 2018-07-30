package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.RotaryLibraryListAdapter;
import com.SampleApp.row.Data.RotaryLibraryData;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
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

/**
 * Created by admin on 22-05-2017.
 */

public class FAQActivity extends Activity {
    public TextView tv_title;
    private ImageView iv_backbutton;
    private String grpId;
    private String ROTARY_LIBRARY_FILE="rotary_lib.json";
    private ArrayList<RotaryLibraryData> rotaryLibraryList = new ArrayList<>();
    public RotaryLibraryListAdapter rv_adapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutmanager;
    private Context context;
    private EditText edt_search;
    public String search = "";
    public String grpID,moduleID,moduleName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rotarylibrarylist);
        rotaryLibraryList = new ArrayList<>();
        context = this;

        moduleName = getIntent().getStringExtra("moduleName");
        grpID = getIntent().getStringExtra("grpID");
        moduleID = getIntent().getStringExtra("moduleID");

        actionbarfunction();
        init();
        loadRotaryLibraryFromServer();

    }

    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText(moduleName);
    }

    private void init() {
        grpId = PreferenceManager.getPreference(FAQActivity.this,PreferenceManager.GROUP_ID);

        edt_search = (EditText)findViewById(R.id.edt_search);
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = s.length();
                ArrayList<RotaryLibraryData> tempArrayList = new ArrayList<>();
                for (RotaryLibraryData c : rotaryLibraryList) {
                    if (c.getTitle().toLowerCase().contains(s.toString().toLowerCase())) {
                        tempArrayList.add(c);
                    }
                }

                rv_adapter = new RotaryLibraryListAdapter(context, tempArrayList);
                mRecyclerView.setAdapter(rv_adapter);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutmanager = new LinearLayoutManager(FAQActivity.this);
        mRecyclerView.setLayoutManager(mLayoutmanager);


    }

    public void loadRotaryLibraryFromServer(){
//        Hashtable<String,String> params = new Hashtable<>();
//        params.put("grpId",grpId);
        try{

            final ProgressDialog progressDialog = new ProgressDialog(FAQActivity.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject();

            requestData.put("SearchText","");
            requestData.put("moduleID",moduleID);
            requestData.put("grpID",grpID);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constant.GetEntityInfo1,
                    requestData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();

                            try {
                                getResult(response);
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
                            Toast.makeText(FAQActivity.this, "Failed to receive Data from server . Please try again.", Toast.LENGTH_LONG).show();

                        }
                    }
            );
            AppController.getInstance().addToRequestQueue(FAQActivity.this, request);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getResult(JSONObject response){

        try{
            Utils.log("Response:" + response);

            JSONObject jsonTBGetRotaryLibraryResult = response.getJSONObject("TBEntityInfoResult");
            final String status = jsonTBGetRotaryLibraryResult.getString("status");
            if (status.equals("0")) {
                    JSONArray EntityInfoResultArray = jsonTBGetRotaryLibraryResult.getJSONArray("EntityInfoResult");
                    if(EntityInfoResultArray.length()>0)
                    {
                        for(int i=0;i<EntityInfoResultArray.length();i++){
                            JSONObject entityObject = EntityInfoResultArray.getJSONObject(i);
                            RotaryLibraryData data = new RotaryLibraryData();
                            data.setModuleName(moduleName);
                            data.setTitle(entityObject.get("enname").toString());
                            data.setDescription(entityObject.get("descptn").toString());

                            rotaryLibraryList.add(data);
                        }
                    }
            }

            rv_adapter = new RotaryLibraryListAdapter(FAQActivity.this, rotaryLibraryList);
            mRecyclerView.setAdapter(rv_adapter);
        }catch (JSONException e){
            Utils.log("Error is : "+e);
            e.printStackTrace();
        }

    }



}
