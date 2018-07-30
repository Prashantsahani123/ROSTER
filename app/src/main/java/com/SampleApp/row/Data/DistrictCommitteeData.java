package com.SampleApp.row.Data;

import java.io.Serializable;

/**
 * Created by admin on 26-07-2017.
 */

public class DistrictCommitteeData implements Serializable {
    String masterUID, grpID,districtCommiteeId, profileID, memberName, membermobile,mailId,districtDesignation, clubName,pic,type,classification,keyword,businessName, MemberDesignation,businessAddress,rotaryId,donarrecognition;

    public DistrictCommitteeData(String masterUID, String grpID, String profileID, String memberName, String membermobile, String memberDesignation, String clubName, String pic) {
        this.masterUID = masterUID;
        this.grpID = grpID;
        this.profileID = profileID;
        this.memberName = memberName;
        this.membermobile = membermobile;
        MemberDesignation = memberDesignation;
        this.clubName = clubName;
        this.pic = pic;
    }

    public DistrictCommitteeData(String districtCommiteeId, String profileID, String memberName, String membermobile, String mailId, String districtDesignation, String clubName, String pic, String type, String classification, String keyword, String businessName, String memberDesignation, String businessAddress, String rotaryId, String donarrecognition) {
        this.districtCommiteeId = districtCommiteeId;
        this.profileID = profileID;
        this.memberName = memberName;
        this.membermobile = membermobile;
        this.mailId = mailId;
        this.districtDesignation = districtDesignation;
        this.clubName = clubName;
        this.pic = pic;
        this.type = type;
        this.classification = classification;
        this.keyword = keyword;
        this.businessName = businessName;
        MemberDesignation = memberDesignation;
        this.businessAddress = businessAddress;
        this.rotaryId = rotaryId;
        this.donarrecognition = donarrecognition;
    }




    public String getDistrictCommiteeId() {
        return districtCommiteeId;
    }

    public void setDistrictCommiteeId(String districtCommiteeId) {
        this.districtCommiteeId = districtCommiteeId;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public String getDistrictDesignation() {
        return districtDesignation;
    }

    public void setDistrictDesignation(String districtDesignation) {
        this.districtDesignation = districtDesignation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getRotaryId() {
        return rotaryId;
    }

    public void setRotaryId(String rotaryId) {
        this.rotaryId = rotaryId;
    }

    public String getDonarrecognition() {
        return donarrecognition;
    }

    public void setDonarrecognition(String donarrecognition) {
        this.donarrecognition = donarrecognition;
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

    public String getMemberDesignation() {
        return MemberDesignation;
    }

    public void setMemberDesignation(String memberDesignation) {
        MemberDesignation = memberDesignation;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
