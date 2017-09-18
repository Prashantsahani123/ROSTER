package com.SampleApp.row.Data.profiledata;

/**
 * Created by USER1 on 17-04-2017.
 */
public class PopupEmailData {
    private String emailId, name, extra;

    public PopupEmailData(String emailId, String name) {
        this.emailId = emailId;
        this.name = name;
    }

    public PopupEmailData(String emailId, String name, String extra) {
        this.emailId = emailId;
        this.name = name;
        this.extra = extra;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
