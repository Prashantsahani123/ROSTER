package com.SampleApp.row.Utils;

/**
 * Created by user on 14-01-2016.
 */
public class Constant {

    //Live Url
    //public final static String BaseURL = "http://version.touchbase.in:8065/v7/api/";      // test server
    //public final static String BaseURL_V2 = "http://version.touchbase.in/V2/api/";   // test server

    //Test Url

    //public final static String BaseURL = "http://servicestest.touchbase.in/v7/api/";// test server
    //public final static String BaseURL_V2 = "http://servicestest.touchbase.in/V2/api/";   // test server


    //ROW test server url

    public static final String  BaseURL="http://rowapi.rosteronwheels.com/V2/api/";
    //public static final String  BaseURL = "http://rowtestapi.rosteronwheels.com/V2/api/";
    public final static String BaseURL_V2 = "http://rowapi.rosteronwheels.com/V1/api/";

//    public final static String BaseURL = "http://rowtestapi.touchbase.in/V1/api/";
//    public final static String BaseURL_V2 = "http://rowtestapi.touchbase.in/V1/api";


    //public final static String INVITE_BASE_URL = "http://servicestest.touchbase.in/TouchBaseInvite/TouchbaseGroup.aspx?GroupID=";
    //public final static String INVITE_BASE_URL = "http://rowapi.rosteronwheels.com/TouchBaseInvite/TouchbaseGroup.aspx?Invite=";

    public final static String INVITE_BASE_URL = "http://rowapi.rosteronwheels.com/InviteMember/Register.aspx?Invite=";

    public final static String versionNo = "2.3";

    public final static String UserLogin = BaseURL + "Login/UserLogin";
    public final static String  PostOTP = BaseURL + "Login/PostOTP";
    public final static String GetWelcomeScreen = BaseURL + "Login/GetWelcomeScreen";
    public final static String GetMemberDetails = BaseURL + "Login/GetMemberDetails";

    public final static String UpdateProfile = BaseURL + "Member/UpdateProfile";

    public final static String GetGroupModulesList = BaseURL + "Group/GetGroupModulesList";
    public final static String GetAllGroupsList = BaseURL + "Group/GetAllGroupsList";

    public final static String UpdateMemberGroupCategory = BaseURL + "Group/UpdateMemberGroupCategory";
    public final static String RemoveGroupCategory = BaseURL + "Group/RemoveGroupCategory";

    public final static String CreateGroup = BaseURL + "Group/CreateGroup";
    public final static String GetAllCountriesAndCategories = BaseURL + "Group/GetAllCountriesAndCategories";
    public final static String AddSelectedModule = BaseURL + "Group/AddSelectedModule";

    //public final static String CreateSubGroup = BaseURL_V2 + "Group/CreateSubGroup";
    public final static String CreateSubGroup = BaseURL + "Group/CreateSubGroup";

    public final static String GetModulesList = BaseURL + "Group/GetModulesList";
    public final static String GetSubGroupListV2 = BaseURL_V2 + "Group/GetSubGroupList";
    public final static String GetSubGroupList = BaseURL + "Group/GetSubGroupList";
    public final static String GetSubGrpDirectoryList = BaseURL + "SubGroupDirectory/GetSubGrpDirectoryList";

    public final static String GetDirectoryList = BaseURL + "Member/GetDirectoryList";

    public final static String GetEventList = BaseURL + "Event/GetEventList";

    public final static String GetAnnouncementList = BaseURL + "Announcement/GetAnnouncementList";
    public final static String GetAnnouncementDetails = BaseURL + "Announcement/GetAnnouncementDetails";
    public final static String AddAnnouncement = BaseURL + "Announcement/AddAnnouncement";

    public final static String GetEventDetails = BaseURL + "Event/GetEventDetails";
    public final static String AddEvent = BaseURL + "Event/AddEvent";

    public final static String GetMember = BaseURL + "Member/GetMember";
    public final static String GetEbulletinList = BaseURL + "Ebulletin/GetEbulletinList";
    public final static String AddEbulletin = BaseURL + "Ebulletin/AddEbulletin";

    public final static String GetSubGroupDetail = BaseURL + "Group/GetSubGroupDetail";

