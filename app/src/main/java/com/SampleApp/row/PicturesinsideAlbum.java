package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import com.SampleApp.row.Adapter.PictureInsideAlbumAdapter;
import com.SampleApp.row.Data.PictureInsideAlbumData;
import com.SampleApp.row.Data.SimplePhotoData;
import com.SampleApp.row.Utils.ImageCompression;


/**
 * Created by user on 17-10-2016.
 */
public class PicturesinsideAlbum extends Activity  {

    ArrayList<PictureInsideAlbumData> mAlbumsList = new ArrayList<PictureInsideAlbumData>();
    GridView gv;
    Uri uri;
    Cursor cursor;
    String foldername = "";
    String flag = "";
    Context context;
    ImageCompression compressImage;
    TextView tv_done;
    PictureInsideAlbumAdapter adapter;
    TextView tv_title;
    ImageView iv_backbutton;
    ArrayList<SimplePhotoData> existingphotolist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pictureinside_album);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText("Select Photo");
        context = this;
        gv = (GridView) findViewById(R.id.gv);
        tv_done = (TextView) findViewById(R.id.tv_done);
        compressImage = new ImageCompression();




        Intent i = getIntent();
        foldername = i.getStringExtra("Foldername");
        flag=i.getStringExtra("isloadmore");

        existingphotolist=i.getParcelableArrayListExtra("Existing photo list");

        AlbumPictureAsync asyncTask = new AlbumPictureAsync(context);
        asyncTask.execute();


                     adapter = new PictureInsideAlbumAdapter(PicturesinsideAlbum.this, mAlbumsList, "0",flag,existingphotolist);
                    gv.setAdapter(adapter);


        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             int count =   adapter.getBox().size();
                if(count>0) {
                    if(count<=5) {
                        ArrayList<PictureInsideAlbumData> selected = adapter.getBox();
                        ArrayList<SimplePhotoData> selectedPhotos = new ArrayList<SimplePhotoData>();

                        Iterator<PictureInsideAlbumData> iterator = selected.iterator();
                        while (iterator.hasNext()) {
                            PictureInsideAlbumData data = iterator.next();
                            SimplePhotoData photoData = new SimplePhotoData(data.getFilepath(), "");

                            selectedPhotos.add(photoData);
                            Log.e("path", photoData.getUrl());
                        }


                        Intent data = new Intent();
                        data.putExtra("selectedPhotos", selectedPhotos);
                        setResult(Activity.RESULT_OK, data);
                        finish();
                    }else{
                        Toast.makeText(PicturesinsideAlbum.this, "Only 5 pictures are allowed to select", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(PicturesinsideAlbum.this, "Please Select Picture", Toast.LENGTH_LONG).show();
                }
            }
        });


                }

    public class AlbumPictureAsync extends AsyncTask<String, String, String> {
        String val = null;
        //ProgressDialog dialog;
        Context context = null;
        ProgressDialog progressDialog = new ProgressDialog(PicturesinsideAlbum.this, R.style.TBProgressBar);
        Bitmap myBitmap;

        public AlbumPictureAsync(Context ctx) {


            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //	dialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
                String searchParams = null;
                searchParams = "bucket_display_name = \"" + foldername + "\"";

                String[] projection = {MediaStore.MediaColumns.DATA,
                        MediaStore.MediaColumns.DISPLAY_NAME};


                Cursor cur = getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                        searchParams, null, orderBy + " DESC");


                if (cur != null && cur.moveToFirst()) {

                    do {
                        String filePath = cur.getString(1);

                        String uri = cur.getString(cur.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
                        File imgFile = new File(filePath);

                        if (imgFile.exists()) {
                            if (imgFile != null) {
                              //  myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

//                                Bitmap bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                                bmp.compress(Bitmap.CompressFormat.JPEG, 70, bos);
//
//                                Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bos.toByteArray()));

                                PictureInsideAlbumData album = new PictureInsideAlbumData(uri,filePath,false,0 );
                                mAlbumsList.add(album);
                                // iv.setImageURI(Uri.parse(uri));

                            }
                        }
                        long creationDate = getCreationDate(filePath);

                    } while (cur.moveToNext());
                    cur.close();
                    val = "true";
                }
            }catch (Exception e) {
                    e.printStackTrace();
                val = "true";
                }
                return val;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                try {
                    if (this.progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    //Log.d("response","Do post"+ result.toString());
                    if (result.equalsIgnoreCase("true")) {

                    } else {
                        Log.d("Response", "Could not load Images inside folder properly");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(PicturesinsideAlbum.this, R.string.messageSorry, Toast.LENGTH_LONG).show();
                }
            }

        }

        public  long getCreationDate(String filePath) {
            File file = new File(filePath);
            return file.lastModified();
        }

//    @Override
//    public void onBackPressed() {
//        if(flag.equalsIgnoreCase("")|| flag.equalsIgnoreCase(null)){
//
//            super.onBackPressed();
//
//        }else{
//
//            Intent intent = new Intent(PicturesinsideAlbum.this, AlbumFolderPage.class);
//            startActivity(intent);
//            finish();
//        }
//
//    }
}





