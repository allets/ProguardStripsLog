package com.alletsxlab.proguardstripslog.base;

import androidx.appcompat.app.AppCompatActivity;

import com.alletsxlab.proguardstripslog.utils.LogUtil;

public class BaseActivity extends AppCompatActivity {

    protected final LogUtil log = new LogUtil(this);

}
