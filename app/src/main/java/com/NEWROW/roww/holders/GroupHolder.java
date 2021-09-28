package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NEWROW.row.R;

/**
 * Created by admin on 25-05-2017.
 */

public class GroupHolder extends RecyclerView.ViewHolder {

   public ImageView iv_img;
   public TextView tv_title;
    public TextView tv_description;
   public TextView tv_group_count;
    public LinearLayout ll_holder;
    public GroupHolder(View v) {
        super(v);
        ll_holder=(LinearLayout)v.findViewById(R.id.ll_holder);
        iv_img = (ImageView) v.findViewById(R.id.imageView);
        tv_title = (TextView)v.findViewById(R.id.title);
        tv_description = (TextView)v.findViewById(R.id.description);
        tv_group_count = (TextView)v.findViewById(R.id.tv_group_count);
    }
}
