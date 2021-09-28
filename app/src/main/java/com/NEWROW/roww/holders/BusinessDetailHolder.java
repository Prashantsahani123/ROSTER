package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.NEWROW.row.R;


/**
 * Created by USER1 on 24-03-2017.
 */

public class BusinessDetailHolder extends RecyclerView.ViewHolder {

    TextView tvDynamicFieldTitle, tvDynamicFieldValue;

    public BusinessDetailHolder(View itemView) {
        super(itemView);
        tvDynamicFieldTitle = (TextView) itemView.findViewById(R.id.tvDynamicFieldTitle);
        tvDynamicFieldValue = (TextView) itemView.findViewById(R.id.tvDynamicFieldValue);
    }
    public TextView getTvDynamicFieldTitle() {
        return tvDynamicFieldTitle;
    }

    public TextView getTvDynamicFieldValue() {
        return tvDynamicFieldValue;
    }
}
