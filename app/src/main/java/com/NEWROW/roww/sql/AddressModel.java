package com.NEWROW.row.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.NEWROW.row.Data.CountryData;
import com.NEWROW.row.Data.profiledata.AddressData;
import com.NEWROW.row.Utils.Utils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Created by USER1 on 22-03-2017.
 */
public class AddressModel {
    Context context;
    SQLiteDatabase db;

    public AddressModel(Context context) {
        this.context = context;
        this.db = DBConnection.getInstance(context);
    }

    // select all addresses
    public Hashtable<String, AddressData> getAddresses(long profileId) {
        Hashtable<String, AddressData> table = new Hashtable<>();
        try {
            Cursor cur = db.rawQuery("select * from "+Tables.AddressDetails.TABLE_NAME+" where "+Tables.AddressDetails.Columns.PROFILE_ID+"="+profileId, null);
            while (cur.moveToNext()) {
                String addressID = cur.getString(cur.getColumnIndex(Tables.AddressDetails.Columns.ADDRESS_ID));
                String addressType = cur.getString(cur.getColumnIndex(Tables.AddressDetails.Columns.ADDRESS_TYPE));
                String address = cur.getString(cur.getColumnIndex(Tables.AddressDetails.Columns.ADDRESS));
                String city = cur.getString(cur.getColumnIndex(Tables.AddressDetails.Columns.CITY));
                String state = cur.getString(cur.getColumnIndex(Tables.AddressDetails.Columns.STATE));
                String country = cur.getString(cur.getColumnIndex(Tables.AddressDetails.Columns.COUNTRY));
                String pincode = cur.getString(cur.getColumnIndex(Tables.AddressDetails.Columns.PINCODE));
                String phoneNo = cur.getString(cur.getColumnIndex(Tables.AddressDetails.Columns.PHONE_NO));
                String fax = cur.getString(cur.getColumnIndex(Tables.AddressDetails.Columns.FAX));
                try {
                    if (phoneNo == null || phoneNo.trim().equals("") || phoneNo.trim().isEmpty()) {

                    } else {
                        phoneNo = searchForCountryCode(country) + phoneNo;
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }

                table.put(addressType, new AddressData("" + profileId, addressID, addressType, address, city, state, country, pincode, phoneNo, fax));
            }
        } catch(Exception e) {
            Utils.log("Error is : "+e);
            e.printStackTrace();
        }
        return table;
    }


    public ArrayList<AddressData> getAddresses(long profileId, String isBusinessAddrVisible, String isResidanceAddrVisible ) {
        ArrayList<AddressData> list = new ArrayList<>();
        try {
            // This query is query to retrieve addresses if isBusinessAddrVisible and isResidanceAddrVisible both are true
            String query = "";
            if  (isBusinessAddrVisible.equals("y") && isResidanceAddrVisible.equals("y")){
                query = "select * from "+Tables.AddressDetails.TABLE_NAME+" where "+Tables.AddressDetails.Columns.PROFILE_ID+"="+profileId;
            } else if  (isBusinessAddrVisible.equals("y")){
                query = "select * from "+Tables.AddressDetails.TABLE_NAME+" where addressType='Business' and "+Tables.AddressDetails.Columns.PROFILE_ID+"="+profileId;
            } else if ( isResidanceAddrVisible.equals("y")) {
                query = "select * from "+Tables.AddressDetails.TABLE_NAME+" where addressType='Residence' and "+Tables.AddressDetails.Columns.PROFILE_ID+"="+profileId;
            } else {
                Utils.log("Both address is slow : "+query);
                return list;
            }

            Utils.log("Query is : "+query);
            Cursor cur = db.rawQuery(query, null);
            while (cur.moveToNext()) {
                String addressID = cur.getString(cur.getColumnIndex(Tables.AddressDetails.Columns.ADDRESS_ID));
                String addressType = cur.getString(cur.getColumnIndex(Tables.AddressDetails.Columns.ADDRESS_TYPE));
                String address = cur.getString(cur.getColumnIndex(Tables.AddressDetails.Columns.ADDRESS));
                String city = cur.getString(cur.getColumnIndex(Tables.AddressDetails.Columns.CITY));
                String state = cur.getString(cur.getColumnIndex(Tables.AddressDetails.Columns.STATE));
                String country = cur.getString(cur.getColumnIndex(Tables.AddressDetails.Columns.COUNTRY));
                String pincode = cur.getString(cur.getColumnIndex(Tables.AddressDetails.Columns.PINCODE));
                String phoneNo = cur.getString(cur.getColumnIndex(Tables.AddressDetails.Columns.PHONE_NO));
                String fax = cur.getString(cur.getColumnIndex(Tables.AddressDetails.Columns.FAX));

                list.add(new AddressData("" + profileId, addressID, addressType, address, city, state, country, pincode, phoneNo, fax));
            }
        } catch(Exception e) {
            Utils.log("Error is : "+e);
            e.printStackTrace();
        }

        return list;
    }

    public String searchForCountryCode(String countryName) {
        ArrayList<CountryData> countryList = Utils.getCountryList(context);
        int n = countryList.size();
        for (int i = 0; i < n; i++) {
            CountryData cd = countryList.get(i);
            String name = cd.getCountryName();
            if (name.equalsIgnoreCase(countryName)) {
                return cd.getCountryCode()+ " ";
            }
        }
        return "";
    }
    // add an address
    public long addAddressDetails(AddressData data) {
        long id = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(Tables.AddressDetails.Columns.PROFILE_ID, data.getProfileID());
            values.put(Tables.AddressDetails.Columns.ADDRESS_ID, data.getAddressID());
            values.put(Tables.AddressDetails.Columns.ADDRESS_TYPE, data.getAddressType());
            values.put(Tables.AddressDetails.Columns.ADDRESS, data.getAddress());
            values.put(Tables.AddressDetails.Columns.CITY, data.getCity());
            values.put(Tables.AddressDetails.Columns.STATE, data.getState());
            values.put(Tables.AddressDetails.Columns.COUNTRY, data.getCountry());
            values.put(Tables.AddressDetails.Columns.PINCODE, data.getPincode());
            values.put(Tables.AddressDetails.Columns.PHONE_NO, data.getPhoneNo());
            values.put(Tables.AddressDetails.Columns.FAX, data.getFax());
            id = db.insert(Tables.AddressDetails.TABLE_NAME, null, values);
        } catch(Exception e) {
            Utils.log("Error is : "+e);
            e.printStackTrace();
        }

        return id;
    }
    // add multiple addresses
    public boolean addAddressDetails(ArrayList<AddressData> list) {
        boolean isSuccessful = false;
        try {
            Iterator<AddressData> iterator = list.iterator();
            while(iterator.hasNext()) {
                long id = addAddressDetails(iterator.next());
                if ( id == -1) {
                    if ( db.inTransaction() ) {
                        db.endTransaction(); // i.e. transaction failed
                    }
                    return false;
                }
            }
            return true;
        } catch(Exception e) {
            Utils.log("Error is : "+e);
            e.printStackTrace();
            isSuccessful = false;
        }
        return isSuccessful;
    }
    // add multiple addresses as transaction
    public boolean addAddressDetailsTrans(ArrayList<AddressData> list) {
        boolean isSuccessful = false;
        try {
            db.beginTransaction();
            Iterator<AddressData> iterator = list.iterator();
            while(iterator.hasNext()) {
                long id = addAddressDetails(iterator.next());
                if ( id == -1) {
                    db.endTransaction();
                    return false;
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            isSuccessful = true;
        } catch(Exception e) {
            Utils.log("Error is : "+e);
            e.printStackTrace();
            db.endTransaction();
            isSuccessful = false;
        }
        return isSuccessful;
    }

    // update address

    public boolean updateAddresses(ArrayList<AddressData> list ) {

        if (list.size()==0) {
            Utils.log("Address list is empty");
            return true;
        }


        try {
            Iterator<AddressData> iterator = list.iterator();
            while(iterator.hasNext()) {
                AddressData data = iterator.next();
                boolean success = updateAddressDetails(data);
                if ( ! success ) {
                    if ( db.inTransaction()) {
                        db.endTransaction();
                    }

                    return false;
                }
            }
            return true;
        } catch(Exception e) {
            Utils.log("Error is : "+e);
            e.printStackTrace();
        }

        return false;
    }
    public boolean updateAddressDetails(AddressData data) {
        boolean isSuccessful = false;

        try {
            ContentValues values = new ContentValues();
            values.put(Tables.AddressDetails.Columns.ADDRESS_TYPE, data.getAddressType());
            values.put(Tables.AddressDetails.Columns.ADDRESS, data.getAddress());
            values.put(Tables.AddressDetails.Columns.CITY, data.getCity());
            values.put(Tables.AddressDetails.Columns.STATE, data.getState());
            values.put(Tables.AddressDetails.Columns.COUNTRY, data.getCountry());
            values.put(Tables.AddressDetails.Columns.PINCODE, data.getPincode());
            values.put(Tables.AddressDetails.Columns.PHONE_NO, data.getPhoneNo());
            values.put(Tables.AddressDetails.Columns.FAX, data.getFax());
            String whereClause = Tables.AddressDetails.Columns.ADDRESS_ID+"="+data.getAddressID();

            Cursor cur = db.query(Tables.AddressDetails.TABLE_NAME, null, whereClause, null, null, null, null);
            if ( cur.getCount() == 0 ) {
                long id = addAddressDetails(data);
                if ( id != -1 ) {
                    return true;
                } else {
                    return false;
                }
            }
            int n = db.update(Tables.AddressDetails.TABLE_NAME, values, whereClause, null);
            Utils.log("Number of updated addresses : "+n);
            if ( n == 1 ) isSuccessful = true;
        } catch(Exception e) {
            Utils.log("Error is : "+e);
            e.printStackTrace();
        }
        return isSuccessful;
    }

    // delete address
    public boolean deleteAddressDetails(String addressId) {
        boolean isSuccessful = false;

        try {
            String whereClause = Tables.AddressDetails.Columns.ADDRESS_ID+"="+addressId;
            int n = db.delete(Tables.AddressDetails.TABLE_NAME, whereClause, null);
            if ( n == 1 ) isSuccessful = true;
        } catch(Exception e) {
            Utils.log("Error is : "+e);
            e.printStackTrace();
        }
        return isSuccessful;
    }

    // delete address
    public boolean deleteAddressByProfileId(long profileId) {
        boolean isSuccessful = false;

        try {
            String whereClause = Tables.AddressDetails.Columns.PROFILE_ID+"="+profileId;
            int n = db.delete(Tables.AddressDetails.TABLE_NAME, whereClause, null);
            if ( n == 1 ) isSuccessful = true;
        } catch(Exception e) {
            Utils.log("Error is : "+e);
            e.printStackTrace();
        }
        return isSuccessful;
    }



}
