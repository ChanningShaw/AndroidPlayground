package com.wedream.demo.util

import android.util.Log

object LogUtils {
    private const val TAG = "xcm"
    fun <T, R> T.log(msg: T.() -> R?) {
        val m = msg()
        Log.e(TAG, m?.toString() ?: "null")
    }
}