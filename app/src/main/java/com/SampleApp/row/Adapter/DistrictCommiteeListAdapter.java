package com.SampleApp.row.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.SampleApp.row.Data.ServiceDirectoryListData;
import com.SampleApp.row.R;
import com.SampleApp.row.ServiceDirectoryDetail;
import com.SampleApp.row.Utils.CircleTransform;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.holders.DistrictCommitteeHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by USER1 on 19-07-2016.
 */
public class DistrictCommiteeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<ServiceDirectoryListData> directoryDatas = new ArrayList<ServiceDirectoryListData>();
    private ArrayList<ServiceDirectoryListData> filterList ;
    String flag ; // 0-Directroy 1-globalsearch ----- To hide the contents

    public DistrictCommiteeListAdapter(Context mContext, ArrayList<ServiceDirectoryListData> directoryDatas, String inputflag) {

        this.mContext = mContext;
        this.directoryDatas = directoryDatas;
        Log.d("directoryDatas",directoryDatas.toString());
        this.filterList = new ArrayList<>();
        filterList.addAll(directoryDatas);
        this.flag=inputflag;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  v = LayoutInflater.from(mContext).inflate(R.layout.directory_commitee_list_item,parent,false);
        DistrictCommitteeHolder holder = new DistrictCommitteeHolder(v);
        Log.d("♦♦♦♦ ","Inside View_TYPE_ROTARIAN_LIST ");
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        DistrictCommitteeHolder hol = (DistrictCommitteeHolder) holder;
        hol.getTvMemberName().setText(directoryDatas.get(position).getMemberName());

        hol.getTvDesignation().setText(directoryDatas.get(position).getKeywords());


        if (directoryDatas.get(position).getImage().trim().length() == 0 || directoryDatas.get(position).getImage().equals("") || directoryDatas.get(position).getImage() == null || directoryDatas.get(position).getImage().isEmpty()) {
            hol.getIvPic().setImageResource(R.drawable.profile_pic);
        } else {
            Picasso.with(mContext).load(directoryDatas.get(position).getImage()).transform(new CircleTransform())
                    .placeholder(R.drawable.profile_pic)
                    .into(hol.getIvPic());
        }

        hol.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ServiceDirectoryDetail.class);
                intent.putExtra("memberProfileId", directoryDatas.get(position).getServiceDirId());
                intent.putExtra("serviceDirId", directoryDatas.get(position).getServiceDirId());
                intent.putExtra("memberData",directoryDatas.get(position));
                ((Activity) mContext).startActivityForResult(intent, 1);
            }
        });
    }


    @Override
    public int getItemCount() {
        return directoryDatas.size();
    }

    // Filter Class
    public void filter(String charText) {
        charText=charText.toLowerCase();
        directoryDatas.clear();
        if (charText.length() == 0) {
            directoryDatas.addAll(filterList);
        }
        else
        {
            for (ServiceDirectoryListData wp : filterList)
            {
                Utils.log(wp.getMemberName());
                if (wp.getMemberName().toLowerCase().contains(charText))
                {
                    directoryDatas.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }



   }

