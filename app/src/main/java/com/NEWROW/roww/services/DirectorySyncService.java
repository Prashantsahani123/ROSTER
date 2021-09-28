package com.NEWROW.row.services;

import android.Manifest;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import androidx.core.content.ContextCompat;

import com.NEWROW.row.Data.profiledata.AddressData;
import com.NEWROW.row.Data.profiledata.BusinessMemberDetails;
import com.NEWROW.row.Data.profiledata.CompleteProfile;
import com.NEWROW.row.Data.profiledata.FamilyMemberData;
import com.NEWROW.row.Data.profiledata.PersonalMemberDetails;
import com.NEWROW.row.Data.profiledata.ProfileMasterData;
import com.NEWROW.row.Utils.AppController;
import com.NEWROW.row.Utils.Constant;
import com.NEWROW.row.Utils.InternetConnection;
import com.NEWROW.row.Utils.PreferenceManager;
import com.NEWROW.row.Utils.TBPrefixes;
import com.NEWROW.row.Utils.UnzipUtility;
import com.NEWROW.row.Utils.Utils;
import com.NEWROW.row.sql.ProfileModel;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

public class DirectorySyncService extends Service {

    Context context;
    String grpId="",zipUrl="",updatedOn="1970/01/01 00:00:00",firstTime="No";
    private ProfileModel profileModel;
    private File downloadDir, tempFile;
    private long downloadId;
    private DownloadManager downloadManager;
    private DownloadCompleteReceiver receiver;
    private UpdateStatusReceiver updateStatusReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        profileModel = new ProfileModel(context);

        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        receiver = new DownloadCompleteReceiver();
        updateStatusReceiver = new UpdateStatusReceiver();

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        registerReceiver(updateStatusReceiver, new IntentFilter(ServiceUpdateService.ACTION_DIRECTORY_SYNC));

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       //take grpID
        grpId = PreferenceManager.getPreference(context,PreferenceManager.GROUP_ID,"");


        if(grpId.equalsIgnoreCase("")) {

            SharedPreferences sp = getSharedPreferences("userName", MODE_PRIVATE);
           // String pp = (sp.getString("grpProfileId", ""));
            String pr = (sp.getString("grpId", ""));

            grpId = pr;
        }


        Utils.log("In Service");

        if ( InternetConnection.checkConnection(context)) {
            getProfileSyncInfo();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.savePreference(getApplicationContext(),PreferenceManager.REQUESTED+grpId,"N");
        unregisterReceiver(receiver);
        unregisterReceiver(updateStatusReceiver);
    }

    public DirectorySyncService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void

    getProfileSyncInfo() {

        //final ProgressDialog pd = new ProgressDialog(context);
        //pd.setCancelable(false);

        boolean isDataAvailable = profileModel.isDataAvailable(Long.parseLong(grpId));

        if(isDataAvailable){
            firstTime="No";
        } else {
            firstTime="Yes";
        }

        String isRequested = PreferenceManager.getPreference(context,PreferenceManager.REQUESTED+grpId,"N");

        if(isRequested.equalsIgnoreCase("N")){

            Utils.log("Started getProfileSyncInfo");

            Hashtable<String, String> paramTable = new Hashtable<>();

            updatedOn = PreferenceManager.getPreference(this, TBPrefixes.DIRECTORY_PREFIX + grpId, "1970/01/01 00:00:00");

            paramTable.put("updatedOn", updatedOn);
            paramTable.put("grpID", "" + grpId);

            JSONObject jsonRequestData = null;

            try {

                jsonRequestData = new JSONObject(new Gson().toJson(paramTable));

                Utils.log("Url : " + Constant.GetMemberListSync + " Data : " + jsonRequestData);//http://rowtestapi.rosteronwheels.com/V4/api/Member/GetMemberListSync

                PreferenceManager.savePreference(context,PreferenceManager.REQUESTED+grpId,"Y");

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                        Constant.GetMemberListSync,
                        jsonRequestData,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                Utils.log("Success : " + response);

                                handleSyncInfo(response);
                            }

                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {

                                PreferenceManager.savePreference(context,PreferenceManager.REQUESTED+grpId,"N");
                                Utils.log("Error is : " + error);
                                error.printStackTrace();
                                stopSelf();
                            }
                        });

