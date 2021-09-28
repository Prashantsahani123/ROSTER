package com.NEWROW.row.Data;

/**
 * Created by user on 09-11-2016.
 */
public class UploadPhotoData {
    String autoId;
    String photoId;
    String photoPath;
    String Description;
    String albumId;
    String groupd;
    String createdBy;
    String isUploaded;

    public UploadPhotoData(){

    }

    public UploadPhotoData(String photoId, String photoPath, String Description, String albumId, String groupd, String createdBy, String isUploaded, String autoId){
        this.photoId = photoId;
        this.photoPath = photoPath;
        this.Description = Description;
        this.albumId = albumId;
        this.groupd = groupd;
        this.createdBy = createdBy;
        this.isUploaded = isUploaded;
        this.autoId = autoId;
    }

    public UploadPhotoData(String photoId, String photoPath, String Description, String albumId, String groupd, String createdBy, String isUploaded){
        this.photoId = photoId;
        this.photoPath = photoPath;
        this.Description = Description;
        this.albumId = albumId;
        this.groupd = groupd;
        this.createdBy = createdBy;
        this.isUploaded = isUploaded;
    }


    @Override
    public String toString() {
        return "photoId:"+photoId+" photoPath:"+photoPath+" desc:"+Description+" albumId:"+albumId+" groupd:"+groupd+" createdBy:"+createdBy;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getGroupd() {
        return groupd;
    }

    public void setGroupd(String groupd) {
        this.groupd = groupd;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
    }

    public String getAutoId() {
        return autoId;
    }

    public void setAutoId(String autoId) {
        this.autoId = autoId;
    }


}
