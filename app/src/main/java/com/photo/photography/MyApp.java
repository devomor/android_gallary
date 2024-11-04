package com.photo.photography;

import static com.photo.photography.util.RatingDialogs.SHOW_LATER;
import static com.photo.photography.util.RatingDialogs.SHOW_NEVER;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;

import com.photo.photography.edit_views.Constants;
import com.photo.photography.callbacks.CallbackAds;
import com.photo.photography.models.AppDataModel;
import com.photo.photography.ads_notifier.AdsEventNotifier;
import com.photo.photography.ads_notifier.AdsEventTypes;
import com.photo.photography.ads_notifier.AdsNotifierFactory;
import com.photo.photography.util.helper.UserHelpers;
import com.photo.photography.util.ApplicationUtil;
import com.photo.photography.util.RatingDialogs;
import com.photo.photography.util.preferences.Prefs;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.Iconics;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

public class MyApp extends MultiDexApplication {
    public static Context mContext;
    private static MyApp mInstance;
    private static MyApp myApplication;
    private static boolean isReviewDialogAsked = false;
    private List<NativeAd> mNativeAdsGHome = new ArrayList<>();
    private List<String> mNativeAdsId = new ArrayList<>();

    public static boolean isReviewOn() {
        return !UserHelpers.isShowRate();
    }

    public static MyApp getApplication() {
        return myApplication;
    }

    public static void setApplication(MyApp application) {
        myApplication = application;
    }

