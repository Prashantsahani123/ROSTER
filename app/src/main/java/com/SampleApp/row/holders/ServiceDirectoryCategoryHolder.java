package com.SampleApp.row.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.SampleApp.row.R;


/**
 * Created by user on 21-03-2017.
 */
public class ServiceDirectoryCategoryHolder extends RecyclerView.ViewHolder {
   public TextView tv_categoryName;
    public LinearLayout linearLayout;

    public ServiceDirectoryCategoryHolder(View view){
        super(view);
        tv_categoryName = (TextView)view.findViewById(R.id.tv_categoryname);
        linearLayout = (LinearLayout)view.findViewById(R.id.linear);

    }


    public TextView getTv_categoryName() {
        return tv_categoryName;
    }

    public void setTv_categoryName(TextView tv_categoryName) {
        this.tv_categoryName = tv_categoryName;
    }


}
