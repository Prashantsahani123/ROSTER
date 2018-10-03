package com.SampleApp.row;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.SampleApp.row.Data.CalendarData;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by USER on 17-12-2015.
 */
public class EventDetails extends Activity {
    String moduleName = "";
    TextView tv_title;
    TextView tv_yes, tv_maybe,txt_reglink;
    TextView tv_no;
    TextView event_title;
    TextView event_desc;
    TextView event_venue;
    TextView event_datetime,event_date,event_time;
    TextView total_count;
    TextView tv_yes_count;
    TextView tv_no_count;
    TextView tv_maybe_count, tv_question;
    ImageView iv_backbutton;
    View yesNopanel, rsvpResponsePanel;
    EditText et_answer;

    TextView tv_question_attending,tv_members_with_you;

    LinearLayout tv_green, tv_red, tv_gray,ll_main,ll_eventDateTime,ll_eventTime,ll_eventLoc,ll_link;

    String flag_callwebsercie = "0";
    String getIsQuesEnable = "";
    String questionType = "";
    //  String Response ;
    String questionText;
    String grpId, eventId, joiningStatus, questionId, answerByme;

    String option1, option2;
    private String eventid;
    ImageView iv_eventimg, iv_actionbtn,iv_actionbtn2;
    private ProgressBar progressbar;
    LinearLayout linear_image;
    String imageurl;
    private String grpID = "0";
    private String memberProfileID = "0";
    private String isAdmin = "No";
    CalendarData data;
    SimpleDateFormat oldFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat newFormat=new SimpleDateFormat("dd MMM yyyy");
    MarshMallowPermission marshMallowPermission;
    //LinearLayout ll_actionbtn,ll_actionbtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.events_detail);

        yesNopanel = findViewById(R.id.YesNoPanel);
        rsvpResponsePanel = findViewById(R.id.rsvpResponsePanel);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        // iv_backbutton.setVisibility(View.GONE);
//        ll_actionbtn = (LinearLayout) findViewById(R.id.ll_actionbtn);
//        ll_actionbtn2 = (LinearLayout) findViewById(R.id.ll_actionbtn2);

        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn.setVisibility(View.VISIBLE); // EDIT ANNOUNCEMEBT
        //ll_actionbtn.setVisibility(View.VISIBLE);
        iv_actionbtn.setImageResource(R.drawable.edit); // EDIT ANNOUNCEMEBT

        iv_actionbtn2 = (ImageView) findViewById(R.id.iv_actionbtn2);

        iv_actionbtn.setVisibility(View.VISIBLE); // EDIT ANNOUNCEMEBT
        //ll_actionbtn.setVisibility(View.VISIBLE);
        iv_actionbtn.setImageResource(R.drawable.edit); // EDIT ANNOUNCEMEBT

        iv_actionbtn2.setVisibility(View.VISIBLE); // EDIT ANNOUNCEMEBT
       // ll_actionbtn2.setVisibility(View.VISIBLE);
        iv_actionbtn2.setImageResource(R.drawable.delete); // EDIT ANNOUNCEMEBT

        moduleName = PreferenceManager.getPreference(this, PreferenceManager.MODUEL_NAME, "Events");
        tv_title.setText("Events");
        tv_yes = (TextView) findViewById(R.id.tv_yes);
        tv_no = (TextView) findViewById(R.id.tv_no);

        event_title = (TextView) findViewById(R.id.event_title);
        event_desc = (TextView) findViewById(R.id.event_desc);
        event_venue = (TextView) findViewById(R.id.event_venue);
        event_datetime = (TextView) findViewById(R.id.event_datetime);
        total_count = (TextView) findViewById(R.id.total_count);
        tv_yes_count = (TextView) findViewById(R.id.tv_yes_count);
        tv_no_count = (TextView) findViewById(R.id.tv_no_count);
        tv_maybe_count = (TextView) findViewById(R.id.tv_maybe_count);
        tv_maybe = (TextView) findViewById(R.id.tv_maybe);
        event_date = (TextView) findViewById(R.id.event_date);
        event_time = (TextView) findViewById(R.id.event_time);
        txt_reglink = (TextView) findViewById(R.id.txt_reglink);

