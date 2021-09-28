package com.NEWROW.row.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.NEWROW.row.Data.CountryData;
import com.NEWROW.row.R;

import java.util.ArrayList;


/**
 * Created by user on 22-02-2016.
 */
public class SelectCountryAdapter extends ArrayAdapter<CountryData> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<CountryData> list_countryData = new ArrayList<CountryData>();


    public SelectCountryAdapter(Context mContext, int layoutResourceId, ArrayList<CountryData> list_countryData) {
        super(mContext, layoutResourceId, list_countryData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.list_countryData = list_countryData;
    }

    public void setGridData(ArrayList<CountryData> list_countryData) {
        this.list_countryData = list_countryData;
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
            holder.country_name = (TextView) row.findViewById(R.id.tv_country_name);
            holder.country_name.setText(list_countryData.get(position).getCountryName());
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        CountryData item = list_countryData.get(position);

        holder.country_name.setText(item.getCountryName());



        return row;

    }

    static class ViewHolder {
        TextView country_name;
        //ImageView imageView;
    }
}