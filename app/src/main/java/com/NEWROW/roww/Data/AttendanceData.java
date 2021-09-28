package com.NEWROW.row.Data;

/**
 * Created by USER on 07-10-2016.
 */
public class AttendanceData {

    String id;
    String name;
    String moduleId;
    String attendance;
    String memberId;
    String month;
    String year;
    String date;
    String time;

    public AttendanceData() {
    }

    public AttendanceData(String id, String name, String moduleId, String attendance, String memberId, String month, String year) {
        this.id = id;
        this.name = name;
        this.moduleId = moduleId;
        this.attendance = attendance;
        this.memberId = memberId;
        this.month = month;
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    @Override
    public String toString() {
        return "AttendanceData{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", moduleId='" + moduleId + '\'' +
                ", attendance='" + attendance + '\'' +
                ", memberId='" + memberId + '\'' +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}
