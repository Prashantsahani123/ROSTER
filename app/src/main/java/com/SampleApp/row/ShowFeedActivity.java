package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Utils.InternetConnection;

/**
 * Created by user on 01-02-2017.
 */
public class ShowFeedActivity extends Activity {
    WebView webview;
    TextView tv_title;
    ImageView iv_backbutton;
    Button btn_close;
    //public ProgressDialog dialog;
    Context context;
    String link, moduleName, description;

    //i.putExtra("link", data.getLink());
    //           i.putExtra("modulename", data.getTitle());
    //            i.putExtra("description", data.getDescription());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_show_feed);
        context = this;
        try {
            init();
            Intent i = getIntent();
            link = i.getStringExtra("link");
            moduleName = i.getStringExtra("modulename");
            description = i.getStringExtra("description");
            tv_title.setText(moduleName);
            webview.getSettings().setJavaScriptEnabled(true);


            webview.loadData(description,"text/html","utf-8");
            //webview.setText(Html.fromHtml(description));
            Log.d("External link is ", link);

        }catch(Exception e){
            Log.e("TouchBase", "♦♦♦♦Error is : "+e.getMessage());
            e.printStackTrace();
            Toast.makeText(context, "Unknown error. Please try again", Toast.LENGTH_LONG).show();
        }
    }



    public void init(){
        webview = (WebView) findViewById(R.id.webview);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        btn_close = (Button) findViewById(R.id.btn_close);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, OpenLinkActivity.class);
                i.putExtra("link", link);
                i.putExtra("modulename", "Rotary Updates");
                context.startActivity(i);
            }
        });
    }
}
