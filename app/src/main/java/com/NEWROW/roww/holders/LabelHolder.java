package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.NEWROW.row.R;

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
