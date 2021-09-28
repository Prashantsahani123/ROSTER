package com.NEWROW.row.Data;

import java.io.Serializable;

/**
 * Created by user on 06-09-2016.
 */
public class FundingData implements Serializable {

    String Pk_Fund_ID;
    String Fund_Name;

    public FundingData() {

    }

    public FundingData(String pk_Fund_ID, String fund_Name) {
        Pk_Fund_ID = pk_Fund_ID;
        Fund_Name = fund_Name;
    }

    @Override
    public String toString() {
        return "FundingData{" +
                "Pk_Fund_ID='" + Pk_Fund_ID + '\'' +
                ", Fund_Name='" + Fund_Name + '\'' +
                '}';
    }

    public String getPk_Fund_ID() {
        return Pk_Fund_ID;
    }

    public void setPk_Fund_ID(String pk_Fund_ID) {
        Pk_Fund_ID = pk_Fund_ID;
    }

    public String getFund_Name() {
        return Fund_Name;
    }

    public void setFund_Name(String fund_Name) {
        Fund_Name = fund_Name;
    }
}
