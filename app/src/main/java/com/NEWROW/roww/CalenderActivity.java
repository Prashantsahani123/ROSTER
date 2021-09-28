package com.NEWROW.row;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.NEWROW.row.NotificationDataBase.DatabaseHelper;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NEWROW.row.Data.CalendarData;
import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.TBPrefixes;
import com.NEWROW.row.Utils.Utils;
import com.NEWROW.row.fragment.AnniversaryFragment;
import com.NEWROW.row.fragment.AnnouncementFragment;
import com.NEWROW.row.fragment.EventFragment;
import com.NEWROW.row.sql.CalendarMasterModel;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.NEWROW.row.Utils.PreferenceManager.MY_CATEGORY;

/**
 * Created by admin on 12-02-2018.
 */

public class CalenderActivity extends AppCompatActivity {
    Context context;
    String selectedDate, catID = "1", initDate, updatedOn;
    CompactCalendarView calendarView;
    LinearLayout ll_calendar, ll_aCont, ll_bCont, ll_eCont, ll_birthday, ll_event, ll_anni, ll_error, ll_left, ll_right, ll_date;
    TextView txt_date;
    public static CalenderActivity act;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
    SimpleDateFormat monthNumber = new SimpleDateFormat("MM");
    SimpleDateFormat yearNumber = new SimpleDateFormat("yyyy");
    SimpleDateFormat dateNumber = new SimpleDateFormat("dd");
    SimpleDateFormat sysDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    ProgressDialog dialog;
    ProgressDialog progressDialog;
    private int month, year, day;
    CalendarMasterModel calendarModel;
    private long grpId;
    ArrayList<Event> list = new ArrayList<>();
    String todayDate, celebType, grpType, memID;
    private String messageId_temp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.calender_layout);

        context = this;

        act = this;

        AppBarCalendarFragment.tv_title.setText("Calendar");

        calendarView = (CompactCalendarView) findViewById(R.id.calendarView);

        calendarView.setUseThreeLetterAbbreviation(true);

        catID = PreferenceManager.getPreference(context, MY_CATEGORY, "" + Constant.GROUP_CATEGORY_CLUB);

        txt_date = (TextView) findViewById(R.id.txt_date);
        ll_calendar = (LinearLayout) findViewById(R.id.ll_calendar);
        ll_aCont = (LinearLayout) findViewById(R.id.ll_aCont);
        ll_bCont = (LinearLayout) findViewById(R.id.ll_bCont);
        ll_eCont = (LinearLayout) findViewById(R.id.ll_eCont);
        ll_birthday = (LinearLayout) findViewById(R.id.ll_birthday);
        ll_anni = (LinearLayout) findViewById(R.id.ll_anni);
        ll_event = (LinearLayout) findViewById(R.id.ll_event);
        ll_error = (LinearLayout) findViewById(R.id.ll_error);
        ll_left = (LinearLayout) findViewById(R.id.ll_left);
        ll_right = (LinearLayout) findViewById(R.id.ll_right);
        ll_date = (LinearLayout) findViewById(R.id.ll_date);

        ll_aCont.removeAllViews();
        ll_bCont.removeAllViews();
        ll_eCont.removeAllViews();

        progressDialog = new ProgressDialog(context, R.style.TBProgressBar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);

        calendarModel = new CalendarMasterModel(context);
        grpId = Long.parseLong(PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID));
        Calendar calendar = Calendar.getInstance();

        Date today = calendar.getTime();

        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //AppBarCalendarFragment.txt_month.setText((monthFormat.format(today)).toUpperCase());
        AppBarCalendarFragment.txt_month.setText("MONTH");
        selectedDate = df.format(today);

        txt_date.setText(sdf.format(today));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        AppBarCalendarFragment.ll_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (ll_calendar.getVisibility() == View.VISIBLE) {
                    ll_calendar.setVisibility(View.GONE);
                    AppBarCalendarFragment.txt_month.setText("MONTH");
                    AppBarCalendarFragment.iv_arrow.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
                } else {
                    AppBarCalendarFragment.txt_month.setText("DAY");
                    AppBarCalendarFragment.iv_arrow.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
                    ll_calendar.setVisibility(View.VISIBLE);
                }

            }
        });

        calendarView.shouldDrawIndicatorsBelowSelectedDays(true);

        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date date) {

                AppBarCalendarFragment.txt_month.setText("DAY");
                selectedDate = df.format(date);
                txt_date.setText(sdf.format(date));

                if (InternetConnection.checkConnection(context)) {
                    celebrationWebservices();
                } else {
                    Utils.showToastWithTitleAndContext(context, getString(R.string.noInternet));
                }

            }

            @Override
            public void onMonthScroll(Date date) {
                AppBarCalendarFragment.txt_month.setText("DAY");
                selectedDate = df.format(date);
                txt_date.setText(sdf.format(date));
                if (InternetConnection.checkConnection(context)) {
                    celebrationWebservices();
                    year = Integer.parseInt(yearNumber.format(date));
                    month = Integer.parseInt(monthNumber.format(date));
                    day = Integer.parseInt(dateNumber.format(date));
                    checkForUpdate();
                } else {
                    year = Integer.parseInt(yearNumber.format(date));
                    month = Integer.parseInt(monthNumber.format(date));
                    setMonthView();
                    Utils.showToastWithTitleAndContext(context, getString(R.string.noInternet));
                }
            }
        });

        ll_left.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                calendarView.showPreviousMonth();
            }
        });

        ll_right.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                calendarView.showNextMonth();
            }
        });

        ll_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new SpinnerDatePickerDialogBuilder()
                        .context(context)
                        .callback(new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int cyear, int monthOfYear, int dayOfMonth) {
                                Calendar calendar = new GregorianCalendar(cyear, monthOfYear, dayOfMonth);
                                Date date = calendar.getTime();
                                calendarView.setCurrentDate(date);
                                AppBarCalendarFragment.txt_month.setText("DAY");
                                selectedDate = df.format(date);
                                txt_date.setText(sdf.format(date));
                                if (InternetConnection.checkConnection(context)) {
                                    celebrationWebservices();
                                    year = Integer.parseInt(yearNumber.format(date));
                                    month = Integer.parseInt(monthNumber.format(date));
                                    day = Integer.parseInt(dateNumber.format(date));
                                    checkForUpdate();
                                } else {
                                    year = Integer.parseInt(yearNumber.format(date));
                                    month = Integer.parseInt(monthNumber.format(date));
                                    setMonthView();
                                    Utils.showToastWithTitleAndContext(context, getString(R.string.noInternet));
                                }
                            }
                        })
                        .defaultDate(year, month - 1, day)

                        .build()
                        .show();
                //Utils.log(""+calendar1.get(Calendar.YEAR));
            }
        });

        Intent intent = getIntent();

        Bundle b = intent.getExtras();

        if (b != null) {

            if (intent.hasExtra("Dashboard")) {

                String mod = b.getString("Dashboard");

                if (mod.equalsIgnoreCase("B")) {
                    viewPager.setCurrentItem(0);
                } else if (mod.equalsIgnoreCase("A")) {
                    viewPager.setCurrentItem(1);
                } else if (mod.equalsIgnoreCase("E")) {
                    viewPager.setCurrentItem(2);
                }

            } else if (intent.hasExtra("fromNoti")) {


                //update Data into Database
                messageId_temp=intent.getStringExtra("messageId");
                if (messageId_temp!=null){
                    //Create Database Helper Class Object
                    DatabaseHelper databaseHelpers = new DatabaseHelper( this );

                    boolean notificationInsert = databaseHelpers.updateData( messageId_temp );
                    Log.d("messageId_temp", "messageID ID ID AFTER :- " + messageId_temp);
                    Log.d("messageId_temp", "Is Data Updated :- " + notificationInsert);


                }


                String type = b.getString("type");



                if (type.equalsIgnoreCase("B")) {
                    viewPager.setCurrentItem(0);
                } else if (type.equalsIgnoreCase("A")) {
                    viewPager.setCurrentItem(1);
                }

                String sysDate = b.getString("date");

                Date date = null;

                try {
                    selectedDate = df.format(sysDateFormat.parse(sysDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        }

        if (InternetConnection.checkConnection(context)) {
            celebrationWebservices();
            checkForUpdate();
        } else {
            setMonthView();
            Utils.showToastWithTitleAndContext(context, getString(R.string.noInternet));
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            Utils.log("" + position);
            Fragment fragment = null;
            if (position == 0) {
                fragment = new AnnouncementFragment();
                Bundle bundle = new Bundle();
                bundle.putString("selectedDate", selectedDate);
                bundle.putString("announcement", "B");
                fragment.setArguments(bundle);
                return fragment;
            } else if (position == 1) {
                fragment = new AnniversaryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("selectedDate", selectedDate);
                bundle.putString("announcement", "A");
                fragment.setArguments(bundle);
                return fragment;
            } else if (position == 2) {
                fragment = new EventFragment();
                Bundle bundle = new Bundle();
                bundle.putString("selectedDate", selectedDate);
                bundle.putString("announcement", "E");
                fragment.setArguments(bundle);
                return fragment;
            } else {
                return null;
            }

        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return context.getString(R.string.lblBirthday);
            } else if (position == 1) {
                return context.getString(R.string.lblAnniversary);
            } else {
                return context.getString(R.string.lblEvent);
            }
        }

    }

    private void celebrationWebservices() {

        try {

            JSONObject requestData = new JSONObject();
            requestData.put("GroupID", PreferenceManager.getPreference(context.getApplicationContext(), PreferenceManager.GROUP_ID));
            requestData.put("SelectedDate", selectedDate);
            requestData.put("ProfileID", PreferenceManager.getPreference(context, PreferenceManager.GRP_PROFILE_ID));
            catID = PreferenceManager.getPreference(context, MY_CATEGORY, "" + Constant.GROUP_CATEGORY_CLUB);
            requestData.put("GroupCategory", catID);

            Utils.log("" + requestData);

            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

            if (ll_calendar.getVisibility() == View.VISIBLE) {
                progressDialog.show();

            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.GetMonthEventListDetails, requestData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    getResult(response);
                    Utils.log(response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Utils.log("VollyError:- " + error);
                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(120000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


            AppController.getInstance().addToRequestQueue(context, request);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getResult(JSONObject responce) {

        ll_bCont.removeAllViews();
        ll_aCont.removeAllViews();
        ll_eCont.removeAllViews();

        ll_birthday.setVisibility(View.GONE);
        ll_event.setVisibility(View.GONE);
        ll_anni.setVisibility(View.GONE);
        ll_error.setVisibility(View.GONE);

        try {

            JSONObject eventListObject = responce.getJSONObject("TBEventListDtlsResult");

            String status = eventListObject.getString("status");

            if (status.equals("0")) {

                JSONObject result = eventListObject.getJSONObject("Result");
                JSONArray events = result.getJSONArray("Events");
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                for (int i = 0; i < events.length(); i++) {

                    ll_error.setVisibility(View.GONE);
                    final JSONObject object = events.getJSONObject(i);
                    String type = object.getString("type");

                    if (type.equalsIgnoreCase("Birthday")) {
                        ll_birthday.setVisibility(View.VISIBLE);
                        final View convertView = layoutInflater.inflate(R.layout.calender_birhday_anniversary_item, null);
                        TextView txt_name = (TextView) convertView.findViewById(R.id.tv_name);
                        txt_name.setText(object.getString("title"));
                        ImageView iv_call, iv_msg, iv_mail;
                        iv_call = (ImageView) convertView.findViewById(R.id.iv_call);
                        iv_msg = (ImageView) convertView.findViewById(R.id.iv_msg);
                        iv_mail = (ImageView) convertView.findViewById(R.id.iv_mail);

                        LinearLayout ll_call, ll_msg, ll_mail, ll_whatsapp;
                        ll_call = (LinearLayout) convertView.findViewById(R.id.ll_call);
                        ll_msg = (LinearLayout) convertView.findViewById(R.id.ll_msg);
                        ll_mail = (LinearLayout) convertView.findViewById(R.id.ll_mail);
                        ll_whatsapp = (LinearLayout) convertView.findViewById(R.id.ll_whatsapp);

//                        final String email=object.getString("EmailId");
                        final JSONArray EmailIds = object.getJSONArray("EmailIds");
                        final String number = object.getString("ContactNumber");
                        final JSONArray MobileNo = object.getJSONArray("MobileNo");
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
//                        }else

                        ll_whatsapp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (Utils.appInstalledOrNot(context)) {


                                    String url = "https://api.whatsapp.com/send?phone=" + number;//"https://wa.me/"+profileData.getMobile()+"/?text=hii";
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                } else if (Utils.whatsBusinessAppInstalledOrNot(context)) {
                                    String url = "https://api.whatsapp.com/send?phone=" + number;//"https://wa.me/"+profileData.getMobile()+"/?text=hii";
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);

                                } else {
                                    Utils.showToastWithTitleAndContext(context, "WhatsApp is not installed");
                                }
                            }
                        });

                        if (MobileNo != null && MobileNo.length() > 0) {

                            if (MobileNo.length() == 1) {
                                ll_call.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String number = null;
                                        try {
                                            number = MobileNo.getJSONObject(0).getString("MobileNo");
                                            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + number));
                                            startActivity(callIntent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });

                                ll_msg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String number = null;
                                        try {
                                            number = MobileNo.getJSONObject(0).getString("MobileNo");
                                            Intent msgIntent = new Intent(Intent.ACTION_VIEW);
                                            msgIntent.setData(Uri.parse("smsto: " + Uri.encode(number)));
                                            startActivity(msgIntent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                });

                            } else {
                                final ArrayList<CalendarData> mobileList = new ArrayList<>();
                                for (int k = 0; k < MobileNo.length(); k++) {
                                    JSONObject mobileObject = MobileNo.getJSONObject(k);
                                    CalendarData mobileData = new CalendarData();
                                    mobileData.setMemberName(mobileObject.getString("MemberName"));
                                    mobileData.setMemberNumber(mobileObject.getString("MobileNo"));
                                    mobileList.add(mobileData);
                                }

                                ll_call.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Utils.showCallPopup(context, mobileList);
                                    }
                                });

                                ll_msg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Utils.showMsgPopup(context, mobileList);
                                    }
                                });

                            }


                        } else {
                            iv_call.setImageDrawable(getResources().getDrawable(R.drawable.call_gray));
                            iv_msg.setImageDrawable(getResources().getDrawable(R.drawable.message_gray));
                            ll_call.setEnabled(false);
                            ll_msg.setEnabled(false);
                        }

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
//                        }else

                        if (EmailIds != null && EmailIds.length() > 0) {

                            if (EmailIds.length() == 1) {

                                ll_mail.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {

                                        String email = null;

                                        try {

                                            email = EmailIds.getJSONObject(0).getString("EmailId");
                                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                                            emailIntent.setType("plain/text");
                                            emailIntent.setData(Uri.parse("mailto:"));
                                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                                            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                                            startActivity(emailIntent);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });

                            } else {
                                final ArrayList<CalendarData> emailList = new ArrayList<>();
                                for (int k = 0; k < EmailIds.length(); k++) {
                                    JSONObject emailObject = EmailIds.getJSONObject(k);
                                    CalendarData emailData = new CalendarData();
                                    emailData.setMemberName(emailObject.getString("MemberName"));
                                    emailData.setMemberEmail(emailObject.getString("EmailId"));
                                    emailList.add(emailData);
                                }
                                ll_mail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Utils.showEmailPopup(context, emailList);
                                    }
                                });
                            }


                        } else {
                            iv_mail.setImageDrawable(getResources().getDrawable(R.drawable.mail_gray));
                            ll_mail.setEnabled(false);

                        }

                        ll_bCont.addView(convertView);

                    } else if (type.equalsIgnoreCase("Anniversary")) {

                        ll_anni.setVisibility(View.VISIBLE);
                        final View convertView = layoutInflater.inflate(R.layout.calender_birhday_anniversary_item, null);

                        TextView txt_name = (TextView) convertView.findViewById(R.id.tv_name);
                        txt_name.setText(object.getString("title"));

                        ImageView iv_call, iv_msg, iv_mail;

                        iv_call = (ImageView) convertView.findViewById(R.id.iv_call);
                        iv_msg = (ImageView) convertView.findViewById(R.id.iv_msg);
                        iv_mail = (ImageView) convertView.findViewById(R.id.iv_mail);

                        LinearLayout ll_call, ll_msg, ll_mail, ll_whatsapp;
                        ll_whatsapp = (LinearLayout) convertView.findViewById(R.id.ll_whatsapp);
                        ll_call = (LinearLayout) convertView.findViewById(R.id.ll_call);
                        ll_msg = (LinearLayout) convertView.findViewById(R.id.ll_msg);
                        ll_mail = (LinearLayout) convertView.findViewById(R.id.ll_mail);


//                        final String email=object.getString("EmailId");
                        final JSONArray EmailIds = object.getJSONArray("EmailIds");
                        final String number = object.getString("ContactNumber");
                        final JSONArray MobileNo = object.getJSONArray("MobileNo");

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
//                        }else

                        ll_whatsapp.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                if (Utils.appInstalledOrNot(context)) {

                                    String url = "https://api.whatsapp.com/send?phone=" + number;//"https://wa.me/"+profileData.getMobile()+"/?text=hii";
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);

                                } else if (Utils.whatsBusinessAppInstalledOrNot(context)) {

                                    String url = "https://api.whatsapp.com/send?phone=" + number;//"https://wa.me/"+profileData.getMobile()+"/?text=hii";
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);

                                } else {
                                    Utils.showToastWithTitleAndContext(context, "WhatsApp is not installed");
                                }
                            }
                        });

                        if (MobileNo != null && MobileNo.length() > 0) {

                            if (MobileNo.length() == 1) {

                                ll_call.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        String number = null;
                                        try {
                                            number = MobileNo.getJSONObject(0).getString("MobileNo");
                                            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + number));
                                            startActivity(callIntent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });

                                ll_msg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String number = null;
                                        try {
                                            number = MobileNo.getJSONObject(0).getString("MobileNo");
                                            Intent msgIntent = new Intent(Intent.ACTION_VIEW);
                                            msgIntent.setData(Uri.parse("smsto: " + Uri.encode(number)));
                                            startActivity(msgIntent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                });
                            } else {
                                final ArrayList<CalendarData> mobileList = new ArrayList<>();
                                for (int k = 0; k < MobileNo.length(); k++) {
                                    JSONObject mobileObject = MobileNo.getJSONObject(k);
                                    CalendarData mobileData = new CalendarData();
                                    mobileData.setMemberName(mobileObject.getString("MemberName"));
                                    mobileData.setMemberNumber(mobileObject.getString("MobileNo"));
                                    mobileList.add(mobileData);
                                }
                                ll_call.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Utils.showCallPopup(context, mobileList);
                                    }
                                });

                                ll_msg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Utils.showMsgPopup(context, mobileList);
                                    }
                                });

                            }

                        } else {
                            iv_call.setImageDrawable(getResources().getDrawable(R.drawable.call_gray));
                            iv_msg.setImageDrawable(getResources().getDrawable(R.drawable.message_gray));
                            ll_call.setEnabled(false);
                            ll_call.setEnabled(false);
                        }

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
//                        }else

                        if (EmailIds != null && EmailIds.length() > 0) {
                            if (EmailIds.length() == 1) {
                                ll_mail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String email = null;
                                        try {
                                            email = EmailIds.getJSONObject(0).getString("EmailId");
                                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                                            emailIntent.setType("plain/text");
                                            emailIntent.setData(Uri.parse("mailto:"));
                                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                                            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                                            startActivity(emailIntent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });

                            } else {
                                final ArrayList<CalendarData> emailList = new ArrayList<>();
                                for (int k = 0; k < EmailIds.length(); k++) {
                                    JSONObject emailObject = EmailIds.getJSONObject(k);
                                    CalendarData emailData = new CalendarData();
                                    emailData.setMemberName(emailObject.getString("MemberName"));
                                    emailData.setMemberEmail(emailObject.getString("EmailId"));
                                    emailList.add(emailData);
                                }
                                ll_mail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Utils.showEmailPopup(context, emailList);
                                    }
                                });
                            }


                        } else {
                            iv_mail.setImageDrawable(getResources().getDrawable(R.drawable.mail_gray));
                            ll_mail.setEnabled(false);

                        }

                        ll_aCont.addView(convertView);

                    } else if (type.equalsIgnoreCase("Event")) {

                        ll_event.setVisibility(View.VISIBLE);
                        View convertView = layoutInflater.inflate(R.layout.calender_event_item, null);
                        TextView txt_name = (TextView) convertView.findViewById(R.id.tv_name);
                        txt_name.setText(object.getString("title"));
                        LinearLayout event_item = (LinearLayout) convertView.findViewById(R.id.ll_event);
                        final String eventID = object.getString("MemberID");

                        event_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                if (catID.equalsIgnoreCase(String.valueOf(Constant.GROUP_CATEGORY_CLUB))) {

                                    Intent intent = new Intent(context, EventDetails.class);
                                    intent.putExtra("eventid", eventID.substring(1));
                                    startActivity(intent);

                                } else if (catID.equalsIgnoreCase(String.valueOf(Constant.GROUP_CATEGORY_DT))) {
                                    Intent intent = new Intent(context, EventDetails.class);
                                    intent.putExtra("eventid", eventID.substring(1));

                                    try {
                                        if (object.getString("GroupId") != null && !object.getString("GroupId").equalsIgnoreCase("null")) {
                                            intent.putExtra("eventList", "");
                                            String grpId = object.getString("GroupId");
                                            intent.putExtra("grpID", grpId);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    startActivity(intent);
                                }
                            }
                        });

                        ll_eCont.addView(convertView);
                    }

                }

                if (events.length() <= 0) {
                    ll_error.setVisibility(View.VISIBLE);
                }

            }

