package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by user on 04-11-2016.
 */
public class SimplePhotoHolder extends RecyclerView.ViewHolder{

    ImageView imageSimplePhoto;

    public SimplePhotoHolder(View itemView) {
        super(itemView);
        this.imageSimplePhoto = (ImageView) itemView.findViewById(com.NEWROW.row.R.id.imageSimplePhoto);
    }

    public ImageView getImageSimplePhoto() {
        return imageSimplePhoto;
    }

    public void setImageSimplePhoto(ImageView imageSimplePhoto) {
        this.imageSimplePhoto = imageSimplePhoto;
    }

}
