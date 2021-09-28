package com.NEWROW.row.Data;

/**
 * Created by user on 17-02-2017.
 */
public class CalendarNotifiationData {
    String profileId;
    String groupId;
    String membername;
    String memberMobile;
    String msg;
    String relation = "";

    public CalendarNotifiationData(){

    }

    public CalendarNotifiationData(String profileId, String groupId, String membername, String memberMobile, String msg){
        this.profileId = profileId;
        this.groupId =groupId;
        this.membername = membername;
        this.memberMobile = memberMobile;
        this.msg =msg;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMemberMobile() {
        return memberMobile;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
