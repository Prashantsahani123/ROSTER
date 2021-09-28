package com.NEWROW.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.NEWROW.row.Data.BlogFeed;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by USER on 09-06-2017.
 */

public class RotaryBlogsModel {

    Context context;
    SQLiteDatabase db;

    public RotaryBlogsModel(Context context) {
        this.context = context;
        db = DBConnection.getInstance(context);
    }

    public boolean syncData(ArrayList<BlogFeed> blogFeeds) {
        deleteBlog();
        db.beginTransaction();
        try {
            // inserting new records in to table
            Iterator<BlogFeed> newIterator = blogFeeds.iterator();
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

    public long insert(BlogFeed bfd) {
        long id = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.RotaryBlogs.Columns.TITLE, bfd.getTitle());
            values.put(Tables.RotaryBlogs.Columns.LINK, bfd.getLink());
            values.put(Tables.RotaryBlogs.Columns.PUBLISHDATE, bfd.getPubDate());
            values.put(Tables.RotaryBlogs.Columns.DESCRIPTION, bfd.getDescription());
            return db.insert(Tables.RotaryBlogs.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            return id;
        }
    }


    public ArrayList<BlogFeed> getBlogsList() {
        try {
            ArrayList<BlogFeed> list = new ArrayList<BlogFeed>();
            Cursor cursor = db.rawQuery("select * from RotaryBlogs", null);

            while (cursor.moveToNext()) {

                String title = cursor.getString(cursor.getColumnIndex(Tables.RotaryBlogs.Columns.TITLE));
                String link = cursor.getString(cursor.getColumnIndex(Tables.RotaryBlogs.Columns.LINK));
                String publishDate = cursor.getString(cursor.getColumnIndex(Tables.RotaryBlogs.Columns.PUBLISHDATE));
                String description = cursor.getString(cursor.getColumnIndex(Tables.RotaryBlogs.Columns.DESCRIPTION));

                BlogFeed bfd = new BlogFeed(title, link, publishDate, description);
                list.add(bfd);

            }
            cursor.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Touchbase", e.toString());
            return null;
        }
    }

    public boolean isBlogAvailable(){
        Cursor c = db.rawQuery("select * from RotaryBlogs",null);
        if(c.getCount() >0)return true;
        return false;
    }

    public void deleteBlog(){
        try {
            db.delete(Tables.RotaryBlogs.TABLE_NAME, null, null);
        }catch(SQLException sqe){
            sqe.printStackTrace();
            Log.d("ROW", sqe.toString());
        }
    }

}
