package com.SampleApp.row;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.CurrentEventsAdapter;
import com.SampleApp.row.Adapter.DashboardRVAdapter;
import com.SampleApp.row.Adapter.NavAdapter;
import com.SampleApp.row.Data.BlogFeed;
import com.SampleApp.row.Data.DashboardData;
import com.SampleApp.row.Data.GroupData;
import com.SampleApp.row.Data.ModuleData;
import com.SampleApp.row.Data.NewsFeed;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.TBPrefixes;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.services.DirectorySyncService;
import com.SampleApp.row.sql.GroupMasterModel;
import com.SampleApp.row.sql.ModuleDataModel;
import com.SampleApp.row.sql.RSSFeedsModel;
import com.SampleApp.row.sql.RotaryBlogsModel;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import me.relex.circleindicator.CircleIndicator;

import static com.SampleApp.row.Utils.PreferenceManager.GROUP_ID;
import static com.SampleApp.row.Utils.PreferenceManager.GROUP_NAME;
import static com.SampleApp.row.Utils.PreferenceManager.GRP_PROFILE_ID;
import static com.SampleApp.row.Utils.PreferenceManager.IS_AG;
import static com.SampleApp.row.Utils.PreferenceManager.IS_GRP_ADMIN;
import static com.SampleApp.row.Utils.PreferenceManager.MASTER_USER_ID;
import static com.SampleApp.row.Utils.PreferenceManager.MY_CATEGORY;
import static com.SampleApp.row.Utils.PreferenceManager.REQUESTED;
import static com.SampleApp.row.Utils.PreferenceManager.savePreference;

public class DashboardActivity extends AppCompatActivity {
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    public static DrawerLayout drawer;
    private ListView drawerList;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private static final Integer[] Images = {R.drawable.club_birhday, R.drawable.district_birthday, R.drawable.district_birthday, R.drawable.district_anniversary, R.drawable.event1, R.drawable.event2, R.drawable.event3, R.drawable.event4};
    private static final Integer[] drawer_Images = {R.drawable.schedule_blue,R.drawable.abt, R.drawable.priv_setting, R.drawable.faq, R.drawable.helpline};
    LinearLayout ll_findClub,ll_findRotarian,ll_showcase,ll_library,ll_RotaryNews,ll_RotaryBlog,ll_Rotaryorg,ll_GlobalRewards;
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    private ArrayList<Integer> drawer_Images_array = new ArrayList<Integer>();
    private CurrentEventsAdapter eventsAdapter;
    private ArrayList<DashboardData> eventList;
    Context context;
    RecyclerView rv_grpList;
    ArrayList<GroupData> grplist = new ArrayList<>();
    ArrayList<ModuleData> moduleList = new ArrayList<ModuleData>();
    private long masterUid;
    String updatedOn = "";
    GroupMasterModel groupModel;
    ModuleDataModel moduleDataModel;
    Toolbar toolbar;
    TextView title,tv_title;
    List<String> listDataHeader;
    int currentPage = 0;
    Timer timer;
    ImageView img_findClub,img_findRotarian;
    private RotaryBlogsModel blogsModel;
    private RSSFeedsModel feedModel;
    int ci=0;
    private JSONObject globalResponse;
    ArrayList<BlogFeed> blogList;
    ArrayList<NewsFeed> feedList;
    TextView img_def;
    Handler handler = new Handler();
    Runnable update;
    private ArrayList<DashboardData> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        setContentView(R.layout.activity_dashboard);

//        MarshMallowPermission permission=new MarshMallowPermission(this);
//        if(!permission.checkPermissionForExternalStorage()){
//            permission.requestPermissionForExternalStorage();
//        }

        init();

