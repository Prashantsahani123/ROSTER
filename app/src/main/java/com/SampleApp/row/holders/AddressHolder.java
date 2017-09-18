package com.SampleApp.row.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.SampleApp.row.R;
import com.SampleApp.row.Utils.Utils;

/**
 * Created by USER1 on 24-03-2017.
 */
public class AddressHolder extends RecyclerView.ViewHolder {
    TextView tvAddressTitle, tvAddressValue;
    public AddressHolder(View itemView) {
        super(itemView);
        try {
            tvAddressTitle = (TextView) itemView.findViewById(R.id.tvProfileAddressTitle);
            tvAddressValue = (TextView) itemView.findViewById(R.id.tvProfileAddressValue);
        } catch(Exception e) {
            Utils.log("Erorr is : "+e);
            e.printStackTrace();
        }
    }

    public TextView getTvAddressTitle() {
        return tvAddressTitle;
    }

    public TextView getTvAddressValue() {
        return tvAddressValue;
    }
}
