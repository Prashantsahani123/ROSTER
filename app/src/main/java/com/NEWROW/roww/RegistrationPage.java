package com.NEWROW.row;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

public class RegistrationPage extends AppCompatActivity {

    private TextView tv_title, tv_submit, Are_U_Rotarian_Yes, Are_U_Rotarian_No, joinrotaryqusntxt;
    private ImageView iv_backbutton;
    public String mobileno = "";
    private EditText et_mobileno, et_fname, et_lname, et_email, et_city, et_state, et_occupation, et_club, et_feedback;
    LinearLayout ll_fields;
    RadioGroup rd_group, rd_group_join;
    int radioselectedId;
    String first_name, last_name, city, state, occupation = "";
    String email = "";
    // String club = "";
    String feedback = "";
    String isRotarian = "Yes";
    TextView tvEmail, tvWebsite,rotaryindialink;
    //Added By Gaurav for validation in email
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);


        init();
        Intent intent = getIntent();
        if (intent.hasExtra("mobileNo")) {
            mobileno = intent.getStringExtra("mobileNo");
            // edt_mobileNo.setText(mobileno);
        }
    }

    public void init() {

        et_email = (EditText) findViewById(R.id.et_email);
        //  et_club = (EditText) findViewById(R.id.et_club);
        et_feedback = (EditText) findViewById(R.id.et_feedback);
        et_mobileno = (EditText) findViewById(R.id.et_mobileno);
        ll_fields = (LinearLayout) findViewById(R.id.ll_fiels);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        // rd_group = (RadioGroup) findViewById(R.id.radio_group_rotarian);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvWebsite = (TextView) findViewById(R.id.tvWebsite);
        rotaryindialink = (TextView) findViewById(R.id.rotaryindialink);

        //Add new field by Gaurav
        et_fname = (EditText) findViewById(R.id.et_fname);
        //  Are_U_Rotarian_Yes = (TextView) findViewById(R.id.areurotarianYes);
        // Are_U_Rotarian_No = (TextView) findViewById(R.id.areurotarianNo);
        // joinrotaryqusntxt = (TextView) findViewById(R.id.joinrotaryqusntxt);
        // rd_group_join = (RadioGroup) findViewById(R.id.radio_group_rotarian_join);
        et_lname = (EditText) findViewById(R.id.et_lname);
        et_city = (EditText) findViewById(R.id.et_city);
        et_state = (EditText) findViewById(R.id.et_state);
        et_occupation = (EditText) findViewById(R.id.et_occupation);


        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (validation()) {
                    if (InternetConnection.checkConnection(getApplicationContext())) {
                        register();
                    } else {
                        Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

      /*  tvEmail.setOnClickListener(new View.OnClickListener() {
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

        });*/

        /*tvWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = tvWebsite.getTag().toString();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data)));
            }
        });*/

         rotaryindialink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "https://rotaryindia.org/";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data)));
            }
        });
    }


    private boolean validation() {
        mobileno = et_mobileno.getText().toString();
        first_name = et_fname.getText().toString();
        last_name = et_lname.getText().toString();
        email = et_email.getText().toString();
        city = et_city.getText().toString();
        state = et_state.getText().toString();
        occupation = et_occupation.getText().toString();
        feedback = et_feedback.getText().toString();


        if (first_name.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter First Name", Toast.LENGTH_LONG).show();
            return false;
        } else if (last_name.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter Last Name", Toast.LENGTH_LONG).show();
            return false;

        } else if (mobileno.equalsIgnoreCase("") || et_mobileno.getText().toString().length() < 0) {
            Toast.makeText(this, "Please enter Mobile Number", Toast.LENGTH_LONG).show();
            return false;
        } else if (email.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter Email ID", Toast.LENGTH_LONG).show();
            return false;

        } else if (!(et_email.getText().toString().trim().matches(emailPattern))) {
            Toast.makeText(getApplicationContext(), "Please enter valid Email ID", Toast.LENGTH_SHORT).show();
           return false;
        } else if (city.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter City Name", Toast.LENGTH_LONG).show();
            return false;

        } else if (state.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter State Name", Toast.LENGTH_LONG).show();
            return false;

        } else if (occupation.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter Occupation", Toast.LENGTH_LONG).show();
            return false;

        }


        return true;
    }

    private void register() {
        Hashtable<String, String> params = new Hashtable<>();

        // params.put("IsRotarian", isRotarian);
        params.put("FirstName", first_name);
        params.put("LastName", last_name);
        params.put("MobileNumber", et_mobileno.getText().toString());
        params.put("EmailID", email);
        params.put("City", city);
        params.put("State", state);
        params.put("Occupation", occupation);
        params.put("Feedaback", feedback);

        Log.d("Response", "PARAMETERS " + Constant.REGISTER + " :- " + params);


        try {
            final ProgressDialog progressDialog = new ProgressDialog(RegistrationPage.this, R.style.TBProgressBar);
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
                            Toast.makeText(RegistrationPage.this, "Failed to receive Data from server . Please try again.", Toast.LENGTH_LONG).show();

                        }
                    }
            );
            request.setRetryPolicy(new DefaultRetryPolicy(
                    Constant.VOLLEY_MAX_REQUEST_TIMEOUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            AppController.getInstance().addToRequestQueue(RegistrationPage.this, request);
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
                Intent intent = new Intent(RegistrationPage.this, ThankYouPageForRegistration.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show();
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
