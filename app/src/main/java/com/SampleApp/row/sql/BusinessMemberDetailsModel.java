package com.SampleApp.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.SampleApp.row.Data.profiledata.BusinessMemberDetails;
import com.SampleApp.row.Data.profiledata.Separator;
import com.SampleApp.row.Utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by USER1 on 22-03-2017.
 */
public class BusinessMemberDetailsModel {
    private Context context;
    private SQLiteDatabase db;

    public BusinessMemberDetailsModel(Context context) {
        this.context = context;
        this.db = DBConnection.getInstance(context);
    }



    public ArrayList<BusinessMemberDetails> getBusinessMemberDetails(long profileId) {
        ArrayList<BusinessMemberDetails> list = new ArrayList<>();
        try {
            Cursor cur = db.rawQuery("select * from "+Tables.BusinessMemberDetails.TABLE_NAME+" where isVisible='y' and "+Tables.BusinessMemberDetails.Columns.PROFILE_ID+"="+profileId, null);
            while(cur.moveToNext()) {
                //, , , key, value, colType, isEditable, isVisible;
                String fieldID = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.FIELD_ID));
                String uniquekey = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.UNIQUE_KEY));
                String key = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.KEY));
                String value = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.VALUE));
                String colType = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.COL_TYPE));
                String isEditable = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.IS_EDITABLE));
                String isVisible = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.IS_VISIBLE));

                if ( ! value.trim().equals("")) {
                    list.add(new BusinessMemberDetails("" + profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible));
                }
            }
        } catch(Exception e) {
            Utils.log("Error : "+e);
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<Object> getOtherBusinessMemberDetails(long profileId) {
        ArrayList<Object> list = new ArrayList<>();
        try {
            Cursor cur = db.rawQuery("select * from "+Tables.BusinessMemberDetails.TABLE_NAME+" where uniquekey IN ('BusinessName', 'businessPosition') and isVisible='y' and "+Tables.BusinessMemberDetails.Columns.PROFILE_ID+"="+profileId +" order by uniquekey", null);
            while(cur.moveToNext()) {
                //, , , key, value, colType, isEditable, isVisible;
                String fieldID = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.FIELD_ID));
                String uniquekey = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.UNIQUE_KEY));
                String key = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.KEY));
                String value = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.VALUE));
                String colType = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.COL_TYPE));
                String isEditable = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.IS_EDITABLE));
                String isVisible = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.IS_VISIBLE));

                if ( ! value.trim().equals("")) {
                    list.add(new BusinessMemberDetails("" + profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible));
                    list.add(new Separator());
                }
            }
        } catch(Exception e) {
            Utils.log("Error : "+e);
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<BusinessMemberDetails> getPhoneNumbers(long profileId) {
        ArrayList<BusinessMemberDetails> list = new ArrayList<>();
        try {
            Cursor cur = db.rawQuery("select distinct * from "+Tables.BusinessMemberDetails.TABLE_NAME+" where colType='Contact' and isVisible='y' and "+Tables.PersonalMemberDetails.Columns.PROFILE_ID+"="+profileId, null);
            while(cur.moveToNext()) {
                //, , , key, value, colType, isEditable, isVisible;
                String fieldID = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.FIELD_ID));
                String uniquekey = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.UNIQUE_KEY));
                String key = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.KEY));
                String value = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.VALUE));
                String colType = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.COL_TYPE));
                String isEditable = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.IS_EDITABLE));
                String isVisible = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.IS_VISIBLE));

                if ( !value.trim().equals("")) {
                    list.add(new BusinessMemberDetails("" + profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible));
                }
            }
        } catch(Exception e) {
            Utils.log("Error : "+e);
            e.printStackTrace();
        }
        return list;

    }

    public ArrayList<BusinessMemberDetails> getEmailIds(long profileId) {
        ArrayList<BusinessMemberDetails> list = new ArrayList<>();
        try {
            Cursor cur = db.rawQuery("select distinct * from "+Tables.BusinessMemberDetails.TABLE_NAME+" where colType='Email' and isVisible='y' and "+Tables.PersonalMemberDetails.Columns.PROFILE_ID+"="+profileId, null);
            while(cur.moveToNext()) {
                //, , , key, value, colType, isEditable, isVisible;
                String fieldID = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.FIELD_ID));
                String uniquekey = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.UNIQUE_KEY));
                String key = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.KEY));
                String value = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.VALUE));
                String colType = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.COL_TYPE));
                String isEditable = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.IS_EDITABLE));
                String isVisible = cur.getString(cur.getColumnIndex(Tables.BusinessMemberDetails.Columns.IS_VISIBLE));
                if ( !value.trim().equals("")) {
                    list.add(new BusinessMemberDetails("" + profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible));
                }
            }
        } catch(Exception e) {
            Utils.log("Error : "+e);
            e.printStackTrace();
        }
        return list;

    }
    public long addBusinessMemberDetails(BusinessMemberDetails data) {
        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.BusinessMemberDetails.Columns.PROFILE_ID , data.getProfileId());
            values.put(Tables.BusinessMemberDetails.Columns.FIELD_ID , data.getFieldID());
            values.put(Tables.BusinessMemberDetails.Columns.UNIQUE_KEY , data.getUniquekey());
            values.put(Tables.BusinessMemberDetails.Columns.KEY , data.getKey());
            values.put(Tables.BusinessMemberDetails.Columns.VALUE , data.getValue());
            values.put(Tables.BusinessMemberDetails.Columns.COL_TYPE , data.getColType());
            values.put(Tables.BusinessMemberDetails.Columns.IS_EDITABLE , data.getIsEditable());
            values.put(Tables.BusinessMemberDetails.Columns.IS_VISIBLE  , data.getIsVisible());

            id = db.insert(Tables.BusinessMemberDetails.TABLE_NAME, null, values);
            return id;
        } catch(Exception e) {
            Utils.log("Error : " + e);
            e.printStackTrace();
        }
        return id;
    }


    public boolean addBusinessMemberDetails(ArrayList<BusinessMemberDetails> list) {
        try {
            Iterator<BusinessMemberDetails> iterator = list.iterator();
            while (iterator.hasNext()) {
                long id = addBusinessMemberDetails(iterator.next());
                if ( id == -1 ) {
                    if ( db.inTransaction() ) {
                        db.endTransaction(); // i.e. transaction is failed
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
    public boolean addBusinessMemberDetailsTrans(ArrayList<BusinessMemberDetails> list) {
        try {
            db.beginTransaction();
            Iterator<BusinessMemberDetails> iterator = list.iterator();
            while (iterator.hasNext()) {
                long id = addBusinessMemberDetails(iterator.next());
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
    public boolean updateBusinessMemberDetails(ArrayList<BusinessMemberDetails> list) {
        try {
            Utils.log("Inside UpdateBusinessMemberDetails");
            if (list.size() == 0 ) {
                Utils.log("Business list is empty");
                return true;
            }
            Iterator<BusinessMemberDetails> iterator = list.iterator();
            while (iterator.hasNext()) {
                Utils.log("Inside while of business details update");
                boolean success = updateBusinessMemberDetails(iterator.next());
                if ( ! success ) {
                    if ( db.inTransaction() ) {
                        db.endTransaction(); // i.e. transaction is failed
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
    public boolean updateBusinessMemberDetails(BusinessMemberDetails data) {
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.BusinessMemberDetails.Columns.UNIQUE_KEY , data.getUniquekey());
            values.put(Tables.BusinessMemberDetails.Columns.KEY , data.getKey());
            values.put(Tables.BusinessMemberDetails.Columns.VALUE , data.getValue());
            values.put(Tables.BusinessMemberDetails.Columns.COL_TYPE , data.getColType());
            values.put(Tables.BusinessMemberDetails.Columns.IS_EDITABLE , data.getIsEditable());
            values.put(Tables.BusinessMemberDetails.Columns.IS_VISIBLE  , data.getIsVisible());
            String whereClause = Tables.BusinessMemberDetails.Columns.PROFILE_ID+"=? and "+Tables.BusinessMemberDetails.Columns.UNIQUE_KEY+"=?";
            String[] whereClauseArgs = new String[]{data.getProfileId(), data.getUniquekey()};


            Cursor cursor = db.query(Tables.BusinessMemberDetails.TABLE_NAME, null, whereClause, whereClauseArgs, null, null, null);
            if ( cursor.getCount() == 0 ) {
                long id = addBusinessMemberDetails(data);
                if ( id != -1 ) {
                    return true;
                } else {
                    return false;
                }
            }

            int n = db.update(Tables.BusinessMemberDetails.TABLE_NAME,values, whereClause, whereClauseArgs);

            Utils.log("Number of updated Business member details : "+n);

            if ( n == 1 ) return true;
            else return false;
        } catch(Exception e) {
            Utils.log("Error : " +e);
            e.printStackTrace();

        }

        return false;
    }


    public boolean deleteBusinessMemberDetails(long profileId, String uniqueKey) {
        try {
            String whereClause = Tables.BusinessMemberDetails.Columns.PROFILE_ID + "=? and " + Tables.BusinessMemberDetails.Columns.UNIQUE_KEY + "=?";
            String[] whereClauseArgs = new String[]{"" + profileId, uniqueKey};
            int n = db.delete(Tables.BusinessMemberDetails.TABLE_NAME, whereClause, whereClauseArgs);
            if ( n == 1 ) return true;
            else return false;
        } catch(Exception e) {
            Utils.log("Error : " + e);
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteBusinessDetailsByProfileId(long profileId) {
        try {
            String whereClause = Tables.BusinessMemberDetails.Columns.PROFILE_ID + "=?";
            String[] whereClauseArgs = new String[]{"" + profileId};
            int n = db.delete(Tables.BusinessMemberDetails.TABLE_NAME, whereClause, whereClauseArgs);
            if ( n == 1 ) return true;
            else return false;
        } catch(Exception e) {
            Utils.log("Error : " + e);
            e.printStackTrace();
        }
        return false;
    }
}
