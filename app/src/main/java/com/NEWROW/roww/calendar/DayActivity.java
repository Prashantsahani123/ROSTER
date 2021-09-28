package com.NEWROW.row.calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.NEWROW.row.Adapter.OptionMenuItemsAdapter;
import com.NEWROW.row.Data.CalendarViewOption;
import com.NEWROW.row.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Created by user on 11-08-2016.
 */
public class DayActivity extends Activity {
    TextView tv_title,tv_month,tv_1am;
    ImageView iv_backbutton,iv_actionbtn2,iv_actionbtn,iv_prev,iv_next;
    Calendar cal;
    String date,month,year;
    Context context;

    OptionMenuItemsAdapter optionMenuAdapter;
    AlertDialog.Builder builder;
    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_day);

        context = DayActivity.this;
        actionbarfunction();
        tv_month = (TextView) findViewById(R.id.tv_year);
        iv_prev = (ImageView)findViewById(R.id.iv_prev);
        iv_next = (ImageView)findViewById(R.id.iv_next);

        cal = GregorianCalendar.getInstance();
         date = String.valueOf(cal.get(Calendar.DATE));
         month = new SimpleDateFormat("MMMM").format(cal.getTime());
         year = String.valueOf(cal.get(Calendar.YEAR));
        tv_month.setText(month +" " + date + ","+ " " + year);



        iv_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.set(Calendar.DATE,(cal.get(Calendar.DATE)-1));
                date = String.valueOf(cal.get(Calendar.DATE));
                month = new SimpleDateFormat("MMMM").format(cal.getTime());
                year = String.valueOf(cal.get(Calendar.YEAR));
                tv_month.setText(month +" " + date + ","+ " " + year);
            }
        });

        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.set(Calendar.DATE,(cal.get(Calendar.DATE)+1));
                date = String.valueOf(cal.get(Calendar.DATE));
                month = new SimpleDateFormat("MMMM").format(cal.getTime());
                year = String.valueOf(cal.get(Calendar.YEAR));
                tv_month.setText(month +" " + date + ","+ " " + year);
            }
        });

    }



//    private int getEventTimeFrame(Date start, Date end){
//        long timeDifference = end.getTime() - start.getTime();
//        Calendar mCal = Calendar.getInstance();
//        mCal.setTimeInMillis(timeDifference);
//        int hours = mCal.get(Calendar.HOUR);
//        int minutes = mCal.get(Calendar.MINUTE);
//        return (hours * 60) + ((minutes * 60) / 100);
//    }
//
//    private void displayEventSection(Date eventDate, int height, String message){
//        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss aa", Locale.ENGLISH);
//        String displayValue = timeFormatter.format(eventDate);
//        String[]hourMinutes = displayValue.split(":");
//        int hours = Integer.parseInt(hourMinutes[0]);
//        int minutes = Integer.parseInt(hourMinutes[1]);
//        Log.d("♦♦♦Hours", "Hour value " + hours);
//        Log.d("♦♦♦Minutes", "Minutes value " + minutes);
//        int topViewMargin = (hours * 60) + ((minutes * 60) / 100);
//        Log.d("", "Margin top " + topViewMargin);
//        createEventView(topViewMargin, height, message);
//    }
//
//
//    private void createEventView(int topMargin, int height, String message){
//        TextView mEventView = new TextView(DayActivity.this);
//        RelativeLayout.LayoutParams lParam = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        lParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//        lParam.topMargin = topMargin * 2;
//        lParam.leftMargin = 24;
//        mEventView.setLayoutParams(lParam);
//        mEventView.setPadding(24, 0, 24, 0);
//        mEventView.setHeight(height * 2);
//        mEventView.setGravity(0x11);
//        mEventView.setTextColor(Color.parseColor("#ffffff"));
//        mEventView.setText(message);
//        mEventView.setBackgroundColor(Color.parseColor("#3F51B5"));
//        mLayout.addView(mEventView, eventIndex - 1);
//    }
    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn2= (ImageView) findViewById(R.id.iv_actionbtn2);
        tv_title.setText("Calendar");
        iv_actionbtn.setVisibility(View.VISIBLE);
        iv_backbutton.setVisibility(View.VISIBLE);
        iv_actionbtn2.setVisibility(View.VISIBLE);
        iv_actionbtn.setImageResource(R.drawable.search_btn_blue);
        iv_actionbtn2.setImageResource(R.drawable.overflow_btn_blue);


        iv_actionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        iv_actionbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(DayActivity.this,iv_actionbtn2);
                popup.getMenu().add(1,R.id.view,1,"View");
                popup.getMenu().add(1,R.id.btn_search,1,"Filter");

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.date:
                                return true;

                            case R.id.view:

                                builder = new AlertDialog.Builder(DayActivity.this);
                                //dialog_view.setContentView(R.layout.layout_view);
                                LayoutInflater inflater = (LayoutInflater)DayActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View view = inflater.inflate(R.layout.custom_dialog_title, null);
                                builder.setCustomTitle(view);
                                ImageView close = (ImageView)view.findViewById(R.id.close);


                                ArrayList<CalendarViewOption> items = new ArrayList<CalendarViewOption>();
                                CalendarViewOption optionDay = new CalendarViewOption("Days",R.drawable.schedule_blue);
                                CalendarViewOption optionToday = new CalendarViewOption("Month",R.drawable.month_blue);
                                CalendarViewOption optionYear = new CalendarViewOption("Year",R.drawable.year_blue);

                                items.add(optionDay);
                                items.add(optionToday);
                                items.add(optionYear);

                                optionMenuAdapter = new OptionMenuItemsAdapter(DayActivity.this,items);
                                builder.setAdapter(optionMenuAdapter,null);

                                dialog = builder.create();
                                dialog.show();

                                close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.hide();
                                    }
                                });

                                return true;

//                                final Dialog dialog_view = new Dialog(DayActivity.this,android.R.style.Theme_Translucent);
//                                dialog_view.setContentView(R.layout.layout_view);
//                                LinearLayout ll_year = (LinearLayout)dialog_view.findViewById(R.id.ll_year);
//                                LinearLayout ll_schedule = (LinearLayout)dialog_view.findViewById(R.id.ll_schedule);
//                                LinearLayout ll_day = (LinearLayout)dialog_view.findViewById(R.id.ll_day);
//                                LinearLayout ll_month = (LinearLayout)dialog_view.findViewById(R.id.ll_month);
//
//                                ll_month.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog_view.dismiss();
//                                        Intent i = new Intent(DayActivity.this,MonthActivity.class);
//                                        startActivity(i);
//                                    }
//                                });
//
//                                ll_day.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog_view.dismiss();
//                                    }
//                                });
//                                ll_schedule.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog_view.dismiss();
//                                        Intent i = new Intent(DayActivity.this,ScheduleView.class);
//                                        startActivity(i);
//                                    }
//                                });
//                                ll_year.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog_view.dismiss();
//                                        Intent i = new Intent(DayActivity.this,YearActivity.class);
//                                        startActivity(i);
//                                    }
//                                });
//                                ImageView view_close = (ImageView)dialog_view.findViewById(R.id.close);
//                                view_close.setOnClickListener(new View.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog_view.dismiss();
//
//                                    }
//                                });
//                                dialog_view.show();
//                                return true;

                            case R.id.btn_search:
                                final Dialog dialog_filter = new Dialog(DayActivity.this,android.R.style.Theme_Translucent);
                                dialog_filter.setContentView(R.layout.layout_filter);
                                ImageView filter_close = (ImageView)dialog_filter.findViewById(R.id.close);
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


}
