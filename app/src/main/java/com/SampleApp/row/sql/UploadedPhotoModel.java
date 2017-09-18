package com.SampleApp.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import com.SampleApp.row.Data.UploadPhotoData;
import com.SampleApp.row.Utils.ImageCompression;

/**
 * Created by user on 09-11-2016.
 */
public class UploadedPhotoModel {

    Context context;
    SQLiteDatabase db;
    ImageCompression compress;
    public UploadedPhotoModel(Context context) {
        this.context = context;
        db = DBConnection.getInstance(context);
        compress = new ImageCompression();
    }


    public long insert(UploadPhotoData ad) {
        long id = - 1;
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.UploadedPhoto.Columns.PHOTO_ID,ad.getPhotoId());
            values.put(Tables.UploadedPhoto.Columns.PATH, ad.getPhotoPath());
            values.put(Tables.UploadedPhoto.Columns.DESCRIPTION, ad.getDescription());
            values.put(Tables.UploadedPhoto.Columns.ALBUM_ID, ad.getAlbumId());
            values.put(Tables.UploadedPhoto.Columns.GROUP_ID, ad.getGroupd());
            values.put(Tables.UploadedPhoto.Columns.CREATED_BY, ad.getCreatedBy());
            values.put(Tables.UploadedPhoto.Columns.IS_UPLOADED, ad.getIsUploaded());

            return db.insert(Tables.UploadedPhoto.TABLE_NAME, null, values);

        } catch(Exception e) {
            e.printStackTrace();
            return id;
        }
    }

    public ArrayList<UploadPhotoData> getUploadedPhoto() {
        try {
            ArrayList<UploadPhotoData> list = new ArrayList<UploadPhotoData>();
            Cursor cursor = db.rawQuery("select * from UploadedPhoto where isUploaded=" + "0", null);

            while (cursor.moveToNext()) {
                String photoId = cursor.getString(cursor.getColumnIndex(Tables.UploadedPhoto.Columns.PHOTO_ID));
                String path = cursor.getString(cursor.getColumnIndex(Tables.UploadedPhoto.Columns.PATH));
                String description = cursor.getString(cursor.getColumnIndex(Tables.UploadedPhoto.Columns.DESCRIPTION));
                String albumId = cursor.getString(cursor.getColumnIndex(Tables.UploadedPhoto.Columns.ALBUM_ID));
                String grpId = cursor.getString(cursor.getColumnIndex(Tables.UploadedPhoto.Columns.GROUP_ID));
                String createdBy = cursor.getString(cursor.getColumnIndex(Tables.UploadedPhoto.Columns.CREATED_BY));
                String isUploaded = cursor.getString(cursor.getColumnIndex(Tables.UploadedPhoto.Columns.IS_UPLOADED));
                String id = cursor.getString(cursor.getColumnIndex(Tables.UploadedPhoto.Columns._ID));

                File f = new File(path);
                String image=  compress.compressImage(f.toString(),context);

                UploadPhotoData ad = new UploadPhotoData(photoId,image, description, albumId, grpId, createdBy,isUploaded,id);
                list.add(ad);

            }
            //if (list.size() == 0 ) return null;
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Touchbase", e.toString());
            return null;
        }
    }

    public void printTable() {
        Cursor cursor = db.rawQuery("select * from UploadedPhoto", null);
        int n = cursor.getColumnCount();
        while(cursor.moveToNext()) {
            String rec = "";
            for(int i=0;i<n;i++) {
                rec = rec + cursor.getString(i) +" - ";
            }
            Log.e("row", rec);
        }
    }


    public boolean UpdateTable(String id) {

        try {
            ContentValues values = new ContentValues();

            values.put(Tables.UploadedPhoto.Columns.IS_UPLOADED, "1");
            db.update(Tables.UploadedPhoto.TABLE_NAME, values, "_id=?", new String[]{id});
            return true;

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }

    }



}
