package com.alletsxlab.proguardstripslog;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.alletsxlab.proguardstripslog.utils.LogUtil;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final LogUtil log = new LogUtil(this);
    private final LogUtil specialTest = new LogUtil("Test", false);


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

        if (savedInstanceState == null) {
            MainFragment fragment = MainFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, null)
                    .setReorderingAllowed(true)
                    .commit();
        }
    }
}