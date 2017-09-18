package com.SampleApp.row.sql;

import java.util.ArrayList;

/**
 * Created by USER on 01-07-2016.
 */
public class Tables {

    /*
    * createTableList : This is array list of string which contains the definitions of all the
    *                   tables to be created while creating the database.*/

    public static ArrayList<String> createTableList = new ArrayList<>();

    /*
    * dropTableList : This is the array of string which contains the drop query of all the
    *                 tables to be deleted while upgrading tha database.
    *
    *                 While upgrading the database, all the existing tables will be dropped first
    *                 and database will be recreated.
    * */
    public static ArrayList<String> dropTableList = new ArrayList<>();

    static {
        createTableList.add(GroupMaster.CREATE_TABLE);
        createTableList.add(ModuleDataMaster.CREATE_TABLE);
        createTableList.add(ServiceDIrectoryDataMaster.CREATE_TABLE);
        createTableList.add(AlbumMaster.CREATE_TABLE);
        createTableList.add(AlbumPhotoMaster.CREATE_TABLE);
        createTableList.add(UploadedPhoto.CREATE_TABLE);
        createTableList.add(AttendanceMaster.CREATE_TABLE);
        createTableList.add(ReplicaInfo.CREATE_TABLE);
        createTableList.add(CalendarMaster.CREATE_TABLE);
        createTableList.add(ProfileMaster.CREATE_TABLE);
        createTableList.add(PersonalMemberDetails.CREATE_TABLE);
        createTableList.add(BusinessMemberDetails.CREATE_TABLE);
        createTableList.add(FamilyMemberDetail.CREATE_TABLE);
        createTableList.add(AddressDetails.CREATE_TABEL);
        createTableList.add(PastPresidentMaster.CREATE_TABLE);
        createTableList.add(RotaryFeeds.CREATE_TABLE);
        createTableList.add(RotaryBlogs.CREATE_TABLE);

        dropTableList.add(GroupMaster.DROP_TABLE);
        dropTableList.add(ModuleDataMaster.DROP_TABLE);
        dropTableList.add(ServiceDIrectoryDataMaster.DROP_TABLE);
        dropTableList.add(AlbumMaster.DROP_TABLE);
        dropTableList.add(AlbumPhotoMaster.DROP_TABLE);
        dropTableList.add(UploadedPhoto.DROP_TABLE);
        dropTableList.add(AttendanceMaster.DROP_TABLE);
        dropTableList.add(ReplicaInfo.DROP_TABLE);
        dropTableList.add(CalendarMaster.DROP_TABLE);
        dropTableList.add(ProfileMaster.DROP_TABLE);
        dropTableList.add(PersonalMemberDetails.DROP_TABLE);
        dropTableList.add(BusinessMemberDetails.DROP_TABLE);
        dropTableList.add(FamilyMemberDetail.DROP_TABLE);
        dropTableList.add(AddressDetails.DROP_TABLE);
        dropTableList.add(PastPresidentMaster.DROP_TABLE);
        dropTableList.add(RotaryFeeds.DROP_TABLE);
        dropTableList.add(RotaryBlogs.DROP_TABLE);
    }

    public static class GroupMaster {
        public static final String TABLE_NAME = "group_master",
                CREATE_TABLE = "create table group_master" +
                        "(" +
                        "_id integer primary key," +
                        "masterUID integer," +
                        "grpId integer," +
                        "grpName text," +
                        "grpImg text," +
                        "grpProfileid integer," +
                        "myCategory integer," +
                        "isGrpAdmin text" +
                        ")";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static class Columns {
            public static final String _ID = "_id",
                    MASTER_UID = "masterUID",
                    GROUP_ID = "grpId",
                    GROUP_NAME = "grpName",
                    GROUP_IMAGE = "grpImg",
                    GROUP_PROFILE_ID = "grpProfileid",
                    MY_CATEGORY = "myCategory",
                    IS_GROUP_ADMIN = "isGrpAdmin";
        }
    }

    /*public static class OtherGroupInfo {
        public static final String TABLE_NAME = "OtherGroupInfo",

        DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME,

        CREATE_TABLE = "create table "+ TABLE_NAME +
                "(" +
                "_id integer primary key," +
                "grpId integer," +
                "expiryDate text," +
                "column1 text," +
                "column2 text," +
                "column3 text," +
                "column4 text," +
                "column5 text" +
                ")";

        public static class Columns {
            public static final String _ID = "_id",
                    EXPIRY_DATE = "validUpto",
                    COLUMN1 = "column1",
                    COLUMN2 = "column2",
                    COLUMN3 = "column3",
                    COLUMN4 = "column4",
                    COLUMN5 = "column5",
                    GROUP_ID = "grpId";
        }
    }*/
//--------------------------Module Data Master----------------------

