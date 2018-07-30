package com.SampleApp.row.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.SampleApp.row.Data.ServiceDirectoryCategoryData;
import com.SampleApp.row.DistrictCommitteeWithCategory;
import com.SampleApp.row.R;
import com.SampleApp.row.ServiceDirectoryList;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.holders.EmptyViewHolder;
import com.SampleApp.row.holders.ServiceDirectoryCategoryHolder;

import java.util.ArrayList;

/**
 * Created by user on 21-03-2017.
 */
public class ServiceDirectoryCategoryRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<ServiceDirectoryCategoryData> list=new ArrayList<>();
    ArrayList<ServiceDirectoryCategoryData> filterList;
    private static final int View_TYPE_EMPTYLIST = 1;
    private static final int View_TYPE_CATEGORY = 2;
    String modulename;

    public ServiceDirectoryCategoryRVAdapter(Context context,ArrayList<ServiceDirectoryCategoryData> list,String modulename){
        this.context = context;
        this.list = list;
        this.modulename = modulename;
        this.filterList = new ArrayList<>();
        filterList.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == View_TYPE_EMPTYLIST) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_calendar_recyclerview,parent,false);
            Log.d("♦♦♦♦ ","Inside View_TYPE_EMPTYLIST ");
            return new EmptyViewHolder(v);
        } else if ( viewType == View_TYPE_CATEGORY ) {
            View  v = LayoutInflater.from(context).inflate(R.layout.holder_servicedirectory_category,parent,false);
            ServiceDirectoryCategoryHolder holder = new ServiceDirectoryCategoryHolder(v);
            Log.d("♦♦♦♦ ","Inside View_TYPE_CATEGORY ");
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == View_TYPE_EMPTYLIST) {
            bindEmptyView(holder, position);
        }
        else {
            bindNonEmptyView(holder, position);
        }

    }

    public void bindEmptyView(RecyclerView.ViewHolder holder, final int position) {
         ((EmptyViewHolder) holder).getEmptyView().setVisibility(View.GONE);
    }

    public void bindNonEmptyView(RecyclerView.ViewHolder holder, final int position) {

        ServiceDirectoryCategoryHolder hol = (ServiceDirectoryCategoryHolder) holder;
        String categoryName = list.get(position).getCategoryName();

        hol.tv_categoryName.setText(categoryName);


        if(modulename.equalsIgnoreCase("District Committee")){
            hol.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DistrictCommitteeWithCategory.class);
                    i.putExtra("categoryId", String.valueOf(list.get(position).getCategoryId()));
                    i.putExtra("categoryName", list.get(position).getCategoryName());
                    context.startActivity(i);
                    Log.e("categoryId", "categoryId : " + String.valueOf(list.get(position).getCategoryId()));
                }
            });
        }else {
            hol.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ServiceDirectoryList.class);
                    i.putExtra("categoryId", String.valueOf(list.get(position).getCategoryId()));
                    i.putExtra("moduleName", modulename);
                    context.startActivity(i);
                    Log.e("categoryId", "categoryId : " + String.valueOf(list.get(position).getCategoryId()));
                }
            });
        }

            }



    @Override
    public int getItemViewType(int position) {

        if(list.size()==0){
            return View_TYPE_EMPTYLIST;
        }else{
            return View_TYPE_CATEGORY;
        }
    }

    @Override
    public int getItemCount() {
        if(list.size()==0){
            return  View_TYPE_EMPTYLIST;
        }
        return list.size();
    }

    // Filter Class
    public void filter(String charText) {
        charText=charText.toLowerCase();
        list.clear();
        if (charText.length() == 0) {
            list.addAll(filterList);
        }
        else
        {
            for (ServiceDirectoryCategoryData wp : filterList)
            {
                Utils.log(wp.getCategoryName());
                if (wp.getCategoryName().toLowerCase().contains(charText))
                {
                    list.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public ArrayList<ServiceDirectoryCategoryData> getList() {
        return list;
    }
}
