package com.qiren.geoapp.common.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.qiren.geoapp.MainApplication;

public class BroadcastUtils {


    public static void sendBroadcast(String action) {
        sendBroadcast(MainApplication.getApplication(), action);
    }

    public static void sendBroadcast(Context context, String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void registerBroadcast(Context context, String action, BroadcastReceiver receiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(action);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
    }

    public static void removeBroadcast(Context context, BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

    public static void registerBroadcast(String action, BroadcastReceiver receiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(action);
        LocalBroadcastManager.getInstance(MainApplication.getContext()).registerReceiver(receiver, filter);
    }

    public static void removeBroadcast(BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(MainApplication.getContext()).unregisterReceiver(receiver);
    }
}
