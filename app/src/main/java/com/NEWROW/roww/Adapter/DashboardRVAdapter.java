package com.NEWROW.row.Adapter;


import android.content.Context;
import android.content.res.Resources;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.NEWROW.row.Data.GroupData;
import com.NEWROW.row.R;
import com.NEWROW.row.Utils.Utils;
import com.NEWROW.row.holders.GroupHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.NEWROW.row.FragmentALL.notificationCountDatas;


public class DashboardRVAdapter extends RecyclerView.Adapter<GroupHolder> {

    private Context context;
    private ArrayList<GroupData> arrayList;
    public static AlertDialog dialog;
//    private int IS_THIRD_POSITION = 0;

    public DashboardRVAdapter(Context con, ArrayList<GroupData> arr) {
        this.context = con;
        this.arrayList = arr;
    }

    @Override
    public GroupHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_listview_holder, parent, false);

        return new GroupHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupHolder holder, final int position) {
        GroupHolder gHolder = (GroupHolder) holder;
        GroupData data = (GroupData) arrayList.get(position);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Resources r = context.getResources();

        int sp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                r.getDimension(R.dimen.text_size_3_sp),
                r.getDisplayMetrics()
        );

        if(arrayList.size() % 2 == 0) {
            if(position % 2 == 0) {
                layoutParams.setMargins(0,0,sp,sp);
            }else {
                layoutParams.setMargins(0,0,0,sp);
            }
        }else {
            if(position == arrayList.size()-1) {
                layoutParams.setMargins(sp,0,sp,0);
            }else {
                if(position % 2 == 0) {
                    layoutParams.setMargins(sp,0,sp,sp);
                }else {
                    layoutParams.setMargins(0,0,sp,sp);
                }
            }
        }

        holder.ll_holder.setLayoutParams(layoutParams);

        Utils.log("expirary falg="+data.getExpiryFlag());

        //add only active club or district added by satish on 26-07-2019
        if(data.getExpiryFlag().equalsIgnoreCase("1")){
            holder.ll_holder.setVisibility(View.GONE);
        }else{
            holder.ll_holder.setVisibility(View.VISIBLE);
        }

        gHolder.tv_title.setText(data.getGrpName());

        if (data.getGrpImg().trim().length() == 0 || data.getGrpImg() == null || data.getGrpImg().isEmpty()) {
           // gHolder.iv_img.setImageResource(R.drawable.app_icon_big);
            gHolder.iv_img.setImageResource(R.drawable.defaultlogo);
        } else {
            Picasso.with(context).load(data.getGrpImg())
                    .placeholder(R.drawable.defaultlogo)
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

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
//        if (position == 2)
//            IS_THIRD_POSITION = 1;
//        return super.getItemViewType(position);
        return 0;
    }


    public void setOnGroupSelectedListener(DashboardRVAdapter.OnGroupSelectedListener onGroupSelectedListener) {
        this.onGroupSelectedListener = onGroupSelectedListener;
    }

    private DashboardRVAdapter.OnGroupSelectedListener onGroupSelectedListener;

    public interface OnGroupSelectedListener {
        void onGroupSelected(int position);
    }

}
