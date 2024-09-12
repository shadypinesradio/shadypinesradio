package com.shadypines.radio;


import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;
import com.shadypines.utils.Constants;
import com.shadypines.radio.api.ApiClient;
import com.shadypines.radio.api.ApiInterface;

import java.util.Objects;


public class SyadApplication extends Application implements OSSubscriptionObserver {

    public static String serverurl="wss://rc.rocket.chat/websocket";
    public static String BASEURL = "https://public.radio.co/stations/s3bc65afb4/";
    private static Context ctx;
    public static String DEVICETOKEN;
    private static final String ONESIGNAL_APP_ID = "50f5c89f-4a93-42c2-a9fc-25ad73f750db";

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPrefUtils.INSTANCE.init(this);
        FacebookSdk.setAutoLogAppEventsEnabled(true);
        FacebookSdk.setAdvertiserIDCollectionEnabled(true);
// OPTIONALLY the following two lines if AutoInitEnabled is set to false in manifest:
        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();
        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);
//        Branch.getAutoInstance(this);
        ctx = getApplicationContext();
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        OneSignal.addSubscriptionObserver(this);
    }

    public static ApiInterface getRetroApiClient() {
        return Objects.requireNonNull(ApiClient.getClient()).create(ApiInterface.class);
    }

    public static ApiInterface getScheduleApiClient(){
        return Objects.requireNonNull(ApiClient.getClientForSchedule()).create(ApiInterface.class);
    }

    public static Context getContext() {
        return ctx;
    }

    @Override
    public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {
        //create user
        if(!stateChanges.getFrom().isSubscribed() && stateChanges.getTo().isSubscribed()){
            if(!SharedPrefUtils.INSTANCE.readBoolean(Constants.Preferences.IS_REGISTERED)){
                OneSignal.getDeviceState();/* SyadApplication.getScheduleApiClient().createUser(stateChanges.getTo().getPushToken(),
                            stateChanges.getTo().getUserId()
                    ).enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if(response.isSuccessful()){
                                BaseResponse baseResponse = response.body();
                                if (baseResponse != null && baseResponse.getSuccess()) {
                                    SharedPrefUtils.INSTANCE.write(Constants.Preferences.IS_REGISTERED, true);
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {

                        }
                    });*/
            }
        }

    }
}

