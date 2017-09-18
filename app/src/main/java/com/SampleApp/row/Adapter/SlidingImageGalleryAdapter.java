package com.SampleApp.row.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.SampleApp.row.ImageZoom;
import com.SampleApp.row.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.SampleApp.row.Data.AlbumPhotoData;

/**
 * Created by SONU on 29/08/15.
 */
public class SlidingImageGalleryAdapter extends PagerAdapter {
    //private ImageView currentImageView;

    private ArrayList<AlbumPhotoData> IMAGES;
    private LayoutInflater inflater;
    private Context context;


    public SlidingImageGalleryAdapter(Context context, ArrayList<AlbumPhotoData> IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);



    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.gallery_image_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);
        //currentImageView = imageView;
        final AlbumPhotoData data = IMAGES.get(position);
        try {
            Picasso.with(context).load(data.getUrl())
                    .placeholder(R.drawable.dashboardplaceholder)
                    .into(imageView);
            view.addView(imageLayout, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageZoom.class);
                intent.putExtra("imgageurl", data.getUrl());
                context.startActivity(intent);
            }
        });
        return imageLayout;
    }

    /*public ImageView getCurrentImageView() {
        return currentImageView;
    }*/

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
