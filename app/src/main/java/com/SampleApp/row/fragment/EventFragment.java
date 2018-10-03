package com.SampleApp.row.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Data.CalendarData;
import com.SampleApp.row.EventDetails;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.Utils;
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

import static com.SampleApp.row.Utils.PreferenceManager.MY_CATEGORY;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {

    Bundle bundle;
    Calendar calendar;
    String selectedDate,celebrationType;
    ArrayList<CalendarData> eventList;

    ProgressDialog progressDialog;
    LinearLayout ll_cont,ll_today,ll_update,ll_error;
    SimpleDateFormat newFormat=new SimpleDateFormat("dd MM");
    SimpleDateFormat oldFormat=new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf=new SimpleDateFormat("dd MMMM");
    String filterDate,catID;
    boolean isVisible,isLoaded,isCreate;

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        calendar = Calendar.getInstance();

        eventList = new ArrayList<>();
        catID= PreferenceManager.getPreference(getActivity(), MY_CATEGORY, "" + Constant.GROUP_CATEGORY_CLUB);
        ll_cont=(LinearLayout)view.findViewById(R.id.ll_cont);
        ll_cont.removeAllViews();
        ll_today=(LinearLayout)view.findViewById(R.id.ll_today);
        ll_today.removeAllViews();
        ll_update=(LinearLayout)view.findViewById(R.id.ll_update);
        ll_error=(LinearLayout)view.findViewById(R.id.ll_error);

        bundle = getArguments();
        selectedDate = bundle.getString("selectedDate");
        celebrationType = bundle.getString("announcement");

        Date date=null;

        try {
            date=oldFormat.parse(selectedDate);
            filterDate=newFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        Utils.log(""+isVisible+" / "+isLoaded);

//        isCreate=true;
//        setUserVisibleHint(true);

        // Inflate the layout for this fragment
        return view;
    }

    private void celebrationWebservices() {
        Utils.log("In Event");
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
                //pBar.setVisibility(View.VISIBLE);


                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.GetMonthEventListTypeWise, requestData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

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

                AppController.getInstance().addToRequestQueue(getActivity(), request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint())
        {
            return;
        }else {
            celebrationWebservices();
        }

    }

    private void getAnnouncementList(JSONObject  responce) {
        ll_cont.removeAllViews();
        ll_today.removeAllViews();
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
                        data.setUniqueId(object.getString("MemberID"));
                        data.setEventDate(object.getString("eventDate"));
                        data.setTitle(object.getString("title"));
                        data.setDescription(object.getString("Description"));
                        data.setEventImg(object.getString("eventImg"));
                        data.setEventTime(object.getString("EventTime"));
                        if(object.getString("GroupId")!=null && !object.getString("GroupId").equalsIgnoreCase("null")){
                            data.setGroupId(Integer.parseInt(object.getString("GroupId")));
                        }

                        dataArrayList.add(data);
                    }


                }

                if(dataArrayList.size()>0){
                    final LayoutInflater layoutInflater = (LayoutInflater) this.getContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view=layoutInflater.inflate(R.layout.upcoming_birthday,null);
                    TextView txt_date=(TextView)view.findViewById(R.id.txt_date);
                    LinearLayout ll_cont_up=(LinearLayout)view.findViewById(R.id.ll_cont);
                    txt_date.setText("Today's Events");
                    txt_date.setTypeface(txt_date.getTypeface(), Typeface.BOLD);
                    txt_date.setTextColor(getResources().getColor(R.color.colorPrimary));

                    for(int k=0;k<dataArrayList.size();k++){
                        final CalendarData data=dataArrayList.get(k);
                        View convertView = layoutInflater.inflate(R.layout.calender_event_item, null);
                        TextView txt_name=(TextView)convertView.findViewById(R.id.tv_name);
                        txt_name.setText(data.getTitle());
                        LinearLayout ll_event=(LinearLayout)convertView.findViewById(R.id.ll_event);
                        ll_event.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(catID.equalsIgnoreCase(String.valueOf(Constant.GROUP_CATEGORY_CLUB))){

                                    Intent intent = new Intent(getActivity(), EventDetails.class);
                                    intent.putExtra("eventid", data.getUniqueId());
                                    startActivity(intent);

                                }else if(catID.equalsIgnoreCase(String.valueOf(Constant.GROUP_CATEGORY_DT))){
                                    Intent intent = new Intent(getActivity(), EventDetails.class);
                                    intent.putExtra("eventid", data.getUniqueId().substring(1));
//                                    intent.putExtra("eventList","");
                                    intent.putExtra("grpID",String.valueOf(data.getGroupId()));
                                    startActivity(intent);
                                }
                            }
                        });
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
                    final ArrayList<CalendarData> list = new ArrayList<>();
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
                                data.setUniqueId(object.getString("MemberID"));
                                data.setEventDate(object.getString("eventDate"));
                                data.setTitle(object.getString("title"));
                                data.setDescription(object.getString("Description"));
                                data.setEventImg(object.getString("eventImg"));
                                data.setEventTime(object.getString("EventTime"));
                                if(object.getString("GroupId")!=null && !object.getString("GroupId").equalsIgnoreCase("null")){
                                    data.setGroupId(Integer.parseInt(object.getString("GroupId")));
                                }
                                list.add(data);
                            } else {
                                if (list.size() > 0) {
                                    LayoutInflater layoutInflater = (LayoutInflater) this.getContext()
                                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View view = layoutInflater.inflate(R.layout.upcoming_birthday, null);
                                    TextView txt_date = (TextView) view.findViewById(R.id.txt_date);
                                    LinearLayout ll_cont_up = (LinearLayout) view.findViewById(R.id.ll_cont);

                                    txt_date.setText(dateDisplay);

                                    for (int j = 0; j < list.size(); j++) {
                                        final CalendarData data = list.get(j);
                                        View convertView = layoutInflater.inflate(R.layout.calender_event_item, null);
                                        TextView txt_name = (TextView) convertView.findViewById(R.id.tv_name);
                                        txt_name.setText(data.getTitle());
                                        LinearLayout ll_event = (LinearLayout) convertView.findViewById(R.id.ll_event);
                                        ll_event.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if(catID.equalsIgnoreCase(String.valueOf(Constant.GROUP_CATEGORY_CLUB))){

                                                    Intent intent = new Intent(getActivity(), EventDetails.class);
                                                    intent.putExtra("eventid", data.getUniqueId());
                                                    startActivity(intent);

                                                }else if(catID.equalsIgnoreCase(String.valueOf(Constant.GROUP_CATEGORY_DT))){
                                                    Intent intent = new Intent(getActivity(), EventDetails.class);
                                                    intent.putExtra("eventid", data.getUniqueId().substring(1));
//                                                    intent.putExtra("eventList","");
                                                    intent.putExtra("grpID",String.valueOf(data.getGroupId()));
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                        ll_cont_up.addView(convertView);
                                    }

                                    if (ll_cont_up.getChildCount() > 0) {
                                        ll_update.setVisibility(View.VISIBLE);
                                        ll_cont.addView(view);
                                    }


                                }

                                compareDate = eventDate;
                                dateDisplay = newDisplayDate;
                                list.clear();
                                CalendarData data = new CalendarData();
                                data.setUniqueId(object.getString("MemberID"));
                                data.setEventDate(object.getString("eventDate"));
                                data.setTitle(object.getString("title"));
                                data.setDescription(object.getString("Description"));
                                data.setEventImg(object.getString("eventImg"));
                                data.setEventTime(object.getString("EventTime"));
                                if(object.getString("GroupId")!=null && !object.getString("GroupId").equalsIgnoreCase("null")){
                                    data.setGroupId(Integer.parseInt(object.getString("GroupId")));
                                }
                                list.add(data);
                            }
                        }


                    }
                    if (list.size() > 0) {
                        LayoutInflater layoutInflater = (LayoutInflater) this.getContext()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View view = layoutInflater.inflate(R.layout.upcoming_birthday, null);
                        TextView txt_date = (TextView) view.findViewById(R.id.txt_date);
                        LinearLayout ll_cont_up = (LinearLayout) view.findViewById(R.id.ll_cont);
                        txt_date.setText(dateDisplay);

                        for (final CalendarData data : list) {
                            View convertView = layoutInflater.inflate(R.layout.calender_event_item, null);
                            TextView txt_name = (TextView) convertView.findViewById(R.id.tv_name);
                            txt_name.setText(data.getTitle());
                            LinearLayout ll_event = (LinearLayout) convertView.findViewById(R.id.ll_event);
                            ll_event.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(catID.equalsIgnoreCase(String.valueOf(Constant.GROUP_CATEGORY_CLUB))){

                                        Intent intent = new Intent(getActivity(), EventDetails.class);
                                        intent.putExtra("eventid", data.getUniqueId());

                                        startActivity(intent);

                                    }else if(catID.equalsIgnoreCase(String.valueOf(Constant.GROUP_CATEGORY_DT))){
                                        Intent intent = new Intent(getActivity(),EventDetails.class);
                                        intent.putExtra("eventid", data.getUniqueId().substring(1));
//                                        intent.putExtra("eventList","");
                                        intent.putExtra("grpID",String.valueOf(data.getGroupId()));
                                        startActivity(intent);
                                    }
                                }
                            });
                            ll_cont_up.addView(convertView);
                        }


                        if (ll_cont_up.getChildCount() > 0) {
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Utils.log("In Event "+isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);

        this.isVisible=isVisibleToUser;

        if(isVisible && !isLoaded && isResumed()){
            onResume();

        }

    }
}