    public static class ModuleDataMaster {

        public static final String TABLE_NAME = "module_data_master",
                CREATE_TABLE = "create table module_data_master" +
                        "(" +
                        "_id integer primary key," +
                        "masterUID integer," +
                        "groupModuleId integer," +
                        "groupId text," +
                        "moduleId text," +
                        "moduleName text," +
                        "moduleStaticRef text," +
                        "image text," +
                        "moduleOrderNo integer" +
                        ")";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static class Columns {
            public static final String _ID = "_id",
                    MASTER_UID = "masterUID",
                    GROUP_MODULE_ID = "groupModuleId",
                    GROUP_ID = "groupId",
                    MODULE_ID = "moduleId",
                    MODULE_NAME = "moduleName",
                    MODULE_STATIC_REF = "moduleStaticRef",
                    IMAGE = "image",
                    MODULE_ORDER_NO = "moduleOrderNo";
        }
    }

    //--------------------------Service Directory Data Master----------------------

    public static class ServiceDIrectoryDataMaster {
        public static final String ADDCOLUMN_CATTEGORY_ID = "ALTER TABLE service_directory_data_master ADD cattegoryId INTEGER";
        public static final String ADDCOLUMN_WEBSITELINK = "ALTER TABLE service_directory_data_master ADD websiteLink TEXT";

        public static final String TABLE_NAME = "service_directory_data_master",
                CREATE_TABLE = "create table service_directory_data_master" +
                        "(" +
                        "_id integer primary key," +
                        "masterUID integer," +
                        "serviceDirId integer," +
                        "groupId integer," +
                        "memberName text," +
                        "image text," +
                        "contactNo text," +
                        "isdeleted text," +
                        "description text," +
                        "contactNo2 text," +
                        "pax text," +
                        "email text," +
                        "address text," +
                        "long text," +
                        "lat text," +
                        "countryId1 integer," +
                        "countryId2 integer," +
                        "csvKeywords text," +
                        "city text," +
                        "state text," +
                        "country text," +
                        "zip text," +
                        "moduleId text," +
                        "cattegoryId integer," +
                        "websiteLink text" +
                        ")";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static class Columns {
            public static final String _ID = "_id",
                    MASTER_UID = "masterUID",
                    SERVICE_DIRECTORY_ID = "serviceDirId",
                    SERVICE_DIRECTORY_GROUP_ID = "groupId",
                    SERVICE_DIRECTORY_MEMBER_NAME = "memberName",
                    SERVICE_DIRECTORY_IMAGE_URL = "image",
                    SERVICE_DIRECTORY_CONTACT_NUMBER = "contactNo",
                    SERVICE_DIRECTORY_ISDELETED = "isdeleted",
                    SERVICE_DIRECTORY_DESCRIPTION = "description",
                    SERVICE_DIRECTORY_CONTACT_NUMBER2 = "contactNo2",
                    SERVICE_DIRECTORY_PAX = "pax",
                    SERVICE_DIRECTORY_EMAIL = "email",
                    SERVICE_DIRECTORY_ADDRESS = "address",
                    SERVICE_DIRECTORY_LONG = "long",
                    SERVICE_DIRECTORY_Lat = "lat",
                    COUNTRY_ID1 = "countryId1",
                    COUNTRY_ID2 = "countryId2",
                    CSV_KEYWORDS = "csvKeywords",
                    CITY = "city",
                    STATE = "state",
                    COUNTRY = "country",
                    ZIP = "zip",
                    MODULE_ID = "moduleId",
                    CATTEGORYID = "cattegoryId",
                    WEBSITELINK = "websiteLink";

        }

    }

    //--------------- gallery data master -----------------------------

    public static class AlbumMaster {
        public static final String TABLE_NAME = "gallery_master",
                CREATE_TABLE = "create table gallery_master" +
                        "(" +
                        "_id integer primary key," +
                        "albumId integer," +
                        "title text," +
                        "description text," +
                        "image text," +
                        "groupId integer," +
                        "isAdmin text," +
                        "moduleId text" +
                        ")";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static class Columns {
            public static final String _ID = "_id",
                    ALBUM_ID = "albumId",
                    TITLE = "title",
                    DESCRIPTION = "description",
                    ALBUM_IMAGE = "image",
                    GROUP_ID = "groupId",
                    IS_GROUP_ADMIN = "isAdmin",
                    MODULE_ID = "moduleId";
        }
    }

