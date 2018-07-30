package com.SampleApp.row;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.FragmentALLAdapter_new;
import com.SampleApp.row.Data.BlogFeed;
import com.SampleApp.row.Data.GroupCountData;
import com.SampleApp.row.Data.GroupData;
import com.SampleApp.row.Data.LoadingMessageData;
import com.SampleApp.row.Data.ModuleData;
import com.SampleApp.row.Data.NewsFeed;
import com.SampleApp.row.Data.NotificationCountData;
import com.SampleApp.row.Inteface.DashboardAPI;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.TBPrefixes;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.sql.GroupMasterModel;
import com.SampleApp.row.sql.ModuleDataModel;
import com.SampleApp.row.sql.RSSFeedsModel;
import com.SampleApp.row.sql.RotaryBlogsModel;

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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit.Callback;
import retrofit.Profiler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

import static android.app.Activity.RESULT_OK;
import static com.SampleApp.row.Utils.PreferenceManager.GROUP_ID;
import static com.SampleApp.row.Utils.PreferenceManager.GROUP_NAME;
import static com.SampleApp.row.Utils.PreferenceManager.GRP_PROFILE_ID;
import static com.SampleApp.row.Utils.PreferenceManager.IS_GRP_ADMIN;
import static com.SampleApp.row.Utils.PreferenceManager.MASTER_USER_ID;
import static com.SampleApp.row.Utils.PreferenceManager.MY_CATEGORY;
import static com.SampleApp.row.Utils.PreferenceManager.savePreference;

/**
 * Created by user on 23-12-2015.
 */
public class FragmentALL extends Fragment {
    // public static int[] prgmImages = {R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher};
    // public static String[] name = {"Fusce ornare", "Ultricies metus", "Fusce fringilla", "Integer sit amet", "Cras blandit dui"};

    //GridView gv;
    ArrayList<String> listGroup;
    ArrayList<Object> grplist = new ArrayList<>();
    ArrayList<ModuleData> moduleList = new ArrayList<ModuleData>();
    public static NotificationCountData notificationCountDatas = new NotificationCountData();
    FloatingActionButton fab;
    private String onresume_flag = "0";
    private long masterUid, grpId;
    GroupMasterModel groupModel;
    ModuleDataModel moduleDataModel;
    ArrayAdapter<String> adapter;
    String updatedOn = "";

    public String RSS_FEEDS_FILE = "rss_feeds.json";
    public String RSS_BLOGS_FILE = "rss_blogs.json";
    //public ListView lv;

    // Code written for RecyclerView

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutmanager;
    public FragmentALLAdapter_new rv_adapter;

    ArrayList<NewsFeed> feedList;
    private RSSFeedsModel feedModel;
    private RotaryBlogsModel blogsModel;

    ArrayList<BlogFeed> blogList;


