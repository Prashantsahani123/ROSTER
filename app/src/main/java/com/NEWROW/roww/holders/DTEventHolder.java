package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.NEWROW.row.R;

/**
 * Created by Admin on 8/2/2017.
 */

public class DTEventHolder extends RecyclerView.ViewHolder {
    TextView tvEventTitle, tvEventTime;
    public DTEventHolder(View itemView) {
        super(itemView);
        try {
            tvEventTitle = (TextView) itemView.findViewById(R.id.tvEventTitle);
            tvEventTime = (TextView) itemView.findViewById(R.id.tvEventTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TextView getTvEventTitle() {
        return tvEventTitle;
    }

    public void setTvEventTitle(TextView tvEventTitle) {
        this.tvEventTitle = tvEventTitle;
    }

    public TextView getTvEventTime() {
        return tvEventTime;
    }

    public void setTvEventTime(TextView tvEventTime) {
        this.tvEventTime = tvEventTime;
    }
}
