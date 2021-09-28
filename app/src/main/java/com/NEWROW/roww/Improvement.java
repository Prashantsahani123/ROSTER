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

import com.NEWROW.row.Adapter.ImprovementsListAdapter;
import com.NEWROW.row.Data.ImprovementListData;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 21-12-2015.
 */
public class Improvement extends Activity {

    ListView listview;
    ArrayAdapter<String> adapter;
    TextView tv_title;
    ImageView iv_backbutton;
    EditText et_serach_announcement;
    Spinner spinner_filter_type;
    String filtertype[] = {"All", "Published", "UnPublished", "Expired"};
    String filtertype_notadmin[] = {"All", "Published", "Expired"};
    String type_filter_flag = "0";


    private ArrayList<ImprovementListData> list_announcmentdata = new ArrayList<ImprovementListData>();
    private ImprovementsListAdapter announcementListAdapter;
    private ImageView iv_actionbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.improvement);

        listview = (ListView) findViewById(R.id.listView);
        //tv_title = (TextView) findViewById(R.id.tv_title);
        //iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        // iv_backbutton.setVisibility(View.GONE);
        //tv_title.setText("Announcement");

        et_serach_announcement = (EditText) findViewById(R.id.et_serach_announcement);
        spinner_filter_type = (Spinner) findViewById(R.id.spinner_filter_type);

        //Spinner DropDown
        if (PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN).equals("No")) {
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, filtertype_notadmin);
            spinner_filter_type.setAdapter(spinnerArrayAdapter);
        } else {
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, filtertype);
            spinner_filter_type.setAdapter(spinnerArrayAdapter);
        }

        // webservices();
        // adapter = new ArrayAdapter<String>(this, R.layout.announcement_list_item, R.id.tv_name, ebulletine);
        // listview.setAdapter(adapter);
        actionbarfunction();
        init();
        checkadminrights();

    }

    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);

        String title = getIntent().getExtras().getString("moduleName", "Improvements");
        tv_title.setText(title);
        iv_actionbtn.setVisibility(View.VISIBLE);

        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Improvement.this, AddImprovement.class);
                startActivityForResult(i, 1);
            }
        });

    }

    private void checkadminrights() {
        if (PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN).equals("No")) {
            iv_actionbtn.setVisibility(View.GONE);
        }
    }

    public void init() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*    Intent i = new Intent(Announcement.this, Announcement_details.class);
                i.putExtra("announcemet_id",list_announcmentdata.get(position).getAnnounID());
                startActivity(i);*/
            }
        });


        et_serach_announcement.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                int textlength = cs.length();
                ArrayList<ImprovementListData> tempArrayList = new ArrayList<ImprovementListData>();
                for (ImprovementListData c : list_announcmentdata) {
                    if (textlength <= c.getImprovementTitle().length()) {
                        if (c.getImprovementTitle().toLowerCase().contains(cs.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                //Data_array= tempArrayList;
                //   DirectoryAdapter adapter = new DirectoryAdapter(Directory.this, tempArrayList);

                ImprovementsListAdapter adapter = new ImprovementsListAdapter(Improvement.this, R.layout.improvement_list_item, tempArrayList);
                listview.setTextFilterEnabled(true);
                listview.setAdapter(adapter);


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


        et_serach_announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_announcmentdata.clear();
                //webservices();
                if (InternetConnection.checkConnection(getApplicationContext())) {
                    // Avaliable
                    webservices();
                } else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }
            }
        });

        spinner_filter_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TouchBase", "@@@@@@@@@@@@@ " + spinner_filter_type.getSelectedItem().toString());
//{"All", "Published", "UnPublished", "Expired"};
                if (spinner_filter_type.getSelectedItem().toString().equals("All")) {
                    type_filter_flag = "0";
                } else if (spinner_filter_type.getSelectedItem().toString().equals("Published")) {
                    type_filter_flag = "1";
                } else if (spinner_filter_type.getSelectedItem().toString().equals("UnPublished")) {
                    type_filter_flag = "2";
                } else if (spinner_filter_type.getSelectedItem().toString().equals("Expired")) {
                    type_filter_flag = "3";
                }
                //webservices();
                if (InternetConnection.checkConnection(getApplicationContext())) {
                    // Avaliable
                    webservices();
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


    public void finishActivity(View v) {
        finish();
    }


    private void webservices() {
//       {"memberProfileId":"1","groupId":"1","searchText":"","type":"1","isAdmin":"0"}
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("memberProfileId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("searchText", et_serach_announcement.getText().toString()));
        arrayList.add(new BasicNameValuePair("type", type_filter_flag));

        if (PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN).equals("No")) {
            arrayList.add(new BasicNameValuePair("isAdmin", "0"));

        } else {
            arrayList.add(new BasicNameValuePair("isAdmin", "1"));
        }


        Log.d("Response", "PARAMETERS " + Constant.GetImprovementList + " :- " + arrayList.toString());
        new WebConnectionAsyncAnnouncement(Constant.GetImprovementList, arrayList, Improvement.this).execute();
    }


    public class WebConnectionAsyncAnnouncement extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(Improvement.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;


        public WebConnectionAsyncAnnouncement(String url, List<NameValuePair> argList, Context ctx) {
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
                list_announcmentdata.clear();
                getAnnouncementItems(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getAnnouncementItems(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject AnnouncementResult = jsonObj.getJSONObject("TBImprovementListResult");
            final String status = AnnouncementResult.getString("status");

            if (AnnouncementResult.has("smscount")) {
                Utils.smsCount = AnnouncementResult.getString("smscount");
            }

            if (status.equals("0")) {
                JSONArray AnnouncementListResdult = AnnouncementResult.getJSONArray("ImprListResult");
                for (int i = 0; i < AnnouncementListResdult.length(); i++) {
                    JSONObject object = AnnouncementListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("ImprovementList");

                    ImprovementListData data = new ImprovementListData();

                    data.setImprovementID(objects.getString("improvementID").toString());
                    data.setImprovementTitle(objects.getString("improvementTitle").toString());
                    data.setImprovementDesc(objects.getString("improvementDesc").toString());
                    data.setCreateDateTime(objects.getString("createDateTime").toString());
                    data.setPublishDateTime(objects.getString("publishDateTime").toString());
                    data.setExpiryDateTime(objects.getString("expiryDateTime").toString());
                    data.setIsAdmin(objects.getString("isAdmin").toString());
                    data.setIsRead(objects.getString("isRead").toString());
                    data.setFilterType(objects.getString("filterType").toString());


                    list_announcmentdata.add(data);

                }
                announcementListAdapter = new ImprovementsListAdapter(Improvement.this, R.layout.improvement_list_item, list_announcmentdata);
                listview.setAdapter(announcementListAdapter);

            } else {
                list_announcmentdata.clear();
                announcementListAdapter = new ImprovementsListAdapter(Improvement.this, R.layout.improvement_list_item, list_announcmentdata);
                listview.setAdapter(announcementListAdapter);
            }

            listview.setEmptyView(((TextView) findViewById(R.id.tv_no_records_found)));

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 1) {
            //webservices();

            if (InternetConnection.checkConnection(getApplicationContext())) {
                // Avaliable
                webservices();
            } else {
                Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                // Not Available...
            }
            //String message=data.getStringExtra("MESSAGE");
            // textView1.setText(message);
        }
    }


}
