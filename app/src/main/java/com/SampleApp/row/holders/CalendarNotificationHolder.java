package com.SampleApp.row.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.SampleApp.row.R;


/**
 * Created by user on 21-02-2017.
 */
public class CalendarNotificationHolder extends RecyclerView.ViewHolder {
    public TextView tv_name, tv_relation;
    public ImageView iv_call;
    public ImageView iv_sms;

    public CalendarNotificationHolder(View v){
        super(v);
        tv_name = (TextView)v.findViewById(R.id.name);
        tv_relation = (TextView) v.findViewById(R.id.tvRelation);
        iv_call = (ImageView) v.findViewById(R.id.call);
        iv_sms = (ImageView) v.findViewById(R.id.sms);
    }


    public TextView getname() {
        return tv_name;
    }

    public void setname(TextView tv_name) {
        this.tv_name = tv_name;
    }

}
