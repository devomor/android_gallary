package com.photo.photography.util;

import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;

import com.photo.photography.BuildConfig;

/**
 * A Chrome Custom Tabs wrapper to preload and show URLs in Chrome Custom Tabs.
 * Also provides a static method to launch a tab directly without warm up.
 */
public class ChromeCustomTab {

    private final Context context;
    private CustomTabsServiceConnection serviceConnection;
    private CustomTabsIntent mCustomTabsIntent;

    public ChromeCustomTab(@NonNull Context context) {
        this.context = context;
        initService();
    }

    /**
     * Launches a Chrome Custom Tab without warmup / service.
     *
     * @param context The context - used for launching an Activity.
     * @param url     The URL to load.
     */
    public static void launchUrl(@NonNull Context context, @NonNull String url) {
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }

    private void initService() {

        serviceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                customTabsClient.warmup(0L);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                // NO-OP
            }
        };

        // Bind the Chrome Custom Tabs service
        CustomTabsClient.bindCustomTabsService(context, BuildConfig.APPLICATION_ID, serviceConnection);
//        CustomTabsClient.bindCustomTabsService(context, ApplicationUtils.getPackageName(), serviceConnection);

        mCustomTabsIntent = new CustomTabsIntent.Builder()
                .setShowTitle(true)
                .build();
    }

    public void launchUrl(String Url) {
        mCustomTabsIntent.launchUrl(context, Uri.parse(Url));
    }

    /**
     * Allow the Chrome Custom Tabs service to disconnect and GC.
     */
    public void destroy() {
        context.unbindService(serviceConnection);
    }
}
