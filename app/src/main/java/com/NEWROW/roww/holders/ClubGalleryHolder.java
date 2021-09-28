package com.NEWROW.row.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NEWROW.row.R;

/**
 * Created by admin on 04-08-2017.
 */

public class ClubGalleryHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView tvAlbumTitle, tvDescription;
    public TextView tv_date;
    public LinearLayout linear;

    public ClubGalleryHolder(View itemView) {
        super(itemView);
        try {
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            tvAlbumTitle = (TextView) itemView.findViewById(R.id.title);
            tvDescription = (TextView) itemView.findViewById(R.id.description);
            tv_date = (TextView)itemView.findViewById(R.id.txt_date);
            linear = (LinearLayout)itemView.findViewById(R.id.ll_holder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getTvAlbumTitle() {
        return tvAlbumTitle;
    }

    public void setTvAlbumTitle(TextView tvAlbumTitle) {
        this.tvAlbumTitle = tvAlbumTitle;
    }

    public TextView getTvDescription() {
        return tvDescription;
    }

    public void setTvDescription(TextView tvDescription) {
        this.tvDescription = tvDescription;
    }
}
