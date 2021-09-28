package com.NEWROW.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.DistrictCommitteeRVAdapter;
import com.NEWROW.row.Adapter.ServiceDirectoryCategoryRVAdapter;
import com.NEWROW.row.Data.DistrictCommitteeData;
import com.NEWROW.row.Data.ServiceDirectoryCategoryData;
import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.Utils;
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

public class DistrictCommitteeWithCategory extends Activity{

    private TextView tv_title,tv_no_records_found;
    private ImageView iv_backbutton,iv_actionbtn;
    private RecyclerView mRecyclerView,categoryRecyclerview;
    private RecyclerView.LayoutManager mLayoutmanager,categoryLayoutmanager;
    private ArrayList<DistrictCommitteeData> committeeList = new ArrayList<>();
    public DistrictCommitteeRVAdapter rv_adapter;
    ServiceDirectoryCategoryRVAdapter categoryRVAdapter;
    private EditText edt_search;
    public String search = "";
    private String DISTRICT_COMMITTEE_FILE = "DistrictCommittee.json";;
    private Spinner sp_year;
    private Context context;
    private String grpId,categoryName,categoryId;

    private ArrayList<DistrictCommitteeData> districtListData = new ArrayList<DistrictCommitteeData>();

    private DistrictCommitteeRVAdapter districtCommiteeListAdapter;
    ArrayList<ServiceDirectoryCategoryData> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.district_committee);
        committeeList = new ArrayList<>();

        context = this;

        categoryId = getIntent().getExtras().getString("categoryId");
        categoryName = getIntent().getExtras().getString("categoryName");

        actionbarfunction();
        init();
        //loadFile();
        getDistrictData();
    }

    private void getDistrictData() {

        Hashtable<String, String> params = new Hashtable<>();
        params.put("DistrictCommitteID",categoryId);
        params.put("groupID",PreferenceManager.getPreference(context,PreferenceManager.GROUP_ID));

        try {

            final ProgressDialog progressDialog = new ProgressDialog(DistrictCommitteeWithCategory.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject(new Gson().toJson(params));
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constant.districtCommitteeDetails ,
                    requestData,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();

                            Utils.log("Responce : "+response);

                            JSONObject ServiceCategoriesDataList = null;

                            try {
                                ServiceCategoriesDataList = response.getJSONObject("TBDistrictCommitteeDetailsResult");
                                final String status = ServiceCategoriesDataList.getString("status");
                                if (status.equals("0")) {
                                    JSONObject Result = ServiceCategoriesDataList.getJSONObject("Result");
                                    JSONArray jsonServiceDirectoryData = Result.getJSONArray("districtCommitteeWithoutCatList");

                                    if(jsonServiceDirectoryData.length()>0){

                                        for (int i = 0; i < jsonServiceDirectoryData.length(); i++) {

                                            JSONObject objects = jsonServiceDirectoryData.getJSONObject(i);
                                            String districtId = objects.getString("Fk_DistrictCommitteeID").toString();
                                            String profileId = objects.getString("fk_Member_profileID").toString();
                                            String memberName = objects.getString("name");
                                            String mobileNo = objects.getString("MobileNumber");
                                            String mailId = objects.getString("MailID");
                                            String districtDesignation = objects.getString("DistrictDesignation");
                                            String clubName = objects.getString("ClubName");
                                            String image  = objects.getString("img");
                                            String type = objects.getString("type");
                                            String classification = objects.getString("classification");
                                            String Keywords = objects.getString("Keywords");
                                            String BusinessName = objects.getString("BusinessName");
                                            String Designation = objects.getString("Designation");
                                            String BusinessAddress = objects.getString("BusinessAddress");
                                            String RotaryID = objects.getString("RotaryID");
                                            String DonarReco = objects.getString("DonarReco");

                                            DistrictCommitteeData gd = new DistrictCommitteeData(districtId, profileId, memberName, mobileNo, mailId, districtDesignation, clubName, image, type, classification,Keywords,BusinessName, Designation, BusinessAddress, RotaryID,DonarReco);
                                            districtListData.add(gd);
                                        }

                                        //commiteeAdapter = new DirectoryCommiteeListAdapter(ServiceCattegoryList.this, R.layout.directory_commitee_list_item, serviceDirectoryListDatas,"0");
                                        //memberListview.setAdapter(commiteeAdapter);

                                        districtCommiteeListAdapter = new DistrictCommitteeRVAdapter(DistrictCommitteeWithCategory.this,districtListData);

                                        mRecyclerView.setAdapter(districtCommiteeListAdapter);
                                    }

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
                            Toast.makeText(DistrictCommitteeWithCategory.this, "Failed to receive category from server . Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
            );
            Utils.log("API : " + Constant.districtCommitteeDetails + " Params : " + params);
            AppController.getInstance().addToRequestQueue(DistrictCommitteeWithCategory.this, request);

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                rv_adapter = new DistrictCommitteeRVAdapter(DistrictCommitteeWithCategory.this, committeeList);
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
                rv_adapter = new DistrictCommitteeRVAdapter(DistrictCommitteeWithCategory.this, committeeList);
                mRecyclerView.setAdapter(rv_adapter);
                Utils.log("Loaded from local file.");
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
        tv_title.setText(categoryName);
    }

    private void init() {
        grpId = PreferenceManager.getPreference(context,PreferenceManager.GROUP_ID);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutmanager = new LinearLayoutManager(DistrictCommitteeWithCategory.this);
        categoryLayoutmanager = new LinearLayoutManager(DistrictCommitteeWithCategory.this);
        mRecyclerView.setLayoutManager(mLayoutmanager);
        categoryRecyclerview = (RecyclerView)findViewById(R.id.categoryRecyclerview);
        categoryRecyclerview.setHasFixedSize(true);
        categoryRecyclerview.setLayoutManager(categoryLayoutmanager);
        edt_search = (EditText)findViewById(R.id.edt_search);
        tv_no_records_found = (TextView)findViewById(R.id.tv_no_records_found);
        sp_year = (Spinner) findViewById(R.id.sp_year);

        sp_year.setVisibility(View.GONE);

        districtCommiteeListAdapter = new DistrictCommitteeRVAdapter(DistrictCommitteeWithCategory.this,districtListData);
        mRecyclerView.setAdapter(null);

        edt_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                tv_no_records_found.setVisibility(View.GONE);
                categoryRecyclerview.setVisibility(View.GONE);


                search = edt_search.getText().toString();
                districtCommiteeListAdapter.filter(search);

                if(districtCommiteeListAdapter.getList().size()<=0){
                    tv_no_records_found.setVisibility(View.VISIBLE);
                    categoryRecyclerview.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                }else {
                    tv_no_records_found.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void loadBODFromServer() {
        Hashtable<String, String> params = new Hashtable<>();
        params.put("grpId", PreferenceManager.getPreference(DistrictCommitteeWithCategory.this,PreferenceManager.GROUP_ID));
        params.put("searchText",search);

        try {
            final ProgressDialog progressDialog = new ProgressDialog(DistrictCommitteeWithCategory.this, R.style.TBProgressBar);
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
                            Toast.makeText(DistrictCommitteeWithCategory.this, "Failed to receive Data from server . Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
            );
            AppController.getInstance().addToRequestQueue(DistrictCommitteeWithCategory.this, request);
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
