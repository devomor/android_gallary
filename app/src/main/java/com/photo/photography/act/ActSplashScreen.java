package com.photo.photography.act;

import static android.Manifest.permission.ACCESS_MEDIA_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.photo.photography.AppOpenManag;
import com.photo.photography.BuildConfig;
import com.photo.photography.MyApp;
import com.photo.photography.R;
import com.photo.photography.models.AppDataModel;
import com.photo.photography.util.RatingDialogs;
import com.photo.photography.util.StringUtil;
import com.photo.photography.util.helper.UserHelpers;
import com.photo.photography.util.preferences.Prefs;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.facebook.ads.AdSettings;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;

public class ActSplashScreen extends ActBase {

    public final static String ACTION_OPEN_ALBUM = "com.photogallery.photography.OPEN_ALBUM";
    public static final int PICK_MEDIA_REQUEST = 44;
    private boolean pickMode = false;
    private boolean isAppOpenAdLoad = false;
    private boolean isRemoteDataComing = false;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    public static AppDataModel parseAppUserListModel(String jsonObject) {
        try {
            Gson gson = new Gson();
            TypeToken<AppDataModel> token = new TypeToken<AppDataModel>() {
            };
            AppDataModel couponModel = gson.fromJson(jsonObject, token.getType());
            return couponModel;
        } catch (Exception e) {
            Log.e("TAG", "Error : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.MODE_NIGHT_NO == Prefs.getTheme()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (AppCompatDelegate.MODE_NIGHT_YES == Prefs.getTheme()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_splash);
        UserHelpers.setClickCount(0);
        hideSystemBars();
        Log.e("AppStartFrom", "Splash OnCreate");

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        UserHelpers.setUserInSplashIntro(true);
        UserHelpers.setOpenAdsShowCount(0);

        setAnimation();
        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake_verticale);
        Animation shake2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake_verticale2);
        findViewById(R.id.ivRow1).startAnimation(shake);
        findViewById(R.id.ivRow3).startAnimation(shake);

        findViewById(R.id.linRow2).startAnimation(shake2);
        findViewById(R.id.ivRow4).startAnimation(shake2);




        if (getIntent().getAction() != null) {
            pickMode = getIntent().getAction().equals(Intent.ACTION_GET_CONTENT) || getIntent().getAction().equals(Intent.ACTION_PICK);
        }

        if (getIntent().getAction() != null && getIntent().getAction().equals(ACTION_OPEN_ALBUM)) {
            Bundle data = getIntent().getExtras();
            if (data != null) {
                String ab = data.getString("albumPath");
                if (ab != null) {
                    Log.e("AppStartFrom", "Splash start 1");
                    start();
                }
            } else StringUtil.showToast(getApplicationContext(), "Album not found");
        } else {  // default intent
            Log.e("AppStartFrom", "Splash start 1");
            start();
        }

    }

    private void setAnimation() {
    }

    public Class getClassForNextMove() {

        if (!isPermissionGranted()) {
            return ActPermission.class;
        } else {
            return ActMain.class;
        }
    }

    public boolean isPermissionGranted() {
        int cameraePermission = ContextCompat.checkSelfPermission(ActSplashScreen.this, CAMERA);
        int readPermission = ContextCompat.checkSelfPermission(ActSplashScreen.this, READ_EXTERNAL_STORAGE);
        int accessMediaPermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            accessMediaPermission = ContextCompat.checkSelfPermission(ActSplashScreen.this, ACCESS_MEDIA_LOCATION);
        }
        return cameraePermission == PackageManager.PERMISSION_GRANTED
                && accessMediaPermission == PackageManager.PERMISSION_GRANTED
                && readPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void start() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(RatingDialogs.SHOW_LATER, false);
        editor.apply();

