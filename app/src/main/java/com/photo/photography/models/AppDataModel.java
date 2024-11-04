package com.photo.photography.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppDataModel {

    @SerializedName("transfer_link")
    @Expose
    private String transfer_link;
    @SerializedName("bannerid")
    @Expose
    private String bannerid;
    @SerializedName("ads_per_session")
    @Expose
    private int adsPerSession;

    @SerializedName("album_click_enabled")
    @Expose
    private int album_click_enabled = 0;

    public int getAlbum_click_enabled() {
        return album_click_enabled;
    }

    public void setAlbum_click_enabled(int album_click_enabled) {
        this.album_click_enabled = album_click_enabled;
    }

    @SerializedName("exit_ad_enable")
    @Expose
    private int exitAdEnable;
    @SerializedName("nativeid")
    @Expose
    private String nativeid;
    @SerializedName("interstitialid")
    @Expose
    private String interstitialid;
    @SerializedName("openadid")
    @Expose
    private String openadid;
    @SerializedName("rewardadid")
    @Expose
    private String rewardadid;
    @SerializedName("adtype")
    @Expose
    private String adtype;
    @SerializedName("banner")
    @Expose
    private Integer banner;
    @SerializedName("native")
    @Expose
    private Integer nativeApp = 0;
    @SerializedName("interstitial")
    @Expose
    private Integer interstitial = 0;
    @SerializedName("openad")
    @Expose
    private Integer openad = 0;
    @SerializedName("rewardad")
    @Expose
    private Integer rewardad = 0;
    @SerializedName("is_ad_enable")
    @Expose
    private Integer is_ad_enable = 0;
    @SerializedName("ads_per_click")
    @Expose
    private Integer ads_per_click = 4;
    @SerializedName("in_appreview")
    @Expose
    private Integer in_appreview = 0;
    @SerializedName("review")
    @Expose
    private Integer review = 0;
    @SerializedName("app_open_count")
    @Expose
    private Integer app_open_count = 3;
    @SerializedName("is_splash_on")
    @Expose
    private Integer is_splash_on = 1;
    @SerializedName("splash_time")
    @Expose
    private Integer splash_time = 10;
    @SerializedName("review_popup_count")
    @Expose
    private Integer review_popup_count = 1;
    @SerializedName("direct_review_enable")
    @Expose
    private Integer direct_review_enable = 1;

    public String getTransfer_link() {
        return transfer_link;
    }

    public void setTransfer_link(String transfer_link) {
        this.transfer_link = transfer_link;
    }

    public Integer getAds_per_click() {
        return ads_per_click;
    }

    public void setAds_per_click(Integer ads_per_click) {
        this.ads_per_click = ads_per_click;
    }

    public String getBannerid() {
        return bannerid;
    }

    public void setBannerid(String bannerid) {
        this.bannerid = bannerid;
    }

    public String getNativeid() {
        return nativeid;
    }

    public void setNativeid(String nativeid) {
        this.nativeid = nativeid;
    }

    public String getInterstitialid() {
        return interstitialid;
    }

    public void setInterstitialid(String interstitialid) {
        this.interstitialid = interstitialid;
    }

    public String getOpenadid() {
        return openadid;
    }

    public void setOpenadid(String openadid) {
        this.openadid = openadid;
    }

    public String getRewardadid() {
        return rewardadid;
    }

    public void setRewardadid(String rewardadid) {
        this.rewardadid = rewardadid;
    }

    public String getAdtype() {
        return adtype;
    }

    public void setAdtype(String adtype) {
        this.adtype = adtype;
    }

    public Integer getBanner() {
        return banner;
    }

    public void setBanner(Integer banner) {
        this.banner = banner;
    }

    public Integer getNativeApp() {
        return nativeApp;
    }

    public void setNativeApp(Integer nativeApp) {
        this.nativeApp = nativeApp;
    }

    public Integer getInterstitial() {
        return interstitial;
    }

    public void setInterstitial(Integer interstitial) {
        this.interstitial = interstitial;
    }

    public Integer getOpenad() {
        return openad;
    }

    public void setOpenad(Integer openad) {
        this.openad = openad;
    }

    public Integer getRewardad() {
        return rewardad;
    }

    public void setRewardad(Integer rewardad) {
        this.rewardad = rewardad;
    }

    public Integer getIs_ad_enable() {
        return is_ad_enable;
    }

    public void setIs_ad_enable(Integer is_ad_enable) {
        this.is_ad_enable = is_ad_enable;
    }

    public Integer getIn_appreview() {
        return in_appreview;
    }

    public void setIn_appreview(Integer in_appreview) {
        this.in_appreview = in_appreview;
    }

    public Integer getReview() {
        return review;
    }

    public void setReview(Integer review) {
        this.review = review;
    }

    public int getAdsPerSession() {
        return adsPerSession;
    }

    public void setAdsPerSession(int adsPerSession) {
        this.adsPerSession = adsPerSession;
    }

    public int getExitAdEnable() {
        return exitAdEnable;
    }

    public void setExitAdEnable(int exitAdEnable) {
        this.exitAdEnable = exitAdEnable;
    }

    public Integer getApp_open_ad_count() {
        return app_open_count;
    }

    public void setApp_open_ad_count(Integer app_open_ad_count) {
        this.app_open_count = app_open_ad_count;
    }

    public Integer getApp_open_ad_splash() {
        return is_splash_on;
    }

    public void setApp_open_ad_splash(Integer app_open_ad_splash) {
        this.is_splash_on = app_open_ad_splash;
    }

    public Integer getSplash_screen_wait_count() {
        return splash_time;
    }

    public void setSplash_screen_wait_count(Integer splash_screen_wait_count) {
        this.splash_time = splash_screen_wait_count;
    }

    public Integer getReview_popup_count() {
        return review_popup_count;
    }

    public void setReview_popup_count(Integer review_popup_count) {
        this.review_popup_count = review_popup_count;
    }

    public Integer getDirect_review_enable() {
        return direct_review_enable;
    }

    public void setDirect_review_enable(Integer direct_review_enable) {
        this.direct_review_enable = direct_review_enable;
    }
}
