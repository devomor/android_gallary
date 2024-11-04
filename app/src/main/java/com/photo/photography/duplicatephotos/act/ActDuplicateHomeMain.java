package com.photo.photography.duplicatephotos.act;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import com.photo.photography.MyApp;
import com.photo.photography.R;
import com.photo.photography.duplicatephotos.backgroundasynk.ScheduleScan;
import com.photo.photography.duplicatephotos.common.CommonUsed;
import com.photo.photography.duplicatephotos.common.GlobalVarsAndFunction;
import com.photo.photography.ads_notifier.AdsEventNotifier;
import com.photo.photography.ads_notifier.AdsEventState;
import com.photo.photography.ads_notifier.AdsEventTypes;
import com.photo.photography.ads_notifier.AdsIEventListener;
import com.photo.photography.ads_notifier.AdsListenerPriority;
import com.photo.photography.ads_notifier.AdsNotifierFactory;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.Calendar;

public class ActDuplicateHomeMain extends AppCompatActivity implements AdsIEventListener {
    private RelativeLayout rel_ads;
    private Context sSContext;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.act_duplicate_photo_main);

        if (AppCompatDelegate.MODE_NIGHT_NO == GlobalVarsAndFunction.getAppTheme(ActDuplicateHomeMain.this)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (AppCompatDelegate.MODE_NIGHT_YES == GlobalVarsAndFunction.getAppTheme(ActDuplicateHomeMain.this)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        registerAdsListener();
        CommonUsed.logmsg("In Splash Screen!!!!!");
        this.sSContext = getApplicationContext();
        startEveryDayScan();
        initUi();
        if (GlobalVarsAndFunction.CURRENT_OS_VERSION <= 22) {

        } else if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0
                && ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            CommonUsed.logmsg("Notify flag -2: " + GlobalVarsAndFunction.getNotifyDupesFlag(getApplicationContext()));

        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, 1);
        }
    }

    private void initUi() {
        rel_ads = findViewById(R.id.rel_ads);


        loadNativeAds();


        TextView scan = findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (SupportClass.isExternalStoragePermissionGranted(ActDuplicateHomeMain.this)) {
                    callScanningActivity();
                } else {
                    SupportClass.showTakeWritePermissionDialog(ActDuplicateHomeMain.this);
                }
            }
        });
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

    private void startEveryDayScan() {
        if (!GlobalVarsAndFunction.getOneTimeSettingInstallationFlag(this.sSContext)) {
            try {
                Calendar instance = Calendar.getInstance();
                GlobalVarsAndFunction.setInitiateTimeForAlarm(this.sSContext, instance.getTimeInMillis());
                startService(new Intent(getApplicationContext(), ScheduleScan.class));
                GlobalVarsAndFunction.setOneTimeSettingInstallationFlag(this.sSContext, true);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerAdsListener() {
        AdsEventNotifier
                notifier = AdsNotifierFactory.getInstance().getNotifier(AdsNotifierFactory.EVENT_NOTIFIER_AD_STATUS);
        notifier.registerListener(this, AdsListenerPriority.PRIORITY_HIGH);
    }

    public void loadNativeAds() {
        if (MyApp.getInstance().getGNativeHome() != null && MyApp.getInstance().getGNativeHome().size() > 0 && MyApp.getInstance().getGNativeHome().get(0) != null) {
            NativeAdView adView = (NativeAdView) LayoutInflater.from(ActDuplicateHomeMain.this).inflate(R.layout.ads_unified, null);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(requestCode, strArr, iArr);
        if (requestCode == 591) {
            if (SupportClass.isExternalStoragePermissionGranted(ActDuplicateHomeMain.this)) {
                callScanningActivity();
            } else {
                Toast.makeText(ActDuplicateHomeMain.this, getString(R.string.require_permission_for_this_operation), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void callScanningActivity() {
        if (!GlobalVarsAndFunction.checkStorage(this.sSContext)) {
            CommonUsed.showmsg(this.sSContext, "No storage found.");
            return;
        }
        GlobalVarsAndFunction.setCancelFlag(this.sSContext, false);
        GlobalVarsAndFunction.setMemoryRegainedExact("");
        GlobalVarsAndFunction.setMemoryRegainedSimilar("");
        GlobalVarsAndFunction.setTotalDuplicatesExact(0);
        GlobalVarsAndFunction.setTotalDuplicatesSimilar(0);

        Bundle bundle = new Bundle();
        bundle.putString(com.photo.photography.secure_vault.helper.Constants.DuplicatePhotosStart, com.photo.photography.secure_vault.helper.Constants.DuplicatePhotosStart);

        Intent intent = new Intent(this, ActScanning.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (MyApp.getInstance().needToShowAd()) {
            MyApp.getInstance().showInterstitial(this, intent, true, -1, null);
        } else {
            startActivity(intent, ActivityOptionsCompat.makeCustomAnimation(this.sSContext, R.anim.anim_slide_in_right, R.anim.anim_slide_out_left).toBundle());
            finish();
        }


    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
        CommonUsed.logmsg("Orientation change in Splash Screen!!!!");
        setContentView(R.layout.act_duplicate_photo_main);
        initUi();
    }

}
