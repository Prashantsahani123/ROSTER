package com.SampleApp.row.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by USER1 on 17-09-2016.
 */
public class AlbumPhotoData implements Parcelable {

    String photoId,url,description;
    String grpId,albumId;
    String photoTitle = "Title";

    public AlbumPhotoData() {
    }

    public AlbumPhotoData(String photoId, String url, String description, String grpId, String albumId, String photoTitle) {
        this.photoId = photoId;
        this.url = url;
        this.description = description;
        this.grpId = grpId;
        this.albumId = albumId;
        this.photoTitle = photoTitle;
    }

    public AlbumPhotoData(String grpId, String albumId, String description, String photoId, String url) {
        this.photoId = photoId;
        this.url = url;
        this.description = description;
        this.grpId = grpId;
        this.albumId = albumId;

    }

    public String getGrpId() {
        return grpId;
    }

    public void setGrpId(String grpId) {
        this.grpId = grpId;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return "AlbumPhotoData{" +
                "photoId='" + photoId + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", grpId='" + grpId + '\'' +
                ", albumId='" + albumId + '\'' +
                '}';
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeString(photoId);
        dest.writeString(url);
        dest.writeString(description);
        dest.writeString(grpId);
        dest.writeString(albumId);
        dest.writeString(photoTitle);

    }

    public AlbumPhotoData(Parcel in) {
        photoId = in.readString();
        url = in.readString();
        description = in.readString();
        grpId = in.readString();
        albumId = in.readString();
        photoTitle = in.readString();;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<AlbumPhotoData> CREATOR = new Parcelable.Creator<AlbumPhotoData>() {
        @Override
        public AlbumPhotoData createFromParcel(Parcel in) {
            return new AlbumPhotoData(in);
        }

        @Override
        public AlbumPhotoData[] newArray(int size) {
            return new AlbumPhotoData[size];
        }
    };
}
