package com.NEWROW.row.Data;

/**
 * Created by admin on 31-07-2017.
 */

public class ClubMemberData {
    String masterUID, grpID, profileID, memberName, membermobile, pic, distDesignation,classification;

    public ClubMemberData(String masterUID, String grpID, String profileID, String memberName, String membermobile, String pic, String distDesignation,String classification) {
        this.masterUID = masterUID;
        this.grpID = grpID;
        this.profileID = profileID;
        this.memberName = memberName;
        this.membermobile = membermobile;
        this.pic = pic;
        this.distDesignation = distDesignation;
        this.classification = classification;
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

    public String getMembermobile() {
        return membermobile;
    }

    public void setMembermobile(String membermobile) {
        this.membermobile = membermobile;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDistDesignation() {
        return distDesignation;
    }

    public void setDistDesignation(String distDesignation) {
        this.distDesignation = distDesignation;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
}
