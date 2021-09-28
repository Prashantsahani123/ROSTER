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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.DirectoryAdapter;
import com.NEWROW.row.Data.DirectoryData;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.NEWROW.row.Utils.PreferenceManager.IS_GRP_ADMIN;
import static com.NEWROW.row.Utils.PreferenceManager.savePreference;

/**
 * Created by USER on 22-12-2015.
 */
public class Global_Search extends Activity {

    ListView listview;

    private ArrayList<DirectoryData> directoryData = new ArrayList<DirectoryData>();
    private DirectoryAdapter directoryAdapter;
    ImageView iv_backbutton;
    EditText et_serach;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_search);

        listview = (ListView) findViewById(R.id.listView);
        et_serach = (EditText) findViewById(R.id.et_serach);


        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);

        savePreference(Global_Search.this, IS_GRP_ADMIN, "No");
        // adapter = new ArrayAdapter<String>(this,R.layout.select_country_item,R.id.tv_lang,announcements );
        //listview.setAdapter(adapter);

        et_serach.requestFocus();
        init();
        //  webservices();
    }

    private void init() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Integer.parseInt(directoryData.get(position).getGrpCount()) > 1) {
                    Intent i = new Intent(Global_Search.this, GroupProfileResult.class);
                    i.putExtra("clickedmemberid", directoryData.get(position).getMasterUID());
                    startActivity(i);
                }else{
                    Intent i = new Intent(Global_Search.this, Profile.class);
                    i.putExtra("memberprofileid",  directoryData.get(position).getProfileID());
                    i.putExtra("groupId",  directoryData.get(position).getGrpID());
                    startActivity(i);
                }

            }
        });
        et_serach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("TOuchBAse", "BEfore");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("TOuchBAse", "ONNN");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("TOuchBAse", "After");
                if (s.length() > 3)
                    //directoryData.clear();
                    webservices();
            }
        });
        et_serach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                  webservices();
                    return true;
                }
                return false;
            }
        });
        iv_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    if (getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                }
                finish();
            }
        });
    }

    private void webservices() {
        //{"masterUID":"1","grpID":"","searchText":"","page":""}
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("masterUID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));
        arrayList.add(new BasicNameValuePair("grpID", "")); // To serach Globally
        arrayList.add(new BasicNameValuePair("searchText", et_serach.getText().toString()));
        arrayList.add(new BasicNameValuePair("page", ""));
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();

        Log.d("Response", "PARAMETERS " + Constant.GetDirectoryList + " :- " + arrayList.toString());
        if (InternetConnection.checkConnection(getApplicationContext())) {
            new WebConnectionAsyncDirectory(Constant.GetDirectoryList, arrayList, Global_Search.this).execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public class WebConnectionAsyncDirectory extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(Global_Search.this, R.style.TBProgressBar);
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
                directoryData.clear();
                getDirectoryItems(result.toString());
            } else {
                Log.d("Response", "Null Resposnse");
            }
        }
    }

    private void getDirectoryItems(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject DirectoryResult = jsonObj.getJSONObject("TBMemberResult");
            final String status = DirectoryResult.getString("status");
            if (status.equals("0")) {
                JSONArray DirectoryListResdult = DirectoryResult.getJSONArray("MemberListResults");

                //   Log.d("@@@@@@@@@", "@@@@@@@@@@" + DirectoryListResdult.length());

                for (int i = 0; i < DirectoryListResdult.length(); i++) {
                    JSONObject object = DirectoryListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("MemberListResult");

                    DirectoryData data = new DirectoryData();

                    data.setMasterUID(objects.getString("masterUID").toString());
                    data.setGrpID(objects.getString("grpID").toString());
                    data.setProfileID(objects.getString("profileID").toString());
                    data.setGroupName(objects.getString("groupName").toString());
                    data.setMemberName(objects.getString("memberName").toString());
                    data.setPic(objects.getString("pic").toString());
                    data.setMembermobile(objects.getString("membermobile").toString());
                    data.setGrpCount(objects.getString("grpCount").toString());

                   directoryData.add(data);
                }
                directoryAdapter = new DirectoryAdapter(Global_Search.this, R.layout.directory_list_item, directoryData, "1");
                listview.setAdapter(directoryAdapter);
            }else {
                directoryData.clear();
                directoryAdapter = new DirectoryAdapter(Global_Search.this, R.layout.directory_list_item, directoryData, "1");
                listview.setAdapter(directoryAdapter);
                TextView empty = ((TextView) findViewById(R.id.tv_no_records_found));
                empty.setText("No records found");
                listview.setEmptyView(empty);
            }
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

}