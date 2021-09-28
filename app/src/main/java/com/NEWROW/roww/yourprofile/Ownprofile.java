package com.NEWROW.row.yourprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.NEWROW.row.R;
import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Utils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;

import org.json.JSONObject;

public class Ownprofile extends AppCompatActivity {

    TextView  tvTitle ;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ownprofile);


        tvTitle = (TextView) findViewById( R.id.tv_title );

        tvTitle.setText("Profile");
        getprofiledetail();



    }
    public void getprofiledetail()
    {
       // http://rowtestapi.rosteronwheels.com/V4/api/Member/GetProfile


       // SharedPreferences sp = getSharedPreferences( "userName", MODE_PRIVATE );
      //  profileID = Long.parseLong(sp.getString( "Session_MemberProfileID", "" ));

        try {

            progressDialog = new ProgressDialog( Ownprofile.this, R.style.TBProgressBar );
            progressDialog.setCancelable( false );
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle( android.R.style.Widget_ProgressBar_Small );
            progressDialog.show();

            JSONObject requestData = new JSONObject();
            requestData.put( "MemberId", 376941 );



          //  Log.d( "Response", "PARAMETERS " + Constant.GetCalendar + " :- " + requestData.toString() );
            JsonObjectRequest request = new JsonObjectRequest( Request.Method.POST, "http://rowtestapi.rosteronwheels.com/V4/api/Member/GetProfile", requestData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    JsonObject result;
                    System.out.println( "testvalue" + "---test" );
                    Log.i("myproresponse", String.valueOf(response));
                   // ShowAllCalendarDetails( response );
                    Utils.log( response.toString() );
                    if ((progressDialog != null) && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if ((progressDialog != null) && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Utils.log( "Volley Error" + error.toString() );
                }
            } );
            request.setRetryPolicy( new DefaultRetryPolicy( 120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ) );

            AppController.getInstance().addToRequestQueue( getApplicationContext(), request );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
