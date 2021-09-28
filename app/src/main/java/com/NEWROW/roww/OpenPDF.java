package com.NEWROW.row;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class OpenPDF extends AppCompatActivity {

    WebView webview;
    ProgressBar progressbar;
    private String fileName;
    private String filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_open_p_d_f );

        webview = (WebView)findViewById(R.id.webview);
      //  progressbar = (ProgressBar) findViewById(R.id.progressbar);
        webview.getSettings().setJavaScriptEnabled(true);

        Intent i = getIntent();
        fileName = i.getStringExtra("fileName");


      //  pdfView.setVisibility(View.GONE);
        //webview.setVisibility(View.VISIBLE);
        filePath = i.getStringExtra("filePath");
        String fname = "url=" + filePath;

        String doc = "<iframe src='http://docs.google.com/viewer?" + fname + "'" +
                " width='100%' height='100%' style='border: none;'></iframe>";

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSupportZoom(true);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading");
        String docUrl = "http://docs.google.com/gview?embedded=true&url=";

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                dialog.dismiss();
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                dialog.show();
                super.onPageStarted(view, url, favicon);
            }
        });

        webview.loadUrl(docUrl + fileName);




    }
}