package com.SampleApp.row.Data;

/**
 * Created by admin on 22-05-2017.
 */

public class RotaryLibraryData {
    String title;
    String description;
    String moduleName;
    public RotaryLibraryData(){

    }

    public RotaryLibraryData(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
