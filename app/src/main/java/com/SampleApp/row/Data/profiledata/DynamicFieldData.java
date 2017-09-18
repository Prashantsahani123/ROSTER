package com.SampleApp.row.Data.profiledata;

import java.util.Comparator;
import java.util.Hashtable;

/**
 * Created by USER1 on 21-03-2017.
 */
public class DynamicFieldData {
    String profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible;

    public DynamicFieldData(String profileId, String fieldID, String uniquekey, String key, String value, String colType, String isEditable, String isVisible) {
        this.profileId = profileId;
        this.fieldID = fieldID;
        this.uniquekey = uniquekey;
        this.key = key;
        this.value = value;
        this.colType = colType;
        this.isEditable = isEditable;
        this.isVisible = isVisible;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getFieldID() {
        return fieldID;
    }

    public void setFieldID(String fieldID) {
        this.fieldID = fieldID;
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

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColType() {
        return colType;
    }

    public void setColType(String colType) {
        this.colType = colType;
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
        //return "";
        return "DynamicFieldData{" +
                "profileId='" + profileId + '\'' +
                ", fieldID='" + fieldID + '\'' +
                ", uniquekey='" + uniquekey + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public static class DynamicFieldComparator implements Comparator<DynamicFieldData> {
        @Override
        public int compare(DynamicFieldData lhs, DynamicFieldData rhs) {
            int lhsNum = 0;
            try {
                lhsNum = fieldSeqTable.get(lhs.getUniquekey()).intValue();
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            int rhsNum = 0;
            try {
                rhsNum = fieldSeqTable.get(rhs.getUniquekey()).intValue();
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ( lhsNum < rhsNum ) {
                return -1;
            } else if ( lhsNum > rhsNum ) {
                return 1;
            }
            return 0;
        }


    }

    private static Hashtable<String, Integer> fieldSeqTable = new Hashtable<>();

    static {
        fieldSeqTable.put("designation", 1); // For classification
        fieldSeqTable.put("Keywords", 2); // For keywords
        fieldSeqTable.put("member_rotary_id", 3); // For rotary id
        fieldSeqTable.put("member_master_designation", 4); // For Club designation
        fieldSeqTable.put("dg_master_designation", 5); // For District Designation
        fieldSeqTable.put("rotary_donar_recognation", 6); // For Donar Recognition
        fieldSeqTable.put("member_date_of_birth", 7); // For Birthday
        fieldSeqTable.put("member_date_of_wedding", 8); // For Anniversary
        fieldSeqTable.put("blood_Group", 9); // For Blood Group

    }
}
