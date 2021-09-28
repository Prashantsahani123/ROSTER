package com.NEWROW.row.Data;

import java.io.Serializable;

public class DependentData implements Serializable {


    String  memberID,pic,memberName,memberDesignation,annsName,annetsName,visitorName, brought,rotarianID,rotarianName,clubName,districtDesignation,type,count,title;
    int responseType;
    public final static int NEW=1,UPDATED=2;
    Boolean isEdited=false;

    public int getResponseType() {
        return responseType;
    }

    public void setResponseType(int responseType) {
        this.responseType = responseType;
    }

    public Boolean getEdited() {
        return isEdited;
    }

    public void setEdited(Boolean edited) {
        isEdited = edited;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberDesignation() {
        return memberDesignation;
    }

    public void setMemberDesignation(String memberDesignation) {
        this.memberDesignation = memberDesignation;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getAnnsName() {
        return annsName;
    }

    public void setAnnsName(String annsName) {
        this.annsName = annsName;
    }

    public String getAnnetsName() {
        return annetsName;
    }

    public void setAnnetsName(String annetsName) {
        this.annetsName = annetsName;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getBrought() {
        return brought;
    }

    public void setBrought(String brought) {
        this.brought = brought;
    }

    public String getRotarianID() {
        return rotarianID;
    }

    public void setRotarianID(String rotarianID) {
        this.rotarianID = rotarianID;
    }

    public String getRotarianName() {
        return rotarianName;
    }

    public void setRotarianName(String rotarianName) {
        this.rotarianName = rotarianName;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getDistrictDesignation() {
        return districtDesignation;
    }

    public void setDistrictDesignation(String districtDesignation) {
        this.districtDesignation = districtDesignation;
    }
}
