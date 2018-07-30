package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.WebView;
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
 * Created by admin on 18-05-2017.
 */

public class ClubHistoryActivity extends Activity {
    public TextView tv_title, tv_clubName;
    private ImageView iv_backbutton;
    private String grpId;
    private String CLUB_HISTORY_FILE = "club_history.json";
    private WebView webview;

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clubhistory);
        context = this;
        actionbarfunction();
        init();
        loadFile();


    }

    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText("Club History");
    }

    private void init() {
        grpId = PreferenceManager.getPreference(ClubHistoryActivity.this, PreferenceManager.GROUP_ID);
        tv_clubName = (TextView) findViewById(R.id.tv_clubName);
        webview = (WebView) findViewById(R.id.webview);
    }

    public void loadClubHistoryFromServer() {
        Hashtable<String, String> params = new Hashtable<>();
        params.put("grpId", grpId);

        try {
            final ProgressDialog progressDialog = new ProgressDialog(ClubHistoryActivity.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject(new Gson().toJson(params));
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constant.GETClubHistory,
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
                            Toast.makeText(ClubHistoryActivity.this, "Failed to receive Data from server . Please try again.", Toast.LENGTH_LONG).show();

                        }
                    }
            );
            AppController.getInstance().addToRequestQueue(ClubHistoryActivity.this, request);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getResult(JSONObject response) {
        JSONObject json = null;
        try {
            Utils.log("Response:" + response);

            JSONObject jsonTBClubHistoryResult = response.getJSONObject("TBClubHistoryResult");
            final String status = jsonTBClubHistoryResult.getString("status");
            if (status.equals("0")) {
                try {
                    FileOutputStream fout = openFileOutput(grpId + "_" + CLUB_HISTORY_FILE, MODE_PRIVATE);
                    fout.write(response.toString().getBytes());
                    fout.close();
                } catch (IOException ioe) {
                    Utils.log("Error is : " + ioe);
                    ioe.printStackTrace();
                }
                reloadFile();
            }

        } catch (JSONException e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }

    }

    public void loadFile() {

        try {
            FileInputStream fin = openFileInput(grpId + "_" + CLUB_HISTORY_FILE);
            String fieldData = new String();
            byte[] buffer = new byte[1024];
            while (fin.available() != 0) {
                int n = fin.read(buffer);
                fieldData = fieldData + new String(buffer, 0, n);
            }
            fin.close();
            JSONObject json = new JSONObject(fieldData);
            JSONObject jsonTBClubHistoryResult = json.getJSONObject("TBClubHistoryResult");
            JSONObject jsonclubHistory = jsonTBClubHistoryResult.getJSONObject("clubHistory");
            String clubName = jsonclubHistory.getString("clubName");
            String description = jsonclubHistory.getString("description");
            tv_clubName.setText(clubName);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.loadData(description, "text/html", "utf-8");
            Utils.log("Loaded from local file");
            Utils.log("Club history File are not present in local file");
            Utils.log("Club history File are not present in local file");


            Utils.log("Club history File are not present in local file");
            if (InternetConnection.checkConnection(this)) {
                loadClubHistoryFromServer();
            } else {
                Toast.makeText(context, "No internet conenction", Toast.LENGTH_LONG).show();
            }
        } catch (FileNotFoundException fne) {  // This means very first time
            Utils.log("Club history File are not present in local file");
            if (InternetConnection.checkConnection(this)) {
                loadClubHistoryFromServer();
            } else {
                Toast.makeText(context, "No internet conenction", Toast.LENGTH_LONG).show();
            }
        } catch (IOException fne) {
            Utils.log("Club history File are not present in local file");
            if (InternetConnection.checkConnection(this)) {
                loadClubHistoryFromServer();
            }
        } catch (Exception jse) {
            Utils.log("Error is : " + jse);
            jse.printStackTrace();
        }

    }

    public void reloadFile() {

        try {
            FileInputStream fin = openFileInput(grpId + "_" + CLUB_HISTORY_FILE);
            String fieldData = new String();
            byte[] buffer = new byte[1024];
            while (fin.available() != 0) {
                int n = fin.read(buffer);
                fieldData = fieldData + new String(buffer, 0, n);
            }
            fin.close();
            JSONObject json = new JSONObject(fieldData);
            JSONObject jsonTBClubHistoryResult = json.getJSONObject("TBClubHistoryResult");
            JSONObject jsonclubHistory = jsonTBClubHistoryResult.getJSONObject("clubHistory");
            String clubName = jsonclubHistory.getString("clubName");
            String description = jsonclubHistory.getString("description");
            tv_clubName.setText(clubName);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.loadData(description, "text/html", "utf-8");
            Utils.log("Loaded from local file");
            Utils.log("Club history File are not present in local file");


        } catch (FileNotFoundException fne) {  // This means very first time
            Utils.log("Club history File are not present in local file");
        } catch (IOException fne) {
            Utils.log("Club history File are not present in local file");
        } catch (Exception jse) {
            Utils.log("Error is : " + jse);
            jse.printStackTrace();
        }
    }

}
