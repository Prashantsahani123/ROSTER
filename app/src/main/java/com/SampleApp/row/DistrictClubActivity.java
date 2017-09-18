package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.ClubAdapter;
import com.SampleApp.row.Data.FindAClubResultData;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by admin on 02-05-2017.
 */

public class DistrictClubActivity extends Activity {

    private TextView tv_title, tv_no_records_found;
    private ImageView iv_backbutton;
    private String authtoken = "", searchterm = "";
    private String SEARCH_CLUB_API = "https://apiuat.rotary.org:8443/v1.1/clubs/search?";
    private LinearLayout llMainWrapper;
    Context context;
    private ArrayList<FindAClubResultData> listresult = new ArrayList<>();
    public ClubAdapter rv_adapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutmanager;
    private EditText edt_search;
    public String search = "";
    public String keyword = "";
    public String country = "";
    public String stateProvinceCity = "";
    public String district = "";
    public String meetingDay = "";

    // Fields Used Only For Near me

    public String distance = "";
    public String distanceUnit = "";
    public String meetingTime = "";
    public String currentLat = "";
    public String currentLong = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_clubs);
        context = this;
        llMainWrapper = (LinearLayout) findViewById(R.id.mainWrapper);
        llMainWrapper.setVisibility(View.GONE);
        listresult = new ArrayList<>();
        //authtoken = getIntent().getStringExtra("auth_token");
        //searchterm = getIntent().getStringExtra("searchterm");
        Intent i = getIntent();

        tv_no_records_found = (TextView) findViewById(R.id.tv_no_records_found);
        Utils.log("auth_token : " + authtoken);

        getClubsList();
        actionbarfunction();
        init();
    }


    public class SearchClubTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.TBProgressBar);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            try {
                url = new URL(params[0]);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setRequestProperty("auth_token", authtoken);
                con.setRequestMethod("GET");
                con.setDoInput(true);

                InputStream in = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                String line = "";
                StringBuffer buffer = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                }
                br.close();
                in.close();
                return new String(buffer);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Utils.log("Response is : " + s);
            getResult(s);
        }
    }

    public void getClubsList() {
        if (InternetConnection.checkConnection(this)) {

            // code to send anyclub data to server
            try {
                Hashtable<String, String> params = new Hashtable<>();
                String groupId = PreferenceManager.getPreference(context, PreferenceManager.GROUP_ID);

                params.put("groupId", groupId);
                params.put("search", "");


                try {
                    final ProgressDialog progressDialog = new ProgressDialog(context, R.style.TBProgressBar);
                    progressDialog.setCancelable(false);
                    progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                    progressDialog.show();

                    JSONObject requestData = new JSONObject(new Gson().toJson(params));
                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.POST,
                            Constant.DT_GET_CLUBS,
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
                                    Toast.makeText(context, "Failed to receive Data from server . Please try again.", Toast.LENGTH_LONG).show();

                                }
                            }
                    );
                    request.setRetryPolicy(new DefaultRetryPolicy(
                            Constant.VOLLEY_MAX_REQUEST_TIMEOUT,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));
                    AppController.getInstance().addToRequestQueue(context, request);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            //  String u = SEARCH_CLUB_API + searchterm;
            // Utils.log("Search URL is : " + u);

            // new SearchClubTask().execute(u);

            /*JsonArrayRequest request = new JsonArrayRequest(
                    url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Utils.log("Search Result : "+response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Utils.log("Error is : "+ error);
                            error.printStackTrace();
                        }
                    }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("auth_token", authtoken);
                    return map;
                }
            };

            AppController.getInstance().addToRequestQueue(context, request);
            */

        }

    }

    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutmanager = new LinearLayoutManager(DistrictClubActivity.this);
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
                    rv_adapter = new ClubAdapter(context, listresult, authtoken);
                    mRecyclerView.setAdapter(rv_adapter);
                    rv_adapter.setOnClubSelectedListener(onClubSelectedListener);
                    return;
                }

                ArrayList<FindAClubResultData> temp = new ArrayList<FindAClubResultData>();

                int listcount = listresult.size();

                for (int j = 0; j < listcount; j++) {
                    FindAClubResultData data = listresult.get(j);
                    //String name = data.getClubName() + ", " + data.getLocation().getInternationalProvince();
                    String name = data.getClubName();

                    if (name.toLowerCase().contains(search.toLowerCase())) {
                        temp.add(listresult.get(j));
                    }
                }
                rv_adapter = new ClubAdapter(context, temp, authtoken);
                mRecyclerView.setAdapter(rv_adapter);
                rv_adapter.setOnClubSelectedListener(onClubSelectedListener);
                if (listresult.size() == 0) {
                    mRecyclerView.setVisibility(View.GONE);
                    tv_no_records_found.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    tv_no_records_found.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText("Clubs");
    }

    ClubAdapter.OnClubSelectedListener onClubSelectedListener = new ClubAdapter.OnClubSelectedListener() {
        @Override
        public void onClubSelected(int position) {
            //Intent intent = new Intent(context, ClubInfo.class);
            Intent intent = new Intent(context, NewClubInfo.class);
            intent.putExtra("auth_token", authtoken);
            intent.putExtra("clubData", rv_adapter.getItems().get(position));// listresult.get(position));
            startActivity(intent);
        }
    };

    public void getResult(String result) {

        try {
            JSONObject json = new JSONObject(result);
            JSONObject jsonTBGetClubResult = json.getJSONObject("ClubListResult");
            final String status = jsonTBGetClubResult.getString("status");
            if (status.equalsIgnoreCase("0")) {
                JSONArray jsonClubResult = jsonTBGetClubResult.getJSONArray("Clubs");
                int count = jsonClubResult.length();
                llMainWrapper.setVisibility(View.VISIBLE);
                if (count > 0) {
                    listresult.clear();

                    Type listType = new TypeToken<List<FindAClubResultData>>() {
                    }.getType();
                    listresult = new Gson().fromJson(jsonClubResult.toString(), listType);
                    rv_adapter = new ClubAdapter(DistrictClubActivity.this, listresult, authtoken);
                    mRecyclerView.setAdapter(rv_adapter);

                    rv_adapter.setOnClubSelectedListener(onClubSelectedListener);

                } else {
                    if (listresult.size() == 0) {
                        mRecyclerView.setVisibility(View.GONE);
                        tv_no_records_found.setVisibility(View.VISIBLE);
                    } else {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        tv_no_records_found.setVisibility(View.GONE);
                    }
                }
            }
//            else if (status.equalsIgnoreCase("1")) {
//                mRecyclerView.setVisibility(View.GONE);
//                tv_no_records_found.setVisibility(View.VISIBLE);
//            }

        } catch (Exception e) {
            e.printStackTrace();
            mRecyclerView.setVisibility(View.GONE);
            tv_no_records_found.setVisibility(View.VISIBLE);
            tv_no_records_found.setText("Something went wrong. Please retry again");
        }
    }
}