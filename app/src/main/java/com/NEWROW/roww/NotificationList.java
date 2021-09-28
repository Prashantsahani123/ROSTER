package com.NEWROW.row;

public class NotificationList {
    String event_title, event_description, date, time, count_notification,flag,messageId;
    //Extra added by Gaurav
    String event_id;
    String group_id;
    String mem_id;
    String isadmin;
    String fromnotification;
    String eventimg;
    String venue;
    String reglink;
    String eventdate;
    String rsvpenable;
    String goingcount;
    String maybecount;
    String notgoingcount;
    String totalcount;
    String myresponse;
    String questiontype;
    String questiontext;
    String questionid;
    String isquesenable;
    String option1;
    String option2;
    String entityname;
    String groupcategory;
    String type;
    String moduleid;
    String modulename;
    String grouptype;

    public NotificationList(String event_title, String event_description, String date, String time, String count_notification, String flag, String messageId, String event_id, String group_id, String mem_id, String isadmin, String fromnotification, String eventimg, String venue, String reglink, String eventdate, String rsvpenable, String goingcount, String maybecount, String notgoingcount, String totalcount, String myresponse, String questiontype, String questiontext, String questionid, String isquesenable, String option1, String option2, String entityname, String groupcategory, String type, String moduleid, String modulename, String grouptype, String expireddate) {
        this.event_title = event_title;
        this.event_description = event_description;
        this.date = date;
        this.time = time;
        this.count_notification = count_notification;
        this.flag = flag;
        this.messageId = messageId;
        this.event_id = event_id;
        this.group_id = group_id;
        this.mem_id = mem_id;
        this.isadmin = isadmin;
        this.fromnotification = fromnotification;
        this.eventimg = eventimg;
        this.venue = venue;
        this.reglink = reglink;
        this.eventdate = eventdate;
        this.rsvpenable = rsvpenable;
        this.goingcount = goingcount;
        this.maybecount = maybecount;
        this.notgoingcount = notgoingcount;
        this.totalcount = totalcount;
        this.myresponse = myresponse;
        this.questiontype = questiontype;
        this.questiontext = questiontext;
        this.questionid = questionid;
        this.isquesenable = isquesenable;
        this.option1 = option1;
        this.option2 = option2;
        this.entityname = entityname;
        this.groupcategory = groupcategory;
        this.type = type;
        this.moduleid = moduleid;
        this.modulename = modulename;
        this.grouptype = grouptype;
        this.expireddate = expireddate;
    }

    public String getGrouptype() {
        return grouptype;
    }

    public void setGrouptype(String grouptype) {
        this.grouptype = grouptype;
    }



    public String getModuleid() {
        return moduleid;
    }

    public void setModuleid(String moduleid) {
        this.moduleid = moduleid;
    }

    public String getModulename() {
        return modulename;
    }

    public void setModulename(String modulename) {
        this.modulename = modulename;
    }

    String expireddate;


    public String getCount_notification() {
        return count_notification;
    }

    public void setCount_notification(String count_notification) {
        this.count_notification = count_notification;
    }
/* public NotificationList(String event_title, String event_description, String date, String time, String messageId, String flag) {
        this.event_title = event_title;
        this.event_description = event_description;
        this.date = date;
        this.time = time;
        this.messageId = messageId;
        this.flag = flag;
    }*/

    public NotificationList(String event_title, String event_description, String date, String time, String count_notification, String flag, String messageId, String event_id, String group_id, String mem_id, String isadmin, String fromnotification, String eventimg, String venue, String reglink, String eventdate, String rsvpenable, String goingcount, String maybecount, String notgoingcount, String totalcount, String myresponse, String questiontype, String questiontext, String questionid, String isquesenable, String option1, String option2, String entityname, String groupcategory, String type, String expireddate) {
        this.event_title = event_title;
        this.event_description = event_description;
        this.date = date;
        this.time = time;
        this.count_notification = count_notification;
        this.flag = flag;
        this.messageId = messageId;
        this.event_id = event_id;
        this.group_id = group_id;
        this.mem_id = mem_id;
        this.isadmin = isadmin;
        this.fromnotification = fromnotification;
        this.eventimg = eventimg;
        this.venue = venue;
        this.reglink = reglink;
        this.eventdate = eventdate;
        this.rsvpenable = rsvpenable;
        this.goingcount = goingcount;
        this.maybecount = maybecount;
        this.notgoingcount = notgoingcount;
        this.totalcount = totalcount;
        this.myresponse = myresponse;
        this.questiontype = questiontype;
        this.questiontext = questiontext;
        this.questionid = questionid;
        this.isquesenable = isquesenable;
        this.option1 = option1;
        this.option2 = option2;
        this.entityname = entityname;
        this.groupcategory = groupcategory;
        this.type = type;
        this.expireddate = expireddate;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getMem_id() {
        return mem_id;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public String getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(String isadmin) {
        this.isadmin = isadmin;
    }

    public String getFromnotification() {
        return fromnotification;
    }

    public void setFromnotification(String fromnotification) {
        this.fromnotification = fromnotification;
    }

    public String getEventimg() {
        return eventimg;
    }

    public void setEventimg(String eventimg) {
        this.eventimg = eventimg;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getReglink() {
        return reglink;
    }

    public void setReglink(String reglink) {
        this.reglink = reglink;
    }

    public String getEventdate() {
        return eventdate;
    }

    public void setEventdate(String eventdate) {
        this.eventdate = eventdate;
    }

    public String getRsvpenable() {
        return rsvpenable;
    }

    public void setRsvpenable(String rsvpenable) {
        this.rsvpenable = rsvpenable;
    }

    public String getGoingcount() {
        return goingcount;
    }

    public void setGoingcount(String goingcount) {
        this.goingcount = goingcount;
    }

    public String getMaybecount() {
        return maybecount;
    }

    public void setMaybecount(String maybecount) {
        this.maybecount = maybecount;
    }

    public String getNotgoingcount() {
        return notgoingcount;
    }

    public void setNotgoingcount(String notgoingcount) {
        this.notgoingcount = notgoingcount;
    }

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public String getMyresponse() {
        return myresponse;
    }

    public void setMyresponse(String myresponse) {
        this.myresponse = myresponse;
    }

    public String getQuestiontype() {
        return questiontype;
    }

    public void setQuestiontype(String questiontype) {
        this.questiontype = questiontype;
    }

    public String getQuestiontext() {
        return questiontext;
    }

    public void setQuestiontext(String questiontext) {
        this.questiontext = questiontext;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    public String getIsquesenable() {
        return isquesenable;
    }

    public void setIsquesenable(String isquesenable) {
        this.isquesenable = isquesenable;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getEntityname() {
        return entityname;
    }

    public void setEntityname(String entityname) {
        this.entityname = entityname;
    }

    public String getGroupcategory() {
        return groupcategory;
    }

    public void setGroupcategory(String groupcategory) {
        this.groupcategory = groupcategory;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpireddate() {
        return expireddate;
    }

    public void setExpireddate(String expireddate) {
        this.expireddate = expireddate;
    }
}
