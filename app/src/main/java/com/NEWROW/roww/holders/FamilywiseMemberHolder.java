package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.NEWROW.row.R;
import com.NEWROW.row.Utils.Utils;

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
