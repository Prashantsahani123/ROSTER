package com.NEWROW.row.Data.profiledata;



import java.util.ArrayList;

/**
 * Created by USER1 on 03-04-2017.
 */
public class CompleteProfile {
    ProfileMasterData profileData;
    ArrayList<PersonalMemberDetails> personalDataList;
    ArrayList<BusinessMemberDetails> businessDataList;
    ArrayList<FamilyMemberData> familyMemberList;
    ArrayList<AddressData> addressData;

    public CompleteProfile(ProfileMasterData profileData, ArrayList<PersonalMemberDetails> personalDataList, ArrayList<BusinessMemberDetails> businessDataList, ArrayList<FamilyMemberData> familyMemberList, ArrayList<AddressData> addressData) {
        this.profileData = profileData;
        this.personalDataList = personalDataList;
        this.businessDataList = businessDataList;
        this.familyMemberList = familyMemberList;
        this.addressData = addressData;
    }

    public ProfileMasterData getProfileData() {
        return profileData;
    }

    public ArrayList<PersonalMemberDetails> getPersonalDataList() {
        return personalDataList;
    }

    public ArrayList<BusinessMemberDetails> getBusinessDataList() {
        return businessDataList;
    }

    public ArrayList<FamilyMemberData> getFamilyMemberList() {
        return familyMemberList;
    }

    public ArrayList<AddressData> getAddressData() {
        return addressData;
    }
}