        if (SupportClass.isInternetAvailable(this)) {
            remoteConfig();
            new Handler(Looper.myLooper()).postDelayed(() -> {
                if (!isRemoteDataComing && !isFinishing() && !UserHelpers.getRemoteData().isEmpty()) {
                    Log.e("From: ", "TimeOut");
                    toHome(UserHelpers.getRemoteData());
                }
            }, 10 * 1000L);

        } else {
            new Handler(Looper.myLooper()).postDelayed(() -> {
                if (!isAppOpenAdLoad) {
                    startActivity(new Intent(ActSplashScreen.this, getClassForNextMove()));
                    finish();
                }
            }, 2000);
        }
    }

    public void remoteConfig() {



        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(30).build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this, task -> {
            isRemoteDataComing = true;
            if (task.isSuccessful()) {
                String response = mFirebaseRemoteConfig.getString("gallery_v1");
                Log.e("RemoteData: ", response);
                if (!response.isEmpty()) {
                    toHome(response);
                } else {
                    startActivity(new Intent(ActSplashScreen.this, getClassForNextMove()));
                    finish();
                }
            }
        }).addOnFailureListener(e -> {
            Log.e("Splash: ", "RemoteData: " + e.getMessage());
            startActivity(new Intent(ActSplashScreen.this, getClassForNextMove()));
            finish();
        });
    }

    public void toHome(String response) {
        UserHelpers.setRemoteData(response);
        AppDataModel appData = parseAppUserListModel(response);

        UserHelpers.setAppData(response);
        assert appData != null;
        UserHelpers.setIsAdEnable(appData.getIs_ad_enable());
        UserHelpers.setAdsPerClick(appData.getAds_per_click());
        UserHelpers.setInAppReview(appData.getIn_appreview());
        UserHelpers.setAdType(appData.getAdtype());
        UserHelpers.setAdsPerSession(appData.getAdsPerSession());
        UserHelpers.setExitAdEnable(appData.getExitAdEnable());
        UserHelpers.setReviewCount(0);
        UserHelpers.setOpenAdsCount(appData.getApp_open_ad_count());
        UserHelpers.setOpenAdsSplash(appData.getApp_open_ad_splash());
        UserHelpers.setSplashScreenWaitCount(appData.getSplash_screen_wait_count());
        UserHelpers.setReviewPopupCount(appData.getReview_popup_count());
        UserHelpers.setDirectReviewEnable(appData.getDirect_review_enable());
        UserHelpers.setAlbumClickEnabled(appData.getAlbum_click_enabled());


        Log.e("AppStartFrom", "from SplashScreen");

        Intent intent = new Intent(ActSplashScreen.this, getClassForNextMove());
        if (pickMode) {
            intent.putExtra(ActMain.ARGS_PICK_MODE, true);
            startActivityForResult(intent, PICK_MEDIA_REQUEST);
        } else {
            loadAds();

            new Handler(Looper.myLooper()).postDelayed(() -> {
                if (!isAppOpenAdLoad) {
                    startActivity(intent);
                    finish();
                }
            }, UserHelpers.getSplashScreenWaitCount() * 1000L);
        }
    }

    public void loadAds() {
        MobileAds.initialize(this, initializationStatus -> {
            boolean isAdEnable = UserHelpers.isAdEnable();
            if (UserHelpers.isAdEnable()) {

                // Change your Id
                RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("")).build();
                MobileAds.setRequestConfiguration(configuration);
                AdSettings.addTestDevice("");

                MyApp.getApplication().loadAdmobNativeAd();
                AppOpenManag appLifecycleObserver = new AppOpenManag(MyApp.getApplication());
                ProcessLifecycleOwner.get().getLifecycle().addObserver(appLifecycleObserver);
                loadOpenAppAdsOnSplash();
            }
        });
    }

    public void loadOpenAppAdsOnSplash() {

        if (!UserHelpers.isAdEnable()) {
            return;
        }

        if (!UserHelpers.isOpenApp()) {
            return;
        }

        if (!SupportClass.checkConnection(this)) {
            return;
        }

        if (SupportClass.checkConnection(this) && UserHelpers.getOpenAdsSplash() == 1) {
            String adUnitId = "";
            if (BuildConfig.DEBUG) {
                Log.e("Ads: ", "Load Open App class");
                adUnitId = getString(R.string.admob_open_Ad_test);
            } else {
                adUnitId = MyApp.getSettings().getOpenadid();
            }
            if (adUnitId == null) {
                return;
            }
            if (adUnitId.isEmpty()) {
                return;
            }
            Log.e("Ads: ", "Load Open App class");
            AppOpenAd.load(this, adUnitId, new AdRequest.Builder().build(), AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, new AppOpenAd.AppOpenAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull AppOpenAd ad) {
                    isAppOpenAdLoad = true;
                    if (!isFinishing() && !isDestroyed()) {
                        FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                UserHelpers.setFullScreenIsInView(false);

                                startActivity(new Intent(ActSplashScreen.this, getClassForNextMove()));
                                finish();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                UserHelpers.setFullScreenIsInView(false);
                                startActivity(new Intent(ActSplashScreen.this, getClassForNextMove()));
                                finish();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                UserHelpers.setFullScreenIsInView(true);
                            }
                        };
                        ad.setFullScreenContentCallback(fullScreenContentCallback);
                        ad.show(ActSplashScreen.this);
                        UserHelpers.setFullScreenIsInView(true);
                    }
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    isAppOpenAdLoad = true;
                    startActivity(new Intent(ActSplashScreen.this, getClassForNextMove()));
                    finish();
                }
            });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_MEDIA_REQUEST) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, data);
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}