package com.NEWROW.row.Data.profiledata;

/**
 * Created by USER1 on 22-03-2017.
 */
public class ProfileMasterData {
    String masterId;
    String grpId;
    String profileId;
    String isAdmin;
    String memberName;
    String memberEmail;
    String memberMobile;
    String memberCountry;
    String profilePic;
    String familyPic;
    String isPersonalDetVisible;
    String isBussinessDetVisible;
    String isFamilyDetVisible;
    String isResidanceAddrVisible;
    String isBusinessAddrVisible;

    public ProfileMasterData(){

    }

    public ProfileMasterData(String masterId, String grpId, String profileId, String isAdmin, String memberName, String memberEmail, String memberMobile, String memberCountry, String profilePic,String familyPic, String isPersonalDetVisible, String isBussinessDetVisible, String isFamilyDetVisible, String isResidanceAddrVisible, String isBusinessAddrVisible) {
        this.masterId = masterId;
        this.grpId = grpId;
        this.profileId = profileId;
        this.isAdmin = isAdmin;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.memberMobile = memberMobile;
        this.memberCountry = memberCountry;
        this.profilePic = profilePic;
        this.familyPic = familyPic;
        this.isPersonalDetVisible = isPersonalDetVisible;
        this.isBussinessDetVisible = isBussinessDetVisible;
        this.isFamilyDetVisible = isFamilyDetVisible;
        this.isResidanceAddrVisible = isResidanceAddrVisible;
        this.isBusinessAddrVisible = isBusinessAddrVisible;

    }

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public String getGrpId() {
        return grpId;
    }

    public void setGrpId(String grpId) {
        this.grpId = grpId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public String getMemberMobile() {
        return memberMobile;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }

    public String getMemberCountry() {
        return memberCountry;
    }

    public void setMemberCountry(String memberCountry) {
        this.memberCountry = memberCountry;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getFamilyPic() {
        return familyPic;
    }

    public void setFamilyPic(String familyPic) {
        this.familyPic = familyPic;
    }

    public String getIsPersonalDetVisible() {
        return isPersonalDetVisible;
    }

    public void setIsPersonalDetVisible(String isPersonalDetVisible) {
        this.isPersonalDetVisible = isPersonalDetVisible;
    }

    public String getIsBussinessDetVisible() {
        return isBussinessDetVisible;
    }

    public void setIsBussinessDetVisible(String isBussinessDetVisible) {
        this.isBussinessDetVisible = isBussinessDetVisible;
    }

    public String getIsFamilyDetVisible() {
        return isFamilyDetVisible;
    }

    public void setIsFamilyDetVisible(String isFamilyDetVisible) {
        this.isFamilyDetVisible = isFamilyDetVisible;
    }

    public String getIsResidanceAddrVisible() {
        return isResidanceAddrVisible;
    }

    public void setIsResidanceAddrVisible(String isResidanceAddrVisible) {
        this.isResidanceAddrVisible = isResidanceAddrVisible;
    }

    public String getIsBusinessAddrVisible() {
        return isBusinessAddrVisible;
    }

    public void setIsBusinessAddrVisible(String isBusinessAddrVisible) {
        this.isBusinessAddrVisible = isBusinessAddrVisible;
    }


    @Override
    public String toString() {
        return "ProfileMasterData{" +
                "masterId='" + masterId + '\'' +
                ", grpId='" + grpId + '\'' +
                ", profileId='" + profileId + '\'' +
                ", isAdmin='" + isAdmin + '\'' +
                ", memberName='" + memberName + '\'' +
                ", memberMobile='" + memberMobile + '\'' +
                '}';
    }
}
