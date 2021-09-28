package com.NEWROW.row;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.InternetConnection;
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
    private TextView tv_title, tv_submit,Are_U_Rotarian_Yes,Are_U_Rotarian_No,joinrotaryqusntxt;
    private ImageView iv_backbutton;
    public String mobileno = "";
    private EditText edt_mobileNo, et_fname,et_lname, et_email,et_city,et_state,et_occupation, et_club, et_feedback;
    LinearLayout ll_fields;
    RadioGroup rd_group,rd_group_join;
    int radioselectedId;
    String first_name,last_name,city,state,occupation = "";
    String email = "";
    // String club = "";
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

        et_email = (EditText) findViewById(R.id.et_email);
      //  et_club = (EditText) findViewById(R.id.et_club);
        et_feedback = (EditText) findViewById(R.id.et_feedback);
        ll_fields = (LinearLayout) findViewById(R.id.ll_fiels);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        rd_group = (RadioGroup) findViewById(R.id.radio_group_rotarian);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvWebsite = (TextView) findViewById(R.id.tvWebsite);

        //Add new field by Gaurav
        et_fname = (EditText) findViewById(R.id.et_fname);
        Are_U_Rotarian_Yes = (TextView) findViewById(R.id.areurotarianYes);
        Are_U_Rotarian_No = (TextView) findViewById(R.id.areurotarianNo);
        joinrotaryqusntxt = (TextView) findViewById(R.id.joinrotaryqusntxt);
        rd_group_join = (RadioGroup) findViewById(R.id.radio_group_rotarian_join);
        et_lname = (EditText) findViewById(R.id.et_lname);
        et_city = (EditText) findViewById(R.id.et_city);
        et_state = (EditText) findViewById(R.id.et_state);
        et_occupation = (EditText) findViewById(R.id.et_occupation);

        rd_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {

                radioselectedId = radioGroup.getCheckedRadioButtonId();
                if (radioselectedId == R.id.radio_yes) {
                    //isRotarian = "Yes";
                    //this below isRotarian=No condition for show exit button with proper handle
                    isRotarian = "No";
                    Are_U_Rotarian_Yes.setVisibility(View.VISIBLE);
                    Are_U_Rotarian_No.setVisibility(View.GONE);
                    ll_fields.setVisibility(View.GONE);
                    joinrotaryqusntxt.setVisibility(View.GONE);
                    rd_group_join.setVisibility(View.GONE);
                    tv_submit.setVisibility(View.VISIBLE);
                    tv_submit.setText(getResources().getString(R.string.exit_btn));
                    //tv_submit.setText(getResources().getString(R.string.label_submit));
                } else {
                    isRotarian = "No";
                    Are_U_Rotarian_No.setVisibility(View.VISIBLE);
                    Are_U_Rotarian_Yes.setVisibility(View.GONE);
                    joinrotaryqusntxt.setVisibility(View.VISIBLE);
                    ll_fields.setVisibility(View.GONE);
                    rd_group_join.setVisibility(View.VISIBLE);
                    tv_submit.setVisibility(View.GONE);
                    //tv_submit.setText(getResources().getString(R.string.exit_btn));
                    // popup();
                }
            }
        });

        rd_group_join.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {

                radioselectedId = radioGroup.getCheckedRadioButtonId();
                if (radioselectedId == R.id.radio_yes_join) {
                    isRotarian = "Yes";
                    // Are_U_Rotarian_Yes.setVisibility(View.VISIBLE);

                    //Here You Have to Open in new Activity
                     Intent intent = new Intent(Registration.this,RegistrationPage.class);
                     intent.putExtra("mobileNo",mobileno);
                     startActivity(intent);
                     finish();
                    /*ll_fields.setVisibility(View.VISIBLE);
                    tv_submit.setVisibility(View.VISIBLE);
                    tv_submit.setText(getResources().getString(R.string.label_submit));*/
                } else {
                    isRotarian = "No";
                  //  ll_fields.setVisibility(View.GONE);

                  //  tv_submit.setVisibility(View.VISIBLE);
                    //tv_submit.setText(getResources().getString(R.string.exit_btn));
                  //  popup();

                    Intent intent = new Intent(Registration.this,ThankYouPageForRegistration.class);
                    startActivity(intent);
                    finish();
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
        first_name = et_fname.getText().toString();
        last_name = et_lname.getText().toString();
        email = et_email.getText().toString();
        city= et_city.getText().toString();
        state= et_state.getText().toString();
        occupation= et_occupation.getText().toString();
        feedback = et_feedback.getText().toString();


        if (mobileno.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter Mobile No", Toast.LENGTH_LONG).show();
            return false;
        } else if (first_name.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter first name", Toast.LENGTH_LONG).show();
            return false;
        } else if (last_name.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter last name", Toast.LENGTH_LONG).show();
            return false;

        }else if (email.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter email ID", Toast.LENGTH_LONG).show();
            return false;

        } else if (city.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter city name", Toast.LENGTH_LONG).show();
            return false;

        }else if (state.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter state name", Toast.LENGTH_LONG).show();
            return false;

        }else if (occupation.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter occupation", Toast.LENGTH_LONG).show();
            return false;

        }




        return true;
    }

    private void register() {
        Hashtable<String, String> params = new Hashtable<>();

       // params.put("IsRotarian", isRotarian);
        params.put("FirstName", first_name);
        params.put("LastName", last_name);
        params.put("MobileNumber", mobileno);
        params.put("EmailID", email);
        params.put("City", city);
        params.put("State", state);
        params.put("Occupation", occupation);
        params.put("Feedaback", feedback);

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
                //Add By Gaurav
                Intent intent = new Intent(Registration.this,ThankYouPageForRegistration.class);
                startActivity(intent);
                //finish();
            } else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show();
                finish();
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
