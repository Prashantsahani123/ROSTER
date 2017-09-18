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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.EventListAdapter;
import com.SampleApp.row.Data.EventListData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.SampleApp.row.Adapter.EventListAdapter.count_read_events;
import static com.SampleApp.row.AddEvent.count_write_events;
import static com.SampleApp.row.Documents_upload.count_write_documents;

/**
 * Created by USER on 17-12-2015.
 */
public class Events extends Activity {
    HashSet<String> admins = new HashSet<String>();

    ListView listview;
    ArrayAdapter<String> adapter;
    TextView tv_title;
    ImageView iv_backbutton, iv_actionbtn;
    RelativeLayout relative_actionbar;
    EditText et_search;
    String filtertype[] = {"All", "Published", "UnPublished", "Expired"};
    String filtertype_notadmin[] = {"All", "Published", "Expired"};
    Spinner spinner_filter_type;
    String moduleName = "";
    private ArrayList<EventListData> eventListDatas = new ArrayList<EventListData>();
    private EventListAdapter eventListAdapter;
    String type_filter_flag = "0";
    private String grpID = "0";

    String moduleId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);

        listview = (ListView) findViewById(R.id.listView);

        et_search = (EditText) findViewById(R.id.et_serach);

        actionbarfunction();
        //webservices();
        spinner_filter_type = (Spinner) findViewById(R.id.spinner_filter_type);
        if(PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN).equals("No")){
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, filtertype_notadmin);
            spinner_filter_type.setAdapter(spinnerArrayAdapter);
            spinner_filter_type.setVisibility(View.GONE);
        } else {
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, filtertype);
            spinner_filter_type.setAdapter(spinnerArrayAdapter);
        }
        /*iv_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        Intent intenti = getIntent();
        grpID = intenti.getStringExtra("GroupID");
        moduleId = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MODULE_ID);
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
        Log.e("Touchbase", "♦♦♦♦Inside OnBackPress");
        try {
            int tempCount = FragmentALL.notificationCountDatas.getModuleCount(grpID, moduleId);
            if (tempCount >= count_read_events) {
                int unreadCount = tempCount - count_read_events;

                if (count_write_documents > 0) {
                    unreadCount = unreadCount + count_write_events;
                }

                FragmentALL.notificationCountDatas.updateModuleCount(grpID, moduleId, "" + unreadCount);
            } else {
                FragmentALL.notificationCountDatas.updateModuleCount(grpID, moduleId, "0");
            }

        /*for(int i =0;i<notificationCountDatas.size();i++)
        {
            if(notificationCountDatas.get(i).getGroupId().equalsIgnoreCase(""+grpID))
            {
                int tempCount = Integer.parseInt(notificationCountDatas.get(i).getId2());


                if(tempCount>=count_read_events)
                {
                    int unreadCount = tempCount - count_read_events;

                    if(count_write_events>0)
                    {
                        unreadCount = unreadCount+count_write_events;
                    }
                    notificationCountDatas.get(i).setId2(""+unreadCount);
                }
                else
                {
                    notificationCountDatas.get(i).setId2(""+0);
                }
            }
        }*/
            count_read_events = 0;
            count_write_events = 0;
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("GroupID", "" + grpID);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
            super.onBackPressed();
        } catch(Exception e) {
            Log.e("TouchBase", "♦♦♦♦Error is : "+e.getMessage());
            e.printStackTrace();
            finish();
            super.onBackPressed();
        }
    }

    private void checkadminrights() {
        if( PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN).equals("No")){
            iv_actionbtn.setVisibility(View.GONE);
        }
    }

    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        moduleName = PreferenceManager.getPreference(this, PreferenceManager.MODUEL_NAME, "Events");
        tv_title.setText(moduleName);
        iv_actionbtn.setVisibility(View.VISIBLE);

        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i = new Intent(Events.this, AddEvent.class);
            startActivityForResult(i, 1);
            }
        });

    }


    public void init() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Intent i = new Intent(Events.this, EventDetails.class);
                i.putExtra("eventid", eventListDatas.get(position).getEventID());
                startActivity(i);*/
            }
        });

        /*iv_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/


        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                //adapter.getFilter().filter(cs);
                //adapter.getFilter().filter(cs.toString());

                int textlength = cs.length();
                ArrayList<EventListData> tempArrayList = new ArrayList<EventListData>();
                for (EventListData c : eventListDatas) {
                    if (textlength <= c.getEventTitle().length()) {
                        if (c.getEventTitle().toLowerCase().contains(cs.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                //Data_array= tempArrayList;
                //DirectoryAdapter adapter = new DirectoryAdapter(Directory.this, tempArrayList);

                EventListAdapter adapter = new EventListAdapter(Events.this, R.layout.events_list_item, tempArrayList);
                listview.setTextFilterEnabled(true);
                listview.setAdapter(adapter);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

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
                if (InternetConnection.checkConnection(getApplicationContext())) {
                    //Available
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

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    webservices();
                    return true;
                }
                return false;
            }
        });

    }


    public void finishActivity(View v) {
        finish();
    }


    private void webservices() {
        //{"groupProfileID":"43","grpId":"74","Type":"0","Admin":"0","searchText":""}
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("groupProfileID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));
        arrayList.add(new BasicNameValuePair("grpId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));

        if (PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN).equals("No")) {
            arrayList.add(new BasicNameValuePair("Type", "1"));
        }else{
            arrayList.add(new BasicNameValuePair("Type", type_filter_flag));
        }
       //arrayList.add(new BasicNameValuePair("Type", type_filter_flag));
        if( PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN).equals("No")){
            arrayList.add(new BasicNameValuePair("Admin","0"));
        } else {
            arrayList.add(new BasicNameValuePair("Admin","1"));
        }
        arrayList.add(new BasicNameValuePair("searchText", et_search.getText().toString()));


        et_search.setText("");
        Log.d("Response", "PARAMETERS " + Constant.GetEventList + " :- " + arrayList.toString());
        if ( InternetConnection.checkConnection(getApplicationContext())) {
            new WebConnectionAsyncDirectory(Constant.GetEventList, arrayList, Events.this).execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    public class WebConnectionAsyncDirectory extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(Events.this, R.style.TBProgressBar);
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
                eventListDatas.clear();
                getEventItems(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getEventItems(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("EventListDetailResult");
            final String status = EventResult.getString("status");


            if (EventResult.has("SMSCount")) {
                Utils.smsCount = EventResult.getString("SMSCount");
            }

            if (status.equals("0")) {
                JSONArray EventListResdult = EventResult.getJSONArray("EventsListResult");
                for (int i = 0; i < EventListResdult.length(); i++) {
                    JSONObject object = EventListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("EventList");

                    EventListData data = new EventListData();

                    data.setEventID(objects.getString("eventID").toString());
                    if (objects.has("eventImg")) {
                        data.setEventImg(objects.getString("eventImg").toString());
                    } else {
                        data.setEventImg("");
                    }
                    data.setEventTitle(objects.getString("eventTitle").toString());
                    data.setEventDateTime(objects.getString("eventDateTime").toString());
                    data.setGoingCount(objects.getString("goingCount").toString());
                    data.setMaybeCount(objects.getString("maybeCount").toString());
                    data.setNotgoingCount(objects.getString("notgoingCount").toString());
                    data.setVenue(objects.getString("venue").toString());
                    data.setMyResponse(objects.getString("myResponse").toString());
                    data.setGrpID(objects.getString("grpID").toString());
                    data.setFilterType(objects.getString("filterType").toString());
                    data.setGrpAdminId(objects.getString("grpAdminId").toString());
                    data.setIsRead(objects.getString("isRead").toString());
                    data.setVenueLat(objects.getString("venueLat").toString());
                    data.setVenueLon(objects.getString("venueLon").toString());
                    eventListDatas.add(data);
                }

                eventListAdapter = new EventListAdapter(Events.this, R.layout.events_list_item, eventListDatas);
                listview.setAdapter(eventListAdapter);

            } else {
                eventListDatas.clear();
                eventListAdapter = new EventListAdapter(Events.this, R.layout.events_list_item, eventListDatas);
                listview.setAdapter(eventListAdapter);
            }

            listview.setEmptyView(((TextView) findViewById(R.id.tv_no_records_found)));
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
            eventListDatas.clear();
            eventListAdapter = new EventListAdapter(Events.this, R.layout.events_list_item, eventListDatas);
            listview.setAdapter(eventListAdapter);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 1) {
            //start of code updation pp
            if ( ! InternetConnection.checkConnection(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
                return;
            }

            // end of code uupdation pp
            webservices();
            //String message=data.getStringExtra("MESSAGE");
            // textView1.setText(message);
        }
    }

}
