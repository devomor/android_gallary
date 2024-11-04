package com.photo.photography.duplicatephotos.extras;

public class MD5CheckWithImagePath {
    private long dateAndTime;
    private String filePath;
    private long id;
    private String imageResolution;
    private String md5Checksum;

    public String getMd5Checksum() {
        return this.md5Checksum;
    }

    public void setMd5Checksum(String str) {
        this.md5Checksum = str;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String str) {
        this.filePath = str;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageResolution() {
        return this.imageResolution;
    }

    public void setImageResolution(String str) {
        this.imageResolution = str;
    }

    public long getDateAndTime() {
        return this.dateAndTime;
    }

    public void setDateAndTime(long j) {
        this.dateAndTime = j;
    }
}

