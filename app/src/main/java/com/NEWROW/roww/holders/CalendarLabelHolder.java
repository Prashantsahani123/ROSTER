package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.NEWROW.row.R;
import com.NEWROW.row.Utils.Utils;

/**
 * Created by USER1 on 24-03-2017.
 */
public class CalendarLabelHolder extends RecyclerView.ViewHolder {
    TextView tvLabel, tvCount;
    public CalendarLabelHolder(View itemView) {
        super(itemView);
        try {
            tvLabel = (TextView) itemView.findViewById(R.id.tvLabel);
            tvCount = (TextView) itemView.findViewById(R.id.tvCount);
        } catch(Exception e) {
            Utils.log("Error is : "+e);
            e.printStackTrace();
        }
    }

    public TextView getTvLabel() {
        return tvLabel;
    }

    public void setTvLabel(TextView tvLabel) {
        this.tvLabel = tvLabel;
    }

    public TextView getTvCount() {
        return tvCount;
    }

    public void setTvCount(TextView tvCount) {
        this.tvCount = tvCount;
    }
}
