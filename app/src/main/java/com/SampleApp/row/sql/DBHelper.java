package com.SampleApp.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.SampleApp.row.Data.ReplicaInfoData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by USER on 01-07-2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "NewTouchbaseDB";
    public DBHelper(Context context) {
        super(context, DB_NAME, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            /*
            * Creating all the tables one by one
            * */
            Utils.log("Inside onCreate function");
            Iterator<String> iterator = Tables.createTableList.iterator();
            while(iterator.hasNext()) {
                String createTable = iterator.next();
                db.execSQL(createTable);
                Utils.log("Table : "+createTable +" has created");
            }

            /*
            * Inserting records of basic module. Basic modules are those modules whose replica is actually created later on.
            * Replica id of basic module is the module id itself
            * */

            insertBasicModuleData(db);
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            db.endTransaction();
        }
    }

    /*
    * In onUpgrade all the existing tables will be deleted and database will be recreated with new required tables.*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.beginTransaction();
        try {

            Utils.log("Inside onUpgrade function");

            if ( oldVersion == 4 && newVersion == 5) {
                db.execSQL(Tables.CalendarMaster.CREATE_TABLE);
                Utils.log("Table created");
            }

            /*
            // Deleting all the existing tables.
            Iterator<String> deleteIterator = Tables.dropTableList.iterator();
            while (deleteIterator.hasNext()) {
                String dropTableQuery = deleteIterator.next();
                db.execSQL(dropTableQuery);
                Utils.log("Table : "+dropTableQuery+" is dropped");
            }

            *//*
            * Creating all the tables one by one
            * *//*

            Iterator<String> createIterator = Tables.createTableList.iterator();
            while(createIterator.hasNext()) {
                String createTable = createIterator.next();
                db.execSQL(createTable);
                Utils.log("Table : "+createTable +" has created");
            }

            *//*
            * Inserting records of basic module. Basic modules are those modules whose replica is actually created later on.
            * Replica id of basic module is the module id itself
            * *//*
            insertBasicModuleData(db);*/

            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            db.endTransaction();
        }
    }

    public void insertBasicModuleData(SQLiteDatabase db) {
        //Adding basic module replica info into table
        ArrayList<ReplicaInfoData> list = new ArrayList<>();
        list.add(new ReplicaInfoData(Constant.Module.DIRECTORY,Constant.Module.DIRECTORY ));  // 1
        list.add(new ReplicaInfoData(Constant.Module.EVENTS, Constant.Module.EVENTS));  // 2
        list.add(new ReplicaInfoData(Constant.Module.ANNOUNCEMENTS, Constant.Module.ANNOUNCEMENTS)); //3
        list.add(new ReplicaInfoData(Constant.Module.E_BULLETINS, Constant.Module.E_BULLETINS)); // 4
        list.add(new ReplicaInfoData(Constant.Module.SUB_GROUPS, Constant.Module.SUB_GROUPS)); //5
        list.add(new ReplicaInfoData(Constant.Module.CELEBRATIONS, Constant.Module.CELEBRATIONS)); // 6
        list.add(new ReplicaInfoData(Constant.Module.MEETINGS, Constant.Module.MEETINGS)); // 7
        list.add(new ReplicaInfoData(Constant.Module.GALLERY, Constant.Module.GALLERY)); // 8
        list.add(new ReplicaInfoData(Constant.Module.DOCUMENTS, Constant.Module.DOCUMENTS)); // 9
        list.add(new ReplicaInfoData(Constant.Module.INFO, Constant.Module.INFO)); // 10
        list.add(new ReplicaInfoData(Constant.Module.CHAT, Constant.Module.CHAT)); // 11
        list.add(new ReplicaInfoData(Constant.Module.TASK, Constant.Module.TASK)); // 12
        //There is no module with id 13 by default
        list.add(new ReplicaInfoData(Constant.Module.TICKETING, Constant.Module.TICKETING)); // 14
        list.add(new ReplicaInfoData(Constant.Module.SERVICE_DIRECTORY, Constant.Module.SERVICE_DIRECTORY)); // 15
        list.add(new ReplicaInfoData(Constant.Module.FEEDBACK, Constant.Module.FEEDBACK)); // 16
        list.add(new ReplicaInfoData(Constant.Module.ATTENDANCE, Constant.Module.ATTENDANCE)); // 17
        list.add(new ReplicaInfoData(Constant.Module.IMPROVEMENT, Constant.Module.IMPROVEMENT)); //18
        list.add(new ReplicaInfoData(Constant.Module.BOARD_OF_DIRECTORS, Constant.Module.BOARD_OF_DIRECTORS)); //26
        list.add(new ReplicaInfoData(Constant.Module.FIND_A_ROTARIAN, Constant.Module.FIND_A_ROTARIAN)); //27
        list.add(new ReplicaInfoData(Constant.Module.WEBLINKS, Constant.Module.WEBLINKS)); //28
        list.add(new ReplicaInfoData(Constant.Module.PAST_PRESIDENTS, Constant.Module.PAST_PRESIDENTS)); //29
        list.add(new ReplicaInfoData(Constant.Module.NEAR_ME, Constant.Module.NEAR_ME)); //30
        list.add(new ReplicaInfoData(Constant.Module.CLUBHISTORY, Constant.Module.CLUBHISTORY)); //31
        list.add(new ReplicaInfoData(Constant.Module.ROTARY_LIBRARY, Constant.Module.ROTARY_LIBRARY)); //32

        Iterator<ReplicaInfoData> iterator = list.iterator();
        while (iterator.hasNext()) {
            ReplicaInfoData data = iterator.next();
            ContentValues values = new ContentValues();
            values.put(Tables.ReplicaInfo.Columns.MODULE_ID, data.getModuleId());
            values.put(Tables.ReplicaInfo.Columns.REPLICA_OF, data.getReplicaOf());
            long id = db.insert(Tables.ReplicaInfo.TABLE_NAME, null, values);
            Log.e("Touchbase", "♦♦♦♦Replica info inserted with id : "+id);
        }
        //Ending of adding basic module replica info into table
    }



    /*@Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(Tables.GroupMaster.CREATE_TABLE);
            db.execSQL(Tables.DirectoryDataMaster.CREATE_TABLE);
            db.execSQL(Tables.AnnouncementDataMaster.CREATE_TABLE);
            db.execSQL(Tables.ModuleDataMaster.CREATE_TABLE);
            db.execSQL(Tables.ServiceDIrectoryDataMaster.CREATE_TABLE);
            db.execSQL(Tables.AlbumMaster.CREATE_TABLE);
            db.execSQL(Tables.AlbumPhotoMaster.CREATE_TABLE);
            db.execSQL(Tables.AttendanceMaster.CREATE_TABLE);
            db.execSQL(Tables.ReplicaInfo.CREATE_TABLE);
            db.execSQL(Tables.UploadedPhoto.CREATE_TABLE);
            db.execSQL(Tables.CalendarMaster.CREATE_TABLE);
            insertBasicModuleData(db);

            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if ( oldVersion == 1 && newVersion == 2 ) {
            db.beginTransaction();
            try {
                db.execSQL(Tables.ReplicaInfo.CREATE_TABLE);
                // new Added Gallery Table
                db.execSQL(Tables.UploadedPhoto.CREATE_TABLE);
                db.execSQL(Tables.CalendarMaster.CREATE_TABLE);
                insertBasicModuleData(db);
                db.setTransactionSuccessful();
                db.endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
                db.endTransaction();
            }
        } else if( oldVersion == 2 && newVersion == 3){
            db.execSQL(Tables.ServiceDIrectoryDataMaster.ADDCOLUMN_CATTEGORY_ID);
            db.execSQL(Tables.ServiceDIrectoryDataMaster.ADDCOLUMN_WEBSITELINK);
        }
    }
    */


}
