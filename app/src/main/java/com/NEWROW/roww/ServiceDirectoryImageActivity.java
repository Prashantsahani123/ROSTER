package com.NEWROW.row;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by USER on 25-07-2016.
 */
public class ServiceDirectoryImageActivity extends Activity {

    ImageView iv_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ervice_directory_image_activity);

        iv_image = (ImageView)findViewById(R.id.iv_image);


        Intent i = getIntent();
        String serviceImage = i.getStringExtra("serviceImage");
        ((TextView)findViewById(R.id.tv_title)).setText("");

        Picasso.with(ServiceDirectoryImageActivity.this).load(serviceImage)
                .placeholder(R.drawable.imageplaceholder)
                .into(iv_image);

    }
}
