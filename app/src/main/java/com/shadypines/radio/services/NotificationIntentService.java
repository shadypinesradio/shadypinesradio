package com.shadypines.radio.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;

import java.util.Objects;

public class NotificationIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public NotificationIntentService() {
        super("notificationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        switch (Objects.requireNonNull(intent != null ? intent.getAction() : null)) {
            case "left":
                android.os.Handler leftHandler = new android.os.Handler(Looper.getMainLooper());
                leftHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(),
                                "You clicked the left button", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case "right":
                android.os.Handler rightHandler = new android.os.Handler(Looper.getMainLooper());
                rightHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(),
                                "You clicked the right button", Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }
}