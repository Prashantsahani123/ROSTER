package com.NEWROW.row.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.NEWROW.row.Data.SubGoupData;
import com.NEWROW.row.R;
import com.NEWROW.row.SubGroupDetails;

import java.util.ArrayList;

/**
 * Created by USER on 10-02-2016.
 */
public class SubGroupAdapter extends ArrayAdapter<SubGoupData> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<SubGoupData> list_subGroup = new ArrayList<SubGoupData>();
    String flag_addsub = "0";

    public SubGroupAdapter(Context mContext, int layoutResourceId, ArrayList<SubGoupData> list_subGroup, String flag_addsub) {
        super(mContext, layoutResourceId, list_subGroup);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.list_subGroup = list_subGroup;
        this.flag_addsub = flag_addsub;
    }

    public void setGridData(ArrayList<SubGoupData> list_subGroup) {
        this.list_subGroup = list_subGroup;
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        final ViewHolder holder;
        final SubGoupData item = list_subGroup.get(position);
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            // holder.imageView = (ImageView) row.findViewById(R.id.imageView1);
            holder.subgroupName = (TextView) row.findViewById(R.id.tv_subgroupName);
            holder.member_number = (TextView) row.findViewById(R.id.tv_member_number);
            holder.iv_arrow = (ImageView) row.findViewById(R.id.iv_arrow);
            holder.cbBox = (CheckBox) row.findViewById(R.id.cbBox);
            //holder.cbBox.setOnCheckedChangeListener(myCheckChangList);
            holder.iv_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(mContext, "Here we come", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, SubGroupDetails.class);
                    intent.putExtra("subgroupid", item.getSubgrpId());
                    intent.putExtra("subgroupname", item.getSubgroup_name());
                    mContext.startActivity(intent);
                }
            });
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        holder.cbBox.setTag(position);
        holder.cbBox.setChecked(item.box);

        if (flag_addsub.equals("1")) {
            holder.cbBox.setVisibility(View.VISIBLE);
            holder.iv_arrow.setVisibility(View.GONE);
         /*   holder.cbBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( holder.cbBox.isChecked()==true)
                    {
                        holder.cbBox.setChecked(false);
                    }
                    else
                        holder.cbBox.setChecked(true);

                }
            });*/
        }

        holder.subgroupName.setText(item.getSubgroup_name());
        holder.member_number.setText("No of Members :- "+item.getNo_of_members());
        return row;
    }


    public ArrayList<SubGoupData> getBox() {
        ArrayList<SubGoupData> box = new ArrayList<SubGoupData>();
        for (SubGoupData p : list_subGroup)
        {
            if (p.box)
                box.add(p);

        }
        return box;
    }

    SubGoupData getsubgrps(int position) {
        return ((SubGoupData) getItem(position));
    }
    OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            getsubgrps((Integer) buttonView.getTag()).box = isChecked;

        }
    };

    static class ViewHolder {
        TextView subgroupName, member_number;
        ImageView iv_arrow;
        CheckBox cbBox;

    }
}
