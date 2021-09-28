package com.NEWROW.row;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.AlbumAdapter;
import com.NEWROW.row.Adapter.GalleryListAdapter;
import com.NEWROW.row.Data.AlbumData;
import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.TBPrefixes;
import com.NEWROW.row.Utils.Utils;
import com.NEWROW.row.sql.GalleryMasterModel;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.clans.fab.FloatingActionMenu;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by user on 02-09-2016.
 */
public class Gallery extends Activity {


    public static int  delt_flg = 0 ;
    boolean ret_v = false  ;
    public static String sub_ct= "";
    public static String moderate_flag="";

    Dialog dialog ;

    public static final int UPDATE_ALBUM_REQEUST = 25;
    public static TextView tv_title;
    public TextView tv_no_records_found;
    ImageView iv_backbutton;
    EditText et_serach;
    private ImageView iv_search;
    GridView gv;
    FloatingActionButton fab;
    FloatingActionMenu materialDesignFAM;
    com.github.clans.fab.FloatingActionButton addAlbum, deleteAlbum, movetos;
    ArrayList<AlbumData> albumlist = new ArrayList<AlbumData>();

    GalleryMasterModel albumModel;
    String updatedOn = "";
    private long grpId;
    AlbumAdapter gridAdapter;
    String moduleName = "";
    int mode = 0;
    RelativeLayout ll_search, ll_service;
    Boolean isSearchVisible = false, isFirstLoad = true;
    EditText edt_search, et_searchBar;
    Button btn_close;
    boolean isinUpdatemode = false;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    boolean isInGridviewMode = false;
    GalleryListAdapter listAdapter;
    String moduleId = "", searchByProject = "";
    ProgressDialog progressDialog;
    public static String year;
    String district_id, club_id, fromShowcase = "1", serviceproject = "1", category_id = "0", ClubRotaryType = "";
    ArrayList<AlbumData> categoryList;
    LinearLayout ll_details;
    TextView tv_cop, tv_beneficiary, tv_manHrSpent, tv_top;
    Spinner sp_year, sp_service;
    private String fromYear, toYear;
    ArrayList<String> filtertype;


    //Add by Gaurav
    String catid, MediaDesc;
    public static String MemberCount = "", maxBeneficiaries = "";
    String sharetype_new = "0";
    public static String deletetitle;
    EditText et_serach_club;
    String albumname = "";
    ArrayList<AlbumData> tempArrayList;

    public Subpro_adapter subpro_adapter ;

    //this is added for Data Came from showcase
    public boolean isShowCase = false;


    ArrayList<Subpro_model> subpro_arr ;  // = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_gallery);
        delt_flg = 0;



        if (PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID) != null) {
            grpId = Long.parseLong(PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID));
        }

        moduleId = PreferenceManager.getPreference(this, PreferenceManager.MODULE_ID);

        gv = (GridView) findViewById(R.id.gridview);

        Intent intent = getIntent();

        this.moduleName = PreferenceManager.getPreference(this, PreferenceManager.MODUEL_NAME, "Gallery");

        district_id = intent.getExtras().getString("districtId", "0");
        club_id = intent.getExtras().getString("clubId", "0");
        year = intent.getExtras().getString("year", "");
        fromShowcase = intent.getExtras().getString("fromShowcase", "1");


        Log.i("fromShowcase1",fromShowcase);
        category_id = intent.getExtras().getString("categoryList", "0");
        //this code added by Gaurav
        //when serv.project=0 then club service data display
        // when serv.project=1 rotary data display
        serviceproject = intent.getExtras().getString("serviceproject", "0");

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_no_records_found = (TextView) findViewById(R.id.tv_no_records_found);
        ll_details = (LinearLayout) findViewById(R.id.ll_details);
        tv_cop = (TextView) findViewById(R.id.tv_cop);
        tv_beneficiary = (TextView) findViewById(R.id.tv_beneficiary);
        tv_manHrSpent = (TextView) findViewById(R.id.tv_manHrSpent);
        tv_top = (TextView) findViewById(R.id.tv_top);
        sp_year = (Spinner) findViewById(R.id.sp_year);
        sp_service = (Spinner) findViewById(R.id.sp_service);
        ll_service = (RelativeLayout) findViewById(R.id.ll_service);
        ll_search = (RelativeLayout) findViewById(R.id.ll_search);
        edt_search = (EditText) findViewById(R.id.edt_search);
        //Added By Gaurav
        et_serach_club = (EditText) findViewById(R.id.et_serach_club);
        et_searchBar = (EditText) findViewById(R.id.et_searchBar);
        btn_close = (Button) findViewById(R.id.btn_close);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        addAlbum = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        deleteAlbum = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        movetos = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item3);


