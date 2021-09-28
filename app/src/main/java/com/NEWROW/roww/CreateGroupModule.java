package com.NEWROW.row;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.CreateGroupModuleAdapter;
import com.NEWROW.row.Data.CreateModuleData;
import com.NEWROW.row.Data.UpdateModuleLabels;
import com.NEWROW.row.Data.moduleIDs;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.JSONHttpConnection;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.Utils;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 30-12-2015.
 */
public class CreateGroupModule extends Activity {

    ListView listview;
    ArrayAdapter<String> adapter;


    private ArrayList<CreateModuleData> list_createModule = new ArrayList<CreateModuleData>();
    private CreateGroupModuleAdapter adapter_createModule;

    TextView tv_title, ib_next, tv_suggest_new_feature;
    ImageView iv_backbutton;
    private String selectedmoduleids, createdgrpid;
    String webservicecall_flag;
    EditText et_numberofmembers;
    EditText et_featureTitle,et_featureDescription;
    ArrayList<moduleIDs> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group_module);

        list = new ArrayList<moduleIDs>();
        listview = (ListView) findViewById(R.id.listView);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_backbutton.setVisibility(View.GONE);
        tv_title.setText("Select Modules");
        ib_next = (TextView) findViewById(R.id.ib_next);
        tv_suggest_new_feature = (TextView) findViewById(R.id.tv_suggest_new_feature);
        et_numberofmembers = (EditText) findViewById(R.id.et_numberofmembers);

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            createdgrpid = intent.getString("createdgroupid"); // Created Group ID
        }

//        webservices();
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
      /*  listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(CreateGroupModule.this, Flow.class);
               // startActivity(i);
            }
        });*/
        tv_suggest_new_feature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(CreateGroupModule.this, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_suggestnew_feature);

                TextView tv_submit = (TextView) dialog.findViewById(R.id.tv_submit);
                TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
                et_featureTitle = (EditText) dialog.findViewById(R.id.et_featureTitle);
                et_featureDescription = (EditText) dialog.findViewById(R.id.et_featureDescription);


                tv_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (et_featureTitle.getText().toString().trim().matches("") || et_featureTitle.getText().toString().trim() == null){
                            Toast.makeText(CreateGroupModule.this, "Please Enter Feature Title.", Toast.LENGTH_SHORT).show();
                        }else{
                            if (et_featureDescription.getText().toString().trim().matches("") || et_featureDescription.getText().toString().trim() == null){
                                Toast.makeText(CreateGroupModule.this, "Please Enter Feature Description.", Toast.LENGTH_SHORT).show();
                            }else{
                                webservices_suggestfeature();
                                dialog.dismiss();
                            }
                        }
                    }
                });
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }

        });
        ib_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedmoduleids = "";

                for (CreateModuleData p : adapter_createModule.getBox()) {
                    if (p.box) {

                        /*result += "\n" + p.name+"\t\t"+p.moduleId;
                        count = count + 1;*/

//                        if (selectedmoduleids.equals("")) {
//                            selectedmoduleids = p.getModuleId();
//                        } else {
//                            selectedmoduleids = selectedmoduleids + "," + p.getModuleId();
//                        }

                        moduleIDs moduleId = new moduleIDs(p.getModuleId(),p.getName(),p.getNewName());
                        list.add(moduleId);
                    }
                }

                Log.d("---------", "--------" + selectedmoduleids);
                if (list.size()==0) {
                    Toast.makeText(CreateGroupModule.this, "Please select at least one Module", Toast.LENGTH_LONG).show();
                } else {

                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        // Avaliable
                        webservices_addmoduls();
                    } else {
                        Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                        // Not Available...
                    }
                }

            }
        });
    }


    private void webservices_addmoduls() {
//{"fk_module_master_id":"1","created_by":"1","fk_group_master_id":"1"}
//        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
//
//        arrayList.add(new BasicNameValuePair("moduleIDs", selectedmoduleids));
//        arrayList.add(new BasicNameValuePair("userID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));
//        arrayList.add(new BasicNameValuePair("grpId", createdgrpid));
//        arrayList.add(new BasicNameValuePair("noOfmember", et_numberofmembers.getText().toString()));
        webservicecall_flag = "1";

        String body = "";
        UpdateModuleLabels newModuleLabels = new UpdateModuleLabels(createdgrpid,list,PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID),et_numberofmembers.getText().toString(),"0","");

        Gson gson = new Gson();
        body = gson.toJson(newModuleLabels);
        new PostMessageThread(body).execute();

