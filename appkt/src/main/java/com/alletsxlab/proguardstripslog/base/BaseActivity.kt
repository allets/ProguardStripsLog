package com.alletsxlab.proguardstripslog.base

import androidx.appcompat.app.AppCompatActivity
import com.alletsxlab.proguardstripslog.utils.LogUtil

open class BaseActivity : AppCompatActivity() {

    protected val log = LogUtil(this)

}