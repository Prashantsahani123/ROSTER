package com.NEWROW.row.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 03-11-2016.
 */
public class SimplePhotoData extends SimpleGalleryItemData implements Parcelable {
    String url;


    // Start of Parcelable stuffs

    protected SimplePhotoData(Parcel in) {
        this.url = in.readString();
        this.description = in.readString();
    }

    @SuppressWarnings("unused")
    public static final Creator<SimplePhotoData> CREATOR = new Creator<SimplePhotoData>() {
        @Override
        public SimplePhotoData createFromParcel(Parcel in) {
            return new SimplePhotoData(in);
        }

        @Override
        public SimplePhotoData[] newArray(int size) {
            return new SimplePhotoData[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.description);
    }

    public SimplePhotoData(String url, String description) {
        this.url = url;
        this.description = description;
    }



    @Override
    public int describeContents() {
        return 0;
    }


    // End of Parcelable stuffs

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    @Override
    public String toString() {
        return "SimplePhotoData{" +
                "url='" + url + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
