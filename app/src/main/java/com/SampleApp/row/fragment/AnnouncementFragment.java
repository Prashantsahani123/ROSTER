package com.SampleApp.row.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Data.CalendarData;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by USER on 05-12-2017.
 */

public class AnnouncementFragment extends Fragment {

    Bundle bundle;
    LinearLayout ll_cont,ll_today,ll_update,ll_error;
    Calendar calendar;
    String selectedDate,celebrationType;
    ArrayList<CalendarData> eventList;
    ProgressDialog progressDialog;

    SimpleDateFormat newFormat=new SimpleDateFormat("dd MM");
    SimpleDateFormat oldFormat=new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf=new SimpleDateFormat("dd MMMM");
    String filterDate;

    boolean isVisible,isLoaded;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_calender, container, false);

        calendar = Calendar.getInstance();

        eventList = new ArrayList<>();
        ll_cont=(LinearLayout)view.findViewById(R.id.ll_cont);
        ll_cont.removeAllViews();
        ll_today=(LinearLayout)view.findViewById(R.id.ll_today);
        ll_today.removeAllViews();
        ll_update=(LinearLayout)view.findViewById(R.id.ll_update);
        ll_error=(LinearLayout)view.findViewById(R.id.ll_error);

        bundle = getArguments();
        selectedDate = bundle.getString("selectedDate");
