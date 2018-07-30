package com.SampleApp.row;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Utils.Constant;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by user on 15-03-2016.
 */
public class ImageZoom extends Activity {

    ImageView iv;
    private String imgageurl;
    private ProgressBar progressbar;
    private TextView tv_title;
    private ImageView iv_backbutton;
    private ImageView iv_actionbtn;
    private String mode = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image_zoom);

        iv = (ImageView) findViewById(R.id.image);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        Bundle intent = getIntent().getExtras();

        if (intent != null) {

            imgageurl = intent.getString("imgageurl"); // Created Group ID

            Log.e("Touchbase", "♦♦♦♦Url : " + imgageurl);

            if ( intent.containsKey("mode") ) {
                mode = intent.getString("mode");
            }
        }


        actionbarfunction();

        progressbar.setVisibility(View.VISIBLE);
        Picasso.with(ImageZoom.this).load(imgageurl)
                .placeholder(R.drawable.imageplaceholder)
                .into(iv, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressbar.setVisibility(View.GONE);
                        if (mode.equals(Constant.MODE_VIEW)) {
                            String tempUrl = imgageurl.replaceAll("file://", "");

                            File file = new File(tempUrl);
                            boolean deleted = file.delete();
                            Log.e("Touchabse", "Image : "+tempUrl+" deleted : " + deleted);
                        }
                    }

                    @Override
                    public void onError() {
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(ImageZoom.this, "Failed to load image", Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        tv_title.setText("");
        if ( getIntent().getExtras().containsKey("title") ) {
            String title = getIntent().getExtras().getString("title");
            tv_title.setText(title);
        }
      //  iv_actionbtn.setVisibility(View.VISIBLE);



    }
}
