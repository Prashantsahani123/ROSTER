package com.NEWROW.row.Data.profiledata;

import java.util.Comparator;
import java.util.Hashtable;

/**
 * Created by USER1 on 21-03-2017.
 */
public class FamilyMemberData {

    String profileId, familyMemberId, memberName, relationship, memberDOB, emailID, memberDOA, contactNo, particulars, bloodGroup, countryID;

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    /*public FamilyMemberData(String profileId, String familyMemberId, String memberName, String relationship, String dob, String emailID, String anniversary, String contactNo, String particulars, String bloodGroup) {
        this.profileId = profileId;
        this.familyMemberId = familyMemberId;
        this.memberName = memberName;
        this.relationship = relationship;
        this.memberDOB = dob;
        this.emailID = emailID;
        this.memberDOA = anniversary;
        this.contactNo = contactNo;
        this.particulars = particulars;
        this.bloodGroup = bloodGroup;
    }*/

    public FamilyMemberData(String profileId, String familyMemberId, String memberName, String relationship, String memberDOB, String emailID, String memberDOA, String contactNo, String particulars, String bloodGroup, String countryID) {
        this.profileId = profileId;
        this.familyMemberId = familyMemberId;
        this.memberName = memberName;
        this.relationship = relationship;
        this.memberDOB = memberDOB;
        this.emailID = emailID;
        this.memberDOA = memberDOA;
        this.contactNo = contactNo;
        this.particulars = particulars;
        this.bloodGroup = bloodGroup;
        this.countryID = countryID;
    }

    public String getMemberDOB() {
        return memberDOB;
    }

    public void setMemberDOB(String memberDOB) {
        this.memberDOB = memberDOB;
    }

    public String getMemberDOA() {
        return memberDOA;
    }

    public void setMemberDOA(String memberDOA) {
        this.memberDOA = memberDOA;
    }

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
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

    public String getDob() {
        return memberDOB;
    }

    public void setDob(String dob) {
        this.memberDOB = dob;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getAnniversary() {
        return memberDOA;
    }

    public void setAnniversary(String anniversary) {
        this.memberDOA = anniversary;
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

    public static class FamilyComparator implements Comparator<FamilyMemberData> {
        @Override
        public int compare(FamilyMemberData lhs, FamilyMemberData rhs) {
            int lhsNum = 0;
            try {
                lhsNum = relSequence.get(lhs.getRelationship());
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            int rhsNum = 0;
            try {
                rhsNum = relSequence.get(rhs.getRelationship());
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (lhsNum > rhsNum) {
                return -1;
            } else if (lhsNum < rhsNum) {
                return 1;
            }
            return 0;
        }

    }

    private static Hashtable<String, Integer> relSequence = new Hashtable<>();

    static {
        //relSequence.put()
        relSequence.put("Spouse", 100);
        relSequence.put("spouse", 100);
        relSequence.put("Son", 99);
        relSequence.put("son", 99);
        relSequence.put("Daughter", 98);
        relSequence.put("daughter", 98);
        relSequence.put("Son-in-law", 97);
        relSequence.put("son-in-law", 97);
        relSequence.put("Daughter-in-law", 96);
        relSequence.put("daughter-in-law", 96);
        relSequence.put("Father", 95);
        relSequence.put("father", 95);
        relSequence.put("Mother", 94);
        relSequence.put("mother", 94);
        relSequence.put("Brother", 93);
        relSequence.put("brother", 93);
        relSequence.put("Sister", 92);
        relSequence.put("sister", 92);


    }

    @Override
    public String toString() {
        return "FamilyMemberData{" +
                "profileId='" + profileId + '\'' +
                ", familyMemberId='" + familyMemberId + '\'' +
                ", memberName='" + memberName + '\'' +
                ", relationship='" + relationship + '\'' +
                ", memberDOB='" + memberDOB + '\'' +
                ", emailID='" + emailID + '\'' +
                ", memberDOA='" + memberDOA + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", particulars='" + particulars + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", countryID='" + countryID + '\'' +
                '}';
    }
}
