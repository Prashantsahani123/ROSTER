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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.ClubAdapter;
import com.SampleApp.row.Data.FindAClubResultData;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.InternetConnection;
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

public class FindAClubResultActivity extends Activity {

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
    Spinner spnrMeetingday;
    String meetingday[] = {"Any", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    // Fields Used Only For Near me

    public String distance = "";
    public String distanceUnit = "";
    public String meetingTime = "";
    public String currentLat = "";
    public String currentLong = "";
    String tabSelected = "";// to know which tab user selected in previous screen


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_club_result);
        context = this;
        llMainWrapper = (LinearLayout) findViewById(R.id.mainWrapper);
        llMainWrapper.setVisibility(View.GONE);
        listresult = new ArrayList<>();
        //authtoken = getIntent().getStringExtra("auth_token");
        //searchterm = getIntent().getStringExtra("searchterm");
        Intent i = getIntent();

        if (i.hasExtra("tabSelected")) {
            tabSelected = i.getStringExtra("tabSelected");

            if (tabSelected.equalsIgnoreCase("Any Club")) {

                if (i.hasExtra("keyword")) {
                    keyword = i.getStringExtra("keyword");
                }
                if (i.hasExtra("country")) {
                    country = i.getStringExtra("country");
                }
                if (i.hasExtra("stateProvinceCity")) {
                    stateProvinceCity = i.getStringExtra("stateProvinceCity");
                }
                if (i.hasExtra("district")) {
                    district = i.getStringExtra("district");
                }
                if (i.hasExtra("meetingDay")) {
                    meetingDay = i.getStringExtra("meetingDay");
                }
            } else {
                if (i.hasExtra("distance")) {
                    distance = i.getStringExtra("distance");
                }
                if (i.hasExtra("distanceUnit")) {
                    distanceUnit = i.getStringExtra("distanceUnit");
                }
                if (i.hasExtra("meetingDay")) {
                    meetingDay = i.getStringExtra("meetingDay");
                }
                if (i.hasExtra("meetingTime")) {
                    meetingTime = i.getStringExtra("meetingTime");
                }
                if (i.hasExtra("currentLat")) {
                    currentLat = i.getStringExtra("currentLat");
                }
                if (i.hasExtra("currentLong")) {
                    currentLong = i.getStringExtra("currentLong");
                }
            }
        }
        tv_no_records_found = (TextView) findViewById(R.id.tv_no_records_found);
        Utils.log("auth_token : " + authtoken);

