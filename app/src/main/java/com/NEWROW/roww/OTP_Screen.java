package com.NEWROW.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import androidx.annotation.NonNull;

import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.Utils;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.NEWROW.row.Utils.PreferenceManager.MASTER_USER_ID;
import static com.NEWROW.row.Utils.PreferenceManager.UDID;
import static com.NEWROW.row.Utils.PreferenceManager.savePreference;

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
    EditText et_1, et_2, et_3, et_4,et_5, et_6;
    LinearLayout linear_timetext;
    TextView timer, tv_notReceiveSMS, tv_resend_textcontent;
    private String devicetokenid = "";
    private String callwebserviceflag = "0";
    private String loginType;
    private MySMSBroadcastReceiver listener = null;
    private IntentFilter intentFilter = null;

    private String verificationId;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.otpscreen);

        context = this;


        mAuth = FirebaseAuth.getInstance();


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
        et_5 = (EditText) findViewById(R.id.et_5);
        et_6 = (EditText) findViewById(R.id.et_6);


        timer = (TextView) findViewById(R.id.timer);
        tv_notReceiveSMS = (TextView) findViewById(R.id.tv_notReceiveSMS);
        tv_resend = (TextView) findViewById(R.id.tv_resend);

        Intent intent = getIntent();
        otpvalue = intent.getStringExtra("otpvalue");
        mobilenumber = intent.getStringExtra("mobilenumber");
        countrycode = intent.getStringExtra("countrycode");
        devicetokenid = intent.getStringExtra("devicetokenid");
        loginType = intent.getStringExtra("loginType");

        init();

        CounterTime();

        //String phonenumber = getIntent().getStringExtra("phonenumber");
        sendVerificationCode(mobilenumber);


        //registration of receiver
        listener = new MySMSBroadcastReceiver();
        intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(listener, intentFilter);

    }

    private void init() {

        iv_backbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(OTP_Screen.this,LoginPage.class);
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
                } else if (s.length() == 0) {
                    et_1.clearFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

        });

        et_2.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    et_3.requestFocus();
                } else if (s.length() == 0) {
                    et_1.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        et_3.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    et_4.requestFocus();
                } else if (s.length() == 0) {
                    et_2.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


        });

        et_4.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    et_5.requestFocus();
                } else if (s.length() == 0) {
                    et_3.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


        });
        et_5.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    et_6.requestFocus();
                } else if (s.length() == 0) {
                    et_4.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


        });


        et_6.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {

                  //  if ((et_1.getText().toString() + et_2.getText().toString() + et_3.getText().toString() + et_4.getText().toString() + et_5.getText().toString()+ et_6.getText().toString()).equals(otpvalue)) {

                    if ((et_1.getText().toString() + et_2.getText().toString() + et_3.getText().toString() + et_4.getText().toString() + et_5.getText().toString()+ et_6.getText().toString()).length() >5) {
                        // Intent i = new Intent(OTP_Screen.this, WelcomeScreen.class);

                       String otp = (et_1.getText().toString() + et_2.getText().toString() + et_3.getText().toString() + et_4.getText().toString() + et_5.getText().toString()+ et_6.getText().toString());
                        //startActivity(i);

                        verifyCode(otp);
                        //  webservices();

                    } else {
                        Toast.makeText(getApplicationContext(), "Entered OTP is Invalid", Toast.LENGTH_SHORT).show();
                    }

                } else if (s.length() == 0) {
                    et_5.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

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
                Log.d("requestpostlogin", url + "parametr --- " + argList.toString());

                val = HttpConnection.postData(url, argList);
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
                   // String phonenumber = getIntent().getStringExtra("phonenumber");
                    sendVerificationCode(mobilenumber);

                    getresult(result.toString());
                } else {
                    sendVerificationCode(mobilenumber);
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


            Log.i("LoginResultttt", String.valueOf(ActivityResult));

            if (status.equals("0")) {

                ActivityResult.getString("isexists");
                storeDataInPreferences(ActivityResult.getString("masterUID"));
                savePreference(OTP_Screen.this, UDID, Utils.getAndroidSecureId(OTP_Screen.this)); // UDID STORED
                Intent i = new Intent(getApplicationContext(), com.NEWROW.row.WelcomeScreen.class);
                i.putExtra("mobilenumber", mobilenumber);
                i.putExtra("loginType", loginType);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

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

    // SmsListener listener = new SmsListener();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (listener != null) {
            unregisterReceiver(listener);
        }
    }

    public class MySMSBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            try {

                if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {

                    Bundle extras = intent.getExtras();

                    if (extras != null) {

                        Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                        if (status != null) {

                            switch (status.getStatusCode()) {

                                case CommonStatusCodes.SUCCESS:
                                    // Get SMS message contents
                                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                                    Log.d("value", message);
                                    // Extract one-time code from the message and complete verification
                                    // by sending the code back to your server.
                                    String[] parts = message.split(">");
                                    checkOTP(parts[1]);
                                    break;
                                case CommonStatusCodes.TIMEOUT:
                                    // Waiting for SMS timed out (5 minutes)
                                    // Handle the error ...
                                    break;
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("error_Beoadcast", e.getMessage());
            }
        }
    }


    public void checkOTP(String msgBody) {
        /*
         * In the current date, first 4 characters of the SMS is OTP.
         * Below code is written accordingly.
         * */
        try {
            String receivedOTP = msgBody.substring(1, 5);
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
    ///////////////////////firebase
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //Toast.makeText(OTP_Screen.this, "Success full login", Toast.LENGTH_LONG).show();
                            webservices();
//
//                            Intent intent = new Intent(VerifyPhoneActivity.this, ProfileActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                            startActivity(intent);

                        } else {

                            Toast.makeText(getApplicationContext(), "Entered OTP is Invalid", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(OTP_Screen.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        // progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+number,
                60,
                TimeUnit.SECONDS,
                OTP_Screen.this,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
               // editText.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Log.d("error",e.getMessage());

            String ee = e.getMessage();

            if(ee.equalsIgnoreCase("We have blocked all requests from this device due to unusual activity. Try again later."))
            {
                Toast.makeText(OTP_Screen.this, "Try after 4 hours ", Toast.LENGTH_LONG).show();
            }
            else {

                Toast.makeText(OTP_Screen.this, "Something went wrong ", Toast.LENGTH_LONG).show();
            }
          //  Toast.makeText(OTP_Screen.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}



