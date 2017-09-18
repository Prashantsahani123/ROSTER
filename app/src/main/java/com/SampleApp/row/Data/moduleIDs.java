package com.SampleApp.row.Data;

/**
 * Created by user on 29-08-2016.
 */
public class moduleIDs {
    String moduleId;
    String oldName;
    String newName;

    public moduleIDs(){
    }
    public moduleIDs(String moduleId, String oldName, String newName){
        this.moduleId = moduleId;
        this.oldName = oldName;
        this.newName = newName;
    }
    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }


}
