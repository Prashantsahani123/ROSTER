package com.SampleApp.row.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.SampleApp.row.Data.CategoryData;
import com.SampleApp.row.R;

/**
 * Created by user on 04-02-2016.
 */
public class SpinnerAdapter_categoery extends BaseAdapter {
    Context ctx;
    LayoutInflater inflater;
    ArrayList<CategoryData> list;


    public SpinnerAdapter_categoery(Context context, ArrayList<CategoryData> memberDatas) {
        ctx = context;
        list = memberDatas;

        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
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


    public class Holder {
        // TextView tv;

        TextView tv_name;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.spinner_layout, null);
        // holder.tv=(TextView) rowView.findViewById(R.id.textView1);

        holder.tv_name = (TextView) rowView.findViewById(R.id.tv_name);

        // holder.tv.setText(result[position]);
        //  holder.img.setImageResource(imageId[position]);

        holder.tv_name.setText(list.get(position).getCatName());


     /*   rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked ", Toast.LENGTH_LONG).show();
            }
        });*/

        return rowView;
    }
}
