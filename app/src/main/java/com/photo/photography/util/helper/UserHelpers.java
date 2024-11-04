package com.photo.photography.util.helper;

import com.photo.photography.MyApp;
import com.photo.photography.BuildConfig;
import com.photo.photography.edit_views.Constants;
import com.photo.photography.util.utilsEdit.SupportClass;

public class UserHelpers {
    public static final String USER_IN_SPLASH_INTRO = "USER_IN_SPLASH_INTRO";
    public static final String APP_DATA = "APP_DATA";
    public static String click = "click";
    public static String OPEN_ADS_COUNT_IN_SESSION = "OPEN_ADS_COUNT_IN_SESSION";
    public static String rateApp = "rateApp";
    public static String adtype = "adtype";
    /*  public static int getClickCounterOnSaveCollage() {
          return SharedPreferencesHelper.getInstance().getInt(Constants.COLLAGE_SAVE, 0);
      }

      public static void setClickCounterOnSaveCollage(int count) {
          SharedPreferencesHelper.getInstance().setInt(Constants.COLLAGE_SAVE, count);
      }*/
    public static String in_appreview = "in_appreview";
    public static String review = "review";
    public static String is_ad_enable = "is_ad_enable";
    public static String remote = "remote";
    public static String ads_per_click = "ads_per_click";
    public static String ads_per_session = "ads_per_session";
    public static String review_count = "review_count";
    public static String app_open_ad_count = "app_open_ad_count";
    public static String app_open_ad_show = "app_open_ad_show";
    public static String direct_review_enable = "direct_review_enable";
    public static String album_click_enabled = "album_click_enabled";
    public static String app_open_ad_splash = "app_open_ad_splash";
    public static String review_popup_count = "review_popup_count";
    public static String splash_screen_wait_count = "splash_screen_wait_count";


  /*  public static int getLastClickScreen() {
        return SharedPreferencesHelper.getInstance().getInt(Constants.APP_CLICK_DRAWER, 0);
    }

    public static void setLastClickScreen(int count) {
        SharedPreferencesHelper.getInstance().setInt(Constants.APP_CLICK_DRAWER, count);
    }*/

   /* public static int getHomeTabClick() {
        return SharedPreferencesHelper.getInstance().getInt(Constants.HOME_TAB_CHANGE, 0);
    }

    public static void setHomeTabClick(int count) {
        SharedPreferencesHelper.getInstance().setInt(Constants.HOME_TAB_CHANGE, count);
    }*/

  /*  public static int getSlideSingleMedia() {
        return SharedPreferencesHelper.getInstance().getInt(Constants.SLIDE_SINGLE_MEDIA, 0);
    }

    public static void setSlideSingleMedia(int count) {
        SharedPreferencesHelper.getInstance().setInt(Constants.SLIDE_SINGLE_MEDIA, count);
    }*/

   /* public static int getClickCounterOnSettings() {
        return SharedPreferencesHelper.getInstance().getInt(Constants.SETTING_CLICK, 0);
    }

    public static void setClickCounterOnSettings(int count) {
        SharedPreferencesHelper.getInstance().setInt(Constants.SETTING_CLICK, count);
    }*/

    /*public static int getClickCounterOnPalette() {
        return SharedPreferencesHelper.getInstance().getInt(Constants.PALETTE_CLICK, 0);
    }

    public static void setClickCounterOnPalette(int count) {
        SharedPreferencesHelper.getInstance().setInt(Constants.PALETTE_CLICK, count);
    }*/

   /* public static int getClickCounterOnEditMedia() {
        return SharedPreferencesHelper.getInstance().getInt(Constants.EDIT_MEDIA_CLICK, 0);
    }

    public static void setClickCounterOnEditMedia(int count) {
        SharedPreferencesHelper.getInstance().setInt(Constants.EDIT_MEDIA_CLICK, count);
    }*/

   /* public static int getClickCounterOnCollage() {
        return SharedPreferencesHelper.getInstance().getInt(Constants.COLLAGE_CLICK, 0);
    }

    public static void setClickCounterOnCollage(int count) {
        SharedPreferencesHelper.getInstance().setInt(Constants.COLLAGE_CLICK, count);
    }*/

