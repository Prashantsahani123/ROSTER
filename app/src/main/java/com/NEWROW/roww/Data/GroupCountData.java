package com.NEWROW.row.Data;

import android.util.Log;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by USER1 on 25-11-2016.
 */
public class GroupCountData {
    String groupId, totalCount, groupCategory;

    Hashtable<String, String> moduleCount;

    public GroupCountData(String groupId, String totalCount, String groupCategory, Hashtable<String, String> moduleCount) {
        this.groupId = groupId;
        this.totalCount = totalCount;
        this.groupCategory = groupCategory;
        this.moduleCount = moduleCount;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getGroupCategory() {
        return groupCategory;
    }

    public void setGroupCategory(String groupCategory) {
        this.groupCategory = groupCategory;
    }

    public void updateModuleCount(String moduleId, String count) {
        try {
            moduleCount.put(moduleId, count);
            Enumeration<String> modules = moduleCount.elements();
            int newTotal = 0;
            while ( modules.hasMoreElements() ) {
                String myModuleId = moduleCount.get(modules.nextElement());
                String tempCount = moduleCount.get(myModuleId);
                int myModuleCount = 0;
                if ( tempCount != null )
                myModuleCount = Integer.parseInt(tempCount);
                newTotal += myModuleCount;
            }

            totalCount = ""+newTotal;

        } catch(Exception e) {
            Log.e("TouchBase", "♦♦♦♦Error is : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getModuleCount(String moduleId) {
        try {
            Log.e("TouchBase", "♦♦♦♦Module Count : "+moduleCount +" and module id is : "+moduleId);
            String count = moduleCount.get(moduleId);
            if ( count == null ) return 0;
            return Integer.parseInt(count);
        } catch(Exception e) {
            Log.e("TouchBase", "♦♦♦♦Error is : "+e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

}
