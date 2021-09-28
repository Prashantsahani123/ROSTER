package com.NEWROW.row.Data;

import java.io.Serializable;

/**
 * Created by user on 06-09-2016.
 */
public class AlbumData implements Serializable {

    String albumId;
    String title;
    String description;
    String image;
    String grpId;
    String isAdmin;
    String OnetimeOrOngoing;

    //add by prashant sahani
    String TtlOfNewOngoingProj;
    String SubCount;
    String moderator_status;
    public String getModerator_status() {
        return moderator_status;
    }

    public void setModerator_status(String moderator_status) {
        this.moderator_status = moderator_status;
    }




    public String getSubCount() {
        return SubCount;
    }

    public void setSubCount(String subCount) {
        SubCount = subCount;
    }


    public String getTtlOfNewOngoingProj() {
        return TtlOfNewOngoingProj;
    }

    public void setTtlOfNewOngoingProj(String ttlOfNewOngoingProj) {
        TtlOfNewOngoingProj = ttlOfNewOngoingProj;
    }




    public String getOnetimeOrOngoing() {
        return OnetimeOrOngoing;
    }

    public void setOnetimeOrOngoing(String onetimeOrOngoing) {
        OnetimeOrOngoing = onetimeOrOngoing;
    }




    //this is for show select text
    String select="select";

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getPk_subcategoryIdupdate() {
        return pk_subcategoryIdupdate;
    }

    public void setPk_subcategoryIdupdate(String pk_subcategoryIdupdate) {
        this.pk_subcategoryIdupdate = pk_subcategoryIdupdate;
    }

    public String getFk_CategoryIDupdate() {
        return fk_CategoryIDupdate;
    }

    public void setFk_CategoryIDupdate(String fk_CategoryIDupdate) {
        this.fk_CategoryIDupdate = fk_CategoryIDupdate;
    }

    public String getSubcategoryNameupdate() {
        return SubcategoryNameupdate;
    }

    public void setSubcategoryNameupdate(String subcategoryNameupdate) {
        SubcategoryNameupdate = subcategoryNameupdate;
    }

    String moduleId;
    String cat_id,cat_name;
    String pk_subcategoryId,fk_CategoryID,SubcategoryName;
    String pk_subcategoryIdupdate,fk_CategoryIDupdate,SubcategoryNameupdate;
    String pk_subtosubcategoryId,fk_subcategoryid,fk_categoryID,subtosubcategoryname;
    String pk_subtosubcategoryIdupdate,fk_subcategoryidupdate,fk_categoryIDupdate,subtosubcategorynameupdate;
    boolean isSelected;

    public String getPk_subtosubcategoryIdupdate() {
        return pk_subtosubcategoryIdupdate;
    }

    public void setPk_subtosubcategoryIdupdate(String pk_subtosubcategoryIdupdate) {
        this.pk_subtosubcategoryIdupdate = pk_subtosubcategoryIdupdate;
    }

    public String getFk_subcategoryidupdate() {
        return fk_subcategoryidupdate;
    }

    public void setFk_subcategoryidupdate(String fk_subcategoryidupdate) {
        this.fk_subcategoryidupdate = fk_subcategoryidupdate;
    }

    public String getFk_categoryIDupdate() {
        return fk_categoryIDupdate;
    }

    public void setFk_categoryIDupdate(String fk_categoryIDupdate) {
        this.fk_categoryIDupdate = fk_categoryIDupdate;
    }

    public String getSubtosubcategorynameupdate() {
        return subtosubcategorynameupdate;
    }

    public void setSubtosubcategorynameupdate(String subtosubcategorynameupdate) {
        this.subtosubcategorynameupdate = subtosubcategorynameupdate;
    }

    String district_id,district_Name,club_id,club_Name,shareType;
    String project_date,project_cost,beneficiary,working_hour,working_hour_type,cost_of_project_type,noOfRotarians,rotractors;
    String serviceType,attendance,attendancePer,meetType,agendaDocID,momDocID;

    public String getRotractors() {
        return rotractors;
    }

    public void setRotractors(String rotractors) {
        this.rotractors = rotractors;
    }

    public String getPk_subtosubcategoryId() {
        return pk_subtosubcategoryId;
    }

    public void setPk_subtosubcategoryId(String pk_subtosubcategoryId) {
        this.pk_subtosubcategoryId = pk_subtosubcategoryId;
    }

    public String getFk_subcategoryid() {
        return fk_subcategoryid;
    }

    public void setFk_subcategoryid(String fk_subcategoryid) {
        this.fk_subcategoryid = fk_subcategoryid;
    }

    public String getFk_categoryID() {
        return fk_categoryID;
    }

    public void setFk_categoryID(String fk_categoryID) {
        this.fk_categoryID = fk_categoryID;
    }

    public String getSubtosubcategoryname() {
        return subtosubcategoryname;
    }

