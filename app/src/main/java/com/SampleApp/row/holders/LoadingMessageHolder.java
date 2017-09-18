package com.SampleApp.row.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.SampleApp.row.R;

/**
 * Created by USER on 09-06-2017.
 */

public class LoadingMessageHolder extends RecyclerView.ViewHolder {
    TextView tvMessage;

    public LoadingMessageHolder(View itemView) {
        super(itemView);
        tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
    }

    public TextView getTvMessage() {
        return tvMessage;
    }

    public void setTvMessage(TextView tvMessage) {
        this.tvMessage = tvMessage;
    }

}
