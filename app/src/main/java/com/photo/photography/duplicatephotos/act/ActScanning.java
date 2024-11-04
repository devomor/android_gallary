package com.photo.photography.duplicatephotos.act;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import com.photo.photography.MyApp;
import com.photo.photography.R;
import com.photo.photography.duplicatephotos.AppDataBaseHandler;
import com.photo.photography.duplicatephotos.backgroundasynk.SearchExactDuplicat;
import com.photo.photography.duplicatephotos.backgroundasynk.SearchSimilarDuplicat;
import com.photo.photography.duplicatephotos.common.CommonUsed;
import com.photo.photography.duplicatephotos.common.GlobalVarsAndFunction;
import com.photo.photography.duplicatephotos.callback.CallbackSearch;
import com.photo.photography.duplicatephotos.services.ServiceKillNotifications;
import com.photo.photography.duplicatephotos.util.PopUpDialogs;
import com.photo.photography.ads_notifier.AdsEventNotifier;
import com.photo.photography.ads_notifier.AdsEventState;
import com.photo.photography.ads_notifier.AdsEventTypes;
import com.photo.photography.ads_notifier.AdsIEventListener;
import com.photo.photography.ads_notifier.AdsListenerPriority;
import com.photo.photography.ads_notifier.AdsNotifierFactory;
import com.photo.photography.secure_vault.helper.Constants;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;


public class ActScanning extends AppCompatActivity implements CallbackSearch, AdsIEventListener {
    private static final String WAKELOCKTAG = "MyWakelockTag";
    ServiceConnection mConnection;
    TextView scanningConditionTv;
    Context scanningContext;
    TextView scanningPleaseWaitTv;
    SearchExactDuplicat searchExactDuplicates;
    SearchSimilarDuplicat searchSimilarDuplicates;
    TextView updateScanningTv;
    TextView totalupdateScanningTv;
    PowerManager.WakeLock wakeLock;
    RelativeLayout rel_ads;

    public void cancelNotification() {
    }

    @TargetApi(16)
    public void showNotification() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.act_duplicate_scanning);
        createNotificationChannel();
        registerAdsListener();
        CommonUsed.logmsg("In Scanning Activity!!!!!!!!!");
        this.scanningContext = getApplicationContext();
        GlobalVarsAndFunction.setNotifyDupesFlag(this.scanningContext, true);
        cancelEveryDayNotification();
        CommonUsed.logmsg("Matching level : " + GlobalVarsAndFunction.getCurrentMatchingLevel(this.scanningContext));
        initUi();
        wakeScreenIfLockForScanning();
        startScanningAsyncTask();
    }

    @SuppressLint("InvalidWakeLockTag")
    private void wakeScreenIfLockForScanning() {
        this.wakeLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(MODE_WORLD_READABLE, WAKELOCKTAG);
        this.wakeLock.acquire(600000);
    }

    private void cancelEveryDayNotification() {
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(0);
    }

    public void onBackPressed() {
        new PopUpDialogs(this.scanningContext, this).showAlertStopScanning(this.searchSimilarDuplicates, this.searchExactDuplicates);
    }

    public void onUserLeaveHint() {
        super.onUserLeaveHint();
        CommonUsed.logmsg("App is minimized!!!!!!!!!");
        showNotification();
        this.mConnection = new ServiceConnection() {
            public void onServiceDisconnected(ComponentName componentName) {
            }

            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                ((ServiceKillNotifications.KillBinder) iBinder).service.startService(new Intent(ActScanning.this, ServiceKillNotifications.class));
                showNotification();
            }
        };
        bindService(new Intent(this, ServiceKillNotifications.class), this.mConnection, BIND_AUTO_CREATE);
    }

    public void onRestart() {
        super.onRestart();
        cancelNotification();
    }

    public void onDestroy() {
        super.onDestroy();
        ServiceConnection serviceConnection = this.mConnection;
        if (serviceConnection != null) {
            unbindService(serviceConnection);
        }
        cancelNotification();
    }
