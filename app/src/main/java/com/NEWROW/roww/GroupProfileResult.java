package com.NEWROW.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.GroupProfileResultAdapter;
import com.NEWROW.row.Data.GroupProfileResultData;
import com.NEWROW.row.Utils.CircleTransform;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.PreferenceManager;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 17-02-2016.
 */
public class GroupProfileResult extends Activity {


    Context context;


    ImageView iv_prof_pic;

    private ArrayList<GroupProfileResultData> list_groupprofile = new ArrayList<GroupProfileResultData>();
    private GroupProfileResultAdapter adapter_groupprofile;

    TextView tv_name,tv_mobile;
    TextView tv_title;
    ImageView iv_backbutton;
    String name,mobile,clickedmemberid;
   // GroupProfileResultData data = new GroupProfileResultData();
    ListView listView;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_profile_result);


        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        // iv_backbutton.setVisibility(View.GONE);
        tv_title.setText("Result");
        listView = (ListView) findViewById(R.id.listView);
        iv_prof_pic = (ImageView) findViewById(R.id.iv_prof_pic);

        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_mobile = (TextView)findViewById(R.id.tv_mobile);

        scrollView = (ScrollView)findViewById(R.id.scrollView);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);


        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            clickedmemberid = intent.getString("clickedmemberid"); // Created Group ID
        }
        webservices();
        init();

    }

    public void init() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(list_groupprofile.get(position).getIsMygrp().equals("Y")){
                    Intent i = new Intent(GroupProfileResult.this,Profile.class);
                    i.putExtra("memberprofileid",list_groupprofile.get(position).getGrpProfileId());
                    i.putExtra("groupId",list_groupprofile.get(position).getGrpId());
                    startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(), "You are not Part of this Group. Access Denied ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void finishActivity(View v) {
        finish();
    }

    private void webservices() {
        //{"masterUID":"1","grpID":"","searchText":"","page":""}
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("memId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));
        arrayList.add(new BasicNameValuePair("otherMemId", clickedmemberid));

        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();

        Log.d("Response", "PARAMETERS " + Constant.GlobalSearchGroup + " :- " + arrayList.toString());
        new WebConnectionAsyncDirectory(Constant.GlobalSearchGroup, arrayList, GroupProfileResult.this).execute();
    }


    public class WebConnectionAsyncDirectory extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(GroupProfileResult.this, R.style.TBProgressBar);
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

        private void getDirectoryItems(String result) {
            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject globalSearchGroupResult = jsonObj.getJSONObject("TBGlobalSearchGroupResult");



                final String status = globalSearchGroupResult.getString("status");
                if (status.equals("0")) {
                    JSONArray allGlobalGroupListResults = globalSearchGroupResult.getJSONArray("AllGlobalGroupListResults");


                    //name =  data.setMembername(globalSearchGroupResult.getString("membername").toString());
                  //  mobile =  data.setMembermobile(globalSearchGroupResult.getString("membermobile").toString());
                    tv_name.setText(globalSearchGroupResult.getString("membername").toString());
                    tv_mobile.setText(globalSearchGroupResult.getString("membermobile").toString());

                    if(globalSearchGroupResult.has("profilepicpath")) {

                        if (globalSearchGroupResult.getString("profilepicpath").toString().equals("") || globalSearchGroupResult.getString("profilepicpath").toString() == null) {

                        } else {

                            Picasso.with(GroupProfileResult.this).load(globalSearchGroupResult.getString("profilepicpath").toString())
                                    .transform(new CircleTransform())
                                    .placeholder(R.drawable.profile_pic)
                                    .into(iv_prof_pic);

                        }
                    }

                    //   Log.d("@@@@@@@@@", "@@@@@@@@@@" + DirectoryListResdult.length());

                    for (int i = 0; i < allGlobalGroupListResults.length(); i++) {
                        JSONObject object = allGlobalGroupListResults.getJSONObject(i);
                        JSONObject objects = object.getJSONObject("GlobalGroupResult");

                        GroupProfileResultData data1 = new GroupProfileResultData();

                        data1.setGrpName(objects.getString("grpName").toString());

                        data1.setGrpId(objects.getString("grpId").toString());
                        data1.setGrpImg(objects.getString("grpImg").toString());
                        //data1.setGrpImg("http://learnonline.canberra.edu.au/pluginfile.php/844029/mod_book/chapter/1788/groups.jpg");
                        data1.setGrpProfileId(objects.getString("grpProfileId").toString());
                        data1.setIsMygrp(objects.getString("isMygrp").toString());



                        list_groupprofile.add(data1);

                       // Log.d("-------","------"+list_groupprofile);
                    }
                    adapter_groupprofile = new GroupProfileResultAdapter(GroupProfileResult.this, R.layout.group_profile_result_listitem, list_groupprofile);

                    listView.setAdapter(adapter_groupprofile);
                    setListViewHeightBasedOnChildren(listView);



                }


            } catch (Exception e) {
                Log.d("exec", "Exception :- " + e.toString());
            }
        }

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);

            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, GridLayout.LayoutParams.MATCH_PARENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + ((listView.getDividerHeight()) * (listAdapter.getCount()));

        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


}
