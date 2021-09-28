package com.NEWROW.row.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.NEWROW.row.Data.ActivityNotiData;
import com.NEWROW.row.Data.LabelData;
import com.NEWROW.row.Data.profiledata.Separator;
import com.NEWROW.row.GalleryDescription;
import com.NEWROW.row.R;
import com.NEWROW.row.Utils.Utils;
import com.NEWROW.row.holders.EmptyViewHolder;

import java.util.ArrayList;

public class ActivitiesNotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<Object> list;
    private static final int VIEW_TYPE_EMPTYLIST = 1;
    private static final int VIEW_TYPE_LIST = 2;
    private static final int VIEW_TYPE_LABEL = 3;
    private static final int VIEW_TYPE_SEPERATOR = 4;


    public ActivitiesNotificationAdapter(Context context, ArrayList<Object> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTYLIST) {
            View v = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.empty_calendar_recyclerview, parent, false);
            return new EmptyViewHolder(v);
        } else if (viewType == VIEW_TYPE_LIST) {
            View v = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.act_noti_details, parent, false);
            ActivityNotificationHolder holder = new ActivityNotificationHolder(v);
            return holder;
        } else if (viewType == VIEW_TYPE_LABEL) {
            View v = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.act_noti_label, parent, false);
            ActivityLabelHolder holder = new ActivityLabelHolder(v);
            return holder;

        } else if ( viewType == VIEW_TYPE_SEPERATOR) {
            View v = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.divider, parent, false);
            DividerHolder holder = new DividerHolder(v);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_EMPTYLIST) {
            Utils.log("View type is EMPTY");
            bindEmptyView(holder, position);

        } else if (getItemViewType(position) == VIEW_TYPE_LABEL) {
            Utils.log("View type is : LABEL");
            bindLabelView(holder, position);
        }else if(getItemViewType(position) == VIEW_TYPE_SEPERATOR){

        } else {
            Utils.log("View type is : EVENT");
            bindNonEmptyView(holder, position);
        }
    }

    private void bindLabelView(RecyclerView.ViewHolder holder, int position) {

        ((ActivityLabelHolder) holder).tv_lable.setText(((LabelData) list.get(position)).getLabel());

    }

    private void bindEmptyView(RecyclerView.ViewHolder holder, int position) {
        ((EmptyViewHolder) holder).getEmptyView().setText("No Records Found");
    }

    private void bindNonEmptyView(RecyclerView.ViewHolder holder, int position) {

        ActivityNotificationHolder activityNotificationHolder= (ActivityNotificationHolder) holder;
        final ActivityNotiData activityNotiData= (ActivityNotiData) list.get(position);

        activityNotificationHolder.tv_title.setText(activityNotiData.getTitle());
        activityNotificationHolder.tv_desc.setText(activityNotiData.getDesc());
        activityNotificationHolder.tv_name.setText(activityNotiData.getName());
        activityNotificationHolder.tv_DateTime.setText(activityNotiData.getDatetime());

        activityNotificationHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context,GalleryDescription.class);
                intent.putExtra("albumID",activityNotiData.getAlbumId());
                intent.putExtra("fromNoti","1");
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.size() == 0) {
            return VIEW_TYPE_EMPTYLIST;
        } else if (list.get(position) instanceof LabelData) {
            return VIEW_TYPE_LABEL;
        } else if ( list.get(position) instanceof Separator) {
            return VIEW_TYPE_SEPERATOR;
        } else {
            return VIEW_TYPE_LIST;
        }
    }

    public class DividerHolder extends RecyclerView.ViewHolder {

        public DividerHolder(View itemView) {
            super(itemView);
        }
    }

    public class ActivityNotificationHolder extends RecyclerView.ViewHolder {
        public TextView tv_name, tv_title,tv_desc,tv_DateTime;

        public ActivityNotificationHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.txt_name);
            tv_title = (TextView) itemView.findViewById(R.id.txt_title);
            tv_desc = (TextView) itemView.findViewById(R.id.tvDesc);
            tv_DateTime = (TextView) itemView.findViewById(R.id.txt_date);

        }
    }

    public class ActivityLabelHolder extends RecyclerView.ViewHolder {
        public TextView tv_lable;

        public ActivityLabelHolder(View itemView) {
            super(itemView);
            tv_lable = (TextView) itemView.findViewById(R.id.tvLabel);
        }
    }
}
