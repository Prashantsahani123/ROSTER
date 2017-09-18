package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
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
public class OpenLinkActivity extends Activity {
    Context context;
    WebView webview;
    TextView tv_title;
    ImageView iv_backbutton;
    Button btn_close;
    public ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_webview);
        context = this;
        try {
            init();
            Intent i = getIntent();
            String link = i.getStringExtra("link");
            if ( !URLUtil.isValidUrl(link) ) {
                link = "http://"+link;
            }

            if ( ! URLUtil.isValidUrl(link)) {
                Toast.makeText(context, "Invalid url. Please contact admin", Toast.LENGTH_LONG).show();
                finish();
            }
            String modulename = i.getStringExtra("modulename");
            tv_title.setText(modulename);
            Log.d("External link is ", link);
            if (!link.equalsIgnoreCase("") && link != null) {
                if (InternetConnection.checkConnection(this)) {
                    webview.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.setMessage("Loading..Please wait.");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    webview.loadUrl(link);
                }else{
                    Toast.makeText(OpenLinkActivity.this,"Unable to load Page.No Internet Connection",Toast.LENGTH_LONG).show();
                    Log.e("OpenLinkActivity" , "♦♦♦♦Error is : "+ "Unable to load Page.No Internet Connection");
                }
            } else {
                Toast.makeText(OpenLinkActivity.this,"No External Link to open",Toast.LENGTH_LONG).show();
                Log.e("OpenLinkActivity" , "♦♦♦♦Error is : "+ "No External Link to open");
            }
        }catch(Exception e){
            Log.e("TouchBase", "♦♦♦♦Error is : "+e.getMessage());
            e.printStackTrace();
            Toast.makeText(OpenLinkActivity.this, "Unknown error. Please try again", Toast.LENGTH_LONG).show();
        }
    }
    public void init(){
        webview = (WebView)findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        btn_close = (Button) findViewById(R.id.btn_close);
        dialog = new ProgressDialog(OpenLinkActivity.this);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
