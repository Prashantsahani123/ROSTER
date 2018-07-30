package com.SampleApp.row.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.SampleApp.row.R;

/**
 * Created by admin on 01-06-2017.
 */

public class PhotoHolder extends RecyclerView.ViewHolder {

    ImageView image, ivIcon;
   public ProgressBar img_prg;

    public PhotoHolder(View v) {
        super(v);
        image = (ImageView)v.findViewById(R.id.image);
        ivIcon = (ImageView) v.findViewById(R.id.ivChangePic);
        img_prg=(ProgressBar)v.findViewById(R.id.img_prg);
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
