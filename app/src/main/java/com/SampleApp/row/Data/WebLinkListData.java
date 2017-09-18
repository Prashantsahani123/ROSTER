package com.SampleApp.row.Data;

/**
 * Created by admin on 27-04-2017.
 */

public class WebLinkListData {
    String webLinkId;
    String groupId;
    String title;
    String description;
    String linkUrl;

    public WebLinkListData(){

    }


    public WebLinkListData(String webLinkId, String groupId, String title, String description, String linkUrl) {
        this.webLinkId = webLinkId;
        this.groupId = groupId;
        this.title = title;
        this.description = description;
        this.linkUrl = linkUrl;
    }

    public String getWebLinkId() {
        return webLinkId;
    }

    public void setWebLinkId(String webLinkId) {
        this.webLinkId = webLinkId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
}
