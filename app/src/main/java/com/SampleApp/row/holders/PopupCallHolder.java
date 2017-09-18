package com.SampleApp.row.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.SampleApp.row.R;


/**
 * Created by USER1 on 17-04-2017.
 */
public class PopupCallHolder extends RecyclerView.ViewHolder {
    TextView tvTitle, tvPhoneNo, tvExtra;
    public PopupCallHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        tvExtra = (TextView) itemView.findViewById(R.id.tvExtraInfo);
        tvPhoneNo = (TextView) itemView.findViewById(R.id.tvPhoneNo);
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }

    public TextView getTvPhoneNo() {
        return tvPhoneNo;
    }

    public void setTvPhoneNo(TextView tvPhoneNo) {
        this.tvPhoneNo = tvPhoneNo;
    }

    public TextView getTvExtra() {
        return tvExtra;
    }

    public void setTvExtra(TextView tvExtra) {
        this.tvExtra = tvExtra;
    }
}
