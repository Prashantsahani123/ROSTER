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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.MemberListAdapter;
import com.NEWROW.row.Data.DirectoryData;
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


public class AddMembers extends Activity {

    MemberListAdapter boxAdapter;
    TextView ib_continue;
    ArrayAdapter<String> adapter;
    EditText et_serach_directory;
    TextView tv_title;
    ImageView iv_backbutton;
    ListView lvMain;
    String edit_announcement_selectedids = "0";
    ArrayList<DirectoryData> directorylist = new ArrayList<>();
    String isAdmin = "";
    boolean isAddCreatorByDefault=false;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_members);

        ib_continue = (TextView) findViewById(R.id.ib_continue);
        lvMain = (ListView) findViewById(R.id.listView);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        et_serach_directory = (EditText) findViewById(R.id.et_serach_directory);

        tv_title.setText("Add Contacts");
        isAdmin = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN);
        // boxAdapter = new MemberListAdapter(this, directorylist);
        // lvMain.setAdapter(boxAdapter);
        //  lvMain.setTextFilterEnabled(true);

        Bundle bundelintent = getIntent().getExtras();
        Intent intenti = getIntent();

        if (bundelintent != null) {

            if(intenti.hasExtra("selected_memberdata")) {
                directorylist = intenti.getExtras().getParcelableArrayList("selected_memberdata");
                boxAdapter = new MemberListAdapter(this, directorylist);
                lvMain.setAdapter(boxAdapter);
                lvMain.setTextFilterEnabled(true);
            }

            if (intenti.hasExtra("edit_announcement_selectedids")) {

                if (intenti.getStringExtra("edit_announcement_selectedids").equals("0")) {

                    directorylist = intenti.getExtras().getParcelableArrayList("selected_memberdata");
                    boxAdapter = new MemberListAdapter(this, directorylist);
                    lvMain.setAdapter(boxAdapter);
                    lvMain.setTextFilterEnabled(true);

                } else {

                    edit_announcement_selectedids = intenti.getStringExtra("edit_announcement_selectedids");
                    //webservices();
                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        // Avaliable
                        webservices();
                    } else {
                        Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                        // Not Available...
                    }
                }

            }

            if(bundelintent.keySet().size()==1 && intenti.hasExtra("isAddCreatorByDefault")){

                isAddCreatorByDefault = intenti.getBooleanExtra("isAddCreatorByDefault",false);

                if (InternetConnection.checkConnection(getApplicationContext())) {
                    // Avaliable
                    webservices();
                } else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }
            }


        } else {
            //fillData();
           // webservices();
            if (InternetConnection.checkConnection(getApplicationContext())) {
                // Avaliable
                webservices();
            } else {
                Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                // Not Available...
            }
        }





        init();
    }

    private void init() {

        et_serach_directory.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                //adapter.getFilter().filter(cs);
                //adapter.getFilter().filter(cs.toString());

                int textlength = cs.length();
                ArrayList<DirectoryData> tempArrayList = new ArrayList<DirectoryData>();
                for (DirectoryData c : directorylist) {
                    if (textlength <= c.getMemberName().length()) {
                        if (c.getMemberName().toLowerCase().contains(cs.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                //Data_array= tempArrayList;
                final MemberListAdapter adapter = new MemberListAdapter(AddMembers.this, tempArrayList);

                lvMain.setAdapter(adapter);

                if ( tempArrayList.size() <= 0 ) {
                    lvMain.setEmptyView(findViewById(R.id.tv_no_records_found));
                    adapter.notifyDataSetChanged();
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


        ib_continue.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                String result = "Selected Product are :";
                // int totalAmount = 0;
                int count = 0;
                // Array[] arr = new Array[0];

                if(boxAdapter!=null){
                    for (DirectoryData p : boxAdapter.getBox()) {
                        if (p.box) {
                            result += "\n" + p.getMemberName();
                            count = count + 1;
                        }
                    }
                    //Toast.makeText(this, result+"\n"+"Total Amount:="+totalAmount, Toast.LENGTH_LONG).show();

                    //   Toast.makeText(AddMembers.this, result + "\n" + "Count:=" + count, Toast.LENGTH_LONG).show();
                    Log.d("TOUCHBASE", "@@@@@@@@@--" + count);
                    if (count <= 0) {
                        Toast.makeText(AddMembers.this, "Please Select at least 1 Member ", Toast.LENGTH_LONG).show();
                    } else {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result", directorylist);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }else {
                    Utils.showToastWithTitleAndContext(AddMembers.this,getString(R.string.msg_no_records_found));
                }


            }
        });
    }

    private void webservices() {

        //{"masterUID":"1","grpID":"","searchText":"","page":""}
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("masterUID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));
        arrayList.add(new BasicNameValuePair("grpID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));

        arrayList.add(new BasicNameValuePair("searchText", et_serach_directory.getText().toString()));
        arrayList.add(new BasicNameValuePair("page", ""));

        if ( isAdmin.equalsIgnoreCase("Partial")) {
            String isSubGroupAdmin = "1";
            arrayList.add(new BasicNameValuePair("isSubGrpAdmin", isSubGroupAdmin));
            String profileId = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID);
            arrayList.add(new BasicNameValuePair("profileId", profileId));
        }

        //arrayList.add(new BasicNameValuePair("isSubGrpAdmin", ))
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "PARAMETERS " + Constant.GetDirectoryList + " :- " + arrayList.toString());

        new WebConnectionAsyncDirectory(Constant.GetDirectoryList, arrayList, AddMembers.this).execute();
    }

    public class WebConnectionAsyncDirectory extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(AddMembers.this, R.style.TBProgressBar);
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
                directorylist.clear();



                for (int i = 0; i < DirectoryListResdult.length(); i++) {

                    JSONObject object = DirectoryListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("MemberListResult");

                    DirectoryData data = new DirectoryData();

                    String profileId = objects.getString("profileID");

                    data.setMasterUID(objects.getString("masterUID").toString());
                    data.setGrpID(objects.getString("grpID").toString());
                    data.setProfileID(profileId);
                    data.setGroupName(objects.getString("groupName").toString());
                    data.setMemberName(objects.getString("memberName").toString());
                    data.setPic(objects.getString("pic").toString());
                    data.setMembermobile(objects.getString("membermobile").toString());
                    data.setGrpCount(objects.getString("grpCount").toString());

                    if (edit_announcement_selectedids.equals("0")) {
                        data.setBox(false);
                    } else {
                        //data.setBox(false);
                        String[] parts = edit_announcement_selectedids.split(",");

                        for (String part : parts) {
                            Log.d("TOUCHBASE", "##### :-" + part);
                            if (objects.getString("profileID").toString().equals(part)) {
                                Log.d("TOUCHBASE", "TRUE ");
                                data.setBox(true);
                            } else {
                                Log.d("TOUCHBASE", "FALSE ");
                                // data.setBox(false);
                            }
                        }
                    }

                    if(isAddCreatorByDefault && PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID).equalsIgnoreCase(profileId)){
                        data.setBox(true);
                    }

                    directorylist.add(data);

                }
                // directoryAdapter = new DirectoryAdapter(Directory.this, R.layout.directory_list_item, directorylist);
                // listview.setAdapter(directoryAdapter);

                boxAdapter = new MemberListAdapter(this, directorylist);
                lvMain.setAdapter(boxAdapter);
                lvMain.setTextFilterEnabled(true);
            }


        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }


}