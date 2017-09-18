package com.SampleApp.row;

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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.SampleApp.row.Adapter.AnnouncementListAdapter;
import com.SampleApp.row.Data.AnnouncementListData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;

import static com.SampleApp.row.Adapter.AnnouncementListAdapter.count_read_announcements;
import static com.SampleApp.row.Adapter.DocumentAdapter.count_read_documents;
import static com.SampleApp.row.AddAnnouncement.count_write_announcement;

/**
 * Created by USER on 21-12-2015.
 */
public class Announcement extends Activity {

    ListView listview;
    ArrayAdapter<String> adapter;
    TextView tv_title;
    ImageView iv_backbutton;
    EditText et_serach_announcement;
    Spinner spinner_filter_type;
    String filtertype[] = {"All", "Published", "UnPublished", "Expired"};
    String filtertype_notadmin[] = {"All", "Published", "Expired"};
    String type_filter_flag = "0";

    public static String moduleName = "";
    private ArrayList<AnnouncementListData> list_announcmentdata = new ArrayList<AnnouncementListData>();
    private AnnouncementListAdapter announcementListAdapter;
    private ImageView iv_actionbtn;
    private String grpID = "0";
    String moduleId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement);

        listview = (ListView) findViewById(R.id.listView);
        //tv_title = (TextView) findViewById(R.id.tv_title);
        //iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        // iv_backbutton.setVisibility(View.GONE);
        //tv_title.setText("Announcement");

        et_serach_announcement = (EditText) findViewById(R.id.et_serach_announcement);
        spinner_filter_type = (Spinner) findViewById(R.id.spinner_filter_type);

        moduleId = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MODULE_ID);

        //Spinner DropDown
        if (PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN).equals("No")) {
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, filtertype_notadmin);
            spinner_filter_type.setAdapter(spinnerArrayAdapter);
            spinner_filter_type.setVisibility(View.GONE);
        } else {
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, filtertype);
            spinner_filter_type.setAdapter(spinnerArrayAdapter);
        }

        Intent intenti = getIntent();
        grpID = intenti.getStringExtra("GroupID");

        // webservices();
        // adapter = new ArrayAdapter<String>(this, R.layout.announcement_list_item, R.id.tv_name, ebulletine);
        // listview.setAdapter(adapter);
        actionbarfunction();
        init();
        checkadminrights();
        // condition is compared here because if user is not admin so spinner is not visible and websevie method is never called for him. So to call webservice.
        // below code is written.
        if (PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN).equals("No")) {
            if (InternetConnection.checkConnection(getApplicationContext())) {
                // Avaliable
                webservices();
            } else {
                Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                // Not Available...
            }
        }

    }


    @Override
    public void onBackPressed() {
        try{
            int tempCount = FragmentALL.notificationCountDatas.getModuleCount(grpID, moduleId);
            if ( tempCount >= count_read_documents ) {
                int unreadCount = tempCount - count_read_announcements;

                if(Documents_upload.count_write_documents>0) {
                    unreadCount = unreadCount + count_write_announcement;
                }
                FragmentALL.notificationCountDatas.updateModuleCount(grpID, moduleId, ""+unreadCount);
            } else {
                FragmentALL.notificationCountDatas.updateModuleCount(grpID, moduleId, "0");
            }
            /*for(int i =0;i<notificationCountDatas.size();i++)
            {
                if(notificationCountDatas.get(i).getGroupId().equalsIgnoreCase(""+grpID))
                {
                    int tempCount = Integer.parseInt(notificationCountDatas.get(i).getId3());

                    if(tempCount>=count_read_announcements)
                    {
                        int unreadCount = tempCount - count_read_announcements;

                        if(count_write_announcement>0)
                        {
                            unreadCount = unreadCount+count_write_announcement;
                        }

                        notificationCountDatas.get(i).setId3(""+unreadCount);
                    }
                    else {
                        notificationCountDatas.get(i).setId3(""+0);
                    }
                }
            }*/
            count_read_announcements=0;
            count_write_announcement=0;
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("GroupID",""+grpID);
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

    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        String title = getIntent().getExtras().getString("moduleName", "Announcement");
        moduleName = getIntent().getExtras().getString("moduleName", "Announcement");

        tv_title.setText(title);
        iv_actionbtn.setVisibility(View.VISIBLE);

        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Announcement.this, AddAnnouncement.class);

                startActivityForResult(i, 1);
            }
        });

        /*iv_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });*/
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
                ArrayList<AnnouncementListData> tempArrayList = new ArrayList<AnnouncementListData>();
                for (AnnouncementListData c : list_announcmentdata) {
                    if (textlength <= c.getAnnounTitle().length()) {
                        if (c.getAnnounTitle().toLowerCase().contains(cs.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                //Data_array = tempArrayList;
                //DirectoryAdapter adapter = new DirectoryAdapter(Directory.this, tempArrayList);
                AnnouncementListAdapter adapter = new AnnouncementListAdapter(Announcement.this, R.layout.announcement_list_item, tempArrayList);
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
        if (PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN).equals("No")) {
            arrayList.add(new BasicNameValuePair("type", "1"));
        }else{
            arrayList.add(new BasicNameValuePair("type", type_filter_flag));
        }
        //arrayList.add(new BasicNameValuePair("type", type_filter_flag));
        arrayList.add(new BasicNameValuePair("moduleId", moduleId));

        if (PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN).equals("No")) {
            arrayList.add(new BasicNameValuePair("isAdmin", "0"));

        } else {
            arrayList.add(new BasicNameValuePair("isAdmin", "1"));
        }


        Log.d("Response", "PARAMETERS " + Constant.GetAnnouncementList + " :- " + arrayList.toString());
        new WebConnectionAsyncAnnouncement(Constant.GetAnnouncementList, arrayList, Announcement.this).execute();
    }


    public class WebConnectionAsyncAnnouncement extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(Announcement.this, R.style.TBProgressBar);
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
            JSONObject AnnouncementResult = jsonObj.getJSONObject("TBAnnounceListResult");
            final String status = AnnouncementResult.getString("status");

            if (AnnouncementResult.has("smscount")) {
                Utils.smsCount = AnnouncementResult.getString("smscount");
            }

            if (status.equals("0")) {
                JSONArray AnnouncementListResdult = AnnouncementResult.getJSONArray("AnnounListResult");
                for (int i = 0; i < AnnouncementListResdult.length(); i++) {
                    JSONObject object = AnnouncementListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("AnnounceList");

                    AnnouncementListData data = new AnnouncementListData();

                    data.setAnnounID(objects.getString("announID").toString());
                    data.setAnnounTitle(objects.getString("announTitle").toString());
                    data.setAnnounceDEsc(objects.getString("announceDEsc").toString());
                    data.setCreateDateTime(objects.getString("createDateTime").toString());
                    data.setPublishDateTime(objects.getString("publishDateTime").toString());
                    data.setExpiryDateTime(objects.getString("expiryDateTime").toString());
                    data.setIsAdmin(objects.getString("isAdmin").toString());
                    data.setIsRead(objects.getString("isRead").toString());
                    data.setFilterType(objects.getString("filterType").toString());


                    list_announcmentdata.add(data);

                }
                announcementListAdapter = new AnnouncementListAdapter(Announcement.this, R.layout.announcement_list_item, list_announcmentdata);
                listview.setAdapter(announcementListAdapter);

            } else {
                list_announcmentdata.clear();
                announcementListAdapter = new AnnouncementListAdapter(Announcement.this, R.layout.announcement_list_item, list_announcmentdata);
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
