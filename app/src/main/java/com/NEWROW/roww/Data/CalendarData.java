package com.NEWROW.row.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user on 08-02-2017.
 */
public class CalendarData implements Serializable {
    int groupId;
    String uniqueId;
    String eventDate,expiryDate;
    String type;
    int typeId;
    String title;
    String memberFamilyID;
    String eventImg,EmailId,ContactNumber,Description,EventTime;
    String memberName,memberEmail,memberNumber;
    ArrayList<CalendarData> emailList,numberList;
    boolean selected;

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public ArrayList<CalendarData> getEmailList() {
        return emailList;
    }

    public void setEmailList(ArrayList<CalendarData> emailList) {
        this.emailList = emailList;
    }

    public ArrayList<CalendarData> getNumberList() {
        return numberList;
    }

    public void setNumberList(ArrayList<CalendarData> numberList) {
        this.numberList = numberList;
    }

    public String getEventImg() {
        return eventImg;
    }

    public void setEventImg(String eventImg) {
        this.eventImg = eventImg;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getEventTime() {
        return EventTime;
    }

    public void setEventTime(String eventTime) {
        EventTime = eventTime;
    }

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
