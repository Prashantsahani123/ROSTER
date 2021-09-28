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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NEWROW.row.Data.SubGroupMemberData;
import com.NEWROW.row.Profile;
import com.NEWROW.row.R;

import java.util.ArrayList;


/**
 * Created by USER on 01-02-2016.
 */
public class MemberSelectionAdapter extends ArrayAdapter<SubGroupMemberData> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<SubGroupMemberData> directoryDatas = new ArrayList<SubGroupMemberData>();
    String flag ; // 0-Directroy 1-globalsearch ----- To hide the contents

    public MemberSelectionAdapter(Context mContext, int layoutResourceId, ArrayList<SubGroupMemberData> directoryDatas, String inputflag) {
        super(mContext, layoutResourceId, directoryDatas);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.directoryDatas = directoryDatas;
        this.flag=inputflag;
    }

    public void setGridData(ArrayList<SubGroupMemberData> directoryDatas) {
        this.directoryDatas = directoryDatas;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;

        if (row == null) {

            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            //holder.imageView = (ImageView) row.findViewById(R.id.imageView1);
            holder.name = (TextView)row.findViewById(R.id.tv_name);
            holder.mobile = (TextView)row.findViewById(R.id.tv_mobile);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final SubGroupMemberData item = directoryDatas.get(position);

        holder.name.setText(item.getMemberName());
        holder.mobile.setText(item.getMembermobile());
        if(flag.equals("1")) {
            holder.grp_name.setVisibility(View.VISIBLE);
        }

        if(flag.equals("0")) {
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, Profile.class);
                    i.putExtra("memberprofileid", item.getProfileID());

                    i.putExtra("nameLabel","Name");
                    i.putExtra("numberLabel","Mobile Number");
                    i.putExtra("memberName",item.getMemberName());
                    i.putExtra("memberMobile",item.getMembermobile());

                    ((Activity) mContext).startActivityForResult(i, 1);
                }
            });
        }

        final CheckBox cbBuy = (CheckBox) row.findViewById(R.id.cbBox);
        cbBuy.setOnCheckedChangeListener(myCheckChangList);
        cbBuy.setTag(position);
        cbBuy.setChecked(item.box);


        LinearLayout name = (LinearLayout)row.findViewById(R.id.linear_name);

        name.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View v) {

                if(cbBuy.isChecked()==true)
                {
                    cbBuy.setChecked(false);
                }
                else
                    cbBuy.setChecked(true);

            }

        });
        // For offline search
        /*if ( item.isVisible() ) {
            row.setVisibility(View.VISIBLE);
        } else {
            row.setVisibility(View.GONE);
        }*/

        return row;
    }

    static class ViewHolder {
        TextView name,mobile,grp_name;
        ImageView imageView;
    }

    public ArrayList<SubGroupMemberData> getBox() {
        ArrayList<SubGroupMemberData> box = new ArrayList<SubGroupMemberData>();
        for (SubGroupMemberData p : directoryDatas)
        {
            if (p.box)
                box.add(p);

        }
        return box;
    }

    CompoundButton.OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            getMember((Integer) buttonView.getTag()).box = isChecked;

        }
    };

    SubGroupMemberData getMember(int position) {
        return getItem(position);
    }
}
