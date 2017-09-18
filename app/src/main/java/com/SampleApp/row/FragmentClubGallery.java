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
import android.widget.EditText;
import android.widget.Toast;

import com.SampleApp.row.Adapter.ClubGalleryAdapter;
import com.SampleApp.row.Data.ClubGalleryData;
import com.SampleApp.row.Data.FindAClubResultData;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
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

public class FragmentClubGallery extends Fragment {

    EditText etSearch;
    RecyclerView rvGallery;
    ArrayList<ClubGalleryData> albumList = new ArrayList<>();
    View view;
    Context context;
    FindAClubResultData clubData;
    Bundle extras;
    ClubGalleryAdapter adapter;
    private RecyclerView.LayoutManager mLayoutmanager;
    ClubGalleryAdapter.OnItemSelectedListener onItemSelectedListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_gallery, container, false);
        init();
        return view;
    }
    public void init() {
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        rvGallery = (RecyclerView) view.findViewById(R.id.rvGallery);
        adapter = new ClubGalleryAdapter(context, albumList);
        rvGallery.setAdapter(adapter);
        mLayoutmanager = new LinearLayoutManager(context);
        rvGallery.setLayoutManager(mLayoutmanager);
        onItemSelectedListener = new ClubGalleryAdapter.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                ClubGalleryData data = adapter.getItems().get(position);

                Intent intent = new Intent(context, DTAlbum.class);
                intent.putExtra("albumname", data.getTitle());
                intent.putExtra("albumDescription", data.getDescription());
                intent.putExtra("albumId", data.getAlbumId());
                intent.putExtra("albumImage", data.getImage());
                intent.putExtra("groupId", data.getGroupId());
                context.startActivity(intent);
            }
        };
        adapter.setOnItemSelectedListener(onItemSelectedListener);

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



    public void search(String text) {
        ArrayList<ClubGalleryData> tempList = new ArrayList<>();
        int n = albumList.size();
        text = text.toLowerCase();

        for(int i=0;i<n; i++) {
            ClubGalleryData data = albumList.get(i);
            if ( data.getTitle().toLowerCase().contains(text) || data.getDescription().toLowerCase().contains(text) ) {
                    tempList.add(data);
            }
        }
        adapter = new ClubGalleryAdapter(context, tempList);
        rvGallery.setAdapter(adapter);
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
        loadAlbums();
    }
    public void loadAlbums() {
        Hashtable<String, String> paramsTable = new Hashtable<>();
        paramsTable.put("grpID", clubData.getGrpID());

        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.TBProgressBar);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);


        try {
            JSONObject jsonParams = new JSONObject(new Gson().toJson(paramsTable));
            Utils.log("URL : "+Constant.DT_GET_ALBUMS+" Params : "+jsonParams);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    Constant.DT_GET_ALBUMS,
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
            Utils.log("Result : "+response);
            JSONObject result = response.getJSONObject("TBPublicAlbumsList");
            String status = result.getString("status");
            if ( status.equals("0")) {
                TypeToken<ArrayList<ClubGalleryData>> typeToken = new TypeToken<ArrayList<ClubGalleryData>>(){};
                JSONArray jsonMembersArray = result.getJSONArray("Result");
                albumList = new Gson().fromJson(jsonMembersArray.toString(), typeToken.getType());
                adapter = new ClubGalleryAdapter(context, albumList);
                rvGallery.setAdapter(adapter);
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
