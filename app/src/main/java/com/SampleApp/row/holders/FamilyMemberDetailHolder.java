package com.SampleApp.row.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.SampleApp.row.R;
import com.SampleApp.row.Utils.Utils;

/**
 * Created by USER1 on 24-03-2017.
 */
public class FamilyMemberDetailHolder extends RecyclerView.ViewHolder{
    TextView tvFamilyMemberRelation, tvFamilyMemberName,tvFamilyMemberEmail,tvFamilyMemberDob, tvFamilyMemberMobile,tvFamilyMemberBloodGroup, tvFamilyMemberAnniversary ;
    public FamilyMemberDetailHolder(View itemView) {
        super(itemView);
        try {
            tvFamilyMemberRelation = (TextView) itemView.findViewById(R.id.tvFamilyMemberRelation);
            tvFamilyMemberName = (TextView) itemView.findViewById(R.id.tvFamilyMemberName);
            tvFamilyMemberEmail = (TextView) itemView.findViewById(R.id.tvFamilyMemberEmail);
            tvFamilyMemberMobile = (TextView) itemView.findViewById(R.id.tvFamilyMemberMobile);
            tvFamilyMemberDob = (TextView) itemView.findViewById(R.id.tvFamilyMemberDob);
            tvFamilyMemberBloodGroup = (TextView) itemView.findViewById(R.id.tvFamilyMemberBloodGroup);
            tvFamilyMemberAnniversary = (TextView) itemView.findViewById(R.id.tvFamilyMemberAnniversary);

        } catch(Exception e) {
            Utils.log("Error is : "+e);
            e.printStackTrace();
        }
    }

    public TextView getTvFamilyMemberRelation() {
        return tvFamilyMemberRelation;
    }

    public TextView getTvFamilyMemberName() {
        return tvFamilyMemberName;
    }

    public TextView getTvFamilyMemberEmail() {
        return tvFamilyMemberEmail;
    }

    public TextView getTvFamilyMemberDob() {
        return tvFamilyMemberDob;
    }

    public TextView getTvFamilyMemberMobile() {
        return tvFamilyMemberMobile;
    }

    public TextView getTvFamilyMemberBloodGroup() {
        return tvFamilyMemberBloodGroup;
    }

    public TextView getTvFamilyMemberAnniversary() {
        return tvFamilyMemberAnniversary;
    }
}
