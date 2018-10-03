package com.SampleApp.row.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.SampleApp.row.R;

/**
 * Created by Admin on 22-02-2018.
 */

public class MobileViewHolder extends RecyclerView.ViewHolder {

   public TextView txt_title,txt_mobile;
   public LinearLayout ll_call;


    public MobileViewHolder(View itemView) {
        super(itemView);
        txt_title=(TextView)itemView.findViewById(R.id.tvTitle);
        txt_mobile=(TextView)itemView.findViewById(R.id.tvPhoneNo);
        ll_call=(LinearLayout)itemView.findViewById(R.id.ll_call);
    }
}