        tv_question_attending = (TextView) findViewById(R.id.tv_question_attending);
        tv_green = (LinearLayout) findViewById(R.id.tv_green);
        tv_red = (LinearLayout) findViewById(R.id.tv_red);
        tv_gray = (LinearLayout) findViewById(R.id.tv_gray);
        iv_eventimg = (ImageView) findViewById(R.id.iv_eventimg);
        linear_image = (LinearLayout) findViewById(R.id.linear_image);
        ll_main = (LinearLayout) findViewById(R.id.ll_main);
        ll_eventDateTime = (LinearLayout) findViewById(R.id.ll_evntDateTime);
        ll_eventTime = (LinearLayout) findViewById(R.id.ll_evntTime);
        ll_eventLoc = (LinearLayout) findViewById(R.id.ll_eventLoc);
        ll_link = (LinearLayout) findViewById(R.id.ll_link);

        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        grpID = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID);
        memberProfileID = PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID);
        isAdmin =PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.IS_GRP_ADMIN,"No");

        Log.d("Touchbase", "ID ID ID :- " + grpID + " - " + memberProfileID);

        Intent intenti = getIntent();
        if (intenti.hasExtra("memID")) {
            memberProfileID = intenti.getStringExtra("memID");
            grpID = intenti.getStringExtra("grpID");
            isAdmin =intenti.getStringExtra("isAdmin");
        }
        Log.d("Touchbase", "ID ID ID AFTER :- " + grpID + " - " + memberProfileID);

//        webservices();

        Bundle intent = getIntent().getExtras();
        if (intent != null) {

            if(intent.containsKey("data")){
                data= (CalendarData) intent.getSerializable("data");
                ll_eventTime.setVisibility(View.GONE);
                ll_eventDateTime.setVisibility(View.VISIBLE);
                setEventData(data);
            }else {
                eventid = intent.getString("eventid");
                if(intent.containsKey("grpID")){
                    grpID=intent.getString("grpID");
                }
                if (InternetConnection.checkConnection(getApplicationContext())) {
                    // Avaliable
                    ll_eventTime.setVisibility(View.VISIBLE);
                    ll_eventDateTime.setVisibility(View.GONE);
                    webservices();
                } else {
                    Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
                    // Not Available...
                }
            }
        }

        //  Log.d("@@@@@@@@", "My Response --- " + Response);
        init();

        adminsettings();

    }

    private void adminsettings() {
        Log.e("isAdmin" ,  "Value of isAdmin is : "+isAdmin);
        if (isAdmin.equals("No")) {
            iv_actionbtn.setVisibility(View.GONE);
            iv_actionbtn2.setVisibility(View.GONE);
//            ll_actionbtn.setVisibility(View.GONE);
//            ll_actionbtn2.setVisibility(View.GONE);
            Log.e("In no : ", "We are in is admin no");
        } else if (isAdmin.equals("Yes")){
            iv_actionbtn.setVisibility(View.VISIBLE);
            iv_actionbtn2.setVisibility(View.VISIBLE);
//            ll_actionbtn.setVisibility(View.VISIBLE);
//            ll_actionbtn2.setVisibility(View.VISIBLE);
            Log.e("Inside yes : ", "We are in is admin yes");
        }
    }

    private void clearselectedtext(String myResponse) {
        if (myResponse.equals("")) {
            //tv_question_attending.setText("");
            tv_question_attending.setVisibility(View.VISIBLE);
            tv_green.setVisibility(View.GONE);
            tv_red.setVisibility(View.GONE);
            tv_gray.setVisibility(View.GONE);
        } else if (myResponse.equals("yes")) {
            tv_question_attending.setVisibility(View.GONE);
            tv_green.setVisibility(View.VISIBLE);
            tv_red.setVisibility(View.GONE);
            tv_gray.setVisibility(View.GONE);
        } else if (myResponse.equals("no")) {
            tv_question_attending.setVisibility(View.GONE);
            tv_red.setVisibility(View.VISIBLE);
            tv_green.setVisibility(View.GONE);
            tv_gray.setVisibility(View.GONE);
        } else if (myResponse.equals("maybe")) {
            tv_question_attending.setVisibility(View.GONE);
            tv_gray.setVisibility(View.VISIBLE);
            tv_green.setVisibility(View.GONE);
            tv_red.setVisibility(View.GONE);
        }
    }

    private void init() {

//        ll_actionbtn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Dialog dialog = new Dialog(EventDetails.this, android.R.style.Theme_Translucent);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.popup_confrm_delete);
//                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
//                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
//                tv_no.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//
//                tv_yes.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//
//                        if (InternetConnection.checkConnection(getApplicationContext())) {
//                            //Utils.showToastWithTitleAndContext(getApplicationContext(), "Delete");
//                            deletewebservices();
//                        } else {
//                            Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");
//
//                        }
//                    }
//                });
//
//                dialog.show();
//            }
//        });


        iv_actionbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(EventDetails.this, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_confrm_delete);
                TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                tv_no.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                tv_yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (InternetConnection.checkConnection(getApplicationContext())) {
                            //Utils.showToastWithTitleAndContext(getApplicationContext(), "Delete");
                            deletewebservices();
                        } else {
                            Utils.showToastWithTitleAndContext(getApplicationContext(), "No Internet Connection!");

                        }
                    }
                });

                dialog.show();
            }

        });

