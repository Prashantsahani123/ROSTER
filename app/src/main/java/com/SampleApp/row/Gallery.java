package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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

import com.SampleApp.row.Adapter.AlbumAdapter;
import com.SampleApp.row.Adapter.GalleryListAdapter;
import com.SampleApp.row.Data.AlbumData;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.TBPrefixes;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.sql.GalleryMasterModel;
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

    public static final int UPDATE_ALBUM_REQEUST = 25;
    TextView tv_title,tv_no_records_found;
    ImageView iv_backbutton;
    EditText et_serach;
    private ImageView iv_actionbtn,iv_actionbtn2;
    GridView gv;
    FloatingActionButton fab;
    FloatingActionMenu materialDesignFAM;
    com.github.clans.fab.FloatingActionButton addAlbum, deleteAlbum;
    ArrayList<AlbumData> albumlist = new ArrayList<AlbumData>();
    GalleryMasterModel albumModel;
    String updatedOn = "";
    private long grpId;
    AlbumAdapter gridAdapter;
    String moduleName = "";
    int mode = 0;
    RelativeLayout ll_search;
    Boolean isSearchVisible = false;
    EditText edt_search;
    Button btn_close;
    boolean isinUpdatemode = false;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    boolean isInGridviewMode = false;
    GalleryListAdapter  listAdapter;
    String moduleId = "";
    ProgressDialog progressDialog;
    String district_id,year,club_id,fromShowcase = "1",category_id="0";
    ArrayList<AlbumData> categoryList;
    LinearLayout ll_details;
    TextView tv_cop,tv_beneficiary,tv_manHrSpent,tv_top;
    Spinner sp_year;
    private String fromYear, toYear;
    ArrayList<String> filtertype;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_gallery);

        if(PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID)!=null) {
            grpId = Long.parseLong(PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID));
        }

        moduleId = PreferenceManager.getPreference(this,PreferenceManager.MODULE_ID);

        gv = (GridView)findViewById(R.id.gridview);

        Intent intent = getIntent();

        this.moduleName = PreferenceManager.getPreference(this, PreferenceManager.MODUEL_NAME, "Gallery");

        String modulename = intent.getExtras().getString("moduleName", "Gallery");

        district_id = intent.getExtras().getString("districtId","0");
        club_id = intent.getExtras().getString("clubId","0");
        year = intent.getExtras().getString("year","");
        fromShowcase = intent.getExtras().getString("fromShowcase","1");
        category_id = intent.getExtras().getString("categoryList","0");


        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_no_records_found = (TextView)findViewById(R.id.tv_no_records_found);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn2 = (ImageView)findViewById(R.id.iv_actionbtn2);
        iv_actionbtn2.setImageResource(R.drawable.grid_view);
        iv_actionbtn.setImageResource(R.drawable.search_blue);
        iv_actionbtn.setVisibility(View.GONE);
        iv_actionbtn2.setVisibility(View.GONE);

        ll_details = (LinearLayout)findViewById(R.id.ll_details);
        tv_cop = (TextView)findViewById(R.id.tv_cop);
        tv_beneficiary = (TextView)findViewById(R.id.tv_beneficiary);
        tv_manHrSpent = (TextView)findViewById(R.id.tv_manHrSpent);
        tv_top = (TextView)findViewById(R.id.tv_top);

        sp_year = (Spinner) findViewById(R.id.sp_year);

        ll_search = (RelativeLayout) findViewById(R.id.ll_search);
        edt_search = (EditText)findViewById(R.id.edt_search);
        btn_close = (Button)findViewById(R.id.btn_close);
        fab = (FloatingActionButton)findViewById(R.id.fab);

        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        addAlbum = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        deleteAlbum = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);

        albumModel = new GalleryMasterModel(this);
        albumModel.printTable();

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if(fromShowcase.equalsIgnoreCase("0")){

            tv_title.setText("Showcase");
            ll_details.setVisibility(View.GONE);
            sp_year.setVisibility(View.GONE);

            getAlbumList();

        } else {

            tv_title.setText(modulename);
            ll_details.setVisibility(View.GONE);
            sp_year.setVisibility(View.VISIBLE);

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
                    i.putExtra("fromShowcase",fromShowcase);
                    startActivityForResult(i, UPDATE_ALBUM_REQEUST);
                }
            }

        });

        init();
        checkadminrights();

        //loadFromDB();
        if(InternetConnection.checkConnection(this)) {
            //getAlbumList();
        } else {
            Toast.makeText(Gallery.this,"No Internet Connection.",Toast.LENGTH_SHORT).show();
        }

    }

    private void getAlbumList() {

        try {
            progressDialog=new ProgressDialog(Gallery.this,R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JSONObject requestData = new JSONObject();
            requestData.put("groupId", PreferenceManager.getPreference(Gallery.this,PreferenceManager.GROUP_ID));
            requestData.put("profileId", PreferenceManager.getPreference(this, PreferenceManager.GRP_PROFILE_ID));
            requestData.put("moduleId",moduleId);
            requestData.put("district_id", district_id);
            requestData.put("club_id", club_id);
            requestData.put("category_id", category_id);
            requestData.put("year", year);
            requestData.put("SharType",fromShowcase);


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

            request.setRetryPolicy(
                    new DefaultRetryPolicy(120000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(Gallery.this, request);

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
                        albumlist.add(data);
                    }
                    listAdapter = new GalleryListAdapter(Gallery.this, albumlist, "1",fromShowcase);
                    mRecyclerView.setAdapter(listAdapter);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    tv_no_records_found.setVisibility(View.GONE);
                }else{
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

    @Override
    protected void onResume() {
        super.onResume();
        checkadminrights();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == UPDATE_ALBUM_REQEUST) {
            if (InternetConnection.checkConnection(this)) {
                //checkForUpdate();
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
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, filtertype);
        sp_year.setAdapter(spinnerArrayAdapter);


        addAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDesignFAM.close(false);
                String groupType = PreferenceManager.getPreference(Gallery.this, PreferenceManager.MY_CATEGORY, "1");
                if ( groupType.equals(""+Constant.GROUP_CATEGORY_DT)) {
                    Intent i = new Intent(Gallery.this, DTAddAlbum.class);
                    startActivityForResult(i, UPDATE_ALBUM_REQEUST);
                } else {
                    Intent i = new Intent(Gallery.this, AddAlbum.class);
                    startActivityForResult(i, UPDATE_ALBUM_REQEUST);
                }
            }
        });

        deleteAlbum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mode = 1;
                materialDesignFAM.close(false);
                materialDesignFAM.setVisibility(View.GONE);

                if(isInGridviewMode) {
                    gridAdapter = new AlbumAdapter(Gallery.this, albumlist, "0");
                    gv.setAdapter(gridAdapter);
                }else{
                    listAdapter = new GalleryListAdapter(Gallery.this,albumlist,"0",fromShowcase);
                    mRecyclerView.setAdapter(listAdapter);
                }
            }
        });

        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSearchVisible){
                    isSearchVisible = false;
                    ll_search.setVisibility(View.GONE);
                    hideInput();
                }else {
                    ll_search.setVisibility(View.VISIBLE);
                    isSearchVisible = true;
                    edt_search.requestFocus();
                    showInput();
                }
            }
        });

    }
    private void checkadminrights() {

        if(fromShowcase.equalsIgnoreCase("0")){
            materialDesignFAM.setVisibility(View.GONE);
        }else {
            if (PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN).equals("No")) {
                //iv_actionbtn.setVisibility(View.GONE);
                materialDesignFAM.setVisibility(View.GONE);
            } else {
                materialDesignFAM.setVisibility(View.VISIBLE);
            }
        }


        edt_search.addTextChangedListener(new TextWatcher() {

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
                        if (c.getClub_Name().toLowerCase().contains(s.toString().toLowerCase())||c.getTitle().toLowerCase().contains(s.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }

                if(tempArrayList.size()<1){
                    mRecyclerView.setVisibility(View.GONE);
                    tv_no_records_found.setVisibility(View.VISIBLE);
                }else {
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

        btn_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ll_search.setVisibility(View.GONE);
                isSearchVisible = false;
                edt_search.setText("");
//                albumlist = albumModel.getAlbums(String.valueOf(grpId),moduleId);
//                gridAdapter = new AlbumAdapter(Gallery.this, albumlist, "1");
//                gv.setAdapter(gridAdapter);
//                gridAdapter.notifyDataSetChanged();
                hideInput();
            }
        });

        iv_actionbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode!=1) {

                    if (!isInGridviewMode) {

                        isInGridviewMode = true;
                        iv_actionbtn2.setImageResource(R.drawable.list_view);
                        mRecyclerView.setVisibility(View.GONE);
                        gv.setVisibility(View.VISIBLE);
                        gridAdapter = new AlbumAdapter(Gallery.this, albumlist, "1");
                        gv.setAdapter(gridAdapter);


                    } else {
                        isInGridviewMode = false;
                        iv_actionbtn2.setImageResource(R.drawable.grid_view);
                        gv.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        listAdapter = new GalleryListAdapter(Gallery.this, albumlist, "1",fromShowcase);
                        mRecyclerView.setAdapter(listAdapter);
                    }
                }else{
                    if (!isInGridviewMode) {
                        isInGridviewMode = true;
                        iv_actionbtn2.setImageResource(R.drawable.list_view);
                        mRecyclerView.setVisibility(View.GONE);
                        gv.setVisibility(View.VISIBLE);
                        gridAdapter = new AlbumAdapter(Gallery.this, albumlist, "0");
                        gv.setAdapter(gridAdapter);


                    } else {
                        isInGridviewMode = false;
                        iv_actionbtn2.setImageResource(R.drawable.grid_view);
                        gv.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        listAdapter = new GalleryListAdapter(Gallery.this, albumlist, "0",fromShowcase);
                        mRecyclerView.setAdapter(listAdapter);
                    }
                }
            }
        });
    }

    public void loadFromDB() {

        Log.d("Touchbase", "Trying to load Album from local db");

        albumlist = albumModel.getAlbums(String.valueOf(grpId),moduleId);

        boolean isDataAvailable = albumModel.isDataAvailable(grpId,moduleId);

        Log.e("DataAvailable", "Data available : " + isDataAvailable);

        if (!isDataAvailable) {

            Log.d("Touchbase---@@@@@@@@", "Loading from server");

            if (InternetConnection.checkConnection(this))
                webservices();
            else
                Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();

        } else {

            GalleryListAdapter adapter = new GalleryListAdapter(Gallery.this,albumlist,"1",fromShowcase);

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
        updatedOn = PreferenceManager.getPreference(this, TBPrefixes.GALLERY_PREFIX+moduleId+"_"+grpId,"1970/01/01 00:00:00");
        Log.e("UpdatedOn", "Last updated date is : " + updatedOn);
        arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));//updatedOn 1970-1-1 0:0:0
        Log.e("UpdatedOn", "Last updated date is : " + updatedOn);;
        Log.e("request", arrayList.toString());
        GalleryDataAsyncTask task = new GalleryDataAsyncTask(url, arrayList,this);
        task.execute();

        Log.d("Response", "PARAMETERS " + Constant.GetAllAlbumList + " :- " + arrayList.toString());
    }

    public void webservices(){

        Log.e("Touchbase", "------ webservices() called for 1st time");
        String url = Constant.GetAllAlbumList;
        ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("profileId", PreferenceManager.getPreference(this, PreferenceManager.GRP_PROFILE_ID)));
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("moduleId", moduleId));
        updatedOn = PreferenceManager.getPreference(this, TBPrefixes.GALLERY_PREFIX+moduleId+"_"+grpId, "1970/01/01 00:00:00");
        Log.e("UpdatedOn", "Last updated date is : " + updatedOn);
        arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));//updatedOn 1970-1-1 0:0:0
        Log.e("UpdatedOn", "Last updated date is : " + updatedOn);;


        Log.d("Request", "PARAMETERS " + Constant.GetAllAlbumList + " :- " + arrayList.toString());
        GalleryDataAsyncTask task = new GalleryDataAsyncTask(url, arrayList,this);
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
            if(!isinUpdatemode) {
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
            if(progressDialog.isShowing()){
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
                if(!jsonDeletedAlbumList.equalsIgnoreCase("")){

                    String[]deletedAlbumArray = jsonDeletedAlbumList.split(",");
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
                            PreferenceManager.savePreference(Gallery.this, TBPrefixes.GALLERY_PREFIX+moduleId+"_"+grpId, updatedOn);
//                            albumlist = new ArrayList<>();
//                            albumlist = albumModel.getAlbums(String.valueOf(grpId));
//                            adapter = new AlbumAdapter(Gallery.this, albumlist, "1");
//                            gv.setAdapter(adapter);


                            albumlist = new ArrayList<>();
                            albumlist = albumModel.getAlbums(String.valueOf(grpId),moduleId);
                            if(!isInGridviewMode) {
                                listAdapter = new GalleryListAdapter(Gallery.this, albumlist, "1",fromShowcase);
                                mRecyclerView.setAdapter(listAdapter);
                            }else{
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

        if(materialDesignFAM.isOpened()){
            materialDesignFAM.close(false);
        }
//        else if(mode==1&&adapter.getIsdelete().equalsIgnoreCase("true")){
//            checkForUpdate();
//        }

        else if(mode==1){

            if(!isInGridviewMode){
                listAdapter = new GalleryListAdapter(Gallery.this, albumlist, "1",fromShowcase);
                if(listAdapter.getIsdelete().equalsIgnoreCase("true")){
                    getAlbumList();
                }
                else {
                    mRecyclerView.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();
                    mode = 0;
                    materialDesignFAM.setVisibility(View.VISIBLE);
                }
            }else {
                gridAdapter = new AlbumAdapter(Gallery.this, albumlist, "1");
                if(gridAdapter.getIsdelete().equalsIgnoreCase("true")){
                    checkForUpdate();
                }else {
                    gv.setAdapter(gridAdapter);
                    gridAdapter.notifyDataSetChanged();
                    mode = 0;
                    materialDesignFAM.setVisibility(View.VISIBLE);
                }
            }
        } else{
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
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}


