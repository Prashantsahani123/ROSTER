package com.SampleApp.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

import com.SampleApp.row.Data.CalendarData;
import com.SampleApp.row.Utils.Utils;

/**
 * Created by user on 08-02-2017.
 */
public class CalendarMasterModel {
    Context context;
    SQLiteDatabase db;

    public CalendarMasterModel(Context context) {
        this.context = context;
        db = DBConnection.getInstance(context);
    }

    public boolean syncData(long grpId, ArrayList<CalendarData> newEventsList, ArrayList<CalendarData> updatedEventsList, ArrayList<CalendarData> deletedEventsList) {
        db.beginTransaction();
        try {
            // inserting new records in to table
            Iterator<CalendarData> newIterator = newEventsList.iterator();
            while (newIterator.hasNext()) {
                long id = insert(newIterator.next());
                if (id == -1) {
                    db.endTransaction();
                    return false;
                }
            }

            //upadting updated records in db
            Iterator<CalendarData> updateIterator = updatedEventsList.iterator();

            while (updateIterator.hasNext()) {
                boolean updated = updateCalendarMasterModel(grpId, updateIterator.next());
                if (!updated) {
                    db.endTransaction();
                    return false;
                }
            }

            //deleting deleted records from db
            Iterator<CalendarData> deletedIterator = deletedEventsList.iterator();

            while (deletedIterator.hasNext()) {

                boolean deleted = deleteCalendarMasterModel(grpId, deletedIterator.next());
                if (!deleted) {
                    db.endTransaction();
                    return false;
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            db.endTransaction();
            return false;
        }
    }

    public long insert(CalendarData data) {
        if ( isDuplicateEntry(data) ) {
            return 1;
        }

        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.CalendarMaster.Columns.GROUP_ID, data.getGroupId());
            values.put(Tables.CalendarMaster.Columns.UNIQUE_ID, data.getUniqueId());
            values.put(Tables.CalendarMaster.Columns.EVENTDATE, data.getEventDate());
            values.put(Tables.CalendarMaster.Columns.TYPE, data.getType());
            values.put(Tables.CalendarMaster.Columns.TYPE_ID, data.getTypeId());
            values.put(Tables.CalendarMaster.Columns.TITLE, data.getTitle());
            values.put(Tables.CalendarMaster.Columns.MEMBER_FAMILY_ID, data.getMemberFamilyID());

            return db.insert(Tables.CalendarMaster.TABLE_NAME, null, values);

        } catch (Exception e) {
            e.printStackTrace();
            return id;
        }
    }

    public boolean updateCalendarMasterModel(long grpId, CalendarData data) {
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.CalendarMaster.Columns.GROUP_ID, data.getGroupId());
            values.put(Tables.CalendarMaster.Columns.UNIQUE_ID, data.getUniqueId());
            values.put(Tables.CalendarMaster.Columns.EVENTDATE, data.getEventDate());
            values.put(Tables.CalendarMaster.Columns.TYPE, data.getType());
            values.put(Tables.CalendarMaster.Columns.TYPE_ID, data.getTypeId());
            values.put(Tables.CalendarMaster.Columns.TITLE, data.getTitle());
            values.put(Tables.CalendarMaster.Columns.MEMBER_FAMILY_ID, data.getMemberFamilyID());

