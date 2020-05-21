package com.save.smartsave;

import android.app.Application;

import com.onesignal.OneSignal;


public class PushNotification extends Application {

    public static PushNotification mInstance;

    public PushNotification() {
        mInstance=this;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    public static synchronized PushNotification getmInstance(){
        return mInstance;
    }
}
