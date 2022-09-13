package com.alletsxlab.proguardstripslog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.alletsxlab.proguardstripslog.utils.LogUtil;

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();
    private final LogUtil log = new LogUtil(this);

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        Log.d(TAG, "Android Log onCreateView");

        log.d("LogUtil Log onCreateView");

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "Android Log onPause");

        log.d("LogUtil Log onPause");
    }
}