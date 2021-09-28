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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.WebListAdapter;
import com.NEWROW.row.Data.WebLinkListData;
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
 * Created by admin on 27-04-2017.
 */

public class WebLinkActivity extends Activity {
    private TextView tv_title,tv_no_records_found;
    private ImageView iv_backbutton;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutmanager;
    public String search = "";
    private ArrayList<WebLinkListData> webLinkList = new ArrayList<>();
    public WebListAdapter rv_adapter;
    private EditText edt_search;
    private String WEB_LINKS_FILE="web_links.json";
    private String grpId;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weblink);
        webLinkList = new ArrayList<>();
        context = this;
        actionbarfunction();
        init();
        loadFile();

    }

    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_no_records_found = (TextView) findViewById(R.id.tv_no_records_found);
        tv_title.setText("Web Links");
    }

    private void init() {

        grpId = PreferenceManager.getPreference(WebLinkActivity.this,PreferenceManager.GROUP_ID);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutmanager = new LinearLayoutManager(WebLinkActivity.this);
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
                    rv_adapter = new WebListAdapter(context, webLinkList);
                    mRecyclerView.setAdapter(rv_adapter);
                    return;
                }

                ArrayList<WebLinkListData> temp = new ArrayList<WebLinkListData>();

                int listcount = webLinkList.size();

                for(int j = 0;j<listcount;j++){

                    String name = webLinkList.get(j).getTitle();
                    String description = webLinkList.get(j).getDescription();

                    if(name.toLowerCase().contains(search.toLowerCase())|| description.toLowerCase().contains(search.toLowerCase())){
                        WebLinkListData data = new WebLinkListData();
                        data.setWebLinkId(webLinkList.get(j).getWebLinkId());
                        data.setGroupId(webLinkList.get(j).getGroupId());
                        data.setTitle(webLinkList.get(j).getTitle());
                        data.setDescription(webLinkList.get(j).getDescription());
                        data.setLinkUrl(webLinkList.get(j).getLinkUrl());
                        temp.add(data);
                    }

                    rv_adapter = new WebListAdapter(WebLinkActivity.this, temp);
                    mRecyclerView.setAdapter(rv_adapter);

                }


