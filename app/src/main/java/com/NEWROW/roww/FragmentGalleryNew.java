package com.NEWROW.row;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

import com.NEWROW.row.Adapter.GalleryListAdapter;
import com.NEWROW.row.Data.AlbumData;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class FragmentGalleryNew extends Fragment {

    String moduleId = "",grpId,district_id,year,club_id,fromShowcase = "1",category_id="0",ClubRotaryType="";
    private Context context;
    private View view;
    private EditText etSearch;
    ProgressDialog progressDialog;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    GalleryListAdapter  listAdapter;
    ArrayList<AlbumData> albumlist = new ArrayList<AlbumData>();
    FindAClubResultData clubData;
    TextView tv_no_records_found;
    Spinner sp_year,sp_service;
    ArrayList<String> filtertype;
    private String fromYear, toYear;
    Boolean isFirstLoad = true;
    Bundle extras;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = getActivity();

        view = inflater.inflate(R.layout.fragment_gallery, container, false);

        tv_no_records_found = (TextView)view.findViewById(R.id.tv_no_records_found);
        sp_year = (Spinner)view.findViewById(R.id.sp_year);
        sp_service = (Spinner)view.findViewById(R.id.sp_service);
        etSearch = (EditText) view.findViewById(R.id.etSearch);

        moduleId = PreferenceManager.getPreference(context,PreferenceManager.MODULE_ID);
        extras = getArguments();

//        Utils.log("My Extras : " + extras);

        try {
            clubData = (FindAClubResultData) extras.getSerializable("clubData");
        } catch (ClassCastException cce) {
            cce.printStackTrace();
            Toast.makeText(context, "Sorry. Something went wrong.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Sorry. Something went wrong.", Toast.LENGTH_LONG).show();
        }

        mRecyclerView = (RecyclerView)view.findViewById(R.id.rvGallery);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        grpId = clubData.getGrpID();
        district_id = "0";//intent.getExtras().getString("districtId","0");
        club_id = clubData.getClubId();//intent.getExtras().getString("clubId","0");
        year = "";//intent.getExtras().getString("year","");
        category_id = "0";//intent.getExtras().getString("categoryList","0");

        intit();

        if(getUserVisibleHint()){ // fragment is visible
            Utils.log("My Extras Gallery : ");
            getAlbumList();
        }

        sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedYear = sp_year.getSelectedItem().toString();
                String array[] = selectedYear.split("-");

                fromYear = array[0];
                toYear = array[1];

                Utils.log(fromYear + " " + toYear);

                year = sp_year.getSelectedItem().toString();

                if(!isFirstLoad){
                    getAlbumList();
                } else {
                    isFirstLoad = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if(position==0){
                    ClubRotaryType="0";
                }else if(position==1){
                    ClubRotaryType="1";
                }

                if(!isFirstLoad){
                    getAlbumList();
                }else {
                    isFirstLoad=false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isResumed()) { // fragment is visible and have created
            Utils.log("My Extras Gallery : ");
            getAlbumList();
        }
    }

    private void intit() {

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

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, filtertype);
        sp_year.setAdapter(spinnerArrayAdapter);
    }

    private void getAlbumList() {

        try {

            progressDialog = new ProgressDialog(context,R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject();

            requestData.put("groupId", PreferenceManager.getPreference(context,PreferenceManager.GROUP_ID));
            requestData.put("profileId", PreferenceManager.getPreference(context, PreferenceManager.GRP_PROFILE_ID));
            requestData.put("moduleId",moduleId);
            requestData.put("district_id", district_id);
            requestData.put("club_id", club_id);
            requestData.put("category_id", category_id);
            requestData.put("year", year);
            requestData.put("SharType",fromShowcase);
            requestData.put("ClubRotaryType",ClubRotaryType);

            Log.d("Response", "PARAMETERS " + Constant.GetAlbumsList_New + " :- " + requestData.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.GetAlbumsList_New, requestData, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
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

            if(status.equalsIgnoreCase("0")) {

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

                albumlist.clear();

                if(newAlbums.length()>0) {

                    for (int i = 0; i < newAlbums.length(); i++) {

                        JSONObject albumObj = newAlbums.getJSONObject(i);
                        AlbumData data = new AlbumData();

                        data.setAlbumId(albumObj.getString("albumId"));
                        data.setTitle(albumObj.getString("title"));
                        data.setDescription(albumObj.getString("description"));
                        data.setImage(albumObj.getString("image"));
                        data.setGrpId(albumObj.getString("groupId"));
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

                        albumlist.add(data);
                    }

                 //   listAdapter = new GalleryListAdapter(context, albumlist, "1",fromShowcase,this);

                    mRecyclerView.setAdapter(listAdapter);

                    mRecyclerView.setVisibility(View.VISIBLE);
                    tv_no_records_found.setVisibility(View.GONE);

                } else {
                    mRecyclerView.setVisibility(View.GONE);
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
