package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.NEWROW.row.R;

/**
 * Created by admin on 25-05-2017.
 */

public class FeedHolder extends RecyclerView.ViewHolder {
    TextView tvTitle, tvDescription;

    public FeedHolder(View itemView) {
        super(itemView);

        tvTitle = (TextView) itemView.findViewById(R.id.title);
        tvDescription = (TextView) itemView.findViewById(R.id.description);
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }

    public TextView getTvDescription() {
        return tvDescription;
    }

    public void setTvDescription(TextView tvDescription) {
        this.tvDescription = tvDescription;
    }
}
