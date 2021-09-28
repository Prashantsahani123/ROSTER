package com.NEWROW.row.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.NEWROW.row.Data.ClubMemberData;
import com.NEWROW.row.R;
import com.NEWROW.row.Utils.CircleTransform;
import com.NEWROW.row.holders.ClubMemberHolder;
import com.NEWROW.row.holders.EmptyViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 25-04-2017.
 */

public class ClubMemberRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    ArrayList<ClubMemberData> list;
    private static final int VIEW_TYPE_EMPTY = 1 ;
    private static final int VIEW_TYPE_NON_EMPTY = 2 ;

    public ClubMemberRVAdapter(Context context, ArrayList<ClubMemberData> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType== VIEW_TYPE_EMPTY){

            View v = LayoutInflater.from(context).inflate(R.layout.empty_club_member_list,parent,false);
            Log.d("♦♦♦♦ ","Inside VIEW_TYPE_EMPTY ");
            return new EmptyViewHolder(v);

        } else if ( viewType == VIEW_TYPE_NON_EMPTY) {
            View  v = LayoutInflater.from(context).inflate(R.layout.holder_club_member_item,parent,false);
            ClubMemberHolder holder = new ClubMemberHolder(v);
            Log.d("♦♦♦♦ ","Inside VIEW_TYPE_NON_EMPTY ");
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == VIEW_TYPE_EMPTY) {
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
        ClubMemberHolder hol = (ClubMemberHolder) holder;
        hol.getTvMemberName().setText(list.get(position).getMemberName());

         //hol.getTvMobileNo().setText(list.get(position).getMembermobile());


         //hol.tv_designation.setText(list.get(position).getDesignation());
        //hol.tv_clubname.setText(list.get(position).getClubName());


        if (list.get(position).getPic().trim().length() == 0 || list.get(position).getPic().equals("") || list.get(position).getPic() == null || list.get(position).getPic().isEmpty()) {
            hol.getImageView().setImageResource(R.drawable.profile_pic);
        } else {
            list.get(position).setPic(list.get(position).getPic().replaceAll(" ", "%20"));
            Picasso.with(context).load(list.get(position).getPic()).transform(new CircleTransform())
                    .placeholder(R.drawable.profile_pic)
                    .into(hol.getImageView());
        }

        hol.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( onItemSelectedListener!=null) {
                    onItemSelectedListener.onItemSelected(position);
                }

            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if(list.size()==0){
            return VIEW_TYPE_EMPTY;
        }else{
            return VIEW_TYPE_NON_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if(list.size()==0){
            return VIEW_TYPE_EMPTY;
        }
        return list.size();
    }

    public OnItemSelectedListener getOnItemSelectedListener() {
        return onItemSelectedListener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    private OnItemSelectedListener onItemSelectedListener;

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

    public ArrayList<ClubMemberData> getItems() {
        return list;
    }

}
