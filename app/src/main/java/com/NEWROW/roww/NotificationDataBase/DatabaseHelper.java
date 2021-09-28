package com.NEWROW.row.NotificationDataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.NEWROW.row.notification.MyFirebaseMessagingService;

import java.util.ArrayList;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "NotificationDetails";

    // Table columns
    public static final String _ID = "_id";
    public static final String EVENT_TITLE = "EVENT_TITLE";
    public static final String EVENT_DESCRIPTION = "EVENT_DESCRIPTION";
    public static final String NOTIFICATION_DATE = "NOTIFICATION_DATE";
    public static final String NOTIFICATION_TIME = "NOTIFICATION_TIME";
    public static final String NOTIFICATION_EXPIRED_DATE = "NOTIFICATION_EXPIRED_DATE";
    public static final String FLAG_FOR_NOTIFICATION_BACKGROUND = "FLAG_FOR_NOTIFICATION_BACKGROUND";
    public static final String COUNT_NOTIFICATION = "COUNT_NOTIFICATION";
    public static final String MESSAGE_ID = "MESSAGE_ID";

    //Extra
    private static final String EVENT_ID = "EVENT_ID";
    private static final String GROUP_ID = "GROUP_ID";
    private static final String MEM_ID = "MEM_ID";
    private static final String ISADMIN = "ISADMIN";
    private static final String FROMNOTIFICATION = "FROMNOTIFICATION";
    private static final String EVENTIMG = "EVENTIMG";
    private static final String VENUE = "VENUE";
    private static final String REGLINK = "REGLINK";
    private static final String EVENTDATE = "EVENTDATE";
    private static final String RSVPENABLE = "RSVPENABLE";
    private static final String GOINGCOUNT = "GOINGCOUNT";
    private static final String MAYBECOUNT = "MAYBECOUNT";
    private static final String NOTGOINGCOUNT = "NOTGOINGCOUNT";
    private static final String TOTALCOUNT = "TOTALCOUNT";
    private static final String MYRESPONSE = "MYRESPONSE";
    private static final String QUESTIONTYPE = "QUESTIONTYPE";
    private static final String QUESTIONTEXT = "QUESTIONTEXT";
    private static final String QUESTIONID = "QUESTIONID";
    private static final String ISQUESENABLE = "ISQUESENABLE";
    private static final String OPTION1 = "OPTION1";
    private static final String OPTION2 = "OPTION2";
    private static final String ENTITY_NAME = "ENTITY_NAME";
    private static final String GROUP_CATEGORY = "GROUP_CATEGORY";
    private static final String TYPE = "TYPE";
    private static final String MODULE_ID = "MODULE_ID";
    private static final String MODULENAME = "MODULENAME";
    private static final String GROUPTYPE = "GROUPTYPE";

    //String current date,expired date
    String currentDate, expiredDate;

    // Database Information
    static final String DB_NAME = "NotificationRecords.DB";

    // database version
    static final int DB_VERSION = 1;

    //Constructor
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    //Creating  QUERY


    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EVENT_TITLE + " TEXT, " + EVENT_DESCRIPTION + " TEXT, " + NOTIFICATION_DATE + " TEXT, " + NOTIFICATION_TIME + " TEXT, " + COUNT_NOTIFICATION + " INTEGER, " + FLAG_FOR_NOTIFICATION_BACKGROUND + " INTEGER, " + MESSAGE_ID + " TEXT, " + EVENT_ID + " TEXT, " + GROUP_ID + " TEXT, " + MEM_ID + " TEXT, " + ISADMIN + " TEXT, " + FROMNOTIFICATION + " TEXT, " + EVENTIMG + " TEXT, " + VENUE + " TEXT, " + REGLINK + " TEXT, " + EVENTDATE + " TEXT, " + RSVPENABLE + " TEXT, " + GOINGCOUNT + " TEXT, " + MAYBECOUNT + " TEXT, " + NOTGOINGCOUNT + " TEXT, " + TOTALCOUNT + " TEXT, " + MYRESPONSE + " TEXT, " + QUESTIONTYPE + " TEXT, " + QUESTIONTEXT + " TEXT, " + QUESTIONID + " TEXT, " + ISQUESENABLE + " TEXT, " + OPTION1 + " TEXT, " + OPTION2 + " TEXT, " + ENTITY_NAME + " TEXT, " + GROUP_CATEGORY + " TEXT, " + TYPE + " TEXT, " + MODULE_ID + " TEXT, " + MODULENAME + " TEXT, " +GROUPTYPE + " TEXT, "+ NOTIFICATION_EXPIRED_DATE + " TEXT);";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean insertNotificationDetailsWithDate(String notification_Date, String notification_Time, String notification_count, String flag, String messageId, String notification_expired_date, Map<String, String> data) {

        SQLiteDatabase db = this.getWritableDatabase();
        // Creating content values
        ContentValues values = new ContentValues();


        currentDate = notification_Date;
        String type = data.get("type");

        if (type.equalsIgnoreCase("Event")) {


            values.put(EVENT_TITLE, data.get("eventTitle"));
            values.put(EVENT_DESCRIPTION, data.get("eventDesc"));
            values.put(NOTIFICATION_DATE, notification_Date);
            values.put(NOTIFICATION_TIME, notification_Time);
            values.put(COUNT_NOTIFICATION, notification_count);
            values.put(FLAG_FOR_NOTIFICATION_BACKGROUND, flag);
            values.put(MESSAGE_ID, messageId);


            values.put(EVENT_ID, data.get("typeID"));
            values.put(GROUP_ID, data.get("grpID"));
            values.put(MEM_ID, data.get("memID"));
            values.put(ISADMIN, "No");
            values.put(FROMNOTIFICATION, "yes");
            values.put(EVENTIMG, data.get("eventImg"));
            values.put(VENUE, data.get("venue"));
            values.put(REGLINK, data.get("reglink"));
            values.put(EVENTDATE, data.get("eventDate"));
            values.put(RSVPENABLE, data.get("rsvpEnable"));
            values.put(GOINGCOUNT, data.get("goingCount"));
            values.put(MAYBECOUNT, data.get("maybeCount"));
            values.put(NOTGOINGCOUNT, data.get("notgoingCount"));
            values.put(TOTALCOUNT, data.get("totalCount"));
            values.put(MYRESPONSE, data.get("myResponse"));
            values.put(QUESTIONTYPE, data.get("questionType"));
            values.put(QUESTIONTEXT, data.get("questionText"));
            values.put(QUESTIONID, data.get("questionID"));
            values.put(ISQUESENABLE, data.get("isQuesEnable"));
            values.put(OPTION1, data.get("option1"));
            values.put(OPTION2, data.get("option2"));
            if (data.containsKey("entityName")) {
                values.put(ENTITY_NAME, data.get("entityName"));
            }
            values.put(GROUP_CATEGORY, data.get("group_category"));
            values.put(TYPE, data.get("type"));
            values.put(MODULE_ID, "");
            values.put(MODULENAME, "");
            values.put(GROUPTYPE, "");
            values.put(NOTIFICATION_EXPIRED_DATE, notification_expired_date);
            currentDate = notification_Date;
            // insert row in CheckInOutDetails table
            long insert = db.insert(TABLE_NAME, null, values);
            return insert > 0;


        } else if (type.equalsIgnoreCase("ann")) {

            values.put(EVENT_TITLE, data.get("ann_title"));
            values.put(EVENT_DESCRIPTION, data.get("ann_desc"));
            values.put(NOTIFICATION_DATE, notification_Date);
            values.put(NOTIFICATION_TIME, notification_Time);
            values.put(COUNT_NOTIFICATION, notification_count);
            values.put(FLAG_FOR_NOTIFICATION_BACKGROUND, flag);
            values.put(MESSAGE_ID, messageId);


            values.put(EVENT_ID, data.get("typeID"));
            values.put(GROUP_ID, data.get("grpID"));
            values.put(MEM_ID, data.get("memID"));
            values.put(ISADMIN, "No");
            values.put(FROMNOTIFICATION, "yes");
            values.put(EVENTIMG, data.get("ann_img"));
            values.put(VENUE, "");
            values.put(REGLINK, data.get("ann_lnk"));
            values.put(EVENTDATE, data.get("Ann_date"));
            values.put(RSVPENABLE, "");
            values.put(GOINGCOUNT, "");
            values.put(MAYBECOUNT, "");
            values.put(NOTGOINGCOUNT, "");
            values.put(TOTALCOUNT, "");
            values.put(MYRESPONSE, "");
            values.put(QUESTIONTYPE, "");
            values.put(QUESTIONTEXT, "");
            values.put(QUESTIONID, "");
            values.put(ISQUESENABLE, "");
            values.put(OPTION1, "");
            values.put(OPTION2, "");
            if (data.containsKey("entityName")) {
                values.put(ENTITY_NAME, data.get("entityName"));
            }
            values.put(GROUP_CATEGORY, data.get("group_category"));
            values.put(TYPE, data.get("type"));
            values.put(MODULE_ID, data.get("moduleID"));
            values.put(MODULENAME, data.get("moduleName"));
            values.put(GROUPTYPE, "");
            values.put(NOTIFICATION_EXPIRED_DATE, notification_expired_date);
            currentDate = notification_Date;
            // insert row in CheckInOutDetails table
            long insert = db.insert(TABLE_NAME, null, values);
            return insert > 0;


        } else if (type.equalsIgnoreCase("Ebulletin")) {

            values.put(EVENT_TITLE, data.get("Message"));
            values.put(EVENT_DESCRIPTION, "");
            values.put(NOTIFICATION_DATE, notification_Date);
            values.put(NOTIFICATION_TIME, notification_Time);
            values.put(COUNT_NOTIFICATION, notification_count);
            values.put(FLAG_FOR_NOTIFICATION_BACKGROUND, flag);
            values.put(MESSAGE_ID, messageId);


            values.put(EVENT_ID, data.get("typeID"));
            values.put(GROUP_ID, data.get("grpID"));
            values.put(MEM_ID, data.get("memID"));
            values.put(ISADMIN, data.get("isAdmin"));
            values.put(FROMNOTIFICATION, "");
            values.put(EVENTIMG, "");
            values.put(VENUE, "");
            values.put(REGLINK, "");
            values.put(EVENTDATE, "");
            values.put(RSVPENABLE, "");
            values.put(GOINGCOUNT, "");
            values.put(MAYBECOUNT, "");
            values.put(NOTGOINGCOUNT, "");
            values.put(TOTALCOUNT, "");
            values.put(MYRESPONSE, "");
            values.put(QUESTIONTYPE, "");
            values.put(QUESTIONTEXT, "");
            values.put(QUESTIONID, "");
            values.put(ISQUESENABLE, "");
            values.put(OPTION1, "");
            values.put(OPTION2, "");
            if (data.containsKey("entityName")) {
                values.put(ENTITY_NAME, data.get("entityName"));
            } else {
                values.put(ENTITY_NAME, "");

            }
            values.put(GROUP_CATEGORY, "");
            values.put(TYPE, data.get("type"));
            values.put(MODULE_ID, "");
            values.put(MODULENAME, "");
            values.put(GROUPTYPE, "");
            values.put(NOTIFICATION_EXPIRED_DATE, notification_expired_date);
            currentDate = notification_Date;
            // insert row in CheckInOutDetails table
            long insert = db.insert(TABLE_NAME, null, values);
            return insert > 0;


        } else if (type.equalsIgnoreCase("AddMember")) {

            values.put(EVENT_TITLE, data.get("Message"));
            values.put(EVENT_DESCRIPTION, "");
            values.put(NOTIFICATION_DATE, notification_Date);
            values.put(NOTIFICATION_TIME, notification_Time);
            values.put(COUNT_NOTIFICATION, notification_count);
            values.put(FLAG_FOR_NOTIFICATION_BACKGROUND, flag);
            values.put(MESSAGE_ID, messageId);


            values.put(EVENT_ID, data.get("typeID"));
            values.put(GROUP_ID, data.get("grpID"));
            values.put(MEM_ID, data.get("memID"));
            values.put(ISADMIN, "No");
            values.put(FROMNOTIFICATION, "");
            values.put(EVENTIMG, "");
            values.put(VENUE, "");
            values.put(REGLINK, "");
            values.put(EVENTDATE, "");
            values.put(RSVPENABLE, "");
            values.put(GOINGCOUNT, "");
            values.put(MAYBECOUNT, "");
            values.put(NOTGOINGCOUNT, "");
            values.put(TOTALCOUNT, "");
            values.put(MYRESPONSE, "");
            values.put(QUESTIONTYPE, "");
            values.put(QUESTIONTEXT, "");
            values.put(QUESTIONID, "");
            values.put(ISQUESENABLE, "");
            values.put(OPTION1, "");
            values.put(OPTION2, "");
            if (data.containsKey("entityName")) {
                values.put(ENTITY_NAME, data.get("entityName"));
            } else {
                values.put(ENTITY_NAME, "");

            }
            values.put(GROUP_CATEGORY, "");
            values.put(TYPE, data.get("type"));
            values.put(MODULE_ID, "");
            values.put(MODULENAME, "");
            values.put(GROUPTYPE, "");
            values.put(NOTIFICATION_EXPIRED_DATE, notification_expired_date);
            currentDate = notification_Date;
            // insert row in CheckInOutDetails table
            long insert = db.insert(TABLE_NAME, null, values);
            return insert > 0;


        } else if (type.equalsIgnoreCase("RemoveMember")) {


            values.put(EVENT_TITLE, "");
            values.put(EVENT_DESCRIPTION, "");
            values.put(NOTIFICATION_DATE, notification_Date);
            values.put(NOTIFICATION_TIME, notification_Time);
            values.put(COUNT_NOTIFICATION, notification_count);
            values.put(FLAG_FOR_NOTIFICATION_BACKGROUND, flag);
            values.put(MESSAGE_ID, messageId);


            values.put(EVENT_ID, "");
            values.put(GROUP_ID, "");
            values.put(MEM_ID, "");
            values.put(ISADMIN, "");
            values.put(FROMNOTIFICATION, "");
            values.put(EVENTIMG, "");
            values.put(VENUE, "");
            values.put(REGLINK, "");
            values.put(EVENTDATE, "");
            values.put(RSVPENABLE, "");
            values.put(GOINGCOUNT, "");
            values.put(MAYBECOUNT, "");
            values.put(NOTGOINGCOUNT, "");
            values.put(TOTALCOUNT, "");
            values.put(MYRESPONSE, "");
            values.put(QUESTIONTYPE, "");
            values.put(QUESTIONTEXT, "");
            values.put(QUESTIONID, "");
            values.put(ISQUESENABLE, "");
            values.put(OPTION1, "");
            values.put(OPTION2, "");
            if (data.containsKey("entityName")) {
                values.put(ENTITY_NAME, data.get("entityName"));
            } else {
                values.put(ENTITY_NAME, "");

            }
            values.put(GROUP_CATEGORY, "");
            values.put(TYPE, data.get("type"));
            values.put(MODULE_ID, "");
            values.put(MODULENAME, "");
            values.put(GROUPTYPE, "");
            values.put(NOTIFICATION_EXPIRED_DATE, notification_expired_date);
            currentDate = notification_Date;
            // insert row in CheckInOutDetails table
            long insert = db.insert(TABLE_NAME, null, values);
            return insert > 0;


        }else if (type.equalsIgnoreCase("Doc")) {

            values.put(EVENT_TITLE, data.get("Message"));
            values.put(EVENT_DESCRIPTION, "");
            values.put(NOTIFICATION_DATE, notification_Date);
            values.put(NOTIFICATION_TIME, notification_Time);
            values.put(COUNT_NOTIFICATION, notification_count);
            values.put(FLAG_FOR_NOTIFICATION_BACKGROUND, flag);
            values.put(MESSAGE_ID, messageId);


            values.put(EVENT_ID, data.get("typeID"));
            values.put(GROUP_ID, data.get("grpID"));
            values.put(MEM_ID, data.get("memID"));
            values.put(ISADMIN, data.get("isAdmin"));
            values.put(FROMNOTIFICATION, "");
            values.put(EVENTIMG, "");
            values.put(VENUE, "");
            values.put(REGLINK, "");
            values.put(EVENTDATE, "");
            values.put(RSVPENABLE, "");
            values.put(GOINGCOUNT, "");
            values.put(MAYBECOUNT, "");
            values.put(NOTGOINGCOUNT, "");
            values.put(TOTALCOUNT, "");
            values.put(MYRESPONSE, "");
            values.put(QUESTIONTYPE, "");
            values.put(QUESTIONTEXT, "");
            values.put(QUESTIONID, "");
            values.put(ISQUESENABLE, "");
            values.put(OPTION1, "");
            values.put(OPTION2, "");
            if (data.containsKey("entityName")) {
                values.put(ENTITY_NAME, data.get("entityName"));
            } else {
                values.put(ENTITY_NAME, "");

            }
            values.put(GROUP_CATEGORY, "");
            values.put(TYPE, data.get("type"));
            values.put(MODULE_ID, "");
            values.put(MODULENAME, "");
            values.put(GROUPTYPE, "");
            values.put(NOTIFICATION_EXPIRED_DATE, notification_expired_date);
            currentDate = notification_Date;
            // insert row in CheckInOutDetails table
            long insert = db.insert(TABLE_NAME, null, values);
            return insert > 0;

        }else if (type.equalsIgnoreCase("EditGroup")) {

            values.put(EVENT_TITLE, data.get("Message"));
            values.put(EVENT_DESCRIPTION, "");
            values.put(NOTIFICATION_DATE, notification_Date);
            values.put(NOTIFICATION_TIME, notification_Time);
            values.put(COUNT_NOTIFICATION, notification_count);
            values.put(FLAG_FOR_NOTIFICATION_BACKGROUND, flag);
            values.put(MESSAGE_ID, messageId);


            values.put(EVENT_ID, data.get("typeID"));
            values.put(GROUP_ID, data.get("grpID"));
            values.put(MEM_ID, data.get("memID"));
            values.put(ISADMIN, "No");
            values.put(FROMNOTIFICATION, "");
            values.put(EVENTIMG, "");
            values.put(VENUE, "");
            values.put(REGLINK, "");
            values.put(EVENTDATE, "");
            values.put(RSVPENABLE, "");
            values.put(GOINGCOUNT, "");
            values.put(MAYBECOUNT, "");
            values.put(NOTGOINGCOUNT, "");
            values.put(TOTALCOUNT, "");
            values.put(MYRESPONSE, "");
            values.put(QUESTIONTYPE, "");
            values.put(QUESTIONTEXT, "");
            values.put(QUESTIONID, "");
            values.put(ISQUESENABLE, "");
            values.put(OPTION1, "");
            values.put(OPTION2, "");
            if (data.containsKey("entityName")) {
                values.put(ENTITY_NAME, data.get("entityName"));
            } else {
                values.put(ENTITY_NAME, "");

            }
            values.put(GROUP_CATEGORY, "");
            values.put(TYPE, data.get("type"));
            values.put(MODULE_ID, "");
            values.put(MODULENAME, "");
            values.put(GROUPTYPE, "");
            values.put(NOTIFICATION_EXPIRED_DATE, notification_expired_date);
            currentDate = notification_Date;
            // insert row in CheckInOutDetails table
            long insert = db.insert(TABLE_NAME, null, values);
            return insert > 0;



        }else if (type.equalsIgnoreCase("imp")) {

            values.put(EVENT_TITLE, data.get("Message"));
            values.put(EVENT_DESCRIPTION, "");
            values.put(NOTIFICATION_DATE, notification_Date);
            values.put(NOTIFICATION_TIME, notification_Time);
            values.put(COUNT_NOTIFICATION, notification_count);
            values.put(FLAG_FOR_NOTIFICATION_BACKGROUND, flag);
            values.put(MESSAGE_ID, messageId);


            values.put(EVENT_ID, data.get("typeID"));
            values.put(GROUP_ID, data.get("grpID"));
            values.put(MEM_ID, data.get("memID"));
            values.put(ISADMIN, "No");
            values.put(FROMNOTIFICATION, "");
            values.put(EVENTIMG, "");
            values.put(VENUE, "");
            values.put(REGLINK, "");
            values.put(EVENTDATE, "");
            values.put(RSVPENABLE, "");
            values.put(GOINGCOUNT, "");
            values.put(MAYBECOUNT, "");
            values.put(NOTGOINGCOUNT, "");
            values.put(TOTALCOUNT, "");
            values.put(MYRESPONSE, "");
            values.put(QUESTIONTYPE, "");
            values.put(QUESTIONTEXT, "");
            values.put(QUESTIONID, "");
            values.put(ISQUESENABLE, "");
            values.put(OPTION1, "");
            values.put(OPTION2, "");
            if (data.containsKey("entityName")) {
                values.put(ENTITY_NAME, data.get("entityName"));
            } else {
                values.put(ENTITY_NAME, "");

            }
            values.put(GROUP_CATEGORY, "");
            values.put(TYPE, data.get("type"));
            values.put(MODULE_ID, "");
            values.put(MODULENAME, "");
            values.put(GROUPTYPE, "");
            values.put(NOTIFICATION_EXPIRED_DATE, notification_expired_date);
            currentDate = notification_Date;
            // insert row in CheckInOutDetails table
            long insert = db.insert(TABLE_NAME, null, values);
            return insert > 0;

            //below code



        }else if (type.equalsIgnoreCase("Calender")) {

            values.put(EVENT_TITLE, data.get("Message"));
            values.put(EVENT_DESCRIPTION, "");
            values.put(NOTIFICATION_DATE, notification_Date);
            values.put(NOTIFICATION_TIME, notification_Time);
            values.put(COUNT_NOTIFICATION, notification_count);
            values.put(FLAG_FOR_NOTIFICATION_BACKGROUND, flag);
            values.put(MESSAGE_ID, messageId);


            values.put(EVENT_ID, data.get("typeID"));
            values.put(GROUP_ID, data.get("grpID"));
            values.put(MEM_ID, data.get("memID"));
            values.put(ISADMIN, "No");
            values.put(FROMNOTIFICATION, "1");
            values.put(EVENTIMG, "");
            values.put(VENUE, data.get("CelebrationType"));//use venue as celebration type
            values.put(REGLINK, "");
            values.put(EVENTDATE, data.get("Todays"));//Event Date use as a todays date
            values.put(RSVPENABLE, "");
            values.put(GOINGCOUNT, "");
            values.put(MAYBECOUNT, "");
            values.put(NOTGOINGCOUNT, "");
            values.put(TOTALCOUNT, "");
            values.put(MYRESPONSE, "");
            values.put(QUESTIONTYPE, "");
            values.put(QUESTIONTEXT, "");
            values.put(QUESTIONID, "");
            values.put(ISQUESENABLE, "");
            values.put(OPTION1, "");
            values.put(OPTION2, "");
            if (data.containsKey("entityName")) {
                values.put(ENTITY_NAME, data.get("entityName"));
            } else {
                values.put(ENTITY_NAME, "");

            }
            values.put(GROUP_CATEGORY, "");
            values.put(TYPE, data.get("type"));
            values.put(MODULE_ID, "");
            values.put(MODULENAME, "");
            values.put(GROUPTYPE, data.get("GroupType"));
            values.put(NOTIFICATION_EXPIRED_DATE, notification_expired_date);
            currentDate = notification_Date;
            // insert row in CheckInOutDetails table
            long insert = db.insert(TABLE_NAME, null, values);
            return insert > 0;




        }else if (type.equalsIgnoreCase("Activity")) {// For club

            values.put(EVENT_TITLE, data.get("Message"));
            values.put(EVENT_DESCRIPTION, "");
            values.put(NOTIFICATION_DATE, notification_Date);
            values.put(NOTIFICATION_TIME, notification_Time);
            values.put(COUNT_NOTIFICATION, notification_count);
            values.put(FLAG_FOR_NOTIFICATION_BACKGROUND, flag);
            values.put(MESSAGE_ID, messageId);


            values.put(EVENT_ID, data.get("typeID"));
            values.put(GROUP_ID, data.get("grpID"));
            values.put(MEM_ID, data.get("memID"));
            values.put(ISADMIN, "No");
            values.put(FROMNOTIFICATION, "1");
            values.put(EVENTIMG, "");
            values.put(VENUE, "");
            values.put(REGLINK, "");
            values.put(EVENTDATE, "");
            values.put(RSVPENABLE, "");
            values.put(GOINGCOUNT, "");
            values.put(MAYBECOUNT, "");
            values.put(NOTGOINGCOUNT, "");
            values.put(TOTALCOUNT, "");
            values.put(MYRESPONSE, "");
            values.put(QUESTIONTYPE, "");
            values.put(QUESTIONTEXT, "");
            values.put(QUESTIONID, "");
            values.put(ISQUESENABLE, "");
            values.put(OPTION1, "");
            values.put(OPTION2, "");
            if (data.containsKey("entityName")) {
                values.put(ENTITY_NAME, data.get("entityName"));
            } else {
                values.put(ENTITY_NAME, "");

            }
            values.put(GROUP_CATEGORY, "");
            values.put(TYPE, data.get("type"));
            values.put(MODULE_ID, "");
            values.put(MODULENAME, "");
            values.put(GROUPTYPE, "");
            values.put(NOTIFICATION_EXPIRED_DATE, notification_expired_date);
            currentDate = notification_Date;
            // insert row in CheckInOutDetails table
            long insert = db.insert(TABLE_NAME, null, values);
            return insert > 0;

            //below code

        }else if (type.equalsIgnoreCase("Gallery")) {// For district

            values.put(EVENT_TITLE, data.get("Message"));
            values.put(EVENT_DESCRIPTION, "");
            values.put(NOTIFICATION_DATE, notification_Date);
            values.put(NOTIFICATION_TIME, notification_Time);
            values.put(COUNT_NOTIFICATION, notification_count);
            values.put(FLAG_FOR_NOTIFICATION_BACKGROUND, flag);
            values.put(MESSAGE_ID, messageId);


            values.put(EVENT_ID, data.get("typeID"));
            values.put(GROUP_ID, data.get("grpID"));
            values.put(MEM_ID, data.get("memID"));
            values.put(ISADMIN, "No");
            values.put(FROMNOTIFICATION, "1");
            values.put(EVENTIMG, "");
            values.put(VENUE, "");
            values.put(REGLINK, "");
            values.put(EVENTDATE, "");
            values.put(RSVPENABLE, "");
            values.put(GOINGCOUNT, "");
            values.put(MAYBECOUNT, "");
            values.put(NOTGOINGCOUNT, "");
            values.put(TOTALCOUNT, "");
            values.put(MYRESPONSE, "");
            values.put(QUESTIONTYPE, "");
            values.put(QUESTIONTEXT, "");
            values.put(QUESTIONID, "");
            values.put(ISQUESENABLE, "");
            values.put(OPTION1, "");
            values.put(OPTION2, "");
            if (data.containsKey("entityName")) {
                values.put(ENTITY_NAME, data.get("entityName"));
            } else {
                values.put(ENTITY_NAME, "");

            }
            values.put(GROUP_CATEGORY, "");
            values.put(TYPE, data.get("type"));
            values.put(MODULE_ID, "");
            values.put(MODULENAME, "");
            values.put(GROUPTYPE, "");
            values.put(NOTIFICATION_EXPIRED_DATE, notification_expired_date);
            currentDate = notification_Date;
            // insert row in CheckInOutDetails table
            long insert = db.insert(TABLE_NAME, null, values);
            return insert > 0;

            //below code

        }else if (type.equalsIgnoreCase("PopupNoti")) {

            values.put(EVENT_TITLE, data.get("title"));
            values.put(EVENT_DESCRIPTION, data.get("Message"));//for message use description
            values.put(NOTIFICATION_DATE, notification_Date);
            values.put(NOTIFICATION_TIME, notification_Time);
            values.put(COUNT_NOTIFICATION, notification_count);
            values.put(FLAG_FOR_NOTIFICATION_BACKGROUND, flag);
            values.put(MESSAGE_ID, messageId);


            values.put(EVENT_ID, data.get("typeID"));
            values.put(GROUP_ID, data.get("grpID"));
            values.put(MEM_ID, data.get("memID"));
            values.put(ISADMIN, "No");
            values.put(FROMNOTIFICATION, "yes");
            values.put(EVENTIMG, "");
            values.put(VENUE, "");
            values.put(REGLINK, "");
            values.put(EVENTDATE, "");
            values.put(RSVPENABLE, "");
            values.put(GOINGCOUNT, "");
            values.put(MAYBECOUNT, "");
            values.put(NOTGOINGCOUNT, "");
            values.put(TOTALCOUNT, "");
            values.put(MYRESPONSE, "");
            values.put(QUESTIONTYPE, "");
            values.put(QUESTIONTEXT, "");
            values.put(QUESTIONID, "");
            values.put(ISQUESENABLE, "");
            values.put(OPTION1, "");
            values.put(OPTION2, "");
            if (data.containsKey("entityName")) {
                values.put(ENTITY_NAME, data.get("entityName"));
            } else {
                values.put(ENTITY_NAME, "");

            }
            values.put(GROUP_CATEGORY, "");
            values.put(TYPE, data.get("type"));
            values.put(MODULE_ID, "");
            values.put(MODULENAME, "");
            values.put(GROUPTYPE, data.get("BAType"));//use GroupType as BA type  1-birthday , 2-Annivasary
            values.put(NOTIFICATION_EXPIRED_DATE, notification_expired_date);
            currentDate = notification_Date;
            // insert row in CheckInOutDetails table
            long insert = db.insert(TABLE_NAME, null, values);
            return insert > 0;

            //below code
          //  intent = new Intent(this, DashboardActivity.class);
           // intent.putExtra("title", data.get("title"));
           // intent.putExtra("message", message);
           // intent.putExtra("fromNotification", "yes");
           // intent.putExtra("BAType", data.get("BAType"));// 1-birthday , 2-Annivasary
        }


        return false;


    }


    public Integer deleteData(String currentDate) {

        SQLiteDatabase db = this.getWritableDatabase();
        //int totalRows = db.delete(TABLE_NAME, NOTIFICATION_EXPIRED_DATE + "= ? " + NOTIFICATION_EXPIRED_DATE + " < '" + currentDate + "'", new String[] {currentDate});
        int totalRows = db.delete(TABLE_NAME, NOTIFICATION_EXPIRED_DATE + "= ? " , new String[] {currentDate});
        db.close();
        return totalRows;

        //  return db.delete(TABLE_NAME, "NOTIFICATION_EXPIRED_DATE = ?", new String[]{currentDate});
       // return db.delete(TABLE_NAME, "NOTIFICATION_EXPIRED_DATE <= ?", new String[]{currentDate});
     //   return db.delete(TABLE_NAME, "NOTIFICATION_EXPIRED_DATE >= ?", new String[]{currentDate});
        // return db.delete(TABLE_NAME, "NOTIFICATION_EXPIRED_DATE = ? and id=?", new String[]{currentDate,"1"});


    }

    public Integer deleteDataBaseOnEventId(String eventId) {

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "EVENT_ID = ?", new String[]{eventId});
        // return db.delete(TABLE_NAME, "NOTIFICATION_EXPIRED_DATE = ? and id=?", new String[]{currentDate,"1"});


    }

    public boolean updateData(String messageId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FLAG_FOR_NOTIFICATION_BACKGROUND, "1");
        db.update(TABLE_NAME, contentValues, "MESSAGE_ID= ? ", new String[]{messageId});
        return true;
        //return db.delete(TABLE_NAME,"NOTIFICATION_EXPIRED_DATE = ?",new String[]{currentDate});


    }


    @SuppressLint("LongLogTag")
    public ArrayList<String> getNotificationCount() {
        ArrayList<String> notificationList = new ArrayList<String>();

        String count_notification = "";


        //Get Current Date
        MyFirebaseMessagingService.setCurrentAndExpiredDate();

        //Delete data base on expired date match with current date


        deleteData(MyFirebaseMessagingService.notification_Date);

        //Execute Records

        //   String deleteQuery = "DELETE FROM "+TABLE_NAME+" WHERE " +NOTIFICATION_EXPIRED_DATE+"="+singleqote+MyMessagingService.notification_Date+singleqote;
        String selectQueryForCountNotification = "SELECT  " + FLAG_FOR_NOTIFICATION_BACKGROUND + " FROM " + TABLE_NAME;

        //  String selectQuery1 = "SELECT  "+TITLE +","+MESSAGE+","+ NOTIFICATION_DATE+","+NOTIFICATION_TIME+" FROM " + TABLE_NAME+" WHERE "+NOTIFICATION_DATE+"="+singleqote+ MyMessagingService.notification_Date+singleqote +" OR "+NOTIFICATION_EXPIRED_DATE+"="+singleqote+expiredDate+singleqote+"ORDER BY " +_ID+ " DESC";

        //  Log.d("delete query", deleteQuery);
        Log.d("select query", selectQueryForCountNotification);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQueryForCountNotification, null);
        //c=db.rawQuery(selectQuery,null);


        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                // id = c.getString(c.getColumnIndex(_ID));
                // ColumnId=id;

                count_notification = c.getString(c.getColumnIndex(FLAG_FOR_NOTIFICATION_BACKGROUND));

                notificationList.add(count_notification);

                // notificationList.add(notification_expired_date);


            } while (c.moveToNext());

            Log.d("notification CountList is: ", notificationList.toString());

        }
        return notificationList;
    }

    public long getProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        long count = DatabaseUtils.queryNumEntries(db, "NotificationDetails");
        db.close();
        return count;
    }

    public  void deletrow() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
          //  Cursor c = db.rawQuery(selectQuery, null);
            db.execSQL("delete from  NotificationDetails where _id IN (SELECT _id from NotificationDetails order by _id desc limit 2 )");
            db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getNotificationRecords() {
        ArrayList<String> notificationList = new ArrayList<String>();
        String id = "";
        String title = "";
        String message = "";
        String notification_Date = "";
        String notification_Time = "";
        String messageId = "";
        String flag = "";

        MyFirebaseMessagingService.setCurrentAndExpiredDate();
        String singleqote = "'";


        //Get Current Date
        MyFirebaseMessagingService.setCurrentAndExpiredDate();

        //Delete data base on expired date match with current date


        deleteData(MyFirebaseMessagingService.notification_Date);

        //NotificationDetails

        //Execute Records

        //   String deleteQuery = "DELETE FROM "+TABLE_NAME+" WHERE " +NOTIFICATION_EXPIRED_DATE+"="+singleqote+MyMessagingService.notification_Date+singleqote;
        // String selectQuery = "SELECT  "+EVENT_TITLE +","+EVENT_DESCRIPTION+","+ NOTIFICATION_DATE+","+NOTIFICATION_TIME+","+MESSAGE_ID+","+FLAG_FOR_NOTIFICATION_BACKGROUND+" FROM " + TABLE_NAME+" ORDER BY " +_ID+ " DESC";
        String selectQuery = "SELECT * " + " FROM " + TABLE_NAME + " ORDER BY " + _ID + " DESC";

        //  String selectQuery1 = "SELECT  "+TITLE +","+MESSAGE+","+ NOTIFICATION_DATE+","+NOTIFICATION_TIME+" FROM " + TABLE_NAME+" WHERE "+NOTIFICATION_DATE+"="+singleqote+ MyMessagingService.notification_Date+singleqote +" OR "+NOTIFICATION_EXPIRED_DATE+"="+singleqote+expiredDate+singleqote+"ORDER BY " +_ID+ " DESC";

        //  Log.d("delete query", deleteQuery);
        Log.d("select query", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        //c=db.rawQuery(selectQuery,null);


        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                // id = c.getString(c.getColumnIndex(_ID));
                // ColumnId=id;

                notificationList.add(c.getString(c.getColumnIndex(EVENT_TITLE)));
                notificationList.add(c.getString(c.getColumnIndex(EVENT_DESCRIPTION)));
                notificationList.add(c.getString(c.getColumnIndex(NOTIFICATION_DATE)));
                notificationList.add(c.getString(c.getColumnIndex(NOTIFICATION_TIME)));
                notificationList.add(c.getString(c.getColumnIndex(COUNT_NOTIFICATION)));
                notificationList.add(c.getString(c.getColumnIndex(FLAG_FOR_NOTIFICATION_BACKGROUND)));
                notificationList.add(c.getString(c.getColumnIndex(MESSAGE_ID)));
                notificationList.add(c.getString(c.getColumnIndex(EVENT_ID)));
                notificationList.add(c.getString(c.getColumnIndex(GROUP_ID)));
                notificationList.add(c.getString(c.getColumnIndex(MEM_ID)));
                notificationList.add(c.getString(c.getColumnIndex(ISADMIN)));
                notificationList.add(c.getString(c.getColumnIndex(FROMNOTIFICATION)));
                notificationList.add(c.getString(c.getColumnIndex(EVENTIMG)));
                notificationList.add(c.getString(c.getColumnIndex(VENUE)));
                notificationList.add(c.getString(c.getColumnIndex(REGLINK)));
                notificationList.add(c.getString(c.getColumnIndex(EVENTDATE)));
                notificationList.add(c.getString(c.getColumnIndex(RSVPENABLE)));
                notificationList.add(c.getString(c.getColumnIndex(GOINGCOUNT)));
                notificationList.add(c.getString(c.getColumnIndex(MAYBECOUNT)));
                notificationList.add(c.getString(c.getColumnIndex(NOTGOINGCOUNT)));
                notificationList.add(c.getString(c.getColumnIndex(TOTALCOUNT)));
                notificationList.add(c.getString(c.getColumnIndex(MYRESPONSE)));
                notificationList.add(c.getString(c.getColumnIndex(QUESTIONTYPE)));
                notificationList.add(c.getString(c.getColumnIndex(QUESTIONTEXT)));
                notificationList.add(c.getString(c.getColumnIndex(QUESTIONID)));
                notificationList.add(c.getString(c.getColumnIndex(ISQUESENABLE)));
                notificationList.add(c.getString(c.getColumnIndex(OPTION1)));
                notificationList.add(c.getString(c.getColumnIndex(OPTION2)));
                notificationList.add(c.getString(c.getColumnIndex(ENTITY_NAME)));
                notificationList.add(c.getString(c.getColumnIndex(GROUP_CATEGORY)));
                notificationList.add(c.getString(c.getColumnIndex(TYPE)));
                notificationList.add(c.getString(c.getColumnIndex(MODULE_ID)));
                notificationList.add(c.getString(c.getColumnIndex(MODULENAME)));
                notificationList.add(c.getString(c.getColumnIndex(GROUPTYPE)));
                notificationList.add(c.getString(c.getColumnIndex(NOTIFICATION_EXPIRED_DATE)));


                //  notification_expired_date = c.getString(c.getColumnIndex(NOTIFICATION_EXPIRED_DATE));


                // adding to checkInOutList
                // notificationList.add(id);
              /*  notificationList.add(title);
                notificationList.add(message);
                notificationList.add(notification_Date);
                notificationList.add(notification_Time);
                notificationList.add(messageId);
                notificationList.add(flag);*/
                // notificationList.add(notification_expired_date);


            } while (c.moveToNext());
            Log.d("notificationList is: ", notificationList.toString());
        }
        return notificationList;
    }
}
