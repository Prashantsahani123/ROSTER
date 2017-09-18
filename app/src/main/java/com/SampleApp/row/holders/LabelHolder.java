package com.SampleApp.row.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.SampleApp.row.R;

/**
 * Created by admin on 25-05-2017.
 */

public class LabelHolder extends RecyclerView.ViewHolder {
       TextView tvLabel;

    public LabelHolder(View itemView) {
        super(itemView);
        tvLabel = (TextView) itemView.findViewById(R.id.tvMyLabel);
    }

    public TextView getTvLabel() {
        return tvLabel;
    }

    public void setTvLabel(TextView tvLabel) {
        this.tvLabel = tvLabel;
    }
}
