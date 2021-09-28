package com.NEWROW.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.NEWROW.row.Data.DirectoryData;
import com.NEWROW.row.Utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by USER1 on 05-07-2016.
 */
public class DirectoryDataModel {
    Context context;
    SQLiteDatabase db;

    public DirectoryDataModel(Context context) {
        this.context = context;
        db = DBConnection.getInstance(context);
    }


    public long insert(long masterUid, DirectoryData dd) {
        long id = - 1;
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.DirectoryDataMaster.Columns.MASTER_UID, masterUid);
            values.put(Tables.DirectoryDataMaster.Columns.GROUP_ID, dd.getGrpID());
            values.put(Tables.DirectoryDataMaster.Columns.DIRECTORY_PROFILE_ID, dd.getProfileID());
            values.put(Tables.DirectoryDataMaster.Columns.DIRECTORY_GROUP_NAME, dd.getGroupName());
            values.put(Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_NAME, dd.getMemberName());
            values.put(Tables.DirectoryDataMaster.Columns.DIRECTOTY_PIC, dd.getPic());
            values.put(Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_MOBILE, dd.getMembermobile());
            values.put(Tables.DirectoryDataMaster.Columns.DIRECTORY_GROUP_COUNT, dd.getGrpCount());
            values.put(Tables.DirectoryDataMaster.Columns.MEMBER_MASTER_UID, dd.getMasterUID());

