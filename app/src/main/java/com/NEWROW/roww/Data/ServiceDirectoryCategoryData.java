package com.NEWROW.row.Data;

/**
 * Created by user on 21-03-2017.
 */
public class ServiceDirectoryCategoryData {
    int categoryId;
    String categoryName;
    int totalCount;

    public ServiceDirectoryCategoryData(){

    }

    public ServiceDirectoryCategoryData(int categoryId,String categoryName,int totalCount ){
        this.categoryId = categoryId;
        this.categoryName  = categoryName;
        this.totalCount = totalCount;

    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

}