    //---------------  gallery photo data master -----------------------------

    public static class AlbumPhotoMaster {
        public static final String TABLE_NAME = "album_photo_master",
                CREATE_TABLE = "create table album_photo_master" +
                        "(" +
                        "_id integer primary key," +
                        "groupId integer," +
                        "albumId integer," +
                        "photoId integer," +
                        "url text," +
                        "description text" +
                        ")";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static class Columns {
            public static final String _ID = "_id",
                    GRP_ID = "groupId",
                    ALBUM_ID = "albumId",
                    PHOTO_ID = "photoId",
                    URL = "url",
                    DESCRIPTION = "description";


        }
    }

    //--------------- Table to keep Records of Photo aded to Server -----------------------------

    public static class UploadedPhoto {
        public static final String TABLE_NAME = "UploadedPhoto",
                CREATE_TABLE = "create table UploadedPhoto" +
                        "(" +
                        "_id integer primary key," +
                        "photoId text," +
                        "path text," +
                        "description text," +
                        "albumId text," +
                        "groupId text," +
                        "createdBy text," +
                        "isUploaded text" +
                        ")";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static class Columns {
            public static final String _ID = "_id",
                    PHOTO_ID = "photoId",
                    PATH = "path",
                    DESCRIPTION = "description",
                    ALBUM_ID = "albumId",
                    GROUP_ID = "groupId",
                    CREATED_BY = "createdBy",
                    IS_UPLOADED = "isUploaded";
        }


    }


    //---------------  Attendance master -----------------------------


    public static class AttendanceMaster {
        public static final String TABLE_NAME = "attendance_master",
                CREATE_TABLE = "create table attendance_master" +
                        "(" +
                        "_id integer primary key," +
                        "masterUID integer," +
                        "moduleId integer," +
                        "memberId integer," +
                        "studentId integer," +
                        "name text," +
                        "month text," +
                        "year text," +
                        "attendence text" +
                        ")";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static class Columns {
            public static final String _ID = "_id",
                    MASTER_UID = "masterUID",
                    MODULE_ID = "moduleId",
                    MEMBER_ID = "memberId",
                    STUDENT_ID = "studentId",
                    NAME = "name",
                    MONTH = "month",
                    YEAR = "year",
                    ATTENDANCE = "attendence";


        }
    }

    //---------------  Replica table -----------------------------

    public static class ReplicaInfo {
        public static final String TABLE_NAME = "ReplicaInfo",
                CREATE_TABLE = "create table ReplicaInfo" +
                        "(" +
                        "_id integer primary key autoincrement," +
                        "moduleId text," +
                        "replicaOf text" +
                        ")";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static class Columns {
            public static final String _ID = "_id",
                    MODULE_ID = "moduleId",
                    REPLICA_OF = "replicaOf";
        }
    }

    //---------------------- Calendar Events table-----------------------

    public static class CalendarMaster {
        public static final String TABLE_NAME = "NewCalendarMaster",
                CREATE_TABLE = " create table NewCalendarMaster" +
                        "(" +
                        "_id integer primary key autoincrement," +
                        "groupId integer," +
                        "uniqueId text," +
                        "eventDate text," +
                        "type text," +
                        "typeId integer," +
                        "title text," +
                        "memberFamilyID integer" +
                        ")";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static class Columns {
            public static final String _ID = "_id",
                    GROUP_ID = "groupId",
                    UNIQUE_ID = "uniqueId",
                    EVENTDATE = "eventDate",
                    TYPE = "type",
                    TYPE_ID = "typeId",
                    TITLE = "title",
                    MEMBER_FAMILY_ID = "memberFamilyID";
        }

    }

