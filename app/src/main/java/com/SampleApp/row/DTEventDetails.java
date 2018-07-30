package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.HttpConnection;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.MarshMallowPermission;
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
    LinearLayout linear_image,ll_eventLoc;
    String imageurl;
    ProgressBar progressBar;

    private Context context;
    private String grpID,memberProfileID,eventId;
    private MarshMallowPermission marshMallowPermission;

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
        ll_eventLoc = (LinearLayout) findViewById(R.id.ll_eventLoc);
        
        grpID = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID);
        memberProfileID = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID);
        Bundle bundle=getIntent().getExtras();
        String strEventTitle="",strEventDesc="",strEventVenue="",strEventDateTime="";
        if(bundle!=null){
            if(bundle.containsKey("eventid")){
                eventId=bundle.getString("eventid");
                if (InternetConnection.checkConnection(getApplicationContext())) {
                    webservices();
                } else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }
            }else {
                imageurl = getIntent().getStringExtra("eventImage");
               strEventTitle = getIntent().getStringExtra("eventTitle");
               strEventDesc = getIntent().getStringExtra("eventDesc");
               strEventVenue = getIntent().getStringExtra("eventVenue");
               strEventDateTime = getIntent().getStringExtra("eventDateTime");

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
                                .placeholder(R.drawable.edit_image)
                                .into(iv_eventimg);
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
                }
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


        ll_eventLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!event_venue.getText().toString().isEmpty())
                {
                    marshMallowPermission = new MarshMallowPermission(DTEventDetails.this);

                /*Intent i = new Intent(mContext, Map.class);
                Bundle bundle = new Bundle();
                //Add your data from getFactualResults method to bundle
                bundle.putString("Long", item.getVenueLon());
                bundle.putString("Lat", item.getVenueLat());
                i.putExtras(bundle);*/
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=" + event_venue.getText().toString()));

                    if (!marshMallowPermission.checkPermissionForLocation()) {
                        marshMallowPermission.requestPermissionForLocation();
                    }else {
                        // mContext.startActivity(i);
                        startActivity(intent);
                    }

               /* if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    mContext.startActivity(i);
                }*/
                }

            }
        });

    }

    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("grpId", grpID));//PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)
        arrayList.add(new BasicNameValuePair("eventID", eventId));//eventid
        arrayList.add(new BasicNameValuePair("groupProfileID", memberProfileID));//PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)

        Log.d("Response", "PARAMETERS " + Constant.GetEventDetails + " :- " + arrayList.toString());
        if ( InternetConnection.checkConnection(getApplicationContext())) {
            new WebConnectionEventDetail(Constant.GetEventDetails, arrayList, DTEventDetails.this).execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    public class WebConnectionEventDetail extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(DTEventDetails.this, R.style.TBProgressBar);
        Context context = null;
        String url = null;
        List<NameValuePair> argList = null;

        public WebConnectionEventDetail(String url, List<NameValuePair> argList, Context ctx) {
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
            //	Log.d("response","Do post"+ result.toString());

            if (result != "") {
                getEventDetailsItems(result.toString());
            } else {
                Log.d("Response", "Null Resposnse");
            }

        }

    }

    private void getEventDetailsItems(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("EventsListDetailResult");
            final String status = EventResult.getString("status");
            if (status.equals("0"))
            {
                JSONArray EventListResdult = EventResult.getJSONArray("EventsDetailResult");
                for (int i = 0; i < EventListResdult.length(); i++) {

                    JSONObject object = EventListResdult.getJSONObject(i);
                    JSONObject objects = object.getJSONObject("EventsDetail");

                    event_title.setText(objects.getString("eventTitle").toString());
                    event_desc.setText(objects.getString("eventDesc").toString());

                    event_datetime.setText(objects.getString("eventDateTime").toString());

                    event_venue.setText(objects.getString("venue").toString());

                    eventId = objects.getString("eventID").toString();

                    if (objects.has("eventImg")) {

                        if (objects.getString("eventImg").toString().length() == 0 || objects.getString("eventImg").toString() == null) {
                            linear_image.setVisibility(View.GONE);
                        } else {
                            linear_image.setVisibility(View.VISIBLE);
                            imageurl = objects.getString("eventImg").toString();
                            progressBar.setVisibility(View.VISIBLE);
                            Picasso.with(DTEventDetails.this).load(objects.getString("eventImg").toString())
                                    .placeholder(R.drawable.imageplaceholder)
                                    .into(iv_eventimg, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                            progressBar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError() {
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                        }
                    }
                }

            }else{
                Utils.showToastWithTitleAndContext(getApplicationContext(), "Service Failed");
                finish();
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    public void finishActivity(View v) {
        finish();
    }
}
