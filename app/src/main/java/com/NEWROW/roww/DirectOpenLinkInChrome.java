package com.NEWROW.row;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import com.NEWROW.row.Utils.InternetConnection;

public class DirectOpenLinkInChrome extends AppCompatActivity {

    WebView webviewdirect;
    Context context;
    String directopenchromelink = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_open_link_in_chrome);
        context = DirectOpenLinkInChrome.this;

        webviewdirect = findViewById(R.id.webviewdirect);
        Intent i = getIntent();
        directopenchromelink = i.getExtras().getString("directopenchromelink", " ");

        if (!directopenchromelink.equals(" ")) {

            // directopenchrome(directopenchromelink);

            if (InternetConnection.checkConnection(this)) {

                webviewdirect.getSettings().setJavaScriptEnabled(true);

                webviewdirect.loadUrl(directopenchromelink);
            } else {
                Toast.makeText(DirectOpenLinkInChrome.this, "Please Check your Internet Connetion!", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(DirectOpenLinkInChrome.this, "Link isn't avialable Currently", Toast.LENGTH_SHORT).show();
        }


    }

    private void directopenchrome(String url) {
        if (InternetConnection.checkConnection(this)) {


            webviewdirect.loadUrl(url);
        } else {
            Toast.makeText(DirectOpenLinkInChrome.this, "Please Check your Internet Connetion!", Toast.LENGTH_SHORT).show();
        }

    }

   /* @Override
    public void onBackPressed() {
      //  super.onBackPressed();
        Intent intent = new Intent(DirectOpenLinkInChrome.this,DashboardActivity.class);
        startActivity(intent);
        finish();
    }*/
}
