package com.NEWROW.row.Data;

import java.io.Serializable;

/**
 * Created by admin on 30-05-2017.
 */

public class FindAClubResultData implements Serializable{

    String grpID;
    String clubId;
    String clubName;
    String meetingDay;
    String meetingTime;
    String distance;
//    long ClubId;
//    String ClubName;
//    String ClubType;
//    String ClubSubType;
//    String CharterDate;
//    long NumberOfActiveMembers;
//    String ClubLanguage;
//    String District;
//    String Website;
//    String Email;
//    String Longitude;
//    String Latitude;
//    String PhoneKey;
//    String PhoneType;
//    String PhoneNumber;
//    String PhoneExtension;
//    String PhoneCountryCode;
//    String PhoneCountryName;
//    boolean IsPrimaryPhone;
//    String PhoneLastUpdated;
//    String FaxKey;
//    String FaxNumber;
//    String FaxExtension;
//    String FaxCountryCode;
//    String FaxCountryName;
//    String FaxType;
//    boolean IsPrimaryFax;
//    String FaxLastUpdated;
//    ClubLocation Location;


    public FindAClubResultData(){

    }

    public FindAClubResultData(String grpID, String clubId, String clubName, String meetingDay, String meetingTime, String distance) {
        this.grpID = grpID;
        this.clubId = clubId;
        this.clubName = clubName;
        this.meetingDay = meetingDay;
        this.meetingTime = meetingTime;
        this.distance = distance;
    }

    public String getGrpID() {
        return grpID;
    }

