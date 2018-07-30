package com.SampleApp.row.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.SampleApp.row.Announcement;
import com.SampleApp.row.Announcement_details;
import com.SampleApp.row.Data.AnnouncementListData;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.Constant;

import java.util.ArrayList;

/**
 * Created by USER on 02-02-2016.
 */
public class AnnouncementListAdapter extends ArrayAdapter<AnnouncementListData> {

    private Context mContext;
    private int layoutResourceId;
    public static int count_read_announcements = 0;
    private ArrayList<AnnouncementListData> announcementListDataArrayList = new ArrayList<AnnouncementListData>();


    public AnnouncementListAdapter(Context mContext, int layoutResourceId, ArrayList<AnnouncementListData> announcementListDataArrayList) {
        super(mContext, layoutResourceId, announcementListDataArrayList);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.announcementListDataArrayList = announcementListDataArrayList;
    }

    public void setGridData(ArrayList<AnnouncementListData> announcementListDataArrayList) {
        this.announcementListDataArrayList = announcementListDataArrayList;
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
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


        final AnnouncementListData item = announcementListDataArrayList.get(position);

        /*holder.textView1.setText(item.getImage());
        //holder.textView1.setText(Html.fromHtml(item.getImage_name()));
        Picasso.with(mContext).load(item.getImage())
                .placeholder(R.drawable.loading_icon)
                        //    .centerCrop()
                .resize(Constant.height, Constant.width)
                .into(holder.imageView);*/


        holder.title.setText(item.getAnnounTitle());
        holder.date.setText(item.getPublishDateTime());
        if (item.getFilterType().equals("1")) //1-publish , 2-unpublish, 3-expired
        {
            holder.time.setText(R.string.published);
        } else if (item.getFilterType().equals("2")) {
            holder.time.setText(R.string.unPublished);
        } else if (item.getFilterType().equals("3")) {
            holder.time.setText(R.string.expired);
        }

        if (item.getIsRead().equalsIgnoreCase("no") && item.getFilterType().equals(Constant.FILTER_TYPE_PUBLISHED)) {
            holder.title.setTextColor(mContext.getResources().getColor(R.color.bluecolor));
        } else {
            holder.title.setTextColor(mContext.getResources().getColor(R.color.black));
        }
        // Picasso.with(mContext).load(item.getImage()).into(holder.imageView);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, Announcement_details.class);
                i.putExtra("announcemet_id", item.getAnnounID());

                i.putExtra("moduleName", Announcement.moduleName);
                //mContext.startActivity(i);

                if(!announcementListDataArrayList.get(position).getIsRead().equals("Yes")) {
                    if ( !announcementListDataArrayList.get(position).getFilterType().equals(Constant.FilterTypes.FILTER_TYPE_EXPIRED))
                        count_read_announcements++;
                }
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
