package com.SampleApp.row.Data;

/**
 * Created by user on 01-02-2017.
 */
public class AddAnnouncementDateTimeData {
    String date;
    String time;

    public AddAnnouncementDateTimeData() {

    }

    public AddAnnouncementDateTimeData(String date, String time) {
        this.date = date;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "" +"" + date + " "+ time ;
    }
}
