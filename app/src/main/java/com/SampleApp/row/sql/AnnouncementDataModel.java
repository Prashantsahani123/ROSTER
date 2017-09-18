package com.SampleApp.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

import com.SampleApp.row.Data.AnnouncementListData;


/**
 * Created by USER1 on 06-07-2016.
 */
public class AnnouncementDataModel {
    Context context;
    SQLiteDatabase db;



    public AnnouncementDataModel(Context context) {
        this.context = context;
        db = DBConnection.getInstance(context);
    }

    public long insert(long masterUid, AnnouncementListData ad) {
        long id = - 1;
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.AnnouncementDataMaster.Columns.MASTER_UID, masterUid);
            values.put(Tables.AnnouncementDataMaster.Columns.ANNOUNCE_ID, ad.getAnnounID());
            values.put(Tables.AnnouncementDataMaster.Columns.ANNOUNCE_TITLE, ad.getAnnounTitle());
            values.put(Tables.AnnouncementDataMaster.Columns.ANNOUNCE_DESC, ad.getAnnounceDEsc());
            values.put(Tables.AnnouncementDataMaster.Columns.ANNOUNCE_CREATE_DATE_TIME, ad.getCreateDateTime());
            values.put(Tables.AnnouncementDataMaster.Columns.ANNOUNCE_PUBLISH_DATE_TIME, ad.getPublishDateTime());
            values.put(Tables.AnnouncementDataMaster.Columns.ANNOUNCE_EXPIRY_DATE_TIME, ad.getExpiryDateTime());
            values.put(Tables.AnnouncementDataMaster.Columns.ANNOUNCE_ISADMIN, ad.getIsAdmin());
            values.put(Tables.AnnouncementDataMaster.Columns.ANNOUNCE_FILTER_TYPE, ad.getFilterType());
            values.put(Tables.AnnouncementDataMaster.Columns.ANNOUNCE_ISREAD, ad.getIsRead());

            return db.insert(Tables.AnnouncementDataMaster.TABLE_NAME, null, values);
        } catch(Exception e) {
            e.printStackTrace();
            return id;
        }
    }

    public boolean insert(long masterUid, ArrayList<AnnouncementListData> list) {
        boolean saved = true;
        db.beginTransaction();
        try {
            Iterator<AnnouncementListData> iterator = list.iterator();
            while ( iterator.hasNext() ) {
                AnnouncementListData gd = iterator.next();
                long id = insert(masterUid, gd);
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

    public ArrayList<AnnouncementListData> getAnnouncementListDatas(long masterUid) {
        try {
            ArrayList<AnnouncementListData> list = new ArrayList<AnnouncementListData>();
            Cursor cursor = db.rawQuery("select * from announcement_data_master where masterUID="+masterUid, null);

            while ( cursor.moveToNext() ) {


                String announceId = cursor.getString(cursor.getColumnIndex(Tables.AnnouncementDataMaster.Columns.ANNOUNCE_ID));
                String announceTitle = cursor.getString(cursor.getColumnIndex(Tables.AnnouncementDataMaster.Columns.ANNOUNCE_TITLE));
                String announceDesc= cursor.getString(cursor.getColumnIndex(Tables.AnnouncementDataMaster.Columns.ANNOUNCE_DESC));
                String announceCreateDateTime= cursor.getString(cursor.getColumnIndex(Tables.AnnouncementDataMaster.Columns.ANNOUNCE_CREATE_DATE_TIME));
                String announcePublishDateTime = cursor.getString(cursor.getColumnIndex(Tables.AnnouncementDataMaster.Columns.ANNOUNCE_PUBLISH_DATE_TIME));
                String announceExpiryDateTime= cursor.getString(cursor.getColumnIndex(Tables.AnnouncementDataMaster.Columns.ANNOUNCE_EXPIRY_DATE_TIME));
                String announceIsAdmin= cursor.getString(cursor.getColumnIndex(Tables.AnnouncementDataMaster.Columns.ANNOUNCE_ISADMIN));

                String announceFilterType= cursor.getString(cursor.getColumnIndex(Tables.AnnouncementDataMaster.Columns.ANNOUNCE_FILTER_TYPE));
                String announceIsRead= cursor.getString(cursor.getColumnIndex(Tables.AnnouncementDataMaster.Columns.ANNOUNCE_ISREAD));
              //  boolean box = false;

                AnnouncementListData ad = new AnnouncementListData(announceId, announceTitle, announceDesc, announceCreateDateTime, announcePublishDateTime, announceExpiryDateTime,announceIsAdmin,announceFilterType,announceIsRead);
                list.add(ad);
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
        Cursor cursor = db.rawQuery("select * from announcement_data_master", null);
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
        Cursor cursor = db.rawQuery("select * from announcement_data_master", null);

        if (cursor.getCount() > 0 ) return true;

        return false;
    }
}
