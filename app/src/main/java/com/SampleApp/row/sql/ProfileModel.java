package com.SampleApp.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.SampleApp.row.Data.CountryData;
import com.SampleApp.row.Data.DirectoryData;
import com.SampleApp.row.Data.FamilywiseMemberData;
import com.SampleApp.row.Data.profiledata.AddressData;
import com.SampleApp.row.Data.profiledata.BusinessMemberDetails;
import com.SampleApp.row.Data.profiledata.ClassificationData;
import com.SampleApp.row.Data.profiledata.CompleteProfile;
import com.SampleApp.row.Data.profiledata.DynamicFieldData;
import com.SampleApp.row.Data.profiledata.FamilyMemberData;
import com.SampleApp.row.Data.profiledata.PersonalMemberDetails;
import com.SampleApp.row.Data.profiledata.ProfileMasterData;
import com.SampleApp.row.Data.profiledata.Separator;
import com.SampleApp.row.Utils.Utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Created by USER1 on 22-03-2017.
 */
public class ProfileModel {
    private Context context;
    private SQLiteDatabase db;

    public ProfileModel(Context context) {
        this.context = context;
        this.db = DBConnection.getInstance(context);
    }


    /*DirectoryData data = new DirectoryData();

                    data.setMasterUID(objects.getString("masterUID").toString());
                    data.setGrpID(objects.getString("grpID").toString());
                    data.setProfileID(objects.getString("profileID").toString());
                    data.setGroupName(objects.getString("groupName").toString());
                    data.setMemberName(objects.getString("memberName").toString());
                    data.setPic(objects.getString("pic").toString());
                    data.setMembermobile(objects.getString("membermobile").toString());
                    data.setGrpCount(objects.getString("grpCount").toString());*/

