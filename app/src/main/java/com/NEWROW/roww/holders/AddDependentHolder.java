package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NEWROW.row.R;

/**
 * Created by Prasad on 03-06-2017.
 */

public class AddDependentHolder extends RecyclerView.ViewHolder{
   public TextView tvDependentType,tvDependentNo;

   public LinearLayout ll_removeRecord,ll_visitor_remove,ll_rotarian_removeRecord,ll_delegatesRemove;
   public EditText et_DependentName,et_visitorsName,et_visitorsInvitorsName,et_RotarianID,et_RotarianName,et_clubName,et_delegates_rotarianName,et_districtDesignation;

    public AddDependentHolder(View itemView) {
        super(itemView);
        this.tvDependentType = (TextView)itemView.findViewById(R.id.tvDependentType);
        this.tvDependentNo = (TextView)itemView.findViewById(R.id.tvDependentNo);
        this.et_DependentName =(EditText)itemView.findViewById(R.id.tvDependentName);
        this.ll_removeRecord = (LinearLayout) itemView.findViewById(R.id.ll_removeRecord);
        this.et_visitorsName = (EditText)itemView.findViewById(R.id.et_visitorsName);
        this.et_visitorsInvitorsName = (EditText)itemView.findViewById(R.id.et_visitorsInvitorsName);
        this.ll_visitor_remove=(LinearLayout)itemView.findViewById(R.id.ll_visitor_remove);
        this.et_RotarianID = (EditText)itemView.findViewById(R.id.et_RotarianID);
        this.et_RotarianName = (EditText)itemView.findViewById(R.id.et_RotarianName);
        this.et_clubName = (EditText)itemView.findViewById(R.id.et_clubName);
        this.et_delegates_rotarianName = (EditText)itemView.findViewById(R.id.et_rotarianName);
        this.et_districtDesignation =(EditText)itemView.findViewById(R.id.et_districtDesignation);
        this.ll_rotarian_removeRecord=(LinearLayout)itemView.findViewById(R.id.ll_rotarian_removeRecord);
        this.ll_delegatesRemove=(LinearLayout)itemView.findViewById(R.id.ll_delegatesRemove);

    }

    public EditText getEt_DependentName() {
        return et_DependentName;
    }

    public void setEt_DependentName(EditText et_DependentName) {
        this.et_DependentName = et_DependentName;
    }

    public TextView getTvDependentType() {
        return tvDependentType;
    }

    public void setTvDependentType(TextView tvDependentType) {
        this.tvDependentType = tvDependentType;
    }

    public TextView getTvDependentNo() {
        return tvDependentNo;
    }

    public void setTvDependentNo(TextView tvDependentNo) {
        this.tvDependentNo = tvDependentNo;
    }


    public LinearLayout getLl_removeRecord() {
        return ll_removeRecord;
    }

    public void setLl_removeRecord(LinearLayout ll_removeRecord) {
        this.ll_removeRecord = ll_removeRecord;
    }

    public EditText getEt_visitorsName() {
        return et_visitorsName;
    }

    public void setEt_visitorsName(EditText et_visitorsName) {
        this.et_visitorsName = et_visitorsName;
    }

    public EditText getEt_visitorsInvitorsName() {
        return et_visitorsInvitorsName;
    }

    public void setEt_visitorsInvitorsName(EditText et_visitorsInvitorsName) {
        this.et_visitorsInvitorsName = et_visitorsInvitorsName;
    }

    public EditText getEt_RotarianID() {
        return et_RotarianID;
    }

    public void setEt_RotarianID(EditText et_RotarianID) {
        this.et_RotarianID = et_RotarianID;
    }

    public EditText getEt_RotarianName() {
        return et_RotarianName;
    }

    public void setEt_RotarianName(EditText et_RotarianName) {
        this.et_RotarianName = et_RotarianName;
    }

    public EditText getEt_clubName() {
        return et_clubName;
    }

    public void setEt_clubName(EditText et_clubName) {
        this.et_clubName = et_clubName;
    }

    public EditText getEt_rotarianName() {
        return et_delegates_rotarianName;
    }

    public void setEt_rotarianName(EditText et_rotarianName) {
        this.et_delegates_rotarianName = et_rotarianName;
    }

    public EditText getEt_districtDesignation() {
        return et_districtDesignation;
    }

    public void setEt_districtDesignation(EditText et_districtDesignation) {
        this.et_districtDesignation = et_districtDesignation;
    }
}
