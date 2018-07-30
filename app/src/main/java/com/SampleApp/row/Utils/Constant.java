package com.SampleApp.row.Utils;

import com.SampleApp.row.BuildConfig;

/**
 * Created by user on 14-01-2016.
 */
public class Constant {

    //Live Url
    //public final static String BaseURL_V3 = "http://version.touchbase.in:8065/v7/api/";      // test server
    //public final static String BaseURL_V3 = "http://version.touchbase.in/V2/api/";   // test server

    //Test Url

    //public final static String BaseURL_V3 = "http://servicestest.touchbase.in/v7/api/";// test server
    //public final static String BaseURL_V3 = "http://servicestest.touchbase.in/V2/api/";   // test server


    //ROW test server url
    public final static String TEST_URL="http://rowtestapi.rosteronwheels.com";
//    public final static String TEST_URL="http://rowapi.rosteronwheels.com";
    public final static String LIVE_URL="http://rowapi.rosteronwheels.com";

  //  public static final String  BaseURL_V3=TEST_URL+"/V2/api/";
    //public static final String  BaseURL_V3 = "http://rowtestapi.rosteronwheels.com/V2/api/";
  //  public final static String BaseURL_V3 = TEST_URL+"/V1/api/";

//    public final static String BaseURL_V3 = "http://rowapi.rosteronwheels.com/V3/api/";//Live URL

    public final static String BaseURL_V3 = LIVE_URL+"/V3/api/";

    public static final String GetMonthEventListTypeWise = BaseURL_V3 + "Celebrations/GetMonthEventListTypeWise";
    public static final String GetMonthEventListDetails = BaseURL_V3 + "Celebrations/GetMonthEventListDetails";
    public static final String GetYearWiseEbulletinList=BaseURL_V3+"Ebulletin/GetYearWiseEbulletinList";
    public final static String GetServiceCategoriesData = BaseURL_V3 + "ServiceDirectory/GetServiceCategoriesData";
//    public final static String BaseURL_V3 = "http://rowtestapi.touchbase.in/V1/api/";
//    public final static String BaseURL_V3 = "http://rowtestapi.touchbase.in/V1/api";


    //public final static String INVITE_BASE_URL = "http://servicestest.touchbase.in/TouchBaseInvite/TouchbaseGroup.aspx?GroupID=";
    //public final static String INVITE_BASE_URL = "http://rowapi.rosteronwheels.com/TouchBaseInvite/TouchbaseGroup.aspx?Invite=";

    public final static String INVITE_BASE_URL = "http://rowapi.rosteronwheels.com/InviteMember/Register.aspx?Invite=";

    public final static String versionNo = BuildConfig.VERSION_NAME;

    public final static String UserLogin = BaseURL_V3 + "Login/UserLogin";
    public final static String  PostOTP = BaseURL_V3 + "Login/PostOTP";
    public final static String GetWelcomeScreen = BaseURL_V3 + "Login/GetWelcomeScreen";
    public final static String GetMemberDetails = BaseURL_V3 + "Login/GetMemberDetails";

    public final static String UpdateProfile = BaseURL_V3 + "Member/UpdateProfile";

    public final static String GetGroupModulesList = BaseURL_V3 + "Group/GetGroupModulesList";

    public final static String GetAllGroupsList = BaseURL_V3 + "Group/GetAllGroupsList";

    public final static String UpdateMemberGroupCategory = BaseURL_V3 + "Group/UpdateMemberGroupCategory";
    public final static String RemoveGroupCategory = BaseURL_V3 + "Group/RemoveGroupCategory";

    public final static String CreateGroup = BaseURL_V3 + "Group/CreateGroup";
    public final static String GetAllCountriesAndCategories = BaseURL_V3 + "Group/GetAllCountriesAndCategories";
    public final static String AddSelectedModule = BaseURL_V3 + "Group/AddSelectedModule";

    //public final static String CreateSubGroup = BaseURL_V3 + "Group/CreateSubGroup";
    public final static String CreateSubGroup = BaseURL_V3 + "Group/CreateSubGroup";

