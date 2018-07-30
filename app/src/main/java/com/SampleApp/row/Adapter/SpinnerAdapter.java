package com.SampleApp.row.Adapter;

/**
 * Created by Admin on 22-03-2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.SampleApp.row.Data.AlbumData;
import com.SampleApp.row.R;

import java.util.ArrayList;


/**
 * Created by Admin on 25-01-2018.
 */

public class SpinnerAdapter extends BaseAdapter implements Filterable{

    LayoutInflater inflator;
    ArrayList<AlbumData> list;

    public SpinnerAdapter(Context context, ArrayList<AlbumData> list) {
        inflator = LayoutInflater.from(context);
        this.list = list;
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
        convertView = inflator.inflate(R.layout.spinner_item, null);
        TextView tv = (TextView) convertView.findViewById(R.id.sp_textView);
        //tv.setMovementMethod(new ScrollingMovementMethod());
        AlbumData data=list.get(position);
        tv.setText(data.getDistrict_Name());
        tv.setTag(data);
        return convertView;
    }

//    @Override
//    public boolean isEnabled(int position) {
//        if(position==0){
//            //disable first item
//            return false;
//        }
//        else {
//            return true;
//        }
//    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//        View view = super.getDropDownView(position, convertView, parent);
//        View v = ((LinearLayout)view).getChildAt(0);
//        TextView tv=(TextView)v;
//        tv.setTextSize(14);
//        if(position == 0){
//            // Set the hint text color gray
//
//            tv.setTextColor(Color.GRAY);
//        }
//        else {
//            tv.setTextColor(Color.BLACK);
//        }

        convertView = inflator.inflate(R.layout.spinner_item, null);
        TextView tv = (TextView) convertView.findViewById(R.id.sp_textView);
        tv.setTextSize(14);
//        if(position == 0){
//            // Set the hint text color gray
//
//            tv.setTextColor(Color.GRAY);
//        }
//        else {
//            tv.setTextColor(Color.BLACK);
//        }


        AlbumData data=list.get(position);
        tv.setText(data.getDistrict_Name());
        tv.setTag(data);
        return convertView;

    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
