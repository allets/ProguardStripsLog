package com.alletsxlab.proguardstripslog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alletsxlab.proguardstripslog.utils.LogUtil

class MainFragment : Fragment() {

    companion object {

        fun newInstance(): MainFragment {
            return MainFragment()
        }

    }

    private val TAG = MainFragment::class.java.simpleName
    private val log = LogUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        Log.d(TAG, "Android Log onCreateView")

        log.d("LogUtil Log onCreateView")

        return root
    }

    override fun onPause() {
        super.onPause()

        Log.d(TAG, "Android Log onPause")

        log.d("LogUtil Log onPause")
    }
}