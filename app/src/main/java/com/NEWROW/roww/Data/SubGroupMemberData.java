package com.NEWROW.row.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by USER on 01-02-2016.
 */
public class SubGroupMemberData implements Parcelable {
    String profileID;
    String memberName;

    String membermobile;

    public boolean box;

    //boolean isVisible;
    public SubGroupMemberData() {
    }

    public SubGroupMemberData(String profileID, String memberName, String membermobile,  boolean box) {

        this.profileID = profileID;

        this.memberName = memberName;

        this.membermobile = membermobile;

        this.box = box;

        //isVisible = true;
    }

    /*@Override
    public DirectoryData clone() throws CloneNotSupportedException {
        try{
            DirectoryData dd = new DirectoryData();
            dd.masterUID = this.masterUID;
            dd.grpID =  new String(this.grpID);
            dd.profileID = new String(this.profileID);
            dd.groupName = new String(groupName);
            dd.memberName = new String(memberName);
            dd.pic = new String(pic);
            dd.membermobile = new String(membermobile);
            dd.grpCount = new String(grpCount);
            dd.box = box;
            dd.isDeleted = isDeleted;
            Log.e("Cloned", "Object is clonned");
            return dd;

        } catch(Exception  e) {
            return null;
        }


    }*/



    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }


    public String getMemberName() {
        return memberName;
    }

    public String setMemberName(String memberName) {
        this.memberName = memberName;
        return memberName;
    }



    public String getMembermobile() {
        return membermobile;
    }

    public String setMembermobile(String membermobile) {
        this.membermobile = membermobile;
        return membermobile;
    }



    public boolean isBox() {
        return box;
    }

    public void setBox(boolean box) {
        this.box = box;
    }



    @Override
    public String toString() {
        return "DirectoryData{" +
                ", profileID='" + profileID + '\'' +
                ", memberName='" + memberName + '\'' +
                ", membermobile='" + membermobile + '\'' +
                ", box=" + box +'\'' +
                '}';
    }

    protected SubGroupMemberData(Parcel in) {

        profileID = in.readString();
        memberName = in.readString();
        membermobile = in.readString();
        box = in.readByte() != 0x00;
    }

    /*//public boolean isVisible() {
        return isVisible;
    }*/

    /*public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }*/
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(profileID);
        dest.writeString(memberName);
        dest.writeString(membermobile);
        dest.writeByte((byte) (box ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Creator<SubGroupMemberData> CREATOR = new Creator<SubGroupMemberData>() {
        @Override
        public SubGroupMemberData createFromParcel(Parcel in) {
            return new SubGroupMemberData(in);
        }

        @Override
        public SubGroupMemberData[] newArray(int size) {
            return new SubGroupMemberData[size];
        }
    };
}