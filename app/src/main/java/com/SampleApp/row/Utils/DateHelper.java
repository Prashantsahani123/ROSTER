package com.SampleApp.row.Utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by USER on 04-07-2016.
 */
public class DateHelper {

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        String date = year+"/"+month+"/"+day+" "+hours+":"+minutes+":"+seconds;
        return date;
    }

    public static String getDateDDMMYYYY() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String date = day+"/"+month+"/"+year;



        return date;
    }
    public static String toCurrentTimeZone(String dateTime, String zoneId) {
        try {
            SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            sourceFormat.setTimeZone(TimeZone.getTimeZone(zoneId));
            Date parsed = null; // => Date is in UTC now

            parsed = sourceFormat.parse(dateTime);

            TimeZone tz = TimeZone.getDefault();
            SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            destFormat.setTimeZone(tz);

            String result = destFormat.format(parsed);

            return result;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateTime;
    }

    public static int compareDate(String d1, String d2) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date1 = sdf.parse(d1);
        Date date2 = sdf.parse(d2);

        int result = date1.compareTo(date2);
        Log.e("TouchBase", "♦♦♦♦Compare Result : "+result);
        return result;

    }

    public static int compareDate(String d1, String d2, String format) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date1 = sdf.parse(d1);
        Date date2 = sdf.parse(d2);

        int result = date1.compareTo(date2);
        Log.e("TouchBase", "♦♦♦♦Compare Result : "+result);
        return result;

    }

}
