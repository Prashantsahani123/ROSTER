package com.NEWROW.row.Data;

import java.io.Serializable;

/**
 * Created by user on 19-01-2016.
 */
public class GroupData implements Serializable {

    String grpId, grpName, grpImg,grpProfileId,myCategory,isGrpAdmin, expiryDate = "",expiryFlag="";
    public boolean box;

    public GroupData() {
    }

    public GroupData(String grpId, String grpName, String grpImg, String grpProfileId, String myCategory, String isGrpAdmin, boolean box) {
        this.grpId = grpId;
        this.grpName = grpName;
        this.grpImg = grpImg;
        this.grpProfileId = grpProfileId;
        this.myCategory = myCategory;
        this.isGrpAdmin = isGrpAdmin;
        this.box = box;
    }

    public String getGrpId() {
        return grpId;
    }

    public void setGrpId(String grpId) {
        this.grpId = grpId;
    }

    public String getGrpName() {
        return grpName;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public String getGrpImg() {
        return grpImg;
    }

    public void setGrpImg(String grpImg) {
        this.grpImg = grpImg;
    }

    public String getGrpProfileId() {
        return grpProfileId;
    }

    public void setGrpProfileId(String grpProfileId) {
        this.grpProfileId = grpProfileId;
    }

    public String getMyCategory() {
        return myCategory;
    }

    public void setMyCategory(String myCategory) {
        this.myCategory = myCategory;
    }

    public String getIsGrpAdmin() {
        return isGrpAdmin;
    }

    public void setIsGrpAdmin(String isGrpAdmin) {
        this.isGrpAdmin = isGrpAdmin;
    }

    public boolean isBox() {
        return box;
    }

    public void setBox(boolean box) {
        this.box = box;
    }

    @Override
    public String toString() {
        return "GroupData{" +
                "grpId='" + grpId + '\'' +
                ", grpName='" + grpName + '\'' +
                ", grpImg='" + grpImg + '\'' +
                ", grpProfileId='" + grpProfileId + '\'' +
                ", myCategory='" + myCategory + '\'' +
                ", isGrpAdmin='" + isGrpAdmin + '\'' +
                ", box=" + box + " expiray="+expiryDate+
                '}';
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getExpiryFlag() {
        return expiryFlag;
    }

    public void setExpiryFlag(String expiryFlag) {
        this.expiryFlag = expiryFlag;
    }
}
