package com.photo.photography;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.photo.photography.util.helper.UserHelpers;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import static androidx.lifecycle.Lifecycle.Event.ON_START;


public class AppOpenManag implements LifecycleObserver, Application.ActivityLifecycleCallbacks {
    private static final String LOG_TAG = "AppOpenManager";
    private static boolean isShowingAd = false;
    private final MyApp myApplication;
    private AppOpenAd appOpenAd = null;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private Activity currentActivity;


    public AppOpenManag(MyApp myApplication) {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(ON_START)
    public void onStart() {
        showAdIfAvailable();
        Log.d(LOG_TAG, "onStart");
    }

    public void showAdIfAvailable() {
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        if (!isShowingAd && isAdAvailable()) {
            Log.d(LOG_TAG, "Will show ad.");
            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            UserHelpers.setFullScreenIsInView(false);
                            // Set the reference to null so isAdAvailable() returns false.
                            appOpenAd = null;
                            isShowingAd = false;
                            fetchAd();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            UserHelpers.setFullScreenIsInView(false);
                            // Toast.makeText(myApplication, ""+adError.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            UserHelpers.setFullScreenIsInView(true);
                            UserHelpers.setOpenAdsCurrentCount(UserHelpers.getOpenAdsCurrentCount() + 1);
                            isShowingAd = true;
                        }
                    };

            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);

            int count  = UserHelpers.getOpenAdsShowCount();
            UserHelpers.setOpenAdsShowCount(count+1);
            appOpenAd.show(currentActivity);
            UserHelpers.setFullScreenIsInView(true);
        } else {
            Log.d(LOG_TAG, "Can not show ad.");
            fetchAd();
        }
    }

    public boolean checkOpenAdsCondition() {

        if (!UserHelpers.isAdEnable()) {
            return false;
        }

        if (!UserHelpers.isOpenApp()) {
            return false;
        }

        int showCount  = UserHelpers.getOpenAdsShowCount();
        int totalLimit  = UserHelpers.getOpenAdsCount();
        if (showCount >= totalLimit) {
            return false;
        }

        if (!UserHelpers.getUserInSplashIntro()) {
            if (!UserHelpers.getFullScreenIsInView()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    /**
     * Request an ad
     */
    public void fetchAd() {
        // We will implement this below.
        if (isAdAvailable()) {
            return;
        }

        if (!UserHelpers.isAdEnable()) {
            return;
        }
        if (!UserHelpers.isOpenApp()) {
            return;
        }

        int showCount  = UserHelpers.getOpenAdsShowCount();
        int totalLimit  = UserHelpers.getOpenAdsCount();
        if (showCount >= totalLimit) {
            return;
        }


        if(appOpenAd == null) {
            loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
                /**
                 * Called when an app open ad has loaded.
                 *
                 * @param ad the loaded app open ad.
                 */
                @Override
                public void onAdLoaded(AppOpenAd ad) {
                    appOpenAd = ad;
                }

                /**
                 * Called when an app open ad has failed to load.
                 *
                 * @param loadAdError the error.
                 */
                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    UserHelpers.setFullScreenIsInView(false);
                    // Handle the error.
                    //    Toast.makeText(myApplication, "" + loadAdError.getMessage(), Toast.LENGTH_SHORT).show();
                }

            };

            if (BuildConfig.DEBUG) {
                Log.e("Ads: ", "Load Open App Manager");
                AppOpenAd.load(myApplication, myApplication.getString(R.string.admob_open_Ad_test), new AdRequest.Builder().build(), AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
            } else {
                String adUnitId = MyApp.getSettings().getOpenadid();
                if (adUnitId == null) {
                    return;
                }
                if (adUnitId.isEmpty()) {
                    return;
                }
                Log.e("Ads: ", "Load Open App Manager");
                AppOpenAd.load(myApplication, adUnitId, new AdRequest.Builder().build(), AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
            }
        }

    }

    /**
     * Creates and returns ad request.
     */
    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    /**
     * Utility method that checks if ad exists and can be shown.
     */
    public boolean isAdAvailable() {
        boolean isAdAvailable = appOpenAd != null && checkOpenAdsCondition();
        return isAdAvailable;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        currentActivity = null;
    }

}