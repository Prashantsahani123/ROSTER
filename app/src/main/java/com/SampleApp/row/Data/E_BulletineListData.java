package com.SampleApp.row.Data;

/**
 * Created by USER on 11-02-2016.
 */

public class E_BulletineListData {

    String ebulletinID,ebulletinTitle,ebulletinlink,ebulletinType,filterType,createDateTime,publishDateTime,expiryDateTime,isAdmin,isRead;



    public E_BulletineListData() {
    }


    public E_BulletineListData(String ebulletinID, String ebulletinTitle, String ebulletinlink, String ebulletinType, String filterType, String createDateTime, String publishDateTime, String expiryDateTime, String isAdmin, String isRead) {

        this.ebulletinID = ebulletinID;
        this.ebulletinTitle = ebulletinTitle;
        this.ebulletinlink = ebulletinlink;
        this.ebulletinType = ebulletinType;
        this.filterType = filterType;
        this.createDateTime = createDateTime;
        this.publishDateTime = publishDateTime;
        this.expiryDateTime = expiryDateTime;
        this.isAdmin = isAdmin;
        this.isRead = isRead;
    }

    public String getEbulletinID() {
        return ebulletinID;
    }

    public String getEbulletinTitle() {
        return ebulletinTitle;
    }

    public String getEbulletinlink() {
        return ebulletinlink;
    }

    public String getEbulletinType() {
        return ebulletinType;
    }

    public String getFilterType() {
        return filterType;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public String getPublishDateTime() {
        return publishDateTime;
    }

    public String getExpiryDateTime() {
        return expiryDateTime;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setEbulletinID(String ebulletinID) {
        this.ebulletinID = ebulletinID;
    }

    public void setEbulletinTitle(String ebulletinTitle) {
        this.ebulletinTitle = ebulletinTitle;
    }

    public void setEbulletinlink(String ebulletinlink) {
        this.ebulletinlink = ebulletinlink;
    }

    public void setEbulletinType(String ebulletinType) {
        this.ebulletinType = ebulletinType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    public void setPublishDateTime(String publishDateTime) {
        this.publishDateTime = publishDateTime;
    }

    public void setExpiryDateTime(String expiryDateTime) {
        this.expiryDateTime = expiryDateTime;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }


    @Override
    public String toString() {
        return "E_BulletineListData{" +
                "ebulletinID='" + ebulletinID + '\'' +
                ", ebulletinTitle='" + ebulletinTitle + '\'' +
                ", ebulletinlink='" + ebulletinlink + '\'' +
                ", ebulletinType='" + ebulletinType + '\'' +
                ", filterType='" + filterType + '\'' +
                ", createDateTime='" + createDateTime + '\'' +
                ", publishDateTime='" + publishDateTime + '\'' +
                ", expiryDateTime='" + expiryDateTime + '\'' +
                ", isAdmin='" + isAdmin + '\'' +
                ", isRead='" + isRead + '\'' +
                '}';
    }
}