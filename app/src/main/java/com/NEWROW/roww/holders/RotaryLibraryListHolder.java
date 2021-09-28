package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NEWROW.row.R;

/**
 * Created by admin on 22-05-2017.
 */

public class RotaryLibraryListHolder extends RecyclerView.ViewHolder {
    public TextView tv_title;
    public LinearLayout linear;

    public RotaryLibraryListHolder(View v) {
        super(v);
        tv_title = (TextView)v.findViewById(R.id.tv_title);
        linear = (LinearLayout)v.findViewById(R.id.linear);
    }
}