    FragmentALLAdapter_new.OnGroupSelectedListener onGroupSelectedListener = new FragmentALLAdapter_new.OnGroupSelectedListener() {
        @Override
        public void onGroupSelected(int position) {
            try {
                Intent i = new Intent(getActivity(), GroupDashboard.class);
                i.putExtra("position", position);
                i.putExtra("groupId", ((GroupData) grplist.get(position)).getGrpId().toString());
                // i.putExtra("groupname", grplist.get(position).getGrpId().toString());
                savePreference(getActivity(), GROUP_ID, ((GroupData) grplist.get(position)).getGrpId().toString());
                savePreference(getActivity(), GRP_PROFILE_ID, ((GroupData) grplist.get(position)).getGrpProfileId().toString());
                try {
                    String loginType = PreferenceManager.getPreference(getContext(), PreferenceManager.LOGIN_TYPE);
                    Utils.log("Login Type : " + loginType);
                    if (loginType.equals("1")) {
                        savePreference(getActivity(), IS_GRP_ADMIN, "No");
                    } else {
                        savePreference(getActivity(), IS_GRP_ADMIN, ((GroupData) grplist.get(position)).getIsGrpAdmin().toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                savePreference(getActivity(), GROUP_NAME, ((GroupData) grplist.get(position)).getGrpName().toString());
                savePreference(getActivity(), MY_CATEGORY, ((GroupData) grplist.get(position)).getMyCategory());
                startActivityForResult(i, Constant.REQUEST_DASHBOARD);
            } catch (ClassCastException cce) {
                Utils.log("Error is : " + cce);
                cce.printStackTrace();
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grp_all, container, false);
        masterUid = Long.parseLong(PreferenceManager.getPreference(getActivity().getApplicationContext(), PreferenceManager.MASTER_USER_ID)); // line added by pp
        //lv = (ListView) view.findViewById(R.id.listview);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        // gv.setAdapter(new FragmentALLAdapter(getActivity(), name));
        groupModel = new GroupMasterModel(getActivity().getApplicationContext()); // line by prasad
        moduleDataModel = new ModuleDataModel(getActivity().getApplicationContext());

        String temp = PreferenceManager.getPreference(getActivity(), TBPrefixes.MODULES_PREFIX + masterUid);

        if (temp != null) {
            Log.e("ModulesUpdatedOn", "ModulesUpdatedOn" + temp);
        } else {
            Log.e("ModulesUpdatedOn", "No date found");
        }

        temp = PreferenceManager.getPreference(getActivity(), TBPrefixes.ENTITY_PREFIX + masterUid);
        if (temp != null) {
            Log.e("EntityUpdatedOn", "EntityUpdatedOn" + temp);
        } else {
            Log.e("EntityUpdatedOn", "No date found");
        }

        //  grpId = Long.parseLong(PreferenceManager.getPreference(getActivity().getApplicationContext(), PreferenceManager.GROUP_ID));
        //groupModel.printTable();

        // Code for Recycler view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutmanager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutmanager);

        init();

        /*if (InternetConnection.checkConnection(getActivity())) {
            //webservice_new(); // this is the retrofit api call
            onresume_flag = "0";
            webservices();
        } else {
            Utils.showToastWithTitleAndContext(getActivity(), "No Internet Connection!");
            // Not Available...
        }
        */
        loadFromDB();
        return view;
    }

    /*private void updateProfilePopup() {
        final Dialog dialog = new Dialog(this.getContext(), android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_update_profile);
        TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
        TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
        TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
        tv_no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                Intent i = new Intent(getActivity().getApplicationContext(), EditProfileActivity.class);
//                i.putExtra("groupId", "31048");
//                i.putExtra("profileId","152116");
//                startActivity(i);
            }
        });

        dialog.show();
    }*/


    /*  @Override
      public void onResume() {
          super.onResume();
          if (InternetConnection.checkConnection(getActivity())) {
              onresume_flag ="1";
              webservices();

              Log.e("CallingFromOnResume", "Service is called from onResume");
          } else {
              Utils.showToastWithTitleAndContext(getActivity(),"No Internet Connection!");
              // Not Available...
          }
      }
  */
    private void init() {

        feedList = new ArrayList<>();
        blogList = new ArrayList<>();
        feedModel = new RSSFeedsModel(getActivity());
        blogsModel = new RotaryBlogsModel(getActivity());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(getActivity(), CreateGroup.class);
                startActivity(i);*/

                String url = "http://bit.ly/2geWOh0RequestTBEntity";

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_DASHBOARD && resultCode == RESULT_OK) {

            refreshFragmentAdapter();
        }
    }

    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("masterUID", PreferenceManager.getPreference(getActivity(), PreferenceManager.MASTER_USER_ID)));
        arrayList.add(new BasicNameValuePair("imeiNo", PreferenceManager.getPreference(getActivity(), PreferenceManager.UDID)));
        String updatedOn = PreferenceManager.getPreference(this.getContext(), TBPrefixes.ENTITY_PREFIX + masterUid, "1970/01/01 00:00:00");

        String countryCode = PreferenceManager.getPreference(this.getContext(), PreferenceManager.COUNTRY_CODE, "0");
        String mobileNo = PreferenceManager.getPreference(this.getContext(), PreferenceManager.MOBILE_NUMBER, "");
        String loginType = PreferenceManager.getPreference(getActivity(), PreferenceManager.LOGIN_TYPE, "0");

        arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));
        arrayList.add(new BasicNameValuePair("loginType", loginType));
        arrayList.add(new BasicNameValuePair("mobileNo", mobileNo));
        arrayList.add(new BasicNameValuePair("countryCode", countryCode));

