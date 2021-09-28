package com.NEWROW.row.Data;

import java.io.Serializable;

/**
 * Created by admin on 30-05-2017.
 */

public class ClubInfoData implements Serializable {
    String clubId;
    String districtId;
    String clubName;
    String address;
    String city;
    String state;
    String country;
    String meetingDay;
    String meetingTime;
    String clubWebsite;
    String lat;
    String longi;


//    String AddressType;
//    String AddressLine1;
//    String AddressLine2;
//    String AddressLine3;
//    String MeetingDay;
//    String MeetingTime;

    public ClubInfoData(String addressType, String addressLine1, String addressLine2, String addressLine3, String meetingDay, String meetingTime) {
//        AddressType = addressType;
//        AddressLine1 = addressLine1;
//        AddressLine2 = addressLine2;
//        AddressLine3 = addressLine3;
//        MeetingDay = meetingDay;
//        MeetingTime = meetingTime;
    }

    public ClubInfoData(String clubId, String districtId, String clubName, String address, String city, String state, String country, String meetingDay, String meetingTime, String clubWebsite, String lat, String longi) {
        this.clubId = clubId;
        this.districtId = districtId;
        this.clubName = clubName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.meetingDay = meetingDay;
        this.meetingTime = meetingTime;
        this.clubWebsite = clubWebsite;
        this.lat = lat;
        this.longi = longi;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getMeetingDay() {
        return meetingDay;
    }

    public void setMeetingDay(String meetingDay) {
        this.meetingDay = meetingDay;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getClubWebsite() {
        return clubWebsite;
    }

    public void setClubWebsite(String clubWebsite) {
        this.clubWebsite = clubWebsite;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

//    public String getAddressType() {
//        return AddressType;
//    }
//
//    public void setAddressType(String addressType) {
//        AddressType = addressType;
//    }
//
//    public String getAddressLine1() {
//        return AddressLine1;
//    }
//
//    public void setAddressLine1(String addressLine1) {
//        AddressLine1 = addressLine1;
//    }
//
//    public String getAddressLine2() {
//        return AddressLine2;
//    }
//
//    public void setAddressLine2(String addressLine2) {
//        AddressLine2 = addressLine2;
//    }
//
//    public String getAddressLine3() {
//        return AddressLine3;
//    }
//
//    public void setAddressLine3(String addressLine3) {
//        AddressLine3 = addressLine3;
//    }
//
//    public String getMeetingDay() {
//        return MeetingDay;
//    }
//
//    public void setMeetingDay(String meetingDay) {
//        MeetingDay = meetingDay;
//    }
//
//    public String getMeetingTime() {
//        return MeetingTime;
//    }
//
//    public void setMeetingTime(String meetingTime) {
//        MeetingTime = meetingTime;
//    }
}
