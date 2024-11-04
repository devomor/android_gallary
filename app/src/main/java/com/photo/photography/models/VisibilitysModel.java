package com.photo.photography.models;

/**
 * Created on 31-05-2018, 02:57:43 PM.
 */

public class VisibilitysModel {

    public int cameraVisible;
    public int galleryVisible;
    public int bannerAdVisible;
    public int intertatialAdVisible;
    public int fbNativeAdVisible;
    public String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getFbNativeAdVisible() {
        return fbNativeAdVisible;
    }

    public void setFbNativeAdVisible(int fbNativeAdVisible) {
        this.fbNativeAdVisible = fbNativeAdVisible;
    }

    public int isCameraVisible() {
        return cameraVisible;
    }

    public void setCameraVisible(int cameraVisible) {
        this.cameraVisible = cameraVisible;
    }

    public int isGalleryVisible() {
        return galleryVisible;
    }

    public void setGalleryVisible(int galleryVisible) {
        this.galleryVisible = galleryVisible;
    }

    public int isBannerAdVisible() {
        return bannerAdVisible;
    }

    public void setBannerAdVisible(int bannerAdVisible) {
        this.bannerAdVisible = bannerAdVisible;
    }

    public int isIntertatialAdVisible() {
        return intertatialAdVisible;
    }

    public void setIntertatialAdVisible(int intertatialAdVisible) {
        this.intertatialAdVisible = intertatialAdVisible;
    }
}
