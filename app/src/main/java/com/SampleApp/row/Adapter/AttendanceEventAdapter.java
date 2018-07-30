package com.SampleApp.row.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.SampleApp.row.Data.EventListData;
import com.SampleApp.row.EditAttendence;
import com.SampleApp.row.R;

import java.util.ArrayList;

public class AttendanceEventAdapter extends ArrayAdapter<EventListData> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<EventListData> eventListDatas = new ArrayList<EventListData>();

    public AttendanceEventAdapter(Context mContext, int layoutResourceId, ArrayList<EventListData> eventListDatas) {
        super(mContext, layoutResourceId, eventListDatas);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.eventListDatas = eventListDatas;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
       ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();



            // holder.imageView = (ImageView) row.findViewById(R.id.imageView1);

            holder.eventTitle = (TextView) row.findViewById(R.id.tvEventName);

            holder.tv_date = (TextView) row.findViewById(R.id.attendenceDate);

            holder.tv_time = (TextView) row.findViewById(R.id.attendenceTime);

            row.setTag(holder);


        } else {
            holder = (ViewHolder) row.getTag();
        }

        final EventListData item = eventListDatas.get(position);

        holder.eventTitle.setText(item.getEventTitle());
        String[] separated = item.getEventDateTime().split(" ");
        String time=separated[1];
        holder.tv_date.setText("" + separated[0]);

        holder.tv_time.setText(time.substring(0,time.length()-3));

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, EditAttendence.class);
                intent.putExtra("eventId",item.getEventID());
                mContext.startActivity(intent);
            }
        });


        return row;
        // return super.getView(position, convertView, parent);
    }

    static class ViewHolder {
        TextView eventTitle, tv_date,tv_time;
    }
}
