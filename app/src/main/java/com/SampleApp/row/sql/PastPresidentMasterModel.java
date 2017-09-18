package com.SampleApp.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.SampleApp.row.Data.PastPresidentData;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by admin on 12-05-2017.
 */

public class PastPresidentMasterModel {

    Context context;
    SQLiteDatabase db;
    public PastPresidentMasterModel(Context context) {
        this.context = context;
        db = DBConnection.getInstance(context);
    }

    public boolean syncData(long grpId, ArrayList<PastPresidentData> newRecords, ArrayList<PastPresidentData> updatedRecords, ArrayList<PastPresidentData> deleteRecords) {
        db.beginTransaction();
        try {
            // inserting new records in to table
            Iterator<PastPresidentData> newIterator = newRecords.iterator();
            while (newIterator.hasNext()) {
                long id = insert(newIterator.next());
                if ( id == -1 ) {
                    db.endTransaction();
                    return false;
                }
            }

            //upadting updated records in db
            Iterator<PastPresidentData> updateIterator = updatedRecords.iterator();

            while ( updateIterator.hasNext() ) {
                boolean updated = updatePastPresidentMasterModel( grpId, updateIterator.next());
                if ( ! updated ) {
                    db.endTransaction();
                    return false;
                }
            }

            //deleting deleted records from db
            Iterator<PastPresidentData> deletedIterator = deleteRecords.iterator();

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

    public long insert( PastPresidentData ppd) {
        long id = - 1;
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.PastPresidentMaster.Columns.PASTPRESIDENT_ID, Integer.parseInt(ppd.getPastPresidentId()));
            values.put(Tables.PastPresidentMaster.Columns.TENURE, ppd.getTenureYear());
            values.put(Tables.PastPresidentMaster.Columns.PHOTO_PATH, ppd.getPhotopath());
            values.put(Tables.PastPresidentMaster.Columns.GROUP_ID, ppd.getGrpID());
            values.put(Tables.PastPresidentMaster.Columns.MEMBERNAME,ppd.getMemberName());

            return db.insert(Tables.PastPresidentMaster.TABLE_NAME, null, values);
        } catch(Exception e) {
            e.printStackTrace();
            return id;
        }
    }

    public boolean updatePastPresidentMasterModel(long grpId, PastPresidentData ppd) {
        try {
            ContentValues values = new ContentValues();

            values.put(Tables.PastPresidentMaster.Columns.PASTPRESIDENT_ID, Integer.parseInt(ppd.getPastPresidentId()));
            values.put(Tables.PastPresidentMaster.Columns.TENURE, ppd.getTenureYear());
            values.put(Tables.PastPresidentMaster.Columns.PHOTO_PATH, ppd.getPhotopath());
            values.put(Tables.PastPresidentMaster.Columns.GROUP_ID, ppd.getGrpID());
            values.put(Tables.PastPresidentMaster.Columns.MEMBERNAME, ppd.getMemberName());

            boolean  dataAvailable = pastPresidentExists(grpId,ppd.getPastPresidentId());
            int n = 0;
            if(dataAvailable) {
                try {
                    n = db.update(Tables.PastPresidentMaster.TABLE_NAME, values, "groupId=? and pastPresidentId=?", new String[]{"" + grpId, "" + ppd.getPastPresidentId()});
                    return n == 1;
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                long id =  insert(ppd);
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


    public boolean pastPresidentExists(long grpId,String pastPresidentId) {
        Cursor cursor = db.rawQuery("select * from PastPresidentMaster where groupId="+ grpId + " and pastPresidentId=" + pastPresidentId , null);

        if (cursor.getCount() > 0 ) return true;

        return false;
    }


    public boolean deleteGroupMasterModel(long grpId,PastPresidentData data) {
        try {
            int n = 0;
            boolean  dataAvailable = isdeletePastPresidentExists(grpId,data.getPastPresidentId());
            if(dataAvailable) {
                n = db.delete(Tables.PastPresidentMaster.TABLE_NAME,  "groupId=? and pastPresidentId=?", new String[]{"" + grpId, "" + data.getPastPresidentId()});
                return n == 1;  // if n = 1 it will return true else will return false
            }else{
                return true;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    public boolean isdeletePastPresidentExists(long grpId,String pastPresidentId) {
        Cursor cursor = db.rawQuery("select * from PastPresidentMaster where groupId="+ grpId + " and pastPresidentId=" + pastPresidentId , null);

        if (cursor.getCount() > 0 ) return true;

        return false;
    }

    public ArrayList<PastPresidentData> getPastPresidentList(String grpId) {
        try {
            ArrayList<PastPresidentData> list = new ArrayList<PastPresidentData>();
            Cursor cursor = db.rawQuery("select * from PastPresidentMaster where groupId=" + grpId+" order by "+Tables.PastPresidentMaster.Columns.TENURE+" desc, "+Tables.PastPresidentMaster.Columns.MEMBERNAME, null);

            while (cursor.moveToNext()) {
                String tenureYear = cursor.getString(cursor.getColumnIndex(Tables.PastPresidentMaster.Columns.TENURE));
                String photopath = cursor.getString(cursor.getColumnIndex(Tables.PastPresidentMaster.Columns.PHOTO_PATH));
                String pastPresidentId = cursor.getString(cursor.getColumnIndex(Tables.PastPresidentMaster.Columns.PASTPRESIDENT_ID));
                String memberName = cursor.getString(cursor.getColumnIndex(Tables.PastPresidentMaster.Columns.MEMBERNAME));
                String grpID = cursor.getString(cursor.getColumnIndex(Tables.PastPresidentMaster.Columns.GROUP_ID));

                PastPresidentData ppd = new PastPresidentData(tenureYear,  photopath, pastPresidentId, memberName, grpID);
                list.add(ppd);

            }
            cursor.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Touchbase", e.toString());
            return null;
        }
    }

    public boolean isDataAvailable(long groupId) {
        Cursor cursor = db.rawQuery("select * from PastPresidentMaster where groupId="+ groupId , null);

        if (cursor.getCount() > 0 ) {
            cursor.close();
            return true;
        }

        return false;
    }


    public ArrayList<PastPresidentData> search (String query,String groupId){
        try{
            query = query.replace(" ", "%");
            ArrayList<PastPresidentData> list = new ArrayList<>();
            Cursor cursor = db.rawQuery("Select * from PastPresidentMaster where (memberName like '%"+query+"%' or tenureYear like '%"+query+"%') and groupId=" + groupId + " " ,null);

            while(cursor.moveToNext()){
                String tenureYear = cursor.getString(cursor.getColumnIndex(Tables.PastPresidentMaster.Columns.TENURE));
                String photopath = cursor.getString(cursor.getColumnIndex(Tables.PastPresidentMaster.Columns.PHOTO_PATH));
                String pastPresidentId = cursor.getString(cursor.getColumnIndex(Tables.PastPresidentMaster.Columns.PASTPRESIDENT_ID));
                String memberName = cursor.getString(cursor.getColumnIndex(Tables.PastPresidentMaster.Columns.MEMBERNAME));
                String grpID = cursor.getString(cursor.getColumnIndex(Tables.PastPresidentMaster.Columns.GROUP_ID));

                PastPresidentData ppd = new PastPresidentData(tenureYear,  photopath, pastPresidentId, memberName, grpID);
                list.add(ppd);

            }
            cursor.close();
            return list;
        }catch(Exception e){
            e.printStackTrace();
            Log.d("Touchbase♦♦♦♦", e.toString());
            return null;
        }
    }


}
