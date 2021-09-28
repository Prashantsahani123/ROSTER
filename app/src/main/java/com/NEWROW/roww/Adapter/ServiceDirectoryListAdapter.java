package com.NEWROW.row.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.NEWROW.row.Data.ServiceDirectoryListData;
import com.NEWROW.row.R;
import com.NEWROW.row.Utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by USER1 on 19-07-2016.
 */
public class ServiceDirectoryListAdapter  extends ArrayAdapter<ServiceDirectoryListData> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<ServiceDirectoryListData> directoryDatas = new ArrayList<ServiceDirectoryListData>();
    String flag ; // 0-Directroy 1-globalsearch ----- To hide the contents

    public ServiceDirectoryListAdapter(Context mContext, int layoutResourceId, ArrayList<ServiceDirectoryListData> directoryDatas,String inputflag) {
        super(mContext, layoutResourceId, directoryDatas);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.directoryDatas = directoryDatas;
        this.flag=inputflag;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(directoryDatas.get(position).getServiceDirId());
    }

    public void setGridData(ArrayList<ServiceDirectoryListData> directoryDatas) {
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

        final ServiceDirectoryListData item = directoryDatas.get(position);

        holder.name.setText(item.getMemberName());
        if ( item.getCsv() == null || item.getCsv().equals("")) {
            holder.mobile.setText("");
        } else {
            holder.mobile.setText(item.getCsv());
        }

       /* if(flag.equals("1")) {
            holder.grp_name.setVisibility(View.VISIBLE);
            holder.grp_name.setText("In no of entities:-"+item.getGrpCount());
        }*/

        if (item.getImage().trim().length() == 0 || item.getImage().equals("") || item.getImage() == null || item.getImage().isEmpty()) {
            holder.imageView.setImageResource(R.drawable.profile_pic);
        } else {
            Picasso.with(mContext).load(item.getImage()).transform(new CircleTransform())
                    .placeholder(R.drawable.profile_pic)
                    .into(holder.imageView);
        }

       /* if(flag.equals("0")) {
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, Profile.class);
                    i.putExtra("memberprofileid", item.getProfileID());
                    i.putExtra("groupId", item.getGrpID());
                    ((Activity) mContext).startActivityForResult(i, 1);
                }
            });
        }*/
        return row;
    }

    static class ViewHolder {
        TextView name,mobile,grp_name;
        ImageView imageView;
    }

    public ArrayList<ServiceDirectoryListData> getGridData() {
        return this.directoryDatas;
    }
}