//        selectedDate = "2018-02-11";
        celebrationType = bundle.getString("announcement");

        Date date=null;

        try {
            date=oldFormat.parse(selectedDate);
            filterDate=newFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        celebrationWebservices();


        // Inflate the layout for this fragment
        return view;
    }

    private void celebrationWebservices() {

        Utils.log("In Announcement");
        if (InternetConnection.checkConnection(getContext())) {
            try {

                JSONObject requestData = new JSONObject();
                requestData.put("GroupID", PreferenceManager.getPreference(getActivity().getApplicationContext(),PreferenceManager.GROUP_ID));
                requestData.put("ProfileID",PreferenceManager.getPreference(getActivity().getApplicationContext(),PreferenceManager.GRP_PROFILE_ID));
                requestData.put("SelectedDate",selectedDate);
                requestData.put("Type",celebrationType);
                requestData.put("groupCategory",PreferenceManager.getPreference(getActivity(),PreferenceManager.MY_CATEGORY));
                Utils.log(""+requestData);

                progressDialog = new ProgressDialog(getActivity(),R.style.TBProgressBar);
                progressDialog.setCancelable(true);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                progressDialog.setMessage("Loading...Please Wait");
                progressDialog.show();



                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.GetMonthEventListTypeWise, requestData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

//                        getAnnouncementList(response);
                        getAnnouncementList(response);
                        Utils.log(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Utils.showToastWithTitleAndContext(getActivity(),"Something went wrong. Please try again later");
                        Utils.log("VollyError:- " + error);
                    }
                });

                request.setRetryPolicy(
                        new DefaultRetryPolicy(120000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                AppController.getInstance().addToRequestQueue(getActivity(), request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

//    private void getAnnouncementList(JSONObject  responce) {
//        try {
//            JSONObject eventListObject = responce.getJSONObject("TBEventListTypeResult");
//            String status = eventListObject.getString("status");
//
//            if(status.equals("0")){
//                JSONObject result = eventListObject.getJSONObject("Result");
//                JSONArray resultArray = result.getJSONArray("Events");
//                ArrayList<CalendarData> dataArrayList=new ArrayList<>();
//
//                for(int j=0;j<resultArray.length();j++){
//                    JSONObject object=resultArray.getJSONObject(j);
//
//                    String eventDate = object.getString("eventDate");
//
//                    Date date1 = null;
//                    try {
//                        date1 = oldFormat.parse(eventDate);
//                        eventDate = newFormat.format(date1);
//
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//                    if(filterDate.equals(eventDate)){
//                        CalendarData data = new CalendarData();
//                        data.setEventDate(object.getString("eventDate"));
//                        data.setTitle(object.getString("title"));
//                        String email=object.getString("EmailId");
//                        JSONArray EmailIds=object.getJSONArray("EmailIds");
//                        String number=object.getString("ContactNumber");
//                        JSONArray MobileNo=object.getJSONArray("MobileNo");
//
//                        if(number!=null && !number.isEmpty()){
//                            data.setContactNumber(number);
//                        }else if(MobileNo!=null && MobileNo.length()>0){
//                            ArrayList<CalendarData> mobileList=new ArrayList<>();
//                            for(int k=0;k<MobileNo.length();k++){
//                                JSONObject mobileObject=MobileNo.getJSONObject(k);
//                                CalendarData mobileData=new CalendarData();
//                                mobileData.setMemberName(mobileObject.getString("MemberName"));
//                                mobileData.setMemberNumber(mobileObject.getString("MobileNo"));
//                                mobileList.add(mobileData);
//                            }
//                            data.setNumberList(mobileList);
//
//                        }
//
//                        if(email!=null && !email.isEmpty()){
//                            data.setEmailId(email);
//                        }else if(EmailIds!=null && EmailIds.length()>0){
//                            ArrayList<CalendarData> emailList=new ArrayList<>();
//                            for(int k=0;k<EmailIds.length();k++){
//                                JSONObject emailObject=EmailIds.getJSONObject(k);
//                                CalendarData emailData=new CalendarData();
//                                emailData.setMemberName(emailObject.getString("MemberName"));
//                                emailData.setMemberEmail(emailObject.getString("EmailId"));
//                                emailList.add(emailData);
//                            }
//                            data.setEmailList(emailList);
//
//                        }
//
//                        dataArrayList.add(data);
//                    }
//
//
//                }
//
//                if(dataArrayList.size()>0){
//                    LayoutInflater layoutInflater = (LayoutInflater) this.getContext()
//                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    View view=layoutInflater.inflate(R.layout.upcoming_birthday,null);
//                    TextView txt_date=(TextView)view.findViewById(R.id.txt_date);
//                    LinearLayout ll_cont_up=(LinearLayout)view.findViewById(R.id.ll_cont);
//                    txt_date.setText("Today's Birthdays");
//                    txt_date.setTypeface(txt_date.getTypeface(), Typeface.BOLD);
//                    txt_date.setTextColor(getResources().getColor(R.color.colorPrimary));
//
//                    for(int k=0;k<dataArrayList.size();k++){
//                        CalendarData data=dataArrayList.get(k);
//                        View convertView = layoutInflater.inflate(R.layout.calender_birhday_anniversary_item, null);
//                        TextView txt_name=(TextView)convertView.findViewById(R.id.tv_name);
//                        txt_name.setText(data.getTitle());
//
//                        ImageView iv_call,iv_msg,iv_mail;
//                        iv_call=(ImageView)convertView.findViewById(R.id.iv_call);
//                        iv_msg=(ImageView)convertView.findViewById(R.id.iv_msg);
//                        iv_mail=(ImageView)convertView.findViewById(R.id.iv_mail);
//
//                        final String number=data.getContactNumber();
//                        final ArrayList<CalendarData> mobileList=data.getNumberList();
//
//                        if(number!=null && !number.isEmpty()){
//                            iv_call.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + number));
//                                    startActivity(callIntent);
//                                }
//                            });
//
//                            iv_msg.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent msgIntent = new Intent(Intent.ACTION_VIEW);
//
//                                    msgIntent.setData(Uri.parse("smsto: "+Uri.encode(number)));
//                                    startActivity(msgIntent);
//                                }
//                            });
//
//                        }else if(mobileList!=null && mobileList.size()>0){
//                            iv_call.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Utils.showCallPopup(getActivity(),mobileList);
//                                }
//                            });
//
//                            iv_msg.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Utils.showMsgPopup(getActivity(),mobileList);
//                                }
//                            });
//
//                        }
//                        else {
//                            iv_call.setImageDrawable(getResources().getDrawable(R.drawable.call_gray));
//                            iv_msg.setImageDrawable(getResources().getDrawable(R.drawable.message_gray));
//                            iv_call.setEnabled(false);
//                            iv_msg.setEnabled(false);
//                        }
//
//                        final String email=data.getEmailId();
//                        final ArrayList<CalendarData> emailList=data.getEmailList();
//
//
//                        if(email!=null && !email.isEmpty()){
//                            iv_mail.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//                                    emailIntent.setType("plain/text");
//                                    emailIntent.setData(Uri.parse("mailto:"));
//                                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
//                                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
//                                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
//                                    startActivity(emailIntent);
//                                }
//                            });
//
//
//                        }else if(emailList!=null && emailList.size()>0){
//                            iv_mail.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Utils.showEmailPopup(getActivity(),emailList);
//                                }
//                            });
//
//
//                        }
//                        else {
//                            iv_mail.setImageDrawable(getResources().getDrawable(R.drawable.mail_gray));
//                            iv_mail.setEnabled(false);
//
//                        }
//
//                        ll_cont_up.addView(convertView);
//                    }
//
//                    if(ll_cont_up.getChildCount()>0){
//                        ll_today.addView(view);
//
//                    }
//
//                }
//
//
//                //upcoming birthday
//                if(resultArray.length()>0){
//
//
//                    String compareDate = resultArray.getJSONObject(0).getString("eventDate");
//                    String dateDisplay="";
//                    Date date = null;
//                    try {
//                        date = oldFormat.parse(compareDate);
//                        compareDate = newFormat.format(date);
//                        dateDisplay=sdf.format(date);
//
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    ArrayList<CalendarData> list=new ArrayList<>();
//                    for(int i=0;i<resultArray.length();i++){
//                        JSONObject object=resultArray.getJSONObject(i);
//
//                        String eventDate = object.getString("eventDate");
//                        String newDisplayDate = object.getString("eventDate");
//                        Date date1 = null;
//                        try {
//                            date1 = oldFormat.parse(eventDate);
//                            eventDate = newFormat.format(date1);
//                            newDisplayDate=sdf.format(date1);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//
//                        if(!filterDate.equals(eventDate)){
//
//                            if(compareDate.equals(eventDate)){
//                                CalendarData data = new CalendarData();
//
//                                data.setEventDate(object.getString("eventDate"));
//
//                                data.setTitle(object.getString("title"));
//                                String email=object.getString("EmailId");
//                                JSONArray EmailIds=object.getJSONArray("EmailIds");
//
//                                String number=object.getString("ContactNumber");
//                                JSONArray MobileNo=object.getJSONArray("MobileNo");
//
//                                if(number!=null && !number.isEmpty()){
//                                    data.setContactNumber(number);
//                                }else if(MobileNo!=null && MobileNo.length()>0){
//                                    ArrayList<CalendarData> mobileList=new ArrayList<>();
//                                    for(int k=0;k<MobileNo.length();k++){
//                                        JSONObject mobileObject=MobileNo.getJSONObject(k);
//                                        CalendarData mobileData=new CalendarData();
//                                        mobileData.setMemberName(mobileObject.getString("MemberName"));
//                                        mobileData.setMemberNumber(mobileObject.getString("MobileNo"));
//                                        mobileList.add(mobileData);
//                                    }
//                                    data.setNumberList(mobileList);
//
//                                }
//
//
//                                if(email!=null && !email.isEmpty()){
//                                    data.setEmailId(email);
//                                }else if(EmailIds!=null && EmailIds.length()>0){
//                                    ArrayList<CalendarData> emailList=new ArrayList<>();
//                                    for(int k=0;k<EmailIds.length();k++){
//                                        JSONObject emailObject=EmailIds.getJSONObject(k);
//                                        CalendarData emailData=new CalendarData();
//                                        emailData.setMemberName(emailObject.getString("MemberName"));
//                                        emailData.setMemberEmail(emailObject.getString("EmailId"));
//                                        emailList.add(emailData);
//                                    }
//                                    data.setEmailList(emailList);
//
//                                }
//
//                                list.add(data);
//                            }else {
//                                if(list.size()>0){
//                                    LayoutInflater layoutInflater = (LayoutInflater) this.getContext()
//                                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                                    View view=layoutInflater.inflate(R.layout.upcoming_birthday,null);
//                                    TextView txt_date=(TextView)view.findViewById(R.id.txt_date);
//                                    LinearLayout ll_cont_up=(LinearLayout)view.findViewById(R.id.ll_cont);
//
//                                    txt_date.setText(dateDisplay);
//
//                                    for(int j=0;j<list.size();j++){
//                                        CalendarData data=list.get(j);
//                                        View convertView = layoutInflater.inflate(R.layout.calender_birhday_anniversary_item, null);
//                                        TextView txt_name=(TextView)convertView.findViewById(R.id.tv_name);
//                                        txt_name.setText(data.getTitle());
//
//                                        ImageView iv_call,iv_msg,iv_mail;
//                                        iv_call=(ImageView)convertView.findViewById(R.id.iv_call);
//                                        iv_msg=(ImageView)convertView.findViewById(R.id.iv_msg);
//                                        iv_mail=(ImageView)convertView.findViewById(R.id.iv_mail);
//
//                                        final String number=data.getContactNumber();
//                                        final ArrayList<CalendarData> mobileList=data.getNumberList();
//                                        if(number!=null && !number.isEmpty()){
//                                            iv_call.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + number));
//                                                    startActivity(callIntent);
//                                                }
//                                            });
//
//                                            iv_msg.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    Intent msgIntent = new Intent(Intent.ACTION_VIEW);
//
//                                                    msgIntent.setData(Uri.parse("smsto: "+Uri.encode(number)));
//                                                    startActivity(msgIntent);
//                                                }
//                                            });
//
//                                        }else if(mobileList!=null && mobileList.size()>0){
//                                            iv_call.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    Utils.showCallPopup(getActivity(),mobileList);
//                                                }
//                                            });
//
//                                            iv_msg.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    Utils.showMsgPopup(getActivity(),mobileList);
//                                                }
//                                            });
//
//
//                                        }
//                                        else {
//                                            iv_call.setImageDrawable(getResources().getDrawable(R.drawable.call_gray));
//                                            iv_msg.setImageDrawable(getResources().getDrawable(R.drawable.message_gray));
//                                            iv_call.setEnabled(false);
//                                            iv_msg.setEnabled(false);
//
//                                        }
//
//                                        final String email=data.getEmailId();
//                                        final ArrayList<CalendarData> emailList=data.getEmailList();
//
//
//                                        if(email!=null && !email.isEmpty()){
//                                            iv_mail.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//                                                    emailIntent.setType("plain/text");
//                                                    emailIntent.setData(Uri.parse("mailto:"));
//                                                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
//                                                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
//                                                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
//                                                    startActivity(emailIntent);
//                                                }
//                                            });
//
//
//                                        }else if(emailList!=null && emailList.size()>0){
//                                            iv_mail.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    Utils.showEmailPopup(getActivity(),emailList);
//                                                }
//                                            });
//
//
//
//                                        }
//                                        else {
//                                            iv_mail.setImageDrawable(getResources().getDrawable(R.drawable.mail_gray));
//                                            iv_mail.setEnabled(false);
//
//                                        }
//
//                                        ll_cont_up.addView(convertView);
//                                    }
//
//                                    if(ll_cont_up.getChildCount()>0){
//                                        ll_update.setVisibility(View.VISIBLE);
//                                        ll_cont.addView(view);
//                                    }
//
//
//                                }
//
//                                compareDate=eventDate;
//                                dateDisplay=newDisplayDate;
//                                list.clear();
//                                CalendarData data = new CalendarData();
//
//                                data.setEventDate(object.getString("eventDate"));
//
//                                data.setTitle(object.getString("title"));
//
//                                String email=object.getString("EmailId");
//                                JSONArray EmailIds=object.getJSONArray("EmailIds");
//
//                                String number=object.getString("ContactNumber");
//                                JSONArray MobileNo=object.getJSONArray("MobileNo");
//
//                                if(number!=null && !number.isEmpty()){
//                                    data.setContactNumber(number);
//                                }else if(MobileNo!=null && MobileNo.length()>0){
//                                    ArrayList<CalendarData> mobileList=new ArrayList<>();
//                                    for(int k=0;k<MobileNo.length();k++){
//                                        JSONObject mobileObject=MobileNo.getJSONObject(k);
//                                        CalendarData mobileData=new CalendarData();
//                                        mobileData.setMemberName(mobileObject.getString("MemberName"));
//                                        mobileData.setMemberNumber(mobileObject.getString("MobileNo"));
//                                        mobileList.add(mobileData);
//                                    }
//                                    data.setNumberList(mobileList);
//
//                                }
//
//
//                                if(email!=null && !email.isEmpty()){
//                                    data.setEmailId(email);
//                                }else if(EmailIds!=null && EmailIds.length()>0){
//                                    ArrayList<CalendarData> emailList=new ArrayList<>();
//                                    for(int k=0;k<EmailIds.length();k++){
//                                        JSONObject emailObject=EmailIds.getJSONObject(k);
//                                        CalendarData emailData=new CalendarData();
//                                        emailData.setMemberName(emailObject.getString("MemberName"));
//                                        emailData.setMemberEmail(emailObject.getString("EmailId"));
//                                        emailList.add(emailData);
//                                    }
//                                    data.setEmailList(emailList);
//
//                                }
//
//                                list.add(data);
//                            }
//                        }
//
//
//
//                    }
//                    if(list.size()>0) {
//                        LayoutInflater layoutInflater = (LayoutInflater) this.getContext()
//                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                        View view = layoutInflater.inflate(R.layout.upcoming_birthday, null);
//                        TextView txt_date = (TextView) view.findViewById(R.id.txt_date);
//                        LinearLayout ll_cont_up = (LinearLayout) view.findViewById(R.id.ll_cont);
//                        txt_date.setText(dateDisplay);
//
//                        for (CalendarData data : list) {
//                            View convertView = layoutInflater.inflate(R.layout.calender_birhday_anniversary_item, null);
//                            TextView txt_name = (TextView) convertView.findViewById(R.id.tv_name);
//                            txt_name.setText(data.getTitle());
//
//                            ImageView iv_call, iv_msg, iv_mail;
//                            iv_call = (ImageView) convertView.findViewById(R.id.iv_call);
//                            iv_msg = (ImageView) convertView.findViewById(R.id.iv_msg);
//                            iv_mail = (ImageView) convertView.findViewById(R.id.iv_mail);
//
//                            final String number = data.getContactNumber();
//                            final ArrayList<CalendarData> mobileList = data.getNumberList();
//                            if (number != null && !number.isEmpty()) {
//                                iv_call.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + number));
//                                        startActivity(callIntent);
//                                    }
//                                });
//
//                                iv_msg.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent msgIntent = new Intent(Intent.ACTION_VIEW);
//
//                                        msgIntent.setData(Uri.parse("smsto: " + Uri.encode(number)));
//                                        startActivity(msgIntent);
//                                    }
//                                });
//
//                            } else if (mobileList != null && mobileList.size() > 0) {
//                                iv_call.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Utils.showCallPopup(getActivity(), mobileList);
//                                    }
//                                });
//
//                                iv_msg.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Utils.showMsgPopup(getActivity(), mobileList);
//                                    }
//                                });
//
//                            } else {
//                                iv_call.setImageDrawable(getResources().getDrawable(R.drawable.call_gray));
//                                iv_msg.setImageDrawable(getResources().getDrawable(R.drawable.message_gray));
//                                iv_call.setEnabled(false);
//                                iv_msg.setEnabled(false);
//                            }
//
//                            final String email = data.getEmailId();
//                            final ArrayList<CalendarData> emailList = data.getEmailList();
//
//
//                            if (email != null && !email.isEmpty()) {
//                                iv_mail.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//                                        emailIntent.setType("plain/text");
//                                        emailIntent.setData(Uri.parse("mailto:"));
//                                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
//                                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
//                                        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
//                                        startActivity(emailIntent);
//                                    }
//                                });
//
//
//                            } else if (emailList != null && emailList.size() > 0) {
//                                iv_mail.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Utils.showEmailPopup(getActivity(), emailList);
//                                    }
//                                });
//
//
//                            } else {
//                                iv_mail.setImageDrawable(getResources().getDrawable(R.drawable.mail_gray));
//                                iv_mail.setEnabled(false);
//
//                            }
//
//                            ll_cont_up.addView(convertView);
//                        }
//
//
//                        if(ll_cont_up.getChildCount()>0){
//                            ll_update.setVisibility(View.VISIBLE);
//                            ll_cont.addView(view);
//                        }
//
//
//                    }
//                }
//
//                if(ll_today.getChildCount()>0 || ll_cont.getChildCount()>0){
//                    ll_error.setVisibility(View.GONE);
//                }else {
//                    ll_update.setVisibility(View.GONE);
//                    ll_error.setVisibility(View.VISIBLE);
//                }
//
//            }
//
////                CalendarEventsAdapter adapter = new CalendarEventsAdapter(getActivity().getApplicationContext(),eventList,celebrationType);
//            pBar.setVisibility(View.GONE);
//        }
//
//        catch (JSONException e) {
//            pBar.setVisibility(View.GONE);
//            e.printStackTrace();
//        }
//
//
//    }

    private void getAnnouncementList(JSONObject  responce) {
        try {
            JSONObject eventListObject = responce.getJSONObject("TBEventListTypeResult");
            String status = eventListObject.getString("status");

            if(status.equals("0")){
                JSONObject result = eventListObject.getJSONObject("Result");
                JSONArray resultArray = result.getJSONArray("Events");
                ArrayList<CalendarData> dataArrayList=new ArrayList<>();
                for(int j=0;j<resultArray.length();j++){
                    JSONObject object=resultArray.getJSONObject(j);

                    String eventDate = object.getString("eventDate");

                    Date date1 = null;
                    try {
                        date1 = oldFormat.parse(eventDate);
                        eventDate = newFormat.format(date1);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(filterDate.equals(eventDate)){
                        CalendarData data = new CalendarData();
                        data.setEventDate(object.getString("eventDate"));
                        data.setTitle(object.getString("title"));
//                        String email=object.getString("EmailId");
                        JSONArray EmailIds=object.getJSONArray("EmailIds");
//                        String number=object.getString("ContactNumber");
                        JSONArray MobileNo=object.getJSONArray("MobileNo");

//                        if(number!=null && !number.isEmpty()){
//                            data.setContactNumber(number);
//                        }else

                        if(MobileNo!=null && MobileNo.length()>0){
                            ArrayList<CalendarData> mobileList=new ArrayList<>();
                            for(int k=0;k<MobileNo.length();k++){
                                JSONObject mobileObject=MobileNo.getJSONObject(k);
                                CalendarData mobileData=new CalendarData();
                                mobileData.setMemberName(mobileObject.getString("MemberName"));
                                mobileData.setMemberNumber(mobileObject.getString("MobileNo"));
                                mobileList.add(mobileData);
                            }
                            data.setNumberList(mobileList);

                        }

//                        if(email!=null && !email.isEmpty()){
//                            data.setEmailId(email);
//                        }else

                        if(EmailIds!=null && EmailIds.length()>0){
                            ArrayList<CalendarData> emailList=new ArrayList<>();
                            for(int k=0;k<EmailIds.length();k++){
                                JSONObject emailObject=EmailIds.getJSONObject(k);
                                CalendarData emailData=new CalendarData();
                                emailData.setMemberName(emailObject.getString("MemberName"));
                                emailData.setMemberEmail(emailObject.getString("EmailId"));
                                emailList.add(emailData);
                            }
                            data.setEmailList(emailList);

                        }

                        dataArrayList.add(data);
                    }


                }

                if(dataArrayList.size()>0){
                    if(getContext()==null){
                        return;
                    }
                    LayoutInflater layoutInflater = (LayoutInflater)getContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view=layoutInflater.inflate(R.layout.upcoming_birthday,null);
                    TextView txt_date=(TextView)view.findViewById(R.id.txt_date);
                    LinearLayout ll_cont_up=(LinearLayout)view.findViewById(R.id.ll_cont);
                    txt_date.setText("Today's Birthdays");
                    txt_date.setTypeface(txt_date.getTypeface(), Typeface.BOLD);
                    txt_date.setTextColor(getResources().getColor(R.color.colorPrimary));

                    for(int k=0;k<dataArrayList.size();k++){
                        CalendarData data=dataArrayList.get(k);
                        View convertView = layoutInflater.inflate(R.layout.calender_birhday_anniversary_item, null);
                        TextView txt_name=(TextView)convertView.findViewById(R.id.tv_name);
                        txt_name.setText(data.getTitle());

                        ImageView iv_call,iv_msg,iv_mail;
                        iv_call=(ImageView)convertView.findViewById(R.id.iv_call);
                        iv_msg=(ImageView)convertView.findViewById(R.id.iv_msg);
                        iv_mail=(ImageView)convertView.findViewById(R.id.iv_mail);

                        LinearLayout ll_call,ll_msg,ll_mail;
                        ll_call=(LinearLayout)convertView.findViewById(R.id.ll_call);
                        ll_msg=(LinearLayout)convertView.findViewById(R.id.ll_msg);
                        ll_mail=(LinearLayout)convertView.findViewById(R.id.ll_mail);
//                        final String number=data.getContactNumber();
                        final ArrayList<CalendarData> mobileList=data.getNumberList();

//                        if(number!=null && !number.isEmpty()){
//                            iv_call.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + number));
//                                    startActivity(callIntent);
//                                }
//                            });
//
//                            iv_msg.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent msgIntent = new Intent(Intent.ACTION_VIEW);
//
//                                    msgIntent.setData(Uri.parse("smsto: "+Uri.encode(number)));
//                                    startActivity(msgIntent);
//                                }
//                            });
//
//                        }else

                        if(mobileList!=null && mobileList.size()>0){
                            if(mobileList.size()==1){
                                ll_call.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String number= null;
                                        number = mobileList.get(0).getMemberNumber();
                                        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + number));
                                        startActivity(callIntent);

                                    }
                                });

                                ll_msg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String number= null;
                                        number = mobileList.get(0).getMemberNumber();
                                        Intent msgIntent = new Intent(Intent.ACTION_VIEW);
                                        msgIntent.setData(Uri.parse("smsto: "+Uri.encode(number)));
                                        startActivity(msgIntent);


                                    }
                                });
                            }else {
                                ll_call.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Utils.showCallPopup(getActivity(),mobileList);
                                    }
                                });

                                ll_msg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Utils.showMsgPopup(getActivity(),mobileList);
                                    }
                                });

                            }

                        }
                        else {
                            iv_call.setImageDrawable(getResources().getDrawable(R.drawable.call_gray));
                            iv_msg.setImageDrawable(getResources().getDrawable(R.drawable.message_gray));
                            ll_call.setEnabled(false);
                            ll_msg.setEnabled(false);
                        }

