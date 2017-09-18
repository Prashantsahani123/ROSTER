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

import com.SampleApp.row.Adapter.BODListAdapter;
import com.SampleApp.row.Adapter.RotaryLibraryListAdapter;
import com.SampleApp.row.Data.RotaryLibraryData;
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
 * Created by admin on 22-05-2017.
 */

public class RotaryLibraryActivity extends Activity {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rotarylibrarylist);
        rotaryLibraryList = new ArrayList<>();
        context = this;
        actionbarfunction();
        init();
        loadFile();

    }

    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText("Rotary Library");
    }

    private void init() {
        grpId = PreferenceManager.getPreference(RotaryLibraryActivity.this,PreferenceManager.GROUP_ID);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutmanager = new LinearLayoutManager(RotaryLibraryActivity.this);
        mRecyclerView.setLayoutManager(mLayoutmanager);
        edt_search = (EditText)findViewById(R.id.edt_search);

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search = edt_search.getText().toString();
                if (edt_search.getText().toString().trim().equalsIgnoreCase("")) {
                    rv_adapter = new RotaryLibraryListAdapter(context, rotaryLibraryList);
                    mRecyclerView.setAdapter(rv_adapter);
                    return;
                }

                ArrayList<RotaryLibraryData> temp = new ArrayList<RotaryLibraryData>();

                int listcount = rotaryLibraryList.size();

                for(int i = 0;i<listcount;i++){
                    String name = rotaryLibraryList.get(i).getTitle();
                    if(name.toLowerCase().contains(search.toLowerCase())){
                        RotaryLibraryData data = new RotaryLibraryData();
                        data.setTitle(rotaryLibraryList.get(i).getTitle());
                        data.setDescription(rotaryLibraryList.get(i).getDescription());
                        temp.add(data);
                    }

                    rv_adapter = new RotaryLibraryListAdapter(RotaryLibraryActivity.this, temp);
                    mRecyclerView.setAdapter(rv_adapter);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void loadRotaryLibraryFromServer(){
//        Hashtable<String,String> params = new Hashtable<>();
//        params.put("grpId",grpId);
        try{

            final ProgressDialog progressDialog = new ProgressDialog(RotaryLibraryActivity.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject();
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constant.GETRotaryLibrary,
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
                            Toast.makeText(RotaryLibraryActivity.this, "Failed to receive Data from server . Please try again.", Toast.LENGTH_LONG).show();

                        }
                    }
            );
            AppController.getInstance().addToRequestQueue(RotaryLibraryActivity.this, request);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getResult(JSONObject response){
        JSONObject json = null;
        try{
            Utils.log("Response:" + response);

            JSONObject jsonTBGetRotaryLibraryResult = response.getJSONObject("TBGetRotaryLibraryResult");
            final String status = jsonTBGetRotaryLibraryResult.getString("status");
            if (status.equals("0")) {
                try {
                    FileOutputStream fout = openFileOutput(ROTARY_LIBRARY_FILE, MODE_PRIVATE);
                    fout.write(response.toString().getBytes());
                    fout.close();
                } catch(IOException ioe) {
                    Utils.log("Error is : "+ioe);
                    ioe.printStackTrace();
                }
                reloadFile();
            }

        }catch (JSONException e){
            Utils.log("Error is : "+e);
            e.printStackTrace();
        }

    }

    public void  loadFile (){

        try {
            FileInputStream fin = openFileInput(ROTARY_LIBRARY_FILE);
            String fieldData = new String();
            byte[] buffer = new byte[1024];
            while ( fin.available() != 0 ) {
                int n = fin.read(buffer);
                fieldData = fieldData + new String(buffer, 0, n);
            }
            fin.close();
            JSONObject json = null;
            json = new JSONObject(fieldData);
            JSONObject jsonTBGetRotaryLibraryResult = json.getJSONObject("TBGetRotaryLibraryResult");
            JSONArray jsonRotaryLibraryListResult = jsonTBGetRotaryLibraryResult.getJSONArray("RotaryLibraryListResult");

            int count = jsonRotaryLibraryListResult.length();
            if(count >0){
                rotaryLibraryList.clear();
                for (int i = 0; i < count; i++) {

                    RotaryLibraryData data = new RotaryLibraryData();
                    JSONObject jsonData = jsonRotaryLibraryListResult.getJSONObject(i);
                    data.setTitle(jsonData.get("title").toString());
                    data.setDescription(jsonData.get("description").toString());
                    rotaryLibraryList.add(data);
                }

                rv_adapter = new RotaryLibraryListAdapter(RotaryLibraryActivity.this, rotaryLibraryList);
                mRecyclerView.setAdapter(rv_adapter);

                if (InternetConnection.checkConnection(this)) {
                    loadRotaryLibraryFromServer();
                }else{
                    Toast.makeText(context, "No internet conenction", Toast.LENGTH_LONG).show();
                }
            }
            Utils.log("Loaded from local file");

        } catch(FileNotFoundException fne) { // this is very first time data loading from server
            Utils.log("Rotary Library File are not present in local file");
            if (InternetConnection.checkConnection(this)) {
                loadRotaryLibraryFromServer();
            }else{
                Toast.makeText(context, "No internet conenction", Toast.LENGTH_LONG).show();
            }
        } catch(IOException fne) {
            Utils.log("Rotary Library File are not present in local file");
            if (InternetConnection.checkConnection(this)) {
                loadRotaryLibraryFromServer();
            }
        } catch (Exception jse) {
            Utils.log("Error is : "+jse);
            jse.printStackTrace();
        }
    }

    public void  reloadFile (){

        try {
            FileInputStream fin = openFileInput(ROTARY_LIBRARY_FILE);
            String fieldData = new String();
            byte[] buffer = new byte[1024];
            while ( fin.available() != 0 ) {
                int n = fin.read(buffer);
                fieldData = fieldData + new String(buffer, 0, n);
            }
            fin.close();
            JSONObject json = null;
            json = new JSONObject(fieldData);
            JSONObject jsonTBGetRotaryLibraryResult = json.getJSONObject("TBGetRotaryLibraryResult");
            JSONArray jsonRotaryLibraryListResult = jsonTBGetRotaryLibraryResult.getJSONArray("RotaryLibraryListResult");

            int count = jsonRotaryLibraryListResult.length();
            if(count >0){
                rotaryLibraryList.clear();
                for (int i = 0; i < count; i++) {

                    RotaryLibraryData data = new RotaryLibraryData();
                    JSONObject jsonData = jsonRotaryLibraryListResult.getJSONObject(i);
                    data.setTitle(jsonData.get("title").toString());
                    data.setDescription(jsonData.get("description").toString());
                    rotaryLibraryList.add(data);
                }

                rv_adapter = new RotaryLibraryListAdapter(RotaryLibraryActivity.this, rotaryLibraryList);
                mRecyclerView.setAdapter(rv_adapter);


            }
            Utils.log("Loaded from local file");

        } catch(FileNotFoundException fne) { // this is very first time data loading from server
            Utils.log("Rotary Library File are not present in local file");
            if (InternetConnection.checkConnection(this)) {
                loadRotaryLibraryFromServer();
            }else{
                Toast.makeText(context, "No internet conenction", Toast.LENGTH_LONG).show();
            }
        } catch(IOException fne) {
            Utils.log("Rotary Library File are not present in local file");
            if (InternetConnection.checkConnection(this)) {
                loadRotaryLibraryFromServer();
            }
        } catch (Exception jse) {
            Utils.log("Error is : "+jse);
            jse.printStackTrace();
        }
    }

}