    public final static String GetModulesList = BaseURL_V3 + "Group/GetModulesList";
    public final static String GetSubGroupListV2 = BaseURL_V3 + "Group/GetSubGroupList";
    public final static String GetSubGroupList = BaseURL_V3 + "Group/GetSubGroupList";
    public final static String GetSubGrpDirectoryList = BaseURL_V3 + "SubGroupDirectory/GetSubGrpDirectoryList";

    public final static String GetDirectoryList = BaseURL_V3 + "Member/GetDirectoryList";

    public final static String GetEventList = BaseURL_V3 + "Event/GetEventList";

    public final static String GetAnnouncementList = BaseURL_V3 + "Announcement/GetAnnouncementList";
    public final static String GetAnnouncementDetails = BaseURL_V3 + "Announcement/GetAnnouncementDetails";
    public final static String AddAnnouncement = BaseURL_V3 + "Announcement/AddAnnouncement";

    public final static String GetEventDetails = BaseURL_V3 + "Event/GetEventDetails";
    public final static String GetEventDetails_New = BaseURL_V3 + "Event/GetEventDetails_New";
//    public final static String AddEvent = BaseURL_V3 + "Event/AddEvent"; old
public final static String AddEvent = BaseURL_V3 + "Event/AddEvent_New";
    public final static String GetMember = BaseURL_V3 + "Member/GetMember";
    public final static String GetEbulletinList = BaseURL_V3 + "Ebulletin/GetEbulletinList";
    public final static String AddEbulletin = BaseURL_V3 + "Ebulletin/AddEbulletin";

    public final static String GetSubGroupDetail = BaseURL_V3 + "Group/GetSubGroupDetail";

    public final static String GlobalSearchGroup = BaseURL_V3 + "Group/GlobalSearchGroup";

    public final static String UploadImage = BaseURL_V3 + "upload/UploadImage?module=";
    public final static String UploadProfilePhoto = BaseURL_V3 + "Member/UploadProfilePhoto?";

    public final static String GetGroupDetail = BaseURL_V3 + "Group/GetGroupDetail";
    public final static String AnsweringEvent = BaseURL_V3 + "Event/AnsweringEvent";
    public final static String UpdateProfilePersonalDetails = BaseURL_V3 + "Member/UpdateProfilePersonalDetails";
    public final static String UpdateProfileBusinessDetails = BaseURL_V3 + "Member/UpdateProfileBusinessDetails";
    public final static String UpdateAddressDetails = BaseURL_V3 + "Member/UpdateAddressDetails";

    public final static String UploadAllDocs = BaseURL_V3 + "upload/UploadAllDocs?module=";

    public final static String GetDocumentList = BaseURL_V3 + "DocumentSafe/GetDocumentList";

    public final static String AddDocument = BaseURL_V3 + "DocumentSafe/AddDocument";

    public final static String AddMemberToGroup = BaseURL_V3 + "Group/AddMemberToGroup";

    public final static String UpdateFamilyDetails = BaseURL_V3 + "Member/UpdateFamilyDetails";

    public final static String GetTouchbaseSetting = BaseURL_V3 + "setting/GetTouchbaseSetting";
    public final static String TouchbaseSetting = BaseURL_V3 + "setting/TouchbaseSetting"; //Update

    public final static String GetGroupSetting = BaseURL_V3 + "setting/GetGroupSetting";
    public final static String GroupSetting = BaseURL_V3 + "setting/GroupSetting"; //Update

    ///http://services.touchbase.in:8060/api/Group/DeleteByModuleName
    public final static String DeleteByModuleName = BaseURL_V3 + "Group/DeleteByModuleName"; // Event / Announcement / Ebulletin / Document / Member/FamilyMember

    public final static String DeleteImage = BaseURL_V3 + "Group/DeleteImage"; // type : Event /Announcement /Member /Group

    public final static String GetGroupInfo = BaseURL_V3 + "group/GetGroupInfo";

    public final static String SuggestFeature = BaseURL_V3 + "Group/SuggestFeature";
    public final static String DeleteEntity = BaseURL_V3 + "group/DeleteEntity";

