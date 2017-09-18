package com.SampleApp.row.Data;

/**
 * Created by USER on 02-02-2016.
 */
public class EventListData {

    String eventID,eventImg,eventTitle,eventDateTime,goingCount,maybeCount,notgoingCount,venue,myResponse,filterType,grpID,grpAdminId,isRead,venueLat,venueLon;

    public EventListData() {
    }

    public EventListData(String eventID, String eventImg, String eventTitle, String eventDateTime, String goingCount, String maybeCount, String notgoingCount, String venue, String myResponse, String filterType, String grpID, String grpAdminId, String isRead, String venueLat, String venueLon) {
        this.eventID = eventID;
        this.eventImg = eventImg;
        this.eventTitle = eventTitle;
        this.eventDateTime = eventDateTime;
        this.goingCount = goingCount;
        this.maybeCount = maybeCount;
        this.notgoingCount = notgoingCount;
        this.venue = venue;
        this.myResponse = myResponse;
        this.filterType = filterType;
        this.grpID = grpID;
        this.grpAdminId = grpAdminId;
        this.isRead = isRead;
        this.venueLat = venueLat;
        this.venueLon = venueLon;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventImg() {
        return eventImg;
    }

    public void setEventImg(String eventImg) {
        this.eventImg = eventImg;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(String eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getGoingCount() {
        return goingCount;
    }

    public void setGoingCount(String goingCount) {
        this.goingCount = goingCount;
    }

    public String getMaybeCount() {
        return maybeCount;
    }

    public void setMaybeCount(String maybeCount) {
        this.maybeCount = maybeCount;
    }

    public String getNotgoingCount() {
        return notgoingCount;
    }

    public void setNotgoingCount(String notgoingCount) {
        this.notgoingCount = notgoingCount;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getMyResponse() {
        return myResponse;
    }

    public void setMyResponse(String myResponse) {
        this.myResponse = myResponse;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getGrpID() {
        return grpID;
    }

    public void setGrpID(String grpID) {
        this.grpID = grpID;
    }

    public String getGrpAdminId() {
        return grpAdminId;
    }

    public void setGrpAdminId(String grpAdminId) {
        this.grpAdminId = grpAdminId;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getVenueLat() {
        return venueLat;
    }

    public void setVenueLat(String venueLat) {
        this.venueLat = venueLat;
    }

    public String getVenueLon() {
        return venueLon;
    }

    public void setVenueLon(String venueLon) {
        this.venueLon = venueLon;
    }

    @Override
    public String toString() {
        return "EventListData{" +
                "eventID='" + eventID + '\'' +
                ", eventImg='" + eventImg + '\'' +
                ", eventTitle='" + eventTitle + '\'' +
                ", eventDateTime='" + eventDateTime + '\'' +
                ", goingCount='" + goingCount + '\'' +
                ", maybeCount='" + maybeCount + '\'' +
                ", notgoingCount='" + notgoingCount + '\'' +
                ", venue='" + venue + '\'' +
                ", myResponse='" + myResponse + '\'' +
                ", filterType='" + filterType + '\'' +
                ", grpID='" + grpID + '\'' +
                ", grpAdminId='" + grpAdminId + '\'' +
                ", isRead='" + isRead + '\'' +
                ", venueLat='" + venueLat + '\'' +
                ", venueLon='" + venueLon + '\'' +
                '}';
    }
}
