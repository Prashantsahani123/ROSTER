package com.SampleApp.row.calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.SampleApp.row.Adapter.OptionMenuItemsAdapter;
import com.SampleApp.row.Adapter.ScheduleAdapter;
import com.SampleApp.row.Data.CalendarViewOption;
import com.SampleApp.row.R;

/**
 * Created by user on 22-08-2016.
 */
public class ScheduleView  extends Activity{
    TextView tv_title,tv_month;
    ImageView iv_backbutton,iv_actionbtn2,iv_actionbtn;
    Calendar cal;
    String date;
    Context context;
    ScheduleAdapter adapter;
    ArrayList<String> list = new ArrayList<String>();
    ListView lv;

    OptionMenuItemsAdapter optionMenuAdapter;
    AlertDialog.Builder builder;
    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_schedule);
        actionbarfunction();
        context = ScheduleView.this;
        list.add("1");
        list.add("8");
        list.add("16");
        list.add("18");
        list.add("25");
        list.add("27");

        lv = (ListView)findViewById(R.id.listview);
        adapter = new ScheduleAdapter(context,list);
        lv.setAdapter(adapter);

        cal = GregorianCalendar.getInstance();
        date = String.valueOf(cal.get(Calendar.DATE));


        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal));
            }

    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn2= (ImageView) findViewById(R.id.iv_actionbtn2);
        tv_title.setText("Calendar");
        iv_actionbtn.setVisibility(View.VISIBLE);
        iv_actionbtn2.setVisibility(View.VISIBLE);
        iv_backbutton.setVisibility(View.VISIBLE);
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
                PopupMenu popup = new PopupMenu(ScheduleView.this,iv_actionbtn2);
                popup.getMenu().add(1,R.id.date,1,"Today");
                popup.getMenu().add(1,R.id.view,1,"View");
                popup.getMenu().add(1,R.id.btn_search,1,"Filter");

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.date:

                                Intent i = new Intent(context, DayActivity.class);
                                startActivity(i);
                                return true;

                            case R.id.view:
                                builder = new AlertDialog.Builder(ScheduleView.this);
                                //dialog_view.setContentView(R.layout.layout_view);
                                LayoutInflater inflater = (LayoutInflater)ScheduleView.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View view = inflater.inflate(R.layout.custom_dialog_title, null);
                                builder.setCustomTitle(view);
                                ImageView close = (ImageView)view.findViewById(R.id.close);


                                ArrayList<CalendarViewOption> items = new ArrayList<CalendarViewOption>();
                                CalendarViewOption optionDay = new CalendarViewOption("Today",R.drawable.day_blue);
                                CalendarViewOption optionToday = new CalendarViewOption("Month",R.drawable.month_blue);
                                CalendarViewOption optionYear = new CalendarViewOption("Year",R.drawable.year_blue);

                                items.add(optionDay);
                                items.add(optionToday);
                                items.add(optionYear);

                                optionMenuAdapter = new OptionMenuItemsAdapter(ScheduleView.this,items);
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

//                                final Dialog dialog_view = new Dialog(ScheduleView.this,android.R.style.Theme_Translucent);
//                                dialog_view.setContentView(R.layout.layout_view);
//                                LinearLayout ll_year = (LinearLayout)dialog_view.findViewById(R.id.ll_year);
//                                LinearLayout ll_schedule = (LinearLayout)dialog_view.findViewById(R.id.ll_schedule);
//                                LinearLayout ll_day = (LinearLayout)dialog_view.findViewById(R.id.ll_day);
//
//                                LinearLayout ll_month = (LinearLayout)dialog_view.findViewById(R.id.ll_month);
//
//                                ll_month.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog_view.dismiss();
//                                        Intent i = new Intent(ScheduleView.this,MonthActivity.class);
//                                        startActivity(i);
//                                    }
//                                });
//
//                                ll_day.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog_view.dismiss();
//                                        Intent i = new Intent(ScheduleView.this,DayActivity.class);
//                                        startActivity(i);
//                                    }
//                                });
//                                ll_schedule.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog_view.dismiss();
//
//                                    }
//                                });
//                                ll_year.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        dialog_view.dismiss();
//                                        Intent i = new Intent(ScheduleView.this,YearActivity.class);
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


                            case R.id.btn_search:
                                final Dialog dialog_filter = new Dialog(ScheduleView.this,android.R.style.Theme_Translucent);
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
