package com.SampleApp.row.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.SampleApp.row.Data.RotarianData;
import com.SampleApp.row.R;
import com.SampleApp.row.RotarianBusinessDetails_ProfileActivity;
import com.SampleApp.row.Utils.CircleTransform;
import com.SampleApp.row.holders.EmptyViewHolder;
import com.SampleApp.row.holders.RotarianListHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 25-04-2017.
 */

public class RotarianListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    ArrayList<RotarianData> list;
    private static final int View_TYPE_EMPTYLIST = 1 ;
    private static final int View_TYPE_ROTARIAN_LIST = 2 ;

    public RotarianListAdapter(Context context, ArrayList<RotarianData> list){
        this.context = context;
        this.list = list;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==View_TYPE_EMPTYLIST){
            View v = LayoutInflater.from(context).inflate(R.layout.empty_calendar_recyclerview,parent,false);
            Log.d("♦♦♦♦ ","Inside View_TYPE_EMPTYLIST ");
            return new EmptyViewHolder(v);

        }
        else if ( viewType == View_TYPE_ROTARIAN_LIST ) {
            View  v = LayoutInflater.from(context).inflate(R.layout.holder_rotarian_list,parent,false);
            RotarianListHolder holder = new RotarianListHolder(v);
            Log.d("♦♦♦♦ ","Inside View_TYPE_ROTARIAN_LIST ");
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
         ((EmptyViewHolder) holder).getEmptyView().setText("No Results");
    }

    public void bindNonEmptyView(RecyclerView.ViewHolder holder, final int position) {

        RotarianListHolder hol = (RotarianListHolder) holder;
        hol.tv_name.setText(list.get(position).getMemberName());
        hol.tv_designation.setText(list.get(position).getDesignation());
        hol.tv_clubname.setText(list.get(position).getClubName());

        if (list.get(position).getPic().trim().length() == 0 || list.get(position).getPic().equals("") || list.get(position).getPic() == null || list.get(position).getPic().isEmpty()) {
            hol.iv_image.setImageResource(R.drawable.profile_pic);
        } else {
            Picasso.with(context).load(list.get(position).getPic().replaceAll(" ", "%20")).transform(new CircleTransform())
                    .placeholder(R.drawable.profile_pic)
                    .into(hol.iv_image);
        }

        hol.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, RotarianBusinessDetails_ProfileActivity.class);
                i.putExtra("memberProfileId",list.get(position).getProfileId());
                i.putExtra("clubname",list.get(position).getClubName());
                context.startActivity(i);
            }
        });





//        hol.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(context,ServiceDirectoryList.class);
//                i.putExtra("categoryId",String.valueOf(list.get(position).getCategoryId()));
//                i.putExtra("moduleName",modulename);
//                context.startActivity(i);
//                Log.e("categoryId" , "categoryId : "+String.valueOf(list.get(position).getCategoryId()));
//            }
//        });

    }

    @Override
    public int getItemViewType(int position) {
        if(list.size()==0){
            return View_TYPE_EMPTYLIST;
        }else{
            return View_TYPE_ROTARIAN_LIST;
        }
    }

    @Override
    public int getItemCount() {
        if(list.size()==0){
            return  View_TYPE_EMPTYLIST;
        }
        return list.size();
    }
}
