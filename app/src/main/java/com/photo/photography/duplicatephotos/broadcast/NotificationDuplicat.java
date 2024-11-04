package com.photo.photography.duplicatephotos.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import com.photo.photography.R;
import com.photo.photography.duplicatephotos.act.ActDuplicateHomeMain;
import com.photo.photography.duplicatephotos.backgroundasynk.GroupExactDuplicat;
import com.photo.photography.duplicatephotos.backgroundasynk.GroupSimilarDuplicat;
import com.photo.photography.duplicatephotos.common.CommonUsed;
import com.photo.photography.duplicatephotos.common.GlobalVarsAndFunction;
import com.photo.photography.duplicatephotos.callback.CallbackEveryDayScan;
import com.photo.photography.duplicatephotos.services.ServiceDeviceLockMonitor;
import com.photo.photography.util.utilsEdit.SupportClass;

public class NotificationDuplicat extends BroadcastReceiver implements CallbackEveryDayScan {
    PowerManager.WakeLock wakeLock;
    private Context nContext;

    public void onReceive(Context context, Intent intent) {
        this.nContext = context;
        CommonUsed.logmsg("bcr noti");
        this.nContext.getSharedPreferences("myflag", 0);
        if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
            CommonUsed.logmsg("/***************Device Active**************/");
            GlobalVarsAndFunction.IS_DEVICE_ACTIVE = true;
        } else if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
            CommonUsed.logmsg("/***************Device InActive**************/");
            CommonUsed.logmsg("isInActive: " + GlobalVarsAndFunction.IS_DEVICE_ACTIVE);
            CommonUsed.logmsg("Device lock: " + GlobalVarsAndFunction.DEVICE_UNLOCK);
            if (GlobalVarsAndFunction.DEVICE_UNLOCK && GlobalVarsAndFunction.IS_DEVICE_ACTIVE) {
                scanForDuplicates(this.nContext);
            }
        }
    }

    private void scanForDuplicates(Context context) {
        GlobalVarsAndFunction.DEVICE_UNLOCK = false;
        context.stopService(new Intent(context, ServiceDeviceLockMonitor.class));
        CommonUsed.logmsg("Start Background scan!!!");
        Log.e("scanForDuplicates", "---asdfasd-machinglevel----" + GlobalVarsAndFunction.getCurrentMatchingLevel(context));
        GlobalVarsAndFunction.setCorrespondingValueForMatchingLevels(GlobalVarsAndFunction.getCurrentMatchingLevel(context));
        GroupExactDuplicat groupExactDuplicates = new GroupExactDuplicat(context, this);
        GroupSimilarDuplicat groupSimilarDuplicates = new GroupSimilarDuplicat(context, this);
        if (Build.VERSION.SDK_INT >= 11) {
            groupSimilarDuplicates.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            groupSimilarDuplicates.execute();
        }
        if (Build.VERSION.SDK_INT >= 11) {
            groupExactDuplicates.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            groupExactDuplicates.execute();
        }
    }

    @Override
    public void everyDayScan() {
        int notificationLimit = GlobalVarsAndFunction.getNotificationLimit(this.nContext) * 10;
        CommonUsed.logmsg("Notification Limit: " + notificationLimit);
        CommonUsed.logmsg("Exact images Background: " + GlobalVarsAndFunction.getExactDuplicateInOneDay() + " Similar images background: " + GlobalVarsAndFunction.getSimilarDuplicatesInOneDay());
        CommonUsed.logmsg("Exact completed: " + GlobalVarsAndFunction.EVERY_DAY_SCAN_EXACT + " Similar Complete: " + GlobalVarsAndFunction.EVERY_DAY_SCAN_SIMILAR);
        if (GlobalVarsAndFunction.EVERY_DAY_SCAN_EXACT && GlobalVarsAndFunction.EVERY_DAY_SCAN_SIMILAR) {
            if (GlobalVarsAndFunction.getExactDuplicateInOneDay() >= notificationLimit || GlobalVarsAndFunction.getSimilarDuplicatesInOneDay() >= notificationLimit) {
                notifyDuplicates(this.nContext);
            }
        }
    }

    private void notifyDuplicates(Context context) {
        Intent intent = new Intent(context, ActDuplicateHomeMain.class);
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        PendingIntent activity = PendingIntent.getActivity(context, 0, intent, SupportClass.returnPendingIntentFlag(0));
        Notification.Builder builder = new Notification.Builder(context);
        Notification.Builder contentTitle = builder.setContentTitle("Duplicate Photos Remover");
        contentTitle.setContentText(context.getString(R.string.We_found_more_than) + (GlobalVarsAndFunction.getNotificationLimit(context) * 10) + context.getString(R.string.duplicates_photos_in_your_mobile))
                .setSmallIcon(GlobalVarsAndFunction.getNotificationIcon()).setPriority(Notification.PRIORITY_HIGH).setAutoCancel(true).setContentIntent(activity);
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, builder.build());
    }

}
