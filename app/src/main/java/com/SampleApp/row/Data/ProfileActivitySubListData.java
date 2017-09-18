package com.SampleApp.row.Data;

import java.io.Serializable;

/**
 * Created by USER1 on 20-09-2016.
 */
public class ProfileActivitySubListData implements Serializable {

    String uniquekey;
    String key;
    String value;
    String colType;

    String familyMemberId,memberName,relationship,dOB,emailID,anniversary,contactNo,particulars,bloodGroup;

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
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

    public String setMemberName(String memberName) {
        this.memberName = memberName;
        return memberName;
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

    public String setContactNo(String contactNo) {
        this.contactNo = contactNo;
        return contactNo;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public ProfileActivitySubListData() {
    }

    public ProfileActivitySubListData(String uniquekey, String key, String value, String colType) {
        this.uniquekey = uniquekey;
        this.key = key;
        this.value = value;
        this.colType = colType;
    }

    public String getUniquekey() {
        return uniquekey;
    }

    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }

    public String getKey() {
        return key;
    }

    public String setKey(String key) {
        this.key = key;
        return key;
    }

    public String getValue() {
        return value;
    }

    public String setValue(String value) {
        this.value = value;
        return value;
    }

    public String getColType() {
        return colType;
    }

    public void setColType(String colType) {
        this.colType = colType;
    }

    @Override
    public String toString() {
        return "ProfileData{" +
                "uniquekey='" + uniquekey + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", colType='" + colType + '\'' +
                '}';
    }
}
