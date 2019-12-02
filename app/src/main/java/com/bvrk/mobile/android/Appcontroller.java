package com.bvrk.mobile.android;

import android.app.Application;
import android.util.Log;

public class Appcontroller extends Application {

    public static final String TAG = "amarr";//AppController.class.getSimpleName();

    private static Appcontroller mInstance;

    public Appcontroller() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Log.d(TAG, "onCreate: appcontroller");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "onTerminate: ");
    }

    public static synchronized Appcontroller getInstance() {
        return mInstance;
    }
}
