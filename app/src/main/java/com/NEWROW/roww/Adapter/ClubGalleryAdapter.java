package com.NEWROW.row.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.NEWROW.row.Data.ClubGalleryData;
import com.NEWROW.row.R;
import com.NEWROW.row.Utils.Utils;
import com.NEWROW.row.holders.ClubGalleryHolder;
import com.NEWROW.row.holders.EmptyViewHolder;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by admin on 25-04-2017.
 */

public class ClubGalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    ArrayList<ClubGalleryData> list;
    private static final int VIEW_TYPE_EMPTY = 1 ;
    private static final int VIEW_TYPE_NON_EMPTY = 2 ;

    SimpleDateFormat oldSdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    SimpleDateFormat newSdf = new SimpleDateFormat("dd MMMM yyyy");

    public ClubGalleryAdapter(Context context, ArrayList<ClubGalleryData> list){
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
            View  v = LayoutInflater.from(context).inflate(R.layout.holder_club_gallery,parent,false);
            ClubGalleryHolder holder = new ClubGalleryHolder(v);
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
         ((EmptyViewHolder) holder).getEmptyView().setText("Please Wait...");
    }

    public void bindNonEmptyView(RecyclerView.ViewHolder holder, final int position) {
        ClubGalleryHolder hol = (ClubGalleryHolder) holder;
        hol.getTvAlbumTitle().setText(list.get(position).getTitle());
        hol.getTvDescription().setText(list.get(position).getDescription());
        //hol.tv_designation.setText(list.get(position).getDesignation());
        //hol.tv_clubname.setText(list.get(position).getClubName());

        Date date = null;

        try {
            date = oldSdf.parse(list.get(position).getProject_date());
            hol.tv_date.setText(newSdf.format(date));
        } catch (ParseException e) {
            hol.tv_date.setText(list.get(position).getProject_date());
            e.printStackTrace();
        }


        if (list.get(position).getImage().trim().length() == 0 || list.get(position).getImage().equals("") || list.get(position).getImage() == null || list.get(position).getImage().isEmpty()) {
            hol.getImageView().setImageResource(R.drawable.imageplaceholder);
        } else {
            try {
                Picasso.with(context).load(list.get(position).getImage())
                        .placeholder(R.drawable.imageplaceholder)
                        .into(hol.getImageView());
            } catch (Exception e) {
                Utils.log("Error : "+e);
                e.printStackTrace();
            }
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

    public ArrayList<ClubGalleryData> getItems() {
        return list;
    }

}
