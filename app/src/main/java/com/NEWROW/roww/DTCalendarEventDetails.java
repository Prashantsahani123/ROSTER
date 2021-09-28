package com.NEWROW.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

/**
 * Created by USER on 17-12-2015.
 */
public class DTCalendarEventDetails extends Activity {
    String eventID = "0";
    String moduleName = "";
    TextView tv_title;

    TextView event_title;
    TextView event_desc;
    TextView event_venue;
    TextView event_datetime;

    ImageView iv_backbutton;

    ImageView iv_eventimg;
    LinearLayout linear_image;
    String imageurl;
    ProgressBar progressBar;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dt_events_detail);
        context = this;
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        moduleName = PreferenceManager.getPreference(this, PreferenceManager.MODUEL_NAME, "Events");
        tv_title.setText("Events");

        event_title = (TextView) findViewById(R.id.event_title);
        event_desc = (TextView) findViewById(R.id.event_desc);
        event_venue = (TextView) findViewById(R.id.event_venue);
        event_datetime = (TextView) findViewById(R.id.event_datetime);
        iv_eventimg = (ImageView) findViewById(R.id.iv_eventimg);
        linear_image = (LinearLayout) findViewById(R.id.linear_image);

        eventID = getIntent().getStringExtra("eventid");


        Hashtable<String, String> params = new Hashtable<>();
        params.put("eventID", eventID);

        if (!InternetConnection.checkConnection(context)) {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            JSONObject jsonParams = new JSONObject(new Gson().toJson(params));
            Utils.log("Request : "+Constant.DT_CALENDAR_EVENT_DETAILS+" Params : "+jsonParams);
            final ProgressDialog dialog = new ProgressDialog(DTCalendarEventDetails.this, R.style.TBProgressBar);
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constant.DT_CALENDAR_EVENT_DETAILS,
                    jsonParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            dialog.hide();
                            loadEventData(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Failed to load event details.", Toast.LENGTH_LONG).show();
                        }
                    }
            );
            AppController.getInstance().addToRequestQueue(context, request);
            dialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        iv_eventimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DTCalendarEventDetails.this, ImageZoom.class);
                i.putExtra("imgageurl", imageurl);
                startActivity(i);
            }
        });

    }

    public void finishActivity(View v) {
        finish();
    }

    public void loadEventData(JSONObject response) {

        try {
            JSONObject result = response.getJSONObject("TBPublicEventList");
            Utils.log("Response : "+result);
            if (result.getString("status").equals("0")) {
                JSONObject jsonResult = result.getJSONObject("Result");

                imageurl = jsonResult.getString("eventImg");
                String strEventTitle = jsonResult.getString("eventTitle");
                String strEventDesc = jsonResult.getString("eventDesc");
                String strEventVenue = jsonResult.getString("venue");
                String strEventDateTime = jsonResult.getString("eventDateTime");

                event_title.setText(strEventTitle);
                event_desc.setText(strEventDesc);
                event_venue.setText(strEventVenue);
                String[] date = strEventDateTime.split(" ");
                event_datetime.setText(date[0] + " " + date[1] + " " + date[2] + "   |   " + date[3] + " " + date[4]);
                if (imageurl.trim().equals("")) {
                    linear_image.setVisibility(View.GONE);
                } else {
                    linear_image.setVisibility(View.VISIBLE);
                    try {
                        Picasso.with(context)
                                .load(imageurl)
                                .placeholder(R.drawable.imageplaceholder)
                                .into(iv_eventimg);
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
                    init();
                }
            } else {
                Toast.makeText(context, R.string.messageSorry, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
