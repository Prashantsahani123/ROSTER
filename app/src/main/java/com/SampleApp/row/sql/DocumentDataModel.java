package com.SampleApp.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.SampleApp.row.Data.DocumentListData;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by USER1 on 08-07-2016.
 */
public class DocumentDataModel {
    Context context;
    SQLiteDatabase db;



    public DocumentDataModel(Context context) {
        this.context = context;
        db = DBConnection.getInstance(context);
    }

    public long insert(long masterUid, DocumentListData dd) {
        long id = - 1;
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.DocumentDataMaster.Columns.MASTER_UID, masterUid);
            values.put(Tables.DocumentDataMaster.Columns.DOCUMENT_ID, dd.getDocID());
            values.put(Tables.DocumentDataMaster.Columns.DOCUMENT_TITLE, dd.getDocTitle());
            values.put(Tables.DocumentDataMaster.Columns.DOCUMENT_TYPE, dd.getDocType());
            values.put(Tables.DocumentDataMaster.Columns.DOCUMENT_URL, dd.getDocURL());
            values.put(Tables.DocumentDataMaster.Columns.DOCUMENT_CREATE_DATE_TIME, dd.getCreateDateTime());
            values.put(Tables.DocumentDataMaster.Columns.ACCESS_TYPE, dd.getAccessType());
            values.put(Tables.DocumentDataMaster.Columns.ACCESS_TYPE, dd.getIsRead());



            return db.insert(Tables.DocumentDataMaster.TABLE_NAME, null, values);
        } catch(Exception e) {
            e.printStackTrace();
            return id;
        }
    }

    public boolean insert(long masterUid, ArrayList<DocumentListData> list) {
        boolean saved = true;
        db.beginTransaction();
        try {
            Iterator<DocumentListData> iterator = list.iterator();
            while ( iterator.hasNext() ) {
                DocumentListData gd = iterator.next();
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

    public ArrayList<DocumentListData> getDocumentData(long masterUid) {
        try {
            ArrayList<DocumentListData> list = new ArrayList<DocumentListData>();
            Cursor cursor = db.rawQuery("select * from document_data_master where masterUID="+masterUid, null);

            while ( cursor.moveToNext() ) {


                String documentId = cursor.getString(cursor.getColumnIndex(Tables.DocumentDataMaster.Columns.DOCUMENT_ID));
                String documentTitle = cursor.getString(cursor.getColumnIndex(Tables.DocumentDataMaster.Columns.DOCUMENT_TITLE));
                String documentType= cursor.getString(cursor.getColumnIndex(Tables.DocumentDataMaster.Columns.DOCUMENT_TYPE));
                String documentUrl= cursor.getString(cursor.getColumnIndex(Tables.DocumentDataMaster.Columns.DOCUMENT_URL));
                String documentCreateDateTime = cursor.getString(cursor.getColumnIndex(Tables.DocumentDataMaster.Columns.DOCUMENT_CREATE_DATE_TIME));
                String accessType = cursor.getString(cursor.getColumnIndex(Tables.DocumentDataMaster.Columns.ACCESS_TYPE));
                String isRead = cursor.getString(cursor.getColumnIndex(Tables.DocumentDataMaster.Columns.IS_READ));


                boolean box = false;

                DocumentListData dd = new DocumentListData(""+documentId,documentTitle, documentType, documentUrl, documentCreateDateTime,accessType,isRead,"");
                list.add(dd);
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
        Cursor cursor = db.rawQuery("select * from document_data_master", null);
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
        Cursor cursor = db.rawQuery("select * from document_data_master", null);

        if (cursor.getCount() > 0 ) return true;

        return false;
    }
}
