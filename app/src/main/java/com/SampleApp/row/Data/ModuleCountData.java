package com.SampleApp.row.Data;

/**
 * Created by USER1 on 25-11-2016.
 */
public class ModuleCountData {
    String moduleId, count;

    public ModuleCountData(String moduleId, String count) {
        this.moduleId = moduleId;
        this.count = count;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
