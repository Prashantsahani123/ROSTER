package com.SampleApp.row.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.SampleApp.row.R;

/**
 * Created by admin on 27-04-2017.
 */

public class WebLinkListHolder extends RecyclerView.ViewHolder {
    public TextView tv_title;
    public TextView tv_designation;
    public LinearLayout linear;


    public WebLinkListHolder(View v) {
        super(v);
        tv_title = (TextView)v.findViewById(R.id.tv_name);
        tv_designation = (TextView)v.findViewById(R.id.tv_designation);
        linear = (LinearLayout)v.findViewById(R.id.linear);
    }
}
