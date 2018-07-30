package com.SampleApp.row.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.SampleApp.row.Data.CountryData;
import com.SampleApp.row.R;

import java.util.ArrayList;


/**
 * Created by user on 22-02-2016.
 */
public class CountrySpAdapter extends BaseAdapter
{
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<CountryData> list_countryData = new ArrayList<CountryData>();
    private boolean showCountryCodes = false;

    public CountrySpAdapter(Context mContext, int layoutResourceId, ArrayList<CountryData> list_countryData) {
        //super(mContext, layoutResourceId, list_countryData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.list_countryData = list_countryData;
    }

    public CountrySpAdapter(Context mContext, int layoutResourceId, ArrayList<CountryData> list_countryData, boolean showCountryCodes) {
        //super(mContext, layoutResourceId, list_countryData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.list_countryData = list_countryData;
        this.showCountryCodes = showCountryCodes;
    }
    public void setGridData(ArrayList<CountryData> list_countryData) {
        this.list_countryData = list_countryData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        try {

            if (row == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
            }
            CountryData item = list_countryData.get(position);
            if ( showCountryCodes ) {
                if ( item.getCountryCode().trim().equals("")) {
                    ((TextView) row.findViewById(R.id.tv_country_name)).setText(item.getCountryName());
                } else {
                    ((TextView) row.findViewById(R.id.tv_country_name)).setText(item.getCountryName()+" ("+item.getCountryCode()+")");
                }

            } else {
                ((TextView) row.findViewById(R.id.tv_country_name)).setText(item.getCountryName());
            }
        } catch(ArrayIndexOutOfBoundsException ie) {
            ie.printStackTrace();
        }
        return row;
    }


    @Override
    public int getCount() {
        return list_countryData.size();
    }

    @Override
    public CountryData getItem(int position) {
        return list_countryData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(list_countryData.get(position).getCountryId());
    }

    public ArrayList<CountryData> getList(){
        return list_countryData;
    }
}