package com.NEWROW.row;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.NEWROW.row.Adapter.AlbumFolderAdapter;
import com.NEWROW.row.Data.AlbumFolder;
import com.NEWROW.row.Data.SimplePhotoData;
import com.NEWROW.row.Utils.PreferenceManager;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by user on 14-09-2016.
 */
public class AlbumFolderPage extends Activity {
    public static final int IMAGE_SELECTION_REQUEST = 10;
    ArrayList<AlbumFolder> mAlbumsList = new ArrayList<AlbumFolder>();
    GridView gv;
    Uri uri;
    Cursor cursor;
    TextView tv_title;
    ImageView iv_backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_albumfolder_page);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText("Select Album");

        gv = (GridView) findViewById(R.id.gv);

        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Thumbnails.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        ArrayList<String> ids = new ArrayList<String>();
        mAlbumsList.clear();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                AlbumFolder album = new AlbumFolder();

                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
                album.setId(cursor.getString(columnIndex));
               // album.setId("");

                if (!ids.contains(cursor.getString(columnIndex))) {
                    columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                    String foldername = cursor.getString(columnIndex);
                    album.setName(cursor.getString(columnIndex));


                    columnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                    album.setAlbumCoverId(cursor.getLong(columnIndex));

                    final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
                    String searchParams = null;
                    searchParams = "bucket_display_name = \"" + foldername + "\"";

                    String[] project = {MediaStore.MediaColumns.DATA,
                            MediaStore.MediaColumns.DISPLAY_NAME};


                    Cursor cur = getContentResolver().query(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                            searchParams, null, orderBy + " DESC");


                    if (cur != null && cur.moveToFirst()) {
                        String filePath = cur.getString(1);
                        //String path = imageCompress.compressImage(filePath,this);
                        album.setFilepath(filePath);

                    }
                    cur.close();


                    mAlbumsList.add(album);
                    ids.add(album.getId());
                } else {

                }
            }
            cursor.close();
            AlbumFolderAdapter adapter = new AlbumFolderAdapter(AlbumFolderPage.this, mAlbumsList, "1");
            gv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
        }

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

ArrayList<String> emptyList = new ArrayList<String>();
                Intent i  = new Intent(AlbumFolderPage.this,PicturesinsideAlbum.class);
                i.putExtra("Foldername",mAlbumsList.get(position).getName());
                i.putExtra("isloadmore","");
                i.putExtra("Existing photo list",emptyList);
                PreferenceManager.savePreference(AlbumFolderPage.this,"galleryfoldername",mAlbumsList.get(position).getName());
                startActivityForResult(i, IMAGE_SELECTION_REQUEST);

//                Bitmap myBitmap = null;
//                String foldername = mAlbumsList.get(position).getName();
//                mAlbumsList.clear();
//                final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
//                String searchParams = null;
//                searchParams = "bucket_display_name = \"" + foldername + "\"";
//
//                String[] projection = {MediaStore.MediaColumns.DATA,
//                        MediaStore.MediaColumns.DISPLAY_NAME};
//
//
//                Cursor cur = getContentResolver().query(
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
//                        searchParams, null, orderBy + " DESC");
//
//
//                if (cur != null && cur.moveToFirst()) {
//
//                    do {
//                        String filePath = cur.getString(1);
//                        File imgFile = new  File(filePath);
//
//                        if(imgFile.exists()){
//                             myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                        }
//                        long creationDate = getCreationDate(filePath);
//                        AlbumFolder album = new AlbumFolder(myBitmap,filePath,0);
//                        mAlbumsList.add(album);
//                    } while (cur.moveToNext());
//                    cur.close();
//                    AlbumFolderAdapter adapter = new AlbumFolderAdapter(AlbumFolderPage.this, mAlbumsList, "0");
//                    gv.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//
//                }

            }
        });
    }



    public  long getCreationDate(String filePath) {
        File file = new File(filePath);
        return file.lastModified();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == Activity.RESULT_OK ) {
            if ( requestCode == IMAGE_SELECTION_REQUEST) {
                ArrayList<SimplePhotoData> list = data.getParcelableArrayListExtra("selectedPhotos");
                Log.e("TouchbaseSelectedPhotos", list.toString());

                Intent finalData = new Intent();
                finalData.putExtra("selectedPhotos", finalData);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        }
    }
}

