package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.NEWROW.row.R;
import com.NEWROW.row.Utils.Utils;

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