//                try{
//                    if (InternetConnection.checkConnection(WebLinkActivity.this)) {
//                        searchOnline();
//                    } else {
//                        Toast.makeText(WebLinkActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
//                    }
//
//                }catch(Exception e){
//                    Log.e("ROW", "♦♦♦♦Error on search is : "+e.getMessage());
//                    e.printStackTrace();
//                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void loadFile(){

        try {

            FileInputStream fin = openFileInput(grpId+"_"+WEB_LINKS_FILE);
            String fieldData = new String();
            byte[] buffer = new byte[1024];

            while ( fin.available() != 0 ) {
                int n = fin.read(buffer);
                fieldData = fieldData + new String(buffer, 0, n);
            }
            fin.close();

            JSONObject json = new JSONObject(fieldData);
            JSONObject jsonTBGetWebLinkListResult = json.getJSONObject("TBGetWebLinkListResult");
            JSONArray jsonWebLinkListResult = jsonTBGetWebLinkListResult.getJSONArray("WebLinkListResult");

            int count = jsonWebLinkListResult.length();

            if(count >0){

                webLinkList.clear();

                for (int i = 0; i < count; i++) {

                    WebLinkListData data = new WebLinkListData();
                    JSONObject jsonData = jsonWebLinkListResult.getJSONObject(i);
                    data.setWebLinkId(jsonData.get("WeblinkId").toString());
                    data.setGroupId(jsonData.get("GroupId").toString());
                    data.setTitle(jsonData.get("Title").toString());
                    data.setDescription(jsonData.get("fullDesc").toString());
                    data.setLinkUrl(jsonData.get("LinkUrl").toString());
                    webLinkList.add(data);
                }

                rv_adapter = new WebListAdapter(WebLinkActivity.this, webLinkList);
                mRecyclerView.setAdapter(rv_adapter);

            }
            Utils.log("Loaded from local file");
            if (InternetConnection.checkConnection(this)) {
                loadWebLinksFromServer();
            }else{
                Toast.makeText(context, "No internet conenction", Toast.LENGTH_LONG).show();
            }
        } catch(FileNotFoundException fne) { // this is very first time data loading from server
            Utils.log("Web Link File are not present in local file");
            if (InternetConnection.checkConnection(this)) {
                loadWebLinksFromServer();
            }else{
                Toast.makeText(context, "No internet conenction", Toast.LENGTH_LONG).show();
            }
        } catch(IOException fne) {
            Utils.log("Web Link File are not present in local file");
            if (InternetConnection.checkConnection(this)) {
                loadWebLinksFromServer();
            }
        } catch (Exception jse) {
            Utils.log("Error is : "+jse);
            jse.printStackTrace();
        }

    }

    public void reloadFile(){
        try {

            FileInputStream fin = openFileInput(grpId+"_"+WEB_LINKS_FILE);
            String fieldData = new String();
            byte[] buffer = new byte[1024];

            while ( fin.available() != 0 ) {
                int n = fin.read(buffer);
                fieldData = fieldData + new String(buffer, 0, n);
            }
            fin.close();

            JSONObject json = new JSONObject(fieldData);
            JSONObject jsonTBGetWebLinkListResult = json.getJSONObject("TBGetWebLinkListResult");
            JSONArray jsonWebLinkListResult = jsonTBGetWebLinkListResult.getJSONArray("WebLinkListResult");

            int count = jsonWebLinkListResult.length();

            if(count >0) {

                webLinkList.clear();

                for (int i = 0; i < count; i++) {

                    WebLinkListData data = new WebLinkListData();
                    JSONObject jsonData = jsonWebLinkListResult.getJSONObject(i);
                    data.setWebLinkId(jsonData.get("WeblinkId").toString());
                    data.setGroupId(jsonData.get("GroupId").toString());
                    data.setTitle(jsonData.get("Title").toString());
                    data.setDescription(jsonData.get("fullDesc").toString());
                    data.setLinkUrl(jsonData.get("LinkUrl").toString());

                    webLinkList.add(data);
                }

                rv_adapter = new WebListAdapter(WebLinkActivity.this, webLinkList);
                mRecyclerView.setAdapter(rv_adapter);
            }

            Utils.log("Loaded from local file");

        } catch(FileNotFoundException fne) { // this is very first time data loading from server
            Utils.log("Web Link File are not present in local file");
        } catch(IOException fne) {
            Utils.log("Web Link File are not present in local file");
        } catch (Exception jse) {
            Utils.log("Error is : "+jse);
            jse.printStackTrace();
        }

    }


    public void loadWebLinksFromServer(){

        Hashtable<String, String> params = new Hashtable<>();
        params.put("GroupId", PreferenceManager.getPreference(WebLinkActivity.this,PreferenceManager.GROUP_ID));
        params.put("SearchText",search);

        try {

        //    Utils.log("Response:" + response);




            final ProgressDialog progressDialog = new ProgressDialog(WebLinkActivity.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject(new Gson().toJson(params));

            Utils.log("web link request paramtr---  " +    Constant.GETWebLinkList + "---"+requestData.toString());



            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constant.GETWebLinkList,
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
                            Toast.makeText(WebLinkActivity.this, "Failed to receive Data from server . Please try again.", Toast.LENGTH_LONG).show();

                        }
                    }
            );

            AppController.getInstance().addToRequestQueue(WebLinkActivity.this, request);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void getResult(JSONObject response){
        JSONObject json = null;
        try{
            Utils.log("Response:" + response);

            JSONObject jsonGetWebLinkResult = response.getJSONObject("TBGetWebLinkListResult");
            final String status = jsonGetWebLinkResult.getString("status");
            if (status.equals("0")) {
                try {
                    FileOutputStream fout = openFileOutput(grpId+"_"+WEB_LINKS_FILE, MODE_PRIVATE);
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




//    private void searchOnline(){
//        Hashtable<String, String> params = new Hashtable<>();
//        params.put("GroupId", PreferenceManager.getPreference(WebLinkActivity.this,PreferenceManager.GROUP_ID));
//        params.put("SearchText",search);
//
//        try {
//            final ProgressDialog progressDialog = new ProgressDialog(WebLinkActivity.this, R.style.TBProgressBar);
//            progressDialog.setCancelable(false);
//            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//            progressDialog.show();
//
//            JSONObject requestData = new JSONObject(new Gson().toJson(params));
//            JsonObjectRequest request = new JsonObjectRequest(
//                    Request.Method.POST,
//                    Constant.GETWebLinkList,
//                    requestData,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            progressDialog.dismiss();
//
//                            try {
//                                getResult(response.toString());
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
//                            Toast.makeText(WebLinkActivity.this, "Failed to receive Data from server . Please try again.", Toast.LENGTH_LONG).show();
//
//                        }
//                    }
//            );
//
//            AppController.getInstance().addToRequestQueue(WebLinkActivity.this, request);
//
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

//    private void webservices() {
//        Hashtable<String, String> params = new Hashtable<>();
//        params.put("GroupId", PreferenceManager.getPreference(WebLinkActivity.this,PreferenceManager.GROUP_ID));
//        params.put("SearchText",search);
//
//        try {
//            final ProgressDialog progressDialog = new ProgressDialog(WebLinkActivity.this, R.style.TBProgressBar);
//            progressDialog.setCancelable(false);
//            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//            progressDialog.show();
//
//            JSONObject requestData = new JSONObject(new Gson().toJson(params));
//            JsonObjectRequest request = new JsonObjectRequest(
//                    Request.Method.POST,
//                    Constant.GETWebLinkList,
//                    requestData,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            progressDialog.dismiss();
//
//                            try {
//                                getResult(response.toString());
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
//                            Toast.makeText(WebLinkActivity.this, "Failed to receive Data from server . Please try again.", Toast.LENGTH_LONG).show();
//
//                        }
//                    }
//            );
//
//            AppController.getInstance().addToRequestQueue(WebLinkActivity.this, request);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//    }



//    public void getResult(String result) {
//
//        JSONObject json = null;
//        try{
//            json = new JSONObject(result);
//            JSONObject jsonGetWebLinkResult = json.getJSONObject("TBGetWebLinkListResult");
//            final String status = jsonGetWebLinkResult.getString("status");
//            if (status.equals("0")) {
//                JSONArray jsonWebLinkResult = jsonGetWebLinkResult.getJSONArray("WebLinkListResult");
//                int webLinkCount = jsonWebLinkResult.length();
//                if (webLinkCount > 0) {
//                    webLinkList.clear();
//                    for (int i = 0; i < webLinkCount; i++) {
//
//                        WebLinkListData data = new WebLinkListData();
//                        JSONObject jsonData = jsonWebLinkResult.getJSONObject(i);
//                        data.setWebLinkId(jsonData.get("WeblinkId").toString());
//                        data.setGroupId(jsonData.get("GroupId").toString());
//                        data.setTitle(jsonData.get("Title").toString());
//                        data.setDescription(jsonData.get("Description").toString());
//                        data.setLinkUrl(jsonData.get("LinkUrl").toString());
//
//                        webLinkList.add(data);
//                    }
//
//                    rv_adapter = new WebListAdapter(WebLinkActivity.this, webLinkList);
//                    mRecyclerView.setAdapter(rv_adapter);
//
//                }
//
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

}
