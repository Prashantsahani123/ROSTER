package com.SampleApp.row.Data;

/**
 * Created by user on 08-02-2017.
 */
public class CalendarData {
    int groupId;
    String uniqueId;
    String eventDate;
    String type;
    int typeId;
    String title;
    String memberFamilyID;

    public CalendarData(){
    }
    /*public CalendarData(int groupId,String uniqueId, String eventDate, String type,int typeId, String title){
        this.groupId = groupId;
        this.uniqueId = uniqueId;
        this.eventDate = eventDate;
        this.type = type;
        this.typeId = typeId;
        this.title = title;

    }*/

    public CalendarData(int groupId, String uniqueId, String eventDate, String type, int typeId, String title, String memberFamilyID) {
        this.groupId = groupId;
        this.uniqueId = uniqueId;
        this.eventDate = eventDate;
        this.type = type;
        this.typeId = typeId;
        this.title = title;
        this.memberFamilyID = memberFamilyID;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
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

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getMemberFamilyID() {
        return memberFamilyID;
    }

    public void setMemberFamilyID(String memberFamilyID) {
        this.memberFamilyID = memberFamilyID;
    }
}
