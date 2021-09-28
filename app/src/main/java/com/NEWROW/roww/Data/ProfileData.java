package com.NEWROW.row.Data;

/**
 * Created by USER on 03-02-2016.
 */
public class ProfileData {
    String fieldId;
    String uniquekey;
    String key;
    String value;
    String colType;
    String date;
    String isEditable;
    String isVisible;

    public ProfileData() {
    }

    public ProfileData(String fieldId,String uniquekey, String key, String value, String colType,String isEditable,String isVisible) {
        this.fieldId = fieldId;
        this.uniquekey = uniquekey;
        this.key = key;
        this.value = value;
        this.colType = colType;
        this.isEditable = isEditable;
        this.isVisible = isVisible;
    }

    public String getUniquekey() {
        return uniquekey;
    }

    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }

    public String getKey() {
        return key;
    }

    public String setKey(String key) {
        this.key = key;
        return key;
    }

    public String getValue() {
        return value;
    }

    public String setValue(String value) {
        this.value = value;
        return value;
    }

    public String getColType() {
        return colType;
    }

    public void setColType(String colType) {
        this.colType = colType;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(String isEditable) {
        this.isEditable = isEditable;
    }

    public String getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(String isVisible) {
        this.isVisible = isVisible;
    }




    @Override
    public String toString() {
        return "ProfileData{" +
                "fieldID='" + fieldId + '\'' +
                "uniquekey='" + uniquekey + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", colType='" + colType + '\'' +
                ", isEditable='" + isEditable + '\'' +
                ", isVisible='" + isVisible + '\'' +
                '}';
    }
}
