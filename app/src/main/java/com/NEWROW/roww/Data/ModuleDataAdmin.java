package com.NEWROW.row.Data;

public class ModuleDataAdmin {

    String moduleID="",title="",imgUrl="",url="",userName="",pass="",fkCountryID="";

    public ModuleDataAdmin(String moduleID,String title,String imgUrl,String url,String userName,String pass,String fkCountryID) {
        this.moduleID = moduleID;
        this.title = title;
        this.imgUrl = imgUrl;
        this.url = url;
        this.userName = userName;
        this.pass = pass;
        this.fkCountryID = fkCountryID;
    }

    public String getModuleID() {
        return moduleID;
    }

    public void setModuleID(String moduleID) {
        this.moduleID = moduleID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getFkCountryID() {
        return fkCountryID;
    }

    public void setFkCountryID(String fkCountryID) {
        this.fkCountryID = fkCountryID;
    }
}
