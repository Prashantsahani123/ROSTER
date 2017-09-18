package com.SampleApp.row.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.SampleApp.row.Data.FindAClubResultData;
import com.SampleApp.row.R;
import com.SampleApp.row.holders.ClubHolder;
import com.SampleApp.row.holders.EmptyViewHolder;

import java.util.ArrayList;

/**
 * Created by admin on 30-05-2017.
 */

public class ClubAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<FindAClubResultData> list;
    private static final int View_TYPE_EMPTYLIST = 1;
    private static final int View_TYPE_CLUB_LIST = 2;
    String authToken;

    public ClubAdapter(Context context, ArrayList<FindAClubResultData> list, String authToken) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == View_TYPE_EMPTYLIST) {
            View v = LayoutInflater.from(context).inflate(R.layout.empty_calendar_recyclerview, parent, false);
            Log.d("♦♦♦♦ ", "Inside View_TYPE_EMPTYLIST ");
            return new EmptyViewHolder(v);
        } else if (viewType == View_TYPE_CLUB_LIST) {
            View v = LayoutInflater.from(context).inflate(R.layout.holder_club_adapter, parent, false);
            ClubHolder holder = new ClubHolder(v);
            Log.d("♦♦♦♦ ", "Inside View_TYPE_ROTARIAN_LIST ");
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == View_TYPE_EMPTYLIST) {
            bindEmptyView(holder, position);
        } else {
            bindNonEmptyView(holder, position);
        }
    }

    public void bindEmptyView(RecyclerView.ViewHolder holder, final int position) {
        ((EmptyViewHolder) holder).getEmptyView().setText("No records found");
    }

    public void bindNonEmptyView(RecyclerView.ViewHolder holder, final int position) {

        ClubHolder hol = (ClubHolder) holder;
        // if(list.get(position).getLocation().getInternationalProvince()!= null) {
        //  hol.tv_name.setText(list.get(position).getClubName() + ", " + list.get(position).getLocation().getInternationalProvince());
        // }else{
        hol.tv_name.setText(list.get(position).getClubName());
        hol.tv_meetingday.setText(list.get(position).getMeetingDay());
        hol.tv_meetingtime.setText(list.get(position).getMeetingTime());
        if (list.get(position).getDistance() != null){
            hol.tv_distance.setVisibility(View.VISIBLE);
            hol.tv_distance.setText(list.get(position).getDistance()+ "Km");
        }
        // }
        if (onClubSelectedListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClubSelectedListener.onClubSelected(position);
                }
            });
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (list.size() == 0) {
            return View_TYPE_EMPTYLIST;
        } else {
            return View_TYPE_CLUB_LIST;
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() == 0) {
            return View_TYPE_EMPTYLIST;
        }
        return list.size();
    }

    public void setOnClubSelectedListener(OnClubSelectedListener onClubSelectedListener) {
        this.onClubSelectedListener = onClubSelectedListener;
    }

    private OnClubSelectedListener onClubSelectedListener;

    public interface OnClubSelectedListener {
        void onClubSelected(int position);
    }

    public ArrayList<FindAClubResultData> getItems() {
        return list;
    }
}