            boolean dataAvailable = eventExists(grpId, data.getUniqueId(), data.getType(), data.getMemberFamilyID());
            int n = 0;
            if (dataAvailable) {
                try {
                    n = db.update(Tables.CalendarMaster.TABLE_NAME, values, "groupId=? and uniqueId=? and type=? and memberFamilyID=?", new String[]{"" + grpId, "" + data.getUniqueId(), "" + data.getType(), data.getMemberFamilyID()});
                    //n = db.update(Tables.CalendarMaster.TABLE_NAME, values, "groupId=? and uniqueId=? and type=? and eventDate=?", new String[]{"" + grpId, "" + data.getUniqueId(), "" + data.getType(), "" + data.getEventDate()});
                    Utils.log("Number of records updated : "+n);
                    return n == 1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                long id = insert(data);
                if (id > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCalendarMasterModel(long grpId, CalendarData data) {
        try {
            int n = 0;
            boolean dataAvailable = eventExists(grpId, data.getUniqueId(), data.getType(), data.getMemberFamilyID());
            if (dataAvailable) {
                n = db.delete(Tables.CalendarMaster.TABLE_NAME, "groupId=? and uniqueId=? and type=? and memberFamilyID=?", new String[]{"" + grpId + "", data.getUniqueId(), data.getType(), data.getMemberFamilyID()});
                return n == 1;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean eventExists(long grpId, String uniqueId, String type, String memberFamilyID) {
        // Cursor cursor = db.rawQuery("select * from CalendarMaster where uniqueId="+ uniqueId + " and type=" + type,null);
        String query = "select * from "+Tables.CalendarMaster.TABLE_NAME+" where groupId=" + grpId + " and uniqueId='" + uniqueId + "' and type='" + type + "' and memberFamilyID="+memberFamilyID;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }

    }
    public boolean isDuplicateEntry(CalendarData data) {
        // Cursor cursor = db.rawQuery("select * from CalendarMaster where uniqueId="+ uniqueId + " and type=" + type,null);


        String query = "select * from "+Tables.CalendarMaster.TABLE_NAME+
                " where "+Tables.CalendarMaster.Columns.GROUP_ID+"=? and "+
                Tables.CalendarMaster.Columns.UNIQUE_ID+"=? and "+
                Tables.CalendarMaster.Columns.TYPE+"=? and "+
                Tables.CalendarMaster.Columns.TYPE_ID+"=? and "+
                Tables.CalendarMaster.Columns.TITLE+"=? and "+
                Tables.CalendarMaster.Columns.MEMBER_FAMILY_ID+"=?";

        String[] params = new String[]{""+data.getGroupId(), data.getUniqueId(), data.getType(), ""+data.getTypeId(), data.getTitle(), data.getMemberFamilyID()};
        Cursor cursor = db.rawQuery(query, params);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }

    }
    public void printTable() {
        Log.e("---------", "-------------Start of calendar master-----------------");
        Cursor cursor = db.rawQuery("select * from "+Tables.CalendarMaster.TABLE_NAME, null);

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


    public ArrayList<CalendarData> getCalendarEvents(long grpId, String selecteddate) {
        try {
            final int YEAR = 0, MONTH = 1, DAY = 2;
            String[] dateFields = selecteddate.split("-");
            String dateForBday = selecteddate;
            try {
                dateForBday = dateFields[MONTH] + "-" + dateFields[DAY];
            } catch (ArrayIndexOutOfBoundsException aie) {
                dateForBday = "____-"+dateFields[1]+"-__";
            }
            //Utils.log("For All Date for Birthday & Anniversary : " + dateForBday);
            ArrayList<CalendarData> list = new ArrayList<CalendarData>();
            String query = "Select _id, groupId, uniqueId, eventDate, type, typeId, title, CAST (substr(eventDate, 9, 2) as decimal) as day, memberFamilyID from "+Tables.CalendarMaster.TABLE_NAME+" where ((eventDate like '%" + selecteddate + "%') and groupId=" + grpId + " and type NOT IN ('Birthday', 'Anniversary')) or ((eventDate like '%" + dateForBday + " 00:00:00') and groupId=" + grpId + " and type IN ('Birthday', 'Anniversary')) order by day";

            /*01234-67-90

            public static final String _ID = "_id",
                    GROUP_ID = "groupId",
                    UNIQUE_ID = "",
                    EVENTDATE = "",
                    TYPE = "",
                    TYPE_ID ="",
                    TITLE = "";

            String query = "Select * from CalendarMaster where (eventDate like '%" + selecteddate + "%') and groupId=" + grpId + " and type NOT IN ('Birthday', 'Anniversary') order by eventDate UNION " +
            "Select * from CalendarMaster where (eventDate like '%" + dateForBday + " 00:00:00') and groupId=" + grpId + " and type IN ('Birthday', 'Anniversary')  order by eventDate ";
            */


            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                int groupId = cursor.getInt(cursor.getColumnIndex(Tables.CalendarMaster.Columns.GROUP_ID));
                String uniqueId = cursor.getString(cursor.getColumnIndex(Tables.CalendarMaster.Columns.UNIQUE_ID));
                String eventDate = cursor.getString(cursor.getColumnIndex(Tables.CalendarMaster.Columns.EVENTDATE));
                String type = cursor.getString(cursor.getColumnIndex(Tables.CalendarMaster.Columns.TYPE));
                int typeId = cursor.getInt(cursor.getColumnIndex(Tables.CalendarMaster.Columns.TYPE_ID));
                String title = cursor.getString(cursor.getColumnIndex(Tables.CalendarMaster.Columns.TITLE));
                String memberFamilyID = cursor.getString(cursor.getColumnIndex(Tables.CalendarMaster.Columns.MEMBER_FAMILY_ID));
                CalendarData data = new CalendarData(groupId, uniqueId, eventDate, type, typeId, title, memberFamilyID);
                list.add(data);
            }
            cursor.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Touchbase Calendar", e.toString());
            return null;
        }

    }

    public boolean isDataAvailable(long grpId, String date) {
        Cursor c = db.rawQuery("Select * from "+Tables.CalendarMaster.TABLE_NAME+" where (eventDate like '%" + date + "%') and groupId=" + grpId, null);
        if (c.getCount() > 0) {
            c.close();
            return true;
        }
        return false;
    }

    public int getCountForParticularDate(String grpId, String selecteddate) {
        final int YEAR = 0, MONTH = 1, DAY = 2;
        String[] dateFields = selecteddate.split("-");
        String dateForBday = selecteddate;
        try {
            dateForBday = dateFields[MONTH] + "-" + dateFields[DAY];
        } catch (ArrayIndexOutOfBoundsException aie) {

        }
        Utils.log("Date for Birthday & Anniversary : " + dateForBday);
        ArrayList<CalendarData> list = new ArrayList<CalendarData>();
        String query = "Select * from "+Tables.CalendarMaster.TABLE_NAME +" where (eventDate like '%" + selecteddate + "%') and groupId=" + grpId + " and type NOT IN ('Birthday', 'Anniversary') UNION " +
                "Select * from "+Tables.CalendarMaster.TABLE_NAME +" where (eventDate like '%" + dateForBday + " 00:00:00') and groupId=" + grpId + " and type IN ('Birthday', 'Anniversary')";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            return cursor.getCount();
        }
        return 0;
    }

    //select count(*) from CalendarMaster where eventDate like '2017-06%' group by eventDate
}
