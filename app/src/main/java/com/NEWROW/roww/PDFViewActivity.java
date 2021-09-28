package com.NEWROW.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.OpenableColumns;

import androidx.core.content.FileProvider;

import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Adapter.DocumentAdapter;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.List;

/**
 * Created by USER1 on 27-10-2016.
 */
public class PDFViewActivity extends Activity implements OnPageChangeListener, OnLoadCompleteListener, View.OnClickListener {

    private static final String TAG = PDFViewActivity.class.getSimpleName();
    private final static int REQUEST_CODE = 42;
    public static final String SAMPLE_FILE = "sample.pdf";
    File actualFile;
    PDFView pdfView;
    Uri uri, senduri;
    Integer pageNumber = 0;
    String pdfFileName;
    String fileName;
    String filePath = "";
    TextView tvTitle;
    String mode = "";
    String mom_gaurav = "0";
    WebView webview;
    //Add By Gaurav
    ImageView iv_sharepdf;
    boolean isFromNotification = false;
    ImageView iv_backbuttonpdf;
    Context context;
    // ImageView iv_backbutton;
    // variable to track event time
    private long mLastClickTime = 0;
    private String storageFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        try {

            setContentView( R.layout.pdf_view_activity );
            //  context = this;
            pdfView = (PDFView) findViewById( R.id.pdfView );
            webview = (WebView) findViewById( R.id.webview );


            Intent i = getIntent();
            fileName = i.getStringExtra( "fileName" );

            Log.e( "Touchbase", "♦♦♦♦File name : " + fileName );

            mode = i.getStringExtra( "mode" );
            mom_gaurav = i.getExtras().getString( "mom_gaurav", "0" );
            storageFileName = i.getExtras().getString( "storageFileName", "" );


            actualFile = new File( fileName );
            tvTitle = (TextView) findViewById( R.id.tv_title );
            iv_sharepdf = (ImageView) findViewById( R.id.iv_share );

            if (mom_gaurav.equalsIgnoreCase( "1" )) {
                //Add By Gaurav
                iv_sharepdf.setVisibility( View.GONE );
            } /*else if (mode.equals(DocumentAdapter.MODE_DOWNLOAD)) {
                //Add By Gaurav
                iv_sharepdf.setVisibility(View.VISIBLE);

            } */ else {
                //Add By Gaurav
                iv_sharepdf.setVisibility( View.VISIBLE );
                iv_sharepdf.setOnClickListener( this );

            }


         /*   iv_sharepdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareFile();
                }
            });
*/

            //Log.e("-----FILE NAME---------", "@@@@@@@@@@@@@" + fileName);


            if (i.getExtras().containsKey( "title" )) {
                String title = i.getStringExtra( "title" );
                tvTitle.setText( title );

            } else {

                tvTitle.setText( fileName );

                // iv_share.setVisibility(View.VISIBLE);
            }

            if (i.hasExtra( "ext" )) {

                String ext = i.getStringExtra( "ext" );

                if (ext.equalsIgnoreCase( "pdf" )) {
                    afterViews();
                } else if (ext.equalsIgnoreCase( "doc" ) || ext.equalsIgnoreCase( "docx" )) {

                    iv_sharepdf.setVisibility( View.GONE );

                    pdfView.setVisibility( View.GONE );
                    webview.setVisibility( View.VISIBLE );
                    filePath = i.getStringExtra( "filePath" );

                    webview.setVerticalScrollBarEnabled( false );
                    webview.setHorizontalScrollBarEnabled( false );


                    String doc = "<iframe src='http://docs.google.com/gview?embedded=true&url=" + filePath + "' width='100%' height='100%' style='border: none;'></iframe>";

                    webview.getSettings().setJavaScriptEnabled( true );
                    webview.getSettings().setSupportZoom( true );

                    final ProgressDialog dialog = new ProgressDialog( this );
                    dialog.setMessage( "Loading" );
                    String docUrl = "http://docs.google.com/gview?embedded=true&url=";
                    String officeOnlineURL = "https://view.officeapps.live.com/op/view.aspx?src=";


                    webview.setWebViewClient( new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl( url );
                            return true;
                        }

                        @Override
                        public void onPageFinished(WebView view, String url) {

                            dialog.dismiss();
                        }

                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {

                            super.onPageStarted( view, url, favicon );
                            dialog.show();
                        }
                    } );

                    Log.e( "Doc", "URL is:" + officeOnlineURL + filePath );

                    //  webview.loadUrl( docUrl + filePath );
                    webview.loadUrl( officeOnlineURL + filePath );

                }

            } else {
                afterViews();

                // Toast.makeText(PDFViewActivity.this, "Unable to open this format", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            Log.e( "TouchBase", "♦♦♦♦Error is : " + e.getMessage() );
            e.printStackTrace();
        }
    }


    //this code is added By Gaurav for sharing PDF file
    public void shareFile() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            senduri = FileProvider.getUriForFile( getApplicationContext(), getPackageName() + ".fileprovider", actualFile );
        } else {
            senduri = Uri.fromFile( actualFile );
        }


        final Intent shareIntent = new Intent();

        shareIntent.setAction( Intent.ACTION_SEND );
        shareIntent.putExtra( Intent.EXTRA_SUBJECT, "" );
        shareIntent.putExtra( Intent.EXTRA_TEXT, "" );
        shareIntent.putExtra( Intent.EXTRA_STREAM, senduri );

        shareIntent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION );

        shareIntent.setType( "application/pdf" );//("image/jpeg");

        //  context.startActivity(Intent.createChooser(shareIntent, "Share to"));


        startActivity( shareIntent );


    }

    void afterViews() {

        uri = Uri.fromFile( actualFile );

        File file = new File( getFilesDir().getPath() + fileName );

        /* uri = Uri.fromFile(file);*/


        // uri = Uri.fromFile(new File("/data/data/kaizen.app.com.touchbase/fileseventpdf-102112016104219AM.pdf"));


        Log.e( "-----FILE NAME---------", "@@@@@@@@@@@@@" + uri );

        // String link = "/storage/emulated/0/Download/pdf.pdf";
        //uri = Uri.parse(link);

        if (uri != null) {

            Log.d( "@@@@@", "----URI-----" + uri );

            displayFromUri( uri );
            // displayFromFile(file.getPath());


        } else {
            displayFromAsset( SAMPLE_FILE );
        }
        setTitle( pdfFileName );
    }

    private void displayFromAsset(String assetFileName) {

        pdfFileName = assetFileName;

        pdfView.fromAsset( SAMPLE_FILE )
                .defaultPage( pageNumber )
                .onPageChange( this )
                .enableAnnotationRendering( true )
                .onLoad( this )
                .scrollHandle( new DefaultScrollHandle( this ) )
                .load();
    }


    private void displayFromUri(Uri uri) {
        pdfFileName = getFileName( uri );

        pdfView.fromUri( uri )
                .defaultPage( pageNumber )
                .onPageChange( this )
                .enableAnnotationRendering( true )
                .onLoad( this )
                .scrollHandle( new DefaultScrollHandle( this ) )
                .load();
    }


    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle( String.format( "%s %s / %s", pdfFileName, page + 1, pageCount ) );
    }

