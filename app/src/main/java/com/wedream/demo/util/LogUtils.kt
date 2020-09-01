package com.wedream.demo.util

import android.util.Log

object LogUtils {
    private const val TAG = "xcm"
    fun log(msg: Any) {
        Log.e(TAG, msg.toString())
    }
}