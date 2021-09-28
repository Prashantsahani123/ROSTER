package com.NEWROW.row;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Utils.InternetConnection;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class ViewMediaPhoto extends AppCompatActivity {
    String imgURL;
    ImageView iv_mediaimg, iv_share;
    TextView mediatitle, tv_title;
    public Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_media_photo);
        context = ViewMediaPhoto.this;
        iv_mediaimg = findViewById(R.id.iv_mediaimg);
        iv_mediaimg.setDrawingCacheEnabled(true);

        mediatitle = findViewById(R.id.mediatitle);
        tv_title = findViewById(R.id.tv_title);
        iv_share = findViewById(R.id.iv_share);
        tv_title.setText("Media Photo");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imgURL = getIntent().getStringExtra("imgURL");
            String description = getIntent().getStringExtra("description");

            if (InternetConnection.checkConnection(ViewMediaPhoto.this)) {
                Picasso.with(ViewMediaPhoto.this).load(imgURL)
                        //.fit()
                        //.resize(200, 200)
                        .placeholder(R.drawable.placeholder_new)
                        .into(iv_mediaimg);
                iv_mediaimg.setBackground(null);

                //title
                if (!description.equalsIgnoreCase("")){
                    mediatitle.setVisibility(View.VISIBLE);
                    mediatitle.setText(description);
                }else{
                    mediatitle.setVisibility(View.GONE);
                }


                iv_share.setVisibility(View.VISIBLE);

            } else {
                Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }

        //share Image

        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();
            }
        });


    }

    public void shareImage() {

        try {

            Picasso.with(context)
                    .load(imgURL)
                    .placeholder(R.drawable.imageplaceholder)
                    .into(iv_mediaimg);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //Bitmap bitmap = ivTemp.getDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) iv_mediaimg.getDrawable()).getBitmap();

        File filesDir = getFilesDir();

        File imageDirectory = new File(filesDir, "ROW");

        if (!imageDirectory.exists()) {
            imageDirectory.mkdirs();
        }

        Date d = new Date();
        String image = "image_" + d.getTime() + ".jpg";
        File imageFile = new File(imageDirectory, image);

        //imageFile
        try {

            FileOutputStream fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);

            fout.close();

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            Uri uri = FileProvider.getUriForFile(ViewMediaPhoto.this, "com.SampleApp.row.fileprovider", imageFile);
            //shareIntent.putExtra(Intent.EXTRA_STREAM, );
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/jpg");
            startActivity(shareIntent);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ActivityNotFoundException anfe) {
            anfe.printStackTrace();
        }
    }


}