    public ArrayList<DirectoryData> getMemberForSelection(long groupId) {
        ArrayList<DirectoryData> list = new ArrayList<>();

        try {
            Cursor cur = db.rawQuery("select * from "+Tables.ProfileMaster.TABLE_NAME+" where "+Tables.ProfileMaster.Columns.GROUP_ID+"="+groupId+" order by LOWER("+Tables.ProfileMaster.Columns.MEMBER_NAME+")", null);
            while(cur.moveToNext()) {
                String masterId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MASTER_ID));
                String grpId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.GROUP_ID));
                String profileId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_ID));
                String isAdmin = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_ADMIN));
                String memberName = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_NAME));
                String memberEmail = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_EMAIL));
                String memberMobile = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_MOBILE));
                String memberCountry = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_COUNTRY));
                String profilePic = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_PIC)).replaceAll(" ", "%20");
                String familyPic = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.FAMILY_PIC));
                String isPersonalDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_PERSONAL_DET_VISIBLE));
                String isBussinessDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_BUSSINESS_DET_VISBILE));
                String isFamilyDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_FAMILY_DET_VISIBLE));

                String isResidanceAddrVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_RESIDANCE_ADDR_VISIBLE));
                String isBusinessAddrVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_BUSINESS_ADDR_VISIBLE));

                DirectoryData data = new DirectoryData();

                data.setMasterUID(masterId);
                data.setGrpID(grpId);
                data.setProfileID(profileId);
                data.setGroupName("");
                data.setMemberName(memberName);
                data.setPic(profilePic);
                data.setMembermobile(memberMobile);
                data.setGrpCount("");
                list.add(data);
            }
        } catch(Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }

        return list;
    }
    public ArrayList<ProfileMasterData> getMembers(long groupId) {
        ArrayList<ProfileMasterData> list = new ArrayList<>();
        try {
            Cursor cur = db.rawQuery("select * from "+Tables.ProfileMaster.TABLE_NAME+" where "+Tables.ProfileMaster.Columns.GROUP_ID+"="+groupId+" order by LOWER("+Tables.ProfileMaster.Columns.MEMBER_NAME+")", null);
            while(cur.moveToNext()) {
                String masterId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MASTER_ID));
                String grpId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.GROUP_ID));
                String profileId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_ID));
                String isAdmin = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_ADMIN));
                String memberName = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_NAME));
                String memberEmail = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_EMAIL));
                String memberMobile = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_MOBILE));
                String memberCountry = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_COUNTRY));
                String profilePic = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_PIC)).replaceAll(" ", "%20");

                Utils.log("Profile Pic Path : "+profilePic);
                String familyPic = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.FAMILY_PIC));
                String isPersonalDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_PERSONAL_DET_VISIBLE));
                String isBussinessDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_BUSSINESS_DET_VISBILE));
                String isFamilyDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_FAMILY_DET_VISIBLE));

                String isResidanceAddrVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_RESIDANCE_ADDR_VISIBLE));
                String isBusinessAddrVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_BUSINESS_ADDR_VISIBLE));

                ProfileMasterData data = new ProfileMasterData(masterId, grpId, ""+profileId, isAdmin, memberName, memberEmail, memberMobile, memberCountry, profilePic,familyPic, isPersonalDetVisible, isBussinessDetVisible, isFamilyDetVisible, isResidanceAddrVisible, isBusinessAddrVisible);
                list.add(data);

            }
        } catch(Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }

        return list;
    }

    public void updateProfilePic(String profileID,String url, String type,String grpID){
        ContentValues contentValues = new ContentValues();
        if(type.equalsIgnoreCase("profile")) {
            contentValues.put(Tables.ProfileMaster.Columns.PROFILE_PIC, url);
        }else{
            contentValues.put(Tables.ProfileMaster.Columns.FAMILY_PIC, url);
        }
        db.update(Tables.ProfileMaster.TABLE_NAME,contentValues,Tables.ProfileMaster.Columns.PROFILE_ID+"="+profileID+" and "+Tables.ProfileMaster.Columns.GROUP_ID+"="+grpID,null);
    }

    public ArrayList<ProfileMasterData> getMembers(long groupId, String classification) {
        ArrayList<ProfileMasterData> list = new ArrayList<>();
        try {

            Cursor cur = db.rawQuery("select masterId, grpId, ProfileMaster.profileId, isAdmin, memberName, memberEmail, memberMobile, memberCountry, profilePic, " +
                    "familyPic, isPersonalDetVisible, isBussinessDetVisible, isFamilyDetVisible, isResidanceAddrVisible, isBusinessAddrVisible from "+
                    "ProfileMaster, PersonalMemberDetails where "+
                    Tables.ProfileMaster.Columns.GROUP_ID+"="+groupId+
                    " and ProfileMaster.profileId=PersonalMemberDetails.profileId " +
                    " and uniquekey='designation' " +
                    " and value='"+classification+"'" +
                    " order by LOWER("+Tables.ProfileMaster.Columns.MEMBER_NAME+")", null);

            while(cur.moveToNext()) {
                String masterId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MASTER_ID));
                String grpId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.GROUP_ID));
                String profileId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_ID));
                String isAdmin = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_ADMIN));
                String memberName = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_NAME));
                String memberEmail = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_EMAIL));
                String memberMobile = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_MOBILE));
                String memberCountry = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_COUNTRY));
                String profilePic = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_PIC)).replaceAll(" ", "%20");
                String familyPic = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.FAMILY_PIC));
                String isPersonalDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_PERSONAL_DET_VISIBLE));
                String isBussinessDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_BUSSINESS_DET_VISBILE));
                String isFamilyDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_FAMILY_DET_VISIBLE));

                String isResidanceAddrVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_RESIDANCE_ADDR_VISIBLE));
                String isBusinessAddrVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_BUSINESS_ADDR_VISIBLE));

                ProfileMasterData data = new ProfileMasterData(masterId, grpId, ""+profileId, isAdmin, memberName, memberEmail, memberMobile, memberCountry, profilePic,familyPic, isPersonalDetVisible, isBussinessDetVisible, isFamilyDetVisible, isResidanceAddrVisible, isBusinessAddrVisible);
                list.add(data);

            }
        } catch(Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }

        return list;
    }

    /*public ArrayList<BoardOfDirectorsData> getMembers(long groupId) {
        ArrayList<ProfileMasterData> list = new ArrayList<>();
        try {
            Cursor cur = db.rawQuery("select masterId, grpId, ProfileMaster.profileId, isAdmin, memberName, memberEmail, memberMobile, memberCountry, profilePic, " +
                    "familyPic, isPersonalDetVisible, isBussinessDetVisible, isFamilyDetVisible, isResidanceAddrVisible, isBusinessAddrVisible from "+
                    "ProfileMaster, BusinessMemberDetails where "+
                    Tables.ProfileMaster.Columns.GROUP_ID+"="+groupId+
                    " and ProfileMaster.profileId=BusinessMemberDetails.profileId " +
                    " and uniquekey='isBOD' " +
                    " and value='1'"+
                    " order by LOWER("+Tables.ProfileMaster.Columns.MEMBER_NAME+")", null);

            while(cur.moveToNext()) {
                String masterId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MASTER_ID));
                String grpId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.GROUP_ID));
                String profileId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_ID));
                String isAdmin = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_ADMIN));
                String memberName = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_NAME));
                String memberEmail = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_EMAIL));
                String memberMobile = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_MOBILE));
                String memberCountry = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_COUNTRY));
                String profilePic = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_PIC));
                String familyPic = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.FAMILY_PIC));
                String isPersonalDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_PERSONAL_DET_VISIBLE));
                String isBussinessDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_BUSSINESS_DET_VISBILE));
                String isFamilyDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_FAMILY_DET_VISIBLE));

                String isResidanceAddrVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_RESIDANCE_ADDR_VISIBLE));
                String isBusinessAddrVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_BUSINESS_ADDR_VISIBLE));

                ProfileMasterData data = new ProfileMasterData(masterId, grpId, ""+profileId, isAdmin, memberName, memberEmail, memberMobile, memberCountry, profilePic,familyPic, isPersonalDetVisible, isBussinessDetVisible, isFamilyDetVisible, isResidanceAddrVisible, isBusinessAddrVisible);
                list.add(data);

            }
        } catch(Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }

        return list;
    }*/

    public ArrayList<ProfileMasterData> getMembers(long groupId, String classification, String searchText) {
        ArrayList<ProfileMasterData> list = new ArrayList<>();
        searchText = "%"+searchText.replaceAll(" ", "%")+"%";
        try {

            Cursor cur = db.rawQuery("select masterId, grpId, ProfileMaster.profileId, isAdmin, memberName, memberEmail, memberMobile, memberCountry, profilePic, " +
                    "familyPic, isPersonalDetVisible, isBussinessDetVisible, isFamilyDetVisible, isResidanceAddrVisible, isBusinessAddrVisible from "+
                    "ProfileMaster, PersonalMemberDetails where "+
                    Tables.ProfileMaster.Columns.GROUP_ID+"="+groupId+
                    " and ProfileMaster.profileId=PersonalMemberDetails.profileId " +
                    " and uniquekey='designation' " +
                    " and value='"+classification+"' " +
                    " and memberName like '"+searchText+"'" +
                    " order by LOWER("+Tables.ProfileMaster.Columns.MEMBER_NAME+")", null);

            while(cur.moveToNext()) {
                String masterId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MASTER_ID));
                String grpId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.GROUP_ID));
                String profileId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_ID));
                String isAdmin = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_ADMIN));
                String memberName = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_NAME));
                String memberEmail = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_EMAIL));
                String memberMobile = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_MOBILE));
                String memberCountry = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_COUNTRY));
                String profilePic = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_PIC)).replaceAll(" ", "%20");;
                String familyPic = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.FAMILY_PIC));
                String isPersonalDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_PERSONAL_DET_VISIBLE));
                String isBussinessDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_BUSSINESS_DET_VISBILE));
                String isFamilyDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_FAMILY_DET_VISIBLE));

                String isResidanceAddrVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_RESIDANCE_ADDR_VISIBLE));
                String isBusinessAddrVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_BUSINESS_ADDR_VISIBLE));

                ProfileMasterData data = new ProfileMasterData(masterId, grpId, ""+profileId, isAdmin, memberName, memberEmail, memberMobile, memberCountry, profilePic,familyPic, isPersonalDetVisible, isBussinessDetVisible, isFamilyDetVisible, isResidanceAddrVisible, isBusinessAddrVisible);
                list.add(data);

            }
        } catch(Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }

        return list;
    }


    public ArrayList<ProfileMasterData> search(long groupId, String keyword) {
        ArrayList<ProfileMasterData> list = new ArrayList<>();
        keyword = '%'+keyword.replaceAll(" ", "%") + '%';
        try {
            Cursor cur = db.rawQuery("select * from "+Tables.ProfileMaster.TABLE_NAME+" " +
                    "where "+Tables.ProfileMaster.Columns.GROUP_ID+"="+groupId+
                    " and ("+Tables.ProfileMaster.Columns.MEMBER_NAME+" like '"+keyword+
                    "' or "+Tables.ProfileMaster.Columns.MEMBER_MOBILE+" like '"+keyword+"' ) order by LOWER("+
                    Tables.ProfileMaster.Columns.MEMBER_NAME+") COLLATE NOCASE ASC", null);
            while(cur.moveToNext()) {
                String masterId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MASTER_ID));
                String grpId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.GROUP_ID));
                String profileId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_ID));
                String isAdmin = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_ADMIN));
                String memberName = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_NAME));
                String memberEmail = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_EMAIL));
                String memberMobile = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_MOBILE));
                String memberCountry = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_COUNTRY));
                String profilePic = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_PIC)).replaceAll(" ", "%20");;
                String familyPic = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.FAMILY_PIC));
                String isPersonalDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_PERSONAL_DET_VISIBLE));
                String isBussinessDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_BUSSINESS_DET_VISBILE));
                String isFamilyDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_FAMILY_DET_VISIBLE));
                String isResidanceAddrVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_RESIDANCE_ADDR_VISIBLE));
                String isBusinessAddrVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_BUSINESS_ADDR_VISIBLE));

                ProfileMasterData data = new ProfileMasterData(masterId, grpId, ""+profileId, isAdmin, memberName, memberEmail, memberMobile, memberCountry, profilePic,familyPic, isPersonalDetVisible, isBussinessDetVisible, isFamilyDetVisible, isResidanceAddrVisible, isBusinessAddrVisible);
                list.add(data);

            }
        } catch(Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<FamilywiseMemberData> getFamilywiseMember(long groupId) {
        ArrayList<FamilywiseMemberData> list = new ArrayList<>();

        try {
            //memberId, memberName, familyMemberId, familyMemberName, relation
            Cursor cur = db.rawQuery("select ProfileMaster.profileId, ProfileMaster.memberName, FamilyMemberDetail.memberName, relationship, familyMemberId from "+Tables.ProfileMaster.TABLE_NAME+" , " +Tables.FamilyMemberDetail.TABLE_NAME +
                    " where "+Tables.ProfileMaster.Columns.GROUP_ID+"="+groupId+
                    " and ProfileMaster.profileId=FamilyMemberDetail.profileId"+
                    " order by LOWER(ProfileMaster."+Tables.ProfileMaster.Columns.MEMBER_NAME+") COLLATE NOCASE ASC", null);
            while(cur.moveToNext()) {
                long profileId = cur.getLong(0);
                String memberName = cur.getString(1);
                String familyMemberName = cur.getString(2);
                String relationship = cur.getString(3);
                String familyMemberId = cur.getString(4);
                FamilywiseMemberData data = new FamilywiseMemberData(profileId, memberName, familyMemberId, familyMemberName, relationship);

                list.add(data);

            }
        } catch(Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<FamilywiseMemberData> getFamilywiseMember(long groupId, String keyword) {
        ArrayList<FamilywiseMemberData> list = new ArrayList<>();
        keyword = "%"+keyword.replaceAll(" ", "%")+"%";
        try {
            Cursor cur = db.rawQuery("select ProfileMaster.profileId, ProfileMaster.memberName, FamilyMemberDetail.memberName, relationship, familyMemberId from "+Tables.ProfileMaster.TABLE_NAME+" , " +Tables.FamilyMemberDetail.TABLE_NAME +
                    " where "+Tables.ProfileMaster.Columns.GROUP_ID+"="+groupId+
                    " and ProfileMaster.profileId=FamilyMemberDetail.profileId" +
                    " and FamilyMemberDetail.memberName like '"+keyword+"'"+
                    " order by LOWER(ProfileMaster."+Tables.ProfileMaster.Columns.MEMBER_NAME+") COLLATE NOCASE ASC", null);
            while(cur.moveToNext()) {
                long profileId = cur.getLong(0);
                String memberName = cur.getString(1);
                String familyMemberName = cur.getString(2);
                String relationship = cur.getString(3);
                String familyMemberId = cur.getString(4);
                FamilywiseMemberData data = new FamilywiseMemberData(profileId, memberName, familyMemberId, familyMemberName, relationship);
                list.add(data);

            }
        } catch(Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }
        return list;
    }
    // Functions to get complete profile of a member
    public ArrayList<Object> getProfileInfo(long profileId) {
        ArrayList<Object> list = new ArrayList<>();

        // Load basic profile information i.e. ProfileMasterData
        Cursor cur = db.rawQuery("select * from "+Tables.ProfileMaster.TABLE_NAME+" where "+Tables.ProfileMaster.Columns.PROFILE_ID+"="+profileId, null);
        if (cur.moveToNext()) {
            String masterId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MASTER_ID));
            String grpId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.GROUP_ID));

            String isAdmin = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_ADMIN));
            String memberName = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_NAME));
            String memberEmail = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_EMAIL));
            String memberMobile = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_MOBILE));
            String memberCountry = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_COUNTRY));
            String profilePic = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_PIC)).replaceAll(" ", "%20");
            Utils.log("Profile Pice Path : "+profilePic);
            String familyPic = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.FAMILY_PIC));
            String isPersonalDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_PERSONAL_DET_VISIBLE));
            String isBussinessDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_BUSSINESS_DET_VISBILE));
            String isFamilyDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_FAMILY_DET_VISIBLE));
            //String isFamilyDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_FAMILY_DET_VISIBLE));

            String isResidanceAddrVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_RESIDANCE_ADDR_VISIBLE));
            String isBusinessAddrVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_BUSINESS_ADDR_VISIBLE));

            ProfileMasterData data = new ProfileMasterData(masterId, grpId, ""+profileId, isAdmin, memberName, memberEmail, memberMobile, memberCountry, profilePic, familyPic,isPersonalDetVisible, isBussinessDetVisible, isFamilyDetVisible, isResidanceAddrVisible, isBusinessAddrVisible);
            /*
            * First object in the list is Profile master data which is basically
            * contains name, profile pic url, primary email, primary mobile, country*/
            list.add(data);

            // Get all mobile number from personal and business
            if ( isPersonalDetVisible.equals("yes")) {
                list.addAll(getPhoneNumbers(profileId));
            }
            if ( isBussinessDetVisible.equals("yes")) {
                list.addAll(getBusinessPhoneNumbers(profileId));
            }

            if ( list.size() > 1) {
                list.add(new Separator());
            }

            // Get all email ids from personal and business information
            if ( isPersonalDetVisible.equals("yes")) {
                list.addAll(getEmailIds(profileId));
            }

            if ( isBussinessDetVisible.equals("yes")) {
                list.addAll(getBusinessEmails(profileId));
            }

            if ( ! (list.get(list.size()-1) instanceof Separator) )
                list.add(new Separator());
            //-------------------End of loading email ids--------------------//
            try {
                if (!(list.get(list.size() - 1) instanceof Separator))
                    list.add(new Separator());
            } catch(ArrayIndexOutOfBoundsException ae) {}

            if (isBussinessDetVisible.equals("yes")) {
                list.addAll(getOtherBusinessInfo(profileId));
            }
            //-------------------Loading business name and designation------------//


            ArrayList<AddressData> addrList = getAddresses(profileId, isBusinessAddrVisible, isResidanceAddrVisible);

            for(int i=0;i<addrList.size();i++) {
                if ( addrList.get(i).getAddressType().equals("Business")) {
                    AddressData addressData=addrList.get(i);
                    if(!addressData.getAddress().trim().isEmpty() || !addressData.getCity().trim().isEmpty() || !addressData.getState().trim().isEmpty() || !addressData.getCountry().trim().isEmpty() || !addressData.getPincode().trim().isEmpty() || !addressData.getFax().trim().isEmpty()){
                        list.add(addrList.get(i));
                        list.add(new Separator());
                    }

                    if ( addrList.get(i).getPhoneNo().trim().equals("")) {

                    } else {
                        String countryCode = searchForCountryCode(addrList.get(i).getCountry());
                        BusinessMemberDetails businessContact = new BusinessMemberDetails("" + profileId, "0", "phone_no", "Business Contact No.", countryCode+addrList.get(i).getPhoneNo(), "Contact", "1", "y");
                        list.add(businessContact);
                    }
                    break;
                }
            }
            try {
                if (!(list.get(list.size() - 1) instanceof Separator))
                    list.add(new Separator());
            } catch(ArrayIndexOutOfBoundsException ae) {}

            // Get all addresses from personal and business information
            // list.addAll(addrList);
            try {
                if (!(list.get(list.size() - 1) instanceof Separator))
                    list.add(new Separator());
            } catch(ArrayIndexOutOfBoundsException ae) {}

            if ( isPersonalDetVisible.equals("yes")) {
                list.addAll(getOtherPersonalInfo(profileId));
            }

            for(int i=0;i<addrList.size();i++) {
                if ( addrList.get(i).getAddressType().equals("Residence")) {
                    AddressData addressData=addrList.get(i);
                    if(!addressData.getAddress().trim().isEmpty() || !addressData.getCity().trim().isEmpty() || !addressData.getState().trim().isEmpty() || !addressData.getCountry().trim().isEmpty() || !addressData.getPincode().trim().isEmpty() || !addressData.getFax().trim().isEmpty()){
                        list.add(addrList.get(i));
                        list.add(new Separator());
                    }

                    if ( addrList.get(i).getPhoneNo().trim().equals("")) {

                    } else {
                        String countryCode = searchForCountryCode(addrList.get(i).getCountry());
                        PersonalMemberDetails residenceContact = new PersonalMemberDetails("" + profileId, "0", "phone_no", "Residential Contact No.", countryCode+addrList.get(i).getPhoneNo(), "Contact", "1", "y");
                        list.add(residenceContact);
                    }
                    break;
                }
            }
            try {
                if (!(list.get(list.size() - 1) instanceof Separator))
                    list.add(new Separator());
            } catch(ArrayIndexOutOfBoundsException ae) {}


            if ( isFamilyDetVisible.equals("yes")) {
                list.addAll(getFamilyMembers(profileId));
            }
        }

        PersonalMemberDetailsModel pm = new PersonalMemberDetailsModel(context);
        //pm.printTable();
        return list;
    }

    public String searchForCountryCode(String countryName) {
        ArrayList<CountryData> countryList = Utils.getCountryList(context);
        int n = countryList.size();
        for (int i = 0; i < n; i++) {
            CountryData cd = countryList.get(i);
            String name = cd.getCountryName();
            if (name.equalsIgnoreCase(countryName)) {
                return cd.getCountryCode()+ " ";
            }
        }
        return "";
    }
    public void printTable() {
        Cursor cursor = db.rawQuery("select * from "+Tables.ProfileMaster.TABLE_NAME, null);
        int n = cursor.getColumnCount();
        String columns = "";
        for(int i=0;i<n;i++) {
            columns = columns +  cursor.getColumnName(i) + " - ";
        }
        Log.e("ColumnName", columns);

        while(cursor.moveToNext()) {
            String rec = "";
            for(int i=0;i<n;i++) {
                rec = rec + cursor.getString(i) +" - ";
            }
            Log.e("row", "♦♦♦♦"+rec);
        }
        cursor.close();
    }

    // Functions to get complete profile of a member
    public ArrayList<Object> getFamilyProfilesInfo(long profileId) {
        ArrayList<Object> list = new ArrayList<>();

        // Load basic profile information i.e. ProfileMasterData
        Cursor cur = db.rawQuery("select * from "+Tables.ProfileMaster.TABLE_NAME+" where "+Tables.ProfileMaster.Columns.PROFILE_ID+"="+profileId, null);
        if (cur.moveToNext()) {
            String masterId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MASTER_ID));
            String grpId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.GROUP_ID));

            String isAdmin = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_ADMIN));
            String memberName = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_NAME));
            String memberEmail = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_EMAIL));
            String memberMobile = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_MOBILE));
            String memberCountry = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_COUNTRY));
            String profilePic = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_PIC)).replaceAll(" ", "%20");
            String familyPic = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.FAMILY_PIC));
            String isPersonalDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_PERSONAL_DET_VISIBLE));
            String isBussinessDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_BUSSINESS_DET_VISBILE));
            String isFamilyDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_FAMILY_DET_VISIBLE));
            //String isFamilyDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_FAMILY_DET_VISIBLE));

            String isResidanceAddrVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_RESIDANCE_ADDR_VISIBLE));
            String isBusinessAddrVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_BUSINESS_ADDR_VISIBLE));

            ProfileMasterData data = new ProfileMasterData(masterId, grpId, ""+profileId, isAdmin, memberName, memberEmail, memberMobile, memberCountry, profilePic, familyPic,isPersonalDetVisible, isBussinessDetVisible, isFamilyDetVisible, isResidanceAddrVisible, isBusinessAddrVisible);
            /*
            * First object in the list is Profile master data which is basically
            * contains name, profile pic url, primary email, primary mobile, country*/
            list.add(data);

            // Get all mobile number from personal and business
            if ( isPersonalDetVisible.equals("yes")) {
                list.addAll(getPhoneNumbers(profileId));
            }

            if ( ! (list.get(list.size()-1) instanceof Separator) )
                list.add(new Separator());


            //-------------------Loading business name and designation------------//

            if ( isPersonalDetVisible.equals("yes")) {
                list.addAll(getClubDesignation(profileId));
            }
            list.addAll(getFamilyMembers(profileId));

        }
        return list;
    }

    public ArrayList<PersonalMemberDetails> getPhoneNumbers(long profileId) {
        PersonalMemberDetailsModel model = new PersonalMemberDetailsModel(context);
        return model.getPhoneNumbers(profileId);
    }



    public ArrayList<BusinessMemberDetails> getBusinessPhoneNumbers(long profileId) {
        BusinessMemberDetailsModel model = new BusinessMemberDetailsModel(context);
        return model.getPhoneNumbers(profileId);
    }

    public ArrayList<BusinessMemberDetails> getBusinessEmails(long profileId) {
        BusinessMemberDetailsModel model = new BusinessMemberDetailsModel(context);
        return model.getEmailIds(profileId);
    }

    public ArrayList<PersonalMemberDetails> getEmailIds(long profileId) {
        PersonalMemberDetailsModel model = new PersonalMemberDetailsModel(context);
        return model.getEmailIds(profileId);
    }
    public ArrayList<PersonalMemberDetails> getPersonalInfo(long profileId) {
        PersonalMemberDetailsModel model = new PersonalMemberDetailsModel(context);
        return model.getPersonalMemberDetails(profileId);
    }

    public ArrayList<Object> getOtherPersonalInfo(long profileId) {
        PersonalMemberDetailsModel model = new PersonalMemberDetailsModel(context);
        return model.getOtherPersonalMemberDetails(profileId);
    }

    public ArrayList<Object> getClubDesignation(long profileId) {
        PersonalMemberDetailsModel model = new PersonalMemberDetailsModel(context);
        return model.getClubDesignation(profileId);
    }
    public ArrayList<BusinessMemberDetails> getBusinessInfo(long profileId) {
        BusinessMemberDetailsModel model = new BusinessMemberDetailsModel(context);
        return model.getBusinessMemberDetails(profileId);
    }


    public ArrayList<Object> getOtherBusinessInfo(long profileId) {
        BusinessMemberDetailsModel model = new BusinessMemberDetailsModel(context);
        return model.getOtherBusinessMemberDetails(profileId);
    }
    public ArrayList<FamilyMemberData> getFamilyMembers(long profileId) {
        return new FamilyMemberModel(context).getFamilyMembers(profileId);
    }

    public ArrayList<AddressData> getAddresses(long profileId, String isBusinessAddrVisible, String isResidanceAddrVisible) {
        return new AddressModel(context).getAddresses(profileId, isBusinessAddrVisible, isResidanceAddrVisible);
    }
    // End of functions to get complete profile of a member
    public Hashtable<String, AddressData> getAddresses(long profileId) {

        return new AddressModel(context).getAddresses(profileId);
    }

    public long addProfileMasterData(ProfileMasterData data) {
        Utils.log("Inside addProfileMasterData() function");
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.ProfileMaster.Columns.GROUP_ID, data.getGrpId());
            values.put(Tables.ProfileMaster.Columns.PROFILE_ID, data.getProfileId());
            values.put(Tables.ProfileMaster.Columns.IS_ADMIN, data.getIsAdmin());
            values.put(Tables.ProfileMaster.Columns.MEMBER_NAME, data.getMemberName());
            values.put(Tables.ProfileMaster.Columns.MEMBER_EMAIL, data.getMemberEmail());
            values.put(Tables.ProfileMaster.Columns.MEMBER_MOBILE, data.getMemberMobile());
            values.put(Tables.ProfileMaster.Columns.MEMBER_COUNTRY, data.getMemberCountry());
            values.put(Tables.ProfileMaster.Columns.PROFILE_PIC, data.getProfilePic());
            values.put(Tables.ProfileMaster.Columns.FAMILY_PIC, data.getFamilyPic());
            values.put(Tables.ProfileMaster.Columns.IS_PERSONAL_DET_VISIBLE, data.getIsPersonalDetVisible());
            values.put(Tables.ProfileMaster.Columns.IS_BUSSINESS_DET_VISBILE, data.getIsBussinessDetVisible());
            values.put(Tables.ProfileMaster.Columns.IS_FAMILY_DET_VISIBLE, data.getIsFamilyDetVisible());
            values.put(Tables.ProfileMaster.Columns.IS_RESIDANCE_ADDR_VISIBLE, data.getIsResidanceAddrVisible());
            values.put(Tables.ProfileMaster.Columns.IS_BUSINESS_ADDR_VISIBLE, data.getIsBusinessAddrVisible());

            long id = db.insert(Tables.ProfileMaster.TABLE_NAME, null, values);
            Utils.log("Value of master profile record is : "+id);
            return id;
        } catch (Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }
        return -1;
    }

    public boolean addProfiles(ArrayList<CompleteProfile> list) {
        if (list.size() == 0 )  return true;
        try {
            db.beginTransaction();

            Iterator<CompleteProfile> iterator = list.iterator();
            Utils.log("Inside addProfiles() function");
            while(iterator.hasNext()) {
                CompleteProfile profile = iterator.next();
                boolean success = addCompleteProfile(profile);
                Utils.log("Result of adding complete profile is : "+ success);
                if ( ! success ) {
                    db.endTransaction();
                    return false;
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            return true;
        } catch (SQLiteException se) {
            db.endTransaction();
            Utils.log("Error is : " + se);
            se.printStackTrace();
        } catch(Exception e) {
            db.endTransaction();
            Utils.log("Error is : "+e);
            e.printStackTrace();
        }
        if (db.inTransaction()) {
            db.endTransaction();
        }
        return false;
    }

    public boolean addCompleteProfile(CompleteProfile profile) {

        try {
            long id = addProfileMasterData(profile.getProfileData());
            boolean success = false;

            if ( id == -1 ) {
                if ( db.inTransaction()) {
                    db.endTransaction();
                }
                Utils.log("Failed to add profile master data");
                return false;
            }
            PersonalMemberDetailsModel mModel = new PersonalMemberDetailsModel(context);
            BusinessMemberDetailsModel bModel = new BusinessMemberDetailsModel(context);
            FamilyMemberModel fModel = new FamilyMemberModel(context);
            AddressModel aModel = new AddressModel(context);
            success = mModel.addPersonalMemberDetails(profile.getPersonalDataList());
            if ( ! success ) {
                if ( db.inTransaction()){
                    db.endTransaction();
                }
                Utils.log("Failed to add personal data");
                return false;
            }

            success = bModel.addBusinessMemberDetails(profile.getBusinessDataList());
            if ( ! success) {
                if (db.inTransaction()){
                    db.endTransaction();
                }
                Utils.log("Failed to add business member details");
                return false;
            }
            success = fModel.addFamilyMembers(profile.getFamilyMemberList());
            if ( ! success ) {
                if ( db.inTransaction()) {
                    db.endTransaction();
                }

                Utils.log("Failed to add family members");
                return false;
            }
            success = aModel.addAddressDetails(profile.getAddressData());

            if ( ! success ) {
                if ( db.inTransaction()) {
                    db.endTransaction();
                }

                Utils.log("Failed to add address");
                return false;
            }
            return true;
        } catch(SQLiteException se) {
            Utils.log("Error is : "+se);
            se.printStackTrace();
        }
        return false;
    }

    public boolean updateProfile(ProfileMasterData data) {
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.ProfileMaster.Columns.GROUP_ID, data.getGrpId());

            values.put(Tables.ProfileMaster.Columns.IS_ADMIN, data.getIsAdmin());
            values.put(Tables.ProfileMaster.Columns.MEMBER_NAME, data.getMemberName());
            values.put(Tables.ProfileMaster.Columns.MEMBER_EMAIL, data.getMemberEmail());
            values.put(Tables.ProfileMaster.Columns.MEMBER_MOBILE, data.getMemberMobile());
            values.put(Tables.ProfileMaster.Columns.MEMBER_COUNTRY, data.getMemberCountry());
            values.put(Tables.ProfileMaster.Columns.PROFILE_PIC, data.getProfilePic());
            values.put(Tables.ProfileMaster.Columns.FAMILY_PIC, data.getFamilyPic());
            values.put(Tables.ProfileMaster.Columns.IS_PERSONAL_DET_VISIBLE, data.getIsPersonalDetVisible());
            values.put(Tables.ProfileMaster.Columns.IS_BUSSINESS_DET_VISBILE, data.getIsBussinessDetVisible());
            values.put(Tables.ProfileMaster.Columns.IS_FAMILY_DET_VISIBLE, data.getIsFamilyDetVisible());
            values.put(Tables.ProfileMaster.Columns.IS_RESIDANCE_ADDR_VISIBLE, data.getIsResidanceAddrVisible());
            values.put(Tables.ProfileMaster.Columns.IS_BUSINESS_ADDR_VISIBLE, data.getIsBusinessAddrVisible());

            String whereClaus = Tables.ProfileMaster.Columns.PROFILE_ID+"="+data.getProfileId();
            Cursor cursor = db.query(Tables.ProfileMaster.TABLE_NAME, null, whereClaus, null, null, null, null);
            if ( cursor.getCount() == 0 ) {
                long id = addProfileMasterData(data);
                if ( id != -1 ) {
                    return true;
                } else {
                    return false;
                }
            }
            int n = db.update(Tables.ProfileMaster.TABLE_NAME, values, whereClaus, null);
            if (n==1) {
                return true;
            } else {
                if ( db.inTransaction() ) {
                    db.endTransaction();
                }
                return false;
            }
        } catch (Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateCompleteProfile(CompleteProfile profile) {
        try {
            boolean success = updateProfile(profile.getProfileData());

            PersonalMemberDetailsModel mModel = new PersonalMemberDetailsModel(context);
            BusinessMemberDetailsModel bModel = new BusinessMemberDetailsModel(context);
            FamilyMemberModel fModel = new FamilyMemberModel(context);
            AddressModel aModel = new AddressModel(context);
            success = mModel.updatePersonalMemberDetails(profile.getPersonalDataList());
            if ( ! success ) {
                return false;
            }

            success = bModel.updateBusinessMemberDetails(profile.getBusinessDataList());
            if ( ! success) {
                return false;
            }
            success = fModel.deleteToUpdate(Long.parseLong(profile.getProfileData().getProfileId()), profile.getFamilyMemberList());

            if ( !success ) return false;

            success = aModel.updateAddresses(profile.getAddressData());

            if ( ! success ) return false;

            return true;
        } catch(SQLiteException se) {
            Utils.log("Error is : "+se);
            se.printStackTrace();
        }
        return false;
    }

    public boolean updateProfiles(ArrayList<CompleteProfile> list) {
        if (list.size() == 0 ) return true;
        try {
            db.beginTransaction();
            Iterator<CompleteProfile> iterator = list.iterator();
            while(iterator.hasNext()) {

                boolean success = updateCompleteProfile(iterator.next());
                Utils.log("Inside update complete profile loop : Value of success is : " + success);

                if ( ! success ) {
                    return false;
                }
            }
            if ( db.inTransaction() ) {
                db.setTransactionSuccessful();
                db.endTransaction();
                Utils.log("Transaction committed successfully");
                return true;
            } else {
                Utils.log("Oops...!!! Transaction is not committed. Whats wrong?");
            }
            return true;
        } catch(SQLiteException se) {
            Utils.log("Error is : "+ se);
            se.printStackTrace();
        } catch(Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }
        if ( db.inTransaction() ) {
            db.endTransaction();
        }
        return false;
    }

    public boolean deleteProfile(long profileId) {
        try {
            int n = db.delete(Tables.ProfileMaster.TABLE_NAME, Tables.ProfileMaster.Columns.PROFILE_ID+"="+profileId, null);
            if (n == 1 )  return true;
            else {
                if ( db.inTransaction()) {
                    db.endTransaction();
                }

                return false;
            }

        } catch(Exception e){
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteProfiles(String profileIds) {
        if ( profileIds.trim().equals("") ) return true;
        try {
            profileIds = "("+profileIds+")";
            db.execSQL("delete from "+Tables.ProfileMaster.TABLE_NAME+" where "+Tables.ProfileMaster.Columns.PROFILE_ID+" in "+profileIds);
            db.execSQL("delete from "+Tables.PersonalMemberDetails.TABLE_NAME+" where "+Tables.PersonalMemberDetails.Columns.PROFILE_ID+" in "+profileIds);
            db.execSQL("delete from "+Tables.BusinessMemberDetails.TABLE_NAME+" where "+Tables.BusinessMemberDetails.Columns.PROFILE_ID+" in "+profileIds);
            db.execSQL("delete from "+Tables.FamilyMemberDetail.TABLE_NAME+" where "+Tables.FamilyMemberDetail.Columns.PROFILE_ID+" in "+profileIds);
            db.execSQL("delete from "+Tables.AddressDetails.TABLE_NAME+" where "+Tables.AddressDetails.Columns.PROFILE_ID+" in "+profileIds);
            return true;
        } catch(Exception e){
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteAddress(String profileIds,String addressType) {
        if ( profileIds.trim().equals("") ) return true;
        try {
            profileIds = "("+profileIds+")";
//            db.execSQL("delete from "+Tables.ProfileMaster.TABLE_NAME+" where "+Tables.ProfileMaster.Columns.PROFILE_ID+" in "+profileIds);
//            db.execSQL("delete from "+Tables.PersonalMemberDetails.TABLE_NAME+" where "+Tables.PersonalMemberDetails.Columns.PROFILE_ID+" in "+profileIds);
//            db.execSQL("delete from "+Tables.BusinessMemberDetails.TABLE_NAME+" where "+Tables.BusinessMemberDetails.Columns.PROFILE_ID+" in "+profileIds);
//            db.execSQL("delete from "+Tables.FamilyMemberDetail.TABLE_NAME+" where "+Tables.FamilyMemberDetail.Columns.PROFILE_ID+" in "+profileIds);
            Utils.log("delete from "+Tables.AddressDetails.TABLE_NAME+" where "+Tables.AddressDetails.Columns.PROFILE_ID+" in "+profileIds+" and "+Tables.AddressDetails.Columns.ADDRESS_TYPE+" = '"+addressType+"'");
            db.execSQL("delete from "+Tables.AddressDetails.TABLE_NAME+" where "+Tables.AddressDetails.Columns.PROFILE_ID+" in "+profileIds+" and "+Tables.AddressDetails.Columns.ADDRESS_TYPE+" = '"+addressType+"'");
           // db.delete(Tables.AddressDetails.TABLE_NAME,Tables.AddressDetails.Columns.PROFILE_ID+" in ? and "+Tables.AddressDetails.Columns.ADDRESS_TYPE+" = ?",new String[]{profileIds,addressType});
            return true;
        } catch(Exception e){
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }

        return false;
    }

    public boolean isDataAvailable(long grpId) {
        try {
            Cursor cursor = db.rawQuery("select * from "+Tables.ProfileMaster.TABLE_NAME+" where "+ Tables.ProfileMaster.Columns.GROUP_ID+"="+grpId, null);
            if (cursor.getCount() == 0 ) return false;
            return true;
        } catch(Exception e) {
            Utils.log("Error is : "+ e);
            e.printStackTrace();

        }
        return false;
    }

    public boolean updateProfiles(ArrayList<CompleteProfile> newRecords, ArrayList<CompleteProfile> updatedRecords, String deleteRecords) {
        try {
            boolean success = addProfiles(newRecords);
            if ( ! success ) {
                return false;
            }

            success = updateProfiles(updatedRecords);
            if ( ! success ) {
                return false;
            }
            success = deleteProfiles(deleteRecords);
            if ( ! success ) {
                return false;
            }
            return success;
        } catch(Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }
        return false;
    }


//    public ArrayList<ChatUser> getChatUsers(long groupId) {
//        ArrayList<ChatUser> list = new ArrayList<ChatUser>();
//        try {
//            //Cursor cursor = db.rawQuery("select * from "+ Tables.DirectoryDataMaster.TABLE_NAME+" where "+Tables.DirectoryDataMaster.Columns.GROUP_ID+"="+groupId, null);
//            Cursor cursor = db.rawQuery("select * from "+ Tables.ProfileMaster.TABLE_NAME+" where "+Tables.ProfileMaster.Columns.GROUP_ID+"="+groupId, null);
//            while (cursor.moveToNext()) {
//                long chatUserId = cursor.getLong(cursor.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_ID));
//                String memberName = cursor.getString(cursor.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_NAME));
//                String tempUserName = cursor.getString(cursor.getColumnIndex(Tables.ProfileMaster.Columns.MASTER_ID));
//                long masterUid = cursor.getLong(cursor.getColumnIndex(Tables.ProfileMaster.Columns.MASTER_ID));
//                Log.e("masterUid", ""+masterUid);
//                //if (tempUserName.contains(" ") ) tempUserName = tempUserName.substring(tempUserName.indexOf(" ")+1);
//                String userName = Constant.CHAT_USER_PREFIX + masterUid; //cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.GROUP_ID)) +"_"+tempUserName;
//                //String pic = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.GROUP_ID)) +"_"+cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTOTY_PIC));
//                String pic = cursor.getString(cursor.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_PIC));
//                ChatUser user = new ChatUser(chatUserId, memberName, userName, pic);
//                list.add(user);
//            }
//            return list;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    public ArrayList<ChatUser> getChatUsers(long groupId, String searchText) {
//        ArrayList<ChatUser> list = new ArrayList<ChatUser>();
//        try {
//            Cursor cursor = db.rawQuery("select * from "+ Tables.ProfileMaster.TABLE_NAME+" where "+Tables.DirectoryDataMaster.Columns.GROUP_ID+"="+groupId+" and (memberName like '%"+searchText+"%' or membermobile like '%"+searchText+"%')", null);
//            while (cursor.moveToNext()) {
//                long chatUserId = cursor.getLong(cursor.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_ID));
//                String memberName = cursor.getString(cursor.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_NAME));
//                String tempUserName = cursor.getString(cursor.getColumnIndex(Tables.ProfileMaster.Columns.MASTER_ID));
//                long masterUid = cursor.getLong(cursor.getColumnIndex(Tables.ProfileMaster.Columns.MASTER_ID));
//                Log.e("masterUid", ""+masterUid);
//                //if (tempUserName.contains(" ") ) tempUserName = tempUserName.substring(tempUserName.indexOf(" ")+1);
//                String userName = Constant.CHAT_USER_PREFIX + masterUid; //cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.GROUP_ID)) +"_"+tempUserName;
//                //String pic = cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.GROUP_ID)) +"_"+cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTOTY_PIC));
//                String pic = cursor.getString(cursor.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_PIC));
//                ChatUser user = new ChatUser(chatUserId, memberName, userName, pic);
//                list.add(user);
//            }
//            return list;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }

    public DirectoryData getChatUserDetails(long memberMasterUserId, long groupId) {
        try {
            String query = "select * from  "+ Tables.ProfileMaster.TABLE_NAME+" where memberMasterUID="+memberMasterUserId+ " and grpId="+groupId;
            Log.e("ChatUserQuery", query);
            Cursor cursor = db.rawQuery(query, null);
            if ( cursor.moveToNext()) {
                String directoryProfileId = cursor.getString(cursor.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_ID));
                String directoryGroupName= cursor.getString(cursor.getColumnIndex(Tables.ProfileMaster.Columns.GROUP_ID));
                String directoryMemberName= cursor.getString(cursor.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_NAME));
                String directoryPic = cursor.getString(cursor.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_PIC)).replaceAll(" ", "%20");;
                String directoryMemberMobile= cursor.getString(cursor.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_MOBILE));
                String directoryGroupCount= "0";//cursor.getString(cursor.getColumnIndex(Tables.DirectoryDataMaster.Columns.DIRECTORY_GROUP_COUNT));
                String memberMasterUID = cursor.getString(cursor.getColumnIndex(Tables.ProfileMaster.Columns.MASTER_ID));
                boolean box = false;
                DirectoryData dd = new DirectoryData(""+memberMasterUID,""+groupId, directoryProfileId, directoryGroupName, directoryMemberName, directoryPic, directoryMemberMobile,directoryGroupCount, box,false);
                return dd;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Advanced search
    public ArrayList<ProfileMasterData> dynamicSearch(long groupId, Hashtable<String, String[]> searchData) {
        ArrayList<ProfileMasterData> list = new ArrayList<>();
        try {
            Cursor cur = db.rawQuery("select * from "+Tables.ProfileMaster.TABLE_NAME+" where "+Tables.ProfileMaster.Columns.GROUP_ID+"="+groupId+" order by LOWER("+Tables.ProfileMaster.Columns.MEMBER_NAME+") COLLATE NOCASE ASC", null);
            while(cur.moveToNext()) {
                String masterId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MASTER_ID));
                String grpId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.GROUP_ID));
                String profileId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_ID));
                String isAdmin = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_ADMIN));
                String memberName = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_NAME));
                String memberEmail = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_EMAIL));
                String memberMobile = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_MOBILE));
                String memberCountry = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_COUNTRY));
                String profilePic = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_PIC)).replaceAll(" ", "%20");;
                String familyPic = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.FAMILY_PIC));
                String isPersonalDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_PERSONAL_DET_VISIBLE));
                String isBussinessDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_BUSSINESS_DET_VISBILE));
                String isFamilyDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_FAMILY_DET_VISIBLE));

                String isResidanceAddrVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_RESIDANCE_ADDR_VISIBLE));
                String isBusinessAddrVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_BUSINESS_ADDR_VISIBLE));

                ProfileMasterData data = new ProfileMasterData(masterId, grpId, ""+profileId, isAdmin, memberName, memberEmail, memberMobile, memberCountry, profilePic,familyPic, isPersonalDetVisible, isBussinessDetVisible, isFamilyDetVisible, isResidanceAddrVisible, isBusinessAddrVisible);

                Enumeration<String> searchKeys = searchData.keys();
                boolean found = true;
                final int SEARCH_KEY_INDEX = 0, COLUMN_NAME_INDEX = 1, TABLE_NAME_INDEX = 2;
                int count = 0;
                while(searchKeys.hasMoreElements()) {
                    String searchKey = searchKeys.nextElement();
                    String[] fields = searchData.get(searchKey);
                    String searchKeyword = fields[SEARCH_KEY_INDEX];
                    searchKeyword = "%"+searchKeyword.replaceAll(" ", "%")+"%";
                    String columnName = fields[COLUMN_NAME_INDEX];
                    String tableName = fields[TABLE_NAME_INDEX];

                    if (tableName.equals(Tables.PersonalMemberDetails.TABLE_NAME)) {
                        String query = "select * from "+Tables.PersonalMemberDetails.TABLE_NAME+
                                " where "+Tables.PersonalMemberDetails.Columns.UNIQUE_KEY+"='"+columnName+"' and "+
                                Tables.PersonalMemberDetails.Columns.VALUE+" like '"+searchKeyword+"' and "+
                                Tables.PersonalMemberDetails.Columns.PROFILE_ID+"="+profileId;
                        try {
                            Cursor cursor = db.rawQuery(query, null);
                            if ( cursor.moveToNext()) {
                              count++;
                            } else {
                                found = false;
                                break;
                            }
                        } catch(SQLiteException sqe) {
                            Utils.log("Error is : "+sqe);
                            sqe.printStackTrace();
                            count++;
                        }
                    } else if (tableName.equals(Tables.BusinessMemberDetails.TABLE_NAME)) {
                        String query = "select * from "+Tables.BusinessMemberDetails.TABLE_NAME+
                                " where "+Tables.BusinessMemberDetails.Columns.UNIQUE_KEY+"='"+columnName+"' and "+
                                Tables.BusinessMemberDetails.Columns.VALUE+" like '"+searchKeyword+"' and "+
                                Tables.BusinessMemberDetails.Columns.PROFILE_ID+"="+profileId;

                        try {
                            Cursor cursor = db.rawQuery(query, null);
                            if ( cursor.moveToNext()) {
                                count++;
                            } else {
                                found = false;
                                break;
                            }
                        } catch(SQLiteException sqe) {
                            Utils.log("Error is : "+sqe);
                            sqe.printStackTrace();
                            count++;
                        }

                    } else {

                        String query = "select * from "+tableName+" where "+columnName+" like '"+searchKeyword+"' and profileId="+profileId;
                        try {
                            Cursor cursor = db.rawQuery(query, null);
                            if ( cursor.moveToNext()) {
                                count++;
                            } else {
                                found = false;
                                break;
                            }
                        } catch(SQLiteException sqe) {
                            Utils.log("Error is : "+sqe);
                            sqe.printStackTrace();
                            count++;
                        }
                    }
                }

                if ( found ) {
                    list.add(data);
                }
            }
        } catch(Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }

        return list;
    }
    public Hashtable<String, DynamicFieldData> getStaticFieldValues(long profileId) {
        Hashtable<String, DynamicFieldData> table = new Hashtable<>();
        try {
            // fieldId = 0 means static field. Not a dynamic field
            String query = "select * from " + Tables.PersonalMemberDetails.TABLE_NAME +
                    " where " + Tables.PersonalMemberDetails.Columns.PROFILE_ID + "=" + profileId + " and fieldID=0" +
                    " UNION " +
                    " select * from " + Tables.BusinessMemberDetails.TABLE_NAME +
                    " where " + Tables.BusinessMemberDetails.Columns.PROFILE_ID + "=" + profileId + " and fieldID=0";

            Cursor cursor = db.rawQuery(query, null);
            while(cursor.moveToNext()) {
                String fieldID = cursor.getString(cursor.getColumnIndex(Tables.PersonalMemberDetails.Columns.FIELD_ID));
                String uniquekey = cursor.getString(cursor.getColumnIndex(Tables.PersonalMemberDetails.Columns.UNIQUE_KEY));
                String key = cursor.getString(cursor.getColumnIndex(Tables.PersonalMemberDetails.Columns.KEY));
                String value = cursor.getString(cursor.getColumnIndex(Tables.PersonalMemberDetails.Columns.VALUE));
                String colType = cursor.getString(cursor.getColumnIndex(Tables.PersonalMemberDetails.Columns.COL_TYPE));
                String isEditable = cursor.getString(cursor.getColumnIndex(Tables.PersonalMemberDetails.Columns.IS_EDITABLE));
                String isVisible = cursor.getString(cursor.getColumnIndex(Tables.PersonalMemberDetails.Columns.IS_VISIBLE));
                DynamicFieldData dfData = new DynamicFieldData(""+profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible);
                table.put(uniquekey, dfData);
                Utils.log("Uniquekey : "+uniquekey+" ---> Data : "+dfData);
            }
        } catch(Exception e) {
            Utils.log("Error is : "+e);
            e.printStackTrace();
        }
        return table;
    }

    /*public ArrayList<DynamicFieldData> getDynamicFieldValues(long profileId) {
        ArrayList<DynamicFieldData> list = new ArrayList<>();
        try {
            String query = "select * from " + Tables.PersonalMemberDetails.TABLE_NAME +
                   " where " + Tables.PersonalMemberDetails.Columns.PROFILE_ID + "=" + profileId + " and fieldID!=0" +
                    " UNION " +
                    " select * from " + Tables.BusinessMemberDetails.TABLE_NAME +
                    " where " + Tables.PersonalMemberDetails.Columns.PROFILE_ID + "=" + profileId + " and fieldID!=0";

            Cursor cursor = db.rawQuery(query, null);
            while(cursor.moveToNext()) {
                String fieldID = cursor.getString(cursor.getColumnIndex(Tables.PersonalMemberDetails.Columns.FIELD_ID));
                String uniquekey = cursor.getString(cursor.getColumnIndex(Tables.PersonalMemberDetails.Columns.UNIQUE_KEY));
                String key = cursor.getString(cursor.getColumnIndex(Tables.PersonalMemberDetails.Columns.KEY));
                String value = cursor.getString(cursor.getColumnIndex(Tables.PersonalMemberDetails.Columns.VALUE));
                String colType = cursor.getString(cursor.getColumnIndex(Tables.PersonalMemberDetails.Columns.COL_TYPE));
                String isEditable = cursor.getString(cursor.getColumnIndex(Tables.PersonalMemberDetails.Columns.IS_EDITABLE));
                String isVisible = cursor.getString(cursor.getColumnIndex(Tables.PersonalMemberDetails.Columns.IS_VISIBLE));
                DynamicFieldData dfData = new DynamicFieldData(""+profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible);
                list.add(dfData);
            }
        } catch(Exception e) {
            Utils.log("Error is : "+e);
            e.printStackTrace();
        }
        return list;
    }*/

    public ArrayList<DynamicFieldData> getDynamicFieldValues(long profileId) {
        ArrayList<DynamicFieldData> list = new ArrayList<>();
        try {
            String query = "select * from " + Tables.PersonalMemberDetails.TABLE_NAME +
                    " where " + Tables.PersonalMemberDetails.Columns.PROFILE_ID + "=" + profileId + " and fieldID!=0" +
                    " UNION " +
                    " select * from " + Tables.BusinessMemberDetails.TABLE_NAME +
                    " where " + Tables.PersonalMemberDetails.Columns.PROFILE_ID + "=" + profileId + " and fieldID!=0";

            Cursor cursor = db.rawQuery(query, null);
            while(cursor.moveToNext()) {
                String fieldID = cursor.getString(cursor.getColumnIndex(Tables.PersonalMemberDetails.Columns.FIELD_ID));
                String uniquekey = cursor.getString(cursor.getColumnIndex(Tables.PersonalMemberDetails.Columns.UNIQUE_KEY));
                String key = cursor.getString(cursor.getColumnIndex(Tables.PersonalMemberDetails.Columns.KEY));
                String value = cursor.getString(cursor.getColumnIndex(Tables.PersonalMemberDetails.Columns.VALUE));
                String colType = cursor.getString(cursor.getColumnIndex(Tables.PersonalMemberDetails.Columns.COL_TYPE));
                String isEditable = cursor.getString(cursor.getColumnIndex(Tables.PersonalMemberDetails.Columns.IS_EDITABLE));
                String isVisible = cursor.getString(cursor.getColumnIndex(Tables.PersonalMemberDetails.Columns.IS_VISIBLE));
                DynamicFieldData dfData = new DynamicFieldData(""+profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible);
                list.add(dfData);
            }
        } catch(Exception e) {
            Utils.log("Error is : "+e);
            e.printStackTrace();
        }
        return list;
    }
    public ProfileMasterData getMasterProfile(String profileId) {
        Cursor cur = db.rawQuery("select * from "+Tables.ProfileMaster.TABLE_NAME+" where "+Tables.ProfileMaster.Columns.PROFILE_ID+"="+profileId, null);
        if (cur.moveToNext()) {
            String masterId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MASTER_ID));
            String grpId = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.GROUP_ID));

            String isAdmin = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_ADMIN));
            String memberName = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_NAME));
            String memberEmail = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_EMAIL));
            String memberMobile = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_MOBILE));
            String memberCountry = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.MEMBER_COUNTRY));
            String profilePic = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.PROFILE_PIC)).replaceAll(" ", "%20");;
            String familyPic = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.FAMILY_PIC));
            String isPersonalDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_PERSONAL_DET_VISIBLE));
            String isBussinessDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_BUSSINESS_DET_VISBILE));
            String isFamilyDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_FAMILY_DET_VISIBLE));
            //String isFamilyDetVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_FAMILY_DET_VISIBLE));

            String isResidanceAddrVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_RESIDANCE_ADDR_VISIBLE));
            String isBusinessAddrVisible = cur.getString(cur.getColumnIndex(Tables.ProfileMaster.Columns.IS_BUSINESS_ADDR_VISIBLE));

            ProfileMasterData data = new ProfileMasterData(masterId, grpId, "" + profileId, isAdmin, memberName, memberEmail, memberMobile, memberCountry, profilePic,familyPic, isPersonalDetVisible, isBussinessDetVisible, isFamilyDetVisible, isResidanceAddrVisible, isBusinessAddrVisible);
            return data;
        }
        return null;
    }

    public ArrayList<ClassificationData> getClassifications(long groupId) {
        ArrayList<ClassificationData> list = new ArrayList<>();
        String query = "select distinct value from " +
                " ProfileMaster, PersonalMemberDetails " +
                " where ProfileMaster.grpId="+groupId +
                " and ProfileMaster.profileId=PersonalMemberDetails.profileId " +
                " and PersonalMemberDetails.uniquekey='designation' order by value";

        try {
            Cursor cursor = db.rawQuery(query, null);
            while(cursor.moveToNext()) {
                String classificationName = cursor.getString(0);
                if ( ! classificationName.trim().equals("")) {
                    Utils.log("Value : " + classificationName);
                    ClassificationData data = new ClassificationData(classificationName);
                    list.add(data);
                }
            }

            return list;

        } catch(SQLiteException se) {
            Utils.log("SQL error : "+se);
            se.printStackTrace();
        } catch (Exception e) {
            Utils.log("Error is : "+e);
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<ClassificationData> searchClassification(long groupId, String keyword) {
        ArrayList<ClassificationData> list = new ArrayList<>();
        keyword = "%"+keyword.replace(" ", "%")+"%";
        String query = "select distinct value from " +
                " ProfileMaster, PersonalMemberDetails " +
                " where ProfileMaster.grpId="+groupId +
                " and ProfileMaster.profileId=PersonalMemberDetails.profileId " +
                " and PersonalMemberDetails.uniquekey='designation' " +
                " and value like '"+keyword+"' order by value";

        try {
            Cursor cursor = db.rawQuery(query, null);
            while(cursor.moveToNext()) {
                String classificationName = cursor.getString(0);
                if ( ! classificationName.trim().equals("")) {
                    Utils.log("Value : " + classificationName);
                    ClassificationData data = new ClassificationData(classificationName);
                    list.add(data);
                }
            }

            return list;

        } catch(SQLiteException se) {
            Utils.log("SQL error : "+se);
            se.printStackTrace();
        } catch (Exception e) {
            Utils.log("Error is : "+e);
            e.printStackTrace();
        }

        return list;
    }
}
