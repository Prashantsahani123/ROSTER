package com.NEWROW.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.NEWROW.row.Data.NewsFeed;
import com.NEWROW.row.Utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by USER on 09-06-2017.
 */

public class RSSFeedsModel {
    Context context;
    SQLiteDatabase db;

    public RSSFeedsModel(Context context) {
        this.context = context;
        db = DBConnection.getInstance(context);
    }

    public boolean syncData(ArrayList<NewsFeed> newsFeeds) {
        deleteFeed();
        db.beginTransaction();
        try {
            // inserting new records in to table
            Iterator<NewsFeed> newIterator = newsFeeds.iterator();
            while (newIterator.hasNext()) {
                long id = insert(newIterator.next());
                if (id == -1) {
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

    public long insert(NewsFeed nfd) {
        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.RotaryFeeds.Columns.TITLE, nfd.getTitle());
            values.put(Tables.RotaryFeeds.Columns.LINK, nfd.getLink());
            values.put(Tables.RotaryFeeds.Columns.PUBLISHDATE, nfd.getPubDate());
            values.put(Tables.RotaryFeeds.Columns.DESCRIPTION, nfd.getDescription());
            return db.insert(Tables.RotaryFeeds.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return id;
        }
    }


    public ArrayList<NewsFeed> getNewsFeedList() {
        try {
            ArrayList<NewsFeed> list = new ArrayList<NewsFeed>();
            Cursor cursor = db.rawQuery("select * from RotaryFeeds", null);

            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(Tables.RotaryFeeds.Columns.TITLE));
                String link = cursor.getString(cursor.getColumnIndex(Tables.RotaryFeeds.Columns.LINK));
                String publishDate = cursor.getString(cursor.getColumnIndex(Tables.RotaryFeeds.Columns.PUBLISHDATE));
                String description = cursor.getString(cursor.getColumnIndex(Tables.RotaryFeeds.Columns.DESCRIPTION));

                NewsFeed nfd = new NewsFeed(title, link, publishDate, description);
                list.add(nfd);

            }
            cursor.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Touchbase", e.toString());
            return null;
        }
    }


    public boolean isFeedAvailable() {
        Cursor c = db.rawQuery("select * from RotaryFeeds", null);
        if (c.getCount() > 0) return true;
        return false;
    }

    public void deleteFeed(){
        try {
            //db.rawQuery("Delete from RotaryFeeds", null);
            int n = db.delete(Tables.RotaryFeeds.TABLE_NAME, null, null);
            Utils.log("Number of records deleted : "+n);
        }catch(SQLException sqe){
            sqe.printStackTrace();
            Log.d("ROW", sqe.toString());
        }
    }

}
