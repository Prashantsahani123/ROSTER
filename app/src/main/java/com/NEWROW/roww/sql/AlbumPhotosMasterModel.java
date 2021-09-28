package com.NEWROW.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.NEWROW.row.Data.AlbumPhotoData;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by USER1 on 17-09-2016.
 */
public class AlbumPhotosMasterModel {

    Context context;
    SQLiteDatabase db;
    public AlbumPhotosMasterModel(Context context) {
        this.context = context;
        db = DBConnection.getInstance(context);
    }

    public ArrayList<AlbumPhotoData> getAlbumsPhoto(String album_Id) {
        try {
            ArrayList<AlbumPhotoData> list = new ArrayList<AlbumPhotoData>();
            Cursor cursor = db.rawQuery("select * from album_photo_master where albumId=" + album_Id, null);

            while (cursor.moveToNext()) {
                String grpId = cursor.getString(cursor.getColumnIndex(Tables.AlbumPhotoMaster.Columns.GRP_ID));
                String albumId = cursor.getString(cursor.getColumnIndex(Tables.AlbumPhotoMaster.Columns.ALBUM_ID));
                String description = cursor.getString(cursor.getColumnIndex(Tables.AlbumPhotoMaster.Columns.DESCRIPTION));
                String photoId = cursor.getString(cursor.getColumnIndex(Tables.AlbumPhotoMaster.Columns.PHOTO_ID));
                String url = cursor.getString(cursor.getColumnIndex(Tables.AlbumPhotoMaster.Columns.URL));

                AlbumPhotoData ad = new AlbumPhotoData(grpId, albumId, description, photoId, url);
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

    public boolean isDataAvailable(String albumId) {
        Cursor cursor = db.rawQuery("select * from album_photo_master where albumId="+albumId, null);

        if (cursor.getCount() > 0 ) return true;

        return false;
    }

    public long insert( AlbumPhotoData ad) {
        long id = - 1;
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.AlbumPhotoMaster.Columns.GRP_ID, Integer.parseInt(ad.getGrpId()));
            values.put(Tables.AlbumPhotoMaster.Columns.ALBUM_ID, Integer.parseInt(ad.getAlbumId()));
            values.put(Tables.AlbumPhotoMaster.Columns.PHOTO_ID, Integer.parseInt(ad.getPhotoId()));
            values.put(Tables.AlbumPhotoMaster.Columns.URL, ad.getUrl());
            values.put(Tables.AlbumPhotoMaster.Columns.DESCRIPTION, ad.getDescription());

            boolean  dataAvailable = albumPhotoExists(ad.getAlbumId(),ad.getPhotoId());
            int n = 0;
            if(dataAvailable) {
                n = db.update(Tables.AlbumPhotoMaster.TABLE_NAME, values, "albumId=? and photoId=?", new String[]{"" + ad.getAlbumId(), "" + ad.getPhotoId()});
                return n;
            }else {
                return db.insert(Tables.AlbumPhotoMaster.TABLE_NAME, null, values);
            }
        } catch(Exception e) {
            e.printStackTrace();
            return id;
        }
    }

    public boolean updateAlbumPhotoMasterModel(AlbumPhotoData data) {
        try {
            ContentValues values = new ContentValues();

            values.put(Tables.AlbumPhotoMaster.Columns.GRP_ID, Integer.parseInt(data.getGrpId()));
            values.put(Tables.AlbumPhotoMaster.Columns.ALBUM_ID, Integer.parseInt(data.getAlbumId()));
            values.put(Tables.AlbumPhotoMaster.Columns.PHOTO_ID, Integer.parseInt(data.getPhotoId()));
            values.put(Tables.AlbumPhotoMaster.Columns.URL, data.getUrl());
            values.put(Tables.AlbumPhotoMaster.Columns.DESCRIPTION, data.getDescription());

            boolean  dataAvailable = albumPhotoExists(data.getAlbumId(),data.getPhotoId());
            int n = 0;
            if(dataAvailable) {
                try {
                    n = db.update(Tables.AlbumPhotoMaster.TABLE_NAME, values, "albumId=? and photoId=?", new String[]{data.getAlbumId(),data.getPhotoId()});
                    return true;
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                long id =  insert(data);
                if(id>0){
                    //return n == 1;
                    return true;
                };
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean albumPhotoExists(String albumId,String photoId) {
        Cursor cursor = db.rawQuery("select * from album_photo_master where albumId="+ albumId + " and photoId" + "=" +  "'" + photoId + "'" , null);

        if (cursor.getCount() > 0 ) return true;

        return false;
    }


    //------------------------------ delete ------------------------------------------------------

    public boolean deleteGroupPhotoMasterModel(AlbumPhotoData data,String albumId) {
        try {
            int n = 0;
            boolean  dataAvailable = albumPhotoExists(albumId, data.getPhotoId());
            if(dataAvailable) {
                n = db.delete(Tables.AlbumPhotoMaster.TABLE_NAME,  "albumId=? and photoId=?", new String[]{ albumId, data.getPhotoId()});
                return n == 1;  // if n = 1 it will return true else will return false
            }else{
                return true;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    //------------------------- Sync Data --------------------------------------------

    public boolean syncData(long grpId , String albumId, ArrayList<AlbumPhotoData> newRecords, ArrayList<AlbumPhotoData> updatedRecords, ArrayList<AlbumPhotoData> deleteRecords) {
        db.beginTransaction();
        try {
            // inserting new records in to table
            Iterator<AlbumPhotoData> newIterator = newRecords.iterator();
            while (newIterator.hasNext()) {
                long id = insert(newIterator.next());
                if ( id == -1 ) {
                    db.endTransaction();
                    return false;
                }
            }

            //upadting updated records in db
            Iterator<AlbumPhotoData> updateIterator = updatedRecords.iterator();

            while ( updateIterator.hasNext() ) {
                boolean updated = updateAlbumPhotoMasterModel(updateIterator.next());
                if ( ! updated ) {
                    db.endTransaction();
                    return false;
                }
            }

            //deleting deleted records from db
            Iterator<AlbumPhotoData> deletedIterator = deleteRecords.iterator();

            while ( deletedIterator.hasNext() ) {
                //boolean deleted = deleteGroupMasterModel(groupId, masterUid, deletedIterator.next().longValue());

                boolean deleted = deleteGroupPhotoMasterModel(deletedIterator.next(),albumId);
                if ( ! deleted ) {
                    db.endTransaction();
                    return false;
                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            db.endTransaction();
            return false;
        }
    }

    public boolean deletePhoto(String albumId,String photoId) {
        try {
            int n = 0;
            boolean  dataAvailable = albumPhotoExists(albumId,photoId);
            if(dataAvailable) {
                n = db.delete(Tables.AlbumPhotoMaster.TABLE_NAME,  "albumId=? and photoId=?", new String[]{ albumId, photoId});
                return n == 1;  // if n = 1 it will return true else will return false
            }else{
                return true;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
