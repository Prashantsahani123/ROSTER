package com.SampleApp.row.Data;

/**
 * Created by user on 06-09-2016.
 */
public class AlbumData {

    String albumId;
    String title;
    String description;
    String image;
    String grpId;
    String isAdmin;
    String moduleId;

    public AlbumData(){

    }
    public AlbumData(String albumId, String title, String description, String image, String grpId, String isAdmin,String moduleId){
        this.albumId = albumId;
        this.title = title;
        this.description = description;
        this.image = image;
        this.grpId = grpId;
        this.isAdmin = isAdmin;
        this.moduleId = moduleId;
    }

    public String getAlbumId() {
        return albumId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getGrpId() {
        return grpId;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setGrpId(String grpId) {
        this.grpId = grpId;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }


}
