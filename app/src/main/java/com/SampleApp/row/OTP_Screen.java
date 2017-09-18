package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.MarshMallowPermission;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;

import static com.SampleApp.row.Utils.PreferenceManager.MASTER_USER_ID;
import static com.SampleApp.row.Utils.PreferenceManager.UDID;
import static com.SampleApp.row.Utils.PreferenceManager.savePreference;

/**
 * Created by USER on 15-12-2015.
 */
public class OTP_Screen extends Activity {

    Context context;
    TextView tv_agree_continue, tv_resend;
    TextView tv_title;
    ImageView iv_backbutton;
    String otpvalue;
    EditText et_otpinput;
    private String mobilenumber, countrycode;
    EditText et_1, et_2, et_3, et_4;
    LinearLayout linear_timetext;

    TextView timer, tv_notReceiveSMS, tv_resend_textcontent;
    private String devicetokenid = "";
    private String callwebserviceflag = "0";
    private String loginType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otpscreen);
        context = this;
        tv_agree_continue = (TextView) findViewById(R.id.tv_agree_continue);
        loginType = getIntent().getStringExtra("loginType");

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_resend = (TextView) findViewById(R.id.tv_resend);
        tv_resend_textcontent = (TextView) findViewById(R.id.tv_resend_textcontent);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        et_otpinput = (EditText) findViewById(R.id.et_otpinput);
        linear_timetext = (LinearLayout) findViewById(R.id.linear_timetext);
        // iv_backbutton.setVisibility(View.GONE);
        tv_title.setText("Got Your Code?");

        et_1 = (EditText) findViewById(R.id.et_1);
        et_2 = (EditText) findViewById(R.id.et_2);
        et_3 = (EditText) findViewById(R.id.et_3);
        et_4 = (EditText) findViewById(R.id.et_4);
        timer = (TextView) findViewById(R.id.timer);
        tv_notReceiveSMS = (TextView) findViewById(R.id.tv_notReceiveSMS);
        tv_resend = (TextView) findViewById(R.id.tv_resend);

        Intent intent = getIntent();
        otpvalue = intent.getStringExtra("otpvalue");
        mobilenumber = intent.getStringExtra("mobilenumber");
        countrycode = intent.getStringExtra("countrycode");
        devicetokenid = intent.getStringExtra("devicetokenid");
        loginType = intent.getStringExtra("loginType");
        permission = new MarshMallowPermission(this);
        // tv_resend.setText(otpvalue);
        if (permission.checkPermissionForReadSms()) {
            IntentFilter intentFilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
            registerReceiver(listener, intentFilter);
            Utils.log("Registered sms receiver");
        } else {
            permission.requestPermissionForReadSms();
            Utils.log("Not Registered sms receiver");
        }
        init();
        CounterTime();
    }

    private void init() {

        iv_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i =new Intent(OTP_Screen.this,LoginPage.class);
                i.putExtra("mobNumber",mobilenumber);
                i.putExtra("countryCode",countrycode);
                startActivity(i);*/
                finish();
            }
        });


        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webservicesResendOTP();
            }
        });
        tv_agree_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_otpinput.getText().toString().equals(otpvalue)) {
                   /* Intent i = new Intent(OTP_Screen.this, WelcomeScreen.class);
                    startActivity(i);*/
                    //webservices();
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter a VALID Verification Code", Toast.LENGTH_SHORT).show();
                }

            }
        });


        et_1.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    et_2.requestFocus();

                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });
        et_2.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et_3.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });
        et_3.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    et_4.requestFocus();
                }


            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }


        });
        et_4.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    if ((et_1.getText().toString() + et_2.getText().toString() + et_3.getText().toString() + et_4.getText().toString()).equals(otpvalue)) {
                        // Intent i = new Intent(OTP_Screen.this, WelcomeScreen.class);
                        //startActivity(i);
                        webservices();
                    } else {
                        Toast.makeText(getApplicationContext(), "Entered OTP is Invalid", Toast.LENGTH_SHORT).show();
                    }
                }


            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }


        });


    }


    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("mobileNo", mobilenumber));
        arrayList.add(new BasicNameValuePair("deviceToken", devicetokenid));
        arrayList.add(new BasicNameValuePair("loginType", loginType));
        arrayList.add(new BasicNameValuePair("countryCode", countrycode));
        arrayList.add(new BasicNameValuePair("deviceName", "Android"));
        arrayList.add(new BasicNameValuePair("versionNo", Constant.versionNo));
        arrayList.add(new BasicNameValuePair("imeiNo", Utils.getAndroidSecureId(OTP_Screen.this)));
        callwebserviceflag = "0";
        Log.d("Response", "PARAMETERS " + Constant.PostOTP + " :- " + arrayList.toString());
        if (InternetConnection.checkConnection(getApplicationContext())) {
            new WebConnectionAsyncLogin(Constant.PostOTP, arrayList, OTP_Screen.this).execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void webservicesResendOTP() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("mobileNo", mobilenumber));
        arrayList.add(new BasicNameValuePair("countryCode", countrycode));
        arrayList.add(new BasicNameValuePair("loginType", loginType));
        callwebserviceflag = "1";
        Log.d("Response", "PARAMETERS " + Constant.UserLogin + " :- " + arrayList.toString());
        if (InternetConnection.checkConnection(getApplicationContext())) {
            new WebConnectionAsyncLogin(Constant.UserLogin, arrayList, OTP_Screen.this).execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    public class WebConnectionAsyncLogin extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(OTP_Screen.this, R.style.TBProgressBar);

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
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Log.d("response","Do post"+ result.toString());
            if (result != "") {
                if (callwebserviceflag.equals("0")) {
                    getresult(result.toString());
                } else {
                    getresultOTP_RESEND(result.toString());
                }
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
                ActivityResult.getString("isexists");
                storeDataInPreferences(ActivityResult.getString("masterUID"));
                savePreference(OTP_Screen.this, UDID, Utils.getAndroidSecureId(OTP_Screen.this)); // UDID STORED
                Intent i = new Intent(getApplicationContext(), WelcomeScreen.class);
                i.putExtra("mobilenumber", mobilenumber);
                i.putExtra("loginType", loginType);
                PreferenceManager.savePreference(context, PreferenceManager.LOGIN_TYPE, loginType);
                startActivity(i);
                hideKeyboard();
                finish();
            }
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }

    public void hideKeyboard() {
        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            if (getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    SmsListener listener = new SmsListener();

    private void getresultOTP_RESEND(String val) {
        try {
            JSONObject jsonObj = new JSONObject(val);
            JSONObject ActivityResult = jsonObj.getJSONObject("LoginResult");
            final String status = ActivityResult.getString("status");
            if (status.equals("0")) {
                ActivityResult.getString("otp");
                Log.d("Touchbase", "*************** " + ActivityResult.getString("otp"));
                CounterTime();
                otpvalue = ActivityResult.getString("otp");
                linear_timetext.setVisibility(View.VISIBLE);
                tv_notReceiveSMS.setVisibility(View.GONE);
                tv_resend.setVisibility(View.GONE);
                tv_resend_textcontent.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }

    private void storeDataInPreferences(String userid) {
        savePreference(this, MASTER_USER_ID, userid);
    }

    public void CounterTime() {
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("" + millisUntilFinished / 1000);

            }

            public void onFinish() {
                timer.setText("0");
                linear_timetext.setVisibility(View.GONE);
                tv_notReceiveSMS.setVisibility(View.VISIBLE);
                tv_resend.setVisibility(View.VISIBLE);
                tv_resend_textcontent.setVisibility(View.VISIBLE);
            }

        }.start();
    }


    /*Code below is written for auto otp detection*/
    private MarshMallowPermission permission;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MarshMallowPermission.READ_SMS_PERMISSION_REQUEST_CODE) {
            if (permission.checkPermissionForReadSms()) {
                // Register read sms receiver here
                IntentFilter intentFilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
                registerReceiver(listener, intentFilter);
            } else {
                Toast.makeText(context, "As you have selected not to allow read SMS, you have to enter OTP manually.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class SmsListener extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Utils.log("Inside SMS receiver");
            if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {//"android.provider.Telephony.SMS_RECEIVED")){
                Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                SmsMessage[] msgs = null;
                String msg_from;
                String msgBody = "";
                if (bundle != null) {
                    //---retrieve the SMS message received---
                    try {
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        for (int i = 0; i < msgs.length; i++) {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            msg_from = msgs[i].getOriginatingAddress();
                            msgBody = msgBody + msgs[i].getMessageBody();
                        }
                        checkOTP(msgBody);
                    } catch (Exception e) {
                        Log.d("Exception caught", e.getMessage());
                    }
                }
            }
        }
    }

    public void checkOTP(String msgBody) {
        /*
        * In the current date, first 4 characters of the SMS is OTP.
        * Below code is written accordingly.
        * */
        try {
            String receivedOTP = msgBody.substring(0, 4);
            Utils.log("Received OTP is : " + receivedOTP);
            if (otpvalue.equals(receivedOTP)) {
                et_1.setText("" + receivedOTP.charAt(0));
                et_2.setText("" + receivedOTP.charAt(1));
                et_3.setText("" + receivedOTP.charAt(2));
                et_4.setText("" + receivedOTP.charAt(3));
                //webservices();
            } else {
                Utils.log("Invalid otp");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
