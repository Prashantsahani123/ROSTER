package com.SampleApp.row.Data;

/**
 * Created by user on 16-02-2016.
 */
public class AddEventDateTimeData {

    String date;
    String time;

    public AddEventDateTimeData() {

    }

    public AddEventDateTimeData(String date, String time) {
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
