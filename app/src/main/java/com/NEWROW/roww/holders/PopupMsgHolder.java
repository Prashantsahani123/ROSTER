package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.NEWROW.row.R;


/**
 * Created by USER1 on 17-04-2017.
 */
public class PopupMsgHolder extends RecyclerView.ViewHolder {
    TextView tvTitle, tvPhoneNo, tvExtra;
    CheckBox cbSelected;
    public PopupMsgHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        tvExtra = (TextView) itemView.findViewById(R.id.tvExtraInfo);
        tvPhoneNo = (TextView) itemView.findViewById(R.id.tvPhoneNo);
        cbSelected = (CheckBox) itemView.findViewById(R.id.cbSelected);
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

    public CheckBox getCbSelected() {
        return cbSelected;
    }

    public void setCbSelected(CheckBox cbSelected) {
        this.cbSelected = cbSelected;
    }
}