        Log.d("Response", "PARAMETERS " + Constant.GetAllGroupsList + " :- " + arrayList.toString());
        new WebConnectionAsync(Constant.GetAllGroupsList, arrayList, getActivity()).execute();
    }


    @Override
    public void onStart() {
        super.onStart();

        //isGroupUpdated is added because we want to call checkForUpdate();
        //only when groups is Updated otherwise no need to call this method
        String isGroupUpdated = PreferenceManager.getPreference(getActivity(), "isGroupEdited");
        if (isGroupUpdated != null && isGroupUpdated.equalsIgnoreCase("Yes")) {
            if (InternetConnection.checkConnection(getActivity().getApplicationContext()))
                checkForUpdate();
            Log.d("TouchBase", "Call to checkForUpdate() in onStart");
        }


    }


    public class WebConnectionAsync extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.TBProgressBar);

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
            if (onresume_flag.equals("0")) {
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
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

                // grplist.clear();

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
                        String expiryDate = objects.getString("expiryDate");
                        gd.setExpiryDate(expiryDate);
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
                grplist.add(new LoadingMessageData());
                rv_adapter = new FragmentALLAdapter_new(this.getContext(), grplist, "1");
                mRecyclerView.setAdapter(rv_adapter);
                rv_adapter.setOnGroupSelectedListener(onGroupSelectedListener);
                getNotificationCountWebservices();
                rv_adapter.notifyDataSetChanged();
            } else if (status.equals("2")) {

                // ActivityResult.getString("message");
                grplist.clear();
                rv_adapter = new FragmentALLAdapter_new(this.getContext(), grplist, "1");
                mRecyclerView.setAdapter(rv_adapter);
                rv_adapter.setOnGroupSelectedListener(onGroupSelectedListener);
                final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_session_expired);

                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);

                tv_line1.setText(ActivityResult.getString("message"));
                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        savePreference(getActivity(), GROUP_ID, null);
                        savePreference(getActivity(), GRP_PROFILE_ID, null);
                        savePreference(getActivity(), IS_GRP_ADMIN, null);
                        savePreference(getActivity(), GROUP_NAME, null);
                        savePreference(getActivity(), MASTER_USER_ID, null);

                        Intent i = new Intent(getActivity(), Splash.class);
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
                            savePreference(getActivity(), GROUP_ID, null);
                            savePreference(getActivity(), GRP_PROFILE_ID, null);
                            savePreference(getActivity(), IS_GRP_ADMIN, null);
                            savePreference(getActivity(), GROUP_NAME, null);
                            savePreference(getActivity(), MASTER_USER_ID, null);

                            Intent i = new Intent(getActivity(), Splash.class);
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

    // THis API is Using retrofit to get the data from server
    private void webservice_new() {
        //While the app fetched data we are displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Fetching Data", "Please wait...", false, false);

        //Creating a rest adapter
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constant.GetAllGroupsList)
                .setProfiler(new Profiler() {
                    @Override
                    public Object beforeCall() {
                        return null;
                    }

                    @Override
                    public void afterCall(RequestInformation requestInfo, long elapsedTime, int statusCode, Object beforeCallData) {
                        Log.d("Retrofit Profiler", String.format("HTTP %d %s %s (%dms)",
                                statusCode, requestInfo.getMethod(), requestInfo.getRelativePath(), elapsedTime));

                    }
                })
                .build();

        //Creating an object of our api interface
        DashboardAPI api = adapter.create(DashboardAPI.class);
        JSONObject jsonRequest = new JSONObject();
        TypedInput in = null;
        try {
            jsonRequest.put("masterUID", PreferenceManager.getPreference(getActivity(), PreferenceManager.MASTER_USER_ID));
            in = new TypedByteArray("application/json", jsonRequest.toString().getBytes("UTF-8"));
        } catch (JSONException e) {
            Log.e("ERROR", "JSON :- " + e.toString());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            Log.e("ERROR", "JSON :- " + e.toString());
            e.printStackTrace();
        }
        //Defining the method
        //Log.d("TEST","########## :- "+jsonRequest.toString());
        api.getGroups(in, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                //  Log.d("TEST","@@@@@@@@@@@@@@ :- "+response.toString()+" -- "+response2.toString());
                loading.dismiss();
                String bodyString = new String(((TypedByteArray) response.getBody()).getBytes());
                Log.d("RESPONSE", "RESULT :- " + bodyString);
                getresult(bodyString);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("ERROR", "JSON :- " + error);
                loading.dismiss();
            }
        });
    }

    public void popup_of_update_app() {
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_session_expired);
        TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
        TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);
        TextView tv_header = (TextView) dialog.findViewById(R.id.tv_header);
        tv_yes.setText("Update");
        tv_header.setVisibility(View.VISIBLE);
        tv_header.setText("New Version Available");
        tv_line1.setText("There is a newer version avaliable for download! Please update the app by visiting the Play Store.");
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
                    Intent i = new Intent(getActivity(), Splash.class);
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

    // code added by prasad
    public void loadFromDB() {

        Log.d("Touchbase", "Trying to load from local db");

        grplist.addAll(groupModel.getGroups(masterUid));
        //Log.e("touchbase list", "**************"+grplist.toString());
        //boolean isDataAvailable = groupModel.isDataAvailable();
        boolean isDataAvailable = groupModel.isDataAvailableBasedOnMasterUid(masterUid);
        Log.e("DataAvailable", "Data available : " + isDataAvailable);

        if (!isDataAvailable) {
            Log.d("Touchbase---@@@@@@@@", "Loading from server");
            if (InternetConnection.checkConnection(getActivity().getApplicationContext()))
                loadFromServer();

            else
                Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
        } else {
            Log.d("Touchbase---@@@@@@@@", "Loaded from local db");
            rv_adapter = new FragmentALLAdapter_new(this.getContext(), grplist, "1");
            mRecyclerView.setAdapter(rv_adapter);
            rv_adapter.setOnGroupSelectedListener(onGroupSelectedListener);

            loadRssFeeds();
            loadRssBlogs();


            //new LoadRssTask().execute();
            //new LoadBlogTask().execute();
            // If data is loaded from local database then check for update

            if (InternetConnection.checkConnection(getActivity().getApplicationContext())) {
                getNotificationCountWebservices();
                checkForUpdate();
                Log.d("---------------", "Check for update gets called------");
            } else {
                //Toast.makeText(getContext(), "No internet connection to get Updated Records", Toast.LENGTH_LONG).show();
            }

        }
    }

    //-----------------------------------------This is to sync the data-------------------------
    public void checkForUpdate() {
        if (InternetConnection.checkConnection(getActivity())) {
            Log.e("Touchbase", "------ Checking for update");
            String url = Constant.GetGetAllGroupListSync;
            ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
            arrayList.add(new BasicNameValuePair("masterUID", PreferenceManager.getPreference(getActivity(), PreferenceManager.MASTER_USER_ID)));
            arrayList.add(new BasicNameValuePair("imeiNo", PreferenceManager.getPreference(getActivity(), PreferenceManager.UDID)));
            updatedOn = PreferenceManager.getPreference(getActivity(), TBPrefixes.ENTITY_PREFIX + masterUid);
            Log.e("UpdatedOn", "Last updated date is : " + updatedOn);
            Log.e("MasterUID", PreferenceManager.getPreference(getActivity(), PreferenceManager.MASTER_USER_ID));

            arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));//updatedOn 1970-1-1 0:0:0

            String countryCode = PreferenceManager.getPreference(this.getContext(), PreferenceManager.COUNTRY_CODE, "0");
            String mobileNo = PreferenceManager.getPreference(this.getContext(), PreferenceManager.MOBILE_NUMBER, "");
            String loginType = PreferenceManager.getPreference(getActivity(), PreferenceManager.LOGIN_TYPE, "0");

            arrayList.add(new BasicNameValuePair("loginType", loginType));
            arrayList.add(new BasicNameValuePair("mobileNo", mobileNo));
            arrayList.add(new BasicNameValuePair("countryCode", countryCode));
            Log.e("UpdatedOn", "Last updated date is : " + updatedOn);
            Log.e("MasterUID", PreferenceManager.getPreference(getActivity(), PreferenceManager.MASTER_USER_ID));
            //arrayList.add(new BasicNameValuePair("updatedOn", "2016/1/18 17:8:34"));

            Log.e("request", arrayList.toString());
            UpdateGroupDataAsyncTask task = new UpdateGroupDataAsyncTask(url, arrayList, getActivity());
            task.execute();
            Log.d("Response", "PARAMETERS " + Constant.GetGetAllGroupListSync + " :- " + arrayList.toString());
            //new WebConnectionAsyncDirectory(Constant.GetDirectoryListSync, arrayList, Directory.this).execute();

        } else {
            Log.e("SyncFailed", "No internet conenction to sync data");
        }
    }


    public class UpdateGroupDataAsyncTask extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.TBProgressBar);
        Context context = null;
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
                                        PreferenceManager.savePreference(getActivity(), TBPrefixes.ENTITY_PREFIX + masterUid, updatedOn);
                                        PreferenceManager.savePreference(getActivity(), TBPrefixes.MODULES_PREFIX + masterUid, updatedOn);

                                        grplist = new ArrayList<>();
                                        grplist.addAll(groupModel.getGroups(masterUid));

                                        rv_adapter = new FragmentALLAdapter_new(getContext(), grplist, "1");
                                        mRecyclerView.setAdapter(rv_adapter);
                                        rv_adapter.setOnGroupSelectedListener(onGroupSelectedListener);
                                        openGroupForLink();
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
                mRecyclerView.setAdapter(new FragmentALLAdapter_new(getActivity(), grplist, "1"));
                rv_adapter.setOnGroupSelectedListener(onGroupSelectedListener);
                final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_session_expired);

                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                TextView tv_line1 = (TextView) dialog.findViewById(R.id.tv_line1);

                tv_line1.setText("Your session has been expired.");

                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        savePreference(getActivity(), GROUP_ID, null);
                        savePreference(getActivity(), GRP_PROFILE_ID, null);
                        savePreference(getActivity(), IS_GRP_ADMIN, null);
                        savePreference(getActivity(), GROUP_NAME, null);
                        savePreference(getActivity(), MASTER_USER_ID, null);

                        Intent i = new Intent(getActivity(), Splash.class);
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
                            savePreference(getActivity(), GROUP_ID, null);
                            savePreference(getActivity(), GRP_PROFILE_ID, null);
                            savePreference(getActivity(), IS_GRP_ADMIN, null);
                            savePreference(getActivity(), GROUP_NAME, null);
                            savePreference(getActivity(), MASTER_USER_ID, null);

                            Intent i = new Intent(getActivity(), Splash.class);
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
        getNotificationCountWebservices();
    }


    public void loadFromServer() {
        webservices();
    }

    /*

        //========================== changes by lekha for updating data ===============

        public void checkForUpdate() {
            if ( ! InternetConnection.checkConnection(getActivity().getApplicationContext())) {
                String url = "";
                ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
                arrayList.add(new BasicNameValuePair("masterUID", PreferenceManager.getPreference(getActivity().getApplicationContext(), PreferenceManager.MASTER_USER_ID)));
                arrayList.add(new BasicNameValuePair("grpID", PreferenceManager.getPreference(getActivity().getApplicationContext(), PreferenceManager.GROUP_ID)));
                //arrayList.add(new BasicNameValuePair("searchText", et_serach_directory.getText().toString()));
                arrayList.add(new BasicNameValuePair("page", ""));
                String updatedOn = PreferenceManager.getPreference(getActivity().getApplicationContext(), "DirectoryUpdatedOn", "1970/01/01 00:00:00");
                arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));

                UpdateDirectoryAsyncTask task = new UpdateDirectoryAsyncTask(url, arrayList, getActivity().getApplicationContext());
                task.execute();
            }
        }

        public class UpdateDirectoryAsyncTask extends AsyncTask<String, Object, Object> {

            String val = null;
            final ProgressDialog progressDialog = new ProgressDialog(getActivity().getApplicationContext(), R.style.TBProgressBar);
            Context context = null;
            String url = null;
            List<NameValuePair> argList = null;


            public UpdateDirectoryAsyncTask(String url, List<NameValuePair> argList, Context ctx) {
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
                //	Log.d("response","Do post"+ result.toString());

                if (result != "") {
                    Log.d("Response", "calling getDirectorydetails");

                    getUpdatedFragmentAllItems(result.toString());

                } else {
                    Log.d("Response", "Null Resposnse");
                }

            }
        }

        public  void getUpdatedFragmentAllItems(String result)
        {
            {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONObject DirectoryResult = jsonObj.getJSONObject("TBMemberResult");

                    final String status = DirectoryResult.getString("status");
                    if (status.equals("0")) {
                        final ArrayList<GroupData> newEntityData = new ArrayList<GroupData>();

                        JSONArray newDirectoryListResult = DirectoryResult.getJSONObject("newRecords").getJSONArray("MemberListResults");
                        for (int i = 0; i < newDirectoryListResult.length(); i++) {
                            JSONObject object = newDirectoryListResult.getJSONObject(i);
                            JSONObject objects = object.getJSONObject("MemberListResult");

                            GroupData data = new GroupData();

                            data.setGrpId(objects.getString("grpId").toString());
                            data.setGrpName(objects.getString("grpName").toString());
                            data.setGrpProfileId(objects.getString("grpProfileId").toString());
                            data.setMyCategory(objects.getString("myCategory").toString());
                            data.setIsGrpAdmin(objects.getString("isGrpAdmin").toString());

                            newEntityData.add(data);
                        }

                        final ArrayList<GroupData> updatedEntityData = new ArrayList<GroupData>();
                        JSONArray updatedDirectoryListResult = DirectoryResult.getJSONObject("updatedRecords").getJSONArray("MemberListResults");
                        for (int i = 0; i < updatedDirectoryListResult.length(); i++) {
                            JSONObject object = updatedDirectoryListResult.getJSONObject(i);
                            JSONObject objects = object.getJSONObject("MemberListResult");

                            GroupData data = new GroupData();

                            data.setGrpId(objects.getString("grpId").toString());
                            data.setGrpName(objects.getString("grpName").toString());
                            data.setGrpProfileId(objects.getString("grpProfileId").toString());
                            data.setMyCategory(objects.getString("myCategory").toString());
                            data.setIsGrpAdmin(objects.getString("isGrpAdmin").toString());
                            updatedEntityData.add(data);
                        }

                        final ArrayList<Long> deletedEntityData = new ArrayList<Long>();
                        JSONArray deletedDirectoryListResult = DirectoryResult.getJSONObject("updatedRecords").getJSONArray("MemberListResults");
                        for (int i = 0; i < deletedDirectoryListResult.length(); i++) {
                            JSONObject object = deletedDirectoryListResult.getJSONObject(i);
                            JSONObject objects = object.getJSONObject("MemberListResult");

                            GroupData data = new GroupData();

                            data.setGrpId(objects.getString("grpId").toString());
                            data.setGrpName(objects.getString("grpName").toString());
                            data.setGrpProfileId(objects.getString("grpProfileId").toString());
                            data.setMyCategory(objects.getString("myCategory").toString());
                            data.setIsGrpAdmin(objects.getString("isGrpAdmin").toString());
                            deletedEntityData.add(Long.getLong(data.getGrpId()));
                        }

                        Handler handler = new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                boolean saved = groupModel.syncData(masterUid, grpId, newEntityData, updatedEntityData, deletedEntityData);
                                if ( ! saved ) {
                                    Log.e("SyncFailed------->", "Failed to update data in local db. Retrying in 2 seconds");
                                    sendEmptyMessageDelayed(0, 2000);
                                } else {
                                    // updating last updated date in shared preferences.
                                    savePreference(getActivity().getApplicationContext(), "DirectoryUpdatedOn", DateHelper.getCurrentDate());

                                    // Reloading all data for display purpose.
                                    grplist = groupModel.getGroups(masterUid);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        };

                        handler.sendEmptyMessageDelayed(0, 1000);
                    }
                } catch (Exception e) {
                    Log.d("exec", "Exception :- " + e.toString());
                    e.printStackTrace();
                }
            }
        }
    */
    public void openGroupForLink() {
        Log.e("♦♦♦♦♦", "Inside openGroupLink");
        if (getActivity().getIntent().getStringExtra("openGroup") != null) {
            Log.e("♦♦♦♦♦", "Needs to open openGroupLink");
            Intent intent = new Intent(getActivity().getApplicationContext(), GroupDashboard.class);
            startActivity(intent);
        } else {
            Log.e("♦♦♦♦♦", "No need to open openGroupLink");
        }
    }

    Handler groupDataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            Log.e("DBError", "Handler is called");
