package com.SampleApp.row;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 17-12-2015.
 */
public class DTEventDetails extends Activity {
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

        imageurl = getIntent().getStringExtra("eventImage");
        String strEventTitle = getIntent().getStringExtra("eventTitle");
        String strEventDesc = getIntent().getStringExtra("eventDesc");
        String strEventVenue = getIntent().getStringExtra("eventVenue");
        String strEventDateTime = getIntent().getStringExtra("eventDateTime");

        event_title.setText(strEventTitle);
        event_desc.setText(strEventDesc);
        event_venue.setText(strEventVenue);
        String[] date = strEventDateTime.split(" ");
        event_datetime.setText(date[0] + " "+date[1]+" "+date[2]+"   |   " + date[3] +" "+date[4]);
        if ( imageurl.trim().equals("")) {
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
        }
        init();
    }

    private void init() {

        iv_eventimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DTEventDetails.this, ImageZoom.class);
                i.putExtra("imgageurl", imageurl);
                startActivity(i);
            }
        });

    }

    public void finishActivity(View v) {
        finish();
    }
}
