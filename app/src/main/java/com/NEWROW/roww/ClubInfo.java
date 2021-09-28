package com.NEWROW.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Data.ClubInfoData;
import com.NEWROW.row.Data.FindAClubResultData;
import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.Utils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * Created by admin on 30-05-2017.
 */

public class ClubInfo extends Activity {
    private TextView tv_title, tv_clubname, tv_clubId, tv_districtId, tv_address,
            tv_meetingday, tv_meetingtime, tv_website;
    private ImageView iv_backbutton;
    private String authtoken = "";
    private FindAClubResultData clubData;
    Context context;
    LinearLayout ll_meeting_location_time;

    private ArrayList<ClubInfoData> clubInfolist = new ArrayList<>();

    LinearLayout ll_clubdetails, ll_website, ll_address;
    public View view_clubDetails;

    private ImageView iv_location;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clubinfo);
        context = this;
        clubInfolist = new ArrayList<>();

        actionbarfunction();
        init();

        Intent intent = getIntent();
        if (intent.hasExtra("auth_token")) {
            authtoken = intent.getStringExtra("auth_token");
        }

        if (intent.hasExtra("clubData")) {
            try {
                clubData = (FindAClubResultData) intent.getExtras().getSerializable("clubData");
//                tv_clubname.setText(clubData.getClubName() + "," + clubData.getLocation().getInternationalProvince());
//                tv_clubId.setText(String.valueOf(clubData.getClubId()));
//                districtId.setText(clubData.getDistrict());
//                website.setText(clubData.getWebsite());

                tv_clubname.setText(clubData.getClubName());
                tv_clubId.setText(clubData.getClubId());
            } catch (ClassCastException cce) {
                cce.printStackTrace();
            }
        }
        //findClubAddress();
        loadClubDetails();


    }

    public void loadClubDetails() {
        if (InternetConnection.checkConnection(this)) {
            try {
                Hashtable<String, String> params = new Hashtable<>();
                params.put("grpId", clubData.getGrpID());

                try {
                    final ProgressDialog progressDialog = new ProgressDialog(context, R.style.TBProgressBar);
                    progressDialog.setCancelable(false);
                    progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                    progressDialog.show();

                    JSONObject requestData = new JSONObject(new Gson().toJson(params));
                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.POST,
                            Constant.GETCLUBDETAILS,
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
                                    Toast.makeText(context, "Failed to receive Data from server . Please try again.", Toast.LENGTH_LONG).show();

                                }
                            }
                    );

                    request.setRetryPolicy(new DefaultRetryPolicy(
                            Constant.VOLLEY_MAX_REQUEST_TIMEOUT,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));
                    AppController.getInstance().addToRequestQueue(context, request);


                } catch (JSONException jse) {
                    jse.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "No internet conenction", Toast.LENGTH_LONG).show();
        }
    }

    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText("Find a Club");
    }

    private void init() {
        tv_clubname = (TextView) findViewById(R.id.tv_clubname);
        tv_clubId = (TextView) findViewById(R.id.clubId);
        tv_districtId = (TextView) findViewById(R.id.districtId);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_meetingday = (TextView) findViewById(R.id.tv_meetingday);
        tv_meetingtime = (TextView) findViewById(R.id.tv_meetingtime);
        ll_meeting_location_time = (LinearLayout) findViewById(R.id.ll_meeting_location_time);
        ll_clubdetails = (LinearLayout) findViewById(R.id.ll_clubdetails);
        tv_website = (TextView) findViewById(R.id.website);

        view_clubDetails = LayoutInflater.from(context).inflate(R.layout.layout_clubdetails, null, false);
        ll_website = (LinearLayout) findViewById(R.id.ll_website);
        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        iv_location = (ImageView) findViewById(R.id.iv_location);
    }

