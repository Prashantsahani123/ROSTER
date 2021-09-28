package com.NEWROW.row.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by USER on 01-02-2016.
 */
public class DirectoryData implements Parcelable,Serializable {
    String masterUID;
    String grpID;
    String profileID;
    String groupName;
    String memberName;
    String pic;
    String membermobile;
    String grpCount;
    public boolean box;
    boolean isDeleted,isEdited=false;
    String designation;
    String type="";
    //boolean isVisible;
    public DirectoryData() {
    }

    public DirectoryData(String masterUID, String grpID, String profileID, String groupName, String memberName, String pic, String membermobile, String grpCount, boolean box,boolean isDeleted) {
        this.masterUID = masterUID;
        this.grpID = grpID;
        this.profileID = profileID;
        this.groupName = groupName;
        this.memberName = memberName;
        this.pic = pic;
        this.membermobile = membermobile;
        this.grpCount = grpCount;
        this.box = box;
        this.isDeleted = isDeleted;
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

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMasterUID() {
        return masterUID;
    }

    public void setMasterUID(String masterUID) {
        this.masterUID = masterUID;
    }

    public String getGrpID() {
        return grpID;
    }

    public void setGrpID(String grpID) {
        this.grpID = grpID;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getGroupName() {
        return groupName;
    }

    public String setGroupName(String groupName) {
        this.groupName = groupName;
        return groupName;
    }

    public String getMemberName() {
        return memberName;
    }

    public String setMemberName(String memberName) {
        this.memberName = memberName;
        return memberName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getMembermobile() {
        return membermobile;
    }

    public String setMembermobile(String membermobile) {
        this.membermobile = membermobile;
        return membermobile;
    }

    public String getGrpCount() {
        return grpCount;
    }

    public void setGrpCount(String grpCount) {
        this.grpCount = grpCount;
    }

    public boolean isBox() {
        return box;
    }

    public void setBox(boolean box) {
        this.box = box;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "DirectoryData{" +
                "masterUID='" + masterUID + '\'' +
                ", grpID='" + grpID + '\'' +
                ", profileID='" + profileID + '\'' +
                ", groupName='" + groupName + '\'' +
                ", memberName='" + memberName + '\'' +
                ", pic='" + pic + '\'' +
                ", membermobile='" + membermobile + '\'' +
                ", grpCount='" + grpCount + '\'' +
                ", box=" + box +'\'' +
                ", isDeleted=" + isDeleted +'\'' +
                ", isEdited=" + isEdited +
                '}';
    }

    protected DirectoryData(Parcel in) {
        masterUID = in.readString();
        grpID = in.readString();
        profileID = in.readString();
        groupName = in.readString();
        memberName = in.readString();
        pic = in.readString();
        membermobile = in.readString();
        grpCount = in.readString();
        box = in.readByte() != 0x00;
        isEdited= in.readByte() != 0x00;
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
        dest.writeString(masterUID);
        dest.writeString(grpID);
        dest.writeString(profileID);
        dest.writeString(groupName);
        dest.writeString(memberName);
        dest.writeString(pic);
        dest.writeString(membermobile);
        dest.writeString(grpCount);
        dest.writeByte((byte) (box ? 0x01 : 0x00));
        dest.writeByte((byte) (isEdited ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Creator<DirectoryData> CREATOR = new Creator<DirectoryData>() {
        @Override
        public DirectoryData createFromParcel(Parcel in) {
            return new DirectoryData(in);
        }

        @Override
        public DirectoryData[] newArray(int size) {
            return new DirectoryData[size];
        }
    };


}