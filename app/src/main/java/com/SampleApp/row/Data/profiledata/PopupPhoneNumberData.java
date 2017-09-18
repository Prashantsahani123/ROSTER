package com.SampleApp.row.Data.profiledata;

/**
 * Created by USER1 on 17-04-2017.
 */
public class PopupPhoneNumberData {
    private String number, name , extra = "";
    private boolean selected;
    public PopupPhoneNumberData(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public PopupPhoneNumberData(String number, String name, String extra) {
        this.number = number;
        this.name = name;
        this.extra = extra;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean checked) {
        this.selected = checked;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    @Override
    public String toString() {
        return "PopupPhoneNumberData{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", extra='" + extra + '\'' +
                ", selected=" + selected +
                '}';
    }
}
