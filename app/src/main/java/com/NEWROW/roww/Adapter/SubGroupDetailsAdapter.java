package com.NEWROW.row.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.NEWROW.row.Data.SubGroupDetailsData;
import com.NEWROW.row.R;

import java.util.ArrayList;


/**
 * Created by USER on 16-02-2016.
 */
public class SubGroupDetailsAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<SubGroupDetailsData> objects;

    public SubGroupDetailsAdapter(Context context, ArrayList<SubGroupDetailsData> memberDatas) {
        ctx = context;
        objects = memberDatas;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.subgroup_details_listitem, parent, false);
        }




        ((TextView) view.findViewById(R.id.tv_name)).setText(objects.get(position).getMemname());
        ((TextView) view.findViewById(R.id.tv_mobile)).setText(objects.get(position).getMobile());




        return view;
    }



}