package com.SampleApp.row.Data;

import java.util.ArrayList;

/**
 * Created by user on 29-08-2016.
 */
public class UpdateModuleLabels {
    String grpId;
    ArrayList<com.SampleApp.row.Data.moduleIDs> moduleIDs;
    String userID;
    String noOfmember;
    String memberCount;
    String Pwd;

    public UpdateModuleLabels(){

    }

    public UpdateModuleLabels(String grpId, ArrayList<com.SampleApp.row.Data.moduleIDs> moduleIDs, String userID, String noOfmember, String memberCount, String Pwd){
        this.grpId = grpId;
        this.moduleIDs = moduleIDs;
        this.userID = userID;
        this.noOfmember = noOfmember;
        this.memberCount = memberCount;
        this.Pwd = Pwd;
    }

    public String getGrpId() {
        return grpId;
    }

    public ArrayList<com.SampleApp.row.Data.moduleIDs> getModuleIDs() {
        return moduleIDs;
    }

    public String getUserID() {
        return userID;
    }

    public String getNoOfmember() {
        return noOfmember;
    }

    public String getMemberCount() {
        return memberCount;
    }

    public String getPwd() {
        return Pwd;
    }

    public void setGrpId(String grpId) {
        this.grpId = grpId;
    }

    public void setModuleIDs(ArrayList<com.SampleApp.row.Data.moduleIDs> moduleIDs) {
        this.moduleIDs = moduleIDs;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setNoOfmember(String noOfmember) {
        this.noOfmember = noOfmember;
    }

    public void setMemberCount(String memberCount) {
        this.memberCount = memberCount;
    }

    public void setPwd(String pwd) {
        Pwd = pwd;
    }







}
