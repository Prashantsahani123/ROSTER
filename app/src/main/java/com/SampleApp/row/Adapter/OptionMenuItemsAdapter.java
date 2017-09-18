package com.SampleApp.row.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import com.SampleApp.row.Data.CalendarViewOption;
import com.SampleApp.row.R;
import com.SampleApp.row.calendar.DayActivity;
import com.SampleApp.row.calendar.MonthActivity;
import com.SampleApp.row.calendar.ScheduleView;
import com.SampleApp.row.calendar.YearActivity;

/**
 * Created by user on 24-01-2017.
 */
public class OptionMenuItemsAdapter extends BaseAdapter {
    Context context;
    ArrayList<CalendarViewOption> list = new ArrayList<>();
    LayoutInflater inflater = null;



    public OptionMenuItemsAdapter(Context con, ArrayList<CalendarViewOption> list){
        context = con;
        this.list = list;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView = convertView;

        if(convertView == null){
            rowView = inflater.inflate(R.layout.layout_popup_menu,null);
            holder.image_close = (ImageView) rowView.findViewById(R.id.close);
            holder.image = (ImageView)rowView.findViewById(R.id.image);
            holder.text = (TextView) rowView.findViewById(R.id.text);
            rowView.setTag(holder);
        }else{
            holder = (Holder)rowView.getTag();
        }

        holder.text.setText(list.get(position).getText());
        holder.image.setImageResource(list.get(position).getImage());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(list.get(position).getText().equalsIgnoreCase("Days")){

                Intent i = new Intent(context, ScheduleView.class);
                    context.startActivity(i);
                }
                else if(list.get(position).getText().equalsIgnoreCase("Today")){
                    Intent i = new Intent(context, DayActivity.class);
                    context.startActivity(i);
                }
                else if(list.get(position).getText().equalsIgnoreCase("Year")){
                    Intent i = new Intent(context, YearActivity.class);
                    context.startActivity(i);
                }

                else if(list.get(position).getText().equalsIgnoreCase("Month")){
                    Intent i = new Intent(context, MonthActivity.class);
                    context.startActivity(i);
                }
            }
        });


        return rowView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class Holder{
        ImageView image,image_close;
        TextView text;

    }
}
