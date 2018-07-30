package com.SampleApp.row.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.SampleApp.row.Data.GroupData;
import com.SampleApp.row.DistrictSettingActivity;
import com.SampleApp.row.GroupSettings;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.holders.FeedHolder;

import java.util.ArrayList;

/**
 * Created by admin on 25-04-2017.
 */

public class SettingsGroupListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    ArrayList<GroupData> list;
    private static final int View_TYPE_EMPTYLIST = 1 ;
    private static final int View_TYPE_ROTARIAN_LIST = 2 ;
    private static LayoutInflater inflater = null;
    public SettingsGroupListAdapter(Context context, ArrayList<GroupData> list){
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = inflater.inflate(R.layout.feed_layout, parent, false);
        FeedHolder holder = new FeedHolder(v);
        Log.d("♦♦♦♦ ", "Inside View_TYPE_Settings ");
        return holder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


            bindNonEmptyView(holder, position);
    }



    public void bindNonEmptyView(RecyclerView.ViewHolder holder, final int position) {

        final GroupData data = (GroupData) list.get(position);
        FeedHolder gHolder = (FeedHolder) holder;

        gHolder.getTvTitle().setText(data.getGrpName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(Constant.GROUP_CATEGORY_CLUB==Integer.parseInt(data.getMyCategory())){
                   Intent i = new Intent(context, GroupSettings.class);
                   i.putExtra("grpName",data.getGrpName());
                   i.putExtra("grpId",data.getGrpId());
                   i.putExtra("grpProfileId", data.getGrpProfileId());
                   context.startActivity(i);
               }else if(Constant.GROUP_CATEGORY_DT ==Integer.parseInt(data.getMyCategory())){
                   Intent i = new Intent(context, DistrictSettingActivity.class);
                   i.putExtra("grpName",data.getGrpName());
                   i.putExtra("grpId",data.getGrpId());
                   i.putExtra("grpProfileId", data.getGrpProfileId());
                   context.startActivity(i);
               }
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