    public static AppDataModel getSettings() {
        try {
            if (!UserHelpers.getAppData().isEmpty()) {
                String response = UserHelpers.getAppData();
                if (!TextUtils.isEmpty(response)) {
                    AppDataModel appResponseApi = parseAppUserListModel(response);
                    return appResponseApi;
                } else {
                    return new AppDataModel();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new AppDataModel();
    }

    public static AppDataModel parseAppUserListModel(String jsonObject) {

        try {
            Gson gson = new Gson();
            TypeToken<AppDataModel> token = new TypeToken<AppDataModel>() {
            };
            AppDataModel couponModel = gson.fromJson(jsonObject.toString(), token.getType());
            return couponModel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MyApp getInstance() {
        return mInstance;
    }

    public void loadAdsDialog(Dialog progressDialogApp) {
        try {
            if (progressDialogApp != null) {
                progressDialogApp.setContentView(R.layout.dialog_load_ad);
                progressDialogApp.setCancelable(false);
                Window window = progressDialogApp.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
                progressDialogApp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                if (!progressDialogApp.isShowing())
                    progressDialogApp.show();
            }
        } catch (Exception exc) {
            Log.e("TAG", "Exception : " + exc.toString());
        }
    }

    public void dismissAdsDialog(Dialog progressDialogApp) {
        if (progressDialogApp != null && progressDialogApp.isShowing()) {
            progressDialogApp.dismiss();
        }
    }

    public void showInterstitial(Activity act, Intent intent, boolean isFinished, int requestCode, Bundle transitionBundle) {

        if (!SupportClass.checkConnection(act)) {
            doStartActivity(act, intent, requestCode, transitionBundle);
            if (isFinished) {
                if (!act.isFinishing())
                    act.finish();
            }
            return;
        }

        if (!UserHelpers.isAdEnable()) {
            doStartActivity(act, intent, requestCode, transitionBundle);
            if (isFinished) {
                if (!act.isFinishing())
                    act.finish();
            }
            return;
        }

        if (!UserHelpers.isInterstitial()) {
            doStartActivity(act, intent, requestCode, transitionBundle);
            if (isFinished) {
                if (!act.isFinishing())
                    act.finish();
            }
            return;
        }

        if (UserHelpers.getAdsPerSession() <= 0) {
            doStartActivity(act, intent, requestCode, transitionBundle);
            if (isFinished) {
                if (!act.isFinishing())
                    act.finish();
            }
            return;
        }
        if (UserHelpers.getAdType().equals(Constants.ADMOB))
            showFSAd(act, intent, isFinished, requestCode, transitionBundle);
        else {
            showFSAdADX(act, intent, isFinished, requestCode, transitionBundle);
        }
    }

    public void showAlbumInterstitial(Activity act, CallbackAds callbackListener) {

        if (!SupportClass.checkConnection(act)) {
            callbackListener.onBackCall();
            return;
        }

        if (!UserHelpers.isAdEnable()) {
            callbackListener.onBackCall();
            return;
        }

        if (!UserHelpers.isInterstitial()) {
            callbackListener.onBackCall();
            return;
        }

        if (UserHelpers.getAdsPerSession() <= 0) {
            callbackListener.onBackCall();
            return;
        }
        if (UserHelpers.getAdType().equals(Constants.ADMOB))
            showAlbumFSAd(act,callbackListener);
        else {
            showAlbumFSAdADX(act, callbackListener);
        }
    }




    public void showInterstitialOnExit(Activity act, Intent intent, boolean isFinished) {

        if (!SupportClass.checkConnection(act)) {
            if (intent != null)
                act.startActivity(intent);
            if (isFinished) {
                if (!act.isFinishing())
                    act.finish();
            }
            return;
        }

        if (!UserHelpers.isAdEnable()) {
            if (intent != null)
                act.startActivity(intent);
            if (isFinished) {
                if (!act.isFinishing())
                    act.finish();
            }
            return;
        }

        if (!UserHelpers.isInterstitial()) {
            if (intent != null)
                act.startActivity(intent);
            if (isFinished) {
                if (!act.isFinishing())
                    act.finish();
            }
            return;
        }

        if (UserHelpers.getExitAdEnable() == 0) {
            if (intent != null)
                act.startActivity(intent);
            if (isFinished) {
                if (!act.isFinishing())
                    act.finish();
            }
            return;
        }

        if (UserHelpers.getAdType().equals(Constants.ADMOB))
            showFSAd(act, intent, isFinished, -1, null);
        else {
            showFSAdADX(act, intent, isFinished, -1, null);
        }
    }

    private void showFSAd(Activity act, Intent intent, boolean isFinished, int requestCode, Bundle transitionBundle) {
        if (SupportClass.checkConnection(act)) {

            Dialog adsDialog = new Dialog(act);
            loadAdsDialog(adsDialog);
            AdRequest adRequest = new AdRequest.Builder().build();
            String adUnitId = "";
            if (BuildConfig.DEBUG)
                adUnitId = getString(R.string.admob_interstitial_ads_id_test);
            else {
                adUnitId = MyApp.getSettings().getInterstitialid();
            }
            Log.e("Ads ", "FullScreenAd adUnitId:  " + adUnitId);
            if (adUnitId == null) {
                dismissAdsDialog(adsDialog);
                doStartActivity(act, intent, requestCode, transitionBundle);
                if (isFinished) {
                    if (act != null && !act.isFinishing())
                        act.finish();
                }
                return;
            }

            if (TextUtils.isEmpty(adUnitId)) {
                dismissAdsDialog(adsDialog);
                doStartActivity(act, intent, requestCode, transitionBundle);
                if (isFinished) {
                    if (act != null && !act.isFinishing())
                        act.finish();
                }
                return;
            }
            InterstitialAd.load(act, adUnitId, adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    Log.e("Ads ", "FullScreenAd: onAdLoaded");
                    Log.e("Ads ", "onAdLoaded");
                    interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            Log.e("Ads ", "The ad was dismissed.");
                            UserHelpers.setFullScreenIsInView(false);
                            doStartActivity(act, intent, requestCode, transitionBundle);
                            if (isFinished) {
                                if (!act.isFinishing())
                                    act.finish();
                            }
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when fullscreen content failed to show.
                            Log.e("Ads ", "The ad failed to show.");
                            UserHelpers.setFullScreenIsInView(false);
                            doStartActivity(act, intent, requestCode, transitionBundle);
                            if (isFinished) {
                                if (!act.isFinishing())
                                    act.finish();
                            }
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when fullscreen content is shown.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            Log.e("Ads ", "onAdShowedFullScreenContent");
                        }
                    });

                    dismissAdsDialog(adsDialog);
                    interstitialAd.show(act);
                    UserHelpers.updateCountAdsSession();
                    UserHelpers.setFullScreenIsInView(true);

                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    Log.e("Ads ", "FullScreenAd: onAdFailedToLoad: " + loadAdError.getCode());
                    Log.e("Ads ", loadAdError.getMessage());
                    dismissAdsDialog(adsDialog);
                    UserHelpers.setFullScreenIsInView(false);
                    doStartActivity(act, intent, requestCode, transitionBundle);
                    if (isFinished) {
                        if (!act.isFinishing())
                            act.finish();
                    }
                }
            });
        } else {
            doStartActivity(act, intent, requestCode, transitionBundle);
            if (isFinished) {
                if (!act.isFinishing())
                    act.finish();
            }
        }
    }
    private void showAlbumFSAd(Activity act, CallbackAds callbackListener) {
        if (SupportClass.checkConnection(act)) {

            Dialog adsDialog = new Dialog(act);
            loadAdsDialog(adsDialog);
            AdRequest adRequest = new AdRequest.Builder().build();
            String adUnitId = "";
            if (BuildConfig.DEBUG)
                adUnitId = getString(R.string.admob_interstitial_ads_id_test);
            else {
                adUnitId = MyApp.getSettings().getInterstitialid();
            }
            Log.e("Ads ", "FullScreenAd adUnitId:  " + adUnitId);
            if (adUnitId == null) {
                dismissAdsDialog(adsDialog);
                callbackListener.onBackCall();
                return;
            }

            if (TextUtils.isEmpty(adUnitId)) {
                dismissAdsDialog(adsDialog);
                callbackListener.onBackCall();
                return;
            }
            InterstitialAd.load(act, adUnitId, adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    Log.e("Ads ", "FullScreenAd: onAdLoaded");
                    Log.e("Ads ", "onAdLoaded");
                    interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            Log.e("Ads ", "The ad was dismissed.");
                            UserHelpers.setFullScreenIsInView(false);
                            callbackListener.onBackCall();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when fullscreen content failed to show.
                            Log.e("Ads ", "The ad failed to show.");
                            UserHelpers.setFullScreenIsInView(false);
                            callbackListener.onBackCall();
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when fullscreen content is shown.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            Log.e("Ads ", "onAdShowedFullScreenContent");
                        }
                    });

