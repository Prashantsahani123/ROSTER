package com.SampleApp.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.SampleApp.row.Data.profiledata.FamilyMemberData;
import com.SampleApp.row.Utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by USER1 on 22-03-2017.
 */
public class FamilyMemberModel {
    private Context context;
    private SQLiteDatabase db;

    public FamilyMemberModel(Context context) {
        this.context = context;
        this.db = DBConnection.getInstance(context);
    }

    public long addFamilyMember(FamilyMemberData data) {
        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.FamilyMemberDetail.Columns.PROFILE_ID, data.getProfileId());
            values.put(Tables.FamilyMemberDetail.Columns.FAMILY_MEMBER_ID, data.getFamilyMemberId());
            values.put(Tables.FamilyMemberDetail.Columns.MEMBER_NAME, data.getMemberName());
            values.put(Tables.FamilyMemberDetail.Columns.RELATIONSHIP, data.getRelationship());
            values.put(Tables.FamilyMemberDetail.Columns.DOB, data.getDob());
            values.put(Tables.FamilyMemberDetail.Columns.EMAIL_ID, data.getEmailID());
            values.put(Tables.FamilyMemberDetail.Columns.ANNIVERSARY, data.getAnniversary());
            values.put(Tables.FamilyMemberDetail.Columns.CONTACT_NO, data.getContactNo());
            values.put(Tables.FamilyMemberDetail.Columns.PARTICULARS, data.getParticulars());
            values.put(Tables.FamilyMemberDetail.Columns.BLOOD_GROUP, data.getBloodGroup());
            values.put(Tables.FamilyMemberDetail.Columns.COUNTRY_ID, data.getCountryID());
            id = db.insert(Tables.FamilyMemberDetail.TABLE_NAME, null, values);
        } catch (SQLiteException se) {
            return id;
        } catch (Exception e) {
            return id;
        }
        return id;
    }

    public boolean addFamilyMembers(ArrayList<FamilyMemberData> list) {
        boolean isSuccessful = false;
        try {
            Iterator<FamilyMemberData> iterator = list.iterator();
            while (iterator.hasNext()) {
                FamilyMemberData data = iterator.next();
                long id = addFamilyMember(data);
                if (id == -1) {
                    if (db.inTransaction()) {
                        db.endTransaction(); // i.e. transaction failed
                    }
                    return false;
                }
            }
            return true;
        } catch (SQLiteException se) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean addFamilyMembersAsTrans(ArrayList<FamilyMemberData> list) {
        boolean isSuccessful = false;
        try {
            db.beginTransaction();
            Iterator<FamilyMemberData> iterator = list.iterator();
            while (iterator.hasNext()) {
                FamilyMemberData data = iterator.next();
                long id = addFamilyMember(data);
                if (id == -1) {
                    db.endTransaction();
                    return false;
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            return true;
        } catch (SQLiteException se) {
            db.endTransaction();
            return false;
        } catch (Exception e) {
            db.endTransaction();
            return false;
        }
    }

    public ArrayList<FamilyMemberData> getFamilyMembers(long pProfileId) {
        ArrayList<FamilyMemberData> familyList = new ArrayList<>();
        try {
            Cursor cur = db.rawQuery("select * from FamilyMemberDetail where profileId=?", new String[]{"" + pProfileId});
            while (cur.moveToNext()) {
                String profileId = cur.getString(cur.getColumnIndex(Tables.FamilyMemberDetail.Columns.PROFILE_ID));
                String familyMemberId = cur.getString(cur.getColumnIndex(Tables.FamilyMemberDetail.Columns.FAMILY_MEMBER_ID));
                String memberName = cur.getString(cur.getColumnIndex(Tables.FamilyMemberDetail.Columns.MEMBER_NAME));
                String relationship = cur.getString(cur.getColumnIndex(Tables.FamilyMemberDetail.Columns.RELATIONSHIP));
                String dob = cur.getString(cur.getColumnIndex(Tables.FamilyMemberDetail.Columns.DOB));
                String emailID = cur.getString(cur.getColumnIndex(Tables.FamilyMemberDetail.Columns.EMAIL_ID));
                String anniversary = cur.getString(cur.getColumnIndex(Tables.FamilyMemberDetail.Columns.ANNIVERSARY));
                String contactNo = cur.getString(cur.getColumnIndex(Tables.FamilyMemberDetail.Columns.CONTACT_NO));
                String particulars = cur.getString(cur.getColumnIndex(Tables.FamilyMemberDetail.Columns.PARTICULARS));
                String bloodGroup = cur.getString(cur.getColumnIndex(Tables.FamilyMemberDetail.Columns.BLOOD_GROUP));
                String countryId = cur.getString(cur.getColumnIndex(Tables.FamilyMemberDetail.Columns.COUNTRY_ID));

                FamilyMemberData data = new FamilyMemberData(profileId, familyMemberId, memberName, relationship, dob, emailID, anniversary, contactNo, particulars, bloodGroup, countryId);
                familyList.add(data);
            }

        } catch (Exception e) {
            Utils.log(e.toString());
            e.printStackTrace();
        }
        Collections.sort(familyList, new FamilyMemberData.FamilyComparator());
        return familyList;
    }

    // While updating family members, straight forward deleting existing family member and
    // Inserting updated family members as new memebrs.
    public boolean deleteToUpdate(long profileId, ArrayList<FamilyMemberData> list) {
        try {
            int n = db.delete(Tables.FamilyMemberDetail.TABLE_NAME, Tables.FamilyMemberDetail.Columns.PROFILE_ID + "=" + profileId, null);
            Utils.log("Deleted " + n + " family members");
            if (list.size() == 0) {
                Utils.log("Family list is empty");
                return true;
            }
            return addFamilyMembers(list);
        } catch (Exception e) {
            Utils.log("Error : " + e);
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateFamilyMembers(ArrayList<FamilyMemberData> list) {
        try {
            if (list.size() == 0) {
                Utils.log("Family list is empty");
                return true;
            }
            Iterator<FamilyMemberData> iterator = list.iterator();
            while (iterator.hasNext()) {
                FamilyMemberData data = iterator.next();
                boolean success = updateFamilyMember(data);

                if (!success) {
                    if (db.inTransaction()) {
                        db.endTransaction();
                    }
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            Utils.log("Error : " + e);
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateFamilyMember(FamilyMemberData data) {
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.FamilyMemberDetail.Columns.FAMILY_MEMBER_ID, data.getFamilyMemberId());
            values.put(Tables.FamilyMemberDetail.Columns.MEMBER_NAME, data.getMemberName());
            values.put(Tables.FamilyMemberDetail.Columns.RELATIONSHIP, data.getRelationship());
            values.put(Tables.FamilyMemberDetail.Columns.DOB, data.getDob());
            values.put(Tables.FamilyMemberDetail.Columns.EMAIL_ID, data.getEmailID());
            values.put(Tables.FamilyMemberDetail.Columns.ANNIVERSARY, data.getAnniversary());
            values.put(Tables.FamilyMemberDetail.Columns.CONTACT_NO, data.getContactNo());
            values.put(Tables.FamilyMemberDetail.Columns.PARTICULARS, data.getParticulars());
            values.put(Tables.FamilyMemberDetail.Columns.BLOOD_GROUP, data.getBloodGroup());
            String whereClause = Tables.FamilyMemberDetail.Columns.FAMILY_MEMBER_ID + "=?";
            String[] whereClauseArgs = new String[]{data.getFamilyMemberId()};
            Cursor cursor = db.query(Tables.FamilyMemberDetail.TABLE_NAME, null, whereClause, whereClauseArgs, null, null, null);
            if (cursor.getCount() == 0) {
                long id = addFamilyMember(data);
                if (id != -1) {
                    return true;
                } else {
                    return false;
                }
            }

            int n = db.update(Tables.FamilyMemberDetail.TABLE_NAME, values,
                    whereClause,
                    whereClauseArgs);

            Utils.log("Number of updated family member details are : " + n);
            if (n == 1) {
                return true;
            } else {
                return false;
            }

        } catch (SQLiteException se) {
            Utils.log(se.toString());
            se.printStackTrace();
            return false;
        } catch (Exception e) {
            Utils.log(e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFamilyMemberData(long familyMemberId) {
        try {
            int n = db.delete(Tables.FamilyMemberDetail.TABLE_NAME,
                    Tables.FamilyMemberDetail.Columns.FAMILY_MEMBER_ID + "=" + familyMemberId,
                    null);

            if (n == 1) return true;
            else return false;
        } catch (Exception e) {
            Utils.log("Error : " + e);
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteFamilyMemberByProfileId(long profileId) {
        try {
            int n = db.delete(Tables.FamilyMemberDetail.TABLE_NAME,
                    Tables.FamilyMemberDetail.Columns.PROFILE_ID + "=" + profileId,
                    null);

            if (n == 1) return true;
            else return false;
        } catch (Exception e) {
            Utils.log("Error : " + e);
            e.printStackTrace();
        }

        return false;
    }

}
