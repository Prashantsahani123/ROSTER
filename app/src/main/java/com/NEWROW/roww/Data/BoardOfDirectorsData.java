package com.NEWROW.row.Data;

/**
 * Created by admin on 26-04-2017.
 */

public class BoardOfDirectorsData {
    String masterUID;
    String grpID;
    String profileID;
    String memberName;
    String pic;
    String memberMobile;
    String memeberDesignation;

    public BoardOfDirectorsData(){

    }


    public BoardOfDirectorsData(String masterUID, String grpID, String profileID, String memberName, String pic, String memberMobile,String memeberDesignation) {
        this.masterUID = masterUID;
        this.grpID = grpID;
        this.profileID = profileID;
        this.memberName = memberName;
        this.pic = pic;
        this.memberMobile = memberMobile;
        this.memeberDesignation = memeberDesignation;
    }

    public String getMasterUID() {
        return masterUID;
    }

    public void setMasterUID(String masterUID) {
        this.masterUID = masterUID;
    }

    public String getGrpID() {
        return grpID;
    }

    public void setGrpID(String grpID) {
        this.grpID = grpID;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getMemberMobile() {
        return memberMobile;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }

    public String getMemeberDesignation() {
        return memeberDesignation;
    }

    public void setMemeberDesignation(String memeberDesignation) {
        this.memeberDesignation = memeberDesignation;
    }

}
