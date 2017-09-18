package com.SampleApp.row.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.SampleApp.row.Data.SubGoupData;
import com.SampleApp.row.R;
import com.SampleApp.row.SubGroupSelectionList;

/**
 * Created by USER on 10-02-2016.
 */
public class SubGroupSelectionAdapter extends ArrayAdapter<SubGoupData> {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<SubGoupData> list_subGroup = new ArrayList<SubGoupData>();
    String flag_addsub = "0";

    public SubGroupSelectionAdapter(Context mContext, int layoutResourceId, ArrayList<SubGoupData> list_subGroup, String flag_addsub) {
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

            holder.cbBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    getsubgrps((Integer) buttonView.getTag()).box = isChecked;
                    Log.e("Before*******  ", SubGroupSelectionList.selected.toString());

                    if ( isChecked ){
                        SubGroupSelectionList.selected.add(item.getSubgrpId());
                        boolean allSelected = true;
                        int n = list_subGroup.size();

                        for(int i=0;i<n;i++) {
                            if ( ! list_subGroup.get(i).isBox()) {
                                allSelected = false;
                                break;
                            }
                        }

                        if ( allSelected ) {
                            String parentId = ((SubGroupSelectionList) mContext).getParentId();
                            SubGroupSelectionList.selected.add(parentId);
                        }
                    } else {
                        SubGroupSelectionList.selected.remove(item.getSubgrpId());
                        try {
                            String parentId = ((SubGroupSelectionList) mContext).getParentId();
                            SubGroupSelectionList.selected.remove(parentId);
                        } catch(ClassCastException cce) {
                            cce.printStackTrace();
                        }
                    }
                    Log.e("After*******  ", SubGroupSelectionList.selected.toString());

                }
            });

            holder.iv_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(mContext, "Here we come", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, SubGroupSelectionList.class);
                    intent.putExtra("parentId", item.getSubgrpId());
                    intent.putExtra("subgroupname", item.getSubgroup_name());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    try {
                        ((Activity) mContext).overridePendingTransition(0, 0);
                        ((Activity) mContext).startActivityForResult(intent, 1);
                        ((Activity) mContext).overridePendingTransition(0, 0);
                    } catch (ClassCastException cc) {
                        cc.printStackTrace();
                    }
                }
            });
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.cbBox.toggle();
                }
            });

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        holder.cbBox.setTag(position);
        holder.cbBox.setChecked(item.box);

        if (flag_addsub.equals("1")) {
            //holder.cbBox.setVisibility(View.VISIBLE);
           // holder.iv_arrow.setVisibility(View.GONE);
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

        if (item.getHasSubgroups().equals("1")) {
            //holder.cbBox.setVisibility(View.VISIBLE);
            holder.iv_arrow.setVisibility(View.VISIBLE);
        } else {
            holder.iv_arrow.setVisibility(View.GONE);
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
    OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


        }
    };

    static class ViewHolder {
        TextView subgroupName, member_number;
        ImageView iv_arrow;
        CheckBox cbBox;

    }
}
