package com.SampleApp.row.Data;

import android.util.Log;

import java.util.Hashtable;

/**
 * Created by USER on 29-08-2016.
 */
public class NotificationCountData {


    Hashtable<String, GroupCountData> countTable;

    public NotificationCountData() {
        countTable = new Hashtable<String, GroupCountData>();
    }

    public void addGroupCountData(String groupId, GroupCountData groupCountData) {
        countTable.put(groupId, groupCountData);
    }

    public int getGroupCount(String groupId) {
        int count = 0;
        try {
            if (countTable.containsKey(groupId)) {
                count = Integer.parseInt(countTable.get(groupId).getTotalCount());
                return count;
            }
        } catch(Exception e){
            Log.e("TouchBase", "♦♦♦♦ Error is : "+e.getMessage());
            e.printStackTrace();
        }
        return count;
    }

    public int getModuleCount(String groupId, String moduleId) {
        try {
            return countTable.get(groupId).getModuleCount(moduleId);
        } catch(NullPointerException e) {

        }
        return 0;
    }

    public void updateModuleCount(String groupId, String moduleId, String newCount) {
        try {
            countTable.get(groupId).updateModuleCount(moduleId, newCount);
        } catch(Exception e) {
            Log.e("TouchBase", "♦♦♦♦Error is : "+e.getMessage());
            e.printStackTrace();
        }
    }
}
