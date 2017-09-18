package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
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

import com.SampleApp.row.Adapter.BODListAdapter;
import com.SampleApp.row.Adapter.DistrictCommitteeRVAdapter;
import com.SampleApp.row.Data.BoardOfDirectorsData;
import com.SampleApp.row.Data.DistrictCommitteeData;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by admin on 26-04-2017.
 */

public class DistrictCommittee extends Activity{

    private TextView tv_title;
    private ImageView iv_backbutton,iv_actionbtn;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutmanager;
    private ArrayList<DistrictCommitteeData> committeeList = new ArrayList<>();
    public DistrictCommitteeRVAdapter rv_adapter;
    private EditText edt_search;
    public String search = "";
    private String DISTRICT_COMMITTEE_FILE = "DistrictCommittee.json";;

    private Context context;
    private String grpId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.district_committee);
        committeeList = new ArrayList<>();
        context = this;
        actionbarfunction();
        init();
        loadFile();
    }

    public void loadFile(){
        try{
            FileInputStream fin = openFileInput(grpId+"_"+ DISTRICT_COMMITTEE_FILE);
            String fieldData = new String();
            byte[] buffer = new byte[1024];
            while(fin.available()!= 0){
                int n = fin.read(buffer);
                fieldData = fieldData + new String(buffer,0,n);
            }
            fin.close();
            Utils.log("Committee Data : "+fieldData);
            JSONObject json = new JSONObject(fieldData);
            JSONObject jsonTBGetRotarianResult = json.getJSONObject("TBGetBODResult");
            final String status = jsonTBGetRotarianResult.getString("status");
            if (status.equals("0")) {
                JSONArray jsonCommitteeArray = jsonTBGetRotarianResult.getJSONArray("DistrictCommitteeResult");
                String committeeArray = jsonCommitteeArray.toString();

                TypeToken<ArrayList<DistrictCommitteeData>> typeToken = new TypeToken<ArrayList<DistrictCommitteeData>>(){};
                committeeList = new Gson().fromJson(committeeArray, typeToken.getType());
                rv_adapter = new DistrictCommitteeRVAdapter(DistrictCommittee.this, committeeList);
                mRecyclerView.setAdapter(rv_adapter);
                mRecyclerView.requestLayout();

                if (InternetConnection.checkConnection(this)) {
                    loadBODFromServer();
                }else{
                    Toast.makeText(context, "No internet conenction", Toast.LENGTH_LONG).show();
                }

                Utils.log("Loaded from local file");
                if (InternetConnection.checkConnection(this)) {
                    loadBODFromServer();
                } else {
                    Toast.makeText(context, "No internet conenction", Toast.LENGTH_LONG).show();
                }
            }
        }catch (FileNotFoundException fne){ // this is very first time data loading from server
            Utils.log("Board Of Directors Library File are not present in local file");
            fne.printStackTrace();
            if (InternetConnection.checkConnection(this)) {
                loadBODFromServer();
            }else{
                Toast.makeText(context, "No internet conenction", Toast.LENGTH_LONG).show();
            }
        }
        catch (IOException ioe){
            Utils.log("Board Of Directors File are not present in local file");
            if (InternetConnection.checkConnection(this)) {
                loadBODFromServer();
            }else{
                Toast.makeText(context, "No internet conenction", Toast.LENGTH_LONG).show();
            }
        }catch(Exception jse){
            Utils.log("Error is : "+jse);
            jse.printStackTrace();
        }
    }


    public void reloadFile(){
        try{
            FileInputStream fin = openFileInput(grpId+"_"+ DISTRICT_COMMITTEE_FILE);
            String fieldData = new String();
            byte[] buffer = new byte[1024];
            while(fin.available()!= 0){
                int n = fin.read(buffer);
                fieldData = fieldData + new String(buffer,0,n);
            }
            fin.close();
            JSONObject json = new JSONObject(fieldData);
            Utils.log("Committee Data : "+fieldData);
            JSONObject jsonTBGetRotarianResult = json.getJSONObject("TBGetBODResult");
            final String status = jsonTBGetRotarianResult.getString("status");
            if (status.equals("0")) {
                JSONArray jsonCommitteeArray = jsonTBGetRotarianResult.getJSONArray("DistrictCommitteeResult");
                String committeeArray = jsonCommitteeArray.toString();
                TypeToken<ArrayList<DistrictCommitteeData>> typeToken = new TypeToken<ArrayList<DistrictCommitteeData>>(){};
                committeeList = new Gson().fromJson(committeeArray, typeToken.getType());
                rv_adapter = new DistrictCommitteeRVAdapter(DistrictCommittee.this, committeeList);
                mRecyclerView.setAdapter(rv_adapter);
                Utils.log("Loaded from local file");
            }
        }catch (FileNotFoundException fne){ // this is very first time data loading from server
            Utils.log("Board Of Directors Library File are not present in local file");
            fne.printStackTrace();
            if (InternetConnection.checkConnection(this)) {
                loadBODFromServer();
            }else{
                Toast.makeText(context, "No internet conenction", Toast.LENGTH_LONG).show();
            }
        }
        catch (IOException ioe){
            Utils.log("Board Of Directors File are not present in local file");
            if (InternetConnection.checkConnection(this)) {
                loadBODFromServer();
            }else{
                Toast.makeText(context, "No internet conenction", Toast.LENGTH_LONG).show();
            }
        }catch(Exception jse){
            Utils.log("Error is : "+jse);
            jse.printStackTrace();
        }
    }
    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn= (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn.setImageResource(R.drawable.search_btn);
        tv_title.setText("District Committee");
    }

    private void init() {
        grpId = PreferenceManager.getPreference(context,PreferenceManager.GROUP_ID);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutmanager = new LinearLayoutManager(DistrictCommittee.this);
        mRecyclerView.setLayoutManager(mLayoutmanager);
        edt_search = (EditText)findViewById(R.id.edt_search);

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                search = edt_search.getText().toString();

                if (edt_search.getText().toString().trim().equalsIgnoreCase("")) {
                    rv_adapter = new DistrictCommitteeRVAdapter(DistrictCommittee.this, committeeList);
                    mRecyclerView.setAdapter(rv_adapter);
                    return;
                }

                ArrayList<DistrictCommitteeData> temp = new ArrayList<>();
                int listcount = committeeList.size();

                for(int j = 0;j<listcount;j++){
                    String name = committeeList.get(j).getMemberName();
                    String designation = committeeList.get(j).getMemberDesignation();
                    if(name.toLowerCase().contains(search.toLowerCase())|| designation.toLowerCase().contains(search.toLowerCase())){
                        DistrictCommitteeData data = committeeList.get(j);
                        temp.add(data);
                    }
                    rv_adapter = new DistrictCommitteeRVAdapter(DistrictCommittee.this, temp);
                    mRecyclerView.setAdapter(rv_adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void loadBODFromServer() {
        Hashtable<String, String> params = new Hashtable<>();
        params.put("grpId", PreferenceManager.getPreference(DistrictCommittee.this,PreferenceManager.GROUP_ID));
        params.put("searchText",search);

        try {
            final ProgressDialog progressDialog = new ProgressDialog(DistrictCommittee.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject(new Gson().toJson(params));
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constant.DT_GET_DISTRICT_COMMITTEE,
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
                            Toast.makeText(DistrictCommittee.this, "Failed to receive Data from server . Please try again.", Toast.LENGTH_LONG).show();

                        }
                    }
            );
            AppController.getInstance().addToRequestQueue(DistrictCommittee.this, request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getResult(String result) {
        Utils.log("Committee Data : "+result);
        JSONObject json = null;
        try{
            json = new JSONObject(result);
            JSONObject jsonTBGetRotarianResult = json.getJSONObject("TBGetBODResult");
            final String status = jsonTBGetRotarianResult.getString("status");
            if (status.equals("0")) {
                try{
                    FileOutputStream fout = openFileOutput(grpId+"_"+ DISTRICT_COMMITTEE_FILE,MODE_PRIVATE);
                    fout.write(result.toString().getBytes());
                    fout.close();
                }catch(IOException ioe){
                    Utils.log("Error is : "+ioe);
                    ioe.printStackTrace();
                }
                reloadFile();
            }
        }catch (JSONException jse){
            Utils.log("Error is : "+jse);
            jse.printStackTrace();
        }
    }

}
