package com.photo.photography.duplicatephotos.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.photo.photography.duplicatephotos.backgroundasynk.ScheduleScan;
import com.photo.photography.duplicatephotos.common.CommonUsed;
import com.photo.photography.duplicatephotos.common.GlobalVarsAndFunction;

public class DeviceBootMonitor extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            CommonUsed.logmsg("Hey, device is rebooted!!!");
            GlobalVarsAndFunction.setInitiateScanningPermission(context, false);
            try {
                context.startService(new Intent(context, ScheduleScan.class));
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }
}
