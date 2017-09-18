package com.SampleApp.row;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.SampleApp.row.Utils.PreferenceManager;

/**
 * Created by admin on 22-05-2017.
 */

public class RotaryLibraryDescription extends Activity {
    private Context context;
    public TextView tv_title,tv_libraryName;
    private ImageView iv_backbutton;
    private WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rotarylibrary_description);
        context = this;
        actionbarfunction();
        init();

        Intent i = getIntent();
        if(i.hasExtra("title")){
            String name = i.getStringExtra("title");
            tv_libraryName.setText(name);
        }
        if(i.hasExtra("description")){
            String description = i.getStringExtra("description");
            webview.getSettings().setJavaScriptEnabled(true);
            webview.loadData(description,"text/html","utf-8");
        }
    }

    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText("Rotary Library");
    }

    private void init() {
        tv_libraryName = (TextView)findViewById(R.id.tv_libraryname);
        webview = (WebView)findViewById(R.id.webview);
    }

}
