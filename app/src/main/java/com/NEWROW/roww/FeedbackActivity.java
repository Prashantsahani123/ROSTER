package com.NEWROW.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;
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
 * Created by admin on 23-05-2017.
 */

public class FeedbackActivity extends Activity {
    private TextView tv_title,tv_send;
    private EditText edt_feedback;
    private ImageView iv_backbutton;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        context = this;
        init();
        actionbarfunction();
    }

    public void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText("Feedback");
    }

    public void init(){

        tv_send = (TextView)findViewById(R.id.tv_send);
        edt_feedback = (EditText)findViewById(R.id.edt_feedback);

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_feedback.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(context,"Please enter feedback",Toast.LENGTH_LONG).show();
                }else{
                    if (InternetConnection.checkConnection(getApplicationContext())) {
                          sendFeedbackToServer();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void sendFeedbackToServer(){

        String profileId = PreferenceManager.getPreference(context,PreferenceManager.GRP_PROFILE_ID);
        Hashtable<String, String> params = new Hashtable<>();
        params.put("ProfileId",profileId);
        params.put("Feedback",edt_feedback.getText().toString());


        Log.d("sas","fedback params "+params.toString());

        try {

            final ProgressDialog progressDialog = new ProgressDialog(context, R.style.TBProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();



            JSONObject requestData = new JSONObject(new Gson().toJson(params));

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constant.FEEDBACK,
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

                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Log.e("ROW", "♦Error : " + error);
                            error.printStackTrace();
                            Toast.makeText(context, "Failed to receive Data from server. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
            );

            request.setRetryPolicy(new DefaultRetryPolicy(
                    Constant.VOLLEY_MAX_REQUEST_TIMEOUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            AppController.getInstance().addToRequestQueue(context, request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void getResult(String result) {

        JSONObject json = null;
        try{
            json = new JSONObject(result);
            JSONObject jsonresult = json.getJSONObject("FeedbackResult");
            final String status = jsonresult.getString("status");
            final String message = jsonresult.getString("message");
            if (status.equals("0")) {
                if(message.equalsIgnoreCase("success")) {
                    Toast.makeText(this, "Feedback sent successfully.", Toast.LENGTH_LONG).show();
                    finish();
                } else if(message.equalsIgnoreCase("Record not found")) {
                    Toast.makeText(this, "Feedback Sending Failed", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                }
            } else{
                if(status.equals("1")){
                    Toast.makeText(this,"Feedback Sending Failed",Toast.LENGTH_LONG).show();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
