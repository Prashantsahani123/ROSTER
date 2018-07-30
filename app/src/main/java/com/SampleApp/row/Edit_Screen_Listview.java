package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.ProfileAdapter_EditDetails;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.Utils;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 18-03-2016.
 */
public class Edit_Screen_Listview extends Activity {
    private ProfileAdapter_EditDetails profileAdapter;
    private ListView listview;
    TextView tv_save;
    private String memberprofileid;
    String json = "";
    TextView tv_title;
    ImageView iv_backbutton;
    //  String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String emailPattern ="^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_screen_listview);

        listview = (ListView) findViewById(R.id.listView);
        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        // iv_backbutton.setVisibility(View.GONE);
        tv_title.setText("Profile Edit");

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            memberprofileid = intent.getString("memberprofileid");
        }
        init();
    }

    private void init() {
        profileAdapter = new ProfileAdapter_EditDetails(Edit_Screen_Listview.this, R.layout.profile_listitem_edit, Utils.profilearraylist);
        listview.setAdapter(profileAdapter);

        Log.e("Here", "We are here business wala part");


        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(InternetConnection.checkConnection(getApplicationContext())) {
                    Log.d("TouchBase ", "ARRAY LIST -------" + Utils.profilearraylist.toString());
                    Gson gson = new Gson();
                    json = gson.toJson(Utils.profilearraylist);
                    if (validate())// validate();
                        test(json);
                }

                else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }



            }
        });
    }

    ///////////////////

    //////////////
    private void test(String str) {
        //{"masterUID":"1","grpID":"","searchText":"","page":""}
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("key", str));
        arrayList.add(new BasicNameValuePair("profileID", memberprofileid));

        //  arrayList.add(new BasicNameValuePair("groupId", groupId));

        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();

        Log.d("Response", "PARAMETERS " + Constant.UpdateProfilePersonalDetails + " :- " + arrayList.toString());
        new WebConnectionAsyncDirectory(Constant.UpdateProfilePersonalDetails, arrayList, Edit_Screen_Listview.this).execute();
    }

    public class WebConnectionAsyncDirectory extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(Edit_Screen_Listview.this, R.style.TBProgressBar);
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

                getrestult(result.toString());


            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getrestult(String result) {
        try {

            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("UserResult");
            final String status = EventResult.getString("status");
            String json = "";
            if (status.equals("0")) {

                Intent intent = new Intent();
                // intent.putExtra("MESSAGE",message);
                setResult(3, intent);

                finish();

            }
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    public boolean validate() {

        for (int i = 0; i < Utils.profilearraylist.size(); i++) {
            Log.d("TOUCHBASE", "--------" + Utils.profilearraylist.get(i).getUniquekey());
            if (Utils.profilearraylist.get(i).getUniquekey().equals("member_name")) {
                //   Log.d("TOUCHBASE","--------@-"+Utils.profilearraylist.get(i).getValue());
                if (Utils.profilearraylist.get(i).getValue().isEmpty() || Utils.profilearraylist.get(i).getValue() == null) {
                    Toast.makeText(Edit_Screen_Listview.this, "Please enter the Name", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            if (Utils.profilearraylist.get(i).getUniquekey().equals("member_email_id")) {
                if( Utils.profilearraylist.get(i).getValue().length() > 0) {
                    if (Utils.profilearraylist.get(i).getValue().matches(emailPattern) && Utils.profilearraylist.get(i).getValue().length() > 0) {
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter a valid Email Address", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }

            if (Utils.profilearraylist.get(i).getUniquekey().equals("member_buss_email")) {
               // Log.d("TOUCHBASE", "LENGHT :-" + Utils.profilearraylist.get(i).getValue().length());
                if (Utils.profilearraylist.get(i).getValue().length() > 0){
                    if (Utils.profilearraylist.get(i).getValue().matches(emailPattern) && Utils.profilearraylist.get(i).getValue().length() > 0) {
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter a valid Email Address", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }

        }
        return true;
    }
}
