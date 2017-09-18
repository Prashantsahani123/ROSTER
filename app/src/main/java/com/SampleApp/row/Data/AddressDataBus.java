package com.SampleApp.row.Data;

/**
 * Created by USER on 30-09-2016.
 */
public class AddressDataBus {
    String addressID,addressType,address,city,state,country,pincode,phoneNo,fax,profileID;

    public AddressDataBus() {
    }

    public AddressDataBus(String addressID, String addressType, String address, String city, String state, String country, String pincode, String phoneNo, String fax, String profileID) {
        this.addressID = addressID;
        this.addressType = addressType;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
        this.phoneNo = phoneNo;
        this.fax = fax;
        this.profileID = profileID;
    }

    public String getAddressID() {
        return addressID;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
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

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    @Override
    public String toString() {
        return "AddressData{" +
                "addressID='" + addressID + '\'' +
                ", addressType='" + addressType + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", pincode='" + pincode + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", fax='" + fax + '\'' +
                ", profileID='" + profileID + '\'' +
                '}';
    }
}