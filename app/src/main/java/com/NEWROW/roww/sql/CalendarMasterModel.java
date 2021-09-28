package com.NEWROW.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.NEWROW.row.Data.CalendarData;
import com.NEWROW.row.R;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.Utils;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

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
              // TODO: 08-01-2018 code added by suhas for unable to open Db
        DBHelper helper = new DBHelper(context);
        db = helper.getWritableDatabase();

        db.beginTransaction();
        // inserting new records in to table
        Iterator<CalendarData> newIterator = newEventsList.iterator();
        while (newIterator.hasNext()) {
            Utils.log("first");
            long id = insert(newIterator.next());
            if (id == -1) {
                db.endTransaction();
                return false;
            }
        }

        //upadting updated records in db
        Iterator<CalendarData> updateIterator = updatedEventsList.iterator();

        while (updateIterator.hasNext()) {
            Utils.log("second");
            boolean updated = updateCalendarMasterModel(grpId, updateIterator.next());
            if (!updated) {
                db.endTransaction();

                return false;
            }
        }

        //deleting deleted records from db
        Iterator<CalendarData> deletedIterator = deletedEventsList.iterator();

        while (deletedIterator.hasNext()) {
            Utils.log("third");
            boolean deleted = deleteCalendarMasterModel(grpId, deletedIterator.next());
            if (!deleted) {
                db.endTransaction();

                return false;
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();

        return true;
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
            values.put(Tables.CalendarMaster.Columns.EXPIRYDATE, data.getExpiryDate());
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
            values.put(Tables.CalendarMaster.Columns.EXPIRYDATE, data.getExpiryDate());
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
                    return n > 0;
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
                return n > 0;
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
        String query = "select count(_id) from "+ Tables.CalendarMaster.TABLE_NAME+" where groupId=" + grpId + " and uniqueId='" + uniqueId + "' and type='" + type + "' and memberFamilyID="+memberFamilyID;
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        int count= cursor.getInt(0);
        cursor.close();

        if (count > 0) {
            //cursor.close();
            return true;
        } else {
           // cursor.close();
            return false;
        }



    }

    public boolean isDuplicateEntry(CalendarData data) {
        // Cursor cursor = db.rawQuery("select * from CalendarMaster where uniqueId="+ uniqueId + " and type=" + type,null);


        String query = "select count(_id) from "+ Tables.CalendarMaster.TABLE_NAME+
                " where "+ Tables.CalendarMaster.Columns.GROUP_ID+"=? and "+
                Tables.CalendarMaster.Columns.UNIQUE_ID+"=? and "+
                Tables.CalendarMaster.Columns.TYPE+"=? and "+
                Tables.CalendarMaster.Columns.TYPE_ID+"=? and "+
                Tables.CalendarMaster.Columns.TITLE+"=? and "+
                Tables.CalendarMaster.Columns.MEMBER_FAMILY_ID+"=?";

        String[] params = new String[]{""+data.getGroupId(), data.getUniqueId(), data.getType(), ""+data.getTypeId(), data.getTitle(), data.getMemberFamilyID()};
        Cursor cursor = db.rawQuery(query, params);

//        if (cursor.getCount() > 0) {
//            return true;
//        } else {
//            return false;
//        }

        cursor.moveToFirst();
        int count= cursor.getInt(0);
        cursor.close();

        if (count > 0) {
            //cursor.close();
            return true;
        } else {
            // cursor.close();
            return false;
        }

    }

    public void printTable() {
        Log.e("---------", "-------------Start of calendar master-----------------");
        Cursor cursor = db.rawQuery("select * from "+ Tables.CalendarMaster.TABLE_NAME, null);

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
            String query = "Select _id, groupId, uniqueId, eventDate,expiryDate, type, typeId, title, CAST (substr(eventDate, 9, 2) as decimal) as day, memberFamilyID from "+ Tables.CalendarMaster.TABLE_NAME+" where ((eventDate like '%" + selecteddate + "%') and groupId=" + grpId + " and type NOT IN ('Birthday', 'Anniversary')) or ((eventDate like '%" + dateForBday + " 00:00:00') and groupId=" + grpId + " and type IN ('Birthday', 'Anniversary')) order by day";

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

            ArrayList<CalendarData> bList=new ArrayList<>();
            ArrayList<CalendarData> aList=new ArrayList<>();
            ArrayList<CalendarData> eList=new ArrayList<>();

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
                if(type.equalsIgnoreCase("Birthday")){
                    bList.add(data);
                }else if(type.equalsIgnoreCase("Anniversary")){
                    aList.add(data);
                }else {
                    eList.add(data);
                }

            }
            list.addAll(bList);
            list.addAll(aList);
            list.addAll(eList);

            cursor.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Touchbase Calendar", e.toString());
            return null;
        }

    }

    public boolean isDataAvailable(long grpId, String date) {
        Cursor c = db.rawQuery("Select * from "+ Tables.CalendarMaster.TABLE_NAME+" where (eventDate like '%" + date + "%') and groupId=" + grpId, null);
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
        String query = "Select * from "+ Tables.CalendarMaster.TABLE_NAME +" where (eventDate like '%" + selecteddate + "%') and groupId=" + grpId + " and type NOT IN ('Birthday', 'Anniversary') UNION " +
                "Select * from "+ Tables.CalendarMaster.TABLE_NAME +" where (eventDate like '%" + dateForBday + " 00:00:00') and groupId=" + grpId + " and type IN ('Birthday', 'Anniversary')";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.getCount() > 0) {
            return cursor.getCount();
        }
        return 0;
    }

    public ArrayList<Event> getMonthData(long grpId,String catId, String month,String year, String cDate){
        String date="'%-"+month+"-%'";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM");
        Date date1=null;
        try {
            date1=sdf.parse(cDate);
            cDate=sdf1.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String currentDate="'%"+cDate+"%'";
        ArrayList<Event> eventArrayList=new ArrayList<>();
        //Cursor cursor= db.query(Tables.CalendarMaster.TABLE_NAME,new String[]{"eventDate"},"eventDate like ? and groupId=?",new String[]{date, String.valueOf(grpId)},null,null,null);
        //String select="select * from NewCalendarMaster where (eventDate like "+date+" and type IN ('Birthday','Anniversary') and groupId="+grpId+") or (eventDate like "+currentDate+" and type IN ('Event') and groupId="+grpId+")";
       String select="";

       if(catId.equalsIgnoreCase(String.valueOf(Constant.GROUP_CATEGORY_CLUB))){
           select="select * from NewCalendarMaster where (groupId="+grpId+" and eventDate like "+date+" and type IN ('Birthday','Anniversary')"+") or (groupId="+grpId+" and type IN ('Event') "+"and eventDate like "+currentDate+" )";

       }else {
           select="select * from NewCalendarMaster where (eventDate like "+date+" and type IN ('Birthday','Anniversary')"+") or (eventDate like "+currentDate+" and type IN ('Event') "+")";

       }

        Utils.log(select);
        Cursor cursor=db.rawQuery(select,null);
        try{
            while (cursor.moveToNext()){
                String eventDate=cursor.getString(cursor.getColumnIndex("eventDate"));
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                SimpleDateFormat df = new SimpleDateFormat("MM-dd");

                long milliseconds;
                Date d = f.parse(eventDate);
                String md= df.format(d);
                String type=cursor.getString(cursor.getColumnIndex("type"));
                if(type.equalsIgnoreCase("Birthday") || type.equalsIgnoreCase("Anniversary")){

//                    String year= String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                    String finalDate=year+"-"+md;
                    Date fDate=sdf.parse(finalDate);
                    milliseconds=fDate.getTime();
                }else {

                    milliseconds = d.getTime();

                }

                ArrayList<Event> tempArray=new ArrayList<>(eventArrayList);
                int count=0;
                if(tempArray.size()>0){
                    for(Event e:tempArray){
                        if(md.equalsIgnoreCase(String.valueOf(e.getData()))){
                            count++;
                        }
                    }
                    if(count==0){

                        if(type.equalsIgnoreCase("Event")){

                            String expDate=cursor.getString(cursor.getColumnIndex("expiryDate"));
                            Date exDate=f.parse(expDate);

                            if(exDate.compareTo(new Date())>=0){
                                Event event=new Event(R.color.black,milliseconds,md);
                                eventArrayList.add(event);
                            }

                        }else {
                            Event event=new Event(R.color.black,milliseconds,md);
                            eventArrayList.add(event);
                        }

                    }
                }else {
                    if(type.equalsIgnoreCase("Event")){
                        String expDate=cursor.getString(cursor.getColumnIndex("expiryDate"));
                        Date exDate=f.parse(expDate);
                        if(exDate.compareTo(new Date())>=0){
                            Event event=new Event(R.color.black,milliseconds,md);
                            eventArrayList.add(event);
                        }
                    }else {
                        Event event=new Event(R.color.black,milliseconds,md);
                        eventArrayList.add(event);
                    }
                }




            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            cursor.close();
        }

        return eventArrayList;

    }

    //select count(*) from CalendarMaster where eventDate like '2017-06%' group by eventDate
}