//        Log.d("Response", "PARAMETERS " + Constant.AddSelectedModule + " :- " + arrayList.toString());
//        new WebConnectionAsyncDirectory(Constant.AddSelectedModule, arrayList, CreateGroupModule.this).execute();
    }

    class PostMessageThread extends AsyncTask<Void, Void, String>
    {
        String body;
        String response=null;
        final ProgressDialog progressDialog = new ProgressDialog(CreateGroupModule.this, R.style.TBProgressBar);

        public PostMessageThread(String body)
        {
            this.body=body;
        }
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();

        }
        @Override
        protected String doInBackground(Void... params)
        {

            JSONHttpConnection connection = null;

            try {
                connection = new JSONHttpConnection();
                response= connection.postRequest(Constant.AddSelectedModule,body);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (result != "") {
                Log.d("Response", "calling addSelectedModule");
                getResult(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }
        }

    }


    private void webservices() {

        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        webservicecall_flag = "0";
        Log.d("Response", "PARAMETERS " + Constant.GetModulesList + " :- " + arrayList.toString());
        new WebConnectionAsyncDirectory(Constant.GetModulesList, arrayList, CreateGroupModule.this).execute();
    }
    private void webservices_suggestfeature() {

        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("title",et_featureTitle.getText().toString() ));
        arrayList.add(new BasicNameValuePair("description", et_featureDescription.getText().toString()));
        arrayList.add(new BasicNameValuePair("profileID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));
        arrayList.add(new BasicNameValuePair("grpId", createdgrpid));//createdgrpid
        webservicecall_flag = "2";
        Log.d("Response", "PARAMETERS " + Constant.SuggestFeature + " :- " + arrayList.toString());
        new WebConnectionAsyncDirectory(Constant.SuggestFeature, arrayList, CreateGroupModule.this).execute();
    }

    public class WebConnectionAsyncDirectory extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(CreateGroupModule.this, R.style.TBProgressBar);
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
                if (webservicecall_flag.equals("0"))
                    getCreateGroupModuleItems(result.toString());
                else if(webservicecall_flag.equals("1")) {
                    getResult(result.toString());
                }else if (webservicecall_flag.equals("2")){
                    getResult_SugestFeature(result.toString());
                }

            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getResult(String result) {

        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject DirectoryResult = jsonObj.getJSONObject("TBAddModuleResult");
            final String status = DirectoryResult.getString("status");
            if (status.equals("0")) {

                Intent i = new Intent(getApplicationContext(), GroupCreated.class);
                i.putExtra("groupid", DirectoryResult.getString("grpID"));
                i.putExtra("groupname", DirectoryResult.getString("grpname"));
                i.putExtra("groupimg", DirectoryResult.getString("grpImg"));
                i.putExtra("trialMsg", DirectoryResult.getString("trialMsg"));

                startActivity(i);
            } else {
                Toast.makeText(CreateGroupModule.this, "Something gone wrong please contact admistrator ", Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }
    private void getResult_SugestFeature(String result) {

        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject DirectoryResult = jsonObj.getJSONObject("SuggestFeatureResult");
            final String status = DirectoryResult.getString("status");
            if (status.equals("0")) {
                Toast.makeText(CreateGroupModule.this, "Your Suggestion sent SUCCESSFULLY!", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(CreateGroupModule.this, "Failed", Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }

    private void getCreateGroupModuleItems(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject DirectoryResult = jsonObj.getJSONObject("TBGetGroupModuleResult");
            final String status = DirectoryResult.getString("status");
            if (status.equals("0")) {
                JSONArray DirectoryListResdult = DirectoryResult.getJSONArray("GroupListResult");


                for (int i = 0; i < DirectoryListResdult.length(); i++) {
                    JSONObject object = DirectoryListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("GroupList");

                    CreateModuleData data = new CreateModuleData();

                    data.setName(objects.getString("moduleName").toString());
                    data.setModuleId(objects.getString("moduleID").toString());
                    data.setModuleInfo(objects.getString("moduleInfo").toString());
                    data.setImage(objects.getString("moduleImage").toString());
                    data.setNewName(objects.getString("moduleName").toString());

                    list_createModule.add(data);


                }
                adapter_createModule = new CreateGroupModuleAdapter(CreateGroupModule.this, R.layout.create_group_module_list_item, list_createModule);
                listview.setAdapter(adapter_createModule);

            }


        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }


}