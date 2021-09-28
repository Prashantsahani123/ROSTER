package com.NEWROW.row.Data;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by USER1 on 19-07-2016.
 */
public class ServiceDirectoryListData implements Serializable {

    String serviceDirId;
    String groupId;
    String memberName;
    String image;
    String contactNo;
    String isdeleted;

    String description;
    String contactNo2;
    String pax;
    String email;
    String address;
    String lat;
    String lng;
    int countryId1, countryId2;
    String csv;
    String city,state,country,zip, moduleId;
    int categoryId;
    String website;
    String countryCode1;
    String countryCode2;
    String keywords;


    /*public ServiceDirectoryListData() {
    }*/


    /*public ServiceDirectoryListData(String serviceDirId, String groupId, String memberName, String image, String contactNo, String isdeleted,String description,
                                    String contactNo2,String pax,String email,String address) {
        this.serviceDirId = serviceDirId;
        this.groupId = groupId;
        this.memberName = memberName;
        this.image = image;
        this.contactNo = contactNo;
        this.isdeleted = isdeleted;

        this.description = description;
        this.contactNo2 = contactNo2;
        this.pax = pax;
        this
    }
*/

    public ServiceDirectoryListData(String serviceDirId, String groupId, String memberName, String image, String contactNo, String isdeleted, String description, String contactNo2, String pax, String email, String address,String lat,String lng, int countryId1, int countryId2, String csv,String city,String state,String country,String zip, String moduleId,int categoryId,String website,String countryCode1,String countryCode2,String keywords) {

        this.serviceDirId = serviceDirId;
        this.groupId = groupId;
        this.memberName = memberName;
        this.image = image;
        this.contactNo = contactNo;
        this.isdeleted = isdeleted;
        this.description = description;
        this.contactNo2 = contactNo2;
        this.pax = pax;
        this.email = email;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.countryId1 = countryId1;
        this.countryId2= countryId2;
        this.csv = csv;
        this.city = city;
        this.country = country;
        this.state = state;
        this.zip = zip;
        this.moduleId = moduleId;
        this.countryCode1 = countryCode1;
        this.countryCode2 = countryCode2;
        this.keywords = keywords;
        this.categoryId = categoryId;
        this.website = website;
    }
    public ServiceDirectoryListData(String serviceDirId, String groupId, String memberName, String image, String contactNo, String isdeleted, String description, String contactNo2, String pax, String email, String address,String lat,String lng, int countryId1, int countryId2, String csv,String city,String state,String country,String zip, String moduleId,int categoryId,String website) {

        this.serviceDirId = serviceDirId;
        this.groupId = groupId;
        this.memberName = memberName;
        this.image = image;
        this.contactNo = contactNo;
        this.isdeleted = isdeleted;
        this.description = description;
        this.contactNo2 = contactNo2;
        this.pax = pax;
        this.email = email;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.countryId1 = countryId1;
        this.countryId2= countryId2;
        this.csv = csv;
        this.city = city;
        this.country = country;
        this.state = state;
        this.zip = zip;
        this.moduleId = moduleId;
        this.categoryId = categoryId;
        this.website = website;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getCountryCode1() {
        return countryCode1;
    }

    public void setCountryCode1(String countryCode1) {
        this.countryCode1 = countryCode1;
    }

    public String getCountryCode2() {
        return countryCode2;
    }

    public void setCountryCode2(String countryCode2) {
        this.countryCode2 = countryCode2;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }

    public int getCountryId1() {
        return countryId1;
    }

    public void setCountryId1(int countryId1) {
        this.countryId1 = countryId1;
    }

    public int getCountryId2() {
        return countryId2;
    }

    public void setCountryId2(int countryId2) {
        this.countryId2 = countryId2;
    }

    public String getServiceDirId() {
        return serviceDirId;
    }

    public void setServiceDirId(String serviceDirId) {
        this.serviceDirId = serviceDirId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(String isdeleted) {
        this.isdeleted = isdeleted;
    }

    public String getDescription() {
        return description;
    }

    public String getContactNo2() {
        return contactNo2;
    }

    public String getPax() {
        return pax;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setContactNo2(String contactNo2) {
        this.contactNo2 = contactNo2;
    }

    public void setPax(String pax) {
        this.pax = pax;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    /*@Override
    public String toString() {
        return "ServiceDirectoryListData{" +
                "serviceDirId='" + serviceDirId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", memberName='" + memberName + '\'' +
                ", image='" + image + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", isdeleted='" + isdeleted + '\'' +
                '}';
    }*/


    @Override
    public String toString() {
        return "ServiceDirectoryListData{" +
                "serviceDirId='" + serviceDirId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", memberName='" + memberName + '\'' +
                ", image='" + image + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", isdeleted='" + isdeleted + '\'' +
                ", description='" + description + '\'' +
                ", contactNo2='" + contactNo2 + '\'' +
                ", pax='" + pax + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }

    public String getModuleId() {
        return moduleId;
    }

    public int getCategoryId() {
        Log.d("categoryId",categoryId+"");
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

}