  /*  public static int getClickCounterOnCollage2() {
        return SharedPreferencesHelper.getInstance().getInt(Constants.COLLAGE_CLICK2, 0);
    }

    public static void setClickCounterOnCollage2(int count) {
        SharedPreferencesHelper.getInstance().setInt(Constants.COLLAGE_CLICK2, count);
    }*/
    public static String exit_ad_enable = "exit_ad_enable";

    public static boolean isShowRate() {
        return PreferencesHelper.getInstance().getBoolean(rateApp, false);
    }

    public static void setShowRate(boolean rate) {
        PreferencesHelper.getInstance().setBoolean(rateApp, rate);
    }

    public static int getOpenAdsCurrentCount() {
        int type = PreferencesHelper.getInstance().getInt(OPEN_ADS_COUNT_IN_SESSION, 0);
        return type;
    }

    public static void setOpenAdsCurrentCount(int str) {
        PreferencesHelper.getInstance().setInt(OPEN_ADS_COUNT_IN_SESSION, str);
    }

    public static boolean getUserInSplashIntro() {
        return PreferencesHelper.getInstance().getBoolean(USER_IN_SPLASH_INTRO, false);
    }

    public static void setUserInSplashIntro(boolean rate) {
        PreferencesHelper.getInstance().setBoolean(USER_IN_SPLASH_INTRO, rate);
    }

    public static boolean getFullScreenIsInView() {
        return Constants.setFullScreenIsInView;
    }

    public static void setFullScreenIsInView(boolean val) {
        Constants.setFullScreenIsInView = val;
    }

    public static int getClickCount() {
        int type = PreferencesHelper.getInstance().getInt(click, 0);
        return type;
    }

    public static void setClickCount(int num) {
        PreferencesHelper.getInstance().setInt(click, num);
    }

    //            in_appreview
    public static int getInAppReview() {
        int type = PreferencesHelper.getInstance().getInt(in_appreview, 0);
        return type;
    }

    public static void setInAppReview(int num) {
        PreferencesHelper.getInstance().setInt(in_appreview, num);
    }

    //            adtype
    public static String getAdType() {
        String type = PreferencesHelper.getInstance().getString(adtype, "adx");
        return type;
    }

    public static void setAdType(String num) {
        PreferencesHelper.getInstance().setString(adtype, num);
    }

    public static void setIsAdEnable(int str) {
        PreferencesHelper.getInstance().setInt(is_ad_enable, str);
    }

    public static boolean isAdEnable() {
        if (BuildConfig.DEBUG || !SupportClass.isInternetAvailable(MyApp.mContext)) {
            return true;
//            return false;
        } else {
            int type = PreferencesHelper.getInstance().getInt(is_ad_enable, 0);
            return type != 0;
        }
    }

    public static String getAppData() {
        String type = PreferencesHelper.getInstance().getString(APP_DATA, "");
        return type;
    }

    public static void setAppData(String str) {
        PreferencesHelper.getInstance().setString(APP_DATA, str);
    }

    public static int getAdsPerClick() {
        int type = PreferencesHelper.getInstance().getInt(ads_per_click, 4);
        return type;
    }

    public static void setAdsPerClick(int str) {
        PreferencesHelper.getInstance().setInt(ads_per_click, str);
    }

    public static int getExitAdEnable() {
        int type = PreferencesHelper.getInstance().getInt(exit_ad_enable, 4);
        return type;
    }

    public static void setExitAdEnable(int str) {
        PreferencesHelper.getInstance().setInt(exit_ad_enable, str);
    }

    public static int getReviewPopupCount() {
        int type = PreferencesHelper.getInstance().getInt(review_popup_count, 1);
        return type;
    }

    public static void setReviewPopupCount(int str) {
        PreferencesHelper.getInstance().setInt(review_popup_count, str);
    }

    public static int getAdsPerSession() {
        int type = PreferencesHelper.getInstance().getInt(ads_per_session, 5);
        return type;
    }

    public static void setAdsPerSession(int str) {
        PreferencesHelper.getInstance().setInt(ads_per_session, str);
    }

    public static int getReviewCount() {
        int type = PreferencesHelper.getInstance().getInt(review_count, 5);
        return type;
    }

