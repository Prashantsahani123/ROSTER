package com.NEWROW.row.ModuleData;

import java.io.Serializable;

/*Created By Gaurav on 9th June 2020*/

public class RotaryIndiaAdminModule implements Serializable {

    String moduleName, groupId, moduleId, moduleOrderNo, image, isweblink, URL;

    //Default Constructor
    public RotaryIndiaAdminModule() {
    }

    public RotaryIndiaAdminModule(String moduleName, String groupId, String moduleId, String moduleOrderNo, String image, String isweblink, String URL) {
        this.moduleName = moduleName;
        this.groupId = groupId;
        this.moduleId = moduleId;
        this.moduleOrderNo = moduleOrderNo;
        this.image = image;
        this.isweblink = isweblink;
        this.URL = URL;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleOrderNo() {
        return moduleOrderNo;
    }

    public void setModuleOrderNo(String moduleOrderNo) {
        this.moduleOrderNo = moduleOrderNo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIsweblink() {
        return isweblink;
    }

    public void setIsweblink(String isweblink) {
        this.isweblink = isweblink;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
