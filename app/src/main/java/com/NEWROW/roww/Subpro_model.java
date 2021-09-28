package com.NEWROW.row;

public class Subpro_model {

    String pk_gallery_id ;
    String fk_group_master_id;
    String date_of_project;
    String album_title ;
    String cost_of_project ;
    String beneficiary ;
    String man_hours_spent;
    String NumberOfRotarian;
    String Rotaractors ;
    String OnetimeOrOngoing ;
    String NewOrExisting ;
    String TtlOfNewOngoingProj;
    public String getTtlOfNewOngoingProj() {
        return TtlOfNewOngoingProj;
    }

    public void setTtlOfNewOngoingProj(String ttlOfNewOngoingProj) {
        TtlOfNewOngoingProj = ttlOfNewOngoingProj;
    }

   // String TtlOfNewOngoingProj;

    public String getPk_gallery_id() {
        return pk_gallery_id;
    }

    public void setPk_gallery_id(String pk_gallery_id) {
        this.pk_gallery_id = pk_gallery_id;
    }

    public String getFk_group_master_id() {
        return fk_group_master_id;
    }

    public void setFk_group_master_id(String fk_group_master_id) {
        this.fk_group_master_id = fk_group_master_id;
    }

    public String getDate_of_project() {
        return date_of_project;
    }

    public void setDate_of_project(String date_of_project) {
        this.date_of_project = date_of_project;
    }

    public String getAlbum_title() {
        return album_title;
    }

    public void setAlbum_title(String album_title) {
        this.album_title = album_title;
    }

    public String getCost_of_project() {
        return cost_of_project;
    }

    public void setCost_of_project(String cost_of_project) {
        this.cost_of_project = cost_of_project;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getMan_hours_spent() {
        return man_hours_spent;
    }

    public void setMan_hours_spent(String man_hours_spent) {
        this.man_hours_spent = man_hours_spent;
    }

    public String getNumberOfRotarian() {
        return NumberOfRotarian;
    }

    public void setNumberOfRotarian(String numberOfRotarian) {
        NumberOfRotarian = numberOfRotarian;
    }

    public String getRotaractors() {
        return Rotaractors;
    }

    public void setRotaractors(String rotaractors) {
        Rotaractors = rotaractors;
    }

    public String getOnetimeOrOngoing() {
        return OnetimeOrOngoing;
    }

    public void setOnetimeOrOngoing(String onetimeOrOngoing) {
        OnetimeOrOngoing = onetimeOrOngoing;
    }

    public String getNewOrExisting() {
        return NewOrExisting;
    }

    public void setNewOrExisting(String newOrExisting) {
        NewOrExisting = newOrExisting;
    }
}