/*
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }
*/

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals( "content" )) {
            Cursor cursor = getContentResolver().query( uri, null, null, null, null );
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString( cursor.getColumnIndex( OpenableColumns.DISPLAY_NAME ) );
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else {

            File file = new File( Environment.DIRECTORY_DOWNLOADS, storageFileName );
            if (file.exists()) {
                Intent intent = new Intent( Intent.ACTION_VIEW );
                Uri uri1 = Uri.fromFile( file );
                intent.setDataAndType( uri1, "application/pdf" );
                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                intent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION );

                try {
                    startActivity( intent );
                } catch (ActivityNotFoundException e) {
                    Toast.makeText( getApplicationContext(), "No Application available to view pdf", Toast.LENGTH_LONG ).show();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }


    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        Log.e( TAG, "title = " + meta.getTitle() );
        Log.e( TAG, "author = " + meta.getAuthor() );
        Log.e( TAG, "subject = " + meta.getSubject() );
        Log.e( TAG, "keywords = " + meta.getKeywords() );
        Log.e( TAG, "creator = " + meta.getCreator() );
        Log.e( TAG, "producer = " + meta.getProducer() );
        Log.e( TAG, "creationDate = " + meta.getCreationDate() );
        Log.e( TAG, "modDate = " + meta.getModDate() );

        printBookmarksTree( pdfView.getTableOfContents(), "-" );

        if (mode.equals( DocumentAdapter.MODE_VIEW )) {
            boolean deleted = actualFile.delete();
            Log.e( "TouchBase", "♦♦♦♦File : " + actualFile.getPath() + " Deleted : " + deleted );
            //actualFile.deleteOnExit();
        }
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {

        for (PdfDocument.Bookmark b : tree) {

            Log.e( TAG, String.format( "%s %s, p %d", sep, b.getTitle(), b.getPageIdx() ) );

            if (b.hasChildren()) {
                printBookmarksTree( b.getChildren(), sep + "-" );
            }
        }
    }

    @Override
    public void onClick(View v) {

        // Preventing multiple clicks, using threshold of 1 second
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        Log.d( "Clicked", "LastClickedTime " + mLastClickTime );


        switch (v.getId()) {

            case R.id.iv_share:
                shareFile();


                break;
        }


    }


}
