package com.photo.photography.edit_views.BrushViewEdit.manag;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class ThreadManag {
    private static ThreadManag instance = null;
    private final Handler mUI_Handler;
    private final Handler mBackgroundHandler;
    private final Handler mAPIQueueThread;
    private final HandlerThread mThread;
    private final HandlerThread apiQueueThread;

    private ThreadManag() {
        mUI_Handler = new Handler(Looper.getMainLooper());
        mThread = new HandlerThread("backgroundThread");
        mThread.start();
        mBackgroundHandler = new Handler(mThread.getLooper());
        apiQueueThread = new HandlerThread("apiQueueThread");
        apiQueueThread.start();
        mAPIQueueThread = new Handler(apiQueueThread.getLooper());
    }

    public synchronized static ThreadManag getInstance() {
        if (instance == null) {
            instance = new ThreadManag();
        }
        return instance;
    }

    public void postToUIThread(Runnable runnable) {
        mUI_Handler.post(runnable);
    }

    public void postToUIThread(Runnable runnable, long delayMillis) {
        mUI_Handler.postDelayed(runnable, delayMillis);
    }

    public void removeCallbacks(Runnable runnable) {
        mUI_Handler.removeCallbacks(runnable);
        mBackgroundHandler.removeCallbacks(runnable);
        mAPIQueueThread.removeCallbacks(runnable);
    }

    public void postToBackgroungThread(Runnable runnable) {
        mBackgroundHandler.post(runnable);
    }

    public void postToBackgroungThread(Runnable runnable, long delayMillis) {
        mBackgroundHandler.postDelayed(runnable, delayMillis);
    }

    public void postToAPIQueueThread(Runnable runnable) {
        mAPIQueueThread.post(runnable);
    }

    public void postToAPIQueueThread(Runnable runnable, long delayMillis) {
        mAPIQueueThread.postDelayed(runnable, delayMillis);
    }

    public void destroy() {
        mThread.quit();
        apiQueueThread.quit();
        instance = null;
    }
}
