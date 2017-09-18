package com.SampleApp.row.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.SampleApp.row.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.SampleApp.row.Data.GroupProfileResultData;


/**
 * Created by user on 17-02-2016.
 */
/*
public class GroupProfileResultAdapter extends BaseAdapter {
    String [] result;
    Context context;
    int [] imageId;
    private static LayoutInflater inflater=null;
    public GroupProfileResultAdapter(GroupProfileResult mainActivity, String[] prgmNameList) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=mainActivity;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.group_profile_result_listitem, null);
        holder.tv=(TextView) rowView.findViewById(R.id.tv_group_name);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + result[position], Toast.LENGTH_LONG).show();
            }
        });

        holder.tv.setText(result[position]);
        return rowView;
    }

}*/

public class GroupProfileResultAdapter extends ArrayAdapter<GroupProfileResultData> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<GroupProfileResultData> list_groupprofiledata = new ArrayList<GroupProfileResultData>();

    public GroupProfileResultAdapter(Context mContext, int layoutResourceId, ArrayList<GroupProfileResultData> list_groupprofiledata) {
        super(mContext, layoutResourceId, list_groupprofiledata);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.list_groupprofiledata = list_groupprofiledata;
    }

    public void setGridData(ArrayList<GroupProfileResultData> list_groupprofiledata) {
        this.list_groupprofiledata = list_groupprofiledata;
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            //  holder.imageView = (ImageView) row.findViewById(R.id.imageView1);
            holder.group_name = (TextView) row.findViewById(R.id.tv_group_name);
            holder.linear_mainlayout = (LinearLayout) row.findViewById(R.id.linear_mainlayout);
            holder.iv_grouppic = (ImageView) row.findViewById(R.id.iv_grouppic);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        GroupProfileResultData item = list_groupprofiledata.get(position);

        if (item.getGrpImg() == "" || item.getGrpImg() == null || item.getGrpImg().isEmpty()) {

        } else {
            Picasso.with(mContext).load(item.getGrpImg())
                    .placeholder(R.drawable.imageplaceholder)
                    .into(holder.iv_grouppic);
        }


        holder.group_name.setText(item.getGrpName());

        if (item.getIsMygrp().equals("Y")) {
            holder.linear_mainlayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        } else {
            holder.linear_mainlayout.setBackgroundColor(mContext.getResources().getColor(R.color.grey_light_shade));
        }
        // Picasso.with(mContext).load(item.getImage()).into(holder.imageView);
        return row;


        // return super.getView(position, convertView, parent);
    }

    static class ViewHolder {
        TextView group_name;
        LinearLayout linear_mainlayout;
        ImageView iv_grouppic;

    }
}