package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NEWROW.row.R;


/**
 * Created by user on 22-12-2016.
 */
public class GalleryListHolder extends RecyclerView.ViewHolder {

    public TextView tv_tile;
    public TextView tv_description;
    public TextView tv_date;
    public ImageView image,iv_delete, move;
    public Button ontimep;
    public LinearLayout modert;
    public LinearLayout linear;

    public TextView mdrt_text;

    public GalleryListHolder(View view){
        super(view);
        tv_tile = (TextView)view.findViewById(R.id.title);
        tv_description = (TextView)view.findViewById(R.id.description);
        tv_date = (TextView)view.findViewById(R.id.txt_date);
        image = (ImageView)view.findViewById(R.id.imageView);
        linear = (LinearLayout)view.findViewById(R.id.ll_holder);
        iv_delete = (ImageView)view.findViewById(R.id.iv_delete);
        move = (ImageView)view.findViewById(R.id.iv_delete);
        ontimep =  view.findViewById(R.id.ontimep);

        modert = view.findViewById(R.id.modert);
        mdrt_text = view.findViewById(R.id.mdrt_text);

    }

    public TextView getTitle() {
        return tv_tile;
    }

    public void setTitle(TextView tv_tile) {
        this.tv_tile = tv_tile;
    }

    public TextView getDescription() {
        return tv_description;
    }

    public void setDescription(TextView tv_description) {
        this.tv_description = tv_description;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

}
