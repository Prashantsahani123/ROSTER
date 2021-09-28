package com.NEWROW.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.NEWROW.row.Data.ServiceDirectoryListData;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by USER on 25-07-2016.
 */
public class ServiceDirectoryDataModel {
    Context context;
    SQLiteDatabase db;


    public ServiceDirectoryDataModel(Context context) {
        this.context = context;
        db = DBConnection.getInstance(context);
    }

    public long insert(long masterUid, ServiceDirectoryListData sd) {
        long id = - 1;
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.MASTER_UID, masterUid);
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_ID, sd.getServiceDirId());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_GROUP_ID, sd.getGroupId());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_MEMBER_NAME, sd.getMemberName());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_IMAGE_URL, sd.getImage());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_CONTACT_NUMBER, sd.getContactNo());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_ISDELETED, sd.getIsdeleted());

            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_DESCRIPTION, sd.getDescription());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_CONTACT_NUMBER2, sd.getContactNo2());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_PAX, sd.getPax());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_EMAIL, sd.getEmail());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_ADDRESS, sd.getAddress());

            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_LONG, sd.getLng());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_Lat, sd.getLat());

       /*     values.put(ChatTables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_EMAIL, sd.getEmail());
            values.put(ChatTables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_ADDRESS, sd.getAddress());

            values.put(ChatTables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_LONG, sd.getLng());
            values.put(ChatTables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_Lat, sd.getLat());*/


            values.put(Tables.ServiceDIrectoryDataMaster.Columns.CITY, sd.getCity());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.STATE, sd.getState());

            values.put(Tables.ServiceDIrectoryDataMaster.Columns.COUNTRY, sd.getCountry());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.ZIP, sd.getZip());

            values.put(Tables.ServiceDIrectoryDataMaster.Columns.CSV_KEYWORDS, sd.getCsv());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.COUNTRY_ID1, sd.getCountryId1());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.COUNTRY_ID2, sd.getCountryId2());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.MODULE_ID, sd.getModuleId());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.CATTEGORYID, sd.getCategoryId());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.WEBSITELINK, sd.getWebsite());


            return db.insert(Tables.ServiceDIrectoryDataMaster.TABLE_NAME, null, values);
        } catch(Exception e) {
            e.printStackTrace();
            return id;
        }
    }

    public boolean insert(long masterUid, ArrayList<ServiceDirectoryListData> list) {
        boolean saved = true;
        db.beginTransaction();
        try {
            Iterator<ServiceDirectoryListData> iterator = list.iterator();
            while ( iterator.hasNext() ) {
                ServiceDirectoryListData sd = iterator.next();
                long id = insert(masterUid, sd);
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

    public ArrayList<ServiceDirectoryListData> getServiceDirectoryData(long masterUid, long groupId, String moduleId,String cattegoryId) {
        ArrayList<ServiceDirectoryListData> list = new ArrayList<ServiceDirectoryListData>();
        try {
            Cursor cursor;
            if(cattegoryId.equalsIgnoreCase("")) {

                cursor = db.rawQuery("select * from service_directory_data_master where masterUID=" + masterUid + " and groupId=" + groupId + " and moduleId='" + moduleId + "' order by " + Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_MEMBER_NAME, null);
            }else{
                cursor = db.rawQuery("select * from service_directory_data_master where masterUID=" + masterUid + " and groupId=" + groupId + " and moduleId='" + moduleId + "' and cattegoryId='" + cattegoryId + "' order by " + Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_MEMBER_NAME, null);
            }
            while ( cursor.moveToNext() ) {


                //String grp_Id = cursor.getString(cursor.getColumnIndex(ChatTables.DirectoryDataMaster.Columns.GROUP_ID));
                String serviceDirectoryId = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_ID));
                String serviceDirectoryGroupId= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_GROUP_ID));
                String servviceDirectoryMemberName= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_MEMBER_NAME));
                String serviceDirectoryImageURL = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_IMAGE_URL));
                String serviceDirectoryContactNumber= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_CONTACT_NUMBER));
                String serviceDirectoryIsDeleted= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_ISDELETED));


                String serviceDirectoryDescription= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_DESCRIPTION));
                String servviceDirectoryContactNumber2= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_CONTACT_NUMBER2));
                String serviceDirectoryPax = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_PAX));
                String serviceDirectoryEmail= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_EMAIL));
                String serviceDirectoryAddress= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_ADDRESS));


                String serviceDirectoryLong= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_LONG));
                String serviceDirectoryLat= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_Lat));

                int countryId1 = cursor.getInt(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.COUNTRY_ID1));
                int countryId2 = cursor.getInt(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.COUNTRY_ID2));
                String csv = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.CSV_KEYWORDS));


                String city= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.CITY));
                String state= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.STATE));
                String country= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.COUNTRY));
                String zip = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.ZIP));
                String amoduleId = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.MODULE_ID));
                int categoryId = cursor.getInt(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.CATTEGORYID));
                String website = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.WEBSITELINK));

                boolean box = false;

                ServiceDirectoryListData sd = new ServiceDirectoryListData(""+serviceDirectoryId, ""+serviceDirectoryGroupId, servviceDirectoryMemberName, serviceDirectoryImageURL, serviceDirectoryContactNumber,
                        serviceDirectoryIsDeleted,serviceDirectoryDescription,servviceDirectoryContactNumber2,serviceDirectoryPax,serviceDirectoryEmail,serviceDirectoryAddress,serviceDirectoryLong,serviceDirectoryLat, countryId1, countryId2, csv,city,state,country,zip, amoduleId,categoryId,website);
                list.add(sd);
            }
            //if (list.size() == 0 ) return null;
            return list;
        } catch(Exception e) {
            e.printStackTrace();
            Log.d("Touchbase", e.toString());
            return list;
        }
    }

    public void printTable() {
        Cursor cursor = db.rawQuery("select * from service_directory_data_master", null);
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

    //======================== Search ================================================

    public ArrayList<ServiceDirectoryListData> search(long masterUid, long grpId, String moduleId,String id, String pattern) {
        try {
            ArrayList<ServiceDirectoryListData> list = new ArrayList<ServiceDirectoryListData>();
            pattern = pattern.trim();
            pattern = pattern.replaceAll("\\s+", " ");
            String[] chunks = pattern.split(" ");

            String namePattern = "(";
            for(int i=0;i<chunks.length-1;i++) {
                namePattern = namePattern + "memberName like '%"+chunks[i]+"%' and ";
            }
            namePattern = namePattern + "memberName like '%"+chunks[chunks.length-1]+"%') ";

            String csvPattern = "(";

            for(int i=0;i<chunks.length-1;i++) {
                csvPattern = csvPattern + "csvKeywords like '%"+chunks[i]+"%' and ";
            }

            csvPattern = csvPattern + "csvKeywords like '%"+chunks[chunks.length-1]+"%') ";

            String newPattern = namePattern +" or "+csvPattern;
            String searchQuery;

            //Cursor cursor = db.rawQuery("select * from service_directory_data_master where masterUID="+masterUid+" and groupId="+grpId+" and (memberName like '%"+pattern+"%' or csvKeywords like '%"+pattern+"%') order by "+ChatTables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_MEMBER_NAME, null);
            if(id.equalsIgnoreCase("")) {
                searchQuery = "select * from service_directory_data_master where (masterUID=" + masterUid + " and groupId=" + grpId + " and moduleId='" + moduleId + "') and " + namePattern + " UNION " + "select * from service_directory_data_master where (masterUID=" + masterUid + " and groupId=" + grpId + " and moduleId='" + moduleId + "') and " + csvPattern + " order by " + Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_MEMBER_NAME;
                ;
            }else{
                searchQuery = "select * from service_directory_data_master where (masterUID=" + masterUid + " and groupId=" + grpId + " and moduleId='" + moduleId + "' and cattegoryId='" + id + "') and " + namePattern + " UNION " + "select * from service_directory_data_master where (masterUID=" + masterUid + " and groupId=" + grpId + " and moduleId='" + moduleId + "' and cattegoryId='" + id + "') and " + csvPattern + " order by " + Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_MEMBER_NAME;
            }            Log.e("TouchBase", "♦♦♦♦Query String is : " + searchQuery);
            Cursor cursor = db.rawQuery(searchQuery, null);
            while ( cursor.moveToNext() ) {
                //String grp_Id = cursor.getString(cursor.getColumnIndex(ChatTables.DirectoryDataMaster.Columns.GROUP_ID));
                //String grp_Id = cursor.getString(cursor.getColumnIndex(ChatTables.DirectoryDataMaster.Columns.GROUP_ID));
                String serviceDirectoryId = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_ID));
                String serviceDirectoryGroupId= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_GROUP_ID));
                String servviceDirectoryMemberName= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_MEMBER_NAME));
                String serviceDirectoryImageURL = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_IMAGE_URL));
                String serviceDirectoryContactNumber= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_CONTACT_NUMBER));
                String serviceDirectoryIsDeleted= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_ISDELETED));


                String serviceDirectoryDescription= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_DESCRIPTION));
                String servviceDirectoryContactNumber2= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_CONTACT_NUMBER2));
                String serviceDirectoryPax = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_PAX));
                String serviceDirectoryEmail= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_EMAIL));
                String serviceDirectoryAddress= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_ADDRESS));


                String serviceDirectoryLong= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_LONG));
                String serviceDirectoryLat= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_Lat));

                int countryId1 = cursor.getInt(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.COUNTRY_ID1));
                int countryId2 = cursor.getInt(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.COUNTRY_ID2));
                String csv = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.CSV_KEYWORDS));


                String city= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.CITY));
                String state= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.STATE));
                String country= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.COUNTRY));
                String zip= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.ZIP));
                int categoryId = cursor.getInt(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.CATTEGORYID));
                String website = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.WEBSITELINK));


                boolean box = false;
                String amoduleId = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.MODULE_ID));
                ServiceDirectoryListData sd1 = new ServiceDirectoryListData(""+serviceDirectoryId, ""+serviceDirectoryGroupId, servviceDirectoryMemberName, serviceDirectoryImageURL, serviceDirectoryContactNumber,
                        serviceDirectoryIsDeleted,serviceDirectoryDescription,servviceDirectoryContactNumber2,serviceDirectoryPax,serviceDirectoryEmail,serviceDirectoryAddress,serviceDirectoryLong,serviceDirectoryLat, countryId1, countryId2, csv,city,state,country,zip, amoduleId,categoryId,website);
                list.add(sd1);
            }
            //if (list.size() == 0 ) return null;
            return list;
        } catch(Exception e) {
            e.printStackTrace();
            Log.d("Touchbase", e.toString());
            return new ArrayList<ServiceDirectoryListData>();
        }
    }

    //=========================== Directory Update by Lekha ====================================

    public boolean updateServiceDirectory(long groupId, long masterUid, ServiceDirectoryListData data) {
        try {

            if ( ! memberExists(Long.parseLong(data.getServiceDirId()))) {
                long id = insert(masterUid,data);

                if ( id > 0 ) return true;
                return false;
            }

            ContentValues values = new ContentValues();

            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_ID, data.getServiceDirId());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_GROUP_ID, data.getGroupId());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_MEMBER_NAME, data.getMemberName());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_IMAGE_URL, data.getImage());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_CONTACT_NUMBER , data.getContactNo());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_ISDELETED , data.getIsdeleted());

            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_DESCRIPTION, data.getDescription());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_CONTACT_NUMBER2, data.getContactNo2());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_PAX, data.getPax());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_EMAIL, data.getEmail());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_ADDRESS, data.getAddress());

            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_LONG, data.getLng());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_Lat, data.getLat());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.CITY, data.getCity());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.STATE, data.getState());

            values.put(Tables.ServiceDIrectoryDataMaster.Columns.COUNTRY, data.getCountry());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.ZIP, data.getZip());

            values.put(Tables.ServiceDIrectoryDataMaster.Columns.CSV_KEYWORDS, data.getCsv());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.COUNTRY_ID1, data.getCountryId1());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.COUNTRY_ID2, data.getCountryId2());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.MODULE_ID, data.getModuleId());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.CATTEGORYID, data.getCategoryId());
            values.put(Tables.ServiceDIrectoryDataMaster.Columns.WEBSITELINK, data.getWebsite());


            long serviceDirId = Long.parseLong(data.getServiceDirId());

            int n = db.update(Tables.ServiceDIrectoryDataMaster.TABLE_NAME, values, "masterUID=? and groupId=? and serviceDirId=?", new String[]{""+masterUid, ""+groupId, ""+serviceDirId});
            return n == 1;  // if n = 1 it will return true else will return false

        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteServiceDirectory(long groupId, long masterUid, long serviceDirId) {
        try {
            if ( ! memberExists(serviceDirId)) {
                return true;
            }
            int n = db.delete(Tables.ServiceDIrectoryDataMaster.TABLE_NAME,  "masterUID=? and groupId=? and serviceDirId=?", new String[]{""+masterUid, ""+groupId, ""+serviceDirId});
            return n == 1;  // if n = 1 it will return true else will return false
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean isDataAvailable(long groupId, String moduleId) {
        Cursor cursor = db.rawQuery("select * from service_directory_data_master where groupId="+groupId+" and moduleId='"+moduleId+"'", null);

        if (cursor.getCount() > 0 ) return true;

        return false;
    }

    public boolean memberExists(long serviceDirId) {
        try {
            Cursor cursor = db.rawQuery("select * from service_directory_data_master where serviceDirId="+serviceDirId, null);
            if ( cursor != null && cursor.getCount() == 1) {
                return true;
            }
            return false;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean syncData(long masterUid, long groupId, ArrayList<ServiceDirectoryListData> newRecords, ArrayList<ServiceDirectoryListData> updatedRecords, ArrayList<ServiceDirectoryListData> deleteRecords) {
        db.beginTransaction();
        try {
            //inserting new records in to table
            Iterator<ServiceDirectoryListData> newIterator = newRecords.iterator();
            while (newIterator.hasNext()) {
                ServiceDirectoryListData data = newIterator.next();

                long id = insert(masterUid, data);
                if ( id == -1 ) {
                    db.endTransaction();
                    Log.e("♦♦♦♦♦", "Failed to insert : "+data);
                    return false;
                }
            }

            //upadting updated records in db
            Iterator<ServiceDirectoryListData> updateIterator = updatedRecords.iterator();

            while ( updateIterator.hasNext() ) {
                boolean updated;
                ServiceDirectoryListData data = updateIterator.next();
                updated = updateServiceDirectory(groupId, masterUid, data);
                if ( ! updated ) {
                    db.endTransaction();
                    Log.e("♥♥♥♥♥", "Failed to update : "+data);
                    return false;
                }
            }
            //deleting deleted records from db
            Iterator<ServiceDirectoryListData> deletedIterator = deleteRecords.iterator();

            while ( deletedIterator.hasNext() ) {
                ServiceDirectoryListData data = deletedIterator.next();
                boolean deleted = deleteServiceDirectory(groupId, masterUid, Long.parseLong(data.getServiceDirId()));
                if ( ! deleted ) {
                    db.endTransaction();
                    Log.e("♣♣♣♣♣", "Failed to delete : "+data);
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

    public ServiceDirectoryListData serviceDirectoryDetail(long ServiceId)
    {
        Cursor cursor = db.rawQuery("select * from service_directory_data_master where serviceDirId="+ServiceId, null);
        if ( cursor.moveToNext() ) {
            String serviceDirId = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_ID));
            String groupId = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_GROUP_ID));
            String memberName = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_MEMBER_NAME));
            String image = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_IMAGE_URL));
            String contactNo = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_CONTACT_NUMBER));
            String isDeleted = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_ISDELETED));
            String description = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_DESCRIPTION));
            String contactNo2 = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_CONTACT_NUMBER2));
            String pax = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_PAX));
            String email = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_EMAIL));
            String address = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_ADDRESS));

            String lat = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_Lat));
            String lng = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.SERVICE_DIRECTORY_LONG));
            int countryId1 = cursor.getInt(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.COUNTRY_ID1));
            int countryId2 = cursor.getInt(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.COUNTRY_ID2));
            String csv = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.CSV_KEYWORDS));

            String city= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.CITY));
            String state= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.STATE));
            String country= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.COUNTRY));
            String zip= cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.ZIP));
            String moduleId = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.MODULE_ID));
            int categoryId = cursor.getInt(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.CATTEGORYID));
            String website = cursor.getString(cursor.getColumnIndex(Tables.ServiceDIrectoryDataMaster.Columns.WEBSITELINK));

            ServiceDirectoryListData data = new ServiceDirectoryListData(serviceDirId, groupId, memberName, image, contactNo, isDeleted, description, contactNo2, pax, email, address,lat,lng, countryId1, countryId2, csv,city,state,country,zip, moduleId,categoryId,website);
            return data;
        }
        return null;
    }

}