//        material_design_floating_action_menu_item3
        albumModel = new GalleryMasterModel(this);

        albumModel.printTable();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_search = (ImageView) findViewById(R.id.iv_search);


        if (fromShowcase.equalsIgnoreCase("0")) {

            searchByProject = intent.getExtras().getString("search By Project", "");

            tv_title.setText("Showcase");
            ll_search.setVisibility(View.VISIBLE);
            ll_service.setVisibility(View.GONE);
            ll_details.setVisibility(View.GONE);
            iv_search.setVisibility(View.GONE);
            sp_year.setVisibility(View.GONE);

            //for showcase data Enable
            isShowCase = true;


            //cmt by prashant sahani
            getAlbumList();

        } else {

            ll_details.setVisibility(View.GONE);
            sp_year.setVisibility(View.VISIBLE);
            ll_search.setVisibility(View.GONE);
            //  ll_service.setVisibility(View.VISIBLE);

            //this code is added By Gaurav
            ll_service.setVisibility(View.GONE);

            catid = PreferenceManager.getPreference(Gallery.this, PreferenceManager.MY_CATEGORY);
            if (catid.equalsIgnoreCase(String.valueOf(Constant.GROUP_CATEGORY_CLUB))) {

                //this code added By Gaurav
                if (serviceproject.equals("0")) {

                    tv_title.setText("Club Meetings");

                    movetos.setLabelText("Move to Service Project");

                    deletetitle = "Club Meeting";
                    addAlbum.setLabelText("Add Meeting");
                    deleteAlbum.setLabelText("Delete Meeting");


                } else {
                    tv_title.setText("Service Projects");
                    movetos.setLabelText("Move to Club Meeting");
                    deletetitle = "Service Project";
                    addAlbum.setLabelText("Add Project");
                    deleteAlbum.setLabelText("Delete Project");

                }

//                String tt= tv_title.getText().toString();
            //    Toast.makeText(getApplicationContext(),"projet "+tt,Toast.LENGTH_LONG).show();

//                Log.d("title of project ",tt);
//
//                    if(tt.equalsIgnoreCase("District Projects") || tt.equalsIgnoreCase("District Events"))
//                    {
//                        movetos.setVisibility(View.GONE);
//                    }
//                    else{
//                    movetos.setVisibility(View.VISIBLE);
//
//
//                       }






            } else {

                //This is for district service
                // ll_service.setVisibility(View.VISIBLE);

                String[] services = getResources().getStringArray(R.array.dtserviceFilter);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, services);

                sp_service.setAdapter(adapter);


                tv_title.setText(moduleName);




                deletetitle = moduleName;
                if (deletetitle.equalsIgnoreCase("District Events")) {
                    addAlbum.setLabelText("Add Event");
                    deleteAlbum.setLabelText("Delete Event");
                } else {
                    addAlbum.setLabelText("Add District Project");
                    deleteAlbum.setLabelText("Delete District Project");
                }

              /*  //this add and delete project option add By Gaurav in district

                addAlbum.setLabelText("Add Project");
                deleteAlbum.setLabelText("Delete Project");*/

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

                    getAlbumList();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });

            sp_service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                    if (position == 0) {
                        ClubRotaryType = "0";
                    } else if (position == 1) {
                        ClubRotaryType = "1";
                    }

                    if (!isFirstLoad) {
                        // cmt by prashant sahan
                       getAlbumList();
                    } else {
                        isFirstLoad = false;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mode != 1) {
                    Intent i = new Intent(Gallery.this, GalleryDescription.class);
                    i.putExtra("albumname", albumlist.get(position).getTitle());
                    i.putExtra("albumDescription", albumlist.get(position).getDescription());
                    i.putExtra("albumId", albumlist.get(position).getAlbumId());
                    i.putExtra("albumImage", albumlist.get(position).getImage());
                    i.putExtra("fromShowcase", fromShowcase);
                    startActivityForResult(i, UPDATE_ALBUM_REQEUST);
                } else {
                }
            }

        });

        init();
        //   checkadminrights();
        //loadFromDB();
        if (InternetConnection.checkConnection(this)) {
            //getAlbumList();
        } else {
            Toast.makeText(Gallery.this, "No Internet Connection.", Toast.LENGTH_SHORT).show();
        }








        String tt= tv_title.getText().toString();
       // Toast.makeText(getApplicationContext(),"projet "+tt,Toast.LENGTH_LONG).show();

        Log.d("title of project ",tt);

        if(tt.equalsIgnoreCase("District Projects") || tt.equalsIgnoreCase("District Events"))
        {
            movetos.setVisibility(View.GONE);
        }
        else{
            movetos.setVisibility(View.VISIBLE);


        }




    }

    public  void getAlbumList() {

        try {

            progressDialog = new ProgressDialog(Gallery.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject();

            //Test Showcase Data is enable or not
            if (isShowCase) {
                //the data comes from showcase module
                requestData.put("SharType", fromShowcase);
                requestData.put("category_id", category_id);
                requestData.put("club_id", club_id);
                requestData.put("district_id", district_id);
                requestData.put("groupId", PreferenceManager.getPreference(Gallery.this, PreferenceManager.GROUP_ID));
                requestData.put("moduleId", moduleId);
                requestData.put("profileId", "");
                requestData.put("searchText", searchByProject);
                requestData.put("year", year);


            } else {


                requestData.put("groupId", PreferenceManager.getPreference(Gallery.this, PreferenceManager.GROUP_ID));
                requestData.put("moduleId", moduleId);
                requestData.put("year", year);

                //this code is addded by Gaurav on 7th April 2020
                //if serviceproject is 0 then club service data display.
                //if serviceproject is 1 then Rotary service data display.

                //this String catid=1 means this is club data display
                //this String catid=2 means this is district data display

                if (ClubRotaryType.equals("") && serviceproject.equals("0") && catid.equals("1")) {

                    //clubmeeting
                    // ClubRotaryType = "0";
                    sharetype_new = "0";

                } else if (serviceproject.equals("1") && catid.equals("1")) {
                    //Service Project
                    //ClubRotaryType = "1";
                    sharetype_new = "1";

                }  //this condition for district data display
                else if (serviceproject.equals("0") && catid.equals("2")) {
                    //District Event
                    //ClubRotaryType = "0";
                    sharetype_new = "0";

                } else if (serviceproject.equals("1") && catid.equals("2")) {
                    //District Project
                    //ClubRotaryType = "1";
                    sharetype_new = "1";
                }
                requestData.put("SharType", sharetype_new);
                //  requestData.put("ClubRotaryType", ClubRotaryType);
                requestData.put("searchText", searchByProject);


            }


            // requestData.put("profileId", PreferenceManager.getPreference(this, PreferenceManager.GRP_PROFILE_ID));
            // requestData.put("district_id", district_id);
            //  requestData.put("club_id", club_id);
            //requestData.put("category_id", category_id);
            //   requestData.put("SharType", fromShowcase);

            Log.d("Responseeeeee", "PARAMETERS " + Constant.GetAlbumsList_New + " :- " + requestData.toString());

            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.GetAlbumsList_New, requestData, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    JSONObject result;
                    Log.d("Responseeeeee", "PARAMETERS " + Constant.GetAlbumsList_New + " :- " + response.toString());

                    getAlbumData(response);
                    Utils.log(response.toString());

                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    if (progressDialog != null) {
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

            AppController.getInstance().addToRequestQueue(Gallery.this, request);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public  void getAlbumData(JSONObject response) {
        try {

            JSONObject TBAlbumsListResult = response.getJSONObject("TBAlbumsListResult");
            String status = TBAlbumsListResult.getString("status");

            if (status.equalsIgnoreCase("0")) {


                 moderate_flag = TBAlbumsListResult.getString("isModerator");
                moderate_flag = "0";
                 Log.d("moderat_flag", moderate_flag);

                JSONObject resultObject = TBAlbumsListResult.getJSONObject("Result");
                //this two values added by Gaurav
                MemberCount = resultObject.getString("MemberCount");
                maxBeneficiaries = resultObject.getString("maxBeneficiaries");
                //closed this two new parameters
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

                if (newAlbums.length() > 0) {

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
                        data.setMomDocID(albumObj.getString("MOMDocID"));//OnetimeOrOngoing
                        data.setOnetimeOrOngoing(albumObj.getString("OnetimeOrOngoing"));

                        data.setTtlOfNewOngoingProj(albumObj.getString("TtlOfNewOngoingProj"));
                        data.setSubCount(albumObj.getString("SubCount"));
                        data.setModerator_status(albumObj.getString("moderator_status"));



                        //TtlOfNewOngoingProj

                        albumlist.add(data);
                    }
                    Log.d("albumlistt", String.valueOf(albumlist));

                    listAdapter = new GalleryListAdapter(Gallery.this, albumlist, "1", fromShowcase);
                    mRecyclerView.setAdapter(listAdapter);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    tv_no_records_found.setVisibility(View.GONE);
                    listAdapter.notifyDataSetChanged();

                    //This code is added By Gaurav on 28th May 2020 for displaying filtering data
                    //When user back press from gallery Description class
                    //the search text isn't clear then
                    //the search text data should display


                    albumname = et_serach_club.getText().toString();


                    albumname = et_serach_club.getText().toString();

                    int textlength = albumname.length();
                    tempArrayList = new ArrayList<AlbumData>();

                    for (AlbumData c : albumlist) {

                        if (textlength <= c.getTitle().length()) {
                            /*if (c.getClub_Name().toLowerCase().contains(albumname.toString().toLowerCase()) || c.getTitle().toLowerCase().contains(albumname.toString().toLowerCase())) {
                                tempArrayList.add(c);
                            }*/

                            if (c.getTitle().toLowerCase().contains(albumname.toString().toLowerCase())) {
                                tempArrayList.add(c);
                            }
                        }
                    }

                    if (tempArrayList.size() < 1) {
                        mRecyclerView.setVisibility(View.GONE);
                        tv_no_records_found.setVisibility(View.VISIBLE);
                    } else {
                        tv_no_records_found.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        listAdapter = new GalleryListAdapter(Gallery.this, tempArrayList, "1", fromShowcase);
                        listAdapter.notifyDataSetChanged();
                        mRecyclerView.setAdapter(listAdapter);

                    }


                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    tv_no_records_found.setVisibility(View.VISIBLE);
                }
                progressDialog.dismiss();
            } else {
                progressDialog.dismiss();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Utils.log(e.getMessage());
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkadminrights();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_ALBUM_REQEUST) {
            if (InternetConnection.checkConnection(this)) {
                //checkForUpdate();

                //cmt by prashant sahani
                getAlbumList();
                Log.d("---------------", "Check for update gets called------");
            } else {
                Toast.makeText(this, "No internet connection to get Updated Records", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void init() {

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
                flag = 1;
            } else if (month <= 6 && i == year) {
                filterYear = (i - 1) + "-" + (i);
                flag = 2;
            } else {
                if (flag == 1) {
                    filterYear = (i) + "-" + (i + 1);
                } else {
                    filterYear = (i - 1) + "-" + (i);
                }
            }

            filtertype.add(filterYear);
        }

        if (flag != 1) {

            filtertype.remove(filtertype.size() - 1);
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, filtertype);
        sp_year.setAdapter(spinnerArrayAdapter);


        addAlbum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                materialDesignFAM.close(false);

                String groupType = PreferenceManager.getPreference(Gallery.this, PreferenceManager.MY_CATEGORY, "1");


                if (groupType.equals("" + Constant.GROUP_CATEGORY_DT)) {
                    Intent i = new Intent(Gallery.this, DTAddAlbum.class);
                    i.putExtra("serviceproject", serviceproject);
                    i.putExtra("maxBeneficiaries", maxBeneficiaries);


                } else {

                      String tt=   tv_title.getText().toString();
                      if(tt.equalsIgnoreCase("Club Meetings"))
                      {
                          Intent i = new Intent(Gallery.this, AddAlbum.class);
                         i.putExtra("serviceproject", serviceproject);
                         i.putExtra("MemberCount", MemberCount);
                         i.putExtra("maxBeneficiaries", maxBeneficiaries);
                    //  i.putExtra("maxBeneficiaries", maxBeneficiaries);
                        startActivityForResult(i, UPDATE_ALBUM_REQEUST);
                      }
                      else
                      {
                         popup1();

                      }
                   // popup1();

//                    Intent i = new Intent(Gallery.this, AddAlbum.class);
//                    i.putExtra("serviceproject", serviceproject);
//                    i.putExtra("MemberCount", MemberCount);
//                    i.putExtra("maxBeneficiaries", maxBeneficiaries);
//                    //  i.putExtra("maxBeneficiaries", maxBeneficiaries);
//                    startActivityForResult(i, UPDATE_ALBUM_REQEUST);
                }
            }
        });

        deleteAlbum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mode = 1;

                materialDesignFAM.close(false);
                materialDesignFAM.setVisibility(View.GONE);

                if (isInGridviewMode) {
                    gridAdapter = new AlbumAdapter(Gallery.this, albumlist, "0");
                    gv.setAdapter(gridAdapter);
                } else {
                    listAdapter = new GalleryListAdapter(Gallery.this, albumlist, "0", fromShowcase);
                    mRecyclerView.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();
                }
            }
        });
        // move to club meeting

        movetos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               String tt =  tv_title.getText().toString();

                mode = 1;

                if(tt.equalsIgnoreCase("Club Meetings"))
                {
                    materialDesignFAM.close(false);
                    materialDesignFAM.setVisibility(View.GONE);

                    if (isInGridviewMode) {
                        gridAdapter = new AlbumAdapter(Gallery.this, albumlist, "3");
                        gv.setAdapter(gridAdapter);
                    } else {
                        listAdapter = new GalleryListAdapter(Gallery.this, albumlist, "3", fromShowcase);
                        mRecyclerView.setAdapter(listAdapter);
                    }
                }

                else {

                    materialDesignFAM.close(false);
                    materialDesignFAM.setVisibility(View.GONE);

                    if (isInGridviewMode) {
                        gridAdapter = new AlbumAdapter(Gallery.this, albumlist, "2");
                        gv.setAdapter(gridAdapter);
                    } else {
                        listAdapter = new GalleryListAdapter(Gallery.this, albumlist, "2", fromShowcase);
                        mRecyclerView.setAdapter(listAdapter);
                        listAdapter.notifyDataSetChanged();
                    }
                }
            }
        });