    //---------------------------- Tables related to Profile -----------------------
    public static class ProfileMaster {
        public static final String TABLE_NAME = "ProfileMaster";
        public static final String CREATE_TABLE = "create table ProfileMaster" +
                "(" +
                "_id integer primary key autoincrement," +
                "masterId integer," +
                "grpId integer," +
                "profileId integer," +
                "isAdmin text," +
                "memberName text collate nocase," +
                "memberEmail text," +
                "memberMobile text," +
                "memberCountry text," +
                "profilePic text," +
                "familyPic text," +
                "isPersonalDetVisible text," +
                "isBussinessDetVisible text," +
                "isFamilyDetVisible text," +
                "isResidanceAddrVisible text," +
                "isBusinessAddrVisible text" +
                ")";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static class Columns {
            public static final String MASTER_ID = "masterId",
                    GROUP_ID = "grpId",
                    PROFILE_ID = "profileId",
                    IS_ADMIN = "isAdmin",
                    MEMBER_NAME = "memberName",
                    MEMBER_EMAIL = "memberEmail",
                    MEMBER_MOBILE = "memberMobile",
                    MEMBER_COUNTRY = "memberCountry",
                    PROFILE_PIC = "profilePic",
                    FAMILY_PIC = "familyPic",
                    IS_PERSONAL_DET_VISIBLE = "isPersonalDetVisible",
                    IS_BUSSINESS_DET_VISBILE = "isBussinessDetVisible",
                    IS_FAMILY_DET_VISIBLE = "isFamilyDetVisible",
                    IS_RESIDANCE_ADDR_VISIBLE = "isResidanceAddrVisible",
                    IS_BUSINESS_ADDR_VISIBLE = "isBusinessAddrVisible";

        }
    }

    public static class PersonalMemberDetails {
        public final static String CREATE_TABLE = "create table PersonalMemberDetails" +
                "(" +
                "_id integer primary key autoincrement," +
                "profileId integer," +
                "fieldId integer," +
                "uniquekey text," +
                "key text," +
                "value text," +
                "colType text," +
                "isEditable text," +
                "isVisible text" +
                ")";

        public static String TABLE_NAME = "PersonalMemberDetails";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static class Columns {
            public static String PROFILE_ID = "profileId",
                    FIELD_ID = "fieldId",
                    UNIQUE_KEY = "uniquekey",
                    KEY = "key",
                    VALUE = "value",
                    COL_TYPE = "colType",
                    IS_EDITABLE = "isEditable",
                    IS_VISIBLE = "isVisible";
        }


    }

    public static class BusinessMemberDetails {
        public final static String CREATE_TABLE = "create table BusinessMemberDetails" +
                "(" +
                "_id integer primary key autoincrement," +
                "profileId integer," +
                "fieldId integer," +
                "uniquekey text," +
                "key text," +
                "value text," +
                "colType text," +
                "isEditable text," +
                "isVisible text" +
                ")";

        public static String TABLE_NAME = "BusinessMemberDetails";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static class Columns {
            public static final String PROFILE_ID = "profileId",
                    FIELD_ID = "fieldId",
                    UNIQUE_KEY = "uniquekey",
                    KEY = "key",
                    VALUE = "value",
                    COL_TYPE = "colType",
                    IS_EDITABLE = "isEditable",
                    IS_VISIBLE = "isVisible";
        }

    }

    public static class FamilyMemberDetail {
        public static final String TABLE_NAME = "FamilyMemberDetail";
        public static final String CREATE_TABLE = "create table FamilyMemberDetail" +
                "(" +
                "_id integer primary key autoincrement," +
                "profileId integer," +
                "familyMemberId integer," +
                "memberName text," +
                "relationship text," +
                "dob text," +
                "emailID text," +
                "anniversary text," +
                "contactNo text," +
                "particulars text," +
                "bloodGroup text," +
                "countryId text" +
                ")";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static class Columns {


            public static final String PROFILE_ID = "profileId",
                    FAMILY_MEMBER_ID = "familyMemberId",
                    MEMBER_NAME = "memberName",
                    RELATIONSHIP = "relationship",
                    DOB = "dob",
                    EMAIL_ID = "emailID",
                    ANNIVERSARY = "anniversary",
                    CONTACT_NO = "contactNo",
                    PARTICULARS = "particulars",
                    BLOOD_GROUP = "bloodGroup",
                    COUNTRY_ID = "countryId";
        }
    }

    public static class AddressDetails {
        public static final String TABLE_NAME = "AddressDetails";
        public static final String CREATE_TABEL = "create table AddressDetails" +
                "(" +
                "_id integer primary key autoincrement," +
                "profileId integer," +
                "addressID integer," +
                "addressType text," +
                "address text," +
                "city text," +
                "state text," +
                "country text," +
                "pincode text," +
                "phoneNo text," +
                "fax text" +
                ")";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static class Columns {
            public static final String
                    PROFILE_ID = "profileId",
                    ADDRESS_ID = "addressID",
                    ADDRESS_TYPE = "addressType",
                    ADDRESS = "address",
                    CITY = "city",
                    STATE = "state",
                    COUNTRY = "country",
                    PINCODE = "pincode",
                    PHONE_NO = "phoneNo",
                    FAX = "fax";
        }
    }
    //---------------------------- End of Tables related to Profile ----------------

