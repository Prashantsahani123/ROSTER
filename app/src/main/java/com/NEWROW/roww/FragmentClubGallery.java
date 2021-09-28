package com.NEWROW.row;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.ClubGalleryAdapter;
import com.NEWROW.row.Adapter.ClubGalleryAdapter_New;
import com.NEWROW.row.Data.ClubGalleryData;
import com.NEWROW.row.Data.FindAClubResultData;
import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.Utils;
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
import java.util.Calendar;
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
    ClubGalleryAdapter_New adapter_new;
    private RecyclerView.LayoutManager mLayoutmanager;
    ClubGalleryAdapter.OnItemSelectedListener onItemSelectedListener;
    ProgressDialog progressDialog=null;
    Spinner sp_year;
    String year="",ClubRotaryType="";;
    boolean isLoading = true;
    private String fromYear, toYear;
    ArrayList<String> filtertype;
    TextView tv_no_records_found;

    //Added By Gaurav
    private String mediaPhotoPath="";
    private String media_discription="";
    private String Ismedia="0";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_gallery, container, false);
         init();

        if(getUserVisibleHint()) { // fragment is visible

            Log.d("row","nivedita getUserVisibleHint");

            init();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    loadData();
                }

            },500);
        }

        return view;
    }

    public void init() {

        etSearch = (EditText) view.findViewById(R.id.etSearch);
        rvGallery = (RecyclerView) view.findViewById(R.id.rvGallery);
        sp_year = (Spinner) view.findViewById(R.id.sp_year);
        tv_no_records_found = (TextView)view.findViewById(R.id.tv_no_records_found);
        adapter = new ClubGalleryAdapter(context, albumList);
        rvGallery.setAdapter(adapter);
        mLayoutmanager = new LinearLayoutManager(context);
        rvGallery.setLayoutManager(mLayoutmanager);

        initYearFilter();

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

                //New field added By Gaurav
                intent.putExtra("rotractors", data.getRotractors());
                intent.putExtra("mediaPhotoPath", mediaPhotoPath);
                intent.putExtra("media_discription", media_discription);
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


        sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedYear = sp_year.getSelectedItem().toString();

                String array[] = selectedYear.split("-");
                fromYear = array[0];
                toYear = array[1];

                Utils.log(fromYear + " " + toYear);

                year = sp_year.getSelectedItem().toString();

                Log.d("row","nivedita sp_year year="+year);

                if(!isLoading) {
                    Log.e("row","nivedita sp_year");
                    etSearch.setText("");
                    loadAlbums();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

    }


    private void initYearFilter(){

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

        Utils.log("Month: " + month + " year : " + year);

        if (month > 6) {
            fromYear = String.valueOf(year);
            toYear = String.valueOf(year + 1);
        } else {
            toYear = String.valueOf(year);
            fromYear = String.valueOf(year - 1);
        }

        filtertype = new ArrayList<>();

        int flag = 0;

        for (int i = year; i >= 2015; i--) {

            String filterYear;

            if (month > 6 && i == year) {
                filterYear = (i) + "-" + (i + 1);
                flag=1;
            } else if (month <= 6 && i == year) {
                filterYear = (i-1) + "-" + (i);
                flag=2;
            } else {
                if(flag==1){
                    filterYear = (i) + "-" + (i+1);
                }else {
                    filterYear = (i-1) + "-" + (i);
                }
            }

            filtertype.add(filterYear);
        }

        if(flag!=1){

            filtertype.remove(filtertype.size()-1);
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, filtertype);
        sp_year.setAdapter(spinnerArrayAdapter);

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

        if (tempList.size()==0){

            /*This code added by Gaurav for displaying blank list*/
            adapter_new = new ClubGalleryAdapter_New(context, tempList);
            rvGallery.setAdapter(adapter_new);
            adapter.setOnItemSelectedListener(onItemSelectedListener);
             adapter.notifyDataSetChanged();
             tv_no_records_found.setVisibility(View.VISIBLE);

        }else {
            tv_no_records_found.setVisibility(View.GONE);
            adapter = new ClubGalleryAdapter(context, tempList);
            rvGallery.setAdapter(adapter);
            adapter.setOnItemSelectedListener(onItemSelectedListener);
            adapter.notifyDataSetChanged();
        }
    }


/*
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
*/

    public void onResume(){

        super.onResume();
        tv_no_records_found.setVisibility(View.GONE);


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        Log.d("row","nivedita setUserVisibleHint flag="+isVisibleToUser+" resumed="+isResumed());

        if (isVisibleToUser && isResumed()) { // fragment is visible and have created
            Log.d("row","nivedita setUserVisibleHint called");
            init();
            loadData();
        }
    }
    private void loadData(){

        extras = getArguments();

        Utils.log("nivedita My Extras : " + extras);

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

    //---Created On 27/12/19 by Nivedita--//
    public void loadAlbums() {

        isLoading = true;

        Hashtable<String, String> paramsTable = new Hashtable<>();

      //  paramsTable.put("grpID", clubData.getGrpID());

        paramsTable.put("groupId", clubData.getGrpID());
//        paramsTable.put("profileId", PreferenceManager.getPreference(getActivity(), PreferenceManager.GRP_PROFILE_ID));
        paramsTable.put("moduleId","52");//this is 8 I have changed to 52
        // paramsTable.put("district_id", "3190"); //Change hard code value district id(get from api).
      //  paramsTable.put("district_id", "0");
      //  paramsTable.put("club_id", clubData.getClubId());
      //  paramsTable.put("category_id", "0");
        paramsTable.put("year", year);
        paramsTable.put("SharType","1");
   //     paramsTable.put("ClubRotaryType","1");
        paramsTable.put("searchText","");

        progressDialog = new ProgressDialog(context, R.style.TBProgressBar);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        try {

            JSONObject jsonParams = new JSONObject(new Gson().toJson(paramsTable));

            Utils.log("URL : satish "+Constant.GetAlbumsList_New+" Params : "+jsonParams);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    Constant.GetAlbumsList_New,
                    jsonParams,

                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.hide();
                            isLoading = false;
                            processSuccess(response);
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.hide();
                            isLoading = false;
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
            isLoading = false;
            e.printStackTrace();
        }
    }

    public void processSuccess(JSONObject response) {

        try {

            Utils.log("Result : "+response);

            /*JSONObject result = response.getJSONObject("TBPublicAlbumsList");
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
            }*/

            JSONObject result = response.getJSONObject("TBAlbumsListResult");

            String status = result.getString("status");

            if ( status.equals("0")) {

                TypeToken<ArrayList<ClubGalleryData>> typeToken = new TypeToken<ArrayList<ClubGalleryData>>(){};

                JSONObject jsonObject = result.getJSONObject("Result");

                JSONArray jsonMembersArray = jsonObject.getJSONArray("newAlbums");
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



/*
    public void loadAlbums() {
        Hashtable<String, String> paramsTable = new Hashtable<>();
        paramsTable.put("grpID", clubData.getGrpID());

        progressDialog = new ProgressDialog(context, R.style.TBProgressBar);
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
*/

/*
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
*/

    public void processError(VolleyError error) {
        error.printStackTrace();
        Toast.makeText(context, R.string.messageSorry, Toast.LENGTH_LONG).show();
    }

    private void getAlbumList() {

        try {

            progressDialog=new ProgressDialog(context,R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject();

            requestData.put("groupId", PreferenceManager.getPreference(context,PreferenceManager.GROUP_ID));
            requestData.put("profileId", PreferenceManager.getPreference(context, PreferenceManager.GRP_PROFILE_ID));
            requestData.put("moduleId","8");
            requestData.put("district_id", "0");
            requestData.put("club_id", clubData.getClubId());
            requestData.put("category_id", "0");
            requestData.put("year", year);
            requestData.put("SharType","1");
            requestData.put("ClubRotaryType","1");
            requestData.put("searchText","");

            Log.d("Response", "PARAMETERS " + Constant.GetAlbumsList_New + " :- " + requestData.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.GetAlbumsList_New, requestData, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    JSONObject result;
                    getAlbumData(response);
                    Utils.log(response.toString());

                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }

                    Utils.log("VollyError:- " + error.toString());

                    //showErrorDialog();
                    //Utils.showMsg(context, "Something went wrong");
                    //Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_LONG).show();
                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(120000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(context, request);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getAlbumData(JSONObject response) {
        try {

            JSONObject TBAlbumsListResult = response.getJSONObject("TBAlbumsListResult");
            String status = TBAlbumsListResult.getString("status");

            if(status.equalsIgnoreCase("0")){

                JSONObject resultObject = TBAlbumsListResult.getJSONObject("Result");
                JSONArray newAlbums = resultObject.getJSONArray("newAlbums");

//                JSONArray totalArray = resultObject.getJSONArray("TotalList");
//                for(int i = 0; i < totalArray.length(); i++){
//                    JSONObject totalObj = totalArray.getJSONObject(i);
//                    tv_top.setText(totalObj.getString("TOTAL_PROJECTS"));
//                    tv_cop.setText(totalObj.getString("COST_OF_PROJECT"));
//                    tv_beneficiary.setText(totalObj.getString("BENEFICIARY"));
//                    tv_manHrSpent.setText(totalObj.getString("MEN_HOURS_SPENT"));
//                }

                albumList.clear();

                if(newAlbums.length()>0) {

                    for (int i = 0; i < newAlbums.length(); i++) {

                        JSONObject albumObj = newAlbums.getJSONObject(i);
                        ClubGalleryData data = new ClubGalleryData();

                        data.setAlbumId(albumObj.getString("albumId"));
                        data.setTitle(albumObj.getString("title"));
                        data.setDescription(albumObj.getString("description"));
                        data.setImage(albumObj.getString("image"));
                        //data.getGroupId(albumObj.getString("groupId"));
                        data.setModuleId(albumObj.getString("moduleId"));
                        data.setIsAdmin(albumObj.getString("isAdmin"));
                        data.setClub_Name(albumObj.getString("clubname"));
                        data.setProject_date(albumObj.getString("project_date"));
                        data.setProject_cost(albumObj.getString("project_cost"));
                        data.setBeneficiary(albumObj.getString("beneficiary"));
                        data.setWorking_hour(albumObj.getString("working_hour"));
                        data.setWorking_hour_type(albumObj.getString("working_hour_type"));
                        data.setCost_of_project_type(albumObj.getString("cost_of_project_type"));
                        data.setNoOfRotarians(albumObj.getString("NumberOfRotarian"));
                        data.setShareType(albumObj.getString("sharetype"));
                        data.setAttendance(albumObj.getString("Attendance"));
                        data.setAttendancePer(albumObj.getString("AttendancePer"));
                        data.setMeetType(albumObj.getString("MeetingType"));
                        data.setAgendaDocID(albumObj.getString("AgendaDocID"));
                        data.setMomDocID(albumObj.getString("MOMDocID"));
                        //Add New Field
                        data.setRotractors(albumObj.getString("Rotaractors"));

                        Ismedia = albumObj.getString("Ismedia");
                        mediaPhotoPath = albumObj.getString("Mediaphoto");

                        media_discription=albumObj.getString("MediaDesc").toString();
                        albumList.add(data);
                    }

                    adapter = new ClubGalleryAdapter(context, albumList);
                    rvGallery.setAdapter(adapter);
                    rvGallery.setVisibility(View.VISIBLE);
                    tv_no_records_found.setVisibility(View.GONE);

                }else{
                    rvGallery.setVisibility(View.GONE);
                    tv_no_records_found.setVisibility(View.VISIBLE);
                }
                progressDialog.dismiss();
            }else{
                progressDialog.dismiss();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Utils.log(e.getMessage());
            progressDialog.dismiss();
        }
    }



}
