package com.SampleApp.row.Data;

/**
 * Created by user on 19-10-2016.
 */
public class PictureInsideAlbumData {

    String uri;
    String filepath;
    public boolean box = false;


    public PictureInsideAlbumData() {


    }


    public PictureInsideAlbumData(String uri, String filepath, Boolean box, int countOfImagesSelected) {
        this.uri = uri;
        this.filepath = filepath;
        this.box = box;


    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public boolean isBox() {
        return box;
    }

    public void setBox(boolean box) {
        this.box = box;
    }





}