        findAClub();
        actionbarfunction();
        init();
        setSpinnerMeetingDay();
    }

    private void setSpinnerMeetingDay() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.layout_meetingday, R.id.textView, meetingday);
        spnrMeetingday.setAdapter(spinnerArrayAdapter);
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

    public void findAClub() {
        if (InternetConnection.checkConnection(this)) {
            if (tabSelected.equalsIgnoreCase("Any Club")) {

                // code to send anyclub data to server
                try {
                    Hashtable<String, String> params = new Hashtable<>();
                    params.put("keyword", keyword);
                    params.put("country", country);
                    params.put("stateProvinceCity", stateProvinceCity);
                    params.put("district", district);
                    params.put("meetingDay", meetingDay);

                    try {
                        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.TBProgressBar);
                        progressDialog.setCancelable(false);
                        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                        progressDialog.show();

                        JSONObject requestData = new JSONObject(new Gson().toJson(params));
                        JsonObjectRequest request = new JsonObjectRequest(
                                Request.Method.POST,
                                Constant.GETCLUBLIST,
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
                // code to send near me details to server
                try {
                    Hashtable<String, String> params = new Hashtable<>();
                    params.put("distance", distance);
                    params.put("distanceUnit", distanceUnit);
                    params.put("meetingDay", meetingDay);
                    params.put("meetingTime", meetingTime);
                    params.put("currentLat", currentLat);
                    params.put("currentLong", currentLong);

                    try {
                        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.TBProgressBar);
                        progressDialog.setCancelable(false);
                        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                        progressDialog.show();

                        JSONObject requestData = new JSONObject(new Gson().toJson(params));
                        JsonObjectRequest request = new JsonObjectRequest(
                                Request.Method.POST,
                                Constant.GETCLUBS_NEARME,
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
        mLayoutmanager = new LinearLayoutManager(FindAClubResultActivity.this);
        mRecyclerView.setLayoutManager(mLayoutmanager);
        spnrMeetingday = (Spinner) findViewById(R.id.spnrMeetingDay);
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


        if (meetingDay.equalsIgnoreCase("") || meetingDay.trim().equalsIgnoreCase("")) {
            spnrMeetingday.setVisibility(View.VISIBLE);
        } else {
            spnrMeetingday.setVisibility(View.GONE);
        }

        spnrMeetingday.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = spnrMeetingday.getSelectedItem().toString();
                if (str.equalsIgnoreCase("Any")) {
                    rv_adapter = new ClubAdapter(context, listresult, authtoken);
                    mRecyclerView.setAdapter(rv_adapter);
                    rv_adapter.setOnClubSelectedListener(onClubSelectedListener);
                } else {
                    ArrayList<FindAClubResultData> listByMeetingDays = new ArrayList<FindAClubResultData>();

                    int listcount = listresult.size();

                    for (int l = 0; l < listcount; l++) {
                        FindAClubResultData data = listresult.get(l);
                        String day = data.getMeetingDay();

                        if (day.toLowerCase().equalsIgnoreCase(spnrMeetingday.getSelectedItem().toString())) {
                            listByMeetingDays.add(listresult.get(l));
                        }
                    }
                    rv_adapter = new ClubAdapter(context, listByMeetingDays, authtoken);
                    mRecyclerView.setAdapter(rv_adapter);
                    rv_adapter.setOnClubSelectedListener(onClubSelectedListener);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText("Find a Club");
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
            JSONObject jsonTBGetClubResult = json.getJSONObject("TBGetClubResult");
            final String status = jsonTBGetClubResult.getString("status");
            if (status.equalsIgnoreCase("0")) {
                JSONArray jsonClubResult = jsonTBGetClubResult.getJSONArray("ClubResult");
                int count = jsonClubResult.length();
                llMainWrapper.setVisibility(View.VISIBLE);
                if (count > 0) {
                    listresult.clear();

                    Type listType = new TypeToken<List<FindAClubResultData>>() {
                    }.getType();
                    listresult = new Gson().fromJson(jsonClubResult.toString(), listType);
                    rv_adapter = new ClubAdapter(FindAClubResultActivity.this, listresult, authtoken);
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


                /*for(int i = 0;i<count;i++){
                    JSONObject jsonObject = json.getJSONObject(i);

                    FindAClubResultData data = new Gson().fromJson(jsonObject.toString(), FindAClubResultData.class);

                    //FindAClubResultData data = new FindAClubResultData();
                    data.setClubId(Long.parseLong(jsonObject.get("ClubId").toString()));
                    data.setClubName(jsonObject.get("ClubName").toString());
                    listresult.add(data);
                }*/


//                    data.setClubType(jsonObject.get("ClubType").toString());
//                    data.setClubSubType(jsonObject.get("ClubSubType").toString());
//                    data.setCharterDate(jsonObject.get("CharterDate").toString());
//                    data.setNumberOfActiveMembers(Long.parseLong(jsonObject.get("NumberOfActiveMembers").toString()));
//                    JSONObject jsonLocation = jsonObject.getJSONObject("Location");
//                    data.setInternationalProvince(jsonLocation.get("InternationalProvince").toString());
//                    data.setCountry(jsonLocation.get("Country").toString());
//                    data.setClubLanguage(jsonObject.get("ClubLanguage").toString());
//                    data.setDistrict(jsonObject.get("District").toString());
//                    data.setWebsite(jsonObject.get("Website").toString());
//                    data.setEmail(jsonObject.get("Email").toString());
//                    if(jsonObject.get("Longitude").toString()!= null){
//                        data.setLongitude(jsonObject.getString("Longitude"));
//                    }
//                    if(jsonObject.get("Latitude").toString()!= null){
//                        data.setLatitude(jsonObject.getString("Latitude"));
//                    }
//                    if(jsonObject.get("PhoneKey").toString()!= null){
//                        data.setPhone(jsonObject.getString("PhoneKey"));
//                    }


