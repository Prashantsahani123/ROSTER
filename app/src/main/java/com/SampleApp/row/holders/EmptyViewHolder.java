package com.SampleApp.row.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.SampleApp.row.R;


/**
 * Created by USER1 on 31-01-2017.
 */
public class EmptyViewHolder extends RecyclerView.ViewHolder {
    TextView tvEmptyView;
    public EmptyViewHolder(View itemView) {
        super(itemView);
        tvEmptyView = (TextView) itemView.findViewById(R.id.tvEmptyView);
    }

    public TextView getEmptyView() {
        return tvEmptyView;
    }

    public void setEmptyView(TextView tvEmptyView) {
        this.tvEmptyView = tvEmptyView;
    }
}
