package com.SampleApp.row;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.TextView;

import com.SampleApp.row.Adapter.DocumentAdapter;
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
public class PDFViewActivity extends Activity implements OnPageChangeListener, OnLoadCompleteListener {

    private static final String TAG = PDFViewActivity.class.getSimpleName();
    private final static int REQUEST_CODE = 42;
    public static final String SAMPLE_FILE = "sample.pdf";
    File actualFile;
    PDFView pdfView;
    Uri uri;
    Integer pageNumber = 0;
    String pdfFileName;
    String fileName;
    String filePath = "";
    TextView tvTitle;
    String mode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {

            setContentView(R.layout.pdf_view_activity);

            pdfView = (PDFView) findViewById(R.id.pdfView);

            Intent i = getIntent();
            fileName = i.getStringExtra("fileName");

            Log.e("Touchbase", "♦♦♦♦File name : "+fileName);

            mode = i.getStringExtra("mode");

            //filePath = i.getStringExtra("filePath");
            //actualFile = new File(Environment.getExternalStorageDirectory() + "/Touchbase/"+fileName);

            actualFile = new File(fileName);

            //Log.e("-----FILE NAME---------", "@@@@@@@@@@@@@" + fileName);
            afterViews();

            tvTitle = (TextView) findViewById(R.id.tv_title);

            if ( i.getExtras().containsKey("title")) {
                String title = i.getStringExtra("title");
                tvTitle.setText(title);
            } else {
                tvTitle.setText(fileName);
            }
        } catch(Exception e){
            Log.e("TouchBase", "♦♦♦♦Error is : "+e.getMessage());
            e.printStackTrace();
        }
    }
    void pickFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, REQUEST_CODE);
    }

    void afterViews() {

       uri = Uri.fromFile(actualFile);
       File file = new File(getFilesDir().getPath() + fileName);

        /* uri = Uri.fromFile(file);*/


       // uri = Uri.fromFile(new File("/data/data/kaizen.app.com.touchbase/fileseventpdf-102112016104219AM.pdf"));


        Log.e("-----FILE NAME---------", "@@@@@@@@@@@@@" + uri);

        // String link = "/storage/emulated/0/Download/pdf.pdf";
        //uri = Uri.parse(link);

        if (uri != null) {

            Log.d("@@@@@","----URI-----"+uri);

            displayFromUri(uri);
          // displayFromFile(file.getPath());


        } else {
            displayFromAsset(SAMPLE_FILE);
        }
        setTitle(pdfFileName);
    }

    private void displayFromAsset(String assetFileName) {

        pdfFileName = assetFileName;

        pdfView.fromAsset(SAMPLE_FILE)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }


    private void displayFromFile(String path) {

        File file = new File(path);

        pdfFileName = file.getName();


        pdfView.fromFile(file)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    private void displayFromUri(Uri uri) {
        pdfFileName = getFileName(uri);

        pdfView.fromUri(uri)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }


    public void onResult(int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            uri = intent.getData();
            displayFromUri(uri);
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

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

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        Log.e(TAG, "title = " + meta.getTitle());
        Log.e(TAG, "author = " + meta.getAuthor());
        Log.e(TAG, "subject = " + meta.getSubject());
        Log.e(TAG, "keywords = " + meta.getKeywords());
        Log.e(TAG, "creator = " + meta.getCreator());
        Log.e(TAG, "producer = " + meta.getProducer());
        Log.e(TAG, "creationDate = " + meta.getCreationDate());
        Log.e(TAG, "modDate = " + meta.getModDate());
        printBookmarksTree(pdfView.getTableOfContents(), "-");

        if ( mode.equals(DocumentAdapter.MODE_VIEW)) {
            boolean deleted = actualFile.delete();
            Log.e("TouchBase", "♦♦♦♦File : " + actualFile.getPath() +" Deleted : "+deleted);
            //actualFile.deleteOnExit();
        }
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }


    }

    /*@Override
    public void onBackPressed() {
        if ( mode.equals(DocumentAdapter.MODE_VIEW)) {
            boolean deleted = actualFile.delete();
            Log.e("TouchBase", "♦♦♦♦File : " + actualFile.getPath() +" Deleted : "+deleted);
        }
        super.onBackPressed();
    }*/

    /*
    @Override
    protected void onDestroy() {
        super.onDestroy();

        File fdelete = new File(Environment.getExternalStorageDirectory() + "/Touchbase/"+fileName);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                System.out.println("file Deleted :" + uri.getPath());
            } else {
                System.out.println("file not Deleted :" + uri.getPath());
            }
        }
    }*/



}
