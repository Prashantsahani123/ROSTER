package com.NEWROW.row.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by USER on 08-01-2016.
 */
public class MemberData implements Parcelable{



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String name;
    public boolean box;


    public MemberData(String _describe, boolean _box) {
        name = _describe;
        box = _box;
    }


    protected MemberData(Parcel in) {
        name = in.readString();
        box = in.readByte() != 0;
    }

    public static final Creator<MemberData> CREATOR = new Creator<MemberData>() {
        @Override
        public MemberData createFromParcel(Parcel in) {
            return new MemberData(in);
        }

        @Override
        public MemberData[] newArray(int size) {
            return new MemberData[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeByte((byte) (box ? 1 : 0));
    }
}



