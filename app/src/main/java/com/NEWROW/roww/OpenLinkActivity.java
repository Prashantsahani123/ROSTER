package com.NEWROW.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.print.PdfPrint;
import android.print.PrintAttributes;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.ScreenshotUtils;
import com.NEWROW.row.Utils.Utils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private ImageView iv_share;
    private LinearLayout parent_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_webview);

        context = OpenLinkActivity.this;

        try {

            init();

            Intent i = getIntent();

            String link = i.getStringExtra("link");


            if (!URLUtil.isValidUrl(link)) {
                link = "http://" + link;
            }

            if (!URLUtil.isValidUrl(link)) {
                Toast.makeText(context, "Invalid url. Please contact admin", Toast.LENGTH_LONG).show();
                finish();
            }

            String modulename = i.getStringExtra("modulename");

            tv_title.setText(modulename);

            Log.d("External link is ", link);

            if (i.hasExtra("isSharing")) {

                boolean isSharing = i.getBooleanExtra("isSharing", false);

                if (isSharing) {
                    iv_share.setVisibility(View.VISIBLE);
                } else {
                    iv_share.setVisibility(View.GONE);
                }
            }

            if (!link.equalsIgnoreCase("") && link != null) {

                if (InternetConnection.checkConnection(this)) {

                    webview.setWebViewClient(new WebViewClient() {

                        @Override
                        public void onPageFinished(WebView view, String url) {

                            if (dialog != null)
                                if (dialog.isShowing())
                                    dialog.dismiss();
                        }




                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                            //return super.shouldOverrideUrlLoading(view, request);
                            String url = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                url = request.getUrl().toString();
                            }
                            Log.i("External url is new ", url);
                            return overrideUrlLoading(url);
                        }

                        @SuppressWarnings("deprecation")
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            //return super.shouldOverrideUrlLoading(view, url);
                            Log.d("External url is old ", url);
                            return overrideUrlLoading(url);
                        }

                    });

                    dialog.setMessage("Loading..Please wait.");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    webview.loadUrl(link);

                } else {
                    Toast.makeText(OpenLinkActivity.this, "Unable to load Page.No Internet Connection", Toast.LENGTH_LONG).show();
                    Log.e("OpenLinkActivity", "♦♦♦♦Error is : " + "Unable to load Page.No Internet Connection");
                }

            } else {
                Toast.makeText(OpenLinkActivity.this, "No External Link to open", Toast.LENGTH_LONG).show();
                Log.e("OpenLinkActivity", "♦♦♦♦Error is : " + "No External Link to open");
            }


        } catch (Exception e) {
            Log.e("TouchBase", "♦♦♦♦Error is : " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(OpenLinkActivity.this, "Unknown error. Please try again", Toast.LENGTH_LONG).show();
        }


        iv_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

//                   createDynamicLink();

                   /*webview.scrollTo(0,1000000000);

                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           webview.scrollTo(0,0);
                           convertWebViewToPdf();
                       }
                   },2000);*/

                convertWebViewToPdf();
            }
        });

    }


    private boolean overrideUrlLoading(String url) {

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


    public void init() {
        parent_layout = (LinearLayout) findViewById(R.id.parent_layout);
        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        //Issue for Load URL Solved by Gaurav on 05Nov2020
        webview.getSettings().setDomStorageEnabled(true);
        //Closed
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_share = (ImageView) findViewById(R.id.iv_share);
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

    private void createDynamicLink(Uri shareUri) {

        // Uri shareUri = takeScreenshot();


        if (shareUri != null) {

            Intent shareIntent = new Intent();

            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "");
            shareIntent.putExtra(Intent.EXTRA_STREAM, shareUri);

            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            shareIntent.setType("application/pdf");//("image/jpeg");

            startActivity(shareIntent);

        } else {
            Utils.showToastWithTitleAndContext(OpenLinkActivity.this, getString(R.string.msgRetry));
        }
    }

    private void convertWebViewToPdf() {

        String jobName = getString(R.string.app_name) + " Document";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            PrintAttributes attributes = new PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 600, 600))
                    .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build();


            PdfPrint pdfPrint = new PdfPrint(attributes);

            //File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/PDFTest/");

            File saveFilePath = ScreenshotUtils.getMainDirectoryName(OpenLinkActivity.this);

            File dir = new File(saveFilePath.getAbsolutePath());

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String today = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());

            final String fileName = "Rotary_News_" + today + ".pdf";

            final ProgressDialog progressDialog = new ProgressDialog(OpenLinkActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.show();

            pdfPrint.print(webview.createPrintDocumentAdapter(jobName), dir, fileName, new PdfPrint.CallbackPrint() {

                @Override
                public void success(File file) {

                    progressDialog.dismiss();

                    try {

                        // this is because some contain get zoomed after share button click
                        webview.setInitialScale(0);
                        webview.getSettings().setLoadWithOverviewMode(true);
                        webview.getSettings().setUseWideViewPort(true);

                        File updateFile = addHeaderFooter(file.getAbsolutePath());
                        //     createDynamicLink(FileProvider.getUriForFile(context, "com.SampleApp.row.fileprovider", file));
                        createDynamicLink(FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", updateFile));

                        webview.getSettings().setLoadWithOverviewMode(false);
                        webview.getSettings().setUseWideViewPort(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure() {
                    progressDialog.dismiss();
                }

            });

        }
    }


    private File addHeaderFooter(String src) throws Exception {

        File saveFilePath = ScreenshotUtils.getMainDirectoryName(OpenLinkActivity.this);

        File dir = new File(saveFilePath.getAbsolutePath());

        if (!dir.exists()) {
            dir.mkdirs();
        }

        String today = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());

        String fileName = "Rotary_India_" + today + ".pdf";

        File dFile = new File(saveFilePath, fileName);

        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dFile.getAbsolutePath()));

        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12);

        font.setColor(new BaseColor(0, 174, 239));

        Phrase header = new Phrase("Create by Rotary India", font);

        //************** header start ***********
       /* Drawable dh = getResources().getDrawable(R.drawable.library_head_new);
        BitmapDrawable bitDwh = ((BitmapDrawable) dh);
        Bitmap bmph = bitDwh.getBitmap();
        ByteArrayOutputStream streamh = new ByteArrayOutputStream();
        bmph.compress(Bitmap.CompressFormat.PNG, 100, streamh);
        Image imageh = Image.getInstance(streamh.toByteArray());

        imageh.scaleAbsoluteHeight(50);

      //  imageh.scaleAbsoluteWidth((imageh.getWidth() * 50) / imageh.getHeight());

        int first = 1;

        imageh.scaleAbsoluteWidth(reader.getPageSize(first).getWidth());

        float xh = reader.getPageSize(first).getLeft();//getRight(50);
        float yh = reader.getPageSize(first).getTop(50);

        PdfContentByte contenth = stamper.getUnderContent(first);
        imageh.setAbsolutePosition(xh, yh);
        contenth.addImage(imageh);*/

        //************** header end ***********

        //************** footer start ***********
        Drawable d = getResources().getDrawable(R.drawable.lib_footer_new);
        BitmapDrawable bitDw = ((BitmapDrawable) d);
        Bitmap bmp = bitDw.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image image = Image.getInstance(stream.toByteArray());

        image.scaleAbsoluteHeight(50);
        // image.scaleAbsoluteWidth((image.getWidth() * 50) / image.getHeight());

        int last = reader.getNumberOfPages();

        image.scaleAbsoluteWidth(reader.getPageSize(last).getWidth());

        float x = reader.getPageSize(last).getLeft();//getRight(50);
        float y = reader.getPageSize(last).getBottom(10);

        PdfContentByte content = stamper.getOverContent(last);
        image.setAbsolutePosition(x, y);
        content.addImage(image);

        //************** footer end ***********

        /*  for (int i = 1; i <= reader.getNumberOfPages(); i++) {

         *//*float x = reader.getPageSize(i).getRight(50);
            float y = reader.getPageSize(i).getBottom(10);
            ColumnText.showTextAligned(
                    stamper.getOverContent(i), Element.ALIGN_CENTER,
                    header, x, y, 0);*//*

         *//*image.scaleAbsoluteWidth(reader.getPageSize(i).getWidth());

            float x = reader.getPageSize(i).getLeft();//getRight(50);
            float y = reader.getPageSize(i).getBottom(10);

            PdfContentByte content = stamper.getOverContent(i);//getUnderContent(i);
            image.setAbsolutePosition(x, y);
            content.addImage(image);*//*
        }*/

        stamper.close();
        reader.close();

        return dFile;
    }


}