//                        final String email=data.getEmailId();
                        final ArrayList<CalendarData> emailList=data.getEmailList();


//                        if(email!=null && !email.isEmpty()){
//                            iv_mail.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//                                    emailIntent.setType("plain/text");
//                                    emailIntent.setData(Uri.parse("mailto:"));
//                                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
//                                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
//                                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
//                                    startActivity(emailIntent);
//                                }
//                            });
//
//
//                        }else

                        if(emailList!=null && emailList.size()>0){
                            if(emailList.size()==1){
                                ll_mail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String email= null;
                                        email = emailList.get(0).getMemberEmail();
                                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                                        emailIntent.setType("plain/text");
                                        emailIntent.setData(Uri.parse("mailto:"));
                                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                                        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                                        startActivity(emailIntent);

                                    }
                                });

                            }else {
                                ll_mail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Utils.showEmailPopup(getActivity(),emailList);
                                    }
                                });

                            }

                        }
                        else {
                            iv_mail.setImageDrawable(getResources().getDrawable(R.drawable.mail_gray));
                            ll_mail.setEnabled(false);

                        }

                        ll_cont_up.addView(convertView);
                    }


                    if(ll_cont_up.getChildCount()>0){
                        ll_today.addView(view);

                    }


                }

                if(resultArray.length()>0) {


                    String compareDate = resultArray.getJSONObject(0).getString("eventDate");
                    String dateDisplay = "";
                    Date date = null;
                    try {
                        date = oldFormat.parse(compareDate);
                        compareDate = newFormat.format(date);
                        dateDisplay = sdf.format(date);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    ArrayList<CalendarData> list = new ArrayList<>();
                    for (int i = 0; i < resultArray.length(); i++) {
                        JSONObject object = resultArray.getJSONObject(i);

                        String eventDate = object.getString("eventDate");
                        String newDisplayDate = object.getString("eventDate");
                        Date date1 = null;
                        try {
                            date1 = oldFormat.parse(eventDate);
                            eventDate = newFormat.format(date1);
                            newDisplayDate = sdf.format(date1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (!filterDate.equals(eventDate)) {

                            if (compareDate.equals(eventDate)) {
                                CalendarData data = new CalendarData();

                                data.setEventDate(object.getString("eventDate"));

                                data.setTitle(object.getString("title"));
//                                String email = object.getString("EmailId");
                                JSONArray EmailIds = object.getJSONArray("EmailIds");

//                                String number = object.getString("ContactNumber");
                                JSONArray MobileNo = object.getJSONArray("MobileNo");

//                                if (number != null && !number.isEmpty()) {
//                                    data.setContactNumber(number);
//                                } else

                                if (MobileNo != null && MobileNo.length() > 0) {
                                    ArrayList<CalendarData> mobileList = new ArrayList<>();
                                    for (int k = 0; k < MobileNo.length(); k++) {
                                        JSONObject mobileObject = MobileNo.getJSONObject(k);
                                        CalendarData mobileData = new CalendarData();
                                        mobileData.setMemberName(mobileObject.getString("MemberName"));
                                        mobileData.setMemberNumber(mobileObject.getString("MobileNo"));
                                        mobileList.add(mobileData);
                                    }
                                    data.setNumberList(mobileList);

                                }


//                                if (email != null && !email.isEmpty()) {
//                                    data.setEmailId(email);
//                                } else

                                if (EmailIds != null && EmailIds.length() > 0) {
                                    ArrayList<CalendarData> emailList = new ArrayList<>();
                                    for (int k = 0; k < EmailIds.length(); k++) {
                                        JSONObject emailObject = EmailIds.getJSONObject(k);
                                        CalendarData emailData = new CalendarData();
                                        emailData.setMemberName(emailObject.getString("MemberName"));
                                        emailData.setMemberEmail(emailObject.getString("EmailId"));
                                        emailList.add(emailData);
                                    }
                                    data.setEmailList(emailList);

                                }

                                list.add(data);
                            } else {
                                if (list.size() > 0) {

                                    if(getContext()==null){
                                        return;
                                    }
                                    LayoutInflater layoutInflater = (LayoutInflater)getContext()
                                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View view = layoutInflater.inflate(R.layout.upcoming_birthday, null);
                                    TextView txt_date = (TextView) view.findViewById(R.id.txt_date);
                                    LinearLayout ll_cont_up = (LinearLayout) view.findViewById(R.id.ll_cont);

                                    txt_date.setText(dateDisplay);

                                    for (int j = 0; j < list.size(); j++) {
                                        CalendarData data = list.get(j);
                                        View convertView = layoutInflater.inflate(R.layout.calender_birhday_anniversary_item, null);
                                        TextView txt_name = (TextView) convertView.findViewById(R.id.tv_name);
                                        txt_name.setText(data.getTitle());

                                        ImageView iv_call, iv_msg, iv_mail;
                                        iv_call = (ImageView) convertView.findViewById(R.id.iv_call);
                                        iv_msg = (ImageView) convertView.findViewById(R.id.iv_msg);
                                        iv_mail = (ImageView) convertView.findViewById(R.id.iv_mail);

                                        LinearLayout ll_call,ll_msg,ll_mail;
                                        ll_call=(LinearLayout)convertView.findViewById(R.id.ll_call);
                                        ll_msg=(LinearLayout)convertView.findViewById(R.id.ll_msg);
                                        ll_mail=(LinearLayout)convertView.findViewById(R.id.ll_mail);

//                                        final String number = data.getContactNumber();
                                        final ArrayList<CalendarData> mobileList = data.getNumberList();
//                                        if (number != null && !number.isEmpty()) {
//                                            iv_call.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + number));
//                                                    startActivity(callIntent);
//                                                }
//                                            });
//
//                                            iv_msg.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    Intent msgIntent = new Intent(Intent.ACTION_VIEW);
//
//                                                    msgIntent.setData(Uri.parse("smsto: " + Uri.encode(number)));
//                                                    startActivity(msgIntent);
//                                                }
//                                            });
//
//                                        } else

                                        if (mobileList != null && mobileList.size() > 0) {

                                            if(mobileList.size()==1){
                                                ll_call.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        String number= null;
                                                        number = mobileList.get(0).getMemberNumber();
                                                        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + number));
                                                        startActivity(callIntent);

                                                    }
                                                });

                                                ll_msg.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        String number= null;
                                                        number = mobileList.get(0).getMemberNumber();
                                                        Intent msgIntent = new Intent(Intent.ACTION_VIEW);
                                                        msgIntent.setData(Uri.parse("smsto: "+Uri.encode(number)));
                                                        startActivity(msgIntent);


                                                    }
                                                });
                                            }else {
                                                ll_call.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Utils.showCallPopup(getActivity(),mobileList);
                                                    }
                                                });

                                                ll_msg.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Utils.showMsgPopup(getActivity(),mobileList);
                                                    }
                                                });

                                            }


                                        } else {
                                            iv_call.setImageDrawable(getResources().getDrawable(R.drawable.call_gray));
                                            iv_msg.setImageDrawable(getResources().getDrawable(R.drawable.message_gray));
                                            ll_call.setEnabled(false);
                                            ll_msg.setEnabled(false);

                                        }

