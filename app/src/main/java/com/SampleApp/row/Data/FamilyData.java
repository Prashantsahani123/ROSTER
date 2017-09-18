package com.SampleApp.row.Data;

import java.io.Serializable;

/**
 * Created by USER on 29-03-2016.
 */
public class FamilyData implements Serializable{
   String familyMemberId,memberName,relationship,dOB,emailID,anniversary,contactNo,particulars,bloodGroup;

    public FamilyData() {
    }

    public FamilyData(String familyMemberId, String memberName, String relationship, String dOB, String emailID, String anniversary, String contactNo, String particulars, String bloodGroup) {
        this.familyMemberId = familyMemberId;
        this.memberName = memberName;
        this.relationship = relationship;
        this.dOB = dOB;
        this.emailID = emailID;
        this.anniversary = anniversary;
        this.contactNo = contactNo;
        this.particulars = particulars;
        this.bloodGroup = bloodGroup;
    }

    public String getFamilyMemberId() {
        return familyMemberId;
    }

    public void setFamilyMemberId(String familyMemberId) {
        this.familyMemberId = familyMemberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getdOB() {
        return dOB;
    }

    public void setdOB(String dOB) {
        this.dOB = dOB;
    }

    public String getEmailID() {
        return emailID;
    }

    public String setEmailID(String emailID) {
        this.emailID = emailID;
        return emailID;
    }

    public String getAnniversary() {
        return anniversary;
    }

    public void setAnniversary(String anniversary) {
        this.anniversary = anniversary;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    @Override
    public String toString() {
        return "FamilyData{" +
                "familyMemberId='" + familyMemberId + '\'' +
                ", memberName='" + memberName + '\'' +
                ", relationship='" + relationship + '\'' +
                ", dOB='" + dOB + '\'' +
                ", emailID='" + emailID + '\'' +
                ", anniversary='" + anniversary + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", particulars='" + particulars + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                '}';
    }
}
