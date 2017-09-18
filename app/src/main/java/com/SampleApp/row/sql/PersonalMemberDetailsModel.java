package com.SampleApp.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.SampleApp.row.Data.profiledata.DynamicFieldData;
import com.SampleApp.row.Data.profiledata.PersonalMemberDetails;
import com.SampleApp.row.Data.profiledata.Separator;
import com.SampleApp.row.Utils.Utils;

/**
 * Created by USER1 on 22-03-2017.
 */
public class PersonalMemberDetailsModel {
    private Context context;
    private SQLiteDatabase db;

    public PersonalMemberDetailsModel(Context context) {
        this.context = context;
        this.db = DBConnection.getInstance(context);
    }

    public ArrayList<Object> getClubDesignation(long profileId) {
        ArrayList<Object> finalList = new ArrayList<>();
        //ArrayList<PersonalMemberDetails> list = new ArrayList<>();
        try {
            Cursor cur = db.rawQuery("select  * from  "+Tables.PersonalMemberDetails.TABLE_NAME+" where uniquekey='member_master_designation' and "+Tables.PersonalMemberDetails.Columns.PROFILE_ID+"="+profileId, null);
            while(cur.moveToNext()) {
                //, , , key, value, colType, isEditable, isVisible;
                String fieldID = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.FIELD_ID));
                String uniquekey = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.UNIQUE_KEY));
                String key = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.KEY));
                String value = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.VALUE));
                String colType = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.COL_TYPE));
                String isEditable = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.IS_EDITABLE));
                String isVisible = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.IS_VISIBLE));

                if ( !value.trim().equals("")) {
                    finalList.add(new PersonalMemberDetails("" + profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible));
                }
            }
        } catch(Exception e) {
            Utils.log("Error : "+e);
            e.printStackTrace();
        }

        return finalList;
    }
    public ArrayList<Object> getOtherPersonalMemberDetails(long profileId) {
        ArrayList<Object> finalList = new ArrayList<>();
        ArrayList<PersonalMemberDetails> list = new ArrayList<>();
        try {
            Cursor cur = db.rawQuery("select  * from  "+Tables.PersonalMemberDetails.TABLE_NAME+" where colType NOT IN ('Contact', 'Email') and isVisible='y' and "+Tables.PersonalMemberDetails.Columns.PROFILE_ID+"="+profileId, null);
            while(cur.moveToNext()) {
                //, , , key, value, colType, isEditable, isVisible;
                String fieldID = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.FIELD_ID));
                String uniquekey = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.UNIQUE_KEY));
                String key = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.KEY));
                String value = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.VALUE));
                String colType = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.COL_TYPE));
                String isEditable = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.IS_EDITABLE));
                String isVisible = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.IS_VISIBLE));

                if ( !value.trim().equals("")) {
                    list.add(new PersonalMemberDetails("" + profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible));
                }
            }
        } catch(Exception e) {
            Utils.log("Error : "+e);
            e.printStackTrace();
        }
        Collections.sort(list, new DynamicFieldData.DynamicFieldComparator());
        for(int i=0;i<list.size();i++) {
            finalList.add(list.get(i));
            finalList.add(new Separator());
        }
        return finalList;
    }
    public ArrayList<PersonalMemberDetails> getPersonalMemberDetails(long profileId) {
        ArrayList<PersonalMemberDetails> list = new ArrayList<>();
        try {
            Cursor cur = db.rawQuery("select distinct * from "+Tables.PersonalMemberDetails.TABLE_NAME+" where isVisible='y' and "+Tables.PersonalMemberDetails.Columns.PROFILE_ID+"="+profileId, null);
            while(cur.moveToNext()) {
                //, , , key, value, colType, isEditable, isVisible;
                String fieldID = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.FIELD_ID));
                String uniquekey = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.UNIQUE_KEY));
                String key = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.KEY));
                String value = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.VALUE));
                String colType = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.COL_TYPE));
                String isEditable = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.IS_EDITABLE));
                String isVisible = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.IS_VISIBLE));

                if ( !value.trim().equals("")) {
                    list.add(new PersonalMemberDetails("" + profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible));
                }
            }
        } catch(Exception e) {
            Utils.log("Error : "+e);
            e.printStackTrace();
        }
        Collections.sort(list, new DynamicFieldData.DynamicFieldComparator());
        Utils.log("List is : "+list);
        return list;
    }

    public ArrayList<PersonalMemberDetails> getPhoneNumbers(long profileId) {
        ArrayList<PersonalMemberDetails> list = new ArrayList<>();
        try {
            Cursor cur = db.rawQuery("select * from "+Tables.PersonalMemberDetails.TABLE_NAME+" where colType='Contact' and isVisible='y' and "+Tables.PersonalMemberDetails.Columns.PROFILE_ID+"="+profileId, null);
            while(cur.moveToNext()) {
                String fieldID = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.FIELD_ID));
                String uniquekey = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.UNIQUE_KEY));
                String key = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.KEY));
                String value = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.VALUE));
                String colType = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.COL_TYPE));
                String isEditable = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.IS_EDITABLE));
                String isVisible = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.IS_VISIBLE));

                if ( ! value.trim().equals("")) {
                    list.add(new PersonalMemberDetails("" + profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible));
                }
            }
        } catch(Exception e) {
            Utils.log("Error : "+e);
            e.printStackTrace();
        }
        return list;

    }

    public ArrayList<PersonalMemberDetails> getEmailIds(long profileId) {
        ArrayList<PersonalMemberDetails> list = new ArrayList<>();
        try {
            Cursor cur = db.rawQuery("select distinct * from "+Tables.PersonalMemberDetails.TABLE_NAME+" where colType='Email' and isVisible='y' and "+Tables.PersonalMemberDetails.Columns.PROFILE_ID+"="+profileId, null);
            while(cur.moveToNext()) {
                //, , , key, value, colType, isEditable, isVisible;
                String fieldID = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.FIELD_ID));
                String uniquekey = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.UNIQUE_KEY));
                String key = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.KEY));
                String value = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.VALUE));
                String colType = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.COL_TYPE));
                String isEditable = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.IS_EDITABLE));
                String isVisible = cur.getString(cur.getColumnIndex(Tables.PersonalMemberDetails.Columns.IS_VISIBLE));

                if ( !value.trim().equals("")) {
                    list.add(new PersonalMemberDetails("" + profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible));
                }
            }
        } catch(Exception e) {
            Utils.log("Error : "+e);
            e.printStackTrace();
        }
        return list;

    }
    public long addPersonalMemberDetails(PersonalMemberDetails data) {
        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.PersonalMemberDetails.Columns.PROFILE_ID , data.getProfileId());
            values.put(Tables.PersonalMemberDetails.Columns.FIELD_ID , data.getFieldID());
            values.put(Tables.PersonalMemberDetails.Columns.UNIQUE_KEY , data.getUniquekey());
            values.put(Tables.PersonalMemberDetails.Columns.KEY , data.getKey());
            values.put(Tables.PersonalMemberDetails.Columns.VALUE , data.getValue());
            values.put(Tables.PersonalMemberDetails.Columns.COL_TYPE , data.getColType());
            values.put(Tables.PersonalMemberDetails.Columns.IS_EDITABLE , data.getIsEditable());
            values.put(Tables.PersonalMemberDetails.Columns.IS_VISIBLE  , data.getIsVisible());

            id = db.insert(Tables.PersonalMemberDetails.TABLE_NAME, null, values);
            return id;
        } catch(Exception e) {
            Utils.log("Error : " + e);
            e.printStackTrace();
        }
        return id;
    }

    public boolean addPersonalMemberDetails(ArrayList<PersonalMemberDetails> list) {
        try {
            Iterator<PersonalMemberDetails> iterator = list.iterator();
            while (iterator.hasNext()) {
                long id = addPersonalMemberDetails(iterator.next());
                if ( id == -1 ) {
                    if (db.inTransaction()) {
                        db.endTransaction(); // i.e. transaction is not successful
                    }
                    return false;
                }
            }
            return true;
        } catch(Exception e){
            Utils.log("Error : "+e);
        }
        return false;
    }
    public boolean addPersonalMemberDetailsTrans(ArrayList<PersonalMemberDetails> list) {
        try {
            db.beginTransaction();
            Iterator<PersonalMemberDetails> iterator = list.iterator();
            while (iterator.hasNext()) {
                long id = addPersonalMemberDetails(iterator.next());
                if ( id == -1 ) {
                    db.endTransaction();
                    return false;
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            return true;
        } catch(Exception e){
            Utils.log("Error : "+e);
            db.endTransaction();
        }
        return false;
    }
    public boolean updatePersonalMemberDetails(PersonalMemberDetails data) {
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.PersonalMemberDetails.Columns.UNIQUE_KEY , data.getUniquekey());
            values.put(Tables.PersonalMemberDetails.Columns.KEY , data.getKey());
            values.put(Tables.PersonalMemberDetails.Columns.VALUE , data.getValue());
            values.put(Tables.PersonalMemberDetails.Columns.COL_TYPE , data.getColType());
            values.put(Tables.PersonalMemberDetails.Columns.IS_EDITABLE , data.getIsEditable());
            values.put(Tables.PersonalMemberDetails.Columns.IS_VISIBLE  , data.getIsVisible());
            String whereClause = Tables.PersonalMemberDetails.Columns.PROFILE_ID+"=? and "+Tables.PersonalMemberDetails.Columns.UNIQUE_KEY+"=?";
            String[] whereClauseArgs = new String[]{data.getProfileId(), data.getUniquekey()};
            int n = db.update(Tables.PersonalMemberDetails.TABLE_NAME,values, whereClause, whereClauseArgs);

            Cursor cursor = db.query(Tables.PersonalMemberDetails.TABLE_NAME, null, whereClause, whereClauseArgs, null, null, null);
            if ( cursor.getCount() == 0 ) {
                long id = addPersonalMemberDetails(data);
                if ( id != -1 ) {
                    return true;
                } else {
                    return false;
                }
            }
            Utils.log("Number of updated personal member details : "+n);
            if ( n != 0 ) {
                return true;
            } else{
                return false;
            }
        } catch(Exception e) {
            Utils.log("Error : " +e);
            e.printStackTrace();
            //return false;
        }

        return true;
    }

    public boolean updatePersonalMemberDetails(ArrayList<PersonalMemberDetails> list) {
        try {
            if ( list.size() == 0 ) return true;
            if(list.size() == 0 ) return true;
            Iterator<PersonalMemberDetails> iterator = list.iterator();
            while ( iterator.hasNext() ) {
                PersonalMemberDetails data = iterator.next();

                boolean  success = updatePersonalMemberDetails(data);
                if ( ! success) {
                    if ( db.inTransaction() ) {
                        db.endTransaction();
                    }
                    return false;
                }
            }

            return true;
        } catch(Exception e) {
            Utils.log("Error : " +e);
            e.printStackTrace();
        }

        return false;
    }

    public boolean deletePersonalMemberDetails(long profileId, String uniqueKey) {
        try {
            String whereClause = Tables.PersonalMemberDetails.Columns.PROFILE_ID + "=? and " + Tables.PersonalMemberDetails.Columns.UNIQUE_KEY + "=?";
            String[] whereClauseArgs = new String[]{"" + profileId, uniqueKey};
            int n = db.delete(Tables.PersonalMemberDetails.TABLE_NAME, whereClause, whereClauseArgs);
            if ( n == 1 ) return true;
            else return false;
        } catch(Exception e) {
            Utils.log("Error : " + e);
            e.printStackTrace();
        }
        return false;
    }

    public boolean deletePersonalDetailsByProfileId(long profileId) {
        try {
            String whereClause = Tables.PersonalMemberDetails.Columns.PROFILE_ID + "=?";
            String[] whereClauseArgs = new String[]{"" + profileId};
            int n = db.delete(Tables.PersonalMemberDetails.TABLE_NAME, whereClause, whereClauseArgs);
            if ( n == 1 ) return true;
            else return false;
        } catch(Exception e) {
            Utils.log("Error : " + e);
            e.printStackTrace();
        }
        return false;
    }
    public void printTable() {
        Log.e("---------", "-------------Start of calendar master-----------------");
        Cursor cursor = db.rawQuery("select * from "+Tables.PersonalMemberDetails.TABLE_NAME, null);

        int n = cursor.getColumnCount();
        String columns = "";
        for (int i = 0; i < n; i++) {
            columns = columns + cursor.getColumnName(i) + " - ";
        }
        Log.e("ColumnName", columns);

        while (cursor.moveToNext()) {
            String rec = "";
            for (int i = 0; i < n; i++) {
                rec = rec + cursor.getString(i) + " - ";
            }
            Log.e("row", rec);
        }

        cursor.close();
        Log.e("---------", "-----------End of calendar master-------------------");
    }


}