//          CalendarEventsAdapter adapter = new CalendarEventsAdapter(getActivity().getApplicationContext(),eventList,celebrationType);
            progressDialog.dismiss();
        } catch (JSONException e) {
            progressDialog.dismiss();
            e.printStackTrace();
        }


    }


//    public void loadFromDB() {
//        Utils.log("Trying to load calendar from local database");
//        String mm = String.valueOf(month);
//        if (mm.length() <= 1) {
//            mm = "0" + mm;
//        }
//        String currentYearAndMonth = year + "-" + mm;
//        boolean isDataAvailable = calendarModel.isDataAvailable(grpId, currentYearAndMonth);
//        Log.e("DataAvailable", "Data available : " + isDataAvailable);
//
//        if (!isDataAvailable) {
//            Utils.log("Trying to load calendar from server");
//
//            if (InternetConnection.checkConnection(context)) {
//                webservices();
//
//            } else {
//
//            }
//        } else {
//
//        }
//
//    }

    public void checkForUpdate() {

        initDate = year + "-" + month + "-" + "01";

        Utils.log("webservices() is called");

        String url = Constant.GET_CALENDAR_MONTH_EVENTS;

        String myCategory = PreferenceManager.getPreference(context, MY_CATEGORY, "" + Constant.GROUP_CATEGORY_CLUB);

        ArrayList<NameValuePair> arrayList = new ArrayList<>();

        arrayList.add(new BasicNameValuePair("profileId", PreferenceManager.getPreference(context, PreferenceManager.GRP_PROFILE_ID)));
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(context, PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("selectedDate", initDate));
        arrayList.add(new BasicNameValuePair("groupCategory", myCategory));

        updatedOn = PreferenceManager.getPreference(context, TBPrefixes.NEW_CALENDAR_UPDATED_ON + grpId + initDate, "1970/01/01 00:00:00");

        Utils.log("Last updated date is" + updatedOn);

        arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));

        Utils.log("PARAMETERS " + Constant.GET_CALENDAR_MONTH_EVENTS + " :- " + arrayList.toString());

        CalendarEventAsynctask task = new CalendarEventAsynctask(url, arrayList);

        task.execute();
    }

    // method to fetch all calendar events for particular month
