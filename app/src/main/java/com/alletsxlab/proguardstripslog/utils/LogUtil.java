package com.alletsxlab.proguardstripslog.utils;

import android.util.Log;

import com.alletsxlab.proguardstripslog.BuildConfig;

public class LogUtil {

    private String tag = "";
    private boolean isDebug = true;

    public LogUtil(String tag) {
        this.tag = tag;
    }

    public LogUtil(String tag, boolean isDebug) {
        this.tag = tag;
        this.isDebug = isDebug;
    }

    public LogUtil(Class<?> clz) {
        this.tag = clz.getSimpleName();
    }

    public LogUtil(Class<?> clz, boolean isDebug) {
        this.tag = clz.getSimpleName();
        this.isDebug = isDebug;
    }

    public LogUtil(Object obj) {
        this.tag = obj.getClass().getSimpleName();
    }

    public LogUtil(Object obj, boolean isDebug) {
        this.tag = obj.getClass().getSimpleName();
        this.isDebug = isDebug;
    }

    public boolean canLog() {
        return !BuildConfig.IS_RELEASE && isDebug;
    }

    public void d(String msg) {
        if (!canLog()) {
            return;
        }

        Log.d(tag, msg);
    }

    public void i(String msg) {
        if (!canLog()) {
            return;
        }

        Log.i(tag, msg);
    }

    public void w(String msg) {
        if (!canLog()) {
            return;
        }

        Log.w(tag, msg);
    }

    public void e(String msg) {
        if (!canLog()) {
            return;
        }

        Log.e(tag, msg);
    }

}
