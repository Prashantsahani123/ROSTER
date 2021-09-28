package com.NEWROW.row;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by admin on 27-04-2017.
 */

public class WebLinkDescription extends Activity {
    public TextView tv_weblinkTitle,tv_url,tv_description,tv_title;
    String webtitle,url,description,webLinkId;
    ImageView iv_backbutton;
    private WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weblink_description);

        init();

        actionbarfunction();

        Intent i = getIntent();

        if(i.hasExtra("title")){
            webtitle = i.getStringExtra("title");
            tv_weblinkTitle.setText(webtitle);
        }if(i.hasExtra("description")){
            description = i.getStringExtra("description");
            webview.getSettings().setJavaScriptEnabled(true);
            webview.loadData(description,"text/html","utf-8");
        }

        if(i.hasExtra("linkUrl")){
            url = i.getStringExtra("linkUrl");
            tv_url.setText(url);
        }

        if(i.hasExtra("webLinkId")){
            webLinkId = i.getStringExtra("webLinkId");
        }

    }

    private void init(){

        tv_weblinkTitle = (TextView)findViewById(R.id.tv_linktitle);
        tv_url = (TextView)findViewById(R.id.tv_url);
        //tv_description = (TextView)findViewById(R.id.tv_description);
        webview = (WebView)findViewById(R.id.webview);

        tv_url.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(WebLinkDescription.this, OpenLinkActivity.class);
                i.putExtra("link", url);
                i.putExtra("modulename", "Web Links");
                startActivity(i);
            }
        });
    }

    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText("Web Links");
    }
}
