package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.NEWROW.row.R;


/**
 * Created by USER1 on 31-01-2017.
 */
public class EmptyViewHolder extends RecyclerView.ViewHolder {
    TextView tvEmptyView;
    public EmptyViewHolder(View itemView) {
        super(itemView);
        tvEmptyView = (TextView) itemView.findViewById(R.id.tvEmptyView);
        tvEmptyView.setVisibility(View.VISIBLE);
    }

    public TextView getEmptyView() {
        return tvEmptyView;
    }

    public void setEmptyView(TextView tvEmptyView) {
        this.tvEmptyView = tvEmptyView;
    }
}
