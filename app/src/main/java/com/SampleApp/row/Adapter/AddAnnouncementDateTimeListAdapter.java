package com.SampleApp.row.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.SampleApp.row.Data.AddAnnouncementDateTimeData;
import com.SampleApp.row.R;


/**
 * Created by user on 01-02-2017.
 */
public class AddAnnouncementDateTimeListAdapter extends ArrayAdapter<AddAnnouncementDateTimeData> {

private Context mContext;
private int layoutResourceId;
private ArrayList<AddAnnouncementDateTimeData> list_datetime = new ArrayList<AddAnnouncementDateTimeData>();

public AddAnnouncementDateTimeListAdapter(Context mContext, int layoutResourceId, ArrayList<AddAnnouncementDateTimeData> list_datetime) {
        super(mContext, layoutResourceId, list_datetime);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.list_datetime = list_datetime;
        }

public void setGridData(ArrayList<AddAnnouncementDateTimeData> list_datetime) {
        this.list_datetime = list_datetime;
        notifyDataSetChanged();
        }

@Override
public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;

        if (row == null) {
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);
        holder = new ViewHolder();


        holder.date = (TextView)row.findViewById(R.id.tv_date);
        holder.time = (TextView)row.findViewById(R.id.tv_time);
        holder.tv_remove = (TextView)row.findViewById(R.id.tv_remove);

        row.setTag(holder);
        } else {
        holder = (ViewHolder) row.getTag();
        }

final AddAnnouncementDateTimeData
        item = list_datetime.get(position);
        holder.date.setText(item.getDate());
        holder.time.setText(item.getTime());

        holder.tv_remove.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        list_datetime.remove(position   );
        notifyDataSetChanged();
        }
        });

        return row;

        }

static class ViewHolder {
    TextView date,time,tv_remove;

}

}
