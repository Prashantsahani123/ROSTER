package com.SampleApp.row.Data;

/**
 * Created by USER on 02-02-2016.
 */
public class AnnouncementListData {
    String announID,announTitle,announceDEsc,createDateTime,publishDateTime,expiryDateTime,isAdmin,filterType,isRead;

    public AnnouncementListData() {
    }

    public AnnouncementListData(String announID, String announTitle, String announceDEsc, String createDateTime, String publishDateTime, String expiryDateTime, String isAdmin , String filterType, String isRead) {
        this.announID = announID;
        this.announTitle = announTitle;
        this.announceDEsc = announceDEsc;
        this.createDateTime = createDateTime;
        this.publishDateTime = publishDateTime;
        this.expiryDateTime = expiryDateTime;
        this.isAdmin = isAdmin;
        this.filterType = filterType;
        this.isRead = isRead;
    }

    public String getAnnounID() {
        return announID;
    }

    public void setAnnounID(String announID) {
        this.announID = announID;
    }

    public String getAnnounTitle() {
        return announTitle;
    }

    public void setAnnounTitle(String announTitle) {
        this.announTitle = announTitle;
    }

    public String getAnnounceDEsc() {
        return announceDEsc;
    }

    public void setAnnounceDEsc(String announceDEsc) {
        this.announceDEsc = announceDEsc;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getPublishDateTime() {
        return publishDateTime;
    }

    public void setPublishDateTime(String publishDateTime) {
        this.publishDateTime = publishDateTime;
    }

    public String getExpiryDateTime() {
        return expiryDateTime;
    }

    public void setExpiryDateTime(String expiryDateTime) {
        this.expiryDateTime = expiryDateTime;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    @Override
    public String toString() {
        return "AnnouncementListData{" +
                "announID='" + announID + '\'' +
                ", announTitle='" + announTitle + '\'' +
                ", announceDEsc='" + announceDEsc + '\'' +
                ", createDateTime='" + createDateTime + '\'' +
                ", publishDateTime='" + publishDateTime + '\'' +
                ", expiryDateTime='" + expiryDateTime + '\'' +
                ", isAdmin='" + isAdmin + '\'' +
                ", filterType='" + filterType + '\'' +
                ", isRead='" + isRead + '\'' +
                '}';
    }
}