            return db.insert(Tables.DirectoryDataMaster.TABLE_NAME, null, values);
        } catch(Exception e) {
            e.printStackTrace();
            return id;
        }
    }

    public boolean insert(long masterUid, ArrayList<DirectoryData> list) {
        boolean saved = true;
        db.beginTransaction();
        try {
            Iterator<DirectoryData> iterator = list.iterator();
            while ( iterator.hasNext() ) {
                DirectoryData gd = iterator.next();
                long id = insert(masterUid, gd);
                if ( id == -1 ) {
                    db.endTransaction();
                    return false;
                }
            }

            db.setTransactionSuccessful();  // commit transaction
            db.endTransaction();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            db.endTransaction();
            return false;
        }
    }

    public ArrayList<DirectoryData> getDirectoryData(long masterUid, long grpId) {
        try {
            ArrayList<DirectoryData> list = new ArrayList<DirectoryData>();
            Cursor cursor = db.rawQuery("select * from directory_data_master where masterUID="+masterUid+" and grpId="+grpId+" order by "+Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_NAME, null);

            while ( cursor.moveToNext() ) {


                //String grp_Id = cursor.getString(cursor.getColumnIndex(ChatTables.DirectoryDataMaster.Columns.GROUP_ID));
                String memberMasterUID = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.MEMBER_MASTER_UID));
                String directoryProfileId = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_PROFILE_ID));
                String directoryGroupName= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_GROUP_NAME));
                String directoryMemberName= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_NAME));
                String directoryPic = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTOTY_PIC));//.replaceAll(" ", "%20");
                Utils.log("Image Url : "+directoryPic);
                String directoryMemberMobile= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_MOBILE));
                String directoryGroupCount= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_GROUP_COUNT));
                boolean box = false;

                DirectoryData dd = new DirectoryData(""+memberMasterUID,""+grpId, directoryProfileId, directoryGroupName, directoryMemberName, directoryPic, directoryMemberMobile,directoryGroupCount, box,false);
                list.add(dd);
            }
            //if (list.size() == 0 ) return null;
            return list;
        } catch(Exception e) {
            e.printStackTrace();
            Log.d("Touchbase", e.toString());
            return null;
        }
    }

    public DirectoryData getUserInfo(long masterUid, long grpId) {
        try {
            ArrayList<DirectoryData> list = new ArrayList<DirectoryData>();
            Cursor cursor = db.rawQuery("select * from directory_data_master where masterUID="+masterUid+" and grpId="+grpId+" order by "+Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_NAME, null);

            if ( cursor.moveToNext() ) {


                //String grp_Id = cursor.getString(cursor.getColumnIndex(ChatTables.DirectoryDataMaster.Columns.GROUP_ID));
                String memberMasterUID = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.MEMBER_MASTER_UID));
                String directoryProfileId = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_PROFILE_ID));
                String directoryGroupName= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_GROUP_NAME));
                String directoryMemberName= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_NAME));
                String directoryPic = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTOTY_PIC));
                String directoryMemberMobile= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_MOBILE));
                String directoryGroupCount= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_GROUP_COUNT));
                boolean box = false;

                DirectoryData dd = new DirectoryData(""+memberMasterUID,""+grpId, directoryProfileId, directoryGroupName, directoryMemberName, directoryPic, directoryMemberMobile,directoryGroupCount, box,false);
               return dd;
            }
            //if (list.size() == 0 ) return null;

        } catch(Exception e) {
            e.printStackTrace();
            Log.d("Touchbase", e.toString());

        }
        return null;
    }
    //=========================To Perticular data
    public ArrayList<DirectoryData> getPericularDirectoryData(long masterUid, long grpId, long profileID) {
        try {
            ArrayList<DirectoryData> list = new ArrayList<DirectoryData>();
            Cursor cursor = db.rawQuery("select * from directory_data_master where masterUID="+masterUid+" and grpId="+grpId+" order by "+Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_NAME, null);

            while ( cursor.moveToNext() ) {


                //String grp_Id = cursor.getString(cursor.getColumnIndex(ChatTables.DirectoryDataMaster.Columns.GROUP_ID));

                String directoryProfileId = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_PROFILE_ID));
                String directoryGroupName= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_GROUP_NAME));
                String directoryMemberName= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_NAME));
                String directoryPic = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTOTY_PIC));
                String directoryMemberMobile= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_MOBILE));
                String directoryGroupCount= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_GROUP_COUNT));
                String memberMasterUID = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.MEMBER_MASTER_UID));
                boolean box = false;

                DirectoryData dd = new DirectoryData(""+memberMasterUID,""+grpId, directoryProfileId, directoryGroupName, directoryMemberName, directoryPic, directoryMemberMobile,directoryGroupCount, box,false);
                list.add(dd);
            }
            //if (list.size() == 0 ) return null;
            return list;
        } catch(Exception e) {
            e.printStackTrace();
            Log.d("Touchbase", e.toString());
            return null;
        }
    }


    public ArrayList<DirectoryData> search(long masterUid, long grpId, String pattern) {
        try {
            ArrayList<DirectoryData> list = new ArrayList<DirectoryData>();

            Cursor cursor = db.rawQuery("select * from directory_data_master where masterUID="+masterUid+" and grpId="+grpId+" and memberName like '%"+pattern+"%' order by "+Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_NAME, null);

            while ( cursor.moveToNext() ) {
                //String grp_Id = cursor.getString(cursor.getColumnIndex(ChatTables.DirectoryDataMaster.Columns.GROUP_ID));
                String directoryProfileId = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_PROFILE_ID));
                String directoryGroupName= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_GROUP_NAME));
                String directoryMemberName= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_NAME));
                String directoryPic = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTOTY_PIC));
                String directoryMemberMobile= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_MOBILE));
                String directoryGroupCount= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_GROUP_COUNT));
                String memberMasterUID = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.MEMBER_MASTER_UID));
                boolean box = false;


            DirectoryData dd = new DirectoryData(""+memberMasterUID,""+grpId, directoryProfileId, directoryGroupName, directoryMemberName, directoryPic, directoryMemberMobile,directoryGroupCount, box,false);
            list.add(dd);
        }
        //if (list.size() == 0 ) return null;
        return list;
    } catch(Exception e) {
        e.printStackTrace();
        Log.d("Touchbase", e.toString());
        return new ArrayList<DirectoryData>();
        }
    }

    public void printTable() {
        Cursor cursor = db.rawQuery("select * from directory_data_master", null);
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
    }

    //=========================== Directory Update by Lekha ====================================

    public boolean updateDirectory(long groupId, long masterUid, DirectoryData data) {
        try {

            if ( ! memberExists(Long.parseLong(data.getProfileID()))) {
                long id = insert(masterUid,data);

                if ( id > 0 ) return true;
                return false;
            }
            ContentValues values = new ContentValues();

            values.put(Tables.DirectoryDataMaster.Columns.DIRECTORY_GROUP_NAME, data.getGroupName());
            values.put(Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_NAME, data.getMemberName());
            values.put(Tables.DirectoryDataMaster.Columns.DIRECTOTY_PIC, data.getPic());
            values.put(Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_MOBILE, data.getMembermobile());
            values.put(Tables.DirectoryDataMaster.Columns.DIRECTORY_GROUP_COUNT , data.getGrpCount());
            long profileId = Long.parseLong(data.getProfileID());
            String whereClause = "masterUID="+masterUid+" and grpId="+groupId+" and profileID="+profileId;

            int n = db.update(Tables.DirectoryDataMaster.TABLE_NAME, values, whereClause, null);

            if ( n == 1 ) return true;
            else return false;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteDirectory(long groupId, long masterUid, long profileId) {
        try {
            if ( ! memberExists(profileId)) {
                return true;
            }
            int n = db.delete(Tables.DirectoryDataMaster.TABLE_NAME,  "profileID="+profileId, null);
            return n == 1;  // if n = 1 it will return true else will return false
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

    public boolean memberExists(long profileId) {
        try {
            Cursor cursor = db.rawQuery("select * from directory_data_master where profileID="+profileId, null);
            if ( cursor != null && cursor.getCount() == 1) {
                return true;
            }
            return false;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean syncData(long masterUid, long groupId, ArrayList<DirectoryData> newRecords, ArrayList<DirectoryData> updatedRecords, ArrayList<DirectoryData> deleteRecords) {
        db.beginTransaction();
        try {
            //inserting new records in to table
            Iterator<DirectoryData> newIterator = newRecords.iterator();
            while ( newIterator.hasNext()) {
                DirectoryData newRec = newIterator.next();
                long id = insert(masterUid, newRec);

                if ( id == -1 ) {
                    db.endTransaction();
                    //Log.e("--------", "Failed to insert the record : " + newRec);
                    return false;
                } else {
                    //Log.e("--------", "Record inserted : "+newRec);
                }
            }

            //upadting updated records in db
            Iterator<DirectoryData> updateIterator = updatedRecords.iterator();

            while ( updateIterator.hasNext() ) {
                DirectoryData updatedRecord = updateIterator.next();
                boolean updated = updateDirectory(groupId, masterUid, updatedRecord);
                Log.e("Updated", "Is updated : "+updated);
                if ( ! updated ) {
                    db.endTransaction();
                    Log.e("*************", "Failed to update record : "+updatedRecord);
                    return false;
                } else {
                    Log.e("*************", "Record updated : "+updatedRecord);
                }
            }

            //deleting deleted records from db
            Iterator<DirectoryData> deletedIterator = deleteRecords.iterator();

            while ( deletedIterator.hasNext() ) {
                DirectoryData deletedRecord = deletedIterator.next();
                boolean deleted = deleteDirectory(groupId, masterUid, Long.parseLong(deletedRecord.getProfileID()));

                if ( ! deleted ) {
                    db.endTransaction();
                    Log.e("#########", "Failed to delete record: "+deletedRecord);
                    return false;
                } else {
                    Log.e("#########", "Recored deleted : "+ deletedRecord);
                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            Log.e("☻☻☻☻☻", "Transaction ended normally");
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            Log.e("♦♦♦♦♦", "Transaction failed");
            db.endTransaction();
            return false;
        }
    }

//    public ArrayList<ChatUser> getChatUsers(long groupId) {
//        ArrayList<ChatUser> list = new ArrayList<ChatUser>();
//        try {
//            Cursor cursor = db.rawQuery("select * from "+ Tables.DirectoryDataMaster.TABLE_NAME+" where "+Tables.DirectoryDataMaster.Columns.GROUP_ID+"="+groupId, null);
//            while (cursor.moveToNext()) {
//                long chatUserId = cursor.getLong(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_PROFILE_ID));
//                String memberName = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_NAME));
//                String tempUserName = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.MASTER_UID));
//                long masterUid = cursor.getLong(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.MEMBER_MASTER_UID));
//                Log.e("masterUid", ""+masterUid);
//                //if (tempUserName.contains(" ") ) tempUserName = tempUserName.substring(tempUserName.indexOf(" ")+1);
//                String userName = Constant.CHAT_USER_PREFIX + masterUid; //cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.GROUP_ID)) +"_"+tempUserName;
//                //String pic = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.GROUP_ID)) +"_"+cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTOTY_PIC));
//                String pic = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTOTY_PIC));
//                ChatUser user = new ChatUser(chatUserId, memberName, userName, pic);
//                list.add(user);
//            }
//            return list;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }


//    public ArrayList<ChatUser> getChatUsers(long groupId, String searchText) {
//        ArrayList<ChatUser> list = new ArrayList<ChatUser>();
//        try {
//            Cursor cursor = db.rawQuery("select * from "+ Tables.DirectoryDataMaster.TABLE_NAME+" where "+Tables.DirectoryDataMaster.Columns.GROUP_ID+"="+groupId+" and (memberName like '%"+searchText+"%' or membermobile like '%"+searchText+"%')", null);
//            while (cursor.moveToNext()) {
//                long chatUserId = cursor.getLong(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_PROFILE_ID));
//                String memberName = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_NAME));
//                String tempUserName = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.MASTER_UID));
//                long masterUid = cursor.getLong(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.MEMBER_MASTER_UID));
//                Log.e("masterUid", ""+masterUid);
//                //if (tempUserName.contains(" ") ) tempUserName = tempUserName.substring(tempUserName.indexOf(" ")+1);
//                String userName = Constant.CHAT_USER_PREFIX + masterUid; //cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.GROUP_ID)) +"_"+tempUserName;
//                //String pic = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.GROUP_ID)) +"_"+cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTOTY_PIC));
//                String pic = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTOTY_PIC));
//                ChatUser user = new ChatUser(chatUserId, memberName, userName, pic);
//                list.add(user);
//            }
//            return list;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }


    public String getUserForLogin(long profileId) {
        try {
            Cursor cursor = db.rawQuery("select * from "+Tables.DirectoryDataMaster.TABLE_NAME+" where "+Tables.DirectoryDataMaster.Columns.DIRECTORY_PROFILE_ID+"="+profileId, null);
            if ( cursor.moveToNext() ) {
                String groupID = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.GROUP_ID));
                String mobileNo = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_MOBILE));

                if ( mobileNo.contains(" ") )
                    mobileNo = mobileNo.substring(mobileNo.indexOf(" ")+1);

                String username = groupID+"_"+mobileNo;
                return username;
            } else {
                return "EMPTY";
            }
        } catch(Exception e) {
            e.printStackTrace();
            Log.e("Error", "Error is : "+e);
        }

        return "EMPTY";
    }


    public void closeDB() {
        try {
            db.close();
        } catch(Exception e) {
            e.printStackTrace();
            Log.e("Error", "Error while closing database : " + e);
        }
    }

    public DirectoryData getChatUserDetails(long memberMasterUserId, long groupId) {
        try {
            String query = "select * from  "+ Tables.DirectoryDataMaster.TABLE_NAME+" where memberMasterUID="+memberMasterUserId+ " and grpId="+groupId;
            Log.e("ChatUserQuery", query);
            Cursor cursor = db.rawQuery(query, null);
            if ( cursor.moveToNext()) {
                String directoryProfileId = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_PROFILE_ID));
                String directoryGroupName= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_GROUP_NAME));
                String directoryMemberName= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_NAME));
                String directoryPic = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTOTY_PIC));
                String directoryMemberMobile= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_MOBILE));
                String directoryGroupCount= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_GROUP_COUNT));
                String memberMasterUID = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.MEMBER_MASTER_UID));
                boolean box = false;
                DirectoryData dd = new DirectoryData(""+memberMasterUID,""+groupId, directoryProfileId, directoryGroupName, directoryMemberName, directoryPic, directoryMemberMobile,directoryGroupCount, box,false);
                return dd;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public DirectoryData getChatUserDetails(long memberMasterUserId, long groupId, String searchText) {
        try {
            String query = "select * from  "+ Tables.DirectoryDataMaster.TABLE_NAME+" where memberMasterUID="+memberMasterUserId+ " and grpId="+groupId+" and (memberName like '%"+searchText+"%' or membermobile like '%"+searchText+"%')";
            Log.e("ChatUserQuery", query);
            Cursor cursor = db.rawQuery(query, null);
            if ( cursor.moveToNext()) {
                String directoryProfileId = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_PROFILE_ID));
                String directoryGroupName= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_GROUP_NAME));
                String directoryMemberName= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_NAME));
                String directoryPic = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTOTY_PIC));
                String directoryMemberMobile= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_MOBILE));
                String directoryGroupCount= cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_GROUP_COUNT));
                String memberMasterUID = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.MEMBER_MASTER_UID));
                boolean box = false;
                DirectoryData dd = new DirectoryData(""+memberMasterUID,""+groupId, directoryProfileId, directoryGroupName, directoryMemberName, directoryPic, directoryMemberMobile,directoryGroupCount, box,false);
                return dd;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // created by yashraj for calendar Module
    public ArrayList<DirectoryData> getMemberProfileData(int profileId,int grpId) {
        ArrayList<DirectoryData> list = new ArrayList<DirectoryData>();
        try {
            Cursor cursor = db.rawQuery("select * from directory_data_master where profileID="+profileId, null);
            if ( cursor != null && cursor.getCount()>0) {
                while (cursor.moveToNext()) {
                    String memberMasterUID = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.MEMBER_MASTER_UID));
                    String directoryProfileId = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_PROFILE_ID));
                    String directoryGroupName = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_GROUP_NAME));
                    String directoryMemberName = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_NAME));
                    String directoryPic = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTOTY_PIC));
                    String directoryMemberMobile = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_MEMBER_MOBILE));
                    String directoryGroupCount = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_GROUP_COUNT));
                    boolean box = false;

                    DirectoryData dd = new DirectoryData("" + memberMasterUID, "" + grpId, directoryProfileId, directoryGroupName, directoryMemberName, directoryPic, directoryMemberMobile, directoryGroupCount, box, false);

                    list.add(dd);
                }
                return list;
            }
        } catch(Exception e) {
            e.printStackTrace();

        }
        return list;
    }

}
