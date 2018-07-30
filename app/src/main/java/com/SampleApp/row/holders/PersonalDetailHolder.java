package com.SampleApp.row.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.SampleApp.row.R;


/**
 * Created by USER1 on 24-03-2017.
 */
public class PersonalDetailHolder extends RecyclerView.ViewHolder {
    TextView tvDynamicFieldTitle, tvDynamicFieldValue;
   public LinearLayout ll_item;

    public PersonalDetailHolder(View itemView) {
        super(itemView);
        tvDynamicFieldTitle = (TextView) itemView.findViewById(R.id.tvDynamicFieldTitle);
        tvDynamicFieldValue = (TextView) itemView.findViewById(R.id.tvDynamicFieldValue);
        ll_item=(LinearLayout)itemView.findViewById(R.id.ll_item);
    }

    public TextView getTvDynamicFieldTitle() {
        return tvDynamicFieldTitle;
    }

    public TextView getTvDynamicFieldValue() {
        return tvDynamicFieldValue;
    }
}
