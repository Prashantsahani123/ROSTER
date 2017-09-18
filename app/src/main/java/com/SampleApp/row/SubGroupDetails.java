package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.SampleApp.row.Adapter.SubGroupDetailsAdapter;
import com.SampleApp.row.Data.SubGroupDetailsData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;

/**
 * Created by USER on 16-02-2016.
 */
public class SubGroupDetails extends Activity implements AdapterView.OnItemClickListener {
    private ListView listview;
    private ImageView iv_backbutton,iv_actionbtn;
    private TextView tv_title;
    private ArrayList<SubGroupDetailsData> list_data = new ArrayList<>();
    SubGroupDetailsAdapter ListAdapter ;
    private String subgroupid;
    private String subgroupname;
    private Context context;
    private boolean subGroupCreated = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subgroupdetails);
        context = getApplicationContext();

        listview = (ListView) findViewById(R.id.listView);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn.setVisibility(View.VISIBLE);
        // iv_backbutton.setVisibility(View.GONE);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("");



        Bundle intent = getIntent().getExtras();
        if (intent != null) {


            if (intent.containsKey("subgroupid")) {
                subgroupid = intent.getString("subgroupid");
                subgroupname = intent.getString("subgroupname");
                tv_title.setText(subgroupname);
            }
        }
        tv_title.setText(subgroupname);
        webservices();

        init();
        checkadminrights();
    }

    private void checkadminrights() {
        if (PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN).equals("No")) {
            iv_actionbtn.setVisibility(View.GONE);
        } else {
            iv_actionbtn.setVisibility(View.VISIBLE);
        }
    }
    public void init() {
        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateSubGroup.class);
                i.putExtra("subgrpId", subgroupid);
                startActivityForResult(i, 1);
            }
        });
    }
    private void webservices() {
        Log.e("webservice", "Starting of webservice");
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("subgrpId", subgroupid));

        Log.d("Response", "PARAMETERS " + Constant.GetSubGroupDetail + " :- " + arrayList.toString());
        if (InternetConnection.checkConnection(context)) {
            new WebConnectionAsyncAnnouncement(Constant.GetSubGroupDetail, arrayList, SubGroupDetails.this).execute();
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
        Log.e("webservice", "end of web service");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SubGroupDetailsData item = list_data.get(position);
        Intent intent = new Intent(context, NewProfileActivity.class);
        intent.putExtra("memberProfileId", item.getProfileId());
        intent.putExtra("groupId", PreferenceManager.getPreference(SubGroupDetails.this, PreferenceManager.GROUP_ID));
        intent.putExtra("fromMainDirectory", "no");




       /* i.putExtra("memberprofileid", );
        i.putExtra("groupId", );

        i.putExtra("nameLabel","Name");
        i.putExtra("numberLabel","Mobile Number");
        i.putExtra("memberName",item.getMemname());
        i.putExtra("memberMobile",item.getMobile());*/

        startActivity(intent);
    }



    public class WebConnectionAsyncAnnouncement extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(SubGroupDetails.this, R.style.TBProgressBar);
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
              //  list_announcmentdata.clear();
              //  getAnnouncementItems(result.toString());
                getsubgroupdetails(result.toString());


            } else {
                Log.d("Response", "Null Resposnse");
            }

        }
    }

    private void getsubgroupdetails(String result) {

        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject AnnouncementResult = jsonObj.getJSONObject("TBGetSubGroupDetailListResult");
            final String status = AnnouncementResult.getString("status");
            if (status.equals("0")) ;
            {
                JSONArray AnnouncementListResdult = AnnouncementResult.getJSONArray("SubGroupResult");
                for (int i = 0; i < AnnouncementListResdult.length(); i++) {
                    JSONObject object = AnnouncementListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("SubgrpMemberDetail");

                    SubGroupDetailsData data = new SubGroupDetailsData();

                    data.setProfileId(objects.getString("profileId").toString());
                    data.setMemname(objects.getString("memname").toString());
                    data.setMobile(objects.getString("mobile").toString());
                    


                    list_data.add(data);

                }
                ListAdapter = new SubGroupDetailsAdapter(SubGroupDetails.this, list_data);
                listview.setAdapter(ListAdapter);
                listview.setOnItemClickListener(this);

            }


        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                subGroupCreated = true;
                Intent intent = new Intent(SubGroupDetails.this, SubGroupList.class);
                intent.putExtra("parentId", subgroupid);
                intent.putExtra("subgroupname", subgroupname);
                startActivity(intent);
                Log.e("InSideOnActivity", "Inside on acitivity result");
                finish();
                finish();
            }
        }
    }

    /*@Override
    public void onBackPressed() {
        if (subGroupCreated)
            setResult(Activity.RESULT_OK);
        else
            setResult(Activity.RESULT_CANCELED);

        super.onBackPressed();
    }*/
}
