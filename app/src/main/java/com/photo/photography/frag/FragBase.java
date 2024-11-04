package com.photo.photography.frag;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.photo.photography.MyApp;
import com.photo.photography.BuildConfig;
import com.photo.photography.R;
import com.photo.photography.edit_views.Constants;
import com.photo.photography.util.helper.UserHelpers;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;

/**
 * Base Fragment for abstraction logic.
 */
public abstract class FragBase extends Fragment {

    protected Activity mActivity;
    private NothingToShowCallback nothingToShowListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        if (context instanceof NothingToShowCallback)
            nothingToShowListener = (NothingToShowCallback) context;
    }

    public NothingToShowCallback getNothingToShowListener() {
        return nothingToShowListener;
    }

    public void setNothingToShowListener(NothingToShowCallback nothingToShowListener) {
        this.nothingToShowListener = nothingToShowListener;
    }

    @SuppressLint("MissingPermission")
    public void loadBannerAds(Activity context,FrameLayout adContainerView) {
//        adContainerView.setVisibility(View.GONE);

        if (!UserHelpers.isAdEnable()) {
            return;
        }
//        if (!UserHelper.isBanner()) {
//            return;
//        }

        AdView mAdView = new AdView(context);

        String adId = "";
        if (BuildConfig.DEBUG) {
            adId = getString(R.string.admob_banner_ads_id_test);
        } else {
            adId = MyApp.getSettings().getBannerid();
        }
        if (adId == null) {
            return;
        }
        if (adId.isEmpty()) {
            return;
        }

        mAdView.setAdUnitId(adId);

        adContainerView.removeAllViews();
        adContainerView.addView(mAdView);

        AdSize adSize = getAdSize(context);
        mAdView.setAdSize(adSize);

        if (SupportClass.checkConnection(context)) {
            if (UserHelpers.getAdType().equals(Constants.ADMOB)) {

                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
                mAdView.setAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Log.e("ADSTAG", "Banner onAdFailedToLoad()" + loadAdError.getCode());
                    }

                    @Override
                    public void onAdLoaded() {
                        Log.e("ADSTAG", "Banner onAdLoaded()");
                        adContainerView.setVisibility(View.VISIBLE);
                    }
                });
            } else {

                AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
                mAdView.loadAd(adRequest);
                mAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.e("ADSTAG", "Banner onAdFailedToLoad()" + loadAdError.getCode());
                    }

                    @Override
                    public void onAdLoaded() {
                        Log.e("ADSTAG", "Banner onAdLoaded()");
                        adContainerView.setVisibility(View.VISIBLE);
                    }
                });
            }
        }
    }

    private AdSize getAdSize(Activity activity) {

        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }
}