    public final static String GetEntityInfo =BaseURL_V3+"Group/GetEntityInfo";

    public final static String AddMultipleMemberToGroup =BaseURL_V3+"Group/AddMultipleMemberToGroup";

    //====================== Read Flag of Ebulletine and Document for Noticfication Count =====================

    public final static String GetReadFlag =BaseURL_V3+"Ebulletin/GetEbulletinDetails";
    public final static String UpdateDocumentIsRead =BaseURL_V3+"DocumentSafe/UpdateDocumentIsRead";


    //===================== Notification Count ===========================

    public final static String GetNotificationCount =BaseURL_V3+"Group/GetNotificationCount";

    //==================== Offline =======================================

    public final static String GetDirectoryListSync =BaseURL_V3+"OfflineData/GetDirectoryListSync";
    public final static String GetServiceDirectoryList =BaseURL_V3+"ServiceDirectory/GetServiceDirectoryList";
    public final static String GetGetAllGroupListSync =BaseURL_V3+"Group/GetAllGroupListSync";
    public final static String GROUP_DATA_LOADED = "TOUCHBASE.GROUPDATA.LOADED";
    //new api for online district
    public final static String GetDistrictMemberList=BaseURL_V3+"District/GetDistrictMemberList";
    //public final static String GetClassificationList=BaseURL_V3+"District/GetClassificationList";
    public final static String GetClassificationList=BaseURL_V3+"District/GetClassificationList_New";
    public final static String GetMemberByClassification=BaseURL_V3+"District/GetMemberByClassification";
    public final static String GetMemberWithDynamicFields=BaseURL_V3+"District/GetMemberWithDynamicFields";
  /*  {"masterUID":"1", "grpId":"174","updatedOn":"2016-01-01 10:00:00"}*/

    public final static String AddServiceDirectory =BaseURL_V3+"ServiceDirectory/AddServiceDirectory";

    public final static String GetServiceDirectoryDetails =BaseURL_V3+"ServiceDirectory/GetServiceDirectoryDetails";

    public final static String GetServiceDirectoryListSync =BaseURL_V3+"OfflineData/GetServiceDirectoryListSync";


    public static final String LOCATION = "location";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";


    public static final String FULLCONTACT_HEADER_NAME = "X-FullContact-APIKey";
    public static final String FULLCONTACT_API_KEY_VALUE = "b7e55e0d284ba5d6";

    public static final String FULL_CONTACT_API = "https://api.fullcontact.com/v2/address/locationEnrichment.json?place=";


    //============================ Gallery ====================================

    public final static String AddAlbum = BaseURL_V3 + "/Gallery/AddUpdateAlbum";
    public final static String GetAllAlbumList = BaseURL_V3 + "/Gallery/GetAlbumsList";

    public final static String DeleteAlbum = BaseURL_V3 + "/Group/DeleteByModuleName";
    public final static String DeletePhoto = BaseURL_V3 + "/Gallery/DeleteAlbumPhoto";

    public final static String GetAlbumPhotoList = BaseURL_V3 + "/Gallery/GetAlbumPhotoList";
    public final static String AddUpdateAlbumPhoto = BaseURL_V3 + "/Gallery/AddUpdateAlbumPhoto";
    public final static String GetAlbumDetails = BaseURL_V3 + "Gallery/GetAlbumDetails";

    public final static String GetEmail = BaseURL_V3 + "Group/GetEmail";


    public final static String CHAT_USER_PREFIX = "touchbase_";

    //============================ IMPROVEMENT ================================
    public final static String GetImprovementList = BaseURL_V3 + "Improvement/GetImprovementList";
    public final static String GetImprovementDetails = BaseURL_V3 + "Improvement/GetImprovementDetails";
    public final static String AddImprovement = BaseURL_V3 + "Improvement/AddImprovement";


    //========================== Attendance ==================================

    public final static String GetAttendanceList = BaseURL_V3 + "Attendance/GetAttendanceList";

    //========================== Module Replica Info ==================================
    public final static String GetReplicaInfo = BaseURL_V3 + "Group/GetReplicaInfo";


