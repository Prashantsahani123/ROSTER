package com.SampleApp.row.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.SampleApp.row.R;


/**
 * Created by USER1 on 16-03-2017.
 */
public class DocumentListItemHolder extends RecyclerView.ViewHolder{
    ImageView ivDelete, ivDownload, ivView;
    TextView tvTitle, tvDate;


    public DocumentListItemHolder(View itemView) {
        super(itemView);
        ivDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
        ivView = (ImageView) itemView.findViewById(R.id.iv_view);
        ivDownload = (ImageView) itemView.findViewById(R.id.iv_download);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        tvDate = (TextView) itemView.findViewById(R.id.tv_date);
    }

    public ImageView getIvDelete() {
        return ivDelete;
    }

    public ImageView getIvDownload() {
        return ivDownload;
    }

    public ImageView getIvView() {
        return ivView;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public TextView getTvDate() {
        return tvDate;
    }
}
