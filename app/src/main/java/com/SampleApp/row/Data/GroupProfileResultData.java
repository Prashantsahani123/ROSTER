package com.SampleApp.row.Data;

/**
 * Created by user on 17-02-2016.
 */
public class GroupProfileResultData {

    String grpId;
    String grpName;
    String grpImg;
    String grpProfileId;
    String isMygrp;
    String membername;



    String membermobile;

    public GroupProfileResultData() {
    }

    public GroupProfileResultData(String grpId, String grpName, String grpImg, String grpProfileId, String isMygrp,String membername,String membermobile) {
        this.grpId = grpId;
        this.grpName = grpName;
        this.grpImg = grpImg;
        this.grpProfileId = grpProfileId;
        this.isMygrp = isMygrp;
        this.membername = membername;
        this.membermobile = membermobile;
    }


    public String getMembermobile() {
        return membermobile;
    }

    public String getMembername() {
        return membername;
    }

    public String setMembername(String membername) {
        this.membername = membername;
        return membername;
    }

    public String setMembermobile(String membermobile) {
        this.membermobile = membermobile;
        return membermobile;
    }

    public String getGrpId() {
        return grpId;
    }

    public String getGrpName() {
        return grpName;
    }

    public String getGrpImg() {
        return grpImg;
    }

    public String getGrpProfileId() {
        return grpProfileId;
    }

    public String getIsMygrp() {
        return isMygrp;
    }

    public void setGrpId(String grpId) {
        this.grpId = grpId;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public void setGrpImg(String grpImg) {
        this.grpImg = grpImg;
    }

    public String setGrpProfileId(String grpProfileId) {
        this.grpProfileId = grpProfileId;
        return grpProfileId;
    }

    public void setIsMygrp(String isMygrp) {
        this.isMygrp = isMygrp;
    }

    @Override
    public String toString() {
        return "GroupProfileResultData{" +
                "grpId='" + grpId + '\'' +
                ", grpName='" + grpName + '\'' +
                ", grpImg='" + grpImg + '\'' +
                ", grpProfileId='" + grpProfileId + '\'' +
                ", isMygrp='" + isMygrp + '\'' +
                ", membername='" + membername + '\'' +
                ", membermobile='" + membermobile + '\'' +
                '}';
    }
}
