package com.photo.photography.act;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.photo.photography.BuildConfig;
import com.photo.photography.R;
import com.photo.photography.util.DeviceUtil;
import com.photo.photography.util.Measures;


import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("ResourceAsColor")
public class ActCollageView extends ActSharedMedia {

    private static final String TAG = ActCollageView.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_collage)
    ImageView tv_collage;

    String filePath;

    public static Uri getFileUri(Activity activity, String filePath) {
        Uri outputUri;
        if (Build.VERSION.SDK_INT >= 24) {
            outputUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", new File(filePath));
//            outputUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", new File(filePath));
        } else {
            outputUri = Uri.fromFile(new File(filePath));
        }
        return outputUri;
    }

    public static void shareWithOther(Context mContext, String shareMessage, Uri imgUri) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.app_name_new));
        if (imgUri != null) {
            sharingIntent.setType("*/*");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
        } else {
            sharingIntent.setType("text/plain");
        }
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        mContext.startActivity(Intent.createChooser(sharingIntent, mContext.getResources().getString(R.string.share_using)));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_collage_media);
        ButterKnife.bind(this);
        filePath = getIntent().getStringExtra("reslutUri");

        // Init and Load Ads
        loadBannerAds(findViewById(R.id.adContainerView));
        // showFullScreenAdsNow(CollageViewActivity.this);
        initUi();
        Glide.with(ActCollageView.this)
                .load(filePath)
                .thumbnail(0.5f)
                .into(tv_collage);
    }

    private void initUi() {
        setSupportActionBar(toolbar);
        toolbar.bringToFront();
        Drawable nav = ContextCompat.getDrawable(this, R.drawable.e_back);
        if (nav != null) {
            nav.setTint(ContextCompat.getColor(this, R.color.black));
            toolbar.setNavigationIcon(nav);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(getApplicationContext()).clearMemory();
        Glide.get(getApplicationContext()).trimMemory(TRIM_MEMORY_COMPLETE);
        System.gc();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.collage_view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                final Dialog dialog = new Dialog(ActCollageView.this);
                dialog.setContentView(R.layout.delete_dialog_views);
                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
                dialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);


                dialog.findViewById(R.id.tvYes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        File file = new File(filePath);
                        file.delete();
                        Toast.makeText(ActCollageView.this, getString(R.string.txt_file_delete_successfully), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

                dialog.findViewById(R.id.tvNo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                return true;
            case R.id.action_share:
                shareWithOther(ActCollageView.this, getString(R.string.txt_app_share_info), getFileUri(this, filePath));

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (DeviceUtil.isLandscape(getResources()))
            params.setMargins(0, 0, Measures.getNavigationBarSize(ActCollageView.this).x, 0);
        else
            params.setMargins(0, 0, 0, 0);

        toolbar.setLayoutParams(params);
    }

    private void updateBrightness(float level) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = level;
        getWindow().setAttributes(lp);
    }

//    @Override
//    public void setNavBarColor() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if (themeOnSingleImgAct())
//                if (isNavigationBarColored())
//                    getWindow().setNavigationBarColor(ColorPalette.getTransparentColor(getPrimaryColor(), getTransparency()));
//                else
//                    getWindow().setNavigationBarColor(ColorPalette.getTransparentColor(ContextCompat.getColor(getApplicationContext(), R.color.md_black_1000), getTransparency()));
//            else
//                getWindow().setNavigationBarColor(ColorPalette.getTransparentColor(ContextCompat.getColor(getApplicationContext(), R.color.md_black_1000), 175));
//        }
//    }

//    @Override
//    protected void setStatusBarColor() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if (themeOnSingleImgAct())
//                if (isTranslucentStatusBar() && isTransparencyZero())
//                    getWindow().setStatusBarColor(ColorPalette.getObscuredColor(getPrimaryColor()));
//                else
//                    getWindow().setStatusBarColor(ColorPalette.getTransparentColor(getPrimaryColor(), getTransparency()));
//            else
//                getWindow().setStatusBarColor(ColorPalette.getTransparentColor(
//                        ContextCompat.getColor(getApplicationContext(), R.color.md_black_1000), 175));
//        }
//    }


    private void hideSystemUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator())
                        .setDuration(200).start();

                getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        Log.wtf(TAG, "ui changed: " + visibility);
                    }
                });
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_IMMERSIVE);

//                changeBackGroundColor();
            }
        });
    }

    private void setupSystemUI() {
        toolbar.animate().translationY(Measures.getStatusBarHeight(getResources())).setInterpolator(new DecelerateInterpolator())
                .setDuration(0).start();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void showSystemUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                toolbar.animate().translationY(Measures.getStatusBarHeight(getResources())).setInterpolator(new DecelerateInterpolator())
                        .setDuration(240).start();

                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

//                changeBackGroundColor();
            }
        });
    }

//    private void changeBackGroundColor() {
//        int colorTo;
//        int colorFrom;
//      /*  if (fullScreenMode) {
//            colorFrom = getBackgroundColor();
//            colorTo = (ContextCompat.getColor(EditMediaActivity.this, R.color.md_black_1000));
//        } else {*/
//        colorFrom = (ContextCompat.getColor(CollageViewActivity.this, R.color.md_black_1000));
//        colorTo = getBackgroundColor();
//        // }
//        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
//        colorAnimation.setDuration(240);
//        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animator) {
//                //  activityBackground.setBackgroundColor((Integer) animator.getAnimatedValue());
//            }
//        });
//        colorAnimation.start();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

