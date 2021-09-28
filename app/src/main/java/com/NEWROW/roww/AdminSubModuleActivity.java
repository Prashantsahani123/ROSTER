package com.NEWROW.row;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.NEWROW.row.Utils.InternetConnection;

import java.net.URISyntaxException;

public class AdminSubModuleActivity extends Activity {
    Context context;
    WebView webview;
    TextView tv_title;
    ImageView iv_backbutton;
    Button btn_close;
    public ProgressDialog dialog;

    // for file chooser
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_webview);

        context = this;

        try {

            init();

            Intent i = getIntent();

            String link = i.getStringExtra("link");

            if (!URLUtil.isValidUrl(link)) {
                link = "http://"+link;
            }

            if ( ! URLUtil.isValidUrl(link)) {
                Toast.makeText(context, "Invalid url. Please contact admin", Toast.LENGTH_LONG).show();
                finish();
            }

            String modulename = i.getStringExtra("modulename");

            tv_title.setText(modulename);

            Log.d("External link is ", link);

            if (!link.equalsIgnoreCase("")) {

                if (InternetConnection.checkConnection(this)) {

                    webview.setWebChromeClient(new WebChromeClient()
                    {
                        // For 3.0+ Devices (Start)
                        // onActivityResult attached before constructor
                        protected void openFileChooser(ValueCallback uploadMsg, String acceptType)
                        {
                            mUploadMessage = uploadMsg;
                            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                            i.addCategory(Intent.CATEGORY_OPENABLE);
                            i.setType("image/*");
                            startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
                        }

                        // For Lollipop 5.0+ Devices
                        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
                        {
                            if (uploadMessage != null) {
                                uploadMessage.onReceiveValue(null);
                                uploadMessage = null;
                            }

                            uploadMessage = filePathCallback;

                            Intent intent = fileChooserParams.createIntent();

                            try
                            {
                                startActivityForResult(intent, REQUEST_SELECT_FILE);
                            } catch (ActivityNotFoundException e)
                            {
                                uploadMessage = null;
                                Toast.makeText(AdminSubModuleActivity.this, "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                                return false;
                            }

                            return true;
                        }

                        //For Android 4.1 only
                        protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
                        {
                            mUploadMessage = uploadMsg;
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
                        }

                        protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
                            mUploadMessage = uploadMsg;
                            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                            i.addCategory(Intent.CATEGORY_OPENABLE);
                            i.setType("image/*");
                            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
                        }
                    });

                    /*webview.setDownloadListener(new DownloadListener() {

                        @Override
                        public void onDownloadStart(String url, String userAgent, String contentDescription, String mimetype, long contentLength) {

                            *//*Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);*//*

                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!

                            *//*String guessFileName (String url, String contentDisposition, String mimeType)
                                Guesses canonical filename that a download would have, using the URL
                                and contentDisposition. File extension, if not defined,
                                is added based on the mimetype
                            Parameters
                                url String : Url to the content
                                contentDisposition String : Content-Disposition HTTP header or null
                                mimeType String : Mime-type of the content or null
                            Returns
                                String : suggested filename*//*

                            String fileName = URLUtil.guessFileName(url,contentDescription,mimetype);

                            Log.e("satish","onDownloadStart file name =>"+fileName);

                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                            dm.enqueue(request);

                            Toast.makeText(getApplicationContext(), "Downloading File "+fileName, Toast.LENGTH_LONG).show();
                        }
                    });*/

                    webview.setWebViewClient(new WebViewClient() {

                        @Override
                        public void onPageFinished(WebView view, String url) {

                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }

                        @TargetApi(Build.VERSION_CODES.N)
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                            //return super.shouldOverrideUrlLoading(view, request);
                            String url = request.getUrl().toString();
                            Log.i("External url is new ", url);
                            return  overrideUrlLoading(url);
                        }

                        @SuppressWarnings("deprecation")
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            //return super.shouldOverrideUrlLoading(view, url);
                            Log.d("External url is old ", url);
                            return  overrideUrlLoading(url);
                        }

                    });

                    dialog.setMessage("Loading..Please wait.");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    webview.loadUrl(link);

                } else {
                    Toast.makeText(AdminSubModuleActivity.this,"Unable to load Page.No Internet Connection",Toast.LENGTH_LONG).show();
                    Log.e("OpenLinkActivity" , "♦♦♦♦Error is : "+ "Unable to load Page.No Internet Connection");
                }

            } else {
                Toast.makeText(AdminSubModuleActivity.this,"No External Link to open",Toast.LENGTH_LONG).show();
                Log.e("OpenLinkActivity" , "♦♦♦♦Error is : "+ "No External Link to open");
            }

        } catch(Exception e){
            Log.e("TouchBase", "♦♦♦♦Error is : "+e.getMessage());
            e.printStackTrace();
            Toast.makeText(AdminSubModuleActivity.this, "Unknown error. Please try again", Toast.LENGTH_LONG).show();
        }
    }

    private boolean overrideUrlLoading(String url){

        if (url.startsWith("http") || url.startsWith("https")) {
            return false;
        }

        if (url.startsWith("intent")) {

            try {

                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                String fallbackUrl = intent.getStringExtra("browser_fallback_url");

                if (fallbackUrl != null) {
                    webview.loadUrl(fallbackUrl);
                    return true;
                }

            } catch (URISyntaxException e) {
                //not an intent uri
            }
        }

        return true;//do nothing in other cases
    }

    public void init(){

        webview = (WebView)findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setAllowContentAccess(true);
        webview.getSettings().setSupportZoom(false);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        btn_close = (Button) findViewById(R.id.btn_close);

        dialog = new ProgressDialog(AdminSubModuleActivity.this);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    // Return here when file selected from camera or from SDCard
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if (requestCode == REQUEST_SELECT_FILE)
            {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        }
        else if (requestCode == FILECHOOSER_RESULTCODE)
        {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != MainActivity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
        else
            Toast.makeText(AdminSubModuleActivity.this, "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }
}