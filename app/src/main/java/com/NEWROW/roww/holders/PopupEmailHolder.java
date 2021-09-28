package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.NEWROW.row.R;


/**
 * Created by USER1 on 17-04-2017.
 */
public class PopupEmailHolder extends RecyclerView.ViewHolder {
    TextView tvTitle, tvEmail, tvExtra;
    CheckBox cbSelected;
    public PopupEmailHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        tvExtra = (TextView) itemView.findViewById(R.id.tvExtraInfo);
        tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
        cbSelected = (CheckBox) itemView.findViewById(R.id.cbSelected);

    }

    public CheckBox getCbSelected() {
        return cbSelected;
    }

    public void setCbSelected(CheckBox cbSelected) {
        this.cbSelected = cbSelected;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }

    public TextView getTvEmail() {
        return tvEmail;
    }

    public void setTvEmail(TextView tvEmail) {
        this.tvEmail = tvEmail;
    }

    public TextView getTvExtra() {
        return tvExtra;
    }

    public void setTvExtra(TextView tvExtra) {
        this.tvExtra = tvExtra;
    }
}
