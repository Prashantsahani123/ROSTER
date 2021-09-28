package com.NEWROW.row.Data;

/**
 * Created by admin on 28-04-2017.
 */

public class PastPresidentData {
    String tenureYear;
    String photopath;
    String pastPresidentId;
    String memberName;
    String grpID;

    public PastPresidentData(){

    }

    public PastPresidentData(String tenureYear, String photopath, String pastPresidentId, String memberName, String grpID) {
     this.tenureYear = tenureYear;
     this.photopath = photopath;
     this.pastPresidentId = pastPresidentId;
     this.memberName = memberName;
     this.grpID = grpID;
    }

    public String getTenureYear() {
        return tenureYear;
    }

    public void setTenureYear(String tenureYear) {
        this.tenureYear = tenureYear;
    }

    public String getPhotopath() {
        return photopath;
    }

    public void setPhotopath(String photopath) {
        this.photopath = photopath;
    }

    public String getPastPresidentId() {
        return pastPresidentId;
    }

    public void setPastPresidentId(String pastPresidentId) {
        this.pastPresidentId = pastPresidentId;
    }


    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getGrpID() {
        return grpID;
    }

    public void setGrpID(String grpID) {
        this.grpID = grpID;
    }

}
