package com.SampleApp.row.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.SampleApp.row.R;
import com.SampleApp.row.Utils.Utils;

/**
 * Created by USER1 on 25-03-2017.
 */
public class ProfileMasterHolder extends RecyclerView.ViewHolder {
    ImageView ivProfilePic;
    TextView tvMemberName, tvMobileNo;

    public ProfileMasterHolder(View itemView) {
        super(itemView);
        try {
            ivProfilePic = (ImageView) itemView.findViewById(R.id.ivProfilePic);
            tvMemberName = (TextView) itemView.findViewById(R.id.tvMemberName);
            tvMobileNo = (TextView) itemView.findViewById(R.id.tvMobileNo);
        } catch(Exception e){
            Utils.log("Error : "+e);
            e.printStackTrace();
        }
    }

    public ImageView getIvProfilePic() {
        return ivProfilePic;
    }

    public TextView getTvMemberName() {
        return tvMemberName;
    }

    public TextView getTvMobileNo() {
        return tvMobileNo;
    }
}
