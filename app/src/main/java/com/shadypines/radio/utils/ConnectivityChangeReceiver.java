package com.shadypines.radio.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.util.Objects;

public class ConnectivityChangeReceiver extends BroadcastReceiver {

    private Handler mHandler;
    private Activity context = null;

    public ConnectivityChangeReceiver(Activity activity, Handler handler) {
        this.context = activity;
        this.mHandler = handler;
    }

    public ConnectivityChangeReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        debugIntent(intent);
    }

    private void debugIntent(Intent intent) {
        // Log.v(tag, "action: " + intent.getAction());
        // Log.v(tag, "component: " + intent.getComponent());
        Bundle extras = intent.getExtras();
        if (extras != null) {
            for (String key : extras.keySet()) {
                if (key.equals("networkInfo") && Objects.requireNonNull(extras.get(key)).toString().contains("DISCONNECTED")) {
                    try {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context,"Please check your internet connection", Toast.LENGTH_SHORT).show();
//                                Utils.showDisconnectionDialog(context);
                                return;
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {

        }
    }

}