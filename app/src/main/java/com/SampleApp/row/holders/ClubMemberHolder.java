package com.SampleApp.row.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.SampleApp.row.R;
import com.SampleApp.row.Utils.Utils;

/**
 * Created by admin on 31-07-2017.
 */

public class ClubMemberHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView tvMemberName, tvMobileNo;

    public ClubMemberHolder(View itemView) {
        super(itemView);
        try {
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            tvMemberName = (TextView) itemView.findViewById(R.id.tvMemberName);
            tvMobileNo = (TextView) itemView.findViewById(R.id.tvMobileNo);
        } catch (Exception e) {
            Utils.log("Error is : " +e );
            e.printStackTrace();
        }
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getTvMemberName() {
        return tvMemberName;
    }

    public void setTvMemberName(TextView tvMemberName) {
        this.tvMemberName = tvMemberName;
    }

    public TextView getTvMobileNo() {
        return tvMobileNo;
    }

    public void setTvMobileNo(TextView tvMobileNo) {
        this.tvMobileNo = tvMobileNo;
    }
}
