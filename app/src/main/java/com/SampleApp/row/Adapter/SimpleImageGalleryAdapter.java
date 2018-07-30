package com.SampleApp.row.Adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.SampleApp.row.Data.SimplePhotoData;
import com.SampleApp.row.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by SONU on 29/08/15.
 */
public class SimpleImageGalleryAdapter extends PagerAdapter {


    private ArrayList<Object> imagesList;
    private LayoutInflater inflater;
    private Context context;


    public SimpleImageGalleryAdapter(Context context, ArrayList<Object> imagesList) {
        this.context = context;
        this.imagesList = imagesList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.simple_gallery_image_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);

        SimplePhotoData data = (SimplePhotoData) imagesList.get(position);
        Picasso.with(context).load(new File(data.getUrl()))
                .placeholder(R.drawable.dashboardplaceholder)
                .into(imageView);
        view.addView(imageLayout, 0);
        return imageLayout;
    }

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

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
