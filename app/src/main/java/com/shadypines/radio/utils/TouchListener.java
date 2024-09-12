package com.shadypines.radio.utils;

public interface TouchListener  {
    default void onSwipeLeft() {
//        Logger.d( "Swipe left");
    }
    default void onSwipeRight() {
//        Logger.d( "Swipe right");
    }

    void onBackPressed();
}
