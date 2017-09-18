package com.SampleApp.row.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.SampleApp.row.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import com.SampleApp.row.Data.SimpleGalleryItemData;
import com.SampleApp.row.Data.SimplePhotoData;
import com.SampleApp.row.holders.SimplePhotoHolder;


/**
 * Created by user on 04-11-2016.
 */
public class SimpleMediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    View previousView;


    OnMediaSelectListener mediaSelectListener;

    public static int MEDIA_TYPE_IMAGE = 1, MEDIA_TYPE_VIDEO = 2, MEDIA_TYPE_ADD_BUTTON = 3;

    Context context;
    ArrayList<Object> list;


    public SimpleMediaAdapter(Context context, ArrayList<Object> list) {
        this.context = context;
        this.list = list;
    }


    public OnMediaSelectListener getOnMediaSelectListener() {
        return mediaSelectListener;
    }

    public void setOnMediaSelectListener(OnMediaSelectListener mediaSelectListener) {
        this.mediaSelectListener = mediaSelectListener;
    }

    public void removeOnMediaSelectListener() {
        this.mediaSelectListener = null;
    }


    @Override
    public int getItemViewType(int position) {
        Object obj = list.get(position);
        if ( obj instanceof SimplePhotoData ) {
            return MEDIA_TYPE_IMAGE;
        } else if ( obj instanceof AddButton ) {
            return MEDIA_TYPE_ADD_BUTTON;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if ( viewType == MEDIA_TYPE_IMAGE ) {
            View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE )).inflate(R.layout.simple_photo_layout, null);

            SimplePhotoHolder holder = new SimplePhotoHolder(view);

            return holder;
        } else if ( viewType == MEDIA_TYPE_ADD_BUTTON ) {

        } else if ( viewType == MEDIA_TYPE_VIDEO) {

        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final int tempPosition = position;
        int viewType = getItemViewType(position);

        if ( previousView == null ) {
            previousView = ((SimplePhotoHolder) holder).itemView;
            previousView.setBackgroundResource(R.color.bluecolor);
        }
        if ( viewType == MEDIA_TYPE_IMAGE ) {

            ((SimplePhotoHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    previousView.setBackgroundResource(R.color.transparent);
                        ((SimplePhotoHolder) holder).itemView.setBackgroundResource(R.color.bluecolor);
                    previousView = holder.itemView;

                    if ( mediaSelectListener != null) {
                        mediaSelectListener.onMediaSelected((SimplePhotoData) list.get(tempPosition), tempPosition);

                    }
                }
            });


            SimplePhotoData data = (SimplePhotoData) list.get(position);

            Picasso.with(context).load(new File(data.getUrl()))
                    .centerCrop()
                    .resize(200,200)
                    .placeholder(R.drawable.dashboardplaceholder)
                    .into(((SimplePhotoHolder) holder).getImageSimplePhoto());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class AddButton
    {

    }

    public interface OnMediaSelectListener {
        public void onMediaSelected(SimpleGalleryItemData spd, int position);


    }

    public interface OnMediaRemoveListener {
        public void onMediaDeleted(SimpleGalleryItemData spd, int position);


    }

    public void color(RecyclerView.ViewHolder v){
        if(v!= null) {
            previousView.setBackgroundResource(R.color.transparent);
            v.itemView.setBackgroundResource(R.color.transparent);
            v.itemView.setBackgroundResource(R.color.bluecolor);
            previousView = ((SimplePhotoHolder) v).itemView;
        }

    }
}
