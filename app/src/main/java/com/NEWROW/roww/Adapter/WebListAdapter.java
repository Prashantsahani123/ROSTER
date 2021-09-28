package com.NEWROW.row.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.NEWROW.row.Data.WebLinkListData;
import com.NEWROW.row.R;
import com.NEWROW.row.WebLinkDescription;
import com.NEWROW.row.holders.EmptyViewHolder;
import com.NEWROW.row.holders.WebLinkListHolder;

import java.util.ArrayList;

/**
 * Created by admin on 27-04-2017.
 */

public class WebListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<WebLinkListData> list;
    public static final int View_TYPE_EMPTYLIST = 1;
    public static final int VIEW_TYPE_WEBLINK = 2;

    public WebListAdapter(Context context,ArrayList<WebLinkListData> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType==View_TYPE_EMPTYLIST){
            View v = LayoutInflater.from(context).inflate(R.layout.empty_calendar_recyclerview,parent,false);
            return new EmptyViewHolder(v);
        }else if(viewType==VIEW_TYPE_WEBLINK){
            View v = LayoutInflater.from(context).inflate(R.layout.holder_weblinklist,parent,false);
            WebLinkListHolder holder = new WebLinkListHolder(v);
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

        WebLinkListHolder hol = (WebLinkListHolder) holder;
        hol.tv_title.setText(list.get(position).getTitle());
        hol.tv_designation.setText(list.get(position).getDescription());

        hol.linear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, WebLinkDescription.class);
                i.putExtra("title",list.get(position).getTitle());
                i.putExtra("description",list.get(position).getDescription());
                i.putExtra("linkUrl",list.get(position).getLinkUrl());
                i.putExtra("webLinkId",list.get(position).getWebLinkId());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemViewType(int position) {

        if(list.size()==0){
            return View_TYPE_EMPTYLIST;
        }else{
           return VIEW_TYPE_WEBLINK;
        }
    }

    @Override
    public int getItemCount() {

        if(list.size()==0){
            return View_TYPE_EMPTYLIST;
        }
        return list.size();
    }
}
