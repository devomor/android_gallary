package com.photo.photography.util.utils;

import static com.photo.photography.util.utilsEdit.SupportClass.returnPendingIntentFlag;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.photo.photography.R;
import com.photo.photography.act.ActMain;
import com.photo.photography.util.configs.AppLog;
import com.photo.photography.BuildConfig;


public class CommonUtil {
    private static final String TAG = CommonUtil.class.getSimpleName();

    public static boolean isStoreVersion(Context context) {
        boolean result = false;

        try {
//            String installer = context.getPackageManager()
//                    .getInstallerPackageName(context.getPackageName());
            String installer = context.getPackageManager()
                    .getInstallerPackageName(BuildConfig.APPLICATION_ID);
            result = (installer != null && installer.trim().length() > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getDeviceId(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice = tm.getDeviceId();
        String androidId = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
        String serial = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) serial = Build.SERIAL;
        String deviceId = "0123456789";
        if (tmDevice != null) deviceId = "01" + tmDevice;
        else if (androidId != null) deviceId = "02" + androidId;
        else if (serial != null) deviceId = "03" + serial;
        AppLog.d(TAG, "deviceId=" + deviceId);
        return deviceId;
    }

    public static void doRestart(Context c) {
        try {
            //check if the context is given
            if (c != null) {
                //fetch the packagemanager so we can get the default launch activity
                // (you can replace this intent with any other activity if you want
                PackageManager pm = c.getPackageManager();
                //check if we got the PackageManager
                if (pm != null) {
                    //create the intent with the default start activity for your application
                    Intent mStartActivity = pm.getLaunchIntentForPackage(
                            BuildConfig.APPLICATION_ID
                    );
//                    Intent mStartActivity = pm.getLaunchIntentForPackage(
//                            c.getPackageName()
//                    );
                    if (mStartActivity != null) {
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //create a pending intent so the application is restarted after System.exit(0) was called.
                        // We use an AlarmManager to call this intent in 100ms
                        int mPendingIntentId = 223344;
                        PendingIntent mPendingIntent;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            mPendingIntent = PendingIntent.getActivity(c, mPendingIntentId, mStartActivity, returnPendingIntentFlag(PendingIntent.FLAG_MUTABLE));
                        }else {
                            mPendingIntent = PendingIntent.getActivity(c, mPendingIntentId, mStartActivity, returnPendingIntentFlag(PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_MUTABLE));
                        }
                        AlarmManager mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        //kill the application
                        if (c instanceof ActMain) {
                            ((Activity) c).finish();
                        } else {
                            System.exit(0);
                        }
                    } else {
                        AppLog.e(TAG, "Was not able to restart application, mStartActivity null");
                    }
                } else {
                    AppLog.e(TAG, "Was not able to restart application, PM null");
                }
            } else {
                AppLog.e(TAG, "Was not able to restart application, Context null");
            }
        } catch (Exception ex) {
            AppLog.e(TAG, "Was not able to restart application");
        }
    }

    public static void startScreenDisplayAnimation(final Context mContext, final RelativeLayout relatveAnimation,
                                                   final ImageView ivFeatures, Drawable drawable) {
        ivFeatures.setImageDrawable(drawable);
        Animation mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.anim_fade_in_animation);
        Animation mAnimation2 = AnimationUtils.loadAnimation(mContext, R.anim.anim_zoom_out);
        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                relatveAnimation.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mAnimation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivFeatures.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        relatveAnimation.startAnimation(mAnimation);
        ivFeatures.startAnimation(mAnimation2);
    }
}
