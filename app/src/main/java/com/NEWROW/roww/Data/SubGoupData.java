package com.NEWROW.row.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by USER on 10-02-2016.
 */
public class SubGoupData implements Parcelable {

    String subgroup_name;
    String no_of_members;
    String subgrpId;
    public boolean box;
    public String hasSubgroups;

    public boolean isBox() {
        return box;
    }

    public void setBox(boolean box) {
        this.box = box;
    }

    public SubGoupData() {
    }

    public SubGoupData(String subgroup_name, String no_of_members, String subgrpId, boolean box, String hasSubgroups) {
        this.subgroup_name = subgroup_name;
        this.no_of_members = no_of_members;
        this.subgrpId = subgrpId;
        this.box = box;
        this.hasSubgroups = hasSubgroups;
    }

    public String getSubgroup_name() {
        return subgroup_name;
    }

    public void setSubgroup_name(String subgroup_name) {
        this.subgroup_name = subgroup_name;
    }

    public String getNo_of_members() {
        return no_of_members;
    }

    public void setNo_of_members(String no_of_members) {
        this.no_of_members = no_of_members;
    }

    public String getSubgrpId() {
        return subgrpId;
    }

    public void setSubgrpId(String subgrpId) {
        this.subgrpId = subgrpId;
    }

    public String getHasSubgroups() {
        return hasSubgroups;
    }

    public void setHasSubgroups(String hasSubgroups) {
        this.hasSubgroups = hasSubgroups;
    }

    @Override
    public String toString() {
        return "SubGoupData{" +
                "subgroup_name='" + subgroup_name + '\'' +
                ", no_of_members='" + no_of_members + '\'' +
                ", subgrpId='" + subgrpId + '\'' +
                ", box=" + box +
                ", hasSubgroups="+hasSubgroups+
                '}';
    }

    protected SubGoupData(Parcel in) {
        subgroup_name = in.readString();
        no_of_members = in.readString();
        subgrpId = in.readString();
        box = in.readByte() != 0x00;
        hasSubgroups = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subgroup_name);
        dest.writeString(no_of_members);
        dest.writeString(subgrpId);
        dest.writeByte((byte) (box ? 0x01 : 0x00));
        dest.writeString(hasSubgroups);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SubGoupData> CREATOR = new Parcelable.Creator<SubGoupData>() {
        @Override
        public SubGoupData createFromParcel(Parcel in) {
            return new SubGoupData(in);
        }

        @Override
        public SubGoupData[] newArray(int size) {
            return new SubGoupData[size];
        }
    };
}