package com.photo.photography.secure_vault.models;

import java.io.Serializable;

public class SnapTrackImg implements Serializable {

    private String mImgName = "";
    private String mImgPath = "";
    private String mCapMillis = "";

    public String getImgPath() {
        return mImgPath;
    }

    public void setImgPath(String mImgPath) {
        this.mImgPath = mImgPath;
    }

    public String getImgName() {
        return mImgName;
    }

    public void setImgName(String mImgName) {
        this.mImgName = mImgName;
    }

    public String getCapMillis() {
        return mCapMillis;
    }

    public void setCapMillis(String mCapMillis) {
        this.mCapMillis = mCapMillis;
    }
}
