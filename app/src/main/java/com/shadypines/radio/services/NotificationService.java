package com.shadypines.radio.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.shadypines.radio.R;
import com.shadypines.radio.utils.AppSettings;
import com.shadypines.radio.view.activity.TempActivity;

public class NotificationService extends Service {
    
    private final String LOG_TAG = "NotificationService";

    NotificationManager notificationManager;
    public static RemoteViews bigViews,views;
    public static Notification.Builder builder;
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            showNotification();
        } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
            Log.i(LOG_TAG, "Clicked Previous");
        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
          //  HomeActivity.NotificationValue();
            Log.i(LOG_TAG, "Clicked Play");
        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
            Log.i(LOG_TAG, "Clicked Next");
        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            stopForeground(true);
            stopSelf();
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return START_STICKY;
    }

    public static Notification status;

    private void showNotification() {
         views = new RemoteViews(getPackageName(),
                R.layout.status_bar);
         bigViews = new RemoteViews(getPackageName(),
                R.layout.status_bar_expanded);


        views.setViewVisibility(R.id.status_bar_album_art, View.VISIBLE);
        bigViews.setImageViewBitmap(R.id.status_bar_album_art,
                Constants.getDefaultAlbumArt(this));

        Intent notificationIntent = new Intent(this, TempActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, NotificationService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, NotificationService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, NotificationService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, NotificationService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);


        views.setTextViewText(R.id.status_bar_track_name, AppSettings.title);
        bigViews.setTextViewText(R.id.status_bar_track_name, AppSettings.title);

        views.setTextViewText(R.id.status_bar_artist_name, AppSettings.album);
        bigViews.setTextViewText(R.id.status_bar_artist_name, AppSettings.album);

        builder = new Notification.Builder(this);

        status = builder.build();
        status.contentView = views;
        status.bigContentView = bigViews;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.drawable.ic_dummy;
        status.contentIntent = pendingIntent;

        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
    }

}
