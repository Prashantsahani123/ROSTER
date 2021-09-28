package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.NEWROW.row.R;
import com.NEWROW.row.Utils.Utils;

/**
 * Created by USER1 on 25-03-2017.
 */
public class ProfileMasterHolder extends RecyclerView.ViewHolder {
    ImageView ivProfilePic;
    TextView tvMemberName, tvMobileNo,tvGroupName;
    public ProgressBar progressBar;

    public ProfileMasterHolder(View itemView) {
        super(itemView);

        try {

            ivProfilePic = (ImageView) itemView.findViewById(R.id.ivProfilePic);
            tvMemberName = (TextView) itemView.findViewById(R.id.tvMemberName);
            tvMobileNo = (TextView) itemView.findViewById(R.id.tvMobileNo);
            tvGroupName = (TextView) itemView.findViewById(R.id.tv_group_name);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);

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

    public TextView getGroupName() {
        return tvGroupName;
    }

}
