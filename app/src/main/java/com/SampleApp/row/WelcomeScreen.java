package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

/**
 * Created by USER on 15-12-2015.
 */
public class WelcomeScreen extends Activity {
    ArrayList<String> listGroup;
    TextView tv_next;
    TextView tv_title,tv_partofgroups,tv_name;
    ImageView iv_backbutton;
    ListView listView;
    private String mobilenumber, loginType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        tv_next = (TextView) findViewById(R.id.tv_next);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setVisibility(View.GONE);
        iv_backbutton.setVisibility(View.GONE);
        listView = (ListView) findViewById(R.id.listView);

        tv_partofgroups = (TextView) findViewById(R.id.tv_partofgroups);
        tv_name = (TextView) findViewById(R.id.tv_name);
        Intent intent = getIntent();
        mobilenumber = intent.getStringExtra("mobilenumber");
        loginType = intent.getStringExtra("loginType");
        init();
        webservices();
    }

    private void init() {
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnection.checkConnection(getApplicationContext())) {

                    Intent i = new Intent(WelcomeScreen.this, DashboardActivity.class);
                    startActivity(i);
                    finish();
//                    Intent i = new Intent(WelcomeScreen.this, InitialProfile.class);
//                    startActivity(i);
//                    finish();
                }
                else
                {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }
            }
        });
    }

    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("masterUID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));
        arrayList.add(new BasicNameValuePair("mobileno", mobilenumber));
        arrayList.add(new BasicNameValuePair("loginType", loginType));
        if (InternetConnection.checkConnection(getApplicationContext())) {
            new WebConnectionAsync(Constant.GetWelcomeScreen, arrayList, WelcomeScreen.this).execute();
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
        final ProgressDialog progressDialog = new ProgressDialog(WelcomeScreen.this, R.style.TBProgressBar);

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
            //Log.d("response","Do post"+ result.toString());
            if (result != "") {
                getresult(result.toString());
            } else {
                Log.d("Response", "Null Resposnse");
            }
        }
    }

    private void getresult(String val) {
        try {
            JSONObject jsonObj = new JSONObject(val);
            JSONObject ActivityResult = jsonObj.getJSONObject("WelcomeResult");
            final String status = ActivityResult.getString("status");
            String name = ActivityResult.getString("Name");
            if (status.equals("0")) {
                //tv_name.setText("Rtn. "+name);
                tv_name.setText(name);
                listGroup = new ArrayList<String>();
                JSONArray grpsarray = ActivityResult.getJSONArray("grpPartResults");
                for (int i = 0; i < grpsarray.length(); i++) {
                    JSONObject object = grpsarray.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("GrpPartResult");
                    // pd.setProduct_master_id(objects.getString("productMasterId").toString());

                    listGroup.add(objects.getString("grpName").toString());
                }
                displaydata();

            }else{
                tv_partofgroups.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }

    private void displaydata() {
        Log.d("TouchBase", "LSIT " + listGroup.toString());
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.tv_list, listGroup));
        setListViewHeightBasedOnChildren(listView);
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

    public void prepareList() {
        listGroup = new ArrayList<String>();
        listGroup.add("Thane Hill");
        listGroup.add("Kaizen Infotech");
        listGroup.add("Indian Overseas");


    }
}
