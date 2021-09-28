package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NEWROW.row.R;

/**
 * Created by admin on 28-04-2017.
 */

public class PastPresidentListHolder  extends RecyclerView.ViewHolder{
    public TextView tv_name,tv_designation;
    public ImageView iv_image;
    public LinearLayout linear;

    public PastPresidentListHolder(View v) {
        super(v);

        tv_name = (TextView)v.findViewById(R.id.tv_name);
        tv_designation = (TextView)v.findViewById(R.id.tv_designation);
        iv_image = (ImageView)v.findViewById(R.id.imageView);
        linear = (LinearLayout)v.findViewById(R.id.linear);
    }
}
