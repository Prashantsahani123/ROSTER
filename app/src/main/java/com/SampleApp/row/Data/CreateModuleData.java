package com.SampleApp.row.Data;

/**
 * Created by USER on 05-02-2016.
 */
public class CreateModuleData  {

    public String name;
    public boolean box;
    public String moduleId;
    public String moduleInfo;
    public String moduleImage;
    public String newModuleName;


    public CreateModuleData() {
    }

    public CreateModuleData(String name, boolean box, String moduleId, String moduleInfo,String moduleImage,String newModuleName) {
        this.name = name;
        this.box = box;
        this.moduleId = moduleId;
        this.moduleInfo = moduleInfo;
        this.moduleImage = moduleImage;
        this.newModuleName = newModuleName;
    }

    public String getImage() {
        return moduleImage;
    }

    public void setImage(String image) {
        this.moduleImage = image;
    }


    public String getNewName() {
        return newModuleName;
    }

    public void setNewName(String newModulename) {
        this.newModuleName = newModulename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBox() {
        return box;
    }

    public void setBox(boolean box) {
        this.box = box;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleInfo() {
        return moduleInfo;
    }

    public void setModuleInfo(String moduleInfo) {
        this.moduleInfo = moduleInfo;
    }

    @Override
    public String toString() {
        return "CreateModuleData{" +
                "name='" + name + '\'' +
                ", box=" + box +
                ", moduleId='" + moduleId + '\'' +
                ", moduleInfo='" + moduleInfo + '\'' +
                ", moduleImage='" + moduleImage + '\'' +
                ", newModuleName='" + newModuleName + '\'' +
                '}';
    }
}
