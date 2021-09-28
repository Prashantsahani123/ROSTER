package com.NEWROW.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.NEWROW.row.Data.GroupData;
import com.NEWROW.row.Utils.DateHelper;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by USER on 01-07-2016.
 */
public class GroupMasterModel {
    Context context;
    SQLiteDatabase db;

    public GroupMasterModel(Context context) {
        this.context = context;
        db = DBConnection.getInstance(context);
    }

    public long insert(long masterUid, GroupData gd) {

        long id = - 1;

        try {

            ContentValues values = new ContentValues();
            values.put(Tables.GroupMaster.Columns.MASTER_UID, masterUid);
            values.put(Tables.GroupMaster.Columns.GROUP_ID, gd.getGrpId());
            values.put(Tables.GroupMaster.Columns.GROUP_NAME, gd.getGrpName());
            values.put(Tables.GroupMaster.Columns.GROUP_IMAGE, gd.getGrpImg());
            values.put(Tables.GroupMaster.Columns.GROUP_PROFILE_ID, gd.getGrpProfileId());
            values.put(Tables.GroupMaster.Columns.IS_GROUP_ADMIN, gd.getIsGrpAdmin());
            values.put(Tables.GroupMaster.Columns.MY_CATEGORY, gd.getMyCategory());

            PreferenceManager.savePreference(context, PreferenceManager.GROUP_EXPIRY_DATE+gd.getGrpId(), gd.getExpiryDate());

            PreferenceManager.savePreference(context, PreferenceManager.GROUP_ACTIVE_FLAG+gd.getGrpId(), gd.getExpiryFlag());

            //Log.e("mode","satish Group insert "+ gd.toString());

            boolean dataAvailable1 = groupExists(masterUid,gd.getGrpId());

            if(!dataAvailable1){

                return db.insert(Tables.GroupMaster.TABLE_NAME, null, values);
            }
            //  return db.insert(Tables.GroupMaster.TABLE_NAME, null, values);

            return 0;

        } catch(Exception e) {
            e.printStackTrace();
            return id;
        }
    }

