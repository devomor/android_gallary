package com.photo.photography.duplicatephotos.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class ServiceKillNotifications extends Service {
    private final IBinder mBinder = new KillBinder(this);
    private NotificationManager mNM;

    public int onStartCommand(Intent intent, int i, int i2) {
        return START_STICKY;
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public void onCreate() {
        this.mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        this.mNM.cancel(1);
    }

    public class KillBinder extends Binder {
        public final Service service;

        public KillBinder(Service service2) {
            this.service = service2;
        }
    }
}
