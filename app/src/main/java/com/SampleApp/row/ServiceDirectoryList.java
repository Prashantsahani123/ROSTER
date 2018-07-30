package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.ServiceDirectoryListAdapter;
import com.SampleApp.row.Data.ServiceDirectoryListData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.sql.ServiceDirectoryDataModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER1 on 19-07-2016.
 */
public class ServiceDirectoryList extends Activity {
    String moduleName = "";
    private static final String TAG = "";
    ListView listview,memberListView;
    RecyclerView recyclerView;

    ArrayAdapter<String> adapter;
    EditText et_serach_directory;

    private ArrayList<ServiceDirectoryListData> serviceDirectoryListDatas; //= new ArrayList<ServiceDirectoryListData>();
    private ServiceDirectoryListAdapter serviceDirectoryListAdapter;

    private ArrayList<ServiceDirectoryListData> newDirectoryData = new ArrayList<>();

    TextView tv_title;
    ImageView iv_backbutton;
    private ImageView iv_actionbtn;
    private long masterUid, grpId;
    ServiceDirectoryDataModel serviceDirectoryDataModel = new ServiceDirectoryDataModel(ServiceDirectoryList.this);
    String updatedOn = "";
    ImageView iv_cleartext;
    String moduleId = "15";
    String cattegory_id = "0";
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_directory_list);
        Utils.log("Inside ServiceDirectoryList activity");
        listview = (ListView) findViewById(R.id.listView);


        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn.setVisibility(View.VISIBLE);
        // iv_backbutton.setVisibility(View.GONE);
        title = getIntent().getExtras().getString("moduleName");
        if(getIntent().hasExtra("categoryId")) {
            cattegory_id = getIntent().getExtras().getString("categoryId");
        }
        tv_title.setText(title);
        serviceDirectoryListDatas = new ArrayList<ServiceDirectoryListData>();
        et_serach_directory = (EditText) findViewById(R.id.et_serach_directory);
        iv_cleartext = (ImageView)findViewById(R.id.iv_cleartext);
        masterUid = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)); // line added by lekha
        grpId = Long.parseLong(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID));
        moduleId = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MODULE_ID, "15");

        //webservices_getdata();
        /*
        if (InternetConnection.checkConnection(getApplicationContext())) {
                    // Avaliable
                    webservices();
                } else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection !!!");
                    // Not Available...
                }
        */
        listview.setTextFilterEnabled(true);
