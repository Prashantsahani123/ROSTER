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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.MarshMallowPermission;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.notification.GCMClientManager;

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
    private MarshMallowPermission permission;
    // login type "0" means user is a member of ROW
    // login type "1" means user is one of the family member of ROW
    public String loginType = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_row);
        Notification_get_id();
        init();
        permission = new MarshMallowPermission(this);

        if (permission.checkPermissionForReadSms()) {

        } else {
            permission.requestPermissionForReadSms();
        }

    }


    private void Notification_get_id() {
        GCMClientManager pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
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
        });
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
                if (flag_click == "1") {
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
                    flag_click = "1";
                } else {
                    flag_click = "0";
                }
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
                    String mobileNo = tv_country_code.getText().toString() + " " + et_mobile.getText().toString();
                    Intent i = new Intent(getApplicationContext(), Registration.class);
                    i.putExtra("mobileNo", mobileNo);
                    startActivity(i);
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


}