    public boolean insert(long masterUid, ArrayList<GroupData> list) {
        boolean saved = true;
        db.beginTransaction();

        try {

            Iterator<GroupData> iterator = list.iterator();

            while ( iterator.hasNext() ) {

                Object obj = iterator.next();

                if ( obj instanceof GroupData){

                    GroupData gd = (GroupData) obj;
//                    Log.d("mode","satish Group insert by list");
                    long id = insert(masterUid, gd); //insert with list

                    if ( id == -1 ) {
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
    // myCategory : 1 - Club, 2 - District, 3 - Event
    public ArrayList<GroupData> getGroups(long masterUid) {

        try {

            ArrayList<GroupData> list = new ArrayList<GroupData>();

            Cursor cursor = db.rawQuery("select * from group_master where masterUID="+masterUid+" order by "+ Tables.GroupMaster.Columns.MY_CATEGORY, null);

            while ( cursor.moveToNext() ) {

                String grpId = cursor.getString(cursor.getColumnIndex(Tables.GroupMaster.Columns.GROUP_ID));
                String grpName= cursor.getString(cursor.getColumnIndex(Tables.GroupMaster.Columns.GROUP_NAME));
                String grpImg= cursor.getString(cursor.getColumnIndex(Tables.GroupMaster.Columns.GROUP_IMAGE));
                String grpProfileId= cursor.getString(cursor.getColumnIndex(Tables.GroupMaster.Columns.GROUP_PROFILE_ID));
                String myCategory = cursor.getString(cursor.getColumnIndex(Tables.GroupMaster.Columns.MY_CATEGORY));
                String isGrpAdmin= cursor.getString(cursor.getColumnIndex(Tables.GroupMaster.Columns.IS_GROUP_ADMIN));

                boolean box = false;

                String expiryDate = PreferenceManager.getPreference(context, PreferenceManager.GROUP_EXPIRY_DATE+grpId, DateHelper.getDateDDMMYYYY());

                String activeFlag = PreferenceManager.getPreference(context, PreferenceManager.GROUP_ACTIVE_FLAG+grpId, "0");

                Utils.log("satish Group expiry get : "+expiryDate+" active flag "+activeFlag);

                if(activeFlag.equalsIgnoreCase("0")) {

                    if (!expiryDate.equals("")) {

                        if (DateHelper.compareDate(DateHelper.getDateDDMMYYYY(), expiryDate, "dd/MM/yyyy") <= 0) {
                            GroupData gd = new GroupData(grpId, grpName, grpImg, grpProfileId, myCategory, isGrpAdmin, box);
                            gd.setExpiryFlag(activeFlag);
                            list.add(gd);
                        }

                    } else {

                        GroupData gd = new GroupData(grpId, grpName, grpImg, grpProfileId, myCategory, isGrpAdmin, box);
                        gd.setExpiryFlag(activeFlag);
                        list.add(gd);
                    }
                }

                /*if (!expiryDate.equals("")) {

                    if (DateHelper.compareDate(DateHelper.getDateDDMMYYYY(), expiryDate, "dd/MM/yyyy") <= 0) {
                        GroupData gd = new GroupData(grpId, grpName, grpImg, grpProfileId, myCategory, isGrpAdmin, box);
                        list.add(gd);
                    }

                } else {

                    GroupData gd = new GroupData(grpId, grpName, grpImg, grpProfileId, myCategory, isGrpAdmin, box);
                    list.add(gd);
                }*/
            }

            //if (list.size() == 0 ) return null;
            Utils.log("Group list is : "+list);

            return list;

        } catch(Exception e) {
            e.printStackTrace();
            Log.d("Touchbase", e.toString());
            return null;
        }
    }

    public ArrayList<GroupData> getGroups(long masterUID, String cattegory) {

        try {

            ArrayList<GroupData> list = new ArrayList<GroupData>();
            Cursor cursor = db.rawQuery("select * from group_master where masterUID="+ masterUID + " and myCategory='" + cattegory + "'",null);

            while (cursor.moveToNext()){
                String grpId = cursor.getString(cursor.getColumnIndex(Tables.GroupMaster.Columns.GROUP_ID));
                String grpName= cursor.getString(cursor.getColumnIndex(Tables.GroupMaster.Columns.GROUP_NAME));
                String grpImg= cursor.getString(cursor.getColumnIndex(Tables.GroupMaster.Columns.GROUP_IMAGE));
                String grpProfileId= cursor.getString(cursor.getColumnIndex(Tables.GroupMaster.Columns.GROUP_PROFILE_ID));
                String myCategory = cursor.getString(cursor.getColumnIndex(Tables.GroupMaster.Columns.MY_CATEGORY));
                String isGrpAdmin= cursor.getString(cursor.getColumnIndex(Tables.GroupMaster.Columns.IS_GROUP_ADMIN));
                boolean box = false;
                GroupData gd = new GroupData(grpId, grpName, grpImg, grpProfileId, myCategory, isGrpAdmin, box);
                list.add(gd);
            }

            cursor.close();

            return list;

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Touchbase",e.toString());
            return null;
        }
    }

    public void printTable() {
        Cursor cursor = db.rawQuery("select * from group_master", null);
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
        Cursor cursor = db.rawQuery("select * from group_master", null);

        if (cursor.getCount() > 0 ) return true;

        return false;
    }


    //=========================== Directory Update by Lekha ====================================

    public boolean updateGroupMasterModel(long masterUid, GroupData data) {

        try {

            ContentValues values = new ContentValues();
            values.put(Tables.GroupMaster.Columns.GROUP_ID,data.getGrpId());
            values.put(Tables.GroupMaster.Columns.GROUP_NAME,data.getGrpName());
            values.put(Tables.GroupMaster.Columns.GROUP_IMAGE,data.getGrpImg());
            values.put(Tables.GroupMaster.Columns.GROUP_PROFILE_ID,data.getGrpProfileId());
            values.put(Tables.GroupMaster.Columns.MY_CATEGORY,data.getMyCategory());
            values.put(Tables.GroupMaster.Columns.IS_GROUP_ADMIN,data.getIsGrpAdmin());

            printTable();

            // long profileId = Long.parseLong(data.getProfileID());
            boolean  dataAvailable = groupExists(masterUid,data.getGrpId());
            int n = 0;
            if(dataAvailable) {
                n = db.update(Tables.GroupMaster.TABLE_NAME, values, "masterUID=? and grpId=?", new String[]{"" + masterUid, "" + data.getGrpId()});
                PreferenceManager.savePreference(context, PreferenceManager.GROUP_EXPIRY_DATE+data.getGrpId(), data.getExpiryDate());
                PreferenceManager.savePreference(context, PreferenceManager.GROUP_ACTIVE_FLAG+data.getGrpId(), data.getExpiryFlag());
                return n >0 ;
            }else{
//                Log.d("mode","satish Group insert by update");
                long id =  insert(masterUid,data); //updateGroupMasterModel
                PreferenceManager.savePreference(context, PreferenceManager.GROUP_EXPIRY_DATE+data.getGrpId(), data.getExpiryDate());
                PreferenceManager.savePreference(context, PreferenceManager.GROUP_ACTIVE_FLAG+data.getGrpId(), data.getExpiryFlag());
                if(id>0){
                    return n >0;
                }
            }

            printTable();
            // if n = 1 it will return true else will return false

        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean groupExists(long masterUid,String groupId) {

        Cursor cursor = db.rawQuery("select * from group_master where masterUID="+ masterUid + " and grpId" + "=" +  "'" + groupId + "'" , null);

        if (cursor.getCount() > 0 ) return true;

        return false;
    }

    public boolean deleteGroupMasterModel(long masterUid,GroupData data) {

        try {

            int n = 0;
            boolean  dataAvailable = groupExists(masterUid,data.getGrpId());
            if(dataAvailable) {
                n = db.delete(Tables.GroupMaster.TABLE_NAME,  "masterUID=? and grpId=?", new String[]{"" + masterUid, "" + data.getGrpId()});
                return n == 1;  // if n = 1 it will return true else will return false
            }else{
                return true;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean isDataAvailable(long groupId) {
        Cursor cursor = db.rawQuery("select * from directory_data_master where grpId="+groupId, null);

        if (cursor.getCount() > 0 ) return true;

        return false;
    }

    public boolean syncData(long masterUid, ArrayList<GroupData> newRecords, ArrayList<GroupData> updatedRecords, ArrayList<GroupData> deleteRecords) {

        db.beginTransaction();

        try {
            // inserting new records in to table
            Iterator<GroupData> newIterator = newRecords.iterator();

            while (newIterator.hasNext()) {

//               Log.d("mode","satish Group insert by normal");

                long id = insert(masterUid, newIterator.next()); // insert

                if ( id == -1 ) {
                    db.endTransaction();
                    return false;
                }
            }

            //upadting updated records in db
            Iterator<GroupData> updateIterator = updatedRecords.iterator();

            while ( updateIterator.hasNext() ) {

                boolean updated = updateGroupMasterModel( masterUid, updateIterator.next());
                if ( ! updated ) {
                    db.endTransaction();
                    return false;
                }
            }

            //deleting deleted records from db
            Iterator<GroupData> deletedIterator = deleteRecords.iterator();

            while ( deletedIterator.hasNext() ) {
                //boolean deleted = deleteGroupMasterModel(groupId, masterUid, deletedIterator.next().longValue());

                boolean deleted = deleteGroupMasterModel(masterUid, deletedIterator.next());

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
    public boolean isGroupDataAvailable(long masterUid,String cattegory){

        Cursor cursor = db.rawQuery("select * from group_master where masterUID="+masterUid + " and myCategory" + "=" + "'" + cattegory + "'",null);

        if(cursor.getCount()>0)return true;

        return false;
    }

    // method created by yashraj to know data availablebased on masterUid
    public boolean isDataAvailableBasedOnMasterUid(long masterUid){
        Cursor c = db.rawQuery("select * from group_master where masterUID="+masterUid,null);
        if(c.getCount() >0)return true;
        return false;
    }


}
