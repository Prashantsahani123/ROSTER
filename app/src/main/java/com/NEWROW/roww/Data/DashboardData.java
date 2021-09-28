package com.NEWROW.row.Data;

/**
 * Created by admin on 23-02-2018.
 */

public class DashboardData {
    String title,description,todaysCount,ClubName,ClubCategory,type,link,publishDate,grpId,grpProfileID,isAdmin;

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getGrpId() {
        return grpId;
    }

    public void setGrpId(String grpId) {
        this.grpId = grpId;
    }

    public String getGrpProfileID() {
        return grpProfileID;
    }

    public void setGrpProfileID(String grpProfileID) {
        this.grpProfileID = grpProfileID;
    }

    public DashboardData() {
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTodaysCount() {
        return todaysCount;
    }

    public void setTodaysCount(String todaysCount) {
        this.todaysCount = todaysCount;
    }

    public String getClubName() {
        return ClubName;
    }

    public void setClubName(String clubName) {
        ClubName = clubName;
    }

    public String getClubCategory() {
        return ClubCategory;
    }

    public void setClubCategory(String clubCategory) {
        ClubCategory = clubCategory;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
