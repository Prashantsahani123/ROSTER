package com.NEWROW.row.calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.EventsAdapter;
import com.NEWROW.row.Adapter.MonthViewGridAdapter;
import com.NEWROW.row.Adapter.OptionMenuItemsAdapter;
import com.NEWROW.row.Data.CalendarData;
import com.NEWROW.row.Data.CalendarViewOption;
import com.NEWROW.row.R;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.HttpConnection;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.TBPrefixes;
import com.NEWROW.row.Utils.Utils;
import com.NEWROW.row.sql.CalendarMasterModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.NEWROW.row.Utils.PreferenceManager.MY_CATEGORY;


/**
 * Created by user on 02-02-2017.
 */
public class MonthActivity extends Activity {
    TextView tv_title, tv_date;
    ImageView iv_backbutton, iv_actionbtn, iv_actionbtn2;
    Context context;

    OptionMenuItemsAdapter optionMonthAdapter;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    public TextView tv_month;

    public Calendar calendar;
    private int month, year;

    ImageButton previous, next;
    private MonthViewGridAdapter gridAdapter;
    private GridView gridView;
    public String updatedOn = "";
    public long grpId;
    CalendarMasterModel calendarModel;
    ArrayList<CalendarData> calendarEventlist = new ArrayList<CalendarData>();
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    EventsAdapter recycleviewAdapter;
    String selecteddate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.month);

        // Getting calendar instance and setting current month and year
        calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);

        // realted to initialization of actionbar variable and on click methods
        actionbarfunction();

        // initialization of variables
        init();

        //method to load data
        loadFromDB();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String displayDate = "";
                String[] selecteddate = gridAdapter.list.get(position).split("-");
                String color = selecteddate[1];
                String day = selecteddate[0];
                int mm = gridAdapter.currentMonth;
                String month = String.valueOf(mm + 1);
                String year = selecteddate[3];

                if (color.equalsIgnoreCase("WHITE")) {

                } else {

                    gridAdapter.setSelected(view.findViewById(R.id.date), position);
//                    String daySuffix = getDaySuffix(day);
//                    tv_date.setText(android.text.format.DateFormat.format("MMMM ", calendar) + " " + day +daySuffix + " " +  "Events:");

                    if (day.length() <= 1) {
                        day = "0" + day;
                    }
                    if (month.length() <= 1) {
                        month = "0" + month;
                    }
                    String date = year + "-" + month + "-" + day;

                    calendarEventlist = calendarModel.getCalendarEvents(grpId, date);

                    recycleviewAdapter = new EventsAdapter(context, calendarEventlist);
                    recycleviewAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(recycleviewAdapter);
                }

            }
        });
    }


    public void loadFromDB() {
        Utils.log("Trying to load calendar from local database");
        String mm = String.valueOf(month);
        if (mm.length() <= 1) {
            mm = "0" + mm;
        }
        String currentYearAndMonth = year + "-" + mm;
        boolean isDataAvailable = calendarModel.isDataAvailable(grpId, currentYearAndMonth);
        Log.e("DataAvailable", "Data available : " + isDataAvailable);

        if (!isDataAvailable) {
            Utils.log("Trying to load calendar from server");

            if (InternetConnection.checkConnection(context)) {
                webservices();

            } else {
                gridAdapter = new MonthViewGridAdapter(getApplicationContext(), R.id.date, month, year, grpId);
                gridView.setAdapter(gridAdapter);

                Utils.log("No Internet Connection to load data");
                Toast.makeText(context, "Unable to load data from server.No Internet Connection", Toast.LENGTH_LONG).show();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                if (year == Calendar.getInstance().get(Calendar.YEAR) && (month - 1) == Calendar.getInstance().get(Calendar.MONTH)) {
                    String date = df.format(calendar.getTime());
                    calendarEventlist = calendarModel.getCalendarEvents(grpId, date);
                    recycleviewAdapter = new EventsAdapter(context, calendarEventlist);
                    mRecyclerView.setAdapter(recycleviewAdapter);
                    recycleviewAdapter.notifyDataSetChanged();
//                    SimpleDateFormat sdf  = new SimpleDateFormat("MMMM dd");
//                    String displayDate = sdf.format(calendar.getTime());
//                    SimpleDateFormat sdf_day  = new SimpleDateFormat("dd");
//                    String day = sdf_day.format(displayDate);
//                    tv_date.setText(displayDate +getDaySuffix(day)+" " + "Events:");

                } else {
                    String mon = String.valueOf(month);
                    if (mon.length() <= 1) {
                        mon = "0" + mon;
                    }
                    String date = year + "-" + mon;
                    calendarEventlist = calendarModel.getCalendarEvents(grpId, date);
                    recycleviewAdapter = new EventsAdapter(context, calendarEventlist);
                    mRecyclerView.setAdapter(recycleviewAdapter);
                    recycleviewAdapter.notifyDataSetChanged();
                    tv_date.setText(android.text.format.DateFormat.format("MMMM ", calendar) + "Events:");
                }
            }
        } else {
            gridAdapter = new MonthViewGridAdapter(getApplicationContext(), R.id.date, month, year, grpId);
            gridView.setAdapter(gridAdapter);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if (year == Calendar.getInstance().get(Calendar.YEAR) && (month - 1) == Calendar.getInstance().get(Calendar.MONTH)) {
                String date = df.format(calendar.getTime());
                calendarEventlist = calendarModel.getCalendarEvents(grpId, date);
                recycleviewAdapter = new EventsAdapter(context, calendarEventlist);
                mRecyclerView.setVisibility(View.VISIBLE);
                mRecyclerView.setAdapter(recycleviewAdapter);

                SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd");
                String displayDate = sdf.format(calendar.getTime());
                SimpleDateFormat sdf_day = new SimpleDateFormat("dd");
//                String day = sdf_day.format(calendar.getTime());
//                String dd = getDaySuffix(day);
//                tv_date.setText(displayDate +dd+" " + "Events:");

            } else {
                String mon = String.valueOf(month);
                if (mon.length() <= 1) {
                    mon = "0" + mon;
                }
                String date = year + "-" + mon;
                calendarEventlist = calendarModel.getCalendarEvents(grpId, date);
                recycleviewAdapter = new EventsAdapter(context, calendarEventlist);
                mRecyclerView.setAdapter(recycleviewAdapter);
                recycleviewAdapter.notifyDataSetChanged();
                tv_date.setText(android.text.format.DateFormat.format("MMMM ", calendar) + "Events:");
            }
            if (InternetConnection.checkConnection(this)) {
                checkForUpdate();
                Utils.log( "Check for update gets called------");
            } else {
                Toast.makeText(this, "No internet connection to get Updated Records", Toast.LENGTH_LONG).show();
                if (year == Calendar.getInstance().get(Calendar.YEAR) && (month - 1) == Calendar.getInstance().get(Calendar.MONTH)) {
                    String date = df.format(calendar.getTime());
                    calendarEventlist = calendarModel.getCalendarEvents(grpId, date);
                    recycleviewAdapter = new EventsAdapter(context, calendarEventlist);
                    mRecyclerView.setAdapter(recycleviewAdapter);
                    recycleviewAdapter.notifyDataSetChanged();
                    SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd");
                    String displayDate = sdf.format(calendar.getTime());
                    SimpleDateFormat sdf_day = new SimpleDateFormat("dd");
//                    String day = sdf_day.format(calendar.getTime());
//                    String dd = getDaySuffix(day);
//                    tv_date.setText(displayDate +dd+" " + "Events:");

                } else {
                    String mon = String.valueOf(month);
                    if (mon.length() <= 1) {
                        mon = "0" + mon;
                    }
                    String date = year + "-" + mon;
                    calendarEventlist = calendarModel.getCalendarEvents(grpId, date);
                    recycleviewAdapter = new EventsAdapter(context, calendarEventlist);
                    mRecyclerView.setAdapter(recycleviewAdapter);
                    recycleviewAdapter.notifyDataSetChanged();
                    tv_date.setText(android.text.format.DateFormat.format("MMMM ", calendar) + "Events:");
                }
            }
        }

    }

    public void checkForUpdate() {

        selecteddate = year + "-" + month + "-" + "01";
        Utils.log("webservices() is called");
        String url = Constant.GET_CALENDAR_MONTH_EVENTS;
        String myCategory = PreferenceManager.getPreference(context, MY_CATEGORY, "" + Constant.GROUP_CATEGORY_CLUB);

        ArrayList<NameValuePair> arrayList = new ArrayList<>();
        arrayList.add(new BasicNameValuePair("profileId", PreferenceManager.getPreference(context, PreferenceManager.GRP_PROFILE_ID)));
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(context, PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("selectedDate", selecteddate));
        arrayList.add(new BasicNameValuePair("groupCategory", myCategory));
        updatedOn = PreferenceManager.getPreference(context, TBPrefixes.NEW_CALENDAR_UPDATED_ON + grpId + selecteddate);
        Utils.log("Last updated date is" + updatedOn);
        arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));
        Utils.log("PARAMETERS " + Constant.GET_CALENDAR_MONTH_EVENTS + " :- " + arrayList.toString());
        CalendarEventAsynctask task = new CalendarEventAsynctask(url, arrayList, context);
        task.execute();
    }

    // method to fetch all calendar events for particular month
    public void webservices() {
        // as you are getting the data for whole month.start date of month will be same for all months. so 01 is kept hardcoded
        selecteddate = year + "-" + month + "-" + "01";
        Utils.log("webservices() is called");
        String url = Constant.GET_CALENDAR_MONTH_EVENTS;
        String myCategory = PreferenceManager.getPreference(context, MY_CATEGORY, "" + Constant.GROUP_CATEGORY_CLUB);

        ArrayList<NameValuePair> arrayList = new ArrayList<>();
        arrayList.add(new BasicNameValuePair("profileId", PreferenceManager.getPreference(context, PreferenceManager.GRP_PROFILE_ID)));
        arrayList.add(new BasicNameValuePair("groupId", PreferenceManager.getPreference(context, PreferenceManager.GROUP_ID)));
        arrayList.add(new BasicNameValuePair("selectedDate", selecteddate));
        arrayList.add(new BasicNameValuePair("groupCategory", myCategory));

        updatedOn = PreferenceManager.getPreference(context, TBPrefixes.NEW_CALENDAR_UPDATED_ON + grpId + selecteddate, "1970/01/01 00:00:00");
        Utils.log("Last updated date is" + updatedOn);
        arrayList.add(new BasicNameValuePair("updatedOn", updatedOn));
        Log.e("Request", "PARAMETERS " + Constant.GET_CALENDAR_MONTH_EVENTS + " :- " + arrayList.toString());

        CalendarEventAsynctask task = new CalendarEventAsynctask(url, arrayList, context);
        task.execute();
    }

    public class CalendarEventAsynctask extends AsyncTask<String, Object, Object> {
        String val = null;
        final ProgressDialog dialog = new ProgressDialog(MonthActivity.this, R.style.TBProgressBar);
        String url = null;
        Context context = null;
        List<NameValuePair> argList = null;

        public CalendarEventAsynctask(String url, List<NameValuePair> argList, Context ctx) {
            this.url = url;
            this.argList = argList;
            this.context = ctx;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            dialog.show();
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
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (result != "" && result != null) {
                getCalendarEvents(result.toString());
            } else {
                Log.e("♦♦♦♦ Calendar", "Null response from server");
            }
        }


    }

    public void getCalendarEvents(String result) {
        try {
            Utils.log("Response from Server : "+result);
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
                    data.setType(result_object.getString("type").toString());
                    data.setTypeId(Integer.parseInt(result_object.getString("typeID").toString()));
                    data.setTitle(result_object.getString("title").toString());

                    deletedEventsList.add(data);
                }

                Handler calendarEventsHandler = new Handler() {

                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        boolean saved = calendarModel.syncData(grpId, newEventsList, updatedEventsList, deletedEventsList);
                        if (!saved) {
                            Log.e("Adding Failed------->", "Failed to update data in local db. Retrying in 2 seconds");
                            sendEmptyMessageDelayed(0, 2000);
                        } else {
                            PreferenceManager.savePreference(context, TBPrefixes.NEW_CALENDAR_UPDATED_ON + grpId + selecteddate, updatedOn);
                            gridAdapter = new MonthViewGridAdapter(getApplicationContext(), R.id.date, month, year, grpId);
                            gridAdapter.notifyDataSetChanged();
                            gridView.setAdapter(gridAdapter);

                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                            if (year == Calendar.getInstance().get(Calendar.YEAR) && (month - 1) == Calendar.getInstance().get(Calendar.MONTH)) {
                                String date = df.format(calendar.getTime());
                                calendarEventlist = calendarModel.getCalendarEvents(grpId, date);
                                recycleviewAdapter = new EventsAdapter(context, calendarEventlist);
                                mRecyclerView.setAdapter(recycleviewAdapter);
                                recycleviewAdapter.notifyDataSetChanged();

                                SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd");
                                String displayDate = sdf.format(calendar.getTime());
                                SimpleDateFormat sdf_day = new SimpleDateFormat("dd");
//                                String day = sdf_day.format(calendar.getTime());
//                                String dd = getDaySuffix(day);
//                                tv_date.setText(displayDate +dd+" " + "Events:");
                            } else {
                                String mm = String.valueOf(month);
                                if (mm.length() <= 1) {
                                    mm = "0" + mm;
                                }
                                String date = year + "-" + mm;
                                calendarEventlist = calendarModel.getCalendarEvents(grpId, date);
                                recycleviewAdapter = new EventsAdapter(context, calendarEventlist);
                                mRecyclerView.setAdapter(recycleviewAdapter);
                                recycleviewAdapter.notifyDataSetChanged();
                                tv_date.setText(android.text.format.DateFormat.format("MMMM ", calendar) + "Events:");
                            }

                        }
                    }
                };

                int overAllCount = newEventsCount + updatedEventsCount + deletedEventsCount;
                System.out.println("Number of records received for calendar events  : " + overAllCount);
                if (newEventsCount + updatedEventsCount + deletedEventsCount != 0) {
                    calendarEventsHandler.sendEmptyMessageDelayed(0, 1000);
                } else {
                    Log.e("NoUpdate", "No updates found for calendar");

                    gridAdapter = new MonthViewGridAdapter(getApplicationContext(), R.id.date, month, year, grpId);
                    gridView.setAdapter(gridAdapter);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                    if (year == Calendar.getInstance().get(Calendar.YEAR) && (month - 1) == Calendar.getInstance().get(Calendar.MONTH)) {
                        String date = df.format(calendar.getTime());
                        calendarEventlist = calendarModel.getCalendarEvents(grpId, date);
                        recycleviewAdapter = new EventsAdapter(context, calendarEventlist);
                        mRecyclerView.setAdapter(recycleviewAdapter);
                        recycleviewAdapter.notifyDataSetChanged();
                        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd");
                        String displayDate = sdf.format(calendar.getTime());
                        SimpleDateFormat sdf_day = new SimpleDateFormat("dd");
//                        String day = sdf_day.format(calendar.getTime());
//                        String dd = getDaySuffix(day);
//                        tv_date.setText(displayDate +dd+" " + "Events:");

                    } else {
                        String mm = String.valueOf(month);
                        if (mm.length() <= 1) {
                            mm = "0" + mm;
                        }
                        String date = year + "-" + mm;
                        calendarEventlist = calendarModel.getCalendarEvents(grpId, date);
                        recycleviewAdapter = new EventsAdapter(context, calendarEventlist);
                        mRecyclerView.setAdapter(recycleviewAdapter);
                        recycleviewAdapter.notifyDataSetChanged();
                        tv_date.setText(android.text.format.DateFormat.format("MMMM ", calendar) + "Events:");
                    }
                }

            } else {
                gridAdapter = new MonthViewGridAdapter(getApplicationContext(), R.id.date, month, year, grpId);
                gridView.setAdapter(gridAdapter);
                Log.e("Response is", "Response:-" + result);
            }

        } catch (Exception e) {
            Log.e("Exception:-", "Error is" + e.toString());
            e.printStackTrace();
            gridAdapter = new MonthViewGridAdapter(getApplicationContext(), R.id.date, month, year, grpId);
            gridView.setAdapter(gridAdapter);
        }

    }

    private void init() {
        context = MonthActivity.this;
        grpId = Long.parseLong(PreferenceManager.getPreference(this, PreferenceManager.GROUP_ID));
        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("Calendar");
        previous = (ImageButton) findViewById(R.id.ib_prev);
        next = (ImageButton) findViewById(R.id.Ib_next);
        gridView = (GridView) findViewById(R.id.gv_calendar);
        calendarModel = new CalendarMasterModel(context);
        calendarModel.printTable();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", calendar));
        gridAdapter = new MonthViewGridAdapter(getApplicationContext(), R.id.date, month, year, grpId);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        tv_date = (TextView) findViewById(R.id.tv_date);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (month <= 1) {
                    month = 12;
                    year--;
                } else {
                    month--;
                }
                Log.d("Calendar", "Setting Prev Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
                setGridCellAdapterToDate(month, year);
                calendarEventlist.clear();
                recycleviewAdapter.notifyDataSetChanged();
                loadFromDB();
//                String mm = String.valueOf(month);
//                if(mm.length()<=1){
//                    mm = "0" + mm;
//                }
//                String date = year + "-" + mm + "-" + " ";
//                calendarEventlist = calendarModel.getCalendarEvents(grpId,date);
//                recycleviewAdapter = new EventsAdapter(context,calendarEventlist);
//                recycleviewAdapter.notifyDataSetChanged();
//                mRecyclerView.setAdapter(recycleviewAdapter);


            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (month > 11) {
                    month = 1;
                    year++;
                } else {
                    month++;
                }
                Log.d("Calendar", "Setting Next Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
                setGridCellAdapterToDate(month, year);
                calendarEventlist.clear();
                recycleviewAdapter.notifyDataSetChanged();
                loadFromDB();
            }
        });

    }

    private void actionbarfunction() {
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn2 = (ImageView) findViewById(R.id.iv_actionbtn2);
        iv_actionbtn.setVisibility(View.VISIBLE);
        iv_actionbtn2.setVisibility(View.GONE);
        iv_backbutton.setVisibility(View.VISIBLE);
        iv_actionbtn.setImageResource(R.drawable.calendar_blue);
        iv_actionbtn2.setImageResource(R.drawable.overflow_btn_blue);


        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                month = calendar.get(Calendar.MONTH) + 1;
                year = calendar.get(Calendar.YEAR);
                tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", calendar));
                loadFromDB();
            }
        });

        iv_actionbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MonthActivity.this, iv_actionbtn2);
                popup.getMenu().add(1, R.id.date, 1, "Today");
                popup.getMenu().add(1, R.id.view, 1, "View");
                popup.getMenu().add(1, R.id.btn_search, 1, "Filter");

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.date:

                                Intent i = new Intent(context, DayActivity.class);
                                startActivity(i);
                                return true;

                            case R.id.view:
                                builder = new AlertDialog.Builder(MonthActivity.this);
                                LayoutInflater inflater = (LayoutInflater) MonthActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View view = inflater.inflate(R.layout.custom_dialog_title, null);
                                builder.setCustomTitle(view);
                                ImageView close = (ImageView) view.findViewById(R.id.close);

                                ArrayList<CalendarViewOption> items = new ArrayList<CalendarViewOption>();
                                CalendarViewOption optionDay = new CalendarViewOption("Days", R.drawable.schedule_blue);
                                CalendarViewOption optionToday = new CalendarViewOption("Today", R.drawable.day_blue);
                                CalendarViewOption optionYear = new CalendarViewOption("Year", R.drawable.year_blue);

                                items.add(optionDay);
                                items.add(optionToday);
                                items.add(optionYear);

                                optionMonthAdapter = new OptionMenuItemsAdapter(MonthActivity.this, items);
                                builder.setAdapter(optionMonthAdapter, null);

                                dialog = builder.create();
                                dialog.show();

                                close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.hide();
                                    }
                                });

                                return true;

                            case R.id.btn_search:
                                final Dialog dialog_filter = new Dialog(MonthActivity.this, android.R.style.Theme_Translucent);
                                dialog_filter.setContentView(R.layout.layout_filter);
                                ImageView filter_close = (ImageView) dialog_filter.findViewById(R.id.close);
                                filter_close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog_filter.dismiss();
                                    }
                                });
                                dialog_filter.show();
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();

            }
        });

    }

    // method used to set month and year in calendar to based on click of previous or next icon
    private void setGridCellAdapterToDate(int month, int year) {
        gridAdapter = new MonthViewGridAdapter(getApplicationContext(), R.id.date, month, year, grpId);
        calendar.set(year, month - 1, calendar.get(Calendar.DAY_OF_MONTH));
        gridView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();
        //  used to set month with year in header
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", calendar));
    }

    public String getDaySuffix(String day) {

        if (day.equalsIgnoreCase("1") || day.equalsIgnoreCase("21") || day.equalsIgnoreCase("31")) {
            return "st";
        } else if (day.equalsIgnoreCase("2") || day.equalsIgnoreCase("22")) {
            return "nd";
        } else if (day.equalsIgnoreCase("3") || day.equalsIgnoreCase("23")) {
            return "rd";
        } else {
            return "th";
        }
    }

}