        loadFromDB();
    }

    @Override
    protected void onResume() {
        super.onResume();

        showEvents();

        if (InternetConnection.checkConnection(context)){
            eventServices();
            checkForUpdate();
        }

//        else {
//            // put default image
//            img_def.setVisibility(View.VISIBLE);
//            viewPager.setVisibility(View.GONE);
//        }

    }

    @Override
    public void onStart() {
        super.onStart();

        //isGroupUpdated is added because we want to call checkForUpdate();
        //only when groups is Updated otherwise no need to call this method

        String isGroupUpdated = PreferenceManager.getPreference(context, "isGroupEdited");

        if (isGroupUpdated != null && isGroupUpdated.equalsIgnoreCase("Yes")) {
            if (InternetConnection.checkConnection(context))
                checkForUpdate();
            Log.d("TouchBase", "Call to checkForUpdate() in onStart");
        }

    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.home);

        title = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));

        title.setText("Roster On Wheels");

        initDrawer();

        img_def=(TextView) findViewById(R.id.img_def);
        rv_grpList=(RecyclerView)findViewById(R.id.rv_grpList);
        rv_grpList.setNestedScrollingEnabled(false);
        rv_grpList.setHasFixedSize(true);
        viewPager = (ViewPager) findViewById(R.id.pager);
        circleIndicator = (CircleIndicator) findViewById(R.id.indicator);
        masterUid = Long.parseLong(PreferenceManager.getPreference(context, PreferenceManager.MASTER_USER_ID));

        groupModel = new GroupMasterModel(context);
        moduleDataModel = new ModuleDataModel(context);
        blogsModel = new RotaryBlogsModel(context);
        feedModel = new RSSFeedsModel(context);

        blogList=new ArrayList<>();
        feedList=new ArrayList<>();

        for(int i = 0; i < Images.length; i++) {
            ImagesArray.add(Images[i]);
        }

        img_findClub = (ImageView)findViewById(R.id.img_findClub);
        img_findRotarian = (ImageView) findViewById(R.id.img_findRotarian);
        ll_findClub = (LinearLayout) findViewById(R.id.ll_findClub);
        ll_findRotarian = (LinearLayout) findViewById(R.id.ll_findRotarian);
        ll_showcase = (LinearLayout) findViewById(R.id.ll_showcase);
        ll_library = (LinearLayout) findViewById(R.id.ll_library);
        ll_RotaryNews = (LinearLayout) findViewById(R.id.ll_RotaryNews);
        ll_RotaryBlog = (LinearLayout) findViewById(R.id.ll_RotaryBlog);
        ll_Rotaryorg = (LinearLayout) findViewById(R.id.ll_Rotaryorg);
        ll_GlobalRewards = (LinearLayout) findViewById(R.id.ll_GlobalRewards);

        rv_grpList.setFocusable(false);

        ll_findClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this,FindAClubActivity.class);
                startActivity(i);
            }
        });

        ll_findRotarian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this,FindRotatrianActivity.class);
                startActivity(i);
            }
        });

        ll_library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this,RotaryLibraryActivity.class);
                startActivity(i);
            }
        });

        ll_RotaryBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this,RotaryBlog.class);
                startActivity(i);
            }
        });

        ll_RotaryNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this,RotaryNews.class);
                startActivity(i);
            }
        });


        ll_Rotaryorg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this,OpenLinkActivity.class);
                i.putExtra("modulename","Rotary.org");
                i.putExtra("link","https://my.rotary.org/en");
                startActivity(i);
            }
        });

        ll_showcase.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                ShowPopUp();
                Intent i = new Intent(DashboardActivity.this,ShowCaseCategoryActivity.class);
                startActivity(i);
            }
        });

        ll_GlobalRewards.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this,OpenLinkActivity.class);
                i.putExtra("modulename","Global Rewards");
                i.putExtra("link","https://my.rotary.org/en/member-center/rotary-global-rewards/offers#/offers");
                startActivity(i);
            }
        });

    }

    private void initDrawer() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        prepareListData();

        for(int i=0;i<drawer_Images.length;i++) {
            drawer_Images_array.add(drawer_Images[i]);
        }

        drawerList.setAdapter(new NavAdapter(this, listDataHeader, drawer_Images_array));

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we don't want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we don't want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };


        //Setting the actionbarToggle to drawer layout

        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch(position){
                    case 1:
                        Intent i = new Intent(DashboardActivity.this,FAQActivity.class);
                        i.putExtra("moduleName","About Developer");
                        i.putExtra("moduleID","101");
                        i.putExtra("grpID","11111");
                        startActivity(i);
                        drawer.closeDrawer(Gravity.LEFT);
                        break;
                    case 2:
                        Intent i1 = new Intent(DashboardActivity.this,SettingActivity.class);
                        i1.putExtra("grpList",grplist);
                        startActivity(i1);
                        drawer.closeDrawer(Gravity.LEFT);
                        break;
                    case 3:
                        Intent i2 = new Intent(DashboardActivity.this,FAQActivity.class);
                        i2.putExtra("moduleName","FAQ's");
                        i2.putExtra("moduleID","102");
                        i2.putExtra("grpID","11111");
                        startActivity(i2);
                        drawer.closeDrawer(Gravity.LEFT);
                        break;
                    case 4:
                        Intent i3 = new Intent(DashboardActivity.this,ROWHelpline.class);
                        startActivity(i3);
                        drawer.closeDrawer(Gravity.LEFT);
                        break;
                }
            }
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();


        Resources res = getResources();
        listDataHeader.add("Roster on Wheels");
        listDataHeader.add(res.getString(R.string.lbl_aboutdeveloper));
        listDataHeader.add(res.getString(R.string.lbl_privacySetting));
        listDataHeader.add(res.getString(R.string.lbl_faq));
        listDataHeader.add(res.getString(R.string.lbl_rowHelpLine));
        listDataHeader.add("");


    }

    private void eventServices() {
        try {
            JSONObject requestData = new JSONObject();
            requestData.put("MasterId", PreferenceManager.getPreference(DashboardActivity.this, PreferenceManager.MASTER_USER_ID));
//            requestData.put("MasterId", "157542");
            Log.d("Response", "PARAMETERS " + Constant.GetNewDashboard + " :- " + requestData.toString());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.GetNewDashboard, requestData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    JSONObject result;
                    globalResponse=response;
                    getAnnouncements(response);
                    //loadRssBlogs();
                    Utils.log(response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.log("VollyError:- " + error.toString());
                    //Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_LONG).show();
                }
            });

            request.setRetryPolicy(
                    new DefaultRetryPolicy(120000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(DashboardActivity.this, request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getAnnouncements(JSONObject response) {

        eventList = new ArrayList<DashboardData>();
        try {
            JSONObject resultObject = response.getJSONObject("TBDashboardResult");
            String status = resultObject.getString("status");

            if (status.equalsIgnoreCase("0")) {
                JSONObject result = resultObject.getJSONObject("Result");
                JSONArray birthdayArray = result.getJSONArray("Birthday");
                if (birthdayArray.length() > 0) {
                    for (int i = 0; i < birthdayArray.length(); i++) {
                        JSONObject birthdayObject = birthdayArray.getJSONObject(i);
                        String title=birthdayObject.getString("Title");
                        if(title!=null && !title.isEmpty()){
                            DashboardData eventData = new DashboardData();
                            eventData.setTitle(birthdayObject.getString("Title"));
                            eventData.setDescription(birthdayObject.getString("Description"));
                            eventData.setTodaysCount(birthdayObject.getString("TodaysCount"));
                            eventData.setClubName(birthdayObject.getString("ClubName"));
                            eventData.setClubCategory(birthdayObject.getString("ClubCategory"));
                            eventData.setType(birthdayObject.getString("Type"));
                            //long masterID= Long.parseLong(PreferenceManager.getPreference(DashboardActivity.this, PreferenceManager.MASTER_USER_ID));
                            // ArrayList<GroupData> list=groupModel.getGroups(masterID,birthdayObject.getString("ClubCategory"));
                            eventData.setGrpId(birthdayObject.getString("ClubId"));
                            eventData.setGrpProfileID(birthdayObject.getString("ProfileId"));
                            eventData.setIsAdmin(birthdayObject.getString("IsAdmin"));
                            eventList.add(eventData);
                        }

                    }
                }

                JSONArray anniversaryArray = result.getJSONArray("Anniversary");
                if (anniversaryArray.length() > 0) {
                    for (int i = 0; i < anniversaryArray.length(); i++) {
                        JSONObject anniversaryObject = anniversaryArray.getJSONObject(i);
                        String title=anniversaryObject.getString("Title");
                        if(title!=null && !title.isEmpty()){
                            DashboardData eventData = new DashboardData();
                            eventData.setTitle(anniversaryObject.getString("Title"));
                            eventData.setDescription(anniversaryObject.getString("Description"));
                            eventData.setTodaysCount(anniversaryObject.getString("TodaysCount"));
                            eventData.setClubName(anniversaryObject.getString("ClubName"));
                            eventData.setClubCategory(anniversaryObject.getString("ClubCategory"));
                            eventData.setType(anniversaryObject.getString("Type"));
                            eventData.setGrpId(anniversaryObject.getString("ClubId"));
                            eventData.setGrpProfileID(anniversaryObject.getString("ProfileId"));
                            eventData.setIsAdmin(anniversaryObject.getString("IsAdmin"));
                            eventList.add(eventData);
                        }

                    }
                }

                JSONArray eventArray = result.getJSONArray("Event");
                if (eventArray.length() > 0) {
                    for (int i = 0; i < eventArray.length(); i++) {
                        JSONObject eventObject = eventArray.getJSONObject(i);
                        String title=eventObject.getString("Title");
                        if(title!=null && !title.isEmpty()){
                            DashboardData eventData = new DashboardData();
                            eventData.setTitle(eventObject.getString("Title"));
                            eventData.setDescription(eventObject.getString("Description"));
                            eventData.setTodaysCount(eventObject.getString("TodaysCount"));
                            eventData.setClubName(eventObject.getString("ClubName"));
                            eventData.setClubCategory(eventObject.getString("ClubCategory"));
                            eventData.setType(eventObject.getString("Type"));
                            eventData.setGrpId(eventObject.getString("ClubId"));
                            eventData.setGrpProfileID(eventObject.getString("ProfileId"));
                            eventData.setIsAdmin(eventObject.getString("IsAdmin"));
                            eventList.add(eventData);
                        }

                    }
                }

//                if(newsList.size()>0){
//                    NewsFeed newsFeed=newsList.get(0);
//                    DashboardData eventData = new DashboardData();
//                    eventData.setTitle(newsFeed.getTitle());
//                    eventData.setDescription(newsFeed.getDescription());
//                    eventData.setLink(newsFeed.getLink());
//                    eventData.setPublishDate(newsFeed.getPubDate());
//                    eventData.setClubName("Rotary News");
//                    eventData.setClubCategory("");
//                    eventData.setType("news");
//                    eventList.add(eventData);
//
//                    Utils.log(newsFeed.getDescription());
//                }
//
//                if(blogList.size()>0){
//                    BlogFeed blogFeed=blogList.get(0);
//                    DashboardData eventData = new DashboardData();
//                    eventData.setTitle(blogFeed.getTitle());
//                    eventData.setDescription(blogFeed.getDescription());
//                    eventData.setLink(blogFeed.getLink());
//                    eventData.setPublishDate(blogFeed.getPubDate());
//                    eventData.setClubName("Rotary Blog");
//                    eventData.setClubCategory("");
//                    eventData.setType("blog");
//                    eventList.add(eventData);
//                }

                img_def.setVisibility(View.GONE);
                viewPager.setVisibility(View.VISIBLE);
                if(eventList.size()>0){
                    PreferenceManager.saveListPreference(DashboardActivity.this,PreferenceManager.eventList,eventList);
                }
//                eventsAdapter = new CurrentEventsAdapter(DashboardActivity.this, eventList, ImagesArray);
//                viewPager.setAdapter(eventsAdapter);
//                circleIndicator.setViewPager(viewPager);
//                viewPagerTimer();
                showEvents();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void viewPagerTimer() {

        if(timer!=null){
            timer.cancel();

        }
        timer=new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                viewPager.post(new Runnable() {
                    @Override
                    public void run() {
//
//                        Log.d("viewPager",""+ci);
//                        viewPager.setCurrentItem(ci%9);
//                        ci++;
                        int i=viewPager.getCurrentItem();

                        if(i<events.size()-1){
                            viewPager.setCurrentItem(i+1);
                        }else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        },1000,5000);



    }

    public void loadFromDB() {

        Log.d("Touchbase", "Trying to load from local db");

        grplist.addAll(groupModel.getGroups(masterUid));
        //Log.e("touchbase list", "**************"+grplist.toString());
        //boolean isDataAvailable = groupModel.isDataAvailable();
        boolean isDataAvailable = groupModel.isDataAvailableBasedOnMasterUid(masterUid);

        Log.e("DataAvailable", "Data available : " + isDataAvailable);

        if (!isDataAvailable) {

            Log.d("Touchbase---@@@@@@@@", "Loading from server");

            if (InternetConnection.checkConnection(context.getApplicationContext()))
                webservices();
            else
                //showEvents();
                Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
        } else {
            Log.d("Touchbase---@@@@@@@@", "Loaded from local db");
//            rv_adapter = new FragmentALLAdapter_new(context, grplist, "1");
//            mRecyclerView.setAdapter(rv_adapter);
//            rv_adapter.setOnGroupSelectedListener(onGroupSelectedListener);
            setDashboardAdapter(grplist);

            if (InternetConnection.checkConnection(context)) {
                checkForUpdate();
                Log.d("---------------", "Check for update gets called------");
            }

            //new LoadRssTask().execute();
            //new LoadBlogTask().execute();
            // If data is loaded from local database then check for update

//            if (InternetConnection.checkConnection(getActivity().getApplicationContext())) {
//                checkForUpdate();
//                Log.d("---------------", "Check for update gets called------");
//            } else {
//                //Toast.makeText(getContext(), "No internet connection to get Updated Records", Toast.LENGTH_LONG).show();
//            }

        }
    }

    private void webservices() {

        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        arrayList.add(new BasicNameValuePair("masterUID", PreferenceManager.getPreference(context, PreferenceManager.MASTER_USER_ID)));
        arrayList.add(new BasicNameValuePair("imeiNo", PreferenceManager.getPreference(context, PreferenceManager.UDID)));
        String updatedOn = PreferenceManager.getPreference(context, TBPrefixes.ENTITY_PREFIX + masterUid, "1970/01/01 00:00:00");

        String countryCode = PreferenceManager.getPreference(context, PreferenceManager.COUNTRY_CODE, "0");
        String mobileNo = PreferenceManager.getPreference(context, PreferenceManager.MOBILE_NUMBER, "");
        String loginType = PreferenceManager.getPreference(context, PreferenceManager.LOGIN_TYPE, "0");

        arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));
        arrayList.add(new BasicNameValuePair("loginType", loginType));
        arrayList.add(new BasicNameValuePair("mobileNo", mobileNo));
        arrayList.add(new BasicNameValuePair("countryCode", countryCode));

        Log.d("Response", "satish PARAMETERS " + Constant.GetAllGroupsList + " :- " + arrayList.toString());

        new WebConnectionAsync(Constant.GetAllGroupsList, arrayList, context).execute();
    }

    public class WebConnectionAsync extends AsyncTask<String, Object, Object> {

        String val = null;
        ProgressDialog dialog;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.TBProgressBar);

        public WebConnectionAsync(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //	dialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();
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
            progressDialog.dismiss();

            if (result != "") {
                getresult(result.toString());
            } else {
                Log.d("Response", "Null Resposnse");
            }
        }
    }

    private void getresult(String val) {

        try {

            //grplist.clear();
            JSONObject jsonObj = new JSONObject(val);

            JSONObject ActivityResult = jsonObj.getJSONObject("TBGroupResult");

            final String status = ActivityResult.getString("status");

            if (status.equals("0")) {

                //version
                /*if(!Constant.versionNo.equals(ActivityResult.getString("version"))){
                    popup_of_update_app();
                }*/
                // updatedOn at the time of updating these records.
                // This server data will be stored in shared preferences.
                // This ServerCurrentDate must be sent in api with parameter "updatedOn"
                // Each time when API is to be called to get the list of new or updated or deleted groups this
                updatedOn = ActivityResult.getString("curDate");
                // Log.d("TouchBase", "VErsion No" + Float.parseFloat(Constant.versionNo));
                // Log.d("TouchBase", "InterNal Version No" + Float.parseFloat(ActivityResult.getString("version")));
                if (Float.parseFloat(Constant.versionNo) < Float.parseFloat(ActivityResult.getString("version"))) {
                    // if(Float.parseFloat(Constant.versionNo) < Float.parseFloat("1.3")){
                    popup_of_update_app();
                }

                //grplist.clear();

                grplist = new ArrayList<>();

                JSONArray grpsarray = ActivityResult.getJSONArray("AllGroupListResults");

                for (int i = 0; i < grpsarray.length(); i++) {

                    JSONObject object = grpsarray.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("GroupResult");

                    //listGroup.add(objects.getString("grpName").toString());
                    // gv.setAdapter(new FragmentALLAdapter(getActivity(), listGroup));

                    GroupData gd = new GroupData();

                    gd.setGrpId(objects.getString("grpId").toString());
                    gd.setGrpName(objects.getString("grpName").toString());
                    gd.setGrpProfileId(objects.getString("grpProfileId").toString());
                    String myCategory = objects.getString("myCategory").toString();

                    try {
                        //String expiryDate = objects.getString("expiryDate");
                        //gd.setExpiryDate(expiryDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Utils.log("MyCategory : " + myCategory);
                    gd.setMyCategory(myCategory);
                    gd.setIsGrpAdmin(objects.getString("isGrpAdmin").toString());

                    //   gd.setGrpImg(objects.getString("grpImg").toString());
                    if (objects.has("grpImg")) {
                        gd.setGrpImg(objects.getString("grpImg").toString());
                    } else {
                        gd.setGrpImg("");
                    }

                    grplist.add(gd);

                    //--------------------Group Module list........by Lekha-----------

                    JSONArray jsonmodulelist = objects.getJSONArray("ModuleList");

                    for (int j = 0; j < jsonmodulelist.length(); j++) {
                        JSONObject module_object = jsonmodulelist.getJSONObject(j);
                        String groupModuleId = module_object.getString("groupModuleId");
                        String groupId = module_object.getString("groupId");
                        String moduleId = module_object.getString("moduleId");
                        String moduleName = module_object.getString("moduleName");
                        String moduleStaticRef = module_object.getString("moduleStaticRef");
                        String image = module_object.getString("image");
                        int moduleOrderNo = Integer.parseInt(module_object.getString("moduleOrderNo"));
                        ModuleData moduleData = new ModuleData(groupModuleId, groupId, moduleId, moduleName, moduleStaticRef, image, moduleOrderNo);
                        moduleList.add(moduleData);
                        //Log.e("ModuleData", "################ " + moduleData);
                    }

                    //-----------------------------------------------------------------

                }
                // displaydata();
                Log.d("ARRAYLIST---@@@@@@@@", "ALL :- " + grplist.toString());

                groupDataHandler.sendEmptyMessageDelayed(0, 1000);
//                grplist.add(new LoadingMessageData());
//                rv_adapter = new FragmentALLAdapter_new(this.getContext(), grplist, "1");
//                mRecyclerView.setAdapter(rv_adapter);
//                rv_adapter.setOnGroupSelectedListener(onGroupSelectedListener);
//                getNotificationCountWebservices();
//                rv_adapter.notifyDataSetChanged();
                setDashboardAdapter(grplist);

            } else if (status.equals("2")) {

                // ActivityResult.getString("message");
                grplist.clear();
//                rv_adapter = new FragmentALLAdapter_new(this.getContext(), grplist, "1");
//                mRecyclerView.setAdapter(rv_adapter);
//                rv_adapter.setOnGroupSelectedListener(onGroupSelectedListener);

                setDashboardAdapter(grplist);
                final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_session_expired);

                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);

                tv_line1.setText(ActivityResult.getString("message"));

                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        savePreference(context, GROUP_ID, null);
                        savePreference(context, GRP_PROFILE_ID, null);
                        savePreference(context, IS_GRP_ADMIN, null);
                        savePreference(context, GROUP_NAME, null);
                        savePreference(context, MASTER_USER_ID, null);
                        savePreference(context, IS_AG , null);

                        Intent i = new Intent(context, Splash.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                });

                dialog.show();
                dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            savePreference(context, GROUP_ID, null);
                            savePreference(context, GRP_PROFILE_ID, null);
                            savePreference(context, IS_GRP_ADMIN, null);
                            savePreference(context, GROUP_NAME, null);
                            savePreference(context, MASTER_USER_ID, null);
                            savePreference(context, IS_AG , null);

                            Intent i = new Intent(context, Splash.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(i);
                            dialog.dismiss();
                        }
                        return true;
                    }

                   /* @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        // TODO Auto-generated method stub
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            finish();
                            dialog.dismiss();
                        }
                        return true;
                    }*/
                });

            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
            e.printStackTrace();
        }

    }

    public void popup_of_update_app() {

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_session_expired);
        TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
        TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
        TextView tv_header = (TextView) dialog.findViewById(R.id.tv_header);
        tv_yes.setText("Update");
        tv_header.setVisibility(View.VISIBLE);
        tv_header.setText("New Version Available");
        tv_line1.setText("There is a newer version available for download! Please update the app by visiting the Play Store.");

        tv_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.SampleApp.row"));
                startActivity(i);
            }
        });

        dialog.show();

        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    Intent i = new Intent(context, Splash.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    dialog.dismiss();
                }

                return true;
            }

        });
    }

    public void setDashboardAdapter(final ArrayList<GroupData> groupDataArrayList) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 12);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {

                if (groupDataArrayList.size() % 2 == 0) {
                    return 6;
                } else {

                    if (position == groupDataArrayList.size() - 1) {
                        return 12;
                    }
                    return 6;
                }
            }
        });

        rv_grpList.setLayoutManager(gridLayoutManager);
        DashboardRVAdapter dashAdapter = new DashboardRVAdapter(context, groupDataArrayList);
        dashAdapter.setOnGroupSelectedListener(onGroupSelectedListener);
        rv_grpList.setAdapter(dashAdapter);
    }

    Handler groupDataHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Log.e("DBError", "Handler is called");
            boolean saved = groupModel.insert(masterUid, grplist);
            if (!saved) {
                Log.d("Touchbase---@@@@@@@@", "Failed to save offlline. Retrying in 2 seconds");
                sendEmptyMessageDelayed(0, 2000);
            } else {
                savePreference(context, TBPrefixes.ENTITY_PREFIX + masterUid, updatedOn);
                Log.d("Touchbase---@@@@@@@@", updatedOn + "groupHandler executed successfully");
                context.sendBroadcast(new Intent(Constant.GROUP_DATA_LOADED));
               // Log.d("Touchbase---@@@@@@@@", moduleDataHandler + "execution starts");
                moduleDataHandler.sendEmptyMessageDelayed(0, 1000);
                //syncModel.update(masterUid, Utils.)
            }
        }
    };


    Handler moduleDataHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            boolean saved = moduleDataModel.insert(masterUid, moduleList);
            if (!saved) {
                Log.d("Touchbase---@@@@@@@@", "Failed to save offlline. Retrying in 2 seconds");
                sendEmptyMessageDelayed(0, 2000);
            } else {
                savePreference(context, TBPrefixes.MODULES_PREFIX + masterUid, updatedOn);
                openGroupForLink();
            }
        }
    };

    public void openGroupForLink() {
        Log.e("♦♦♦♦♦", "Inside openGroupLink");
        if (getIntent().getStringExtra("openGroup") != null) {
            Log.e("♦♦♦♦♦", "Needs to open openGroupLink");
            Intent intent = new Intent(context, GroupDashboard.class);
            startActivity(intent);
        } else {
            Log.e("♦♦♦♦♦", "No need to open openGroupLink");
        }
    }

    public void checkForUpdate() {

        if (InternetConnection.checkConnection(context)) {

            Log.e("Touchbase", "------ Checking for update");

            String url = Constant.GetGetAllGroupListSync;
            ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
            arrayList.add(new BasicNameValuePair("masterUID", PreferenceManager.getPreference(context, PreferenceManager.MASTER_USER_ID)));
            arrayList.add(new BasicNameValuePair("imeiNo", PreferenceManager.getPreference(context, PreferenceManager.UDID)));

            updatedOn = PreferenceManager.getPreference(context, TBPrefixes.ENTITY_PREFIX + masterUid);

            Log.e("UpdatedOn", "Last updated date is : " + updatedOn);

            Log.e("MasterUID", PreferenceManager.getPreference(context, PreferenceManager.MASTER_USER_ID));

            arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));//updatedOn 1970-1-1 0:0:0

            String countryCode = PreferenceManager.getPreference(context, PreferenceManager.COUNTRY_CODE, "0");
            String mobileNo = PreferenceManager.getPreference(context, PreferenceManager.MOBILE_NUMBER, "");
            String loginType = PreferenceManager.getPreference(context, PreferenceManager.LOGIN_TYPE, "0");

            arrayList.add(new BasicNameValuePair("loginType", loginType));
            arrayList.add(new BasicNameValuePair("mobileNo", mobileNo));
            arrayList.add(new BasicNameValuePair("countryCode", countryCode));

            Log.e("MasterUID", PreferenceManager.getPreference(context, PreferenceManager.MASTER_USER_ID));
            //arrayList.add(new BasicNameValuePair("updatedOn", "2016/1/18 17:8:34"));

            Log.e("request", arrayList.toString());
            UpdateGroupDataAsyncTask task = new UpdateGroupDataAsyncTask(url, arrayList, context);
            task.execute();
            Log.d("Response", "PARAMETERS " + Constant.GetGetAllGroupListSync + " :- " + arrayList.toString());
            //new WebConnectionAsyncDirectory(Constant.GetDirectoryListSync, arrayList, Directory.this).execute();

        } else {
            Log.e("SyncFailed", "No internet connection to sync data");
        }
    }

    public class UpdateGroupDataAsyncTask extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.TBProgressBar);

        String url = null;
        List<NameValuePair> argList = null;

        public UpdateGroupDataAsyncTask(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //	dialog.show();

            /*progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();*/
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
            //progressDialog.dismiss();
            //	Log.d("response","Do post"+ result.toString());
            if (result != "") {
                Log.d("Response", "calling getDirectorydetails");
                getUpdatedGroupdata(result.toString());
            } else {
                Log.d("Response", "Null Resposnse");
            }
        }
    }

    public void getUpdatedGroupdata(String result) {

        try {

            JSONObject jsonObj = new JSONObject(result);
            final String status = jsonObj.getString("status");

            if (status.equals("0")) {

                updatedOn = jsonObj.getString("updatedOn");
                final ArrayList<GroupData> newGroupList = new ArrayList<GroupData>();

                JSONObject jsonResultGroupList = jsonObj.getJSONObject("Result").getJSONObject("GroupList");

                JSONArray jsonNewGroupList = jsonResultGroupList.getJSONArray("NewGroupList");

                int newGroupListCount = jsonNewGroupList.length();

                for (int i = 0; i < newGroupListCount; i++) {

                    GroupData data = new GroupData();

                    JSONObject result_object = jsonNewGroupList.getJSONObject(i);

                    data.setGrpId(result_object.getString("grpId").toString());
                    data.setGrpName(result_object.getString("grpName").toString());
                    data.setGrpProfileId(result_object.getString("grpProfileId").toString());
                    String myCategory = result_object.getString("myCategory").toString();
                    Utils.log("MyCategory : " + myCategory);
                    data.setMyCategory(result_object.getString("myCategory").toString());
                    data.setIsGrpAdmin(result_object.getString("isGrpAdmin").toString());
                    try {
                        String expiryDate = result_object.getString("expiryDate");
                        data.setExpiryDate(expiryDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (result_object.has("grpImg")) {
                        data.setGrpImg(result_object.getString("grpImg").toString());
                    } else {
                        data.setGrpImg("");
                    }

                    newGroupList.add(data);

                }

                final ArrayList<GroupData> UpdatedGroupList = new ArrayList<GroupData>();
                JSONArray jsonUpdatedGroupList = jsonResultGroupList.getJSONArray("UpdatedGroupList");

                int updateGroupListCount = jsonUpdatedGroupList.length();

                for (int i = 0; i < updateGroupListCount; i++) {

                    GroupData data = new GroupData();

                    JSONObject result_object = jsonUpdatedGroupList.getJSONObject(i);

                    data.setGrpId(result_object.getString("grpId").toString());
                    data.setGrpName(result_object.getString("grpName").toString());
                    data.setGrpProfileId(result_object.getString("grpProfileId").toString());
                    String myCategory = result_object.getString("myCategory").toString();
                    Utils.log("MyCategory : " + myCategory);
                    data.setMyCategory(result_object.getString("myCategory").toString());
                    data.setIsGrpAdmin(result_object.getString("isGrpAdmin").toString());
                    String expiryDate = result_object.getString("expiryDate");
                    data.setExpiryDate(expiryDate);
                    if (result_object.has("grpImg")) {
                        data.setGrpImg(result_object.getString("grpImg").toString());
                    } else {
                        data.setGrpImg("");
                    }

                    UpdatedGroupList.add(data);
                    //directoryData.add(data);
                }

                final ArrayList<GroupData> DeletedGroupList = new ArrayList<GroupData>();
                JSONArray jsonDeletedGroupList = jsonResultGroupList.getJSONArray("DeletedGroupList");

                int deleteGroupListCount = jsonDeletedGroupList.length();

                for (int i = 0; i < deleteGroupListCount; i++) {

                    GroupData data = new GroupData();

                    JSONObject result_object = jsonDeletedGroupList.getJSONObject(i);

                    data.setGrpId(result_object.getString("grpId").toString());
                    data.setGrpName(result_object.getString("grpName").toString());
                    data.setGrpProfileId(result_object.getString("grpProfileId").toString());
                    String myCategory = result_object.getString("myCategory").toString();
                    Utils.log("MyCategory : " + myCategory);

                    data.setMyCategory(result_object.getString("myCategory").toString());
                    data.setIsGrpAdmin(result_object.getString("isGrpAdmin").toString());
                    String expiryDate = result_object.getString("expiryDate");
                    data.setExpiryDate(expiryDate);
                    if (result_object.has("grpImg")) {
                        data.setGrpImg(result_object.getString("grpImg").toString());
                    } else {
                        data.setGrpImg("");
                    }

                    DeletedGroupList.add(data);
                    //directoryData.add(data);
                }

                final ArrayList<ModuleData> newModuleList = new ArrayList<ModuleData>();
                JSONObject jsonResultModuleList = jsonObj.getJSONObject("Result").getJSONObject("ModuleList");
                JSONArray jsonNewModuleList = jsonResultModuleList.getJSONArray("NewModuleList");
                int newModuleListcount = jsonNewModuleList.length();

                for (int i = 0; i < newModuleListcount; i++) {
                    ModuleData moduledata = new ModuleData();
                    JSONObject result_object = jsonNewModuleList.getJSONObject(i);
                    moduledata.setGroupModuleId(result_object.getString("groupModuleId"));
                    moduledata.setGroupId(result_object.getString("groupId"));
                    moduledata.setModuleId(result_object.getString("moduleId"));
                    moduledata.setModuleName(result_object.getString("moduleName"));
                    moduledata.setModuleStaticRef(result_object.getString("moduleStaticRef"));
                    moduledata.setImage(result_object.getString("image"));
                    moduledata.setModuleOrderNo(Integer.parseInt(result_object.getString("moduleOrderNo")));
                    newModuleList.add(moduledata);
                }

                final ArrayList<ModuleData> updatedModuleList = new ArrayList<ModuleData>();

                JSONArray jsonUpdatedModuleList = jsonResultModuleList.getJSONArray("UpdatedModuleList");
                int updatedModuleListcount = jsonUpdatedModuleList.length();

                for (int i = 0; i < updatedModuleListcount; i++) {
                    ModuleData moduledata = new ModuleData();

                    JSONObject result_object = jsonUpdatedModuleList.getJSONObject(i);
                    moduledata.setGroupModuleId(result_object.getString("groupModuleId"));
                    moduledata.setGroupId(result_object.getString("groupId"));
                    moduledata.setModuleId(result_object.getString("moduleId"));
                    moduledata.setModuleName(result_object.getString("moduleName"));
                    moduledata.setModuleStaticRef(result_object.getString("moduleStaticRef"));
                    moduledata.setImage(result_object.getString("image"));
                    moduledata.setModuleOrderNo(Integer.parseInt(result_object.getString("moduleOrderNo")));
                    updatedModuleList.add(moduledata);
                }


                final ArrayList<ModuleData> deletedModuleList = new ArrayList<ModuleData>();

                JSONArray jsonDeletedModuleList = jsonResultModuleList.getJSONArray("DeletedModuleList");
                int DeletedModuleListcount = jsonDeletedModuleList.length();

                for (int i = 0; i < DeletedModuleListcount; i++) {
                    ModuleData moduledata = new ModuleData();

                    JSONObject result_object = jsonDeletedModuleList.getJSONObject(i);
                    moduledata.setGroupModuleId(result_object.getString("groupModuleId"));
                    moduledata.setGroupId(result_object.getString("groupId"));
                    moduledata.setModuleId(result_object.getString("moduleId"));
                    moduledata.setModuleName(result_object.getString("moduleName"));
                    moduledata.setModuleStaticRef(result_object.getString("moduleStaticRef"));
                    moduledata.setImage(result_object.getString("image"));
                    moduledata.setModuleOrderNo(Integer.parseInt(result_object.getString("moduleOrderNo")));
                    deletedModuleList.add(moduledata);
                }

                if (Float.parseFloat(Constant.versionNo) < Float.parseFloat(jsonObj.getString("version"))) {
                    // if(Float.parseFloat(Constant.versionNo) < Float.parseFloat("1.3")){
                    popup_of_update_app();

                }

                Handler UpdateGroupdatahandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        boolean saved = groupModel.syncData(masterUid, newGroupList, UpdatedGroupList, DeletedGroupList);
                        if (!saved) {
                            Log.e("SyncFailed------->", "Failed to update group data in local db. Retrying in 2 seconds");
                            sendEmptyMessageDelayed(0, 2000);
                        } else {

                            Handler UpdateModuledatahandler = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    super.handleMessage(msg);
                                    boolean saved = moduleDataModel.syncData(masterUid, newModuleList, updatedModuleList, deletedModuleList);
                                    if (!saved) {
                                        Log.e("SyncFailed------->", "Failed to update module data in local db. Retrying in 2 seconds");
                                        sendEmptyMessageDelayed(0, 2000);
                                    } else {
                                        PreferenceManager.savePreference(context, TBPrefixes.ENTITY_PREFIX + masterUid, updatedOn);
                                        PreferenceManager.savePreference(context, TBPrefixes.MODULES_PREFIX + masterUid, updatedOn);

                                        grplist = new ArrayList<>();
                                        grplist.addAll(groupModel.getGroups(masterUid));

                                        if(context!=null){
                                            setDashboardAdapter(grplist);
                                            openGroupForLink();
                                        }

                                    }
                                }
                            };
                            UpdateModuledatahandler.sendEmptyMessageDelayed(0, 1000);

                        }
                    }
                };

                int overAllCount = newGroupListCount + updateGroupListCount + deleteGroupListCount + newModuleListcount + DeletedModuleListcount;

                System.out.println("Number of records for update are : " + overAllCount);

                if (newGroupListCount + updateGroupListCount + deleteGroupListCount + newModuleListcount + updatedModuleListcount + DeletedModuleListcount != 0) {

                    UpdateGroupdatahandler.sendEmptyMessageDelayed(0, 1000);

                } else {
                    Log.e("NoUpdate", "No updates found");
                }

            } else if (status.equals("2")) {
                //addhere

                grplist.clear();
               setDashboardAdapter(grplist);
                final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_session_expired);

                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);

                tv_line1.setText("Your session has been expired.");

                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        savePreference(context, GROUP_ID, null);
                        savePreference(context, GRP_PROFILE_ID, null);
                        savePreference(context, IS_GRP_ADMIN, null);
                        savePreference(context, GROUP_NAME, null);
                        savePreference(context, MASTER_USER_ID, null);
                        savePreference(context, IS_AG , null);

                        Intent i = new Intent(context, Splash.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                });

                dialog.show();
                dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            savePreference(context, GROUP_ID, null);
                            savePreference(context, GRP_PROFILE_ID, null);
                            savePreference(context, IS_GRP_ADMIN, null);
                            savePreference(context, GROUP_NAME, null);
                            savePreference(context, MASTER_USER_ID, null);
                            savePreference(context, IS_AG , null);

                            Intent i = new Intent(context, Splash.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(i);
                            dialog.dismiss();
                        }
                        return true;
                    }

                   /* @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        // TODO Auto-generated method stub
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            finish();
                            dialog.dismiss();
                        }
                        return true;
                    }*/
                });
            }
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
            e.printStackTrace();
        }

    }

    DashboardRVAdapter.OnGroupSelectedListener onGroupSelectedListener = new DashboardRVAdapter.OnGroupSelectedListener() {

        @Override
        public void onGroupSelected(int position) {

            try {

                String grpId=((GroupData) grplist.get(position)).getGrpId();

                if(((GroupData) grplist.get(position)).getMyCategory().equalsIgnoreCase(String.valueOf(Constant.GROUP_CATEGORY_CLUB))){

                    Intent service=new Intent(context, DirectorySyncService.class);

                    if(PreferenceManager.getPreference(context,PreferenceManager.REQUESTED+grpId,"").equalsIgnoreCase("Y") && !Utils.isServiceRunning(DirectorySyncService.class,context)){
                        savePreference(context,REQUESTED+grpId,"N");

                    }else if(PreferenceManager.getPreference(context,PreferenceManager.REQUESTED+grpId,"").equalsIgnoreCase("N") && Utils.isServiceRunning(DirectorySyncService.class,context)){
                        stopService(service);
                    }

                    startService(service);
                }

                Intent i = new Intent(context, GroupDashboard.class);
                i.putExtra("position", position);
                i.putExtra("groupId", ((GroupData) grplist.get(position)).getGrpId().toString());
                // i.putExtra("groupname", grplist.get(position).getGrpId().toString());
                savePreference(context, GROUP_ID, ((GroupData) grplist.get(position)).getGrpId().toString());
                savePreference(context, GRP_PROFILE_ID, ((GroupData) grplist.get(position)).getGrpProfileId().toString());

                try {

                    String loginType = PreferenceManager.getPreference(context, PreferenceManager.LOGIN_TYPE);

                    Utils.log("Login Type : " + loginType);

                    if (loginType.equals("1")) {
                        savePreference(context, IS_GRP_ADMIN, "No");
                    } else {
                        savePreference(context, IS_GRP_ADMIN, ((GroupData) grplist.get(position)).getIsGrpAdmin().toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                savePreference(context, GROUP_NAME, ((GroupData) grplist.get(position)).getGrpName().toString());
                savePreference(context, MY_CATEGORY, ((GroupData) grplist.get(position)).getMyCategory());

                startActivityForResult(i, Constant.REQUEST_DASHBOARD);

            } catch (ClassCastException cce) {
                Utils.log("Error is : " + cce);
                cce.printStackTrace();
            }
        }
    };

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(Gravity.START)){
            drawer.closeDrawer(Gravity.START);
        }else{
            finish();
        }
    }

    public void loadRssBlogs() {
        new AsyncTask<Void, Void, String>() {
            FileInputStream fin;
            ArrayList<BlogFeed> list = new ArrayList<BlogFeed>();
            boolean isFound = false;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                try {
                    isFound = blogsModel.isBlogAvailable();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    if (!isFound) {
                        return "failed";
                    }
                    return "success";
                } catch (Exception e) {
                    Utils.log("RSS Blogs File are not present in local database");
                    return "failed";
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("failed")) {
                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        new LoadBlogTask().execute();
                    }
                } else {
                   blogList.clear();
                   blogList = blogsModel.getBlogsList();
                   //loadRssFeeds();
                }
            }
        }.execute();

    }

    public class LoadBlogTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try {

                URL url = new URL("https://blog.rotary.org/feed/");
                //URL url = new URL("http://rosteronwheels.com/resources/feed.xml");

                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                //HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("GET");
                con.setDoInput(true);
                con.setConnectTimeout(60000);
                InputStream in = con.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line = "";
                StringBuffer buffer = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                }
                in.close();

                return new String(buffer);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("RSS", "doInBackground: " + s);
            saveRssBlogs(s);
        }
    }

    public void saveRssBlogs(final String s) {
        try {
            ArrayList<BlogFeed>  blogList1 = parseBlogs(s);
            boolean saved = blogsModel.syncData(blogList1);
            if (!saved) {


            } else {
                blogList.clear();
                blogList = blogsModel.getBlogsList();
                loadRssFeeds();
            }

        } catch (Exception ioe) {
            Utils.log("Error is : " + ioe);
            ioe.printStackTrace();
        }

    }

    BlogFeed feedB = new BlogFeed();

    public ArrayList<BlogFeed> parseBlogs(String feedData) {
        ArrayList<BlogFeed> feedList = new ArrayList<>();
        try {
            /*feedData = feedData.replaceAll("<media:title>", "<media:title1>");
            feedData = feedData.replaceAll("</media:title>", "</media:title1>");
            */
            InputStream in = new ByteArrayInputStream(feedData.getBytes());
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParserFactory.setNamespaceAware(true);
            XmlPullParser parser = xmlPullParserFactory.newPullParser();
            parser.setInput(in, null);
            String text = "";

            int eventType = parser.getEventType();
            int ctr = 1;
            String prevTag = "";
            endParsing:
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equals("item")) {
                            feedB = new BlogFeed();
                            //Utils.log("Tag name : "+tagName);
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();

                        break;

                    case XmlPullParser.END_TAG:
                        prevTag = tagName;
                        if (tagName.equals("item")) {
                            feedList.add(feedB);
                            ctr++;
                            if (ctr > 10) {
                                break endParsing;
                            }
                        } else if (tagName.equals("title")) {
                            if (prevTag.equals("item")) {  // if prevTag is "item" then only its actual <title> otherwise its <media:title>
                                feedB.setTitle(text);
                            }
                        } else if (tagName.equals("link")) {
                            feedB.setLink(text);
                        } else if (tagName.equals("pubDate")) {
                            feedB.setPubDate(text);
                        } else if (tagName.equals("description")) {
                            Utils.log("Description : " + text);
                            /*if ( text.contains("<img")) {
                                int index = text.indexOf(">");
                                if ( index != -1 ) {
                                    String image = text.substring(text.indexOf("http"), index-2);
                                    Utils.log("Image Path is : "+image);
                                    text = text.substring(index+1);
                                }
                            }*/
                            feedB.setDescription(text);
                        }
                        break;
                }
                eventType = parser.next();
            }

            return feedList;
            /*if (feedList.size() != 0) {
                grplist.add("Rotary Blogs");
                grplist.addAll(feedList);
                rv_adapter.notifyDataSetChanged();
            }*/

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        return feedList;
    }

    public void loadRssFeeds() {
        new AsyncTask<Void, Void, String>() {
            boolean isFound = false;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                try {
                    isFound = feedModel.isFeedAvailable();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    if (!isFound) {
                        return "failed";
                    }
                    return "success";
                } catch (Exception e) {
                    Utils.log("RSS Feeds File are not present in local database");
                    return "failed";
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("failed")) {
                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        new LoadRssTask().execute();
                    }
                } else {

                    feedList.clear();
                    feedList = feedModel.getNewsFeedList();
                    //getAnnouncements(globalResponse,blogList,feedList);
                }
            }
        }.execute();

    }

    public class LoadRssTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("https://my.rotary.org/en/rss.xml");
                //URL url = new URL("http://rosteronwheels.com/resources/rss.xml");
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                //HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("GET");
                con.setDoInput(true);
                con.setConnectTimeout(60000);
                InputStream in = con.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line = "";
                StringBuffer buffer = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                }
                in.close();

                return new String(buffer);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Utils.log("http://rosteronwheels.com/resources/rss.xml");
            Log.e("RSS", "doInBackground: " + s);
            saveRssFeeds(s);

        }
    }

    public void saveRssFeeds(final String s) {
        try {
            /*FileOutputStream fout = this.getContext().openFileOutput(RSS_FEEDS_FILE, MODE_PRIVATE);
            fout.write(s.getBytes());
            fout.close();*/


            ArrayList<NewsFeed> feedList1 = parseFeeds(s);
            boolean saved = feedModel.syncData(feedList1);
            if (!saved) {


            } else {

                feedList.clear();
                feedList = feedModel.getNewsFeedList();
                //getAnnouncements(globalResponse,blogList,feedList);
            }

        } catch (Exception ioe) {
            Utils.log("Error is : " + ioe);
            ioe.printStackTrace();
        }

    }

    NewsFeed feed = new NewsFeed();

    public ArrayList<NewsFeed> parseFeeds(String feedData) {
        ArrayList<NewsFeed> feedList = new ArrayList<>();
        try {
            InputStream in = new ByteArrayInputStream(feedData.getBytes());
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParserFactory.setNamespaceAware(true);
            XmlPullParser parser = xmlPullParserFactory.newPullParser();
            parser.setInput(in, null);
            String text = "";

            int eventType = parser.getEventType();
            int ctr = 1;
            endParsing:
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equals("item")) {
                            feed = new NewsFeed();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagName.equals("item")) {
                            feedList.add(feed);
                            ctr++;
                            if (ctr > 10) {
                                break endParsing;
                            }
                        } else if (tagName.equals("title")) {
                            feed.setTitle(text);
                        } else if (tagName.equals("link")) {
                            feed.setLink(text);
                        } else if (tagName.equals("pubDate")) {
                            feed.setPubDate(text);
                        } else if (tagName.equals("description")) {
                            feed.setDescription(text);
                        }
                        break;
                }

                eventType = parser.next();
            }

            if (feedList.size() != 0) {
                //grplist.add("Rotary News & Updates");
                return feedList;
                /*grplist.addAll(feedList);
                rv_adapter.notifyDataSetChanged();*/
            }


        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        return feedList;
    }

    public void showEvents(){
        events = PreferenceManager.getListPreference(DashboardActivity.this,PreferenceManager.eventList);
        if(events!=null) {
            viewPager.setVisibility(View.VISIBLE);
            eventsAdapter = new CurrentEventsAdapter(DashboardActivity.this, events, ImagesArray);
            viewPager.setAdapter(eventsAdapter);
            circleIndicator.setViewPager(viewPager);
            viewPagerTimer();
        }
    }

    public void ShowPopUp() {

        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.popup_simple_message, null);
        TextView tv_yes = (TextView) alertLayout.findViewById(R.id.tv_yes);
        tv_yes.setText("OK");

        TextView tv_line1 = (TextView) alertLayout.findViewById(R.id.tv_line1);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("");

        //tv_header.setText("GPS is not Enabled!");
        tv_line1.setText("This feature is Coming soon");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();

        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            }

        });
    }

}
