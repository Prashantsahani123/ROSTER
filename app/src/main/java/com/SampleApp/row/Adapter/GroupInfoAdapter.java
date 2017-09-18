package com.SampleApp.row.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.SampleApp.row.Data.GroupInfoData;
import com.SampleApp.row.GroupInfo_desc;
import com.SampleApp.row.R;


/**
 * Created by USER1 on 07-06-2016.
 */
public class GroupInfoAdapter extends ArrayAdapter<GroupInfoData> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<GroupInfoData> list_groupInfoData = new ArrayList<GroupInfoData>();


    public GroupInfoAdapter(Context mContext, int layoutResourceId, ArrayList<GroupInfoData> list_groupInfoData) {
        super(mContext, layoutResourceId, list_groupInfoData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.list_groupInfoData = list_groupInfoData;
    }

    public void setGridData(ArrayList<GroupInfoData> list_groupInfoData) {
        this.list_groupInfoData = list_groupInfoData;
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

            // holder.imageView = (ImageView) row.findViewById(R.id.imageView1);
            holder.rowTextView = (TextView) row.findViewById(R.id.rowTextView);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final GroupInfoData item = list_groupInfoData.get(position);
        holder.rowTextView.setText(item.getTitle());


        Log.d("==============","@@@@@@@@@@@@@@@" + item.getDescription());

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, GroupInfo_desc.class);


                i.putExtra("title",item.getTitle());
                i.putExtra("desc",item.getDescription());
                mContext.startActivity(i);

            }
        });


        return row;

    }

    static class ViewHolder {
        TextView rowTextView;
        //ImageView imageView;
    }
}
