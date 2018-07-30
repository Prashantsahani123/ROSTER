package com.SampleApp.row.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.SampleApp.row.R;

/**
 * Created by Prasad on 03-06-2017.
 */

public class ClassificationHolder extends RecyclerView.ViewHolder{
    TextView tvClassificationName;
    public ProgressBar progressBar;
    public ClassificationHolder(View itemView) {
        super(itemView);
        tvClassificationName = (TextView) itemView.findViewById(R.id.tvClassificationTitle);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
    }

    public TextView getTvClassificationName() {
        return tvClassificationName;
    }

    public void setTvClassificationName(TextView tvClassificationName) {
        this.tvClassificationName = tvClassificationName;
    }
}