    //========================== External Link Module link ==================================
    public final static String GET_EXTERNAL_LINK = BaseURL_V3 + "Group/GetExternalLink";

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
        CLUBS = "35",
        CLUB_MONTHLY_REPORT = "38";
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

    public static final String GET_CALENDAR_MONTH_EVENTS = BaseURL_V3 + "Celebrations/GetMonthEventList";
    public static final String GET_TODAYS_BIRTHDAY = BaseURL_V3 + "Celebrations/GetTodaysBirthday";

    public static String MODE_VIEW = "VIEW", MODE_DOWNLOAD = "DOWNLOAD";
    // profile Dynamic Fields api

    public final static String GetMemberDynamicFields = BaseURL_V3 + "Member/GetMemberWithDynamicFields";

    // service Dirctory api to get Cattegory
    public final static String GetServiceDirectoryCattegory = BaseURL_V3 + "ServiceDirectory/GetServiceDirectoryCategories";

    public final static String GetMemberListSync = BaseURL_V3 + "Member/GetMemberListSync";

    public final static int VOLLEY_MAX_REQUEST_TIMEOUT = 120000; // 120 seconds i.e. 2 minutes

    // ROW Related API

    // Api to register to ROW if member is not registered

    public static final String REGISTER = BaseURL_V3 + "/Login/Registration";

    // Find a Rotarian Api

    public static final String GETRotarianList = BaseURL_V3 + "FindRotarian/GetRotarianList";
    public static final String GETRotarianDetails = BaseURL_V3 + "FindRotarian/GetRotarianDetails";
    public static final String GETListOfBOD = BaseURL_V3 + "Member/GetBODList";
    public static final String GETWebLinkList = BaseURL_V3 + "WebLink/GetWebLinksList";

    // Api for Past President
    public static final String GETPastPresident = BaseURL_V3 + "PastPresidents/getPastPresidentsList";


    // Api for getting club History

    public static final String GETClubHistory = BaseURL_V3 + "Group/GetClubHistory";

    // Api for getting Rotary Library

    public static final String GETRotaryLibrary = BaseURL_V3 + "Group/GetRotaryLibraryData";


    // Api for getting my club details option menu dashboard

    public static final String GETClubDetails = BaseURL_V3 + "Group/GetClubDetails";

    // Api  for feedback

    public static final String FEEDBACK = BaseURL_V3 + "/Group/Feedback";

    // Api for Find a club Module

    public static final String AUTHENTICATION = "https://apiuat.rotary.org:8443/v1.1/authentication";
    public static final String GETCOUNTRIES = "https://apiuat.rotary.org:8443/v1.1/countries";


    public final static String UPDATE_FILE_NAME = "TBUpdateFileName";
    public static final String DELETE_ZIP_FILE = BaseURL_V3 + "Member/DeleteFolder";
    public static final String GET_ADVANCED_SEARCH_FIELDS = BaseURL_V3 + "Member/GetAdvanceSearchFilters";
    public final static int NORMAL_TEXT_SIZE = 16;
    public final static String NewUpdateProfile = BaseURL_V3 + "Member/UpdateProfileDetails";



    public static final String THEME_COLOR = "#00AEEF";
    public static final String LOADING_MESSAGE = "Downloading updates from cloud\nPlease wait...";
    public static final String GETCLUBLIST = BaseURL_V3 + "/FindClub/GetClubList";

    //API To Get Club Details
    public static final String GETCLUBDETAILS = BaseURL_V3 + "FindClub/GetClubDetails";
    public static final String GET_CLUB_MEMBERS = BaseURL_V3 + "FindClub/GetClubMembers";
    public static final String GET_CLUB_COMMUNICATION_COUNT = BaseURL_V3 + "FindClub/GetCommunicationCount";
    public static final int GROUP_CATEGORY_CLUB = 1, GROUP_CATEGORY_DT = 2, GROUP_CATEGORY_EVENT = 3;

