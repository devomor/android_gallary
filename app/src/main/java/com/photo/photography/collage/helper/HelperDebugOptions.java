package com.photo.photography.collage.helper;

import android.content.Context;

import com.photo.photography.BuildConfig;

public class HelperDebugOptions {
    public static final boolean ENABLE_LOG = BuildConfig.DEBUG;

    public static final boolean ENABLE_DEBUG = BuildConfig.DEBUG;

    public static final boolean ENABLE_FOR_DEV = true;

    public static boolean isProVersion(Context context) {
        if (context == null) {
            return false;
        }

        final String packageName = BuildConfig.APPLICATION_ID;
//        final String packageName = context.getPackageName();
        //&& CommonUtils.isStoreVersion(PhotoCollageApp.getAppContext());
        return packageName != null && packageName.equals("com.photogallery.photography.collageMain");
    }
}
