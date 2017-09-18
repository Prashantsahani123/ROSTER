package com.SampleApp.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

import com.SampleApp.row.Data.AlbumData;
import com.SampleApp.row.Data.AlbumPhotoData;


/**
 * Created by user on 06-09-2016.
 */
public class GalleryMasterModel {

    Context context;
    SQLiteDatabase db;
    public GalleryMasterModel(Context context) {
        this.context = context;
        db = DBConnection.getInstance(context);
    }

       public ArrayList<AlbumData> getAlbums(String grpId,String moduleId) {
        try {
            ArrayList<AlbumData> list = new ArrayList<AlbumData>();
            Cursor cursor = db.rawQuery("select * from gallery_master where groupId=" + grpId + " and moduleId=" + moduleId, null);

            while (cursor.moveToNext()) {
                String albumId = cursor.getString(cursor.getColumnIndex(Tables.AlbumMaster.Columns.ALBUM_ID));
                String title = cursor.getString(cursor.getColumnIndex(Tables.AlbumMaster.Columns.TITLE));
                String description = cursor.getString(cursor.getColumnIndex(Tables.AlbumMaster.Columns.DESCRIPTION));
                String image = cursor.getString(cursor.getColumnIndex(Tables.AlbumMaster.Columns.ALBUM_IMAGE));
                String isAdmin = cursor.getString(cursor.getColumnIndex(Tables.AlbumMaster.Columns.IS_GROUP_ADMIN));
                String moduleIdd = cursor.getString(cursor.getColumnIndex(Tables.AlbumMaster.Columns.MODULE_ID));

                AlbumData ad = new AlbumData(albumId, title, description, image, grpId, isAdmin,moduleIdd);
                    list.add(ad);

            }
            //if (list.size() == 0 ) return null;
            cursor.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Touchbase", e.toString());
            return null;
        }
    }

    public boolean isDataAvailable(long groupId,String moduleId) {
        Cursor cursor = db.rawQuery("select * from gallery_master where groupId="+ groupId + " " + "and moduleId=" + "'" + moduleId + "'" , null);

        if (cursor.getCount() > 0 ) {
            cursor.close();
            return true;
        }

        return false;
    }

    public long insert( AlbumData ad) {
        long id = - 1;
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.AlbumMaster.Columns.ALBUM_ID, Integer.parseInt(ad.getAlbumId()));
            values.put(Tables.AlbumMaster.Columns.TITLE, ad.getTitle());
            values.put(Tables.AlbumMaster.Columns.DESCRIPTION, ad.getDescription());
            values.put(Tables.AlbumMaster.Columns.ALBUM_IMAGE, ad.getImage());
            values.put(Tables.AlbumMaster.Columns.GROUP_ID, Integer.parseInt(ad.getGrpId()));
            values.put(Tables.AlbumMaster.Columns.IS_GROUP_ADMIN, ad.getIsAdmin());
            values.put(Tables.AlbumMaster.Columns.MODULE_ID, ad.getModuleId());

