package com.NEWROW.row;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.Utils;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 13-04-2017.
 */
public class LoginPage_Row extends Activity {
    private LinearLayout linear_member;
    private LinearLayout linear_family;
    private TextView iv_member, tv_select_country, tv_country_code;
    private TextView iv_family, ib_continue;
    private View view_member;
    private View view_family;
    private int mRequestCode = 100;
    String devicetokenid = "";
    String PROJECT_NUMBER = "787992671225";
    EditText et_mobile;
    String flag_click = "0";
    // login type "0" means user is a member of ROW
    // login type "1" means user is one of the family member of ROW
    public String loginType = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_row_new);

        Notification_get_id();

        init();

        /* AppSignatureHelper appSignatureHelper = new AppSignatureHelper(LoginPage_Row.this);
        appSignatureHelper.getAppSignatures();*/
    }

    private void Notification_get_id() {

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {

                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if (!task.isSuccessful()) {
                            Log.w("row", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d("Registration id", token);
                        devicetokenid = token;
                    }
                });

        /*GCMClientManager pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);

        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {

            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {

                Log.d("Registration id", registrationId);
                devicetokenid = registrationId;
                //send this registrationId to your server
            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
            }

        });*/
    }

    public void init() {

        linear_member = (LinearLayout) findViewById(R.id.ll_member);
        linear_family = (LinearLayout) findViewById(R.id.ll_family);
        iv_member = (TextView) findViewById(R.id.tv_member);
        iv_family = (TextView) findViewById(R.id.tv_family);
        view_member = (View) findViewById(R.id.view_member);
        view_family = (View) findViewById(R.id.view_family);
        tv_select_country = (TextView) findViewById(R.id.tv_select_country);
        tv_country_code = (TextView) findViewById(R.id.tv_country_code);
        ib_continue = (TextView) findViewById(R.id.ib_continue);
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        tv_country_code.setTag("1");
        iv_member.setTextColor(getResources().getColor(R.color.view_color_blue));
        view_member.setBackgroundColor(getResources().getColor(R.color.view_color_blue));

        linear_member.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loginType = "0";
                iv_member.setTextColor(getResources().getColor(R.color.view_color_blue));
                view_member.setVisibility(View.VISIBLE);
                iv_family.setTextColor(getResources().getColor(R.color.black));
                view_family.setVisibility(View.GONE);
            }
        });

        linear_family.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loginType = "1";
                iv_family.setTextColor(getResources().getColor(R.color.view_color_blue));
                view_family.setVisibility(View.VISIBLE);
                iv_member.setTextColor(getResources().getColor(R.color.black));
                view_member.setVisibility(View.GONE);
            }

        });

        tv_select_country.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (InternetConnection.checkConnection(getApplicationContext())) {
                    Intent i = new Intent(LoginPage_Row.this, SelectCountry.class);
                    startActivityForResult(i, mRequestCode);
                } else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }
            }
        });

        ib_continue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (flag_click.equalsIgnoreCase("1")) {

                    final Dialog dialog = new Dialog(LoginPage_Row.this, android.R.style.Theme_Translucent);
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
                            dialog.dismiss();
                            if (InternetConnection.checkConnection(getApplicationContext())) {
                               // webservices();
                                startSMSListener(); // for auto read OTP
                            } else {
                                Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                                // Not Available...
                            }
                        }
                    });

                    dialog.show();
                }else{
                    Toast.makeText(LoginPage_Row.this,"Please enter Mobile Number",Toast.LENGTH_SHORT).show();

                }
            }
        });

        et_mobile.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (et_mobile.getText().length()>0) {
                    flag_click = "1";
                } else {
                    Toast.makeText(LoginPage_Row.this,"Please enter Mobile Number",Toast.LENGTH_SHORT).show();
                    flag_click = "0";
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == mRequestCode && resultCode == RESULT_OK) {
            tv_select_country.setText(data.getStringExtra("selectedValue"));
            tv_country_code.setText(data.getStringExtra("countryCode"));
            tv_country_code.setTag(data.getStringExtra("countryid"));
        }
    }

    private void webservices() {

        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("mobileNo", et_mobile.getText().toString()));
        arrayList.add(new BasicNameValuePair("countryCode", tv_country_code.getTag().toString()));
        arrayList.add(new BasicNameValuePair("loginType", loginType));

        Log.d("Response", "PARAMETERS " + Constant.UserLogin + " :- " + arrayList.toString());

        new WebConnectionAsyncLogin(Constant.UserLogin, arrayList, LoginPage_Row.this).execute();
    }

    public class WebConnectionAsyncLogin extends AsyncTask<String, Object, Object> {
        String val = null;
        ProgressDialog dialog;
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;
        final ProgressDialog progressDialog = new ProgressDialog(LoginPage_Row.this, R.style.TBProgressBar);

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
            /*ProgressBar pb = (ProgressBar) progressDialog.findViewById(android.R.id.progress);
            pb.getIndeterminateDrawable().setColorFilter(Color.parseColor(Constant.THEME_COLOR), android.graphics.PorterDuff.Mode.MULTIPLY);*/
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
            Log.d("response","Do post"+ result.toString());
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
            String message = ActivityResult.getString("message");

            if (status.equals("0")) {

                if (message.equalsIgnoreCase("Member not registered")) {
                    String mobileNo = tv_country_code.getText().toString() + "-" + et_mobile.getText().toString();
                    Intent i = new Intent(getApplicationContext(), Registration.class);
                    i.putExtra("mobileNo", mobileNo);
                    startActivity(i);
                    finish();
                } else {
                    ActivityResult.getString("otp");
                    Log.d("Touchbase", "*************** " + ActivityResult.getString("otp"));

                    Intent i = new Intent(getApplicationContext(), OTP_Screen.class);
                    i.putExtra("otpvalue", ActivityResult.getString("otp"));
                    i.putExtra("mobilenumber", et_mobile.getText().toString());
                    i.putExtra("countrycode", tv_country_code.getTag().toString());
                    i.putExtra("devicetokenid", devicetokenid);
                    i.putExtra("loginType", loginType);
                    PreferenceManager.savePreference(LoginPage_Row.this, PreferenceManager.MOBILE_NUMBER, et_mobile.getText().toString());
                    PreferenceManager.savePreference(LoginPage_Row.this, PreferenceManager.COUNTRY_CODE, tv_country_code.getTag().toString());

                    startActivity(i);
                    finish();
                }
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }

    }

    private  void startSMSListener() {

        // Get an instance of SmsRetrieverClient, used to start listening for a matching
// SMS message.
        SmsRetrieverClient client = SmsRetriever.getClient(this /* context */);

// Starts SmsRetriever, which waits for ONE matching SMS message until timeout
// (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
// action SmsRetriever#SMS_RETRIEVED_ACTION.

        Task<Void> task = client.startSmsRetriever();

// Listen for success/failure of the start Task. If in a background thread, this
// can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                // ...
                webservices();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
                webservices();
            }
        });
    }


    }



