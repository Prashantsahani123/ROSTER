package com.SampleApp.row;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.SampleApp.row.Adapter.ClubMemberRVAdapter;
import com.SampleApp.row.Data.ClubMemberData;
import com.SampleApp.row.Data.FindAClubResultData;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
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

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by admin on 01-08-2017.
 */

public class FragmentClubMembers extends Fragment {

    EditText etSearch;
    Spinner spFilter;
    RecyclerView rvMembers;
    ArrayList<ClubMemberData> memberList = new ArrayList<>();
    View view;
    Context context;
    FindAClubResultData clubData;
    Bundle extras;
    ClubMemberRVAdapter adapter;
    private RecyclerView.LayoutManager mLayoutmanager;
    private String groupType;
    ClubMemberRVAdapter.OnItemSelectedListener onItemSelectedListener;
    String myCategory = "0";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_club_members, container, false);
        myCategory = PreferenceManager.getPreference(context, PreferenceManager.MY_CATEGORY, "0");

        init();
        return view;
    }
    public void init() {
        etSearch = (EditText) view.findViewById(R.id.edt_search);
        spFilter = (Spinner) view.findViewById(R.id.spFilter);
        rvMembers = (RecyclerView) view.findViewById(R.id.rvMembers);
        adapter = new ClubMemberRVAdapter(context, memberList);
        rvMembers.setAdapter(adapter);
        mLayoutmanager = new LinearLayoutManager(context);
        rvMembers.setLayoutManager(mLayoutmanager);
        onItemSelectedListener = new ClubMemberRVAdapter.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                Intent i = new Intent(context, RotarianBusinessDetails_ProfileActivity.class);
                i.putExtra("memberProfileId",adapter.getItems().get(position).getProfileID());
                i.putExtra("clubname",clubData.getClubName());
                context.startActivity(i);
            }
        };
        adapter.setOnItemSelectedListener(onItemSelectedListener);

        if ( myCategory.equals("2")) { // means its District
            spFilter.setVisibility(View.VISIBLE);
        } else {
            spFilter.setVisibility(View.GONE);
        }

        spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                etSearch.setText("");
                showFilteredItems(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(""+s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void showFilteredItems(int position) {
        ArrayList<ClubMemberData> tempList = new ArrayList<>();
        int n = memberList.size();
        for(int i=0;i<n; i++) {
            ClubMemberData data = memberList.get(i);
            if ( data.getDistDesignation().equals(""+position)) {
                tempList.add(data);
            }
        }
        adapter = new ClubMemberRVAdapter(context, tempList);
        rvMembers.setAdapter(adapter);
        adapter.setOnItemSelectedListener(onItemSelectedListener);
        adapter.notifyDataSetChanged();

    }

    public void search(String text) {
        ArrayList<ClubMemberData> tempList = new ArrayList<>();
        int n = memberList.size();
        text = text.toLowerCase();
        int selectedPosition = spFilter.getSelectedItemPosition();
        for(int i=0;i<n; i++) {
            ClubMemberData data = memberList.get(i);
            if ( data.getMemberName().toLowerCase().contains(text) ) {
                if ( selectedPosition == 1 ) { //i.e. selected filter is DistrictCommittee member
                    if ( data.getDistDesignation().equals("1")) {
                        tempList.add(data);
                    }
                } else {
                    tempList.add(data);
                }

            }
        }
        adapter = new ClubMemberRVAdapter(context, tempList);
        rvMembers.setAdapter(adapter);
        adapter.setOnItemSelectedListener(onItemSelectedListener);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        extras = getArguments();
        Utils.log("My Extras : " + extras);
        try {
            clubData = (FindAClubResultData) extras.getSerializable("clubData");
        } catch (ClassCastException cce) {
            cce.printStackTrace();
            Toast.makeText(context, "Sorry. Something went wrong.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Sorry. Something went wrong.", Toast.LENGTH_LONG).show();
        }
        loadMembers();
    }
    public void loadMembers() {
        Hashtable<String, String> paramsTable = new Hashtable<>();
        paramsTable.put("grpID", clubData.getGrpID());
        paramsTable.put("searchText", "");

        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.TBProgressBar);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);


        try {
            JSONObject jsonParams = new JSONObject(new Gson().toJson(paramsTable));
            Utils.log("URL : "+Constant.GET_CLUB_MEMBERS+" Params : "+jsonParams);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    Constant.GET_CLUB_MEMBERS,
                    jsonParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.hide();
                            processSuccess(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.hide();
                            processError(error);
                        }
                    }
            );

            request.setRetryPolicy(new DefaultRetryPolicy(
                    Constant.VOLLEY_MAX_REQUEST_TIMEOUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            AppController.getInstance().addToRequestQueue(context, request);
            progressDialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void processSuccess(JSONObject response) {
        try {
            JSONObject result = response.getJSONObject("TBMemberList");
            String status = result.getString("status");
            if ( status.equals("0")) {
                TypeToken<ArrayList<ClubMemberData>> typeToken = new TypeToken<ArrayList<ClubMemberData>>(){};
                JSONArray jsonMembersArray = result.getJSONArray("Result");
                memberList = new Gson().fromJson(jsonMembersArray.toString(), typeToken.getType());
                adapter = new ClubMemberRVAdapter(context, memberList);
                rvMembers.setAdapter(adapter);
                adapter.setOnItemSelectedListener(onItemSelectedListener);
            } else if ( status.equals("1") ) {
                Toast.makeText(context, "Failed to get members of club. Please retry", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processError(VolleyError error) {
        error.printStackTrace();
        Toast.makeText(context, R.string.messageSorry, Toast.LENGTH_LONG).show();
    }
}
