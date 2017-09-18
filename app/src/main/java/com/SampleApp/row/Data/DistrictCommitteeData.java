package com.SampleApp.row.Data;

/**
 * Created by admin on 26-07-2017.
 */

public class DistrictCommitteeData {
    String masterUID, grpID, profileID, memberName, membermobile, MemberDesignation, clubName,pic;

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
