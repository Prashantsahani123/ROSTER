package com.SampleApp.row.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.SampleApp.row.Data.ProfileData;
import com.SampleApp.row.R;

import java.util.ArrayList;


/**
 * Created by user on 02-03-2016.
 */
public class ProfileAdapter extends ArrayAdapter<ProfileData> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<ProfileData> profileListDatas = new ArrayList<ProfileData>();

    public ProfileAdapter(Context mContext, int layoutResourceId, ArrayList<ProfileData> profileListDatas) {
        super(mContext, layoutResourceId, profileListDatas);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.profileListDatas = profileListDatas;
    }

    public void setGridData(ArrayList<ProfileData> profileListDatas) {
        this.profileListDatas = profileListDatas;
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


            holder.profileTitle = (TextView)row.findViewById(R.id.tv_name);
            holder.profileDesc = (TextView)row.findViewById(R.id.tv_mobile);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ProfileData item = profileListDatas.get(position);

        holder.profileTitle.setText(item.getKey());
        holder.profileDesc.setText(item.getValue());

        return row;

    }

    static class ViewHolder {
        TextView profileTitle,profileDesc;

    }
}