//                                        final String email = data.getEmailId();
                                        final ArrayList<CalendarData> emailList = data.getEmailList();


//                                        if (email != null && !email.isEmpty()) {
//                                            iv_mail.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//                                                    emailIntent.setType("plain/text");
//                                                    emailIntent.setData(Uri.parse("mailto:"));
//                                                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
//                                                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
//                                                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
//                                                    startActivity(emailIntent);
//                                                }
//                                            });
//
//
//                                        } else

                                        if (emailList != null && emailList.size() > 0) {
                                            if(emailList.size()==1){
                                                ll_mail.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        String email= null;
                                                        email = emailList.get(0).getMemberEmail();
                                                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                                                        emailIntent.setType("plain/text");
                                                        emailIntent.setData(Uri.parse("mailto:"));
                                                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                                                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                                                        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                                                        startActivity(emailIntent);

                                                    }
                                                });

                                            }else {
                                                ll_mail.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Utils.showEmailPopup(getActivity(),emailList);
                                                    }
                                                });

                                            }

                                        } else {
                                            iv_mail.setImageDrawable(getResources().getDrawable(R.drawable.mail_gray));
                                            ll_mail.setEnabled(false);

                                        }

                                        ll_cont_up.addView(convertView);
                                    }

                                    if(ll_cont_up.getChildCount()>0){
                                        ll_update.setVisibility(View.VISIBLE);
                                        ll_cont.addView(view);
                                    }


                                }

                                compareDate = eventDate;
                                dateDisplay = newDisplayDate;
                                list.clear();
                                CalendarData data = new CalendarData();

                                data.setEventDate(object.getString("eventDate"));

                                data.setTitle(object.getString("title"));

