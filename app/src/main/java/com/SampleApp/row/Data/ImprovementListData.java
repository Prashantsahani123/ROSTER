package com.SampleApp.row.Data;

/**
 * Created by USER on 02-02-2016.
 */
public class ImprovementListData {
    //String announID,announTitle,announceDEsc,createDateTime,publishDateTime,expiryDateTime,isAdmin,filterType,isRead;
    String improvementID, improvementTitle,improvementDesc,createDateTime,publishDateTime,expiryDateTime,isAdmin,filterType,isRead;

    public ImprovementListData(){

    }
    public ImprovementListData(String improvementID, String improvementTitle, String improvementDesc, String createDateTime, String publishDateTime, String expiryDateTime, String isAdmin, String filterType, String isRead) {
        this.improvementID = improvementID;
        this.improvementTitle = improvementTitle;
        this.improvementDesc = improvementDesc;
        this.createDateTime = createDateTime;
        this.publishDateTime = publishDateTime;
        this.expiryDateTime = expiryDateTime;
        this.isAdmin = isAdmin;
        this.filterType = filterType;
        this.isRead = isRead;
    }

    public String getImprovementID() {
        return improvementID;
    }

    public void setImprovementID(String improvementID) {
        this.improvementID = improvementID;
    }

    public String getImprovementTitle() {
        return improvementTitle;
    }

    public void setImprovementTitle(String improvementTitle) {
        this.improvementTitle = improvementTitle;
    }

    public String getImprovementDesc() {
        return improvementDesc;
    }

    public void setImprovementDesc(String improvementDesc) {
        this.improvementDesc = improvementDesc;
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
}