package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NEWROW.row.R;

/**
 * Created by admin on 30-05-2017.
 */

public class ClubHolder extends RecyclerView.ViewHolder {
    public TextView tv_name;
    public TextView tv_meetingday;
    public TextView tv_meetingtime;
    public LinearLayout linear;
    public TextView tv_distance;

    public ClubHolder(View v) {
        super(v);
        tv_name = (TextView)v.findViewById(R.id.tv_clubname);
        tv_meetingday = (TextView)v.findViewById(R.id.tv_meetingday);
        tv_meetingtime = (TextView)v.findViewById(R.id.tv_meetingtime);
        linear = (LinearLayout)v.findViewById(R.id.linear);
        tv_distance = (TextView)v.findViewById(R.id.tv_distance);
    }
}