//                                String email = object.getString("EmailId");
                                JSONArray EmailIds = object.getJSONArray("EmailIds");

//                                String number = object.getString("ContactNumber");
                                JSONArray MobileNo = object.getJSONArray("MobileNo");

//                                if (number != null && !number.isEmpty()) {
//                                    data.setContactNumber(number);
//                                } else

                                if (MobileNo != null && MobileNo.length() > 0) {
                                    ArrayList<CalendarData> mobileList = new ArrayList<>();
                                    for (int k = 0; k < MobileNo.length(); k++) {
                                        JSONObject mobileObject = MobileNo.getJSONObject(k);
                                        CalendarData mobileData = new CalendarData();
                                        mobileData.setMemberName(mobileObject.getString("MemberName"));
                                        mobileData.setMemberNumber(mobileObject.getString("MobileNo"));
                                        mobileList.add(mobileData);
                                    }
                                    data.setNumberList(mobileList);

                                }

//
//                                if (email != null && !email.isEmpty()) {
//                                    data.setEmailId(email);
//                                } else

                                if (EmailIds != null && EmailIds.length() > 0) {
                                    ArrayList<CalendarData> emailList = new ArrayList<>();
                                    for (int k = 0; k < EmailIds.length(); k++) {
                                        JSONObject emailObject = EmailIds.getJSONObject(k);
                                        CalendarData emailData = new CalendarData();
                                        emailData.setMemberName(emailObject.getString("MemberName"));
                                        emailData.setMemberEmail(emailObject.getString("EmailId"));
                                        emailList.add(emailData);
                                    }
                                    data.setEmailList(emailList);

                                }

                                list.add(data);
                            }
                        }


                    }
                    if (list.size() > 0) {
                        if(getContext()==null){
                            return;
                        }
                        LayoutInflater layoutInflater = (LayoutInflater) getContext()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View view = layoutInflater.inflate(R.layout.upcoming_birthday, null);
                        TextView txt_date = (TextView) view.findViewById(R.id.txt_date);
                        LinearLayout ll_cont_up = (LinearLayout) view.findViewById(R.id.ll_cont);
                        txt_date.setText(dateDisplay);

                        for (CalendarData data : list) {
                            View convertView = layoutInflater.inflate(R.layout.calender_birhday_anniversary_item, null);
                            TextView txt_name = (TextView) convertView.findViewById(R.id.tv_name);
                            txt_name.setText(data.getTitle());

                            ImageView iv_call, iv_msg, iv_mail;
                            iv_call = (ImageView) convertView.findViewById(R.id.iv_call);
                            iv_msg = (ImageView) convertView.findViewById(R.id.iv_msg);
                            iv_mail = (ImageView) convertView.findViewById(R.id.iv_mail);

                            LinearLayout ll_call,ll_msg,ll_mail;
                            ll_call=(LinearLayout)convertView.findViewById(R.id.ll_call);
                            ll_msg=(LinearLayout)convertView.findViewById(R.id.ll_msg);
                            ll_mail=(LinearLayout)convertView.findViewById(R.id.ll_mail);

//                            final String number = data.getContactNumber();
                            final ArrayList<CalendarData> mobileList = data.getNumberList();
//                            if (number != null && !number.isEmpty()) {
//                                iv_call.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + number));
//                                        startActivity(callIntent);
//                                    }
//                                });
//
//                                iv_msg.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent msgIntent = new Intent(Intent.ACTION_VIEW);
//
//                                        msgIntent.setData(Uri.parse("smsto: " + Uri.encode(number)));
//                                        startActivity(msgIntent);
//                                    }
//                                });
//
//                            } else

                            if (mobileList != null && mobileList.size() > 0) {
                                if(mobileList.size()==1){
                                    ll_call.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String number= null;
                                            number = mobileList.get(0).getMemberNumber();
                                            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + number));
                                            startActivity(callIntent);

                                        }
                                    });

                                    ll_msg.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            String number= null;
                                            number = mobileList.get(0).getMemberNumber();
                                            Intent msgIntent = new Intent(Intent.ACTION_VIEW);
                                            msgIntent.setData(Uri.parse("smsto: "+Uri.encode(number)));
                                            startActivity(msgIntent);


                                        }
                                    });
                                }else {
                                    ll_call.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Utils.showCallPopup(getActivity(),mobileList);
                                        }
                                    });

                                    ll_msg.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Utils.showMsgPopup(getActivity(),mobileList);
                                        }
                                    });

                                }
                            } else {
                                iv_call.setImageDrawable(getResources().getDrawable(R.drawable.call_gray));
                                iv_msg.setImageDrawable(getResources().getDrawable(R.drawable.message_gray));
                                ll_call.setEnabled(false);
                                ll_msg.setEnabled(false);
                            }

