package com.photo.photography.act.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.photo.photography.util.configs.AppLog;
import com.photo.photography.util.utils.BigDAdsHelpers;

import java.util.HashMap;

public class PackageInstalleReceiver extends BroadcastReceiver {
    public static String clickedApp = null;
    public static HashMap<String, Boolean> reportedMap = new HashMap<>();

    @Override
    public void onReceive(final Context context, final Intent intent) {
        // when package removed
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            AppLog.d(" PackageInstallReceiver ", "onReceive called " + " PACKAGE_REMOVED, packageName = " + intent.getData());

        } else if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            String packageName = intent.getDataString();
            if (packageName != null && packageName.length() > 0 && packageName.startsWith("package")) {
                packageName = packageName.substring("package:".length());
            }

            AppLog.d(" PackageInstallReceiver ", "onReceive called " + "PACKAGE_ADDED, installedPackageName=" + packageName);
            if (packageName != null && packageName.length() > 0
                    && clickedApp != null && reportedMap.get(clickedApp) != Boolean.TRUE
                    && clickedApp.contains(packageName)) {
                BigDAdsHelpers.addInstalledApp(packageName);
                Bundle bundle = new Bundle();
               /* bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "installed/".concat(packageName));
                FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);*/
                reportedMap.put(clickedApp, Boolean.TRUE);
            }
        }
    }
}
