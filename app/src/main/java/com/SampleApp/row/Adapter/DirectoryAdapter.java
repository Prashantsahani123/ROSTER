package com.SampleApp.row.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.SampleApp.row.Data.DirectoryData;
import com.SampleApp.row.ProfileActivityV4;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by USER on 01-02-2016.
 */
public class DirectoryAdapter extends ArrayAdapter<DirectoryData> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<DirectoryData> directoryDatas = new ArrayList<DirectoryData>();
    String flag ; // 0-Directroy 1-globalsearch ----- To hide the contents

    public DirectoryAdapter(Context mContext, int layoutResourceId, ArrayList<DirectoryData> directoryDatas,String inputflag) {
        super(mContext, layoutResourceId, directoryDatas);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.directoryDatas = directoryDatas;
        this.flag=inputflag;
    }

    public void setGridData(ArrayList<DirectoryData> directoryDatas) {
        this.directoryDatas = directoryDatas;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;

        if (row == null) {

            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            holder.imageView = (ImageView) row.findViewById(R.id.imageView1);
            holder.name = (TextView)row.findViewById(R.id.tv_name);
            holder.mobile = (TextView)row.findViewById(R.id.tv_mobile);
            holder.grp_name = (TextView)row.findViewById(R.id.tv_group_name);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final DirectoryData item = directoryDatas.get(position);

        holder.name.setText(item.getMemberName());
        holder.mobile.setText(item.getMembermobile());
        if(flag.equals("1")) {
            holder.grp_name.setVisibility(View.VISIBLE);
            holder.grp_name.setText("In no of entities:-"+item.getGrpCount());
        }

        if (item.getPic().trim().length() == 0 || item.getPic().equals("") || item.getPic() == null || item.getPic().isEmpty()) {
            holder.imageView.setImageResource(R.drawable.profile_pic);
        } else {
            Picasso.with(mContext).load(item.getPic()).transform(new CircleTransform())
                    .placeholder(R.drawable.profile_pic)
                    .into(holder.imageView);
        }

        if(flag.equals("0")) {
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, ProfileActivityV4.class);
                    i.putExtra("memberprofileid", item.getProfileID());
                    i.putExtra("groupId", item.getGrpID());

                    i.putExtra("nameLabel","Name");
                    i.putExtra("numberLabel","Mobile Number");
                    i.putExtra("memberName",item.getMemberName());
                    i.putExtra("memberMobile",item.getMembermobile());

                    ((Activity) mContext).startActivityForResult(i, 1);
                }
            });
        }

        // For offline search
        /*if ( item.isVisible() ) {
            row.setVisibility(View.VISIBLE);
        } else {
            row.setVisibility(View.GONE);
        }*/

        return row;
    }

    static class ViewHolder {
        TextView name,mobile,grp_name;
        ImageView imageView;
    }
}
