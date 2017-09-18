package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Created by admin on 23-05-2017.
 */

public class MyClubActivity extends Activity {
    private TextView tv_title,tv_clubname,tv_meetingplace,tv_meetingday,tv_meetingtime,tv_clubid,tv_ridistrict;
    private ImageView iv_backbutton;
    private String grpId;
    private String MYCLUB_FILE="myclub.json";
    private Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myclub);
        context = this;
        actionbarfunction();
        init();
        loadFile();
    }

    public void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText("My Club");
    }

    private void init() {
        grpId = PreferenceManager.getPreference(MyClubActivity.this,PreferenceManager.GROUP_ID);
        tv_clubname = (TextView)findViewById(R.id.tv_clubname);
        tv_meetingplace = (TextView)findViewById(R.id.tv_meetingplace);
        tv_meetingday = (TextView)findViewById(R.id.tv_meetingday);
        tv_meetingtime = (TextView)findViewById(R.id.tv_meetingtime);
        tv_clubid = (TextView)findViewById(R.id.tv_clubid);
        tv_ridistrict = (TextView)findViewById(R.id.tv_ridistrict);


    }


    public void loadFile(){
        try {
            FileInputStream fin = openFileInput(grpId+"_"+MYCLUB_FILE);
            String fieldData = new String();
            byte[] buffer = new byte[1024];
            while ( fin.available() != 0 ) {
                int n = fin.read(buffer);
                fieldData = fieldData + new String(buffer, 0, n);
            }
            fin.close();
            JSONObject json = new JSONObject(fieldData);
            JSONObject jsonTBGetGroupResult = json.getJSONObject("TBGetGroupResult");
            JSONObject jsongetGroupDetailResult = jsonTBGetGroupResult.getJSONObject("getGroupDetailResult");
            String clubname = jsongetGroupDetailResult.getString("ClubName");
            String meetingplace = jsongetGroupDetailResult.getString("MeetingPlace");
            String meetingday = jsongetGroupDetailResult.getString("MeetingDay");
            String meetingtime = jsongetGroupDetailResult.getString("MeetingTime");
            String clubId = jsongetGroupDetailResult.getString("ClubID");
            String districtno = jsongetGroupDetailResult.getString("DistrictNumber");

            tv_clubname.setText(clubname);
            tv_meetingplace.setText(meetingplace);
            tv_meetingday.setText(meetingday);
            tv_meetingtime.setText(meetingtime);
            tv_clubid.setText(clubId);
            tv_ridistrict.setText(districtno);


            Utils.log("Loaded from local file");

        } catch(FileNotFoundException fne) {  // This means very first time
            Utils.log(" MyClub File are not present in local file");
            if (InternetConnection.checkConnection(this)) {
                loadMyClubFromServer();
            } else {
                Toast.makeText(context, "No internet conenction", Toast.LENGTH_LONG).show();
            }
        } catch(IOException fne) {
            Utils.log("MyClub File are not present in local file");
            if (InternetConnection.checkConnection(this)) {
                loadMyClubFromServer();
            }
        } catch (Exception jse) {
            Utils.log("Error is : "+jse);
            jse.printStackTrace();
        }
    }


    public void loadMyClubFromServer(){
        Hashtable<String, String> params = new Hashtable<>();
        params.put("grpID", grpId);

        try {
            final ProgressDialog progressDialog = new ProgressDialog(MyClubActivity.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject(new Gson().toJson(params));
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constant.GETClubDetails,
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
                            Toast.makeText(MyClubActivity.this, "Failed to receive Data from server . Please try again.", Toast.LENGTH_LONG).show();

                        }
                    }
            );
            AppController.getInstance().addToRequestQueue(MyClubActivity.this, request);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getResult(JSONObject response){
        JSONObject json = null;
        try{
            Utils.log("Response:" + response);

            JSONObject jsonTBGetGroupResult = response.getJSONObject("TBGetGroupResult");
            final String status = jsonTBGetGroupResult.getString("status");
            if (status.equals("0")) {
                try {
                    FileOutputStream fout = openFileOutput(grpId+"_"+MYCLUB_FILE, MODE_PRIVATE);
                    fout.write(response.toString().getBytes());
                    fout.close();
                } catch(IOException ioe) {
                    Utils.log("Error is : "+ioe);
                    ioe.printStackTrace();
                }
                loadFile();
            }

        }catch (JSONException e){
            Utils.log("Error is : "+e);
            e.printStackTrace();
        }

    }
}
