package com.wedream.demo.util

import android.util.Log
import com.wedream.demo.BuildConfig
import java.lang.StringBuilder

object LogUtils {
    private const val TAG = "LogUtils"
    fun <T, R> T.log(msg: T.() -> R?) {
        val m = msg()
        Log.e(TAG, m?.toString() ?: "null")
    }

    fun Throwable.printAndDie() {
        Log.e(TAG, "FATAL Exception :${getStack()}")
        if (BuildConfig.DEBUG) {
            throw this
        }
    }

    fun Throwable.getStack(): String {
        val trace = stackTrace
        val sb = StringBuilder()
        sb.append(toString())
        sb.append(", StackTrace:\n")
        for (i in trace.indices) {
            val element = trace[i]
            sb.append(element.toString())
            sb.append("\n")
        }
        return sb.toString()
    }
}