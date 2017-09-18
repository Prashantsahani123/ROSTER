package com.SampleApp.row.Data;

/**
 * Created by user on 04-02-2016.
 */
public class CategoryData  {
    String catId,catName;

    public CategoryData() {
    }

    public CategoryData(String catId, String catName) {
        this.catId = catId;
        this.catName = catName;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    @Override
    public String toString() {
        return "CategoryData{" +
                "catId='" + catId + '\'' +
                ", catName='" + catName + '\'' +
                '}';
    }
}