    // API To Get Near Me Details
    public static final String GETCLUBS_NEARME = BaseURL_V3 + "/FindClub/GetClubsNearMe";
    public static final String DT_DIRECTORY = BaseURL_V3 + "District/GetDistrictMemberListSync";
    public static final String DT_GET_DISTRICT_COMMITTEE = BaseURL_V3+"/District/GetDistrictCommittee";
    public static final String DT_GET_CLUBS = BaseURL_V3 + "District/GetClubs";
    public static final String DT_GET_EVENTS = BaseURL_V3 + "FindClub/GetPublicEventsList";
    public static final String DT_GET_NEWSLETTERS = BaseURL_V3 + "FindClub/GetPublicNewsletterList";
    public static final String DT_GET_ALBUMS = BaseURL_V3 +"FindClub/GetPublicAlbumsList";
    public static final String DT_CALENDAR_EVENT_DETAILS = BaseURL_V3 + "Celebrations/GetEventMinDetails";
    public static final String GetNewDashboard   = BaseURL_V3 + "Group/GetNewDashboard";
    public final static String GetEntityInfo1 =BaseURL_V3+"Group/GetEntityInfo";

    //ShowCase Rupesh
    public final static String GetShowcaseDetails =BaseURL_V3+"Gallery/GetShowcaseDetails";
    public final static String GetClassificationList_new=BaseURL_V3+"District/GetClassificationList_new";
    public final static String GetAlbumsList_New=BaseURL_V3+"Gallery/GetAlbumsList_New";
    public final static String AddUpdateAlbum_New = BaseURL_V3 + "Gallery/AddUpdateAlbum_New";
    public final static String GetAlbumPhotoList_New = BaseURL_V3 + "Gallery/GetAlbumPhotoList_New";
    public final static String GetAlbumDetails_New = BaseURL_V3 + "Gallery/GetAlbumDetails_New";
    public final static String districtCommitteeList = BaseURL_V3 + "DistrictCommittee/districtCommitteeList";
    public final static String districtCommitteeDetails = BaseURL_V3 + "DistrictCommittee/districtCommitteeDetails";
    public final static String districtCommitteeSearchList = BaseURL_V3 + "DistrictCommittee/districtCommitteeSearchList";
    // Monthly Report
    public final static String GetMonthlyReportList = BaseURL_V3 + "ClubMonthlyReport/GetMonthlyReportList";

    //Leader board details
    public final static String GetLeaderBoardDetail = BaseURL_V3 + "LeaderBoard/GetLeaderBoardDetail";

    //API for Attendance

    public static class Dependent {
        public final static String

                MEMBER = "1",
                ANNS = "2",
                ANNETS = "3",
                VISITORS = "4",
                ROTARIAN = "5",
                DELEGETS = "6";

    }
    public static final String GetAttendanceListNew = BaseURL_V3+"Attendance/GetAttendanceListNew";
    public static final String GetAttendanceDetails = BaseURL_V3+"Attendance/getAttendanceDetails";
    public static final String GetAttendanceMemberDetails = BaseURL_V3+"Attendance/getAttendanceMemberDetails";
    public static final String GetAttendanceAnnsDetails = BaseURL_V3+"Attendance/getAttendanceAnnsDetails";
    public static final String GetAttendanceAnnetsDetails = BaseURL_V3+"Attendance/getAttendanceAnnetsDetails";
    public static final String GetAttendanceVisitorsDetails = BaseURL_V3+"Attendance/getAttendanceVisitorsDetails";
    public static final String GetAttendanceRotariansDetails = BaseURL_V3+"Attendance/getAttendanceRotariansDetails";
    public static final String GetAttendanceDistrictDeleagateDetails = BaseURL_V3+"Attendance/getAttendanceDistrictDeleagateDetails";
    public static final String GetrotarianDetailsbyRotarianID=BaseURL_V3+"Attendance/GetrotarianDetailsbyRotarianID";
    public static final String AttendanceAddEdit=BaseURL_V3+"Attendance/AttendanceAddEdit";
    public static final String GetAttendanceEventsListNew =BaseURL_V3+"Attendance/GetAttendanceEventsListNew";
    public static final String AttendanceDelete =BaseURL_V3+"Attendance/AttendanceDelete";

}
