package com.SampleApp.row.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.SampleApp.row.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Created by user on 11-08-2016.
 */
public class YearAdapter extends BaseAdapter {

    private Context mcontext;
    ArrayList<String>list;
    TextView textView ;

    public YearAdapter(Context c, ArrayList<String>monthList){
     this.mcontext = c;
        this.list = monthList;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
       View v = convertView;

        if(convertView == null){
            LayoutInflater infaltor = (LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = infaltor.inflate(R.layout.year_adapter,null);
        }else{
            v = (View)convertView;
        }

        textView = (TextView)v.findViewById(R.id.tv_monthName);
        // Code to get Current Month from calendar
        Calendar cal = GregorianCalendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        if(position==month){
            textView.setTextColor((Color.parseColor("#1875D1")));
        }
        textView.setText(list.get(position));

        return v;
    }
}
