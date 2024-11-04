package com.photo.photography.util.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.LayoutInflater;
import android.view.View;

import com.photo.photography.R;

import java.util.List;


public class BigDAdsHelpers {
    private static List<ApplicationInfo> installedApps;

    private final View mBigDAdsLayout;

    public BigDAdsHelpers(Context context) {
        mBigDAdsLayout = LayoutInflater.from(context).inflate(R.layout.big_ads_banner, null);
    }

    public static void clearInstalledApp() {
        if (installedApps != null) installedApps.clear();
        installedApps = null;
    }

    public static void addInstalledApp(String packageName) {
        if (installedApps != null) {
            ApplicationInfo info = new ApplicationInfo();
            info.packageName = packageName;
            installedApps.add(info);
        }
    }

    public boolean isVisible() {
        return mBigDAdsLayout.getVisibility() == View.VISIBLE;
    }

}
