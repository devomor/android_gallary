package com.photo.photography.act;

import static com.photo.photography.util.RatingDialogs.SHOW_NEVER;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.photo.photography.MyApp;
import com.photo.photography.BuildConfig;
import com.photo.photography.R;
import com.photo.photography.edit_views.Constants;
import com.photo.photography.util.helper.UserHelpers;
import com.photo.photography.util.RatingDialogs;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ActBase extends AppCompatActivity {
    public static String MyPrefs = "RatingDialog";
    CompositeDisposable disposables = new CompositeDisposable();

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
    }

    public void disposeLater(Disposable disposable) {
        disposables.add(disposable);
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        disposables.dispose();
        super.onDestroy();
    }

    public void restartScreenApp() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    public void restartHomeScreen() {
        startActivity(new Intent(this, ActMain.class));
        finishAffinity();
    }

    public void hideSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }
        // Configure the behavior of the hidden system bars
        windowInsetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
    }

    private void ShowSnackbar(String message) {
        //finish();
    }

    public void showRate() {
        if (UserHelpers.getInAppReview() == 1) {
            SharedPreferences sharedpreferences = getSharedPreferences(RatingDialogs.MyPrefs, Context.MODE_PRIVATE);
            if (sharedpreferences.getBoolean(SHOW_NEVER, false)) {
                return;
            }
            if (!UserHelpers.getRateDone()) {
                if (UserHelpers.getDirectReviewEnable() == 1) {
                    UserHelpers.setRateDone(true);
                    MyApp.getApplication().inAppReview(ActBase.this, feedback -> ShowSnackbar(""), view -> {
                    });
                } else {
                    MyApp.getApplication().showRatingDialog(ActBase.this, feedback -> ShowSnackbar(""), view -> {
                    });
                }
            } else {
                MyApp.getApplication().showRatingDialog(ActBase.this, feedback -> ShowSnackbar(""), view -> {
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
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    @SuppressLint("MissingPermission")
    public void loadBannerAds(FrameLayout adContainerView) {

        AdView mAdView = new AdView(this);

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

        AdSize adSize = getAdSize(this);
        mAdView.setAdSize(adSize);

        if (SupportClass.checkConnection(this)) {
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
}
