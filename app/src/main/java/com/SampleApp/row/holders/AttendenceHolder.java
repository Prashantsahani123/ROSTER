package com.SampleApp.row.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.SampleApp.row.R;

/**
 * Created by Prasad on 03-06-2017.
 */

public class AttendenceHolder extends RecyclerView.ViewHolder{
    TextView tvEventName,tvDate,tvTime;

    public AttendenceHolder(View itemView) {
        super(itemView);
        tvEventName = (TextView) itemView.findViewById(R.id.tvEventName);
        tvDate = (TextView)itemView.findViewById(R.id.attendenceDate);
        tvTime = (TextView)itemView.findViewById(R.id.attendenceTime);
    }

    public TextView getTvEventName() {
        return tvEventName;
    }

    public void setTvEventName(TextView tvEventName) {
        this.tvEventName = tvEventName;
    }

    public TextView getTvDate() {
        return tvDate;
    }

    public void setTvDate(TextView tvDate) {
        this.tvDate = tvDate;
    }

    public TextView getTvTime() {
        return tvTime;
    }

    public void setTvTime(TextView tvTime) {
        this.tvTime = tvTime;
    }
}
