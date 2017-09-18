package com.SampleApp.row.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.SampleApp.row.Data.BlogFeed;
import com.SampleApp.row.Data.GroupData;
import com.SampleApp.row.Data.LoadingMessageData;
import com.SampleApp.row.Data.NewsFeed;
import com.SampleApp.row.Data.NotificationCountData;
import com.SampleApp.row.R;
import com.SampleApp.row.ShowFeedActivity;
import com.SampleApp.row.holders.EmptyViewHolder;
import com.SampleApp.row.holders.FeedHolder;
import com.SampleApp.row.holders.GroupHolder;
import com.SampleApp.row.holders.LabelHolder;
import com.SampleApp.row.holders.LoadingMessageHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.SampleApp.row.FragmentALL.notificationCountDatas;

/**
 * Created by user on 29-12-2015.
 */
public class FragmentALLAdapter_new extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String[] result;
    Context context;
    ArrayList<Object> listgrp = new ArrayList<>();
    ArrayList<NotificationCountData> notificationCountDataArrayList = new ArrayList<NotificationCountData>();
    private static LayoutInflater inflater = null;
    String editflag; //0-edit grps . 1- normal listing

    private static final int VIEW_TYPE_GROUP = 1;
    private static final int VIEW_TYPE_FEED = 2;
    private static final int VIEW_TYPE_BLOG = 3;
    private static final int VIEW_TYPE_EMPTYLIST = 4;
    private static final int VIEW_TYPE_LABEL = 5;
    private static final int VIEW_TYPE_LOADING_MESSAGE = 6;


    public FragmentALLAdapter_new(Context cnt, ArrayList<Object> list, String edit) {
        // TODO Auto-generated constructor stub

        context = cnt;
        listgrp = list;
        editflag = edit;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (viewType == VIEW_TYPE_GROUP) {
            View v = inflater.inflate(R.layout.group_listview_holder, parent, false);
            GroupHolder holder = new GroupHolder(v);
            Log.d("♦♦♦♦ ", "Inside View_TYPE_GROUP ");
            return holder;
        } else if (viewType == VIEW_TYPE_FEED) {
            View v = inflater.inflate(R.layout.feed_layout, parent, false);
            FeedHolder holder = new FeedHolder(v);
            Log.d("♦♦♦♦ ", "Inside View_TYPE_FEED ");
            return holder;
        } else if (viewType == VIEW_TYPE_BLOG) {
            View v = inflater.inflate(R.layout.feed_layout, parent, false);
            FeedHolder holder = new FeedHolder(v);
            Log.d("♦♦♦♦ ", "Inside View_TYPE_BLOG ");
            return holder;
        } else if (viewType == VIEW_TYPE_EMPTYLIST) {
            View v = inflater.inflate(R.layout.empty_calendar_recyclerview, parent, false);
            Log.d("♦♦♦♦ ", "Inside View_TYPE_EMPTYLIST ");
            return new EmptyViewHolder(v);
        } else if (viewType == VIEW_TYPE_LABEL) {
            View v = inflater.inflate(R.layout.layout_label, parent, false);
            Log.d("♦♦♦♦ ", "Inside View_TYPE_LABEL");
            LabelHolder holder = new LabelHolder(v);
            return holder;
        } else if (viewType == VIEW_TYPE_LOADING_MESSAGE) {
            View v = inflater.inflate(R.layout.layout_loading_message, parent, false);
            Log.d("♦♦♦♦ ", "Inside VIEW_TYPE_LOADING_MESSAGE");
            LoadingMessageHolder holder = new LoadingMessageHolder(v);
            return holder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) return;
        if (getItemViewType(position) == VIEW_TYPE_EMPTYLIST) {
            bindEmptyView(holder, position);
        } else if (getItemViewType(position) == VIEW_TYPE_GROUP) {
            bindGroupData(holder, position);
        } else if (getItemViewType(position) == VIEW_TYPE_FEED) {
            bindFeedData(holder, position);
        } else if (getItemViewType(position) == VIEW_TYPE_BLOG) {
            bindBlogData(holder, position);
        } else if (getItemViewType(position) == VIEW_TYPE_LABEL) {
            bindLabelData(holder, position);
        } else if (getItemViewType(position) == VIEW_TYPE_LOADING_MESSAGE) {
            bindLoadingMessageData(holder, position);
        }
    }

    public void bindLoadingMessageData(RecyclerView.ViewHolder holder, final int position) {
        LoadingMessageData data = (LoadingMessageData) listgrp.get(position);
        LoadingMessageHolder messageHolder = (LoadingMessageHolder) holder;
        messageHolder.getTvMessage().setText("Loading...\n" +
                "News/Blogs from rotary.org\n" +
                "Please wait...");
    }

    public void bindEmptyView(RecyclerView.ViewHolder holder, final int position) {
        ((EmptyViewHolder) holder).getEmptyView().setText("No records found");
    }


    public void bindLabelData(RecyclerView.ViewHolder holder, final int position) {
        String label = (String) listgrp.get(position);
        LabelHolder lHolder = (LabelHolder) holder;
        lHolder.getTvLabel().setText(label);
    }

    public void bindGroupData(RecyclerView.ViewHolder holder, final int position) {
        GroupData data = (GroupData) listgrp.get(position);
        GroupHolder gHolder = (GroupHolder) holder;

        gHolder.tv_title.setText(data.getGrpName());
        if (data.getGrpImg().trim().length() == 0 || data.getGrpImg() == null || data.getGrpImg().isEmpty()) {
            gHolder.iv_img.setImageResource(R.drawable.app_icon_big);
        } else {
            Picasso.with(context).load(data.getGrpImg())
                    .placeholder(R.drawable.row_icon)
                    .resize(150, 150)
                    .into(gHolder.iv_img);
        }

        int count = notificationCountDatas.getGroupCount(data.getGrpId());

        if (count == 0) {
            gHolder.tv_group_count.setVisibility(View.GONE);
        } else {
            gHolder.tv_group_count.setVisibility(View.VISIBLE);
            gHolder.tv_group_count.setText("" + notificationCountDatas.getGroupCount(data.getGrpId()));
            gHolder.tv_group_count.setBackgroundResource(R.drawable.notification_count);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onGroupSelectedListener != null) {
                    onGroupSelectedListener.onGroupSelected(position);
                }
            }
        });
    }

    public void setOnGroupSelectedListener(OnGroupSelectedListener onGroupSelectedListener) {
        this.onGroupSelectedListener = onGroupSelectedListener;
    }

    private OnGroupSelectedListener onGroupSelectedListener;

    public interface OnGroupSelectedListener {
        void onGroupSelected(int position);
    }

    public void bindBlogData(RecyclerView.ViewHolder holder, final int position) {
        final BlogFeed data = (BlogFeed) listgrp.get(position);
        FeedHolder gHolder = (FeedHolder) holder;

        gHolder.getTvTitle().setText(data.getDescription());
        gHolder.getTvDescription().setText(data.getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(context, OpenLinkActivity.class);
                i.putExtra("link", data.getLink());
                i.putExtra("modulename", "Rotary Blog");
                context.startActivity(i);*/
                Intent i = new Intent(context, ShowFeedActivity.class);

                i.putExtra("link", data.getLink());
                i.putExtra("modulename", "Rotary Blog");
                i.putExtra("description", data.getDescription());

                context.startActivity(i);
            }
        });
    }

    public void bindFeedData(RecyclerView.ViewHolder holder, final int position) {
        final NewsFeed data = (NewsFeed) listgrp.get(position);
        FeedHolder gHolder = (FeedHolder) holder;

        gHolder.getTvTitle().setText(data.getTitle());
        gHolder.getTvDescription().setText(data.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(context, OpenLinkActivity.class);

                i.putExtra("link", data.getLink());
                i.putExtra("modulename", "Rotary News & Updates");
                context.startActivity(i);*/
                Intent i = new Intent(context, ShowFeedActivity.class);

                i.putExtra("link", data.getLink());
                i.putExtra("modulename", "Rotary News & Updates");
                i.putExtra("description", data.getDescription());

                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;//= 0;
        if (listgrp.get(position) instanceof GroupData) {
            viewType = VIEW_TYPE_GROUP;
        } else if (listgrp.get(position) instanceof NewsFeed) {
            viewType = VIEW_TYPE_FEED;
        } else if (listgrp.get(position) instanceof BlogFeed) {
            viewType = VIEW_TYPE_BLOG;
        } else if (listgrp.get(position) instanceof String) {
            viewType = VIEW_TYPE_LABEL;
        } else if (listgrp.get(position) instanceof LoadingMessageData) {
            viewType = VIEW_TYPE_LOADING_MESSAGE;
        } else {
            viewType = super.getItemViewType(position);
        }

        return viewType;
    }

    @Override
    public int getItemCount() {
        return listgrp.size();
    }

    /*public FragmentALLAdapter_new(Context cnt, ArrayList<NotificationCountData> notificationCountDataArrayList) {
        // TODO Auto-generated constructor stub

        context = cnt;
        notificationCountDataArrayList = notificationCountDataArrayList;
        //editflag = edit;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }*/

//    @Override
//    public int getCount() {
//        // TODO Auto-generated method stub
//        return listgrp.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        // TODO Auto-generated method stub
//        return listgrp.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        // TODO Auto-generated method stub
//        return position;
//    }

//    public class Holder {
//        // TextView tv;
//        ImageView img;
//        TextView text;
//        FrameLayout framelayout;
//        TextView tv_group_count;
//        //CheckBox cb_select;
//    }

//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        // TODO Auto-generated method stub
//       Holder holder = new Holder();
//        View rowView = convertView;
//        if (convertView == null) {
//            rowView = inflater.inflate(R.layout.group_listview_holder, null);
//            holder.img = (ImageView) rowView.findViewById(R.id.imageView);
//            holder.text = (TextView) rowView.findViewById(R.id.title);
//            //holder.framelayout = (FrameLayout) rowView.findViewById(R.id.framelayout);
//            holder.tv_group_count = (TextView) rowView.findViewById(R.id.tv_group_count);
//
//            rowView.setTag(holder);
//
//        }else {
//            holder = (Holder)rowView.getTag();
//        }
//        holder = (Holder)rowView.getTag();


//        final GroupData p = getMember(position);
//        final CheckBox cb_select = (CheckBox) rowView.findViewById(R.id.cb_select);
//        cb_select.setOnCheckedChangeListener(myCheckChangList);
//        cb_select.setTag(position);
//        cb_select.setChecked(p.box);


//        if (editflag.equals("0")) {
//            cb_select.setVisibility(View.VISIBLE);
//          //  cb_select.setTag(listgrp.get(position).getGrpProfileId());
//            holder.framelayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//
//
//                public void onClick(View v) {
//
//                    if (cb_select.isChecked() == true) {
//                        cb_select.setChecked(false);
//                    } else
//                        cb_select.setChecked(true);
//
//                }
//
//            });
//        }
//        if (listgrp.get(position).getGrpImg().trim().length() == 0 || listgrp.get(position).getGrpImg() == null || listgrp.get(position).getGrpImg().isEmpty()) {
//            holder.img.setImageResource(R.drawable.dashboardplaceholder);
//        } else {
//            Picasso.with(context).load(listgrp.get(position).getGrpImg())
//                    .placeholder(R.drawable.dashboardplaceholder)
//                    .resize(150,150)
//                    .into(holder.img);
//        }
//
//        holder.text.setText(listgrp.get(position).getGrpName());
//
//        int count = notificationCountDatas.getGroupCount(listgrp.get(position).getGrpId());
//
//        if ( count == 0 ) {
//            holder.tv_group_count.setVisibility(View.GONE);
//        } else {
//            holder.tv_group_count.setVisibility(View.VISIBLE);
//            holder.tv_group_count.setText(""+notificationCountDatas.getGroupCount(listgrp.get(position).getGrpId()));
//            holder.tv_group_count.setBackgroundResource(R.drawable.notification_count);
//        }
        /*if (!notificationCountDatas.isEmpty()) {

            for (int i = 0; i < notificationCountDatas.size(); i++) {
                if (listgrp.get(position).getGrpId().equals(notificationCountDatas.get(i).getGroupId()))

                {
                    if (notificationCountDatas.get(i).getTotalCount().equals("0")) {
                        holder.tv_group_count.setVisibility(View.GONE);
                    } else {

                      *//*  int count_read_module = GroupDashboadAdapter_new.count_read_module;
                        int total_module = Integer.parseInt(notificationCountDatas.get(i).getTotalCount());
                        String finalcount_module = String.valueOf(total_module-count_read_module);
                        //holder.group_count.setVisibility(View.VISIBLE);
                        holder.tv_group_count.setText(finalcount_module);*//*

                        holder.tv_group_count.setVisibility(View.VISIBLE);
                        holder.tv_group_count.setText(notificationCountDatas.get(i).getTotalCount());
                        holder.tv_group_count.setBackgroundResource(R.drawable.notification_count);
                    }
                }

            }
        }*/

      /*  for (int i = 0 ;i<notificationCountDatas.size();i++) {
            if(listgrp.get(position).getGrpId()== notificationCountDatas.get(i).getGroupId())

            {
                holder.tv_group_count.setVisibility(View.VISIBLE);
                holder.tv_group_count.setText(notificationCountDatas.get(i).getTotalCount());
                holder.tv_group_count.setBackgroundResource(R.drawable.notification_count);
            }
        }*/


    //holder.text.setText(listgrp[position]);

     /*   rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked ", Toast.LENGTH_LONG).show();
            }
        });*/

//        return rowView;
//    }
//    GroupData getMember(int position) {
//        return ((GroupData) getItem(position));
//    }
//
//    public ArrayList<GroupData> getBox() {
//        ArrayList<GroupData> box = new ArrayList<GroupData>();
//        for (GroupData p : listgrp)
//        {
//            if (p.box)
//                box.add(p);
//
//        }
//        return box;
//    }
//
//    OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
//        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
//            getMember((Integer) buttonView.getTag()).box = isChecked;
//
//        }
//    };

}