    public final static String GlobalSearchGroup = BaseURL + "Group/GlobalSearchGroup";

    public final static String UploadImage = BaseURL + "upload/UploadImage?module=";
    public final static String UploadProfilePhoto = BaseURL + "Member/UploadProfilePhoto?";

    public final static String GetGroupDetail = BaseURL + "Group/GetGroupDetail";
    public final static String AnsweringEvent = BaseURL + "Event/AnsweringEvent";
    public final static String UpdateProfilePersonalDetails = BaseURL + "Member/UpdateProfilePersonalDetails";
    public final static String UpdateProfileBusinessDetails = BaseURL + "Member/UpdateProfileBusinessDetails";
    public final static String UpdateAddressDetails = BaseURL + "Member/UpdateAddressDetails";

    public final static String UploadAllDocs = BaseURL + "upload/UploadAllDocs?module=";

    public final static String GetDocumentList = BaseURL + "DocumentSafe/GetDocumentList";

    public final static String AddDocument = BaseURL + "DocumentSafe/AddDocument";

    public final static String AddMemberToGroup = BaseURL + "Group/AddMemberToGroup";

    public final static String UpdateFamilyDetails = BaseURL + "Member/UpdateFamilyDetails";

    public final static String GetTouchbaseSetting = BaseURL + "setting/GetTouchbaseSetting";
    public final static String TouchbaseSetting = BaseURL + "setting/TouchbaseSetting"; //Update

    public final static String GetGroupSetting = BaseURL + "setting/GetGroupSetting";
    public final static String GroupSetting = BaseURL + "setting/GroupSetting"; //Update

    ///http://services.touchbase.in:8060/api/Group/DeleteByModuleName
    public final static String DeleteByModuleName = BaseURL + "Group/DeleteByModuleName"; // Event / Announcement / Ebulletin / Document / Member/FamilyMember

    public final static String DeleteImage = BaseURL + "Group/DeleteImage"; // type : Event /Announcement /Member /Group

    public final static String GetGroupInfo = BaseURL + "group/GetGroupInfo";


    public final static String SuggestFeature = BaseURL + "Group/SuggestFeature";
    public final static String DeleteEntity = BaseURL + "group/DeleteEntity";

    public final static String GetEntityInfo =BaseURL+"Group/GetEntityInfo";

    public final static String AddMultipleMemberToGroup =BaseURL+"Group/AddMultipleMemberToGroup";

    //====================== Read Flag of Ebulletine and Document for Noticfication Count =====================

    public final static String GetReadFlag =BaseURL+"Ebulletin/GetEbulletinDetails";
    public final static String UpdateDocumentIsRead =BaseURL+"DocumentSafe/UpdateDocumentIsRead";


    //===================== Notification Count ===========================

    public final static String GetNotificationCount =BaseURL+"Group/GetNotificationCount";

    //==================== Offline =======================================

    public final static String GetDirectoryListSync =BaseURL+"OfflineData/GetDirectoryListSync";
    public final static String GetServiceDirectoryList =BaseURL+"ServiceDirectory/GetServiceDirectoryList";
    public final static String GetGetAllGroupListSync =BaseURL+"Group/GetAllGroupListSync";
    public final static String GROUP_DATA_LOADED = "TOUCHBASE.GROUPDATA.LOADED";

  /*  {"masterUID":"1", "grpId":"174","updatedOn":"2016-01-01 10:00:00"}*/

    public final static String AddServiceDirectory =BaseURL+"ServiceDirectory/AddServiceDirectory";

    public final static String GetServiceDirectoryDetails =BaseURL+"ServiceDirectory/GetServiceDirectoryDetails";

    public final static String GetServiceDirectoryListSync =BaseURL+"OfflineData/GetServiceDirectoryListSync";


    public static final String LOCATION = "location";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";


    public static final String FULLCONTACT_HEADER_NAME = "X-FullContact-APIKey";
    public static final String FULLCONTACT_API_KEY_VALUE = "b7e55e0d284ba5d6";

    public static final String FULL_CONTACT_API = "https://api.fullcontact.com/v2/address/locationEnrichment.json?place=";