    public void setSubtosubcategoryname(String subtosubcategoryname) {
        this.subtosubcategoryname = subtosubcategoryname;
    }

    public AlbumData(){

    }

    public AlbumData(String albumId, String title, String description, String image, String grpId, String isAdmin, String moduleId){
        this.albumId = albumId;
        this.title = title;
        this.description = description;
        this.image = image;
        this.grpId = grpId;
        this.isAdmin = isAdmin;
        this.moduleId = moduleId;
    }

    public AlbumData(String albumId, String title, String description, String image, String grpId, String isAdmin, String moduleId, String district_id, String district_Name, String club_Name, String project_date, String project_cost, String beneficiary, String working_hour, String working_hour_type, String cost_of_project_type) {
        this.albumId = albumId;
        this.title = title;
        this.description = description;
        this.image = image;
        this.grpId = grpId;
        this.isAdmin = isAdmin;
        this.moduleId = moduleId;
        this.district_id = district_id;
        this.district_Name = district_Name;
        this.club_Name = club_Name;
        this.project_date = project_date;
        this.project_cost = project_cost;
        this.beneficiary = beneficiary;
        this.working_hour = working_hour;
        this.working_hour_type = working_hour_type;
        this.cost_of_project_type = cost_of_project_type;
    }

    public AlbumData(String albumId, String title, String description, String image, String grpId, String isAdmin, String moduleId, String cat_id, String cat_name, boolean isSelected, String district_id, String district_Name, String club_id, String club_Name, String project_date, String project_cost, String beneficiary, String working_hour, String working_hour_type, String cost_of_project_type, String noOfRotarians,String rotractors) {
        this.albumId = albumId;
        this.title = title;
        this.description = description;
        this.image = image;
        this.grpId = grpId;
        this.isAdmin = isAdmin;
        this.moduleId = moduleId;
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.isSelected = isSelected;
        this.district_id = district_id;
        this.district_Name = district_Name;
        this.club_id = club_id;
        this.club_Name = club_Name;
        this.project_date = project_date;
        this.project_cost = project_cost;
        this.beneficiary = beneficiary;
        this.working_hour = working_hour;
        this.working_hour_type = working_hour_type;
        this.cost_of_project_type = cost_of_project_type;
        this.noOfRotarians = noOfRotarians;
        this.rotractors = rotractors;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getNoOfRotarians() {
        return noOfRotarians;
    }

    public void setNoOfRotarians(String noOfRotarians) {
        this.noOfRotarians = noOfRotarians;
    }

    public String getProject_date() {
        return project_date;
    }

    public void setProject_date(String project_date) {
        this.project_date = project_date;
    }

    public String getProject_cost() {
        return project_cost;
    }

    public void setProject_cost(String project_cost) {
        this.project_cost = project_cost;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getWorking_hour() {
        return working_hour;
    }

    public void setWorking_hour(String working_hour) {
        this.working_hour = working_hour;
    }

    public String getWorking_hour_type() {
        return working_hour_type;
    }

    public void setWorking_hour_type(String working_hour_type) {
        this.working_hour_type = working_hour_type;
    }

    public String getCost_of_project_type() {
        return cost_of_project_type;
    }

    public void setCost_of_project_type(String cost_of_project_type) {
        this.cost_of_project_type = cost_of_project_type;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getDistrict_Name() {
        return district_Name;
    }

    public void setDistrict_Name(String district_Name) {
        this.district_Name = district_Name;
    }

    public String getClub_id() {
        return club_id;
    }

    public void setClub_id(String club_id) {
        this.club_id = club_id;
    }

    public String getClub_Name() {
        return club_Name;
    }

    public void setClub_Name(String club_Name) {
        this.club_Name = club_Name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getAlbumId() {
        return albumId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getGrpId() {
        return grpId;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setGrpId(String grpId) {
        this.grpId = grpId;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getAttendancePer() {
        return attendancePer;
    }

    public void setAttendancePer(String attendancePer) {
        this.attendancePer = attendancePer;
    }

    public String getMeetType() {
        return meetType;
    }

    public void setMeetType(String meetType) {
        this.meetType = meetType;
    }

    public String getAgendaDocID() {
        return agendaDocID;
    }

    public void setAgendaDocID(String agendaDocID) {
        this.agendaDocID = agendaDocID;
    }

    public String getMomDocID() {
        return momDocID;
    }

    public void setMomDocID(String momDocID) {
        this.momDocID = momDocID;
    }

    public String getPk_subcategoryId() {
        return pk_subcategoryId;
    }

    public void setPk_subcategoryId(String pk_subcategoryId) {
        this.pk_subcategoryId = pk_subcategoryId;
    }

    public String getFk_CategoryID() {
        return fk_CategoryID;
    }

    public void setFk_CategoryID(String fk_CategoryID) {
        this.fk_CategoryID = fk_CategoryID;
    }

    public String getSubcategoryName() {
        return SubcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        SubcategoryName = subcategoryName;
    }
}
