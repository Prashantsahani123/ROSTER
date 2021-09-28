package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NEWROW.row.R;


/**
 * Created by Admin on 10-04-2018.
 */

public class ShowCaseCatHolder extends RecyclerView.ViewHolder {

    public TextView txt_title;
    public LinearLayout ll_catitem;
    public CheckBox cbCat;

    public ShowCaseCatHolder(View itemView) {
        super(itemView);
        txt_title=(TextView)itemView.findViewById(R.id.txt_title);
        ll_catitem=(LinearLayout)itemView.findViewById(R.id.ll_catitem);
        cbCat = (CheckBox)itemView.findViewById(R.id.cb_cat);
    }
}
