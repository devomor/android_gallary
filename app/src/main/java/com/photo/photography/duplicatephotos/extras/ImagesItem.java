package com.photo.photography.duplicatephotos.extras;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImagesItem implements Serializable {

    @SerializedName("dateAndTime")
    private long dateAndTime;
    @SerializedName("image")
    private String image;
    @SerializedName("id")
    private long id = 0;
    @SerializedName("imageCheckBox")
    private boolean imageCheckBox;
    @SerializedName("imageItemGrpTag")
    private int imageItemGrpTag;
    @SerializedName("imageResolution")
    private String imageResolution;
    @SerializedName("position")
    private int position;
    @SerializedName("sizeOfTheFile")
    private long sizeOfTheFile;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String str) {
        this.image = str;
    }

    public boolean isImageCheckBox() {
        return this.imageCheckBox;
    }

    public void setImageCheckBox(boolean z) {
        this.imageCheckBox = z;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int i) {
        this.position = i;
    }

    public int getImageItemGrpTag() {
        return this.imageItemGrpTag;
    }

    public void setImageItemGrpTag(int i) {
        this.imageItemGrpTag = i;
    }

    public long getSizeOfTheFile() {
        return this.sizeOfTheFile;
    }

    public void setSizeOfTheFile(long j) {
        this.sizeOfTheFile = j;
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