//    public void webservices() {
//        // as you are getting the data for whole month.start date of month will be same for all months. so 01 is kept hardcoded
//        initDate = year + "-" + month + "-" + "01";
//        Utils.log("webservices() is called");
//        String url = Constant.GET_CALENDAR_MONTH_EVENTS;
//        String myCategory = PreferenceManager.getPreference(context, MY_CATEGORY, "" + Constant.GROUP_CATEGORY_CLUB);
//
//        ArrayList<NameValuePair> arrayList = new ArrayList<>();
//        arrayList.add(new BasicNameValuePair("profileId", PreferenceManager.getPreference(context, PreferenceManager.GRP_PROFILE_ID)));
//        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(context, PreferenceManager.GROUP_ID)));
//        arrayList.add(new BasicNameValuePair("selectedDate", initDate));
//        arrayList.add(new BasicNameValuePair("groupCategory", myCategory));
//
//        updatedOn = PreferenceManager.getPreference(context, TBPrefixes.CALENDAR_UPDATED_ON + grpId + initDate, "1970/01/01 00:00:00");
//        Utils.log("Last updated date is" + updatedOn);
//        arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));
//        Log.e("Request", "PARAMETERS " + Constant.GET_CALENDAR_MONTH_EVENTS + " :- " + arrayList.toString());
//
//        CalendarEventAsynctask task = new CalendarEventAsynctask(url, arrayList);
//        task.execute();
//    }

    public class CalendarEventAsynctask extends AsyncTask<String, Object, Object> {
        String val = null;

        String url = null;

        List<NameValuePair> argList = null;

        public CalendarEventAsynctask(String url, List<NameValuePair> argList) {
            this.url = url;
            this.argList = argList;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context, R.style.TBProgressBar);
            dialog.setCancelable(false);
            dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            if (ll_calendar.getVisibility() == View.VISIBLE) {
                dialog.show();
            }
        }

        @Override
        protected Object doInBackground(String... params) {
            try {
                val = HttpConnection.postData(url, argList);
                val = val.toString();
                Utils.log("Server response : " + val);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return val;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            if (result != "" && result != null) {
                getCalendarEvents(result.toString());
            } else {
//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }
                Log.e("♦♦♦♦ Calendar", "Null response from server");
            }
        }


    }

    public void getCalendarEvents(String result) {
        try {
            Utils.log("Response from Server : " + result);
            JSONObject jsonObj = new JSONObject(result);
            JSONObject jsonTBEventListResult = jsonObj.getJSONObject("TBEventListResult");
            final String status = jsonTBEventListResult.getString("status");
            if (status.equals("0")) {
                updatedOn = jsonTBEventListResult.getString("updatedOn");
                final ArrayList<CalendarData> newEventsList = new ArrayList<CalendarData>();
                JSONObject jsonResult = jsonTBEventListResult.getJSONObject("Result");
                JSONArray jsonNewEventsList = jsonResult.getJSONArray("newEvents");

                int newEventsCount = jsonNewEventsList.length();

                for (int i = 0; i < newEventsCount; i++) {
                    CalendarData data = new CalendarData();

                    JSONObject result_object = jsonNewEventsList.getJSONObject(i);
                    data.setGroupId(Integer.parseInt(result_object.getString("groupID").toString()));
                    String uniqueId = result_object.getString("uniqueID").toString();//memberFamilyID
                    data.setMemberFamilyID(result_object.getString("memberFamilyID").toString());
                    data.setUniqueId(uniqueId);
                    data.setEventDate(result_object.getString("eventDate").toString());
                    data.setExpiryDate(result_object.getString("ExpiryDate").toString());
                    data.setType(result_object.getString("type").toString());
                    data.setTypeId(Integer.parseInt(result_object.getString("typeID").toString()));
                    data.setTitle(result_object.getString("title").toString());
                    newEventsList.add(data);
                }

                final ArrayList<CalendarData> updatedEventsList = new ArrayList<CalendarData>();
                JSONArray jsonUpdatedEventsList = jsonResult.getJSONArray("updatedEvents");

                int updatedEventsCount = jsonUpdatedEventsList.length();

                for (int i = 0; i < updatedEventsCount; i++) {
                    CalendarData data = new CalendarData();

                    JSONObject result_object = jsonUpdatedEventsList.getJSONObject(i);
                    data.setGroupId(Integer.parseInt(result_object.getString("groupID").toString()));
                    String uniqueId = result_object.getString("uniqueID").toString();//memberFamilyID

                    data.setUniqueId(uniqueId);
                    data.setEventDate(result_object.getString("eventDate").toString());
                    data.setExpiryDate(result_object.getString("ExpiryDate").toString());
                    data.setType(result_object.getString("type").toString());
                    data.setTypeId(Integer.parseInt(result_object.getString("typeID").toString()));
                    data.setTitle(result_object.getString("title").toString());
                    data.setMemberFamilyID(result_object.getString("memberFamilyID").toString());
                    updatedEventsList.add(data);
                }

                final ArrayList<CalendarData> deletedEventsList = new ArrayList<CalendarData>();
                JSONArray jsonDeletedEventsList = jsonResult.getJSONArray("deletedEvents");

                int deletedEventsCount = jsonDeletedEventsList.length();

                for (int i = 0; i < deletedEventsCount; i++) {
                    CalendarData data = new CalendarData();

                    JSONObject result_object = jsonDeletedEventsList.getJSONObject(i);
                    data.setGroupId(Integer.parseInt(result_object.getString("groupID").toString()));
                    String uniqueId = result_object.getString("uniqueID").toString();//memberFamilyID
                    data.setMemberFamilyID(result_object.getString("memberFamilyID").toString());
                    data.setUniqueId(uniqueId);
                    data.setEventDate(result_object.getString("eventDate").toString());
                    data.setExpiryDate(result_object.getString("ExpiryDate").toString());
                    data.setType(result_object.getString("type").toString());
                    data.setTypeId(Integer.parseInt(result_object.getString("typeID").toString()));
                    data.setTitle(result_object.getString("title").toString());


                    deletedEventsList.add(data);
                }


                int overAllCount = newEventsCount + updatedEventsCount + deletedEventsCount;
                System.out.println("Number of records received for calendar events  : " + overAllCount);
                if (newEventsCount + updatedEventsCount + deletedEventsCount != 0) {
                    boolean saved = calendarModel.syncData(grpId, newEventsList, updatedEventsList, deletedEventsList);
                    if (!saved) {
                        Log.e("Adding Failed------->", "Failed to update data in local db. Retrying in 2 seconds");

                    } else {
                        PreferenceManager.savePreference(context, TBPrefixes.NEW_CALENDAR_UPDATED_ON + grpId + initDate, updatedOn);
                        setMonthView();
                    }
                } else {
                    setMonthView();
                }

//                Handler calendarEventsHandler = new Handler() {
//
//                    @Override
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//                        boolean saved = calendarModel.syncData(grpId, newEventsList, updatedEventsList, deletedEventsList);
//                        if (!saved) {
//                            Log.e("Adding Failed------->", "Failed to update data in local db. Retrying in 2 seconds");
//                            sendEmptyMessageDelayed(0, 2000);
//                        } else {
//
//                            setMonthView();
//                        }
//                    }
//                };
//
//                int overAllCount = newEventsCount + updatedEventsCount + deletedEventsCount;
//                System.out.println("Number of records received for calendar events  : " + overAllCount);
//                if (newEventsCount + updatedEventsCount + deletedEventsCount != 0) {
//                    calendarEventsHandler.sendEmptyMessageDelayed(0, 1000);
//                } else {
//                    setMonthView();
//                }

            } else {
                setMonthView();
                Log.e("Response is", "Response:-" + result);
            }

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            Log.e("Exception:-", "Error is" + e.toString());
            e.printStackTrace();
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }

    }

    public void setMonthView() {
        String mm = String.valueOf(month);
        if (mm.length() <= 1) {
            mm = "0" + mm;
        }
        list.clear();
        // String profileID=PreferenceManager.getPreference(context, PreferenceManager.GRP_PROFILE_ID);
        list = calendarModel.getMonthData(grpId, catID, mm, String.valueOf(year), selectedDate);
        calendarView.removeAllEvents();
        calendarView.addEvents(list);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
