package com.SampleApp.row;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.InternetConnection;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

/**
 * Created by admin on 18-04-2017.
 */

public class Registration extends Activity {
    private TextView tv_title, tv_submit;
    private ImageView iv_backbutton;
    public String mobileno = "";
    private EditText edt_mobileNo, et_name, et_email, et_club, et_feedback;
    LinearLayout ll_fields;
    RadioGroup rd_group;
    int radioselectedId;
    String name = "";
    String email = "";
    String club = "";
    String feedback = "";
    String isRotarian = "Yes";
    TextView tvEmail, tvWebsite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        edt_mobileNo = (EditText) findViewById(R.id.et_mobileNo);
        Intent intent = getIntent();
        if (intent.hasExtra("mobileNo")) {
            mobileno = intent.getStringExtra("mobileNo");
            edt_mobileNo.setText(mobileno);
        }
        actionbarfunction();
        init();

    }

    public void init() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_club = (EditText) findViewById(R.id.et_club);
        et_feedback = (EditText) findViewById(R.id.et_feedback);
        ll_fields = (LinearLayout) findViewById(R.id.ll_fiels);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        rd_group = (RadioGroup) findViewById(R.id.radio_group_rotarian);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvWebsite = (TextView) findViewById(R.id.tvWebsite);

        rd_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {

                radioselectedId = radioGroup.getCheckedRadioButtonId();
                if (radioselectedId == R.id.radio_yes) {
                    isRotarian = "Yes";
                    ll_fields.setVisibility(View.VISIBLE);
                    tv_submit.setText(getResources().getString(R.string.label_submit));
                } else {
                    isRotarian = "No";
                    ll_fields.setVisibility(View.GONE);
                    tv_submit.setText(getResources().getString(R.string.exit_btn));
                    popup();
                }
            }
        });

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isRotarian.equalsIgnoreCase("Yes")) {
                    if (validation()) {
                        if (InternetConnection.checkConnection(getApplicationContext())) {
                            register();
                        } else {
                            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    finish();
                }
            }
        });
        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tvEmail.getTag().toString();


                final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setType("plain/text");
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});

                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(emailIntent);
            }

        });

        tvWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = tvWebsite.getTag().toString();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data)));
            }
        });
    }

    public void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText("Registration");
    }


    private boolean validation() {
        mobileno = edt_mobileNo.getText().toString();
        name = et_name.getText().toString();
        email = et_email.getText().toString();
        club = et_club.getText().toString();
        feedback = et_feedback.getText().toString();


        if (mobileno.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter Mobile No", Toast.LENGTH_LONG).show();
            return false;
        } else if (name.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter name", Toast.LENGTH_LONG).show();
            return false;
        } else if (email.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter email ID", Toast.LENGTH_LONG).show();
            return false;

        } else if (club.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter club name", Toast.LENGTH_LONG).show();
            return false;

        }

        return true;
    }

    private void register() {
        Hashtable<String, String> params = new Hashtable<>();
        params.put("MobileNo", mobileno);
        params.put("IsRotarian", isRotarian);
        params.put("Name", name);
        params.put("Email", email);
        params.put("Club", club);
        params.put("Feedback", feedback);

        try {
            final ProgressDialog progressDialog = new ProgressDialog(Registration.this, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

            JSONObject requestData = new JSONObject(new Gson().toJson(params));
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constant.REGISTER,
                    requestData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();

                            try {
                                getResult(response.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Log.e("ROW", "♦Error : " + error);
                            error.printStackTrace();
                            Toast.makeText(Registration.this, "Failed to receive Data from server . Please try again.", Toast.LENGTH_LONG).show();

                        }
                    }
            );
            request.setRetryPolicy(new DefaultRetryPolicy(
                    Constant.VOLLEY_MAX_REQUEST_TIMEOUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            AppController.getInstance().addToRequestQueue(Registration.this, request);
            progressDialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void getResult(String result) {

        JSONObject json = null;
        try {
            json = new JSONObject(result);
            JSONObject jsonresult = json.getJSONObject("RegistrationResult");
            final String status = jsonresult.getString("status");
            if (status.equals("0")) {
                Toast.makeText(this, "Registered Successfully", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void popup() {
        final Dialog dialog = new Dialog(Registration.this, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_registration_notrotarian);
        TextView ok = (TextView) dialog.findViewById(R.id.tv_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
