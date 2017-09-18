package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.SampleApp.row.Adapter.GroupInfoAdapter;
import com.SampleApp.row.Data.GroupInfoData;
import com.SampleApp.row.Utils.CircleTransform;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;

/**
 * Created by USER1 on 02-06-2016.
 */
public class GroupInfo_New extends Activity {

    private ListView mainListView;
    private ArrayAdapter<String> listAdapter;
    View headerView;
    TextView tv_title, tv_group_name;
    ImageView iv_backbutton, iv_prof_pic;
    EditText et_search;
    private ImageView iv_actionbtn;

    LinearLayout call_button, mail_button, call_location,btn_admin;

    String mobile, mail, location, desc;
    ProgressBar progressbar;

    private ArrayList<GroupInfoData> list_groupInfoData = new ArrayList<GroupInfoData>();
    private GroupInfoAdapter adapter_groupInfoData;
    private String isAdmin = "0";
    private String grpID="0";
    private String GroupAdminID = "0";
    private String moduleId="0";
    private String moduleName = "";
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.group_info_new);
        setContentView(R.layout.new_group_info);
        headerView = getLayoutInflater().inflate(R.layout.group_info_list_header, null);

        moduleId = getIntent().getExtras().getString("moduleId");
        moduleName = getIntent().getExtras().getString("moduleName");

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        // iv_backbutton.setVisibility(View.GONE);

        tv_title.setText(moduleName);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn.setVisibility(View.VISIBLE); // EDIT ANNOUNCEMEBT
        iv_actionbtn.setImageResource(R.drawable.edit); // EDIT ANNOUNCEMEBT


        mainListView = (ListView) findViewById(R.id.listView);
        mainListView.addHeaderView(headerView);

        call_button = (LinearLayout) findViewById(R.id.call_button);
        mail_button = (LinearLayout) findViewById(R.id.mail_button);
        call_location = (LinearLayout) findViewById(R.id.call_location);
        btn_admin = (LinearLayout) findViewById(R.id.btn_admin);
        tv_group_name = (TextView) headerView.findViewById(R.id.tv_group_name);
        progressbar = (ProgressBar) headerView.findViewById(R.id.progressbar);
        iv_prof_pic = (ImageView) headerView.findViewById(R.id.iv_prof_pic);
        et_search = (EditText) headerView.findViewById(R.id.et_search);


        isAdmin = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN);
        grpID=PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID);

        Log.d("TOUCHBASE","PREFF IS ADMIN"+isAdmin);

        Intent intenti = getIntent();
        if (intenti.hasExtra("memID")) {

            grpID = intenti.getStringExtra("grpID");
            isAdmin = intenti.getStringExtra("isAdmin");
        }

        webservices_getdata();
        init();
        adminsettings();
    }

    private void adminsettings() {
        if (isAdmin.equals("No")) {
            iv_actionbtn.setVisibility(View.GONE);
        } else {
            if (moduleId.equals(Constant.Module.INFO))
                iv_actionbtn.setVisibility(View.VISIBLE);
            else
                iv_actionbtn.setVisibility(View.GONE);
        }
    }

    private void init() {
        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateGroup.class);
                i.putExtra("groupid",grpID);
                startActivityForResult(i,1);
            }
        });
        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("tel", mobile, null));
                startActivity(intent);
            }
        });


        mail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setType("plain/text");
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mail});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                GroupInfo_New.this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });

        call_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location.trim().length() != 0) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=" + location));
                    startActivity(intent);
                }else{
                    Toast.makeText(GroupInfo_New.this, "Address details not entered", Toast.LENGTH_LONG).show();
                }
            }
        });
        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Profile.class);
                i.putExtra("memberprofileid", GroupAdminID);
                i.putExtra("groupId", grpID);
                i.putExtra("isAdmin","1"); // 1- yes
                startActivity(i);
            }
        });


        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                //adapter.getFilter().filter(cs);
                //adapter.getFilter().filter(cs.toString());

                int textlength = cs.length();
                ArrayList<GroupInfoData> tempArrayList = new ArrayList<GroupInfoData>();

                for (GroupInfoData c : list_groupInfoData) {
                    if (textlength <= c.getTitle().length()) {
                        if (c.getTitle().toLowerCase().contains(cs.toString().toLowerCase())) {

                            tempArrayList.add(c);
                        }
                    }
                }
                //Data_array= tempArrayList;
                //   DirectoryAdapter adapter = new DirectoryAdapter(Directory.this, tempArrayList);

                GroupInfoAdapter adapter = new GroupInfoAdapter(GroupInfo_New.this, R.layout.group_info_new_item, tempArrayList);
                mainListView.setTextFilterEnabled(true);
                mainListView.setAdapter(adapter);
                setListViewHeightBasedOnChildren(mainListView);
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

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    webservices_getdata();
                    return true;
                }
                return false;
            }
        });


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

    private void webservices_getdata() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        //arrayList.add(new BasicNameValuePair("masterUID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));


        arrayList.add(new BasicNameValuePair("grpID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("moduleID", moduleId));

        Log.d("Response", "PARAMETERS " + Constant.GetEntityInfo + " :- " + arrayList.toString());
        if (InternetConnection.checkConnection( getApplicationContext()) ) {
            new WebConnectionAsync(Constant.GetEntityInfo, arrayList, GroupInfo_New.this).execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public class WebConnectionAsync extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(GroupInfo_New.this, R.style.TBProgressBar);

        public WebConnectionAsync(String url, List<NameValuePair> argList, Context ctx) {
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
            if (result != "") {
                getresult(result.toString());

            } else {
                Log.d("Response", "Null Resposnse");
            }
        }

        private void getresult(String val) {
            try {
                JSONObject jsonObj = new JSONObject(val);
                JSONObject ActivityResult = jsonObj.getJSONObject("TBEntityInfoResult");

                ActivityResult.getString("contactNo");

                mail = ActivityResult.getString("email");
                location = ActivityResult.getString("address");
                tv_group_name.setText(ActivityResult.getString("groupName"));

                if (ActivityResult.has("groupImg")) {
                    if (ActivityResult.getString("groupImg").toString().equals("") || ActivityResult.getString("groupImg").toString() == null) {
                        //  linear_image.setVisibility(View.GONE);
                    } else {
                        progressbar.setVisibility(View.VISIBLE);
                        Picasso.with(GroupInfo_New.this).load(ActivityResult.getString("groupImg").toString()).transform(new CircleTransform())
                                .placeholder(R.drawable.imageplaceholder)
                                .into(iv_prof_pic, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        progressbar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });
                    }
                }
                final String status = ActivityResult.getString("status");
                //-------------ADMIN INFO
                JSONArray Adminarray = ActivityResult.getJSONArray("AdminInfoResult");
                for (int i = 0; i < Adminarray.length(); i++) {
                    JSONObject object = Adminarray.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("AdminInfo");

                    GroupAdminID = objects.getString("adminId").toString();
                    mobile = objects.getString("memberMobile").toString();
                      /*  GroupInfoData gd = new GroupInfoData();
                        gd.setTitle(objects.getString("enname").toString());
                        gd.setDescription(objects.getString("descptn").toString());
                        list_groupInfoData.add(gd);*/
                }

                if (status.equals("0")) {

                    //----------- ENTITY INFOT
                    list_groupInfoData.clear();
                    JSONArray grpsarray = ActivityResult.getJSONArray("EntityInfoResult");
                    for (int i = 0; i < grpsarray.length(); i++) {
                        JSONObject object = grpsarray.getJSONObject(i);
                        JSONObject objects = object.getJSONObject("EntityInfo");
                       GroupInfoData gd = new GroupInfoData();
                        gd.setTitle(objects.getString("enname").toString());
                        gd.setDescription(objects.getString("descptn").toString());
                        list_groupInfoData.add(gd);
                    }

                    adapter_groupInfoData = new GroupInfoAdapter(GroupInfo_New.this, R.layout.group_info_new_item, list_groupInfoData);
                    mainListView.setAdapter(adapter_groupInfoData);

                    if ( list_groupInfoData.size() <= 7 ) {
                        headerView.setVisibility(View.GONE);
                    } else {
                        headerView.setVisibility(View.VISIBLE);
                    }
                    //setListViewHeightBasedOnChildren(mainListView);
                }

            } catch (Exception e) {
                Log.d("exec", "♦♦♦♦Exception :- " + e.toString());
                e.printStackTrace();
            }

        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 1) {
            webservices_getdata();

        }
    }
}