package com.SampleApp.row.Data.profiledata;

/**
 * Created by USER1 on 21-03-2017.
 */
public class EmailData {
    String emailTitle, emailId;

    public EmailData(String emailTitle, String emailId) {
        this.emailTitle = emailTitle;
        this.emailId = emailId;
    }

    public String getEmailTitle() {
        return emailTitle;
    }

    public void setEmailTitle(String emailTitle) {
        this.emailTitle = emailTitle;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
