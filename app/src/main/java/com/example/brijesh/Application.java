package com.example.brijesh;

import com.onesignal.OneSignal;

public class Application extends android.app.Application {

    private static final String ONESIGNAL_APP_ID = "607f0a5d-515a-41dd-ab8c-f470c0324ea5";

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }
}
