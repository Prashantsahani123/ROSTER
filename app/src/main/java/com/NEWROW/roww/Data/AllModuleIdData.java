package com.NEWROW.row.Data;

/**
 * Created by user on 05-02-2016.
 */
public class AllModuleIdData {

    String moduleName;
    String moduleID;

    public AllModuleIdData(String moduleName, String moduleID) {
        this.moduleName = moduleName;
        this.moduleID = moduleID;
    }

    public AllModuleIdData() {
    }


    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setModuleID(String moduleID) {
        this.moduleID = moduleID;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getModuleID() {
        return moduleID;
    }

    @Override
    public String toString() {
        return "AllModuleIdData{" +
                "moduleName='" + moduleName + '\'' +
                ", moduleID='" + moduleID + '\'' +
                '}';
    }
}
