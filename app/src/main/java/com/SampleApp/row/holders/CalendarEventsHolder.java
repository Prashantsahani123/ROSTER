package com.SampleApp.row.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.SampleApp.row.R;


/**
 * Created by user on 13-02-2017.
 */
public class CalendarEventsHolder extends RecyclerView.ViewHolder {


       public TextView tv_date;
       public TextView tv_tile;
       public TextView tv_type;
       public TextView tv_eventtime;
       public LinearLayout ll_calendar;
       public TextView tv_name;


    public CalendarEventsHolder(View view){
        super(view);
        tv_date = (TextView)view.findViewById(R.id.date);
        tv_tile = (TextView)view.findViewById(R.id.title);
        tv_type = (TextView)view.findViewById(R.id.type);
        tv_eventtime = (TextView)view.findViewById(R.id.eventtime);
        ll_calendar = (LinearLayout)view.findViewById(R.id.ll_calendar);
        tv_name = (TextView)view.findViewById(R.id.tv_name);
    }

    public TextView getDate() {
        return tv_date;
    }

    public void setDate(TextView tv_date) {
        this.tv_date = tv_date;
    }

    public TextView getTitle() {
        return tv_tile;
    }

    public void setTitle(TextView title) {
        this.tv_tile = title;
    }

    public TextView getType() {
        return tv_type;
    }

    public void setType(TextView tv_type) {
        this.tv_type = tv_type;
    }

    public TextView getEventtime() {
        return tv_eventtime;
    }

    public void setEventtime(TextView tv_eventtime) {
        this.tv_eventtime = tv_eventtime;
    }

    public TextView getTv_name() {
        return tv_name;
    }

    public void setTv_name(TextView tv_name) {
        this.tv_name = tv_name;
    }
}
