package com.SampleApp.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.SampleApp.row.Data.ReplicaInfoData;

import java.util.ArrayList;

/**
 * Created by USER1 on 15-11-2016.
 */
public class ReplicaInfoModel {
    Context context;
    SQLiteDatabase db;


    public ReplicaInfoModel(Context context) {
        this.context = context;
        db = DBConnection.getInstance(context);
    }

    public long insert(ReplicaInfoData data) {
        ContentValues values = new ContentValues();
        values.put(Tables.ReplicaInfo.Columns.MODULE_ID, data.getModuleId());
        values.put(Tables.ReplicaInfo.Columns.REPLICA_OF, data.getReplicaOf());
        long n = db.insert(Tables.ReplicaInfo.TABLE_NAME, null, values);
        Log.e("Touchbase", "♦♦♦♦Replica info : "+data);
        return n;
    }

    public boolean insert(ArrayList<ReplicaInfoData> list) {
        db.beginTransaction();
        try {
            //db.delete(Tables.ReplicaInfo.TABLE_NAME, null, null);
            int n = list.size();

            for(int i=0;i<n;i++) {
                long id = insert(list.get(i));
                if ( id == -1 ) {
                    db.endTransaction();
                    Log.e("Touchbase", "♦♦♦♦Problem occured while inserting record "+list.get(i));
                    return false;
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();

            return true;
        } catch(SQLiteException se) {
            db.endTransaction();
        }
        return false;
    }

    public String getReplicaOf(String moduleId) {
        try {
            Cursor cursor = db.rawQuery("select "+Tables.ReplicaInfo.Columns.REPLICA_OF+" from "+Tables.ReplicaInfo.TABLE_NAME+" where moduleId=?", new String[]{moduleId});
            if ( cursor.moveToNext() ) {
                String replicaOf = cursor.getString(0);
                cursor.close();

                return replicaOf;
            }
            cursor.close();
        } catch(SQLiteException se) {
            Log.e("Touchbase", "♦♦♦♦Error while getting replica info of module");
            se.printStackTrace();
        }

        return "0";
    }
    public void printTable() {
        Cursor cursor = db.rawQuery("select * from "+Tables.ReplicaInfo.TABLE_NAME, null);
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
            Log.e("row", "♦♦♦♦"+rec);
        }

        cursor.close();
    }

    public int getLastModuleId() {
        int lastModuleId = 0;
        try {
            Cursor cursor = db.query(Tables.ReplicaInfo.TABLE_NAME, null, null, null, null, null, null);
            while ( cursor.moveToNext() ) {
                int moduleId = Integer.parseInt(cursor.getString(1)); // Column name : moduleId
                if ( moduleId > lastModuleId ) {
                    lastModuleId  = moduleId;
                }
            }

            return lastModuleId;
        } catch(SQLiteException se) {
            Log.e("Touchbase", "♦♦♦♦Error while getting last module id");
            se.printStackTrace();
        }

        return lastModuleId;
    }
}
