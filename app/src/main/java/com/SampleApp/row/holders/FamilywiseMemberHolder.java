package com.SampleApp.row.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.SampleApp.row.R;
import com.SampleApp.row.Utils.Utils;

/**
 * Created by USER1 on 25-03-2017.
 */
public class FamilywiseMemberHolder extends RecyclerView.ViewHolder {
    TextView tvMemberName, tvNameRelation;

    public FamilywiseMemberHolder(View itemView) {
        super(itemView);
        try {
            tvMemberName = (TextView) itemView.findViewById(R.id.tvMemberName);
            tvNameRelation = (TextView) itemView.findViewById(R.id.tvNameRelation);
        } catch(Exception e){
            Utils.log("Error : "+e);
            e.printStackTrace();
        }
    }


    public TextView getTvMemberName() {
        return tvMemberName;
    }

    public TextView getTvNameRelation() {
        return tvNameRelation;
    }
}