//                            final String email = data.getEmailId();
                            final ArrayList<CalendarData> emailList = data.getEmailList();


//                            if (email != null && !email.isEmpty()) {
//                                iv_mail.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//                                        emailIntent.setType("plain/text");
//                                        emailIntent.setData(Uri.parse("mailto:"));
//                                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
//                                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
//                                        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
//                                        startActivity(emailIntent);
//                                    }
//                                });
//
//
//                            } else


                            if (emailList != null && emailList.size() > 0) {
                                if(emailList.size()==1){
                                    ll_mail.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String email= null;
                                            email = emailList.get(0).getMemberEmail();
                                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                                            emailIntent.setType("plain/text");
                                            emailIntent.setData(Uri.parse("mailto:"));
                                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                                            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                                            startActivity(emailIntent);

                                        }
                                    });

                                }else {
                                    ll_mail.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Utils.showEmailPopup(getActivity(),emailList);
                                        }
                                    });

                                }



                            } else {
                                iv_mail.setImageDrawable(getResources().getDrawable(R.drawable.mail_gray));
                                ll_mail.setEnabled(false);

                            }

                            ll_cont_up.addView(convertView);
                        }


                        if(ll_cont_up.getChildCount()>0){
                            ll_update.setVisibility(View.VISIBLE);
                            ll_cont.addView(view);
                        }



                    }
                }

                if(ll_today.getChildCount()>0 || ll_cont.getChildCount()>0){
                    ll_error.setVisibility(View.GONE);
                }else {
                    ll_update.setVisibility(View.GONE);
                    ll_error.setVisibility(View.VISIBLE);
                }

            }



//                CalendarEventsAdapter adapter = new CalendarEventsAdapter(getActivity().getApplicationContext(),eventList,celebrationType);
            progressDialog.dismiss();
            isLoaded=true;
        }

        catch (JSONException e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }


    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        this.isVisible=isVisibleToUser;
//        if(isVisibleToUser && !isLoaded){
//            celebrationWebservices();
//        }
//    }
}
