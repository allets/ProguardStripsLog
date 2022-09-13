package com.alletsxlab.proguardstripslog;

import android.app.Application;
import android.util.Log;

import com.alletsxlab.proguardstripslog.utils.LogUtil;

public class App extends Application {

    private static final String TAG = App.class.getSimpleName();
    private final LogUtil log = new LogUtil(this);

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Android Log onCreate");

        log.d("LogUtil Log onCreate");
    }
}
