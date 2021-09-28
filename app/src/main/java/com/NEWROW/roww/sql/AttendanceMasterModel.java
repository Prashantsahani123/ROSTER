package com.NEWROW.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.NEWROW.row.Data.AttendanceData;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by USER on 07-10-2016.
 */
public class AttendanceMasterModel {
    Context context;
    SQLiteDatabase db;

    public AttendanceMasterModel(Context context) {
        this.context = context;
        db = DBConnection.getInstance(context);
    }

    public long insert(long masterUid, AttendanceData ad) {
        long id = - 1;
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.AttendanceMaster.Columns.MASTER_UID, masterUid);
            values.put(Tables.AttendanceMaster.Columns.MODULE_ID, ad.getModuleId());
            values.put(Tables.AttendanceMaster.Columns.MEMBER_ID, ad.getMemberId());
            values.put(Tables.AttendanceMaster.Columns.STUDENT_ID, ad.getId());
            values.put(Tables.AttendanceMaster.Columns.NAME,ad.getName());
            values.put(Tables.AttendanceMaster.Columns.ATTENDANCE, ad.getAttendance());
            values.put(Tables.AttendanceMaster.Columns.MONTH, ad.getMonth());
            values.put(Tables.AttendanceMaster.Columns.YEAR, ad.getYear());


            return db.insert(Tables.AttendanceMaster.TABLE_NAME, null, values);
        } catch(Exception e) {
            e.printStackTrace();
            return id;
        }
    }

    public int update(long masterUid, AttendanceData ad) {

        try {
            ContentValues values = new ContentValues();
            values.put(Tables.AttendanceMaster.Columns.MASTER_UID, masterUid);
            values.put(Tables.AttendanceMaster.Columns.MODULE_ID, ad.getModuleId());
            values.put(Tables.AttendanceMaster.Columns.MEMBER_ID, ad.getMemberId());
            values.put(Tables.AttendanceMaster.Columns.STUDENT_ID, ad.getId());
            values.put(Tables.AttendanceMaster.Columns.NAME,ad.getName());
            values.put(Tables.AttendanceMaster.Columns.ATTENDANCE, ad.getAttendance());
            values.put(Tables.AttendanceMaster.Columns.MONTH, ad.getMonth());
            values.put(Tables.AttendanceMaster.Columns.YEAR, ad.getYear());
            String whereClause = " " +
                    "masterUID=? and memberId=? and month=? and year=? and moduleId=?";
            String[] whereClauseArgs = new String[]{""+masterUid, ad.getMemberId(), ad.getMonth(), ad.getYear(), ad.getModuleId() };
            return db.update(Tables.AttendanceMaster.TABLE_NAME, values,whereClause, whereClauseArgs );
        } catch(Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public boolean attendanceExists(long masterUid, AttendanceData ad) {
        boolean exist = false;
        String query = "select * from attendance_master where masterUID=? and memberId=? and month=? and year=? and moduleId=?";

        Cursor cur = db.rawQuery(query, new String[]{""+masterUid, ad.getMemberId(), ad.getMonth(), ad.getYear(), ad.getModuleId() });
        if ( cur.getCount() > 0 ) {
            exist = true;
        }
        return exist;
    }
    public boolean insert(long masterUid, ArrayList<AttendanceData> list) {
        boolean saved = true;
        db.beginTransaction();
        try {
            Iterator<AttendanceData> iterator = list.iterator();
            while ( iterator.hasNext() ) {
                AttendanceData gd = iterator.next();

                /*Checking if attendance already exists for the given user.
                * If attendance already exists, we have to update existing
                * If attendance does not exist, we have to insert it as new record
                * */
                boolean attendanceExist = attendanceExists(masterUid, gd);

                Log.e("TouchBase", "♦♦♦♦Attendance exists : "+attendanceExist);


                if ( attendanceExist ) {
                    int n = update(masterUid, gd);
                    if ( n == -1 ) {
                        db.endTransaction();
                        return false;
                    }
                } else {
                    long id = insert(masterUid, gd);
                    if (id == -1) {
                        db.endTransaction();
                        return false;
                    }
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

    public ArrayList<AttendanceData> getAttendanceList(long masterUid,long memId,String mon,String yr) {
        try {
            ArrayList<AttendanceData> list = new ArrayList<AttendanceData>();
            String query = "select * from attendance_master where masterUID="+masterUid + " and memberId=" + memId + " and month='" + mon + "'" +  " and year='" + yr + "'";
            Log.e("Query", query);
            Cursor cursor = db.rawQuery(query, null);

            while ( cursor.moveToNext() ) {
                String memberId = cursor.getString(cursor.getColumnIndex(Tables.AttendanceMaster.Columns.MEMBER_ID));
                String moduleId = cursor.getString(cursor.getColumnIndex(Tables.AttendanceMaster.Columns.MODULE_ID));
                String studId= cursor.getString(cursor.getColumnIndex(Tables.AttendanceMaster.Columns.STUDENT_ID));
                String name= cursor.getString(cursor.getColumnIndex(Tables.AttendanceMaster.Columns.NAME));
                String attendance= cursor.getString(cursor.getColumnIndex(Tables.AttendanceMaster.Columns.ATTENDANCE));
                String month= cursor.getString(cursor.getColumnIndex(Tables.AttendanceMaster.Columns.MONTH));
                String year= cursor.getString(cursor.getColumnIndex(Tables.AttendanceMaster.Columns.YEAR));
                boolean box = false;
                AttendanceData gd = new AttendanceData(studId,name,moduleId,attendance, memberId,month,year);
                list.add(gd);
            }
            //if (list.size() == 0 ) return null;
            return list;
        } catch(Exception e) {
            e.printStackTrace();
            Log.d("Touchbase", e.toString());
            return null;
        }
    }
    public void printTable() {
        Cursor cursor = db.rawQuery("select * from attendance_master", null);
        int n = cursor.getColumnCount();
        while(cursor.moveToNext()) {
            String rec = "";
            for(int i=0;i<n;i++) {
                rec = rec + cursor.getString(i) +" - ";
            }
            Log.e("row", rec);
        }
    }

    public boolean isDataAvailable(long memberProfileID) {
        Cursor cursor = db.rawQuery("select * from attendance_master where memberId="+memberProfileID, null);

        if (cursor.getCount() > 0 ) return true;

        return false;
    }

//======================================Unused =======================================


   /* public ArrayList<AttendanceData>getAttendanceList(long masterUID,String cattegory) {
        try {
            ArrayList<AttendanceData> list = new ArrayList<AttendanceData>();
            Cursor cursor = db.rawQuery("select * from attendance_master where masterUID="+ masterUID + " and myCategory='" + cattegory + "'",null);
            while (cursor.moveToNext()){
                String memId = cursor.getString(cursor.getColumnIndex(Tables.AttendanceMaster.Columns.MEMBER_ID));
                String studId= cursor.getString(cursor.getColumnIndex(Tables.AttendanceMaster.Columns.STUDENT_ID));
                String name= cursor.getString(cursor.getColumnIndex(Tables.AttendanceMaster.Columns.NAME));
                String attendance= cursor.getString(cursor.getColumnIndex(Tables.AttendanceMaster.Columns.ATTENDANCE));
                boolean box = false;

                AttendanceData gd = new AttendanceData(memId, studId, name, attendance);
                list.add(gd);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Touchbase",e.toString());
            return null;

        }
    }*/




}