    //--------Directory data Master ----------------

    public static class DirectoryDataMaster {
        public static final String TABLE_NAME = "directory_data_master",
                CREATE_TABLE = "create table directory_data_master" +
                        "(" +
                        "_id integer primary key," +
                        "masterUID integer," +
                        "grpId integer," +
                        "memberMasterUID integer," +
                        "profileID integer," +
                        "groupName text," +
                        "memberName text," +
                        "pic text," +
                        "membermobile text," +
                        "grpCount integer," +
                        "moduleId text" +
                        ")";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static class Columns {
            public static final String _ID = "_id",
                    MASTER_UID = "masterUID",
                    GROUP_ID = "grpId",
                    MEMBER_MASTER_UID = "memberMasterUID",
                    DIRECTORY_PROFILE_ID = "profileID",
                    DIRECTORY_GROUP_NAME = "groupName",
                    DIRECTORY_MEMBER_NAME = "memberName",
                    DIRECTOTY_PIC = "pic",
                    DIRECTORY_MEMBER_MOBILE = "membermobile",
                    DIRECTORY_GROUP_COUNT = "grpCount",
                    MODULE_ID = "moduleId";
        }

        //public static final String UPGRADE_TO_V2 = "alter table "+DirectoryDataMaster.TABLE_NAME+" add column memberMasterUID integer";
    }

    //---------------------Announcement data master ---------------

    public static class AnnouncementDataMaster {

        public static final String TABLE_NAME = "announcement_data_master",
                CREATE_TABLE = "create table announcement_data_master" +
                        "(" +
                        "_id integer primary key," +
                        "masterUID integer," +
                        "announID integer," +
                        "announTitle text," +
                        "announceDEsc text," +
                        "createDateTime text," +
                        "publishDateTime text," +
                        "expiryDateTime text," +
                        "isAdmin text," +
                        "filterType text," +
                        "isRead text," +
                        "moduleId text" +

                        ")";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static class Columns {
            public static final String _ID = "_id",
                    MASTER_UID = "masterUID",
                    ANNOUNCE_ID = "announID",
                    ANNOUNCE_TITLE = "announTitle",
                    ANNOUNCE_DESC = "announceDEsc",
                    ANNOUNCE_CREATE_DATE_TIME = "createDateTime",
                    ANNOUNCE_PUBLISH_DATE_TIME = "publishDateTime",
                    ANNOUNCE_EXPIRY_DATE_TIME = "expiryDateTime",
                    ANNOUNCE_ISADMIN = "isAdmin",
                    ANNOUNCE_FILTER_TYPE = "filterType",
                    ANNOUNCE_ISREAD = "isRead",
                    MODULE_ID = "moduelId";
        }

    }

    //-------------------------Ebulletin data Master---------------

    public static class EbulletineDataMaster {

        public static final String TABLE_NAME = "ebulletine_data_master",
                CREATE_TABLE = "create table ebulletine_data_master" +
                        "(" +
                        "_id integer primary key," +
                        "masterUID integer," +
                        "ebulletinID integer," +
                        "ebulletinTitle text," +
                        "ebulletinlink text," +
                        "ebulletinType text," +
                        "filterType text," +
                        "createDateTime text," +
                        "publishDateTime text," +
                        "expiryDateTime text," +
                        "isAdmin text," +
                        "isRead text," +
                        "moduleId text" +

                        ")";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static class Columns {
            public static final String _ID = "_id",
                    MASTER_UID = "masterUID",
                    EBULLETIN_ID = "ebulletinID",
                    EBULLETIN_TITLE = "ebulletinTitle",
                    EBULLETIN_LINK = "ebulletinlink",
                    EBULLETIN_TYPE = "ebulletinType",
                    EBULLETIN_FILTER_TYPE = "filterType",
                    EBULLETIN_CREATE_DATE_TIME = "createDateTime",
                    EBULLETIN_PUBLISH_DATE = "publishDateTime",
                    EBULLETIN_EXPIRY_DATE = "expiryDateTime",
                    EBULLETIN_ISADMIN = "isAdmin",
                    EBULLETIN_ISREAD = "isRead",
                    MODULE_ID = "moduleId";
        }

    }

