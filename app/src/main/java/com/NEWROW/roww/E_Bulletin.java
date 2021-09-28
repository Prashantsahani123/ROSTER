package com.NEWROW.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.E_BulletineAdapter;
import com.NEWROW.row.Data.E_BulletineListData;
import com.NEWROW.row.NotificationDataBase.DatabaseHelper;
import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.Utils;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.NEWROW.row.Adapter.E_BulletineAdapter.count_read_ebulletines;
import static com.NEWROW.row.AddE_bulletin.count_write_eBulletin;
import static com.NEWROW.row.Documents_upload.count_write_documents;
import static com.NEWROW.row.Utils.PreferenceManager.isRIadminModule;

/**
 * Created by USER on 21-12-2015.
 */
public class E_Bulletin extends Activity {
    String moduleName = "";
    ListView listview;
    ArrayAdapter<String> adapter;
    TextView tv_title;
    ImageView iv_backbutton, iv_actionbtn;
    EditText et_serach_ebulletin;
    private ArrayList<E_BulletineListData> list_ebulletineData = new ArrayList<E_BulletineListData>();
    private E_BulletineAdapter adapter_ebuletine;
    private Spinner spinner_filter_type;
    ArrayList<String> filtertype ;
    String filtertype_notadmin[] = {"Published","All",  "Expired"};
    String type_filter_flag = "0";
    private String grpID = "0";
    private String memberProfileID = "0";
    private String isAdmin = "No";
    private String moduleId;
    private String fromYear,toYear;
    private ProgressDialog progressDialog;
    private String messageId_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.e_bulletin);

        try {


            listview = (ListView) findViewById(R.id.listView);
            tv_title = (TextView) findViewById(R.id.tv_title);
            iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
            // iv_backbutton.setVisibility(View.GONE);

            moduleName = getIntent().getExtras().getString("moduleName", "Newsletters");

            tv_title.setText(moduleName);

            et_serach_ebulletin = (EditText) findViewById(R.id.et_serach_ebulletin);
            iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
            iv_actionbtn.setVisibility(View.VISIBLE);

            spinner_filter_type = (Spinner) findViewById(R.id.spinner_filter_type);

            //-------------------------------
            grpID = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID);
            memberProfileID = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID);
            isAdmin = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN);

            Log.d("Touchbase", "ID ID ID :- " + grpID + " - " + memberProfileID);
            Bundle intent = getIntent().getExtras();

            Intent intenti = getIntent();

            if (intenti.hasExtra("memID")) {
                memberProfileID = intenti.getStringExtra("memID");
                grpID = intenti.getStringExtra("grpID");
                isAdmin = intenti.getStringExtra("isAdmin");

                //Extra filed added by Gaurav for Notification Read

                //Update Records notification count

                //update Data into Database
                messageId_temp = intenti.getStringExtra("messageId");
                if (messageId_temp != null) {
                    //Create Database Helper Class Object
                    DatabaseHelper databaseHelpers = new DatabaseHelper(this);

                    boolean notificationInsert = databaseHelpers.updateData(messageId_temp);
                    Log.d("messageId_temp", "messageID ID ID AFTER :- " + messageId_temp);
                    Log.d("messageId_temp", "Is Data Updated :- " + notificationInsert);


                }


            }

            Log.d("Touchbase", "ID ID ID AFTER :- " + grpID + " - " + memberProfileID);
            //-------------------------------

        /*iv_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });*/
            //Spinner DropDown

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
            spinner_filter_type.setAdapter(spinnerArrayAdapter);


            moduleId = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MODULE_ID);
            //webservices();
            init();
            checkadminrights();

        }
        catch (Exception e)
        {

        }
    }

    private void checkadminrights() {
        if (isAdmin.equals("No")) {
            iv_actionbtn.setVisibility(View.GONE);
        }
        if (PreferenceManager.getPreference(getApplicationContext(), isRIadminModule).equals("Yes")){
            iv_actionbtn.setVisibility(View.GONE);
        }
    }

    private void getNewsletter(){

        try {

            JSONObject requestData = new JSONObject();
            requestData.put("fromYear", fromYear);
            requestData.put("toYear",toYear);
            requestData.put("memberProfileId",PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID));
           // requestData.put("memberProfileId",memberProfileID);
            requestData.put("groupId",grpID);

          //  Utils.log(""+requestData);
            Utils.log("URL : "+Constant.GetYearWiseEbulletinList+" PARAMETERS : "+requestData);

            progressDialog = new ProgressDialog(E_Bulletin.this,R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.GetYearWiseEbulletinList, requestData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();

                    setNewsletter(response);
                    Utils.log(response.toString());

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Utils.log("VollyError:- " + error);
                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(
                    60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(E_Bulletin.this, request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNewsletter(JSONObject response){

        try {

            JSONObject ebulletinJson=response.getJSONObject("TBYearWiseEbulletinList");

            String status =  ebulletinJson.getString("status");

            if(status.equalsIgnoreCase("0")){

                ArrayList<E_BulletineListData> listData = new ArrayList<>();

                JSONArray result=ebulletinJson.getJSONArray("Result");

                for(int i=0;i<result.length();i++){

                    JSONObject objects = result.getJSONObject(i);

                    E_BulletineListData data = new E_BulletineListData();

                    data.setEbulletinID(objects.getString("ebulletinID").toString());
                    data.setEbulletinTitle(objects.getString("ebulletinTitle").toString());
                    data.setEbulletinlink(objects.getString("ebulletinlink").toString());
                    data.setEbulletinType(objects.getString("ebulletinType").toString());
                    data.setFilterType(objects.getString("filterType").toString());
                    data.setCreateDateTime(objects.getString("createDateTime").toString());
                    data.setPublishDateTime(objects.getString("publishDateTime").toString());
                    data.setExpiryDateTime(objects.getString("expiryDateTime").toString());
//                    data.setEbulletinlink(objects.getString("ebulletinlink").toString());
                    data.setIsAdmin(objects.getString("isAdmin").toString());
                    data.setIsRead(objects.getString("isRead").toString());
                    listData.add(data);
                }

                adapter_ebuletine = new E_BulletineAdapter(E_Bulletin.this, R.layout.e_bulletin_list_item, listData);
                listview.setAdapter(adapter_ebuletine);
                listview.setEmptyView(((TextView) findViewById(R.id.tv_no_records_found)));

            } else {
                Utils.showToastWithTitleAndContext(E_Bulletin.this,getString(R.string.msgRetry));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {


            if (PreferenceManager.getPreference(E_Bulletin.this, PreferenceManager.IS_GRP_ADMIN).equals("No")) {

                if (InternetConnection.checkConnection(E_Bulletin.this)) {
                    // Avaliable
                    // webservices();
                } else {
                    Utils.showToastWithTitleAndContext(E_Bulletin.this, "No Internet Connection!");
                    // Not Available...
                }
            }
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void onBackPressed() {

        try {

            int tempCount = FragmentALL.notificationCountDatas.getModuleCount(grpID, moduleId);

            if (tempCount >= count_read_ebulletines) {
                int unreadCount = tempCount - count_read_ebulletines;

                if (count_write_documents > 0) {
                    unreadCount = unreadCount + count_write_eBulletin;
                }

                FragmentALL.notificationCountDatas.updateModuleCount(grpID, moduleId, "" + unreadCount);

            } else {
                FragmentALL.notificationCountDatas.updateModuleCount(grpID, moduleId, "0");
            }

        /*for(int i =0;i<notificationCountDatas.size();i++)
        {
            if(notificationCountDatas.get(i).getGroupId().equalsIgnoreCase(""+grpID))
            {
                int tempCount = Integer.parseInt(notificationCountDatas.get(i).getId4());


                if(tempCount>=count_read_ebulletines)
                {
                    int unreadCount = tempCount - count_read_ebulletines;

                    if(count_write_eBulletin>0)
                    {
                        unreadCount = unreadCount+count_write_eBulletin;
                    }

                    notificationCountDatas.get(i).setId4(""+unreadCount);
                }
                else {
                    notificationCountDatas.get(i).setId4(""+0);
                }
            }
        }*/
            count_read_ebulletines = 0;
            count_write_eBulletin = 0;
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("GroupID", "" + grpID);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        } catch(Exception e) {
            Log.e("TouchBase", "♦♦♦♦Error is : "+e.getMessage());
            e.printStackTrace();
            finish();
            super.onBackPressed();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(adapter_ebuletine!=null)
        adapter_ebuletine.notifyDataSetChanged();
    }

    public void init() {

        iv_actionbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(E_Bulletin.this, AddE_bulletin.class);
                i.putExtra("moduleName",moduleName);
                startActivityForResult(i, 2);
            }
        });

        et_serach_ebulletin.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

//                int textlength = cs.length();
//                ArrayList<E_BulletineListData> tempArrayList = new ArrayList<E_BulletineListData>();
//
//                for (E_BulletineListData c : list_ebulletineData) {

//                    if (textlength <= c.getEbulletinTitle().length()) {

//                        if (c.getEbulletinTitle().toLowerCase().contains(cs.toString().toLowerCase())) {
//
//                            tempArrayList.add(c);
//                        }
//                    }
//                }
//                //Data_array= tempArrayList;
//                //   DirectoryAdapter adapter = new DirectoryAdapter(Directory.this, tempArrayList);
//
//                E_BulletineAdapter adapter = new E_BulletineAdapter(E_Bulletin.this, R.layout.e_bulletin_list_item, tempArrayList);
//
//
//                listview.setTextFilterEnabled(true);
//                listview.setAdapter(adapter);
                String text = et_serach_ebulletin.getText().toString().toLowerCase();

                if(adapter_ebuletine!=null) {
                    adapter_ebuletine.filter(text);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

//        et_serach_ebulletin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    if ( ! InternetConnection.checkConnection(getApplicationContext())) {
//                        Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
//                        return true;
//                    }
//                    //webservices();
//                    return true;
//                }
//                return false;
//            }
//        });

        spinner_filter_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("TouchBase", "@@@@@@@@@@@@@ " + spinner_filter_type.getSelectedItem().toString());
//                //{"All", "Published", "UnPublished", "Expired"};
//                if (spinner_filter_type.getSelectedItem().toString().equals("All")) {
//                    type_filter_flag = "0";
//                } else if (spinner_filter_type.getSelectedItem().toString().equals("Published")) {
//                    type_filter_flag = "1";
//                } else if (spinner_filter_type.getSelectedItem().toString().equals("UnPublished")) {
//                    type_filter_flag = "2";
//                } else if (spinner_filter_type.getSelectedItem().toString().equals("Expired")) {
//                    type_filter_flag = "3";
//                }

                String selectedYear=spinner_filter_type.getSelectedItem().toString();
                String array[]= selectedYear.split("-");

                fromYear= array[0];
                toYear= array[1];

                Utils.log(fromYear+" "+toYear);

                if (InternetConnection.checkConnection(getApplicationContext())) {
                    // Avaliable
//                    webservices();
                    if (PreferenceManager.getPreference(getApplicationContext(), isRIadminModule).equals("Yes")){
                        getNewsletterRotaryIndia();

                    }else{
                        getNewsletter();

                    }

                } else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getNewsletterRotaryIndia() {

        try {

            JSONObject requestData = new JSONObject();
            requestData.put("fromYear", fromYear);
            requestData.put("toYear",toYear);
            requestData.put("memberProfileId",PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID));
           // requestData.put("memberProfileId",memberProfileID);
            requestData.put("groupId",grpID);

            //  Utils.log(""+requestData);
            Utils.log("URL : "+Constant.RotaryIndiaNewsletterlist+" PARAMETERS : "+requestData);

            progressDialog = new ProgressDialog(E_Bulletin.this,R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.RotaryIndiaNewsletterlist, requestData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();

                    setNewsletter(response);
                    Utils.log(response.toString());

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Utils.log("VollyError:- " + error);
                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(
                    60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(E_Bulletin.this, request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void finishActivity(View v) {
        finish();
    }


    private void webservices() {
        //{"masterUID":"1","grpID":"","searchText":"","page":""}

        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("memberProfileId", memberProfileID));
        arrayList.add(new BasicNameValuePair("groupId", grpID));
        arrayList.add(new BasicNameValuePair("searchText", et_serach_ebulletin.getText().toString()));
      //  arrayList.add(new BasicNameValuePair("type", "1"));


       // arrayList.add(new BasicNameValuePair("type", type_filter_flag));
        // arrayList.add(new BasicNameValuePair("isAdmin", "0"));
        if (isAdmin.equals("No")) {
            arrayList.add(new BasicNameValuePair("isAdmin", "0"));

        } else {
            arrayList.add(new BasicNameValuePair("isAdmin", "1"));
        }
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();

        Log.d("Response", "PARAMETERS " + Constant.GetEbulletinList + " :- " + arrayList.toString());
        if  (InternetConnection.checkConnection(getApplicationContext())) {
            new WebConnectionAsyncDirectory(Constant.GetEbulletinList, arrayList, E_Bulletin.this).execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }



    public class WebConnectionAsyncDirectory extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(E_Bulletin.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;


        public WebConnectionAsyncDirectory(String url, List<NameValuePair> argList, Context ctx) {
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
            //progressDialog.setMessage(Constant.LOADING_MESSAGE);
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

                getEbulletineItems(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getEbulletineItems(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject DirectoryResult = jsonObj.getJSONObject("TBEbulletinListResult");
            final String status = DirectoryResult.getString("status");

            if (DirectoryResult.has("smscount")) {
                Utils.smsCount = DirectoryResult.getString("smscount");
            }

            if (status.equals("0")) {
                JSONArray DirectoryListResdult = DirectoryResult.getJSONArray("EbulletinListResult");

                Log.d("@@@@@@@@@", "@@@@@@@@@@" + DirectoryListResdult.length());
                list_ebulletineData.clear();
                for (int i = 0; i < DirectoryListResdult.length(); i++) {
                    JSONObject object = DirectoryListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("EbulletinList");

                    E_BulletineListData data = new E_BulletineListData();

                    data.setEbulletinID(objects.getString("ebulletinID").toString());
                    data.setEbulletinTitle(objects.getString("ebulletinTitle").toString());
                    data.setEbulletinlink(objects.getString("ebulletinlink").toString());
                    data.setEbulletinType(objects.getString("ebulletinType").toString());
                    data.setFilterType(objects.getString("filterType").toString());
                    data.setCreateDateTime(objects.getString("createDateTime").toString());
                    data.setPublishDateTime(objects.getString("publishDateTime").toString());
                    data.setExpiryDateTime(objects.getString("expiryDateTime").toString());
                    data.setEbulletinlink(objects.getString("ebulletinlink").toString());
                    data.setIsAdmin(objects.getString("isAdmin").toString());
                    data.setIsRead(objects.getString("isRead").toString());

                    list_ebulletineData.add(data);
                }
                adapter_ebuletine = new E_BulletineAdapter(E_Bulletin.this, R.layout.e_bulletin_list_item, list_ebulletineData);
                listview.setAdapter(adapter_ebuletine);
                listview.setEmptyView(((TextView) findViewById(R.id.tv_no_records_found)));
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2) {
            if ( ! InternetConnection.checkConnection(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                return;
            }else {

                if (PreferenceManager.getPreference(getApplicationContext(), isRIadminModule).equals("Yes")){
                    getNewsletterRotaryIndia();

                }else{
                    getNewsletter();

                }
            }
           // webservices();

        }
    }
}
