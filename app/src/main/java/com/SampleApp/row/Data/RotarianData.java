package com.SampleApp.row.Data;

/**
 * Created by admin on 25-04-2017.
 */

public class RotarianData {
    String masterUID;
    String profileId;
    String memberName;
    String memberMobile;
    String designation;
    String clubName;
    String pic;

    public RotarianData(){

    }

    public RotarianData(String masterUID, String profileId, String memberName, String memberMobile, String designation, String clubName, String pic) {
        this.masterUID = masterUID;
        this.profileId = profileId;
        this.memberName = memberName;
        this.memberMobile = memberMobile;
        this.designation = designation;
        this.clubName = clubName;
        this.pic = pic;
    }

    public String getMasterUID() {
        return masterUID;
    }

    public void setMasterUID(String masterUID) {
        this.masterUID = masterUID;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberMobile() {
        return memberMobile;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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
        this.pic = pic.replaceAll(" ", "%20");
    }

}
