package com.SampleApp.row.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.SampleApp.row.R;

/**
 * Created by admin on 27-07-2017.
 */

public class DistrictCommitteeHolder extends RecyclerView.ViewHolder {
    ImageView ivPic;
    TextView tvMemberName, tvDesignation;

    public DistrictCommitteeHolder(View itemView) {
        super(itemView);
        try {
            ivPic = (ImageView) itemView.findViewById(R.id.imageView);
            tvDesignation = (TextView) itemView.findViewById(R.id.tv_designation);
            tvMemberName = (TextView) itemView.findViewById(R.id.tv_name);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public ImageView getIvPic() {
        return ivPic;
    }

    public void setIvPic(ImageView ivPic) {
        this.ivPic = ivPic;
    }

    public TextView getTvMemberName() {
        return tvMemberName;
    }

    public void setTvMemberName(TextView tvMemberName) {
        this.tvMemberName = tvMemberName;
    }

    public TextView getTvDesignation() {
        return tvDesignation;
    }

    public void setTvDesignation(TextView tvDesignation) {
        this.tvDesignation = tvDesignation;
    }
}
