package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NEWROW.row.R;

public class DependentHolder extends RecyclerView.ViewHolder {

   public TextView txt_name,txt_designation,tvDescr,tv_mobile;
   public ImageView imageView1;
   public LinearLayout ll_item,ll_mobile;
    public DependentHolder(View itemView) {
        super(itemView);
        txt_name=(TextView)itemView.findViewById(R.id.txt_Name);
        txt_designation=(TextView)itemView.findViewById(R.id.txt_designation);
        tvDescr=(TextView)itemView.findViewById(R.id.tvDescr);
        tv_mobile=(TextView)itemView.findViewById(R.id.tv_mobile);
        imageView1=(ImageView)itemView.findViewById(R.id.imageView1);
        ll_item=(LinearLayout)itemView.findViewById(R.id.ll_item);
        ll_mobile=(LinearLayout)itemView.findViewById(R.id.ll_mobile);
    }
}