    //============================ Gallery ====================================

    public final static String AddAlbum = BaseURL + "/Gallery/AddUpdateAlbum";
    public final static String GetAllAlbumList = BaseURL + "/Gallery/GetAlbumsList";

    public final static String DeleteAlbum = BaseURL + "/Group/DeleteByModuleName";
    public final static String DeletePhoto = BaseURL + "/Gallery/DeleteAlbumPhoto";

    public final static String GetAlbumPhotoList = BaseURL + "/Gallery/GetAlbumPhotoList";
    public final static String AddUpdateAlbumPhoto = BaseURL + "/Gallery/AddUpdateAlbumPhoto";
    public final static String GetAlbumDetails = BaseURL + "Gallery/GetAlbumDetails";

    public final static String GetEmail = BaseURL + "Group/GetEmail";


    public final static String CHAT_USER_PREFIX = "touchbase_";

    //============================ IMPROVEMENT ================================
    public final static String GetImprovementList = BaseURL + "Improvement/GetImprovementList";
    public final static String GetImprovementDetails = BaseURL + "Improvement/GetImprovementDetails";
    public final static String AddImprovement = BaseURL + "Improvement/AddImprovement";


    //========================== Attendance ==================================

    public final static String GetAttendanceList = BaseURL + "Attendance/GetAttendanceList";

    //========================== Module Replica Info ==================================
    public final static String GetReplicaInfo = BaseURL + "Group/GetReplicaInfo";


    //========================== External Link Module link ==================================
    public final static String GET_EXTERNAL_LINK = BaseURL + "Group/GetExternalLink";

    public final static String FILTER_TYPE_ALL = "0", FILTER_TYPE_PUBLISHED = "1", FILTER_TYPE_UNPUBLISHED = "2", FILTER_TYPE_EXPIRED = "3";
    public static class Module {
        public final static String /*INFO = "10",
        SERVICE_DIRECTORY = "15",*/
        /*ATTENDANCE = "17",*/


        DIRECTORY = "1",
        EVENTS = "2",
        ANNOUNCEMENTS = "3",
        E_BULLETINS = "4",
        SUB_GROUPS = "5",
        CELEBRATIONS = "6",
        MEETINGS = "7",
        GALLERY = "8",
        DOCUMENTS = "9",
        INFO = "10",
        CHAT = "11",
        TASK = "12",
        TICKETING = "14",
        SERVICE_DIRECTORY = "15",
        FEEDBACK = "16",
        IMPROVEMENT = "18",
        ATTENDANCE = "17",
        FAQ = "19",
        VVIP = "20",
        PROGRAM_SCHEDULE = "21",
        EXTERNAL_LINK = "22",
        FIND_A_ROTARIAN = "27",
        BOARD_OF_DIRECTORS = "26",
        WEBLINKS = "28",
        PAST_PRESIDENTS = "29",
        NEAR_ME = "30",
        CLUBHISTORY = "31",
        ROTARY_LIBRARY = "32",
        DISTRICT_COMMITTEE = "33",
        CLUBS = "35";

    }

    public static final int REQUEST_DOC=100,REQUEST_EVENT=101, REQUEST_EBULLITION =102,REQUEST_ANNOUNCEMENT=103;
    public static final int REQUEST_DASHBOARD=104;

    // For google analytics
    public static final String ANALYTICS_TRACKER_ID = "UA-87969792-1";
    public static final int DISPATCH_PERIOD = 2000;
    // End of for google anaylitcs

    public class FilterTypes {
        public static final String FILTER_TYPE_ALL = "0",
                FILTER_TYPE_UNPUBLISHED = "1",
                FILTER_TYPE_PUBLISHED = "2",
                FILTER_TYPE_EXPIRED = "3";
    }

    public static class BroadcastMessages {
        public static final String COUNT_UPDATED = "kaizen.app.com.touchbase.count_updated";
    }

    //----------------------- Calendar/Celebrations-----------------------

    public static final String GET_CALENDAR_MONTH_EVENTS = BaseURL + "Celebrations/GetMonthEventList";
    public static final String GET_TODAYS_BIRTHDAY = BaseURL + "Celebrations/GetTodaysBirthday";

