package com.SampleApp.row.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.SampleApp.row.Data.DistrictCommitteeData;
import com.SampleApp.row.NewProfileActivity;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.CircleTransform;
import com.SampleApp.row.holders.BODListHolder;
import com.SampleApp.row.holders.DistrictCommitteeHolder;
import com.SampleApp.row.holders.EmptyViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 27-07-2017.
 */

public class DistrictCommitteeRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<DistrictCommitteeData> list;
    private static final int VIEW_TYPE_EMPTYLIST = 1 ;
    private static final int VIEW_TYPE_DT_ITEM = 2 ;

    public DistrictCommitteeRVAdapter(Context context, ArrayList<DistrictCommitteeData> list){
        this.context = context;
        this.list = list;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType== VIEW_TYPE_EMPTYLIST){
            View v = LayoutInflater.from(context).inflate(R.layout.empty_calendar_recyclerview,parent,false);
            Log.d("♦♦♦♦ ","Inside VIEW_TYPE_EMPTYLIST ");
            return new EmptyViewHolder(v);
        } else if(viewType== VIEW_TYPE_DT_ITEM) {
            View  v = LayoutInflater.from(context).inflate(R.layout.district_committee_item,parent,false);
            DistrictCommitteeHolder holder = new DistrictCommitteeHolder(v);
            Log.d("♦♦♦♦ ","Inside View_TYPE_ROTARIAN_LIST ");
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_EMPTYLIST) {
            bindEmptyView(holder, position);
        } else {
            bindNonEmptyView(holder, position);
        }
    }

    public void bindEmptyView(RecyclerView.ViewHolder holder, final int position) {
        ((EmptyViewHolder) holder).getEmptyView().setText("No records found");
    }

    public void bindNonEmptyView(RecyclerView.ViewHolder holder, final int position) {

        DistrictCommitteeHolder hol = (DistrictCommitteeHolder) holder;
        hol.getTvMemberName().setText(list.get(position).getMemberName());
        String clubInfo = list.get(position).getMemberDesignation();
        if (! list.get(position).getClubName().trim().equals("")) {
            clubInfo = list.get(position).getClubName()+" - "+list.get(position).getMemberDesignation();
        }
        clubInfo = clubInfo.trim();
        hol.getTvDesignation().setText(clubInfo);


        if (list.get(position).getPic().trim().length() == 0 || list.get(position).getPic().equals("") || list.get(position).getPic() == null || list.get(position).getPic().isEmpty()) {
            hol.getIvPic().setImageResource(R.drawable.profile_pic);
        } else {
            Picasso.with(context).load(list.get(position).getPic()).transform(new CircleTransform())
                    .placeholder(R.drawable.profile_pic)
                    .into(hol.getIvPic());
        }

        hol.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewProfileActivity.class);
                /*i.putExtra("memberprofileid", list.get(position).getProfileID());
                i.putExtra("groupId", list.get(position).getGrpID());

                i.putExtra("nameLabel","Name");
                i.putExtra("numberLabel","Mobile Number");
                i.putExtra("memberName",list.get(position).getMemberName());
                i.putExtra("memberMobile",list.get(position).getMemberMobile());*/
                intent.putExtra("memberProfileId", list.get(position).getProfileID());
                intent.putExtra("groupId", list.get(position).getGrpID());
                intent.putExtra("fromMainDirectory", "no");
                intent.putExtra("fromDistrictCommitteeList", "yes");
                ((Activity) context).startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        if(list.size()==0){
            return VIEW_TYPE_EMPTYLIST;
        }else{
            return VIEW_TYPE_DT_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if(list.size()==0){
            return VIEW_TYPE_EMPTYLIST;
        }
        return list.size();
    }

}