    //--------------------------------
    public static class EventDataMaster {
        public static final String TABLE_NAME = "event_data_master",
                CREATE_TABLE = "create table event_data_master" +
                        "(" +
                        "_id integer primary key," +
                        "masterUID integer," +
                        "eventID integer," +
                        "eventImg text," +
                        "eventTitle text," +
                        "eventDateTime text," +
                        "goingCount integer," +
                        "maybeCount integer," +
                        "notgoingCount integer," +
                        "venue text," +
                        "myResponse text," +
                        "filterType text," +

                        "grpID integer," +
                        "grpAdminId integer," +
                        "isRead text," +
                        "venueLat text," +
                        "venueLon text," +


                        ")";

        public static class Columns {
            public static final String _ID = "_id",
                    MASTER_UID = "masterUID",
                    EBULLETIN_ID = "eventID",
                    EBULLETIN_TITLE = "eventImg",
                    EBULLETIN_LINK = "eventTitle",
                    EBULLETIN_TYPE = "eventDateTime",
                    EBULLETIN_FILTER_TYPE = "goingCount",
                    EBULLETIN_CREATE_DATE_TIME = "createDateTime",
                    EBULLETIN_PUBLISH_DATE = "publishDateTime",
                    EBULLETIN_EXPIRY_DATE = "expiryDateTime",
                    EBULLETIN_ISADMIN = "isAdmin",
                    EBULLETIN_ISREAD = "isRead";
        }

    }

    //--------------------------Document Data Master----------------------

    public static class DocumentDataMaster {

        public static final String TABLE_NAME = "document_data_master",
                CREATE_TABLE = "create table document_data_master" +
                        "(" +
                        "_id integer primary key," +
                        "masterUID integer," +
                        "docID integer," +
                        "docTitle text," +
                        "docType text," +
                        "docURL text," +
                        "createDateTime text," +
                        "moduleId text," +
                        "accessType text," +
                        "isRead text" +
                        ")";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static class Columns {
            public static final String _ID = "_id",
                    MASTER_UID = "masterUID",
                    DOCUMENT_ID = "docID",
                    DOCUMENT_TITLE = "docTitle",
                    DOCUMENT_TYPE = "docType",
                    DOCUMENT_URL = "docURL",
                    DOCUMENT_CREATE_DATE_TIME = "createDateTime",
                    MODULE_ID = "moduleId",
                    ACCESS_TYPE = "accessType",
                    IS_READ = "isRead";


        }

    }


    // Tables for past President starts

    public static class PastPresidentMaster {
        public static final String TABLE_NAME = "PastPresidentMaster",

        DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME,

        CREATE_TABLE = "create table PastPresidentMaster" +
                "(" +
                "_id integer primary key," +
                "pastPresidentId integer," +
                "tenureYear text," +
                "photopath text," +
                "groupId integer," +
                "memberName text" +
                ")";


        public static class Columns {
            public static final String _ID = "_id",
                    PASTPRESIDENT_ID = "pastPresidentId",
                    TENURE = "tenureYear",
                    PHOTO_PATH = "photopath",
                    GROUP_ID = "groupId",
                    MEMBERNAME = "memberName";
        }
    }

    /********************Feeds & Blogs********************/
    public static class RotaryFeeds {
        public static final String TABLE_NAME = "RotaryFeeds",

        DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME,

        CREATE_TABLE = "create table RotaryFeeds" +
                "(" +
                "_id integer primary key," +
                "title text," +
                "link text," +
                "publishdate text," +
                "description text" +
                ")";


        public static class Columns {
            public static final String _ID = "_id",
                    TITLE = "title",
                    LINK = "link",
                    PUBLISHDATE = "publishdate",
                    DESCRIPTION = "description";
        }
    }

    // Table for Rotary Blogs

    public static class RotaryBlogs {
        public static final String TABLE_NAME = "RotaryBlogs",

        DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME,

        CREATE_TABLE = "create table RotaryBlogs" +
                "(" +
                "_id integer primary key," +
                "title text," +
                "link text," +
                "publishdate text," +
                "description text" +
                ")";


        public static class Columns {
            public static final String _ID = "_id",
                    TITLE = "title",
                    LINK = "link",
                    PUBLISHDATE = "publishdate",
                    DESCRIPTION = "description";
        }
    }


}

