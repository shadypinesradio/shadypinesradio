/*
package com.shadypines.radio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shadypines.radio.model.RadioResponse;
import com.shadypines.radio.presenter.HomePresenter;
import com.shadypines.radio.presenter.HomePresenterImpl;
import com.shadypines.radio.services.Constants;
import com.shadypines.radio.services.NotificationService;
import com.shadypines.radio.utils.AppSettings;
import com.shadypines.radio.view.activity.BaseActivity;
import com.shadypines.radio.view.viewinteractors.HomeView;

import java.io.IOException;

public class MainActivity extends BaseActivity implements HomeView {

    HomePresenter presenter;
    ImageView iv_image;
    TextView tv_title, tv_albume;
    public static MediaPlayer mPlayer;
    public RelativeLayout rl_refresh;
    RelativeLayout rl_play;
    boolean musicPause = false;
    RemoteViews collapsedView;
    int NOTIF_ID = 1;
    NotificationCompat.Builder builder;
    Drawable imageValue;
    Bitmap bitmap;
    public static NotificationManager mNotificationManager;

*/
/**//*

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rl_refresh = findViewById(R.id.rl_refresh);


        init();


//
//        rl_refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tv_title.setText("");
//                tv_albume.setText("");
//                iv_image.setImageBitmap(null);
//                mPlayer.start();
//                rl_play.setBackgroundResource(R.drawable.ic_pause);
//                presenter.onRadioValue();
//            }
//        });
    }

    private void init() {
        getSupportActionBar().hide();
        iv_image = (ImageView) findViewById(R.id.iv_image);
        rl_play = findViewById(R.id.rl_play);
        tv_albume = findViewById(R.id.tv_albume);
        tv_title = (TextView) findViewById(R.id.tv_title);

        presenter = new HomePresenterImpl(this);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.onRadioValue();
                playMediaPlayer();
                startService();


                mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                rl_play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {

                            if (mPlayer.isPlaying()) {
//                        updateNotification("Playing");
                                mPlayer.pause();
                                rl_play.setBackgroundResource(R.drawable.ic_pause);
                            } else {
//                        updateNotification("Pause");
                                rl_play.setBackgroundResource(R.drawable.ic_play);
                                mPlayer.start();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        }, 2000);

    }


    @Override
    public void onRadioValue(RadioResponse radioResponse) {
//        Glide
//                .with(MainActivity.this)
//                .load(radioResponse.getCurrentTrack().getArtworkUrl())
//                .centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL);

        AppSettings.imageurl = radioResponse.getCurrentTrack().getArtworkUrlLarge();
//        Glide.with(MainActivity.this).load(radioResponse.getCurrentTrack().getArtworkUrlLarge()).placeholder(R.drawable.ic_dummy).dontAnimate().into(iv_image);

//        Glide.with(MainActivity.this).load(radioResponse.getCurrentTrack().getArtworkUrl())
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
//                        imageValue=resource;
//                        return true;
//                    }
//                }).into(iv_image);


        Glide.with(this)
                .asBitmap()
                .load(radioResponse.getCurrentTrack().getArtworkUrlLarge())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        iv_image.setImageBitmap(resource);
                        bitmap = resource;
                        updateNotification();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

        Log.d("sksoheldsss", "dlsjdlsjdlksjldkjsl");
        try {
            String[] value = radioResponse.getCurrentTrack().getTitle().split(",");

            AppSettings.title = value[1];
            AppSettings.album = value[0];
            Log.d("ALBUT", AppSettings.album);

            tv_title.setText(value[1]);
            tv_albume.setText(value[0]);

        } catch (Exception e) {
            e.printStackTrace();
            AppSettings.title = radioResponse.getCurrentTrack().getTitle();
            AppSettings.album = "";
            Log.d("ALBUT2", AppSettings.title);
            tv_title.setText(radioResponse.getCurrentTrack().getTitle());
            tv_title.setText(radioResponse.getCurrentTrack().getTitle());
        }
        counter();
//        updateNotification( Settings.title);
        updateNotification();


    }

    public void counter() {
        CountDownTimer countDownTimer = new CountDownTimer(300000, 1000) {
            @Override
            public void onTick(long l) {
//                Log.e("Value","Working Timer");

            }

            @Override
            public void onFinish() {
                presenter.onRadioValue();
            }
        }.start();
    }



    public void playMediaPlayer() {
        String url = "https://streamer.radio.co/s3bc65afb4/listen";

        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(url);
        } catch (IllegalArgumentException | IllegalStateException | SecurityException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mPlayer.prepareAsync();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
        mPlayer.start();
    }

    public void startService() {
        Intent serviceIntent = new Intent(MainActivity.this, NotificationService.class);
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(serviceIntent);
    }


    private void updateNotification() {

        int api = Build.VERSION.SDK_INT;

        try {

            if (bitmap != null) {
                NotificationService.bigViews.setImageViewBitmap(R.id.status_bar_album_art,
                        bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        NotificationService.views.setTextViewText(R.id.status_bar_track_name, AppSettings.title);
        NotificationService.bigViews.setTextViewText(R.id.status_bar_track_name, AppSettings.title);

        NotificationService.views.setTextViewText(R.id.status_bar_artist_name, AppSettings.album);
        NotificationService.bigViews.setTextViewText(R.id.status_bar_artist_name, AppSettings.album);


        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        try {
            if (api < Build.VERSION_CODES.HONEYCOMB) {

                mNotificationManager.notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, NotificationService.status);
                NotificationService.status.notify();
            } else {
                mNotificationManager.notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, NotificationService.status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void NotificationValue() {
        int api = Build.VERSION.SDK_INT;



        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            NotificationService.views.setImageViewResource(R.id.status_bar_play, R.drawable.notification_play);
            NotificationService.bigViews.setImageViewResource(R.id.status_bar_play, R.drawable.notification_play);
        } else {
            mPlayer.start();
            NotificationService.views.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_notification);
            NotificationService.bigViews.setImageViewResource(R.id.status_bar_play, R.drawable.ic_pause_notification);
        }


        try {
            if (api < Build.VERSION_CODES.HONEYCOMB) {

                mNotificationManager.notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, NotificationService.status);
                NotificationService.status.notify();
            } else {
                mNotificationManager.notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, NotificationService.status);
//            notificationManager.notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, builder.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


//    @Override
//    public void onStop() {
//        super.onStop();
//        mPlayer.pause();
//        rl_play.setBackgroundResource(R.drawable.ic_play);
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}*/
