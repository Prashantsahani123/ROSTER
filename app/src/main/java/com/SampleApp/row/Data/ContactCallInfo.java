package com.SampleApp.row.Data;

/**
 * Created by USER1 on 16-06-2016.
 */
public class ContactCallInfo {

    String contactName,contactMob,contactEmail,contactCategory,contactSecondMob,labelmob1,labelmob2,labelemail1,labelemail2;

    public boolean box;

    public ContactCallInfo() {
    }

    public ContactCallInfo(String contactName, String contactMob, String contactSecondMob, String contactEmail, String contactCategory ) {
        this.contactName = contactName;
        this.contactMob = contactMob;
        this.contactSecondMob = contactSecondMob;
        this.contactEmail = contactEmail;
        this.contactCategory = contactCategory;

    }

    public ContactCallInfo(String contactName, String contactMob, String contactSecondMob, String contactEmail, String contactCategory ,String labelemail1) {
        this.contactName = contactName;
        this.contactMob = contactMob;
        this.contactSecondMob = contactSecondMob;
        this.contactEmail = contactEmail;
        this.contactCategory = contactCategory;
        this.labelemail1 = labelemail1;

    }





    public String getLabelmob1() {
        return labelmob1;
    }

    public void setLabelmob1(String labelmob1) {
        this.labelmob1 = labelmob1;
    }

    public String getLabelmob2() {
        return labelmob2;
    }

    public void setLabelmob2(String labelmob2) {
        this.labelmob2 = labelmob2;
    }

    public String getLabelemail1() {
        return labelemail1;
    }

    public void setLabelemail1(String labelemail1) {
        this.labelemail1 = labelemail1;
    }

    public String getLabelemail2() {
        return labelemail2;
    }

    public void setLabelemail2(String labelemail2) {
        this.labelemail2 = labelemail2;
    }

    public boolean isBox() {
        return box;
    }

    public void setBox(boolean box) {
        this.box = box;
    }

    public String getContactSecondMob() {
        return contactSecondMob;
    }

    public void setContactSecondMob(String contactSecondMob) {
        this.contactSecondMob = contactSecondMob;
    }

    public String getContactCategory() {
        return contactCategory;
    }

    public void setContactCategory(String contactCategory) {
        this.contactCategory = contactCategory;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactMob() {
        return contactMob;
    }

    public void setContactMob(String contactMob) {
        this.contactMob = contactMob;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    @Override
    public String toString() {
        return "ContactCallInfo{" +
                "contactName='" + contactName + '\'' +
                ", contactMob='" + contactMob + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", contactCategory='" + contactCategory + '\'' +
                ", contactSecondMob='" + contactSecondMob + '\'' +
                ", box=" + box +
                '}';
    }
}