                request.setRetryPolicy(new DefaultRetryPolicy(
                        Constant.VOLLEY_MAX_REQUEST_TIMEOUT,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));

                AppController.getInstance().addToRequestQueue(context, request);

            } catch (JSONException e) {
                e.printStackTrace();
                stopSelf();
            }
        }
    }

    public void handleSyncInfo(JSONObject response) {

        try {

            String status = response.getString("status");

            if (status.equals("0")) {

                Utils.log("Response is : " + response);

                updatedOn = response.getString("curDate");
                Utils.serUpdateOn=response.getString("curDate");
                zipUrl = response.getString("zipFilePath");

                Utils.log("Zip File Path : " + zipUrl);

                PreferenceManager.savePreference(context, TBPrefixes.TEMP_UPDATED_ON + grpId, updatedOn);

                if (zipUrl.trim().equals("")) {
                    processDirectRecords(response);
                } else { // means zip file is available for download
//                    if (checkPermissionForExternalStorage()) {
//
//                    } else {
//                        PreferenceManager.savePreference(context, PreferenceManager.REQUESTED + grpId, "N");
//                        stopSelf();
//                    }
                    startDownload();
                }

            }else {
                updatedOn = response.getString("curDate");
                PreferenceManager.savePreference(context, TBPrefixes.DIRECTORY_PREFIX + grpId, updatedOn);
                PreferenceManager.savePreference(context, PreferenceManager.REQUESTED + grpId, "N");
                stopSelf();
            }

        } catch (JSONException je) {
            Utils.log("Error is : " + je);
            je.printStackTrace();
            PreferenceManager.savePreference(context, PreferenceManager.REQUESTED + grpId, "N");
            stopSelf();
        }
    }

    public void processDirectRecords(JSONObject response) {

        try {

            final ProfileModel model = new ProfileModel(getBaseContext());
            //final ProgressDialog pd = new ProgressDialog(context);
            //pd.setMessage("Processing contacts & Storing offline");

            if (firstTime.equalsIgnoreCase("yes")) {  // Means process only new records.

                final JSONArray newRecords = response.getJSONObject("MemberDetail").getJSONArray("NewMemberList");

                Utils.log("Found records : " + newRecords.length());

                final ArrayList<CompleteProfile> newRecordsList = processRecords(newRecords);

                final int numberOfRecords = newRecordsList.size();

                Utils.log("Parsed number of records : " + newRecordsList.size());

                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        boolean success = model.addProfiles(newRecordsList);

                        if (!success) {
                            Utils.log("Failed to insert new records in local db. Retrying in 2 seconds");
                            sendEmptyMessageDelayed(0, 2000);
                        } else {
                            PreferenceManager.savePreference(context, TBPrefixes.DIRECTORY_PREFIX + grpId, updatedOn);
                            PreferenceManager.savePreference(context, PreferenceManager.REQUESTED + grpId, "N");
                            stopSelf();
                            //pd.hide();

                          //  Toast.makeText(context, numberOfRecords + " contacts stored", Toast.LENGTH_LONG).show();

                        }
                    }
                };
                handler.sendEmptyMessage(0);
                //pd.show();
            } else {  // means process all new, updated and deleted records
                JSONArray newRecords = response.getJSONObject("MemberDetail").getJSONArray("NewMemberList");
                JSONArray updatedRecords = response.getJSONObject("MemberDetail").getJSONArray("UpdatedMemberList");
                final String deletedRecords = response.getJSONObject("MemberDetail").getString("DeletedMemberList");
                final ArrayList<CompleteProfile> newRecordsList = processRecords(newRecords);
                final ArrayList<CompleteProfile> updatedRecordsList = processRecords(updatedRecords);

                final int numberOfRecordsProcessed;


                if (!deletedRecords.equals("")) {
                    numberOfRecordsProcessed = newRecordsList.size() + updatedRecordsList.size() + deletedRecords.split(",").length;
                } else {
                    numberOfRecordsProcessed = newRecordsList.size() + updatedRecordsList.size();
                }


                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);

                        boolean success = model.updateProfiles(newRecordsList, updatedRecordsList, deletedRecords);
                        if (!success) {
                            Utils.log("Failed to update updated records in local db. Retrying in 2 seconds");
                            sendEmptyMessageDelayed(0, 2000);
                        } else {
                            Utils.log("in sync handler");
                            PreferenceManager.savePreference(context, TBPrefixes.DIRECTORY_PREFIX + grpId, updatedOn);
                            PreferenceManager.savePreference(context, PreferenceManager.REQUESTED + grpId, "N");
                            stopSelf();

                        }
                    }
                };

                handler.sendEmptyMessage(0);

                if(numberOfRecordsProcessed==0){
                    PreferenceManager.savePreference(context, PreferenceManager.REQUESTED + grpId, "N");
                    stopSelf();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            PreferenceManager.savePreference(context, PreferenceManager.REQUESTED + grpId, "N");
            stopSelf();
        }

        //loadDynamicFields();
    }

    public ArrayList<CompleteProfile> processRecords(JSONArray memberProfiles) {

        ArrayList<CompleteProfile> profileList = new ArrayList<>();

        try {

            int n = memberProfiles.length();

            for (int i = 0; i < n; i++) {

                JSONObject profile = memberProfiles.getJSONObject(i);
                Utils.log(profile.toString());
                // start of ProfileMasterData
                String masterId = profile.getString("masterID");
                String grpId = profile.getString("grpID");
                String profileId = profile.getString("profileID");
                String isAdmin = profile.getString("isAdmin");
                String memberName = profile.getString("memberName");
                String memberEmail = profile.getString("memberEmail");
                String memberMobile = profile.getString("memberMobile");
                String memberCountry = profile.getString("memberCountry");
                String profilePic = profile.getString("profilePic");
                String familyPic = profile.getString("familyPic");
                String isPersonalDetVisible = profile.getString("isPersonalDetVisible");
                String isBussinessDetVisible = profile.getString("isBusinDetVisible");
                String isFamilyDetVisible = profile.getString("isFamilDetailVisible");
                String isResidanceAddrVisible = profile.getJSONObject("addressDetails").getString("isResidanceAddrVisible");
                String isBusinessAddrVisible = profile.getJSONObject("addressDetails").getString("isBusinessAddrVisible");
                ProfileMasterData profileData = new ProfileMasterData(
                        masterId,
                        grpId,
                        profileId,
                        isAdmin,
                        memberName,
                        memberEmail,
                        memberMobile,
                        memberCountry,
                        profilePic,
                        familyPic,
                        isPersonalDetVisible,
                        isBussinessDetVisible,
                        isFamilyDetVisible,
                        isResidanceAddrVisible,
                        isBusinessAddrVisible);
                // end of ProfileMasterData

                // start of PersonalMemberDetails
                ArrayList<PersonalMemberDetails> personalDetailsList = new ArrayList<>();

                try {

                    JSONArray personalMemberDetails = profile.getJSONArray("personalMemberDetails");

                    int m = personalMemberDetails.length();

                    for (int j = 0; j < m; j++) {
                        JSONObject personalDetail = personalMemberDetails.getJSONObject(j);
                        String fieldID = personalDetail.getString("fieldID");
                        String uniquekey = personalDetail.getString("uniquekey");
                        String key = personalDetail.getString("key");
                        String value = personalDetail.getString("value");
                        String colType = personalDetail.getString("colType");
                        String isEditable = personalDetail.getString("isEditable");
                        String isVisible = personalDetail.getString("isVisible");

                        PersonalMemberDetails data = new PersonalMemberDetails(profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible);
                        personalDetailsList.add(data);
                    }
                } catch (JSONException jsone) {
                    Utils.log("Error is : " + jsone);
                    jsone.printStackTrace();
                }
                // end of PersonalMemberDetails

                // start of BusinessMemberDetails

                ArrayList<BusinessMemberDetails> businessDetailsList = new ArrayList<>();

                try {

                    JSONArray businessMemberDetails = profile.getJSONArray("businessMemberDetails");

                    int m = businessMemberDetails.length();

                    for (int j = 0; j < m; j++) {
                        JSONObject personalDetail = businessMemberDetails.getJSONObject(j);
                        String fieldID = personalDetail.getString("fieldID");
                        String uniquekey = personalDetail.getString("uniquekey");
                        String key = personalDetail.getString("key");
                        String value = personalDetail.getString("value");
                        String colType = personalDetail.getString("colType");
                        String isEditable = personalDetail.getString("isEditable");
                        String isVisible = personalDetail.getString("isVisible");

                        BusinessMemberDetails data = new BusinessMemberDetails(profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible);
                        businessDetailsList.add(data);
                    }
                } catch (JSONException jsone) {
                    Utils.log("Error is : " + jsone);
                    jsone.printStackTrace();
                }
                // end of BusinessMemberDetails

                // Start of family MemberDetails

                ArrayList<FamilyMemberData> familyMemberList = new ArrayList<>();
                try {
                    JSONArray familyMemberDetails = profile.getJSONObject("familyMemberDetails").getJSONArray("familyMemberDetail");

                    int m = familyMemberDetails.length();

                    for (int j = 0; j < m; j++) {
                        JSONObject familyMember = familyMemberDetails.getJSONObject(j);
                        String familyMemberId = familyMember.getString("familyMemberId");
                        String familyMemberName = familyMember.getString("memberName");
                        String relationship = familyMember.getString("relationship");
                        String dob = familyMember.getString("dOB");
                        String emailID = familyMember.getString("emailID");
                        String anniversary = familyMember.getString("anniversary");
                        String contactNo = familyMember.getString("contactNo");
                        String[] contactFields = contactNo.split(" "); // contact number is in the form "countryId contactNumber" e.g. 1 8877665544 here 1 is country code and 8877665544 is mobile number
                        String countryId = "0";

                        try {
                            countryId = contactFields[0];
                            String countryCode = Utils.getCountryCode(context, countryId);
                            contactNo = countryCode + " " +contactFields[1];
                        } catch(Exception e) {
                            e.printStackTrace();
                        }

                        String particulars = familyMember.getString("particulars");
                        String bloodGroup = familyMember.getString("bloodGroup");
                        FamilyMemberData data = new FamilyMemberData(profileId, familyMemberId, familyMemberName, relationship, dob, emailID, anniversary, contactNo, particulars, bloodGroup, countryId);
                        familyMemberList.add(data);
                    }
                } catch (JSONException jsone) {
                    Utils.log("Error is : " + jsone);
                    jsone.printStackTrace();
                }
                // End of family member details

                // Start of address details
                ArrayList<AddressData> addressList = new ArrayList<>();

                try {

                    JSONArray addressDetails = profile.getJSONObject("addressDetails").getJSONArray("addressResult");
                    int m = addressDetails.length();

                    for (int j = 0; j < m; j++) {
                        JSONObject address = addressDetails.getJSONObject(j);
                        String addressID = address.getString("addressID");
                        String addressType = address.getString("addressType");
                        String addressText = address.getString("address");
                        String city = address.getString("city");
                        String state = address.getString("state");
                        String country = address.getString("country");
                        String pincode = address.getString("pincode");
                        String phoneNo = address.getString("phoneNo");
                        String fax = address.getString("fax");

                        AddressData data = new AddressData(profileId, addressID, addressType, addressText, city, state, country, pincode, phoneNo, fax);
                        addressList.add(data);
                    }

                } catch (JSONException jsone) {
                    Utils.log("Error is : " + jsone);
                    jsone.printStackTrace();
                }
                // end of address details
                CompleteProfile cp = new CompleteProfile(profileData, personalDetailsList, businessDetailsList, familyMemberList, addressList);
                profileList.add(cp);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return profileList;
    }

    public void startDownload() {

        try {

            Utils.log("Starting downloading file");
            // File sdCard = Environment.getExternalStorageDirectory();
            File sdCard=getExternalFilesDir(null);
            downloadDir = new File(sdCard, "Touchbase/temp");

            if (!downloadDir.exists()) {
                downloadDir.mkdirs();
            }
            tempFile = new File(downloadDir, "directory_" + new Date().getTime() + ".zip");
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(zipUrl));
            request.setVisibleInDownloadsUi(false);
            request.setDestinationUri(Uri.fromFile(tempFile));
            downloadId = downloadManager.enqueue(request);
            Utils.log("Files is added to the download queue");
        }catch (Exception e){
            PreferenceManager.savePreference(context,PreferenceManager.REQUESTED+grpId,"N");
            stopSelf();
        }


        //loadDynamicFieldsFromServer();
    }

    public class DownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String actionString = intent.getAction();

            if (actionString.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

                try {

                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);

                    Cursor cur = downloadManager.query(query);

                    if (cur.moveToNext()) {

                        int status = cur.getInt(cur.getColumnIndex(DownloadManager.COLUMN_STATUS));

                        if (status == DownloadManager.STATUS_SUCCESSFUL) {

                            UnzipUtility uu = new UnzipUtility();

                            try {

                                File unzipDir = new File(downloadDir.getPath() + File.separator + tempFile.getName().replaceAll(".zip", ""));
                                unzipDir.mkdir();
                                uu.unzip(tempFile.getPath(), unzipDir.getPath());
                                tempFile.delete();  // delete zip file after extracting
                                PreferenceManager.savePreference(context, Constant.UPDATE_FILE_NAME, unzipDir.getPath());
                                Intent serviceIntent = new Intent(context, ServiceUpdateService.class);
                                serviceIntent.putExtra("directoryPath", unzipDir.getPath());
                                context.startService(serviceIntent);
                                Utils.log("Download completed and services is processing the updates");
                                deleteServerFile();

                            } catch (IOException e) {
                                PreferenceManager.savePreference(context,PreferenceManager.REQUESTED+grpId,"N");
                                Utils.log("Error is : " + e);
                                e.printStackTrace();

                            }

                        } else {
                            Utils.log("Failed to download update file");
                            PreferenceManager.savePreference(context,PreferenceManager.REQUESTED+grpId,"N");
                            stopSelf();
                        }
                    }
                } catch (NullPointerException npe) {
                    Utils.log("Error is : " + npe);
                    npe.printStackTrace();
                    PreferenceManager.savePreference(context,PreferenceManager.REQUESTED+grpId,"N");
                    stopSelf();

                } catch (Exception e) {
                    Utils.log("Error is : " + e);
                    e.printStackTrace();
                    PreferenceManager.savePreference(context,PreferenceManager.REQUESTED+grpId,"N");
                    stopSelf();

                }
            }
        }
    }

    public void deleteServerFile() {
        try {
            String zipFileDelete = new URL(zipUrl).getFile();
            Hashtable<String, String> params = new Hashtable<>();
            params.put("folderPath", zipFileDelete);
            JSONObject jsonRequest = new JSONObject(new Gson().toJson(params));

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constant.DELETE_ZIP_FILE, jsonRequest,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.log(response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
            AppController.getInstance().addToRequestQueue(context, request);
        } catch (MalformedURLException mfe) {
            Utils.log("Error is : " + mfe);
            mfe.printStackTrace();
        } catch (JSONException jse) {
            Utils.log("Error is : " + jse);
            jse.printStackTrace();
        } catch (Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }

    }

    public class UpdateStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Utils.log("Inside UpdateStatusReceiver");
            String action = intent.getAction();
            Utils.log("Value of action is : " + action);
            if (action.equals(ServiceUpdateService.ACTION_DIRECTORY_SYNC)) {
                String message = intent.getExtras().getString("ExtraMessage");
                Utils.log("Value of message : " + message);
                if (message.equals(ServiceUpdateService.ACTION_DIRECTORY_SYNC_COMPLETED)) {
                    Utils.log("Processing is completed and fetching data from local data");
                    PreferenceManager.savePreference(context, TBPrefixes.DIRECTORY_PREFIX + grpId, updatedOn);
                    refreshData("");
                }
            }
        }
    }

    public void refreshData(String msg) {
//        String temp= PreferenceManager.getPreference(context,TBPrefixes.TEMP_UPDATED_ON+grpId,"");
//        PreferenceManager.savePreference(context, TBPrefixes.DIRECTORY_PREFIX + grpId, temp);
        PreferenceManager.savePreference(context,PreferenceManager.REQUESTED+grpId,"N");
        stopSelf();
    }

    public boolean checkPermissionForExternalStorage(){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }



}
