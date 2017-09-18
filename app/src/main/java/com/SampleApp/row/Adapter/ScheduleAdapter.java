package com.SampleApp.row.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.SampleApp.row.R;

import java.util.ArrayList;


/**
 * Created by user on 22-08-2016.
 */
public class ScheduleAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<String>list;
    TextView tv_date,tv_day,tv_event ;
    LinearLayout ll_event;

    public ScheduleAdapter(Context c, ArrayList<String> lst){
        this.mContext = c;
        this.list = lst;

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(convertView == null){
            LayoutInflater inflator = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflator.inflate(R.layout.schedule_adapter,null);
        }else{
            v = (View)convertView;
        }

        tv_date = (TextView)v.findViewById(R.id.tv_date);
        tv_day = (TextView)v.findViewById(R.id.tv_day);
       // tv_event = (TextView)v.findViewById(R.id.tv_event);
        ll_event = (LinearLayout)v.findViewById(R.id.ll_event);

        tv_date.setText(list.get(position).toString());
        tv_day.setText("Mon");
        String str[] = new String[]{"Prasad Payment","Lekha birthday Party"};

                for(int i =0;i<str.length;i++) {

                      tv_event = new TextView(mContext);
                        tv_event.setText(str[i]);
                    ll_event.addView(tv_event);
                    if(str[i].equalsIgnoreCase("Prasad Payment")){
                        tv_event.setBackgroundColor(Color.parseColor("#008000"));
                    }else{
                        tv_event.setBackgroundColor(Color.parseColor("#990033"));
                    }
                    tv_event.setPadding(0,15,0,0);

                }


        return v;
    }
}
