package com.NEWROW.row.Data;

/**
 * Created by admin on 18-05-2017.
 */

public class ClubHistoryData {
    String title;
    String Description;
    String grpID;

    public ClubHistoryData(String title, String description, String grpID) {
        this.title = title;
        this.Description = description;
        this.grpID = grpID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getGrpID() {
        return grpID;
    }

    public void setGrpID(String grpID) {
        this.grpID = grpID;
    }
}
