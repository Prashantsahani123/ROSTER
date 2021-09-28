package com.NEWROW.row.Data;

/**
 * Created by admin on 21-07-2017.
 */

public class LabelData {
    String label;
    String count;

    public LabelData(String label, String count) {
        this.label = label;
        this.count = count;
    }

    public LabelData(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
