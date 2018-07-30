package com.SampleApp.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.SampleApp.row.Data.E_BulletineListData;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by USER1 on 08-07-2016.
 */
public class EbullletinDataModel {
    Context context;
    SQLiteDatabase db;



    public EbullletinDataModel(Context context) {
        this.context = context;
        db = DBConnection.getInstance(context);
    }

    public long insert(long masterUid, E_BulletineListData ed) {
        long id = - 1;
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.EbulletineDataMaster.Columns.MASTER_UID, masterUid);
            values.put(Tables.EbulletineDataMaster.Columns.EBULLETIN_ID, ed.getEbulletinID());
            values.put(Tables.EbulletineDataMaster.Columns.EBULLETIN_TITLE, ed.getEbulletinTitle());
            values.put(Tables.EbulletineDataMaster.Columns.EBULLETIN_LINK, ed.getEbulletinlink());
            values.put(Tables.EbulletineDataMaster.Columns.EBULLETIN_TYPE, ed.getEbulletinType());
            values.put(Tables.EbulletineDataMaster.Columns.EBULLETIN_FILTER_TYPE, ed.getFilterType());
            values.put(Tables.EbulletineDataMaster.Columns.EBULLETIN_CREATE_DATE_TIME, ed.getCreateDateTime());
            values.put(Tables.EbulletineDataMaster.Columns.EBULLETIN_PUBLISH_DATE, ed.getPublishDateTime());

            values.put(Tables.EbulletineDataMaster.Columns.EBULLETIN_EXPIRY_DATE, ed.getExpiryDateTime());
            values.put(Tables.EbulletineDataMaster.Columns.EBULLETIN_ISADMIN, ed.getIsAdmin());
            values.put(Tables.EbulletineDataMaster.Columns.EBULLETIN_ISREAD, ed.getIsRead());


            return db.insert(Tables.EbulletineDataMaster.TABLE_NAME, null, values);
        } catch(Exception e) {
            e.printStackTrace();
            return id;
        }
    }

    public boolean insert(long masterUid, ArrayList<E_BulletineListData> list) {
        boolean saved = true;
        db.beginTransaction();
        try {
            Iterator<E_BulletineListData> iterator = list.iterator();
            while ( iterator.hasNext() ) {
                E_BulletineListData ed = iterator.next();
                long id = insert(masterUid, ed);
                if ( id == -1 ) {
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

    public ArrayList<E_BulletineListData> getEbulletineData(long masterUid) {
        try {
            ArrayList<E_BulletineListData> list = new ArrayList<E_BulletineListData>();
            Cursor cursor = db.rawQuery("select * from ebulletine_data_master where masterUID="+masterUid, null);

            while ( cursor.moveToNext() ) {


                String ebulletinId = cursor.getString(cursor.getColumnIndex(Tables.EbulletineDataMaster.Columns.EBULLETIN_ID));
                String ebulletinTitle = cursor.getString(cursor.getColumnIndex(Tables.EbulletineDataMaster.Columns.EBULLETIN_TITLE));
                String ebulletinLink= cursor.getString(cursor.getColumnIndex(Tables.EbulletineDataMaster.Columns.EBULLETIN_LINK));
                String ebulletinType= cursor.getString(cursor.getColumnIndex(Tables.EbulletineDataMaster.Columns.EBULLETIN_TYPE));
                String ebulletinFilterType = cursor.getString(cursor.getColumnIndex(Tables.EbulletineDataMaster.Columns.EBULLETIN_FILTER_TYPE));
                String ebulletinCreateDateTime= cursor.getString(cursor.getColumnIndex(Tables.EbulletineDataMaster.Columns.EBULLETIN_CREATE_DATE_TIME));
                String ebulletinPublishDate= cursor.getString(cursor.getColumnIndex(Tables.EbulletineDataMaster.Columns.EBULLETIN_PUBLISH_DATE));

                String ebulletinExpiryDate = cursor.getString(cursor.getColumnIndex(Tables.EbulletineDataMaster.Columns.EBULLETIN_EXPIRY_DATE));
                String ebulletinIsAdmin= cursor.getString(cursor.getColumnIndex(Tables.EbulletineDataMaster.Columns.EBULLETIN_ISADMIN));
                String ebulletinIsRead= cursor.getString(cursor.getColumnIndex(Tables.EbulletineDataMaster.Columns.EBULLETIN_ISREAD));

                boolean box = false;

                E_BulletineListData ed = new E_BulletineListData(""+ebulletinId, ebulletinTitle, ebulletinLink, ebulletinType, ebulletinFilterType, ebulletinCreateDateTime,ebulletinPublishDate,ebulletinExpiryDate,ebulletinIsAdmin,ebulletinIsRead);
                list.add(ed);
            }
            if (list.size() == 0 ) return null;
            return list;
        } catch(Exception e) {
            e.printStackTrace();
            Log.d("Touchbase", e.toString());
            return null;
        }
    }

    public void printTable() {
        Cursor cursor = db.rawQuery("select * from ebulletine_data_master", null);
        int n = cursor.getColumnCount();
        while(cursor.moveToNext()) {
            String rec = "";
            for(int i=0;i<n;i++) {
                rec = rec + cursor.getString(i) +" - ";
            }
            Log.e("row", rec);
        }
    }

    public boolean isDataAvailable() {
        Cursor cursor = db.rawQuery("select * from ebulletine_data_master", null);

        if (cursor.getCount() > 0 ) return true;

        return false;
    }
}

