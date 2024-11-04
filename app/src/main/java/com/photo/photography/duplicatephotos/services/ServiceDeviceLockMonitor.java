package com.photo.photography.duplicatephotos.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;

import com.photo.photography.duplicatephotos.broadcast.NotificationDuplicat;
import com.photo.photography.duplicatephotos.common.CommonUsed;
import com.photo.photography.duplicatephotos.common.GlobalVarsAndFunction;

import java.util.Calendar;

public class ServiceDeviceLockMonitor extends Service {
    Context dLSContext;
    BroadcastReceiver mReceiver;

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.dLSContext = getApplicationContext();
        CommonUsed.logmsg("DeviceLockMonitorService started!!!");
        if (GlobalVarsAndFunction.properScheduleStartScanningFunction(this.dLSContext, Calendar.getInstance().getTimeInMillis())) {
            GlobalVarsAndFunction.DEVICE_UNLOCK = true;
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            this.mReceiver = new NotificationDuplicat();
            registerReceiver(this.mReceiver, intentFilter);
            GlobalVarsAndFunction.IS_DEVICE_ACTIVE = checkDeviceActive();
            return;
        }
        stopSelf();
    }

    private boolean checkDeviceActive() {
        getApplicationContext();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= 20) {
            return powerManager.isInteractive();
        }
        return powerManager.isScreenOn();
    }

    public void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy", "------destroy--service---");
        try {
            unregisterReceiver(this.mReceiver);
        } catch (Exception unused) {
        }
    }
}
