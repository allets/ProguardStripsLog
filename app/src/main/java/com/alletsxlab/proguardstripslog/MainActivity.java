package com.alletsxlab.proguardstripslog;

import android.os.Bundle;
import android.util.Log;

import com.alletsxlab.proguardstripslog.base.BaseActivity;
import com.alletsxlab.proguardstripslog.utils.LogUtil;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final LogUtil specialTest = new LogUtil("Test", false);
    private final LogUtil unusedLogger = new LogUtil(this);

    private String memberVar = "member var";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG, "Android Log onCreate");
        Log.d(TAG, "Android Log onCreate");
        Log.i(TAG, "Android Log onCreate");
        Log.w(TAG, "Android Log onCreate");
        Log.e(TAG, "Android Log onCreate");

        log.d("LogUtil Log onCreate");
        log.i("LogUtil Log onCreate");
        log.w("LogUtil Log onCreate");
        log.e("LogUtil Log onCreate");

        specialTest.d("LogUtil Log onCreate");
        specialTest.i("LogUtil Log onCreate");
        specialTest.w("LogUtil Log onCreate");
        specialTest.e("LogUtil Log onCreate");

        Log.d(TAG, "Android Log concat " + "string");
        log.d("LogUtil Log concat " + "string");

        String localVar = "local var";
        Log.d(TAG, "Android Log concat " + localVar);
        log.d("LogUtil Log concat " + localVar);

        Log.d(TAG, "Android Log concat " + memberVar);
        log.d("LogUtil Log concat " + memberVar);
        log.d("LogUtil Log concat %s", memberVar);

        if (savedInstanceState == null) {
            MainFragment fragment = MainFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, null)
                    .setReorderingAllowed(true)
                    .commit();
        }
    }
}