    public static void setReviewCount(int str) {
        PreferencesHelper.getInstance().setInt(review_count, str);
    }

    public static void updateCountAdsSession() {
        int lastCount = getAdsPerSession();
        PreferencesHelper.getInstance().setInt(ads_per_session, lastCount - 1);
    }

    public static void updateReviewCount() {
        int lastCount = getReviewCount();
        PreferencesHelper.getInstance().setInt(review_count, lastCount + 1);
    }

    public static boolean isBanner() {
        int type = MyApp.getSettings().getBanner();
        if (type == 0) {
            return false;
        }
        return true;
    }

    public static boolean isNative() {
        int type = MyApp.getSettings().getNativeApp();
        if (type == 0) {
            return false;
        }
        return true;
    }
 public static boolean isInterstitial() {
        int type = MyApp.getSettings().getInterstitial();
        if (type == 0) {
            return false;
        }
        return true;
    }

    public static boolean isOpenApp() {
        int type = MyApp.getSettings().getOpenad();
        if (type == 0) {
            return false;
        }
        return true;
    }


    public static int getOpenAdsCount() {
        int type = PreferencesHelper.getInstance().getInt(app_open_ad_count, 3);
        return type;
    }

    public static void setOpenAdsCount(int str) {
        PreferencesHelper.getInstance().setInt(app_open_ad_count, str);
    }

    public static int getOpenAdsSplash() {
        int type = PreferencesHelper.getInstance().getInt(app_open_ad_splash, 1);
        return type;
    }

    public static void setOpenAdsSplash(int str) {
        PreferencesHelper.getInstance().setInt(app_open_ad_splash, str);
    }

    public static int getSplashScreenWaitCount() {
        if(BuildConfig.DEBUG)
            return 3;
        int type = PreferencesHelper.getInstance().getInt(splash_screen_wait_count, 10);
        return type;
    }

    public static void setSplashScreenWaitCount(int str) {
        PreferencesHelper.getInstance().setInt(splash_screen_wait_count, str);
    }


    public static int getOpenAdsShowCount() {
        int type = PreferencesHelper.getInstance().getInt(app_open_ad_show, 1);
        return type;
    }

    public static void setOpenAdsShowCount(int str) {
        PreferencesHelper.getInstance().setInt(app_open_ad_show, str);
    }

    public static int getDirectReviewEnable() {
        int type = PreferencesHelper.getInstance().getInt(direct_review_enable, 1);
        return type;
    }

    public static void setDirectReviewEnable(int str) {
        PreferencesHelper.getInstance().setInt(direct_review_enable, str);
    }

    public static final String RateDone = "RateDone";
    public static boolean getRateDone() {
        boolean data = PreferencesHelper.getInstance().getBoolean(RateDone, false);
        return data;
    }

    public static void setRateDone(boolean key) {
        PreferencesHelper.getInstance().setBoolean(RateDone, key);
    }

    public static int getAlbumClickEnabled() {
        int type = PreferencesHelper.getInstance().getInt(album_click_enabled, 0);
        return type;
    }

    public static void setAlbumClickEnabled(int str) {
        PreferencesHelper.getInstance().setInt(album_click_enabled, str);
    }

    public static String USER_BIO_TRY_FAIL = "USER_BIO_TRY_FAIL";
    public static int getFailCount() {
        int type = PreferencesHelper.getInstance().getInt(USER_BIO_TRY_FAIL, 0);
        return type;
    }

    public static void setFailCount(int str) {
        PreferencesHelper.getInstance().setInt(USER_BIO_TRY_FAIL, str);
    }



    public static String USER_VALUT_SET = "USER_VALUT_SET";
    public static boolean getVaultSet() {
        boolean type = PreferencesHelper.getInstance().getBoolean(USER_VALUT_SET, false);
        return type;
    }

    public static void setVaultSet(boolean str) {
        PreferencesHelper.getInstance().setBoolean(USER_VALUT_SET, str);
    }


    public static void setRemoteData(String str) {
        PreferencesHelper.getInstance().setString(remote, str);
    }

    public static String getRemoteData() {
        String type = PreferencesHelper.getInstance().getString(remote, "");
        return type;
    }

}