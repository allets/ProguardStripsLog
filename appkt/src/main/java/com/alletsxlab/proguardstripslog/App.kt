package com.alletsxlab.proguardstripslog

import android.app.Application
import android.util.Log
import com.alletsxlab.proguardstripslog.utils.LogUtil

class App : Application() {

    private val TAG = App::class.java.simpleName
    private val log = LogUtil(this)

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "Android Log onCreate")

        log.d("LogUtil Log onCreate")
    }
}