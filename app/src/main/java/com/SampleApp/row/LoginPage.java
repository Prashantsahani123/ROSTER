package com.SampleApp.row;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.notification.GCMClientManager;

/**
 * Created by USER on 15-12-2015.
 */
public class LoginPage extends Activity {

    TextView ib_continue, tv_select_country;
    private int mRequestCode = 100;
    EditText et_mobile;
    Context context;
    TextView tv_title, tv_country_code;
    ImageView iv_backbutton;
    LinearLayout linear_buttonbackground;
    String flag_click="0";
    String PROJECT_NUMBER="1084355842406";
    String devicetokenid="";
    String mobNumber,countryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        ib_continue = (TextView) findViewById(R.id.ib_continue);
        tv_select_country = (TextView) findViewById(R.id.tv_select_country);
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);

        tv_country_code = (TextView) findViewById(R.id.tv_country_code);
        linear_buttonbackground = (LinearLayout) findViewById(R.id.linear_buttonbackground);
        iv_backbutton.setVisibility(View.GONE);
        tv_title.setText("Get Started!");
        tv_country_code.setTag("1");
/*
        Intent intent = getIntent();

        //otpvalue = intent.getStringExtra("otpvalue");
        mobNumber = intent.getStringExtra("mobNumber");
        countryCode = intent.getStringExtra("countryCode");
        et_mobile.setText(mobNumber);
        tv_country_code.setText(countryCode);*/

        Notification_get_id();
        init();
    }

    private void Notification_get_id() {
        GCMClientManager pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {

                Log.d("Registration id", registrationId);
                devicetokenid =registrationId;
                //send this registrationId to your server
            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
            }
        });
    }

    private void init() {
        tv_select_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetConnection.checkConnection(getApplicationContext())) {
                    Intent i = new Intent(LoginPage.this, SelectCountry.class);
                    startActivityForResult(i, mRequestCode);
                }
                else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }
            }
        });

        ib_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(flag_click == "1") {
                    final Dialog dialog = new Dialog(LoginPage.this, android.R.style.Theme_Translucent);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.popup_login_verify);

                    TextView tv_edit = (TextView) dialog.findViewById(R.id.tv_edit);
                    TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                    TextView tv_mobile = (TextView) dialog.findViewById(R.id.tv_mobile);
                    tv_mobile.setText(et_mobile.getText().toString());
                    TextView mobile_code = (TextView) dialog.findViewById(R.id.mobile_code);
                    mobile_code.setText(tv_country_code.getText().toString());

                    tv_edit.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    tv_yes.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            if (InternetConnection.checkConnection(getApplicationContext())) {
                                webservices();
                            } else {
                                Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                                // Not Available...
                            }
                        }
                    });
                    dialog.show();
                }
            }
        });
        et_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_mobile.getText().length() > 0) {
                    flag_click="1";
                    linear_buttonbackground.setBackgroundColor(getResources().getColor(R.color.bottom_button));
                } else {
                    flag_click="0";
                    linear_buttonbackground.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                }
            }
        });
    }

    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("mobileNo", et_mobile.getText().toString()));
        arrayList.add(new BasicNameValuePair("deviceToken", devicetokenid));
        arrayList.add(new BasicNameValuePair("countryCode", tv_country_code.getTag().toString()));

        Log.d("Response", "PARAMETERS " + Constant.UserLogin + " :- " + arrayList.toString());
        new WebConnectionAsyncLogin(Constant.UserLogin, arrayList, LoginPage.this).execute();
    }


    public class WebConnectionAsyncLogin extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(LoginPage.this, R.style.TBProgressBar);

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
            JSONObject ActivityResult = jsonObj.getJSONObject("LoginResult");
            final String status = ActivityResult.getString("status");
            if (status.equals("0")) {
                ActivityResult.getString("otp");
                Log.d("Touchbase", "*************** " + ActivityResult.getString("otp"));

                Intent i = new Intent(getApplicationContext(), OTP_Screen.class);
                i.putExtra("otpvalue", ActivityResult.getString("otp"));
                i.putExtra("mobilenumber", et_mobile.getText().toString());
                i.putExtra("countrycode", tv_country_code.getTag().toString());
                i.putExtra("devicetokenid", devicetokenid);
                startActivity(i);
                finish();
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mRequestCode && resultCode == RESULT_OK) {
            tv_select_country.setText(data.getStringExtra("selectedValue"));
            tv_country_code.setText(data.getStringExtra("countryCode"));
            tv_country_code.setTag(data.getStringExtra("countryid"));
        }
    }
}