                    dismissAdsDialog(adsDialog);
                    interstitialAd.show(act);
                    UserHelpers.updateCountAdsSession();
                    UserHelpers.setFullScreenIsInView(true);

                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    Log.e("Ads ", "FullScreenAd: onAdFailedToLoad: " + loadAdError.getCode());
                    Log.e("Ads ", loadAdError.getMessage());
                    dismissAdsDialog(adsDialog);
                    UserHelpers.setFullScreenIsInView(false);
                    callbackListener.onBackCall();
                }
            });
        } else {
            callbackListener.onBackCall();
        }
    }

    private void showFSAdADX(Activity act, Intent intent, boolean isFinished, int requestCode, Bundle transitionBundle) {
        if (SupportClass.checkConnection(act)) {

            Dialog adsDialog = new Dialog(act);
            loadAdsDialog(adsDialog);
            AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
            String adUnitId = "";
            if (BuildConfig.DEBUG)
                adUnitId = getString(R.string.admob_interstitial_ads_id_test);
            else {
                adUnitId = MyApp.getSettings().getInterstitialid();
            }
            Log.e("Ads ", "FullScreenAd adUnitId:  " + adUnitId);
            if (adUnitId == null) {
                dismissAdsDialog(adsDialog);
                doStartActivity(act, intent, requestCode, transitionBundle);
                if (isFinished) {
                    if (act != null && !act.isFinishing())
                        act.finish();
                }
                return;
            }

            if (TextUtils.isEmpty(adUnitId)) {
                dismissAdsDialog(adsDialog);
                doStartActivity(act, intent, requestCode, transitionBundle);
                if (isFinished) {
                    if (act != null && !act.isFinishing())
                        act.finish();
                }
                return;
            }

            AdManagerInterstitialAd.load(act, adUnitId, adRequest,
                    new AdManagerInterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                            // The mAdManagerInterstitialAd reference will be null until
                            // an ad is loaded.
                            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    // Called when fullscreen content is dismissed.
                                    Log.e("Ads ", "The ad was dismissed.");
                                    UserHelpers.setFullScreenIsInView(false);
                                    doStartActivity(act, intent, requestCode, transitionBundle);
                                    if (isFinished) {
                                        if (!act.isFinishing())
                                            act.finish();
                                    }
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                    // Called when fullscreen content failed to show.
                                    Log.e("Ads ", "The ad failed to show.");
                                    UserHelpers.setFullScreenIsInView(false);
                                    doStartActivity(act, intent, requestCode, transitionBundle);
                                    if (isFinished) {
                                        if (!act.isFinishing())
                                            act.finish();
                                    }
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    // Called when fullscreen content is shown.
                                    // Make sure to set your reference to null so you don't
                                    // show it a second time.
                                    Log.d("TAG", "The ad was shown.");
                                }
                            });

                            dismissAdsDialog(adsDialog);
                            interstitialAd.show(act);
                            UserHelpers.updateCountAdsSession();
                            UserHelpers.setFullScreenIsInView(true);
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            dismissAdsDialog(adsDialog);
                            UserHelpers.setFullScreenIsInView(false);
                            doStartActivity(act, intent, requestCode, transitionBundle);
                            if (isFinished) {
                                if (!act.isFinishing())
                                    act.finish();
                            }
                        }
                    });
        } else {
            doStartActivity(act, intent, requestCode, transitionBundle);
            if (isFinished) {
                if (!act.isFinishing())
                    act.finish();
            }
        }
    }
 private void showAlbumFSAdADX(Activity act, CallbackAds callbackListener) {
        if (SupportClass.checkConnection(act)) {

            Dialog adsDialog = new Dialog(act);
            loadAdsDialog(adsDialog);
            AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
            String adUnitId = "";
            if (BuildConfig.DEBUG)
                adUnitId = getString(R.string.admob_interstitial_ads_id_test);
            else {
                adUnitId = MyApp.getSettings().getInterstitialid();
            }
            Log.e("Ads ", "FullScreenAd adUnitId:  " + adUnitId);
            if (adUnitId == null) {
                dismissAdsDialog(adsDialog);
                callbackListener.onBackCall();
                return;
            }

            if (TextUtils.isEmpty(adUnitId)) {
                dismissAdsDialog(adsDialog);
                callbackListener.onBackCall();
                return;
            }

            AdManagerInterstitialAd.load(act, adUnitId, adRequest,
                    new AdManagerInterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                            // The mAdManagerInterstitialAd reference will be null until
                            // an ad is loaded.
                            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    // Called when fullscreen content is dismissed.
                                    Log.e("Ads ", "The ad was dismissed.");
                                    UserHelpers.setFullScreenIsInView(false);
                                    callbackListener.onBackCall();
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                    // Called when fullscreen content failed to show.
                                    Log.e("Ads ", "The ad failed to show.");
                                    UserHelpers.setFullScreenIsInView(false);
                                    callbackListener.onBackCall();
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    // Called when fullscreen content is shown.
                                    // Make sure to set your reference to null so you don't
                                    // show it a second time.
                                    Log.d("TAG", "The ad was shown.");
                                }
                            });

                            dismissAdsDialog(adsDialog);
                            interstitialAd.show(act);
                            UserHelpers.updateCountAdsSession();
                            UserHelpers.setFullScreenIsInView(true);
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            dismissAdsDialog(adsDialog);
                            UserHelpers.setFullScreenIsInView(false);
                            callbackListener.onBackCall();
                        }
                    });
        } else {
            callbackListener.onBackCall();
        }
    }

    private void doStartActivity(Activity act, Intent intent, int requestCode, Bundle transitionBundle) {
        if (intent != null && requestCode > 0 && transitionBundle != null)
//            act.startActivityForResult(intent, requestCode, transitionBundle);
            act.startActivityForResult(intent, requestCode);
        else if (intent != null && requestCode > 0)
            act.startActivityForResult(intent, requestCode);
        else if (intent != null)
            act.startActivity(intent);
    }

    public List<NativeAd> getGNativeHome() {
        return mNativeAdsGHome;
    }

    public void loadAdmobNativeAd() {

        if (!UserHelpers.isAdEnable()) {
            return;
        }

        if (!UserHelpers.isNative()) {
            return;
        }
        if (!SupportClass.checkConnection(mContext)) {
            return;
        }

        if (SupportClass.checkConnection(mContext)) {
            loadGNativeIntermediate(0);
        }

    }


    public void loadGNativeIntermediate(int adCount) {
        if (adCount == 0) {
            mNativeAdsGHome = new ArrayList<>();
            mNativeAdsId.clear();
            mNativeAdsId.add(MyApp.getSettings().getNativeid());
        }
        AdLoader.Builder builder;

        if (BuildConfig.DEBUG) {
            builder = new AdLoader.Builder(this, getString(R.string.admob_interstitial_native_id_test));
            Log.e("NativeAd", "adUnitId:" + getString(R.string.admob_interstitial_native_id_test));
            Log.e("Ads ", "NativeAd adUnitId:  " + getString(R.string.admob_interstitial_native_id_test));

        } else {
            String adUnitId = mNativeAdsId.get(adCount);
            Log.e("Ads ", "NativeAd adUnitId:  " + adUnitId);
            Log.e("NativeAd", "adUnitId:" + adUnitId);
            if (adUnitId == null) {
                return;
            }
            if (TextUtils.isEmpty(adUnitId)) {
                return;
            }
            builder = new AdLoader.Builder(this, adUnitId);
        }

        int native_ads_count = 1;
        builder.forNativeAd(nativeAd -> {
            mNativeAdsGHome.add(nativeAd);
            int nextConunt = adCount + 1;
            if (nextConunt < native_ads_count) {
                Log.e("Ads ", "NativeAd nextConunt: " + nextConunt);
                loadGNativeIntermediate(nextConunt);
            }

            if (nextConunt == native_ads_count) {
                Log.e("Ads ", "NativeAd " + nextConunt + ":Last");
                Log.e("NativeAds: ", "last == ");
                AdsEventNotifier notifier = AdsNotifierFactory.getInstance().getNotifier(AdsNotifierFactory.EVENT_NOTIFIER_AD_STATUS);
                notifier.eventNotify(AdsEventTypes.EVENT_AD_LOADED_NATIVE, null);
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();

        com.google.android.gms.ads.nativead.NativeAdOptions adOptions = new com.google.android.gms.ads.nativead.NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                Log.e("Ads ", "NativeAd onAdFailedToLoad: " + adError.getMessage());
            }
        }).build();

        if (UserHelpers.getAdType().equals(Constants.ADMOB)) {
            AdRequest.Builder builerRe = new AdRequest.Builder();
            adLoader.loadAd(builerRe.build());
        } else {
            AdManagerAdRequest.Builder builerRe = new AdManagerAdRequest.Builder();
            adLoader.loadAd(builerRe.build());
        }
    }

    public boolean needToShowAd() {
        int ads_per_click = UserHelpers.getAdsPerClick();
        int getCount = UserHelpers.getClickCount();
        int newCount = getCount + 1;
        UserHelpers.setClickCount(newCount);
        return newCount % ads_per_click == 0;
    }

    public boolean checkForNativeAdMain() {
        return mNativeAdsGHome != null && mNativeAdsGHome.size() > 0;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = getApplicationContext();
        setApplication(this);
        ApplicationUtil.init(this);

        registerFontIcons();
        initialiseStorage();


    }

    private void registerFontIcons() {
        Iconics.registerFont(new GoogleMaterial());
        Iconics.registerFont(new CommunityMaterial());
        Iconics.registerFont(new FontAwesome());
    }

    private void initialiseStorage() {
        Prefs.init(this);
        Hawk.init(this).build();
    }

    public boolean showRatingDialog(Activity act, RatingDialogs.Builder.RatingDialogFormListener listener, final View.OnClickListener dismissListner) {
        SharedPreferences sharedpreferences = getSharedPreferences(RatingDialogs.MyPrefs, Context.MODE_PRIVATE);
        if (sharedpreferences.getBoolean(SHOW_NEVER, false)) {
            return false;
        }
        if (sharedpreferences.getBoolean(SHOW_LATER, false)) {
            return false;
        }
        if (!act.isDestroyed()) {
            final RatingDialogs ratingDialog = new RatingDialogs.Builder(act)
                    .icon(getResources().getDrawable(R.mipmap.ic_launcher)).positiveButtonTextColor(R.color.black)
                    .ratingBarColor(R.color.colorPrimary)
                    .playstoreUrl("https://play.google.com/store/apps/details?id=" + /*act.getPackageName()*/BuildConfig.APPLICATION_ID)
                    .onRatingBarFormSumbit(listener)
                    .build();
            ratingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dismissListner.onClick(null);
                }
            });
            ratingDialog.show();
        }
        return true;
    }

    public void inAppReview(final Activity activity, final RatingDialogs.Builder.RatingDialogFormListener listener, final View.OnClickListener dismissListener) {
        ReviewManager manager = ReviewManagerFactory.create(activity);
        SharedPreferences sharedpreferences = getSharedPreferences(RatingDialogs.MyPrefs, Context.MODE_PRIVATE);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();
                manager.launchReviewFlow(activity, reviewInfo)
                        .addOnFailureListener(e -> {
                            Log.e("inAppReview", " onFailure " + e.getMessage());
                            // Toast.makeText(activity, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            showRatingDialog(activity, listener, dismissListener);
                        }).addOnCompleteListener(task1 -> {
                    Log.e("inAppReview", " onComplete");
                    if (task1.isSuccessful()) {
                        Toast.makeText(activity, "Thanks for the feedback!", Toast.LENGTH_SHORT).show();
//                                    dismissListener.onClick(null);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(SHOW_NEVER, true);
                        editor.commit();
                    }
                }).addOnSuccessListener(result -> {
                    Log.e("inAppReview", " onSuccess ");
                    // showRatingDialog(activity, listener, dismissListener);
                    dismissListener.onClick(null);
                });

            } else {
                /*// There was some problem, continue regardless of the result.
                // Toast.makeText(activity, "There was some problem, continue regardless of the result.", Toast.LENGTH_SHORT).show();
                if (BuildConfig.DEBUG && !DeviceInstallUtils.isLiveAds())
                    Log.e("inAppReview", " There was some problem, continue regardless of the result. ");*/
                showRatingDialog(activity, listener, dismissListener);
            }
        });
    }


}