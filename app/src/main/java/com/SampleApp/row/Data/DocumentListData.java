package com.SampleApp.row.Data;

/**
 * Created by user on 22-03-2016.
 */
public class DocumentListData {

    String docID,docTitle,docType,docURL,createDateTime,accessType,isRead,filterType;

    public DocumentListData() {
    }

    public DocumentListData(String docID, String docTitle, String docType, String docURL, String createDateTime, String accessType, String isRead,String filterType) {
        this.docID = docID;
        this.docTitle = docTitle;
        this.docType = docType;
        this.docURL = docURL;
        this.createDateTime = createDateTime;
        this.accessType = accessType;
        this.isRead = isRead;
        this.filterType = filterType;
    }


    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getDocID() {
        return docID;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public String getDocType() {
        return docType;
    }

    public String getDocURL() {
        return docURL;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String setDocURL(String docURL) {
        this.docURL = docURL;
        return docURL;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }


    @Override
    public String toString() {
        return "DocumentListData{" +
                "docID='" + docID + '\'' +
                ", docTitle='" + docTitle + '\'' +
                ", docType='" + docType + '\'' +
                ", docURL='" + docURL + '\'' +
                ", docURL='" + createDateTime + '\'' +
                '}';
    }
}
