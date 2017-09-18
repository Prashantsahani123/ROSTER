package com.SampleApp.row.Data;

import android.graphics.Bitmap;

/**
 * Created by user on 14-09-2016.
 */
public class AlbumFolder {

    String id;
    String name;
    long albumCoverId;
    Bitmap bitmap;
    String filepath;

    public AlbumFolder(){


    }

    public AlbumFolder(String id, String name, long albumCoverId,String filepath){
        this.id = id;
        this.name = name;
        this.albumCoverId = albumCoverId;
        this.filepath = filepath;
    }

    public AlbumFolder(Bitmap bitmap, String name, long albumCoverId){
        this.bitmap = bitmap;
        this.name = name;
        this.albumCoverId = albumCoverId;

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getAlbumCoverId() {
        return albumCoverId;
    }

    public void setAlbumCoverId(long albumCoverId) {
        this.albumCoverId = albumCoverId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }


}
