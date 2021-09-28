package com.NEWROW.row.Data;

import java.io.Serializable;

/**
 * Created by USER1 on 16-06-2016.
 */
public class ContactData implements Serializable{

    String contactName,contactNumber,countryCode,idNumber,id;

    public boolean box;

    public boolean isBox() {
        return box;
    }

    public void setBox(boolean box) {
        this.box = box;
    }

    public ContactData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ContactData(String contactName, String contactNumber, String countryCode, String idNumber, boolean box, String id) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.countryCode = countryCode;
        this.idNumber = idNumber;
        this.box = box;
        this.id =id;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getIdNumber() {
        return idNumber;
    }


    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    @Override
    public String toString() {
        return "ContactData{" +
                "contactName='" + contactName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", countryCode='" + countryCode + '\'' +",id = '"+id+'\''+", idNumber='" + idNumber + '\'' +'}';
    }
}
