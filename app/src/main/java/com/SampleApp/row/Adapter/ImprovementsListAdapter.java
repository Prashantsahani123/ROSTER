package com.SampleApp.row.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.SampleApp.row.Data.ImprovementListData;
import com.SampleApp.row.Improvement_details;
import com.SampleApp.row.R;


/**
 * Created by USER on 02-02-2016.
 */
public class ImprovementsListAdapter extends ArrayAdapter<ImprovementListData> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<ImprovementListData> announcementListDataArrayList = new ArrayList<ImprovementListData>();


    public ImprovementsListAdapter(Context mContext, int layoutResourceId, ArrayList<ImprovementListData> announcementListDataArrayList) {
        super(mContext, layoutResourceId, announcementListDataArrayList);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.announcementListDataArrayList = announcementListDataArrayList;
    }

    public void setGridData(ArrayList<ImprovementListData> announcementListDataArrayList) {
        this.announcementListDataArrayList = announcementListDataArrayList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            // holder.imageView = (ImageView) row.findViewById(R.id.imageView1);
            holder.title = (TextView) row.findViewById(R.id.tv_announTitle);
            holder.date = (TextView) row.findViewById(R.id.tv_announDAte);
            holder.time = (TextView) row.findViewById(R.id.tv_announTime);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        final ImprovementListData item = announcementListDataArrayList.get(position);

        /*holder.textView1.setText(item.getImage());
        //holder.textView1.setText(Html.fromHtml(item.getImage_name()));
        Picasso.with(mContext).load(item.getImage())
                .placeholder(R.drawable.loading_icon)
                        //    .centerCrop()
                .resize(Constant.height, Constant.width)
                .into(holder.imageView);*/


        holder.title.setText(item.getImprovementTitle());
        holder.date.setText(item.getPublishDateTime());
        if (item.getFilterType().equals("1")) //1-publish , 2-unpublish, 3-expired
        {
            holder.time.setText("PUBLISHED");
        } else if (item.getFilterType().equals("2")) {
            holder.time.setText("UN-PUBLISHED");
        } else if (item.getFilterType().equals("3")) {
            holder.time.setText("EXPIRED");
        }

        if (item.getIsRead().equals("Yes")) {
            holder.title.setTextColor(mContext.getResources().getColor(R.color.black));

        } else {
            holder.title.setTextColor(mContext.getResources().getColor(R.color.bluecolor));
        }
        // Picasso.with(mContext).load(item.getImage()).into(holder.imageView);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, Improvement_details.class);
                i.putExtra("improvementId", item.getImprovementID());
                //mContext.startActivity(i);
                ((Activity) mContext).startActivityForResult(i,1);

            }
        });
        return row;


        // return super.getView(position, convertView, parent);
    }

    static class ViewHolder {
        TextView title, date, time;
        //ImageView imageView;
    }
}
