package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.NEWROW.row.R;

/**
 * Created by Admin on 8/2/2017.
 */

public class DTNewslettersHolder extends RecyclerView.ViewHolder {
    TextView tvNewsletterTitle, tvDateTime;
    public DTNewslettersHolder(View itemView) {
        super(itemView);
        try {
            tvNewsletterTitle = (TextView) itemView.findViewById(R.id.tvNewsLettersTitle);
            tvDateTime = (TextView) itemView.findViewById(R.id.tvNewsLettersTime);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public TextView getTvNewsletterTitle() {
        return tvNewsletterTitle;
    }

    public void setTvNewsletterTitle(TextView tvNewsletterTitle) {
        this.tvNewsletterTitle = tvNewsletterTitle;
    }

    public TextView getTvDateTime() {
        return tvDateTime;
    }

    public void setTvDateTime(TextView tvDateTime) {
        this.tvDateTime = tvDateTime;
    }
}