//            boolean saved = groupModel.insert(masterUid, grplist);
//            if (!saved) {
//                Log.d("Touchbase---@@@@@@@@", "Failed to save offlline. Retrying in 2 seconds");
//                sendEmptyMessageDelayed(0, 2000);
//            } else {
//                savePreference(getActivity().getApplicationContext(), TBPrefixes.ENTITY_PREFIX + masterUid, updatedOn);
//                Log.d("Touchbase---@@@@@@@@", updatedOn + "groupHandler executed successfully");
//                getActivity().sendBroadcast(new Intent(Constant.GROUP_DATA_LOADED));
//                Log.d("Touchbase---@@@@@@@@", moduleDataHandler + "execution starts");
//                moduleDataHandler.sendEmptyMessageDelayed(0, 1000);
//                //syncModel.update(masterUid, Utils.)
//            }
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
                savePreference(getActivity().getApplicationContext(), TBPrefixes.MODULES_PREFIX + masterUid, updatedOn);
                openGroupForLink();
                //updateProfilePopup();
                loadRssFeeds();
                loadRssBlogs();
                // new LoadRssTask().execute();
                //new LoadBlogTask().execute();


            }
        }
    };

    //===========================get Notification Count Webservice =======================
    public void getNotificationCountWebservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("masterUID", "" + masterUid));//PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)
        try {
            Log.d("Response", "PARAMETERS " + Constant.GetNotificationCount + " :- " + arrayList.toString());
            if (InternetConnection.checkConnection(getActivity())) {
                new WebConnectionGetNotificationCount(Constant.GetNotificationCount, arrayList, getContext()).execute();
            } else {
                Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException npe) {

        } catch (Exception e) {
            Log.e("TouchBase", "♦♦♦♦Error is : " + e.getMessage());
            e.printStackTrace();
        }
    }


    public class WebConnectionGetNotificationCount extends AsyncTask<String, Object, Object> {

        String val = null;

        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;

        public WebConnectionGetNotificationCount(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //	dialog.show();
        }

        @Override
        protected Object doInBackground(String... params) {
            try {
                val = HttpConnection.postData(url, argList);
                val = val.toString();
                //Log.d("Response", "we" + val);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return val;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            //	Log.d("response","Do post"+ result.toString());

            if (result != "") {
                getNotificationCountResult(result.toString());
            } else {
                Log.d("Response", "Null Resposnse");
            }
        }
    }

    private void getNotificationCountResult(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject groupResult = jsonObj.getJSONObject("TBGroupResult");
            final String status = groupResult.getString("status");
            if (status.equals("0")) {
                listGroup = new ArrayList<String>();
                JSONArray groupCountList = groupResult.getJSONArray("GrpCountList");
                //notificationCountDatas.clear();
                for (int i = 0; i < groupCountList.length(); i++) {
                    JSONObject result_object = groupCountList.getJSONObject(i);
                    String countGroupId = result_object.getString("groupId");
                    String countTotalCount = result_object.getString("totalCount");
                    String countGroupCategory = result_object.getString("groupCategory");

                    Hashtable<String, String> moduleCount = new Hashtable<String, String>();

                    JSONArray moduleCountArray = result_object.getJSONArray("ModCount");
                    for (int j = 0; j < moduleCountArray.length(); j++) {
                        JSONObject moduleCountObject = moduleCountArray.getJSONObject(j);
                        String moduleId = moduleCountObject.getString("moduleId");
                        String count = moduleCountObject.getString("count");
                        moduleCount.put(moduleId, count);
                    }

                    GroupCountData countData = new GroupCountData(countGroupId, countTotalCount, countGroupCategory, moduleCount);
                    notificationCountDatas.addGroupCountData(countGroupId, countData);
                }

                rv_adapter.notifyDataSetChanged();
                //Log.e("TouchBase", "♦♦♦♦I must be first");
                try {
                    Intent intent = new Intent(Constant.BroadcastMessages.COUNT_UPDATED);
                    getActivity().sendBroadcast(intent);
                } catch (NullPointerException npe) {
                    //Log.e("TouchBase", "♦♦♦♦Error is : "+npe.getMessage());
                    npe.printStackTrace();
                }
                /*gv.smoothScrollToPosition(grplist.size()-1);
                gv.smoothScrollToPosition(0);*/

                /*fragmentALLAdapter_new = new FragmentALLAdapter_new(getContext(), notificationCountDatas);
                  gv.setAdapter(fragmentALLAdapter_new);*/
            }
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
            e.printStackTrace();
        }
    }


    /*@Override
    public void onPause() {
        super.onPause();
        refreshFragmentAdapter();
    }*/
    // https://my.rotary.org/en/rss.xml
    // https://blog.rotary.org/feed/

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

    private void refreshFragmentAdapter() {
        if (rv_adapter != null)
            rv_adapter.notifyDataSetChanged();
//        loadRssFeeds();
//        loadRssBlogs();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshFragmentAdapter();
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
                    if (InternetConnection.checkConnection(getActivity())) {
                        new LoadRssTask().execute();
                    }
                } else {
                    feedList = new ArrayList<>();
                    feedList = feedModel.getNewsFeedList();
                    grplist.add("Rotary News & Updates");
                    grplist.addAll(feedList);
                    rv_adapter.notifyDataSetChanged();
                    checkForFeedsUpdate();
                }
            }
        }.execute();

    }

    public void saveRssFeeds(String s) {
        try {
            /*FileOutputStream fout = this.getContext().openFileOutput(RSS_FEEDS_FILE, MODE_PRIVATE);
            fout.write(s.getBytes());
            fout.close();*/
            feedList = parseFeeds(s);

            Handler RssFeedHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    boolean saved = feedModel.syncData(feedList);
                    if (!saved) {
                        Log.e("SyncFailed------->", "Failed to update data in local db for RSS Feeds. Retrying in 2 seconds");
                        sendEmptyMessageDelayed(0, 2000);
                    } else {
                        grplist.clear();
                        grplist.addAll(groupModel.getGroups(masterUid));

                        feedList = new ArrayList<>();
                        feedList = feedModel.getNewsFeedList();
                        grplist.add("Rotary News & Updates");
                        grplist.addAll(feedList);
                        rv_adapter.notifyDataSetChanged();
                        checkForBlogsUpdate();
                    }
                }
            };

            if (feedList.size() > 0) {
                RssFeedHandler.sendEmptyMessageDelayed(0, 1000);
            } else {

            }
        } catch (Exception ioe) {
            Utils.log("Error is : " + ioe);
            ioe.printStackTrace();
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
                    if (InternetConnection.checkConnection(getActivity())) {
                        new LoadBlogTask().execute();
                    }
                } else {
                    blogList = new ArrayList<>();
                    blogList = blogsModel.getBlogsList();
                    grplist.add("Rotary Blogs");
                    grplist.addAll(blogList);
                    rv_adapter.notifyDataSetChanged();
                }
            }
        }.execute();

        /*try {
            FileInputStream fin = this.getContext().openFileInput(RSS_BLOGS_FILE);
            String fieldData = new String();
            byte[] buffer = new byte[1024];
            while ( fin.available() != 0 ) {
                int n = fin.read(buffer);
                fieldData = fieldData + new String(buffer, 0, n);
            }
            fin.close();
            parseBlogs(fieldData);
        }catch (FileNotFoundException fne){
            Utils.log("RSS Blogs File are not present in local file");
            if (InternetConnection.checkConnection(this.getContext())) {
                new LoadBlogTask().execute();
            }else{
                Toast.makeText(this.getContext(), "No internet conenction", Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            Utils.log("RSS Blogs File are not present in local file");
            if (InternetConnection.checkConnection(this.getContext())) {
                new LoadBlogTask().execute();
            }
        }*/
    }

    public void saveRssBlogs(String s) {
        try {

            blogList = parseBlogs(s);

            Handler RssBlogsHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    boolean saved = blogsModel.syncData(blogList);
                    if (!saved) {
                        Log.e("SyncFailed------->", "Failed to update data in local db for RSS Feeds. Retrying in 2 seconds");
                        sendEmptyMessageDelayed(0, 2000);
                    } else {
                        blogList = new ArrayList<>();
                        blogList = blogsModel.getBlogsList();
                        grplist.add("Rotary Blogs");

                        grplist.addAll(blogList);
                        rv_adapter.notifyDataSetChanged();
                    }
                }
            };

            if (blogList.size() > 0) {
                RssBlogsHandler.sendEmptyMessageDelayed(0, 1000);
            } else {

            }

        } catch (Exception ioe) {
            Utils.log("Error is : " + ioe);
            ioe.printStackTrace();
        }

    }

    public void checkForFeedsUpdate() {
        if (InternetConnection.checkConnection(getActivity())) {
            new LoadRssTask().execute();
        }
    }

    public void checkForBlogsUpdate() {
        new LoadBlogTask().execute();
    }
}