//        serviceDirectoryDataModel = new ServiceDirectoryDataModel(getApplicationContext());
//        serviceDirectoryDataModel.printTable();
        Log.e("ModuleId" , "Module ID : "+moduleId);
        init();


        loadFromDB();
        //loadFromServer();
        checkadminrights();
    }



    @Override
    protected void onResume() {
        super.onResume();
        et_serach_directory.setText("");
    }

    private void checkadminrights() {
        if (PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN).equals("No")) {
            iv_actionbtn.setVisibility(View.GONE);
        }
    }

    public void init() {

        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (InternetConnection.checkConnection(getApplicationContext())) {
                    // Avaliable
                    Intent i = new Intent(ServiceDirectoryList.this, ServiceDirectoryAdd_new.class);
                    i.putExtra("categoryId",cattegory_id);
                    startActivityForResult(i, 1);

                    //webservices_update();
                } else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }

            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent i = new Intent(ServiceDirectoryList.this, ServiceDirectoryDetail.class);
                    i.putExtra("serviceDirId", serviceDirectoryListAdapter.getGridData().get(position).getServiceDirId());
                    i.putExtra("categoryId",String.valueOf(cattegory_id));
                    // i.putExtra("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID));
                    //startActivity(i);
                    startActivityForResult(i,1);
            }
        });

        iv_cleartext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (!(et_serach_directory.getText().toString().trim().matches("")) || !(et_serach_directory.getText().toString().trim() == null) ){

                    et_serach_directory.setText("");
                }*/

                et_serach_directory.setText("");
            }
        });

        et_serach_directory.addTextChangedListener(new TextWatcher() {
            String q;


            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                q = et_serach_directory.getText().toString();

//                if (et_serach_directory.getText().toString().trim().matches("") || et_serach_directory.getText().toString().trim() == null)
//                {
//                    iv_cleartext.setVisibility(View.INVISIBLE);
//                }
//                iv_cleartext.setVisibility(View.VISIBLE);
//
//
//
//                String q = et_serach_directory.getText().toString();
//                if ( q.indexOf("'") != -1 ) {
//                    Toast.makeText(ServiceDirectoryList.this, "Invalid characters in search keyword", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                ArrayList<ServiceDirectoryListData> newDirectoryData = serviceDirectoryDataModel.search(masterUid, grpId, moduleId, q);
//                serviceDirectoryListAdapter = new ServiceDirectoryListAdapter(ServiceDirectoryList.this, R.layout.service_directory_list_item1, newDirectoryData, "0");
//                listview.setAdapter(serviceDirectoryListAdapter);
//                if ( newDirectoryData.size() == 0 ) {
//                    listview.setEmptyView(findViewById(R.id.tv_no_records_found));
//                }

            }




            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {

                if (et_serach_directory.getText().toString().trim().matches("") || et_serach_directory.getText().toString().trim() == null)
                {
                    iv_cleartext.setVisibility(View.INVISIBLE);
                    ArrayList<ServiceDirectoryListData> newDirectoryData = serviceDirectoryDataModel.search(masterUid, grpId, moduleId,cattegory_id,q);
                    serviceDirectoryListAdapter = new ServiceDirectoryListAdapter(ServiceDirectoryList.this, R.layout.service_directory_list_item1, newDirectoryData, "0");
                    listview.setAdapter(serviceDirectoryListAdapter);
                    if ( newDirectoryData.size() == 0 ) {
                        listview.setEmptyView(findViewById(R.id.tv_no_records_found));
                    }

                }
                else {
                    iv_cleartext.setVisibility(View.VISIBLE);

                    if (q.indexOf("'") != -1) {
                        Toast.makeText(ServiceDirectoryList.this, "Invalid characters in search keyword", Toast.LENGTH_LONG).show();
                        return;
                    }
                    ArrayList<ServiceDirectoryListData> newDirectoryData = serviceDirectoryDataModel.search(masterUid, grpId, moduleId,cattegory_id, q);
                    serviceDirectoryListAdapter = new ServiceDirectoryListAdapter(ServiceDirectoryList.this, R.layout.service_directory_list_item1, newDirectoryData, "0");
                    listview.setAdapter(serviceDirectoryListAdapter);
                    if ( newDirectoryData.size() == 0 ) {
                        listview.setEmptyView(findViewById(R.id.tv_no_records_found));
                    }
                }

                // TODO Auto-generated method stub
            }
        });


        et_serach_directory.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    // webservices();

                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        webservices_getdata();
                    } else {
                        Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                    return true;

                }
                return false;
            }
        });
    }



    public void finishActivity(View v) {
        finish();
    }

    private void webservices_getdata() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        //arrayList.add(new BasicNameValuePair("masterUID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));

        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        //arrayList.add(new BasicNameValuePair("searchText", ""));
      //  arrayList.add(new BasicNameValuePair("updatedOn", "1972/01/01 15:00"));

        updatedOn = PreferenceManager.getPreference(this, "ServiceDirectoryUpdatedOn_"+moduleId+"_"+grpId, "1970/01/01 00:00:00");

        arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));
        arrayList.add(new BasicNameValuePair("moduleId", moduleId));
        Log.e("UpdatedOn", "Last updated on time is : "+updatedOn);

        Log.d("Response", "PARAMETERS " + Constant.GetServiceDirectoryList + " :- " + arrayList.toString());
        if ( InternetConnection.checkConnection(ServiceDirectoryList.this))
            new WebConnectionAsync(Constant.GetServiceDirectoryList, arrayList, ServiceDirectoryList.this).execute();
        else Toast.makeText(ServiceDirectoryList.this, "No internet connection", Toast.LENGTH_SHORT).show();
    }


    public class WebConnectionAsync extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(ServiceDirectoryList.this, R.style.TBProgressBar);

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
            if (result != null && result != "") {
                getresult(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }
        }

        private void getresult(String val) {
            try {
                JSONObject jsonObj = new JSONObject(val);
                JSONObject ServiceDirectoryResult = jsonObj.getJSONObject("TBServiceDirectoryResult");
                final String status = ServiceDirectoryResult.getString("status");
                if (status.equals("0")) {
                    serviceDirectoryListDatas = new ArrayList<ServiceDirectoryListData>();
                    JSONArray result = ServiceDirectoryResult.getJSONArray("ServiceDirectoryResult");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject object = result.getJSONObject(i);
                        JSONObject objects = object.getJSONObject("ServiceDirResult");

                        String serviceDirId = objects.getString("serviceDirId").toString();
                        String groupId = objects.getString("groupId").toString();
                        String memberName = objects.getString("memberName");
                        String image  = objects.getString("image");
                        String contactNo = objects.getString("contactNo");
                        String isdeleted = objects.getString("isdeleted");
                        String description = objects.getString("descriptn");
                        String contactNo2 = objects.getString("contactNo2");
                        String pax = objects.getString("pax_no");
                        String email = objects.getString("email");
                        String address = objects.getString("address");
                        String lat = objects.getString("latitude");
                        String lng = objects.getString("longitude");

                        String city = objects.getString("city");
                        String state = objects.getString("state");
                        String country = objects.getString("addressCountry");
                        String zip = objects.getString("zipCode");
                        String moduleId = objects.getString("moduleId");

                        int countryId1 = -1;
                        int countryId2 = -1;

                        try {
                            countryId1 = Integer.parseInt(objects.getString("countryId1"));
                            Log.e("Country ID 1", "Country id 1 is : "+countryId1);
                        } catch(NumberFormatException npe) {
                            Log.e("NoCountryCode1", "Country code1 not found");
                        }

                        try {
                            countryId2 = Integer.parseInt(objects.getString("countryId2"));
                            Log.e("Country ID 2", "Country id 2 is : "+countryId2);
                        } catch(NumberFormatException npe) {
                            Log.e("NoCountryCode2", "Country code2 not found");
                        }

                        String csv = objects.getString("keywords");
                        int categoryId = Integer.parseInt(objects.getString("categoryId"));
                        String website = objects.getString("website");
                        ServiceDirectoryListData gd = new ServiceDirectoryListData(serviceDirId, groupId, memberName, image, contactNo, isdeleted, description, contactNo2, pax, email, address,lat,lng, countryId1, countryId2, csv,city,state,country,zip, moduleId,categoryId,website);

                        serviceDirectoryListDatas.add(gd);

                    }

                    serviceDirectoryListAdapter = new ServiceDirectoryListAdapter(ServiceDirectoryList.this, R.layout.service_directory_list_item1, serviceDirectoryListDatas,"0");

                    Log.e("-------","-------------"+serviceDirectoryListDatas);
                    if ( serviceDirectoryListDatas.isEmpty()) {
                        listview.setEmptyView(findViewById(R.id.tv_no_records_found));
                    } else {
                        listview.setAdapter(serviceDirectoryListAdapter);
                    }

                    updatedOn = ServiceDirectoryResult.getString("updatedOn");
                    serviceDirectoryDataHandler.sendEmptyMessageDelayed(0, 1000);
                }
                else {
                    serviceDirectoryListDatas.clear();
                    serviceDirectoryListAdapter = new ServiceDirectoryListAdapter(ServiceDirectoryList.this, R.layout.service_directory_list_item1, serviceDirectoryListDatas,"0");
                    listview.setAdapter(serviceDirectoryListAdapter);
                    //serviceDirectoryDataHandler.sendEmptyMessageDelayed(0, 1000);
                    //serviceDirectoryListAdapter.notifyDataSetChanged();
                }

            } catch (Exception e) {
                Log.d("exec", "Exception :- " + e.toString());
            }
        }
    }

    //-----------------------Offline Database ----------------------
    public void loadFromDB() {

        Log.d("Touchbase", "Trying to load from local db");

        //directoryData.clear();

        // directoryData = directoryDataModel.getGroups(masterUid);

        serviceDirectoryListDatas = new ArrayList<ServiceDirectoryListData>();
        serviceDirectoryListDatas = serviceDirectoryDataModel.getServiceDirectoryData(masterUid, grpId, moduleId,cattegory_id);

        //Log.e("touchbase list", "**************"+grplist.toString());
        boolean isDataAvailable = serviceDirectoryDataModel.isDataAvailable(grpId, moduleId);
        Log.e("DataAvailable", "Data available : " + isDataAvailable);

        if (!isDataAvailable) {
            Log.d("Touchbase", "Loading from server");
            if (InternetConnection.checkConnection(getApplicationContext()))
                loadFromServer();
            else
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
        } else {

            //Log.d("Touchbase", "Loaded from local db. Number of objects found : "+serviceDirectoryListDatas.size());
            serviceDirectoryListAdapter = new ServiceDirectoryListAdapter(ServiceDirectoryList.this, R.layout.service_directory_list_item1, serviceDirectoryListDatas, "0");
            if ( serviceDirectoryListDatas.isEmpty() ){
                listview.setEmptyView(findViewById(R.id.tv_no_records_found));
            } else {
                listview.setAdapter(serviceDirectoryListAdapter);
            }
            Log.e("-----","----@@@@@@@@@@@@@@@@@@@@@@@@@---------"+serviceDirectoryListDatas);

            // If data is loaded from local database then check for update
            Log.d("=====================","Check for update gets called------");
            checkForUpdate();
            Log.d("---------------","Check for update gets called------");
        }
    }

    public void loadFromServer() {
        checkForUpdate();
        //webservices_getdata();
    }

    Handler serviceDirectoryDataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("DBError", "Handler is called");
            boolean saved = serviceDirectoryDataModel.insert(masterUid, serviceDirectoryListDatas);
            if (!saved) {
                Log.d("Touchbase", "Failed to save offlline. Retrying in 2 seconds");
                sendEmptyMessageDelayed(0, 2000);
            } else {

                Log.d("Touchbase", "SAVED offlline.........................");
                PreferenceManager pm = new PreferenceManager();
                pm.savePreference(getApplicationContext(), "ServiceDirectoryUpdatedOn_"+moduleId+"_"+grpId, updatedOn);

                //System.out.println(savePreference())
               /* SyncInfoModel syncModel = new SyncInfoModel(getContext());
                boolean updated = syncModel.update(masterUid, DateHelper.getCurrentDate());
                if ( !updated ) syncModel.insert(masterUid, DateHelper.getCurrentDate());
              */

                Log.d("-----------", "----Directory data Offline----");
                //syncModel.update(masterUid, Utils.)
            }
        }
    };


    //========================== changes by lekha for updating data(Sync) ===============

    public void checkForUpdate() {
        if (InternetConnection.checkConnection(getApplicationContext())) {
            Log.e("Touchbase", "------ Checking for update");
            String url = Constant.GetServiceDirectoryListSync;
            ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();

            //arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
            arrayList.add(new BasicNameValuePair("groupId", String.valueOf(grpId)));


            //arrayList.add(new BasicNameValuePair("searchText", et_serach_directory.getText().toString()));
            //   arrayList.add(new BasicNameValuePair("page", ""));
            updatedOn = PreferenceManager.getPreference(this, "ServiceDirectoryUpdatedOn_"+moduleId+"_"+grpId,"1970/01/01 00:00:00");
            //updatedOn="2017/03/14 15:34:27";

            Log.e("UpdatedOn", "Last updated date is : "+updatedOn);
            Log.e("MasterUID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID));

            arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));
            //arrayList.add(new BasicNameValuePair("moduleId", moduleId));
            arrayList.add(new BasicNameValuePair("moduleId", moduleId));
            //arrayList.add(new BasicNameValuePair("updatedOn", "2016/1/18 17:8:34"));

            Log.e("request", arrayList.toString());
            UpdateServiceDirectoryAsyncTask task = new UpdateServiceDirectoryAsyncTask(url, arrayList, getApplicationContext());
            task.execute();
            Log.d("Response", "PARAMETERS " + Constant.GetServiceDirectoryListSync + " :- " + arrayList.toString());
            //new WebConnectionAsyncDirectory(Constant.GetDirectoryListSync, arrayList, Directory.this).execute();

        } else {
            Log.e("SyncFailed", "No internet connection to sync data");
        }
    }

    public class UpdateServiceDirectoryAsyncTask extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(ServiceDirectoryList.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;


        public UpdateServiceDirectoryAsyncTask(String url, List<NameValuePair> argList, Context ctx) {
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

                getUpdatedServiceDirectoryItems(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }
    }

    public void getUpdatedServiceDirectoryItems(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject DirectoryResult = jsonObj.getJSONObject("TBServiceDirectoryResult");

            final String status = DirectoryResult.getString("status");
            newDirectoryData = new ArrayList<>();
            if (status.equals("0")) {
                // final ArrayList<DirectoryData> newDirectoryData = new ArrayList<DirectoryData>();
                JSONArray newDirectoryListResult = DirectoryResult.getJSONObject("Result").getJSONArray("newMembers");
                int newCount = newDirectoryListResult.length();


                for (int i = 0; i < newCount; i++) {
                    // JSONObject object = newDirectoryListResult.getJSONObject(i);
                    //JSONObject objects = object.getJSONObject("MemberListResult");

                   // ServiceDirectoryListData data = new ServiceDirectoryListData();

                    JSONObject result_object = newDirectoryListResult.getJSONObject(i);

                    String serviceDirId = result_object.getString("serviceDirId").toString();
                    String groupId = result_object.getString("groupId").toString();
                    String memberName = result_object.getString("memberName");
                    String image  = result_object.getString("image");
                    String contactNo = result_object.getString("contactNo");
                    String isdeleted = result_object.getString("isdeleted");
                    String description = result_object.getString("descriptn");
                    String contactNo2 = result_object.getString("contactNo2");
                    String pax = result_object.getString("pax_no");
                    String email = result_object.getString("email");
                    String address = result_object.getString("address");
                    String lat = result_object.getString("latitude");
                    String lng = result_object.getString("longitude");

                    String city = result_object.getString("city");
                    String state = result_object.getString("state");
                    String country = result_object.getString("addressCountry");
                    String zip = result_object.getString("zipCode");

                    int countryId1 = -1;
                    int countryId2 = -1;

                    try {
                        countryId1 = Integer.parseInt(result_object.getString("countryId1"));
                        Log.e("Country ID 1", "Country id 1 is : "+countryId1);
                    } catch(NumberFormatException npe) {
                        Log.e("NoCountryCode1", "Country code1 not found");
                    }

                    try {
                        countryId2 = Integer.parseInt(result_object.getString("countryId2"));
                        Log.e("Country ID 2", "Country id 2 is : "+countryId2);
                    } catch(NumberFormatException npe) {
                        Log.e("NoCountryCode2", "Country code2 not found");
                    }

                    String csv = result_object.getString("keywords");
                    Log.e("CSV" , "Value of csv : "+csv);

                    String moduleId = result_object.getString("moduleId");
                    int categoryId = Integer.parseInt(result_object.getString("categoryId"));
                    String website = result_object.getString("website");
                    ServiceDirectoryListData data = new ServiceDirectoryListData(serviceDirId, groupId, memberName, image, contactNo, isdeleted, description, contactNo2, pax, email, address,lat,lng, countryId1, countryId2, csv,city,state,country,zip, moduleId,categoryId,website);

                    data.setServiceDirId(result_object.getString("serviceDirId").toString());
                    data.setGroupId(result_object.getString("groupId").toString());
                    data.setMemberName(result_object.getString("memberName").toString());
                    data.setImage(result_object.getString("image").toString());
                    data.setContactNo(result_object.getString("contactNo").toString());
                    //  data.setPic("https://rockyourcareer.files.wordpress.com/2012/11/george_blomgren_med-pic.jpg");
                    data.setIsdeleted(result_object.getString("isdeleted").toString());

                    newDirectoryData.add(data);
                    //directoryData.add(data);
                }
                final ArrayList<ServiceDirectoryListData> updatedServiceDirectoryData = new ArrayList<ServiceDirectoryListData>();
                JSONArray updatedServiceDirectoryListResult = DirectoryResult.getJSONObject("Result").getJSONArray("updatedMembers");
                int updateCount = updatedServiceDirectoryListResult.length();
                for (int i = 0; i < updateCount; i++) {
                    /*JSONObject object = updatedServiceDirectoryListResult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("MemberListResult");*/

                  //  ServiceDirectoryListData data = new ServiceDirectoryListData();
                    JSONObject updated_object = updatedServiceDirectoryListResult.getJSONObject(i);

                    String serviceDirId = updated_object.getString("serviceDirId").toString();
                    String groupId = updated_object.getString("groupId").toString();
                    String memberName = updated_object.getString("memberName");
                    String image  = updated_object.getString("image");
                    String contactNo = updated_object.getString("contactNo");
                    String isdeleted = updated_object.getString("isdeleted");
                    String description = updated_object.getString("descriptn");
                    String contactNo2 = updated_object.getString("contactNo2");
                    String pax = updated_object.getString("pax_no");
                    String email = updated_object.getString("email");
                    String address = updated_object.getString("address");
                    String lat = updated_object.getString("latitude");
                    String lng = updated_object.getString("longitude");

                    String city = updated_object.getString("city");
                    String state = updated_object.getString("state");
                    String country = updated_object.getString("addressCountry");
                    String zip = updated_object.getString("zipCode");

                    int countryId1 = -1;
                    int countryId2 = -1;

                    try {
                        countryId1 = Integer.parseInt(updated_object.getString("countryId1"));
                        Log.e("Country ID 1", "Country id 1 is : "+countryId1);
                    } catch(NumberFormatException npe) {
                        Log.e("NoCountryCode1", "Country code1 not found");
                    }

                    try {
                        countryId2 = Integer.parseInt(updated_object.getString("countryId2"));
                        Log.e("Country ID 2", "Country id 2 is : "+countryId2);
                    } catch(NumberFormatException npe) {
                        Log.e("NoCountryCode2", "Country code2 not found");
                    }

                    String csv = updated_object.getString("keywords");
                    //String csv = "abc, xyz";
                    String moduleId = updated_object.getString("moduleId");
                    int categoryId = Integer.parseInt(updated_object.getString("categoryId"));
                    String website = updated_object.getString("website");
                    ServiceDirectoryListData data = new ServiceDirectoryListData(serviceDirId, groupId, memberName, image, contactNo, isdeleted, description, contactNo2, pax, email, address,lat,lng, countryId1, countryId2, csv,city,state,country,zip, moduleId,categoryId,website);

                    data.setServiceDirId(updated_object.getString("serviceDirId").toString());
                    data.setGroupId(updated_object.getString("groupId").toString());
                    data.setMemberName(updated_object.getString("memberName").toString());
                    data.setImage(updated_object.getString("image").toString());
                    data.setContactNo(updated_object.getString("contactNo").toString());
                    //  data.setPic("https://rockyourcareer.files.wordpress.com/2012/11/george_blomgren_med-pic.jpg");
                    data.setIsdeleted(updated_object.getString("isdeleted").toString());

                    updatedServiceDirectoryData.add(data);
                }

                final ArrayList<ServiceDirectoryListData> deletedServiceDirectoryData = new ArrayList<ServiceDirectoryListData>();
                JSONArray deletedServiceDirectoryListResult = DirectoryResult.getJSONObject("Result").getJSONArray("deletedMembers");
                int deleteCount = deletedServiceDirectoryListResult.length();
                for (int i = 0; i < deleteCount; i++) {
                /*    JSONObject object = deletedServiceDirectoryListResult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("MemberListResult");*/

                  //  ServiceDirectoryListData data = new ServiceDirectoryListData();
                    JSONObject deleted_object = deletedServiceDirectoryListResult.getJSONObject(i);

                    String serviceDirId = deleted_object.getString("serviceDirId").toString();
                    String groupId = deleted_object.getString("groupId").toString();
                    String memberName = deleted_object.getString("memberName");
                    String image  = deleted_object.getString("image");
                    String contactNo = deleted_object.getString("contactNo");
                    String isdeleted = deleted_object.getString("isdeleted");
                    String description = deleted_object.getString("descriptn");
                    String contactNo2 = deleted_object.getString("contactNo2");
                    String pax = deleted_object.getString("pax_no");
                    String email = deleted_object.getString("email");
                    String address = deleted_object.getString("address");
                    String lat = deleted_object.getString("latitude");
                    String lng = deleted_object.getString("longitude");

                    String city = deleted_object.getString("city");
                    String state = deleted_object.getString("state");
                    String country = deleted_object.getString("addressCountry");
                    String zip = deleted_object.getString("zipCode");


                    int countryId1 = -1;
                    int countryId2 = -1;

                    try {
                        countryId1 = Integer.parseInt(deleted_object.getString("countryId1"));
                    } catch(NumberFormatException npe) {
                        Log.e("NoCountryCode1", "Country code1 not found");
                    }

                    try {
                        countryId2 = Integer.parseInt(deleted_object.getString("countryId2"));
                    } catch(NumberFormatException npe) {
                        Log.e("NoCountryCode2", "Country code2 not found");
                    }
                    String csv = deleted_object.getString("keywords");
                    //String csv = "abc, xyz";
                    String moduleId = deleted_object.getString("moduleId");
                    int categoryId = Integer.parseInt(deleted_object.getString("categoryId"));
                    String website = deleted_object.getString("website");
                    ServiceDirectoryListData data = new ServiceDirectoryListData(serviceDirId, groupId, memberName, image, contactNo, isdeleted, description, contactNo2, pax, email, address,lat,lng, countryId1, countryId2, csv,city,state,country,zip, moduleId,categoryId,website);


                    data.setServiceDirId(deleted_object.getString("serviceDirId").toString());
                    data.setGroupId(deleted_object.getString("groupId").toString());
                    data.setMemberName(deleted_object.getString("memberName").toString());
                    data.setImage(deleted_object.getString("image").toString());
                    data.setContactNo(deleted_object.getString("contactNo").toString());
                    //  data.setPic("https://rockyourcareer.files.wordpress.com/2012/11/george_blomgren_med-pic.jpg");
                    data.setIsdeleted(deleted_object.getString("isdeleted").toString());

                    deletedServiceDirectoryData.add(data);
                }

                Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Log.e("♦♦♦♦♦", "Upading in local db");
                        boolean saved = serviceDirectoryDataModel.syncData(masterUid, grpId, newDirectoryData, updatedServiceDirectoryData, deletedServiceDirectoryData);
                        if ( ! saved ) {
                            Log.e("SyncFailed------->", "Failed to update data in local db. Retrying in 2 seconds");
                            sendEmptyMessageDelayed(0, 2000);
                        } else {
                            // updating last updated date in shared preferences.
                            PreferenceManager.savePreference(getApplicationContext(), "ServiceDirectoryUpdatedOn_"+moduleId+"_"+grpId, updatedOn);
                            // Reloading all data for display purpose.
                            serviceDirectoryListDatas = serviceDirectoryDataModel.getServiceDirectoryData(masterUid, grpId, moduleId,cattegory_id);
                            serviceDirectoryListAdapter = new ServiceDirectoryListAdapter(ServiceDirectoryList.this, R.layout.service_directory_list_item1, serviceDirectoryListDatas, "0");
                            //directoryAdapter.notifyDataSetChanged();
                            listview.setVisibility(View.VISIBLE);
                            listview.setAdapter(serviceDirectoryListAdapter);

                            Log.d("-----------","----Updated data------"+serviceDirectoryListAdapter);
                        }
                    }
                };
                int overAllCount = newCount + updateCount + deleteCount;
                System.out.println("Number of records for update are : "+overAllCount);
                if ( newCount + updateCount + deleteCount != 0) {
                    updatedOn = DirectoryResult.getString("updatedOn");
                    handler.sendEmptyMessageDelayed(0, 1000);
                } else {
                    Log.e("NoUpdate", "No updates found");
                }
            }
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
            e.printStackTrace();
        }


    }


    //========================= onActivityResult ========================================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(data!=null && data.getStringExtra("deleted").equals("yes"))
        {
            checkForUpdate();
        }else {
            serviceDirectoryListDatas = new ArrayList<ServiceDirectoryListData>();
            serviceDirectoryListDatas = serviceDirectoryDataModel.getServiceDirectoryData(masterUid, grpId, moduleId,cattegory_id);
            serviceDirectoryListAdapter = new ServiceDirectoryListAdapter(ServiceDirectoryList.this, R.layout.service_directory_list_item1, serviceDirectoryListDatas, "0");
            listview.setAdapter(serviceDirectoryListAdapter);
        }
        // check if the request code is same as what is passed  here it is 2
        /*if (requestCode == 1) {
            //start of code updation pp
            if ( ! InternetConnection.checkConnection(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
                return;
            }

            // end of code uupdation pp
           // serviceDirectoryListDatas.clear();
          //  webservices_getdata();
            checkForUpdate();

            //String message=data.getStringExtra("MESSAGE");
            // textView1.setText(message);
        }*/


        /*if (requestCode == VIEW_SERVICE_DIR_DETAIL) {
            if (InternetConnection.checkConnection(getApplicationContext())) {
                checkForUpdate();
               // webservices_getdata();
            } else {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
            // webservices();
        }*/

    }

    static final int VIEW_SERVICE_DIR_DETAIL = 1;

}