    public void setGrpID(String grpID) {
        this.grpID = grpID;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

//    public FindAClubResultData(long clubId, String clubName, String clubType, String clubSubType, String charterDate, long numberOfActiveMembers, String clubLanguage, String district, String website, String email, String longitude, String latitude, String phoneKey, String phoneType, String phoneNumber, String phoneExtension, String phoneCountryCode, String phoneCountryName, boolean isPrimaryPhone, String phoneLastUpdated, String faxKey, String faxNumber, String faxExtension, String faxCountryCode, String faxCountryName, String faxType, boolean isPrimaryFax, String faxLastUpdated, ClubLocation location) {
//        ClubId = clubId;
//        ClubName = clubName;
//        ClubType = clubType;
//        ClubSubType = clubSubType;
//        CharterDate = charterDate;
//        NumberOfActiveMembers = numberOfActiveMembers;
//        ClubLanguage = clubLanguage;
//        District = district;
//        Website = website;
//        Email = email;
//        Longitude = longitude;
//        Latitude = latitude;
//        PhoneKey = phoneKey;
//        PhoneType = phoneType;
//        PhoneNumber = phoneNumber;
//        PhoneExtension = phoneExtension;
//        PhoneCountryCode = phoneCountryCode;
//        PhoneCountryName = phoneCountryName;
//        IsPrimaryPhone = isPrimaryPhone;
//        PhoneLastUpdated = phoneLastUpdated;
//        FaxKey = faxKey;
//        FaxNumber = faxNumber;
//        FaxExtension = faxExtension;
//        FaxCountryCode = faxCountryCode;
//        FaxCountryName = faxCountryName;
//        FaxType = faxType;
//        IsPrimaryFax = isPrimaryFax;
//        FaxLastUpdated = faxLastUpdated;
//        this.Location = location;
//    }

//    public ClubLocation getLocation() {
//        return Location;
//    }
//
//    public void setLocation(ClubLocation location) {
//        Location = location;
//    }
//
//    public long getClubId() {
//        return ClubId;
//    }
//
//    public void setClubId(long clubId) {
//        ClubId = clubId;
//    }
//
//    public String getClubName() {
//        return ClubName;
//    }
//
//    public void setClubName(String clubName) {
//        ClubName = clubName;
//    }
//
//    public String getClubType() {
//        return ClubType;
//    }
//
//    public void setClubType(String clubType) {
//        ClubType = clubType;
//    }
//
//    public String getClubSubType() {
//        return ClubSubType;
//    }
//
//    public void setClubSubType(String clubSubType) {
//        ClubSubType = clubSubType;
//    }
//
//    public String getCharterDate() {
//        return CharterDate;
//    }
//
//    public void setCharterDate(String charterDate) {
//        CharterDate = charterDate;
//    }
//
//    public long getNumberOfActiveMembers() {
//        return NumberOfActiveMembers;
//    }
//
//    public void setNumberOfActiveMembers(long numberOfActiveMembers) {
//        NumberOfActiveMembers = numberOfActiveMembers;
//    }
//
//    public String getClubLanguage() {
//        return ClubLanguage;
//    }
//
//    public void setClubLanguage(String clubLanguage) {
//        ClubLanguage = clubLanguage;
//    }
//
//    public String getDistrict() {
//        return District;
//    }
//
//    public void setDistrict(String district) {
//        District = district;
//    }
//
//    public String getWebsite() {
//        return Website;
//    }
//
//    public void setWebsite(String website) {
//        Website = website;
//    }
//
//    public String getEmail() {
//        return Email;
//    }
//
//    public void setEmail(String email) {
//        Email = email;
//    }
//
//    public String getLongitude() {
//        return Longitude;
//    }
//
//    public void setLongitude(String longitude) {
//        Longitude = longitude;
//    }
//
//    public String getLatitude() {
//        return Latitude;
//    }
//
//    public void setLatitude(String latitude) {
//        Latitude = latitude;
//    }
//
//    public String getPhoneKey() {
//        return PhoneKey;
//    }
//
//    public void setPhoneKey(String phoneKey) {
//        PhoneKey = phoneKey;
//    }
//
//    public String getPhoneType() {
//        return PhoneType;
//    }
//
//    public void setPhoneType(String phoneType) {
//        PhoneType = phoneType;
//    }
//
//    public String getPhoneNumber() {
//        return PhoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        PhoneNumber = phoneNumber;
//    }
//
//    public String getPhoneExtension() {
//        return PhoneExtension;
//    }
//
//    public void setPhoneExtension(String phoneExtension) {
//        PhoneExtension = phoneExtension;
//    }
//
//    public String getPhoneCountryCode() {
//        return PhoneCountryCode;
//    }
//
//    public void setPhoneCountryCode(String phoneCountryCode) {
//        PhoneCountryCode = phoneCountryCode;
//    }
//
//    public String getPhoneCountryName() {
//        return PhoneCountryName;
//    }
//
//    public void setPhoneCountryName(String phoneCountryName) {
//        PhoneCountryName = phoneCountryName;
//    }
//
//    public boolean isPrimaryPhone() {
//        return IsPrimaryPhone;
//    }
//
//    public void setPrimaryPhone(boolean primaryPhone) {
//        IsPrimaryPhone = primaryPhone;
//    }
//
//    public String getPhoneLastUpdated() {
//        return PhoneLastUpdated;
//    }
//
//    public void setPhoneLastUpdated(String phoneLastUpdated) {
//        PhoneLastUpdated = phoneLastUpdated;
//    }
//
//    public String getFaxKey() {
//        return FaxKey;
//    }
//
//    public void setFaxKey(String faxKey) {
//        FaxKey = faxKey;
//    }
//
//    public String getFaxNumber() {
//        return FaxNumber;
//    }
//
//    public void setFaxNumber(String faxNumber) {
//        FaxNumber = faxNumber;
//    }
//
//    public String getFaxExtension() {
//        return FaxExtension;
//    }
//
//    public void setFaxExtension(String faxExtension) {
//        FaxExtension = faxExtension;
//    }
//
//    public String getFaxCountryCode() {
//        return FaxCountryCode;
//    }
//
//    public void setFaxCountryCode(String faxCountryCode) {
//        FaxCountryCode = faxCountryCode;
//    }
//
//    public String getFaxCountryName() {
//        return FaxCountryName;
//    }
//
//    public void setFaxCountryName(String faxCountryName) {
//        FaxCountryName = faxCountryName;
//    }
//
//    public String getFaxType() {
//        return FaxType;
//    }
//
//    public void setFaxType(String faxType) {
//        FaxType = faxType;
//    }
//
//    public boolean isPrimaryFax() {
//        return IsPrimaryFax;
//    }
//
//    public void setPrimaryFax(boolean primaryFax) {
//        IsPrimaryFax = primaryFax;
//    }
//
//    public String getFaxLastUpdated() {
//        return FaxLastUpdated;
//    }
//
//    public void setFaxLastUpdated(String faxLastUpdated) {
//        FaxLastUpdated = faxLastUpdated;
//    }
//
//    public static class ClubLocation implements Serializable{
//        String InternationalProvince;
//        String Country;
//
//        public ClubLocation(String internationalProvince, String country) {
//            InternationalProvince = internationalProvince;
//            Country = country;
//        }
//
//        public String getInternationalProvince() {
//            return InternationalProvince;
//        }
//
//        public void setInternationalProvince(String internationalProvince) {
//            InternationalProvince = internationalProvince;
//        }
//
//        public String getCountry() {
//            return Country;
//        }
//
//        public void setCountry(String country) {
//            Country = country;
//        }
//    }


    @Override
    public String toString() {
        return "FindAClubResultData{" +
                "grpID='" + grpID + '\'' +
                ", clubId='" + clubId + '\'' +
                ", clubName='" + clubName + '\'' +
                ", meetingDay='" + meetingDay + '\'' +
                ", meetingTime='" + meetingTime + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }
}
