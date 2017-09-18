package com.SampleApp.row.Data;

/**
 * Created by user on 21-01-2016.
 */
public class ModuleData {
    String groupModuleId,groupId,moduleId,moduleName,moduleStaticRef,image;
    int moduleOrderNo;

    public ModuleData() {
    }

    public ModuleData(String groupModuleId, String groupId, String moduleId, String moduleName, String moduleStaticRef, String image, int moduelOrderNo) {
        this.groupModuleId = groupModuleId;
        this.groupId = groupId;
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.moduleStaticRef = moduleStaticRef;
        this.image = image;
        this.moduleOrderNo = moduelOrderNo;
    }

    public String getGroupModuleId() {
        return groupModuleId;
    }

    public void setGroupModuleId(String groupModuleId) {
        this.groupModuleId = groupModuleId;
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

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleStaticRef() {
        return moduleStaticRef;
    }

    public void setModuleStaticRef(String moduleStaticRef) {
        this.moduleStaticRef = moduleStaticRef;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ModuleData{" +
                "groupModuleId='" + groupModuleId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", moduleId='" + moduleId + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", moduleStaticRef='" + moduleStaticRef + '\'' +
                ", image='" + image + '\'' +
                ", moduleOrderNo=" + moduleOrderNo +
                '}';
    }

    public int getModuleOrderNo() {
        return moduleOrderNo;
    }

    public void setModuleOrderNo(int moduleOrderNo) {
        this.moduleOrderNo = moduleOrderNo;
    }

    @Override
    public boolean equals(Object o) {
        if ( o instanceof ModuleData) {
            ModuleData md = (ModuleData) o;
            if ( groupId.equals(((ModuleData) o).getGroupId()) && moduleName.equals(((ModuleData) o).moduleName))
                return true;
            else return false;
        }
        return super.equals(o);
    }
}
