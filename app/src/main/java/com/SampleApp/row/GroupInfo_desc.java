package com.SampleApp.row;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by USER1 on 07-06-2016.
 */
public class GroupInfo_desc extends Activity {

    private WebView webView;
    TextView title;
    TextView tv_title,tv_group_name;
    ImageView iv_backbutton,iv_prof_pic;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_info_desc);

        title = (TextView)findViewById(R.id.tv_title);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        // iv_backbutton.setVisibility(View.GONE);
        //tv_title.setText("Description");


        Intent i = getIntent();
        String desc = i.getStringExtra("desc");
        title.setText(i.getStringExtra("title"));

        Log.d("==============","========="+desc);

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadData(desc,"text/html", "UTF-8");


    }


}