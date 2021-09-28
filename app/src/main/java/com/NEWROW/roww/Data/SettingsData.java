package com.NEWROW.row.Data;

/**
 * Created by user on 29-03-2016.
 */
public class SettingsData {

String grpId,grpVal,grpName;

    public SettingsData() {
    }

    public SettingsData(String grpId, String grpVal, String grpName) {
        this.grpId = grpId;
        this.grpVal = grpVal;
        this.grpName = grpName;
    }

    public String getGrpId() {
        return grpId;
    }

    public void setGrpId(String grpId) {
        this.grpId = grpId;
    }

    public String getGrpVal() {
        return grpVal;
    }

    public void setGrpVal(String grpVal) {
        this.grpVal = grpVal;
    }

    public String getGrpName() {
        return grpName;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    @Override
    public String toString() {
        return "SettingsData{" +
                "grpId='" + grpId + '\'' +
                ", grpVal='" + grpVal + '\'' +
                ", grpName='" + grpName + '\'' +
                '}';
    }
}