//        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isSearchVisible){
//                    isSearchVisible = false;
//                    ll_search.setVisibility(View.GONE);
//                    hideInput();
//                }else {
//                    ll_search.setVisibility(View.VISIBLE);
//                    isSearchVisible = true;
//                    edt_search.requestFocus();
//                    showInput();
//                }
//            }
//        });

    }

    private void checkadminrights() {
        //Back to this page

        if (fromShowcase.equalsIgnoreCase("0")) {

            materialDesignFAM.setVisibility(View.GONE);

        } else {

            if (PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN).equals("No")) {
                //iv_actionbtn.setVisibility(View.GONE);
                materialDesignFAM.setVisibility(View.GONE);
            } else {
                materialDesignFAM.setVisibility(View.VISIBLE);
            }
        }


      /*  edt_search.addTextChangedListener(new TextWatcher() {

            String albumname = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                albumname = edt_search.getText().toString();

                int textlength = s.length();
                ArrayList<AlbumData> tempArrayList = new ArrayList<AlbumData>();

                for (AlbumData c : albumlist) {

                    if (textlength <= c.getTitle().length()) {
                        if (c.getClub_Name().toLowerCase().contains(s.toString().toLowerCase()) || c.getTitle().toLowerCase().contains(s.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }

                if (tempArrayList.size() < 1) {
                    mRecyclerView.setVisibility(View.GONE);
                    tv_no_records_found.setVisibility(View.VISIBLE);
                } else {
                    tv_no_records_found.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    listAdapter = new GalleryListAdapter(Gallery.this, tempArrayList, "1", fromShowcase);
                    listAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(listAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

//                albumlist = albumModel.getAlbumByName(albumname,String.valueOf(grpId),moduleId);
//                if(albumlist!= null && albumlist.size() > 0) {
//                    if(isInGridviewMode) {
//                        gridAdapter = new AlbumAdapter(Gallery.this, albumlist, "1");
//                        gv.setAdapter(gridAdapter);
//                        gridAdapter.notifyDataSetChanged();
//                    }else{
//                        listAdapter = new GalleryListAdapter(Gallery.this, albumlist, "1",fromShowcase);
//                        mRecyclerView.setAdapter(listAdapter);
//                        listAdapter.notifyDataSetChanged();
//                    }
//
//                } else {
////                    Toast.makeText(Gallery.this, "No Albums Found ", Toast.LENGTH_SHORT).show();
//                    //tv_no_records_found
//                    if(isInGridviewMode) {
//                        TextView tvEmpty = (TextView) findViewById(R.id.tv_no_records_found);
//                        gv.setEmptyView(tvEmpty);
//                        gv.setAdapter(null);
//                    }else{
//                        albumlist.add(new AlbumData("-1","","","","","",""));
//                        listAdapter = new GalleryListAdapter(Gallery.this, albumlist, "1",fromShowcase);
//                        mRecyclerView.setAdapter(listAdapter);
//                        listAdapter.notifyDataSetChanged();
//
//                    }
//                }

            }
        });*/

        //this Added By Gaurav For New Design
        et_serach_club.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                albumname = et_serach_club.getText().toString().toLowerCase();

                int textlength = s.length();
                tempArrayList = new ArrayList<AlbumData>();

                for (AlbumData c : albumlist) {

                    if (textlength <= c.getTitle().length()) {

                       /* if (c.getClub_Name().toLowerCase().contains(s.toString().toLowerCase()) || c.getTitle().toLowerCase().contains(s.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }*/


                        if (c.getTitle().toLowerCase().contains(s.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }

                if (tempArrayList.size() < 1) {
                    mRecyclerView.setVisibility(View.GONE);
                    tv_no_records_found.setVisibility(View.VISIBLE);
                } else {
                    tv_no_records_found.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    listAdapter = new GalleryListAdapter(Gallery.this, tempArrayList, "1", fromShowcase);
                    listAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(listAdapter);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {


//                albumlist = albumModel.getAlbumByName(albumname,String.valueOf(grpId),moduleId);
//                if(albumlist!= null && albumlist.size() > 0) {
//                    if(isInGridviewMode) {
//                        gridAdapter = new AlbumAdapter(Gallery.this, albumlist, "1");
//                        gv.setAdapter(gridAdapter);
//                        gridAdapter.notifyDataSetChanged();
//                    }else{
//                        listAdapter = new GalleryListAdapter(Gallery.this, albumlist, "1",fromShowcase);
//                        mRecyclerView.setAdapter(listAdapter);
//                        listAdapter.notifyDataSetChanged();
//                    }
//
//                } else {
////                    Toast.makeText(Gallery.this, "No Albums Found ", Toast.LENGTH_SHORT).show();
//                    //tv_no_records_found
//                    if(isInGridviewMode) {
//                        TextView tvEmpty = (TextView) findViewById(R.id.tv_no_records_found);
//                        gv.setEmptyView(tvEmpty);
//                        gv.setAdapter(null);
//                    }else{
//                        albumlist.add(new AlbumData("-1","","","","","",""));
//                        listAdapter = new GalleryListAdapter(Gallery.this, albumlist, "1",fromShowcase);
//                        mRecyclerView.setAdapter(listAdapter);
//                        listAdapter.notifyDataSetChanged();
//
//                    }
//                }

            }
        });

      /*  et_searchBar.addTextChangedListener(new TextWatcher() {

            String albumname = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                albumname = et_searchBar.getText().toString();

                int textlength = s.length();
                ArrayList<AlbumData> tempArrayList = new ArrayList<AlbumData>();

                for (AlbumData c : albumlist) {
                    if (textlength <= c.getTitle().length()) {
                        if (c.getClub_Name().toLowerCase().contains(s.toString().toLowerCase()) || c.getTitle().toLowerCase().contains(s.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }

                if (tempArrayList.size() < 1) {
                    mRecyclerView.setVisibility(View.GONE);
                    tv_no_records_found.setVisibility(View.VISIBLE);
                } else {
                    tv_no_records_found.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    listAdapter = new GalleryListAdapter(Gallery.this, tempArrayList, "1", fromShowcase);
                    listAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(listAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


//                albumlist = albumModel.getAlbumByName(albumname,String.valueOf(grpId),moduleId);
//                if(albumlist!= null && albumlist.size() > 0) {
//                    if(isInGridviewMode) {
//                        gridAdapter = new AlbumAdapter(Gallery.this, albumlist, "1");
//                        gv.setAdapter(gridAdapter);
//                        gridAdapter.notifyDataSetChanged();
//                    }else{
//                        listAdapter = new GalleryListAdapter(Gallery.this, albumlist, "1",fromShowcase);
//                        mRecyclerView.setAdapter(listAdapter);
//                        listAdapter.notifyDataSetChanged();
//                    }
//
//                } else {
////                    Toast.makeText(Gallery.this, "No Albums Found ", Toast.LENGTH_SHORT).show();
//                    //tv_no_records_found
//                    if(isInGridviewMode) {
//                        TextView tvEmpty = (TextView) findViewById(R.id.tv_no_records_found);
//                        gv.setEmptyView(tvEmpty);
//                        gv.setAdapter(null);
//                    }else{
//                        albumlist.add(new AlbumData("-1","","","","","",""));
//                        listAdapter = new GalleryListAdapter(Gallery.this, albumlist, "1",fromShowcase);
//                        mRecyclerView.setAdapter(listAdapter);
//                        listAdapter.notifyDataSetChanged();
//
//                    }
//                }

            }
        });*/

      /*  btn_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ll_search.setVisibility(View.GONE);
                isSearchVisible = false;
                edt_search.setText("");
                //  et_serach_club.setText("");
//                albumlist = albumModel.getAlbums(String.valueOf(grpId),moduleId);
//                gridAdapter = new AlbumAdapter(Gallery.this, albumlist, "1");
//                gv.setAdapter(gridAdapter);
//                gridAdapter.notifyDataSetChanged();
                hideInput();
            }
        });*/

//        iv_actionbtn2.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if(mode!=1) {
////
////                    if (!isInGridviewMode) {
////
////                        isInGridviewMode = true;
////                        iv_actionbtn2.setImageResource(R.drawable.list_view);
////                        mRecyclerView.setVisibility(View.GONE);
////                        gv.setVisibility(View.VISIBLE);
////                        gridAdapter = new AlbumAdapter(Gallery.this, albumlist, "1");
////                        gv.setAdapter(gridAdapter);
////
////
////                    } else {
////                        isInGridviewMode = false;
////                        iv_actionbtn2.setImageResource(R.drawable.grid_view);
////                        gv.setVisibility(View.GONE);
////                        mRecyclerView.setVisibility(View.VISIBLE);
////                        listAdapter = new GalleryListAdapter(Gallery.this, albumlist, "1",fromShowcase);
////                        mRecyclerView.setAdapter(listAdapter);
////                    }
////                }else{
////                    if (!isInGridviewMode) {
////                        isInGridviewMode = true;
////                        iv_actionbtn2.setImageResource(R.drawable.list_view);
////                        mRecyclerView.setVisibility(View.GONE);
////                        gv.setVisibility(View.VISIBLE);
////                        gridAdapter = new AlbumAdapter(Gallery.this, albumlist, "0");
////                        gv.setAdapter(gridAdapter);
////
////
////                    } else {
////                        isInGridviewMode = false;
////                        iv_actionbtn2.setImageResource(R.drawable.grid_view);
////                        gv.setVisibility(View.GONE);
////                        mRecyclerView.setVisibility(View.VISIBLE);
////                        listAdapter = new GalleryListAdapter(Gallery.this, albumlist, "0",fromShowcase);
////                        mRecyclerView.setAdapter(listAdapter);
////                    }
////                }
////            }
////        });
    }

    public void loadFromDB() {

        Log.d("Touchbase", "Trying to load Album from local db");

        albumlist = albumModel.getAlbums(String.valueOf(grpId), moduleId);

        boolean isDataAvailable = albumModel.isDataAvailable(grpId, moduleId);

        Log.e("DataAvailable", "Data available : " + isDataAvailable);

        if (!isDataAvailable) {

            Log.d("Touchbase---@@@@@@@@", "Loading from server");

            if (InternetConnection.checkConnection(this))
                webservices();
            else
                Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();

        } else {

            GalleryListAdapter adapter = new GalleryListAdapter(Gallery.this, albumlist, "1", fromShowcase);

            mRecyclerView.setAdapter(adapter);

            if (InternetConnection.checkConnection(this)) {
                checkForUpdate();
                Log.d("---------------", "Check for update gets called------");
            } else {
                Toast.makeText(this, "No internet connection to get Updated Records", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void checkForUpdate() {

        isinUpdatemode = true;

        Log.e("Touchbase", "------ checkForUpdate() called for update");

        String url = Constant.GetAllAlbumList;
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("profileId", PreferenceManager.getPreference(this, PreferenceManager.GRP_PROFILE_ID)));
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("moduleId", PreferenceManager.getPreference(this, PreferenceManager.MODULE_ID)));
        updatedOn = PreferenceManager.getPreference(this, TBPrefixes.GALLERY_PREFIX + moduleId + "_" + grpId, "1970/01/01 00:00:00");
        Log.e("UpdatedOn", "Last updated date is : " + updatedOn);
        arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));//updatedOn 1970-1-1 0:0:0
        Log.e("UpdatedOn", "Last updated date is : " + updatedOn);
        ;
        Log.e("request", arrayList.toString());
        GalleryDataAsyncTask task = new GalleryDataAsyncTask(url, arrayList, this);
        task.execute();

        Log.d("Response", "PARAMETERS " + Constant.GetAllAlbumList + " :- " + arrayList.toString());
    }

    public void webservices() {

        Log.e("Touchbase", "------ webservices() called for 1st time");
        String url = Constant.GetAllAlbumList;
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("profileId", PreferenceManager.getPreference(this, PreferenceManager.GRP_PROFILE_ID)));
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("moduleId", moduleId));
        updatedOn = PreferenceManager.getPreference(this, TBPrefixes.GALLERY_PREFIX + moduleId + "_" + grpId, "1970/01/01 00:00:00");
        Log.e("UpdatedOn", "Last updated date is : " + updatedOn);
        arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));//updatedOn 1970-1-1 0:0:0
        Log.e("UpdatedOn", "Last updated date is : " + updatedOn);
        ;


        Log.d("Request", "PARAMETERS " + Constant.GetAllAlbumList + " :- " + arrayList.toString());
        GalleryDataAsyncTask task = new GalleryDataAsyncTask(url, arrayList, this);
        task.execute();
    }


    public class GalleryDataAsyncTask extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(Gallery.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;

        public GalleryDataAsyncTask(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            if (!isinUpdatemode) {
                progressDialog.show();
            }
        }

        @Override
        protected Object doInBackground(String... params) {
            try {
                val = HttpConnection.postData(url, argList);
                val = val.toString();
                Log.d("Response", "we" + val);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return val;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            isinUpdatemode = false;
            if (result != "") {
                Log.d("Response", "calling getAllAlbumList");

                getGalleryData(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }
    }


    public void getGalleryData(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);

            JSONObject jsonTBAlbumsListResult = jsonObj.getJSONObject("TBAlbumsListResult");
            final String status = jsonTBAlbumsListResult.getString("status");

            if (status.equals("0")) {

                updatedOn = jsonTBAlbumsListResult.getString("updatedOn");
                final ArrayList<AlbumData> newAlbums = new ArrayList<AlbumData>();

                JSONObject jsonResult = jsonTBAlbumsListResult.getJSONObject("Result");

                JSONArray jsonNewAlbumList = jsonResult.getJSONArray("newAlbums");

                int newAlbumListCount = jsonNewAlbumList.length();

                for (int i = 0; i < newAlbumListCount; i++) {

                    AlbumData data = new AlbumData();

                    JSONObject result_object = jsonNewAlbumList.getJSONObject(i);
                    data.setAlbumId(result_object.getString("albumId").toString());
                    data.setTitle(result_object.getString("title").toString());
                    data.setDescription(result_object.getString("description").toString());
                    data.setGrpId(result_object.getString("groupId").toString());
                    data.setIsAdmin(result_object.getString("isAdmin").toString());
                    data.setModuleId(result_object.getString("moduleId").toString());

                    if (result_object.has("image")) {
                        data.setImage(result_object.getString("image").toString());
                    } else {
                        data.setImage("");
                    }

                    newAlbums.add(data);

                }

                final ArrayList<AlbumData> UpdatedAlbumList = new ArrayList<AlbumData>();
                JSONArray jsonUpdatedAlbumList = jsonResult.getJSONArray("updatedAlbums");
                int updateAlbumListCount = jsonUpdatedAlbumList.length();
                for (int i = 0; i < updateAlbumListCount; i++) {

                    AlbumData data = new AlbumData();

                    JSONObject result_object = jsonUpdatedAlbumList.getJSONObject(i);

                    data.setAlbumId(result_object.getString("albumId").toString());
                    data.setTitle(result_object.getString("title").toString());
                    data.setDescription(result_object.getString("description").toString());
                    data.setGrpId(result_object.getString("groupId").toString());
                    data.setIsAdmin(result_object.getString("isAdmin").toString());
                    data.setModuleId(result_object.getString("moduleId").toString());

                    if (result_object.has("image")) {
                        data.setImage(result_object.getString("image").toString());
                    } else {
                        data.setImage("");
                    }
                    UpdatedAlbumList.add(data);

                }

                final ArrayList<AlbumData> DeletedAlbumList = new ArrayList<AlbumData>();
                String jsonDeletedAlbumList = jsonResult.getString("deletedAlbums");
                int deleteAlbumListCount = 0;
                if (!jsonDeletedAlbumList.equalsIgnoreCase("")) {

                    String[] deletedAlbumArray = jsonDeletedAlbumList.split(",");
                    deleteAlbumListCount = deletedAlbumArray.length;

                    for (int i = 0; i < deleteAlbumListCount; i++) {
                        AlbumData data = new AlbumData();
                        data.setAlbumId(String.valueOf(deletedAlbumArray[i].toString()));
                        DeletedAlbumList.add(data);
                    }

                }

                Handler Albumdatahandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        boolean saved = albumModel.syncData(grpId, newAlbums, UpdatedAlbumList, DeletedAlbumList);
                        if (!saved) {
                            Log.e("SyncFailed------->", "Failed to update data in local db. Retrying in 2 seconds");
                            sendEmptyMessageDelayed(0, 2000);
                        } else {
                            PreferenceManager.savePreference(Gallery.this, TBPrefixes.GALLERY_PREFIX + moduleId + "_" + grpId, updatedOn);
//                            albumlist = new ArrayList<>();
//                            albumlist = albumModel.getAlbums(String.valueOf(grpId));
//                            adapter = new AlbumAdapter(Gallery.this, albumlist, "1");
//                            gv.setAdapter(adapter);


                            albumlist = new ArrayList<>();
                            albumlist = albumModel.getAlbums(String.valueOf(grpId), moduleId);
                            if (!isInGridviewMode) {
                                listAdapter = new GalleryListAdapter(Gallery.this, albumlist, "1", fromShowcase);
                                mRecyclerView.setAdapter(listAdapter);
                            } else {
                                gridAdapter = new AlbumAdapter(Gallery.this, albumlist, "1");
                                gv.setAdapter(gridAdapter);
                            }
                            Log.e("AlbumList", albumlist.toString());
                        }
                    }
                };

                int overAllCount = newAlbumListCount + updateAlbumListCount + deleteAlbumListCount;

                System.out.println("Number of records received for albums  : " + overAllCount);
                if (newAlbumListCount + updateAlbumListCount + deleteAlbumListCount != 0) {
                    Albumdatahandler.sendEmptyMessageDelayed(0, 1000);
                } else {
                    TextView tvEmpty = (TextView) findViewById(R.id.tv_no_records_found);
                    gv.setEmptyView(tvEmpty);
                    gv.setAdapter(null);
                    Log.e("NoUpdate", "No updates found");
                }
            }
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {

        if (materialDesignFAM.isOpened()) {
            materialDesignFAM.close(false);
        }
//        else if(mode==1&&adapter.getIsdelete().equalsIgnoreCase("true")){
//            checkForUpdate();
//        }

        else if (mode == 1) {

            if (!isInGridviewMode) {

                listAdapter = new GalleryListAdapter(Gallery.this, albumlist, "1", fromShowcase);

                if (listAdapter.getIsdelete().equalsIgnoreCase("true")) {
                    getAlbumList();
                } else {
                    mRecyclerView.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();
                    mode = 0;
                    materialDesignFAM.setVisibility(View.VISIBLE);
                }
            } else {
                gridAdapter = new AlbumAdapter(Gallery.this, albumlist, "1");
                if (gridAdapter.getIsdelete().equalsIgnoreCase("true")) {
                    checkForUpdate();
                } else {
                    gv.setAdapter(gridAdapter);
                    gridAdapter.notifyDataSetChanged();
                    mode = 0;
                    materialDesignFAM.setVisibility(View.VISIBLE);
                }
            }

        } else {
            super.onBackPressed();
        }
    }

    public void hideInput() {

        try {

            final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

            if (inputMethodManager.isActive()) {

                if (getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showInput() {

        try {

            final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

            if (inputMethodManager.isActive()) {

                if (getCurrentFocus() != null) {
                    //inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    //inputMethodManager.showSoftInputFromInputMethod(getCurrentFocus().getWindowToken(), 0);
                    inputMethodManager.showSoftInput(edt_search, InputMethodManager.SHOW_IMPLICIT);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void movetoclub(String albumid, String flg)
    {

        materialDesignFAM.close(false);



            String groupType = PreferenceManager.getPreference(Gallery.this, PreferenceManager.MY_CATEGORY, "1");

            if (flg.equalsIgnoreCase("2")) {


                if (groupType.equals("" + Constant.GROUP_CATEGORY_DT)) {
                    Intent i = new Intent(Gallery.this, DTAddAlbum.class);

                    i.putExtra("serviceprojecttt", 0);
                    i.putExtra("maxBeneficiaries", maxBeneficiaries);

                    startActivityForResult(i, UPDATE_ALBUM_REQEUST);
                } else {
                    Intent i = new Intent(Gallery.this, EditAlbumActivity.class);

                    // i.putExtra("albumId", albumlist.get(position).getAlbumId());

                    i.putExtra("serviceprojecttt", 0);
                    i.putExtra("albumId", albumid);
                    i.putExtra("description", "");
                    i.putExtra("albumImage", "");
                    i.putExtra("albumname", "");
                    i.putExtra("header", "Club Meeting");


//                i.putExtra("serviceproject", 0);
//                i.putExtra("MemberCount", MemberCount);
//                i.putExtra("maxBeneficiaries", maxBeneficiaries);

                    //  i.putExtra("maxBeneficiaries", maxBeneficiaries);
                    startActivityForResult(i, UPDATE_ALBUM_REQEUST);
                }
            }
            if (flg.equalsIgnoreCase("3")) {


                if (groupType.equals("" + Constant.GROUP_CATEGORY_DT)) {
                    Intent i = new Intent(Gallery.this, DTAddAlbum.class);

                    i.putExtra("serviceproject", 1);
                    i.putExtra("maxBeneficiaries", maxBeneficiaries);


                    startActivityForResult(i, UPDATE_ALBUM_REQEUST);
                } else {
                    Intent i = new Intent(Gallery.this, EditAlbumActivity.class);
//                i.putExtra("serviceproject", "1");
//                i.putExtra("MemberCount", MemberCount);
//                i.putExtra("maxBeneficiaries", maxBeneficiaries);

                    i.putExtra("serviceprojecttt", 1);
                    i.putExtra("albumId", albumid);
                    i.putExtra("description", "");
                    i.putExtra("albumImage", "");
                    i.putExtra("albumname", "");
                    i.putExtra("header", "Service Project");


                    //  i.putExtra("maxBeneficiaries", maxBeneficiaries);
                    startActivityForResult(i, UPDATE_ALBUM_REQEUST);
                }
            }



        }

    public void popup1(String title) {

        // dialog = new Dialog(Gallery.this, android.R.style.Theme_Translucent);
        final Dialog  dialog = new Dialog(Gallery.this, android.R.style.Theme_Translucent);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sprilist);

         final RecyclerView ongrecyl;
         TextView pro_title;


         pro_title = dialog.findViewById(R.id.pro_title);
         ongrecyl = (RecyclerView) dialog.findViewById(R.id.sub_list);

        ongrecyl.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        ongrecyl.setLayoutManager(mLayoutManager);

        ongrecyl.setAdapter(subpro_adapter);

        pro_title.setText(title);



        ImageView tv_yes = (ImageView) dialog.findViewById(R.id.cut);
        tv_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }

//   sub project listing

    public void getong_pro(String id, final String title) {

        try {

            progressDialog = new ProgressDialog(Gallery.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

             // Log.d("Responseeeeee", "PARAMETERS " + Constant.GetAlbumsList_New + " :- " + requestData.toString());
            JSONObject requestData = new JSONObject();
            requestData.put("GallaryId",id);
            requestData.put("FinancialYear",year);


            Log.d("Responseeeeee", "PARAMETERS " + Constant.ongproject_detail + " :- " + requestData.toString());
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.ongproject_detail,requestData , new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }

                    JSONObject result;
                    Log.d("ongoingprojectdetails", "PARAMETERS " + Constant.ongproject_detail + " :- " + response.toString());

                    subproject(response, title);
                    Utils.log(response.toString());

                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }

                    Utils.log("VollyError:- " + error.toString());

                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(120000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(Gallery.this, request);




        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void subproject(JSONObject response, String titl) {

        try {
            String status = response.getString("status");

            if (status.equalsIgnoreCase("0")) {

                JSONObject businessObject = response.getJSONObject("SubProjResult");

                JSONArray detail = businessObject.getJSONArray("Table");


                JSONArray ttl = businessObject.getJSONArray("Table1");

               //
               // Subpro_model subpro_model = new Subpro_model();

                subpro_arr = new ArrayList<>();


                String TtlOfNewOngoingProj = "";
                for (int i = 0; i < ttl.length(); i++) {

                    Subpro_model subpro_model2 = new Subpro_model();

                    JSONObject categoryObj = ttl.getJSONObject(i);

                    TtlOfNewOngoingProj = (categoryObj.getString("TtlOfNewOngoingProj"));//

                    if(TtlOfNewOngoingProj == null)
                    {
                        TtlOfNewOngoingProj = "";                    }
                    else if(TtlOfNewOngoingProj.equalsIgnoreCase("" )|| TtlOfNewOngoingProj.isEmpty())
                    {
                        TtlOfNewOngoingProj = "";
                    }
                    else
                    {

                    }






//                    subpro_model2.setTtlOfNewOngoingProj(TtlOfNewOngoingProj);
//
//                    subpro_arr.add(subpro_model2);

                   // subpro_arr.add(subpro_model);

                }

              //  Table1":[{"TtlOfNewOngoingProj"


                for (int i = 0; i < detail.length(); i++) {
                    Subpro_model subpro_model = new Subpro_model();
                    JSONObject categoryObj = detail.getJSONObject(i);

                    String pk_gallery_id = (categoryObj.getString("pk_gallery_id"));
                    String fk_group_master_id = (categoryObj.getString("fk_group_master_id"));
                    String date_of_project = (categoryObj.getString("date_of_project"));
                    String album_title = (categoryObj.getString("album_title"));
                    String cost_of_project = (categoryObj.getString("cost_of_project"));
                    String beneficiary = (categoryObj.getString("beneficiary"));
                    String man_hours_spent = (categoryObj.getString("man_hours_spent"));
                    String NumberOfRotarian = (categoryObj.getString("NumberOfRotarian"));
                    String Rotaractors = (categoryObj.getString("Rotaractors"));
                    String OnetimeOrOngoing = (categoryObj.getString("OnetimeOrOngoing"));
                    String NewOrExisting = (categoryObj.getString("NewOrExisting"));

                    subpro_model.setPk_gallery_id(pk_gallery_id);
                    subpro_model.setFk_group_master_id(fk_group_master_id);
                    subpro_model.setDate_of_project(date_of_project);
                    subpro_model.setAlbum_title(album_title);
                    subpro_model.setCost_of_project(cost_of_project);
                    subpro_model.setBeneficiary(beneficiary);
                    subpro_model.setMan_hours_spent(man_hours_spent);
                    subpro_model.setNumberOfRotarian(NumberOfRotarian);

                    subpro_model.setRotaractors(Rotaractors);
                    subpro_model.setOnetimeOrOngoing(OnetimeOrOngoing);
                    subpro_model.setNewOrExisting(NewOrExisting);

                    subpro_arr.add(subpro_model);

                }

                subpro_adapter = new Subpro_adapter(getApplicationContext(), subpro_arr);


                Log.i("getcountofresult", String.valueOf(subpro_adapter.getItemCount()));


               // Subpro_model subpro_model = new Subpro_model();
                String ttle ="";
                try {
                     ttle = TtlOfNewOngoingProj;

                    if (ttle == null) {
                        ttle = "";
                    }
                }catch (Exception e)
                {
                    ttle ="";
                    e.printStackTrace();
                }

                popup1(ttle);


            } else {
                //progressDialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //progressDialog.dismiss();
        }

    }


    public void popup1() {

        final Dialog dialog = new Dialog(Gallery.this, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.serv_pop);


        dialog.setCanceledOnTouchOutside(false);

        Button tv_yes = (Button) dialog.findViewById(R.id.serbtn2);
        tv_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(Gallery.this, AddAlbum.class);
                i.putExtra("serviceproject", serviceproject);
                i.putExtra("MemberCount", MemberCount);
                i.putExtra("maxBeneficiaries", maxBeneficiaries);
                //  i.putExtra("maxBeneficiaries", maxBeneficiaries);
                startActivityForResult(i, UPDATE_ALBUM_REQEUST);


                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private String[] getmoveordelete(String albumidd) {
        final String[] sb_ct = new String[1];

        try {

            progressDialog = new ProgressDialog(Gallery.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject();
            requestData.put("GallaryId",albumidd);
            requestData.put("FinancialYear",year);


            Log.d("Responseeeeee", "PARAMETERS " + Constant.Getprojectmove_or_delete + " :- " + requestData.toString());

            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.Getprojectmove_or_delete, requestData, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    JSONObject result;
                    Log.d("move_result", "PARAMETERS " + Constant.Getprojectmove_or_delete + " :- " + response.toString());

                   // getAlbumData(response);

                 //  sb_ct[0] =   getmove_or_deletereponse(response);

                    Utils.log(response.toString());

                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    sb_ct[0]= "0";

                    Log.d("move_result", "PARAMETERS " + Constant.Getprojectmove_or_delete + " :- " + error.toString());

                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }

                    Utils.log("VollyError:- " + error.toString());

                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(120000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(Gallery.this, request);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb_ct;

    }



    private void getmove_or_deletereponse(JSONObject response, String alb_id, String typ) {
        String sub_ct= "";
        try {

               // progressDialogg.dismiss();


            JSONObject TBAlbumsListResult = response;
            String status = TBAlbumsListResult.getString("status");

            if (status.equalsIgnoreCase("0")) {

                JSONObject resultObject = TBAlbumsListResult.getJSONObject("ProjResult");
                // added by prashant sahani


                JSONArray newAlbums = resultObject.getJSONArray("Table");

                JSONObject albumObj = newAlbums.getJSONObject(0);
                sub_ct = albumObj.getString("SubCount");


               // Toast.makeText(this, "tt"+sub_ct, Toast.LENGTH_SHORT).show();


                if(sub_ct.equalsIgnoreCase("0"))
                {
                    movetoclub(alb_id,typ);
                }
                else{
                    Toast.makeText(getApplicationContext(),"This is main project. You can not move this project",Toast.LENGTH_LONG).show();
                }



            }
            else{

            }





             } catch (JSONException e) {
            e.printStackTrace();
            Utils.log(e.getMessage());
         //   progressDialog.dismiss();
        }

    }


    public void movetestt(final String alb_id, final String typ) {


        try {

            final ProgressDialog progressDialogg = new ProgressDialog(Gallery.this, R.style.TBProgressBar);
            progressDialogg.setCancelable(false);
            progressDialogg.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialogg.show();

            JSONObject requestData = new JSONObject();

            requestData.put("GallaryId",alb_id);
            requestData.put("FinancialYear",year);


                    Log.d("Responseeeeee", "PARAMETERS " + Constant.Getprojectmove_or_delete + " :- " + requestData.toString());

            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.Getprojectmove_or_delete, requestData, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    progressDialogg.dismiss();


                    JSONObject result;
                    Log.d("Responseeeeee", "PARAMETERS " + Constant.Getprojectmove_or_delete + " :- " + response.toString());


                    getmove_or_deletereponse(response,alb_id, typ);


                    Utils.log(response.toString());

                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressDialog != null) {
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

            AppController.getInstance().addToRequestQueue(Gallery.this, request);

        } catch (Exception e) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            e.printStackTrace();
        }


    }

    public String delete_single(final String alb_id, final String typ, final int positionn) {

        try {

            progressDialog = new ProgressDialog(Gallery.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject();

            requestData.put("GallaryId",alb_id);
            requestData.put("FinancialYear",year);


            Log.d("Responseeeeee", "PARAMETERS " + Constant.Getprojectmove_or_delete + " :- " + requestData.toString());

            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.Getprojectmove_or_delete, requestData, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    if (progressDialog != null) {

                        progressDialog.dismiss();
                    }

                    JSONObject result;
                    Log.d("Responseeeeee", "PARAMETERS " + Constant.Getprojectmove_or_delete + " :- " + response.toString());


                   if(delete_respone(response,alb_id, typ, positionn));
                    {
                        progressDialog.dismiss();
                        ret_v = true;
                    }


                    //=============


//                    boolean ret = false;
//                    try {
//
//                        JSONObject TBAlbumsListResult = response;
//                        String status = TBAlbumsListResult.getString("status");
//
//                        if (status.equalsIgnoreCase("0")) {
//
//                            JSONObject resultObject = TBAlbumsListResult.getJSONObject("ProjResult");
//                            // added by prashant sahani
//
//                            JSONArray newAlbums = resultObject.getJSONArray("Table");
//
//                            JSONObject albumObj = newAlbums.getJSONObject(0);
//                            sub_ct = albumObj.getString("SubCount");
//
//
//                            if(sub_ct.equalsIgnoreCase("0"))
//                            {
//                                delt_flg = 1;
//
//                                ret_v = true;
//                            }
//                            else
//                            {
//                                delt_flg = 0;
//
//                                ret_v = false;
//
//                                Toast.makeText(getApplicationContext(),"This is main project, You can not delete this project.",Toast.LENGTH_LONG).show();
//                            }
//
//                        }
//                        else{
//                            Toast.makeText(getApplicationContext(),"Something went wrong ",Toast.LENGTH_LONG).show();
//
//                        }
//
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Utils.log(e.getMessage());
//                        progressDialog.dismiss();
//                    }
//



                   // ==========






                    Utils.log(response.toString());

                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressDialog != null) {
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

            AppController.getInstance().addToRequestQueue(Gallery.this, request);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sub_ct;


    }


    private boolean delete_respone(JSONObject response, String alb_id, String typ, int position) {
        String sub_ct= "";
        boolean ret = false;
        try {

            JSONObject TBAlbumsListResult = response;
            String status = TBAlbumsListResult.getString("status");

            if (status.equalsIgnoreCase("0")) {

                JSONObject resultObject = TBAlbumsListResult.getJSONObject("ProjResult");
                // added by prashant sahani

                JSONArray newAlbums = resultObject.getJSONArray("Table");

                JSONObject albumObj = newAlbums.getJSONObject(0);
                sub_ct = albumObj.getString("SubCount");


              //  Toast.makeText(this, "tt"+sub_ct, Toast.LENGTH_SHORT).show();


                if(sub_ct.equalsIgnoreCase("0"))
                {
                    delt_flg = 1;

                    ret = true;

                  GalleryListAdapter.deleteAlbum(alb_id, position);

                    //movetoclub(alb_id,typ);
                }
                else
                {
                    delt_flg = 0;

                    ret = false;
                     progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"This is main project, You can not delete this project.",Toast.LENGTH_LONG).show();
                }

            }
            else{
                Toast.makeText(getApplicationContext(),"Something went wrong ",Toast.LENGTH_LONG).show();

            }



        } catch (JSONException e) {
            e.printStackTrace();
            Utils.log(e.getMessage());
            progressDialog.dismiss();
        }
        return ret;

    }




}



