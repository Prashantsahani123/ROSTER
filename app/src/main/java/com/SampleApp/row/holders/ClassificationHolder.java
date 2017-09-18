package com.SampleApp.row.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.SampleApp.row.R;

/**
 * Created by Prasad on 03-06-2017.
 */

public class ClassificationHolder extends RecyclerView.ViewHolder{
    TextView tvClassificationName;
    public ClassificationHolder(View itemView) {
        super(itemView);
        tvClassificationName = (TextView) itemView.findViewById(R.id.tvClassificationTitle);
    }

    public TextView getTvClassificationName() {
        return tvClassificationName;
    }

    public void setTvClassificationName(TextView tvClassificationName) {
        this.tvClassificationName = tvClassificationName;
    }
}
