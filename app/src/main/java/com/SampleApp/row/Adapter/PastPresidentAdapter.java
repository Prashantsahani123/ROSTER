package com.SampleApp.row.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.SampleApp.row.Data.BoardOfDirectorsData;
import com.SampleApp.row.Data.PastPresidentData;
import com.SampleApp.row.R;
import com.SampleApp.row.Utils.CircleTransform;
import com.SampleApp.row.holders.EmptyViewHolder;
import com.SampleApp.row.holders.PastPresidentListHolder;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

/**
 * Created by admin on 28-04-2017.
 */

public class PastPresidentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<PastPresidentData> list;
    private static final int View_TYPE_EMPTYLIST = 1 ;
    private static final int View_TYPE_PAST_PRESIDENT_LIST = 2 ;

    public PastPresidentAdapter(Context context, ArrayList<PastPresidentData> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType== View_TYPE_EMPTYLIST){
            View v = LayoutInflater.from(context).inflate(R.layout.empty_calendar_recyclerview,parent,false);
            return new EmptyViewHolder(v);
        }
        else if(viewType==View_TYPE_PAST_PRESIDENT_LIST){
            View v = LayoutInflater.from(context).inflate(R.layout.holder_past_president,parent,false);
            PastPresidentListHolder holder = new PastPresidentListHolder(v);
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

        PastPresidentListHolder hol = (PastPresidentListHolder) holder;
        hol.tv_name.setText(list.get(position).getMemberName());
        hol.tv_designation.setText(list.get(position).getTenureYear());


        if (list.get(position).getPhotopath().trim().length() == 0 || list.get(position).getPhotopath().equals("") || list.get(position).getPhotopath() == null || list.get(position).getPhotopath().isEmpty()) {
            hol.iv_image.setImageResource(R.drawable.profile_pic);
        } else {
            Picasso.with(context).load(list.get(position).getPhotopath()).transform(new CircleTransform())
                    .placeholder(R.drawable.profile_pic)
                    .into(hol.iv_image);
        }

        hol.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }


    @Override
    public int getItemViewType(int position) {
       if(list.size()==0){
           return View_TYPE_EMPTYLIST;
       }else {
           return View_TYPE_PAST_PRESIDENT_LIST;
       }
    }

    @Override
    public int getItemCount() {
        if (list.size() == 0) {
            return View_TYPE_EMPTYLIST;
        }
        return list.size();
    }

}
