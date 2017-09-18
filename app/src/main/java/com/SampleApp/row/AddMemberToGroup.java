package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;

/**
 * Created by user on 30-03-2016.
 */
public class AddMemberToGroup extends Activity {

    TextView tv_country_code;
    private int mRequestCode = 100;
    TextView tv_title,tv_save;
    ImageView iv_backbutton;
    EditText name,et_mobile,et_email;


    final Pattern USERNAME_PATTERN = Pattern.compile("[a-zA-Z0-9 ]{2,25}");
    final Pattern MOBILE_PATTERN = Pattern.compile("[0-9]{3,20}");

    final Pattern EMAIL_PATTERN = Pattern.compile( "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_member_detail);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText("New Member");

        tv_country_code = (TextView) findViewById(R.id.tv_country_code);
        tv_country_code.setTag("1");

        name = (EditText)findViewById(R.id.name);
        et_mobile = (EditText)findViewById(R.id.et_mobile);
        et_email = (EditText)findViewById(R.id.et_email);
        tv_save = (TextView)findViewById(R.id.tv_save);

        init();
    }

    private void init() {
        iv_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.popupback(AddMemberToGroup.this);
            }

        });

        tv_country_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddMemberToGroup.this, SelectCountry.class);
                startActivityForResult(i, mRequestCode);
            }
        });

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validation() == true) {
                 //   webservices();
                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        // Avaliable
                        webservices();
                    } else {
                        Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                        // Not Available...
                    }
                }

                //Toast.makeText(AddMemberToGroup.this, "Done", Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mRequestCode && resultCode == RESULT_OK) {

            tv_country_code.setText(data.getStringExtra("countryCode"));
            tv_country_code.setTag(data.getStringExtra("countryid"));

        }
    }


    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();

        arrayList.add(new BasicNameValuePair("userName", name.getText().toString()));
        arrayList.add(new BasicNameValuePair("mobile", et_mobile.getText().toString()));
        arrayList.add(new BasicNameValuePair("totalMember", ""));
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("masterID", PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.MASTER_USER_ID)));
        arrayList.add(new BasicNameValuePair("countryId", tv_country_code.getTag().toString()));
        arrayList.add(new BasicNameValuePair("memberEmail", et_email.getText().toString()));

        Log.d("Response", "PARAMETERS " + Constant.AddMemberToGroup + " :- " + arrayList.toString());
        new WebConnectionAsyncLogin(Constant.AddMemberToGroup, arrayList, AddMemberToGroup.this).execute();
    }

    public class WebConnectionAsyncLogin extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(AddMemberToGroup.this, R.style.TBProgressBar);

        public WebConnectionAsyncLogin(String url, List<NameValuePair> argList, Context ctx) {
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
            JSONObject ActivityResult = jsonObj.getJSONObject("TBAddMemberGroupResult");
            final String status = ActivityResult.getString("status");
            if (status.equals("0")) {
                String msg = ActivityResult.getString("message");

                Log.d("Touchbase", "*************** " + status);
                Log.d("Touchbase", "*************** " + msg);

                Intent intent = new Intent();
                setResult(1, intent);
                Toast.makeText(this, "Added successfully", Toast.LENGTH_LONG).show();
                finish();//finishing activity

            } else if ( status.equals("2")) {
                Toast.makeText(this, "Member is already existing in this club", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AddMemberToGroup.this, "Adding Member Failed please Contact Administrator", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }


    public boolean validation() {


        if (name.getText().toString().trim().matches("") || name.getText().toString().trim() == null) {
            Toast.makeText(AddMemberToGroup.this, "Please enter a Name ", Toast.LENGTH_LONG).show();
            return false;
        }
        if(et_mobile.getText().toString().trim().matches("") || et_mobile.getText().toString().trim()== null ) {

            Toast.makeText(getApplicationContext(), "Please enter a Mobile Number ", Toast.LENGTH_LONG).show();
            return false;
        }
        if(MOBILE_PATTERN.matcher(et_mobile.getText().toString()).matches()==false)
        {
            Toast.makeText(getApplicationContext(), "Please enter a valid Mobile Number", Toast.LENGTH_LONG).show();
            return false;
        }

        if(et_email.getText().toString().length()!=0 )
        {
            if(EMAIL_PATTERN.matcher(et_email.getText().toString()).matches()==false){

                Toast.makeText(getApplicationContext(), "Enter Valid Email Address ", Toast.LENGTH_LONG).show();
                return false;

            }else{

                return true;
            }
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        Utils.popupback(AddMemberToGroup.this);
        //
        // super.onBackPressed();
    }

}