//    public void findClubAddress() {
//        try {
//            String SEARCH_CLUB_ADDRESS_API = "https://apiuat.rotary.org:8443/v1.1/clubs/Rotary%20Club/" + clubData.getClubId() + "/addresses";
//            String url = SEARCH_CLUB_ADDRESS_API;
//            Utils.log("Search URL is : " + url);
//
//            new SearchClubAddressTask().execute(url);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public class SearchClubAddressTask extends AsyncTask<String, Void, String> {
//        ProgressDialog progressDialog = new ProgressDialog(context, R.style.TBProgressBar);
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog.setCancelable(false);
//            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            URL url = null;
//            try {
//                url = new URL(params[0]);
//                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
//                con.setRequestProperty("auth_token", authtoken);
//                con.setRequestMethod("GET");
//                con.setConnectTimeout(120000);
//                con.setDoInput(true);
//
//                InputStream in = con.getInputStream();
//                BufferedReader br = new BufferedReader(new InputStreamReader(in));
//
//                String line = "";
//                StringBuffer buffer = new StringBuffer();
//                while ((line = br.readLine()) != null) {
//                    buffer.append(line);
//                }
//                br.close();
//                in.close();
//                return new String(buffer);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (ProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//            return null;
//        }
//
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            progressDialog.dismiss();
//            Utils.log("Response is : " + s);
//            getResult(s);
//        }
//    }

    public void getResult(String result) {
        try {
            JSONObject json = new JSONObject(result);
            JSONObject jsonTBGetClubDetailResult = json.getJSONObject("TBGetClubDetailResult");
            final String status = jsonTBGetClubDetailResult.getString("status");
            if (status.equalsIgnoreCase("0")) {
                JSONObject jsonClubDetailResult = jsonTBGetClubDetailResult.getJSONObject("ClubDetailResult");
                String clubId = jsonClubDetailResult.getString("clubId");
                String districtId = jsonClubDetailResult.getString("districtId");
                String clubName = jsonClubDetailResult.getString("clubName");
                String address = jsonClubDetailResult.getString("address");
                String city = jsonClubDetailResult.getString("city");
                String state = jsonClubDetailResult.getString("state");
                String country = jsonClubDetailResult.getString("country");
                String meetingDay = jsonClubDetailResult.getString("meetingDay");
                String meetingTime = jsonClubDetailResult.getString("meetingTime");
                final String clubWebsite = jsonClubDetailResult.getString("clubWebsite");
                final String lat = jsonClubDetailResult.getString("lat");
                final String longi = jsonClubDetailResult.getString("longi");


                if ((!lat.equalsIgnoreCase("") && !lat.equalsIgnoreCase("null")) &&
                        (!longi.equalsIgnoreCase("") && !longi.equalsIgnoreCase("null"))) {

                    iv_location.setVisibility(View.VISIBLE);
                    iv_location.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                /*Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("geo: " + lat + "," + longi));
                                startActivity(i);*/

                                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?q=loc:" + lat + "," + longi+"&name=Prasad"));
                                startActivity(i);


                            } catch (ActivityNotFoundException anfe) {
                                anfe.printStackTrace();
                            }
                        }
                    });

                }

                tv_clubname.setText(clubName);
                tv_clubId.setText(clubId);
                tv_districtId.setText(districtId);
                if (!address.equalsIgnoreCase("") && !address.equalsIgnoreCase("null")) {
                    ll_address.setVisibility(View.VISIBLE);
                    tv_address.setText(address);
                }
                tv_meetingday.setText(meetingDay);
                tv_meetingtime.setText(meetingTime);
                if (clubWebsite.equalsIgnoreCase("") || clubWebsite.equalsIgnoreCase("null") || clubWebsite.equalsIgnoreCase("Not Known")) {

                } else {
                    ll_website.setVisibility(View.VISIBLE);
                    tv_website.setText(clubWebsite);
                }

                ll_website.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (InternetConnection.checkConnection(context)) {
                            Intent i = new Intent(ClubInfo.this, OpenLinkActivity.class);
                            i.putExtra("link", clubWebsite);
                            i.putExtra("modulename", "Find a Club");
                            startActivity(i);
                        } else {
                            Toast.makeText(context, "No internet conenction", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                //ll_clubdetails.removeAllViews();
                JSONArray jsonPresident = jsonClubDetailResult.getJSONArray("president");
                int count_president = jsonPresident.length();
                if (count_president > 0) {
                    for (int i = 0; i < count_president; i++) {
                        JSONObject jsonPres = jsonPresident.getJSONObject(i);
                        String name = jsonPres.getString("name");
                        final String mobileNumber = jsonPres.getString("mobileNo");
                        final String emailId = jsonPres.getString("emailId");
                        view_clubDetails = LayoutInflater.from(context).inflate(R.layout.layout_clubdetails, null);
                        TextView tvLabel = (TextView) view_clubDetails.findViewById(R.id.tv_label);
                        TextView tvValue = (TextView) view_clubDetails.findViewById(R.id.tv_value);
                        ImageView imgCall = (ImageView) view_clubDetails.findViewById(R.id.iv_call);
                        ImageView imgMessage = (ImageView) view_clubDetails.findViewById(R.id.iv_message);
                        ImageView imgMail = (ImageView) view_clubDetails.findViewById(R.id.iv_mail);

                        if (!mobileNumber.equalsIgnoreCase("") && !mobileNumber.equalsIgnoreCase("null")) {
                            imgCall.setImageDrawable(getResources().getDrawable(R.drawable.call_blue));
                            imgMessage.setImageDrawable(getResources().getDrawable(R.drawable.message_blue));

                            imgCall.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + mobileNumber));
                                    startActivity(callIntent);
                                }
                            });

                            imgMessage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent msgIntent = new Intent(Intent.ACTION_VIEW);
                                    //msgIntent.setType("vnd.android-dir/mms-sms");
                                    String address = Utils.implode(", ", Arrays.asList(mobileNumber));
                                    //msgIntent.putExtra("address", );
                                    msgIntent.setData(Uri.parse("smsto: "+Uri.encode(address)));
                                    startActivity(msgIntent);
                                }
                            });

                        }

                        if (!emailId.equalsIgnoreCase("") && !emailId.equalsIgnoreCase("null")) {
                            imgMail.setImageDrawable(getResources().getDrawable(R.drawable.mail_blue));
                            imgMail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                                    emailIntent.setType("plain/text");
                                    emailIntent.setData(Uri.parse("mailto:"));
                                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailId});
                                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                                    startActivity(emailIntent);
                                }
                            });
                        }


                        tvLabel.setText("President");
                        tvValue.setText(name);
                        ll_clubdetails.addView(view_clubDetails);
                    }
                }

                JSONArray jsonSecretary = jsonClubDetailResult.getJSONArray("secretary");
                int count_secretary = jsonSecretary.length();
                if (count_secretary > 0) {
                    for (int i = 0; i < count_secretary; i++) {
                        JSONObject jsonSecre = jsonSecretary.getJSONObject(i);
                        String name = jsonSecre.getString("name");
                        final String mobileNumber = jsonSecre.getString("mobileNo");
                        final String emailId = jsonSecre.getString("emailId");
                        view_clubDetails = LayoutInflater.from(context).inflate(R.layout.layout_clubdetails, null);
                        TextView tvLabel = (TextView) view_clubDetails.findViewById(R.id.tv_label);
                        TextView tvValue = (TextView) view_clubDetails.findViewById(R.id.tv_value);
                        ImageView imgCall = (ImageView) view_clubDetails.findViewById(R.id.iv_call);
                        ImageView imgMessage = (ImageView) view_clubDetails.findViewById(R.id.iv_message);
                        ImageView imgMail = (ImageView) view_clubDetails.findViewById(R.id.iv_mail);

                        if (!mobileNumber.equalsIgnoreCase("") && !mobileNumber.equalsIgnoreCase("null")) {
                            imgCall.setImageDrawable(getResources().getDrawable(R.drawable.call_blue));
                            imgMessage.setImageDrawable(getResources().getDrawable(R.drawable.message_blue));

                            imgCall.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + mobileNumber));
                                    startActivity(callIntent);
                                }
                            });

                            imgMessage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent msgIntent = new Intent(Intent.ACTION_VIEW);
                                    msgIntent.setType("vnd.android-dir/mms-sms");
                                    String address = Utils.implode(", ", Arrays.asList(mobileNumber));
                                    msgIntent.putExtra("address", address);
                                    startActivity(msgIntent);
                                }
                            });

                        }

                        if (!emailId.equalsIgnoreCase("") && !emailId.equalsIgnoreCase("null")) {
                            imgMail.setImageDrawable(getResources().getDrawable(R.drawable.mail_blue));
                            imgMail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                                    emailIntent.setType("plain/text");
                                    emailIntent.setData(Uri.parse("mailto:"));
                                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailId});
                                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                                    startActivity(emailIntent);
                                }
                            });

                        }

                        tvLabel.setText("Secretary");
                        tvValue.setText(name);
                        ll_clubdetails.addView(view_clubDetails);
                    }
                }

                JSONArray jsonDistrictGovernor = jsonClubDetailResult.getJSONArray("districtGovernor");
                int count_district_governor = jsonDistrictGovernor.length();
                if (count_district_governor > 0) {
                    for (int i = 0; i < count_district_governor; i++) {
                        JSONObject jsonGovernor = jsonDistrictGovernor.getJSONObject(i);
                        String name = jsonGovernor.getString("name");
                        final String mobileNumber = jsonGovernor.getString("mobileNo");
                        final String emailId = jsonGovernor.getString("emailId");
                        view_clubDetails = LayoutInflater.from(context).inflate(R.layout.layout_clubdetails, null);
                        TextView tvLabel = (TextView) view_clubDetails.findViewById(R.id.tv_label);
                        TextView tvValue = (TextView) view_clubDetails.findViewById(R.id.tv_value);
                        ImageView imgCall = (ImageView) view_clubDetails.findViewById(R.id.iv_call);
                        ImageView imgMessage = (ImageView) view_clubDetails.findViewById(R.id.iv_message);
                        ImageView imgMail = (ImageView) view_clubDetails.findViewById(R.id.iv_mail);

                        if (!mobileNumber.equalsIgnoreCase("") && !mobileNumber.equalsIgnoreCase("null")) {
                            imgCall.setImageDrawable(getResources().getDrawable(R.drawable.call_blue));
                            imgMessage.setImageDrawable(getResources().getDrawable(R.drawable.message_blue));
                            imgCall.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + mobileNumber));
                                    startActivity(callIntent);
                                }
                            });

                            imgMessage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent msgIntent = new Intent(Intent.ACTION_VIEW);
                                    msgIntent.setType("vnd.android-dir/mms-sms");
                                    String address = Utils.implode(", ", Arrays.asList(mobileNumber));
                                    msgIntent.putExtra("address", address);
                                    startActivity(msgIntent);
                                }
                            });

                        }

                        if (!emailId.equalsIgnoreCase("") && !emailId.equalsIgnoreCase("null")) {
                            imgMail.setImageDrawable(getResources().getDrawable(R.drawable.mail_blue));
                            imgMail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                                    emailIntent.setType("plain/text");
                                    emailIntent.setData(Uri.parse("mailto:"));
                                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailId});
                                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                                    startActivity(emailIntent);
                                }
                            });
                        }

                        tvLabel.setText("District Governor");
                        tvValue.setText(name);
                        ll_clubdetails.addView(view_clubDetails);
                    }
                }

                if (count_president == 0 && count_secretary == 0 && count_district_governor == 0) {
                    ll_clubdetails.setVisibility(View.GONE);
                }
            }
//            JSONArray json = new JSONArray(result);
//            int count = json.length();
//            if (count > 0) {
//                clubInfolist.clear();
//                Type listType = new TypeToken<List<ClubInfoData>>() {
//                }.getType();
//                clubInfolist = new Gson().fromJson(result, listType);
//
//
//                if (count == 1) {
//
//                    tv_meeting_address.setText(clubInfolist.get(0).getAddressLine1() + ","
//                            + clubInfolist.get(0).getAddressLine2() + ","
//                            + clubInfolist.get(0).getAddressLine3());
//
//                    if (clubInfolist.get(0).getMeetingDay() != null) {
//                        ll_meeting_location_time.setVisibility(View.VISIBLE);
//                        tv_meetingday.setText(clubInfolist.get(0).getMeetingDay());
//
//                    }
//                    if (clubInfolist.get(0).getMeetingTime() != null) {
//                        ll_meeting_location_time.setVisibility(View.VISIBLE);
//                        tv_meetingtime.setText(clubInfolist.get(0).getMeetingTime());
//                    }
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
