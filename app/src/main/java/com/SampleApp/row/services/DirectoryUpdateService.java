package com.SampleApp.row.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.SampleApp.row.Data.profiledata.AddressData;
import com.SampleApp.row.Data.profiledata.BusinessMemberDetails;
import com.SampleApp.row.Data.profiledata.CompleteProfile;
import com.SampleApp.row.Data.profiledata.FamilyMemberData;
import com.SampleApp.row.Data.profiledata.PersonalMemberDetails;
import com.SampleApp.row.Data.profiledata.ProfileMasterData;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.PreferenceManager;
import com.SampleApp.row.Utils.TBPrefixes;
import com.SampleApp.row.Utils.Utils;
import com.SampleApp.row.sql.ProfileModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;



/**
 * Created by USER1 on 30-03-2017.
 * @author Prasad
 */


public class DirectoryUpdateService extends IntentService{

    public static final String ACTION_DIRECTORY_SYNC = "com.touchbase.DirectorSync",
        ACTION_DIRECTORY_SYNC_COMPLETED = "com.touchbase.DirectorySyncCompleted",
        ACTION_DIRECTORY_SYNC_FAILED = "com.touchbase.DirectorySyncFailed",
        ACTION_DIRECTORY_SYNC_IN_PROGRESS = "com.touchbase.DirectorySyncInProgress";
    String groupId = "";
    String directoryPath;
    static final int MODE_INSERT = 1, MODE_UPDATE = 2;
    InsertHandler newHandler;
    UpdateHandler updateHandler;
    DeleteHandler deleteHandler;

