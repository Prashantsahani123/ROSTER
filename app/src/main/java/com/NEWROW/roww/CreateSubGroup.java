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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.MemberSelectionAdapter;
import com.NEWROW.row.Data.SubGroupMemberData;
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
 * Created by user on 10-02-2016.
 */
public class CreateSubGroup extends Activity {

    ListView listview;
    String result = "";
    TextView tv_title, tv_create;
    ImageView iv_backbutton;
    EditText et_subgrpname,et_search;
    ArrayList<String> n = new ArrayList<String>();
    String profile_id_string = "";

 //   private ArrayList<CreateSubGroupListData> list_createsubgroup = new ArrayList<CreateSubGroupListData>();
  //  private CreateSubGroupAdapter adapter_createsubgroup;
    private ArrayList<SubGroupMemberData> list_directorylist = new ArrayList<SubGroupMemberData>();
    private MemberSelectionAdapter adapter_memberlsit;
    Context context;
    String subgroupid = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_subgroup);
        context = this;
        listview = (ListView) findViewById(R.id.listView);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        // iv_backbutton.setVisibility(View.GONE);
        tv_title.setText("Create Sub Group");

        tv_create = (TextView) findViewById(R.id.tv_create);
        et_subgrpname = (EditText) findViewById(R.id.et_subgrpname);
        et_search = (EditText)findViewById(R.id.et_serach);
        try {
            subgroupid = getIntent().getExtras().getString("subgrpId", "0");
        } catch(NullPointerException npe) {
            npe.printStackTrace();
        }
        //adapter_createsubgroup = new CreateSubGroupAdapter(this, R.layout.create_subgroup_list_item, list_createsubgroup);

        //webservices();
        if (InternetConnection.checkConnection(getApplicationContext())) {
            // Avaliable
            webservices();
        } else {
            Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
            // Not Available...
        }
        init();
    }

    public void init() {

        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                //adapter.getFilter().filter(cs);
                //adapter.getFilter().filter(cs.toString());
                //Toast.makeText(context, "We are inside search", Toast.LENGTH_LONG).show();
                int textlength = cs.length();
                ArrayList<SubGroupMemberData> tempArrayList = new ArrayList<SubGroupMemberData>();
                for (SubGroupMemberData c : list_directorylist) {
                    if (textlength <= c.getMemberName().length()) {
                        if (c.getMemberName().toLowerCase().contains(cs.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                //Data_array= tempArrayList;
                //   DirectoryAdapter adapter = new DirectoryAdapter(Directory.this, tempArrayList);

               // CreateSubGroupAdapter adapter = new CreateSubGroupAdapter(CreateSubGroup.this,  tempArrayList);
              //  listview.setTextFilterEnabled(true);
              //  listview.setAdapter(adapter);
                //adapter_memberlsit = new MemberSelectionAdapter(CreateSubGroup.this,R.layout.subgroup_member_list_item, datas,tempArrayList);
                adapter_memberlsit = new MemberSelectionAdapter(CreateSubGroup.this, R.layout.subgroup_member_list_item, tempArrayList, "0");
                listview.setTextFilterEnabled(true);
                listview.setAdapter(adapter_memberlsit);
                Log.e("InsideSearch", "Inside search : "+list_directorylist);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  list_directorylist.clear();
                //webservices();
            }
        });

        tv_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_subgrpname.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter a Sub Group Name",Toast.LENGTH_LONG).show();
                } else {


                    int count = 0;

                    for (SubGroupMemberData p : adapter_memberlsit.getBox()) {
                        if (p.box) {


                            result += p.getProfileID();
                            count = count + 1;
                            // n.add(p.profileId);

                        }
                        n.add(p.getProfileID());
                    }

                    //  Log.d("--------","comma ...."+n);

                    for (int i = 0; i < n.size(); i++) {
                        //commaSepValueBuilder.append(n.get(i));
                        profile_id_string = profile_id_string + n.get(i);

                        if (i != n.size() - 1) {

                            // commaSepValueBuilder.append(", ");
                            profile_id_string = profile_id_string + ", ";
                        }

                    }

                    Log.d("--------", "comma ...." + profile_id_string);

                    //createSubGroupwebservices();
                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        // Avaliable
                        Log.d("--------", "comma ...." + profile_id_string.trim().length());
                        if(profile_id_string.trim().length() > 0) {
                            createSubGroupwebservices();
                        }else {
                            Utils.showToastWithTitleAndContext(getApplicationContext(), "Please Select atleast one member");
                        }
                    } else {
                        Utils.showToastWithTitleAndContext(getApplicationContext(), " No Internet Connection!");
                        // Not Available...
                    }

                    profile_id_string = "";

                }

            }
        });

        //createSubGroupwebservices();
    }

    public void finishActivity(View v) {
        finish();
    }

    /* old web service
    private void webservices() {
        //{"masterUID":"1","grpID":"","searchText":"","page":""}
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("masterUID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));
        arrayList.add(new BasicNameValuePair("grpID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("searchText", ""));
        arrayList.add(new BasicNameValuePair("page", ""));
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();

        Log.d("Response", "PARAMETERS " + Constant.GetDirectoryList + " :- " + arrayList.toString());
        if ( InternetConnection.checkConnection(getApplicationContext())) {
            new WebConnectionAsyncDirectory(Constant.GetDirectoryList, arrayList, CreateSubGroup.this).execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void webservices() {
        Log.e("webservice", "Starting of webservice");
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("subgrpId", subgroupid));

        Log.d("Response", "PARAMETERS " + Constant.GetSubGroupDetail + " :- " + arrayList.toString());
        if (InternetConnection.checkConnection(context)) {
            new WebConnectionAsyncDirectory(Constant.GetSubGroupDetail, arrayList, context).execute();
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
        Log.e("webservice", "end of web service");
    }
    /********************* Member List Web Service**************************/

    public class WebConnectionAsyncDirectory extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(CreateSubGroup.this, R.style.TBProgressBar);
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
            	Log.d("response","Do post"+ result.toString());

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
            JSONObject DirectoryResult = jsonObj.getJSONObject("TBGetSubGroupDetailListResult");
            final String status = DirectoryResult.getString("status");
            if (status.equals("0")) {
                JSONArray DirectoryListResdult = DirectoryResult.getJSONArray("SubGroupResult");

                Log.d("@@@@@@@@@", "@@@@@@@@@@" + DirectoryListResdult.length());

                for (int i = 0; i < DirectoryListResdult.length(); i++) {
                    JSONObject object = DirectoryListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("SubgrpMemberDetail");


                    String profileId = objects.getString("profileId").toString();
                    String memberName = objects.getString("memname").toString();
                    String mobileNo = objects.getString("mobile").toString();
                    SubGroupMemberData data = new SubGroupMemberData(profileId,memberName, mobileNo, false);

                    list_directorylist.add(data);


                }
                Log.e("Members", list_directorylist.toString());
                adapter_memberlsit = new MemberSelectionAdapter(CreateSubGroup.this,R.layout.subgroup_member_list_item, list_directorylist, "0");
                listview.setAdapter(adapter_memberlsit);
            }


        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    /******************************************** Create Group **************************/

    private void createSubGroupwebservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("subGroupTitle", et_subgrpname.getText().toString()));
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("memberProfileId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)));
        arrayList.add(new BasicNameValuePair("memberIds", profile_id_string));
        arrayList.add(new BasicNameValuePair("parentID", subgroupid));

        //arrayList.add(new BasicNameValuePair("memberMainId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));


        Log.d("Response", "PARAMETERS " + Constant.CreateSubGroup + " :- " + arrayList.toString());
        new WebConnectionAsyncCreateSubGroup(Constant.CreateSubGroup, arrayList, CreateSubGroup.this).execute();
    }

    public class WebConnectionAsyncCreateSubGroup extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(CreateSubGroup.this, R.style.TBProgressBar);

        public WebConnectionAsyncCreateSubGroup(String url, List<NameValuePair> argList, Context ctx) {
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
            //Log.d("response","Do post"+ result.toString());
            if (result != "") {
                getsubgroupresult(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getsubgroupresult(String val) {
        try {
            JSONObject jsonObj = new JSONObject(val);
            JSONObject ActivityResult = jsonObj.getJSONObject("TBGetSubGroupListResult");
            final String status = ActivityResult.getString("status");
            if (status.equals("0")) {
                String msg = ActivityResult.getString("message");
                Toast.makeText(getApplicationContext(), et_subgrpname.getText().toString()+" created successfully!",Toast.LENGTH_SHORT).show();

                // OnActivityResult
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }

}
