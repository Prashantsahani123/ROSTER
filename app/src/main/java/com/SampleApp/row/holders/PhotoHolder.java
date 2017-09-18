package com.SampleApp.row.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.SampleApp.row.R;

/**
 * Created by admin on 01-06-2017.
 */

public class PhotoHolder extends RecyclerView.ViewHolder {

    ImageView image, ivIcon;

    public PhotoHolder(View v) {
        super(v);
        image = (ImageView)v.findViewById(R.id.image);
        ivIcon = (ImageView) v.findViewById(R.id.ivChangePic);
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public ImageView getIvIcon() {
        return ivIcon;
    }

    public void setIvIcon(ImageView ivIcon) {
        this.ivIcon = ivIcon;
    }
}
