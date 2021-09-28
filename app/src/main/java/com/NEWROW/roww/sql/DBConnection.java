package com.NEWROW.row.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by USER on 01-07-2016.
 */
public class DBConnection {
    static DBHelper helper;
    static SQLiteDatabase db;

    public static SQLiteDatabase getInstance(Context context) {
        if ( helper == null || db == null ) {
            helper = new DBHelper(context);
            db = helper.getWritableDatabase();
        }

        return db;
    }

    public static void closeConnection() {
        db.close();
        helper = null;
        db = null;
    }
}
