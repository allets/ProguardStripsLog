package com.alletsxlab.proguardstripslog.utils

import android.util.Log
import com.alletsxlab.proguardstripslog.BuildConfig

class LogUtil(
    private val tag: String,
    private var isDebug: Boolean = true,
) {

    constructor(clz: Class<*>, isDebug: Boolean = true) : this(clz.simpleName, isDebug) {
    }

    constructor(obj: Any, isDebug: Boolean = true) : this(obj.javaClass.simpleName, isDebug) {
    }

    fun canLog(): Boolean {
        return !BuildConfig.IS_RELEASE && isDebug
    }

    fun d(msg: String) {
        if (!canLog()) {
            return
        }

        Log.d(tag, msg)
    }

    fun i(msg: String) {
        if (!canLog()) {
            return
        }

        Log.i(tag, msg)
    }

    fun w(msg: String) {
        if (!canLog()) {
            return
        }

        Log.w(tag, msg)
    }

    fun e(msg: String) {
        if (!canLog()) {
            return
        }

        Log.e(tag, msg)
    }

    fun d(format: String, vararg args: Any) {
        if (!canLog()) {
            return
        }

        Log.d(tag, String.format(format, *args))
    }

    fun i(format: String, vararg args: Any) {
        if (!canLog()) {
            return
        }

        Log.i(tag, String.format(format, *args))
    }

    fun w(format: String, vararg args: Any) {
        if (!canLog()) {
            return
        }

        Log.w(tag, String.format(format, *args))
    }

    fun e(format: String, vararg args: Any) {
        if (!canLog()) {
            return
        }

        Log.e(tag, String.format(format, *args))
    }

}