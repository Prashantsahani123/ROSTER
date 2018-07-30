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
import com.SampleApp.row.Data.BoardOfDirectorsData;
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
 * Created by admin on 26-04-2017.
 */

public class BoardOfDirectorsActivity extends Activity{

    private TextView tv_title;
    private ImageView iv_backbutton,iv_actionbtn;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutmanager;
    private ArrayList<BoardOfDirectorsData> bodList = new ArrayList<>();
    public BODListAdapter rv_adapter;
    private EditText edt_search;
    public String search = "";
    private String BOARD_OF_DIRECTORS_FILE="bod.json";;
    private Context context;
    private String grpId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_of_directors_list);
        bodList = new ArrayList<>();
        context = this;
        actionbarfunction();
        init();
        loadFile();
    }

    public void loadFile(){
        try{
            FileInputStream fin = openFileInput(grpId+"_"+BOARD_OF_DIRECTORS_FILE);
            String fieldData = new String();
            byte[] buffer = new byte[1024];
            while(fin.available()!= 0){
                int n = fin.read(buffer);
                fieldData = fieldData + new String(buffer,0,n);
            }
            fin.close();
            JSONObject json = new JSONObject(fieldData);
            JSONObject jsonTBGetRotarianResult = json.getJSONObject("TBGetBODResult");
            final String status = jsonTBGetRotarianResult.getString("status");
            if (status.equals("0")) {
                JSONArray jsonBODresult = jsonTBGetRotarianResult.getJSONArray("BODResult");
                int bodCount = jsonBODresult.length();
                if (bodCount > 0) {
                    bodList.clear();
                    for (int i = 0; i < bodCount; i++) {

                        BoardOfDirectorsData data = new BoardOfDirectorsData();
                        JSONObject jsonData = jsonBODresult.getJSONObject(i);
                        data.setMasterUID(jsonData.get("masterUID").toString());
                        data.setGrpID(jsonData.get("grpID").toString());
                        data.setProfileID(jsonData.get("profileID").toString());
                        data.setMemberName(jsonData.get("memberName").toString());
                        data.setPic(jsonData.get("pic").toString());
                        data.setMemberMobile(jsonData.get("membermobile").toString());
                        data.setMemeberDesignation(jsonData.get("MemberDesignation").toString());
                        bodList.add(data);
                    }

                    rv_adapter = new BODListAdapter(BoardOfDirectorsActivity.this, bodList);
                    mRecyclerView.setAdapter(rv_adapter);
                    mRecyclerView.requestLayout();

                    if (InternetConnection.checkConnection(this)) {
                        loadBODFromServer();
                    }else{
                        Toast.makeText(context, "No internet conenction", Toast.LENGTH_LONG).show();
                    }
                }
                Utils.log("Loaded from local file");
                if (InternetConnection.checkConnection(this)) {
                    loadBODFromServer();
                }else{
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
            FileInputStream fin = openFileInput(grpId+"_"+BOARD_OF_DIRECTORS_FILE);
            String fieldData = new String();
            byte[] buffer = new byte[1024];
            while(fin.available()!= 0){
                int n = fin.read(buffer);
                fieldData = fieldData + new String(buffer,0,n);
            }
            fin.close();
            JSONObject json = new JSONObject(fieldData);
            JSONObject jsonTBGetRotarianResult = json.getJSONObject("TBGetBODResult");
            final String status = jsonTBGetRotarianResult.getString("status");
            if (status.equals("0")) {
                JSONArray jsonBODresult = jsonTBGetRotarianResult.getJSONArray("BODResult");
                int bodCount = jsonBODresult.length();
                if (bodCount > 0) {
                    bodList.clear();
                    for (int i = 0; i < bodCount; i++) {

                        BoardOfDirectorsData data = new BoardOfDirectorsData();
                        JSONObject jsonData = jsonBODresult.getJSONObject(i);
                        data.setMasterUID(jsonData.get("masterUID").toString());
                        data.setGrpID(jsonData.get("grpID").toString());
                        data.setProfileID(jsonData.get("profileID").toString());
                        data.setMemberName(jsonData.get("memberName").toString());
                        data.setPic(jsonData.get("pic").toString());
                        data.setMemberMobile(jsonData.get("membermobile").toString());
                        data.setMemeberDesignation(jsonData.get("MemberDesignation").toString());
                        bodList.add(data);
                    }

                    rv_adapter = new BODListAdapter(BoardOfDirectorsActivity.this, bodList);
                    mRecyclerView.setAdapter(rv_adapter);

                }
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
        tv_title.setText("Board Of Directors");
    }

    private void init() {
        grpId = PreferenceManager.getPreference(context,PreferenceManager.GROUP_ID);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutmanager = new LinearLayoutManager(BoardOfDirectorsActivity.this);
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
                    rv_adapter = new BODListAdapter(BoardOfDirectorsActivity.this, bodList);
                    mRecyclerView.setAdapter(rv_adapter);
                    return;
                }

                ArrayList<BoardOfDirectorsData> temp = new ArrayList<BoardOfDirectorsData>();
                int listcount = bodList.size();

                for(int j = 0;j<listcount;j++){
                    String name = bodList.get(j).getMemberName();
                    String designation = bodList.get(j).getMemeberDesignation();
                    if(name.toLowerCase().contains(search.toLowerCase())|| designation.toLowerCase().contains(search.toLowerCase())){
                        BoardOfDirectorsData data = new BoardOfDirectorsData();
                        data.setMasterUID(bodList.get(j).getMasterUID());
                        data.setGrpID(bodList.get(j).getGrpID());
                        data.setProfileID(bodList.get(j).getProfileID());
                        data.setMemberName(bodList.get(j).getMemberName());
                        data.setPic(bodList.get(j).getPic());
                        data.setMemberMobile(bodList.get(j).getMemberMobile());
                        data.setMemeberDesignation(bodList.get(j).getMemeberDesignation());
                        temp.add(data);
                    }
                    rv_adapter = new BODListAdapter(BoardOfDirectorsActivity.this, temp);
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
        params.put("grpId", PreferenceManager.getPreference(BoardOfDirectorsActivity.this,PreferenceManager.GROUP_ID));
        params.put("searchText",search);

        try {
            final ProgressDialog progressDialog = new ProgressDialog(BoardOfDirectorsActivity.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject(new Gson().toJson(params));
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constant.GETListOfBOD,
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
                            Toast.makeText(BoardOfDirectorsActivity.this, "Failed to receive Data from server . Please try again.", Toast.LENGTH_LONG).show();

                        }
                    }
            );

            AppController.getInstance().addToRequestQueue(BoardOfDirectorsActivity.this, request);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void getResult(String result) {

        JSONObject json = null;
        try{
            json = new JSONObject(result);
            JSONObject jsonTBGetRotarianResult = json.getJSONObject("TBGetBODResult");
            final String status = jsonTBGetRotarianResult.getString("status");
            if (status.equals("0")) {
                try{
                    FileOutputStream fout = openFileOutput(grpId+"_"+BOARD_OF_DIRECTORS_FILE,MODE_PRIVATE);
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