//
//        ll_actionbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if ( InternetConnection.checkConnection(getApplicationContext())) {
//                    Intent i = new Intent(getApplicationContext(), AddEvent.class);
//                    i.putExtra("event_id", eventid);
//                    i.putExtra("edit", "yes");
//                    startActivityForResult(i, 1);
//                } else {
//                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( InternetConnection.checkConnection(getApplicationContext())) {
                    Intent i = new Intent(getApplicationContext(), AddEvent.class);
                    i.putExtra("event_id", eventid);
                    i.putExtra("edit", "yes");
                    startActivityForResult(i, 1);
                } else {
                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ll_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link=txt_reglink.getText().toString();

                if(link!=null && !link.trim().isEmpty()){
                    Intent intent=new Intent(EventDetails.this,OpenLinkActivity.class);
                    intent.putExtra("link",link);
                    startActivity(intent);
                }
            }
        });

        tv_yes.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View arg0) {

                joiningStatus = "yes";

                if (getIsQuesEnable.equals("2") || getIsQuesEnable.equals("1")) {
                    final Dialog dialog = new Dialog(EventDetails.this, android.R.style.Theme_Translucent);
                    Log.d("@@@@@@@@", "questionType --- " + questionType);
                    if (questionType.equals("1")) {
                        // final Dialog dialog = new Dialog(EventDetails.this, android.R.style.Theme_Translucent);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.event_alert);

                        tv_members_with_you = (TextView)dialog.findViewById(R.id.tv_members_with_you);
                        et_answer = (EditText)dialog.findViewById(R.id.et_member);
                        ImageView iv_close = (ImageView)dialog.findViewById(R.id.iv_close);

                        iv_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        tv_members_with_you.setText(questionText);


                        final TextView tv_submit = (TextView) dialog.findViewById(R.id.tv_submit);



                        tv_submit.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                answerByme = et_answer.getText().toString();
                                AnsweringEventwebservices();
                                Utils.hideKeyBoard(EventDetails.this,tv_submit);
                                dialog.dismiss();

                            }
                        });

                        dialog.show();

                    } else if (questionType.equals("2")) {
                        // final Dialog dialog = new Dialog(EventDetails.this, android.R.style.Theme_Translucent);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                        dialog.setContentView(R.layout.event_alert_objective_que);


                        TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
                        TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
                        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
                        tv_question = (TextView) dialog.findViewById(R.id.tv_question);
                        tv_question.setText(questionText);

                        tv_question.setText(questionText);
                        tv_yes.setText(option1);
                        tv_no.setText(option2);

                        tv_question.setMovementMethod(new ScrollingMovementMethod());
                        tv_yes.setMovementMethod(new ScrollingMovementMethod());
                        tv_no.setMovementMethod(new ScrollingMovementMethod());

                        iv_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        tv_yes.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // dialog.dismiss();
                                answerByme = option1;
                                AnsweringEventwebservices();
                                dialog.dismiss();

                            }
                        });
                        tv_no.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                answerByme = option2;
                                AnsweringEventwebservices();
                                dialog.dismiss();
                            }
                        });

                        dialog.show();


                    } else {
                        // dialog.dismiss();
                    }

                } else if (getIsQuesEnable.equals("0")) {
                    AnsweringEventwebservices();
                }

            }
        });

        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                joiningStatus = "no";
                answerByme = "";

                AnsweringEventwebservices();
            }
        });

        tv_maybe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joiningStatus = "maybe";
                answerByme = "";
                AnsweringEventwebservices();
            }
        });

        iv_eventimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventDetails.this, ImageZoom.class);
                i.putExtra("imgageurl", imageurl);
                startActivity(i);
            }
        });


        ll_eventLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!event_venue.getText().toString().isEmpty())
                {
                    marshMallowPermission = new MarshMallowPermission(EventDetails.this);

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

    public void finishActivity(View v) {
        finish();
    }

    private void deletewebservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("typeID", eventid));
        arrayList.add(new BasicNameValuePair("type", "Event"));
        //   arrayList.add(new BasicNameValuePair("searchText",""));
        arrayList.add(new BasicNameValuePair("profileID", memberProfileID));

        flag_callwebsercie = "2";
        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        Log.d("Response", "PARAMETERS " + Constant.DeleteByModuleName + " :- " + arrayList.toString());
        new WebConnectionEventDetail(Constant.DeleteByModuleName, arrayList, EventDetails.this).execute();
    }

    private void webservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("grpId", grpID));//PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GROUP_ID)
        arrayList.add(new BasicNameValuePair("eventID", eventid));//eventid
        arrayList.add(new BasicNameValuePair("groupProfileID", memberProfileID));//PreferenceManager.getPreference(getApplicationContext(), PreferenceManager.GRP_PROFILE_ID)
        flag_callwebsercie = "0";
        Log.d("Response", "PARAMETERS " + Constant.GetEventDetails + " :- " + arrayList.toString());
        if ( InternetConnection.checkConnection(getApplicationContext())) {
            new WebConnectionEventDetail(Constant.GetEventDetails, arrayList, EventDetails.this).execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void AnsweringEventwebservices() {
        List<NameValuePair> arrayList = new ArrayList<NameValuePair>();
        arrayList.add(new BasicNameValuePair("grpId", grpID));
        arrayList.add(new BasicNameValuePair("eventId", eventid));
        arrayList.add(new BasicNameValuePair("profileID", memberProfileID));
        arrayList.add(new BasicNameValuePair("joiningStatus", joiningStatus));
        arrayList.add(new BasicNameValuePair("questionId", questionId));
        arrayList.add(new BasicNameValuePair("answerByme", answerByme));

        //new  WebConnectionAsyncLogin("http://192.168.2.3:8059/api/values/getPhotosList",arrayList,Gallery.this).execute();
        flag_callwebsercie = "1";
        Log.d("Response", "PARAMETERS " + Constant.AnsweringEvent + " :- " + arrayList.toString());
        if (InternetConnection.checkConnection(getApplicationContext())) {
            new WebConnectionEventDetail(Constant.AnsweringEvent, arrayList, EventDetails.this).execute();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    public class WebConnectionEventDetail extends AsyncTask<String, Object, Object> {

        String val = null;
        final ProgressDialog progressDialog = new ProgressDialog(EventDetails.this, R.style.TBProgressBar);
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
                if (flag_callwebsercie.equals("0")) {
                    getEventDetailsItems(result.toString());
                } else if(flag_callwebsercie.equals("1")) {
                    getAnsweringItems(result.toString());
                }else if(flag_callwebsercie.equals("2")) {
                    getdata(result.toString());
                }

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
                    total_count.setText(objects.getString("totalCount").toString());
                    tv_yes_count.setText(objects.getString("goingCount").toString());
                    tv_maybe_count.setText(objects.getString("maybeCount").toString());
                    tv_no_count.setText(objects.getString("notgoingCount").toString());
                    event_venue.setText(objects.getString("venue").toString());
                    getIsQuesEnable = objects.getString("isQuesEnable").toString();
                    grpId = objects.getString("grpID").toString();
                    eventId = objects.getString("eventID").toString();
                    String link=objects.getString("reglink");
                    if(link!=null && !link.isEmpty()){
                        ll_link.setVisibility(View.VISIBLE);
                        txt_reglink.setText(link);
                    }else {
                        txt_reglink.setText("");
                        ll_link.setVisibility(View.GONE);
                    }

                    String rsvpEnabled = objects.getString("rsvpEnable").toString();
                    Log.e("rsvpEnabled", "Value of rsvpEnabled : "+rsvpEnabled);
                    if ( rsvpEnabled.equals("1")) {
                        yesNopanel.setVisibility(View.VISIBLE);
                        rsvpResponsePanel.setVisibility(View.VISIBLE);
                        String Response = objects.getString("myResponse").toString();
                        clearselectedtext(objects.getString("myResponse").toString());

                   /* JSONArray questionArray = objects.getJSONArray("questionArray");
                    for (int j = 0; j < questionArray.length(); j++) {
                        JSONObject qobject = questionArray.getJSONObject(j);
                        JSONObject questionList = qobject.getJSONObject("QuestionList");*/

                        questionType = objects.getString("questionType").toString();
                        questionText = objects.getString("questionText").toString();
                        questionId = objects.getString("questionId").toString();
                        option1 = objects.getString("option1").toString();
                        option2 = objects.getString("option2").toString();
                        clearselectedtext(objects.getString("myResponse").toString());
                    } else {
                        yesNopanel.setVisibility(View.GONE);
                        rsvpResponsePanel.setVisibility(View.GONE);
                        tv_gray.setVisibility(View.GONE);
                        tv_green.setVisibility(View.GONE);
                        tv_red.setVisibility(View.GONE);
                    }

                    if(objects.getString("filterType").toString().equals("3")){
                        iv_actionbtn.setVisibility(View.GONE);
                       // ll_actionbtn.setVisibility(View.GONE);
                        yesNopanel.setVisibility(View.GONE);
                        rsvpResponsePanel.setVisibility(View.GONE);
                        tv_gray.setVisibility(View.GONE);
                        tv_green.setVisibility(View.GONE);
                        tv_red.setVisibility(View.GONE);
                    }

                    String thisGrpId=PreferenceManager.getPreference(EventDetails.this,PreferenceManager.GROUP_ID,"");
                    if(!thisGrpId.equalsIgnoreCase(grpId)){
                        yesNopanel.setVisibility(View.GONE);
                        rsvpResponsePanel.setVisibility(View.GONE);
                        tv_gray.setVisibility(View.GONE);
                        tv_green.setVisibility(View.GONE);
                        tv_red.setVisibility(View.GONE);
                        iv_actionbtn.setVisibility(View.GONE);
                        iv_actionbtn2.setVisibility(View.GONE);
                    }

                    if (objects.has("eventImg")) {

                        if (objects.getString("eventImg").toString().length() == 0 || objects.getString("eventImg").toString() == null) {
                            linear_image.setVisibility(View.GONE);
                        } else {
                            linear_image.setVisibility(View.VISIBLE);
                            imageurl = objects.getString("eventImg").toString();
                            progressbar.setVisibility(View.VISIBLE);
                            Picasso.with(EventDetails.this).load(objects.getString("eventImg").toString())
                                    .placeholder(R.drawable.edit_image)
                                    .into(iv_eventimg, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                            progressbar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError() {
                                            progressbar.setVisibility(View.GONE);
                                        }
                                    });
                        }
                    }
                    //Picasso.with(this).load("").placeholder()

                    // Log.d("@@@@@@@@@", "---------" + questionType);

                 /*   }*/

                }

            }else{
                Utils.showToastWithTitleAndContext(getApplicationContext(), "Service Failed");
                finish();
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    private void getAnsweringItems(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("EventJoinResult");
            final String status = EventResult.getString("status");
            if (status.equals("0")) ;
            {
                String msg = EventResult.getString("message");


                String gngCnt = EventResult.getString("goingCount");
                int yes = 0;
                int no = 0;
                int maybe = 0;
                int total = 0;


                tv_yes_count.setText(EventResult.getString("goingCount").toString());
                tv_maybe_count.setText(EventResult.getString("maybeCount").toString());
                tv_no_count.setText(EventResult.getString("notgoingCount").toString());

                yes = Integer.parseInt(EventResult.getString("goingCount").toString());
                no = Integer.parseInt(EventResult.getString("notgoingCount").toString());
                maybe = Integer.parseInt(EventResult.getString("maybeCount").toString());
                total = yes + no + maybe;

                total_count.setText("" + String.valueOf(total));

                //Integer.parseInt(EventResult.getString("goingCount").toString())
                clearselectedtext(EventResult.getString("myResponse").toString());

            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 1) {
            webservices();

        }
    }

    private void getdata(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject EventResult = jsonObj.getJSONObject("DeleteResult");
            final String status = EventResult.getString("status");
            if (status.equals("0"))
            {
                //Intent i = new Intent(Announcement_details.this,Announcement.class);
                //startActivityForResult(i,1);
                Intent intent = new Intent();
                setResult(1, intent);
                finish();//finishing activity
                finish();
                Utils.showToastWithTitleAndContext(getApplicationContext(), "Event Deleted Successfully");
            }else{
                Utils.showToastWithTitleAndContext(getApplicationContext(), "Could not DELETE, please Try Again!");
            }

        } catch (Exception e) {
            Log.d("exec", "Exception :- " + e.toString());
        }
    }

    private void setEventData(CalendarData data){
        String imgUrl=data.getEventImg();

        if(imgUrl!=null && !imgUrl.isEmpty()){
            linear_image.setVisibility(View.VISIBLE);
            progressbar.setVisibility(View.VISIBLE);
            Picasso.with(EventDetails.this).load(imgUrl)
                    .placeholder(R.drawable.imageplaceholder)
                    .into(iv_eventimg, new Callback() {
                        @Override
                        public void onSuccess() {

                            progressbar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            progressbar.setVisibility(View.GONE);
                        }
                    });
        }
        else {
            linear_image.setVisibility(View.GONE);
        }

        event_title.setText(data.getTitle());
        event_desc.setText(data.getDescription());
        event_time.setText(data.getEventTime());

        Date date=null;

        try {
            date=oldFormat.parse(data.getEventDate());
            event_date.setText(newFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
