package com.NEWROW.row;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.ScreenshotUtils;
import com.NEWROW.row.Utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.NEWROW.row.Utils.PreferenceManager.isRIadminModule;

/**
 * Created by user on 15-03-2016.
 */
public class ImageZoom extends Activity implements View.OnClickListener {

    ImageView iv;
    private String imgageurl,shareurl;
    private ProgressBar progressbar;
    private TextView tv_title;
    private ImageView iv_backbutton;
    private ImageView iv_actionbtn;
    private String mode = "";
    //Added By Gaurav
    ImageView iv_sharepdf;
    Uri senduri;
    File actualFile;
    boolean isFromNotification=false;
    RelativeLayout ll_root;

    // variable to track event time
    private long mLastClickTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image_zoom);

        iv = (ImageView) findViewById(R.id.image);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        ll_root = (RelativeLayout) findViewById(R.id.ll_root);

        Bundle intent = getIntent().getExtras();

        if (intent != null) {

            imgageurl = intent.getString("imgageurl"); // Created Group ID
            actualFile = new File(imgageurl);

            Log.e("Touchbase", "♦♦♦♦Url : " + imgageurl);

            if (intent.containsKey("mode")) {
                mode = intent.getString("mode");
            }
        }


        actionbarfunction();

        progressbar.setVisibility(View.VISIBLE);
        Picasso.with(ImageZoom.this).load(imgageurl)
                .placeholder(R.drawable.imageplaceholder)
                .resize(2048, 1600)
                .onlyScaleDown() // the image will only be resized if it's bigger than 2048x 1600 pixels.
                .into(iv, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressbar.setVisibility(View.GONE);
                        if (mode.equals(Constant.MODE_VIEW)) {

                            String tempUrl = imgageurl.replaceAll("file://", "");
                            shareurl=tempUrl;

                            File file = new File(tempUrl);
                            actualFile=file;
                            boolean deleted = file.delete();
                            Log.e("Touchabse", "Image : " + tempUrl + " deleted : " + deleted);
                        }
                    }

                    @Override
                    public void onError() {
                        progressbar.setVisibility(View.GONE);
                        //Toast.makeText(ImageZoom.this, "Failed to load image", Toast.LENGTH_LONG).show();
                    }
                });


      /*  iv_sharepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //shareFile();

                Uri shareUri = takeScreenshot();

                if(shareUri != null) {

                    Intent shareIntent = new Intent();

                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,"");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, shareUri);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    shareIntent.setType("application/pdf");

                    startActivity(shareIntent);

                } else {
                    Utils.showToastWithTitleAndContext(ImageZoom.this,getString(R.string.msgRetry));
                }

            }
        });
*/

    }

    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_sharepdf = (ImageView) findViewById(R.id.iv_share);
        iv_sharepdf.setVisibility(View.VISIBLE);
        iv_sharepdf.setOnClickListener(this);

        tv_title.setText("");
        if (getIntent().getExtras().containsKey("title")) {
            String title = getIntent().getExtras().getString("title");
            tv_title.setText(title);
        }
        //  iv_actionbtn.setVisibility(View.VISIBLE);


    }

    //this code is added By Gaurav for sharing PDF file
    public void shareFile() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {


            senduri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", actualFile);
        } else {
            senduri = Uri.fromFile(actualFile);
        }


        final Intent shareIntent = new Intent();

        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "");
        shareIntent.putExtra(Intent.EXTRA_STREAM, senduri);

        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        shareIntent.setType("image/jpeg");//("image/jpeg");

        //  context.startActivity(Intent.createChooser(shareIntent, "Share to"));


        startActivity(shareIntent);


    }

    private Uri takeScreenshot() {

        Bitmap screenBitmap = ScreenshotUtils.getScreenShot(ll_root);


        Bitmap waterMarkPic =  null;//ScreenshotUtils.addWaterMark(screenBitmap,EventDetails.this,PreferenceManager.getPreference(EventDetails.this,PreferenceManager.MY_CATEGORY),title);

        if (PreferenceManager.getPreference(getApplicationContext(), isRIadminModule).equals("Yes")) {
            waterMarkPic = ScreenshotUtils.addWaterMark(screenBitmap, ImageZoom.this, "0", " ");

        }else {

            if (isFromNotification) {
                waterMarkPic = ScreenshotUtils.addWaterMark(screenBitmap, ImageZoom.this, "0", tv_title.getText().toString());
            } else {
                waterMarkPic = ScreenshotUtils.addWaterMark(screenBitmap, ImageZoom.this, PreferenceManager.getPreference(ImageZoom.this, PreferenceManager.MY_CATEGORY), tv_title.getText().toString());
            }
        }

        File imageFile = createPdf(waterMarkPic);//ScreenshotUtils.store(waterMarkPic,"shared_image.png",ScreenshotUtils.getMainDirectoryName(EventDetails.this));

        // Log.d("sa","file path=>"+imageFile.getPath());
        //return Uri.parse(imageFile.getPath());

        return  FileProvider.getUriForFile(this, "com.SampleApp.row.fileprovider", imageFile);
    }

    private File createPdf(Bitmap b) {

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(b.getWidth(), b.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();

        paint.setColor(Color.parseColor("#ffffff"));

        canvas.drawPaint(paint);

        Bitmap bitmap = Bitmap.createScaledBitmap(b, b.getWidth(), b.getHeight(), true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        //ScreenshotUtils.getMainDirectoryName(EventDetails.this)

        File saveFilePath = ScreenshotUtils.getMainDirectoryName(ImageZoom.this);

        File dir = new File(saveFilePath.getAbsolutePath());

        if (!dir.exists()) {
            dir.mkdirs();
        }

        String today = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());

        //String fileName = "ROW_Event_"+today+".pdf";

        //---***Add By Nivedita***---//
        String fileName = tv_title.getText().toString()+"_"+today+".pdf";

        File file = new File(saveFilePath.getAbsolutePath(), fileName);

        try {
            document.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

       /* String extr = Environment.getExternalStorageDirectory() + "/CreatePDF/";

        File file = new File(extr);

        if (!file.exists()) {
            file.mkdir();
        }

        String fileName = "ROW"+System.currentTimeMillis()+".pdf";

       File filePath = new File(extr, fileName);

       try {
            document.writeTo(new FileOutputStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }*/
        // close the document

        document.close();

        return  file;
    }

    @Override
    public void onClick(View v) {
        // Preventing multiple clicks, using threshold of 1 second
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        Log.d("Clicked", "LastClickedTime "+mLastClickTime);


        switch (v.getId()) {

            case R.id.iv_share:
                // do your code
                Uri shareUri = takeScreenshot();

                if (shareUri != null) {

                    Intent shareIntent = new Intent();

                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, shareUri);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    shareIntent.setType("application/pdf");
                    startActivity(shareIntent);

                } else {
                    Utils.showToastWithTitleAndContext(ImageZoom.this, getString(R.string.msgRetry));
                }

                break;
        }
    }
}