//    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    public void checkSearchFinish() {
        try {
            CommonUsed.logmsg("Exact Flag: " + GlobalVarsAndFunction.SCANNING_FLAG_EXACT + "Similar Flag: " + GlobalVarsAndFunction.SCANNING_FLAG_SIMILAR);
            if (!GlobalVarsAndFunction.SCANNING_FLAG_EXACT && !GlobalVarsAndFunction.SCANNING_FLAG_SIMILAR) {
                if (this.wakeLock.isHeld()) {
                    this.wakeLock.release();
                }

                Bundle bundle = new Bundle();
                bundle.putString(Constants.DuplicatePhotosResult, Constants.DuplicatePhotosResult);
//                mFirebaseAnalytics.logEvent(Constants.DuplicatePhotos, bundle);

                Intent intent = new Intent(this, ActPhotoList.class);
                intent.putExtra("memoryPopUpAndRecoverPopUp", "showMemoryPopUp");
                intent.putExtra("tS", "exact");
                intent.putExtra("showSimilarRegainedPopUpExact", false);
                intent.putExtra("showSimilarRegainedPopUpSimilar", false);


                if (MyApp.getInstance().needToShowAd()) {
                    MyApp.getInstance().showInterstitial(this, intent, true, -1, null);
                } else {
                    startActivity(intent, ActivityOptionsCompat.makeCustomAnimation(this.scanningContext, R.anim.anim_slide_in_right, R.anim.anim_slide_out_left).toBundle());
                    finish();
                }

            }
        } catch (IllegalArgumentException e) {
            CommonUsed.logmsg("Exception in ChechSearchFinish: " + e.getMessage());
        }
    }

    @Override
    public void updateUi(String... strArr) {
        if (strArr[0].equalsIgnoreCase("scanning")) {
            this.updateScanningTv.setText(strArr[1]);
        } else if (strArr[0].equalsIgnoreCase("sorting")) {
            this.scanningPleaseWaitTv.setText(getString(R.string.Sorting_duplicates));
            this.scanningConditionTv.setText(getString(R.string.sortingtime));
        }
    }

    @Override
    public void updateTotalFileCountUi(String str) {
        this.totalupdateScanningTv.setText(str);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        CommonUsed.logmsg("Orientation Change in Scanning Activity!!!!!!!");
        setContentView(R.layout.act_duplicate_scanning);
        initUi();
    }

    private void initUi() {
        rel_ads = findViewById(R.id.rel_ads);

        loadNativeAds();

        this.scanningPleaseWaitTv = (TextView) findViewById(R.id.scanningduplicatetext);
        this.updateScanningTv = (TextView) findViewById(R.id.numberoffileinscanning);
        this.totalupdateScanningTv = (TextView) findViewById(R.id.totalnumberoffileinscanning);
        this.scanningConditionTv = (TextView) findViewById(R.id.progressbarmessage);
    }

    private void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        try {
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        } catch (Exception e) {
            Log.e("TAG", "Error : " + e.getMessage());
            e.printStackTrace();
        }

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.getStoreView().setVisibility(View.GONE);
        adView.getPriceView().setVisibility(View.GONE);

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getMediaContent().getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {
//			videoStatus.setText(String.format(Locale.getDefault(),
//					"Video status: Ad contains a %.2f:1 video asset.",
//					vc.getAspectRatio()));

            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
//					refresh.setEnabled(true);
//					videoStatus.setText("Video status: Video playback has ended.");
                    super.onVideoEnd();
                }
            });
        } else {
//			videoStatus.setText("Video status: Ad does not contain a video asset.");
//			refresh.setEnabled(true);
        }
    }

    public void startScanningAsyncTask() {
        GlobalVarsAndFunction.setCorrespondingValueForMatchingLevels(GlobalVarsAndFunction.getCurrentMatchingLevel(this.scanningContext));
        GlobalVarsAndFunction.index = 0;
        AppDataBaseHandler dbHandler = new AppDataBaseHandler(scanningContext);
        dbHandler.removeAllRecord();
        this.searchSimilarDuplicates = new SearchSimilarDuplicat(this, this.scanningContext, this);
        this.searchExactDuplicates = new SearchExactDuplicat(this, this.scanningContext, this);
        if (Build.VERSION.SDK_INT >= 11) {
            this.searchSimilarDuplicates.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            this.searchSimilarDuplicates.execute();
        }
        if (Build.VERSION.SDK_INT >= 11) {
            this.searchExactDuplicates.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            this.searchExactDuplicates.execute();
        }
    }


    private void registerAdsListener() {
        AdsEventNotifier
                notifier = AdsNotifierFactory.getInstance().getNotifier(AdsNotifierFactory.EVENT_NOTIFIER_AD_STATUS);
        notifier.registerListener(this, AdsListenerPriority.PRIORITY_HIGH);
    }

    public void loadNativeAds() {
        if (MyApp.getInstance().getGNativeHome() != null && MyApp.getInstance().getGNativeHome().size() > 0 && MyApp.getInstance().getGNativeHome().get(0) != null) {
            NativeAdView adView = (NativeAdView) LayoutInflater.from(ActScanning.this).inflate(R.layout.ads_unified, null);
            populateUnifiedNativeAdView(MyApp.getInstance().getGNativeHome().get(0), adView);
            rel_ads.removeAllViews();
            rel_ads.addView(adView);
            rel_ads.setVisibility(View.VISIBLE);
        } else {
            rel_ads.setVisibility(View.GONE);
        }
    }

    @Override
    public int eventNotify(int eventType, final Object eventObject) {
        Log.e("Update: ", "eventNotify");
        int eventState = AdsEventState.EVENT_IGNORED;
        switch (eventType) {
            case AdsEventTypes.EVENT_AD_LOADED_NATIVE:
                Log.e("Update: ", "Case");
                eventState = AdsEventState.EVENT_PROCESSED;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadNativeAds();
                            }
                        }, 500);

                    }
                });
        }
        return eventState;
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("1", "RDPR", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("");
            notificationChannel.setSound(null, null);
            ((NotificationManager) getSystemService(NotificationManager.class)).createNotificationChannel(notificationChannel);
        }
    }
}