            return db.insert(Tables.AlbumMaster.TABLE_NAME, null, values);
        } catch(Exception e) {
            e.printStackTrace();
            return id;
        }
    }


    public boolean updateAlbumMasterModel(long grpId, AlbumData data) {
        try {
            ContentValues values = new ContentValues();

            values.put(Tables.AlbumMaster.Columns.ALBUM_ID, Integer.parseInt(data.getAlbumId()));
            values.put(Tables.AlbumMaster.Columns.TITLE, data.getTitle());
            values.put(Tables.AlbumMaster.Columns.DESCRIPTION, data.getDescription());
            values.put(Tables.AlbumMaster.Columns.ALBUM_IMAGE, data.getImage());
            values.put(Tables.AlbumMaster.Columns.GROUP_ID, Integer.parseInt(data.getGrpId()));
            values.put(Tables.AlbumMaster.Columns.IS_GROUP_ADMIN, data.getIsAdmin());
            values.put(Tables.AlbumMaster.Columns.MODULE_ID, data.getModuleId());

            boolean  dataAvailable = albumExists(grpId,data.getModuleId(),data.getAlbumId());
            int n = 0;
            if(dataAvailable) {
                try {
                    n = db.update(Tables.AlbumMaster.TABLE_NAME, values, "groupId=? and albumId=?", new String[]{"" + grpId, "" + data.getAlbumId()});
                    return n == 1;
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

    public boolean albumExists(long grpId, String moduleId,String albumId) {
         Cursor cursor = db.rawQuery("select * from gallery_master where groupId="+ grpId + " and moduleId=" + moduleId + " and albumId=" + albumId , null);

        if (cursor.getCount() > 0 ) return true;

        return false;
    }

    public boolean isdeleteAlbumExists(long grpId,String albumId) {
        Cursor cursor = db.rawQuery("select * from gallery_master where groupId="+ grpId + " and albumId=" + albumId , null);

        if (cursor.getCount() > 0 ) return true;

        return false;
    }


    public boolean deleteGroupMasterModel(long grpId,AlbumData data) {
        try {
            int n = 0;
            boolean  dataAvailable = isdeleteAlbumExists(grpId,data.getAlbumId());
            if(dataAvailable) {
                n = db.delete(Tables.AlbumMaster.TABLE_NAME,  "groupId=? and albumId=?", new String[]{"" + grpId, "" + data.getAlbumId()});
                return n == 1;  // if n = 1 it will return true else will return false
            }else{
                return true;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean syncData(long grpId, ArrayList<AlbumData> newRecords, ArrayList<AlbumData> updatedRecords, ArrayList<AlbumData> deleteRecords) {
        db.beginTransaction();
        try {
            // inserting new records in to table
            Iterator<AlbumData> newIterator = newRecords.iterator();
            while (newIterator.hasNext()) {
                long id = insert(newIterator.next());
                if ( id == -1 ) {
                    db.endTransaction();
                    return false;
                }
            }

            //upadting updated records in db
            Iterator<AlbumData> updateIterator = updatedRecords.iterator();

            while ( updateIterator.hasNext() ) {
                boolean updated = updateAlbumMasterModel( grpId, updateIterator.next());
                if ( ! updated ) {
                    db.endTransaction();
                    return false;
                }
            }

            //deleting deleted records from db
            Iterator<AlbumData> deletedIterator = deleteRecords.iterator();

            while ( deletedIterator.hasNext() ) {
                //boolean deleted = deleteGroupMasterModel(groupId, masterUid, deletedIterator.next().longValue());

                boolean deleted = deleteGroupMasterModel(grpId, deletedIterator.next());
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
    public AlbumPhotoData getPhotoData(String photoid) {
        try {
            Cursor cursor = db.rawQuery("select * from album_photo_master where photoId="+photoid, null);
            if ( cursor.moveToNext()) {
                String grpId = cursor.getString(cursor.getColumnIndex(Tables.AlbumPhotoMaster.Columns.GRP_ID));
                String albumId = cursor.getString(cursor.getColumnIndex(Tables.AlbumPhotoMaster.Columns.ALBUM_ID));
                String description = cursor.getString(cursor.getColumnIndex(Tables.AlbumPhotoMaster.Columns.DESCRIPTION));
                String photoId = cursor.getString(cursor.getColumnIndex(Tables.AlbumPhotoMaster.Columns.PHOTO_ID));
                String url = cursor.getString(cursor.getColumnIndex(Tables.AlbumPhotoMaster.Columns.URL));

                AlbumPhotoData data = new AlbumPhotoData(grpId, albumId, description, photoId, url);
                return data;
            }

            return null;
        } catch(Exception e) {
            Log.e("ErrorGettingPhoto", "Error while loading photo data");
            e.printStackTrace();
        }

        return null;
    }

    public void printTable() {
        Log.e("---------", "-------------Start of gallery master-----------------");
        Cursor cursor = db.rawQuery("select * from gallery_master", null);

        int n = cursor.getColumnCount();
        String columns = "";
        for(int i=0;i<n;i++) {
            columns = columns +  cursor.getColumnName(i) + " - ";
        }
        Log.e("ColumnName", columns);

        while(cursor.moveToNext()) {
            String rec = "";
            for(int i=0;i<n;i++) {
                rec = rec + cursor.getString(i) +" - ";
            }
            Log.e("row", rec);
        }

        cursor.close();
        Log.e("---------", "-----------End of gallery master-------------------");
    }


    public ArrayList<AlbumData> getAlbumByName (String name,String groupId,String moduleIdd){
        try{
            ArrayList<AlbumData> list = new ArrayList<>();
            Cursor cur = db.rawQuery("Select * from gallery_master where (title like '%"+name+"%' or description like '%"+name+"%') and groupId=" + groupId + " " + "and moduleId=" + moduleIdd,null);

            while(cur.moveToNext()){
                String albumId = cur.getString(cur.getColumnIndex(Tables.AlbumMaster.Columns.ALBUM_ID));
                String title = cur.getString(cur.getColumnIndex(Tables.AlbumMaster.Columns.TITLE));
                String description = cur.getString(cur.getColumnIndex(Tables.AlbumMaster.Columns.DESCRIPTION));
                String image = cur.getString(cur.getColumnIndex(Tables.AlbumMaster.Columns.ALBUM_IMAGE));
                String isAdmin = cur.getString(cur.getColumnIndex(Tables.AlbumMaster.Columns.IS_GROUP_ADMIN));
                String moduleId = cur.getString(cur.getColumnIndex(Tables.AlbumMaster.Columns.MODULE_ID));

                AlbumData ad = new AlbumData(albumId, title, description, image, groupId, isAdmin,moduleId);
                list.add(ad);
            }
            cur.close();
            return list;
        }catch(Exception e){
            e.printStackTrace();
            Log.d("Touchbase♦♦♦♦", e.toString());
            return null;
        }
    }
}
