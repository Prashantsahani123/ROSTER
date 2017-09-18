package com.SampleApp.row.Data;

/**
 * Created by admin on 04-08-2017.
 */

public class ClubGalleryData {
    private String albumId,
            title,
            type,
            description,
            image,
            groupId,
            moduleId,
            isAdmin;

    public ClubGalleryData(String albumId, String title, String type, String description, String image, String groupId, String moduleId, String isAdmin) {
        this.albumId = albumId;
        this.title = title;
        this.type = type;
        this.description = description;
        this.image = image;
        this.groupId = groupId;
        this.moduleId = moduleId;
        this.isAdmin = isAdmin;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }
}