    public DirectoryUpdateService() {
        super("TouchBase");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            directoryPath = PreferenceManager.getPreference(getBaseContext(), Constant.UPDATE_FILE_NAME);
            if (directoryPath != null) {
                Utils.log("Value of path is : " + directoryPath);
            } else {
                Utils.log("Value of path is null");
            }
            newHandler = new InsertHandler(directoryPath + "/NewMembers");
            updateHandler = new UpdateHandler(directoryPath +"/UpdatedMembers");
            deleteHandler = new DeleteHandler(directoryPath + "/DeletedMembers");
            Utils.log("Starting inserting new records");
            newHandler.sendEmptyMessage(0);
        } catch(Exception e) {
            Utils.log("Error is : " + e);
            e.printStackTrace();
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
    /*@Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }
    */
    //     /**
    //     * Processing this json string
    //     *
    //     *{
    //     *"MemberDetails": [
    //     *    {
    //     *        "masterID": "40",
    //     *        "grpID": "157",
    //     *        "profileID": "1310",
    //     *        "isAdmin": "no",
    //     *        "memberName": "Yugandhara",
    //     *        "memberEmail": "",
    //     *        "memberMobile": "+91 9552243221",
    //     *         "memberCountry": "India",
    //     *         "profilePic": "",
    //     *        "isPersonalDetVisible": "yes",
    //     *        "isBusinDetVisible": "yes",
    //     *        "isFamilDetailVisible": "yes",
    //     *
    //     *         "personalMemberDetails": [
    //     *            {
    //     *                "fieldID": "0",
    //     *                "uniquekey": "member_name",
    //     *                "key": "Name",
    //     *                "value": "Yugandhara",
    //     *                "colType": "Text",
    //     *                "isEditable": "1",
    //     *                "isVisible": "y"
    //     *            },
    //     *
    //     *            {
    //     *                "fieldID": "0",
    //     *                "uniquekey": "member_mobile_no",
    //     *                "key": "Mobile Number",
    //     *                 "value": "+91 9552243221",
    //     *                "colType": "Number",
    //     *                "isEditable": "1",
    //     *                "isVisible": "y"
    //     *            },
    //     *
    //     *            {
    //     *                "fieldID": "0",
    //     *                "uniquekey": "secondry_mobile_no",
    //     *                "key": "Secondary Mobile Number",
    //     *                "value": "",
    //     *                 "colType": "Number",
    //     *                 "isEditable": "1",
    //     *                "isVisible": "y"
    //     *            },
    //     *
    //     *            {
    //     *                "fieldID": "0",
    //     *                 "uniquekey": "member_email_id",
    //     *                 "key": "Email Id",
    //     *                 "value": "",
    //     *                "colType": "Text",
    //     *                 "isEditable": "1",
    //     *                 "isVisible": "y"
    //     *             },
    //     *
    //     *            {
    //     *                 "fieldID": "0",
    //     *                 "uniquekey": "member_date_of_birth",
    //     *                 "key": "Birth Date",
    //     *                "value": "01/01/1753",
    //     *                 "colType": "Date",
    //     *                 "isEditable": "1",
    //     *                 "isVisible": "y"
    //     *             },
    //     *
    //     *            {
    //     *                "fieldID": "0",
    //     *                "uniquekey": "member_date_of_wedding",
    //     *                "key": "Anniversary Date",
    //     *                "value": "01/01/1753",
    //     *                "colType": "Date",
    //     *                "isEditable": "1",
    //     *                "isVisible": "y"
    //     *            },
    //     *
    //     *            {
    //     *                "fieldID": "0",
    //     *                "uniquekey": "blood_Group",
    //     *                "key": "Blood Group",
    //     *                "value": "",
    //     *                "colType": "BloodGroup",
    //     *                "isEditable": "1",
    //     *                "isVisible": "y"
    //     *            },
    //     *
    //     *            {
    //     *                "fieldID": "13",
    //     *                "uniquekey": "Passport Number",
    //     *                "key": "Passport Number",
    //     *                "value": "ADF6999W",
    //     *                "colType": "Text",
    //     *                "isEditable": "0",
    //     *                "isVisible": "y"
    //     *            },
    //     *
    //     *            {
    //     *                 "fieldID": "12",
    //     *                "uniquekey": "BranchName",
    //     *                "key": "BranchName",
    //     *                "value": "CBUH",
    //     *                "colType": "Text",
    //     *                "isEditable": "1",
    //     *                "isVisible": "y"
    //     *            },
    //     *
    //     *            {
    //     *                "fieldID": "15",
    //     *                "uniquekey": "Salary",
    //     *                "key": "Salary",
    //     *                "value": "1000",
    //     *                "colType": "Number",
    //     *                "isEditable": "1",
    //     *                "isVisible": "y"
    //     *            },
    //     *
    //     *            {
    //     *                "fieldID": "1",
    //     *                "uniquekey": "Student Roll No",
    //     *                "key": "Student Roll No",
    //     *                "value": "67",
    //     *                 "colType": "Text",
    //     *                "isEditable": "1",
    //     *                "isVisible": "y"
    //     *            }
    //     *
    //     *        ],
    //     *        "businessMemberDetails": [
    //     *            {
    //     *                "fieldID": "0",
    //     *                "uniquekey": "member_buss_email",
    //     *                "key": "Business Email Id",
    //     *                "value": "",
    //     *                "colType": "Text",
    //     *                 "isEditable": "1",
    //     *                "isVisible": "y"
    //     *            },
    //     *
    //     *            {
    //     *                "fieldID": "0",
    //     *                "uniquekey": "BusinessName",
    //     *                "key": "Business Name",
    //     *                "value": "",
    //     *                 "colType": "Text",
    //     *                 "isEditable": "1",
    //     *                "isVisible": "y"
    //     *            }
    //     *        ],
    //     *
    //     *        "familyMemberDetails": {
    //     *            "isVisible": "y",
    //     *            "familyMemberDetail": [
    //     *             	{
    //	 *                  "familyMemberId": "524",
    //	 *                  "memberName": "Rupali",
    //	 *                  "relationship": "Sister",
    //	 *                  "dOB": "05/10/2010",
    //	 *                  "emailID": "",
    //	 *                  "anniversary": "",
    //	 *                  "contactNo": "+91 8828387261",
    //	 *                  "particulars": "",
    //	 *                   "bloodGroup": "--Select--"
    //	 *                },
    //     *
    //	 *                {
    //	 *                  "familyMemberId": "524",
    //	 *                  "memberName": "Shamli",
    //	 *                  "relationship": "Sister",
    //	 *                  "dOB": "05/10/2002",
    //	 *                  "emailID": "",
    //	 *                  "anniversary": "",
    //	 *                  "contactNo": "+91 8828387000",
    //	 *                  "particulars": "",
    //	 *                  "bloodGroup": "O+"
    //	 *                }
    //     *            ]
    //     *        },
    //     *        "addressDetails": {
    //     *            "isResidanceAddrVisible": "y",
    //     *            "isBusinessAddrVisible": "y",
    //     *            "addressResult": [
    //     *    		  {
    //     *			      "addressID": "301",
    //	 *			      "addressType": "Residence",
    //	 *			      "address": "Vishnu Nagar",
    //	 *			      "city": "Dombivali",
    //	 *			      "state": "Maharashtra",
    //	 *			      "country": "",
    //	 * 			      "pincode": "",
    //	 *			      "phoneNo": " ",
    //	 *			      "fax": "",
    //	 *			      "profileID": "409"
    //     *
    //	 *			  },
    //	 *			  {
    //	 *			      "addressID": "343",
    //	 *			      "addressType": "Business",
    //	 * 			      "address": "Kaizen Infotech, Gala Complex,Mulund west",
    //	 *			      "city": "Mumbai",
    //	 *			      "state": "Maharashtra",
    //	 *			      "country": "",
    //	 *			      "pincode": "420785",
    //	 *			      "phoneNo": " ",
    //	 *			      "fax": "",
    //	 *			      "profileID": "409"
    //     * 			  }
    //	 * 			]
    //     *         }
    //     *     }
    //     *  }
    //     */

   /**
    * Process record function process all records either for inserting as new records or updating new records
    * depending on the value of the mode. Mode can be MODE_INSERT or MODE_UPDATE. This function takes two parameters
    *
    * @param records    Records is string type of data which is basically JSON object string
    *                   Format of this JSON string as described in above comment.
    *
    * @param mode       mode is integer type of value which has two possible values as of now.
    *                   1) MODE_INSERT ( value is 1 ) - Means processRecords function is called to insert records.
    *                   2) MODE_UPDATE ( value is 2 ) - Means processRecords function is called to update records.
    *
    * @return           After processing all the records for either insert or update, it returns boolean value. If
    *                   Function returns true means all records processed successfully, else it means there was some
    *                   problem while processing records.
    */
    public boolean processRecords(String records, int mode) {
        try {
            JSONObject json = new JSONObject(records);
            JSONArray memberProfiles = json.getJSONArray("MemberDetail");
            int n = memberProfiles.length();
            Utils.log("Number of records for processing are : "+n);


            ArrayList<CompleteProfile> profileList = new ArrayList<>();
            for(int i=0;i<n;i++) {
                JSONObject profile = memberProfiles.getJSONObject(i);
                Utils.log("Complete profile is : "+profile);
                // start of ProfileMasterData
                String masterId = profile.getString("masterID");
                String grpId = profile.getString("grpID");
                this.groupId = grpId;
                String profileId = profile.getString("profileID");
                String isAdmin = profile.getString("isAdmin");
                String memberName = profile.getString("memberName").trim();
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
                JSONArray personalMemberDetails = profile.getJSONArray("personalMemberDetails");
                int m = personalMemberDetails.length();
                ArrayList<PersonalMemberDetails> personalDetailsList = new ArrayList<>();
                for(int j=0;j<m;j++) {
                    JSONObject personalDetail = personalMemberDetails.getJSONObject(j);
                    String fieldID = personalDetail.getString("fieldID");
                    String uniquekey = personalDetail.getString("uniquekey");
                    //Utils.log("Unique Key : "+uniquekey);
                    String key = personalDetail.getString("key");
                    String value = personalDetail.getString("value");
                    String colType = personalDetail.getString("colType");
                    String isEditable = personalDetail.getString("isEditable");
                    String isVisible = personalDetail.getString("isVisible");

                    PersonalMemberDetails data = new PersonalMemberDetails(profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible);
                    personalDetailsList.add(data);
                }
                // end of PersonalMemberDetails

                // start of BusinessMemberDetails
                JSONArray businessMemberDetails = profile.getJSONArray("businessMemberDetails");
                m = businessMemberDetails.length();
                ArrayList<BusinessMemberDetails> businessDetailsList = new ArrayList<>();
                for(int j=0;j<m;j++) {

                    JSONObject personalDetail = businessMemberDetails.getJSONObject(j);
                    String fieldID = personalDetail.getString("fieldID");
                    String uniquekey = personalDetail.getString("uniquekey");
                    String key = personalDetail.getString("key");
                    String value = personalDetail.getString("value");
                    String colType = personalDetail.getString("colType");
                    String isEditable = personalDetail.getString("isEditable");
                    String isVisible =  personalDetail.getString("isVisible");

                    BusinessMemberDetails data = new BusinessMemberDetails(profileId, fieldID, uniquekey, key, value, colType, isEditable, isVisible);
                    businessDetailsList.add(data);
                }
                // end of BusinessMemberDetails

                // Start of family MemberDetails
                JSONArray familyMemberDetails = profile.getJSONObject("familyMemberDetails").getJSONArray("familyMemberDetail");

                m = familyMemberDetails.length();
                ArrayList<FamilyMemberData> familyMemberList = new ArrayList<>();
                for(int j=0;j<m;j++) {
                    JSONObject familyMember = familyMemberDetails.getJSONObject(j);
                    String familyMemberId = familyMember.getString("familyMemberId");
                    String familyMemberName = familyMember.getString("memberName");
                    String relationship = familyMember.getString("relationship");
                    String dob = familyMember.getString("dOB");
                    String emailID = familyMember.getString("emailID");
                    String anniversary = familyMember.getString("anniversary");
                    String contactNo = familyMember.getString("contactNo");
                    String particulars = familyMember.getString("particulars");
                    String bloodGroup = familyMember.getString("bloodGroup");
                    String[] contactFields = contactNo.split(" "); // contact number is in the form "countryId contactNumber" e.g. 1 8877665544 here 1 is country code and 8877665544 is mobile number
                    String countryId = "0";

                    try {
                        countryId = contactFields[0];
                        String countryCode = Utils.getCountryCode(getBaseContext(), countryId);
                        contactNo = countryCode + " " +contactFields[1];
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    FamilyMemberData data = new FamilyMemberData(profileId, familyMemberId, familyMemberName, relationship, dob, emailID, anniversary, contactNo, particulars, bloodGroup, countryId);
                    familyMemberList.add(data);
                }
                // End of family member details

                // Start of address details
                JSONArray addressDetails = profile.getJSONObject("addressDetails").getJSONArray("addressResult");
                m = addressDetails.length();
                ArrayList<AddressData> addressList = new ArrayList<>();

                for(int j=0;j<m;j++) {
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
                // end of address details

                CompleteProfile cp = new CompleteProfile(profileData, personalDetailsList, businessDetailsList, familyMemberList,addressList);
                profileList.add(cp);
            }

            ProfileModel model = new ProfileModel(getBaseContext());
            if ( mode == MODE_INSERT ) {
                return model.addProfiles(profileList);
            } else if ( mode == MODE_UPDATE) {
                return model.updateProfiles(profileList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


    // Insert handler process files in NewProfiles folder. It fetches next file name from the queue and sends it for processing
    // processRecords() function performs actual job of inserting all the records to the database.
    public class InsertHandler extends Handler {
        ArrayList<File> queue;
        String newProfileDir;
        boolean needToProcess = false;
        public InsertHandler(String newProfileDir) {
            queue = new ArrayList<>();
            try {
                this.newProfileDir = newProfileDir;
                Utils.log("Path of new record files : " + newProfileDir);
                File dir = new File(newProfileDir);
                File[] files = dir.listFiles();

                for (int i = 0; i < files.length; i++) {
                    queue.add(files[i]);
                    Utils.log("File : " + files[i].getPath() + " added to the insert queue");
                }
                needToProcess = true;
            } catch (NullPointerException npe) {
                Utils.log("Error is : "+npe);
                // This is temporary adjustment done for zip file problem
                npe.printStackTrace();
                try {
                    File dir = new File(directoryPath);
                    File[] files = dir.listFiles();

                    for (int i = 0; i < files.length; i++) {
                        if ( files[i].getName().startsWith("NewMembers")) {
                            queue.add(files[i]);
                            Utils.log("File : " + files[i].getPath() + " added to the insert queue");
                        }
                    }
                    needToProcess = true;
                } catch (NullPointerException npe1) {

                    Utils.log("Error while re-loading file : "+npe1);
                }
            } catch(Exception e) {
                Utils.log("Error is : "+e);
                e.printStackTrace();
            }
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if ( ! queue.isEmpty() ) {
                File file = queue.remove(0);
                byte[] buffer = new byte[(int)file.length()];
                try {
                    Utils.log("Processing file for inserting : "+file.getPath());
                    FileInputStream fin = new FileInputStream(file);
                    fin.read(buffer);
                    String data = new String(buffer);
                    fin.close();

                    boolean completed = processRecords(data, MODE_INSERT);
                    if (!completed ) {
                        queue.add(0, file);
                    } else {
                        Utils.log("Ended with inserting records");
                        // Current file is completed processing.
                        // We can delete file
                        boolean deleted = file.delete();
                        Utils.log("File : "+ file.getPath()+" is deleted : "+ deleted);
                        //Utils.log("Started processing updated records");
                        //updateHandler.sendEmptyMessage(0);

                    }
                    sendEmptyMessage(0);
                } catch (FileNotFoundException e) {
                    Utils.log("Error is : "+e);
                    e.printStackTrace();
                } catch(Exception e) {
                    Utils.log("Error is : "+e);
                    e.printStackTrace();
                }
                //Byte[] buffer = new Byte[]
            } else {  // this means completed with inserting records. Next step is to process updated records
                File newRecFolder = new File(newProfileDir);
                boolean deleted = newRecFolder.delete();
                Utils.log("Folder : "+newRecFolder.getPath()+" delete status : "+deleted);
                Utils.log("Started processing updated records");
                updateHandler.sendEmptyMessage(0);
            }
        }
    }

    // Update handler processes file one by one for updating profiles.
    public class UpdateHandler extends Handler {
        ArrayList<File> queue;
        String updateProfileDir;
        boolean needToProcess = false;
        public UpdateHandler(String updateProfileDir) {
            this.updateProfileDir = updateProfileDir;
            queue = new ArrayList<>();
            try {
                File dir = new File(updateProfileDir);
                if ( dir.exists() ) {
                    File[] files = dir.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        queue.add(files[i]);
                    }
                }
                needToProcess = true;
            } catch(NullPointerException npe) {
                needToProcess = false;
            } catch (Exception e) {
                Utils.log("Error is : "+e);
                e.printStackTrace();
            }
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if ( ! needToProcess ) {
                deleteHandler.sendEmptyMessage(0);
                return;
            }
            if ( ! queue.isEmpty() ) {
                File file = queue.remove(0);
                byte[] buffer = new byte[(int)file.length()];
                try {
                    FileInputStream fin = new FileInputStream(file);
                    fin.read(buffer);
                    String data = new String(buffer);
                    fin.close();
                    boolean completed = processRecords(data, MODE_UPDATE);
                    if (!completed ) {
                        queue.add(0, file);
                    } else {
                        Utils.log("Ended processing updated records");
                        // Current file is completed processing.
                        // We can delete file
                        boolean deleted = file.delete();
                        Utils.log("File : "+ file.getPath()+" is deleted : "+ deleted);

                    }
                    sendEmptyMessage(0);
                } catch (FileNotFoundException e) {
                    Utils.log("Error is : "+e);
                    e.printStackTrace();
                } catch(Exception e) {
                    Utils.log("Error is : "+e);
                    e.printStackTrace();
                }
                //Byte[] buffer = new Byte[]
            } else { // this means update records is completed. Now process deleted records
                File updateRecFolder = new File(updateProfileDir);
                boolean deleted = updateRecFolder.delete();
                Utils.log("Folder : "+updateRecFolder.getPath()+" delete status : "+deleted);
                Utils.log("Started deleting records");
                deleteHandler.sendEmptyMessage(0);
            }
        }
    }

    // Delete handler deletes desired profile records
    public class DeleteHandler extends Handler {
        ArrayList<File> queue;
        boolean needToProcess = false;
        String deleteProfileDir;
        public DeleteHandler(String deleteProfileDir) {
            queue = new ArrayList<>();
            this.deleteProfileDir = deleteProfileDir;
            try {
                File dir = new File(deleteProfileDir);
                File[] files = dir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    queue.add(files[i]);
                }
                needToProcess = true;
            } catch(NullPointerException npe) {
                Utils.log("Error is : " + npe);
                npe.printStackTrace();
                needToProcess = false;
            }
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if ( ! needToProcess ) {
                Utils.log("Ended processing deleted records");

                Intent updateIntent = new Intent(ACTION_DIRECTORY_SYNC);
                updateIntent.putExtra("ExtraMessage", ACTION_DIRECTORY_SYNC_COMPLETED);
                updateIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);
                getBaseContext().sendBroadcast(updateIntent);
                Utils.log("Broadcast message sent");


                String updatedOn = PreferenceManager.getPreference(getApplicationContext(), TBPrefixes.TEMP_UPDATED_ON+groupId);
                Utils.log("Shared date in preferences is : "+updatedOn+" with name : "+TBPrefixes.DIRECTORY_PREFIX+groupId);
                PreferenceManager.savePreference(getApplicationContext(), TBPrefixes.DIRECTORY_PREFIX+groupId, updatedOn);
                boolean deleted = new File(directoryPath).delete();
                Utils.log("Folder : "+directoryPath+" delete status : "+deleted);
                return;
            }
            if ( ! queue.isEmpty() ) {
                File file = queue.remove(0);
                byte[] buffer = new byte[(int)file.length()];
                try {
                    FileInputStream fin = new FileInputStream(file);
                    fin.read(buffer);
                    String data = new String(buffer);
                    fin.close();
                    boolean completed = new ProfileModel(getApplicationContext()).deleteProfiles(data);

                    if (!completed ) {
                        queue.add(0, file);
                    } else {
                        Utils.log("Ended processing deleted records");
                        // Current file is completed processing.
                        // We can delete file
                        boolean deleted = file.delete();
                        Utils.log("File : "+ file.getPath()+" is deleted : "+ deleted);

                        Intent intent = new Intent(ACTION_DIRECTORY_SYNC);
                        intent.putExtra("ExtraMessage", ACTION_DIRECTORY_SYNC_COMPLETED);
                        getBaseContext().sendBroadcast(intent);

                        Utils.log("Broadcast message sent");


                        String updatedOn = PreferenceManager.getPreference(getApplicationContext(), TBPrefixes.TEMP_UPDATED_ON+groupId);
                        Utils.log("Shared date in preferences is : "+updatedOn+" with name : "+TBPrefixes.DIRECTORY_PREFIX+groupId);
                        PreferenceManager.savePreference(getApplicationContext(), TBPrefixes.DIRECTORY_PREFIX+groupId, updatedOn);

                        Utils.log("Broadcast message sent");
                        File deletedRecFolder = new File(deleteProfileDir);
                        deleted = deletedRecFolder.delete();
                        Utils.log("Folder : "+deletedRecFolder.getPath()+" delete status : "+deleted);

                        deleted = new File(directoryPath).delete();
                        Utils.log("Folder : "+directoryPath+" delete status : "+deleted);
                    }
                    sendEmptyMessage(0);
                } catch (FileNotFoundException e) {
                    Utils.log("Error is : "+e);
                    e.printStackTrace();
                } catch(Exception e) {
                    Utils.log("Error is : "+e);
                    e.printStackTrace();
                }
                //Byte[] buffer = new Byte[]
            }
        }
    }
}
