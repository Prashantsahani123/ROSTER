package com.SampleApp.row.Data;

/**
 * Created by admin on 03-08-2017.
 */

public class DTNewslettersData {
    String ebulletinID,
            ebulletinTitle,
            ebulletinlink,
            ebulletinType,
            filterType,
            createDateTime,
            publishDateTime,
            expiryDateTime,
            isAdmin,
            isRead;

    public DTNewslettersData(String ebulletinID, String ebulletinTitle, String ebulletinlink, String ebulletinType, String filterType, String createDateTime, String publishDateTime, String expiryDateTime, String isAdmin, String isRead) {
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

    public void setEbulletinID(String ebulletinID) {
        this.ebulletinID = ebulletinID;
    }

    public String getEbulletinTitle() {
        return ebulletinTitle;
    }

    public void setEbulletinTitle(String ebulletinTitle) {
        this.ebulletinTitle = ebulletinTitle;
    }

    public String getEbulletinlink() {
        return ebulletinlink;
    }

    public void setEbulletinlink(String ebulletinlink) {
        this.ebulletinlink = ebulletinlink;
    }

    public String getEbulletinType() {
        return ebulletinType;
    }

    public void setEbulletinType(String ebulletinType) {
        this.ebulletinType = ebulletinType;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
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

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
}
