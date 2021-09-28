package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NEWROW.row.R;
import com.NEWROW.row.Utils.SquareImageView;

/**
 * Created by admin on 25-05-2017.
 */

public class RotaryIndiaAdminHolder extends RecyclerView.ViewHolder {

   public SquareImageView iv_img;
   public TextView tv_title;
    public TextView tv_description;
   public TextView tv_group_count;
    public LinearLayout ll_holder;
    public RotaryIndiaAdminHolder(View v) {
        super(v);
       // ll_holder=(LinearLayout)v.findViewById(R.id.ll_holder);
        iv_img = (SquareImageView) v.findViewById(R.id.image_view);
        tv_title = (TextView)v.findViewById(R.id.text_view);
     //   tv_description = (TextView)v.findViewById(R.id.description);
       // tv_group_count = (TextView)v.findViewById(R.id.tv_group_count);
    }
}