    public static String MODE_VIEW = "VIEW", MODE_DOWNLOAD = "DOWNLOAD";
    // profile Dynamic Fields api

    public final static String GetMemberDynamicFields = BaseURL + "Member/GetMemberWithDynamicFields";

    // service Dirctory api to get Cattegory
    public final static String GetServiceDirectoryCattegory = BaseURL + "ServiceDirectory/GetServiceDirectoryCategories";

    public final static String GetMemberListSync = BaseURL + "Member/GetMemberListSync";

    public final static int VOLLEY_MAX_REQUEST_TIMEOUT = 120000; // 120 seconds i.e. 2 minutes

    // ROW Related API

    // Api to register to ROW if member is not registered

    public static final String REGISTER = BaseURL + "/Login/Registration";

    // Find a Rotarian Api

    public static final String GETRotarianList = BaseURL + "FindRotarian/GetRotarianList";
    public static final String GETRotarianDetails = BaseURL + "FindRotarian/GetRotarianDetails";
    public static final String GETListOfBOD = BaseURL + "Member/GetBODList";
    public static final String GETWebLinkList = BaseURL + "WebLink/GetWebLinksList";

    // Api for Past President
    public static final String GETPastPresident = BaseURL + "PastPresidents/getPastPresidentsList";


    // Api for getting club History

    public static final String GETClubHistory = BaseURL + "Group/GetClubHistory";

    // Api for getting Rotary Library

    public static final String GETRotaryLibrary = BaseURL + "Group/GetRotaryLibraryData";


    // Api for getting my club details option menu dashboard

    public static final String GETClubDetails = BaseURL + "Group/GetClubDetails";

    // Api  for feedback

    public static final String FEEDBACK = BaseURL + "/Group/Feedback";

    // Api for Find a club Module

    public static final String AUTHENTICATION = "https://apiuat.rotary.org:8443/v1.1/authentication";
    public static final String GETCOUNTRIES = "https://apiuat.rotary.org:8443/v1.1/countries";


    public final static String UPDATE_FILE_NAME = "TBUpdateFileName";
    public static final String DELETE_ZIP_FILE = BaseURL + "Member/DeleteFolder";
    public static final String GET_ADVANCED_SEARCH_FIELDS = BaseURL + "Member/GetAdvanceSearchFilters";
    public final static int NORMAL_TEXT_SIZE = 16;
    public final static String NewUpdateProfile = BaseURL + "Member/UpdateProfileDetails";



    public static final String THEME_COLOR = "#00AEEF";
    public static final String LOADING_MESSAGE = "Downloading updates from cloud\nPlease wait...";
    public static final String GETCLUBLIST = BaseURL + "/FindClub/GetClubList";

    //API To Get Club Details
    public static final String GETCLUBDETAILS = BaseURL + "FindClub/GetClubDetails";
    public static final String GET_CLUB_MEMBERS = BaseURL + "FindClub/GetClubMembers";
    public static final String GET_CLUB_COMMUNICATION_COUNT = BaseURL + "FindClub/GetCommunicationCount";
    public static final int GROUP_CATEGORY_CLUB = 1, GROUP_CATEGORY_DT = 2, GROUP_CATEGORY_EVENT = 3;

    // API To Get Near Me Details
    public static final String GETCLUBS_NEARME = BaseURL + "/FindClub/GetClubsNearMe";
    public static final String DT_DIRECTORY = BaseURL + "District/GetDistrictMemberListSync";
    public static final String DT_GET_DISTRICT_COMMITTEE = BaseURL+"/District/GetDistrictCommittee";
    public static final String DT_GET_CLUBS = BaseURL + "District/GetClubs";
    public static final String DT_GET_EVENTS = BaseURL + "FindClub/GetPublicEventsList";
    public static final String DT_GET_NEWSLETTERS = BaseURL + "FindClub/GetPublicNewsletterList";
    public static final String DT_GET_ALBUMS = BaseURL +"FindClub/GetPublicAlbumsList";
    public static final String DT_CALENDAR_EVENT_DETAILS = BaseURL + "Celebrations/GetEventMinDetails";

}
