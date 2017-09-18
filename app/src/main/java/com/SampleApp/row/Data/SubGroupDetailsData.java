package com.SampleApp.row.Data;

/**
 * Created by USER on 16-02-2016.
 */
public class SubGroupDetailsData {
    String profileId,memname,mobile;

    public SubGroupDetailsData() {
    }

    public SubGroupDetailsData(String profileId, String memname, String mobile) {
        this.profileId = profileId;
        this.memname = memname;
        this.mobile = mobile;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getMemname() {
        return memname;
    }

    public void setMemname(String memname) {
        this.memname = memname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String
    toString() {
        return "SubGroupDetailsData{" +
                "profileId='" + profileId + '\'' +
                ", memname='" + memname + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
