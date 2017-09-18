package com.SampleApp.row.Data;

/**
 * Created by USER on 05-06-2017.
 */

public class FamilywiseMemberData {
    private long memberId;
    private String memberName, familyMemberId, familyMemberName, relation;

    public FamilywiseMemberData(long memberId, String memberName, String familyMemberId, String familyMemberName, String relation) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.familyMemberId = familyMemberId;
        this.familyMemberName = familyMemberName;
        this.relation = relation;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getFamilyMemberId() {
        return familyMemberId;
    }

    public void setFamilyMemberId(String familyMemberId) {
        this.familyMemberId = familyMemberId;
    }

    public String getFamilyMemberName() {
        return familyMemberName;
    }

    public void setFamilyMemberName(String familyMemberName) {
        this.familyMemberName = familyMemberName;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
