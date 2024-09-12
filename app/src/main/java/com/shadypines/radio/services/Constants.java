package com.shadypines.radio.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.shadypines.radio.R;


public class Constants {

    public interface ACTION {
        public static String MAIN_ACTION = "com.marothiatechs.customnotification.action.main";
        public static String INIT_ACTION = "com.marothiatechs.customnotification.action.init";
        public static String PREV_ACTION = "com.marothiatechs.customnotification.action.prev";
        public static String PLAY_ACTION = "com.marothiatechs.customnotification.action.play";
        public static String NEXT_ACTION = "com.marothiatechs.customnotification.action.next";
        public static String STARTFOREGROUND_ACTION = "com.marothiatechs.customnotification.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.marothiatechs.customnotification.action.stopforeground";
    }
    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
        public int showWeb = 0;

    }
    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ic_play, options);
        } catch (Error | Exception ignored) {
        }
        return bm;
    }
}

