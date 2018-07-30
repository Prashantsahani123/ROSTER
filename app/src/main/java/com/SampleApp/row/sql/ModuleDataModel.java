package com.SampleApp.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.SampleApp.row.Data.ModuleData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.PreferenceManager;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by USER1 on 08-07-2016.
 */
public class ModuleDataModel
{
    Context context;
    SQLiteDatabase db;

    public ModuleDataModel(Context context) {
        this.context = context;
        db = DBConnection.getInstance(context);
    }

    public long insert(long masterUid, ModuleData md) {
        long id = - 1;
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.ModuleDataMaster.Columns.MASTER_UID, masterUid);
            values.put(Tables.ModuleDataMaster.Columns.GROUP_MODULE_ID, md.getGroupModuleId());
            values.put(Tables.ModuleDataMaster.Columns.GROUP_ID, md.getGroupId());
            values.put(Tables.ModuleDataMaster.Columns.MODULE_ID, md.getModuleId());
            values.put(Tables.ModuleDataMaster.Columns.MODULE_NAME, md.getModuleName());
            values.put(Tables.ModuleDataMaster.Columns.MODULE_STATIC_REF, md.getModuleStaticRef());
            values.put(Tables.ModuleDataMaster.Columns.IMAGE, md.getImage());
            values.put(Tables.ModuleDataMaster.Columns.MODULE_ORDER_NO, md.getModuleOrderNo());
            return db.insert(Tables.ModuleDataMaster.TABLE_NAME, null, values);
        } catch(Exception e) {
            e.printStackTrace();
            return id;
        }
    }

    public boolean insert(long masterUid, ArrayList<ModuleData> list) {
        boolean saved = true;
        db.beginTransaction();
        try {
            Iterator<ModuleData> iterator = list.iterator();
            while ( iterator.hasNext() ) {
                ModuleData gd = iterator.next();
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

    public ArrayList<ModuleData> getModuleData(long masterUid) {
        try {
            ArrayList<ModuleData> list = new ArrayList<ModuleData>();
            Cursor cursor = db.rawQuery("select * from module_data_master where masterUID="+masterUid, null);

            while ( cursor.moveToNext() ) {
                String groupModuleId = cursor.getString(cursor.getColumnIndex(Tables.ModuleDataMaster.Columns.GROUP_MODULE_ID));
                String groupId = cursor.getString(cursor.getColumnIndex(Tables.ModuleDataMaster.Columns.GROUP_ID));
                String moduleId= cursor.getString(cursor.getColumnIndex(Tables.ModuleDataMaster.Columns.MODULE_ID));
                String moduleName= cursor.getString(cursor.getColumnIndex(Tables.ModuleDataMaster.Columns.MODULE_NAME));
                String moduleStaticRef = cursor.getString(cursor.getColumnIndex(Tables.ModuleDataMaster.Columns.MODULE_STATIC_REF));
                String image= cursor.getString(cursor.getColumnIndex(Tables.ModuleDataMaster.Columns.IMAGE));
                int moduleOrderNo = cursor.getInt(cursor.getColumnIndex(Tables.ModuleDataMaster.Columns.MODULE_ORDER_NO));
                boolean box = false;

                ModuleData md = new ModuleData(""+groupModuleId, groupId, moduleId, moduleName, moduleStaticRef, image, moduleOrderNo);
                list.add(md);

                cursor.close();
            }
            //if (list.size() == 0 ) return null;
            return list;
        } catch(Exception e) {
            e.printStackTrace();
            Log.d("Touchbase", e.toString());
            return null;
        }
    }

    public ArrayList<ModuleData> getModuleData(long masterUid,long grpId) {
        try {

            ArrayList<ModuleData> list = new ArrayList<ModuleData>();
            //Cursor cursor = db.rawQuery("select  _id, masterUID, groupModuleId, groupId, moduleId,  moduleName, moduleStaticRef,image from module_data_master where masterUID="+masterUid+" and grpId="+grpId, null);
            Cursor cursor = db.rawQuery("select * from module_data_master where masterUID="+masterUid+" and groupId="+grpId+ " order by "+Tables.ModuleDataMaster.Columns.MODULE_ORDER_NO, null);
            //Cursor cursor = db.query(true, Tables.ModuleDataMaster.TABLE_NAME, null,"masterUID="+masterUid+" and groupId="+grpId,null, null, null,  "groupModuleId",null);
            while ( cursor.moveToNext() ) {

                String groupModuleId = cursor.getString(cursor.getColumnIndex(Tables.ModuleDataMaster.Columns.GROUP_MODULE_ID));
                String groupId = cursor.getString(cursor.getColumnIndex(Tables.ModuleDataMaster.Columns.GROUP_ID));
                String moduleId= cursor.getString(cursor.getColumnIndex(Tables.ModuleDataMaster.Columns.MODULE_ID));
                String moduleName= cursor.getString(cursor.getColumnIndex(Tables.ModuleDataMaster.Columns.MODULE_NAME));
                String moduleStaticRef = cursor.getString(cursor.getColumnIndex(Tables.ModuleDataMaster.Columns.MODULE_STATIC_REF));
                String image= cursor.getString(cursor.getColumnIndex(Tables.ModuleDataMaster.Columns.IMAGE));

                int moduleOrderNo = cursor.getInt(cursor.getColumnIndex(Tables.ModuleDataMaster.Columns.MODULE_ORDER_NO));
                boolean box = false;
                ModuleData md = new ModuleData(""+groupModuleId, groupId, moduleId, moduleName, moduleStaticRef, image, moduleOrderNo);

                if ( list.indexOf(md) == -1) {
                    if(moduleId.equalsIgnoreCase(Constant.Module.SUB_GROUPS)){
                        String is_grp_admin = PreferenceManager.getPreference(context,PreferenceManager.IS_GRP_ADMIN);
                        if(!is_grp_admin.equalsIgnoreCase("No")){
                            list.add(md);
                        }else{

                        }

                    }else {
                        list.add(md);
                    }
                }

                if(moduleId.equalsIgnoreCase(Constant.Module.ATTENDANCE)){
                    String is_grp_admin = PreferenceManager.getPreference(context,PreferenceManager.IS_GRP_ADMIN);
                    if(is_grp_admin.equalsIgnoreCase("No")){
                        list.remove(md);
                    }

                }

            }
            cursor.close();
            //if (list.size() == 0 ) return null;
            return list;
        } catch(Exception e) {
            e.printStackTrace();
            Log.d("Touchbase", e.toString());
            return null;
        }
    }


    public void printTable() {
        Cursor cursor = db.rawQuery("select * from module_data_master", null);
        int n = cursor.getColumnCount();
        StringWriter writer = new StringWriter();
        for(int i=0;i<n;i++) {
            writer.append(cursor.getColumnName(i)+" - ");
        }

        String columns = writer.toString();

        Log.e("Columns", columns);

        while(cursor.moveToNext()) {
            String rec = "";
            for(int i=0;i<n;i++) {
                rec = rec + cursor.getString(i) +" - ";
            }
            Log.e("row", rec);
        }
        cursor.close();
    }

    public boolean isDataAvailable() {
        Cursor cursor = db.rawQuery("select * from module_data_master", null);

        if (cursor.getCount() > 0 ){
            cursor.close();
            return true;
        }

        return false;
    }

    public boolean syncData(long masterUid, ArrayList<ModuleData> newRecords, ArrayList<ModuleData> updatedRecords, ArrayList<ModuleData> deleteRecords) {
        int num = updatedRecords.size();
        boolean[] moduleExists = new boolean[num];
        for(int i=0;i<num; i++){
            moduleExists[i] = moduleExists(updatedRecords.get(i));
        }

        db.beginTransaction();
        try {
            // inserting new records in to table
            Iterator<ModuleData> newIterator = newRecords.iterator();
            while (newIterator.hasNext()) {
                long id = insert(masterUid, newIterator.next());
                if ( id == -1 ) {
                    db.endTransaction();
                    return false;
                }
            }

            // updating records in table

            for(int i=0;i<num;i++) {
                ModuleData md = updatedRecords.get(i);

                if ( moduleExists[i] ){  // if record exists then update it
                    boolean updated = updateModuleMasterWithoutChecking(masterUid, md);
                    if ( ! updated ) {
                        db.endTransaction();
                        return false;
                    }
                } else {  // if recored does not exist insert as new record
                    long id = insert(masterUid, md);
                    if ( id == -1 ) {
                        db.endTransaction();
                        return false;
                    }
                }
            }
            /*Iterator<ModuleData> updatedIterator = updatedRecords.iterator();

            while(updatedIterator.hasNext()) {
                boolean updated = updateModuleMaster(masterUid, updatedIterator.next());
                if ( ! updated ) {
                    db.endTransaction();
                    return false;
                }
            }*/
            //deleting deleted records from db
            Iterator<ModuleData> deletedIterator = deleteRecords.iterator();

            while ( deletedIterator.hasNext() ) {
                //boolean deleted = deleteGroupMasterModel(groupId, masterUid, deletedIterator.next().longValue());

                boolean deleted = deleteModuleMasterModel(masterUid, deletedIterator.next());
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

    public boolean updateModuleMaster(long masterUid, ModuleData md) {
        try {

            ContentValues values = new ContentValues();
            values.put(Tables.ModuleDataMaster.Columns.MASTER_UID, masterUid);
            values.put(Tables.ModuleDataMaster.Columns.GROUP_MODULE_ID, md.getGroupModuleId());
            values.put(Tables.ModuleDataMaster.Columns.GROUP_ID, md.getGroupId());
            values.put(Tables.ModuleDataMaster.Columns.MODULE_ID, md.getModuleId());
            values.put(Tables.ModuleDataMaster.Columns.MODULE_NAME, md.getModuleName());
            values.put(Tables.ModuleDataMaster.Columns.MODULE_STATIC_REF, md.getModuleStaticRef());
            values.put(Tables.ModuleDataMaster.Columns.IMAGE, md.getImage());

            //printTable();

            // long profileId = Long.parseLong(data.getProfileID());
            boolean  moduleAvailable = moduleExists(masterUid,md.getGroupModuleId());
            int n = 0;
            if(moduleAvailable) {
                n = db.update(Tables.ModuleDataMaster.TABLE_NAME, values, "masterUID=? and moduleId=? and groupId=?", new String[]{"" + masterUid, "" + md.getModuleId(), md.getGroupId()});
                return n == 1;
            }else{
                long id =  insert(masterUid,md);
                if(id>0){
                    return n == 1;
                };
            }

            //printTable();
            // if n = 1 it will return true else will return false

        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateModuleMasterWithoutChecking(long masterUid, ModuleData md) {
        try {

            ContentValues values = new ContentValues();
            values.put(Tables.ModuleDataMaster.Columns.MASTER_UID, masterUid);
            values.put(Tables.ModuleDataMaster.Columns.GROUP_MODULE_ID, md.getGroupModuleId());
            values.put(Tables.ModuleDataMaster.Columns.GROUP_ID, md.getGroupId());
            values.put(Tables.ModuleDataMaster.Columns.MODULE_ID, md.getModuleId());
            values.put(Tables.ModuleDataMaster.Columns.MODULE_NAME, md.getModuleName());
            values.put(Tables.ModuleDataMaster.Columns.MODULE_STATIC_REF, md.getModuleStaticRef());
            values.put(Tables.ModuleDataMaster.Columns.IMAGE, md.getImage());

            //printTable();

            // long profileId = Long.parseLong(data.getProfileID());

            int n = db.update(Tables.ModuleDataMaster.TABLE_NAME, values, "masterUID=? and moduleId=? and groupId=?", new String[]{"" + masterUid, "" + md.getModuleId(), ""+md.getGroupId()});
            Log.e("UpdatedRecords" , "Updated records are : "+n);
            return true;


            //printTable();
            // if n = 1 it will return true else will return false

        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteModuleMasterModel(long masterUid,ModuleData data) {
        try {
            int n = 0;
            boolean  dataAvailable = moduleExists(masterUid,data.getModuleId());
            if(dataAvailable) {
                n = db.delete(Tables.GroupMaster.TABLE_NAME,  "masterUID=? and groupModuleId=? and groupId=? and moduleId=?", new String[]{"" + masterUid, "" + data.getGroupModuleId(),""+data.getGroupId(),""+data.getModuleId()});
                return n > 0;  // if n = 1 it will return true else will return false
            }else{
                return true;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean moduleExists(ModuleData md) {
        try {
            //Log.e("Before", "Before executing moduleExists query");
            //Cursor cursor = db.rawQuery("select * from module_data_master where masterUID=" + masterUid + " and groupModuleId=" + grpModuleId, null);
            //Cursor cursor = db.rawQuery("select * from module_data_master where masterUID=? and groupModuleId="+grpModuleId, new String[]{""+masterUid});
            Cursor cursor = db.rawQuery("select * from module_data_master where groupId=? and moduleId=?", new String[]{""+md.getGroupId(), ""+md.getModuleId()});

            //Log.e("After", "Master id : " + masterUid+" Group Module id : "+grpModuleId);
            if (cursor.getCount() > 0) {
                cursor.close();
                Log.e("Module exists", "Module exists");
                return true;
            }
        }catch (Exception e) {
            Log.e("Database error", e.getMessage());
            e.printStackTrace();
        }
        Log.e("ModuleDoesNotExist", "Module does not exists");
        return false;
    }
    public boolean moduleExists(long masterUid,String grpModuleId) {
        try {
            Log.e("Before", "Before executing moduleExists query");
            //Cursor cursor = db.rawQuery("select * from module_data_master where masterUID=" + masterUid + " and groupModuleId=" + grpModuleId, null);
            //Cursor cursor = db.rawQuery("select * from module_data_master where masterUID=? and groupModuleId="+grpModuleId, new String[]{""+masterUid});
            Cursor cursor = db.rawQuery("select * from module_data_master where masterUID=? and groupModuleId="+grpModuleId, new String[]{""+masterUid});

            Log.e("After", "Master id : " + masterUid+" Group Module id : "+grpModuleId);
            if (cursor.getCount() > 0) {
                cursor.close();
                Log.e("Module exists", "Module exists");
                return true;
            }
        }catch (Exception e) {
            Log.e("Database error", e.getMessage());
            e.printStackTrace();
        }
        Log.e("ModuleDoesNotExist", "Module does not exists");
        return false;
    }
}
