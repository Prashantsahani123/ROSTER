package com.NEWROW.row.Data;

/**
 * Created by USER1 on 07-06-2016.
 */
public class GroupInfoData {

    String title,description;

    String contactNo;
    String address; String email;


    public void setEmail(String email) {
        this.email = email;
    }

    public String setContactNo(String contactNo) {
        this.contactNo = contactNo;
        return contactNo;
    }

    public String setAddress(String address) {
        this.address = address;
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getAddress() {
        return address;
    }

    public GroupInfoData() {
    }

    public GroupInfoData(String title, String description,String contactNo,String address,String email) {
        this.title = title;
        this.description = description;
        this.contactNo = contactNo;
        this.address = address;
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String setTitle(String title) {
        this.title = title;
        return title;
    }

    public String setDescription(String description) {
        this.description = description;
        return description;
    }

    @Override
    public String toString() {
        return "GroupInfoData{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

