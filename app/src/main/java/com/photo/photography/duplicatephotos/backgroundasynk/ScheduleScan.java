package com.photo.photography.duplicatephotos.backgroundasynk;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;

import com.photo.photography.duplicatephotos.common.GlobalVarsAndFunction;
import com.photo.photography.duplicatephotos.services.ServiceDeviceLockMonitor;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduleScan extends Service {
    PowerManager.WakeLock screenLock;
    Timer timer = new Timer();

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        this.timer.schedule(new TimerTask() {
            public void run() {
                Log.e("run", "---asdfasd-----check schedule---" + GlobalVarsAndFunction.properScheduleStartScanningFunction(ScheduleScan.this.getApplicationContext(), Calendar.getInstance().getTimeInMillis()));
                Log.e("run", "--ssss-asdfasd-----check schedule---" + GlobalVarsAndFunction.getInitiateScanningPermission(ScheduleScan.this.getApplicationContext()));
                if (!GlobalVarsAndFunction.properScheduleStartScanningFunction(ScheduleScan.this.getApplicationContext(), Calendar.getInstance().getTimeInMillis())) {
                    GlobalVarsAndFunction.setInitiateScanningPermission(ScheduleScan.this.getApplicationContext(), false);
                    ScheduleScan.this.getApplicationContext().stopService(new Intent(ScheduleScan.this.getApplicationContext(), ServiceDeviceLockMonitor.class));
                } else if (!GlobalVarsAndFunction.getInitiateScanningPermission(ScheduleScan.this.getApplicationContext())) {
                    GlobalVarsAndFunction.setInitiateScanningPermission(ScheduleScan.this.getApplicationContext(), true);
                    ScheduleScan.this.getApplicationContext().startService(new Intent(ScheduleScan.this.getApplicationContext(), ServiceDeviceLockMonitor.class));
                }
            }
        }, 0, 5000);
    }

    @SuppressLint("InvalidWakeLockTag")
    private void wakeUpScreen() {
        this.screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        this.screenLock.acquire();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        return super.onStartCommand(intent, i, i2);
    }
}
