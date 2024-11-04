package com.photo.photography.act.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.photo.photography.util.utils.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

public class NetworkReceiver extends BroadcastReceiver {
    private static final List<NetworkStateReceiverListener> networkStateListeners = new ArrayList<NetworkStateReceiverListener>();
    private static boolean connected;

    private static void notifyState(NetworkStateReceiverListener listener) {
        if (connected == false || listener == null)
            return;

        if (connected == true)
            listener.onNetworkAvailable();
        else
            listener.onNetworkUnavailable();
    }

    public static void addListener(NetworkStateReceiverListener l) {
        networkStateListeners.add(l);
        notifyState(l);
    }

    public static void removeListener(NetworkStateReceiverListener l) {
        networkStateListeners.remove(l);
    }

    public static void clear() {
        connected = false;
        networkStateListeners.clear();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getExtras() == null) {
            connected = false;
            return;
        } else {
            connected = NetworkUtil.checkNetworkAvailable(context);
            notifyStateToAll();
        }
    }

    private void notifyStateToAll() {
        for (NetworkStateReceiverListener listener : networkStateListeners)
            notifyState(listener);
    }

    public interface NetworkStateReceiverListener {
        void onNetworkAvailable();

        void onNetworkUnavailable();
    }

}
