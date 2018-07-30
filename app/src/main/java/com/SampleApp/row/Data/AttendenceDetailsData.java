package com.SampleApp.row.Data;

/**
 * Created by user on 06-09-2016.
 */
public class AttendenceDetailsData {

    String title;
    String count;
    String type;
    String attendanceID;

    public AttendenceDetailsData(){

    }

    public AttendenceDetailsData(String title, String count) {
        this.title = title;
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getAttendanceID() {
        return attendanceID;
    }

    public void setAttendanceID(String attendanceID) {
        this.attendanceID = attendanceID;
    }
}
