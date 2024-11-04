package com.photo.photography.duplicatephotos.extras;

import com.photo.photography.duplicatephotos.pixelsrelated.RGBObject;

import java.util.List;

public class RGBValueNdPath {
    private long dateAndTime;
    private String filePath;
    private long id;
    private String imageResolution;
    private List<RGBObject> listOfPixelsRgbValue;

    public List<RGBObject> getListOfPixelsRgbValue() {
        return this.listOfPixelsRgbValue;
    }

    public void setListOfPixelsRgbValue(List<RGBObject> list) {
        this.listOfPixelsRgbValue = list;
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
